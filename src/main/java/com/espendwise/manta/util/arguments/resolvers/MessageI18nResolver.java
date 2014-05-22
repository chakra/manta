package com.espendwise.manta.util.arguments.resolvers;

import com.espendwise.manta.i18n.I18nResource;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.ArgumentResolver;
import org.apache.log4j.Logger;


public class MessageI18nResolver  implements ArgumentResolver<ArgumentedMessage> {

    private static final Logger logger = Logger.getLogger(MessageI18nResolver.class);

    @Override
    public String resolve(AppLocale locale, ArgumentedMessage obj) {
        String result = I18nResource.getResource().getMessage(obj.getKey(),
                obj.getArguments(),
                obj.getDefaultMessage(),
                locale.getLocale()
        );
        return Utility.strNN(result) ;
    }

    @Override
    public String resolve(ArgumentedMessage obj) {
        String result = I18nResource.getResource().getMessage(obj.getKey(),
                obj.getArguments(),
                obj.getDefaultMessage(),
                null
        );
        return Utility.strNN(result) ;
    }
}

