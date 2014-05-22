package com.espendwise.manta.util.criteria;

import java.io.Serializable;

public class DistributorListViewCriteria implements Serializable {
    private Long storeId;
    private String  distributorId;
    private String  distributorName;
    private String  distributorNameFilterType;

    private Boolean activeOnly;

    private Integer limit;

    public DistributorListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }


    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public Boolean isActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorNameFilterType() {
        return distributorNameFilterType;
    }

    public void setDistributorNameFilterType(String distributorNameFilterType) {
        this.distributorNameFilterType = distributorNameFilterType;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    @Override
    public String toString() {
        return "DistributorListViewCriteria{" +
                "storeId=" + storeId +
                ", distributorId='" + distributorId + '\'' +
                ", distributorName='" + distributorName + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit + '\'' +
                '}';
    }

}
