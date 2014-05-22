package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.AssetData;
import com.espendwise.manta.model.view.AssetListView;
import com.espendwise.manta.util.criteria.AssetListViewCriteria;
import java.util.Collection;
import java.util.List;

public interface AssetDAO {

    public List<AssetData> getAssetDataList(Collection<Long> assetIds);
 
    public List<AssetListView> findAssetsByCriteria(AssetListViewCriteria criteria);
    
}
