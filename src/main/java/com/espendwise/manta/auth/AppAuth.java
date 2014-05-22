package com.espendwise.manta.auth;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

public class AppAuth implements Serializable {

    private UserDetails userDetails;
    private AppUser user;

    public AppAuth(AppUser user, UserDetails userDetails) {
        this.user =  user;
        this.userDetails = userDetails;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    public String getPassword() {
        return userDetails.getPassword();
    }

    public String getUsername() {
        return userDetails.getUsername();
    }

    public boolean isAccountNonExpired() {
        return userDetails.isAccountNonLocked();
    }


    public boolean isAccountNonLocked() {
        return userDetails.isAccountNonLocked();
    }

    public boolean isCredentialsNonExpired() {
        return userDetails.isAccountNonExpired();
    }

    public boolean isEnabled() {
        return userDetails.isEnabled();
    }

    public AppUser getUser() {
        return user;
    }
}
