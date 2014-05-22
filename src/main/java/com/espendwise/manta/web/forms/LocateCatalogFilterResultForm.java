package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.CatalogListView;

import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class LocateCatalogFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<CatalogListView>> {

    private SelectableObjects<CatalogListView> selectedCatalogs;
    private boolean multiSelected;

    public SelectableObjects<CatalogListView> getSelectedCatalogs() {
        return selectedCatalogs;
    }

    public void setSelectedCatalogs(SelectableObjects<CatalogListView> selectedCatalogs) {
        this.selectedCatalogs = selectedCatalogs;
    }

    @Override
    public List<SelectableObjects.SelectableObject<CatalogListView>> getResult() {
        return selectedCatalogs != null ? selectedCatalogs.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        selectedCatalogs = null;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }
}
