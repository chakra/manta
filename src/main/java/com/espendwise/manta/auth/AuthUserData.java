package com.espendwise.manta.auth;

import com.espendwise.manta.model.entity.StoreListEntity;

import java.io.Serializable;
import java.util.List;

public class AuthUserData implements Serializable {

    private AuthUserDetails userDetails;
    private List<StoreListEntity> userStores;

    public AuthUserData(AuthUserDetails userDetails, List<StoreListEntity> userStores) {
        this.userDetails = userDetails;
        this.userStores = userStores;
    }

    public AuthUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(AuthUserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public List<StoreListEntity> getUserStores() {
        return userStores;
    }

    public void setUserStores(List<StoreListEntity> userStores) {
        this.userStores = userStores;
    }

}
