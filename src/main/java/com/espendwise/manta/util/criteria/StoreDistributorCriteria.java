package com.espendwise.manta.util.criteria;


import java.io.Serializable;
import java.util.List;

public class StoreDistributorCriteria implements Serializable {

    private  List<Long> distributorIds;
    private  List<String> distributorNames;
    private  Long storeId;

    public StoreDistributorCriteria() {
    }

    public List<Long> getDistributorIds() {
		return distributorIds;
	}

	public void setDistributorIds(List<Long> distributorIds) {
		this.distributorIds = distributorIds;
	}

	public List<String> getDistributorNames() {
		return distributorNames;
	}

	public void setDistributorNames(List<String> distributorNames) {
		this.distributorNames = distributorNames;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Override
    public String toString() {
        return "StoreDistributorCriteria {" +
                "storeId=" + storeId +
                ", distributorIds=" + distributorIds +
                ", distributorNames='" + distributorNames +
                '}';
    }
}
