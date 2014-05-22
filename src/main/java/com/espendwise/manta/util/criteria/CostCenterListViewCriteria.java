package com.espendwise.manta.util.criteria;


import java.io.Serializable;
import java.util.List;

public class CostCenterListViewCriteria implements Serializable {

    private Long costCenterId;
    private Integer limit;
    private Long storeId;
    private String costCenterName;
    private boolean activeOnly;
    private List<Long> accountIds;
    private List<Long> catalogIds;

    private String filterType;

    private String filterAccCatType;

    public CostCenterListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public void setCostCenterId(Long costCenterId) {
        this.costCenterId = costCenterId;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public void setCostCenterName(String costCenterName,String filterType) {
        this.costCenterName = costCenterName;
        this.filterType = filterType;
    }

    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public Long getCostCenterId() {
        return costCenterId;
    }

    public Integer getLimit() {
        return limit;
    }

    public Long getStoreId() {
        return storeId;
    }


    public String getCostCenterName() {
        return costCenterName;
    }

    public boolean isActiveOnly() {
        return activeOnly;
    }

    public void setAccountIds(List<Long> accountIds) {
        this.accountIds = accountIds;
    }

    public List<Long> getAccountIds() {
        return accountIds;
    }

    public void setCatalogIds(List<Long> catalogIds) {
        this.catalogIds = catalogIds;
    }

    public List<Long> getCatalogIds() {
        return catalogIds;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterAccCatType() {
        return filterAccCatType;
    }

    public void setFilterAccCatType(String filterAccCatType) {
        this.filterAccCatType = filterAccCatType;
    }


    @Override
    public String toString() {
        return "CostCenterListViewCriteria{" +
                "costCenterId=" + costCenterId +
                ", limit=" + limit +
                ", storeId=" + storeId +
                ", costCenterName='" + costCenterName + '\'' +
                ", filterType='" + filterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", filterAccCatType='" + filterAccCatType + "\'" +
                ", accountIds=" + accountIds +
                ", catalogIds=" + catalogIds +
                '}';
    }
}
