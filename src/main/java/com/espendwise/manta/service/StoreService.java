package com.espendwise.manta.service;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.model.data.StoreProfileData;
import com.espendwise.manta.model.view.StoreIdentView;
import com.espendwise.manta.util.criteria.StoreListEntityCriteria;

import java.util.List;
import java.util.Locale;

public interface StoreService {

    public List<PropertyData> findStoreUiProperties(Long storeId, Locale locale);
    
    public List<PropertyData> findStoreProperties(Long storeId, List<String> propertyTypes);

    public List<StoreListEntity> findListEntity(Long userId, Integer limit);

    public List<StoreListEntity> findListEntity(Long userId, StoreListEntityCriteria criteria);
    
    public List<BusEntityData> findStores();
    
    public List<BusEntityData> findUserStores(Long userId);

    public List<StoreProfileData> findStoreProfile(Long storeId);

    public StoreIdentView findStoreIdent(Long storeId);
}
