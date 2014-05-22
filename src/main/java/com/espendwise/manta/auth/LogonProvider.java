package com.espendwise.manta.auth;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface LogonProvider {

  public LogonService getService();

  public boolean isAuthorized(Collection<? extends GrantedAuthority> credentials);

  public LogonRequest createLogonRequest(Authentication auth, HttpServletRequest request);

}
