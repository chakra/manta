package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreSiteCriteria;
import com.espendwise.manta.util.criteria.UserAssocCriteria;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteDAOImpl extends DAOImpl implements SiteDAO{

    private static final Logger logger = Logger.getLogger(SiteDAOImpl.class);

    public SiteDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<SiteListView> findSitesByCriteria(SiteListViewCriteria criteria) {

        logger.info("findSitesByCriteria()=> BEGIN, criteria : " +criteria);
        boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(criteria.getWorkflowFilterType());
        logger.info("findSitesByCriteria()=> isOrca : " +isOrca);
        
          Query q = em.createQuery("select distinct new com.espendwise.manta.model.view.SiteListView("
                + "site.busEntityId, "
                + "site.shortDesc, "
                + "account.busEntityId,"
                + "account.shortDesc,"
                + "site.busEntityStatusCd,"
                + "address.address1,"
                + "address.address2,"
                + "address.address2,"
                + "address.address4,"
                + "address.countryCd,"
                + "address.countyCd,"
                + "address.city,"
                + "address.stateProvinceCd,"
                + "address.postalCode ) "
                + "from BusEntityFullEntity site "
                + "left outer join site.addresses address with address.addressTypeCd in (:shippingAddressCd) "
                + "left outer join site.properties referenceNumber with referenceNumber.shortDesc=(:refNumberCode) and referenceNumber.value is not null "
                + "inner join site.busEntityAssocsForBusEntity1Id siteAccount "
                + "inner join siteAccount.busEntity2Id account "
                + "inner join account.busEntityAssocsForBusEntity1Id accountStore "
		        + ((Utility.isSet(criteria.getWorkflowId()) && !isOrca) ? "inner join site.siteWorkflows siteWorkflow ":"")
		        + ((Utility.isSet(criteria.getWorkflowFilterType()) && !isOrca) ? "inner join account.workflows accountWorkflow ":"")
		        + ((Utility.isSet(criteria.getWorkflowId()) &&  isOrca) ? "inner join site.woSiteWorkflows siteWorkflow ":"")
		        + ((Utility.isSet(criteria.getWorkflowFilterType()) && isOrca) ? "inner join account.woWorkflows accountWorkflow ":"")
                + "where site.busEntityTypeCd = (:typeCdOfSite) "
                + "and accountStore.busEntity2Id.busEntityId = (:storeId) "
                + "and accountStore.busEntityAssocCd = (:accountStoreAssocCd)"
                + "and siteAccount.busEntityAssocCd = (:siteAccountAssocCd) "
                + "and account.busEntityTypeCd = (:typeCdOfAccount) "
                + (Utility.isSet(criteria.getSiteId())     ? " and site.busEntityId = (:siteId)":"")
                + (Utility.isSet(criteria.getSiteName())   ? " and Upper(site.shortDesc)  like :siteName":"")
                + (Utility.isSet(criteria.getAccountIds()) ? " and account.busEntityId  in (:accountIds)":"")
                + (Utility.isTrue(criteria.isActiveOnly()) ? " and site.busEntityStatusCd <> (:inactiveStatusCd)":"")
                + (Utility.isSet(criteria.getRefNumber())  ? " and Upper(referenceNumber.value) like :referenceNumber":"")
                + (Utility.isSet(criteria.getCity())       ? " and Upper(address.city) like :city":"")
                + (Utility.isSet(criteria.getState())      ? " and Upper(address.stateProvinceCd) like :state":"")
                +( Utility.isSet(criteria.getPostalCode()) ? " and Upper(address.postalCode) like :postalCode":"")
		        + (Utility.isSet(criteria.getWorkflowId()) ? " and siteWorkflow.workflowId.workflowId = (:workflowId)":"")
//        		+ (Utility.isSet(criteria.getWorkflowFilterType()) ? " and accountWorkflow.workflowId = (:workflowId)":"")
        ) ;



        q.setParameter("siteAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
        q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        q.setParameter("typeCdOfAccount", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
        q.setParameter("typeCdOfSite", RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
        q.setParameter("refNumberCode", RefCodeNames.PROPERTY_TYPE_CD.SITE_REFERENCE_NUMBER);
        q.setParameter("shippingAddressCd", Utility.toList(RefCodeNames.ADDRESS_TYPE_CD.CUSTOMER_SHIPPING, RefCodeNames.ADDRESS_TYPE_CD.SHIPPING));
        q.setParameter("storeId", criteria.getStoreId());


        if (Utility.isSet(criteria.getSiteId())) {
            q.setParameter("siteId", criteria.getSiteId());
        }

        if (Utility.isSet(criteria.getSiteName())) {
            q.setParameter("siteName",
                    QueryHelp.toFilterValue(
                            criteria.getSiteName().toUpperCase(),
                            criteria.getSiteNameFilterType()
                    )
            );
        }

        if (Utility.isSet(criteria.getRefNumber())) {
            q.setParameter("referenceNumber",
                    QueryHelp.toFilterValue(
                            criteria.getRefNumber().toUpperCase(),
                            criteria.getRefNumberFilterType()
                    )
            );
        }

        if(Utility.isSet(criteria.getAccountIds())){
            q.setParameter("accountIds", criteria.getAccountIds());
        }

        if (Utility.isSet(criteria.getCity())) {
            q.setParameter("city",
                    QueryHelp.toFilterValue(
                            criteria.getCity().toUpperCase(),
                            criteria.getCityFilterType()
                    )
            );
        }

        if (Utility.isSet(criteria.getPostalCode())) {
            q.setParameter("postalCode",
                    QueryHelp.toFilterValue(
                            criteria.getPostalCode().toUpperCase(),
                            criteria.getPostalCodeFilterType()
                    )
            );
        }

        if (Utility.isSet(criteria.getState())) {
            q.setParameter("state",
                    QueryHelp.toFilterValue(
                            criteria.getState().toUpperCase(),
                            criteria.getStateFilterType()
                    )
            );
        }

        if (Utility.isSet(criteria.getWorkflowId())) {
            q.setParameter("workflowId", criteria.getWorkflowId());
        }

        if (Utility.isTrue(criteria.isActiveOnly())) {
            q.setParameter("inactiveStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE);
        }

        if (Utility.isSet(criteria.getLimit())) {
            q.setMaxResults(criteria.getLimit()) ;
        }

        List<SiteListView>  r = q.getResultList();

        logger.info("findSitesByCriteria)=> END, fetched : " + r.size() + " rows");

        return r;
    }

    @Override
    public SiteHeaderView findSiteHeader(Long storeId, Long siteId) {

        SiteHeaderView header = null;

        StoreSiteCriteria criteria = new StoreSiteCriteria();
        criteria.setStoreId(storeId);
        criteria.setSiteIds(Utility.toList(siteId));

        List<SiteAccountView> sites = findSites(criteria);

        if(!sites.isEmpty())  {
            SiteAccountView site = sites.get(0);
            return  new SiteHeaderView(site.getBusEntityData().getBusEntityId(),
                    site.getBusEntityData().getShortDesc(),
                    site.getAccountId(),
                    site.getAccountName()
            );
        } else {
            return null;
        }

    }


    @Override
    public SiteIdentView findSiteToEdit(Long storeId,  Long siteId) {

        StoreSiteCriteria criteria = new StoreSiteCriteria();
        criteria.setStoreId(storeId);
        criteria.setSiteIds(Utility.toList(siteId));

        List<SiteAccountView> sites = findSites(criteria);

        if(!sites.isEmpty())  {
            return  pickupSiteIdentView(criteria.getStoreId(), sites.get(0));
        } else {
            return null;
        }

    }


    @Override
    public List<SiteAccountView> findSites(StoreSiteCriteria criteria) {

        Query q = em.createQuery("select new com.espendwise.manta.model.view.SiteAccountView(site,account.busEntityId, account.shortDesc)  "
                + "from BusEntityData site, BusEntityData account, BusEntityAssocData siteAccount, BusEntityAssocData accountStore "
                + (Utility.isSet(criteria.getRefNums()) ? " , PropertyData refNumber " :"")
                + "where site.busEntityId = siteAccount.busEntity1Id "
                + "and siteAccount.busEntity2Id = account.busEntityId "
                + "and account.busEntityId = accountStore.busEntity1Id "
                + "and site.busEntityTypeCd = (:typeCdOfSite) "
                + "and account.busEntityTypeCd = (:typeCdOfAccount) "
                + "and accountStore.busEntity2Id = (:storeId) "
                + "and accountStore.busEntityAssocCd = (:accountStoreAssocCd)"
                + "and siteAccount.busEntityAssocCd = (:siteAccountAssocCd) "
                + (Utility.isSet(criteria.getSiteIds()) ? (" and site.busEntityId in (:siteIds)") : "")
                + (Utility.isSet(criteria.getSiteNames()) ? (" and site.shortDesc in (:siteNames)") : "")
                + (Utility.isSet(criteria.getAccountIds()) ? (" and account.busEntityId in (:accountIds)") : "")
                + (Utility.isTrue(criteria.isActiveOnly()) ? " and site.busEntityStatusCd <> (:inactiveStatusCd)":"")
                + (Utility.isSet(criteria.getRefNums()) ? " and refNumber.busEntityId = site.busEntityId " :"")
                + (Utility.isSet(criteria.getRefNums()) ? "and refNumber.shortDesc = (:refNumberProperty)  and refNumber.value in (:refNums) " :"")
        );

        q.setParameter("siteAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
        q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        q.setParameter("typeCdOfSite", RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
        q.setParameter("typeCdOfAccount", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
        q.setParameter("storeId", criteria.getStoreId());

        if (Utility.isSet(criteria.getSiteIds())) {
            q.setParameter("siteIds", criteria.getSiteIds());
        }

        if (Utility.isSet(criteria.getAccountIds())) {
            q.setParameter("accountIds", criteria.getAccountIds());
        }
        
        if (Utility.isTrue(criteria.isActiveOnly())) {
            q.setParameter("inactiveStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE);
        }

        if (Utility.isSet(criteria.getSiteNames())) {
            q.setParameter("siteNames", criteria.getSiteNames());
        }

        if (Utility.isSet(criteria.getRefNums())) {
            q.setParameter("refNumberProperty", RefCodeNames.PROPERTY_TYPE_CD.SITE_REFERENCE_NUMBER);
            q.setParameter("refNums", criteria.getRefNums());
        }

        return q.getResultList();

    }


    private SiteIdentView pickupSiteIdentView(Long storeId, SiteAccountView siteOfAccount) {


        SiteIdentView identView = new SiteIdentView();

        SiteContactView siteContactView = findContact(siteOfAccount.getBusEntityData().getBusEntityId());
        List<PropertyData> properties = findSiteIdentProperty(siteOfAccount.getBusEntityData().getBusEntityId());
        CorporateScheduleView corporateScheduleCorporate = findCorporateSchedules(storeId,siteOfAccount.getBusEntityData().getBusEntityId() );

        identView.setBusEntityData(siteOfAccount.getBusEntityData());
        identView.setAccountId(siteOfAccount.getAccountId());
        identView.setAccountName(siteOfAccount.getAccountName());
        identView.setContact(new ContactView(siteContactView.getAddress(), siteContactView.getPhone(), null, null, null));
        identView.setProperties(properties);
        identView.setCorporateScheduleCorporate(corporateScheduleCorporate);


        return identView;
    }

    private CorporateScheduleView findCorporateSchedules(Long storeId, Long siteId) {
        ScheduleDAO schDao = new ScheduleDAOImpl(em);
        return schDao.findCorporateSiteSchedule(storeId, siteId);
    }

    private List<PropertyData> findSiteIdentProperty(Long siteId) {
        PropertyDAO propertyDao = new PropertyDAOImpl(em);
        List<PropertyData> properties = propertyDao.findEntityProperties(siteId, Utility.typeCodes(SitePropertyTypeCode.values()));
        List<PropertyData> extraProperties = propertyDao.findEntityNamedProperties(siteId, Utility.nameExtraCodes(SitePropertyExtraCode.values()));
        return PropertyUtil.joins(properties, extraProperties);
    }

    private SiteContactView findContact(Long siteId) {

        logger.info("findContact=> BEGIN, siteId: " + siteId);

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.SiteContactView(" +
                "address," +
                "phone) " +
                "from  EntityContactEntity contact" +
                " left join contact.address address  with address.addressTypeCd in  (:addressTypeProperty)" +
                " left join contact.phone phone with phone.phoneTypeCd  = (:phoneTypeProperty) and phone.primaryInd = (:primaryInd)" +
               " where contact.busEntityId = (:siteId) "
        );

        q.setParameter("siteId", siteId);
        q.setParameter("addressTypeProperty",  Utility.toList(RefCodeNames.ADDRESS_TYPE_CD.CUSTOMER_SHIPPING, RefCodeNames.ADDRESS_TYPE_CD.SHIPPING));
        q.setParameter("phoneTypeProperty", RefCodeNames.PHONE_TYPE_CD.PHONE);
        q.setParameter("primaryInd", true);

        List contacts = q.getResultList();

        return !contacts.isEmpty() ? (SiteContactView) contacts.get(0) : null;

    }

    @Override
    public SiteIdentView saveSiteIdent(Long storeId, SiteIdentView siteIdentView) throws DatabaseUpdateException {

        BusEntityData be = siteIdentView.getBusEntityData();
        ContactView contact = siteIdentView.getContact();
        List<PropertyData> siteIdentProperties = siteIdentView.getProperties();

        if (be.getBusEntityId() == null) {

            be = saveSiteBusEntity(be);

            if (Utility.longNN(be.getBusEntityId()) > 0) {

                if (createSiteOfAccountAssoc(siteIdentView.getAccountId(), be.getBusEntityId())) {

                    contact = createOrUpdateSiteContact(be.getBusEntityId(),
                            contact
                    );

                    siteIdentProperties = createOrUpdateSiteIdentProperties(be.getBusEntityId(),
                            siteIdentProperties
                    );

                } else {

                    throw new DatabaseUpdateException();

                }

            } else {

                throw new DatabaseUpdateException();

            }

        } else {

            be = saveSiteBusEntity(be);

            contact = createOrUpdateSiteContact(be.getBusEntityId(), contact);
            siteIdentProperties = createOrUpdateSiteIdentProperties(be.getBusEntityId(), siteIdentProperties);

        }


        siteIdentView.setBusEntityData(be);
        siteIdentView.setContact(contact);
        siteIdentView.setProperties(siteIdentProperties);

        return siteIdentView;
    }

    private List<PropertyData> createOrUpdateSiteIdentProperties(Long siteId, List<PropertyData> siteIdentProperties) {
        PropertyDAO propertyDAO = new PropertyDAOImpl(em);
        return propertyDAO.updateEntityProperties(siteId, siteIdentProperties);
    }

    private void removeEntity(BusEntityData busEntityData) {
        if (busEntityData != null) {
            em.remove(busEntityData);
        }
    }

    private boolean createSiteOfAccountAssoc(Long accountId, Long siteId) {

        BusEntityAssocDAO busEntityAssocDao = new BusEntityAssocDAOImpl(em);

        BusEntityAssocData assoc = busEntityAssocDao.createAssoc(siteId,
                accountId,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT
        );

        return assoc != null && Utility.longNN(assoc.getBusEntityAssocId()) > 0;
    }


    private BusEntityData saveSiteBusEntity(BusEntityData be) {

        if (be.getBusEntityId() == null) {

            be = super.create(be);

            if (Utility.longNN(be.getBusEntityId()) > 0) {
                if (!Utility.isSet(be.getErpNum())) {
                    be = updateErpNumber(be, Constants.ERP_NUM_PREFIX + be.getBusEntityId());
                }
            }

        } else {

            if (!Utility.isSet(be.getErpNum())) {
                be.setErpNum(Constants.ERP_NUM_PREFIX + be.getBusEntityId());
            }

            be = super.update(be);

        }

        return be;
    }

    private BusEntityData updateErpNumber(BusEntityData be, String erp) {
        be.setErpNum(erp);
        return super.update(be);
    }

    private ContactView createOrUpdateSiteContact(Long busEntityId, ContactView contact) {

        AddressData address = contact.getAddress();
        PhoneData phone = contact.getPhone();

        AddressDAO addressDao = new AddressDAOImpl(em);
        PhoneDAO phoneDao = new PhoneDAOImpl(em);

        address = addressDao.updateEntityAddress(busEntityId, address);
        phone = phoneDao.updateEntityPhone(busEntityId, phone);

        contact.setAddress(address);
        contact.setPhone(phone);

        return contact;

    }

    public void removeSiteIdent(SiteIdentView site) {

        logger.info("removeSiteIdent()=> BEGIN, site: " + site.getBusEntityData().getBusEntityId());

        Long siteId = site.getBusEntityData().getBusEntityId();
        Long accountId =  site.getAccountId();

        logger.info("removeSiteIdent()=> removing properties...");
        PropertyDAO propertyDao = new PropertyDAOImpl(em);
        propertyDao.removeProperties(site.getProperties());

        logger.info("removeSiteIdent()=> removing contacts...");
        AddressDAO addressDao = new AddressDAOImpl(em);
        addressDao.removeContact(site.getContact());

        logger.info("removeSiteIdent()=> removing catalog assoc...");
        CatalogDAO catalogDao = new CatalogDAOImpl(em);
        catalogDao.removeCatalogAssoc(siteId, RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE);

        logger.info("removeSiteIdent()=> removing entity associations...");
        BusEntityAssocDAO assocDao = new BusEntityAssocDAOImpl(em);
        assocDao.removeAssoc(siteId, accountId, RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);

        logger.info("removeSiteIdent()=> removing entity...");
        removeEntity(site.getBusEntityData());

        site = null;

        logger.info("removeSiteIdent()=> OK!");
        logger.info("removeSiteIdent()=> END, site has been removed, site object: " + site);


    }


    public void configureSiteWithUsers(Long siteId, List<UserData> users) {

        UserAssocCriteria criteria = new UserAssocCriteria();

        criteria.setEntityIds(Utility.toList(siteId));

        UserAssocDAO userAssocDao = new UserAssocDAOImpl(em);

        List<UserAssocData> assocs = userAssocDao.findEntityAssoc(siteId,
                RefCodeNames.USER_ASSOC_CD.SITE
        );

        Map<Long, UserAssocData> userAssocMap = new HashMap<Long, UserAssocData>();
        for (UserAssocData assocData : assocs) {
            userAssocMap.put(assocData.getUserId(), assocData);
        }

        for (UserData user : users) {
            if (!userAssocMap.containsKey(user.getUserId())) {
                UserAssocData assoc = userAssocDao.createAssoc(user.getUserId(), siteId, RefCodeNames.USER_ASSOC_CD.SITE);
                userAssocMap.put(assoc.getUserId(), assoc);
            }
        }

    }

    public void configureSiteWithUsersFromAnotherSite(Long srcSiteId, Long destSiteId, Long storeId, Long userId) {

        logger.info("configureSiteWithUsersFromAnotheSite)=>  BEGIN" +
                ", srcSiteId:  " + srcSiteId  +
                ", destSiteId:  " + destSiteId +
                ", storeId:  " + storeId +
                ", userId  " + userId
        );

        UserDAOImpl userDao = new UserDAOImpl(em);
        UserAssocDAO userAssocDao = new UserAssocDAOImpl(em);

        List<String> userTypes = userDao.findManagableUserTypesFor(userId);

        logger.info("configureSiteWithUsersFromAnotheSite)=> managableUserTypes: " +userTypes);

        UserAssocCriteria criteria = new UserAssocCriteria();

        criteria.setUserTypes(userTypes);
        criteria.setEntityIds(Utility.toList(srcSiteId));
        criteria.setActiveUserOnly(true);

        List<UserData> users = userAssocDao.findAssocUsers(storeId,
                criteria,
                RefCodeNames.USER_ASSOC_CD.SITE
        );

        logger.info("configureSiteWithUsersFromAnotheSite)=> users.size: " +users.size());

        configureSiteWithUsers(destSiteId, users);

        logger.info("configureSiteWithUsersFromAnotheSite)=> END.");

    }


    @Override
    public void configureCatalogFromAnotherSite(Long createAssocFromSiteId, BusEntityData busEntity) throws IllegalDataStateException {

        logger.info("configureCatalogFromAnotherSite()=>  BEGIN" +
                ", createAssocFromSiteId:  " + createAssocFromSiteId  +
                ", busEntityId:  " + busEntity.getBusEntityId()
        );

        CatalogDAO catalogDao = new CatalogDAOImpl(em);

        CatalogAssocView catalog = catalogDao.findEntityCatalog(createAssocFromSiteId, RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE);
        if (catalog != null && catalog.getCatalog().getCatalogStatusCd().equals(RefCodeNames.CATALOG_STATUS_CD.ACTIVE)) {
            catalogDao.createCatalogAssoc(catalog.getCatalog(), busEntity);
        }

        logger.info("configureCatalogFromAnotherSite()=> END.");

    }
    
    @Override
    public void configureSiteUsersList(Long siteId, Long storeId, List<UserListView> selected, List<UserListView> deselected) {
        if (siteId != null && storeId != null) {
            UserAssocDAO userAssocDao = new UserAssocDAOImpl(em);

            List<UserAssocData> oldAssocList = userAssocDao.readSiteUserAssoc(siteId, storeId);
            
            Map<Long, UserAssocData> oldMap = new HashMap<Long, UserAssocData>();
            
            if (Utility.isSet(oldAssocList)) {
                for (UserAssocData el : oldAssocList) {
                    oldMap.put(el.getUserId(), el);
                }
            }
            
            UserAssocData assoc;
            if (Utility.isSet(selected)) {
                for (UserListView el : selected) {
                    if (!oldMap.containsKey(el.getUserId())) { // create new assoc
                        assoc = new UserAssocData();
                        assoc.setUserId(el.getUserId());
                        assoc.setBusEntityId(siteId);
                        assoc.setUserAssocCd(RefCodeNames.USER_ASSOC_CD.SITE);
                        super.create(assoc);
                    }
                }
                
            }
            
            // remove newly deselected items only
            if (Utility.isSet(deselected)) {
                for (UserListView el : deselected) {
                    assoc = oldMap.get(el.getUserId());
                    if (assoc != null) {
                        em.remove(assoc);
                    }
                }
            }
        }
    }
   
    @Override
    public void configureSiteCatalog(Long siteId, Long catalogId) {
        if (siteId != null && catalogId != null) {
            CatalogAssocDAO catalogAssocDao = new CatalogAssocDAOImpl(em);

            List<CatalogAssocData> oldAssocList = catalogAssocDao.readCatalogAssoc(siteId, RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE);
            
            Map<Long, CatalogAssocData> oldMap = new HashMap<Long, CatalogAssocData>();
            
            if (Utility.isSet(oldAssocList)) {
                for (CatalogAssocData el : oldAssocList) {
                    oldMap.put(el.getCatalogId(), el);
                }
            }
            
            CatalogAssocData assoc;
            if (oldMap.containsKey(catalogId)) {
                oldMap.remove(catalogId);
            } else { // create new assoc
                assoc = new CatalogAssocData();
                assoc.setCatalogId(catalogId);
                assoc.setBusEntityId(siteId);
                assoc.setCatalogAssocCd(RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE);
                super.create(assoc);
            }

            
            if (oldMap.size() > 0) {
                for (CatalogAssocData assocData : oldMap.values()) { // remove unused old assocs
                    assoc = assocData;
                    em.remove(assoc);
                }
            }
        }
    }
    
    @Override
    public BusEntityAssocData findSiteAccountAssoc(Long siteId) {
        BusEntityAssocData accountAssoc = null;
        if (siteId != null) {
            BusEntityAssocDAO busEntityAssocDao = new BusEntityAssocDAOImpl(em);
            
            List<BusEntityAssocData> accountAssocList = busEntityAssocDao.findAssocs(siteId, new ArrayList<Long>(), RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            
            if (Utility.isSet(accountAssocList)) {
                accountAssoc = accountAssocList.get(0);
            }
        }
        
        return accountAssoc;
    }
    
    @Override
    public List<ContractData> findSiteContract(Long siteId) {
        List<ContractData> contracts = null;
        if (siteId != null) {
            StringBuilder baseQuery = new StringBuilder("Select object(contract) from ContractData contract, CatalogAssocData contractToCatalog");
                baseQuery.append(" WHERE contractToCatalog.busEntityId = (:siteId)");
                baseQuery.append(" AND contractToCatalog.catalogAssocCd =(:catalogToSiteAssocCd)");
                baseQuery.append(" AND contractToCatalog.catalogId = contract.catalogId");
                baseQuery.append(" AND contract.contractStatusCd = (:contractStatusCd)");
            
            Query q = em.createQuery(baseQuery.toString());
            
            q.setParameter("siteId", siteId);
            q.setParameter("catalogToSiteAssocCd", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE);
            q.setParameter("contractStatusCd", RefCodeNames.CONTRACT_STATUS_CD.ACTIVE);
            
            contracts = (List<ContractData>) q.getResultList();
        }
        return contracts;
    }
    
    @Override
    public List<BusEntityData> findSiteAccount(Long siteId) {
        List<BusEntityData> accounts = null;
        if (siteId != null) {
            StringBuilder baseQuery = new StringBuilder("Select object(account) from BusEntityData account, BusEntityAssocData siteToAccount");
                baseQuery.append(" WHERE siteToAccount.busEntity1Id = (:siteId)");
                baseQuery.append(" AND siteToAccount.busEntityAssocCd =(:siteToAccountAssocCd)");
                baseQuery.append(" AND siteToAccount.busEntity2Id = account.busEntityId");
                baseQuery.append(" AND account.busEntityStatusCd = (:accountStatusCd)");
            
            Query q = em.createQuery(baseQuery.toString());
            
            q.setParameter("siteId", siteId);
            q.setParameter("siteToAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            q.setParameter("accountStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);
            
            accounts = (List<BusEntityData>) q.getResultList();
        }
        return accounts;
    }
    
    @Override
    public List<Long> findSiteByDistRefNumberAndStoreId(String distSiteRefNumber, Long storeId) {
    	Query q = em.createQuery("select site.busEntityId "
                + "from BusEntityFullEntity site "
                + "left outer join site.properties distSiteReferenceNumber with distSiteReferenceNumber.shortDesc=(:distRefNumberCode) and distSiteReferenceNumber.value is not null "
                + "inner join site.busEntityAssocsForBusEntity1Id siteAccount "
                + "inner join siteAccount.busEntity2Id account "
                + "inner join account.busEntityAssocsForBusEntity1Id accountStore "
                + "where site.busEntityTypeCd = (:typeCdOfSite) "
                + "and accountStore.busEntity2Id.busEntityId = (:storeId) "
                + "and accountStore.busEntityAssocCd = (:accountStoreAssocCd)"
                + "and siteAccount.busEntityAssocCd = (:siteAccountAssocCd) "
                + "and account.busEntityTypeCd = (:typeCdOfAccount) "
                + "and distSiteReferenceNumber.value = (:distReferenceNumber) "
                + "and site.busEntityStatusCd = (:statusCd)"
        ) ;

    	q.setParameter("storeId", storeId);
        q.setParameter("siteAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
        q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        q.setParameter("typeCdOfAccount", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
        q.setParameter("typeCdOfSite", RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
        q.setParameter("distRefNumberCode", RefCodeNames.PROPERTY_TYPE_CD.DIST_SITE_REFERENCE_NUMBER);
        q.setParameter("distReferenceNumber",distSiteRefNumber);
        q.setParameter("statusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);


        List<Long>  r = q.getResultList();

        logger.info("findSiteByDistRefNumberAndStoreId)=> END, fetched : " + r.size() + " rows");

        return r;
    }
    
    @Override
    public List<Long> findSiteByAccountIdsAndStoreId(List<Long> accountIds, Long storeId) {
    	Query q = em.createQuery("select site.busEntityId "
                + "from BusEntityFullEntity site "
                + "inner join site.busEntityAssocsForBusEntity1Id siteAccount "
                + "inner join siteAccount.busEntity2Id account "
                + "inner join account.busEntityAssocsForBusEntity1Id accountStore "
                + "where site.busEntityTypeCd = (:typeCdOfSite) "
                + "and accountStore.busEntity2Id.busEntityId = (:storeId) "
                + ((!accountIds.isEmpty()) ? "and accountStore.busEntity1Id.busEntityId in (:accountIds) " : "")
                + "and accountStore.busEntityAssocCd = (:accountStoreAssocCd)"
                + "and siteAccount.busEntityAssocCd = (:siteAccountAssocCd) "
                + "and account.busEntityTypeCd = (:typeCdOfAccount) "
                + "and site.busEntityStatusCd = (:statusCd)"
        ) ;

    	q.setParameter("storeId", storeId);
        q.setParameter("siteAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
        q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        q.setParameter("typeCdOfAccount", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
        q.setParameter("typeCdOfSite", RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
        if (!accountIds.isEmpty())
        	q.setParameter("accountIds",accountIds);
        q.setParameter("statusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);


        List<Long>  r = q.getResultList();

        logger.info("findSiteByAccountIdsAndStoreId)=> END, fetched : " + r.size() + " rows");

        return r;
    }
}
