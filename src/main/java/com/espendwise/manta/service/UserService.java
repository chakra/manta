package com.espendwise.manta.service;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.criteria.UserListViewCriteria;
import com.espendwise.manta.web.util.SuccessActionMessage;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface UserService {

    public UserData findByUserName(String username, boolean b);
    
    public UserHeaderView findUserHeader(Long userId);
    
    public List<UserListView> findUsersByCriteria(UserListViewCriteria criteria);
    
    public UserIdentView findUserToEdit(Long storeId, Long userId);
    
    public UserData findByUserId(Long userId);
    
    public UserIdentView saveUserIdentToDs(@AppDS String dsName, UserIdentView userIdent, Boolean allowCreate);

    public UserIdentView saveUserIdent( UserIdentView userIdent, Boolean allowCreate);

    public UserIdentView createUserIdent(UserIdentView userIdent);

    public UserIdentView modifyUserIdent(UserIdentView userIdent);

    public Set<String> getAllUserPresentDS(Long userId);
    
    public List<BusEntityData> findUserAccounts(StoreAccountCriteria criteria);
    
    public void configureUserAccounts(Long userId, Long storeId, List<BusEntityData> selected, List<BusEntityData> deselected, Boolean removeSiteAssocToo);
    
    public void configureAllUserAccounts(Long userId, Long storeId, Boolean associateAllAccounts, Boolean associateAllSites);
    
    public void configureAllUserSites(Long userId, Long storeId, List<BusEntityData> selected);
    
    public void configureUserSitesList(Long userId, Long storeId, List<SiteListView> selected, List<SiteListView> deselected);
    
    public void configureUserProperty(Long userId, String propertyType, String propertyValue);
 
    public List<SiteListView> findUserSitesByCriteria(SiteListViewCriteria criteria);
    
    public List<PropertyData> findUserProperty(Long userId, String propertyType);

    public Map<Long, GroupData> findGroupsUserIsMemberOf(Long userId, String userType);

    public List<UserGroupInformationView> findUserGroupInformation(Long userId, String userTypeCd);

    public void configureUserGroups(Long userId, Long storeId, UpdateRequest<Long> request);

    public List<PropertyData> findEmailNotificationProperties(UserData userData);

    public void configureUserNotifications(Long storeId, UserData user, List<PropertyData> emailProperties);

    public Long findUserDefaultStore(Long userId);

    public List<String> findManagableUserTypesFor(Long userId);
    
    public SuccessActionMessage processUserUpload(Locale locale, Long currStoreId, InputStream inputStream,String streamType);

    }
