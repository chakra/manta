package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.SiteFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

import java.util.List;

public class SiteFilterFormValidator extends AbstractFormValidator {

    public SiteFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        SiteFilterForm form = (SiteFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getRefNumber())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.REF_NUMBER_LENGTH);
            CodeValidationResult vr = validator.validate(form.getRefNumber(), new TextErrorWebResolver("admin.global.filter.label.siteRefNumber"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getSiteId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getSiteId(), new SearchByIdErrorResolver("admin.global.filter.label.siteId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getSiteName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getSiteName(), new TextErrorWebResolver("admin.global.filter.label.siteName", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getPostalCode())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DB_CODE_LENGTH);
            CodeValidationResult vr = validator.validate(form.getPostalCode(), new TextErrorWebResolver("admin.global.filter.label.postalCode", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getCity())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getCity(), new TextErrorWebResolver("admin.global.filter.label.city", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        if (Utility.isSet(form.getState())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);
            CodeValidationResult vr = validator.validate(form.getState(), new TextErrorWebResolver("admin.global.filter.label.state", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getAccountFilter())) {
            try {
                List<Long> values = Utility.splitLong(form.getAccountFilter());
                if (values != null && values.size() > Constants.FILTER_RESULT_LIMIT.ACCOUNT) {
                    errors.putError(
                            "validation.web.error.maximumFilterSize",
                            new MessageI18nArgument("admin.global.filter.label.filterByAccounts"),
                            new NumberArgument(Constants.FILTER_RESULT_LIMIT.ACCOUNT)
                    );
                }
            } catch (Exception e) {
                errors.putError("validation.web.error.wrongFilterFormat", new MessageI18nArgument("admin.global.filter.label.filterByAccounts"));
            }
        }

        return new MessageValidationResult(errors.get());

    }
}
