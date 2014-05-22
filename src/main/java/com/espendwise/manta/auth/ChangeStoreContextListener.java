package com.espendwise.manta.auth;

public interface ChangeStoreContextListener {
    public AppUser changeStoreContext(AppUser user, String datasource, Long storeId);

    public AppUser changeStoreContext(AppUser appUser, Long globalEntityId);
}
