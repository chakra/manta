package com.espendwise.manta.service;


import com.espendwise.manta.auth.AuthUserDetails;
import com.espendwise.manta.auth.AuthenticationUserMap;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.RefCdData;
import com.espendwise.manta.model.data.WoRefCdData;
import com.espendwise.manta.model.view.InstanceView;
import com.espendwise.manta.model.view.LogonOptionView;
import com.espendwise.manta.model.view.StoreUiOptionView;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.HomeViewType;
import com.espendwise.manta.util.criteria.StoreListEntityCriteria;

import java.util.List;
import java.util.Locale;

public interface UserLogonService {

    public InstanceView getDefaultUserStore(String username, AuthenticationUserMap authUserMap, String domainName);

    public List<InstanceView> getUserStoreDataSources();

    public List<InstanceView> getUserStoreDataSources(AuthenticationUserMap authUserMap);

    public InstanceView findInstance(AuthenticationUserMap authUserMap, String ds, Long storeId);

    public StoreUiOptionView getStoreUiOptions(String dataSource,
                                               Long userId,
                                               Long store,
                                               HomeViewType homeViewType,
                                               Locale userLocale);

    public Locale getUserLocale(String country, String lang, Locale defLocale, Locale preferedLocale);

    public boolean verifyAccessToStore(String datasource, Long storeId);

    public AuthUserDetails retrieveAuthenticatedUserForStore(String datasource, Long storeId);

    public InstanceView retrieveAuthenticatedUserStore(String datasource, Long storeId);

    public LogonOptionView getLogonOptions(String domainName, Locale locale);

    public List<InstanceView> findUserStoreDataSources(Long userId, StoreListEntityCriteria criteria);

    public List<WoRefCdData> getWoRefCodes(String dataSource, String applicationFunctions, int orderByName);

    public List<RefCdData> getRefCodes(String dataSource, String applicationFunctions, int orderByName);

    public List<GroupAssocData> findUserGroupFunctions(String dataSource, Long userId, String userType);

    public byte[] getBestLogo(@AppDS String datasource, Long storeId, String path, Locale locale);

    public InstanceView findInstanceByGlobalId(Long globalEntityId);

    public String provideTokenForExternalAccess(@AppDS String dataSourceName, Long storeId, Long userId, Long asUserId);

 }
