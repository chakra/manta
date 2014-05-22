package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.DistributorListView;

import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class LocateDistributorFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<DistributorListView>> {

    private SelectableObjects<DistributorListView> selectedDistributors;
    private boolean multiSelected;

    public SelectableObjects<DistributorListView> getSelectedDistributors() {
        return selectedDistributors;
    }

    public void setSelectedDistributors(SelectableObjects<DistributorListView> selectedDistributors) {
        this.selectedDistributors = selectedDistributors;
    }

    @Override
    public List<SelectableObjects.SelectableObject<DistributorListView>> getResult() {
        return selectedDistributors != null ? selectedDistributors.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        selectedDistributors = null;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }
}
