package com.espendwise.manta.web.resolver;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.arguments.resolvers.AbstractNumberResolver;
import com.espendwise.manta.util.validation.ValidationCode;

public class NumberErrorElementWebResolver extends AbstractNumberResolver {

    private String elementName;
    private String fieldKey;
    private String defaultFieldName;

    public NumberErrorElementWebResolver(String elementName, String fieldKey, String defaultFieldName) {
        this.elementName = elementName;
        this.fieldKey = fieldKey;
        this.defaultFieldName = defaultFieldName;
    }

    public NumberErrorElementWebResolver(String elementName, String fieldKey) {
        this.elementName = elementName;
        this.fieldKey  = fieldKey;
    }

    @Override
    public ArgumentedMessage isInvalidNumberFormat(ValidationCode code) {
        TypedArgument[] args;
        if (Utility.isSet(elementName)) {
            args = new TypedArgument[]{new StringArgument(elementName), new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.wrongNumberFormat.element", Utility.joins(args , code.getArguments()));
        } else {
            args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.wrongNumberFormat", Utility.joins(args , code.getArguments()));
        }
    }

    @Override
    public ArgumentedMessage isInvalidPositive(ValidationCode code) {
        TypedArgument[] args;
        if (Utility.isSet(elementName)) {
            args = new TypedArgument[]{new StringArgument(elementName), new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.wrongPositiveValue.element", Utility.joins(args , code.getArguments()));
        } else {
            args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.wrongPositiveValue", Utility.joins(args , code.getArguments()));
        }
    }

    @Override
    public ArgumentedMessage isNotSet(ValidationCode code) {
        TypedArgument[] args;
        if (Utility.isSet(elementName)) {
            args = new TypedArgument[]{new StringArgument(elementName), new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
            return new ArgumentedMessageImpl("validation.web.error.emptyValue.element", args);
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

    public String getElementName() {
        return elementName;
    }

}

