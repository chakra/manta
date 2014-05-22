package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.util.SelectableObjects;

import java.util.List;

public class WorkflowRuleFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<WorkflowRuleData>> {

    //private List<WorkflowRuleData> rules;
    private SelectableObjects<WorkflowRuleData> rules;
    

    public SelectableObjects<WorkflowRuleData> getRules() {
		return rules;
	}

	public void setRules(SelectableObjects<WorkflowRuleData> rules) {
		this.rules = rules;
	}

	@Override
    public List<SelectableObjects.SelectableObject<WorkflowRuleData>> getResult() {
        return rules != null ? rules.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        this.rules = null;
    }


}
