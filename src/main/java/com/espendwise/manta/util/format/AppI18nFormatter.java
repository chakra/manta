package com.espendwise.manta.util.format;


import com.espendwise.manta.util.alert.*;
import com.espendwise.manta.util.arguments.ArgumentResolver;
import com.espendwise.manta.util.arguments.ArgumentType;
import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

import java.math.BigDecimal;
import java.util.Date;

public class AppI18nFormatter extends AbstractAppFormatter {

    private AppLocale locale;

    public AppI18nFormatter(AppLocale locale) {
        this.locale = locale;
    }

    public String formatDate(Date date) {
        ArgumentResolver<Date> resolver = ResolverFactory.getFactory().getArgumentResolver(ArgumentType.DATE, Date.class);
        return resolver.resolve(getLocale(), date);
    }

    @Override
    public String formatTime(Date date) {
        ArgumentResolver<Date> resolver = ResolverFactory.getFactory().getArgumentResolver(ArgumentType.TIME, Date.class);
        return resolver.resolve(getLocale(), date);
    }

    public String formatMonthDay(Date date) {
        ArgumentResolver<Date> resolver = ResolverFactory.getFactory().getArgumentResolver(ArgumentType.MONTH_WITH_DAY, Date.class);
        return resolver.resolve(getLocale(), date);
    }

    public String formatCurrency(BigDecimal currency) {
        ArgumentResolver<BigDecimal> resolver = ResolverFactory.getFactory().getArgumentResolver(ArgumentType.CURRENCY, BigDecimal.class);
        return resolver.resolve(getLocale(), currency);
    }

    public String formatNumber(Number number) {
        ArgumentResolver<Number> resolver = ResolverFactory.getFactory().getArgumentResolver(ArgumentType.NUMBER, Number.class);
        return resolver.resolve(getLocale(), number);
    }

    public String formatInteger(Integer number) {
        ArgumentResolver<Number> resolver = ResolverFactory.getFactory().getArgumentResolver(ArgumentType.NUMBER, Number.class);
        return getLocale() == null ? resolver.resolve(number) : resolver.resolve(getLocale(), number);
    }

    public String formatString(String string) {
        ArgumentResolver<String> resolver = ResolverFactory.getFactory().getArgumentResolver(ArgumentType.I18N_STRING, String.class);
        return resolver.resolve(getLocale(), string);
    }

    public String formatObject(Object object) {
        ArgumentResolver<Object> resolver = ResolverFactory.getFactory().getArgumentResolver(ArgumentType.OBJECT, Object.class);
        return resolver.resolve(getLocale(), object);
    }

    public static String formatDate(Date date, AppLocale locale) {
        return new AppI18nFormatter(locale).formatDate(date);
    }

    public static String formatTime(Date date, AppLocale locale) {
        return new AppI18nFormatter(locale).formatTime(date);
    }


    public static String formatMonthDay(Date date, AppLocale locale) {
        return new AppI18nFormatter(locale).formatMonthDay(date);
    }

    public static String formatCurrency(BigDecimal currency, AppLocale locale) {
        return new AppI18nFormatter(locale).formatCurrency(currency);
    }

    public static String formatNumber(Number number, AppLocale locale) {
        return new AppI18nFormatter(locale).formatNumber(number);
    }

    public static String formatInteger(Integer number, AppLocale locale) {
        return new AppI18nFormatter(locale).formatInteger(number);
    }

    public String formatString(String string, AppLocale locale) {
        return new AppI18nFormatter(locale).formatString(string);
    }

    public String formatObject(Object object, AppLocale locale) {
        return new AppI18nFormatter(locale).formatObject(object);
    }

    public AppLocale getLocale() {
        return locale;
    }
}
