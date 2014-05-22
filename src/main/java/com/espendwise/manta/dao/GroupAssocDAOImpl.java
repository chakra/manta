package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupAssocDAOImpl extends DAOImpl implements GroupAssocDAO {

    private static final Logger logger = Logger.getLogger(GroupAssocDAOImpl.class);

    public GroupAssocDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Long> findUserGroupIds(Long userId) {

        logger.info("findUserGroupIds()=> userId :  " + userId);

        if (userId != null) {

            Query q = em.createQuery("Select assoc.groupId from GroupAssocData assoc " +
                    "where assoc.userId = (:userId)" +
                    " and assoc.groupAssocCd = (:assocCd)");

            q.setParameter("userId", userId);
            q.setParameter("assocCd", RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);

            return q.getResultList();
        }

        logger.info("findUserGroupIds()=> END  ");

        return new ArrayList<Long>();
    }


    public List<GroupAssocData> findFunctionsByGroupIds(Collection<Long> groupIds) {

        if (Utility.isSet(groupIds)) {

            Query q = em.createQuery("Select assoc from GroupAssocData assoc " +
                    "where assoc.groupId in (:groupIds) " +
                    " and assoc.groupAssocCd = (:assocCd)");

            q.setParameter("groupIds", groupIds);
            q.setParameter("assocCd", RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP);

            return (List<GroupAssocData>) q.getResultList();

        } else {

            return Utility.emptyList(GroupAssocData.class);

        }
    }
    public List<GroupAssocData> findReportsByGroupIds(Collection<Long> groupIds) {

        if (Utility.isSet(groupIds)) {

            Query q = em.createQuery("Select assoc from GroupAssocData assoc " +
                    "where assoc.groupId in (:groupIds) " +
                    " and assoc.groupAssocCd = (:assocCd)");

            q.setParameter("groupIds", groupIds);
            q.setParameter("assocCd", RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP);

            return (List<GroupAssocData>) q.getResultList();

        } else {

            return Utility.emptyList(GroupAssocData.class);

        }
    }

    public void removeUserGroupAssociation(Long userId, List<Long> groupIds)  {

        if ((userId== null || userId == 0) && !Utility.isSet(groupIds)) {
            return;
        }

        Query q = em.createQuery("Delete from GroupAssocData assoc where assoc.userId = (:userId)" +
                " and assoc.groupId in (:groupIds) " +
                "and assoc.groupAssocCd = (:groupAssocCd)"
        );

        q.setParameter("userId", userId);
        q.setParameter("groupIds", groupIds);
        q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);

        q.executeUpdate();


    }


    public void addUserGroupAssociation(Long userId, Long groupId)  {


        if ((userId== null || userId == 0)) {
            return;
        }

        if ((groupId== null || groupId == 0)) {
            return;
        }


        Query q = em.createQuery("select count(*) from GroupAssocData assoc " +
                "where assoc.groupId = (:groupId) and  assoc.userId = (:userId) " +
                "and assoc.groupAssocCd = (:groupAssocCd)");

        q.setParameter("groupId", groupId);
        q.setParameter("userId", userId);
        q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);

        if(q.getFirstResult() == 0 ) {

            GroupAssocData assocData = new GroupAssocData();

            assocData.setGroupAssocCd(RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);
            assocData.setGroupId(groupId);
            assocData.setUserId(userId);

            create(assocData);
        }

    }       
    
    public void removeStoreGroupAssociation(Long groupId)  {
        if ((groupId== null || groupId.intValue() == 0) ) {
            return;
        }

        Query q = em.createQuery("Delete from GroupAssocData assoc " +
        		"where assoc.groupId = (:groupId) " +
                "and assoc.groupAssocCd = (:groupAssocCd)"
        );

        q.setParameter("groupId", groupId);
        q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
        q.executeUpdate();
    }

    public void addStoreGroupAssociation(Long storeId, Long groupId)  {

        if ((storeId== null || storeId.intValue() == 0)) {
            return;
        }

        if ((groupId== null || groupId.intValue() == 0)) {
            return;
        }

        Query q = em.createQuery("select count(*) from GroupAssocData assoc " +
                "where assoc.groupId = (:groupId) and  assoc.busEntityId = (:storeId) " +
                "and assoc.groupAssocCd = (:groupAssocCd)");

        q.setParameter("groupId", groupId);
        q.setParameter("storeId", storeId);
        q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
        Long count = (Long)(q.getResultList().get(0));
        
        if(count.intValue()==0 ) {
            GroupAssocData assocData = new GroupAssocData();
            assocData.setGroupAssocCd(RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP);
            assocData.setGroupId(groupId);
            assocData.setBusEntityId(storeId);
            create(assocData);
        }
    }
    
    public void addReportGroupAssociation(List<Long> reportIds, Long groupId)  {

        if (!Utility.isSet(reportIds) || !Utility.isSet(groupId)) {
            return;
        }

        for (Long reportId : reportIds){
        	Query q = em.createQuery("select count(*) from GroupAssocData assoc " +
                    "where assoc.groupId = (:groupId) and  assoc.genericReportId = (:reportId) " +
                    "and assoc.groupAssocCd = (:groupAssocCd)");

            q.setParameter("groupId", groupId);
            q.setParameter("reportId", reportId);
            q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP);
            Long count = (Long)(q.getResultList().get(0));
            
            if(count.intValue()==0 ) {
                GroupAssocData assocData = new GroupAssocData();
                assocData.setGroupAssocCd(RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP);
                assocData.setGroupId(groupId);
                assocData.setGenericReportId(reportId);
                create(assocData);
            }
        }        
    }
    
    public void removeReportGroupAssociation(List<Long> reportIds, Long groupId){
    	if (!Utility.isSet(groupId== null) || !Utility.isSet(reportIds)) {
            return;
        }

        Query q = em.createQuery("Delete from GroupAssocData assoc where assoc.groupId = (:groupId)" +
                " and assoc.genericReportId in (:reportIds) " +
                "and assoc.groupAssocCd = (:groupAssocCd)"
        );

        q.setParameter("groupId", groupId);
        q.setParameter("reportIds", reportIds);
        q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP);

        q.executeUpdate();
    }
    
    
    public void addFunctionGroupAssociation(List<String> functionNames, Long groupId) {

        if (!Utility.isSet(functionNames) || !Utility.isSet(groupId)) {
            return;
        }

        for (String functionName : functionNames){
        	Query q = em.createQuery("select count(*) from GroupAssocData assoc " +
                    "where assoc.groupId = (:groupId) and  assoc.applicationFunction = (:functionName) " +
                    "and assoc.groupAssocCd = (:groupAssocCd)");

            q.setParameter("groupId", groupId);
            q.setParameter("functionName", functionName);
            q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP);
            Long count = (Long)(q.getResultList().get(0));
            
            if(count.intValue()==0 ) {
                GroupAssocData assocData = new GroupAssocData();
                assocData.setGroupAssocCd(RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP);
                assocData.setGroupId(groupId);
                assocData.setApplicationFunction(functionName);
                create(assocData);
            }
        }        
    }
    
    public void removeFunctionGroupAssociation(List<String> functionNames, Long groupId){
    	if (!Utility.isSet(groupId== null) || !Utility.isSet(functionNames)) {
            return;
        }

        Query q = em.createQuery("Delete from GroupAssocData assoc where assoc.groupId = (:groupId)" +
                " and assoc.applicationFunction in (:functionNames) " +
                "and assoc.groupAssocCd = (:groupAssocCd)"
        );

        q.setParameter("groupId", groupId);
        q.setParameter("functionNames", functionNames);
        q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP);

        q.executeUpdate();
    }
    
    public void addUserGroupAssociation(List<Long> userIds, Long groupId)  {

        if (!Utility.isSet(userIds) || !Utility.isSet(groupId)) {
            return;
        }

        for (Long userId : userIds){
        	Query q = em.createQuery("select count(*) from GroupAssocData assoc " +
                    "where assoc.groupId = (:groupId) and  assoc.userId = (:userId) " +
                    "and assoc.groupAssocCd = (:groupAssocCd)");

            q.setParameter("groupId", groupId);
            q.setParameter("userId", userId);
            q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);
            Long count = (Long)(q.getResultList().get(0));
            
            if(count.intValue()==0 ) {
                GroupAssocData assocData = new GroupAssocData();
                assocData.setGroupAssocCd(RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);
                assocData.setGroupId(groupId);
                assocData.setUserId(userId);
                create(assocData);
            }
        }        
    }
    
    public void removeUserGroupAssociation(List<Long> reportIds, Long groupId){
    	if (!Utility.isSet(groupId== null) || !Utility.isSet(reportIds)) {
            return;
        }

        Query q = em.createQuery("Delete from GroupAssocData assoc where assoc.groupId = (:groupId)" +
                " and assoc.genericReportId in (:reportIds) " +
                "and assoc.groupAssocCd = (:groupAssocCd)"
        );

        q.setParameter("groupId", groupId);
        q.setParameter("reportIds", reportIds);
        q.setParameter("groupAssocCd", RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP);

        q.executeUpdate();
    }

    public void addGroupAssociation(List assocKeys, Long groupId, String groupAssocType) {

        if (!Utility.isSet(assocKeys) || !Utility.isSet(groupId)) {
            return;
        }

        String temp=null;
        if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP)) temp = "assoc.userId = (:assocKey) ";
        else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP)) temp = "assoc.genericReportId = (:assocKey) ";
        else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP)) temp = "assoc.applicationFunction = (:assocKey) ";
        else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.BUS_ENTITY_OF_GROUP)) temp = "assoc.busEntityId = (:assocKey) ";
        else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP)) temp = "assoc.busEntityId = (:assocKey) ";
        else logger.error("Group Assocition Type not supported for configuration: " + groupAssocType);
        
        for (Object key : assocKeys){
        	Query q = em.createQuery("select count(*) from GroupAssocData assoc " +
                    "where assoc.groupId = (:groupId) and " + temp +
                    "and assoc.groupAssocCd = (:groupAssocCd)");

            q.setParameter("groupId", groupId);
            q.setParameter("assocKey", key);
            q.setParameter("groupAssocCd", groupAssocType);
            Long count = (Long)(q.getResultList().get(0));
            
            if(count.intValue()==0 ) {
                GroupAssocData assocData = new GroupAssocData();
                assocData.setGroupAssocCd(groupAssocType);
                assocData.setGroupId(groupId);
                if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP))
                	assocData.setUserId((Long)key);
                else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP)) 
                	assocData.setGenericReportId((Long)key);
                else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP)) 
                	assocData.setApplicationFunction((String)key);
                else  
                	assocData.setBusEntityId((Long)key);
                create(assocData);
            }
        }        
    }
    
    public void removeGroupAssociation(List assocKeys, Long groupId, String groupAssocType) {
    	if (!Utility.isSet(groupId== null) || !Utility.isSet(assocKeys)) {
            return;
        }

    	String temp=null;
        if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP)) temp = "and assoc.userId in (:assocKeys) ";
        else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP)) temp = "and assoc.genericReportId in (:assocKeys) ";
        else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP)) temp = "and assoc.applicationFunction in (:assocKeys) ";
        else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.BUS_ENTITY_OF_GROUP)) temp = "and assoc.busEntityId in (:assocKeys) ";
        else if (groupAssocType.equals(RefCodeNames.GROUP_ASSOC_CD.STORE_OF_GROUP)) temp = "and assoc.busEntityId in (:assocKeys) ";
        else logger.error("Group Assocition Type not supported for configuration: " + groupAssocType);
        
        Query q = em.createQuery("Delete from GroupAssocData assoc where assoc.groupId = (:groupId) " +
                temp + "and assoc.groupAssocCd = (:groupAssocCd)"
        );

        q.setParameter("groupId", groupId);
        q.setParameter("assocKeys", assocKeys);
        q.setParameter("groupAssocCd", groupAssocType);

        q.executeUpdate();
    }
    
    public List<GroupAssocData> getGroupAssociation(Long groupId, String groupAssocCd)  {
        if ((groupId== null || groupId.intValue() == 0) ) {
            return new ArrayList<GroupAssocData>();
        }

        Query q = em.createQuery("Select assoc from GroupAssocData assoc " +
        		"where assoc.groupId = (:groupId) " +
                "and assoc.groupAssocCd = (:groupAssocCd)"
        );

        q.setParameter("groupId", groupId);
        q.setParameter("groupAssocCd", groupAssocCd);
        List<GroupAssocData> results = q.getResultList();
        
        return results;
    }
    
    public List<GroupAssocData> updateUserGroupAssocs(Long userId, List<GroupAssocData> newAssocs) {
        List<GroupAssocData> resultAssoc = new ArrayList<GroupAssocData>();
        
        UserDAO userDAO = new UserDAOImpl(em);
        
        List<GroupAssocData> oldAssoc = userDAO.findUserGroupAssocs(userId);

        GroupAssocData element;
        if (Utility.isSet(newAssocs)) {
            for (GroupAssocData assoc : newAssocs) {
                assoc.setUserId(userId);
                if (assoc.getGroupAssocId() == null) {
                    assoc = super.create(assoc);
                } else {
                    assoc = super.update(assoc);
                    if (Utility.isSet(oldAssoc)) {
                        int i;
                        for (i = 0; i < oldAssoc.size(); i++) {
                            element = oldAssoc.get(i);
                            if (assoc.getGroupAssocId().equals(element.getGroupAssocId()) &&
                                assoc.getGroupId().equals(element.getGroupId()) &&
                                assoc.getGroupAssocCd().equals(element.getGroupAssocCd())) {
                                break;
                            }    
                        }
                        if (i < oldAssoc.size()) {
                            oldAssoc.remove(i);
                        }
                    }
                }

                resultAssoc.add(assoc);
            }
        }
        
        if (Utility.isSet(oldAssoc)) {
            for (GroupAssocData assoc : oldAssoc) {
                em.remove(assoc);
            }
        }

        return resultAssoc;
   }   

}
