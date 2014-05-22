package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.UserFilterForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;

import java.util.List;

public class UserFilterFormValidator extends AbstractFormValidator {

    private static final Logger logger = Logger.getLogger(UserFilterFormValidator.class);

    public UserFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        UserFilterForm valueObj = (UserFilterForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        IntegerValidator idValidator = Validators.getIntegerValidator();
        
        if (Utility.isSet(valueObj.getUserId())) {
            vr = idValidator.validate(valueObj.getUserId(), new NumberErrorWebResolver("admin.global.filter.label.userId"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSet(valueObj.getUserLoginName())) {
            vr = shortDescValidator.validate(valueObj.getUserLoginName(), new TextErrorWebResolver("admin.global.filter.label.userName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSet(valueObj.getLastName())) {
            vr = shortDescValidator.validate(valueObj.getLastName(), new TextErrorWebResolver("admin.global.filter.label.userLastName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSet(valueObj.getFirstName())) {
            vr = shortDescValidator.validate(valueObj.getFirstName(), new TextErrorWebResolver("admin.global.filter.label.userFirstName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(valueObj.getEmail())) {
            vr = shortDescValidator.validate(valueObj.getEmail(), new TextErrorWebResolver("admin.global.filter.label.userEmail"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(valueObj.getAccountFilter())) {
            try {
                List<Long> values = Utility.splitLong(valueObj.getAccountFilter());
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
