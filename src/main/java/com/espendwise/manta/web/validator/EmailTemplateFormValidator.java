package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.EmailTemplateForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;

public class EmailTemplateFormValidator extends AbstractFormValidator {

    private static final Logger logger = Logger.getLogger(EmailTemplateFormValidator.class);

    public EmailTemplateFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        EmailTemplateForm valueObj = (EmailTemplateForm) obj;

        ValidationResult vr;
       
        TextValidator nameValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SPEC_50_LENGTH);
        IntegerValidator intValidator = Validators.getIntegerValidator();

        if (!valueObj.isPreviewRequest()) {
            vr = nameValidator.validate(valueObj.getTemplateName(), new TextErrorWebResolver("admin.template.email.label.templateName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(valueObj.getTemplateSubject())) {
            TextValidator subjectValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.BIG_TEXT_LENGTH);
            vr = subjectValidator.validate(valueObj.getTemplateSubject(), new TextErrorWebResolver("admin.template.email.label.templateSubject"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (!Utility.isSet(valueObj.getTemplateContent())) {
            errors.putError("validation.web.error.emptyValue", Args.i18nTyped("admin.template.email.label.templateBody"));
        }

        if (!Utility.isSet(valueObj.getTemplateLocaleCode())) {
            errors.putError("validation.web.error.emptyValue", Args.i18nTyped("admin.template.email.label.templateLocaleCode"));
        }

        if (!Utility.isSet(valueObj.getEmailTypeCode())) {
            errors.putError("validation.web.error.emptyValue", Args.i18nTyped("admin.template.email.label.emailTypeCode"));
        } else {
            if (valueObj.isPreviewRequest()) {
                String key = valueObj.getIsServiceTemplate() ? "admin.template.email.label.serviceTicketId" : "admin.template.email.label.previewId";
                vr = intValidator.validate(valueObj.getPreviewId(), new NumberErrorWebResolver((key)));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }
            }
        }

        if (!Utility.isSet(valueObj.getEmailObject())) {
            errors.putError("validation.web.error.emptyValue", Args.i18nTyped("admin.template.email.label.emailObject"));
        }

        if (!Utility.isSet(valueObj.getTemplateType())) {
            errors.putError("validation.web.error.template.noTypeWasSpecified");
        } else if (!RefCodeNames.TEMPLATE_TYPE_CD.EMAIL.equals(valueObj.getTemplateType())) {
            errors.putError("validation.web.error.template.invalidTypeWasSpecified", Args.typed(valueObj.getTemplateType()));
        }

        return new MessageValidationResult(errors.get());

    }


}
