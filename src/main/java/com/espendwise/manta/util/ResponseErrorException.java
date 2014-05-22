package com.espendwise.manta.util;

import com.espendwise.ocean.common.webaccess.ResponseError;

public class ResponseErrorException extends RuntimeException {
    private ResponseError error;

    public ResponseErrorException(ResponseError error) {
        this.error = error;
    }

    public ResponseError getError() {
        return error;
    }
}
