package com.espendwise.manta.web.resolver;


import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.validation.resolvers.AbstractFilterResultValidationResolver;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

public class FilterResultErrorWebResolver extends AbstractFilterResultValidationResolver {


    public FilterResultErrorWebResolver(String fieldKey, String defaulFieldName) {
        super(fieldKey, defaulFieldName);
    }

    public FilterResultErrorWebResolver(String fieldKey) {
        super(fieldKey, null);
    }


    @Override
    public ArgumentedMessage isSizeOut(ValidationCode code) throws ValidationException {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), this.getDefaulFieldName())};
        return new ArgumentedMessageImpl("validation.web.error.itemFilterResultLimitOut", Utility.joins( new TypedArgument[]{code.getArguments()[0] }));
    }

}
