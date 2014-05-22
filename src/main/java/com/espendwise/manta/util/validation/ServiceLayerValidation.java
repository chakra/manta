package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ApplicationIllegalArgumentException;
import com.espendwise.manta.util.trace.ExceptionReason;

import java.util.ArrayList;
import java.util.List;

public class ServiceLayerValidation {

    List<ValidationRule> rules;

    public ServiceLayerValidation() {
        this.rules = new ArrayList<ValidationRule>();
    }

    public boolean validate() throws ValidationException {
        for (ValidationRule rule : getRules()) {

            ValidationRuleResult result = rule.apply();
            if (result == null) {
                throw new ApplicationIllegalArgumentException(ExceptionReason.SystemReason.ILLEGAL_VALIDATION_RESULT);
            }

            if (result.isFailed()) {
                List<ApplicationExceptionCode> codes = result.getCodes();
                
                ApplicationExceptionCode[] codesArray = codes.toArray(new ApplicationExceptionCode[codes.size()]);
                throw new ValidationException(codesArray);
            }
        }

        return true;
    }

    public List<ValidationRule> getRules() {
        return rules;
    }

    public void addRule(ValidationRule rule) {
        rules.add(rule);
    }
}
