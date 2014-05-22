package com.espendwise.manta.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.view.TradingPartnerListView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.TradingPartnerListViewCriteria;
import com.espendwise.manta.util.parser.Parse;

public class TradingPartnerDAOImpl extends DAOImpl implements TradingPartnerDAO{

    private static final Logger logger = Logger.getLogger(TradingPartnerDAOImpl.class);

    public TradingPartnerDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<TradingPartnerListView> findTradingPartnersByCriteria(TradingPartnerListViewCriteria criteria) {

        logger.info("findTradingPartnersByCriteria()=> BEGIN, criteria: "+criteria);

        StringBuilder baseQuery = new StringBuilder("select distinct new com.espendwise.manta.model.view.TradingPartnerListView(");
        baseQuery.append(" tradingPartner.tradingPartnerId, tradingPartner.shortDesc, tradingPartner.tradingTypeCd,");
        baseQuery.append(" tradingPartner.tradingPartnerTypeCd, tradingPartner.tradingPartnerStatusCd, tradingPartner.skuTypeCd, tradingPartner.uomConversionTypeCd,"); 
        baseQuery.append(" tradingPartner.siteIdentifierTypeCd, tradingPartner.validateContractPrice, tradingPartner.poNumberType, tradingPartner.accountIdentifierInbound) ");
        baseQuery.append(" from com.espendwise.manta.model.fullentity.TradingPartnerFullEntity tradingPartner ");
        if (Utility.isSet(criteria.getDistributorId())) {
        	baseQuery.append(" inner join tradingPartner.tradingPartnerAssocs assoc ");
        	baseQuery.append(" where tradingPartner.tradingPartnerId = assoc.tradingPartnerId ");
        	baseQuery.append(" and assoc.busEntityId = ");
            baseQuery.append(criteria.getDistributorId());
        }

        if (Utility.isSet(criteria.getTradingPartnerId())) {
            baseQuery.append(" and tradingPartner.tradingPartnerId = ").append(Parse.parseLong(criteria.getTradingPartnerId()));
        }

        if (Utility.isSet(criteria.getTradingPartnerName())) {
        	if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getTradingPartnerNameFilterType())) {
                baseQuery.append(" and UPPER(tradingPartner.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getTradingPartnerName().toUpperCase()))
                        .append("'");
            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getTradingPartnerNameFilterType())) {
                baseQuery.append(" and UPPER(tradingPartner.shortDesc) like '")
                        .append(QueryHelp.contains(criteria.getTradingPartnerName().toUpperCase()))
                        .append("'");
            }
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and tradingPartner.tradingPartnerStatusCd <> ")
                    .append(QueryHelp.toQuoted(RefCodeNames.TRADING_PARTNER_STATUS_CD.INACTIVE));
        }
        
        baseQuery.append(" order by tradingPartner.shortDesc ASC");

        Query q = em.createQuery(baseQuery.toString());

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        List<TradingPartnerListView> r = q.getResultList();

        logger.info("findTradingPartnersByCriteria)=> END, fetched : " + r.size() + " rows");

        return r;

    }

}
