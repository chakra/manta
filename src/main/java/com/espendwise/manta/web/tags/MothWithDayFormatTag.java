package com.espendwise.manta.web.tags;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.ArgumentType;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.web.util.AppI18nUtil;

import javax.servlet.jsp.JspException;


public class MothWithDayFormatTag extends AppFormatTag{

    @Override
    public int doStartTag() throws JspException {
        setType(ArgumentType.MONTH_WITH_DAY);
        return super.doStartTag();
    }


    @Override
    public Object retrieveValue() {
        Object value = getValue();
        if (value instanceof String) {
            try {
                return Parse.parseMonthWithDay((String) value, Constants.NOT_LEAP_YEAR, AppLocale.SYSTEM_LOCALE);
            } catch (AppParserException e) {
                e.printStackTrace();
                return AppI18nUtil.getMessage("admin.global.text.wrongValue", new Object[]{value});
            }
        }
        return super.retrieveValue();
    }
}