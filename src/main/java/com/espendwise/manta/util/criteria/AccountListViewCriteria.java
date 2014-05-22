package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.List;

public class AccountListViewCriteria implements Serializable {

    private Long storeId;
    private String  accountId;
    private String  accountName;
    private String  accountNameFilterType;
    private Boolean activeOnly;
    private String distrRefNumber;
    private String distrRefNumberFilterType;
    private Integer limit;
    private List<Long> accountsGroups;

    public AccountListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public String getAccountNameFilterType() {
        return accountNameFilterType;
    }

    public void setAccountNameFilterType(String accountNameFilterType) {
        this.accountNameFilterType = accountNameFilterType;
    }

    public String getDistrRefNumber() {
        return distrRefNumber;
    }

    public void setDistrRefNumber(String distrRefNumber) {
        this.distrRefNumber = distrRefNumber;
    }

    public String getDistrRefNumberFilterType() {
        return distrRefNumberFilterType;
    }

    public void setDistrRefNumberFilterType(String distrRefNumberFilterType) {
        this.distrRefNumberFilterType = distrRefNumberFilterType;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setAccountsGroups(List<Long> accountsGroups) {
        this.accountsGroups = accountsGroups;
    }

    public List<Long> getAccountsGroups() {
        return accountsGroups;
    }

    @Override
    public String toString() {
        return "AccountListViewCriteria{" +
                "storeId=" + storeId +
                ", accountName='" + accountName + '\'' +
                ", accountNameFilterType='" + accountNameFilterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", distrRefNumber='" + distrRefNumber + '\'' +
                ", distrRefNumberFilterType='" + distrRefNumberFilterType + '\'' +
                ", limit=" + limit +
                ", accountsGroups=" + accountsGroups +
                '}';
    }
}
