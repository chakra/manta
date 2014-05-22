package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.CostCenterListView;

import java.util.List;

public class CostCenterFilterResultForm  extends AbstractFilterResult<CostCenterListView> {

    private List<CostCenterListView> costCenters;

    public CostCenterFilterResultForm() {
    }

    public List<CostCenterListView> getCostCenters() {
        return costCenters;
    }

    public void setCostCenters(List<CostCenterListView> costCenters) {
        this.costCenters = costCenters;
    }

    @Override
    public List<CostCenterListView> getResult() {
        return costCenters;
    }

    @Override
    public void reset() {
        super.reset();
        this.costCenters = null;
    }


}
