package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.CatalogAssocData;
import com.espendwise.manta.util.Utility;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class CatalogAssocDAOImpl extends DAOImpl implements CatalogAssocDAO {
    
    public CatalogAssocDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<CatalogAssocData> readCatalogAssoc(Long siteId, String assocType) {
        List<CatalogAssocData> assocs = new ArrayList<CatalogAssocData>();
        
        if (Utility.isSet(siteId) && Utility.isSet(assocType)) {
            
            Query q = em.createQuery("Select object(assoc) from CatalogAssocData assoc" +
                    " where assoc.busEntityId = (:siteId)" +
                    " and assoc.catalogAssocCd = (:catalogAssocType)"
                    );

            q.setParameter("siteId", siteId);
            q.setParameter("catalogAssocType", assocType);

            assocs = (List<CatalogAssocData>) q.getResultList();
        }
        
        return assocs;

    }

}