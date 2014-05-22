package com.espendwise.manta.util.arguments;

import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

public class StringArgument extends AbstractArgument<String> {

    public StringArgument(String c, ArgumentResolver<String> stringArgumentResolver) {
        super(c, stringArgumentResolver, ArgumentType.I18N_STRING);
    }

    public StringArgument(String c, String defaultValue, ArgumentResolver<String> stringArgumentResolver, ArgumentType argumentType) {
        super(c, defaultValue, stringArgumentResolver, argumentType);
    }

    public StringArgument(String c) {
        super(c, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.STRING, String.class), ArgumentType.STRING);
    }

    public StringArgument(String c, String defaultValue) {
        super(c, defaultValue, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.STRING, String.class), ArgumentType.STRING);
    }


}