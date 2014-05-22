package com.espendwise.manta.service;

import com.espendwise.manta.dao.BusEntityAssocDAO;
import com.espendwise.manta.dao.BusEntityAssocDAOImpl;
import com.espendwise.manta.dao.BusEntityDAO;
import com.espendwise.manta.dao.BusEntityDAOImpl;
import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BusEntityServiceImpl extends DataAccessService implements BusEntityService {

    private static final Logger logger = Logger.getLogger(BusEntityServiceImpl.class);

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public BusEntityData create(BusEntityData busEntityData) {

        EntityManager entityManager = getEntityManager();
        BusEntityDAO beDAO = new BusEntityDAOImpl(entityManager);
        
        return beDAO.create(busEntityData);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(List<Long> entityIds) {

        EntityManager entityManager = getEntityManager();
        BusEntityDAO beDAO = new BusEntityDAOImpl(entityManager);
        
        beDAO.delete(entityIds);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeEntities(List<BusEntityData> entities) {

        EntityManager entityManager = getEntityManager();
        BusEntityDAO beDAO = new BusEntityDAOImpl(entityManager);
        
        beDAO.removeEntities(entities);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public BusEntityData createOrUpdate(BusEntityData entity) {

        EntityManager entityManager = getEntityManager();
        BusEntityDAO beDAO = new BusEntityDAOImpl(entityManager);
        
        return beDAO.createOrUpdate(entity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> find(List<Long> entityIds) {

        EntityManager entityManager = getEntityManager();
        BusEntityDAO beDAO = new BusEntityDAOImpl(entityManager);
        
        return beDAO.find(entityIds);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> find(List<Long> entityIds, String busEntityTypeCd) {

        EntityManager entityManager = getEntityManager();
        BusEntityDAO beDAO = new BusEntityDAOImpl(entityManager);
        
        return beDAO.find(entityIds, busEntityTypeCd);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public BusEntityData getDistributorByErpNum(String distributorErpNum) {

        EntityManager entityManager = getEntityManager();
        BusEntityDAO beDAO = new BusEntityDAOImpl(entityManager);
        
        return beDAO.getDistributorByErpNum(distributorErpNum);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityAssocData> findAssocs(Long busEntity1Id, Long busEntity2Id, String busEntityAssocCd) {
        EntityManager entityManager = getEntityManager();
        BusEntityAssocDAO beAssocDAO = new BusEntityAssocDAOImpl(entityManager);
        
        return beAssocDAO.findAssocs(busEntity1Id, busEntity2Id, busEntityAssocCd);
    }

}
