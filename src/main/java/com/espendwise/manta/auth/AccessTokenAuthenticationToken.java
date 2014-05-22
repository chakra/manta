package com.espendwise.manta.auth;

public class AccessTokenAuthenticationToken extends org.springframework.security.authentication.AbstractAuthenticationToken  {

    private final Object credentials;
    private final Object principal;
    private String datasource;

    public AccessTokenAuthenticationToken(Object credentials, String datasource) {
        super(null);
        this.principal = null;
        this.credentials = credentials;
        this.datasource= datasource;
        super.setAuthenticated(false);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public String getDatasource() {
        return datasource;
    }

    @Override
    public String toString() {
        return "AccessTokenAuthenticationToken{" +
                "credentials=" + "xxxxxx" +
                ", principal=" + principal +
                ", datasource='" + datasource + '\'' +
                '}';
    }
}
