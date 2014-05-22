package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.service.WorkflowService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.List;

public class WorkflowUniqueConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(WorkflowUniqueConstraint.class);

    private Long accountId;
    private String workflowName;
    private Long workflowId;
    private String workflowTypeCd;

    public WorkflowUniqueConstraint(Long accountId, String workflowName, Long workflowId, String workflowTypeCd) {
        this.accountId = accountId;
        this.workflowName = workflowName;
        this.workflowId = workflowId;
        this.workflowTypeCd = workflowTypeCd;

    }

    public Long getAccountId() {
		return accountId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public String getWorkflowTypeCd() {
		return workflowTypeCd;
	}

	public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getWorkflowName() == null) {
            return null;
        }

        ValidationRuleResult result = new ValidationRuleResult();

//        StoreAccountCriteria criteria = new StoreAccountCriteria();
//        criteria.setStoreId(getStoreId());

        WorkflowService workflowService = getWorkflowService();

        List<WorkflowData> workflows = workflowService.findWorkflowCollection(getAccountId());
        if (workflows != null && workflows.size() > 0) {

            for (WorkflowData workflow : workflows) {

                if (!Utility.strNN(getWorkflowName()).equalsIgnoreCase(workflow.getShortDesc())) {
                    continue;
                }

                if (getWorkflowId() == null 
                		|| (workflow.getWorkflowId().longValue() != getWorkflowId().longValue())
                		|| (!workflow.getWorkflowTypeCd().equals(getWorkflowTypeCd()) )) {

                    result.failed(
                            ExceptionReason.WorflowRuleUpdateReason.WORKFLOW_MUST_BE_UNIQUE,
                            new ObjectArgument<String>(getWorkflowName())
                    );

                    return result;
                }
            }
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }


    public WorkflowService getWorkflowService() {
        return ServiceLocator.getWorkflowService();
    }
}
