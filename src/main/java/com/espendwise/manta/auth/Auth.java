package com.espendwise.manta.auth;


import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class Auth {

    private static final Logger logger = Logger.getLogger(Auth.class);

    public static SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    public static AppUser getAppUser() {
        AuthUser authUser = getAuthUser();
        return authUser == null ? null : authUser.getAppUser();
    }

    public static AuthUser getAuthUser() {
        Authentication authentication = getAuthentication();
        logger.debug("getAuthUser()=< authentication:"+authentication);
        return (AuthUser) (authentication == null ? null : authentication.getPrincipal());
    }

    public static Authentication getAuthentication() {
        SecurityContext context = getContext();
        return context == null ? null : context.getAuthentication();
    }

    public static ApplicationDataSource getApplicationDataSource() {

        AppUser appUser = getAppUser();

        if (appUser != null) {
            AppStoreContext storeContext = appUser.getContext(AppCtx.STORE);
            if (storeContext != null) {
                return storeContext.getDataSource();
            }

        }

        return null;

    }

    public static boolean isAuthorizedForFunction(String name) {

        AppUser appUser = getAppUser();

        if (appUser != null) {
            ApplicationUserPermission permissions = appUser.getContext(AppCtx.STORE).getUserPermission();
            return permissions.getApplicationFunctions().contains(name) || permissions.getWoApplicationFunctions().contains(name);
        }

        return false;
    }
}
