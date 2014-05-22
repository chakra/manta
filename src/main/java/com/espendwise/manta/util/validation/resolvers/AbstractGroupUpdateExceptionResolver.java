package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGroupUpdateExceptionResolver implements ValidationResolver<ValidationException> {

    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.GroupUpdateReason) {

                    switch ((ExceptionReason.GroupUpdateReason) code.getReason()) {
                        case GROUP_MUST_BE_UNIQUE:
                            errors.add(isGroupNameNotUnique(code, (String) code.getArguments()[0].get()));
                            break;
                        case GROUP_HAS_MULTI_STORE_ASSOCIATION:
                        	errors.add(isGroupHasMultiStoreAssociation(code, (String) code.getArguments()[0].get(), (String) code.getArguments()[1].get(), (String) code.getArguments()[2].get()));
                            break;
                        
                    }

                }

            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isGroupNameNotUnique(ApplicationExceptionCode code, String groupName);

	protected abstract ArgumentedMessage isGroupHasMultiStoreAssociation(
			ApplicationExceptionCode code, String groupName, String entityType,
			String assocStoreNames) ;

}
