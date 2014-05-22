package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.DistributorListView;

import java.util.List;

public class DistributorFilterResultForm  extends AbstractFilterResult<DistributorListView> {

    private List<DistributorListView> distributors;

    public DistributorFilterResultForm() {
    }

    public List<DistributorListView> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<DistributorListView> distributors) {
        this.distributors = distributors;
    }

    @Override
    public List<DistributorListView> getResult() {
        return distributors;
    }

    @Override
    public void reset() {
        super.reset();
        this.distributors = null;
    }


}
