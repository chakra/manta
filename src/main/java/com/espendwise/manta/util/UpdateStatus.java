package com.espendwise.manta.util;


import com.espendwise.manta.util.alert.ArgumentedMessage;

import java.util.List;

public class UpdateStatus<T> {
    
    public static String FAILED = "FAILED";
    public static String OK = "OK ";

    private String updateStatus;
    private T object;
    private List<? extends ArgumentedMessage> errors;

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public List<? extends ArgumentedMessage> getErrors() {
        return errors;
    }

    public boolean isFailed() {
        return FAILED.equals(getUpdateStatus());
    }
}
