package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.AssetData;
import com.espendwise.manta.model.view.AssetListView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.AssetListViewCriteria;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class AssetDAOImpl extends DAOImpl implements AssetDAO {

    private static final Logger logger = Logger.getLogger(AssetDAOImpl.class);
    
    public AssetDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
    
    @Override
    public List<AssetData> getAssetDataList(Collection<Long> assetIds) {
        StringBuilder baseQuery = new StringBuilder("Select object(asset) from AssetData asset");
        baseQuery.append(" WHERE asset.assetId").append(assetIds.size() == 1 ? " = " : " in ").append("(:assetIds)");

        Query query = em.createQuery(baseQuery.toString());
        query.setParameter("assetIds", assetIds);

        List<AssetData> assetList = (List<AssetData>) query.getResultList();
        
        return assetList;
    }
    
    @Override
    public List<AssetListView> findAssetsByCriteria(AssetListViewCriteria criteria) {
        List<AssetListView> assets = new ArrayList<AssetListView>();
        
        if (criteria != null && criteria.getStoreId() != null && criteria.getSiteId() != null) {
            
            logger.info("findAssetsByCriteria()=> BEGIN, criteria: " + criteria);

            StringBuilder baseQuery = new StringBuilder("Select DISTINCT new com.espendwise.manta.model.view.AssetListView");
                    baseQuery.append("(asset.assetId,");
                    baseQuery.append(" asset.shortDesc,");
                    baseQuery.append(" asset.assetTypeCd,");
                    baseQuery.append(" asset.assetNum,");
                    baseQuery.append(" asset.serialNum,");
                    baseQuery.append(" asset.statusCd,");
                    baseQuery.append(" site.busEntityId,");
                    baseQuery.append(" site.shortDesc)");
                    baseQuery.append(" FROM AssetData asset, AssetAssocData assetToSite, AssetAssocData assetToStore, BusEntityData site");

                    baseQuery.append(" WHERE assetToStore.busEntityId = (:storeId)");
                    baseQuery.append(" AND assetToStore.assetAssocCd = (:assetToStoreAssocCd)");
                    baseQuery.append(" AND assetToSite.busEntityId = (:siteId)");
                    baseQuery.append(" AND assetToSite.assetAssocCd = (:assetToSiteAssocCd)");
                    baseQuery.append(" AND site.busEntityId = assetToSite.busEntityId");
                    baseQuery.append(" AND asset.assetId = assetToSite.assetId");
                    baseQuery.append(" AND asset.assetId = assetToStore.assetId");


            if (Utility.isSet(criteria.getAssetId())) {
                baseQuery.append(" AND asset.assetId = (:assetId)");
            }

            if (Utility.isSet(criteria.getAssetName())) {
                if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getAssetNameFilterType())) {
                    baseQuery.append(" AND UPPER(asset.shortDesc) like '")
                            .append(QueryHelp.startWith(criteria.getAssetName().toUpperCase()))
                            .append("'");
                } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getAssetNameFilterType())) {
                    baseQuery.append(" AND UPPER(account.shortDesc) like '")
                            .append(QueryHelp.contains(criteria.getAssetName().toUpperCase()))
                            .append("'");
                }
            }

            if (Utility.isTrue(criteria.getActiveOnly())) {
                baseQuery.append(" AND asset.statusCd <> ")
                        .append(QueryHelp.toQuoted(RefCodeNames.ASSET_STATUS_CD.INACTIVE));
            }

            Query q = em.createQuery(baseQuery.toString());

            q.setParameter("storeId", criteria.getStoreId());
            q.setParameter("siteId", criteria.getSiteId());
            q.setParameter("assetToStoreAssocCd", RefCodeNames.ASSET_ASSOC_CD.ASSET_STORE);
            q.setParameter("assetToSiteAssocCd", RefCodeNames.ASSET_ASSOC_CD.ASSET_SITE);
            if (Utility.isSet(criteria.getAssetId())) {
                q.setParameter("assetId", criteria.getAssetId());
            }

            if (criteria.getLimit() != null) {
                q.setMaxResults(criteria.getLimit());
            }

            assets = (List<AssetListView>) q.getResultList();
        }
        
        logger.info("findAssetsByCriteria)=> END, fetched : " + assets.size() + " rows");
        
        return assets;
    }
    
}
