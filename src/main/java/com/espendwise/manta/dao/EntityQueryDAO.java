package com.espendwise.manta.dao;


import java.util.List;

public interface EntityQueryDAO {

    public <T> List<T> findEntities(Class<T> entityClass, String orderByField, Boolean orderAsc);

}
