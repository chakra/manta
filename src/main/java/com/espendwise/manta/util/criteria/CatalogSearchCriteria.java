package com.espendwise.manta.util.criteria;

import java.io.Serializable;

public class CatalogSearchCriteria implements Serializable {
    private Long siteId;
    private String catalogName;
    private String catalogNameFilterType;

    private Boolean activeOnly;
    private Boolean configuredOnly;
    private Integer limit;

    public CatalogSearchCriteria(Integer limit) {
        this.limit = limit;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogNameFilterType() {
        return catalogNameFilterType;
    }

    public void setCatalogNameFilterType(String catalogNameFilterType) {
        this.catalogNameFilterType = catalogNameFilterType;
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

    public Boolean getConfiguredOnly() {
        return configuredOnly;
    }

    public void setConfiguredOnly(Boolean configuredOnly) {
        this.configuredOnly = configuredOnly;
    }

    @Override
    public String toString() {
        return "CatalogSearchCriteria{" +
                "catalogShortDesc=" + catalogName +
                ", catalogNameFilterType=" + catalogNameFilterType + '\'' +
                ", siteId=" + siteId + '\'' +
                ", activeOnly=" + activeOnly + '\'' +
                ", configuredOnly=" + configuredOnly + '\'' +
                ", limit=" + limit +
                '}';
    }

}
