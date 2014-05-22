package com.espendwise.manta.web.controllers;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.UrlPathAssistent;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

public class ControllerSupport {

    public String redirect(String path) {
        return "redirect:"+ path;
    }

    public String redirect(WebRequest request, String template) {
        return redirect(UrlPathAssistent.createPath(request, template));
    }

    public String trim(String value){
        return Utility.trim(value);
    }

    public String formatDate(AppLocale locale, Date date) {
        return AppI18nUtil.formatDate(locale, date);
    }

    public String formatNumber(AppLocale locale, Number n) {
        return AppI18nUtil.formatNumber(locale, n);
    }

    public Date parseDateNN(AppLocale locale, String date) {
        return AppI18nUtil.parseDateNN(locale, date);
    }

    public Date parseDate(AppLocale locale, String date) {
        return AppI18nUtil.parseDate(locale, date);
    }

    public Number parseNumber(String value) {
        return AppI18nUtil.parseNumber(value);
    }

    public Long parseNumberNN(String value) {
        return AppI18nUtil.parseNumberNN(value);
    }

    public void retrieveModel(WebRequest request, String modelKey) {
        request.setAttribute(
                modelKey,
                request.getAttribute(modelKey, WebRequest.SCOPE_REQUEST), WebRequest.SCOPE_REQUEST);
    }


    
}
