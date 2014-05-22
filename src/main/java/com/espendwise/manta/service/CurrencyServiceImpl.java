package com.espendwise.manta.service;


import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.CurrencyDAO;
import com.espendwise.manta.dao.CurrencyDAOImpl;
import com.espendwise.manta.model.data.CurrencyData;
import com.espendwise.manta.util.criteria.CurrencyCriteria;

@Service
public class CurrencyServiceImpl extends DataAccessService implements CurrencyService {

    private static final Logger logger = Logger.getLogger(CurrencyServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CurrencyData> findCurrenciesByCriteria(CurrencyCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        CurrencyDAO currencyDao = new CurrencyDAOImpl(entityManager);

        return currencyDao.findCurrenciesByCriteria(criteria);
    }
}
