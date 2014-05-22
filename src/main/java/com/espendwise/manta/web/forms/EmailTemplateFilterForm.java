package com.espendwise.manta.web.forms;

public class EmailTemplateFilterForm extends SimpleFilterForm {
    @Override
    public String getFilterKey() {  return "admin.template.email.label.templateName"; }
    @Override
    public String getFilterIdKey() {  return "admin.template.email.label.templateId"; }
}

