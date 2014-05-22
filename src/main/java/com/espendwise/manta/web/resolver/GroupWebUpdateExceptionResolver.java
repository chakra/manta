package com.espendwise.manta.web.resolver;

import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractGroupUpdateExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class GroupWebUpdateExceptionResolver extends AbstractGroupUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isGroupNameNotUnique(ApplicationExceptionCode code, String groupName) {
        return new ArgumentedMessageImpl("validation.web.error.groupNameNotUnique", Args.typed(groupName));
    }
    
    @Override
    protected ArgumentedMessage isGroupHasMultiStoreAssociation(ApplicationExceptionCode code, String groupName, String entityType, String assocStoreNames) {
        return new ArgumentedMessageImpl("validation.web.error.groupContainsEntityFromOtherStore", Args.typed(groupName, entityType, assocStoreNames));
    }

}
