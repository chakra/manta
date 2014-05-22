package com.espendwise.manta.util.validation;

import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.parser.AmountParser;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.AppParserFactory;
import com.espendwise.manta.util.validation.resolvers.ExceptionResolver;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;

import java.math.BigDecimal;

public class AmountValidator implements CustomValidator<String, ValidationCodeResolver> {
    private BigDecimal range;
    private BigDecimal parsedValue;
    
    public AmountValidator () {
        range = null;
    }
    
    public AmountValidator (Integer precision, Integer scale) {
        if (precision != null &&
            precision > 0 &&
            scale != null &&
            scale >= 0 &&
            precision >= scale) {
            range = BigDecimal.valueOf(10).pow(precision - scale);
        } else {
            range = null;
        }
    }
    
    public boolean isSet(String value) {
        return value != null && value.trim().length() > 0;
    }

    public boolean isPositiveValue(BigDecimal value) {
        return value.doubleValue() >= 0;
    }
    
    public boolean isPrecisionScaleValid(BigDecimal value) {
        if (range != null) {
            return value.compareTo(range) < 1;
        } else {
            return true;
        }
    }

    public BigDecimal parse(String value) throws AppParserException {
        AmountParser parser = AppParserFactory.getAmountParser();
        setParsedVslue(parser.parse(value));
        return getParsedValue();
    }

    public CodeValidationResult validate(String obj, ValidationCodeResolver resolver) throws ValidationException {

        reset();

        if (!isSet(obj)) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.VALUE_IS_NOT_SET, new StringArgument(obj))

            );
        }

        BigDecimal value;

        try {

            value = parse(obj);

        } catch (AppParserException e) {

            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.WRONG_NUMBER_FORMAT, new StringArgument(obj))

            );
        }


        if (!isPositiveValue(value)) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.INVALID_POSITIVE_VALUE, new StringArgument(obj))
            );
        }
        
        if (!isPrecisionScaleValid(value)) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.RANGE_OUT, new StringArgument(obj))
            );
        }

        return null;
    }

    @Override
    public CodeValidationResult validate(String obj) throws ValidationException {
        return validate(obj, new ExceptionResolver());
    }

    private void reset() {
        this.parsedValue = null;
    }

    private void setParsedVslue(BigDecimal parsedValue) {
        this.parsedValue = parsedValue;
    }

    public BigDecimal getParsedValue() {
        return parsedValue;
    }

}
