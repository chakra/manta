package com.espendwise.manta.web.resolver;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractSiteUpdateExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class SiteWebUpdateExceptionResolver extends AbstractSiteUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isSiteNameNotUnique(ApplicationExceptionCode code, BusEntityData busEntityData) {
        return new ArgumentedMessageImpl("validation.web.error.locationNotUnique", Args.typed(busEntityData.getShortDesc()));
    }

    @Override
    protected ArgumentedMessage isAccountDoeNotHaveAssocWithStore(ApplicationExceptionCode code, Long storeId, Long accountId) {
        return new ArgumentedMessageImpl("validation.web.error.accountDoesNotHaveAssocWithStore", Args.i18nTyped(accountId, storeId));
    }

    @Override
    protected ArgumentedMessage isAccountDoesNotHaveErpSystems(ApplicationExceptionCode code, Long storeId, Long accountId, BusEntityData busEntityData) {
        return new ArgumentedMessageImpl("validation.web.error.accountDoesNotHaveErpSystems", Args.i18nTyped(accountId, storeId));
    }

    @Override
    protected ArgumentedMessage isAccountHasMultipleErpSystems(ApplicationExceptionCode code, Long storeId, Long accountId, BusEntityData busEntityData) {
        return new ArgumentedMessageImpl("validation.web.error.accountHasMultipleErpSystems", Args.i18nTyped(accountId, storeId));
    }

    @Override
    protected ArgumentedMessage isCantSetBothEnableInvAndAllowCorpSchorder(ApplicationExceptionCode code, BusEntityData busEntityData) {
        return new ArgumentedMessageImpl("validation.web.error.locationCantSetBothEnableInvAndAllowCorpSchOrderProps",
                Args.i18nTyped("refcodes.PROPERTY_TYPE_CD.ALLOW_CORPORATE_SCHED_ORDER", "refcodes.PROPERTY_TYPE_CD.INVENTORY_SHOPPING"));
    }

    @Override
    protected ArgumentedMessage isEffDateCantBeAfterExpFate(ApplicationExceptionCode code, BusEntityData busEntityData) {
        return new ArgumentedMessageImpl("validation.web.error.wrongDateInterval",
                Args.i18nTyped("admin.site.label.effDate", "admin.site.label.expDate")
        );
    }

    @Override
    protected ArgumentedMessage isAccountNotSet(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.emptyValue", Args.i18nTyped("admin.site.label.accounte"));
    }
}
