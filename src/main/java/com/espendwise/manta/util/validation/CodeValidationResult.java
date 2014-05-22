package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;

import java.util.List;

public class CodeValidationResult implements ValidationResult {

    private ValidationCodeResolver resolver;
    private ValidationCode[] validationCodes;

    public CodeValidationResult() {
    }

    public CodeValidationResult(ValidationCodeResolver resolver, ValidationCode... validationCodes ) {
        this.validationCodes = validationCodes;
        this.resolver = resolver;
    }

    public ValidationCode[] getValidationCodes() {
        return validationCodes;
    }

    @Override
    public List<? extends ArgumentedMessage> getResult() {
        return resolver.resolve(this.validationCodes);
    }
}
