package com.espendwise.manta.auth.ctx;

import com.espendwise.manta.auth.ApplicationDataSource;
import com.espendwise.manta.auth.ApplicationUserPermission;
import com.espendwise.manta.model.view.MainStoreIdentView;
import com.espendwise.manta.model.view.StoreUiOptionView;

public class AppStoreContext implements AppCtx {

    private Long storeId;
    private Long globalEntityId;
    private String storeName;

    private ApplicationUserPermission userPermission;
    private StoreUiOptionView uiOptions;

    private MainStoreIdentView mainStoreIdent;
    private ApplicationDataSource dataSource;


    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setUserPermission(ApplicationUserPermission userPermission) {
        this.userPermission = userPermission;
    }

    public void setUiOptions(StoreUiOptionView uiOptions) {
        this.uiOptions = uiOptions;
    }

    public StoreUiOptionView getUiOptions() {
        return uiOptions;
    }

    public Long getStoreId() {
        return storeId;
    }

    public ApplicationUserPermission getUserPermission() {
        return userPermission;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setDataSource(ApplicationDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ApplicationDataSource getDataSource() {
        return dataSource;
    }

    public void setMainStoreIdent(MainStoreIdentView mainStoreIdent) {
        this.mainStoreIdent = mainStoreIdent;
    }

    public MainStoreIdentView getMainStoreIdent() {
        return mainStoreIdent;
    }

    public Long getGlobalEntityId() {
        return globalEntityId;
    }

    public void setGlobalEntityId(Long globalEntityId) {
        this.globalEntityId = globalEntityId;
    }

    @Override
    public String toString() {
        return "AppStoreContext{" +
                "storeId=" + storeId +
                ", globalEntityId=" + globalEntityId +
                ", storeName='" + storeName + '\'' +
                ", userPermission=" + userPermission +
                ", uiOptions=" + uiOptions +
                ", mainStoreIdent=" + mainStoreIdent +
                ", dataSource=" + dataSource +
                '}';
    }
}
