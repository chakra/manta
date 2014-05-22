package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.parser.AppParser;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.AppParserFactory;
import com.espendwise.manta.util.validation.resolvers.ExceptionResolver;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;

public class DoubleValidator implements CustomValidator<String, ValidationCodeResolver> {

    private Double parsedValue;

    public boolean isSet(String value) {
        return value != null && value.trim().length() > 0;
    }

    public boolean isPositiveValue(Double value) {
        return value >= 0;
    }

    public Double parse(String value) throws AppParserException {
        AppParser<Double> parser = AppParserFactory.getParser(Double.class);
        setParsedValue(parser.parse(value));
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

        Double value;

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

        return null;
    }

    @Override
    public CodeValidationResult validate(String obj) throws ValidationException {
        return validate(obj, new ExceptionResolver());
    }

    private void reset() {
        this.parsedValue = null;
    }

    private void setParsedValue(Double parsedValue) {
        this.parsedValue = parsedValue;
    }

    public Double getParsedValue() {
        return parsedValue;
    }

}
