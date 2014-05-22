package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.model.view.EmailTemplateListView;
import com.espendwise.manta.service.TemplateService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.StoreTemplateCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.List;

public class EmailTemplateUniqueConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(EmailTemplateUniqueConstraint.class);

    private Long storeId;
    private EmailTemplateIdentView templateIdentView;

    public EmailTemplateUniqueConstraint(Long storeId, EmailTemplateIdentView templateIdentView) {
        this.storeId = storeId;
        this.templateIdentView = templateIdentView;
    }

    public Long getStoreId() {
        return storeId;
    }

    public EmailTemplateIdentView getTemplateIdentView() {
        return templateIdentView;
    }

    public Long getId() {
        return getTemplateIdentView().getTemplateData().getTemplateId();
    }

    public String getName() {
        return getTemplateIdentView().getTemplateData().getName();
    }

    public String getLocale() {
        return PropertyUtil.toValueNN(
                getTemplateIdentView().getProperties(),
                RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.LOCALE
        );
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getTemplateIdentView() == null) {
            return null;
        }

        ValidationRuleResult result = new ValidationRuleResult();

        StoreTemplateCriteria criteria = new StoreTemplateCriteria(getStoreId());

        TemplateService templateService = getTemplateService();

        List<EmailTemplateListView> dbTemplates = templateService.findEmailTemplatesByCriteria(criteria);
        if (dbTemplates != null && dbTemplates.size() > 0) {

            for (EmailTemplateListView dbTemplate : dbTemplates) {

                if (!Utility.strNN(getName()).equalsIgnoreCase(dbTemplate.getName())) {
                    continue;
                }

                if (!getLocale().equalsIgnoreCase(dbTemplate.getLocale())) {
                    continue;
                }

                if (getId() == null || (getId().longValue() != dbTemplate.getTemplateId())) {

                    result.failed(
                            ExceptionReason.EmailTemplateUpdateReason.EMAIL_TEMPLATE_MUST_BE_UNIQUE,
                            new ObjectArgument<EmailTemplateIdentView>(getTemplateIdentView())
                    );

                    return result;
                }
            }
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }

    public TemplateService getTemplateService() {
        return ServiceLocator.getTemplateService();
    }
}

