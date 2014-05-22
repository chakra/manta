package com.espendwise.manta.dao;


import com.espendwise.manta.model.view.CountryView;

import java.util.List;

public interface CountryDAO {
   List<CountryView> findAll(String... sortBy);
}
