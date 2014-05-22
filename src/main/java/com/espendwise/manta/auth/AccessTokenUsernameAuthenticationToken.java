package com.espendwise.manta.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class  AccessTokenUsernameAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private AuthUserAccessTokenProperty authToken;

    public AccessTokenUsernameAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, AuthUserAccessTokenProperty authToken) {
        super(principal, credentials, authorities);
        this.authToken = authToken;
    }

    public AuthUserAccessTokenProperty getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthUserAccessTokenProperty authToken) {
        this.authToken = authToken;
    }
}
