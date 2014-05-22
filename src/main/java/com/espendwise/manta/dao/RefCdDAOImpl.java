package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.RefCdData;
import com.espendwise.manta.model.data.WoRefCdData;
import com.espendwise.manta.util.Constants;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class RefCdDAOImpl extends DAOImpl implements RefCdDAO {

    private static final Logger logger = Logger.getLogger(GroupDAOImpl.class);

    public RefCdDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public List<RefCdData> getRefCodes(String refCd, int pOrderFlag) {

            Query q = em.createQuery("Select refcd from RefCdData refCd " +
                    "where refCd.refCd = (:refCd) " +
                    "order by (:orderBy)");

            q.setParameter("refCd", refCd);

            switch (pOrderFlag) {
                case Constants.ORDER_TYPE.ORDER_BY_ID:
                    q.setParameter("orderBy", "refCdId");
                    break;
                case Constants.ORDER_TYPE.ORDER_BY_NAME:
                    q.setParameter("orderBy", "shortDesc");
                    break;
            }

            return (List<RefCdData>) q.getResultList();


    }

    public List<WoRefCdData> getWoRefCodes(String refCd, int pOrderFlag) {

          Query q = em.createQuery("Select refcd from WoRefCdData refCd" +
                    " where refCd.refCd = (:refCd)" +
                    " order by (:orderBy)");

            q.setParameter("refCd", refCd);

            switch (pOrderFlag) {
                case Constants.ORDER_TYPE.ORDER_BY_ID:
                    q.setParameter("orderBy", "refCdId");
                    break;
                case Constants.ORDER_TYPE.ORDER_BY_NAME:
                    q.setParameter("orderBy", "shortDesc");
                    break;
            }

            return (List<WoRefCdData>) q.getResultList();


    }
}
