package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.LocateAccountFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class LocateAccountFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        LocateAccountFilterForm form = (LocateAccountFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getDistrRefNumber())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.REF_NUMBER_LENGTH);
            CodeValidationResult vr = validator.validate(form.getDistrRefNumber(), new TextErrorWebResolver("admin.global.filter.label.distrRefNumber"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        if (Utility.isSet(form.getAccountName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getAccountName(), new TextErrorWebResolver("admin.global.filter.label.accountName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        if (Utility.isSet(form.getAccountId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getAccountId(), new SearchByIdErrorResolver("admin.global.filter.label.accountId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        return new MessageValidationResult(errors.get());

    }


}
