package com.espendwise.manta.util.arguments;


import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

import java.math.BigDecimal;

public class CurrencyArgument  extends I18nArgument<BigDecimal> {

    public CurrencyArgument(BigDecimal c, ArgumentResolver<BigDecimal> resolver) {
        super(c, resolver, ArgumentType.CURRENCY);
    }

    public CurrencyArgument(BigDecimal c) {
        super(c, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.CURRENCY, BigDecimal.class), ArgumentType.CURRENCY);
    }
}

