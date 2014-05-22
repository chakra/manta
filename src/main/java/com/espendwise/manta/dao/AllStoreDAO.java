package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.AllStoreData;

import java.util.List;

public interface AllStoreDAO {

    public List<AllStoreData> findSroresByDomain(String domainName);

    public  AllStoreData findUserDefaultStore(String username);
}
