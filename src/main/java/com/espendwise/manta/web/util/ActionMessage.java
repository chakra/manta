package com.espendwise.manta.web.util;


import com.espendwise.manta.i18n.I18nResource;
import com.espendwise.manta.i18n.MessageResource;
import com.espendwise.manta.support.spring.ApplicationResourceBundleMessageSource;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.AbstractTypedMessage;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.arguments.TypedArgument;
import org.springframework.context.MessageSource;

public class ActionMessage extends AbstractTypedMessage implements DisplayMessage<ActionMessage> {

    public ActionMessage(MessageType type, String key, TypedArgument[] arguments, String defaultMessage) {
        super(type, key, defaultMessage, arguments);
    }

    private MessageSource getMessageSource() {
        return new ApplicationResourceBundleMessageSource() {
            @Override
            public MessageResource getMessageResource() {
                return I18nResource.getUserResource();
            }
        };
    }

    @Override
    public String resolve(AppLocale locale) {
        return getMessageSource().getMessage(this, locale.getLocale());
    }

    public String resolve() {
        return getMessageSource().getMessage(this, Constants.DEFAULT_LOCALE);
    }

}
