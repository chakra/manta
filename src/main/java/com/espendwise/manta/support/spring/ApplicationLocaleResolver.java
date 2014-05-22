package com.espendwise.manta.support.spring;


import com.espendwise.manta.auth.Auth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class ApplicationLocaleResolver extends AcceptHeaderLocaleResolver {

    private static Log logger = LogFactory.getLog(ApplicationLocaleResolver.class);

    public ApplicationLocaleResolver() { }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        Locale locale;

        if (Auth.getAppUser() != null) {
            locale = Auth.getAppUser().getLocale();
        } else if (Auth.getAuthUser() != null) {
            locale = Auth.getAuthUser().getAuthUserLocale();
        } else {
            locale = null;
        }

        return locale == null ? super.resolveLocale(request) : locale;
    }

}
