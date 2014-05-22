package com.espendwise.ocean.common.webaccess;


import java.io.Serializable;
import java.util.Map;

public class ResponseError implements Serializable {

    private String key;
    private String message;
    private String excClassName;
    private Object[] args;
    private Map<String, String> errorProperties;

    public ResponseError(String message, String excClassName, Map<String, String> errorProperties, String key, Object[] args) {
        this.message = message;
        this.excClassName = excClassName;
        this.errorProperties = errorProperties;
        this.key = key;
        this.args = args;
    }

    public ResponseError(String message, String excClassName, Map<String, String> errorProperties) {
        this.message = message;
        this.excClassName = excClassName;
        this.errorProperties = errorProperties;
    }

    public ResponseError(String message, String excClassName) {
        this.message = message;
        this.excClassName = excClassName;
    }

    public ResponseError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExcClassName() {
        return excClassName;
    }

    public void setExcClassName(String excClassName) {
        this.excClassName = excClassName;
    }

    public Map<String, String> getErrorProperties() {
        return errorProperties;
    }

    public void setErrorProperties(Map<String, String> errorProperties) {
        this.errorProperties = errorProperties;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "message='" + message + '\'' +
                ", key='" + key + '\'' +
                ", excClassName='" + excClassName + '\'' +
                ", errorProperties=" + errorProperties +
                ", args='" + args + '\'' +
                '}';
    }
}
