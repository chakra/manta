package com.espendwise.manta.util.trace;

import java.util.Arrays;

public class ApplicationRuntimeException extends RuntimeException {

    private ApplicationExceptionCode[] exceptionCodes;

    public ApplicationRuntimeException(ApplicationExceptionCode... exceptionCodes) {
        this.exceptionCodes = exceptionCodes;
    }

    public ApplicationRuntimeException(String message, ApplicationExceptionCode... exceptionCodes) {
        super(message);
        this.exceptionCodes = exceptionCodes;
    }

    public ApplicationRuntimeException(String message, Throwable cause, ApplicationExceptionCode... exceptionCodes) {
        super(message, cause);
        this.exceptionCodes = exceptionCodes;
    }

    public ApplicationRuntimeException(Throwable cause, ApplicationExceptionCode... exceptionCodes) {
        super(cause);
        this.exceptionCodes = exceptionCodes;
    }

    public ApplicationExceptionCode[] getExceptionCodes() {
        return exceptionCodes;
    }

    @Override
    public String toString() {
        return "ApplicationRuntimeException{" +
                "exceptionCodes=" + (exceptionCodes == null ? null : Arrays.asList(exceptionCodes)) +
                '}';
    }
}
