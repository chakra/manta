package com.espendwise.manta.service;


import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.ManufacturerDAO;
import com.espendwise.manta.dao.ManufacturerDAOImpl;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.ManufacturerIdentView;
import com.espendwise.manta.model.view.ManufacturerListView;
import com.espendwise.manta.util.criteria.ManufacturerListViewCriteria;
import com.espendwise.manta.util.criteria.StoreManufacturerCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.rules.ManufacturerUpdateConstraint;

@Service
public class ManufacturerServiceImpl extends DataAccessService implements ManufacturerService {

    private static final Logger logger = Logger.getLogger(ManufacturerServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ManufacturerListView> findManufacturersByCriteria(ManufacturerListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        ManufacturerDAO manufacturerDao = new ManufacturerDAOImpl(entityManager);

        return manufacturerDao.findManufacturersByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EntityHeaderView findManufacturerHeader(Long manufacturerId) {

        EntityManager entityManager = getEntityManager();
        ManufacturerDAO manufacturerDao = new ManufacturerDAOImpl(entityManager);

        return manufacturerDao.findManufacturerHeader(manufacturerId);
    } 


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ManufacturerIdentView findManufacturerToEdit(Long storeId, Long manufacturerId) {

        EntityManager entityManager = getEntityManager();
        ManufacturerDAO manufacturerDao = new ManufacturerDAOImpl(entityManager);

        return manufacturerDao.findManufacturerToEdit(storeId, manufacturerId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ManufacturerIdentView saveManufacturer(Long storeId,
                                       Long userId,
                                       ManufacturerIdentView ManufacturerIdentView) throws ValidationException, DatabaseUpdateException, IllegalDataStateException {

        ServiceLayerValidation validation = new ServiceLayerValidation();

        validation.addRule(new ManufacturerUpdateConstraint(storeId, ManufacturerIdentView));
        

        validation.validate();

        EntityManager entityManager = getEntityManager();

        ManufacturerDAO manufacturerDao = new ManufacturerDAOImpl(entityManager);

        ManufacturerIdentView manufacturer = manufacturerDao.saveManufacturer(storeId, ManufacturerIdentView);

        return manufacturer;
    }

 /*
    @Override
    public void deleteManufacturerIdent(Long storeId, Long manufacturerId) throws DatabaseUpdateException {

        try {

            ServiceLocator
                    .getManufacturerService()
                    .deleteManufacturer(storeId, manufacturerId);

        } catch (JpaSystemException e) {

                throw new DatabaseUpdateException(
                        e.getMessage(),
                        e.getMostSpecificCause(),
                        ExceptionReason.ManufacturerUpdateReason.SITE_CANT_BE_DELETED
                );


        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteManufacturer(Long storeId, Long manufacturerId) {

        EntityManager entityManager = getEntityManager();
        ManufacturerDAO manufacturerDao = new ManufacturerDAOImpl(entityManager);

        ManufacturerIdentView Manufacturer = manufacturerDao.findManufacturerToEdit(storeId, manufacturerId);
        if (Manufacturer != null) {
            manufacturerDao.removeManufacturer(Manufacturer);
        }

    }  */

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findManufacturers(StoreManufacturerCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        ManufacturerDAO manufacturerDao = new ManufacturerDAOImpl(entityManager);

        return manufacturerDao.findManufacturers(criteria);
    }

}
