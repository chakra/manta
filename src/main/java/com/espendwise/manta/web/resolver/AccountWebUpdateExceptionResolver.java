package com.espendwise.manta.web.resolver;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractAccountUpdateExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class AccountWebUpdateExceptionResolver extends AbstractAccountUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isMultipleAccountCatalogs(ApplicationExceptionCode code, Long accountId) {
        return null;
    }

    @Override
    protected ArgumentedMessage isAccountNameNotUnique(ApplicationExceptionCode code, BusEntityData account) {
        return new ArgumentedMessageImpl("validation.web.error.accountNotUnique", Args.typed(account.getShortDesc()));
    }
}
