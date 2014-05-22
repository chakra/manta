package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogAssocData;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.view.CatalogStructureListView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.model.view.ItemLoaderView;
import com.espendwise.manta.model.view.CatalogAssocView;
import com.espendwise.manta.model.view.CatalogView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.model.view.ServiceListView;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.util.criteria.CatalogSearchCriteria;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.util.criteria.CatalogStructureListViewCriteria;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;

import com.espendwise.manta.util.criteria.ServiceListViewCriteria;
import java.util.List;

public interface CatalogDAO {

    public CatalogAssocData createCatalogAssoc(CatalogData catalog, BusEntityData busEntity) throws IllegalDataStateException;

    public CatalogAssocView findEntityCatalog(Long createAssocFromSiteId, String catalogSite) throws IllegalDataStateException;
    
    public List<CatalogView> findCatalogsByCriteria(CatalogSearchCriteria criteria);

    public List<CatalogListView> findCatalogsByCriteria(CatalogListViewCriteria criteria);

    public List<CatalogStructureListView> findCatalogStructuresByCriteria(CatalogStructureListViewCriteria criteria);

    public void removeCatalogAssoc(Long entityId, String catalogAssoc);
    
    public List<ItemView> findCatalogCategories (List<Long> catalogIds);
    
    public List<ItemData> findProducts (Long pStoreId, List<Long> skuNumbers);

    public List<ProductListView> findProductsByCriteria (ProductListViewCriteria criteria);
    
    public List<ServiceListView> findServicesByCriteria(ServiceListViewCriteria criteria);
 
    public List<ItemView> findItems (ProductListViewCriteria criteria);
    
    public List<CatalogData> findCatalogs ( Long pStoreId, List<Long> pCatalogIds);
    	 
    public List<ItemLoaderView> findItems (List<Long> catalogIds);
    
    public List<CatalogStructureData> findCatalogStructuresUsingCriteria(CatalogStructureListViewCriteria criteria);
    
    public List<CatalogData> findCatalogDataUsingCriteria(CatalogListViewCriteria criteria);
    
}
