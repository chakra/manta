package com.espendwise.manta.service;

import java.util.List;

import com.espendwise.manta.model.view.TradingPartnerListView;
import com.espendwise.manta.util.criteria.TradingPartnerListViewCriteria;

public interface TradingPartnerService {

    public List<TradingPartnerListView> findTradingPartnersByCriteria(TradingPartnerListViewCriteria criteria);

}
