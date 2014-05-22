package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.ItemListView;
import java.util.List;

public class MasterItemFilterResultForm extends AbstractFilterResult<ItemListView> {

    private List<ItemListView> items;
    private Long cloneItemId;

    public MasterItemFilterResultForm() {
    }

    public Long getCloneItemId() {
        return cloneItemId;
    }

    public void setCloneItemId(Long id) {
        this.cloneItemId = id;
    }

    public List<ItemListView> getItems() {
        return items;
    }

    public void setItems(List<ItemListView> items) {
        this.items = items;
    }

    @Override
    public List<ItemListView> getResult() {
        return items;
    }

    @Override
    public void reset() {
        super.reset();
        this.items = null;
    }


}
