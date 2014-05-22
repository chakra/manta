package com.espendwise.manta.service;


import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.model.view.AllUserIdentView;
import com.espendwise.manta.spi.AppDS;


public interface AllUserService {

    public AllUserData findById(Long allUserId);

    public AllUserData findByUserName(String username);
    
    public AllUserIdentView findViewByName(String userName);

    public AllUserData findById(@AppDS String unit, Long allUserId);

    public AllUserData findByUserName(@AppDS String unit, String username);

    public AllUserIdentView findViewByName(@AppDS String unit,String userName);

    public AllUserIdentView saveUserIdentToMain(@AppDS String unit, AllUserIdentView allUserIdent);
    
}
