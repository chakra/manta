package com.espendwise.manta.service;


import com.espendwise.manta.model.view.CountryView;
import com.espendwise.manta.spi.AppDS;

import java.util.List;

public interface CountryService {

    public List<CountryView> findAllCountries(@AppDS String unit, String... sortBy);
}
