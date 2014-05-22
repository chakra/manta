package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserAssocData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.UserHeaderView;
import com.espendwise.manta.model.view.UserIdentView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.criteria.UserListViewCriteria;

import java.util.Collection;
import java.util.List;

public interface UserDAO extends DAO {

    public UserData findByUserName(String userName, boolean activeOnly);

    public UserData findByUserId(Long id);
    
    public UserHeaderView findUserHeader(Long userId);

    public Collection<UserData> findUserDatas();
    
    public List<UserListView> findUsersByCriteria(UserListViewCriteria criteria);
    
    public UserIdentView findUserToEdit(Long storeId, Long userId);

    public void delete(UserData user);

    public List<String> findManagableUserTypesFor(Long userId);

    public UserIdentView createUserIdent(UserIdentView userIdent);

    public UserIdentView modifyUserIdent(UserIdentView userIdent);

    public UserIdentView saveUserIdentToDs(UserIdentView userIdent, Boolean allowCreate);
    
    public List<UserAssocData> findUserAssocs(Long userId);

    public List<BusEntityData> findUserAccounts(StoreAccountCriteria criteria);
    
    public void configureUserAccounts(Long userId, Long storeId, List<BusEntityData> selected, List<BusEntityData> deselected, Boolean removeSiteAssocToo);
    
    public void configureAllUserAccounts(Long userId, Long storeId, Boolean associateAllAccounts, Boolean associateAllSites);
    
    public void configureUserSites(Long userId, Long storeId, List<BusEntityData> selected, List<BusEntityData> deselected);
    
    public void configureUserSitesList(Long userId, Long storeId, List<SiteListView> selected, List<SiteListView> deselected);
    
    public List<SiteListView> findUserSitesByCriteria(SiteListViewCriteria criteria);
    
    public List<PropertyData> findUserProperty(Long userId, String propertyType);
    
    public void configureUserProperty(Long userId, String propertyType, String propertyValue);

    public void configureUserGroups(UserData user, List<PropertyData> emailProperties);
    
    public UserData updateUserData(UserData userData);
    
    public List<GroupAssocData> findUserGroupAssocs(Long userId);
}
