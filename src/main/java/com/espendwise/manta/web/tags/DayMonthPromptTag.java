package com.espendwise.manta.web.tags;

import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.i18n.I18nUtil;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class DayMonthPromptTag extends TagSupport {

    @Override
    public int doEndTag() throws JspException {

        try {

            AppLocale locale = new AppLocale(Auth.getAppUser().getLocale());
            pageContext.getOut().write(I18nUtil.getDayWithMonthPatternPrompt(locale));

        } catch (IOException e) {  //ignore

        }

        return TagSupport.EVAL_PAGE;

    }


}
