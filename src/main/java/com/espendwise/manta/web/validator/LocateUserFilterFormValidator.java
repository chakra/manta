package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.LocateUserFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class LocateUserFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        LocateUserFilterForm form = (LocateUserFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getFirstName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getFirstName(), new TextErrorWebResolver("admin.global.filter.label.userFirstName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getLastName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getLastName(), new TextErrorWebResolver("admin.global.filter.label.userLastName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        if (Utility.isSet(form.getUserName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getUserName(), new TextErrorWebResolver("admin.global.filter.label.userName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        if (Utility.isSet(form.getUserId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getUserId(), new SearchByIdErrorResolver("admin.global.filter.label.userId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        return new MessageValidationResult(errors.get());

    }


}
