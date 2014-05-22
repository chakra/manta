package com.espendwise.manta.web.validator;


import org.springframework.validation.Validator;

public abstract  class AbstractFormValidator extends SpringValidatorSupport implements Validator, FormValidator {

    public AbstractFormValidator() {

    }

}
