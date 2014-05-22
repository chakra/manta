package com.espendwise.manta.auth;


import org.springframework.security.core.Authentication;

import javax.annotation.Resource;

@Resource(mappedName="authenticationHolder")
public class AuthenticationHolder {

    public AuthUser getAuthUser() {  return  Auth.getAuthUser(); }

    public Authentication getAuthentication() { return Auth.getAuthentication();  }

    public ApplicationDataSource getApplicationDataSource() {  return Auth.getApplicationDataSource(); }

}