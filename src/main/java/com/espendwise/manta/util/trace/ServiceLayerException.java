package com.espendwise.manta.util.trace;


import java.util.Arrays;

public class ServiceLayerException extends Exception {
  
    private ApplicationExceptionCode[] exceptionCodes;

    public ServiceLayerException(ApplicationExceptionCode... exceptionCodes) {
        this.exceptionCodes = exceptionCodes;
    }

    public ServiceLayerException(String message, ApplicationExceptionCode... exceptionCodes) {
        super(message);
        this.exceptionCodes = exceptionCodes;
    }

    public ServiceLayerException(String message, Throwable cause, ApplicationExceptionCode... exceptionCodes) {
        super(message, cause);
        this.exceptionCodes = exceptionCodes;
    }

    public ServiceLayerException(Throwable cause, ApplicationExceptionCode... exceptionCodes) {
        super(cause);
        this.exceptionCodes = exceptionCodes;
    }

    public ApplicationExceptionCode[] getExceptionCodes() {
        return exceptionCodes;
    }

    @Override
    public String toString() {
        return "ServiceLayerException{" +
                "exceptionCodes=" + (exceptionCodes == null ? null : Arrays.asList(exceptionCodes)) +
                '}';
    }
}
