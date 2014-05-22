package com.espendwise.ocean.common.emails;

import java.io.Serializable;

public class TemplateEmailValue implements Serializable {

    private String templateSubject;
    private String templateText;

    public TemplateEmailValue() {
    }

    public TemplateEmailValue(String templateSubject, String templateText) {
        this.templateSubject = templateSubject;
        this.templateText = templateText;
    }

    public String getTemplateSubject() {
        return templateSubject;
    }

    public void setTemplateSubject(String templateSubject) {
        this.templateSubject = templateSubject;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }
}