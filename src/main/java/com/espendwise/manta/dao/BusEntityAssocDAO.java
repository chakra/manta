package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityAssocData;

import java.util.List;

public interface  BusEntityAssocDAO {

    public BusEntityAssocData createAssoc(Long busEntity1Id, Long busEntity2Id, String assocCode);

    public void removeAssoc(Long busEntity1Id, Long busEntity2Id, String assocCode);

    public List<BusEntityAssocData> findAssocs(Long busEntity1Id, Long busEntity2Id, String busEntityAssocCd);

    public void deleteAssoc(List<Long> busEntity1Id, Long busEntity2Id);

    public void deleteAssoc(List<Long> deselected, Long parentId, String assocCd);

    public BusEntityAssocData saveAssoc(Long busEntity1Id, Long busEntity2Id, String busEntityAssocCd);

    public void removeAssoc(List<BusEntityAssocData> entityAssocs);

    public List<BusEntityAssocData> findAssocs(List<Long>busEntity1Ids, Long busEntity2Id, String assocCd);

    public List<BusEntityAssocData> findAssocs(Long busEntity1Id, List<Long>busEntity2Ids, String assocCd);

}
