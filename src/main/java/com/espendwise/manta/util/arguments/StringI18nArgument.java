package com.espendwise.manta.util.arguments;


import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

public class StringI18nArgument extends I18nArgument<String> {

    public StringI18nArgument(String c, ArgumentResolver<String> stringArgumentResolver) {
        super(c, stringArgumentResolver, ArgumentType.I18N_STRING);
    }

    public StringI18nArgument(String c, String defaultValue, ArgumentResolver<String> stringArgumentResolver, ArgumentType argumentType) {
        super(c, defaultValue, stringArgumentResolver, argumentType);
    }

    public StringI18nArgument(String c) {
        super(c, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.I18N_STRING, String.class), ArgumentType.I18N_STRING);
    }

    public StringI18nArgument(String c, String defaultValue) {
        super(c, defaultValue, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.I18N_STRING, String.class), ArgumentType.I18N_STRING);
    }


}