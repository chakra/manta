package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.AllStoreData;

import java.util.List;

public interface AllUserStoreDAO {

    public List<AllStoreData> getAllUserStores(Long userId);
}
