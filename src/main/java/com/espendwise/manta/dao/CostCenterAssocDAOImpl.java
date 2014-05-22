package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.CostCenterAssocData;
import com.espendwise.manta.util.Utility;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class CostCenterAssocDAOImpl extends DAOImpl implements CostCenterAssocDAO {
    
    public CostCenterAssocDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<CostCenterAssocData> readCostCenterAssocs(Long costCenterId, String assocType) {
        List<CostCenterAssocData> assocs = new ArrayList<CostCenterAssocData>();
        
        if (Utility.isSet(costCenterId) && Utility.isSet(assocType)) {
            
            Query q = em.createQuery("Select object(assoc) from CostCenterAssocData assoc" +
                    " where assoc.costCenterId = (:costCenterId)" +
                    " and assoc.costCenterAssocCd = (:catalogAssocType)"
                    );

            q.setParameter("costCenterId", costCenterId);
            q.setParameter("catalogAssocType", assocType);

            assocs = (List<CostCenterAssocData>) q.getResultList();

        }

        return assocs;


    }

}