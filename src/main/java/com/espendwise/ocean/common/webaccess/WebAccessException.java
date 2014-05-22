package com.espendwise.ocean.common.webaccess;


import java.util.List;

public class WebAccessException extends RuntimeException {

    private static final String WEB_ACCESS_ERROR = "An error is occurred during the execution of ";

    private List<ResponseError> errors;
    private String url;
    private int status;

    public WebAccessException(String url, int status, List<ResponseError> errors) {
        super(WEB_ACCESS_ERROR + url);
        this.url = url;
        this.status = status;
        this.errors = errors;
    }

    public List<ResponseError> getErrors() {
        return errors;
    }

    public int getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }
}
