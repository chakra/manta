package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSiteUpdateExceptionResolver implements ValidationResolver<ValidationException> {

    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.SiteUpdateReason) {

                    switch ((ExceptionReason.SiteUpdateReason) code.getReason()) {
                        case NO_ACCOUNT_SET:
                            errors.add(isAccountNotSet(code));
                            break;
                        case EFFECTIVE_DATE_CANT_BY_AFTER_EXP_DATE:
                            errors.add(isEffDateCantBeAfterExpFate(code, (BusEntityData) code.getArguments()[0].get()));
                            break;
                        case CANT_SET_BOTH_ENABLE_INV_AND_ALLOW_CORP_SCH_ORDER:
                            errors.add(isCantSetBothEnableInvAndAllowCorpSchorder(code, (BusEntityData) code.getArguments()[0].get()));
                            break;
                        case ACCOUNT_DOES_NOT_HAVE_ERP_SYSTEMS:
                            errors.add(
                                    isAccountDoesNotHaveErpSystems(
                                            code,
                                            (Long) code.getArguments()[0].get(),
                                            (Long) code.getArguments()[1].get(),
                                            (BusEntityData) code.getArguments()[3].get()
                                    )
                            );
                            break;
                        case ACCOUNT_HAS_MULTIPLE_ERP_SYSTEMS:
                            errors.add(
                                    isAccountHasMultipleErpSystems(
                                            code,
                                            (Long) code.getArguments()[0].get(),
                                            (Long) code.getArguments()[1].get(),
                                            (BusEntityData) code.getArguments()[3].get()
                                    )
                            );
                            break;
                        case ACCOUNT_DOES_NOT_HAVE_ASSOCIATION_WITH_STORE:
                            errors.add(
                                    isAccountDoeNotHaveAssocWithStore(
                                            code,
                                            (Long) code.getArguments()[0].get(),
                                            (Long) code.getArguments()[1].get()
                                    )
                            );
                            break;
                        case SITE_MUST_BE_UNIQUE:
                            errors.add(isSiteNameNotUnique(code, (BusEntityData) code.getArguments()[0].get()));
                            break;
                    }

                }

            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isSiteNameNotUnique(ApplicationExceptionCode code, BusEntityData busEntityData);

    protected abstract ArgumentedMessage isAccountDoeNotHaveAssocWithStore(ApplicationExceptionCode code, Long storeId, Long accountId);

    protected abstract ArgumentedMessage isAccountDoesNotHaveErpSystems(ApplicationExceptionCode code, Long storeId, Long accountId, BusEntityData busEntityData);

    protected abstract ArgumentedMessage isAccountHasMultipleErpSystems(ApplicationExceptionCode code, Long storeId, Long accountId, BusEntityData busEntityData);

    protected abstract ArgumentedMessage isCantSetBothEnableInvAndAllowCorpSchorder(ApplicationExceptionCode code, BusEntityData busEntityData);

    protected abstract ArgumentedMessage isEffDateCantBeAfterExpFate(ApplicationExceptionCode code, BusEntityData busEntityData);

    protected abstract ArgumentedMessage isAccountNotSet(ApplicationExceptionCode code);


}
