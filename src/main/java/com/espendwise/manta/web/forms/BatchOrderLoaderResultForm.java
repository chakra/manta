package com.espendwise.manta.web.forms;

import java.util.List;

import com.espendwise.manta.model.view.BatchOrderView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.SelectableObjects.SelectableObject;


public class BatchOrderLoaderResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<BatchOrderView>> {

    private SelectableObjects<BatchOrderView> batchOrders;

    public BatchOrderLoaderResultForm() {
    }
    
    public void setBatchOrders(SelectableObjects<BatchOrderView> batchOrders) {
		this.batchOrders = batchOrders;
	}

	public SelectableObjects<BatchOrderView> getBatchOrders() {
		return batchOrders;
	}

    @Override
    public List<SelectableObjects.SelectableObject<BatchOrderView>> getResult() {
        return batchOrders != null ? batchOrders.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        batchOrders = null;
    }	

}
