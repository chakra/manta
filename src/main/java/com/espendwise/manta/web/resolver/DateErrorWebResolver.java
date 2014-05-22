package com.espendwise.manta.web.resolver;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.resolvers.AbstractDateResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import org.apache.log4j.Logger;

public class DateErrorWebResolver extends AbstractDateResolver {
private static final Logger logger = Logger.getLogger(DateErrorWebResolver.class);

    private String fieldKey;
    private String defaultFieldName;
    private String additionalFieldKey;
    private String additionalDefaultFieldName;
    private String dateFormat;

    public DateErrorWebResolver(String fieldKey, String defaultFieldName, String dateFormat) {
        this.fieldKey = fieldKey;
        this.defaultFieldName = defaultFieldName;
        this.dateFormat = dateFormat;
    }

    public DateErrorWebResolver(String fieldKey, String defaultFieldName) {
        this.fieldKey = fieldKey;
        this.defaultFieldName = defaultFieldName;
        this.dateFormat = AppI18nUtil.getDatePatternPrompt();
    }
    
    public DateErrorWebResolver(String fieldKey, String defaultFieldName, String additionalFieldKey, String additionalDefaultFieldName) {
        this(fieldKey, defaultFieldName);
        this.additionalFieldKey = additionalFieldKey;
        this.additionalDefaultFieldName = additionalDefaultFieldName;
    }

    public DateErrorWebResolver(String fieldKey) {
        this.fieldKey = fieldKey;
        this.dateFormat = AppI18nUtil.getDatePatternPrompt();
    }

    @Override
    protected ArgumentedMessage isRangeOut(ValidationCode code) throws ValidationException {
    	if (code.getArguments()[3].get() != null && code.getArguments()[4].get() != null){
	        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName()),
	                code.getArguments()[3],
	                code.getArguments()[4],
	        };
	        return new ArgumentedMessageImpl("validation.web.error.rangeOut", args);
    	} else if (code.getArguments()[3].get() != null){
    		TypedArgument[] args = new TypedArgument[]{
                    Args.i18nTyped(getFieldKey())[0],
                    code.getArguments()[2],
                    Args.i18nTyped("admin.global.text.error.param.currentDate")[0],
                    code.getArguments()[3]
            };
	        return new ArgumentedMessageImpl("validation.web.error.wrongDateRange", args);
    	} else {
    		TypedArgument[] args = new TypedArgument[]{
                    Args.i18nTyped("admin.global.text.error.param.currentDate")[0],
                    code.getArguments()[3],
                    Args.i18nTyped(getFieldKey())[0],
                    code.getArguments()[2]
            };
	        return new ArgumentedMessageImpl("validation.web.error.wrongDateRange", args);
    	}
    }

    @Override
    protected ArgumentedMessage isDatesRangeOut(ValidationCode code) throws ValidationException {
        TypedArgument[] args = new TypedArgument[]{
                Args.i18nTyped("admin.corporateSchedule.label.dateTo")[0],
                code.getArguments()[1],
                Args.i18nTyped("admin.corporateSchedule.label.dateFrom")[0],
                code.getArguments()[0]
        };

        return new ArgumentedMessageImpl("validation.web.error.wrongDateRange", args);

    }


    @Override
    protected ArgumentedMessage isWrongDateFormat(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName()),
                code.getArguments()[0],
                new StringArgument(getDateFormat())
        };
        return new ArgumentedMessageImpl("validation.web.error.wrongDateFormat", args);
    }
    @Override
    protected ArgumentedMessage isWrongDateFormatExt(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName()),
                code.getArguments()[0],
                new StringArgument(getDateFormat())
        };
        return new ArgumentedMessageImpl("validation.web.error.wrongDateFormatExt", args);
    }

    @Override
    public ArgumentedMessage isNotSet(ValidationCode code) throws ValidationException {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName())};
        return new ArgumentedMessageImpl("validation.web.error.emptyValue", args);
    }
    
    @Override
    protected ArgumentedMessage isWrongBounds(ValidationCode code) {
        TypedArgument[] args = new TypedArgument[]{new StringI18nArgument(getFieldKey(), getDefaultFieldName()),
                                                   new StringI18nArgument(getAdditionalFieldKey(), getAdditionalDefaultFieldName())
        };
        return new ArgumentedMessageImpl("validation.web.error.wrongDateBounds", args);
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

    public String getDateFormat() {
            return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
    }

    public String getAdditionalDefaultFieldName() {
        return additionalDefaultFieldName;
    }

    public void setAdditionalDefaultFieldName(String additionalDefaultFieldName) {
        this.additionalDefaultFieldName = additionalDefaultFieldName;
    }

    public String getAdditionalFieldKey() {
        return additionalFieldKey;
    }

    public void setAdditionalFieldKey(String additionalFieldKey) {
        this.additionalFieldKey = additionalFieldKey;
    }
    
}
