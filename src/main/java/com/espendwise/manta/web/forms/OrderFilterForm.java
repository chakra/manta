package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.LocateAssistant;

import com.espendwise.manta.web.validator.OrderFilterFormValidator;
import java.util.ArrayList;
import java.util.List;

@Validation(OrderFilterFormValidator.class)
public class OrderFilterForm extends WebForm implements Resetable, Initializable {

    private List<AccountListView> filteredAccounts;
    private String accountFilter;
    
    private List<DistributorListView> filteredDistributors;
    private String distributorFilter;
    
    private List<UserListView> filteredUsers;
    private String userFilter;
    
    private List<SiteListView> filteredSites;
    private String siteFilter;

    private String orderFromDate;
    private String orderToDate;
    private String outboundPONum;
    private String erpPONum;
    private String customerPONum;
    private String webOrderConfirmationNum;
    private String refOrderNum;
    private String orderMethod;
    private String siteZipCode;
    private String placedBy;
    private List<Pair<String, String>> orderSources;
    private SelectableObjects searchOrderStatuses;
    
    private boolean init;
    
    public OrderFilterForm() {
        super();
    }

    public String getAccountFilter() {
        return accountFilter;
    }

    public void setAccountFilter(String accountFilter) {
        this.accountFilter = accountFilter;
    }

    public String getDistributorFilter() {
        return distributorFilter;
    }

    public void setDistributorFilter(String distributorFilter) {
        this.distributorFilter = distributorFilter;
    }

    public String getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(String userFilter) {
        this.userFilter = userFilter;
    }

    public List<AccountListView> getFilteredAccounts() {
        return filteredAccounts;
    }

    public void setFilteredAccounts(List<AccountListView> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }

    public String getFilteredAccountCommaNames() {
        return LocateAssistant.getFilteredAccountCommaNames(getFilteredAccounts());
    }

    public String getFilteredAccountCommaIds() {
        return LocateAssistant.getFilteredAccountCommaIds(getFilteredAccounts());
    }

    public List<DistributorListView> getFilteredDistributors() {
        return filteredDistributors;
    }

    public void setFilteredDistributors(List<DistributorListView> filteredDistributors) {
        this.filteredDistributors = filteredDistributors;
    }
    
    public String getFilteredDistributorCommaNames() {
        return LocateAssistant.getFilteredDistrCommaNames(getFilteredDistributors());
    }

    public String getFilteredDistributorCommaIds() {
        return LocateAssistant.getFilteredDistrCommaIds(getFilteredDistributors());
    }

    public List<UserListView> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<UserListView> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }
    
    public String getFilteredUserCommaNames() {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredUsers)) {
            for (UserListView value : filteredUsers) {
                builder.append((builder.length() > 0) ? ", " + value.getUserName() : value.getUserName());
            }
        }
        return builder.toString();
    }

    public String getFilteredUserCommaIds() {
        return LocateAssistant.getFilteredUserCommaIds(getFilteredUsers());
    }

    public List<SiteListView> getFilteredSites() {
        return filteredSites;
    }

    public void setFilteredSites(List<SiteListView> filteredSites) {
        this.filteredSites = filteredSites;
    }

    public String getSiteFilter() {
        return siteFilter;
    }

    public void setSiteFilter(String siteFilter) {
        this.siteFilter = siteFilter;
    }
    
    public String getFilteredSiteCommaNames() {
        return LocateAssistant.getFilteredSiteCommaNames(getFilteredSites());
    }

    public String getFilteredSiteCommaIds() {
        return LocateAssistant.getFilteredSiteCommaIds(getFilteredSites());
    }
    
    public String getCustomerPONum() {
        return customerPONum;
    }

    public void setCustomerPONum(String customerPONum) {
        this.customerPONum = customerPONum;
    }

    public String getErpPONum() {
        return erpPONum;
    }

    public void setErpPONum(String erpPONum) {
        this.erpPONum = erpPONum;
    }

    public String getOrderFromDate() {
        return orderFromDate;
    }

    public void setOrderFromDate(String orderFromDate) {
        this.orderFromDate = orderFromDate;
    }

    public String getOrderToDate() {
        return orderToDate;
    }

    public void setOrderToDate(String orderToDate) {
        this.orderToDate = orderToDate;
    }

    public String getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod(String orderMethod) {
        this.orderMethod = orderMethod;
    }

    public String getOutboundPONum() {
        return outboundPONum;
    }

    public void setOutboundPONum(String outboundPONum) {
        this.outboundPONum = outboundPONum;
    }

    public String getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(String placedBy) {
        this.placedBy = placedBy;
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

    public SelectableObjects getSearchOrderStatuses() {
        return searchOrderStatuses;
    }

    public void setSearchOrderStatuses(SelectableObjects searchOrderStatuses) {
        this.searchOrderStatuses = searchOrderStatuses;
    }

    public List<Pair<String, String>> getOrderSources() {
        return orderSources;
    }

    public void setOrderSources(List<Pair<String, String>> orderSources) {
        this.orderSources = orderSources;
    }

    @Override
    public void initialize() {
        reset();
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;
    }

    @Override
    public void reset() {
        this.filteredAccounts = new ArrayList();
        this.accountFilter = null;
        this.filteredDistributors = new ArrayList();
        this.distributorFilter = null;
        this.filteredUsers = new ArrayList();
        this.userFilter = null;
        this.filteredSites = new ArrayList();
        this.siteFilter = null;
        this.orderFromDate = null;
        this.orderToDate = null;
        this.outboundPONum = null;
        this.erpPONum = null;
        this.customerPONum = null;
        this.webOrderConfirmationNum = null;
        this.refOrderNum = null;
        this.orderMethod = null;
        this.siteZipCode = null;
        this.placedBy = null;
    }
}
