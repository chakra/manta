package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class LocateSiteFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<SiteListView>> {

    private SelectableObjects<SiteListView> selectedSites;
    private boolean multiSelected;

    public SelectableObjects<SiteListView> getSelectedSites() {
        return selectedSites;
    }

    public void setSelectedSites(SelectableObjects<SiteListView> selectedSites) {
        this.selectedSites = selectedSites;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    @Override
    public List<SelectableObjects.SelectableObject<SiteListView>> getResult() {
        return selectedSites != null ? selectedSites.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        this.selectedSites = null;
    }

}
