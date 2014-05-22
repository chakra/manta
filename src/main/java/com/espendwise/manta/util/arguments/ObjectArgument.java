package com.espendwise.manta.util.arguments;


import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

public class ObjectArgument<T> extends I18nArgument<T> {

    public ObjectArgument(T c, ArgumentResolver<T> resolver) {
        super(c, resolver, ArgumentType.OBJECT);
    }

    public ObjectArgument(T c) {
        super(c, (ArgumentResolver<T>) ResolverFactory.getFactory().getArgumentResolver(ArgumentType.OBJECT, c.getClass()),
                ArgumentType.OBJECT);
    }
}
