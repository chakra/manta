package com.espendwise.manta.util.criteria;


import java.io.Serializable;

public class StoreListEntityCriteria implements Serializable {
   
    private String storeName;
    private String storeNameFilterType;
    private Long globalEntityId;
    private Boolean showInactive;
    private Integer limit;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNameFilterType() {
        return storeNameFilterType;
    }

    public void setStoreNameFilterType(String storeNameFilterType) {
        this.storeNameFilterType = storeNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getGlobalEntityId() {
        return globalEntityId;
    }

    public void setGlobalEntityId(Long globalEntityId) {
        this.globalEntityId = globalEntityId;
    }

    @Override
    public String toString() {
        return "StoreListEntityCriteria{" +
                "storeName='" + storeName + '\'' +
                ", storeNameFilterType='" + storeNameFilterType + '\'' +
                ", globalEntityId='" + globalEntityId + '\'' +
                ", showInactive=" + showInactive +
                ", limit=" + limit +
                '}';
    }
}
