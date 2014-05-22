package com.espendwise.manta.service;


import java.util.List;

public interface EntityQueryService {

    public <T> List<T> findEntities(String unit, Class<T> entityClass, String orderByField, Boolean orderAsc);

}
