package com.espendwise.manta.web.tags;


import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.ArgumentType;
import com.espendwise.manta.util.format.AppI18nFormatter;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Locale;

public class AppFormatTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(AppFormatTag.class);

    private Object value;
    private ArgumentType type;
    private Locale locale;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ArgumentType getType() {
        return type;
    }

    public void setType(ArgumentType type) {
        this.type = type;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int doEndTag() throws JspException {

        AppLocale locale = new AppLocale(getLocale() != null ? getLocale() : Auth.getAppUser().getLocale());
        logger.debug("doEndTag()=> locale: " + locale);
        try {
            Object value = retrieveValue();
            if (Utility.isSet(value)) {
                AppI18nFormatter format = new AppI18nFormatter(locale);
                logger.debug("doEndTag()=> formatter: " + format + ", value: " + value + ", type: " + getType());
                pageContext.getOut().write(format.format(getType(), value));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }

        return super.doEndTag();

    }

    public Object retrieveValue() {
          return getValue();
    }

    @Override
    public void release() {

        this.value = null;
        this.type = null;
        this.locale = null;

        super.release();
    }
}
