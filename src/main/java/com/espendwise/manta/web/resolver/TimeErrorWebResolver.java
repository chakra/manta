package com.espendwise.manta.web.resolver;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.web.util.AppI18nUtil;

public class TimeErrorWebResolver extends DateErrorWebResolver {

    public TimeErrorWebResolver(String fieldKey, String defaultFieldName, String dateFormat) {
    	super(fieldKey, defaultFieldName, dateFormat);
    }

    public TimeErrorWebResolver(String fieldKey, String defaultFieldName) {
    	super(fieldKey, defaultFieldName, AppI18nUtil.getTimePatternPrompt());
    }

    public TimeErrorWebResolver(String fieldKey) {
    	super(fieldKey, null, AppI18nUtil.getTimePatternPrompt());
    }

    @Override
    protected ArgumentedMessage isWrongDateFormat(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName()),
                code.getArguments()[0],
                new StringArgument(getDateFormat())
        };
        return new ArgumentedMessageImpl("validation.web.error.wrongTimeFormat", args);
    }
}
