package com.espendwise.manta.dao;


import com.espendwise.manta.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class EntityQueryDAOImpl extends DAOImpl implements EntityQueryDAO {

    public EntityQueryDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public <T> List<T> findEntities(Class<T> entityClass, String orderByField, Boolean orderAsc) {

        Query q = em.createQuery("select entities from " + entityClass.getName() + " entities"
                + (orderByField != null ? (" order by " + orderByField + (orderAsc!=null && orderAsc ? " asc" : " desc")) : Constants.EMPTY));

        return (List<T>)q.getResultList();
    }
}
