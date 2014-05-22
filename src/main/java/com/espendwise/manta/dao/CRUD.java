package com.espendwise.manta.dao;


import javax.persistence.EntityManager;
import java.io.Serializable;

public interface CRUD<T, PK extends Serializable> {

    public void create(EntityManager manager, T pNewInstance);

    public T read(EntityManager manager, Class pType, PK pId);

    public void update(EntityManager manager, T pValueObject);

    public void delete(EntityManager manager, T pValueObject);
}
