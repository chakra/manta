package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderListViewCriteria implements Serializable {

    private Date orderFromDate;
    private Date orderToDate;
    private String outboundPoNum;
    private String customerPoNum;
    private String erpPoNum;
    private String webOrderConfirmationNum;
    private String refOrderNum;
    private String siteZipCode;
    private String method;
    private List<String> orderStatuses;
    private List<Long> accountFilter;
    private List<Long> distibutorFilter;
    private List<Long> siteFilter;
    private List<String> userFilter;
    
    private List<Long> storeIds;
    private Long userId;
    private String userType;

    private Integer limit;

    public OrderListViewCriteria(List<Long> storeIds, Integer limit) {
        this.storeIds = storeIds;
        this.limit = limit;
    }

    public List<Long> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<Long> storeIds) {
        this.storeIds = storeIds;
    }

    public String getCustomerPoNum() {
        return customerPoNum;
    }

    public void setCustomerPoNum(String customerPoNum) {
        this.customerPoNum = customerPoNum;
    }

    public String getErpPoNum() {
        return erpPoNum;
    }

    public void setErpPoNum(String erpPoNum) {
        this.erpPoNum = erpPoNum;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getOrderFromDate() {
        return orderFromDate;
    }

    public void setOrderFromDate(Date orderFromDate) {
        this.orderFromDate = orderFromDate;
    }

    public Date getOrderToDate() {
        return orderToDate;
    }

    public void setOrderToDate(Date orderToDate) {
        this.orderToDate = orderToDate;
    }

    public String getOutboundPoNum() {
        return outboundPoNum;
    }

    public void setOutboundPoNum(String outboundPoNum) {
        this.outboundPoNum = outboundPoNum;
    }

    public String getRefOrderNum() {
        return refOrderNum;
    }

    public void setRefOrderNum(String refOrderNum) {
        this.refOrderNum = refOrderNum;
    }

    public String getSiteZipCode() {
        return siteZipCode;
    }

    public void setSiteZipCode(String siteZipCode) {
        this.siteZipCode = siteZipCode;
    }

    public String getWebOrderConfirmationNum() {
        return webOrderConfirmationNum;
    }

    public void setWebOrderConfirmationNum(String webOrderConfirmationNum) {
        this.webOrderConfirmationNum = webOrderConfirmationNum;
    }

    public List<String> getOrderStatuses() {
        return orderStatuses;
    }

    public void setOrderStatuses(List<String> orderStatuses) {
        this.orderStatuses = orderStatuses;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<Long> getAccountFilter() {
        return accountFilter;
    }

    public void setAccountFilter(List<Long> accountFilter) {
        this.accountFilter = accountFilter;
    }

    public List<Long> getDistibutorFilter() {
        return distibutorFilter;
    }

    public void setDistibutorFilter(List<Long> distibutorFilter) {
        this.distibutorFilter = distibutorFilter;
    }

    public List<Long> getSiteFilter() {
        return siteFilter;
    }

    public void setSiteFilter(List<Long> siteFilter) {
        this.siteFilter = siteFilter;
    }

    public List<String> getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(List<String> userFilter) {
        this.userFilter = userFilter;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "OrderListViewCriteria{" +
                "storeIds=" + storeIds +
                ", userId=" + userId +
                ", userType=" + userType + 
                ", orderFromDate=" + orderFromDate +
                ", orderToDate='" + orderToDate + '\'' +
                ", outboundPoNum='" + outboundPoNum + '\'' +
                ", customerPoNum='" + customerPoNum + '\'' +
                ", erpPoNum='" + erpPoNum + '\'' +
                ", webOrderNum='" + webOrderConfirmationNum + '\'' +
                ", refOrderNum='" + refOrderNum + '\'' +
                ", siteZipCode='" + siteZipCode + '\'' +
                ", method='" + method + '\'' +
                ", orderStatuses='" + orderStatuses + '\'' +
                ", accountFilter=" + accountFilter +
                ", distibutorFilter=" + distibutorFilter +
                ", siteFilter=" + siteFilter +
                ", userFilter=" + userFilter +
                ", limit=" + limit +
                '}';
    }
}
