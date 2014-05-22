package com.espendwise.manta.util.arguments;

public class I18nArgument<T> extends AbstractI18nArgument<T> {

    public I18nArgument(T c, ArgumentResolver<T> tArgumentResolver, ArgumentType argumentType) {
        super(c, tArgumentResolver, argumentType);
    }

    public I18nArgument(T c, String defaultValue, ArgumentResolver<T> tArgumentResolver, ArgumentType argumentType) {
        super(c, defaultValue, tArgumentResolver, argumentType);
    }
}
