package com.espendwise.manta.auth;

import java.io.Serializable;

public class AuthMainStoreIdent implements Serializable {

    private Long storeId;
    private Long mainStoreId;
    private String mainStoreName;
    private String datasource;
    private String mainDomain;

    public AuthMainStoreIdent(Long mainStoreId, Long storeId, String mainStoreName, String datasource, String mainDomain) {
        this.mainStoreId = mainStoreId;
        this.storeId = storeId;
        this.mainStoreName = mainStoreName;
        this.datasource = datasource;
        this.mainDomain = mainDomain;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getMainStoreId() {
        return mainStoreId;
    }

    public void setMainStoreId(Long mainStoreId) {
        this.mainStoreId = mainStoreId;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getMainStoreName() {
        return mainStoreName;
    }

    public void setMainStoreName(String mainStoreName) {
        this.mainStoreName = mainStoreName;
    }

    public String getMainDomain() {
        return mainDomain;
    }

    public void setMainDomain(String mainDomain) {
        this.mainDomain = mainDomain;
    }
}
