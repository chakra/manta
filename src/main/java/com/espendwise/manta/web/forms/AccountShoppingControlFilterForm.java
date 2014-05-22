package com.espendwise.manta.web.forms;


import java.util.List;

import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.web.util.LocateAssistant;

public class AccountShoppingControlFilterForm extends WebForm implements Resetable, Initializable {

    private Long accountId;
    private Long locationId;
    private List<ProductListView> items;
    private Boolean showUncontrolledItems;
    
    private boolean init;

    public AccountShoppingControlFilterForm() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public List<ProductListView> getItems() {
		return items;
	}

	public void setItems(List<ProductListView> items) {
		this.items = items;
	}
    
    public String getFilteredItemCommaSkus() {
        return LocateAssistant.getFilteredItemCommaSkus(
        		getItems());
    }

    public Boolean getShowUncontrolledItems() {
		return showUncontrolledItems;
	}

	public void setShowUncontrolledItems(Boolean showUncontrolledItems) {
		this.showUncontrolledItems = showUncontrolledItems;
	}

	@Override
    public void initialize() {
        reset();
        init = true;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }

    @Override
    public void reset() {
        this.accountId = null;
        this.items = null;
        this.showUncontrolledItems = null;
    }
}
