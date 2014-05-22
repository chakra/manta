package com.espendwise.manta.util.arguments.resolvers;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.ArgumentResolver;

public class ObjectI18nResolver implements ArgumentResolver<Object> {

    @Override
    public String resolve(AppLocale locale, Object obj) {
        return obj != null ? obj.toString() : Constants.EMPTY;
    }

    @Override
    public String resolve(Object obj) {
        return obj != null ? obj.toString() : Constants.EMPTY;
    }
}
