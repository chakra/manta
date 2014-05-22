package com.espendwise.manta.service;

import com.espendwise.manta.dao.CountryDAO;
import com.espendwise.manta.dao.CountryDAOImpl;
import com.espendwise.manta.model.view.CountryView;
import com.espendwise.manta.spi.AppDS;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CountryServiceImpl extends DataAccessService implements CountryService{

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CountryView> findAllCountries(@AppDS String unit, String... sortBy) {
        if (isAliveUnit(unit)) {
            CountryDAO countryDao = new CountryDAOImpl(getEntityManager(unit));
            return countryDao.findAll(sortBy);
        } else {
            return null;
        }
    }
}
