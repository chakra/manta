package com.espendwise.manta.web.util;


import java.io.Serializable;

public class AsynchError implements Serializable {

    private String errorTitle;
    private String errorComponent;
    private String[] errorMessages;


    public AsynchError() {
    }

    public AsynchError(String errorTitle, String... errorMessages) {
        this.errorTitle = errorTitle;
        this.errorMessages = errorMessages;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorComponent() {
        return errorComponent;
    }

    public void setErrorComponent(String errorComponent) {
        this.errorComponent = errorComponent;
    }

    public String[] getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String[] errorMessages) {
        this.errorMessages = errorMessages;
    }
}
