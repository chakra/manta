package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStoreMessageUpdateExceptionResolver implements ValidationResolver<ValidationException> {


    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.StoreMessageUpdateReason) {

                    switch ((ExceptionReason.StoreMessageUpdateReason) code.getReason()) {
                    	case STORE_MESSAGE_NAME_MUST_BE_UNIQUE:            errors.add(isMessageNameNotUnique(code));       break;
                        case STORE_MESSAGE_TITLE_MUST_BE_UNIQUE:            errors.add(isMessageTitleNotUnique(code));       break;
                        case STORE_MESSAGE_POSTED_DATE_BEFORE_CURRENT_DATE: errors.add(isPostedDateBeforeCurrentDate(code)); break;
                        case STORE_MESSAGE_END_DATE_BEFORE_CURRENT_DATE:    errors.add(isEndDateBeforeCurrentDate(code));    break;
                        case STORE_MESSAGE_END_DATE_BEFORE_POSTED_DATE:     errors.add(isEndDateBeforePostedDate(code));     break;
                        case FORCE_READ_COUNT_LESS_THAN_ORIGINAL_VALUE:     errors.add(isForceReadCountLessThanOriginal(code));     break;
                    }
                }

            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isEndDateBeforeCurrentDate(ApplicationExceptionCode code);

    protected abstract ArgumentedMessage isEndDateBeforePostedDate(ApplicationExceptionCode code);

    protected abstract ArgumentedMessage isPostedDateBeforeCurrentDate(ApplicationExceptionCode code);

    protected abstract ArgumentedMessage isMessageTitleNotUnique(ApplicationExceptionCode code);
    
    protected abstract ArgumentedMessage isMessageNameNotUnique(ApplicationExceptionCode code);
    
    protected abstract ArgumentedMessage isForceReadCountLessThanOriginal(ApplicationExceptionCode code);


}

