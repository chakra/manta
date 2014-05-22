package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.UserAccountFilterForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class UserAccountFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        UserAccountFilterForm form = (UserAccountFilterForm) obj;

        WebErrors errors = new WebErrors();
        
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        IntegerValidator idValidator = Validators.getIntegerValidator();
        
        ValidationResult vr;
        if (Utility.isSet(form.getAccountId())) {
            vr = idValidator.validate(form.getAccountId(), new NumberErrorWebResolver("admin.global.filter.label.accountId"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getAccountName())) {
            vr = shortDescValidator.validate(form.getAccountName(), new TextErrorWebResolver("admin.global.filter.label.accountName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }


}
