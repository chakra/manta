package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.List;

import com.espendwise.manta.model.view.ProductListView;

public class AccountShoppingControlItemViewCriteria implements Serializable {
    private Long accountId;
    private Long locationId;
    private List<ProductListView> items;
    private Boolean showUncontrolledItems;
    private Integer limit;

    public AccountShoppingControlItemViewCriteria(Long accountId, Integer limit) {
        this(accountId, new Long(0), limit);
    }

    public AccountShoppingControlItemViewCriteria(Long accountId, Long locationId, Integer limit) {
        this.accountId = accountId;
        this.locationId = locationId;
        this.limit = limit;
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

	public Boolean getShowUncontrolledItems() {
		return showUncontrolledItems;
	}

	public void setShowUncontrolledItems(Boolean showUncontrolledItems) {
		this.showUncontrolledItems = showUncontrolledItems;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
    public String toString() {
        return "AccountShoppingControlListViewCriteria{" +
                "accountId=" + accountId +
                "locationId=" + locationId +
                ", limit=" + limit + '\'' +
                '}';
    }

}
