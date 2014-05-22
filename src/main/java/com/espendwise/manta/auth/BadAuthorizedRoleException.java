package com.espendwise.manta.auth;


public class BadAuthorizedRoleException extends org.springframework.security.core.AuthenticationException {

    public BadAuthorizedRoleException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadAuthorizedRoleException(String msg) {
        super(msg);
    }

    public BadAuthorizedRoleException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }

}