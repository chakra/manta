package com.espendwise.manta.auth;


import com.espendwise.manta.service.StoreService;
import com.espendwise.manta.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractLogonService implements LogonService {

    private List<String> roles;

    @Autowired
    protected StoreService storeService;

    @Autowired
    protected UserPermissionService userPermissionService;

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public UserPermissionService getUserPermissionService() {
        return userPermissionService;
    }

    public void setUserPermissionService(UserPermissionService userPermissionService) {
        this.userPermissionService = userPermissionService;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }


}
