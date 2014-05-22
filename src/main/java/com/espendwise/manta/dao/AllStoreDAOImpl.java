package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.AllStoreData;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.List;

public class AllStoreDAOImpl extends  DAOImpl implements AllStoreDAO {
  
    public AllStoreDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<AllStoreData> findSroresByDomain(String domainName) {
        Query q = em.createQuery("Select stores from AllStoreData stores where stores.domain = (:domain)");
        q.setParameter("domain", domainName);
        return (List<AllStoreData>) q.getResultList();
    }

    @Override
    public AllStoreData findUserDefaultStore(String username) {
      
        Query q = em.createQuery("Select stores from AllStoreData stores, UserStoreData userstore, AllUserData user" +       
                " where user.userName = (:username)" +
                " and  user.defaultStoreId = stores.allStoreId" +
                " and userstore.allStoreId = stores.allStoreId" +
                " and user.allUserId = userstore.allUserId");
        
        q.setParameter("username", username);

        List r = q.getResultList();

        return r.isEmpty() ? null : (AllStoreData) r.get(0);
    }

}
