package com.espendwise.manta.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.HistoryCriteria;

@Repository
public class HistoryDAOImpl extends DAOImpl implements HistoryDAO {

    private static final Logger logger = Logger.getLogger(HistoryDAOImpl.class);

    public HistoryDAOImpl() {
        this(null);
    } 

    public HistoryDAOImpl(EntityManager entityManager) {
        super(entityManager);
    } 
 
    @Override
    public void createHistoryRecord(HistoryData historyData, Set<HistoryObjectData> historyObjectDatas, Set<HistorySecurityData> historySecurityDatas) {
    	HistoryData newHistoryData = super.create(historyData);
    	historyData.setHistoryId(newHistoryData.getHistoryId());
    	if (Utility.isSet(historyObjectDatas)) {
    		Iterator<HistoryObjectData> historyObjectDataIterator = historyObjectDatas.iterator();
    		while (historyObjectDataIterator.hasNext()) {
    			HistoryObjectData historyObjectData = historyObjectDataIterator.next();
    			historyObjectData.setHistoryId(newHistoryData.getHistoryId());
    			HistoryObjectData newHistoryObjectData = super.create(historyObjectData);
    			historyObjectData.setHistoryObjectId(newHistoryObjectData.getHistoryObjectId());
    		}
    	}
    	if (Utility.isSet(historySecurityDatas)) {
    		Iterator<HistorySecurityData> historySecurityDataIterator = historySecurityDatas.iterator();
    		while (historySecurityDataIterator.hasNext()) {
    			HistorySecurityData historySecurityData = historySecurityDataIterator.next();
    			historySecurityData.setHistoryId(newHistoryData.getHistoryId());
    			HistorySecurityData newHistorySecurityData = super.create(historySecurityData);
    			historySecurityData.setHistorySecurityId(newHistorySecurityData.getHistorySecurityId());
    		}
    	}
    }

    
    @Override
    public List<HistoryData> findHistoryRecords(HistoryCriteria criteria) {
        
        StringBuilder historyQuery = new StringBuilder("select object(history) from HistoryData history");
        if (Utility.isSet(criteria.getObjectType()) || Utility.isSet(criteria.getObjectId())) {
        	historyQuery.append(", HistoryObjectData historyObject");
        }
        String appender = " where ";
        if (Utility.isSet(criteria.getObjectId())) {
        	historyQuery.append(appender);
        	appender = " and ";
        	historyQuery.append("history.historyId = historyObject.historyId and historyObject.objectId = (:objectId)");
        }
        if (Utility.isSet(criteria.getObjectType())) {
        	historyQuery.append(appender);
        	appender = " and ";
        	historyQuery.append("history.historyId = historyObject.historyId and historyObject.objectTypeCd = (:objectTypeCd)");
        }
        if (Utility.isSet(criteria.getTransactionType())) {
        	historyQuery.append(appender);
        	appender = " and ";
        	historyQuery.append("history.historyTypeCd = (:transactionType)");
        }
        if (Utility.isSet(criteria.getStartDate())) {
        	historyQuery.append(appender);
        	appender = " and ";
        	historyQuery.append("history.activityDate >= (:startDate)");
        }
        if (Utility.isSet(criteria.getEndDate())) {
        	historyQuery.append(appender);
        	appender = " and ";
        	historyQuery.append("history.activityDate <= (:endDate)");
        }
        
        //only return those history records associated with the users current store, the accounts underneath that store,
        //or the locations underneath those accounts
    	historyQuery.append(appender);
    	appender = " and ";
    	
    	// system admin and admin user should allow access history record without history security record associated
    	boolean checkSecurity = !Auth.getAppUser().getIsSystemAdmin() && !Auth.getAppUser().getIsAdmin();
    	if (!checkSecurity){
    		historyQuery.append(" (not exists (select historySecurity.historyId from HistorySecurityData historySecurity where history.historyId = historySecurity.historyId) or");
    	}
    	
    	//include records associated with the current store
    	historyQuery.append(" exists (select historySecurity.historyId from HistorySecurityData historySecurity where history.historyId = historySecurity.historyId");
    	historyQuery.append(" and ((historySecurity.busEntityTypeCd = (:typeCodeStore)");
    	historyQuery.append(" and historySecurity.busEntityId = (:storeId))");
    	//include records associated with any accounts under the current store
    	historyQuery.append(" OR (historySecurity.busEntityTypeCd = (:typeCodeAccount)");
    	historyQuery.append(" and historySecurity.busEntityId IN (Select busEntity1Id from BusEntityAssocData accountAssociations");
    	historyQuery.append(" where accountAssociations.busEntityAssocCd = (:storeAccountAssocCd) and accountAssociations.busEntity2Id = (:storeId)))");
    	//include records associated with any locations under any accounts under the current store
    	historyQuery.append(" OR (historySecurity.busEntityTypeCd = (:typeCodeLocation)");
    	historyQuery.append(" and historySecurity.busEntityId IN (select busEntity1Id from BusEntityAssocData locationAssociations");
    	historyQuery.append(" where locationAssociations.busEntityAssocCd = (:accountLocationAssocCd) and busEntity2Id in (Select busEntity1Id from BusEntityAssocData accountAssociations");
    	historyQuery.append(" where accountAssociations.busEntityAssocCd = (:storeAccountAssocCd) and accountAssociations.busEntity2Id = (:storeId))))))");
    	if (!checkSecurity){
    		historyQuery.append(")");
    	}
        Query q = em.createQuery(historyQuery.toString());
        if (Utility.isSet(criteria.getObjectId())) {
            q.setParameter("objectId", criteria.getObjectId());
        }
        if (Utility.isSet(criteria.getObjectType())) {
            q.setParameter("objectTypeCd", criteria.getObjectType());
        }
        if (Utility.isSet(criteria.getTransactionType())) {
            q.setParameter("transactionType", criteria.getTransactionType());
        }
        if (Utility.isSet(criteria.getStartDate())) {
            q.setParameter("startDate", criteria.getStartDate());
        }
        if (Utility.isSet(criteria.getEndDate())) {
            q.setParameter("endDate", criteria.getEndDate());
        }
        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }
        q.setParameter("typeCodeStore", RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
        AppStoreContext storeContext = Auth.getAppUser().getContext(AppCtx.STORE);
        Long storeId = storeContext.getStoreId();
        q.setParameter("storeId", storeId);
        q.setParameter("typeCodeAccount", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
        q.setParameter("storeAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        q.setParameter("typeCodeLocation", RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
        q.setParameter("accountLocationAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
        
        List<HistoryData> historyRecords = q.getResultList();
        
        

    	logger.info("findHistoryRecords()===> found : "+ historyRecords.size());
        return historyRecords;

    }
}
