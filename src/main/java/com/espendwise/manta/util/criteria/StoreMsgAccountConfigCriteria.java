package com.espendwise.manta.util.criteria;


import java.io.Serializable;

public class StoreMsgAccountConfigCriteria implements Serializable {

    private  Long storeId;
    private  Long storeMessageId;
    private  String name;
    private  String filterType;
    private  Boolean activeOnly;
    private Integer limit;

    public StoreMsgAccountConfigCriteria(Long storeId, Long storeMessageId) {
        this.storeId = storeId;
        this.storeMessageId = storeMessageId;
    }

    public Long getStoreId() {
        return storeId;
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

    public Long getStoreMessageId() {
        return storeMessageId;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "StoreMsgAccountConfigCriteria{" +
                "storeId=" + storeId +
                ", storeMessageId=" + storeMessageId +
                ", name='" + name + '\'' +
                ", filterType='" + filterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit +
                '}';
    }


}
