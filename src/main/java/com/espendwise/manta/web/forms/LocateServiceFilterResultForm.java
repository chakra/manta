package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.ServiceListView;
import com.espendwise.manta.util.SelectableObjects;

import java.util.List;

public class LocateServiceFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<ServiceListView>> {

    private SelectableObjects<ServiceListView> selectedServices;
    private boolean multiSelected;

    public SelectableObjects<ServiceListView> getSelectedServices() {
        return selectedServices;
    }

    public void setSelectedServices(SelectableObjects<ServiceListView> selectedServices) {
        this.selectedServices = selectedServices;
    }

    @Override
    public List<SelectableObjects.SelectableObject<ServiceListView>> getResult() {
        return selectedServices != null ? selectedServices.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        selectedServices = null;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }
}
