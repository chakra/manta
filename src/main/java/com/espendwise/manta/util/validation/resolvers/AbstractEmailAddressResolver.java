package com.espendwise.manta.util.validation.resolvers;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEmailAddressResolver  implements ValidationCodeResolver {


    public AbstractEmailAddressResolver() {
    }

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        for (ValidationCode code : codes) {
            switch ( code.getReason()) {
                case      WRONG_EMAIL_ADDRESS_FORMAT  : errors.add(isWrongEmailAddressFormat(code));    break;
                default: break;
            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isWrongEmailAddressFormat(ValidationCode code)throws ValidationException ;

}