package com.espendwise.manta.web.forms;

import java.util.List;

import com.espendwise.manta.model.view.CatalogManagerView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.SelectableObjects.SelectableObject;


public class CatalogLoaderResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<CatalogManagerView>> {

    private SelectableObjects<CatalogManagerView> catalogs;

    public CatalogLoaderResultForm() {
    }
    
    public void setCatalogs(SelectableObjects<CatalogManagerView> catalogs) {
		this.catalogs = catalogs;
	}

	public SelectableObjects<CatalogManagerView> getCatalogs() {
		return catalogs;
	}

    @Override
    public List<SelectableObjects.SelectableObject<CatalogManagerView>> getResult() {
        return catalogs != null ? catalogs.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        catalogs = null;
    }	

}
