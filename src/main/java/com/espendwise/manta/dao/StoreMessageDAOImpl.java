package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.StoreMessageAssocData;
import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.model.data.StoreMessageDetailData;
import com.espendwise.manta.model.entity.StoreMessageEntity;
import com.espendwise.manta.model.entity.StoreMessageListEntity;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.StoreMessageListView;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.criteria.StoreMessageDataCriteria;
import com.espendwise.manta.util.criteria.StoreMsgAccountConfigCriteria;
import com.espendwise.manta.util.parser.Parse;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Sample StoreMessage DAO class for fetching store messages.
 */
public class StoreMessageDAOImpl extends DAOImpl implements StoreMessageDAO {

    private static final Logger logger = Logger.getLogger(StoreMessageDAOImpl.class);

    public StoreMessageDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public StoreMessageData saveMessageData(Long storeId, StoreMessageData storeMessageData) {

        if (storeMessageData.getStoreMessageId() == null) {
            storeMessageData = (StoreMessageData) create(storeMessageData);
            createAssociation(storeMessageData.getStoreMessageId(), Utility.toList(storeId), RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_STORE);
        } else {
            storeMessageData = (StoreMessageData) update(storeMessageData);
        }

        return storeMessageData;
    }
    
    
    public StoreMessageDetailData saveMessageDetailData( StoreMessageDetailData storeMessageDetailData) {

        if (storeMessageDetailData.getStoreMessageDetailId() == null) {
            storeMessageDetailData = (StoreMessageDetailData) create(storeMessageDetailData);
        } else {
            storeMessageDetailData = (StoreMessageDetailData) update(storeMessageDetailData);
        }

        return storeMessageDetailData;
    }
    
    
    public StoreMessageEntity saveMessage(Long storeId, StoreMessageEntity storeMessage){
    	storeMessage.setMessage(saveMessageData(storeId, storeMessage.getMessage()));
    	List<StoreMessageDetailData> detailList = new ArrayList<StoreMessageDetailData>();
    	
    	for(StoreMessageDetailData  detailData : storeMessage.getDetails()){
    		detailData.setStoreMessageId(storeMessage.getMessage().getStoreMessageId());
            detailList.add(saveMessageDetailData(  detailData));
    	}
    	storeMessage.setDetails(detailList);
    	return storeMessage;
    	
    }

    public StoreMessageData findStoreMessage(Long storeId, Long storeMessageId) {

        logger.info(" findStoreMessage()=> BEGIN");
        logger.info(" findStoreMessage()=> storeId: " + storeId);
        logger.info(" findStoreMessage()=> storeMessageId: " + storeMessageId);

        Query q = em.createQuery("select object(m) from StoreMessageData m, StoreMessageAssocData a  " +
                "where m.storeMessageId=:storeMessageId" +
                " and a.storeMessageId = m.storeMessageId" +
                " and a.busEntityId=(:storeId) " +
                " and a.storeMessageAssocCd = (:assocCd)");

        q.setParameter("storeMessageId", storeMessageId);
        q.setParameter("storeId", storeId);
        q.setParameter("assocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_STORE);

        List<StoreMessageData> messages = (List<StoreMessageData>) q.getResultList();

        logger.info("findStoreMessage()=> END,  " + (messages.isEmpty() ? "message not found" : "OK!"));

        return messages.isEmpty() ? null : messages.get(0);
    }

    public List<StoreMessageListView> findMessagesByCriteria(StoreMessageDataCriteria criteria) {

        logger.info("findMessagesByCriteria()=> BEGIN");
        logger.info("findMessagesByCriteria()=> criteria: " + criteria);

        //StoreMessageListEntity(Long storeMessageId, String messageTitle, String shortDesc, Date postedDate, Date endDate, String forcedRead, Long howManyTimes, String published, String languageCd, String country, String modBy, String messageAuthor, String messageAbstract, String messageBody, String storeMessageStatusCd) ;
        
        
        StringBuilder baseSql = new StringBuilder("Select " +
        		" new com.espendwise.manta.model.view.StoreMessageListView ( "+
        		" messages.storeMessageId, " +
        		" detail.storeMessageDetailId, " +
        		" messages.shortDesc, " +
        		" messages.storeMessageStatusCd, " +
        		" messages.published, " +
        		" detail.messageTitle, " +
        		" detail.messageAbstract, " +
        		" detail.messageBody, " +
        		" messages.messageType, " +
        		" messages.postedDate, " +
        		" messages.endDate, " +
        		" messages.forcedReadCount, " +
        		" messages.forcedReadCount, " +
        		" detail.languageCd, " +
        		" detail.country, " +
        		" detail.messageAuthor, " +
        		" detail.messageDetailTypeCd, " +
        		" messages.addBy, " +
        		" messages.addDate, " +
        		" messages.modBy, " +
        		" messages.modDate " +
        		") " +
        		" from com.espendwise.manta.model.fullentity.StoreMessageFullEntity messages " +
        		" inner join  messages.storeMessageAssocs assoc " +
        		" inner join  messages.storeMessageDetails detail " +
                " where assoc.busEntityId.busEntityId = (:storeId)" +
                " and assoc.storeMessageAssocCd = (:assocCd)" +
                " and messages.storeMessageId = detail.storeMessageId ");
        
        if (Utility.isSet(criteria.getMessageId())) {
        	baseSql.append(" and messages.storeMessageId = ").append(Parse.parseLong(criteria.getMessageId()));
        }
        
        
        if (Utility.isSet(criteria.getName())) {

            if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(messages.shortDesc) like '")
                         .append(QueryHelp.startWith(criteria.getName().toUpperCase()))
                         .append("'");

             } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(messages.shortDesc) like '")
                         .append(QueryHelp.contains(criteria.getName().toUpperCase()))
                         .append("'");


             } else if (Constants.FILTER_TYPE.EXACT_MATCH.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(messages.shortDesc) = '")
                         .append(QueryHelp.contains(criteria.getName().toUpperCase()))
                         .append("'");


             }   
         }
        
        
        

        if (Utility.isSet(criteria.getMessageTitle())) {

           if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getMessageTitleFilterType())) {

                baseSql.append(" and UPPER(detail.messageTitle) like '")
                        .append(QueryHelp.startWith(criteria.getMessageTitle().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getMessageTitleFilterType())) {

                baseSql.append(" and UPPER(detail.messageTitle) like '")
                        .append(QueryHelp.contains(criteria.getMessageTitle().toUpperCase()))
                        .append("'");


            } else if (Constants.FILTER_TYPE.EXACT_MATCH.equals(criteria.getMessageTitleFilterType())) {

            	baseSql.append(" and UPPER(detail.messageTitle) = '")
            			.append(QueryHelp.contains(criteria.getMessageTitle().toUpperCase()))
            			.append("'");


            }  
        }

        List<String> statusList = new ArrayList<String>();
        statusList.add(RefCodeNames.STORE_MESSAGE_STATUS_CD.ACTIVE);
        
        if (Utility.isTrue(criteria.getShowInactive())) {
        	statusList.add(RefCodeNames.STORE_MESSAGE_STATUS_CD.INACTIVE);
        }
        
        if(Utility.isSet(statusList)){
        	baseSql.append("and messages.storeMessageStatusCd in (:statusList)");
        	
        }
        
        if (Utility.isTrue(criteria.getPublished())) {
        	baseSql.append(" and messages.published = '" + criteria.getPublished() + "'");
        }
        
        if (criteria.getPostedDateFrom() != null) {
            baseSql.append("and messages.postedDate >= (:postedDateFrom)");
        }
        
        if (criteria.getPostedDateTo() != null) {
            baseSql.append(" and messages.postedDate <= (:postedDateTo)");
        }

        if (Utility.isSet(criteria.getCountry())) {
            baseSql.append(" and detail.country = '" + criteria.getCountry() + "'");
        }

        if (Utility.isSet(criteria.getLanguageCd())) {
            baseSql.append(" and detail.languageCd = '" + criteria.getLanguageCd() + "'");
        }

        Query q = em.createQuery(baseSql.toString());

        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("assocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_STORE);

        if (criteria.getPostedDateFrom() != null) {
            q.setParameter("postedDateFrom", criteria.getPostedDateFrom());
        }

        if (criteria.getPostedDateTo() != null) {
            q.setParameter("postedDateTo", criteria.getPostedDateTo());
        }
        
        if (Utility.isSet(statusList)) {
            q.setParameter("statusList", statusList);
        }

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        List<StoreMessageListView> r = (List<StoreMessageListView>) q.getResultList();

        logger.info("findMessagesByCriteria()=> END, fetched " + r.size() + " rows");

        return r;
    }


    public List<BusEntityData> findConfiguratedAccounts(StoreMsgAccountConfigCriteria criteria) {

        StringBuilder baseQuery = new StringBuilder("select account " +
                " from StoreMessageData m, StoreMessageAssocData accountAssoc,StoreMessageAssocData storeAssoc,BusEntityAssocData accountStoreAssoc,BusEntityData account " +
                " where m.storeMessageId=:storeMessageId" +
                " and accountAssoc.storeMessageAssocCd = :accountAssocCd" +
                " and storeAssoc.storeMessageAssocCd= :storeAssocCd" +
                " and accountAssoc.storeMessageId = m.storeMessageId" +
                " and account.busEntityTypeCd = :accountCd " +
                " and accountAssoc.busEntityId = account.busEntityId " +
                " and account.busEntityId =  accountStoreAssoc.busEntity1Id" +
                " and accountStoreAssoc.busEntity2Id =  storeAssoc.busEntityId" +
                " and accountStoreAssoc.busEntityAssocCd =  :accountStoreAssocCd" +
                " and storeAssoc.storeMessageId = m.storeMessageId" +
                " and storeAssoc.busEntityId=(:storeId)");

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and account.busEntityStatusCd = '" + RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE + "'");
        }

        if (Utility.isSet(criteria.getName())) {

            if (Constants.FILTER_TYPE.ID.equals(criteria.getFilterType())) {

                baseQuery.append(" and account.busEntityId = ").append(Parse.parseLong(criteria.getName()));

            } else if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getFilterType())) {

                baseQuery.append(" and UPPER(account.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getName().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getFilterType())) {

                baseQuery.append(" and UPPER(account.shortDesc) like '%")
                        .append(QueryHelp.contains(criteria.getName().toUpperCase()))
                        .append("'");


            }
        }

        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("storeMessageId", criteria.getStoreMessageId());
        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("accountAssocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_ACCOUNT);
        q.setParameter("storeAssocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_STORE);
        q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        q.setParameter("accountCd", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit()) ;
        }

        return q.getResultList();

    }

    public void configureAllAccounts(Long storeId, Long storeMessageId, Boolean activeOnly) {

        logger.info("configureAllAccounts()=> BEGIN");

        Query q = em.createQuery("select accounts.busEntityId from BusEntityData accounts, BusEntityAssocData assoc  " +
                "where accounts.busEntityId = assoc.busEntity1Id" +
                " and accounts.busEntityTypeCd = :accountCd" +
                " and assoc.busEntity2Id = :storeId" +
                " and assoc.busEntityAssocCd =  :accountStoreAssocCd" +
                (Utility.isTrue(activeOnly) ? " and accounts.busEntityStatusCd = :statusCd" : Constants.EMPTY));

        q.setParameter("storeId", storeId);
        q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        q.setParameter("accountCd", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);

        if (Utility.isTrue(activeOnly)) {
            q.setParameter("statusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);
        }

        List<Long> accounIds = q.getResultList();

        logger.info("configureAllAccounts()=> found : " + accounIds.size() + " accounts for store : " + storeId);

        List<Long> configuratedAccounts = findConfiguratedAccountIds(storeId, storeMessageId);

        logger.info("configureAllAccounts()=> existing list size : " + configuratedAccounts.size());

        SelectableObjects<Long> selectableObjects = new SelectableObjects<Long>(accounIds, configuratedAccounts, null);

        List<Long> tocreate = selectableObjects.getDeselected();

        logger.info("configureAllAccounts()=> tocreate : " + tocreate.size());

        createAssociation(storeMessageId, tocreate, RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_ACCOUNT);

    }

    private List<StoreMessageAssocData> createAssociation(Long storeMessageId, List<Long> tocreate, String assocType) {

        logger.info("createAssociation()=> BEGIN");

        List<StoreMessageAssocData> assocList = Utility.emptyList(StoreMessageAssocData.class);

        logger.info("createAssociation()=> storeMessageId - " + storeMessageId + ", assocType - " + assocType);

        for (Long id : tocreate) {

            logger.info("createAssociation()=> busEnttityId = " + id + ", storeMessageId - " + storeMessageId + ", assocType - " + assocType);

            StoreMessageAssocData storeMessageAssoc = new StoreMessageAssocData();

            storeMessageAssoc.setStoreMessageId(storeMessageId);
            storeMessageAssoc.setBusEntityId(id);
            storeMessageAssoc.setStoreMessageAssocCd(assocType);

            create(storeMessageAssoc);

            assocList.add(storeMessageAssoc);
        }

        logger.info("createAssociation()=> END.");

        return assocList;
    }

    private List<Long> findConfiguratedAccountIds(Long storeId, Long storeMessageId) {

        logger.info("findConfiguratedAccountIds()=> BEGIN, storeId: " + storeId + ", storeMessageId: " + storeMessageId);

        Query q = em.createQuery("select accountAssoc.busEntityId from StoreMessageData m, StoreMessageAssocData accountAssoc, StoreMessageAssocData storeAssoc" +
                " where accountAssoc.storeMessageAssocCd = :accountAssocCd" +
                " and accountAssoc.storeMessageId = m.storeMessageId" +
                " and storeAssoc.storeMessageAssocCd = :storeAssocCd" +
                " and storeAssoc.storeMessageId = m.storeMessageId" +
                " and storeAssoc.busEntityId = :storeId" +
                " and m.storeMessageId = :storeMessageId");

        q.setParameter("storeId", storeId);
        q.setParameter("storeMessageId", storeMessageId);
        q.setParameter("accountAssocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_ACCOUNT);
        q.setParameter("storeAssocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_STORE);

        List<Long> r = q.getResultList();


        logger.info("configureAllAccounts()=> END, fetched : " + r.size() + " roes");

        return r;
    }

    public void configureAccounts(Long storeId, Long storeMessageId, UpdateRequest<Long> configurationRequest) {

        if(Utility.isSet( configurationRequest.getToDelete())) {

            Query q = em.createQuery("Delete from StoreMessageAssocData assoc where assoc.storeMessageId = (:storeMessageId)" +
                    " and assoc.storeMessageId in (Select storeAssoc.storeMessageId from  StoreMessageAssocData storeAssoc " +
                    "                              where storeAssoc.busEntityId = (:storeId)" +
                    "                              and  storeAssoc.storeMessageId = (:storeMessageId)" +
                    "                              and  storeAssoc.storeMessageAssocCd =  (:storeAssocCd))" +
                    " and assoc.storeMessageAssocCd =  (:accountAssocCd)" +
                    " and assoc.busEntityId in (:accounts)");


            q.setParameter("storeMessageId", storeMessageId);
            q.setParameter("storeId", storeId);
            q.setParameter("accountAssocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_ACCOUNT);
            q.setParameter("storeAssocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_STORE);
            q.setParameter("accounts",  configurationRequest.getToDelete());

            q.executeUpdate();

        }

        if(Utility.isSet( configurationRequest.getToCreate())) {
        	
        	List<Long> configuredAccounts = findConfiguratedAccountIds(storeId, storeMessageId);
        	List toCreate = configurationRequest.getToCreate();
        	
        	Iterator it = toCreate.iterator();
        	while(it.hasNext()){
        		if(configuredAccounts.contains((Long)it.next())){
        			it.remove();
        		}
        	}
        	
            createAssociation(storeMessageId, toCreate, RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_ACCOUNT);
        }
    }

    @Override
    public EntityHeaderView findStoreMessageHeader(Long storeMessageId) {

        Query q = em.createQuery("select new com.espendwise.manta.model.view.EntityHeaderView(m.storeMessageId, m.shortDesc) " +
                "from StoreMessageData m  " +
                "where m.storeMessageId=:storeMessageId");

        q.setParameter("storeMessageId", storeMessageId);

        List headers = q.getResultList();

        return headers.isEmpty() ? null : (EntityHeaderView) headers.get(0);

    }

    @Override
    public void clearViewHistory(Long storeId, Long storeMessageId) {

        logger.info("clearViewHistory=> BEGIN, storeId: " + storeId + ", storeMessageId: " + storeMessageId);

        Query q = em.createQuery("Delete from UserMessageAssocData assoc where assoc.storeMessageId = (:storeMessageId)" +
                " and assoc.storeMessageId in (Select storeAssoc.storeMessageId from  StoreMessageAssocData storeAssoc " +
                "                              where storeAssoc.busEntityId = (:storeId)" +
                "                              and  storeAssoc.storeMessageId = (:storeMessageId)" +
                "                              and  storeAssoc.storeMessageAssocCd =  (:storeAssocCd))"
        );

        q.setParameter("storeMessageId", storeMessageId);
        q.setParameter("storeId", storeId);
        q.setParameter("storeAssocCd", RefCodeNames.STORE_MESSAGE_ASSOC_CD.MESSAGE_STORE);

        int n = q.executeUpdate();

        logger.info("clearViewHistory=> END, deleted " + n + " rows");
    }
    
    
    
	public StoreMessageEntity findStoreMessages(Long storeMessageId) {

		logger.info("findStoreMessages()=> BEGIN" + ",\n busEntityId: "
				+ storeMessageId);

		StoreMessageEntity x = new StoreMessageEntity();
		if (!Utility.isSet(storeMessageId)) {
			return x;
		}
		Query q = em
				.createQuery("Select storeMessageEntity from StoreMessageEntity storeMessageEntity "
						+ "where storeMessageEntity.storeMessageId=:storeMessageId"
				);

		q.setParameter("storeMessageId", storeMessageId);
		StoreMessageEntity r = (StoreMessageEntity) q.getSingleResult();
		return r;
	}

	@Override
    public void deleteTranslation(Long storeMessageId, Long storeMessageDetailId) {

        Query q = em.createQuery("delete  from StoreMessageDetailData detail where detail.storeMessageDetailId = (:storeMessageDetailId)");
        q.setParameter("storeMessageDetailId", storeMessageDetailId);
        q.executeUpdate();

    }

}

