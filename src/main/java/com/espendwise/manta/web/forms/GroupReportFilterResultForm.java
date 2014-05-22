package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.GroupReportListView;
import com.espendwise.manta.util.SelectableObjects;

import java.util.List;

public class GroupReportFilterResultForm extends AbstractFilterResult<SelectableObjects.SelectableObject<GroupReportListView>> {

    private SelectableObjects<GroupReportListView> groupReports;


    public GroupReportFilterResultForm() {
        super();
    }

    public void setAccounts(SelectableObjects<GroupReportListView> groupReports) {
        this.groupReports = groupReports;
    }
    
    public void setGroupReports(SelectableObjects<GroupReportListView> groupReports) {
		this.groupReports = groupReports;
	}

	public SelectableObjects<GroupReportListView> getGroupReports() {
		return groupReports;
	}	

    @Override
    public List<SelectableObjects.SelectableObject<GroupReportListView>> getResult() {
        return groupReports != null ? groupReports.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        groupReports = null;
    }
}
