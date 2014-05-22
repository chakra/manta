package com.espendwise.manta.auth;


import org.apache.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

public class AuthProcessingFilter extends AnonymousAuthenticationFilter {

    private static final Logger logger = Logger.getLogger(AuthProcessingFilter.class);

    @Override
    public void afterPropertiesSet() {

    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {

        logger.info("createAuthentication()=> BEGIN");

        AnonymousAuthenticationToken anonymous = new AnonymousAuthenticationToken(AuthConst.GUEST,
                AuthConst.ANONYMOUS,
                AuthConst.ANONYMOUS.getAuthorities().toArray(new GrantedAuthority[AuthConst.ANONYMOUS.getAuthorities().size()])
        );

        logger.info("createAuthentication()=> END, anonymous: " + anonymous);

        return  anonymous;
    }


}
