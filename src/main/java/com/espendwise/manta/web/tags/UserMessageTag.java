package com.espendwise.manta.web.tags;


import com.espendwise.manta.i18n.I18nResource;
import com.espendwise.manta.i18n.MessageResource;
import com.espendwise.manta.support.spring.ApplicationResourceBundleMessageSource;
import org.springframework.context.MessageSource;

public class UserMessageTag extends org.springframework.web.servlet.tags.MessageTag {

   @Override
    protected MessageSource getMessageSource() {
        return new ApplicationResourceBundleMessageSource() {
            @Override
            public MessageResource getMessageResource() {
                return I18nResource.getUserResource();
            }
        } ;
    }


}
