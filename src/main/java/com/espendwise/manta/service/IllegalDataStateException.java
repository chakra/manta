package com.espendwise.manta.service;


import com.espendwise.manta.util.trace.ServiceLayerException;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class IllegalDataStateException extends ServiceLayerException {

    public IllegalDataStateException(ApplicationExceptionCode... exceptionCodes) {
        super(exceptionCodes);
    }

    public IllegalDataStateException(String message, ApplicationExceptionCode... exceptionCodes) {
        super(message, exceptionCodes);
    }

    public IllegalDataStateException(Throwable cause, ApplicationExceptionCode... exceptionCodes) {
        super(cause, exceptionCodes);
    }

    public IllegalDataStateException(String message, Throwable cause, ApplicationExceptionCode... exceptionCodes) {
        super(message, cause, exceptionCodes);
    }
}
