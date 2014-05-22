package com.espendwise.manta.auth;


import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.TypedArgument;

public class AuthenticationAccessTokenException  extends org.springframework.security.core.AuthenticationException  {

    private TypedArgument[] excArgs;

    public AuthenticationAccessTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationAccessTokenException(String msg) {
        super(msg);
    }

    public AuthenticationAccessTokenException(String msg, Object... extraInformation) {
        super(msg);
        this.excArgs = Args.typed(extraInformation);
    }


    public TypedArgument[] getExcArgs() {
        return excArgs;
    }
}
