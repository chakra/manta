package com.espendwise.manta.web.resolver;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.arguments.resolvers.AbstractNumberResolver;
import com.espendwise.manta.util.validation.ValidationCode;

public class NumberErrorInLineWebResolver extends AbstractNumberResolver {

    private String lineNumber;
    private String fieldKey;
    private String defaultFieldName;

    public NumberErrorInLineWebResolver(String lineNumber, String fieldKey, String defaultFieldName) {
        this.lineNumber = lineNumber;
        this.fieldKey = fieldKey;
        this.defaultFieldName = defaultFieldName;
    }

    public NumberErrorInLineWebResolver(String lineNumber, String fieldKey) {
        this.lineNumber = lineNumber;
        this.fieldKey  = fieldKey;
    }

    @Override
    public ArgumentedMessage isInvalidNumberFormat(ValidationCode code) {
        TypedArgument[] args;
        if (Utility.isSet(lineNumber)) {
            args = new TypedArgument[]{new StringArgument(lineNumber), new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.wrongNumberFormat.inLine", Utility.joins(args , code.getArguments()));
        } else {
            args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.wrongNumberFormat", Utility.joins(args , code.getArguments()));
        }
    }

    @Override
    public ArgumentedMessage isInvalidPositive(ValidationCode code) {
        TypedArgument[] args;
        if (Utility.isSet(lineNumber)) {
            args = new TypedArgument[]{new StringArgument(lineNumber), new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.wrongPositiveValue.inLine", Utility.joins(args , code.getArguments()));
        } else {
            args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.wrongPositiveValue", Utility.joins(args , code.getArguments()));
        }
    }

    @Override
    public ArgumentedMessage isNotSet(ValidationCode code) {
        TypedArgument[] args;
        if (Utility.isSet(lineNumber)) {
            args = new TypedArgument[]{new StringArgument(lineNumber), new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.emptyValue.inLine", args);
        } else {
            args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.emptyValue", args);
        }
    }

    public String getDefaultFieldName() {
        return defaultFieldName;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public String getLineNumber() {
        return lineNumber;
    }

}

