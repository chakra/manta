package com.espendwise.manta.util.criteria;


import java.io.Serializable;
import java.util.List;

public class StoreSiteCriteria implements Serializable {

    private List<Long> siteIds;
    private List<String> siteNames;
    private List<String> refNums;
    private Long storeId;
    private List<Long> accountIds;
    private boolean activeOnly;

    public List<Long> getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(List<Long> siteIds) {
        this.siteIds = siteIds;
    }

    public List<String> getSiteNames() {
        return siteNames;
    }

    public void setSiteNames(List<String> siteNames) {
        this.siteNames = siteNames;
    }

    public List<String> getRefNums() {
        return refNums;
    }

    public void setRefNums(List<String> refNums) {
        this.refNums = refNums;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public List<Long> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<Long> accountIds) {
        this.accountIds = accountIds;
    }
    
    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }
    
    public boolean isActiveOnly() {
        return activeOnly;
    }

}
