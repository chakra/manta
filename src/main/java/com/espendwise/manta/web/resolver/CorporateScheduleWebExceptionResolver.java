package com.espendwise.manta.web.resolver;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.resolvers.AbstractCorporateScheduleExceptionResolver;

public class CorporateScheduleWebExceptionResolver extends AbstractCorporateScheduleExceptionResolver {

    @Override
    protected ArgumentedMessage isCorporateScheduleNameNotUnique(ApplicationExceptionCode code, String scheduleName) {
        return new ArgumentedMessageImpl("validation.web.error.corporateScheduleNotUnique", Args.typed(scheduleName));
    }

    @Override
    protected ArgumentedMessage isCorporateScheduleConfiguredToAccount(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.corporateScheduleConfiguredToAccount");
    }

    @Override
    protected ArgumentedMessage isCorporateScheduleConfiguredToLocation(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.corporateScheduleConfiguredToLocation");
    }
    
    @Override
    protected ArgumentedMessage isCorporateScheduleConfiguredToLocationForAccount(ApplicationExceptionCode code, Long accountId, String siteList) {
        return new ArgumentedMessageImpl("validation.web.error.corporateScheduleCantRemoveAcctAssoc", Args.typed(accountId, siteList));
    }

    @Override
    protected ArgumentedMessage isCorporateScheduleFindIncorrectDates(ApplicationExceptionCode code, String ids) {
        return new ArgumentedMessageImpl("validation.web.error.corporateScheduleFinedIncorrectDates", Args.typed(ids));
    }
    
}
