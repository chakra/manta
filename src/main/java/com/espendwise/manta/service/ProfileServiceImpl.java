package com.espendwise.manta.service;

import com.espendwise.manta.dao.ProfileDAO;
import com.espendwise.manta.dao.ProfileDAOImpl;
import com.espendwise.manta.model.view.ProfilePasswordMgrView;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Service
public class ProfileServiceImpl extends DataAccessService implements ProfileService {

    @Override
    public ProfilePasswordMgrView getPasswordManagementInfo(Long storeId) {

        EntityManager entityManager = getEntityManager();
        ProfileDAO profileDao = new ProfileDAOImpl(entityManager);

        return profileDao.getPasswordManagementInfo(storeId);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ProfilePasswordMgrView savePasswordManagementInfo(ProfilePasswordMgrView passwordView) {

        EntityManager entityManager = getEntityManager();
        ProfileDAO profileDao = new ProfileDAOImpl(entityManager);

        return profileDao.savePasswordManagementInfo(passwordView);
    }
}
