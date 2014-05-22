package com.espendwise.webservice.restful.value;

import com.espendwise.ocean.common.webaccess.LoginData;

public abstract class BaseRequestData {
    protected String accessToken;
    protected LoginData loginData;    

    public BaseRequestData() {}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }
}


