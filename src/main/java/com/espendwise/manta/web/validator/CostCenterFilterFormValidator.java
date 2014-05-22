package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.CostCenterFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

import java.util.List;

public class CostCenterFilterFormValidator extends AbstractFormValidator {

    public CostCenterFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        CostCenterFilterForm form = (CostCenterFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getCostCenterId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getCostCenterId(), new SearchByIdErrorResolver("admin.global.filter.label.costCenterId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getCostCenterName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getCostCenterName(), new TextErrorWebResolver("admin.global.filter.label.costCenterName", null));
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
