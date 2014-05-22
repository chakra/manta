package com.espendwise.manta.util.arguments.resolvers;


import com.espendwise.manta.i18n.I18nResource;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.ArgumentResolver;
import org.apache.log4j.Logger;

public class StringI18nResolver  implements ArgumentResolver<String> {

    private static final Logger logger = Logger.getLogger(StringI18nResolver.class);

    @Override
    public String resolve(AppLocale locale, String obj) {
        String result = I18nResource.getResource().getMessage(obj, locale);
        return Utility.strNN(result, obj) ;
    }

    @Override
    public String resolve(String obj) {
        return Utility.strNN(obj) ;
    }
}

