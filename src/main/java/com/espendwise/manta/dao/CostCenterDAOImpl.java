package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.CostCenterData;
import com.espendwise.manta.model.data.CostCenterAssocData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.CostCenterListView;
import com.espendwise.manta.model.view.CostCenterView;
import com.espendwise.manta.model.view.CostCenterHeaderView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.binder.PropertyBinder;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.criteria.CostCenterListViewCriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class CostCenterDAOImpl extends DAOImpl implements CostCenterDAO {
    private static final Logger logger = Logger.getLogger(CostCenterDAOImpl.class);

    public CostCenterDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<CostCenterData> findCatalogCostCenters(Long catalogId, String assocTypeCode) {

        if (!Utility.isSet(catalogId) || !Utility.isSet(assocTypeCode)) {
             return  Utility.emptyList(CostCenterData.class);
        }

        Query q = em.createQuery("Select costCenter from CostCenterData costCenter, CostCenterAssocData  assoc" +
                " where costCenter.costCenterId = assoc.costCenterId " +
                " and assoc.catalogId =  (:catalogId) " +
                " and assoc.costCenterAssocCd = (:assocType)"
        );


        q.setParameter("catalogId", catalogId);
        q.setParameter("assocType", assocTypeCode);

        return q.getResultList();

    }

     @Override
    public List<CostCenterListView> findCostCentersByCriteria(CostCenterListViewCriteria criteria) {
        logger.info("findCostCentersByCriteria START");
        String filterCatalogAccount = "";
        List<Long> filterCatalogAccountIds = null;

        String filterAccCatType = criteria.getFilterAccCatType();
        if (RefCodeNames.COST_CENTER_FILTER_ACC_CAT_TYPE.CATALOG.equals(filterAccCatType)) {
            filterCatalogAccountIds = criteria.getCatalogIds();
            if ( filterCatalogAccountIds != null && filterCatalogAccountIds.size() > 0) {
                filterCatalogAccount = "and catalog.catalogId in (:filterIds) and ";
            }
        } else if (RefCodeNames.COST_CENTER_FILTER_ACC_CAT_TYPE.ACCOUNT.equals(filterAccCatType)) {
           filterCatalogAccountIds = criteria.getAccountIds();
           if (filterCatalogAccountIds != null && filterCatalogAccountIds.size() > 0) {
                filterCatalogAccount =  "and accountStore.busEntity1Id.busEntityId in (:filterIds) and ";
           }
        }
       
        StringBuilder baseQuery = new StringBuilder("Select distinct " +
                "new com.espendwise.manta.model.view.CostCenterListView(" +
                "   costCenter.costCenterId," +
                "   costCenter.shortDesc, " +
                "   costCenter.costCenterTypeCd, " +
                "   costCenter.costCenterStatusCd, " +
                "   costCenter.costCenterTaxType, " +
                "   costCenter.allocateFreight, " +
                "   costCenter.allocateDiscount" +
                ") " +
                "from CostCenterFullEntity costCenter ");
        if (Utility.isSet(filterCatalogAccount)) {
           baseQuery.append(
                "inner join costCenter.costCenterAssocs costCenterAssoc  " +
                "inner join costCenterAssoc.catalogId catalog " +
                "inner join catalog.catalogAssocs catalogAssoc  "  +
                "inner join catalogAssoc.busEntityId account " +
                "inner join account.busEntityAssocsForBusEntity1Id accountStore ");
        }

        baseQuery.append(
                "where " +
                " costCenter.storeId.busEntityId = :storeId ");

        if (Utility.isSet(filterCatalogAccount)) {
           baseQuery.append(
                filterCatalogAccount +
                "   costCenterAssoc.costCenterAssocCd = (:costCenterAssocTypeCd) and " +
                "   catalogAssoc.catalogAssocCd = (:catalogAssocCd) and " +
                "   accountStore.busEntity2Id.busEntityId = :storeId and " +
                "   accountStore.busEntityAssocCd = (:busEntityAssocCd) "
                );
        }

        if (Utility.isSet(criteria.getCostCenterName())) {

            if (Constants.FILTER_TYPE.ID.equals(criteria.getFilterType())) {

                baseQuery.append(" and costCenter.costCenterId = ").append(Parse.parseLong(criteria.getCostCenterName()));

            } else if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getFilterType())) {

                baseQuery.append(" and UPPER(costCenter.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getCostCenterName().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getFilterType())) {

                baseQuery.append(" and UPPER(costCenter.shortDesc) like '")
                        .append(QueryHelp.contains(criteria.getCostCenterName().toUpperCase()))
                        .append("'");
            } else if (Constants.FILTER_TYPE.EXACT_MATCH.equals(criteria.getFilterType())) {
                baseQuery.append(" and costCenter.shortDesc = ")
                        .append(QueryHelp.toQuoted(criteria.getCostCenterName()));


            }
        }

        if (!Utility.isSet(criteria.getCostCenterName()) || !Constants.FILTER_TYPE.ID.equals(criteria.getFilterType())) {
            if (criteria.getCostCenterId() != null && criteria.getCostCenterId() > 0) {
                baseQuery.append(" and costCenter.costCenterId = ").append(criteria.getCostCenterId());
            }
        }

        if (Utility.isTrue(criteria.isActiveOnly())) {
            baseQuery.append(" and costCenter.costCenterStatusCd <> ").
                    append(QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE));
        }
        Query q = em.createQuery(baseQuery.toString());
        q.setParameter("storeId", criteria.getStoreId());

        if (Utility.isSet(filterCatalogAccount)) {
            q.setParameter("costCenterAssocTypeCd", RefCodeNames.COST_CENTER_ASSOC_CD.COST_CENTER_ACCOUNT_CATALOG);
            q.setParameter("catalogAssocCd", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT);
            q.setParameter("busEntityAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
            q.setParameter("filterIds", filterCatalogAccountIds);
        }
        return q.getResultList();

     }


    @Override
     public CostCenterView findCostCenterToEdit(Long storeId, Long costCenterId) {

         Query q = em.createQuery("Select object(costCenter) from CostCenterData costCenter " +
                " where costCenter.costCenterId = (:costCenterId)" );

        q.setParameter("costCenterId", costCenterId);

        List<CostCenterData> costCenters = (List<CostCenterData>) q.getResultList();

        CostCenterView costCenterView = new CostCenterView();

        if (Utility.isSet(costCenters)) {
            costCenterView.setCostCenterData(costCenters.get(0));
            return costCenterView;
        } else {
            return null;
        }
    }

    @Override
    public CostCenterHeaderView findCostCenterHeader(Long costCenterId) {
        Query q = em.createQuery(
            "Select new com.espendwise.manta.model.view.CostCenterHeaderView(" +
                    "costCenter.costCenterId, costCenter.costCenterName)" +
                " from CostCenterData costCenter where costCenter.costCenterId = (:costCenterId) "
        );

        q.setParameter("costCenterId", costCenterId);

        List x = q.getResultList();

        return !x.isEmpty() ? (CostCenterHeaderView) x.get(0) : null;

    }

    @Override
    public CostCenterView saveCostCenter(Long storeId, CostCenterView costCenter) {
        if (costCenter == null) {
            return null;
        }
        CostCenterData ccD = costCenter.getCostCenterData();
        ccD.setStoreId(storeId);

        if (ccD.getCostCenterId() == null ) {//|| ccD.getCostCenterId() == 0) {
            ccD = super.create(ccD);
        } else {
            ccD = super.update(ccD);
        }
        costCenter.setCostCenterData(ccD);
        return costCenter;
    }


    @Override
    public void configureCostCenterCatalogs(Long costCenterId, Long storeId, List<CatalogListView> selected,
                                            List<CatalogListView> fullList) {
        if (costCenterId != null && storeId != null) {
            CostCenterAssocDAO  costCenterAssocDao = new CostCenterAssocDAOImpl(em);

           List<CostCenterAssocData> oldAssocList =
                costCenterAssocDao.readCostCenterAssocs(costCenterId,
                            RefCodeNames.COST_CENTER_ASSOC_CD.COST_CENTER_ACCOUNT_CATALOG);

           Map<Long, CostCenterAssocData> oldMap = new HashMap<Long, CostCenterAssocData>();

           if (Utility.isSet(oldAssocList)) {
                for (CostCenterAssocData el : oldAssocList) {
                    oldMap.put(el.getCatalogId(), el);
                }
           }

           Map<Long, CatalogListView> filterCatalogMap = new HashMap<Long, CatalogListView>();

           if (Utility.isSet(fullList)) {
               for (CatalogListView catalog : fullList) {
                   filterCatalogMap.put(catalog.getCatalogId(), catalog);
               }
           }

            CostCenterAssocData assoc;
            if (Utility.isSet(selected)) {
                for (CatalogListView el : selected) {
                    if (oldMap.containsKey(el.getCatalogId())) {
                        oldMap.remove(el.getCatalogId());
                    } else { // create new assoc
                        assoc = new CostCenterAssocData();
                        assoc.setCatalogId(el.getCatalogId());
                        assoc.setCostCenterId(costCenterId);
                        assoc.setCostCenterAssocCd(RefCodeNames.COST_CENTER_ASSOC_CD.COST_CENTER_ACCOUNT_CATALOG);
                        super.create(assoc);
                    }
                }

            }

            if (oldMap.size() > 0) {
                for (CostCenterAssocData assocData : oldMap.values()) { // remove unused old assocs
                    if (filterCatalogMap.containsKey(assocData.getCatalogId())) {
                        assoc = assocData;
                        em.remove(assoc);
                    }
                }
            }

        }
    }


}
