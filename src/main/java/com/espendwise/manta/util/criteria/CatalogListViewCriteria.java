package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.List;

public class CatalogListViewCriteria implements Serializable {

    private Long storeId;
    private String  catalogId;
    private String  catalogName;
    private String  catalogNameFilterType;
    private String catalogType;
    private Boolean activeOnly;
    private Boolean configuredOnly;

    private Long costCenterId;

    private Integer limit;

    private String catalogTypeCd;
    public CatalogListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public CatalogListViewCriteria(Integer limit) {
        this.limit = limit;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public String getCatalogNameFilterType() {
        return catalogNameFilterType;
    }

    public void setCatalogNameFilterType(String catalogNameFilterType) {
        this.catalogNameFilterType = catalogNameFilterType;
    }

    public String getCatalogType() {
        return this.catalogType;
    }

    public void setCatalogType(String catalogType) {
        this.catalogType = catalogType;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(Long costCenterId) {
        this.costCenterId = costCenterId;
    }

     public Boolean getConfiguredOnly() {
        return configuredOnly;
    }

    public void setConfiguredOnly(Boolean configuredOnly) {
        this.configuredOnly = configuredOnly;
    }

    
    public String getCatalogTypeCd() {
        return this.catalogTypeCd;
    }

    public void setCatalogTypeCd(String catalogTypeCd) {
        this.catalogTypeCd = catalogTypeCd;
    }

    @Override
    public String toString() {
        return "CatalogListViewCriteria{" +
                "storeId=" + storeId +
                ", catalogName='" + catalogName + '\'' +
                ", catalogNameFilterType='" + catalogNameFilterType + '\'' +
                ", catalogType='" + catalogType + '\'' +
                ", activeOnly=" + activeOnly +
                ", costCenterId=" + costCenterId +
                ", configuredonly=" + configuredOnly +
                ", limit=" + limit +
                '}';
    }
}
