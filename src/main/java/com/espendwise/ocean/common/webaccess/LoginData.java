package com.espendwise.ocean.common.webaccess;

import java.io.Serializable;

public class LoginData implements Serializable {

    private String userName;
    private String language;
    private String country;
    private String password;
    private boolean encrypted;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "userName='" + userName + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", encrypted='" + encrypted + '\'' +
                ", password='" +"xxxxxx" + '\'' +
                '}';
    }
}

