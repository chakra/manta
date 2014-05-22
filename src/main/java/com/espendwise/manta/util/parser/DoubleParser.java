package com.espendwise.manta.util.parser;


public class DoubleParser implements AppParser<Double> {

    public static DoubleParser instance = new DoubleParser();

    public DoubleParser() {
    }

    @Override
    public Double parse(String parserValue, Object... params) throws AppParserException {
        try {
            return Double.parseDouble(parserValue);
        } catch (NumberFormatException e) {
            throw new AppParserException(parserValue, e);
        }
    }

    public static DoubleParser getInstance() {
        return instance;
    }
}
