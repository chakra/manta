package com.espendwise.manta.util.arguments.resolvers;


import com.espendwise.manta.util.arguments.ArgumentResolver;
import com.espendwise.manta.util.arguments.ArgumentType;

public class ResolverFactory {

    private static ResolverFactory ourInstance = new ResolverFactory();


    public static ResolverFactory getFactory() {
        return ourInstance;
    }

    private ResolverFactory() {
    }

    public <T> ArgumentResolver<T> getArgumentResolver(ArgumentType type, Class<T> classType) {
        switch (type) {
            case DATE :  return (ArgumentResolver<T>) new DateI18nResolver();
            case TIME:  return (ArgumentResolver<T>) new TimeI18nResolver();
            case MONTH_WITH_DAY:  return (ArgumentResolver<T>) new MonthWithDayI18nResolver();
            case NUMBER:  return (ArgumentResolver<T>) new NumberI18nResolver();
            case CURRENCY:  return (ArgumentResolver<T>) new CurrencyI18nResolver();
            case I18N_STRING:  return (ArgumentResolver<T>) new StringI18nResolver();
            case STRING:  return (ArgumentResolver<T>) new StringI18nResolver();
            case MESSAGE:  return (ArgumentResolver<T>) new MessageI18nResolver();
            case OBJECT:  return (ArgumentResolver<T>) new ObjectI18nResolver();
            default:  return (ArgumentResolver<T>) new ObjectI18nResolver();
        }
    }

}
