package com.espendwise.manta.service;


import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.DistributorDAO;
import com.espendwise.manta.dao.DistributorDAOImpl;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.DistributorConfigurationView;
import com.espendwise.manta.model.view.DistributorIdentView;
import com.espendwise.manta.model.view.DistributorInfoView;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.util.criteria.DistributorListViewCriteria;
import com.espendwise.manta.util.criteria.StoreDistributorCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.rules.DistributorUpdateConstraint;

@Service
public class DistributorServiceImpl extends DataAccessService implements DistributorService {

    private static final Logger logger = Logger.getLogger(DistributorServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<DistributorListView> findDistributorsByCriteria(DistributorListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        DistributorDAO distributorDao = new DistributorDAOImpl(entityManager);

        return distributorDao.findDistributorsByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EntityHeaderView findDistributorHeader(Long distributorId) {

        EntityManager entityManager = getEntityManager();
        DistributorDAO distributorDao = new DistributorDAOImpl(entityManager);

        return distributorDao.findDistributorHeader(distributorId);
    } 


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public DistributorIdentView findDistributorToEdit(Long storeId, Long distributorId) {

        EntityManager entityManager = getEntityManager();
        DistributorDAO distributorDao = new DistributorDAOImpl(entityManager);

        return distributorDao.findDistributorToEdit(storeId, distributorId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public DistributorIdentView saveDistributor(Long storeId, Long userId, DistributorIdentView distributorIdentView) throws ValidationException, DatabaseUpdateException, IllegalDataStateException {

        ServiceLayerValidation validation = new ServiceLayerValidation();

        validation.addRule(new DistributorUpdateConstraint(storeId, distributorIdentView));
        validation.validate();

        EntityManager entityManager = getEntityManager();
        DistributorDAO distributorDao = new DistributorDAOImpl(entityManager);

        DistributorIdentView distributor = distributorDao.saveDistributor(storeId, distributorIdentView);

        return distributor;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findDistributors(StoreDistributorCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        DistributorDAO distributorDao = new DistributorDAOImpl(entityManager);

        return distributorDao.findDistributors(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public DistributorConfigurationView findDistributorConfigurationInformation(Long distributorId) {

        EntityManager entityManager = getEntityManager();
        DistributorDAO distributorDao = new DistributorDAOImpl(entityManager);

        return distributorDao.findDistributorConfigurationInformation(distributorId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public DistributorConfigurationView saveDistributorConfigurationInformation(Long distributorId, DistributorConfigurationView configuration) {

        EntityManager entityManager = getEntityManager();
        DistributorDAO distributorDao = new DistributorDAOImpl(entityManager);

        DistributorConfigurationView savedConfiguration = distributorDao.saveDistributorConfigurationInformation(distributorId, configuration);

        return savedConfiguration;
    	
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public DistributorInfoView findDistributorInfo(Long distributorId) {
        EntityManager entityManager = getEntityManager();
        DistributorDAO distributorDao = new DistributorDAOImpl(entityManager);

        return distributorDao.findDistributorInfo(distributorId);
    }
}
