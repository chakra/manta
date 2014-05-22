package com.espendwise.manta.support.spring;


import com.espendwise.manta.auth.ApplicationDataSource;
import com.espendwise.manta.auth.Auth;
import org.apache.log4j.Logger;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import java.util.Map;

public class ApplicationTransactionManager implements PlatformTransactionManager {

    private static final Logger logger = Logger.getLogger(ApplicationTransactionManager.class);

    private Map<String, JpaTransactionManager> managers;

    public void setManagers(Map<String, JpaTransactionManager> managers) {
        this.managers = managers;
    }

    public Map<String, JpaTransactionManager> getManagers() {
        return managers;
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        AbstractPlatformTransactionManager manager = txManager();
        if (manager != null) {
            logger.info("<Begin transaction>");
            return manager.getTransaction(definition);
        } else {
            logger.info("<Transaction is unavailable>");
        }
        return null;
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        AbstractPlatformTransactionManager manager = txManager();
        if (manager != null) {
            logger.info("<Commit transaction>");
            manager.commit(status);
            logger.info("<Commited>");
 }
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        AbstractPlatformTransactionManager manager = txManager();
        if (manager != null) {
            logger.info("<Rollback transaction>");
            manager.rollback(status);
        }
    }

    public JpaTransactionManager txManager(Object datasource) {

        if (datasource == null) {
            return null;
        }

        if (datasource instanceof ApplicationDataSource) {
            return txManager(((ApplicationDataSource) datasource).getDataSourceIdent().getDataSourceName());
        } else if (datasource instanceof String) {
            return txManager((String) datasource);
        }


        return null;

    }

    public JpaTransactionManager txManager() {
        ApplicationDataSource ds = Auth.getApplicationDataSource();
        return txManager( (ds != null ?  ds.getDataSourceIdent().getDataSourceName() : null));
    }

    public JpaTransactionManager txManager(String datasource) {
        return datasource != null ? managers.get(datasource) : null;
    }

}
