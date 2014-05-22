package com.espendwise.manta.util.validation.resolvers;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOrderUpdateExceptionResolver implements ValidationResolver<ValidationException> {

    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {
        
        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();
        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {
            for (ApplicationExceptionCode code : codes) {
                if (code.getReason() instanceof ExceptionReason.OrderUpdateCheckNewSiteReason) {
                    switch ((ExceptionReason.OrderUpdateCheckNewSiteReason) code.getReason()) {
                        case SITE_NOT_FOUND: errors.add(isSiteNotFound(code, (Long) code.getArguments()[0].get()));
                            break;
                        case FEW_SITES_FOUND: errors.add(isSiteNotUnique(code, (Long) code.getArguments()[0].get()));       
                            break;
                        case NOT_ACTIVE_SITE_FOUND: errors.add(isSiteNotActive(code, (Long) code.getArguments()[0].get()));
                            break;
                        case NO_ACCOUNT_FOR_SITE_FOUND: errors.add(isNoAccountForSite(code, (Long) code.getArguments()[0].get()));
                            break;
                        case FEW_ACCOUNTS_FOR_SITE_FOUND: errors.add(isFewAccountsForSite(code,
                                                                                   (Long) code.getArguments()[0].get(),
                                                                                   (String) code.getArguments()[1].get()));
                            break;
                        case NO_CONTRACT_FOR_SITE_FOUND: errors.add(isNoContractForSite(code, (Long) code.getArguments()[0].get()));
                            break;
                        case FEW_CONTRACTS_FOR_SITE_FOUND: errors.add(isFewContractsForSite(code,
                                                                                           (Long) code.getArguments()[0].get(),
                                                                                           (String) code.getArguments()[1].get()));
                            break;
                    }
                }
            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isSiteNotFound(ApplicationExceptionCode code, Long siteId);
    
    protected abstract ArgumentedMessage isSiteNotUnique(ApplicationExceptionCode code, Long siteId);
    
    protected abstract ArgumentedMessage isSiteNotActive(ApplicationExceptionCode code, Long siteId);
    
    protected abstract ArgumentedMessage isNoAccountForSite(ApplicationExceptionCode code, Long siteId);
    
    protected abstract ArgumentedMessage isFewAccountsForSite(ApplicationExceptionCode code, Long siteId, String accountNames);

    protected abstract ArgumentedMessage isNoContractForSite(ApplicationExceptionCode code, Long siteId);
    
    protected abstract ArgumentedMessage isFewContractsForSite(ApplicationExceptionCode code, Long siteId, String contractNames);

}

