package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.List;

public class ServiceListViewCriteria implements Serializable {

    private Long    storeId;
    private Long    siteId;
    private Long    contractId;
    private Long    assetId;
    private String  serviceId;
    private String  serviceName;
    private String  serviceNameFilterType;
    private Boolean activeOnly;
    private  List<Long> serviceIds;
    
     private Integer limit;

    public ServiceListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public ServiceListViewCriteria(Integer limit) {
        this.limit = limit;
    }

    public ServiceListViewCriteria() {}

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNameFilterType() {
        return serviceNameFilterType;
    }

    public void setServiceNameFilterType(String serviceNameFilterType) {
        this.serviceNameFilterType = serviceNameFilterType;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    
    public Boolean getActiveOnly() {
            return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
            this.activeOnly = activeOnly;
    }

    @Override
    public String toString() {
        return "ServiceListViewCriteria {" +
                "storeId=" + storeId +
                ", siteId='" + siteId + '\'' +
                ", assetId='" + assetId + '\'' +
                ", contractId='" + contractId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceFilterType='" + serviceNameFilterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit +
                '}';
    }
}
