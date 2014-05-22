package com.espendwise.manta.service;

import com.espendwise.manta.dao.AllUserDAO;
import com.espendwise.manta.dao.AllUserDAOImpl;
import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.model.view.AllUserIdentView;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.UserUpdateMainConstraint;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Service
public class AllUserServiceImpl extends DataAccessService implements AllUserService {

    private static final Logger logger = Logger.getLogger(AllUserServiceImpl.class);

    @Override
    public AllUserData findById(Long allUserId) {
        return ServiceLocator
                .getAllUserService()
                .findById(getMainUnit(),  allUserId);
    }

    @Override
    public AllUserData findByUserName(String username) {
        return ServiceLocator
                .getAllUserService()
                .findByUserName(getMainUnit(),  username);
    }

    @Override
    public AllUserIdentView findViewByName(String userName) {
        return ServiceLocator
                .getAllUserService()
                .findViewByName(getMainUnit(), userName);
    }

    @Override
    @Transactional(readOnly = true)
    public AllUserData findById(@AppDS String unit, Long allUserId) {
        AllUserDAO allUserDao = new AllUserDAOImpl(getEntityManager(unit));
        return allUserDao.findById(allUserId);
    }

    @Override
    public AllUserData findByUserName(@AppDS String unit, String username) {
        AllUserDAO allUserDao = new AllUserDAOImpl(getEntityManager(unit));
        return allUserDao.findByUserName(username);
    }

    @Override
    @Transactional(readOnly = true)
    public AllUserIdentView findViewByName(@AppDS String unit, String userName) {
        AllUserDAO allUserDao = new AllUserDAOImpl(getEntityManager(unit));
        return allUserDao.findViewByName(userName);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public AllUserIdentView saveUserIdentToMain(@AppDS String unit, AllUserIdentView allUserIdent) {
        
        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new UserUpdateMainConstraint(allUserIdent.getAllUserData().getAllUserId(), allUserIdent.getAllUserData().getUserName()));

        validation.validate();

        EntityManager entityManagerForDsName = getEntityManager(unit);
        AllUserDAO allUserDao = new AllUserDAOImpl(entityManagerForDsName);
        return allUserDao.saveUserIdentToMain(allUserIdent);
    }

}
