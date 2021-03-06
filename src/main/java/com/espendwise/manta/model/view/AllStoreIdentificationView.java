package com.espendwise.manta.model.view;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
/**
 * AllStoreIdentificationView generated by hbm2java
*/
public class AllStoreIdentificationView extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String MAIN_STORE_ID = "mainStoreId";
    public static final String STORE_ID = "storeId";
    public static final String STORE_NAME = "storeName";
    public static final String DS_NAME = "dsName";
    public static final String ALIVE = "alive";

    private Long mainStoreId;
    private Long storeId;
    private String storeName;
    private String dsName;
    private boolean alive;

    public AllStoreIdentificationView() {
    }
	
    public AllStoreIdentificationView(Long mainStoreId) {
        this.setMainStoreId(mainStoreId);
    }

    public AllStoreIdentificationView(Long mainStoreId, Long storeId, String storeName, String dsName, boolean alive) {
        this.setMainStoreId(mainStoreId);
        this.setStoreId(storeId);
        this.setStoreName(storeName);
        this.setDsName(dsName);
        this.setAlive(alive);
    }

    public Long getMainStoreId() {
        return this.mainStoreId;
    }

    public void setMainStoreId(Long mainStoreId) {
        this.mainStoreId = mainStoreId;
        setDirty(true);
    }

    public Long getStoreId() {
        return this.storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
        setDirty(true);
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
        setDirty(true);
    }

    public String getDsName() {
        return this.dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
        setDirty(true);
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        setDirty(true);
    }




}


