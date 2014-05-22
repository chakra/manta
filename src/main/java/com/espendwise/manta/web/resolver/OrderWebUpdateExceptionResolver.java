package com.espendwise.manta.web.resolver;


import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.resolvers.AbstractOrderUpdateExceptionResolver;

public class OrderWebUpdateExceptionResolver extends AbstractOrderUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isSiteNotFound(ApplicationExceptionCode code, Long siteId) {
        return new ArgumentedMessageImpl("validation.web.error.noSiteFoundForId", Args.typed(siteId));
    }
    @Override
    protected ArgumentedMessage isSiteNotUnique(ApplicationExceptionCode code, Long siteId) {
        return new ArgumentedMessageImpl("validation.web.error.fewSitesFoundForId", Args.typed(siteId));
    }
    @Override
    protected ArgumentedMessage isSiteNotActive(ApplicationExceptionCode code, Long siteId) {
        return new ArgumentedMessageImpl("validation.web.error.siteNotActiveForId", Args.typed(siteId));
    }
    @Override
    protected ArgumentedMessage isNoAccountForSite(ApplicationExceptionCode code, Long siteId) {
        return new ArgumentedMessageImpl("validation.web.error.noAccountFoundForSite", Args.typed(siteId));
    }
    @Override
    protected ArgumentedMessage isFewAccountsForSite(ApplicationExceptionCode code, Long siteId, String accountNames) {
        return new ArgumentedMessageImpl("validation.web.error.fewAccountsFoundForSite", Args.typed(siteId, accountNames));
    }
    @Override
    protected ArgumentedMessage isNoContractForSite(ApplicationExceptionCode code, Long siteId) {
        return new ArgumentedMessageImpl("validation.web.error.noContrectFoundForSite", Args.typed(siteId));
    }
    @Override
    protected ArgumentedMessage isFewContractsForSite(ApplicationExceptionCode code, Long siteId, String contractNames) {
        return new ArgumentedMessageImpl("validation.web.error.fewContractsFoundForSite", Args.typed(siteId, contractNames));
    }

}
