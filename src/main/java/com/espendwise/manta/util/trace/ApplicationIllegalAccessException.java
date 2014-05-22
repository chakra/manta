package com.espendwise.manta.util.trace;


import com.espendwise.manta.util.arguments.TypedArgument;

public class ApplicationIllegalAccessException extends ApplicationRuntimeException {

    public ApplicationIllegalAccessException(ExceptionReason.SystemReason excReason, TypedArgument... args) {
        super(new ApplicationExceptionCode<ExceptionReason.SystemReason>(excReason, args));
    }

}
