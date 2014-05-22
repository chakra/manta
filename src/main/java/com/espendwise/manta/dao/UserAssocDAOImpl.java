package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.UserAssocData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.UserAssocCriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class UserAssocDAOImpl extends DAOImpl implements UserAssocDAO {
    
    public UserAssocDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public UserAssocData readAssoc(Long userId, Long entityId, String assocType) {

        Query q = em.createQuery("select assoc from UserAssocData assoc where assoc.busEntityId = (:entityId)" +
                " and assoc.userId = (:userId)" +
                " and assoc.userAssocCd  = (:assocType)");

        q.setParameter("userId", userId);
        q.setParameter("entityId", entityId);
        q.setParameter("assocType", assocType);

        return (UserAssocData) q.getSingleResult();

    }

    @Override
    public UserAssocData findAssoc(Long userId, Long entityId, String assocType) {

        Query q = em.createQuery("select assoc from UserAssocData assoc where assoc.busEntityId = (:entityId)" +
                " and assoc.userId = (:userId)" +
                " and assoc.userAssocCd  = (:assocType)");

        q.setParameter("userId", userId);
        q.setParameter("entityId", entityId);
        q.setParameter("assocType", assocType);

        List<UserAssocData> x = (List<UserAssocData>) q.getResultList();
        if(!x.isEmpty()) {
            return x.get(0);
        }

        return null;
    }

    @Override
    public List<UserAssocData> findEntityAssoc(Long entityId, String assocType) {

        Query q = em.createQuery("select assoc from UserAssocData assoc where assoc.busEntityId= (:entityId)" +
                " and assoc.userAssocCd  = (:assocType)");

        q.setParameter("entityId", entityId);
        q.setParameter("assocType", assocType);

        return (List<UserAssocData>) q.getResultList();

    }

    @Override
    public UserAssocData createAssoc(Long userId, Long entityId, String assocCd) {

        UserAssocData assocData = new UserAssocData();

        assocData.setBusEntityId(entityId);
        assocData.setUserAssocCd(assocCd);
        assocData.setUserId(userId);

        return create(assocData);
    }

    @Override
    public void removeEntityAssoc(Long entityId, String assocCd) {
        List<UserAssocData> assocs = findEntityAssoc(entityId, assocCd);
        if (Utility.isSet(assocs)) {
            for (UserAssocData assoc : assocs) {
                em.remove(assoc);
            }
        }
    }

    @Override
    public List<UserData> findAssocUsers(Long storeId, UserAssocCriteria criteria, String assocType) {

        Query q = em.createQuery("select user from UserData user, UserAssocData storeAssoc " +
                (Utility.isSet(criteria.getEntityIds()) ? ", UserAssocData entityAssoc" : "") +
                " where storeAssoc.busEntityId = (:storeId) and storeAssoc.userAssocCd  = (:storeAssocType) and user.userId = storeAssoc.userId " +
                (Utility.isSet(criteria.getEntityIds()) ? " and entityAssoc.busEntityId in (:entityIds)" : "") +
                (Utility.isSet(criteria.getEntityIds()) ? " and entityAssoc.userAssocCd = (:assocType)" : "") +
                (Utility.isSet(criteria.getEntityIds()) ? " and user.userId = entityAssoc.userId" : "") +
                (Utility.isSet(criteria.getUserTypes()) ? " and user.userTypeCd in (:userTypes)" : "") + 
                (criteria.isActiveUserOnly() ? " and user.userStatusCd = (:userStatusCd)" : "")
        );

        q.setParameter("storeId", storeId);
        q.setParameter("storeAssocType", RefCodeNames.USER_ASSOC_CD.STORE);

        if (Utility.isSet(criteria.getEntityIds())) {
            q.setParameter("entityIds", criteria.getEntityIds());
            q.setParameter("assocType", assocType);
        }

        if (Utility.isSet(criteria.getUserTypes())) {
            q.setParameter("userTypes", criteria.getUserTypes());
        }

        if ((criteria.isActiveUserOnly())){
        	q.setParameter("userStatusCd", RefCodeNames.USER_STATUS_CD.ACTIVE);
        }
        		
        return (List<UserData>) q.getResultList();

    }

    
    @Override
    public List<UserAssocData> readUserAccountAssoc(Long userId, Long storeId) {
        List<UserAssocData> assocs = new ArrayList<UserAssocData>();
        
        if (userId != null && storeId != null) {
            
            Query q = em.createQuery("Select object(assoc) from UserAssocData assoc, BusEntityAssocData accountToStore" +
                    " where assoc.userId = (:userId)" +
                    " and assoc.userAssocCd = (:userAssocCd)" +
                    " and assoc.busEntityId = accountToStore.busEntity1Id" +
                    " and accountToStore.busEntity2Id = (:storeId)" +
                    " and accountToStore.busEntityAssocCd = (:accountToStoreAssocCd)"
                    );

            q.setParameter("userId", userId);
            q.setParameter("storeId", storeId);
            q.setParameter("userAssocCd", RefCodeNames.USER_ASSOC_CD.ACCOUNT);
            q.setParameter("accountToStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);

            assocs = (List<UserAssocData>) q.getResultList();
        }
        
        return assocs;

    }
    
    @Override
    public List<UserAssocData> readUserSiteAssoc(Long userId, Long storeId) {
        List<UserAssocData> assocs = new ArrayList<UserAssocData>();
        
        if (userId != null && storeId != null) {
            
            Query q = em.createQuery("Select object(assoc) from UserAssocData assoc, BusEntityAssocData siteToAccount, BusEntityAssocData accountToStore" +
                    " where assoc.userId = (:userId)" +
                    " and assoc.userAssocCd = (:userAssocCd)" +
                    " and assoc.busEntityId = siteToAccount.busEntity1Id" +
                    " and siteToAccount.busEntityAssocCd = (:siteToAccountAssocCd)" +
                    " and siteToAccount.busEntity2Id = accountToStore.busEntity1Id" +
                    " and accountToStore.busEntity2Id = (:storeId)" +
                    " and accountToStore.busEntityAssocCd = (:accountToStoreAssocCd)"
                    );

            q.setParameter("userId", userId);
            q.setParameter("storeId", storeId);
            q.setParameter("userAssocCd", RefCodeNames.USER_ASSOC_CD.SITE);
            q.setParameter("siteToAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            q.setParameter("accountToStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);

            assocs = (List<UserAssocData>) q.getResultList();
        }
        
        return assocs;

    }
    
    @Override
    public List<UserAssocData> readSiteUserAssoc(Long siteId, Long storeId) {
        List<UserAssocData> assocs = new ArrayList<UserAssocData>();
        
        if (siteId != null && storeId != null) {
            
            Query q = em.createQuery("Select object(assoc) from UserAssocData assoc, UserAssocData userToStore" +
                    " where assoc.busEntityId = (:siteId)" +
                    " and assoc.userAssocCd = (:userToSiteAssocCd)" +
                    " and assoc.userId = userToStore.userId" +
                    " and userToStore.userAssocCd = (:userToStoreAssocCd)" +
                    " and userToStore.busEntityId = (:storeId)"
                    );

            q.setParameter("siteId", siteId);
            q.setParameter("storeId", storeId);
            q.setParameter("userToSiteAssocCd", RefCodeNames.USER_ASSOC_CD.SITE);
            q.setParameter("userToStoreAssocCd", RefCodeNames.USER_ASSOC_CD.STORE);

            assocs = (List<UserAssocData>) q.getResultList();
        }
        
        return assocs;

    }
    
    @Override
    public List<UserAssocData> readUserSiteAssocsForAccount(Long userId, Long accountId) {
        List<UserAssocData> assocs = new ArrayList<UserAssocData>();
        
        if (userId != null && accountId != null) {
            
            Query q = em.createQuery("Select object(siteAssoc) from UserAssocData siteAssoc, BusEntityAssocData siteToAccount" +
                    " where siteAssoc.userId = (:userId)" +
                    " and siteAssoc.userAssocCd = (:userToSiteAssocCd)" +
                    " and siteAssoc.busEntityId = siteToAccount.busEntity1Id" +
                    " and siteToAccount.busEntity2Id = (:accountId)" +
                    " and siteToAccount.busEntityAssocCd = (:siteToAccountAssocCd)"
                    );

            q.setParameter("userId", userId);
            q.setParameter("accountId", accountId);
            q.setParameter("userToSiteAssocCd", RefCodeNames.USER_ASSOC_CD.SITE);
            q.setParameter("siteToAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);

            assocs = (List<UserAssocData>) q.getResultList();
        }
        
        return assocs;
    }
    
    public List<UserAssocData> updateUserAssocs(Long userId, List<UserAssocData> newAssocs) {
        List<UserAssocData> resultAssoc = new ArrayList<UserAssocData>();
        
        UserDAO userDAO = new UserDAOImpl(em);
        
        List<UserAssocData> oldAssoc = userDAO.findUserAssocs(userId);

        UserAssocData element;
        if (Utility.isSet(newAssocs)) {
            for (UserAssocData assoc : newAssocs) {
                assoc.setUserId(userId);
                if (assoc.getUserAssocId() == null) {
                    assoc = super.create(assoc);
                } else {
                    assoc = super.update(assoc);
                    if (Utility.isSet(oldAssoc)) {
                        int i;
                        for (i = 0; i < oldAssoc.size(); i++) {
                            element = oldAssoc.get(i);
                            if (assoc.getUserAssocId().equals(element.getUserAssocId()) &&
                                assoc.getBusEntityId().equals(element.getBusEntityId()) &&
                                assoc.getUserAssocCd().equals(element.getUserAssocCd())) {
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
            for (UserAssocData assoc : oldAssoc) {
                em.remove(assoc);
            }
        }

        return resultAssoc;
   }   
                
}