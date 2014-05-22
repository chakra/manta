package com.espendwise.manta.service;


import com.espendwise.manta.model.view.CatalogView;
import com.espendwise.manta.util.criteria.CatalogSearchCriteria;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.util.criteria.CatalogStructureListViewCriteria;
import com.espendwise.manta.web.util.SuccessActionMessage;
import com.espendwise.manta.model.view.CatalogStructureListView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;
import com.espendwise.manta.model.view.ProductListView;

import com.espendwise.manta.model.view.ServiceListView;
import com.espendwise.manta.util.criteria.ServiceListViewCriteria;
import java.util.List;
import java.io.InputStream;
import java.util.Locale;

public interface CatalogService {

    public List<CatalogView> findCatalogsByCriteria(CatalogSearchCriteria criteria);

    public List<CatalogListView> findCatalogsByCriteria(CatalogListViewCriteria criteria);

    public List<CatalogStructureListView> findCatalogStructuresByCriteria(CatalogStructureListViewCriteria criteria);

    public List<ItemView> findCatalogCategories(List<Long> catalogIds);

    public List<ItemData> findProducts(Long pStoreId, List<Long> pSkuNumbers);

    public List<ProductListView> findProductsByCriteria(ProductListViewCriteria criteria);
    
    public List<ServiceListView> findServicesByCriteria(ServiceListViewCriteria criteria);

    public List<ItemView> findItems(ProductListViewCriteria criteria);
    
    public int processCatalogUpload(Locale locale, Long currStoreId, InputStream inputStream,String streamType);
    
    public List<CatalogStructureData> findCatalogStructuresUsingCriteria(CatalogStructureListViewCriteria criteria);
    
    public List<CatalogData> findCatalogDataUsingCriteria(CatalogListViewCriteria criteria);
    
}
