package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUserUpdateExceptionResolver implements ValidationResolver<ValidationException> {


    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {
                if (code.getReason() instanceof ExceptionReason.UserUpdateReason) {
                    switch ((ExceptionReason.UserUpdateReason) code.getReason()) {
                        case USER_MUST_BE_UNIQUE:  errors.add(isUserNameNotUnique(code, (AllUserData) code.getArguments()[0].get()));       
                        case USER_MUST_BE_UNIQUE1:  errors.add(isUserNameNotUnique(code, (String) code.getArguments()[0].get()));       
                        break;
                    }
                }else if (code.getReason() instanceof ExceptionReason.UserAccountConfigUpdateReason) {
                    switch ((ExceptionReason.UserAccountConfigUpdateReason) code.getReason()) {
                    case USER_CONFIGURED_TO_LOCATION_OF_ACCOUNT:
                        errors.add(isUserConfiguredToLocationForAccount(code, (Long) code.getArguments()[0].get(), (String) code.getArguments()[1].get()));
                        break;
                    }            
                }
            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isUserNameNotUnique(ApplicationExceptionCode code, AllUserData allUser);
    protected abstract ArgumentedMessage isUserNameNotUnique(ApplicationExceptionCode code, String userName);

	protected abstract ArgumentedMessage isUserConfiguredToLocationForAccount(ApplicationExceptionCode code, Long accountId, String siteList);

}

