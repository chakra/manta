package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.model.view.AllUserIdentView;

public interface AllUserDAO {
    public AllUserData findById(Long allUserId);

    public AllUserData findByUserName(String username);
    
    public AllUserIdentView findViewByName(String userName);
    
    public AllUserIdentView saveUserIdentToMain(AllUserIdentView allUserIdent);

}
