package com.espendwise.manta.util.criteria;


import java.io.Serializable;
import java.util.List;

public class SiteListViewCriteria implements Serializable {

    private Long siteId;
    private Integer limit;
    private Long storeId;
    private Long userId;
    private Long scheduleId;
    private Long workflowId;
    private String siteName;
    private String refNumber;
    private String city;
    private String state;
    private String postalCode;
    private boolean activeOnly;
    private boolean useUserToAccountAssoc = false;
    private boolean configuredOnly = false;
    private List<Long> accountIds;

    private String siteNameFilterType;
    private String refNumberFilterType;
    private String cityFilterType;
    private String stateFilterType;
    private String postalCodeFilterType;
    private String workflowFilterType;

    public SiteListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setSiteName(String siteName,String filterType) {
        this.siteName = siteName;
        this.siteNameFilterType = filterType;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public void setRefNumber(String refNumber,String filterType) {
        this.refNumber = refNumber;
        this.refNumberFilterType = filterType;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public void setCity(String city,String filterType) {
        this.city = city;
        this.cityFilterType = filterType;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setState(String state, String filterType) {
        this.state = state;
        this.stateFilterType = filterType;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPostalCode(String postalCode, String filterType) {
        this.postalCode = postalCode;
        this.postalCodeFilterType = filterType;
    }

    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public Long getSiteId() {
        return siteId;
    }

    public Integer getLimit() {
        return limit;
    }

    public Long getStoreId() {
        return storeId;
    }


    public String getSiteName() {
        return siteName;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
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

    public String getSiteNameFilterType() {
        return siteNameFilterType;
    }

    public void setSiteNameFilterType(String siteNameFilterType) {
        this.siteNameFilterType = siteNameFilterType;
    }

    public String getRefNumberFilterType() {
        return refNumberFilterType;
    }

    public void setRefNumberFilterType(String refNumberFilterType) {
        this.refNumberFilterType = refNumberFilterType;
    }

    public String getCityFilterType() {
        return cityFilterType;
    }

    public void setCityFilterType(String cityFilterType) {
        this.cityFilterType = cityFilterType;
    }

    public String getStateFilterType() {
        return stateFilterType;
    }

    public void setStateFilterType(String stateFilterType) {
        this.stateFilterType = stateFilterType;
    }

    public String getPostalCodeFilterType() {
        return postalCodeFilterType;
    }

    public void setPostalCodeFilterType(String postalCodeFilterType) {
        this.postalCodeFilterType = postalCodeFilterType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}
	
    public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public boolean isUseUserToAccountAssoc() {
        return useUserToAccountAssoc;
    }

    public void setUseUserToAccountAssoc(boolean useUserToAccountAssoc) {
        this.useUserToAccountAssoc = useUserToAccountAssoc;
    }

    public boolean isConfiguredOnly() {
        return configuredOnly;
    }

    public String getWorkflowFilterType() {
		return workflowFilterType;
	}

	public void setWorkflowFilterType(String workflowFilterType) {
		this.workflowFilterType = workflowFilterType;
	}

	public void setConfiguredOnly(boolean configuredOnly) {
        this.configuredOnly = configuredOnly;
    }

    @Override
    public String toString() {
        return "SiteListViewCriteria{" +
                "siteId=" + siteId +
                ", limit=" + limit +
                ", storeId=" + storeId +
                ", userId=" + userId +
                ", scheduleId=" + scheduleId +
                ", workflowId=" + workflowId +
                ", siteName='" + siteName + '\'' +
                ", siteNameFilterType='" + siteNameFilterType + '\'' +
                ", refNumber='" + refNumber + '\'' +
                ", refNumberFilterType='" + refNumberFilterType + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", cityFilterType='" + cityFilterType + '\'' +
                ", stateFilterType='" + stateFilterType + '\'' +
                ", postalCodeFilterType='" + postalCodeFilterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", useUserToAccountAssoc=" + useUserToAccountAssoc +
                ", configuredOnly=" + configuredOnly +
                ", accountIds=" + accountIds +
                '}';
    }

	
}
