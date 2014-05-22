package com.espendwise.manta.web.forms;


import java.util.Map;
import java.util.Set;
import java.util.List;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.WorkflowFormValidator;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.WorkflowRuleData;

@Validation(WorkflowFormValidator.class)
public class WorkflowForm  extends WebForm implements Initializable {

    private Long workflowId;

    private Long accountId;
    private String workflowName;
    private String workflowTypeCd;
    private String workflowStatus;
    private String workflowRuleTypeCdToEdit;
    private List<GroupData> userGroups = null;
    private Map<Long, String> userGroupMap = null;
    private Map<Long, String> applySkipGroupMap = null;
   
    private boolean initialize;
  //  private List<WorkflowRuleData> rules;

 
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

    public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getWorkflowTypeCd() {
		return workflowTypeCd;
	}

	public void setWorkflowTypeCd(String workflowTypeCd) {
		this.workflowTypeCd = workflowTypeCd;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getWorkflowRuleTypeCdToEdit() {
		return workflowRuleTypeCdToEdit;
	}

	public void setWorkflowRuleTypeCdToEdit(String workflowRuleTypeCdToEdit) {
		this.workflowRuleTypeCdToEdit = workflowRuleTypeCdToEdit;
	}

	
	public Map<Long, String> getUserGroupMap() {
		return userGroupMap;
	}

	public void setUserGroupMap(Map<Long, String> userGroupMap) {
		this.userGroupMap = userGroupMap;
	}

	public Map<Long, String> getApplySkipGroupMap() {
		return applySkipGroupMap;
	}

	public void setApplySkipGroupMap(Map<Long, String> applySkipGroupMap) {
		this.applySkipGroupMap = applySkipGroupMap;
	}

	public List<GroupData> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<GroupData> userGroups) {
		this.userGroups = userGroups;
	}

	public boolean isInitialize() {
		return initialize;
	}

	public void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}
   @Override
    public void initialize() {
        initialize = true;
    }
   @Override
   public boolean isInitialized() {
       return  initialize;
   }
   public boolean getIsNew() {
       return isNew();
   }

   public boolean isNew() {
     return isInitialized() && (workflowId  == null || workflowId == 0);
   }


}
