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
import com.espendwise.manta.i18n.I18nUtil;

public class DatePromptTag extends TagSupport {

    @Override
    public int doEndTag() throws JspException {

        try {

            AppLocale locale = new AppLocale(Auth.getAppUser().getLocale());
            pageContext.getOut().write(I18nUtil.getDatePatternPrompt(locale));

        } catch (IOException e) {  //ignore

        }

        return TagSupport.EVAL_PAGE;

    }


}
