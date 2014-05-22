package com.espendwise.manta.web.resolver;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.resolvers.AbstractEmailAddressResolver;

public class EmailAddressErrorWebResolver extends AbstractEmailAddressResolver {

    private String fieldKey;
    private String defaultFieldName;

    public EmailAddressErrorWebResolver(String fieldKey, String defaultFieldName) {
        this.fieldKey = fieldKey;
        this.defaultFieldName = defaultFieldName;
    }

    public EmailAddressErrorWebResolver(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    @Override
    protected ArgumentedMessage isWrongEmailAddressFormat(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName()),
                code.getArguments()[0],
                new StringArgument("name@domain.topleveldomain")
        };
        return new ArgumentedMessageImpl("validation.web.error.wrongEmailAddressFormat", args);
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getDefaultFieldName() {
        return defaultFieldName;
    }

    public void setDefaultFieldName(String defaultFieldName) {
        this.defaultFieldName = defaultFieldName;
    }
}
