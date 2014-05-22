package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityData;

import java.util.List;

public interface BusEntityDAO {

   public BusEntityData create(BusEntityData busEntityData);

   public void  delete(List<Long> entityIds);

   public void removeEntities(List<BusEntityData> entities);

   public BusEntityData createOrUpdate(BusEntityData entity);

   public List<BusEntityData> find(List<Long> entityIds);

   public List<BusEntityData> find(List<Long> entityIds, String busEntityTypeCd);
   
   public BusEntityData getDistributorByErpNum(String distributorErpNum);

}
