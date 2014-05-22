package com.espendwise.manta.util.parser;


public class LongParser implements AppParser<Long> {

    public static LongParser instance = new LongParser();

    public LongParser() {
    }

    @Override
    public Long parse(String parserValue, Object... params) throws AppParserException {
        try {
            return Long.parseLong(parserValue);
        } catch (NumberFormatException e) {
            throw new AppParserException(parserValue, e);
        }
    }

    public static LongParser getInstance() {
        return instance;
    }
}
