package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.ProductListView;

import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.LocateItemFilterResultFormValidator;

import java.util.List;

@Validation(LocateItemFilterResultFormValidator.class)
public class LocateItemFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<ProductListView>> {

    private SelectableObjects<ProductListView> selectedItems;
    private boolean multiSelected;

    public SelectableObjects<ProductListView> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(SelectableObjects<ProductListView> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @Override
    public List<SelectableObjects.SelectableObject<ProductListView>> getResult() {
        return selectedItems != null ? selectedItems.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        selectedItems = null;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }
}
