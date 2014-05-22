package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.AllStoreData;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class AllUserStoreDAOImpl extends DAOImpl implements AllUserStoreDAO {

    public AllUserStoreDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<AllStoreData> getAllUserStores(Long allUserId) {

        Query q = em.createQuery("Select stores from AllStoreData stores, UserStoreData userStore" +
                " where userStore.allStoreId = stores.allStoreId" +
                " and userStore.allUserId = (:allUserId)");

        q.setParameter("allUserId", allUserId);

        return (List<AllStoreData>) q.getResultList();
    }
}
