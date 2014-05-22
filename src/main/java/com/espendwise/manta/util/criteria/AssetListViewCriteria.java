package com.espendwise.manta.util.criteria;

import java.io.Serializable;

public class AssetListViewCriteria implements Serializable {

    private Long storeId;
    private Long siteId;
    private String assetId;
    private String assetName;
    private String assetNameFilterType;
    private Boolean activeOnly;
    private Integer limit;

    public AssetListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetNameFilterType() {
        return assetNameFilterType;
    }

    public void setAssetNameFilterType(String assetNameFilterType) {
        this.assetNameFilterType = assetNameFilterType;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "AssetListViewCriteria {" +
                "storeId=" + storeId +
                ", siteId='" + siteId + '\'' +
                ", assetId='" + assetId + '\'' +
                ", assetName='" + assetName + '\'' +
                ", assetNameFilterType='" + assetNameFilterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit +
                '}';
    }
}
