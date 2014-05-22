package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.LocateSiteFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class LocateSiteFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        LocateSiteFilterForm form = (LocateSiteFilterForm) obj;

        WebErrors errors = new WebErrors();
        
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        TextValidator shortDBCodeValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DB_CODE_LENGTH);
        
        if (Utility.isSet(form.getLocationId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getLocationId(), new SearchByIdErrorResolver("admin.global.filter.label.siteId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        ValidationResult vr;
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
        	vr = shortDBCodeValidator.validate(form.getPostalCode(), new TextErrorWebResolver("admin.global.filter.label.postalCode"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());
    }

}
