package com.espendwise.manta.web.resolver;


import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractUserUpdateExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class UserWebUpdateExceptionResolver extends AbstractUserUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isUserNameNotUnique(ApplicationExceptionCode code, AllUserData allUser) {
        return new ArgumentedMessageImpl("validation.web.error.userNotUnique", Args.typed(allUser.getUserName()));
    }
    @Override
    protected ArgumentedMessage isUserNameNotUnique(ApplicationExceptionCode code, String userName) {
        return new ArgumentedMessageImpl("validation.web.error.userNotUnique", Args.typed(userName));
    }
    
    @Override
    protected ArgumentedMessage isUserConfiguredToLocationForAccount(ApplicationExceptionCode code, Long accountId, String siteList) {
        return new ArgumentedMessageImpl("validation.web.error.cantRemoveUserAcctAssoc", Args.typed(accountId, siteList));
    }

}
