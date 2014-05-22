package com.espendwise.manta.util.criteria;


import java.io.Serializable;

public class StoreAccountCriteria implements Serializable {

    private  Long storeId;
    private  Long accountId;
    private  Long userId;
    private  Long scheduleId;
    private  String name;
    private  String filterType;
    private  Boolean activeOnly;
    private  Integer limit;

    public StoreAccountCriteria() {
    }

    public Long getStoreId() {
        return storeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getUserId() {
        return userId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "StoreAccountCriteria{" +
                "storeId=" + storeId +
                ", userId=" + userId +
                ", scheduleId=" + scheduleId +
                ", name='" + name + '\'' +
                ", filterType='" + filterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit +
                '}';
    }

	
}
