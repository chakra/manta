package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.validation.resolvers.ExceptionResolver;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;

public class FilterResultValidator implements CustomValidator<Integer, ValidationCodeResolver> {

    private Integer limit;

    public FilterResultValidator(Integer limit) {
        this.limit = limit;
    }

    public boolean isSizeOut(Integer value) throws AppParserException {
        return limit != null && value.intValue() >= limit.intValue();
    }

    public CodeValidationResult validate(Integer obj, ValidationCodeResolver resolver) throws ValidationException {


        if (obj != null && isSizeOut(obj)) {

            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.RESULT_SIZE_LIMIT_OUT,  new NumberArgument(getLimit()))
            );
        }

        return null;
    }

    @Override
    public CodeValidationResult validate(Integer obj) throws ValidationException {
        return validate(obj, new ExceptionResolver());
    }


    public Integer getLimit() {
        return limit;
    }

 }
