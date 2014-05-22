package com.espendwise.manta.web.resolver;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractManufacturerUpdateExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class ManufacturerWebUpdateExceptionResolver extends AbstractManufacturerUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isManufacturerNameNotUnique(ApplicationExceptionCode code, BusEntityData manufacturer) {
        return new ArgumentedMessageImpl("validation.web.error.manufacturerNotUnique", Args.typed(manufacturer.getShortDesc()));
    }
}
