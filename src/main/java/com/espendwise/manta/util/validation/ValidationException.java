package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ApplicationReason;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;

public class ValidationException extends ApplicationRuntimeException {

    public ValidationException(ApplicationReason excReason, TypedArgument... args) {
        super(new ApplicationExceptionCode<ApplicationReason>(excReason, args));
    }

    public ValidationException(ApplicationExceptionCode... exceptionCodes) {
        super(exceptionCodes);
    }

}
