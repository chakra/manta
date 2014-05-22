package com.espendwise.manta.dao;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.auth.AuthUser;
import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public abstract  class DAOImpl/*<T>*/ implements DAO/*<T, Long>*/  {

    private static final Logger logger = Logger.getLogger(DAOImpl.class);

    protected EntityManager em;

    public DAOImpl(EntityManager entityManager) {
    	super();
    	setEntityManager(entityManager);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }
    
    public <T extends TableObject> T manyTransactionCreate(T obj) {

        if (obj != null) {

            AuthUser authUser = Auth.getAuthUser();

            Date date = new Date();

            obj.setModBy(authUser.getModByName());
            obj.setModDate(date);
            obj.setAddBy(authUser.getModByName());
            obj.setAddDate(date);
            
            obj = em.merge(obj);


        }


        return obj;

    }    

    public <T extends TableObject> T create(T obj) {

        if (obj != null) {

            AuthUser authUser = Auth.getAuthUser();

            Date date = new Date();

            obj.setModBy(authUser.getModByName());
            obj.setModDate(date);
            obj.setAddBy(authUser.getModByName());
            obj.setAddDate(date);
            
            em.persist(obj);


        }


        return obj;

    }

    public <T extends TableObject> T update(T obj) {


        if (obj != null) {

            AuthUser authUser = Auth.getAuthUser();

            Date date = new Date();

            obj.setModBy(authUser.getModByName());
            obj.setModDate(date);

            obj = em.merge(obj);

        }

        return obj;

    }

    public <T extends TableObject> void remove(List<T> entities) {
        for (T e : Utility.listNN(entities)) {
            remove(e);
        }
    }

    protected <T extends TableObject> void remove(T obj) {
        em.remove(obj);
    }

    protected <T> T first(Query q, Class<T> resultType) {
        List<T> x = result(q, resultType);
        return (x.isEmpty()?null:  x.get(0));
    }

    protected <T> List<T> result(Query q, Class<T> resultType) {
        return  (List<T>) q.getResultList();
    }


}
