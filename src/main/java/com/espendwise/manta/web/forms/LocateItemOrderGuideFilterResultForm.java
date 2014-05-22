package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.model.view.OrderGuideItemView;
import com.espendwise.manta.model.view.ProductListView;

import java.util.List;

public class LocateItemOrderGuideFilterResultForm extends AbstractFilterResult<SelectableObjects.SelectableObject<ProductListView>> {

    private SelectableObjects<ProductListView> selectedItems;
    private Long catalogId;
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
    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }
    
}
