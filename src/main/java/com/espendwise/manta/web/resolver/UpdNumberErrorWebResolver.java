package com.espendwise.manta.web.resolver;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.arguments.resolvers.AbstractNumberResolver;
import com.espendwise.manta.util.validation.ValidationCode;

public class UpdNumberErrorWebResolver extends NumberErrorWebResolver {
    

    public UpdNumberErrorWebResolver(String fieldKey, String defaultFieldName) {
        super(fieldKey, defaultFieldName);
    }
    public UpdNumberErrorWebResolver(String fieldKey) {
        super(fieldKey);
    }

    @Override
    public ArgumentedMessage isNotSet(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName() )};
        return new ArgumentedMessageImpl("validation.web.error.wrongNumberFormat", Utility.joins(args , code.getArguments()));
    }

}

