package com.espendwise.manta.auth;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Locale;

public class ApplicationLogonProvider implements LogonProvider {

    private LogonService logonService;
    private AdministratorLogonRoles roles;

    @Override
    public LogonService getService() {
        return getLogonService();
    }

    public boolean isAuthorized(Collection<? extends GrantedAuthority> credentials) {
        for (GrantedAuthority x : credentials) {
            for (String access : getRoles().getList()) {
                if (x.getAuthority().equals(access)) {
                    return true;
                }
            }
        }
        return false;
    }


    public LogonRequest createLogonRequest(Authentication auth, HttpServletRequest request) {

        AuthUser authUser = (AuthUser)auth.getPrincipal();

        String serverName = request.getServerName();
        Locale locale = request.getLocale();
        AuthenticationUserMap authUserMap = authUser.getAuthUserContext().get(AuthUserContext.AUTHI_USER_MAP);
        AuthUserAccessTokenProperty authToken = authUser.getAuthUserContext().get(AuthUserContext.AUTH_USER_ACCESS_TOKEN_PROPERTY);

        return new AdministratorLogonRequest(serverName,
                authUserMap,
                locale,
                authToken
        );
    }

    public LogonService getLogonService() {
        return logonService;
    }

    public void setLogonService(LogonService logonService) {
        this.logonService = logonService;
    }


    public void setRoles(AdministratorLogonRoles roles) {
        this.roles = roles;
    }

    public AdministratorLogonRoles getRoles() {
        return roles;
    }
}
