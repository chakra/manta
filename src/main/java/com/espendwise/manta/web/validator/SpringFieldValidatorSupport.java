package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import org.springframework.validation.Errors;

public abstract class SpringFieldValidatorSupport extends SpringValidatorSupport  implements FormFieldValidator{

    @Override
    public void putError(ArgumentedMessage m, Errors errors) {
         errors.rejectValue(getFieldLabel(), m.getKey(), m.getArguments(), m.getDefaultMessage());
    }

}
