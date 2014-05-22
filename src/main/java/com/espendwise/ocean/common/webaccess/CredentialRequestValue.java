package com.espendwise.ocean.common.webaccess;


import java.io.Serializable;

public class CredentialRequestValue implements Serializable {

    private LoginCredential loginCredential;

    public LoginCredential getLoginCredential() {
        return loginCredential;
    }

    public void setLoginCredential(LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
    }
}
