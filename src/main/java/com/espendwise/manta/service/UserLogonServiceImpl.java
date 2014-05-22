package com.espendwise.manta.service;


import com.espendwise.manta.auth.*;
import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.binder.PropertyBinder;
import com.espendwise.manta.util.criteria.StoreListEntityCriteria;
import com.espendwise.manta.util.parser.Parse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserLogonServiceImpl extends DataAccessService implements UserLogonService {

    private static final Logger logger = Logger.getLogger(UserLogonServiceImpl.class);

    @Override
    public boolean verifyAccessToStore(String datasource, Long storeId) {

        InstanceView store = null;

        List<InstanceView> stores = getUserStoreDataSources();

        logger.info("verifyAccessToStore()=> stores: " + stores.size() +
                ", datasource: " + datasource + "" +
                ", storeId: " + storeId);


        for (InstanceView x : stores) {

            logger.info("verifyAccessToStore()=> verify," +
                    " dataAccessUnit " + x.getUnitName() + "," +
                    " storeId: " + x.getStore().getStoreId());

            if (x.getStore().getStoreId().longValue() == storeId && x.getDataSourceIdent().getDataSourceName().equals(datasource)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public AuthUserDetails retrieveAuthenticatedUserForStore(String datasource, Long storeId) {

        AuthenticationUserMap authUserMap = Auth.getAuthUser().getAuthUserContext().get(AuthUserContext.AUTHI_USER_MAP);
        if (authUserMap != null) {
            for (Map.Entry<AuthDatabaseAccessUnit, AuthUserData> entry : authUserMap.entrySet()) {
                if (entry.getKey().getAuthDataSourceIdent().getDataSourceName().equals(datasource)) {
                    for (StoreListEntity store : entry.getValue().getUserStores()) {
                        if (store.getStoreId().longValue() == storeId) {
                            return entry.getValue().getUserDetails();
                        }
                    }
                }
            }
        }

        return null;

    }

    @Override
    public InstanceView retrieveAuthenticatedUserStore(String datasource, Long storeId) {

        AuthenticationUserMap authUserMap = Auth.getAuthUser().getAuthUserContext().get(AuthUserContext.AUTHI_USER_MAP);

        if (authUserMap != null) {

            for (Map.Entry<AuthDatabaseAccessUnit, AuthUserData> entry : authUserMap.entrySet()) {

                if (entry.getKey().getAuthDataSourceIdent().getDataSourceName().equals(datasource)) {

                    for (StoreListEntity store : entry.getValue().getUserStores()) {

                        if (store.getStoreId().longValue() == storeId) {

                            return new InstanceView(
                                    store.getStoreId(),
                                    store.getStoreName(),
                                    store,
                                    selectMainStore(entry.getKey().getMainStoreIdents(), store.getStoreId()),
                                    entry.getKey().getAuthDataSourceIdent(),
                                    entry.getKey().getUnitName(),
                                    entry.getKey().isAlive()
                            );

                        }
                    }
                }
            }
        }

        return null;

    }

    private MainStoreIdentView selectMainStore(List<AuthMainStoreIdent> availableMainStoresInDs, Long storeId) {

        if (Utility.isSet(availableMainStoresInDs)) {

            for (AuthMainStoreIdent x : availableMainStoresInDs) {

                if (Utility.longNN(x.getStoreId()) == Utility.longNN(storeId)) {

                    MainStoreIdentView v = new MainStoreIdentView();

                    v.setId(x.getMainStoreId());
                    v.setShortDesc(x.getMainStoreName());

                    return v;
                }
            }
        }

        return null;

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<RefCdData> getRefCodes(@AppDS String dataSource, String applicationFunctions, int orderByName) {

        EntityManager entityManager = getEntityManager(dataSource);
        RefCdDAO refCdDAO = new RefCdDAOImpl(entityManager);

        return refCdDAO.getRefCodes(Constants.APPLICATION_FUNCTIONS, Constants.ORDER_TYPE.ORDER_BY_NAME);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<WoRefCdData> getWoRefCodes(@AppDS String dataSource, String applicationFunctions, int orderByName) {

        EntityManager entityManager = getEntityManager(dataSource);
        RefCdDAO refCdDAO = new RefCdDAOImpl(entityManager);

        return refCdDAO.getWoRefCodes(Constants.APPLICATION_FUNCTIONS, Constants.ORDER_TYPE.ORDER_BY_NAME);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<GroupAssocData> findUserGroupFunctions(@AppDS String dataSource, Long userId, String userType) {

        EntityManager entityManager = getEntityManager(dataSource);

        GroupDAO groupDao = new GroupDAOImpl(entityManager);
        Map<Long, GroupData> groups = groupDao.findAssociatedUserGroups(userId, userType);

        GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(entityManager);
        return groupAssocDao.findFunctionsByGroupIds(groups.keySet());

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public byte[] getBestLogo(@AppDS String datasource, Long storeId, String path, Locale locale){
    	logger.info("getBestLogo() ===================> storeId="+storeId +", path="+ path+ ", locale="+ locale);
    	EntityManager entityManager = getEntityManager(datasource);
        ContentDAO contentDao = new ContentDAOImpl(entityManager);
        return contentDao.findBestLogo( storeId,  path, locale);
        //return getBestLogoFromStorage(path);
    }



    @Override
    public Locale getUserLocale(String country, String lang, Locale defLocale, Locale preferedLocale) {

        logger.info("getUserLocale()=> BEGIN" +
                ", country: " + country +
                ", lang: " + lang +
                ", defLocale: " + defLocale +
                ", preferedLocale: " + preferedLocale
        );

        Locale userLocale;

        Locale locale = preferedLocale == null
                ? defLocale == null ? Locale.US : defLocale
                : preferedLocale;

        if (preferedLocale == null && (country != null || lang != null)) {

            if (!Utility.isSet(country)) {
                country = locale.getCountry();
            }

            if (!Utility.isSet(lang)) {
                lang = locale.getLanguage();
            }


            userLocale = new Locale(lang, country);

        } else {

            userLocale = locale;

        }

        logger.info("getUserLocale()=> END, userLocale: " + userLocale);

        return userLocale;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public StoreUiOptionView getStoreUiOptions(@AppDS String dataSource,
                                               Long userId,
                                               Long store,
                                               HomeViewType homeViewType,
                                               Locale userLocale) {

        EntityManager entityManager = getEntityManager(dataSource);

        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        StoreUiOptionView uiOptions = new StoreUiOptionView();
        if (store != null) {
            List<PropertyData> uiProperties = propertyDao.findStoreUiProperties(store, userLocale);
            if (uiProperties != null) {
                uiOptions = PropertyBinder.bindUiOptions(uiOptions, store, uiProperties, homeViewType);
            }
        }

        if (Utility.isSet(uiOptions.getLogo())) {

            ContentDAO contentDao = new ContentDAOImpl(entityManager);
            byte[] binaryData = contentDao.findBestLogo(store,
                    uiOptions.getLogo(),
                    userLocale
            );
            uiOptions.setLogoBinaryData(binaryData);
            //-----------from E3 Storage---------------//
//        	logger.info("getStoreUiOptions() ===> uiOptions.getLogo()=" + uiOptions.getLogo());
//            byte[] binaryData = getBestLogoFromStorage(uiOptions.getLogo());
//            uiOptions.setLogoBinaryData(binaryData);
        }


        return uiOptions;
    }


    @Override
    public List<InstanceView> getUserStoreDataSources() {
        return
                getUserStoreDataSources(Auth.getAuthUser()
                        .getAuthUserContext()
                        .get(AuthUserContext.AUTHI_USER_MAP)
                );
    }

    @Override
    public List<InstanceView> getUserStoreDataSources(AuthenticationUserMap authUserMap) {

        List<InstanceView> stores = new ArrayList<InstanceView>();
        Set<String> duplCtrl = new HashSet<String>();

        if (authUserMap != null) {

            for (Map.Entry<AuthDatabaseAccessUnit, AuthUserData> e : authUserMap.entrySet()) {

                for (StoreListEntity store : e.getValue().getUserStores()) {

                    String duplCtrlKey = e.getKey().getUnitName() + "_" + store.getStoreId();

                    if (!duplCtrl.contains(duplCtrlKey)) {

                        InstanceView view = new InstanceView(
                                store.getStoreId(),
                                store.getStoreName(),
                                store,
                                selectMainStore(e.getKey().getMainStoreIdents(), store.getStoreId()),
                                e.getKey().getAuthDataSourceIdent(),
                                e.getKey().getUnitName(),
                                e.getKey().isAlive()
                        );

                        stores.add(view);
                        duplCtrl.add(duplCtrlKey);
                    }
                }
            }


        }

        return stores;
    }

    @Override
    public InstanceView findInstance(AuthenticationUserMap authUserMap, String ds, Long storeId) {

        if (authUserMap != null) {
            for (Map.Entry<AuthDatabaseAccessUnit, AuthUserData> e : authUserMap.entrySet()) {
                if (e.getKey().getUnitName().equals(ds)) {
                    for (StoreListEntity store : e.getValue().getUserStores()) {
                        if (store.getStoreId() == Utility.longNN(storeId)) {
                            return new InstanceView(
                                    store.getStoreId(),
                                    store.getStoreName(),
                                    store,
                                    selectMainStore(e.getKey().getMainStoreIdents(), store.getStoreId()),
                                    e.getKey().getAuthDataSourceIdent(),
                                    e.getKey().getUnitName(),
                                    e.getKey().isAlive()
                            );
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public InstanceView findInstanceByGlobalId(Long globalEntityId) {

        AuthenticationUserMap authUserMap = Auth.getAuthUser().getAuthUserContext().get(AuthUserContext.AUTHI_USER_MAP);
        if (authUserMap != null) {
            for (Map.Entry<AuthDatabaseAccessUnit, AuthUserData> e : authUserMap.entrySet()) {
                for (StoreListEntity store : e.getValue().getUserStores()) {
                    if (store.getGlobalEntityId() == Utility.longNN(globalEntityId)) {
                        return new InstanceView(
                                store.getStoreId(),
                                store.getStoreName(),
                                store,
                                selectMainStore(e.getKey().getMainStoreIdents(), store.getStoreId()),
                                e.getKey().getAuthDataSourceIdent(),
                                e.getKey().getUnitName(),
                                e.getKey().isAlive()
                        );
                    }
                }
            }
        }

        return null;
    }

    @Override
    public List<InstanceView> findUserStoreDataSources(Long userId, StoreListEntityCriteria criteria) {

        logger.info("findUserStoreDataSources()=> BEGIN");

        List<InstanceView> stores = getUserStoreDataSources();

        String storeName = criteria.getStoreName();
        String filterType = criteria.getStoreNameFilterType();
        Long globalEntityId = criteria.getGlobalEntityId();
        filterType = Utility.isSet(filterType) ? filterType : Constants.FILTER_TYPE.DEFAULT;
        Boolean showInactive = criteria.getShowInactive();

        Iterator<InstanceView> it = stores.iterator();

        while (it.hasNext()) {

            boolean match = true;

            InstanceView e = it.next();

            StoreListEntity store = e.getStore();

            if (Utility.isSet(storeName)) {

                if (Constants.FILTER_TYPE.ID.equals(filterType)) {

                    if (store.getStoreId() != Parse.parseLong(storeName).longValue()) {
                        match = false;
                    }
                } else if (Constants.FILTER_TYPE.START_WITH.equals(filterType)) {
                    if (!store.getStoreName().toUpperCase().startsWith(storeName.toUpperCase())) {
                        match = false;
                    }
                } else if (Constants.FILTER_TYPE.CONTAINS.equals(filterType)) {
                    if (!store.getStoreName().toUpperCase().contains(storeName.toUpperCase())) {
                        match = false;
                    }
                }
            }

            if (globalEntityId != null) {
                if (!globalEntityId.equals(store.getGlobalEntityId())) {
                    match = false;
                }
            }
            
            if (Utility.isSet(showInactive) && showInactive.booleanValue()) {
            	//MANTA-419
            	//nothing to do - the user has specified they want to include inactive stores so no need
            	//to check the store status
            }
            else {
                if (RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE.equals(store.getStatus())) {
                    match = false;
                }
            }


            if (!match) {
                it.remove();
            }
        }

        Collections.sort(stores, new Comparator<InstanceView>() {
            @Override
            public int compare(InstanceView o1, InstanceView o2) {

                StoreListEntity store1 = o1.getStore();
                StoreListEntity store2 = o2.getStore();

                if (store1 != null && store2 != null) {
                    return store1.getStoreName().toUpperCase().compareTo(store2.getStoreName().toUpperCase());
                } else if (store1 != null) {
                    return 1;
                } else if (store2 != null) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        if (criteria.getLimit() != null) {

            it = stores.iterator();

            if (stores.size() > criteria.getLimit())
                while (stores.size() > criteria.getLimit()) {
                    it.remove();
                }
        }

        logger.info("findUserStoreDataSources()=> END, stores: " + stores.size());

        return stores;
    }

    @Override
    public InstanceView getDefaultUserStore(String username, AuthenticationUserMap authUserMap, String domainName) {


        InstanceView defaultStoreDsView = null;

        if (isAliveUnit(DatabaseAccess.getMainUnit())) {

            defaultStoreDsView = getDefaultUserStoreFromMainUnit(DatabaseAccess.getMainUnit(),
                    username,
                    authUserMap);

        }

        logger.debug("getDefaultUserStore()=> main defaultStoreDsView : " + defaultStoreDsView);

        if (defaultStoreDsView == null) {

            for (Map.Entry<AuthDatabaseAccessUnit, AuthUserData> entry : authUserMap.entrySet()) {

                defaultStoreDsView = getDefaultStoreFromWorkUnit(entry.getKey().getAuthDataSourceIdent().getDataSourceName(),
                        entry.getKey(),
                        domainName,
                        entry.getValue()
                );

                if (defaultStoreDsView != null) {
                    break;
                }

            }


        }

        logger.debug("getDefaultUserStore()=> work defaultStoreDsView : " + defaultStoreDsView);

        if (defaultStoreDsView == null) {

            for (Map.Entry<AuthDatabaseAccessUnit, AuthUserData> entry : authUserMap.entrySet()) {

                if (Utility.isSet(entry.getValue().getUserStores())) {

                    StoreListEntity store = entry.getValue().getUserStores().get(0);

                    return new InstanceView(
                            store.getStoreId(),
                            store.getStoreName(),
                            store,
                            selectMainStore(entry.getKey().getMainStoreIdents(), store.getStoreId()),
                            entry.getKey().getAuthDataSourceIdent(),
                            entry.getKey().getUnitName(),
                            entry.getKey().isAlive()
                    );
                }
            }

        }

        return defaultStoreDsView;

    }


    @Override
    public LogonOptionView getLogonOptions(String domainName, Locale locale) {

        logger.info("getLogonOptions()=> BEGIN, domainName: " + domainName + ", locale: " + locale);

        LogonOptionView logonUiOptions;

        if (isAliveUnit(DatabaseAccess.getMainUnit())) {
            logonUiOptions = getLogonMainUnitOprions(domainName, locale);
            if (logonUiOptions != null) {
                logger.info("getLogonOptions()=> END, logon options created for the main datasource");
                return logonUiOptions;
            }
        }

        logger.info("getLogonOptions()=> MainDb is unavailable, find in all datasource ... ");

        for (String datasource : DatabaseAccess.availableUnits()) {

            logger.info("getLogonOptions()=> check datasource: " + datasource);

            if (isAliveUnit(datasource)) {

                logger.info("getLogonOptions()=> datasource is valid, find  store for domain,  domain : " + domainName);

                logonUiOptions = getLogonWorkUnitOprions(datasource, domainName, locale);
                if (logonUiOptions != null) {
                    logger.info("getLogonOptions()=> END,  logon options has been created for " + datasource);
                    return logonUiOptions;
                } else {
                    logger.info("getLogonOptions()=> logon options not found, chech next datasource");
                }

            } else {

                logger.info("getLogonOptions()=> " + datasource + " is unavailable, next");

            }

        }

        logger.info("getLogonOptions()=> END, logon options not found!");

        return null;

    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, noRollbackFor = Throwable.class)
    public boolean isAliveUnit(@AppDS String unit) {
        return super.isAliveUnit(unit);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    private UiOptionView getUiOptions(@AppDS String unit, Long store, Locale userLocale) {

        EntityManager entityManager = getEntityManager(unit);
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        return propertyDao.getUiOptions(store, userLocale);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public LogonOptionView getLogonMainUnitOprions(String domainName, Locale locale) {

        EntityManager entityManager = getEntityManager(DatabaseAccess.getMainUnit());

        AllStoreDAO allStoreDao = new AllStoreDAOImpl(entityManager);

        List<AllStoreData> allStoreDatas = allStoreDao.findSroresByDomain(domainName);

        if (!allStoreDatas.isEmpty()) {
            AllStoreData store = selectAliveStore(allStoreDatas);
            if (store != null) {
                PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);
                return propertyDao.getLogonOptions(DatabaseAccess.getMainUnit(),
                        domainName,
                        store.getStoreId(),
                        locale
                );
            }
        }

        return null;
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public LogonOptionView getLogonWorkUnitOprions(@AppDS String unit, String domainName, Locale locale) {

        logger.info("getLogonWorkUnitOprions()=> BEGIN , unit: " + unit);

        LogonOptionView logonOptions = null;

        EntityManager entityManager = getEntityManager(unit);

        DomainDAO domainDao = new DomainDAOImpl(entityManager);

        Long storeId = domainDao.findDomainStore(domainName);
        if (storeId != null) {
            logger.info("getLogonWorkUnitOprions()=> END, store is found, storeId: : " + storeId);
            PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);
            logonOptions = propertyDao.getLogonOptions(unit, domainName, storeId, locale);
        }

        logger.info("getLogonWorkUnitOprions()=> END.");

        return logonOptions;

    }


    private AllStoreData selectAliveStore(List<AllStoreData> allStoreDatas) {
        for (AllStoreData allStoreData : allStoreDatas) {
            if (isAliveUnit(allStoreData.getDatasource())) {
                return allStoreData;
            }
        }
        return null;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public InstanceView getDefaultUserStoreFromMainUnit(@AppDS String mainUnit, String userName, AuthenticationUserMap authUserMap) {

        EntityManager entityManager = getEntityManager(mainUnit);
        AllStoreDAO allStoreDao = new AllStoreDAOImpl(entityManager);

        AllStoreData allStoreData = allStoreDao.findUserDefaultStore(userName);

        if (allStoreData != null) {
            for (Map.Entry<AuthDatabaseAccessUnit, AuthUserData> entry : authUserMap.entrySet()) {
                if (allStoreData.getDatasource().equals(entry.getKey().getAuthDataSourceIdent().getDataSourceName())) {
                    for (StoreListEntity store : entry.getValue().getUserStores()) {
                        if (store.getStoreId().intValue() == allStoreData.getStoreId()) {
                            return new InstanceView(
                                    store.getStoreId(),
                                    store.getStoreName(),
                                    store,
                                    selectMainStore(entry.getKey().getMainStoreIdents(), store.getStoreId()),
                                    entry.getKey().getAuthDataSourceIdent(),
                                    entry.getKey().getUnitName(),
                                    entry.getKey().isAlive()
                            );
                        }
                    }
                }
            }
        }

        return null;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public InstanceView getDefaultStoreFromWorkUnit(@AppDS String dataSourceName, AuthDatabaseAccessUnit workUnit, String domainName, AuthUserData authUserData) {

        EntityManager em = getEntityManager(dataSourceName);

        DomainDAO domainDao = new DomainDAOImpl(em);
        PropertyDAO propertyDao = new PropertyDAOImpl(em);

        String defStore =
                PropertyUtil.toFirstValueNN(
                        propertyDao.findUserProperties(
                                authUserData.getUserDetails().getUserId(),
                                Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_STORE)
                        )
                );

        Long storeId = Utility.isSet(defStore)
                ? Parse.parseLong(defStore, true)
                : domainDao.findDomainStore(domainName);

        if (storeId != null) {

            for (StoreListEntity store : authUserData.getUserStores()) {

                if (store.getStoreId().intValue() == storeId) {

                    return new InstanceView(
                            store.getStoreId(),
                            store.getStoreName(),
                            store,
                            selectMainStore(workUnit.getMainStoreIdents(), store.getStoreId()),
                            workUnit.getAuthDataSourceIdent(),
                            workUnit.getUnitName(),
                            workUnit.isAlive()
                    );
                }
            }
        }

        return null;
    }
    private byte[] getBestLogoFromStorage(String imageName){
        logger.info("getBestLogoFromStorage()=====> imageName =" + imageName);
        byte[] binaryData = new byte[0];
		try {
			binaryData = (byte[]) IOUtility
					.getContentFromStorage(
							System.getProperty(Constants.APPLICATION_SETTINGS.STORAGE__LOGO1_CONTENT_PREFIX),
							IOUtility.extractContentName("content:",imageName),
							IOUtility.extractRefFileName("content:",imageName));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        //----------------------------------------------//
		return binaryData;

    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String provideTokenForExternalAccess(@AppDS String dataSourceName, Long storeId, Long userId, Long asUserId) {

        logger.info("provideTokenForExternalAccess()=> BEGIN" +
                ",\n dataSourceName: "+dataSourceName+
                ",\n        storeId: "+storeId+
                ",\n         userId: "+userId+
                ",\n       asUserId: "+asUserId
        );

        PropertyDAO propertyDAO = new PropertyDAOImpl(getEntityManager(dataSourceName));
        List<PropertyData> accessTokens = propertyDAO.findUserAccessTokens( asUserId, userId);

       PropertyData tokenProperty;

        if (Utility.isSet(accessTokens)) {

            tokenProperty = accessTokens.get(0);

            tokenProperty.setPropertyStatusCd(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);
            tokenProperty.setValue(String.valueOf(new SecureRandom().nextLong()));
            tokenProperty.setBusEntityId(storeId);

        } else {

            tokenProperty = PropertyUtil.createProperty(
                    storeId,
                    asUserId,
                    userId,
                    null,
                    RefCodeNames.PROPERTY_TYPE_CD.ACCESS_TOKEN,
                    String.valueOf(new SecureRandom().nextLong()),
                    RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                    null
            );

        }

        List<PropertyData> x = propertyDAO.updateUserProperties(asUserId, Utility.toList(tokenProperty));

        logger.info("provideTokenForExternalAccess()=> END.");

        return x.get(0).getValue();


    }
}
