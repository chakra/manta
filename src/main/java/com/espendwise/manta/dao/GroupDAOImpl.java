package com.espendwise.manta.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.espendwise.manta.model.data.GenericReportData;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.GroupConfigAllListView;
import com.espendwise.manta.model.view.GroupConfigListView;
import com.espendwise.manta.model.view.GroupFunctionListView;
import com.espendwise.manta.model.view.GroupListView;
import com.espendwise.manta.model.view.GroupReportListView;
import com.espendwise.manta.model.view.GroupView;
import com.espendwise.manta.model.view.UserGroupInformationView;
import com.espendwise.manta.model.view.UserGroupsView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.DbConstantResource;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.ApplicationFunctionSearchCriteria;
import com.espendwise.manta.util.criteria.GroupConfigSearchCriteria;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.criteria.ReportSearchCriteria;

@Repository
public class GroupDAOImpl extends DAOImpl implements GroupDAO {

    private static final Logger logger = Logger.getLogger(GroupDAOImpl.class);

    public GroupDAOImpl() {
        this(null);
    }

    public GroupDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }


    public Map<Long, GroupData> findAssociatedUserGroups(Long userId, String userType)  {

        logger.info("findAssociatedUserGroups()=> BEGIN. pUserId: " + userId+", userType: "+userType);
       
        Map<Long, GroupData> retValue = new HashMap<Long, GroupData>();

        GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(em);

        if (userId != null && userType!=null) {

            List<GroupData> groups;

            logger.info("findUserGroupIds()=> ...");

            List<Long> groupIds = groupAssocDao.findUserGroupIds(userId);

            groups = findActiveUserGroupsByIds(new HashSet<Long>(groupIds));
            logger.info("findAssociatedUserGroups()=> found " + groups.size() + " active user groups");
            for (GroupData group : groups) {
                retValue.put(group.getGroupId(), group);
            }

            groups = findActiveUserGroupsByUserType(userType);
            logger.info("findAssociatedUserGroups()=> found " + groups.size() + " active default(" + userType + ") groups");
            for (GroupData group : groups) {
                retValue.put(group.getGroupId(), group);
            }

            groups = findActiveUserGroupsByUserType(Constants.DEFAULT_GROUP);
            logger.info("findAssociatedUserGroups()=> found " + groups.size() + " active default(" + Constants.DEFAULT_GROUP + ") groups");
            for (GroupData group : groups) {
                retValue.put(group.getGroupId(), group);
            }

        }

        logger.info("findAssociatedUserGroups()=> END, retValue.Size: "+retValue.size());

         return retValue;

    }

    
    private List<GroupData> findActiveUserGroupsByUserType(String userTypeCd) {

        Query q = em.createQuery("Select groups from GroupData groups" +
                " where groups.shortDesc = (:userTypeCd)" +
                " and groups.groupStatusCd = (:statusCd)" +
                " and groups.groupTypeCd = (:typeCd)");

        q.setParameter("userTypeCd",  userTypeCd);
        q.setParameter("statusCd", RefCodeNames.GROUP_STATUS_CD.ACTIVE);
        q.setParameter("typeCd", RefCodeNames.GROUP_TYPE_CD.USER);

        return (List<GroupData>) q.getResultList();
    }

    private List<GroupData> findActiveUserGroupsByIds(Collection<Long> groupIds) {

        logger.info("findActiveUserGroupsByIds()=> BEGIN");

        if (!Utility.isSet(groupIds)) {
             logger.info("findActiveUserGroupsByIds()=> empty groups, return ");
             return Utility.emptyList(GroupData.class);
            
        }
        Query q = em.createQuery("Select groups from GroupData groups" +
                " where groups.groupId in (:groupIds)" +
                " and groups.groupStatusCd = (:statusCd)" +
                " and groups.groupTypeCd = (:typeCd)");

        q.setParameter("groupIds", groupIds);
        q.setParameter("statusCd", RefCodeNames.GROUP_STATUS_CD.ACTIVE);
        q.setParameter("typeCd", RefCodeNames.GROUP_TYPE_CD.USER);

        logger.info("findActiveUserGroupsByIds()=> END");

        return (List<GroupData>) q.getResultList();
    }

    public List<GroupData> findAccountsGroups(Long storeId) {
        return findEntityGroups(storeId,
                RefCodeNames.GROUP_TYPE_CD.ACCOUNT,
                null
        );
    }

    public List<GroupData> findAccountsGroups(Long storeId, List<String> status) {
        return findEntityGroups(storeId,
                RefCodeNames.GROUP_TYPE_CD.ACCOUNT,
                status
        );
    }

    public List<GroupData> findStoreGroups(Long storeId) {
        return findEntityGroups(storeId,
                RefCodeNames.GROUP_TYPE_CD.STORE,
                null
        );
    }

    public List<GroupData> findStoreGroups(Long storeId, List<String> status) {
        return findEntityGroups(storeId,
                RefCodeNames.GROUP_TYPE_CD.STORE,
                status
        );
    }

    public List<GroupData> findStoresGroups() {
        return findEntityGroups(null,
                RefCodeNames.GROUP_TYPE_CD.STORE,
                null
        );
    }


    private List<GroupData> findEntityGroups(Long storeId, String entityGroupTypeCd, List<String> status) {

        logger.info(" findEntityGroups()=> BEGIN" +
                ", storeId: " + storeId +
                ", entityGroupTypeCd: " + entityGroupTypeCd +
                ", status: " + status
        );

        Query q = em.createQuery("Select groupData from GroupData groupData" + (storeId != null ? ", GroupAssocData storeAssoc" : "")
                + " where groupData.groupTypeCd = (:entityGroupTypeCd) "
                + (status != null ? (status.size() == 1 ? " and groupData.groupStatusCd = (:status)" : "  and groupData.groupStatusCd in(:status)") : "")
                + (storeId != null ? " and groupData.groupId = storeAssoc.groupId" : "")
                + (storeId != null ? " and storeAssoc.groupAssocCd = (:groupOfStore)" : "")
                + (storeId != null ? " and storeAssoc.busEntityId = (:storeId)" : "")
        );

        q.setParameter("entityGroupTypeCd", entityGroupTypeCd);

        if (storeId != null) {
            q.setParameter("storeId", storeId);
            q.setParameter("groupOfStore", RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
        }

        if (status != null) {
            q.setParameter("status", status.size() == 1 ? status.get(0) : status);
        }

        List<GroupData> r = q.getResultList();

        logger.info("findSitesByCriteria)=> END, fetched : " + r.size() + " rows");

        return r;

    }

    public List<GroupData> findGroupsByCriteria(GroupSearchCriteria criteria)  {    	
        logger.info("findGroupsByCriteria()=> BEGIN, criteria: " + criteria);

        List<GroupData> x = new ArrayList<GroupData>();

        if (criteria.getStoreIds() != null && criteria.getStoreIds().isEmpty()) {
            return x;
        }

        if (criteria.getGroupNameIn() != null && criteria.getGroupNameIn().isEmpty()) {
            return x;
        }

        if (criteria.getGroupIds() != null && criteria.getGroupIds().isEmpty()) {
            return x;
        }
        
        String sql = "Select distinct groupData " +
		" from GroupData groupData " +
		" where 1 = 1 " +
		(Utility.isSet(criteria.getGroupStatus()) ? " and groupData.groupStatusCd = (:groupStatusCd)" : "") +
		(criteria.isActiveOnly() ? " and groupData.groupStatusCd <> " + QueryHelp.toQuoted(RefCodeNames.GROUP_STATUS_CD.INACTIVE) : "") +                
		(Utility.isSet(criteria.getGroupType()) ? " and groupData.groupTypeCd = (:groupTypeCd)" : "") +
		(Utility.isSet(criteria.getGroupName()) ? " and UPPER(groupData.shortDesc) like :shortDesc" : "") +
		(criteria.getGroupNameIn() != null ? " and groupData.shortDesc in (:shortDescList)" : "") +
		(criteria.getStoreIds() != null ? " and exists (select groupAssocId from GroupAssocData storeAssoc where groupData.groupId = storeAssoc.groupId and storeAssoc.groupAssocCd = (:storeOfGroup) and storeAssoc.busEntityId in (:storeIds))" : "") +
		(criteria.getUserId() != null ? " and (not exists (select groupAssocId from GroupAssocData storeAssoc where groupData.groupId = storeAssoc.groupId and storeAssoc.groupAssocCd = (:storeOfGroup)) " +
    									"	or (exists (select groupAssocId from GroupAssocData storeAssoc where groupData.groupId = storeAssoc.groupId and storeAssoc.groupAssocCd = (:storeOfGroup) and storeAssoc.busEntityId in (select busEntityId from UserAssocData where userAssocCd = 'STORE' and userId = (:userId)))))" : "") +
		(criteria.getGroupId() != null ? " and  groupData.groupId = (:groupId)" : "") +
		(Utility.isSet(criteria.getGroupIds()) ? " and groupData.groupId in (:groupIds)" : "");

        Query query = em.createQuery(sql);


        if (Utility.isSet(criteria.getGroupType())) {
            query.setParameter("groupTypeCd", criteria.getGroupType());
        }

        if (Utility.isSet(criteria.getGroupStatus())) {
            query.setParameter("groupStatusCd", criteria.getGroupStatus());
        }

        if (Utility.isSet(criteria.getGroupName())) {
            query.setParameter("shortDesc", QueryHelp.toFilterValue(criteria.getGroupName().toUpperCase(), criteria.getGroupNameMatchType()));
        }

        if (Utility.isSet(criteria.getStoreIds())) {
            query.setParameter("storeOfGroup", RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
            query.setParameter("storeIds", criteria.getStoreIds().size() == 1 ? criteria.getStoreIds().get(0) : criteria.getStoreIds());
        }

        if (criteria.getUserId() != null){
        	query.setParameter("storeOfGroup", RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
        	query.setParameter("userId", criteria.getUserId());
        }
        if (Utility.isSet(criteria.getGroupId())) {
            query.setParameter("groupId", criteria.getGroupId());
        }
        if (Utility.isSet(criteria.getGroupIds())) {
            query.setParameter("groupIds", criteria.getGroupIds());
        }

        if (Utility.isSet(criteria.getGroupNameIn())) {
            query.setParameter("shortDescList", criteria.getGroupNameIn());
        }


        x = query.getResultList(); 

        logger.info("findGroupsByCriteria()=> END, fetched: " + x.size() + " rows");

        return x;
    }
    
    public List<GroupListView> findGroupViewsByCriteria(GroupSearchCriteria criteria) throws IllegalStateException {
    	List<GroupData> x = findGroupsByCriteria(criteria);
    	List<GroupListView> groupVList= new ArrayList<GroupListView>();
    	if (x.size()==0)
    		return groupVList;
    	
    	for (GroupData groupD : x){    		
    		GroupView groupV = getGroupView(groupD);
    		GroupListView groupLV = new GroupListView(groupD.getGroupId(), groupD.getShortDesc(), groupD.getGroupTypeCd(), groupD.getGroupStatusCd(), groupV.getAssocStoreName());
    		groupVList.add(groupLV);    		
    	}
    	return groupVList;
    }
    
    private GroupView getGroupView(GroupData groupD){
    	Query query = em.createQuery("Select store.busEntityId, store.shortDesc from BusEntityData store, GroupAssocData storeAssoc " +
				"where storeAssoc.groupId = (:groupId) " +
				"and store.busEntityId = storeAssoc.busEntityId " +
				"and store.busEntityStatusCd = (:busEntityStatusCd) " +
				"and storeAssoc.groupAssocCd = (:storeOfGroup) ");
		
		query.setParameter("groupId", groupD.getGroupId());
		query.setParameter("busEntityStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);
		query.setParameter("storeOfGroup", RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
		List<Object[]> storeNames= query.getResultList();
		Long storeId = new Long(0);
		String storeName = null;
		if (storeNames.size() > 1){
			throw new IllegalStateException("Multiple active stores have been configured for groupId: " + groupD.getGroupId());
		}else if (storeNames.size() == 1){
			storeId = (Long)storeNames.get(0)[0];
			storeName = (String)storeNames.get(0)[1];    			
		}
		GroupView gv = new GroupView(groupD, storeId, storeName);
		return gv;
    }


    public List<UserGroupInformationView> findUserGroupInformation(Long userId, String userTypeCd) {

        logger.info("findUserGroupInformation()=> BEGIN");

        List<UserGroupInformationView> groupInfViews = new ArrayList<UserGroupInformationView>();

        GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(em);
        GenericReportDAO reportDao = new GenericReportDAOImpl(em);


        Map<Long, GroupData> groupsMap = findAssociatedUserGroups(userId, userTypeCd);

        if (groupsMap != null) {

            logger.info("findUserGroupInformation()=> groupsMap.size()=" + groupsMap.size());

            for (Long groupId : groupsMap.keySet()) {

                UserGroupInformationView groupInfView = new UserGroupInformationView();

                List<String> appFunctions = new ArrayList<String>();

                List<GroupAssocData> groupAssocDataList = groupAssocDao.findFunctionsByGroupIds(Utility.toList(groupId));
                if (groupAssocDataList != null) {
                    for (GroupAssocData item : groupAssocDataList) {
                        appFunctions.add(item.getApplicationFunction());
                    }
                }

                List<GenericReportData> reports = reportDao.findGenericReports(groupId);

                groupInfView.setGroup(groupsMap.get(groupId));
                groupInfView.setApplicationFunctionNames(appFunctions);
                groupInfView.setReports(reports);
                groupInfViews.add(groupInfView);
            }
        }

        logger.info("findUserGroupInformation()=> END. fetched " + groupInfViews.size() + " rows");

        return groupInfViews;


    }


    public UserGroupsView configureUserGroups(Long userId, List<Long> assignGroups, List<Long> unassignGroups) {
        GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(em);
        if (Utility.isSet(unassignGroups)) {
            groupAssocDao.removeUserGroupAssociation(userId, unassignGroups);
        }
        if (Utility.isSet(assignGroups)) {
            for (Long groupId : assignGroups) {
                groupAssocDao.addUserGroupAssociation(userId, groupId);
            }
        }
        //populate the return value with information necessary to record the history of this activity.
    	UserGroupsView returnValue = new UserGroupsView();
        returnValue.setUser(new UserDAOImpl(em).findByUserId(userId));
        returnValue.setUserStores(new StoreDAOImpl(em).findUserStores(userId));
		GroupSearchCriteria groupSearchCriteria = new GroupSearchCriteria();
        if (Utility.isSet(assignGroups)) {
        	groupSearchCriteria.setGroupIds(assignGroups);
        	returnValue.setAssignedGroups(findGroupsByCriteria(groupSearchCriteria));
        }
        if (Utility.isSet(unassignGroups)) {
        	groupSearchCriteria.setGroupIds(unassignGroups);
        	returnValue.setUnassignedGroups(findGroupsByCriteria(groupSearchCriteria));
        }
        return returnValue;
    }
	
	@Override
	public GroupView getGroupView(Long groupId) {
		GroupView groupV = null;
		GroupData groupD = em.find(GroupData.class, groupId);
		if (groupD != null){
			groupV = getGroupView(groupD);
		}
		return groupV;
	}
	
	@Override
    public GroupView saveGroup(GroupView groupView){
		GroupData groupD = groupView.getGroup();
		boolean newGroup = groupView.getGroup().getGroupId() == null;
		if (newGroup){
			groupD = super.create(groupD);
        } else {
        	groupD = super.update(groupD);
        }
		
		// update group store association
		GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(em);        
		if (groupView.getAssocStoreId() == null || groupView.getAssocStoreId().intValue()==0){// remove the association
			if (!newGroup){
				groupAssocDao.removeStoreGroupAssociation(groupD.getGroupId());
			}
		}else{// add or update group store association
			groupAssocDao.addStoreGroupAssociation(groupView.getAssocStoreId(), groupD.getGroupId());
		}
		
		return groupView;
	}
	
	@Override
	public List<GroupReportListView> findGroupReportViewsByCriteria(ReportSearchCriteria criteria) {
		List<GroupReportListView> reportList = new ArrayList<GroupReportListView>();
		Query query = em.createQuery("Select user.userTypeCd from UserData user " +
				"where user.userId = (:userId) " );
		
		query.setParameter("userId", criteria.getUserId());
		String userType = (String) query.getSingleResult();
		boolean isSystemAdmin = userType.equals(RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR);
		
		String sql = "SELECT distinct B.GENERIC_REPORT_ID, B.NAME \n" +
			"FROM CLW_GENERIC_REPORT B \n" ;
		if (!isSystemAdmin){
			sql += ", CLW_GROUP_ASSOC A \n" +
			"WHERE A.GROUP_ID IN ( \n" +
			"  SELECT DISTINCT GROUP_ID FROM CLW_GROUP_ASSOC WHERE USER_ID = (:userId) AND GROUP_ASSOC_CD = (:groupAssocCd1) \n" +
			"  union \n" +
			"  SELECT DISTINCT GROUP_ID FROM CLW_GROUP WHERE SHORT_DESC IN ('" + Constants.DEFAULT_GROUP + "','" +userType+ "') \n" +
			") \n" +
			"AND A.GROUP_ASSOC_CD = (:groupAssocCd2) \n" +
			"AND A.GENERIC_REPORT_ID = B.GENERIC_REPORT_ID \n";
			
		}else{
			sql += "WHERE 1=1 \n";
		}
		sql += (criteria.getReportId() != null ? "AND B.GENERIC_REPORT_ID = (:reportId) \n" : "") +
		(Utility.isSet(criteria.getReportName()) ? "AND UPPER(B.NAME) like (:reportName) \n" : "") +
		(criteria.isShowConfiguredOnly() ? "AND B.GENERIC_REPORT_ID IN (SELECT GENERIC_REPORT_ID FROM CLW_GROUP_ASSOC WHERE GROUP_ID = (:groupId) AND GROUP_ASSOC_CD = (:groupAssocCd2)) \n" : "");
		
		query = em.createNativeQuery(sql);
		if (!isSystemAdmin){
			query.setParameter("userId", criteria.getUserId());
			query.setParameter("groupAssocCd1", RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);			
		}
		if (criteria.isShowConfiguredOnly())
			query.setParameter("groupId", criteria.getGroupId());
		if (!isSystemAdmin || criteria.isShowConfiguredOnly())
			query.setParameter("groupAssocCd2", RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP);
		if (criteria.getReportId() != null)
			query.setParameter("reportId", criteria.getReportId());
		if (Utility.isSet(criteria.getReportName())){
			query.setParameter("reportName", QueryHelp.toFilterValue(criteria.getReportName().toUpperCase(), criteria.getReportNameMatchType()));
		}
		
		List<Object[]> results = query.getResultList();
		for (Object[] report : results){
			reportList.add(new GroupReportListView(((BigDecimal)report[0]).longValue(), (String)report[1]));			
		}

		return reportList;
	}
	
	public void configureGroupReports(Long groupId, List<Long> configuredIds, List<Long> notConfiguredIds){
		GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(em);
		groupAssocDao.addReportGroupAssociation(configuredIds, groupId);
		groupAssocDao.removeReportGroupAssociation(notConfiguredIds, groupId);
	}
	
	@Override
	public Map<String, List<GroupFunctionListView>> findGroupFunctionMapByCriteria(ApplicationFunctionSearchCriteria criteria){
		Map<String, List<GroupFunctionListView>> functionMap = new HashMap<String, List<GroupFunctionListView>>();	
		List<String> results = null;
		Query query = em.createQuery("Select user.userTypeCd from UserData user " +
		"where user.userId = (:userId) " );
		query.setParameter("userId", criteria.getUserId());
		String userType = (String) query.getSingleResult();
		boolean isSystemAdmin = userType.equals(RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR);
		Map<String, String> funtionToTypeMap = DbConstantResource.getApplicationFuntionsToTypeMap();
		if (isSystemAdmin && !criteria.isShowConfiguredOnly()){
			results = new ArrayList(funtionToTypeMap.keySet());
			if (Utility.isSet(criteria.getFunctionName())){
				results = getMatchStrings(results, criteria.getFunctionName(), criteria.getFunctionNameMatchType().equals(Constants.FILTER_TYPE.START_WITH));
			}
		}else{
			String sql = "SELECT DISTINCT APPLICATION_FUNCTION \n" +
				"FROM CLW_GROUP_ASSOC A WHERE 1=1 \n" ;
			if (!isSystemAdmin){
				sql += "AND GROUP_ID IN ( \n" +
				"  SELECT DISTINCT GROUP_ID FROM CLW_GROUP_ASSOC WHERE USER_ID = (:userId) AND GROUP_ASSOC_CD = (:groupAssocCd1) \n" +
				"  union \n" +
				"  SELECT DISTINCT GROUP_ID FROM CLW_GROUP WHERE SHORT_DESC IN ('" + Constants.DEFAULT_GROUP + "','" +userType+ "') \n" +
				") \n" +
				"AND GROUP_ASSOC_CD = (:groupAssocCd2) \n";
			}
			sql += (Utility.isSet(criteria.getFunctionName()) ? "AND UPPER(APPLICATION_FUNCTION) like (:functionName) \n" : "") +
				(criteria.isShowConfiguredOnly() ? "AND APPLICATION_FUNCTION IN (SELECT APPLICATION_FUNCTION FROM CLW_GROUP_ASSOC WHERE GROUP_ID = (:groupId) AND GROUP_ASSOC_CD = (:groupAssocCd2)) \n" : "");
						
			query = em.createNativeQuery(sql);
			if (!isSystemAdmin){
				query.setParameter("userId", criteria.getUserId());
				query.setParameter("groupAssocCd1", RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);			
			}
			if (criteria.isShowConfiguredOnly())
				query.setParameter("groupId", criteria.getGroupId());
			if (!isSystemAdmin || criteria.isShowConfiguredOnly())
				query.setParameter("groupAssocCd2", RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP);
			if (Utility.isSet(criteria.getFunctionName())){
				query.setParameter("functionName", QueryHelp.toFilterValue(criteria.getFunctionName().toUpperCase(), criteria.getFunctionNameMatchType()));
			}
			
			results = query.getResultList();
		}
		
		for (String function : results){
			String functionType = funtionToTypeMap.get(function);
			if (!Utility.isSet(functionType))
				continue;
			if (Utility.isSet(criteria.getFunctionType()) && !criteria.getFunctionType().equals(functionType)){
				continue;
			}
			if (functionType != null){
				List<GroupFunctionListView> functions = functionMap.get(functionType);
				if (functions == null){
					functions = new ArrayList<GroupFunctionListView>();
					functionMap.put(functionType, functions);
				}
				functions.add(new GroupFunctionListView(function, functionType));
			}
		}		
		
		return functionMap;
	}
	
	public void configureGroupFunctions(Long groupId, List<String> configuredFunctions, List<String> notConfiguredFunctions){
		GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(em);
		groupAssocDao.addFunctionGroupAssociation(configuredFunctions, groupId);
		groupAssocDao.removeFunctionGroupAssociation(notConfiguredFunctions, groupId);
	}
	
	private List<String> getMatchStrings(List<String> stringList, String matchString, boolean startWith){
		if (Utility.isSet(matchString)){
			matchString = matchString.toUpperCase().trim();
			Iterator<String> it = stringList.iterator();
			while (it.hasNext()){
				String str = (String) it.next();
				String temp = str.toUpperCase();
				if (startWith){
					if (!temp.startsWith(matchString))
						it.remove();
				}else{
					if (temp.indexOf(matchString) < 0)
						it.remove();
				}				
			}
		}		
		
		return stringList;
	}
	
	@Override
	/**
	 * Return all the configured list within the user stores
	 */
	public List<GroupConfigAllListView> findGroupConfigAllByCriteria(GroupConfigSearchCriteria criteria){
		List<GroupConfigAllListView> results = new ArrayList<GroupConfigAllListView>();
		List<GroupConfigListView> configs = findGroupConfigByCriteria(criteria);
		String groupType = criteria.getGroupType();
		String assocTypeCd = groupType.equals(RefCodeNames.GROUP_TYPE_CD.USER) ? RefCodeNames.USER_ASSOC_CD.STORE 
        		: groupType.equals(RefCodeNames.GROUP_TYPE_CD.ACCOUNT) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE 
                : groupType.equals(RefCodeNames.GROUP_TYPE_CD.DISTRIBUTOR) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.DISTRIBUTOR_OF_STORE 
        		: groupType.equals(RefCodeNames.GROUP_TYPE_CD.MANUFACTURER) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.MANUFACTURER_OF_STORE : "";
		for (GroupConfigListView v : configs){
			String storeName = "";
			String storeNames = "";
			if (!groupType.equals(RefCodeNames.GROUP_TYPE_CD.STORE)){
				List<String> assocStories = getAssociatedStores(v.getId(), assocTypeCd);
				for (int i = 0; i < assocStories.size(); i++){
					if (i == 0){
						storeName = assocStories.get(i);
						storeNames = assocStories.get(i);
					}else{
						if (i == 1){
							storeName += "...";
						}
						storeNames += "\r\n"+ assocStories.get(i);
					}
				}
			}else{
				storeName = storeNames = v.getName();
			}
			results.add(new GroupConfigAllListView(v, storeName, storeNames));
		}
		return results;
	}
	
	@Override
	/**
	 * User must exists in current store
	 * User cannot associated with store that not associated with current user (not SYSTEM_ADMINISTRATOR)
	 * Return empty result if group is associated with a store (STORE_OF_GROUP) and current configured store is not same store
	 */	
	public List<GroupConfigListView> findGroupConfigByCriteria(GroupConfigSearchCriteria criteria){
		List<GroupConfigListView> results = new ArrayList<GroupConfigListView>();
		Query query = em.createQuery("Select user.userTypeCd from UserData user " +
		"where user.userId = (:userId) ").setParameter("userId", criteria.getUserId());

		String userType = (String) query.getSingleResult();
		boolean isSystemAdmin = userType.equals(RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR);
		
		String selUserStoreIds = "select busEntityId from UserAssocData where userAssocCd = '" + RefCodeNames.USER_ASSOC_CD.STORE + "' and user_id = " + criteria.getUserId();
						
		query = em.createQuery("Select busEntityId from GroupAssocData where groupId = (:groupId) and groupAssocCd = (:groupOfStore)")
			.setParameter("groupId", criteria.getGroupId()).setParameter("groupOfStore", RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
		
		Long storeAssocId = null;
		List<Long> storeAssocIds = query.getResultList();
		if (!storeAssocIds.isEmpty())
			storeAssocId = storeAssocIds.get(0);
		if (storeAssocId != null && criteria.getStoreId() != null && !storeAssocId.equals(criteria.getStoreId()))
				return results;
		
		String sql = null;
		String groupType = criteria.getGroupType();
		if (groupType.equals(RefCodeNames.GROUP_TYPE_CD.USER)){			
			sql = "Select new com.espendwise.manta.model.view.GroupConfigListView(\n" +
            "   user.userId,\n" +
            "   user.userName, '', '', '', '',\n" +
            "   user.userStatusCd,\n" +            
            "   user.firstName,\n" +
            "   user.lastName,\n" +
            "   user.userTypeCd\n" +
            ") " +
            " from UserData user";			
            sql += " where 1=1\n";
            ;
            
			if (Utility.isSet(criteria.getStoreId()) || !isSystemAdmin){
				String temp = Utility.isSet(criteria.getStoreId()) ? "   and userAssoc.busEntityId = " + criteria.getStoreId() 
						: "   and userAssoc.busEntityId in (" + selUserStoreIds + ")\n";
				sql += " and user.userId in (\n" +
						"   select userAssoc.userId from UserAssocData userAssoc \n" +
						"   where user.userId = userAssoc.userId \n" +
						"   and userAssoc.userAssocCd = '" + RefCodeNames.USER_ASSOC_CD.STORE + "'\n" + temp +
						" )\n";
			}
						
			if (Utility.isSet(criteria.getSearchId())){
				sql += " and user.userId = " + criteria.getSearchId();
			}
			if (Utility.isSet(criteria.getSearchName())){
				sql += " and UPPER(user.userName) like :searchName\n";
			}
			if (criteria.isShowConfiguredOnly()){
				sql += " and user.userId in (select userId from GroupAssocData groupAssoc where groupAssoc.groupId = " + criteria.getGroupId() + 
					" and groupAssoc.groupAssocCd = '" + RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP + "')\n";
			}
			if (!criteria.isShowInactive()){
				sql += " and user.userStatusCd = '" + RefCodeNames.USER_STATUS_CD.ACTIVE + "'\n";
			}
			if (!isSystemAdmin && Utility.isSet(criteria.getStoreId())){
				sql += " and not exists(\n" +
					"    select busEntityId from UserAssocData where userId=user.userId and userAssocCd = '" + RefCodeNames.USER_ASSOC_CD.STORE + "'\n" +
					"    and busEntityId not in (" + selUserStoreIds + ")\n" +
					" )\n";
			}
			
			query = em.createQuery(sql);
				
		} else {
			
			sql = "Select new com.espendwise.manta.model.view.GroupConfigListView(" +
            "   b.busEntityId," +
            "   b.shortDesc, " +
            "   address.address1, " +
            "   address.city, " +
            "   address.stateProvinceCd, " +
            "   address.postalCode," +
            "   b.busEntityStatusCd," +            
            "   '', '', ''" +
            ")" +
            " from BusEntityFullEntity b " +
            "    left join b.addresses address with address.primaryInd = 1";
			if (groupType.equals(RefCodeNames.GROUP_TYPE_CD.STORE)){
				sql += " where b.busEntityTypeCd = '" + RefCodeNames.BUS_ENTITY_TYPE_CD.STORE + "'";
				if (!isSystemAdmin){
					sql += " and b.busEntityId in (" + selUserStoreIds + ")";
				}
			}else{
				String busEntityAssocCd = groupType.equals(RefCodeNames.GROUP_TYPE_CD.DISTRIBUTOR) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.DISTRIBUTOR_OF_STORE
						: groupType.equals(RefCodeNames.GROUP_TYPE_CD.MANUFACTURER) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.MANUFACTURER_OF_STORE 
						: groupType.equals(RefCodeNames.GROUP_TYPE_CD.ACCOUNT) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE : "";
							
				sql += ", BusEntityAssocData ba" + 
					" where b.busEntityId = ba.busEntity1Id" + 
					" and ba.busEntityAssocCd = '" + busEntityAssocCd + "'";
				if (Utility.isSet(criteria.getStoreId())){
					sql += " and ba.busEntity2Id = " + criteria.getStoreId();
				}else if (!isSystemAdmin){
					sql += " and ba.busEntity2Id in (" + selUserStoreIds + ")";
				}
			}
			
			if (Utility.isSet(criteria.getSearchId())){
				sql += " and b.busEntityId = " + criteria.getSearchId();
			}
			if (Utility.isSet(criteria.getSearchName())){
				sql += " and UPPER(b.shortDesc) like :searchName";
			}
			
			if (criteria.isShowConfiguredOnly()){
				sql += " and b.busEntityId in (select busEntityId from GroupAssocData groupAssoc where groupAssoc.groupId = " + criteria.getGroupId() + 
					" and groupAssoc.groupAssocCd = '" + RefCodeNames.GROUP_ASSOC_CD.BUS_ENTITY_OF_GROUP + "')";
			}
			if (!criteria.isShowInactive()){
				sql += " and b.busEntityStatusCd = '" + RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE + "'";
			}
			
			query = em.createQuery(sql);			
				
		}		
		
		if (Utility.isSet(criteria.getSearchName())){
			query.setParameter("searchName", QueryHelp.toFilterValue(criteria.getSearchName().toUpperCase(), criteria.getSearchNameMatchType()));
		}
		
		
		if (criteria.getLimit() != null) {  query.setMaxResults(criteria.getLimit());  }
		results = query.getResultList();		
		
		logger.info("findGroupConfigByCriteria)=> END, fetched : " + results.size() + " rows");
		return results;
		
	}
			
	public void configureGroupAssocioation(Long groupId, List configured,List unConfigured, String groupAssocCd){
		GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(em);
		groupAssocDao.addGroupAssociation(configured, groupId, groupAssocCd);
		groupAssocDao.removeGroupAssociation(unConfigured, groupId, groupAssocCd);
	}
	
	/**
	 * Get list of associated store names for a giving user or bus entity
	 */
	private List<String> getAssociatedStores(Long id, String storeAssocCd){
		List<String> returnList = new ArrayList<String>();
		String sql = null;
		
		if (storeAssocCd.equals(RefCodeNames.USER_ASSOC_CD.STORE)){
			sql = "select store.shortDesc from BusEntityFullEntity store inner join store.userAssocs userAssoc inner join userAssoc.userId user " +
					"where user.userId = :id " +
					"and userAssoc.userAssocCd = '" +storeAssocCd + "'";
		}else{
			sql = "select store.shortDesc from BusEntityFullEntity store join store.busEntityAssocsForBusEntity2Id busAssoc inner join busAssoc.busEntity1Id entity " +
					"where entity.busEntityId = :id " +
					"and busAssoc.busEntityAssocCd = '" +storeAssocCd + "'";
		}
			
		Query query = em.createQuery(sql).setParameter("id", id);
		returnList = query.getResultList();

		return returnList;
	}
	
	@Override
	/**
	 * return list of store names where entity (User, Store, Account, Manufacturer....) associated excluding the store id: storeIdExcluded
	 */
	public List<String> getStoreAssociations(Long groupId, String groupType, Long storeIdExcluded){
		String groupAssocCd = groupType.equals(RefCodeNames.GROUP_TYPE_CD.USER) ? RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP 
        		: RefCodeNames.GROUP_ASSOC_CD.BUS_ENTITY_OF_GROUP;
		String storeAssocCd = groupType.equals(RefCodeNames.GROUP_TYPE_CD.USER) ? RefCodeNames.USER_ASSOC_CD.STORE 
        		: groupType.equals(RefCodeNames.GROUP_TYPE_CD.ACCOUNT) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE 
                : groupType.equals(RefCodeNames.GROUP_TYPE_CD.DISTRIBUTOR) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.DISTRIBUTOR_OF_STORE 
        		: groupType.equals(RefCodeNames.GROUP_TYPE_CD.MANUFACTURER) ? RefCodeNames.BUS_ENTITY_ASSOC_CD.MANUFACTURER_OF_STORE : "";
		
		String sql = null;
		
		if (groupType.equals(RefCodeNames.GROUP_TYPE_CD.USER)){
			sql = "select distinct s.short_desc from clw_bus_entity s " +
					"inner join clw_user_assoc ua on s.bus_entity_id = ua.bus_entity_id and ua.user_assoc_cd = :storeAssocCd " +
					"where ua.user_id in (select user_id from clw_group_assoc where group_id=:groupId and group_assoc_cd = :groupAssocCd) " +
					"and exists (select user_id from clw_user_assoc where user_id = ua.user_id and bus_entity_id != :storeIdExcluded) " +
					"and not exists (select user_id from clw_user_assoc where user_id = ua.user_id and bus_entity_id = :storeIdExcluded)";			
		}else{
			sql = "select distinct s.short_desc from clw_bus_entity s " +
					"inner join clw_bus_entity_assoc ba on s.bus_entity_id = ba.bus_entity2_id and ba.bus_entity_assoc_cd = :storeAssocCd " +
					"where ba.bus_entity1_id in (select bus_entity_id from clw_group_assoc where group_id=:groupId and group_assoc_cd = :groupAssocCd) " +
					"and exists (select bus_entity_id from clw_bus_entity_assoc where bus_entity_id != :storeIdExcluded)";
		}
		Query query = em.createNativeQuery(sql)
			.setParameter("storeAssocCd", storeAssocCd)
			.setParameter("groupId", groupId)
			.setParameter("groupAssocCd", groupAssocCd)
			.setParameter("storeIdExcluded", storeIdExcluded);
		return query.getResultList();
	}	
}
