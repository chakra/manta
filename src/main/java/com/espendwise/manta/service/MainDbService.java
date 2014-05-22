package com.espendwise.manta.service;

import com.espendwise.manta.auth.AuthDatabaseAccessUnit;

import com.espendwise.manta.model.data.AllUserData;
import java.util.List;
import java.util.Map;

public interface MainDbService {

    public Map<String,AuthDatabaseAccessUnit> getUserDataAccessUnits(String userName);
    
    public Map<String,AuthDatabaseAccessUnit> getUserDataAccessMainUnits(String userName);
    
    public AllUserData findByUserName(String userName);

    public List<String> getAliveUnits();
    
    public boolean isAliveMainUnit();

    public AuthDatabaseAccessUnit createDataAccessUnit(String datasource);
}