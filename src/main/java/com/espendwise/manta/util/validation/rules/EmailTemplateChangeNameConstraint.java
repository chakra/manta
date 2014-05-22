package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.TemplateService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

public class EmailTemplateChangeNameConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(EmailTemplateUniqueConstraint.class);

    private Long storeId;
    private EmailTemplateIdentView templateIdentView;

    public EmailTemplateChangeNameConstraint(Long storeId, EmailTemplateIdentView templateIdentView) {
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

        Long id = getId();
        if (id != null) {

            EntityHeaderView header = getTemplateService().findTemplateHeader(id);

            if (header != null && !header.getShortDesc().equals(getName())) {

                if (getTemplateService().isTemplateUsedBySystem(getStoreId(), getId())) {

                    result.failed(
                            ExceptionReason.EmailTemplateUpdateReason.EMAIL_TEMPLATE_CANT_BE_RENAMED,
                            new ObjectArgument<EmailTemplateIdentView>(getTemplateIdentView()),
                            new StringArgument(header.getShortDesc())
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
