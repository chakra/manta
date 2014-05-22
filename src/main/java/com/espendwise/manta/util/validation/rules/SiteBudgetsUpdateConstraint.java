package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.view.BudgetView;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.List;

public class SiteBudgetsUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(SiteBudgetsUpdateConstraint.class);

    private Long siteId;
    private Long storeId;
    private List<BudgetView> budgets;

    public SiteBudgetsUpdateConstraint(Long storeId, Long siteId, List<BudgetView> budgets) {
        this.storeId = storeId;
        this.siteId = siteId;
        this.budgets = budgets;
    }

    @Override
    public ValidationRuleResult apply() {
        ValidationRuleResult vrr = new ValidationRuleResult();
        vrr.success();
        return vrr;
    }

}