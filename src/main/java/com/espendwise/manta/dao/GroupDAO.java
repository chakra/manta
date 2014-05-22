package com.espendwise.manta.dao;


import java.util.List;
import java.util.Map;

import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.GroupConfigAllListView;
import com.espendwise.manta.model.view.GroupConfigListView;
import com.espendwise.manta.model.view.GroupFunctionListView;
import com.espendwise.manta.model.view.GroupListView;
import com.espendwise.manta.model.view.GroupReportListView;
import com.espendwise.manta.model.view.GroupView;
import com.espendwise.manta.model.view.UserGroupInformationView;
import com.espendwise.manta.model.view.UserGroupsView;
import com.espendwise.manta.util.criteria.ApplicationFunctionSearchCriteria;
import com.espendwise.manta.util.criteria.GroupConfigSearchCriteria;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.criteria.ReportSearchCriteria;

public interface GroupDAO extends DAO {

    public Map<Long, GroupData> findAssociatedUserGroups(Long userId, String userType);

    public List<GroupData> findAccountsGroups(Long storeId);

    public List<GroupData> findAccountsGroups(Long storeId, List<String> status);

    public List<GroupData> findStoreGroups(Long storeId);

    public List<GroupData> findStoreGroups(Long storeId, List<String> status);

    public List<GroupData> findStoresGroups();

    public List<GroupData> findGroupsByCriteria(GroupSearchCriteria criteria);
    public List<GroupListView> findGroupViewsByCriteria(GroupSearchCriteria criteria) throws IllegalStateException;

    public List<UserGroupInformationView> findUserGroupInformation(Long userId, String userTypeCd);

    public UserGroupsView configureUserGroups(Long userId, List<Long> assignGroups, List<Long> unassignGroups);
    public GroupView getGroupView(Long groupId);
    public GroupView saveGroup(GroupView groupView);
    public List<GroupReportListView> findGroupReportViewsByCriteria(ReportSearchCriteria criteria) ;
    public void configureGroupReports(Long groupId, List<Long> configuredIds, List<Long> notConfiguredIds);
	public Map<String, List<GroupFunctionListView>> findGroupFunctionMapByCriteria(ApplicationFunctionSearchCriteria criteria);
	public void configureGroupFunctions(Long groupId, List<String> configuredFunctions, List<String> notConfiguredFunctions);
	public List<GroupConfigListView> findGroupConfigByCriteria(GroupConfigSearchCriteria criteria);
	public void configureGroupAssocioation(Long groupId, List configured,List unConfigured, String groupAssocCd);
	public List<GroupConfigAllListView> findGroupConfigAllByCriteria(GroupConfigSearchCriteria criteria);
	/**
	 * return list of store names where entity (User, Store, Account, Manufacturer....) associated excluding the store id: storeIdExcluded
	 */
	public List<String> getStoreAssociations(Long groupId, String groupType, Long storeIdExcluded);

}

