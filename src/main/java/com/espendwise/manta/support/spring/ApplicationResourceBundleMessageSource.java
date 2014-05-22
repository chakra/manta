package com.espendwise.manta.support.spring;


import com.espendwise.manta.i18n.I18nResource;
import com.espendwise.manta.i18n.MessageResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;

public class ApplicationResourceBundleMessageSource implements MessageSource {

    private static Log logger = LogFactory.getLog(ApplicationResourceBundleMessageSource.class);

    public MessageResource getMessageResource() {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        MessageResource resoource;

        if (securityContext != null
                && securityContext.getAuthentication() != null
                && securityContext.getAuthentication().getPrincipal() != null
                && securityContext.getAuthentication().isAuthenticated()) {

            logger.debug("getMessageResource()=> getting message resource for user - " + securityContext.getAuthentication().getPrincipal());

            resoource = I18nResource.getUserResource();

        } else {

            logger.debug("getMessageResource()=> getting base message resource");

            resoource = I18nResource.getResource();

        }

        logger.debug("getMessageResource()=> resoource " + resoource);

        return resoource;

    }


    @Override
    public String getMessage(String s, Object[] objects, String s1, Locale locale) {
        return getMessageResource().getMessage(s, objects, s1, locale);
    }

    @Override
    public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
        return getMessageResource().getMessage(s, objects, locale);
    }


    @Override
    public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {

        String[] codes = messageSourceResolvable.getCodes();

        if (codes != null) {

            for (String code : codes) {

                String message = getMessage(
                        code,
                        messageSourceResolvable.getArguments(),
                        messageSourceResolvable.getDefaultMessage(),
                        locale
                );

                if (message != null) {
                    return message;
                }

            }
        }

        return messageSourceResolvable.getDefaultMessage();

    }

}
