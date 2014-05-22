package com.espendwise.manta.service;


import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.GroupConfigAllListView;
import com.espendwise.manta.model.view.GroupConfigListView;
import com.espendwise.manta.model.view.GroupFunctionListView;
import com.espendwise.manta.model.view.GroupHeaderView;
import com.espendwise.manta.model.view.GroupListView;
import com.espendwise.manta.model.view.GroupReportListView;
import com.espendwise.manta.model.view.GroupView;
import com.espendwise.manta.util.criteria.ApplicationFunctionSearchCriteria;
import com.espendwise.manta.util.criteria.GroupConfigSearchCriteria;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.criteria.ReportSearchCriteria;

import java.util.List;
import java.util.Map;

public interface GroupService {

    public List<GroupData> findGroupsByCriteria(GroupSearchCriteria criteria);
    public List<GroupListView> findGroupViewsByCriteria(GroupSearchCriteria criteria);
    public GroupView saveGroup(GroupView groupView) throws DatabaseUpdateException, IllegalDataStateException;
	public GroupHeaderView findGroupHeader(Long groupId);
	public GroupView getGroupView(Long groupId);
	public List<GroupReportListView> findGroupReportViewsByCriteria(ReportSearchCriteria criteria);
	public void configureGroupReports(Long groupId, List<Long> configuredIds, List<Long> notConfiguredIds);
	public Map<String, List<GroupFunctionListView>> findGroupFunctionMapByCriteria(ApplicationFunctionSearchCriteria criteria);
	public void configureGroupFunctions(Long groupId, List<String> configuredFunctions, List<String> notConfiguredFunctions);
	public List<GroupConfigListView> findGroupConfigByCriteria(GroupConfigSearchCriteria criteria);
	public void configureGroupAssocioation(Long groupId, List configured,List unConfigured, String groupAssocCd);
	public List<GroupConfigAllListView> findGroupConfigAllByCriteria(GroupConfigSearchCriteria criteria);
	public List<String> getStoreAssociations(Long groupId, String groupTypeCd, Long storeIdExcluded);
	public List<GroupAssocData> getGroupAssociation(Long groupId, String groupAssocCd);
}
