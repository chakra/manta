package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.SiteUserFilterForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class SiteUserFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        SiteUserFilterForm form = (SiteUserFilterForm) obj;

        WebErrors errors = new WebErrors();
        
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        IntegerValidator idValidator = Validators.getIntegerValidator();
        
        ValidationResult vr;
        if (Utility.isSet(form.getUserId())) {
            vr = idValidator.validate(form.getUserId(), new NumberErrorWebResolver("admin.site.users.label.userId"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getUserName())) {
            vr = shortDescValidator.validate(form.getUserName(), new TextErrorWebResolver("admin.site.users.label.userName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }

}
