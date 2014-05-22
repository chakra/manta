package com.espendwise.manta.auth;


import java.io.Serializable;
import java.util.Date;

public class AuthUserAccessTokenProperty implements Serializable {

    private Long busEntityId;
    private Long userId;
    private String value;
    private Date addDate;
    private String addBy;
    private Date modDate;
    private String modBy;
    private Long originalUserId;
    private String datasource;

    public AuthUserAccessTokenProperty(String datasource, Long userId, String value, Long busEntityId, Long originalUserId, Date addDate, String addBy, Date modDate, String modBy) {
        this.datasource = datasource;
        this.userId = userId;
        this.value = value;
        this.busEntityId = busEntityId;
        this.originalUserId = originalUserId;
        this.addDate = addDate;
        this.addBy = addBy;
        this.modDate = modDate;
        this.modBy = modBy;
    }

    public Long getBusEntityId() {
        return busEntityId;
    }

    public void setBusEntityId(Long busEntityId) {
        this.busEntityId = busEntityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAddBy() {
        return addBy;
    }

    public void setAddBy(String addBy) {
        this.addBy = addBy;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public String getModBy() {
        return modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
    }

    public Long getOriginalUserId() {
        return originalUserId;
    }

    public void setOriginalUserId(Long originalUserId) {
        this.originalUserId = originalUserId;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    @Override
    public String toString() {
        return "AuthUserAccessTokenProperty{" +
                "busEntityId=" + busEntityId +
                ", userId=" + userId +
                ", value='" + value + '\'' +
                ", addDate=" + addDate +
                ", addBy='" + addBy + '\'' +
                ", modDate=" + modDate +
                ", modBy='" + modBy + '\'' +
                ", originalUserId=" + originalUserId +
                ", datasource='" + datasource + '\'' +
                '}';
    }
}
