package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.web.forms.SimpleFilterForm;
import com.espendwise.manta.web.util.WebErrors;
import org.springframework.validation.Validator;

public class SimpleFilterFormValidator extends AbstractFormValidator implements Validator, FormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        SimpleFilterForm valueObj = (SimpleFilterForm) obj;

        WebErrors errors = new WebErrors();

        SimpleFilterFormFieldValidator validator = new SimpleFilterFormFieldValidator(valueObj.getFilterKey(), valueObj.getFilterIdKey());
        ValidationResult vr = validator.validate(obj);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        return new MessageValidationResult(errors.get());
    }


}
