package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.validation.CustomValidator;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;

public interface FormFieldValidator extends CustomValidator<Object, ValidationCodeResolver> {
    public String getFieldLabel();
}
