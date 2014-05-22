package com.espendwise.manta.auth;


import com.espendwise.manta.util.trace.ApplicationRuntimeException;

public interface LogonService {

    public AppUser logon(String userName, LogonRequest request) throws ApplicationRuntimeException;

}
