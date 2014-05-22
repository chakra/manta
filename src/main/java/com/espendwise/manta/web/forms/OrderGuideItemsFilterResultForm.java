package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.model.view.OrderGuideItemView;

import java.util.List;

public class OrderGuideItemsFilterResultForm extends AbstractFilterResult<SelectableObjects.SelectableObject<OrderGuideItemView>> {

    
    private SelectableObjects<OrderGuideItemView> items;


    public SelectableObjects<OrderGuideItemView> getItems() {
		return items;
	}

	public void setItems(SelectableObjects<OrderGuideItemView> items) {
		this.items = items;
	}

	@Override
    public List<SelectableObjects.SelectableObject<OrderGuideItemView>> getResult() {
        return items != null ? items.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        this.items = null;
    }


}
