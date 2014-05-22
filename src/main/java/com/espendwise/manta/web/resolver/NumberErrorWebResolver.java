package com.espendwise.manta.web.resolver;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.arguments.resolvers.AbstractNumberResolver;
import com.espendwise.manta.util.validation.ValidationCode;

public class NumberErrorWebResolver extends AbstractNumberResolver {


    private String fieldKey;
    private String defaultFieldName;

    public NumberErrorWebResolver(String fieldKey, String defaultFieldName) {
        this.fieldKey  = fieldKey;
        this.defaultFieldName = defaultFieldName;
    }
    public NumberErrorWebResolver(String fieldKey) {
        this.fieldKey  = fieldKey;
    }

    @Override
    public ArgumentedMessage isInvalidNumberFormat(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
        return new ArgumentedMessageImpl("validation.web.error.wrongNumberFormat", Utility.joins(args , code.getArguments()));
    }

    @Override
    public ArgumentedMessage isInvalidPositive(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
        return new ArgumentedMessageImpl("validation.web.error.wrongPositiveValue", Utility.joins(args , code.getArguments()));
    }

    @Override
    public ArgumentedMessage isNotSet(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
        return new ArgumentedMessageImpl("validation.web.error.emptyValue", args);
    }

    public String getDefaultFieldName() {
        return defaultFieldName;
    }

    public String getFieldKey() {
        return fieldKey;
    }
}

