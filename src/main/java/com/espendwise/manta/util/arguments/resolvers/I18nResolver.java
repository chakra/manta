package com.espendwise.manta.util.arguments.resolvers;


import com.espendwise.manta.util.alert.AppLocale;

public interface I18nResolver<T> {
    public String resolve(AppLocale locale, T obj);
}
