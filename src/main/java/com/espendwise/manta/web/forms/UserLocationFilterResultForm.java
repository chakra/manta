package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class UserLocationFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<SiteListView>> {

    private SelectableObjects<SiteListView> sites;
    private String defaultLocation;

    public SelectableObjects<SiteListView> getSites() {
        return sites;
    }

    public void setSites(SelectableObjects<SiteListView> sites) {
        this.sites = sites;
    }

    public String getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(String defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    @Override
    public List<SelectableObjects.SelectableObject<SiteListView>> getResult() {
        return sites != null ? sites.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        this.sites = null;
        this.defaultLocation = null;
    }

}
