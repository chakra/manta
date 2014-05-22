package com.espendwise.manta.web.resolver;


import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.spi.Resolver;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

import java.util.ArrayList;
import java.util.List;

public class DatabaseWebUpdateExceptionResolver implements Resolver<DatabaseUpdateException, List<ArgumentedMessage>> {

    @Override
    public  List<ArgumentedMessage>resolve(DatabaseUpdateException exc) throws RuntimeException {

        List<ArgumentedMessage>  errors = new ArrayList<ArgumentedMessage>();

        if (exc.getExceptionCodes()!=null && exc.getExceptionCodes().length>0) {

            ApplicationExceptionCode[] exceptionCodes = exc.getExceptionCodes();
            for (ApplicationExceptionCode code : exceptionCodes) {
                errors.add(new ArgumentedMessageImpl("exception.web.error.code." + code.getReason(), code.getArguments()));
            }

        } else if (exc.getMessage() != null) {

            throw new ApplicationRuntimeException(new DatabaseUpdateException(exc.getMessage(), exc.getCause()));

        } else {

            throw new ApplicationRuntimeException(new DatabaseUpdateException(exc.getCause()));

        }


        return errors;


    }
}
