package com.espendwise.manta.dao;


import java.util.List;

import com.espendwise.manta.model.data.CurrencyData;
import com.espendwise.manta.util.criteria.CurrencyCriteria;

public interface CurrencyDAO {

    public List<CurrencyData> findCurrenciesByCriteria(CurrencyCriteria criteria);
}
