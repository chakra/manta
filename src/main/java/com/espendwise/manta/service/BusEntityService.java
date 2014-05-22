package com.espendwise.manta.service;

import com.espendwise.manta.model.data.*;

import java.util.List;

public interface BusEntityService {
    
    public BusEntityData create(BusEntityData busEntityData);

    public void  delete(List<Long> entityIds);

    public void removeEntities(List<BusEntityData> entities);

    public BusEntityData createOrUpdate(BusEntityData entity);

    public List<BusEntityData> find(List<Long> entityIds);

    public List<BusEntityData> find(List<Long> entityIds, String busEntityTypeCd);
   
    public BusEntityData getDistributorByErpNum(String distributorErpNum);
    
    public List<BusEntityAssocData> findAssocs(Long busEntity1Id, Long busEntity2Id, String busEntityAssocCd);
    
}
