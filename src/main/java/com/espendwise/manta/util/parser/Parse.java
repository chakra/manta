package com.espendwise.manta.util.parser;


import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;

import java.math.BigDecimal;
import java.util.Date;

public class Parse {

    public static Integer parseInt(String value) {
        return  AppParserFactory.getParser(Integer.class).parse(value);
    }

    public static Date parseDate(String value, AppLocale locale) {
        return AppParserFactory.getParser(Date.class).parse(value, I18nUtil.getDatePattern(locale));
    }

    public static Date parseTime(String value, String pattern) {
        return AppParserFactory.getParser(Date.class).parse(value, pattern);
    }

    public static Date parseDateNN(String value, AppLocale locale) {
        String s = Utility.ignorePattern(value, I18nUtil.getDatePatternPrompt(locale));
        return Utility.isSet(s) ? AppParserFactory.getParser(Date.class).parse(s, I18nUtil.getDatePattern(locale)) : null;
    }

    public static Date parseDate(String value, String pattern) {
        return AppParserFactory.getParser(Date.class).parse(value, pattern);
    }

    public static Date parseMonthWithDay(String value, Integer year, AppLocale locale) {
        return parseMonthWithDay(value, year,  I18nUtil.getDayWithMonthPattern(locale));
    }

    public static Date parseMonthWithDay(String value, Integer year, String mothWithDayPattern) {
        return AppParserFactory.getMonthWithDayParser().parse(value, year, mothWithDayPattern);
    }

    public static Integer parsePercentInt(String percent) {
        return AppParserFactory.getPercentParserInt().parse(percent);
    }

    public static BigDecimal parseAmount(AppLocale locale, String value) {
        return AppParserFactory.getAmountParser().parse(value, locale.getLocale());
    }


    public static Long parseLong(String value) {
        return  AppParserFactory.getParser(Long.class).parse(value);
    }

    public static Long parseLong(String value, boolean nullIfExc) {
        try {
            return AppParserFactory.getParser(Long.class).parse(value);
        } catch (AppParserException e) {
            if (nullIfExc) {
                return null;
            }
            throw e;
        }
    }

}
