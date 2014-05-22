package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.List;

public class UserListViewCriteria implements Serializable {
    private Long storeId;
    private Long siteId;
    private String  userId;
    private String  userName;
    private String  userNameFilterType;
    private String  firstName;
    private String  lastName;
    private String  userType;
    private String  email;
    private String  emailFilterType;
    private String  role;
    private String  language;
    private List<Long> accountFilter;
    
    private Boolean activeOnly;

    private List<String> allowedUserTypes;

    private Integer limit;

    public UserListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailFilterType() {
        return emailFilterType;
    }

    public void setEmailFilterType(String emailFilterType) {
        this.emailFilterType = emailFilterType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameFilterType() {
        return userNameFilterType;
    }

    public void setUserNameFilterType(String userNameFilterType) {
        this.userNameFilterType = userNameFilterType;
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

    public List<String> getAllowedUserTypes() {
        return allowedUserTypes;
    }

    public void setAllowedUserTypes(List<String> allowedUserTypes) {
        this.allowedUserTypes = allowedUserTypes;
    }

    @Override
    public String toString() {
        return "UserListViewCriteria{" +
                "storeId=" + storeId +
                ", siteId=" + siteId +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userNameFilterType='" + userNameFilterType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userType='" + userType + '\'' +
                ", email='" + email + '\'' +
                ", emailFilterType='" + emailFilterType + '\'' +
                ", role='" + role + '\'' +
                ", language='" + language + '\'' +
                ", accountFilter=" + accountFilter +
                ", activeOnly=" + activeOnly +
                ", allowedUserTypes=" + allowedUserTypes +
                ", limit=" + limit +
                '}';
    }
}
