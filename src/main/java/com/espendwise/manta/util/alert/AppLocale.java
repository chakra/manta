package com.espendwise.manta.util.alert;


import com.espendwise.manta.util.Constants;

import java.io.Serializable;
import java.util.Locale;

public class AppLocale  implements Serializable {

    public static final AppLocale DEFAULT_LOCALE = new  AppLocale(Constants.DEFAULT_LOCALE);
    public static final AppLocale SYSTEM_LOCALE = new  AppLocale(Constants.SYSTEM_LOCALE);

    private Locale locale;

    public AppLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
