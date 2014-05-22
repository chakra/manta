package com.espendwise.manta.web.tags;


import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.alert.AppLocale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class DatePickerIncludeTag extends TagSupport {

    private static final String INCLUDE_JQUERY_UI_JS = "<script type=\"text/javascript\" src=\"%1$s/jquery/ajax/libs/jqueryui/1.8.9/jquery-ui.min.js\"></script>";
    private static final String INCLUDE_JQUERY_UI_I18N_JS = "<script type=\"text/javascript\" src=\"%1$s/jquery/ajax/libs/jqueryui/1.8.14/jquery-ui-i18n.min.js\"></script>";
    private static final String INCLUDE_CALENDAR_JS = "<script type=\"text/javascript\" src=\"%1$s/js/calendar.js\"></script>";
    private static final String INCLUDE_CALENDAR_CSS = "<link href=\"%1$s/css/jquery.ui.datepicker.esw.css\" rel=\"Stylesheet\" type=\"text/css\" media=\"all\" />";
    private static final String INIT_DATE_PICKERS = "<script type=\"text/javascript\">$(document).ready(function () { initI18nDatePickers('%1$s','%2$s','%3$s','%4$s'); });</script>";

    @Override
    public int doEndTag() throws JspException {

        try {

            AppLocale locale = new AppLocale(Auth.getAppUser().getLocale());
            String resources = (String) pageContext.getRequest().getAttribute("resources");

            StringBuilder sb = new StringBuilder();

            sb.append(String.format(INCLUDE_CALENDAR_CSS, resources));
            sb.append(String.format(INCLUDE_JQUERY_UI_JS, resources));
            sb.append(String.format(INCLUDE_JQUERY_UI_I18N_JS, resources));
            sb.append(String.format(INCLUDE_CALENDAR_JS, resources));
            sb.append(
                    String.format(INIT_DATE_PICKERS,
                            locale.getLocale().getLanguage(),
                            locale.getLocale().getCountry(),
                            I18nUtil.getDatePickerPattern(locale),
                            I18nUtil.getDatePatternPrompt(locale)
                    )
            );

            pageContext.getOut().write(sb.toString());

        } catch (IOException e) {  //ignore

        }

        return TagSupport.EVAL_PAGE;

    }


}
