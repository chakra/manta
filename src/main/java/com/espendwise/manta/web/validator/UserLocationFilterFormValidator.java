package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.UserLocationFilterForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class UserLocationFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        UserLocationFilterForm form = (UserLocationFilterForm) obj;

        WebErrors errors = new WebErrors();
        
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        IntegerValidator idValidator = Validators.getIntegerValidator();
        
        ValidationResult vr;
        if (Utility.isSet(form.getSiteId())) {
            vr = idValidator.validate(form.getSiteId(), new NumberErrorWebResolver("admin.user.configuration.location.locationId"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getSiteName())) {
            vr = shortDescValidator.validate(form.getSiteName(), new TextErrorWebResolver("admin.user.configuration.location.locationName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSet(form.getReferenceNumber())) {
            vr = shortDescValidator.validate(form.getReferenceNumber(), new TextErrorWebResolver("admin.user.configuration.location.referenceNumber"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSet(form.getCity())) {
            vr = shortDescValidator.validate(form.getCity(), new TextErrorWebResolver("admin.user.configuration.location.city"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSet(form.getStateProvince())) {
            vr = shortDescValidator.validate(form.getStateProvince(), new TextErrorWebResolver("admin.user.configuration.location.stateProvince"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSet(form.getPostalCode())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DB_CODE_LENGTH);
            vr = validator.validate(form.getPostalCode(), new TextErrorWebResolver("admin.global.filter.label.postalCode", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }


}
