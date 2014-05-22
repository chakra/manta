package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAddressValidationCodeResolver implements ValidationCodeResolver {

    private static final Logger logger = Logger.getLogger(AbstractAddressValidationCodeResolver.class);

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        for(ValidationCode code : codes) {
             switch (code.getReason()) {
                case VALUE_IS_NOT_SET  : addErrors(errors, isNotSet((String) code.getArguments()[0].get(), code));  break;
                case RANGE_OUT: addErrors(errors, isRangeOut((String) code.getArguments()[0].get(), code));  break;
                case VALUE_MUST_BE_EMPTY: addErrors(errors, isMustBeEmpty((String) code.getArguments()[0].get(), code));  break;
                default: break;
            }
        }

        return errors;
    }


    protected void addErrors(List<ArgumentedMessage> errors, ArgumentedMessage message) {
        if (message != null) {
            errors.add(message);
        }
    }

    protected abstract ArgumentedMessage isNotSet(String field, ValidationCode code);

    protected abstract ArgumentedMessage isRangeOut(String field, ValidationCode code);

    protected abstract  ArgumentedMessage isMustBeEmpty(String field, ValidationCode code);

}
