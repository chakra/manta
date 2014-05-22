package com.espendwise.manta.web.resolver;


import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.validation.resolvers.AbstractTextValidationResolver;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

public class TextErrorWebResolver extends AbstractTextValidationResolver {


    public TextErrorWebResolver(String fieldKey, String defaulFieldName) {
        super(fieldKey, defaulFieldName);
    }

    public TextErrorWebResolver(String fieldKey) {
        super(fieldKey, null);
    }

    @Override
    public ArgumentedMessage isNotSet(ValidationCode code) throws ValidationException {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), this.getDefaulFieldName())};
        return new ArgumentedMessageImpl("validation.web.error.emptyValue", args);
    }

    @Override
    public ArgumentedMessage isRangeOut(ValidationCode code) throws ValidationException {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), this.getDefaulFieldName())};
        return new ArgumentedMessageImpl("validation.web.error.stringRangeOut", Utility.joins(args, new TypedArgument[]{code.getArguments()[1]}));
    }
    
    @Override
    public ArgumentedMessage isNotSupported(ValidationCode code) throws ValidationException {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), this.getDefaulFieldName())};
        return new ArgumentedMessageImpl("validation.web.error.notSupportedValue", args);
    }

}
