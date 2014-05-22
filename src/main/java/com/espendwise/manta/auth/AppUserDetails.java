package com.espendwise.manta.auth;


import org.springframework.security.core.userdetails.UserDetails;

public interface AppUserDetails extends UserDetails {
    public AppUser getApplicationUser();
}
