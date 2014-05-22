package com.espendwise.manta.util.arguments;


import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

import java.util.Date;

public class DateArgument extends I18nArgument<Date> {

    public DateArgument(Date c, ArgumentResolver<Date> dateArgumentResolver, ArgumentType argumentType) {
        super(c, dateArgumentResolver, argumentType);
    }

    public DateArgument(Date c, String defaultValue, ArgumentResolver<Date> dateArgumentResolver, ArgumentType argumentType) {
        super(c, defaultValue, dateArgumentResolver, argumentType);
    }

    public DateArgument(Date c, ArgumentResolver<Date> resolver) {
        super(c, resolver, ArgumentType.DATE);
    }

    public DateArgument(Date c) {
        super(c, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.DATE, Date.class), ArgumentType.DATE);
    }

}
