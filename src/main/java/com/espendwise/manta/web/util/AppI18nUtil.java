package com.espendwise.manta.web.util;


import com.espendwise.manta.i18n.I18nResource;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.format.AppFormatter;
import com.espendwise.manta.util.format.AppI18nFormatter;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.AppParserFactory;
import com.espendwise.manta.util.parser.Parse;

import java.math.BigDecimal;
import java.util.Date;

public class AppI18nUtil {

    public static String getMessage(String key, Object[] args) {
        return I18nResource.getUserResource().getMessage(
                key,
                args,
                false
        );
    }

    public static String getMessage(String key) {
        return I18nResource.getUserResource().getMessage(
                key,
                null,
                false
        );
    }

    public static String getMessageOrNull(String key) {
        return I18nResource.getUserResource().getMessage(
                key,
                null,
                true
        );
    }

    public static String getMessageOrDefault(String key, String pDefaultValue) {
        return I18nResource.getUserResource().getMessageOrDefault(
                key,
                pDefaultValue
        );
    }

    public static String getMessage(String key, Object[] args, boolean pReturnNullIfNotFound) {

        return I18nResource.getUserResource().getMessage(
                key,
                args,
                pReturnNullIfNotFound
        );
    }

    public static String getDatePattern() {
        return I18nUtil.getDatePattern();
    }

    public static String getDatePatternPrompt() {
        return I18nUtil.getDatePatternPrompt();
    }

    public static String getTimePatternPrompt() {
        return I18nUtil.getTimePatternPrompt();
    }

    public static String getDayWithMonthPattern() {
        return I18nUtil.getDayWithMonthPattern();
    }

    public static String getDatePickerPattern() {
        return I18nUtil.getDatePickerPattern();
    }

    public static Date parseDate(AppLocale locale, String value) {
        String datePattern = I18nUtil.getDatePattern(locale);
        return AppParserFactory.getParser(Date.class).parse(value,datePattern);
    }

    public static Date parseDateNN(AppLocale locale, String value) {
        return !Utility.isSetIgnorePattern(value, I18nUtil.getDatePatternPrompt(locale))
                ? null
                : parseDate(locale, value);
    }

    public static Date parseTimeNN(AppLocale locale, String value) {
        return !Utility.isSetIgnorePattern(value, I18nUtil.getTimePatternPrompt(locale))
                ? null
                : parseTime(value, I18nUtil.getTimePatternPrompt(locale));
    }

    public static Date parseTime(String value, String pattern) {
        return AppParserFactory.getParser(Date.class).parse(value, pattern);
    }

    public static Long parseNumber(String value) {
        return AppParserFactory.getParser(Long.class).parse(value);
    }

    public static Long parseNumberNN(String value) {
        return !Utility.isSet(value) ? null : parseNumber(value);
    }

    public static String formatDate(AppLocale locale, Date date) {
        AppFormatter formatter = new AppI18nFormatter(locale);
        return date != null ? formatter.formatDate(date) : Constants.EMPTY;
    }

    public static String formatTime(AppLocale locale, Date date) {
        AppFormatter formatter = new AppI18nFormatter(locale);
        return date != null ? formatter.formatTime(date) : Constants.EMPTY;
    }


    public static String formatNumber(AppLocale locale, Number number) {
        AppFormatter formatter = new AppI18nFormatter(locale);
        return number != null ? formatter.formatNumber(number) : Constants.EMPTY;
    }

    public static Date parseMonthWithDay(String str, Integer year) {
        return parseMonthWithDay(str, year, false);
    }
    
    public static Date parseMonthWithDay(String str, Integer year, boolean  nullIfError) {
        try {
            return Parse.parseMonthWithDay(str, year, getDayWithMonthPattern());
        } catch (AppParserException e) {
            if(nullIfError){
                return  null;
            }  else {
                throw e;
            }
        }
    }

    public static BigDecimal parseAmountNN(AppLocale locale, String value) {
        return !Utility.isSet(value)
                ? null
                : parseAmount(locale, value);
    }

    private static BigDecimal parseAmount(AppLocale locale, String value) {
        return Parse.parseAmount(locale, value);
    }
}
