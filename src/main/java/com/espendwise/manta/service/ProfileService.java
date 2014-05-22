package com.espendwise.manta.service;

import com.espendwise.manta.model.view.ProfilePasswordMgrView;


public interface ProfileService {

    public ProfilePasswordMgrView getPasswordManagementInfo(Long storeId);
    public ProfilePasswordMgrView savePasswordManagementInfo(ProfilePasswordMgrView passwordView) throws DatabaseUpdateException;
}
