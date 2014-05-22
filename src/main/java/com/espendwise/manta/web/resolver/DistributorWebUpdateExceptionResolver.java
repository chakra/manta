package com.espendwise.manta.web.resolver;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.resolvers.AbstractDistributorUpdateExceptionResolver;

public class DistributorWebUpdateExceptionResolver extends AbstractDistributorUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isDistributorNameNotUnique(ApplicationExceptionCode code, BusEntityData distributor) {
        return new ArgumentedMessageImpl("validation.web.error.distributorNotUnique", Args.typed(distributor.getShortDesc()));
    }
}
