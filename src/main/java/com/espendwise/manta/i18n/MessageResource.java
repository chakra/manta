package com.espendwise.manta.i18n;


import com.espendwise.manta.util.alert.AppLocale;

import java.util.Locale;

public interface MessageResource {

    public String getMessage(String key, Object[] args, Locale locale);
   
    public String getMessage(String key, Object[] args, String defaulrMessage, Locale locale);

    public String getMessage(String key, Locale locale);

    public String getMessage(String key, AppLocale locale);
}
