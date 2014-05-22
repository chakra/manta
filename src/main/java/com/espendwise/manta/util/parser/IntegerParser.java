package com.espendwise.manta.util.parser;


public class IntegerParser implements AppParser<Integer> {

    public IntegerParser() {
    }

    @Override
    public Integer parse(String parserValue, Object... params) throws AppParserException {
        try {
            return Integer.parseInt(parserValue);
        } catch (NumberFormatException e) {
            throw new AppParserException(parserValue, e);
        }
    }
}
