package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.view.FiscalCalendarIdentView;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

public class FiscalCalendarDependanciesRule implements ValidationRule {

    private static final Logger logger = Logger.getLogger(FiscalCalendarDependanciesRule.class);

    private FiscalCalendarIdentView calendar;

    public FiscalCalendarDependanciesRule(FiscalCalendarIdentView calendar) {
        this.calendar = calendar;
    }

    @Override
    public ValidationRuleResult apply() {
        ValidationRuleResult vrr = new ValidationRuleResult();
        vrr.success();
        return vrr;
    }
}