package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.List;

public class AccountUniqueConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(AccountUniqueConstraint.class);

    private Long storeId;
    private BusEntityData accountData;

    public AccountUniqueConstraint(Long storeId, BusEntityData accountData) {
        this.storeId = storeId;
        this.accountData = accountData;
    }

    public Long getStoreId() {
        return storeId;
    }

    public BusEntityData getAccountData() {
        return accountData;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getAccountData() == null) {
            return null;
        }

        ValidationRuleResult result = new ValidationRuleResult();

        StoreAccountCriteria criteria = new StoreAccountCriteria();
        criteria.setStoreId(getStoreId());

        AccountService accountService = getAccountService();

        List<BusEntityData> dbAccounts = accountService.findStoreAccountBusDatas(criteria);
        if (dbAccounts != null && dbAccounts.size() > 0) {

            for (BusEntityData dbAccount : dbAccounts) {

                if (!Utility.strNN(getAccountData().getShortDesc()).equalsIgnoreCase(dbAccount.getShortDesc())) {
                    continue;
                }

                if (getAccountData().getBusEntityId() == null || (dbAccount.getBusEntityId().longValue() != getAccountData().getBusEntityId())) {

                    result.failed(
                            ExceptionReason.AccountUpdateReason.ACCOUNT_MUST_BE_UNIQUE,
                            new ObjectArgument<BusEntityData>(getAccountData())
                    );

                    return result;
                }
            }
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }


    public AccountService getAccountService() {
        return ServiceLocator.getAccountService();
    }
}
