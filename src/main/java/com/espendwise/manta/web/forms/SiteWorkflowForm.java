package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.SiteWorkflowListView;
import com.espendwise.manta.spi.Initializable;

import java.util.List;
import java.util.Map;

public class SiteWorkflowForm extends AbstractFilterResult<SiteWorkflowListView>/*WebForm*/ implements  Initializable {

    private boolean initialize;

    private Long siteId;
    private Long accountId;

    private List<SiteWorkflowListView> workflows;
    private String selectedWorkflow;

    private Map<Long, String> userGroupMap = null;
    private Map<Long, String> applySkipGroupMap = null;

    public SiteWorkflowForm() {
    }

    public SiteWorkflowForm(Long siteId) {
        this.siteId = siteId;
    }

     public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }


    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setWorkflows(List<SiteWorkflowListView> workflows) {
        this.workflows = workflows;
    }

    public List<SiteWorkflowListView> getWorkflows() {
        return workflows;
    }

    public void setSelectedWorkflow(String v) {
        this.selectedWorkflow = v;
    }

    public String getSelectedWorkflow() {
        return this.selectedWorkflow;
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

   @Override
    public List<SiteWorkflowListView> getResult() {
        return workflows;
    }
    
    public void reset() {
    	//this.siteId = null;
    	//this.accountId = null;
    	//this.workflows = null;
    	this.selectedWorkflow = null;
   }

   @Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

}
