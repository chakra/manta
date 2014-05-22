package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.StoreProfileData;
import com.espendwise.manta.model.view.EntityHeaderView;

import com.espendwise.manta.model.view.StoreIdentView;
import java.util.List;

public interface StoreDAO {

    public BusEntityData findStoreById(Long storeId);

    public List<BusEntityData> findUserStores(Long userId);
    
    public List<BusEntityData> findStores();
    
    public EntityHeaderView findStoreHeader(Long storeId);

    public List<StoreProfileData> findStoreProfile(Long storeId);
    
    public StoreIdentView findStoreIdent(Long storeId);
}
