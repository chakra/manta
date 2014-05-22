package com.espendwise.manta.auth;


import com.espendwise.manta.auth.ctx.AppAdminUserContext;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.view.InstanceView;
import com.espendwise.manta.model.view.StoreUiOptionView;
import com.espendwise.manta.service.UserLogonService;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.HomeViewType;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.RefCodeNamesKeys;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.trace.ApplicationIllegalAccessException;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;
import com.espendwise.manta.util.trace.ExceptionReason;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class AdministratorLogonService implements LogonService, ChangeStoreContextListener {

    private static final Logger logger = Logger.getLogger(AdministratorLogonService.class);

    @Autowired
    private UserLogonService userLogonService;

    private boolean supports(LogonRequest request) {
        return request != null && request.getClass().isAssignableFrom(AdministratorLogonRequest.class);
    }

    public AppUser logon(String username, LogonRequest request) throws ApplicationRuntimeException {

        if (!supports(request)) {
            return null;
        }

        return logon(username, (AdministratorLogonRequest) request);

    }


    public AppUser logon(String username, AdministratorLogonRequest request) throws ApplicationRuntimeException {

        AppUser appUser = new AppUser();

        String country = request.getLocale().getCountry();
        String language = request.getLocale().getLanguage();


        List<InstanceView> stores = getUserStoreDataSources(request.getAuthUserMap());

        AuthUserAccessTokenProperty authToken  = request.getAuthAccessTokenProperty();
        InstanceView  store = authToken == null ? getDefaultUserStore(username, request.getAuthUserMap(), request.getServerName())
                : findInstance(request.getAuthUserMap(), authToken.getDatasource(), authToken.getBusEntityId());

        AuthUserData authUserData = getUnitUser(request.getAuthUserMap(), store);

        Locale locale = getUserLocale(authUserData.getUserDetails().getPrefLocale(),
                country,
                language,
                Locale.US
        );

        AppAdminUserContext appUserCtx = createUserContext(authUserData.getUserDetails(),
                locale,
                store,
                getHomeViewType(stores.size())
        );

        appUser.setUserName(authUserData.getUserDetails().getUsername());
        appUser.setUserId(authUserData.getUserDetails().getUserId());
        appUser.setUserTypeCd(authUserData.getUserDetails().getUserTypeCd());
        appUser.setLocale(locale);
        appUser.setUserContext(appUserCtx);

        return appUser;
    }


    private AuthUserData getUnitUser(AuthenticationUserMap authUserMap, InstanceView storeDsView) {

        Set<AuthDatabaseAccessUnit> units = authUserMap.keySet();

        for (AuthDatabaseAccessUnit unit : units) {
            if (storeDsView.getDataSourceIdent().equals(unit.getAuthDataSourceIdent())) {
                return authUserMap.get(unit);
            }
        }

        return null;
    }


    private AppAdminUserContext createUserContext(AuthUserDetails user,
                                                  Locale locale,
                                                  InstanceView store,
                                                  HomeViewType homeViewType) {

        AppAdminUserContext appUserCtx = new AppAdminUserContext();

        StoreUiOptionView uiOptions = getUiOptions(store.getDataSourceIdent().getDataSourceName(),
                user.getUserId(),
                store.getStore().getStoreId(),
                homeViewType,
                locale
        );

        ApplicationUserPermission userPermission = getUserPermission(store.getDataSourceIdent().getDataSourceName(),
                user.getUserId(),
                user.getUserTypeCd()
        );

        logger.debug("createUserContex()=> store: " + store);
        logger.debug("createUserContex()=> uiOptions: " + uiOptions);
        logger.debug("createUserContex()=> userPermission: " + userPermission);

        appUserCtx.getStoreContext().setMainStoreIdent(store.getMainStoreIdent());
        appUserCtx.getStoreContext().setGlobalEntityId(store.getStore().getGlobalEntityId());
        appUserCtx.getStoreContext().setStoreId(store.getStore().getStoreId());
        appUserCtx.getStoreContext().setStoreName(store.getStore().getStoreName());
        appUserCtx.getStoreContext().setDataSource(new ApplicationDataSource(store.getDataSourceIdent()));
        appUserCtx.getStoreContext().setUiOptions(uiOptions);
        appUserCtx.getStoreContext().setUserPermission(userPermission);

        return appUserCtx;
    }

    private boolean verifyAccessToStore(String datasource, Long storeId) throws ApplicationIllegalAccessException {
        return userLogonService.verifyAccessToStore(datasource, storeId);
    }

    private AuthUserDetails retrieveAuthenticatedUserForStore(String datasource, Long storeId) throws ApplicationIllegalAccessException {
        return userLogonService.retrieveAuthenticatedUserForStore(datasource, storeId);
    }

    private InstanceView retrieveAuthenticatedUserStore(String datasource, Long storeId) {
        return userLogonService.retrieveAuthenticatedUserStore(datasource, storeId);
    }

    private List<InstanceView> getUserStoreDataSources() {
        return userLogonService.getUserStoreDataSources();
    }

    private InstanceView findInstanceByGlobalId(Long globalEntityId) {
        return userLogonService.findInstanceByGlobalId(globalEntityId);
    }

    private InstanceView findInstance(AuthenticationUserMap authUserMap, String ds,  Long busEntityId) {
        return userLogonService.findInstance(authUserMap, ds, busEntityId);
    }

    private List<InstanceView> getUserStoreDataSources(AuthenticationUserMap authUserMap) {
        return userLogonService.getUserStoreDataSources(authUserMap);
    }

    private InstanceView getDefaultUserStore(String username, AuthenticationUserMap authUserMap, String serverName) {

        return userLogonService.getDefaultUserStore(username,
                authUserMap,
                serverName
        );

    }

    private StoreUiOptionView getUiOptions(String dataSource, Long userId, Long store, HomeViewType homeViewType, Locale userLocale) {
        return userLogonService.getStoreUiOptions(dataSource,
                userId,
                store,
                homeViewType,
                userLocale
        );
    }


    private Locale getUserLocale(Locale userLocale, String country, String lang, Locale defLocale) {
        return userLogonService.getUserLocale(country,
                lang,
                defLocale,
                userLocale
        );
    }


    private HomeViewType getHomeViewType(int userStoreSize) {

        for (HomeViewType viewType : HomeViewType.values()) {
            if (viewType.isApplied(userStoreSize)) {
                return viewType;
            }
        }

        return null;
    }

    @Override
    public AppUser changeStoreContext(AppUser appUser, Long globalEntityId) {

        InstanceView instance = findInstanceByGlobalId(globalEntityId);

        if(instance == null){

            throw new ApplicationIllegalAccessException(
                    ExceptionReason.SystemReason.USER_DOES_NOT_HAVE_ACCESS_TO_INSTANCE,
                    new NumberArgument(globalEntityId)
            );

        }

        return changeStoreContext(
                appUser,
                instance.getDataSourceIdent().getDataSourceName(),
                instance.getStore().getStoreId()
        );

    }


    @Override
    public AppUser changeStoreContext(AppUser appUser, String datasource, Long storeId) {

        if (!verifyAccessToStore( datasource, storeId)) {

            throw new ApplicationIllegalAccessException(
                    ExceptionReason.SystemReason.USER_DOES_NOT_HAVE_ACCESS_TO_STORE,
                    new StringArgument(datasource),
                    new NumberArgument(storeId)
            );

        }

        AuthUserDetails user = retrieveAuthenticatedUserForStore(datasource, storeId);
        InstanceView storeView = retrieveAuthenticatedUserStore(datasource, storeId);

        Locale locale = getUserLocale(user.getPrefLocale(), null, null, appUser.getLocale());

        AppAdminUserContext appUserCtx = createUserContext(
                user,
                locale,
                storeView,
                appUser.getContext(AppCtx.STORE).getUiOptions().getHomeViewType()
        );

        appUser.setUserName(user.getUsername());
        appUser.setUserId(user.getUserId());
        appUser.setUserTypeCd(user.getUserTypeCd());
        appUser.setLocale(locale);

        appUser.setUserContext(appUserCtx);

        return appUser;
    }


    public ApplicationUserPermission getUserPermission(@AppDS String dataSource, Long userId, String userTypeCd) {

        logger.info("getUserPermission()=> BEGIN");

        ApplicationUserPermission permission;
        logger.info("getUserPermission()=> userId: " + userId + ", userTypeCd: " + userTypeCd);
        if (RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR.equals(userTypeCd)) {
            permission = getAllPermission();
        } else {
            permission = getApplicationUserPermission(dataSource, userId, userTypeCd);
        }
        logger.info("getUserPermission()=> END.");

        return permission;

    }

    public ApplicationUserPermission getAllPermission() {

        ApplicationUserPermission permission = new ApplicationUserPermission();

        List<Pair<String, String>>  refCds = RefCodeNamesKeys.getRefCodeValues(RefCodeNames.APPLICATION_FUNCTIONS.class);

        Set<String> functions = new HashSet<String>();
        for (Pair<String, String> refCd : refCds) {
            functions.add(refCd.getObject2());
        }


        List<Pair<String, String>> woRefCds = RefCodeNamesKeys.getRefCodeValues(RefCodeNames.APPLICATION_WO_FUNCTIONS.class);
        Set<String> wofunctions = new HashSet<String>();
        for (Pair<String, String> refCd : woRefCds) {
            wofunctions.add(refCd.getObject2());
        }

        permission.setApplicationFunctions(functions);
        permission.setWoApplicationFunctions(wofunctions);

        return permission;
    }


    public ApplicationUserPermission getApplicationUserPermission(@AppDS String dataSource, Long userId, String userType) {

        logger.info("getApplicationUserPermission()=> BEGIN");

        ApplicationUserPermission permission = new ApplicationUserPermission();

        permission.setApplicationFunctions(new HashSet<String>());
        permission.setWoApplicationFunctions(new HashSet<String>());

        List<GroupAssocData> functions = userLogonService.findUserGroupFunctions(dataSource, userId, userType);

        ApplicationUserPermission allAvailablePermissions = getAllPermission();

        for (GroupAssocData function : functions) {

            if (allAvailablePermissions.getApplicationFunctions().contains(function.getApplicationFunction())) {
                permission.getApplicationFunctions().add(function.getApplicationFunction());
            }

            if (allAvailablePermissions.getWoApplicationFunctions().contains(function.getApplicationFunction())) {
                permission.getWoApplicationFunctions().add(function.getApplicationFunction());
            }
        }


        logger.info("getApplicationUserPermission()=> END.");

        return permission;
    }

}
