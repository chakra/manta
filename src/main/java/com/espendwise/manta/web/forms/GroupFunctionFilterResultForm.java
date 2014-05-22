package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.GroupFunctionListView;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupFunctionFilterResultForm extends AbstractFilterResult<SelectableObjects.SelectableObject<GroupFunctionListView>> {

    private Map<String,SelectableObjects<GroupFunctionListView>> functionsByType;
    private List<SelectableObjects.SelectableObject<GroupFunctionListView>> result;


    public GroupFunctionFilterResultForm() {
        super();
    }
    
	public void setFunctionsByType(Map<String,SelectableObjects<GroupFunctionListView>> functionsByType) {
		this.functionsByType = functionsByType;
	}

	public Map<String,SelectableObjects<GroupFunctionListView>> getFunctionsByType() {
		return functionsByType;
	}
	
	public SelectableObjects<GroupFunctionListView> getFunctionsByType(String functionType) {
		return functionsByType.get(functionType);
	}
		
	@Override
    public List<SelectableObjects.SelectableObject<GroupFunctionListView>> getResult() {
        return result;
    }
	
	public void setResult(List<SelectableObjects.SelectableObject<GroupFunctionListView>> result) {
		this.result = result;
	}
	
    @Override
    public void reset() {
        super.reset();
        functionsByType = null;
    }
	
}
