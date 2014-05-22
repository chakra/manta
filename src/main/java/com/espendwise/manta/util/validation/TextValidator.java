package com.espendwise.manta.util.validation;


import java.util.Set;

import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.validation.resolvers.ExceptionResolver;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;

public class TextValidator implements CustomValidator<String, ValidationCodeResolver> {

    private Integer maxSize;
    private boolean isRequired;
    private Set<String> supportedValues;

    public TextValidator(Integer maxSize) {
        this(maxSize, true);
    }

    public TextValidator(Integer maxSize, boolean isRequired) {
         this(maxSize, isRequired, null);
    }
   
    public TextValidator(Integer maxSize, boolean isRequired, Set<String> supportedValues) {
        this.maxSize = maxSize;
        this.isRequired = isRequired;
        this.supportedValues = supportedValues;
    }

    public boolean isSet(String value) {
        return value != null && value.trim().length() > 0;
    }
 
    public boolean isSupported(String value) {
    	System.out.println("isSupported() ====> supportedValues =" +((supportedValues == null) ? "NULL" : supportedValues.toString()) + ", value = " + value);
    	if (supportedValues == null ) {
    		return true;
    	} else {
    		return value != null && value.trim().length() > 0 && supportedValues.contains(value);
    	}	
    }

    public boolean isRangeOut(String value) throws AppParserException {
        return maxSize != null && value.length() > maxSize;
    }

    public CodeValidationResult validate(String obj, ValidationCodeResolver resolver) throws ValidationException {

        if (isRequired && !isSet(obj)) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.VALUE_IS_NOT_SET, new StringArgument(obj))
            );
        }

        if (isSet(obj) && !isSupported(obj)) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.VALUE_IS_NOT_SUPPORTED, new StringArgument(obj))
            );
        	
        }

        if (isSet(obj) && isRangeOut(obj)) {

            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.RANGE_OUT, new StringArgument(obj), new NumberArgument(getMaxSize()))
            );
        }

        return null;
    }

    @Override
    public CodeValidationResult validate(String obj) throws ValidationException {
        return validate(obj, new ExceptionResolver());
    }


    public Integer getMaxSize() {
        return maxSize;
    }

    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
}
