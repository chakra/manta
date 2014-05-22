package com.espendwise.manta.dao;

import com.espendwise.manta.model.view.ProfilePasswordMgrView;


public interface ProfileDAO {
	public ProfilePasswordMgrView getPasswordManagementInfo(Long storeId);
    public ProfilePasswordMgrView savePasswordManagementInfo(ProfilePasswordMgrView passwordView);
}

