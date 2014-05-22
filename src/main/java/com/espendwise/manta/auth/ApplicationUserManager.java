package com.espendwise.manta.auth;

import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;
import com.espendwise.ocean.common.webaccess.*;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


public class ApplicationUserManager {

    private static final Logger logger = Logger.getLogger(ApplicationUserManager.class);
    private static final String ORCA_LOGIN_PATH_TEMPLATE = "/instance/%s/%s/login";

    private List<LogonProvider> logonProviders;

    public AuthUser logon(HttpServletRequest request, Authentication auth) throws Exception {

        AuthUser authUser = (AuthUser)auth.getPrincipal();

        AppUser appUser =  null;

        List<LogonProvider> providers =  getLogonProviders();
        for (LogonProvider provider : providers) {
            if (isAuthorized(provider, authUser)) {
                LogonService service = provider.getService();
                logger.info("logon()=> run service for logon, service:  " + service);
                appUser = service.logon(authUser.getUsername(), provider.createLogonRequest(auth, request));
            }
        }

        return  assignAppUser(appUser, authUser);

    }

    public AuthUser logonToOrca(AuthUser authUser) throws Exception {

        AppUser appUser = authUser.getAppUser();

        AppUser user = authUser.getAppUser();
        AppStoreContext storeContext = user.getContext(AppCtx.STORE);
        String dsUnit = storeContext.getDataSource().getDataSourceIdent().getDataSourceName();
        Long storeId = storeContext.getStoreId();

        LoginData loginData = new LoginData();

        loginData.setUserName(user.getUserName());
        loginData.setPassword(authUser.getPassword());
        loginData.setCountry(user.getLocale().getCountry());
        loginData.setLanguage(user.getLocale().getLanguage());
        loginData.setEncrypted(true);

        logger.info("logonToOrca()=> loginData:  " + loginData);

        String hostAddress = AppResourceHolder
                .getAppResource()
                .getApplicationSettings()
                .getSettings(Constants.APPLICATION_SETTINGS.ORCA_HOST_ADDRESS);

        String requestPath = String.format(ORCA_LOGIN_PATH_TEMPLATE, dsUnit, storeId.toString());

        logger.info("logonToOrca()=> hostAddress:  " + hostAddress);
        logger.info("logonToOrca()=> requestPath:  " + requestPath);

        RestRequest request = new RestRequest(hostAddress, requestPath);

        LoginCredential lc = request.doPut(loginData, new ObjectTokenType<BasicResponseValue<LoginCredential>>() {
        });

        appUser.getContext(AppCtx.ORCA_INSTANCE).setLoginCredential(lc);

        return assignAppUser(appUser, authUser);

    }


    private boolean isAuthorized(LogonProvider provider, AuthUser authUser) {
        return provider.isAuthorized(authUser.getAuthorities());
    }


    public AuthUser assignAppUser(AppUser appUser, AuthUser authUser) {

        logger.debug("assignAppUser()=> BEGIN, appUser: " + appUser);

        if (appUser != null) {

            AppAuth appAuth = new AppAuth(appUser, authUser.getAppUserDetails());
            authUser.setAppAuth(appUser.getUserName(), appAuth);

        }

        logger.debug("assignAppUser()=> END, appUser: " + appUser);

        return authUser;
    }

    public void changeInstance(AuthUser authUser, Long globalEntityId) {

        logger.info("changeInstance()=> BEGIN, user: " + authUser +" , : " + globalEntityId);

        AppUser appUser = authUser.getAppUser();

        List<LogonService> services = getUserServices(authUser);
        for (LogonService service : services) {
            if (service instanceof ChangeStoreContextListener) {
                appUser = ((ChangeStoreContextListener) service).changeStoreContext(appUser, globalEntityId);
            }
        }

        assignAppUser(appUser, authUser);

        logger.info("changeInstance()=> END.");
    }


    public void changeInstance(AuthUser authUser, ApplicationDataSource datasource, Long storeId) throws ApplicationRuntimeException {
        changeInstance(authUser, datasource.getDataSourceIdent().getDataSourceName(), storeId);
    }

    public void changeInstance(AuthUser authUser, String datasource, Long storeId) throws ApplicationRuntimeException {

        logger.info("changeInstance()=> BEGIN, user: " + authUser +
                " , datasource: " + datasource +
                ", storeId: " + storeId);

        AppUser appUser = authUser.getAppUser();

        List<LogonService> services = getUserServices(authUser);
        for (LogonService service : services) {
            if (service instanceof ChangeStoreContextListener) {
                appUser = ((ChangeStoreContextListener) service).changeStoreContext(appUser, datasource, storeId);
            }
        }

        assignAppUser(appUser, authUser);

        logger.info("changeInstance()=> END.");
    }


    private List<LogonService> getUserServices(AuthUser authUser) {

        List<LogonService> x = new ArrayList<LogonService>();

        List<LogonProvider> providers = getLogonProviders();
        for (LogonProvider provider : providers) {
            if (provider.isAuthorized(authUser.getAuthUserDetails().getAuthorities())) {
                x.add(provider.getService());
            }
        }

        return x;

    }

    public void clearAppUserSession(HttpServletRequest request) {
        if (Auth.getAppUser() != null) {
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
        }
    }

    public void setLogonProviders(List<LogonProvider> logonProviders) {
        this.logonProviders = logonProviders;
    }

    public List<LogonProvider> getLogonProviders() {
        return logonProviders;
    }


}