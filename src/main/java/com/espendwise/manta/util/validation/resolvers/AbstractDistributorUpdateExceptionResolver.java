package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDistributorUpdateExceptionResolver implements ValidationResolver<ValidationException> {


    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.DistributorUpdateReason) {

                    switch ((ExceptionReason.DistributorUpdateReason) code.getReason()) {
                        case DISTRIBUTOR_MUST_BE_UNIQUE:  errors.add(isDistributorNameNotUnique(code, (BusEntityData) code.getArguments()[0].get()));       
                        break;
                    }

                }

            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isDistributorNameNotUnique(ApplicationExceptionCode code, BusEntityData manufacturer);


}

