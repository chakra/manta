package com.espendwise.manta.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.data.CurrencyData;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.CurrencyCriteria;

/**
 * Sample StoreMessage DAO class for fetching store messages.
 */
public class CurrencyDAOImpl extends DAOImpl implements CurrencyDAO {

    private static final Logger logger = Logger.getLogger(CurrencyDAOImpl.class);

    public CurrencyDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
    public List<CurrencyData> findCurrenciesByCriteria(CurrencyCriteria criteria) {

        logger.info("findCurrenciesByCriteria()=> BEGIN");
        logger.info("findCurrenciesByCriteria()=> criteria: " + criteria);
        
        StringBuilder baseSql = new StringBuilder("Select c from CurrencyData c ");
        String whereOrAnd = " where ";
        
        if (Utility.isSet(criteria.getCurrencyId())) {
        	baseSql.append(whereOrAnd);
        	whereOrAnd = " and ";
        	baseSql.append(" and c.currencyId = (:currencyId) ");
        }     
        
        if (Utility.isSet(criteria.getLocale())) {
        	baseSql.append(whereOrAnd);
        	whereOrAnd = " and ";
        	baseSql.append(" and c.locale = (:locale) ");
        }
        
        if (Utility.isSet(criteria.getShortDesc())) {
        	baseSql.append(whereOrAnd);
        	whereOrAnd = " and ";
        	baseSql.append(" and c.shortDesc = (:shortDesc) ");
        }
        
        if (Utility.isSet(criteria.getLocalCode())) {
        	baseSql.append(whereOrAnd);
        	whereOrAnd = " and ";
        	baseSql.append(" and c.localCode = (:localCode) ");
        }
        
        if (Utility.isSet(criteria.getCurrencyPositionCd())) {
        	baseSql.append(whereOrAnd);
        	whereOrAnd = " and ";
        	baseSql.append(" and c.currencyPositionCd = (:currencyPositionCd) ");
        }
        
        if (Utility.isSet(criteria.getDecimals())) {
        	baseSql.append(whereOrAnd);
        	whereOrAnd = " and ";
        	baseSql.append(" and c.decimals = (:decimals) ");
        }
        
        if (Utility.isSet(criteria.getGlobalCode())) {
        	baseSql.append(whereOrAnd);
        	whereOrAnd = " and ";
        	baseSql.append(" and c.globalCode = (:globalCode) ");
        }

        Query q = em.createQuery(baseSql.toString());

        if (criteria.getCurrencyId() != null) {
            q.setParameter("currencyId", criteria.getCurrencyId());
        }

        if (criteria.getLocale() != null) {
            q.setParameter("locale", criteria.getLocale());
        }

        if (criteria.getShortDesc() != null) {
            q.setParameter("shortDesc", criteria.getShortDesc());
        }

        if (criteria.getLocalCode() != null) {
            q.setParameter("localCode", criteria.getLocalCode());
        }

        if (criteria.getCurrencyPositionCd() != null) {
            q.setParameter("currencyPositionCd", criteria.getCurrencyPositionCd());
        }

        if (criteria.getDecimals() != null) {
            q.setParameter("decimals", criteria.getDecimals());
        }

        if (criteria.getGlobalCode() != null) {
            q.setParameter("globalCode", criteria.getGlobalCode());
        }

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        List<CurrencyData> returnValue = (List<CurrencyData>) q.getResultList();
        if (returnValue == null) {
        	returnValue = new ArrayList<CurrencyData>();
        }

        logger.info("findCurrenciesByCriteria()=> END, fetched " + returnValue.size() + " rows");

        return returnValue;
    }

}

