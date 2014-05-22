package com.espendwise.manta.util.trace;


import com.espendwise.manta.spi.Resolver;
import com.espendwise.manta.util.alert.ArgumentedMessage;

public abstract class AbstractSystemReasonResolver implements Resolver<ApplicationExceptionCode, ArgumentedMessage> {


    @Override
    public ArgumentedMessage resolve(ApplicationExceptionCode code) throws RuntimeException {

        if (code != null) {

            if (code.getReason() instanceof ExceptionReason.SystemReason) {

                switch ((ExceptionReason.SystemReason) code.getReason()) {

                    case APPLICATION_ILLEGAL_ACCESS_EXCEPTION:  return isApplicationIllegalAccessException(code);
                    case USER_DOES_NOT_HAVE_ACCESS_TO_INSTANCE:  return isUserDoesNotHaveAccessToInstance(code);
                    case USER_DOES_NOT_HAVE_ACCESS_TO_STORE:   return  isUserDoesNotHaveAccessToStore(code);
                    case ILLEGAL_VALIDATION_RESULT:    return isIllegalValidationResult(code);

                }
            }
        }

        return null;

    }

    public abstract ArgumentedMessage isIllegalValidationResult(ApplicationExceptionCode code);

    public abstract ArgumentedMessage isUserDoesNotHaveAccessToStore(ApplicationExceptionCode code);

    public abstract ArgumentedMessage isUserDoesNotHaveAccessToInstance(ApplicationExceptionCode code);

    public abstract ArgumentedMessage isApplicationIllegalAccessException(ApplicationExceptionCode code);
}
