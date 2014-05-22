package com.espendwise.manta.util.criteria;


import java.io.Serializable;

public class StoreManufacturerCriteria implements Serializable {

    private  Long storeId;
    private  Long manufacturerId;
    private  String name;
    private  String filterType;
    private  Boolean activeOnly;
    private  Integer limit;

    public StoreManufacturerCriteria() {
    }

    public Long getStoreId() {
        return storeId;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "StoreManufacturerCriteria{" +
                "storeId=" + storeId +
                ", name='" + name + '\'' +
                ", filterType='" + filterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit +
                '}';
    }
}
