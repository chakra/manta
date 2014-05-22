package com.espendwise.manta.service;


import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.CostCenterListViewCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.CostCenterUpdateConstraint;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class CostCenterServiceImpl extends DataAccessService implements CostCenterService {

    private static final Logger logger = Logger.getLogger(CostCenterServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CostCenterListView> findCostCentersByCriteria(CostCenterListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        CostCenterDAO costCenterDao = new CostCenterDAOImpl(entityManager);

        return costCenterDao.findCostCentersByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public CostCenterHeaderView findCostCenterHeader(Long costCenterId) {

        EntityManager entityManager = getEntityManager();
        CostCenterDAO costCenterDao = new CostCenterDAOImpl(entityManager);

        return costCenterDao.findCostCenterHeader(costCenterId);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CostCenterView findCostCenterToEdit(Long storeId, Long costCenterId) {

        EntityManager entityManager = getEntityManager();
        CostCenterDAO costCenterDao = new CostCenterDAOImpl(entityManager);

        return costCenterDao.findCostCenterToEdit(storeId, costCenterId);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CostCenterView saveCostCenter(Long storeId,  CostCenterView costCenterView)
                throws DatabaseUpdateException, IllegalDataStateException {

        EntityManager entityManager = getEntityManager();
        CostCenterDAO costCenterDao = new CostCenterDAOImpl(entityManager);

        CostCenterView costCenter = costCenterDao.saveCostCenter(storeId, costCenterView);

        return costCenter;
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureCostCenterCatalogs(Long costCenterId, Long storeId,
                                            List<CatalogListView> selected,
                                            List<CatalogListView> fullList) {
        EntityManager entityManager = getEntityManager();
        CostCenterDAO costCenterDao = new CostCenterDAOImpl(entityManager);

        costCenterDao.configureCostCenterCatalogs(costCenterId, storeId, selected, fullList);
    }

}
