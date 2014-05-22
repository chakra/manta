package com.espendwise.manta.service;


import com.espendwise.manta.auth.AuthDataSourceIdent;
import com.espendwise.manta.auth.AuthDatabaseAccessUnit;
import com.espendwise.manta.auth.AuthMainStoreIdent;
import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.AllStoreData;
import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainDbServiceImpl extends DataAccessService implements MainDbService {

    private static final Logger logger = Logger.getLogger(MainDbServiceImpl.class);

        public Map<String, AuthDatabaseAccessUnit> getUserDataAccessUnits(String userName) {

        logger.info("getUserDataAccessUnits()=> BEGIN, userName: " + userName);

        Map<String, AuthDatabaseAccessUnit> m;

        logger.info("getUserDataAccessUnits()=> check MAIN_UNIT: ");
        if (isAliveUnit(DatabaseAccess.getMainUnit())) {

            m = createMainDataAccessUnits(
                    DatabaseAccess.getMainUnit(),
                    userName
            );

        } else {

            m = createWorkDataAccessUnits();

        }
        logger.info("getUserDataAccessUnits()=> END, m: " + m);

        return m;
    }
    
    public Map<String, AuthDatabaseAccessUnit> getUserDataAccessMainUnits(String userName) {

       return createMainDataAccessUnits(
                DatabaseAccess.getMainUnit(),
                userName
        );
    }

    public AuthDatabaseAccessUnit createDataAccessUnit(String datasource) {

        if (isAliveUnit(datasource)) {

            AuthDataSourceIdent dsIdent = new AuthDataSourceIdent(
                    datasource,
                    getDataSourceUrl(datasource)
            );

            return new AuthDatabaseAccessUnit(
                    datasource,
                    dsIdent,
                    true,
                    null
            );

        }

        return null;
    }


    private Map<String, AuthDatabaseAccessUnit> createWorkDataAccessUnits() {

        Map<String, AuthDatabaseAccessUnit> m = new HashMap<String, AuthDatabaseAccessUnit>();

        for (String datasource : DatabaseAccess.availableUnits()) {

            if (isAliveUnit(datasource)) {
                AuthDatabaseAccessUnit unit = createDataAccessUnit(datasource);
                m.put(unit.getUnitName(), unit);
            }

        }

        return m;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, noRollbackFor = Throwable.class)
    public Map<String, AuthDatabaseAccessUnit> createMainDataAccessUnits(@AppDS String mainUnit, String userName) {
        Map<String, AuthDatabaseAccessUnit> m = new HashMap<String, AuthDatabaseAccessUnit>();

        EntityManager mainEntityManager = getEntityManager(mainUnit);

        AllUserDAO allUserDao = new AllUserDAOImpl(mainEntityManager);
        AllUserStoreDAO allStoreDao = new AllUserStoreDAOImpl(mainEntityManager);

        AllUserData userData = allUserDao.findByUserName(userName);

        if (userData != null) {

            List<AllStoreData> allStores = allStoreDao.getAllUserStores(userData.getAllUserId());

            for (AllStoreData x : selectAliveStores(allStores)) {

                if (Utility.isSet(x.getDatasource())) {

                    AuthDataSourceIdent dsIdent = new AuthDataSourceIdent(
                            x.getDatasource(),
                            getDataSourceUrl(x.getDatasource())
                    );

                    m.put(x.getDatasource(), 
                            new AuthDatabaseAccessUnit(
                                    x.getDatasource(),
                                    dsIdent,
                                    true,
                                    selectDataSourcesStores(allStores, x.getDatasource())
                            )
                    );
                }
            }
        }

        return m;
    }

    private List<AuthMainStoreIdent> selectDataSourcesStores(List<AllStoreData> allStores, String datasource) {

        List<AuthMainStoreIdent> x = new ArrayList<AuthMainStoreIdent>();

        if (Utility.isSet(allStores)) {
            for (AllStoreData store : allStores) {
                if (datasource.equalsIgnoreCase(store.getDatasource())) {
                    x.add(new AuthMainStoreIdent(store.getAllStoreId(),
                            store.getStoreId(),
                            store.getStoreName(),
                            store.getDatasource(),
                            store.getDomain()
                    ));
                }
            }
        }

        return x;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, noRollbackFor = Throwable.class)
    public boolean isAliveUnit(@AppDS String unit) {
        return super.isAliveUnit(unit);
    }

    private List<AllStoreData> selectAliveStores(List<AllStoreData> allStoreDatas) {

        List<AllStoreData> x = new ArrayList<AllStoreData>();

        for (AllStoreData allStoreData : allStoreDatas) {
            if (isAliveUnit(allStoreData.getDatasource())) {
                x.add(allStoreData);
            }
        }

        return x;
    }

    public List<String> getAliveUnits() {
        List<String> x = new ArrayList<String>();
        for (String unit : availableUnits()) {
            if (isAliveUnit(unit)) {
                x.add(unit);
            }
        }
        return x;
    }
    
    public AllUserData findByUserName(String userName) {
        return findByUserName(DatabaseAccess.getMainUnit(), userName);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    private AllUserData findByUserName(@AppDS String mainUnit, String userName) {
        EntityManager mainEntityManager = getEntityManager(mainUnit);
        AllUserDAO allUserDao = new AllUserDAOImpl(mainEntityManager);
       
        return allUserDao.findByUserName(userName);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public boolean isAliveMainUnit() {
        return super.isAliveUnit(getMainUnit());
    }
}
