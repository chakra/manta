package com.espendwise.manta.util.arguments;

import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

import java.util.Date;

public class MonthWithDayArgument extends I18nArgument<Date>{

    public MonthWithDayArgument(Date c, ArgumentResolver<Date> resolver) {
        super(c, resolver, ArgumentType.MONTH_WITH_DAY);
    }

    public MonthWithDayArgument(Date c) {
        super(c, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.MONTH_WITH_DAY, Date.class), ArgumentType.MONTH_WITH_DAY);
    }
}
