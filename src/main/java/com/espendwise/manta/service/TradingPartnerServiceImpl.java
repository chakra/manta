package com.espendwise.manta.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.TradingPartnerDAO;
import com.espendwise.manta.dao.TradingPartnerDAOImpl;
import com.espendwise.manta.model.view.TradingPartnerListView;
import com.espendwise.manta.util.criteria.TradingPartnerListViewCriteria;

@Service
public class TradingPartnerServiceImpl extends DataAccessService implements TradingPartnerService {

    private static final Logger logger = Logger.getLogger(TradingPartnerServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<TradingPartnerListView> findTradingPartnersByCriteria(TradingPartnerListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        TradingPartnerDAO tradingPartnerDao = new TradingPartnerDAOImpl(entityManager);

        return tradingPartnerDao.findTradingPartnersByCriteria(criteria);
    }

}
