package com.espendwise.manta.service;

import com.espendwise.manta.dao.AssetDAO;
import com.espendwise.manta.dao.AssetDAOImpl;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.AssetListViewCriteria;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AssetServiceImpl extends DataAccessService implements AssetService {

    private static final Logger logger = Logger.getLogger(AssetServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<AssetData> getAssetDataList(Collection<Long> assetIds) {
        EntityManager entityManager = getEntityManager();
        AssetDAO assetDAO = new AssetDAOImpl(entityManager);

        return assetDAO.getAssetDataList(assetIds);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<AssetListView> findAssetsByCriteria(AssetListViewCriteria criteria) {
        EntityManager entityManager = getEntityManager();
        AssetDAO assetDAO = new AssetDAOImpl(entityManager);

        return assetDAO.findAssetsByCriteria(criteria);
    }
}
