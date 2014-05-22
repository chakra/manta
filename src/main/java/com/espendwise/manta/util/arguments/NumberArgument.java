package com.espendwise.manta.util.arguments;


import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

public class NumberArgument extends I18nArgument<Number> {

    public NumberArgument(Number c, ArgumentResolver<Number> resolver) {
        super(c, resolver, ArgumentType.NUMBER);
    }

    public NumberArgument(Number c) {
        super(c, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.NUMBER, Number.class), ArgumentType.NUMBER);
    }

}
