package com.espendwise.manta.service;

import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.StoreProfileData;
import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.model.view.StoreIdentView;
import com.espendwise.manta.util.criteria.StoreListEntityCriteria;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class StoreServiceImpl extends DataAccessService implements StoreService {

    private static final Logger logger = Logger.getLogger(StoreServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<StoreListEntity> findListEntity(Long userId, Integer limit) {

        EntityManager entityManager = getEntityManager();
        StoreListDAO storeListDao = new StoreListDAOImpl(entityManager);

        return storeListDao.find(userId, limit);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<PropertyData> findStoreUiProperties(Long storeId, Locale locale) {

        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        return propertyDao.findStoreUiProperties(storeId, locale);

    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<StoreListEntity> findListEntity(Long userId, StoreListEntityCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        StoreListDAO storeListDao = new StoreListDAOImpl(entityManager);

        return storeListDao.find(userId, criteria);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findStores() {

        EntityManager entityManager = getEntityManager();
        StoreDAO storeDao = new StoreDAOImpl(entityManager);

        return storeDao.findStores();
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findUserStores(Long userId) {

        EntityManager entityManager = getEntityManager();
        StoreDAO storeDao = new StoreDAOImpl(entityManager);

        return storeDao.findUserStores(userId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<StoreProfileData> findStoreProfile(Long storeId) {
        EntityManager entityManager = getEntityManager();
        StoreDAO storeDao = new StoreDAOImpl(entityManager);

        return storeDao.findStoreProfile(storeId);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<PropertyData> findStoreProperties(Long storeId, List<String> propertyTypes) {
        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        return propertyDao.findStoreProperties(storeId, propertyTypes);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public StoreIdentView findStoreIdent(Long storeId) {
        EntityManager entityManager = getEntityManager();
        StoreDAO storeDao = new StoreDAOImpl(entityManager);

        return storeDao.findStoreIdent(storeId);
    }

}
