package com.espendwise.manta.dao;


import java.util.List;
import com.espendwise.manta.model.data.UserAssocData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.util.criteria.UserAssocCriteria;

import java.util.List;

public interface UserAssocDAO {

    public UserAssocData readAssoc(Long userId, Long storeId, String assocType);

    public List<UserData> findAssocUsers(Long storeId, UserAssocCriteria criteria, String assocType);

    public List<UserAssocData> findEntityAssoc(Long entityId, String assocType);

    public UserAssocData createAssoc(Long userId, Long entityId, String assocCd);

    public void removeEntityAssoc(Long entityId, String assocCd);

    public UserAssocData findAssoc(Long userId, Long entityId, String assocType);

    public List<UserAssocData> updateUserAssocs(Long userId, List<UserAssocData> newAssocs);

    public List<UserAssocData> readUserAccountAssoc(Long userId, Long storeId);
    
    public List<UserAssocData> readUserSiteAssoc(Long userId, Long storeId);
    
    public List<UserAssocData> readUserSiteAssocsForAccount(Long userId, Long accountId);
    
    public List<UserAssocData> readSiteUserAssoc(Long siteId, Long storeId);

}
