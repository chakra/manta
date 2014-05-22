package com.espendwise.manta.util.arguments;

import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.Display18nOutput;

public abstract class AbstractI18nArgument<T> extends AbstractArgument<T> implements TypedArgument<T>, Display18nOutput<T> {


    public AbstractI18nArgument(T c, String defaultValue, ArgumentResolver<T> tArgumentResolver, ArgumentType argumentType) {
        super(c, defaultValue, tArgumentResolver, argumentType);
    }

    public AbstractI18nArgument(T c, ArgumentResolver<T> tArgumentResolver, ArgumentType argumentType) {
        super(c, tArgumentResolver, argumentType);
    }

    public String resolve() {
        String value = resolver.resolve(AppLocale.DEFAULT_LOCALE, get());
        return value == null ? getDefaultValue() : value;
    }

    public String resolve(AppLocale locale) {
        return resolver.resolve(locale, get());
    }

}

