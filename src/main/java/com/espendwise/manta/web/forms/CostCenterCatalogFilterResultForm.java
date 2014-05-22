package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.util.SelectableObjects;

import java.util.List;

public class CostCenterCatalogFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<CatalogListView>> {

    private SelectableObjects<CatalogListView> catalogs;


    public SelectableObjects<CatalogListView> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(SelectableObjects<CatalogListView> catalogs) {
        this.catalogs = catalogs;
    }

    @Override
    public List<SelectableObjects.SelectableObject<CatalogListView>> getResult() {
        return catalogs != null ? catalogs.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        this.catalogs = null;
    }

}
