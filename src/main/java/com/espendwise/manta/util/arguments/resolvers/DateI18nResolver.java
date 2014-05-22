package com.espendwise.manta.util.arguments.resolvers;


import com.espendwise.manta.i18n.I18nResource;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.arguments.ArgumentResolver;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateI18nResolver implements ArgumentResolver<Date> {
   
    private static final Logger logger = Logger.getLogger(DateI18nResolver.class);

    @Override
    public String resolve(AppLocale locale, Date obj) {
        String pattern = I18nUtil.getDatePattern(locale);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        return format.format(obj);
    }

    @Override
    public String resolve(Date obj) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN);
        format.setLenient(false);
        return format.format(obj);
    }
}
