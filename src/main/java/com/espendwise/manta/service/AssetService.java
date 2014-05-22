package com.espendwise.manta.service;


import com.espendwise.manta.model.data.AssetData;
import com.espendwise.manta.model.view.AssetListView;
import com.espendwise.manta.util.criteria.AssetListViewCriteria;
import java.util.Collection;

import java.util.List;

public interface AssetService {
    
    public List<AssetData> getAssetDataList(Collection<Long> assetIds);
    
    public List<AssetListView> findAssetsByCriteria(AssetListViewCriteria criteria);
    
}
