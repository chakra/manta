package com.espendwise.manta.util.arguments.resolvers;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNumberResolver implements ValidationCodeResolver {

    
    public AbstractNumberResolver() {
    }

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();
       
        for(ValidationCode code : codes) {
            switch (code.getReason()) {
                case       VALUE_IS_NOT_SET  : errors.add(isNotSet(code));               break;
                case  INVALID_POSITIVE_VALUE : errors.add(isInvalidPositive(code));      break;
                case      WRONG_NUMBER_FORMAT: errors.add(isInvalidNumberFormat(code));  break;
                default: break;
            }
        }

        return errors;
    }

    public  abstract ArgumentedMessage isInvalidNumberFormat(ValidationCode code) throws ValidationException ;

    public  abstract ArgumentedMessage isInvalidPositive(ValidationCode code) throws ValidationException ;

    public  abstract ArgumentedMessage isNotSet(ValidationCode code) throws ValidationException ;
}
