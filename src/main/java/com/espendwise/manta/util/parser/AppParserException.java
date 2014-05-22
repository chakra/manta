package com.espendwise.manta.util.parser;


public class AppParserException  extends RuntimeException {

    private String parserValue;

    public AppParserException(String parserValue,Throwable cause) {
        super(cause);
        this.parserValue = parserValue;
    }

    public AppParserException(String message, String parserValue) {
        super(message);
        this.parserValue = parserValue;
    }

    public String getParserValue() {
        return parserValue;
    }
}

