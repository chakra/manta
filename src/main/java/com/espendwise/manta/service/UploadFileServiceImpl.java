package com.espendwise.manta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.apache.log4j.Logger;
import com.espendwise.manta.model.view.UploadFileListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.UploadSkuView;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.UploadSkuData;
import com.espendwise.manta.model.data.UploadData;
import com.espendwise.manta.util.criteria.UploadFileListViewCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.dao.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.ArrayList;

@Service
public class UploadFileServiceImpl extends DataAccessService implements UploadFileService {

    private static final Logger logger = Logger.getLogger(UploadFileServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UploadFileListView> findUploadFilesByCriteria(UploadFileListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.findUploadFilesByCriteria(criteria);
    }

    @Override
    public UploadFileListView findUploadFile(Long storeId, Long uploadId) {

        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);
        UploadFileListViewCriteria criteria = new UploadFileListViewCriteria();
        criteria.setStoreId(storeId);
        criteria.setUploadId(uploadId);
        List<UploadFileListView> list = fileDao.findUploadFilesByCriteria(criteria);
        if (list != null && list.size()> 0) {
            return list.get(0);
        } else {
            return null;
        }

    }

    @Override
    public UploadData findUploadData(Long uploadId) {

        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.findUploadData(uploadId);
    }

    @Override
    public List<UploadSkuData> findUploadSkuDataList(Long tableId) {
        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.findUploadSkuDataList(tableId);
    }

    @Override
    public List<UploadSkuView> findUploadSkuViewList(Long tableId) {
        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.findUploadSkuViewList(tableId);

    }

    @Override
    public EntityHeaderView findUploadFileHeader(Long uploadId) {
        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.findUploadFileHeader(uploadId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UploadData saveUploadData(Long storeId, UploadData uploadData) {
        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);
        return fileDao.saveUploadData(storeId, uploadData);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UploadSkuData saveUploadSkuData(Long tableId, UploadSkuData skuData) {
        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);
        return fileDao.saveUploadSkuData(tableId, skuData);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<UploadSkuView> saveUploadSkuViewList(Long tableId, List<UploadSkuView> skuViewList) {
        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.saveUploadSkuViewList(tableId, skuViewList);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<UploadSkuData> saveUploadSkuDataList(Long tableId, List<UploadSkuData> skuDataList) {
        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.saveUploadSkuDataList(tableId, skuDataList);

    }

    @Override
    public List<UploadSkuView> matchItems(Long storeId, Long uploadId, List<UploadSkuView> itemsToMatch) {

        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.matchItems(storeId, uploadId, itemsToMatch);
    }

    @Override
    public List<UploadSkuView> getMatchedItems(Long storeId, Long uploadId, List<Long> pUploadSkuIds) {

        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.getMatchedItems(storeId, uploadId, pUploadSkuIds);
    }

    @Override
    public Long getStoreCatalogId(Long storeId) {
        EntityManager entityManager = getEntityManager();
        UploadFileDAO fileDao = new UploadFileDAOImpl(entityManager);

        return fileDao.getStoreCatalogId(storeId);
    }
}
