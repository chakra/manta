package com.espendwise.manta.util.validation.resolvers;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGenericErrorResolver implements ValidationCodeResolver {


    public AbstractGenericErrorResolver() {
    }

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        for(ValidationCode code : codes) {
            switch ( code.getReason()) {
                case      GENERIC_ERROR: 
                	errors.add(isDataValidationError(code, code.getArguments()));
                	break;
                default: break;
            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isDataValidationError(ApplicationExceptionCode code, TypedArgument[] args);

}