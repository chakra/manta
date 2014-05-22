package com.espendwise.manta.auth;


import com.espendwise.manta.util.Constants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationLogonRequest implements LogonRequest {

    public  static interface  ADDITIONAL_PARAMETERS {
        public static final String ACCESS_TOKEN  = Constants.ACCESS_TOKEN;
    }

    private String serverName;
    private Locale locale;
    private Map<String, Object> additionalParameters;
    private AuthenticationUserMap authUserMap;

    public ApplicationLogonRequest(String serverName, AuthenticationUserMap authUserMap, Locale locale) {
        this.serverName  = serverName;
        this.locale  = locale;
        this.authUserMap  =  authUserMap;
        this.additionalParameters = new HashMap<String, Object>();
    }

    public ApplicationLogonRequest(String serverName, AuthenticationUserMap authUserMap, Locale locale, Map<String, Object> additionalParameters) {
        this.serverName  = serverName;
        this.locale  = locale;
        this.authUserMap  =  authUserMap;
        this.additionalParameters = additionalParameters;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Object getAdditionalParameter(String name) {
       return this.additionalParameters.get(name);
    }

    public Map<String, Object> getAdditionalParameters() {
        return additionalParameters;
    }

    public AuthenticationUserMap getAuthUserMap() {
        return authUserMap;
    }

    public void setAuthUserMap(AuthenticationUserMap authUserMap) {
        this.authUserMap = authUserMap;
    }

    @Override
    public String toString() {
        return "ApplicationLogonRequest{" +
                "serverName='" + serverName + '\'' +
                ", locale=" + locale +
                ", additionalParameters=" + additionalParameters +
                ", authUserMap=" + authUserMap +
                '}';
    }
}
