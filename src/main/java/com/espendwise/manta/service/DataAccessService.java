package com.espendwise.manta.service;


import com.espendwise.manta.auth.ApplicationDataSource;
import com.espendwise.manta.spi.AppDS;
import com.espendwise.manta.util.TxUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public abstract class DataAccessService extends DatabaseAccess {

    @Autowired
    protected ApplicationDataSource dataSource;

    private ApplicationDataSource getDataSource() {
        return dataSource;
    }

    public EntityManager getEntityManager() {

        return getEntityManager(
                getDataSource()
                        .getDataSourceIdent()
                        .getDataSourceName()
        );
    }

    public boolean isAliveUnit(@AppDS String unit) {
        return TxUtil.isAliveEntityManager(unit, getEntityManager(unit));
    }

    public String getDataSourceUrl(String datasource) {
       return TxUtil.getDataSourceUrl(getEntityManager(datasource));
    }

}
