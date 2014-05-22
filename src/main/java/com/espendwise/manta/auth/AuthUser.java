package com.espendwise.manta.auth;


import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Locale;

public class AuthUser extends User implements IAuthUser {

    private static final Logger logger = Logger.getLogger(AuthUser.class);

    private Locale locale;
    private AuthUserContext context;
    private AppAuthPool appAuthPool;
    private AppAuth appAuth;


    public AuthUser() {

        super(AuthConst.NONE_PROVIDED, AuthConst.NONE_PROVIDED, false, false, false, false, new GrantedAuthority[]{new GrantedAuthorityImpl(AuthConst.ROLE_UNDEFINED)});

        this.locale      = Locale.US;

        this.context     = null;
        this.appAuthPool = null;
        this.appAuth     = null;

    }

    public AuthUser(UserDetails authUserDetails, Locale locale, AuthUserContext context) {

        super(authUserDetails.getUsername(),
                authUserDetails.getPassword(),
                authUserDetails.isEnabled(),
                authUserDetails.isAccountNonExpired(),
                authUserDetails.isCredentialsNonExpired(),
                authUserDetails.isAccountNonLocked(),
                authUserDetails.getAuthorities());

        this.locale      = locale;
        this.context     = context;
        this.appAuthPool = new AppAuthPool(new AppAuth(null, authUserDetails));
        this.appAuth     = getAppAuth(authUserDetails.getUsername());   //

        logger .info("constructor()=> appAuth "+ this.appAuth);
        logger .info("constructor()=> authorities "+ getAuthorities());
    }

    public String getModByName() {
        return appAuth.getUsername();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return appAuth.getAuthorities();
    }

    @Override
    public String getPassword() {
        return appAuth.getPassword();
    }

    @Override
    public String getUsername() {
        return appAuth.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return appAuth.isEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return appAuth.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return appAuth.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return appAuth.isCredentialsNonExpired();
    }

    @Override
    public Locale getAuthUserLocale() {
        return locale;
    }

    @Override
    public AuthUserContext getAuthUserContext() {
        return context;
    }

    @Override
    public AppUser getAppUser() {
        return appAuth == null ? null : appAuth.getUser();
    }

    @Override
    public AppUser getAppUser(String name) {
        AppAuth appAuth = getAppAuth(name);
        return appAuth == null ? null : appAuth.getUser();
    }

    @Override
    public UserDetails getAuthUserDetails() {

        return new User(
                super.getUsername(),
                super.getPassword(),
                super.isEnabled(),
                super.isAccountNonExpired(),
                super.isCredentialsNonExpired(),
                super.isAccountNonLocked(),
                super.getAuthorities()
        );
    }


    public UserDetails getAppUserDetails() {

        return new User(
                getUsername(),
                getPassword(),
                isEnabled(),
                isAccountNonExpired(),
                isCredentialsNonExpired(),
                isAccountNonLocked(),
                getAuthorities()

        );
    }

    private AppAuth getAppAuth(String username) {
        return appAuthPool.get(username);
    }

    public void setAppAuth(String userName, AppAuth appAuth) {
        this.appAuthPool.put(userName, appAuth);
        this.appAuth = getAppAuth(userName);
    }


}
