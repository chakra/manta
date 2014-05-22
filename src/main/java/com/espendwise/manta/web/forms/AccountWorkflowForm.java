package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.spi.Initializable;

import java.util.List;

public class AccountWorkflowForm extends AbstractFilterResult<WorkflowData> {

    private Long accountId;

    private List<WorkflowData> workflows;

    public AccountWorkflowForm(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setWorkflows(List<WorkflowData> workflows) {
        this.workflows = workflows;
    }

    public List<WorkflowData> getWorkflows() {
        return workflows;
    }


    @Override
    public List<WorkflowData> getResult() {
        return workflows;
    }
    @Override
    public void reset() {
    	Exception e = new Exception();
    	e.printStackTrace();
/*    	super.reset();
        this.workflows = null;
*/    }
}
