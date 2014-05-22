package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.web.forms.AbstractFilterResult;

import java.util.List;


public class LocateLocationFilterResultForm extends AbstractFilterResult<SelectableObjects.SelectableObject<SiteListView>> {


    private  SelectableObjects<SiteListView> sites;
    private boolean multiSelected;

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean isMultiSelected() {
        return multiSelected;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }

    public LocateLocationFilterResultForm() {
    }

    public SelectableObjects<SiteListView> getSites() {
        return sites;
    }

    public void setSites(SelectableObjects<SiteListView> sites) {
        this.sites = sites;
    }

    @Override
    public List<SelectableObjects.SelectableObject<SiteListView>> getResult() {
        return sites != null ? sites.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        this.sites = null;
    }


}
