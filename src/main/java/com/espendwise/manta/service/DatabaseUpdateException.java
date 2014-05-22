package com.espendwise.manta.service;


import com.espendwise.manta.util.trace.ApplicationReason;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.trace.ServiceLayerException;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class DatabaseUpdateException extends ServiceLayerException {

    public DatabaseUpdateException(String message, Throwable cause, ApplicationReason excReason, TypedArgument... args) {
        super(message, cause, new ApplicationExceptionCode<ApplicationReason>(excReason, args));
    }

    public DatabaseUpdateException(ApplicationExceptionCode... exceptionCodes) {
        super(exceptionCodes);
    }

    public DatabaseUpdateException(String message, Throwable cause, ApplicationExceptionCode... exceptionCodes) {
        super(message, cause, exceptionCodes);
    }

    public DatabaseUpdateException(Throwable cause, ApplicationExceptionCode... exceptionCodes) {
        super(cause, exceptionCodes);
    }

    public DatabaseUpdateException(String message, ApplicationExceptionCode... exceptionCodes) {
        super(message, exceptionCodes);
    }
}
