package com.espendwise.manta.util.arguments;

import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

import java.util.Date;

public class TimeArgument extends DateArgument {

    public TimeArgument(Date c, ArgumentResolver<Date> resolver) {
        super(c, resolver, ArgumentType.TIME);
    }

    public TimeArgument(Date c) {
        super(c, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.TIME, Date.class), ArgumentType.TIME);
    }

}