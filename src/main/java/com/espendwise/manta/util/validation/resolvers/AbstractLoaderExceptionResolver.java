package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLoaderExceptionResolver implements ValidationResolver<ValidationException> {

    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.LoaderReason) {

                    switch ((ExceptionReason.LoaderReason) code.getReason()) {
                        case GENERAL_ERROR:
                        	errors.add(isGeneralException(code, (String) code.getArguments()[0].get()));
                            break;
                    }

                }

            }
        }

        return errors;
    }

	protected abstract ArgumentedMessage isGeneralException(ApplicationExceptionCode code, String errorMsg);

}
