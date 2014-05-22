package com.espendwise.manta.dao;


import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.StoreListEntityCriteria;
import com.espendwise.manta.util.parser.Parse;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class StoreListDAOImpl extends DAOImpl implements StoreListDAO {
   
    private static final Logger logger = Logger.getLogger(StoreListDAOImpl.class);

    public StoreListDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<StoreListEntity> find(Long userId, Integer limit) {
        return find(userId, null, limit);
    }

    @Override
    public List<StoreListEntity> find(Long userId, List<Long> storeIds, Integer limit) {

        logger.info("find(()=> BEGIN,  userId: " + userId + ", limit: " + limit);

        Query q = em.createQuery("select stores from UserStoreListEntity userStores " +
                " inner join userStores.stores stores left join fetch stores.addresses addresses" +
                " where stores.busEntityTypeCd = (:storeCd)" +
                " and userStores.userAssocCd = (:userAssocd)" +
                " and userStores.userId = (:userId)" +
                (storeIds != null ? " and stores.storeId in (:storeIds)" : "")
        );

        q.setParameter("storeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
        q.setParameter("userAssocd", RefCodeNames.USER_ASSOC_CD.STORE);
        q.setParameter("userId", userId);
        if (storeIds != null) {
            q.setParameter("storeIds", storeIds);
        }

        if (limit != null) {
            q.setMaxResults(limit);
        }

        List<StoreListEntity> r = (List<StoreListEntity>) q.getResultList();

        logger.info("find(()=> END, fetched " + r.size() + " rows");

        return r;
    }

    @Override
    public List<Long> findIds(Long userId) {

        Query q = em.createQuery("select stores.id from UserStoreListEntity userStores inner join userStores.stores stores " +
                " where stores.busEntityTypeCd = (:storeCd)" +
                " and userStores.userAssocCd = (:userAssocd)" +
                " and userStores.userId = (:userId)");

        q.setParameter("storeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
        q.setParameter("userAssocd", RefCodeNames.USER_ASSOC_CD.STORE);
        q.setParameter("userId", userId);

        return (List<Long>) q.getResultList();
    }

    @Override
    public List<StoreListEntity> find(Long userId, StoreListEntityCriteria criteria) {

        logger.info("find(()=> BEGIN, criteria " + criteria);

        StringBuilder baseSql = new StringBuilder("select stores from UserStoreListEntity userStores inner join userStores.stores stores " +
                " where stores.busEntityTypeCd = (:busentityTypeCd)" +
                " and userStores.userAssocCd = (:userAssocd)" +
                " and userStores.userId = (:userId)");


        if (Utility.isSet(criteria.getStoreName())) {

            String filterType = Utility.isSet(criteria.getStoreNameFilterType())
                    ? criteria.getStoreNameFilterType()
                    : Constants.FILTER_TYPE.DEFAULT;

            if (Constants.FILTER_TYPE.ID.equals(filterType)) {

                baseSql.append(" and userStores.stores.storeId = ").append(Parse.parseLong(criteria.getStoreName()));

            } else if (Constants.FILTER_TYPE.START_WITH.equals(filterType)) {

                baseSql.append(" and UPPER(userStores.stores.storeName) like '")
                        .append(QueryHelp.startWith(criteria.getStoreName().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(filterType)) {

                baseSql.append(" and UPPER(userStores.stores.storeName) like '%")
                        .append(QueryHelp.contains(criteria.getStoreName().toUpperCase()))
                        .append("'");


            }
        }

        if (!Utility.isSet(criteria.getShowInactive())) {
            baseSql.append(" and userStores.stores.status = '")
                    .append(RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE)
                    .append("'");
        }

        Query q = em.createQuery(baseSql.toString());

        q.setParameter("busentityTypeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
        q.setParameter("userAssocd", RefCodeNames.USER_ASSOC_CD.STORE);
        q.setParameter("userId", userId);

        if (criteria.getLimit() != null) { q.setMaxResults(criteria.getLimit()); }

        logger.info("find(()=> busentityTypeCd - " + RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
        logger.info("find(()=> userAssocd - " + RefCodeNames.USER_ASSOC_CD.STORE);
        logger.info("find(()=> userId - " + userId);
        logger.info("find(()=> limit - " + criteria.getLimit());

        List<StoreListEntity> r = (List<StoreListEntity>) q.getResultList();

        logger.info("find(()=> END, fetched " + r.size() + " rows");

        return r;
    }

    @Override
    public List<StoreListEntity> find(Long... storeIds) {

        logger.info("find(()=> BEGIN, filter: " + (storeIds == null ? "is not set" : Arrays.toString(storeIds)));

        Query q = em.createQuery("select stores from StoreListEntity stores " +
                " left join fetch stores.addresses addresses" +
                " where stores.busEntityTypeCd = (:storeCd)" +
                (storeIds != null ? " and stores.storeId in (:storeIds)" : "")
        );

        q.setParameter("storeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
        if (storeIds != null) {
            q.setParameter("storeIds", Arrays.asList(storeIds));
        }

        List<StoreListEntity> r = (List<StoreListEntity>) q.getResultList();

        logger.info("find(()=> END, fetched " + r.size() + " rows");

        return r;
    }

}
