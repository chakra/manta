package com.espendwise.manta.util.arguments;


import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

import java.math.BigDecimal;
import java.util.Date;

public class ArgumentTypeFactory {

    private static ArgumentTypeFactory ourInstance = new ArgumentTypeFactory();


    public static ArgumentTypeFactory getFactory() {
        return ourInstance;
    }

    private ArgumentTypeFactory() {
    }

    public TypedArgument getArgImp(Object obj, ArgumentType type) {
        switch (type) {
            case DATE : return new DateArgument((Date) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.DATE, Date.class));
            case TIME : return new TimeArgument((Date) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.TIME, Date.class));
            case MONTH_WITH_DAY : return new MonthWithDayArgument((Date) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.MONTH_WITH_DAY, Date.class));
            case NUMBER: return new NumberArgument((Number) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.NUMBER, Number.class));
            case CURRENCY: return new CurrencyArgument((BigDecimal) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.CURRENCY, BigDecimal.class));
            case OBJECT: return new ObjectArgument((Object) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.OBJECT, Object.class));
            case I18N_STRING: return new StringI18nArgument((String) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.I18N_STRING,String.class));
            case STRING : return new StringArgument((String) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.STRING,String.class));
            default:  return new ObjectArgument((Object) obj, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.OBJECT, Object.class));
        }
    }

    public TypedArgument getArgImp(Object obj) {
          if (obj instanceof Number) {  return getArgImp(obj, ArgumentType.NUMBER);
        } else if (obj instanceof Date) { return getArgImp(obj, ArgumentType.DATE);
        } else if (obj instanceof String) { return getArgImp(obj, ArgumentType.STRING);
        } else { return getArgImp(obj, ArgumentType.OBJECT); }
    }

    public TypedArgument getI18nArgImp(Object obj) {
        if (obj instanceof Number) {  return getArgImp(obj, ArgumentType.NUMBER);
        } else if (obj instanceof Date) { return getArgImp(obj, ArgumentType.DATE);
        } else if (obj instanceof String) { return getArgImp(obj, ArgumentType.I18N_STRING);
        } else { return getArgImp(obj, ArgumentType.OBJECT); }
    }
}
