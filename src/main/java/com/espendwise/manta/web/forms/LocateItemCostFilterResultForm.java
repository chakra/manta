package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.ItemContractCostView;
import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class LocateItemCostFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<ItemContractCostView>> {

    private SelectableObjects<ItemContractCostView> selectedItemCosts;
    private boolean multiSelected;

    public SelectableObjects<ItemContractCostView> getSelectedItemCosts() {
        return selectedItemCosts;
    }

    public void setSelectedItemCosts(SelectableObjects<ItemContractCostView> selectedItemCosts) {
        this.selectedItemCosts = selectedItemCosts;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    @Override
    public List<SelectableObjects.SelectableObject<ItemContractCostView>> getResult() {
        return selectedItemCosts != null ? selectedItemCosts.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        this.selectedItemCosts = null;
    }

}
