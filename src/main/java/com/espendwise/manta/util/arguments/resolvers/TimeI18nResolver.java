package com.espendwise.manta.util.arguments.resolvers;


import com.espendwise.manta.i18n.I18nResource;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.AppLocale;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeI18nResolver extends DateI18nResolver {

   @Override
    public String resolve(AppLocale locale, Date obj) {
        String pattern= I18nResource.getResource().getMessage(Constants.TIME_PATTERN, locale.getLocale());
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        return format.format(obj);
    }

    @Override
    public String resolve(Date obj) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.SYSTEM_TIME_PATTERN);
        format.setLenient(false);
        return format.format(obj);
    }

}
