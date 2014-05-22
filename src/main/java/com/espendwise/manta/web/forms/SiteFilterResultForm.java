package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.SiteListView;

import java.util.List;

public class SiteFilterResultForm  extends AbstractFilterResult<SiteListView> {

    private List<SiteListView> sites;

    public SiteFilterResultForm() {
    }

    public List<SiteListView> getSites() {
        return sites;
    }

    public void setSites(List<SiteListView> sites) {
        this.sites = sites;
    }

    @Override
    public List<SiteListView> getResult() {
        return sites;
    }

    @Override
    public void reset() {
        super.reset();
        this.sites = null;
    }


}
