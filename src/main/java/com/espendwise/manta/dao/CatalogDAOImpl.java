package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogAssocData;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.ContractData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.view.CatalogStructureListView;
import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.model.view.CatalogAssocView;
import com.espendwise.manta.model.view.CatalogView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.model.view.ServiceListView;
import com.espendwise.manta.model.view.ItemLoaderView;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.DatabaseError;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

import com.espendwise.manta.util.criteria.CatalogSearchCriteria;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.util.criteria.CatalogStructureListViewCriteria;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;

import com.espendwise.manta.util.criteria.ServiceListViewCriteria;
import com.espendwise.manta.web.util.WebSort;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CatalogDAOImpl extends DAOImpl implements CatalogDAO {

    private static final Logger logger = Logger.getLogger(CatalogDAOImpl.class);
 
    public CatalogDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public CatalogAssocData createCatalogAssoc(CatalogData catalog, BusEntityData busEntity) throws IllegalDataStateException {

       if(catalog == null || busEntity == null){
           return null;
       }

        String entityType = busEntity.getBusEntityTypeCd();

        String assocType = entityType.equals(RefCodeNames.BUS_ENTITY_TYPE_CD.STORE) ? RefCodeNames.CATALOG_ASSOC_CD.CATALOG_STORE
                : entityType.equals(RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT) ? RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT
                : entityType.equals(RefCodeNames.BUS_ENTITY_TYPE_CD.SITE) ? RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE
                : null;

        if(assocType == null){
            return null;
        }

        CatalogAssocView assoc = findEntityCatalog(busEntity.getBusEntityId(), assocType);
        if (assoc != null) {
            return assoc.getAssoc();
        }

        CatalogAssocData catalogAssoc = new CatalogAssocData();

        catalogAssoc.setCatalogId(catalog.getCatalogId());
        catalogAssoc.setBusEntityId(busEntity.getBusEntityId());
        catalogAssoc.setCatalogAssocCd(assocType);

        return  create(catalogAssoc);

    }

    @Override
    public CatalogAssocView findEntityCatalog(Long entityId, String catalogAssoc) throws IllegalDataStateException {
    	String catalogTypeCd = catalogAssoc.equals(RefCodeNames.CATALOG_ASSOC_CD.CATALOG_STORE) ?  RefCodeNames.CATALOG_TYPE_CD.STORE
    			: catalogAssoc.equals(RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT) ? RefCodeNames.CATALOG_TYPE_CD.ACCOUNT 
    			: RefCodeNames.CATALOG_TYPE_CD.SHOPPING;
    					

        Query query = em.createQuery("Select " +
                "new com.espendwise.manta.model.view.CatalogAssocView(catalog, assoc) " +
                "from CatalogData catalog, CatalogAssocData assoc " +
                " where assoc.busEntityId = (:entityId)  " +
                "  and assoc.catalogAssocCd = (:catalogAssoc) " +
                "  and assoc.catalogId = catalog.catalogId" +
                "  and catalog.catalogTypeCd = (:catalogTypeCd)"
        );


        query.setParameter("entityId", entityId);
        query.setParameter("catalogAssoc", catalogAssoc);
        query.setParameter("catalogTypeCd", catalogTypeCd);

        List<CatalogAssocView> catalogs = query.getResultList();

        if (catalogs.size() > 1) {
            throw DatabaseError.multipleEntityCatalogConfiguration(
                    entityId,
                    catalogAssoc
            );
        }

        return catalogs.isEmpty() ? null : catalogs.get(0);

    }

    @Override
    public void removeCatalogAssoc(Long entityId, String catalogAssoc) {

        if (Utility.isSet(entityId) && Utility.isSet(catalogAssoc)) {

            Query query = em.createQuery("Select  assoc  from CatalogAssocData assoc " +
                    " where assoc.busEntityId = (:entityId)  " +
                    "  and assoc.catalogAssocCd = (:catalogAssoc)");

            query.setParameter("entityId", entityId);
            query.setParameter("catalogAssoc", catalogAssoc);

            List<CatalogAssocData> assoc = query.getResultList();
            for (CatalogAssocData x : assoc) {
                em.remove(x);
            }

        }

    }
    
    @Override
    public List<CatalogView> findCatalogsByCriteria(CatalogSearchCriteria criteria) {
    /*
     select distinct catalog1.* from clw_catalog catalog1


           inner join clw_catalog_assoc catalogAccount on catalog1.catalog_id = catalogAccount.catalog_id
           inner join clw_bus_entity_assoc siteAccount on catalogAccount.bus_entity_id = siteAccount.bus_entity2_id

           inner join clw_catalog_assoc catalogSite on catalog1.catalog_id = catalogSite.catalog_id

           where
           catalog1.catalog_type_cd = 'SHOPPING'

           and siteAccount.bus_entity1_id = 556
           and siteAccount.bus_entity_assoc_cd = 'SITE OF ACCOUNT'
           and catalogAccount.catalog_assoc_cd = 'CATALOG_ACCOUNT'

           and catalogSite.bus_entity_id = siteAccount.bus_entity1_id
           and catalogSite.catalog_assoc_cd = 'CATALOG_SITE';
     */
        List<CatalogView> resultList = new ArrayList<CatalogView>();
        if (Utility.isSet(criteria.getSiteId())) {
            StringBuilder baseQuery = new StringBuilder();
                    baseQuery.append("Select distinct new com.espendwise.manta.model.view.CatalogView (" +
                            " catalog.catalogId," +
                            " catalog.shortDesc," +
                            " catalog.catalogStatusCd )" +
                            " from CatalogFullEntity catalog");
                    baseQuery.append(" inner join catalog.catalogAssocs catalogAccount");
                    baseQuery.append(" inner join catalogAccount.busEntityId account");
                    baseQuery.append(" inner join account.busEntityAssocsForBusEntity2Id siteAccount");
                    baseQuery.append(" inner join siteAccount.busEntity1Id site");

                    if (criteria.getConfiguredOnly()) {
                        baseQuery.append(" inner join catalog.catalogAssocs catalogSite");
                    }

                    baseQuery.append(" where catalog.catalogTypeCd = (:catalogTypeCd)" +
                                     " and site.busEntityId.busEntityId = (:siteId)" +
                                     " and siteAccount.busEntityAssocCd = (:siteOfAccount)" +
                                     " and catalogAccount.catalogAssocCd = (:catalogAccount)");

                    if (criteria.getConfiguredOnly()) {
                        baseQuery.append(" and catalogSite.busEntityId.busEntityId = site.busEntityId" + 
                                         " and catalogSite.catalogAssocCd = (:catalogSite)");
                    }

                    baseQuery.append((Utility.isSet(criteria.getCatalogName()) ? " and Upper(catalog.shortDesc) like (:catalogName)" : "") +
                                     (Utility.isTrue(criteria.getActiveOnly()) ? " and catalog.catalogStatusCd <> (:inactiveStatusCd)" : ""));

            String query = baseQuery.toString();
            Query q = em.createQuery(query);
            
            q.setParameter("catalogTypeCd", RefCodeNames.CATALOG_TYPE_CD.SHOPPING);
            q.setParameter("siteId", criteria.getSiteId());
            q.setParameter("siteOfAccount", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            q.setParameter("catalogAccount", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT);
            
            if (criteria.getConfiguredOnly()) {
                q.setParameter("catalogSite", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE);
            }

            if (Utility.isSet(criteria.getCatalogName())) {
                    q.setParameter("catalogName",
                        QueryHelp.toFilterValue(
                                criteria.getCatalogName().toUpperCase(),
                                criteria.getCatalogNameFilterType())
                );
            }

            if (Utility.isTrue(criteria.getActiveOnly())) {
                q.setParameter("inactiveStatusCd", RefCodeNames.CATALOG_STATUS_CD.INACTIVE);
            }

            if (Utility.isSet(criteria.getLimit())) {
                q.setMaxResults(criteria.getLimit());
            }

            resultList = q.getResultList();
        }
        
        return resultList;
    }
    
    
    @Override
    public List<CatalogListView> findCatalogsByCriteria(CatalogListViewCriteria criteria) {
        List<CatalogListView> resultList = new ArrayList<CatalogListView>();

        StringBuilder baseQuery = new StringBuilder();
        baseQuery.append(
            " Select distinct new com.espendwise.manta.model.view.CatalogListView (" +
            "   catalog.catalogId," +
            "   catalog.shortDesc," +
            "   catalog.catalogTypeCd," +
            "   catalog.catalogStatusCd )" +
            " from CatalogFullEntity catalog " );

        if (Utility.isSet(criteria.getCostCenterId()) && criteria.getConfiguredOnly()) {
            baseQuery.append(" inner join catalog.costCenterAssocs costCenterCatalog");
            baseQuery.append(" inner join costCenterCatalog.costCenterId costCenter");
        }

        if (RefCodeNames.CATALOG_TYPE_CD.STORE.equals(criteria.getCatalogType()) ||
            RefCodeNames.CATALOG_TYPE_CD.ACCOUNT.equals(criteria.getCatalogType())) {
            baseQuery.append(" inner join catalog.catalogAssocs catalogStore");
            baseQuery.append(" inner join catalogStore.busEntityId store");
        } else {
            baseQuery.append(" inner join catalog.catalogAssocs catalogAccount");
            baseQuery.append(" inner join catalogAccount.busEntityId account");
            baseQuery.append(" inner join account.busEntityAssocsForBusEntity1Id storeAccount");
            baseQuery.append(" inner join storeAccount.busEntity2Id store");
        }

        baseQuery.append(" where catalog.catalogTypeCd = (:catalogTypeCd)" +
                                     " and store.busEntityId.busEntityId = (:storeId)") ;
        if (!RefCodeNames.CATALOG_TYPE_CD.STORE.equals(criteria.getCatalogType()) &&
            !RefCodeNames.CATALOG_TYPE_CD.ACCOUNT.equals(criteria.getCatalogType())) {
        	      baseQuery.append(  " and storeAccount.busEntityAssocCd = (:accountOfStore)" +
                                     " and catalogAccount.catalogAssocCd = (:catalogAccount)");
        }

        if (Utility.isSet(criteria.getCostCenterId()) && criteria.getConfiguredOnly()) {
            baseQuery.append(" and costCenter.costCenterId = (:costCenterId)" +
                             " and costCenterCatalog.costCenterAssocCd = (:costCenterAssocCd)");
        }

        if (Utility.isSet(criteria.getCatalogId())) {
            baseQuery.append(" and catalog.catalogId = " + criteria.getCatalogId());
        }
        
        baseQuery.append((Utility.isSet(criteria.getCatalogName()) ? " and Upper(catalog.shortDesc) like (:catalogName)" : "") +
                          (Utility.isTrue(criteria.getActiveOnly()) ? " and catalog.catalogStatusCd <> (:inactiveStatusCd)" : ""));


        String query = baseQuery.toString();
        Query q = em.createQuery(query);

        q.setParameter("catalogTypeCd", criteria.getCatalogType());
        q.setParameter("storeId", criteria.getStoreId());
        if (!RefCodeNames.CATALOG_TYPE_CD.STORE.equals(criteria.getCatalogType()) &&
            !RefCodeNames.CATALOG_TYPE_CD.ACCOUNT.equals(criteria.getCatalogType()) ){
        	q.setParameter("accountOfStore", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        	q.setParameter("catalogAccount", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT);
        }

        if (Utility.isSet(criteria.getCatalogName())) {
                    q.setParameter("catalogName",
                        QueryHelp.toFilterValue(
                                criteria.getCatalogName().toUpperCase(),
                                criteria.getCatalogNameFilterType())
                );
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
                q.setParameter("inactiveStatusCd", RefCodeNames.CATALOG_STATUS_CD.INACTIVE);
        }

        if (Utility.isSet(criteria.getCostCenterId()) && criteria.getConfiguredOnly()) {
            q.setParameter("costCenterId", criteria.getCostCenterId()) ;
            q.setParameter("costCenterAssocCd", RefCodeNames.COST_CENTER_ASSOC_CD.COST_CENTER_ACCOUNT_CATALOG);
        }

        if (Utility.isSet(criteria.getLimit())) {
                q.setMaxResults(criteria.getLimit());
        }

        resultList = q.getResultList();
        return resultList;
    }
    
    @Override
    public List<CatalogStructureListView> findCatalogStructuresByCriteria(CatalogStructureListViewCriteria criteria) {
        List<CatalogStructureListView> resultList = new ArrayList<CatalogStructureListView>();

        StringBuilder baseQuery = new StringBuilder();
        baseQuery.append(
            " Select new com.espendwise.manta.model.view.CatalogStructureListView (" +
             "   catalogStructure.catalogStructureId" +
            //"   catalogStructure.catalogStructureId," +
            //"   catalogStructure.catalogId," +
            //"   catalogStructure.busEntityId," +
            //"   catalogStructure.catalogStructureCd," +
            //"   catalogStructure.itemId," +
            //"   catalogStructure.customerSkuNum," +
            ////"   catalogStructure.shortDesc," +
            //"   catalogStructure.effDate," +
            //"   catalogStructure.expDate," +
            //"   catalogStructure.statusCd," +
            //"   catalogStructure.costCenterId," +
            //"   catalogStructure.taxExempt," +
            //"   catalogStructure.itemGroupId," +
            //"   catalogStructure.specialPermission," +
            //"   catalogStructure.sortOrder" +
            " ) from CatalogStructureData catalogStructure " );

        if (criteria.getBusEntityId() != null) {
            baseQuery.append(", BusEntityData busEntity");
        }

        if (Utility.isSet(criteria.getCatalogType())) {
        	baseQuery.append(", CatalogData catalog");
        }
        
        baseQuery.append(" where ");
        boolean andRequired = false;

        if (criteria.getBusEntityId() != null) {
        	if (andRequired) {
        		baseQuery.append(" and ");
        	}
            baseQuery.append("catalogStructure.busEntityId = (:busEntityId)");
            baseQuery.append(" and catalogStructure.busEntityId = busEntity.busEntityId");
            baseQuery.append(" and upper(busEntity.busEntityStatusCd) <> '");
            baseQuery.append(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE);
            baseQuery.append("'");
            andRequired = true;
        }

        if (Utility.isSet(criteria.getCatalogType())) {
        	if (andRequired) {
        		baseQuery.append(" and ");
        	}
            baseQuery.append("catalogStructure.catalogId = catalog.catalogId");
            baseQuery.append(" and catalog.catalogTypeCd = (:catalogTypeCd)");
            andRequired = true;
        }

        String query = baseQuery.toString();
        Query q = em.createQuery(query);

        if (criteria.getBusEntityId() != null) {
                q.setParameter("busEntityId", criteria.getBusEntityId());
        }

        if (Utility.isSet(criteria.getCatalogType())) {
            q.setParameter("catalogTypeCd", criteria.getCatalogType()) ;
        }

        if (Utility.isSet(criteria.getLimit())) {
                q.setMaxResults(criteria.getLimit());
        }

        resultList = q.getResultList();
        return resultList;
    }

    
    @Override
    public List<ItemView> findCatalogCategories (List<Long> pCatalogIds){

        logger.info("findCatalogCategories()  BEGIN. =====> catalogIds :" + pCatalogIds);
    	List<ItemView> resultList = new ArrayList<ItemView>();
        StringBuilder baseQuery= new StringBuilder(
    		 " Select new com.espendwise.manta.model.view.ItemView("
    	                + "item.itemId, "
    	                + "categories.catalogId.catalogId, "
    	                + "item.shortDesc, "
    	                + "item.longDesc,"
    	                + "item.itemStatusCd,"
    	                + "item.itemTypeCd," 
    	                + "coalesce(categories.customerSkuNum, str(item.skuNum))) "  +
    	                
    		 " from ItemFullEntity item" +
    		 " inner join item.catalogStructuresForItemId categories"	+
             " where categories.catalogId.catalogId in (:catalogIds)" +
             "   and categories.catalogStructureCd = (:catalogStructureCd)" );
        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("catalogIds", pCatalogIds);
        q.setParameter("catalogStructureCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_CATEGORY);
        resultList = (List<ItemView>)q.getResultList();
        logger.info("findCatalogCategories()  END. =====> item categories :" + resultList.size());
	
        return resultList;
    }
    
    @Override
    public List<ItemData> findProducts (Long pStoreId, List<Long> pSkuNumbers){
        logger.info("findProducts()  BEGIN. =====> skuNumbers :" + pSkuNumbers);
        Long catalogId = null;
        if (pStoreId != null && pStoreId.intValue()>0){
//        	catalogId = findEntityCatalog(pStoreId, RefCodeNames.CATALOG_ASSOC_CD.CATALOG_STORE)
//        				.getCatalog()
//        				.getCatalogId();
        }
        
        StringBuilder baseQuery= new StringBuilder(
       			"Select item from ItemData item" +
                " where item.skuNum in (:skuNumbers)" );
       Query q = em.createQuery(baseQuery.toString());

       q.setParameter("skuNumbers", pSkuNumbers);
           
       List<ItemData> result = (List<ItemData>) q.getResultList();

        
        logger.info("findProducts()  END.  ");
        return result;
    }
    

    
    @Override
    public List<ItemView> findItems (ProductListViewCriteria criteria){
        logger.info("findItems()  BEGIN. =====> skuNumbers :" + criteria.getItemIds());
        
        StringBuilder baseQuery= new StringBuilder(
       		 " Select distinct new com.espendwise.manta.model.view.ItemView("
                + "   item.itemId, "
                + "   catalogStructure.catalogId.catalogId, "
                + "   item.shortDesc, "
                + "   item.longDesc,"
                + "   item.itemStatusCd, "
                + "   item.itemTypeCd, " 
                + "   coalesce(catalogStructure.customerSkuNum, str(item.skuNum)) )  " 
		     	+ " from ItemFullEntity item" 
		        + " inner join item.catalogStructuresForItemId catalogStructure"	);
           baseQuery.append(" where item.itemId in (:itemIds)");
           baseQuery.append(" and catalogStructure.catalogId.catalogId = (:catalogId)");

           Query q = em.createQuery(baseQuery.toString());

       q.setParameter("itemIds", criteria.getItemIds());
       q.setParameter("catalogId", criteria.getCatalogId());
           
       List<ItemView> result = (List<ItemView>) q.getResultList();

        
        logger.info("findProducts()  END.  ");
        return result;
    }

    @Override
    public List<ProductListView> findProductsByCriteria (ProductListViewCriteria criteria){
        logger.info("findProductsByCriteria()  BEGIN. catalogId = " + criteria.getCatalogId());
        Long catalogId = null;
        //List<ProductListView> resultList = new ArrayList<ProductListView>();

        StringBuilder baseQuery = new StringBuilder();
        
        StringBuilder manufWhere = new StringBuilder(" with manufItemMapping.itemMappingCd = 'ITEM_MANUFACTURER'");
        StringBuilder distrWhere = new StringBuilder(" with distItemMapping.itemMappingCd = 'ITEM_DISTRIBUTOR'");
        StringBuilder categWhere = new StringBuilder();
        boolean distSKUfilter = Utility.isSet(criteria.getItemSku())  && RefCodeNames.SKU_TYPE_CD.DISTRIBUTOR.equalsIgnoreCase(criteria.getItemSkuFilterType());
        if (Utility.isSet(criteria.getItemCategory())) {
    		categWhere.append(" with upper(categ.shortDesc) like (:category)");
        }
        if (Utility.isSet(criteria.getItemSku())) {
        	if (criteria.getItemSkuFilterType().equalsIgnoreCase(RefCodeNames.SKU_TYPE_CD.MANUFACTURER)){
        		manufWhere.append(" and upper(manufItemMapping.itemNum) like (:sku)");
        	} else if (criteria.getItemSkuFilterType().equalsIgnoreCase(RefCodeNames.SKU_TYPE_CD.DISTRIBUTOR)) {
        		distrWhere.append(" and upper(distItemMapping.itemNum) like (:sku)");
        	}
        }

        baseQuery.append(
            " Select distinct new com.espendwise.manta.model.view.ProductListView (" +
             "   item.itemId, " +
             "   item.itemId, " +  

             "   catalogStructure.catalogId.catalogId, " +
             "   categ.itemId, " +
             "   manuf.busEntityId, ");
        Boolean doSubstitute = criteria.getSubstituteItemSKUbyCustSKU();
             if (Utility.isSet(doSubstitute) && !Utility.isTrue(doSubstitute)) {
                baseQuery.append("   str(item.skuNum), ");
             } else {
                baseQuery.append("   coalesce(catalogStructure.customerSkuNum, str(item.skuNum)), ");
             }
            
             // Added this condition to resolve the issue MANTA-878             
             
             if (distSKUfilter) { 
                 baseQuery.append("   item.shortDesc, " + 
                         "   uom.value, " + 
                         "   pack.value, " + 
                         "   categ.shortDesc, " +
                         "   manuf.shortDesc, " +
                         "   manufItemMapping.itemNum , " + 
                         "   distItemMapping.itemNum , " +
                         "   catalogStructure.busEntityId.busEntityId , " + // distributorId from catalogStructure table
                         "   distItemMapping.busEntityId.busEntityId , " + // distributorId from itemMapping table
                         "   item.itemStatusCd " +
                    	" ) from ItemFullEntity item ");
                 }else{
                 	
                 	 baseQuery.append("   item.shortDesc, " + 
                              "   uom.value, " + 
                              "   pack.value, " + 
                              "   categ.shortDesc, " +
                              "   manuf.shortDesc, " +
                              "   manufItemMapping.itemNum , " + 
                              "   manufItemMapping.itemNum  , " +  // duplicate
                              //"   distItemMapping.itemNum , " +
                              "   catalogStructure.busEntityId.busEntityId , " + // distributorId from catalogStructure table
                              "  catalogStructure.busEntityId.busEntityId , " + // duplicate
                              //"   distItemMapping.busEntityId.busEntityId , " + // distributorId from itemMapping table
                              "   item.itemStatusCd " +
                         	" ) from ItemFullEntity item ");
                 }
                 
        baseQuery.append(" inner join item.catalogStructuresForItemId catalogStructure ");
        baseQuery.append(" left outer join item.itemMetas uom with uom.nameValue = 'UOM'");
        baseQuery.append(" left outer join item.itemMetas pack with pack.nameValue = 'PACK'");
        
        baseQuery.append(" left outer join item.itemAssocsForItem1Id itemAssoc with itemAssoc.itemAssocCd = 'PRODUCT_PARENT_CATEGORY' and itemAssoc.catalogId.catalogId = (:catalogId)");
        baseQuery.append(" left outer join itemAssoc.item2Id categ " + categWhere.toString());

        baseQuery.append(" left outer join item.itemMappings manufItemMapping " + manufWhere.toString());
        baseQuery.append(" left outer join manufItemMapping.busEntityId manuf");
 
        if (distSKUfilter) {
        	baseQuery.append(" left outer join item.itemMappings distItemMapping " + distrWhere.toString());
        	baseQuery.append(" left outer join distItemMapping.busEntityId dist");
        }
        
        
        baseQuery.append(" where ");
        baseQuery.append(" catalogStructure.catalogId.catalogId = (:catalogId)");
        baseQuery.append(" and catalogStructure.catalogStructureCd = '"+RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT+"'");
        boolean andRequired = true;
       
        if (Utility.isTrue(criteria.getActiveOnly())){
        	baseQuery.append(((andRequired) ? " and " : "")+ "item.itemStatusCd = (:activeStatusCd)");
        	andRequired = true;
        }
      
        if (Utility.isSet(criteria.getItemId())) {
    		baseQuery.append(((andRequired) ? " and " : "")+ "upper(item.itemId) = (:itemId)");
        	andRequired = true;
        }
        if (Utility.isSet(criteria.getItemName() ) ) {
    		baseQuery.append(((andRequired) ? " and " : "")+ "upper(item.shortDesc) like (:itemName)");
        	andRequired = true;
        }
        
        if (Utility.isSet(criteria.getItemCategory())) {
        	baseQuery.append(" and upper(categ.shortDesc) like (:category)");
      	}
        
        if (Utility.isSet(criteria.getItemSku())) {
        	if (criteria.getItemSkuFilterType().equalsIgnoreCase(RefCodeNames.SKU_TYPE_CD.STORE)){

        		Boolean doSubstituteItemSku = criteria.getSubstituteItemSKUbyCustSKU();
                
        		if (Utility.isSet(doSubstituteItemSku) && !Utility.isTrue(doSubstituteItemSku)) {
                    baseQuery.append(((andRequired) ? " and " : "") + "str(item.skuNum) like (:sku) ");
                } else {
                    baseQuery.append(((andRequired) ? " and " : "") + "coalesce(catalogStructure.customerSkuNum, str(item.skuNum)) like (:sku) ");
               }
        		//baseQuery.append(((andRequired) ? " and " : "")+ "coalesce(catalogStructure.customerSkuNum, str(item.skuNum)) like (:sku)");
            	andRequired = true;
        	} else if (criteria.getItemSkuFilterType().equalsIgnoreCase(RefCodeNames.SKU_TYPE_CD.MANUFACTURER)){
        		baseQuery.append(" and upper(manufItemMapping.itemNum) like (:sku)");
        	} else if (criteria.getItemSkuFilterType().equalsIgnoreCase(RefCodeNames.SKU_TYPE_CD.DISTRIBUTOR)) {
        		baseQuery.append(" and upper(distItemMapping.itemNum) like (:sku)");
        	}
        }
        /* Formatted on 10/19/2012 6:06:55 PM (QP5 v5.136.908.31019) */
            				
 /*       SELECT DISTINCT i.item_id,
                        i.item_status_cd,
                        cstr.customer_sku_num AS sku_num,
                        i.short_desc,
                        uom.clw_value uom,
                        isize.clw_value isize,
                        pack.clw_value pack,
                        color.clw_value color,
                        categ.short_desc category,
                        categ.item_id category_id,
                        mfim.item_num manuf_sku,
                        mf.bus_entity_id manuf_id,
                        mf.short_desc manuf
          FROM clw_item i,
               clw_catalog_structure cstr,
               clw_item_meta uom,
               clw_item_meta color,
               clw_item_meta isize,
               clw_item_meta pack,
               clw_item_assoc ia,
               clw_item categ,
               clw_item_mapping mfim,
               clw_bus_entity mf
         WHERE     i.item_id = cstr.item_id
               AND cstr.catalog_id = 9647
               AND cstr.catalog_structure_cd = 'CATALOG_PRODUCT'
               AND i.item_id = uom.item_id(+)
               AND uom.name_value(+) = 'UOM'
               AND i.item_id = isize.item_id(+)
               AND isize.name_value(+) = 'SIZE'
               AND i.item_id = pack.item_id(+)
               AND pack.name_value(+) = 'PACK'
               AND i.item_id = color.item_id(+)
               AND color.name_value(+) = 'COLOR'
               
               AND i.item_id = ia.item1_id(+)
               AND ia.item2_id = categ.item_id(+)  
               AND ia.item_assoc_cd(+) = 'PRODUCT_PARENT_CATEGORY'
               AND ia.catalog_id(+) = 9647
               
               AND mfim.item_id(+) = i.item_id
               AND mfim.item_mapping_cd(+) = 'ITEM_MANUFACTURER'
               AND mf.bus_entity_id(+) = mfim.bus_entity_id
               AND UPPER (cstr.customer_sku_num) = '1008795'
               AND UPPER (i.short_desc) LIKE '%BOOK%'
               AND UPPER (categ.short_desc) LIKE '%PEOPLE%'
               AND UPPER (i.item_status_cd) = 'ACTIVE'
      ORDER BY cstr.customer_sku_num
*/        
             
//        boolean andRequired = false;

        String query = baseQuery.toString();
        Query q = em.createQuery(query);      
        q.setParameter("catalogId", criteria.getCatalogId());
 
        if (Utility.isTrue(criteria.getActiveOnly())){
        	q.setParameter("activeStatusCd", "ACTIVE");
        }
        if (criteria.getItemId() != null) {
                q.setParameter("itemId", new Long(criteria.getItemId()));
        }

        if (Utility.isSet(criteria.getItemCategory())) {
            q.setParameter("category",
                    QueryHelp.toFilterValue(
                            criteria.getItemCategory().toUpperCase(),
                            criteria.getItemCategoryFilterType())
            );
        }
        if (criteria.getItemSku() != null) {
                q.setParameter("sku",
                        QueryHelp.toFilterValue(
                                criteria.getItemSku().toUpperCase(),
                                criteria.getItemSkuFilterSubType())
                );

	    }
	
	    if (Utility.isSet(criteria.getItemName())) {
            q.setParameter("itemName",
                    QueryHelp.toFilterValue(
                            criteria.getItemName().toUpperCase(),
                            criteria.getItemNameFilterType())
            );
	    }
	 
        if (Utility.isSet(criteria.getLimit())) {
                q.setMaxResults(criteria.getLimit());
        }
        List<ProductListView> resultList = q.getResultList();

        logger.info("findProductsByCriteria() => fetched : " + resultList.size() + " rows");
        logger.info("findProductsByCriteria() => clean up duplicates");


        Map<Long, ProductListView> productMap = new Hashtable<Long, ProductListView>();
        if (Utility.isSet(resultList)) {
            for (ProductListView product : resultList) {
            	
            	//logger.info("in loop : " +product.getItemManufacturerSku() + " DistributorSKU "+ product.getItemDistributorSku() );
            	
                Long catalogDistId = product.getCatalogDistId();
                Long itemMappingDistId = product.getItemMappingDistId();
                if (!productMap.containsKey(product.getItemId())) {
                                      
                    if (!distSKUfilter && product.getItemManufacturerSku().equals(product.getItemDistributorSku())) {
                        product.setItemDistributorSku(null);
                    }
                    
                    if (!distSKUfilter && !(catalogDistId != null && catalogDistId.equals(itemMappingDistId))) {
                        product.setItemDistributorSku(null);
                    }
                    
                    if (!(product.getItemDistributorSku() == null && distSKUfilter)) {
                        productMap.put(product.getItemId(), product);
                    }
                } else {
                    ProductListView storedProduct = productMap.get(product.getItemId());
                    if (storedProduct.getItemDistributorSku() == null) {
                        if (storedProduct.getCatalogDistId() != null && storedProduct.getCatalogDistId().equals(itemMappingDistId)) {
                            storedProduct.setItemDistributorSku(product.getItemDistributorSku());
                        }
                    }
                }
            }
            resultList = new ArrayList(productMap.values());
            WebSort.sort(resultList, "itemSku");
        }

        logger.info("findProductsByCriteria() => final result: " + resultList.size() + " rows");
        
        return resultList;
    }
    
    @Override
    public List<ServiceListView> findServicesByCriteria(ServiceListViewCriteria criteria) {
        List<ServiceListView> services = new ArrayList<ServiceListView>();
        
        Long catalogId = null;
        if (criteria.getContractId() != null) {
            ContractData contract = em.find(ContractData.class, criteria.getContractId());
            if (contract != null) {
                catalogId = contract.getCatalogId();
            }
        }
        
        StringBuilder baseQuery = new StringBuilder("Select DISTINCT new com.espendwise.manta.model.view.ServiceListView");
                    baseQuery.append("(item.itemId,");
                    baseQuery.append(" item.shortDesc,");
                    baseQuery.append(" item.itemStatusCd)");
                    baseQuery.append(" FROM ItemData item");
                    baseQuery.append(" WHERE item.itemTypeCd = (:itemTypeCd)");

        if (Utility.isSet(criteria.getServiceId())) {
            baseQuery.append(" AND item.itemId = (:itemId)");
        }

        if (Utility.isSet(criteria.getServiceName())) {
            if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getServiceNameFilterType())) {
                baseQuery.append(" AND UPPER(item.shortDesc) like '")
                         .append(QueryHelp.startWith(criteria.getServiceName().toUpperCase()))
                         .append("'");
            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getServiceNameFilterType())) {
                baseQuery.append(" AND UPPER(item.shortDesc) like '")
                         .append(QueryHelp.contains(criteria.getServiceName().toUpperCase()))
                         .append("'");
            }
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" AND item.itemStatusCd <> ")
                    .append(QueryHelp.toQuoted(RefCodeNames.ITEM_STATUS_CD.INACTIVE));
        }

        if (criteria.getStoreId() != null) {
            baseQuery.append(" AND item.itemId IN (Select catalogStructure.itemId FROM CatalogStructureData catalogStructure, CatalogData catalog, CatalogAssocData catalogToStore");
            baseQuery.append(" WHERE catalog.catalogTypeCd = (:catalogTypeCd)");
            baseQuery.append(" AND catalog.catalogStatusCd = (:catalogStatusCd)");
            if (catalogId != null) {
                baseQuery.append(" AND catalog.catalogId = (:catalogId)");
            }
            baseQuery.append(" AND catalog.catalogId = catalogToStore.catalogId");
            baseQuery.append(" AND catalogToStore.busEntityId = (:storeId)");
            baseQuery.append(" AND catalogToStore.catalogAssocCd = (:catalogAssocCd)");
            baseQuery.append(" AND catalogStructure.catalogId = catalogToStore.catalogId)");
        }       

        Query query = em.createQuery(baseQuery.toString());

        query.setParameter("itemTypeCd", RefCodeNames.ITEM_TYPE_CD.SERVICE);
        if (Utility.isSet(criteria.getServiceId())) {
            query.setParameter("itemId", criteria.getServiceId());
        }
        if (Utility.isSet(criteria.getStoreId())) {
            query.setParameter("storeId", criteria.getStoreId());
            query.setParameter("catalogTypeCd", RefCodeNames.CATALOG_TYPE_CD.STORE);
            query.setParameter("catalogStatusCd", RefCodeNames.CATALOG_STATUS_CD.ACTIVE);
            if (catalogId != null) {
                query.setParameter("catalogId", catalogId);
            }
            query.setParameter("catalogAssocCd", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_STORE);
        }

        if (criteria.getLimit() != null) {
            query.setMaxResults(criteria.getLimit());
        }

        services = (List<ServiceListView>) query.getResultList();
        
        if (Utility.isSet(services) && criteria.getSiteId() != null) {
            baseQuery = new StringBuilder("Select object(CatalogStructureData) from CatalogStructureData catalogStructure, CatalogAssocData catalogToSite");
                baseQuery.append(" WHERE catalogStructure.catalogStructureCd = (:catalogServiceCd)");
                baseQuery.append(" AND catalogStructure.catalogId = catalogToSite.catalogId");
                baseQuery.append(" AND catalogToSite.catalogAssocCd = (:catalogToSiteAssocCd)");
                baseQuery.append(" AND catalogToSite.busEntityId = (:siteId)");
                if (catalogId != null) {
                    baseQuery.append(" AND catalogStructure.catalogId = (:catalogId)");
                }

            query = em.createQuery(baseQuery.toString());
            query.setParameter("catalogServiceCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_SERVICE);
            query.setParameter("catalogToSiteAssocCd", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE);
            query.setParameter("siteId", criteria.getSiteId());
            if (catalogId != null) {
                query.setParameter("catalogId", catalogId);
            }

            List<CatalogStructureData> catalogStructures = (List<CatalogStructureData>) query.getResultList();
            if (Utility.isSet(catalogStructures)) {
                Set<Long> siteFilterServiceIds = new HashSet<Long>();
                for (CatalogStructureData catalogStructure: catalogStructures) {
                    siteFilterServiceIds.add(catalogStructure.getItemId());
                }
                
                List<ServiceListView> filteredServices = new ArrayList<ServiceListView>();
                for (ServiceListView service : services) {
                    if (siteFilterServiceIds.contains(service.getServiceId())) {
                        filteredServices.add(service);
                    }
                }
                services = filteredServices;
            } else {
                services = new ArrayList<ServiceListView>();
            }
        }
        
        return services;
    }
    @Override
    public List<CatalogData> findCatalogs ( Long pStoreId, List<Long> pCatalogIds){
        logger.info("findCatalogs()  BEGIN. =====> catalogIds :" + pCatalogIds + ", storeId =" + pStoreId);
        
        StringBuilder baseQuery= new StringBuilder(
       			"Select catalog from CatalogData catalog, CatalogAssocData catalogAssoc" +
                " where catalog.catalogId = catalogAssoc.catalogId " +
                " and catalogAssoc.busEntityId =(:storeId) " +
                " and catalogAssoc.catalogAssocCd =(:assocCd) " +
                " and catalog.catalogId in (:catalogIds)" +
                " and catalog.catalogStatusCd = (:status)" );
       Query q = em.createQuery(baseQuery.toString());

       q.setParameter("catalogIds", pCatalogIds);
       q.setParameter("storeId", pStoreId);
       q.setParameter("status", RefCodeNames.CATALOG_STATUS_CD.ACTIVE);
       q.setParameter("assocCd", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_STORE);
           
       List<CatalogData> result = (List<CatalogData>) q.getResultList();

        
        logger.info("findCatalogs()  END.  ");
        return result;
    }
    @Override
    public List<ItemLoaderView> findItems (List<Long> catalogIds){
        logger.info("findItems()  BEGIN. =====> catalogIds :" + catalogIds);
        
        StringBuilder baseQuery= new StringBuilder(
          		 " Select distinct new com.espendwise.manta.model.view.ItemLoaderView("
                   + "   item.itemId, "
        		   + "   catalogStructure.catalogId.catalogId, "
                   + "   item.itemStatusCd, "
                   + "   item.itemTypeCd, " 
                   //+ "   coalesce(catalogStructure.customerSkuNum, str(item.skuNum)) ,  " 
                   + "   str(item.skuNum) ,  " 
                   + "   distItemMapping.busEntityId.busEntityId, " 
                   + "   distItemMapping.itemNum, " 
                   + "   distItemMapping.itemUom )" 
   		     	+ " from ItemFullEntity item" 
   		        + " inner join item.catalogStructuresForItemId catalogStructure with catalogStructure.catalogStructureCd='CATALOG_PRODUCT'"
   		        + " left outer join item.itemMappings distItemMapping with distItemMapping.itemMappingCd = 'ITEM_DISTRIBUTOR' " 
        		+ " left outer join item.itemAssocsForItem1Id itemAssoc with itemAssoc.itemAssocCd = 'PRODUCT_PARENT_CATEGORY' and itemAssoc.catalogId.catalogId in (:catalogIds)");

        baseQuery.append(" where catalogStructure.catalogId.catalogId in (:catalogIds)" +
        		" and item.itemTypeCd = 'PRODUCT'" +
        		" and nvl(distItemMapping.busEntityId.busEntityId, 0) =nvl(catalogStructure.busEntityId.busEntityId, nvl(distItemMapping.busEntityId.busEntityId,0))");
 
           Query q = em.createQuery(baseQuery.toString());

       q.setParameter("catalogIds", catalogIds);
           
       List<ItemLoaderView> result = (List<ItemLoaderView>) q.getResultList();

        
        logger.info("findItems()  END.  ");
        return result;
    }
    
    
    @Override
    public List<CatalogStructureData> findCatalogStructuresUsingCriteria(CatalogStructureListViewCriteria criteria) {
    	
    	 List<CatalogStructureData> resultList = new ArrayList<CatalogStructureData>();

         Query q = em.createQuery("Select  catalogStructure  from CatalogStructureData catalogStructure, CatalogData catalog where catalog.catalogId= catalogStructure.catalogId " +
                 " and catalogStructure.itemId = (:itemId) and catalog.catalogTypeCd != '"+RefCodeNames.CATALOG_TYPE_CD.STORE+"' and  catalog.catalogTypeCd != '"+RefCodeNames.CATALOG_TYPE_CD.SYSTEM+"' ");
         
         
         if(criteria.getItemId() != null){
         	//baseQuery.append("catalogStructure.itemId = (:itemId)");
         	 q.setParameter("itemId", criteria.getItemId());
         }
         resultList = q.getResultList();
         return resultList;
    	
    }
    

    @Override
    public List<CatalogData> findCatalogDataUsingCriteria(CatalogListViewCriteria criteria) {
    	
    	List<CatalogData> resultList = new ArrayList<CatalogData>();

        Query q = em.createQuery("Select  catalogData  from CatalogData catalogStructure  " +
                " where catalogData.catalogTypeCd != (:catalogTypeCd) ");
        
        
        if(criteria != null){
        	//baseQuery.append("catalogStructure.itemId = (:itemId)");
        	 q.setParameter("catalogTypeCd", criteria.getCatalogTypeCd());
        }
        resultList = q.getResultList();
        return resultList;
    }
    
}





