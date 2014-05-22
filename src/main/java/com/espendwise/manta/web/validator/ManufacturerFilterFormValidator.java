package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.ManufacturerFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

import java.util.List;

public class ManufacturerFilterFormValidator extends AbstractFormValidator {

    public ManufacturerFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        ManufacturerFilterForm form = (ManufacturerFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getManufacturerId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getManufacturerId(), new SearchByIdErrorResolver("admin.global.filter.label.manufacturerId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getManufacturerName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getManufacturerName(), new TextErrorWebResolver("admin.global.filter.label.manufacturerName", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }
}
