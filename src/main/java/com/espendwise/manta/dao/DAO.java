package com.espendwise.manta.dao;

import javax.persistence.EntityManager;

public interface DAO/*<T, PK extends Serializable> extends CRUD<T, PK>*/ {
	
    public void setEntityManager(EntityManager entityManager);

}
