package com.espendwise.manta.web.resolver;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationCode;

public class SearchByIdErrorResolver extends NumberErrorWebResolver {

    public SearchByIdErrorResolver(String fieldKey,  String defaultFieldName) {
        super(fieldKey, defaultFieldName);
    }

    @Override
    public ArgumentedMessage isInvalidNumberFormat(ValidationCode code) {
        return super.isInvalidNumberFormat(code);
    }

    @Override
    public ArgumentedMessage isInvalidPositive(ValidationCode code) {
        return super.isInvalidPositive(code);
    }

    @Override
    public ArgumentedMessage isNotSet(ValidationCode code) {
        return super.isNotSet(code);
    }
}
