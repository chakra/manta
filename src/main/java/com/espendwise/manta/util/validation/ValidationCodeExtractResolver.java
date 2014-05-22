package com.espendwise.manta.util.validation;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidationCodeExtractResolver implements ValidationCodeResolver {

    private List<ValidationCode> errorCodes;

    public ValidationCodeExtractResolver() {
        this.errorCodes = new ArrayList<ValidationCode>();
    }

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] code) throws ValidationException {
        if (code != null) {
            errorCodes.addAll(Arrays.asList(code));
        }
        return null;
    }

    public List<ValidationCode> getErrorCodes(ValidationResult vr) {
        if (vr != null) {
            vr.getResult();
        }
        return errorCodes;
    }
}
