package com.espendwise.manta.util.arguments;


import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.resolvers.I18nResolver;
import com.espendwise.manta.spi.MessageResolver;

public interface ArgumentResolver<T> extends MessageResolver<T>,I18nResolver<T> {
    String resolve(AppLocale locale, T obj);


}
