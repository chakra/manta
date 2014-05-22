package com.espendwise.manta.auth;


public class StoreContext {

    private static final String STORE_ID = "storeId";

    private AuthUserContext authUserContext;

    public StoreContext(AuthUserContext authUserContext) {
        this.authUserContext = authUserContext;
    }

    public int getStoreId() {
        return (Integer) authUserContext.get(STORE_ID);
    }
}
