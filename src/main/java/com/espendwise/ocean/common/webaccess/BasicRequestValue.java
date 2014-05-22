package com.espendwise.ocean.common.webaccess;


public class BasicRequestValue<T> extends CredentialRequestValue {

    private T object;

    public BasicRequestValue() {
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
