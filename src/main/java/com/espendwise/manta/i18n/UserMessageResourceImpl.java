package com.espendwise.manta.i18n;


import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.AuthUser;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.ResourceNames;
import com.espendwise.manta.util.alert.AppLocale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Locale;


@Resource(mappedName = ResourceNames.USER_MESSAGE_RESOURCE)
public class UserMessageResourceImpl implements UserMessageResource {

    private static Log logger = LogFactory.getLog(UserMessageResourceImpl.class);

    @Autowired
    protected AuthUser authUser;

    private MessageResourceImpl messageResource;


    public UserMessageResourceImpl(MessageResourceImpl messageResource) {
        this.messageResource = messageResource;
    }

    private AuthUser getAuthUser() {
        return authUser;
    }

    private MessageResourceImpl getMessageResource() {
        return messageResource;
    }

    @Override
    public String getMessage(String key, Object[] args, Locale locale) {
        return getMessage(getAuthUser(),
                key,
                args,
                locale,
                false
        );
    }

    @Override
    public String getMessage(String key, Locale locale) {
        return getMessage(getAuthUser(),
                key,
                null,
                locale,
                false
        );
    }

    @Override
    public String getMessage(String key, AppLocale locale) {
        return getMessage(getAuthUser(),
                key,
                null,
                locale == null ? null : locale.getLocale(),
                false
        );
    }

    @Override
    public String getMessage(String key, Object[] args) {
        return getMessage(key,
                args,
                false
        );
    }

    @Override
    public String getMessage(String key) {
        return getMessage(key,
                null,
                false
        );
    }

    @Override
    public String getMessageOrNull(String key) {
        return getMessage(key,
                null,
                true
        );
    }

    @Override
    public String getMessageOrDefault(String key, String pDefaultValue) {
        String message = getMessageOrNull(key);
        return message == null ? pDefaultValue : message;
    }

    @Override
    public String getMessage(String key, Object[] args, boolean returnNullIfNotFound) {
        return getMessage(getAuthUser(),
                key,
                args,
                null,
                returnNullIfNotFound
        );
    }

    @Override
    public String getMessage(String key, Object[] args, boolean returnNullIfNotFound, Locale locale) {
        return getMessage(getAuthUser(),
                key,
                args,
                locale,
                returnNullIfNotFound
        );
    }

    @Override
    public String getMessage(String key, Object[] args, String defMessage, Locale locale) {

        String message = getMessage(getAuthUser(), key, args, locale, true);
        String resolvedDefMessage = getMessage(getAuthUser(), defMessage, args, locale, true);
        return message != null ? message : resolvedDefMessage != null ? resolvedDefMessage : defMessage;

    }

    public String getMessage(AuthUser authUser, String key, Object[] args, Locale preferedLocale, boolean returnNullIfNotFound) {

        return getMessage(authUser == null ? null : authUser.getAppUser(),
                key,
                args,
                preferedLocale,
                returnNullIfNotFound
        );

    }

    public String getMessage(AppUser appUser, String key, Object[] args, Locale preferedLocale, boolean returnNullIfNotFound) {

        logger.debug("getMessage()=> BEGIN");

        String message;

        if (args == null) {
            args = new Object[0];
        }

        try {

            Locale locale;

            if (preferedLocale == null) {
                if (appUser == null) {
                    locale = authUser == null ? Locale.US : authUser.getAuthUserLocale();
                } else {
                    locale = appUser.getLocale();
                }
            } else {
                locale = preferedLocale;
            }

            locale = locale == null ? Locale.US : locale;

            AppStoreContext storeContext = appUser == null ? null : appUser.getContext(AppCtx.STORE);

            Long storeId = storeContext == null ? null : storeContext.getStoreId();

            message = getMessageResource().getMessage(storeId,
                    locale,
                    key,
                    args
            );

            if (message == null && !locale.equals(Constants.DEFAULT_LOCALE)) {
                message = getMessageResource().getMessage(storeId,
                        Constants.DEFAULT_LOCALE,
                        key,
                        args
                );
            }

            if (message == null && returnNullIfNotFound) {
                return null;
            }

            if (message == null) {
                message = Constants.UNDEFINED_MESSAGE_KEY + key;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        logger.debug("getMessage()=> END.");
        return message;
    }

}
