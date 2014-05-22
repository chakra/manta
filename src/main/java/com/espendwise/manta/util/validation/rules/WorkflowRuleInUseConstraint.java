package com.espendwise.manta.util.validation.rules;

import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.data.WorkflowQueueData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;

import java.util.*;


public class WorkflowRuleInUseConstraint implements ValidationRule {


    private List<Long> ruleIds;
    private Long workflowId;
    private String workflowType;
    private boolean toDelete;

    public WorkflowRuleInUseConstraint(Long workflowId, String workflowType, List<Long> ruleIds, boolean toDelete) {
        this.ruleIds = ruleIds;
        this.workflowId = workflowId;
        this.workflowType = workflowType;
        this.toDelete = toDelete;
    }


    public List<Long> getRuleIds() {
        return ruleIds;
    }

    @Override
    public ValidationRuleResult apply() {

        ValidationRuleResult vr = new ValidationRuleResult();
        Set<String> errors = new HashSet<String>();
        if (isToDelete() && (getRuleIds() == null || getRuleIds().isEmpty())) {

            vr.failed(
                    ExceptionReason.WorflowRuleUpdateReason.WORKFLOW_NO_RULES_SELECTED,
                    new ObjectArgument<Set>(errors)
            );

            return vr;

        }
 
        List<WorkflowRuleData> used = ServiceLocator.getWorkflowService().findRulesInUse(
                getWorkflowId(),
                getWorkflowType(),
                getRuleIds()
        );

 
        if (used != null) {

            for (WorkflowRuleData u : used) {

                String rNum = u.getRuleSeq().toString();
                errors.add(rNum);

            }
        }

        if (!errors.isEmpty()) {

            vr.failed(
                    ((isToDelete()) ? ExceptionReason.WorflowRuleUpdateReason.WORKFLOW_RULE_IN_USE_DEL : ExceptionReason.WorflowRuleUpdateReason.WORKFLOW_RULE_IN_USE_UPD),
                    new ObjectArgument<Set>(errors)
            );

            return vr;

        }

        vr.success();

        return vr;

    }

    public boolean isToDelete() {
		return toDelete;
	}


	public Long getWorkflowId() {
        return workflowId;
    }
    public String getWorkflowType() {
        return workflowType;
    }
}
