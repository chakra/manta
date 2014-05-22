package com.espendwise.manta.auth;


import org.springframework.security.core.userdetails.UserDetails;

import java.util.Locale;

public interface IAuthUser {

    public AppUser getAppUser();

    public AppUser getAppUser(String username);

    public UserDetails getAppUserDetails();

    public Locale getAuthUserLocale();

    public AuthUserContext getAuthUserContext();

    public UserDetails getAuthUserDetails();

    public String getModByName();



}
