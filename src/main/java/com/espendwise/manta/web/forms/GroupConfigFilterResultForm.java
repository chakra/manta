package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.GroupConfigAllListView;
import com.espendwise.manta.model.view.GroupConfigListView;
import com.espendwise.manta.util.SelectableObjects;

import java.util.List;
import java.util.Map;

public class GroupConfigFilterResultForm extends AbstractFilterResult<SelectableObjects.SelectableObject<GroupConfigListView>> {

    private SelectableObjects<GroupConfigListView> groupConfigs;
    private List<GroupConfigAllListView> groupAllEntityConfigs;
    private Map<Long, List<String>> primaryEntitiesMap;
    private List listResult; // hold the result list for sorting

    public GroupConfigFilterResultForm() {
        super();
    }
    
	public void setGroupConfigs(SelectableObjects<GroupConfigListView> groupConfigs) {
		this.groupConfigs = groupConfigs;
		List listResult = groupConfigs == null ? null : groupConfigs.getSelectableObjects();
		setResult(listResult);
	}

	public SelectableObjects<GroupConfigListView> getGroupConfigs() {
		return groupConfigs;
	}
	
	public void setGroupAllEntityConfigs(List<GroupConfigAllListView> groupAllEntityConfigs) {
		this.groupAllEntityConfigs = groupAllEntityConfigs;		
	}

	public List<GroupConfigAllListView> getGroupAllEntityConfigs() {
		return groupAllEntityConfigs;
	}
	
	@Override
    public List getResult() {
		return listResult;
    }
	
	public void setResult(List listResult) {
		this.listResult = listResult;
	}
	
    @Override
    public void reset() {
        super.reset();
        groupConfigs = null;
    }
}
