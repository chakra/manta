package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.List;

public class ManufacturerListViewCriteria implements Serializable {
    private Long storeId;
    private String  manufacturerId;
    private String  manufacturerName;
    private String  manufacturerNameFilterType;

    private Boolean activeOnly;

    private Integer limit;

    public ManufacturerListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
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

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerNameFilterType() {
        return manufacturerNameFilterType;
    }

    public void setManufacturerNameFilterType(String manufacturerNameFilterType) {
        this.manufacturerNameFilterType = manufacturerNameFilterType;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    @Override
    public String toString() {
        return "ManufacturerListViewCriteria{" +
                "storeId=" + storeId +
                ", manufacturerId='" + manufacturerId + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit + '\'' +
                '}';
    }

}
