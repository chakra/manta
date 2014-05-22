package com.espendwise.manta.auth;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class ApplicationSecurityFilter extends org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter {

    private static final Logger logger = Logger.getLogger(ApplicationSecurityFilter.class);

    private ApplicationUserManager applicationUserManager;
    private ProviderManager accessTokenAuthenticationManager;

    public ApplicationSecurityFilter() {
        super();
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return super.requiresAuthentication(request, response);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        logger.info("attemptAuthentication()=> BEGIN, Time at: " + new Date());

        clearAppUserSession(request);

        Authentication auth = authenticateUser(request, response);
        if (auth != null && auth.isAuthenticated()) {
            logonUser(request, auth);
        }

        logger.info("attemptAuthentication()=> END.");

        return auth;
    }


    private Authentication authenticateUser(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = null;

        try {

            auth = isAccessTokenAuthentication(request)
                    ? this.attemptAccessTokenAuthentication(request, response)
                    : super.attemptAuthentication(request, response);

        } catch (AuthenticationException e) {

            logger.info("authenticateUser()=> EXC TRACE BEGIN");
            logger.error(e.getMessage(), e);
            logger.info("authenticateUser)=> EXC TRACE END");

            throw e;
        }

        return auth;
    }

    private Authentication attemptAccessTokenAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = Utility.strNN(obtainAccessToken(request)).trim();
        String datasource = Utility.strNN(obtainDatasource(request)).trim();

        AccessTokenAuthenticationToken authRequest = new AccessTokenAuthenticationToken(accessToken, datasource );
        setAccessTokenDetails(request, authRequest);

        return this.getAccessTokenAuthenticationManager().authenticate(authRequest);
    }

    protected void setAccessTokenDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(getAuthenticationDetailsSource().buildDetails(request));
    }

    private void logonUser(HttpServletRequest request, Authentication auth) throws AuthenticationException {

        ApplicationUserManager appUserMgr = getApplicationUserManager();

        try {

            appUserMgr.logon(request, auth);

        } catch (Exception e) {

            logger.info("logonUser)=> EXC TRACE BEGIN");
            logger.error(e.getMessage(), e);
            logger.info("logonUser)=> EXC TRACE END");

            throw new AuthenticationServiceException(e.getMessage());
        }
    }
    protected String obtainAccessToken(HttpServletRequest request) {
        return request.getParameter(Constants.ACCESS_TOKEN);
    }

    protected String obtainDatasource(HttpServletRequest request) {
        return "ds01";
    }

    private boolean isAccessTokenAuthentication(HttpServletRequest request) {
        return request.getParameter(Constants.ACCESS_TOKEN) != null;
    }

    private void clearAppUserSession(HttpServletRequest request) {
        if (Auth.getAppUser() != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
                request.getSession();
            }
            SecurityContextHolder.clearContext();
        }
    }

    public void setApplicationUserManager(ApplicationUserManager applicationUserManager) {
        this.applicationUserManager = applicationUserManager;
    }

    public ApplicationUserManager getApplicationUserManager() {
        return applicationUserManager;
    }


    public void setAccessTokenAuthenticationManager(ProviderManager accessTokenAuthenticationManager) {
        this.accessTokenAuthenticationManager = accessTokenAuthenticationManager;
    }

    public ProviderManager getAccessTokenAuthenticationManager() {
        return accessTokenAuthenticationManager;
    }
}
