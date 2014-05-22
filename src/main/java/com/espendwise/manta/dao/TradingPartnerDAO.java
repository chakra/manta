package com.espendwise.manta.dao;

import java.util.List;

import com.espendwise.manta.model.view.TradingPartnerListView;
import com.espendwise.manta.util.criteria.TradingPartnerListViewCriteria;

public interface TradingPartnerDAO {

    public List<TradingPartnerListView> findTradingPartnersByCriteria(TradingPartnerListViewCriteria criteria);

}
