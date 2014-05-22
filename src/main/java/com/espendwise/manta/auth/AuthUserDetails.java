package com.espendwise.manta.auth;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Locale;

public class AuthUserDetails implements UserDetails {


    private Collection<GrantedAuthority> authorities;
    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Long userId;
    private String userTypeCd;
    private Locale prefLocale;

    public AuthUserDetails(Long userId,
                           String  userTypeCd,
                           Locale prefLocale,
                           Collection<GrantedAuthority> authorities,
                           String password,
                           String username,
                           boolean accountNonExpired,
                           boolean enabled,
                           boolean credentialsNonExpired,
                           boolean accountNonLocked) {

        this.userId = userId;
        this.userTypeCd= userTypeCd;
        this.authorities = authorities;
        this.password = password;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.enabled = enabled;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.userTypeCd = userTypeCd;
        this.prefLocale = prefLocale;
    }

    public AuthUserDetails() {

    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserTypeCd() {
        return userTypeCd;
    }


    public Locale getPrefLocale() {
        return prefLocale;
    }

    public void setPrefLocale(Locale prefLocale) {
        this.prefLocale = prefLocale;
    }
}
