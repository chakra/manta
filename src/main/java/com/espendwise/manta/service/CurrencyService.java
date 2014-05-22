package com.espendwise.manta.service;


import java.util.List;

import com.espendwise.manta.model.data.CurrencyData;
import com.espendwise.manta.util.criteria.CurrencyCriteria;

public interface CurrencyService {

    public List<CurrencyData> findCurrenciesByCriteria(CurrencyCriteria criteria);
}
