package com.espendwise.ocean.common.webaccess;


import java.io.Serializable;

public class LoginCredential implements Serializable {

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String USER_NAME = "userName";
    public static final String HOST_ADDRESS = "hostAddress";

    private String userName;
    private String accessToken;
    private String hostAddress;

    public String getHostAddress() {
        return hostAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
