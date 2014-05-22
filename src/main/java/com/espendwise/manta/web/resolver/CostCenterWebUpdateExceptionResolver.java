package com.espendwise.manta.web.resolver;


import com.espendwise.manta.model.data.CostCenterData;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractCostCenterUpdateExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class CostCenterWebUpdateExceptionResolver extends AbstractCostCenterUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isCostCenterNameNotUnique(ApplicationExceptionCode code, CostCenterData costCenterData) {
        return new ArgumentedMessageImpl("validation.web.error.costCenterNotUnique", Args.typed(costCenterData.getShortDesc()));
    }

}
