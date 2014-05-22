package com.espendwise.manta.auth.ctx;


import com.espendwise.ocean.common.webaccess.LoginCredential;

public class AppStjohnInstanceContext implements AppCtx {

    LoginCredential loginCredential;

    public LoginCredential getLoginCredential() {
        return loginCredential;
    }

    public void setLoginCredential(LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
    }
}
