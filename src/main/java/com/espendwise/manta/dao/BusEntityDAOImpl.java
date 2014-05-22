package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class BusEntityDAOImpl extends DAOImpl implements BusEntityDAO{

    public BusEntityDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public BusEntityData create(BusEntityData busEntityData) {
        return busEntityData != null && Utility.longNN(busEntityData.getBusEntityId()) == 0
                ? super.create(busEntityData)
                : busEntityData;
    }

    @Override
    public void delete(List<Long> entityIds) {
        if (Utility.isSet(entityIds)) {
            for (List<Long> ids : Utility.createPackages(entityIds, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {
                Query q = em.createQuery("Delete from BusEntityData where busEntityId in (:busEntityId )");
                q.setParameter(BusEntityData.BUS_ENTITY_ID, ids);
                q.executeUpdate();
            }

        }
    }

    @Override
    public void removeEntities(List<BusEntityData> entities) {
        for(BusEntityData e:Utility.listNN(entities)) {
            remove(e);
        }
    }

    @Override
    public BusEntityData createOrUpdate(BusEntityData entity) {
        if (Utility.isSet(entity)) {
            entity = entity.getBusEntityId() == null ? create(entity) : update(entity);
        }
        return entity;
    }

    @Override
    public List<BusEntityData> find(List<Long> entityIds) {
         return find(entityIds, null);
    }

    @Override
    public List<BusEntityData> find(List<Long> entityIds, String busEntityTypeCd) {

        List<BusEntityData> x = new ArrayList<BusEntityData>();

        if (Utility.isSet(entityIds)) {

            for (List<Long> ids : Utility.createPackages(entityIds, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {

                Query q = em.createQuery("Select entity from BusEntityData entity where busEntityId in (:busEntityId )"
                        + (Utility.isSet(busEntityTypeCd) ? " and entity.busEntityTypeCd = (:busEntityTypeCd)" : "")
                );

                q.setParameter(BusEntityData.BUS_ENTITY_ID, ids);
                if (Utility.isSet(busEntityTypeCd)) {
                    q.setParameter(BusEntityData.BUS_ENTITY_TYPE_CD, busEntityTypeCd);
                }

                x.addAll(result(q, BusEntityData.class));
            }

        } else if (Utility.isSet(busEntityTypeCd)) {
                Query q = em.createQuery("Select entity from BusEntityData entity where " +
                         " entity.busEntityTypeCd = (:busEntityTypeCd)"
                );

                q.setParameter(BusEntityData.BUS_ENTITY_TYPE_CD, busEntityTypeCd);

                x.addAll(result(q, BusEntityData.class));

        }
        return x;
    }

    @Override
    public BusEntityData getDistributorByErpNum(String distributorErpNum) {
        BusEntityData distributor = null;
        
        if (Utility.isSet(distributorErpNum)) {
            StringBuilder baseQuery = new StringBuilder("Select object(busEntity) from BusEntityData busEntity");
            baseQuery.append(" WHERE busEntity.erpNum =(:distributorErpNum)");
            baseQuery.append(" AND busEntity.busEntityTypeCd =(:busEntityType)");
                    
            Query q = em.createQuery(baseQuery.toString());
            q.setParameter("distributorErpNum", distributorErpNum);
            q.setParameter("busEntityType", RefCodeNames.BUS_ENTITY_TYPE_CD.DISTRIBUTOR);

            List<BusEntityData> distributors = (List<BusEntityData>) q.getResultList();
            if (Utility.isSet(distributors)) {
                distributor = distributors.get(0);
            }
        }
        return distributor;
    }

}
