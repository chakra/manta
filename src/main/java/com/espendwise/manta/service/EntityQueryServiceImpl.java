package com.espendwise.manta.service;

import com.espendwise.manta.dao.EntityQueryDAOImpl;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EntityQueryServiceImpl extends DataAccessService implements EntityQueryService {

    private static final Logger logger = Logger.getLogger(EntityQueryServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> List<T> findEntities(@AppDS String unit, Class<T> entityClass, String orderByField, Boolean orderAsc) {

        logger.info("findEntities()=> BEGIN,  entityClass:  " + entityClass);

        if (isAliveUnit(unit)) {

            List<T> r = isAliveUnit(unit)
                    ? new EntityQueryDAOImpl(getEntityManager(unit)).findEntities(entityClass, orderByField, orderAsc)
                    : Utility.emptyList(entityClass);

            logger.info("findEntities()=> END, fetched:  " + r.size() + " rows");

            return r;

        } else {

            logger.info("findEntities()=> END, unit is unavailable");

            return null;

        }
    }
}
