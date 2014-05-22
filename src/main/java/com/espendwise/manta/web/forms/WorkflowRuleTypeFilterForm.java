package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.WorkflowRuleTypeFilterFormValidator;

import java.util.List;

@Validation(WorkflowRuleTypeFilterFormValidator.class)
public class WorkflowRuleTypeFilterForm extends WebForm implements Resetable, Initializable {

    private String ruleType;
    
    private Long accountId;
    private Long workflowId;
    private String workflowName;
    private String workflowType;
    private boolean orca;
    private boolean init;

    public WorkflowRuleTypeFilterForm() {
        super();
    }


	public boolean isOrca() {
		return RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(workflowType);
	}


	public String getWorkflowName() {
		return workflowName;
	}


	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}


	public Long getAccountId() {
		return accountId;
	}


	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}


	public Long getWorkflowId() {
		return workflowId;
	}


	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}


	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getWorkflowType() {
		return workflowType;
	}
	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	@Override
    public void reset() {
        this.ruleType = null;
    }

    @Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

}
