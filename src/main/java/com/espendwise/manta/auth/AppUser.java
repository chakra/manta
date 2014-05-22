package com.espendwise.manta.auth;


import com.espendwise.manta.auth.ctx.AppUserContext;
import com.espendwise.manta.auth.ctx.Ctx;
import com.espendwise.manta.util.RefCodeNames;

import java.util.Locale;

public class AppUser implements IAppUser {

    private static final long serialVersionUID = -1;

    private Locale locale;
    private String userName;
    private Long userId;
    private String userTypeCd;

    private AppUserContext userContext;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setUserContext(AppUserContext userContext) {
        this.userContext = userContext;
    }

    public AppUserContext getUserContext() {
        return userContext;
    }

    public <T> T getContext(Ctx<T> ctx) {
        return  userContext.get(ctx);
    }

    public String getUserName() {
        return userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTypeCd() {
        return userTypeCd;
    }

    public void setUserTypeCd(String userTypeCd) {
        this.userTypeCd = userTypeCd;
    }

    public boolean isSystemAdmin() {
        return RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR.equals(this.userTypeCd);
    }

    public boolean isStoreAdmin() {
        return RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR.equals(this.userTypeCd);
    }

    public boolean isAdmin() {
        return RefCodeNames.USER_TYPE_CD.ADMINISTRATOR.equals(this.userTypeCd);
    }

    public boolean isQneOfAdmin() {
        return isAdmin() || isStoreAdmin() || isSystemAdmin();
    }


    public boolean getIsSystemAdmin() {
        return isSystemAdmin();
    }

    public boolean getIsStoreAdmin() {
        return isStoreAdmin();
    }

    public boolean getIsAdmin() {
        return isAdmin();
    }

    public boolean getIsQneOfAdmin() {
        return isQneOfAdmin();
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "locale=" + locale +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                ", userTypeCd='" + userTypeCd + '\'' +
                '}';
    }
}
