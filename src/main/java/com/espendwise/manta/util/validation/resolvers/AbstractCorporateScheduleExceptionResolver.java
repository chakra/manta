package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCorporateScheduleExceptionResolver implements ValidationResolver<ValidationException> {

    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {
        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();
        ApplicationExceptionCode[] codes = exception.getExceptionCodes();
        if (codes != null) {
            for (ApplicationExceptionCode code : codes) {
                if (code.getReason() instanceof ExceptionReason.CorporateScheduleUpdateReason) {
                    switch ((ExceptionReason.CorporateScheduleUpdateReason) code.getReason()) {
                        case CORPORATE_SCHEDULE_MUST_BE_UNIQUE:
                            errors.add(isCorporateScheduleNameNotUnique(code, (String) code.getArguments()[0].get()));
                            break;
                    }
                }
                else if (code.getReason() instanceof ExceptionReason.CorporateScheduleDeleteReason) {
                    switch ((ExceptionReason.CorporateScheduleDeleteReason) code.getReason()) {
                        case CORPORATE_SCHEDULE_CONFIGURED_TO_ACCOUNT:
                            errors.add(isCorporateScheduleConfiguredToAccount(code));
                            break;
                            case CORPORATE_SCHEDULE_CONFIGURED_TO_LOCATION:
                                errors.add(isCorporateScheduleConfiguredToLocation(code));
                                break;
                    }
                }
                else if (code.getReason() instanceof ExceptionReason.CorporateScheduleAccountConfigUpdateReason) {
                    switch ((ExceptionReason.CorporateScheduleAccountConfigUpdateReason) code.getReason()) {
                        case CORPORATE_SCHEDULE_CONFIGURED_TO_LOCATION_OF_ACCOUNT:
                            errors.add(isCorporateScheduleConfiguredToLocationForAccount(code, (Long) code.getArguments()[0].get(), (String) code.getArguments()[1].get()));
                            break;
                    }
                
                }
                else if (code.getReason() instanceof ExceptionReason.CorporateScheduleFilterRuleReason) {
                    switch ((ExceptionReason.CorporateScheduleFilterRuleReason) code.getReason()) {
                    case INCORRECT_SCHEDULE_DATE_FOUND:
                        errors.add(isCorporateScheduleFindIncorrectDates(code, (String) code.getArguments()[0].get()));
                        break;
                    }
                	
                }
            }
        }
        return errors;
    }

    protected abstract ArgumentedMessage isCorporateScheduleNameNotUnique(ApplicationExceptionCode code, String corporateScheduleName);

    protected abstract ArgumentedMessage isCorporateScheduleConfiguredToAccount(ApplicationExceptionCode code);

    protected abstract ArgumentedMessage isCorporateScheduleConfiguredToLocation(ApplicationExceptionCode code);

    protected abstract ArgumentedMessage isCorporateScheduleConfiguredToLocationForAccount(ApplicationExceptionCode code, Long accountId, String siteList);

    protected abstract ArgumentedMessage isCorporateScheduleFindIncorrectDates(ApplicationExceptionCode code, String ids);

}
