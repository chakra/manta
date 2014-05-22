package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.EmailTemplateFormValidator;
import com.espendwise.ocean.common.emails.EmailMeta;
import com.espendwise.ocean.common.emails.EmailTypes;

import java.util.Arrays;


@Validation(EmailTemplateFormValidator.class)
public class EmailTemplateForm extends WebForm implements Initializable {

    private boolean initialize;

    private Long templateId;
    private String templateName;
    private String templateLocaleCode;
    private String templateContent;
    private String templateSubject;
    private String templateType;
    private String emailTypeCode;
    private String emailObject;
    private String previewId;
    private boolean previewRequest;


    public void setEmailObject(String emailObject) {
        this.emailObject = emailObject;
    }

    public String getEmailObject() {
        return emailObject;
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateLocaleCode() {
        return templateLocaleCode;
    }

    public void setTemplateLocaleCode(String templateLocaleCode) {
        this.templateLocaleCode = templateLocaleCode;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getTemplateSubject() {
        return templateSubject;
    }

    public void setTemplateSubject(String templateSubject) {
        this.templateSubject = templateSubject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public void initialize() {
        this.initialize =true;
    }

    @Override
    public boolean isInitialized() {
        return initialize;
    }

    public boolean getIsNew() {
        return isNew();
    }
    public boolean preciewRequest() {
        return previewRequest;
    }
    public boolean getIsServiceTemplate() {
        return this.emailTypeCode != null
                && EmailMeta.SERVICE_TICKET_META.getServiceClass().getName().equals(emailObject)
                && Arrays.asList(EmailTypes.ServiceTicketEmails.values())
                .contains(EmailTypes.ServiceTicketEmails.valueOf(this.emailTypeCode));
    }


    public boolean isNew() {
        return isInitialized() && (templateId  == null || !(templateId > 0));
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }


    public String getEmailTypeCode() {
        return emailTypeCode;
    }

    public void setEmailTypeCode(String emailTypeCode) {
        this.emailTypeCode = emailTypeCode;
    }

    public String getPreviewId() {
        return previewId;
    }

    public void setPreviewId(String previewId) {
        this.previewId = previewId;
    }

    public boolean isPreviewRequest() {
        return previewRequest;
    }

    public void setPreviewRequest(boolean previewRequest) {
        this.previewRequest = previewRequest;
    }
}
