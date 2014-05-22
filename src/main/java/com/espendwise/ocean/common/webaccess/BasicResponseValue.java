package com.espendwise.ocean.common.webaccess;


import java.io.Serializable;
import java.util.List;

public class BasicResponseValue<T> implements Serializable {

    public static interface STATUS {
        public static final int OK = 1;
        public static final int EXCEPTION = 2;
        public static final int ERROR = 3;
        public static final int ACCESS_DENIED = 4;
        public static final int ACCESS_TOKEN_EXPIRED = 5;
    }

    private List<ResponseError> errors;
    private int status;
    private T object;

    public BasicResponseValue(T object, int status, List<ResponseError> errors) {
        this.object = object;
        this.status = status;
        this.errors = errors;
    }

    public BasicResponseValue() {
    }


    public List<ResponseError> getErrors() {
        return errors;
    }

    public void setErrors(List<ResponseError> errors) {
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "BasicResponseValue{" +
                "errors=" + errors +
                ", status=" + status +
                ", object=" + object +
                '}';
    }
}
