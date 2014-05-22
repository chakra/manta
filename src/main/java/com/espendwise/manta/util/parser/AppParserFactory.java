package com.espendwise.manta.util.parser;


import org.apache.log4j.Logger;

import java.util.Date;

public class AppParserFactory {

    private static final Logger logger = Logger.getLogger(AppParserFactory.class);


    public static <T> AppParser<T> getParser(Class<T> type) {

        if (Integer.class.isAssignableFrom(type)) {
            return (AppParser<T>) getIntegerParser();
        } else if (Long.class.isAssignableFrom(type)) {
            return (AppParser<T>) getLongParser();
        } else if (Date.class.isAssignableFrom(type)) {
            return (AppParser<T>) getDateParser();
        } else if (Double.class.isAssignableFrom(type)) {
        	return (AppParser<T>) getDoubleParser();
        }

        return null;
    }

    public static MonthWithDayParser getMonthWithDayParser() {
        return new MonthWithDayParser();
    }

    public static DateParser getDateParser() {
        return new DateParser();
    }

    public static LongParser getLongParser() {
        return new LongParser();
    }

    public static DoubleParser getDoubleParser() {
        return new DoubleParser();
    }

    public static IntegerParser getIntegerParser() {
        return new IntegerParser();
    }

    public static PercentParserInt getPercentParserInt() {
        return new PercentParserInt();
    }

    public static AmountParser getAmountParser() {
        return new AmountParser();
    }
}
