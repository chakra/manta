package com.espendwise.manta.util.arguments.resolvers;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.ArgumentResolver;

public class NumberI18nResolver implements ArgumentResolver<Number> {

    @Override
    public String resolve(AppLocale locale, Number obj) {
        return obj!=null ? obj.toString(): Constants.EMPTY ;
    }

    @Override
    public String resolve(Number obj) {
        return obj!=null ? obj.toString(): Constants.EMPTY ;
    }
}
