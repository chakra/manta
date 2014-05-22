package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.web.forms.ValidForm;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;


public abstract class SpringValidatorSupport implements Validator{

    private static final Logger logger = Logger.getLogger(SpringValidatorSupport.class);

    public boolean supports(Class aClass) {
        boolean b = ValidForm.class.isAssignableFrom(aClass);
        logger.info("supports()=> aClass:" + aClass + "(" + b + ")");
        return b;
    }

    @Override
    public void validate(Object o, Errors errors) {

        ValidationResult result = validate(o);

        if (result != null) {
            List<? extends ArgumentedMessage> messages = result.getResult();
            if (Utility.isSet(messages)) {
                for (ArgumentedMessage m : messages) {
                     putError(m, errors);
                }
            }
        }
    }

    public void putError(ArgumentedMessage m, Errors errors) {
        errors.reject(m.getKey(), m.getArguments(), m.getDefaultMessage());
    }

    public abstract ValidationResult validate(Object o);

}
