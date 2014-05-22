package com.espendwise.manta.dao;

import com.espendwise.manta.model.view.UploadFileListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.data.UploadSkuData;
import com.espendwise.manta.model.view.UploadSkuView;
import com.espendwise.manta.model.data.UploadData;
import com.espendwise.manta.util.criteria.UploadFileListViewCriteria;

import java.util.List;

public interface UploadFileDAO {

    public List<UploadFileListView> findUploadFilesByCriteria(UploadFileListViewCriteria criteria);

    public UploadData findUploadData(Long uploadId);

    public List<UploadSkuData> findUploadSkuDataList(Long tableId);

    public List<UploadSkuView> findUploadSkuViewList(Long tableId);

   public EntityHeaderView findUploadFileHeader(Long uploadId);

   public UploadData saveUploadData(Long storeId, UploadData uploadData);

   public UploadSkuData saveUploadSkuData(Long uploadId, UploadSkuData uploadSkuData);

   public List<UploadSkuData> saveUploadSkuDataList(Long uploadId, List<UploadSkuData> skuDataList);

   public List<UploadSkuView> saveUploadSkuViewList(Long uploadId, List<UploadSkuView> skuViewList);

   public List<UploadSkuView> matchItems(Long storeId, Long uploadId, List<UploadSkuView> itemsToMatch);

   public List<UploadSkuView> getMatchedItems(Long storeId, Long uploadId, List<Long> pUploadSkuIds);

   public Long getStoreCatalogId(Long storeId);

}
