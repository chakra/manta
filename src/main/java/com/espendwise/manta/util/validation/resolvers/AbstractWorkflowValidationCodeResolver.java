package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractWorkflowValidationCodeResolver  implements ValidationResolver<ValidationException> {


    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.WorflowRuleUpdateReason) {

                    switch ((ExceptionReason.WorflowRuleUpdateReason) code.getReason()) {
                    	case WORKFLOW_MUST_BE_UNIQUE:  errors.add(workflowNotUnique(code, (String) code.getArguments()[0].get()));       break;
                        case WORKFLOW_RULE_IN_USE_DEL:  errors.add(workflowRuleInUseDel(code, (Set) code.getArguments()[0].get()));       break;
                        case WORKFLOW_RULE_IN_USE_UPD:  errors.add(workflowRuleInUseUpd(code, (Set) code.getArguments()[0].get()));       break;
                        case WORKFLOW_NO_RULES_SELECTED:  errors.add(workflowNoRulesSelected(code));       break;
                        case WORKFLOW_RULE_SKU_NOT_FOUND:  errors.add(workflowRuleSkuNotFound(code, (Set) code.getArguments()[0].get()));       break;

                    }

                }

            }
        }

        return errors;
    }


    protected abstract ArgumentedMessage workflowNotUnique(ApplicationExceptionCode code, String workflowName);
    protected abstract ArgumentedMessage workflowRuleInUseDel(ApplicationExceptionCode code, Set<Long> ruleIds);
    protected abstract ArgumentedMessage workflowRuleInUseUpd(ApplicationExceptionCode code, Set<Long> ruleIds);
    protected abstract ArgumentedMessage workflowNoRulesSelected(ApplicationExceptionCode code);
    protected abstract ArgumentedMessage workflowRuleSkuNotFound(ApplicationExceptionCode code, Set<Long> skuNumbers);

}
