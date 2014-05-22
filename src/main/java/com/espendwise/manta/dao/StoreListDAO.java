package com.espendwise.manta.dao;

import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.util.criteria.StoreListEntityCriteria;

import java.util.List;

public interface StoreListDAO {

    public List<StoreListEntity> find(Long userId, Integer limit);

    public List<StoreListEntity> find(Long userId, List<Long> stores, Integer limit);

   public List<Long> findIds(Long userId);

   public List<StoreListEntity> find(Long userId, StoreListEntityCriteria criteria);

   public List<StoreListEntity> find(Long... stores);
}
