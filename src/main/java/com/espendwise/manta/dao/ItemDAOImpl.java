package com.espendwise.manta.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.ItemIdentView;
import com.espendwise.manta.model.view.ItemListView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.ItemListViewCriteria;

import java.util.List;
import java.util.ArrayList;

public class ItemDAOImpl extends DAOImpl implements ItemDAO{

    private static final Logger logger = Logger.getLogger(ItemDAOImpl.class);

    static public final int SKU_MINIMUM = 10000;

    public ItemDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public List<ItemListView> findItemsByCriteria(ItemListViewCriteria criteria) {

        logger.info("findItemsByCriteria()=> BEGIN, criteria: "+criteria);

        StringBuilder categWhere = new StringBuilder();
        StringBuilder manufWhere = new StringBuilder(" with manufItemMapping.itemMappingCd = 'ITEM_MANUFACTURER'");
        StringBuilder distrWhere = new StringBuilder(" with distItemMapping.itemMappingCd = 'ITEM_DISTRIBUTOR'");
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

        List<Long> itemIds = new ArrayList<Long>();
        if (Utility.isSet(criteria.getItemId())) {
            itemIds.add(new Long(criteria.getItemId()));
        }
        if (criteria.getItemIds() != null && criteria.getItemIds().size() > 0) {
            itemIds.addAll(criteria.getItemIds());
        }

           StringBuilder baseQuery = new StringBuilder("Select distinct " +
                "new com.espendwise.manta.model.view.ItemListView(" +
                "    item.itemId," +
                "    item.shortDesc," +
                "    coalesce(catalogStructure.customerSkuNum, str(item.skuNum)), " +
                "    itemMetaSize.value, " +
                "    itemMetaPack.value, " +
                "    itemMetaUom.value, " +
                "    itemMetaColor.value, " +
                "    categ.shortDesc, ");
            if (criteria.getShowMfgInfo()) {
                baseQuery.append(
                "   manuf.shortDesc, " +
                "   manufItemMapping.itemNum , ");
            } else {
                baseQuery.append(
                "    '', " +
                "    '', ");
            }
            if (criteria.getShowDistInfo()) {
                baseQuery.append(
                "   dist.shortDesc, " +
                "   distItemMapping.itemNum , ");
            } else {
                baseQuery.append(
                "    '', " +
                "    '', " );
            }
                baseQuery.append(
                "    item.itemStatusCd" +
                ") " +
                " from ItemFullEntity item " +
                " inner join item.catalogStructuresForItemId catalogStructure " +
                " left join item.itemMetas itemMetaSize with itemMetaSize.nameValue = (:itemMetaSizeCode) " +
                " left join item.itemMetas itemMetaPack with itemMetaPack.nameValue = (:itemMetaPackCode) " +
                " left join item.itemMetas itemMetaUom with itemMetaUom.nameValue = (:itemMetaUomCode) " +
                " left join item.itemMetas itemMetaColor with itemMetaColor.nameValue = (:itemMetaColorCode) " +

                " left outer join item.itemAssocsForItem1Id itemAssoc " +
                   "with itemAssoc.itemAssocCd = 'PRODUCT_PARENT_CATEGORY' and " +
                   "itemAssoc.catalogId.catalogId = (:catalogId)" +
                " left outer join itemAssoc.item2Id categ " + categWhere.toString()
             );

             if (criteria.getShowMfgInfo() ||
                 Utility.isSet(criteria.getManufacturer()) ||
                    (Utility.isSet(criteria.getItemSku()) &&
                    		RefCodeNames.SKU_TYPE_CD.MANUFACTURER.equalsIgnoreCase(criteria.getItemSkuTypeFilterType()))
                 ) {
                baseQuery.append(
                " left outer join item.itemMappings manufItemMapping " + manufWhere.toString() +
                " left outer join manufItemMapping.busEntityId manuf ");
             }
             if (criteria.getShowDistInfo() ||
                 Utility.isSet(criteria.getDistributor()) ||
                    (Utility.isSet(criteria.getItemSku()) &&
                    		RefCodeNames.SKU_TYPE_CD.DISTRIBUTOR.equalsIgnoreCase(criteria.getItemSkuTypeFilterType()))
                 ) {
                baseQuery.append(

                " left outer join item.itemMappings distItemMapping " + distrWhere.toString()+
                " left outer join distItemMapping.busEntityId dist ");
             }
             if (Utility.isSet(criteria.getItemProperty()) && Utility.isSet(criteria.getItemPropertyCd())) {
                 baseQuery.append(
                " inner join item.itemMetas im with upper(im.nameValue) = (:itemMetaPropertyCd) "
                 );
             }

        baseQuery.append(" where ");
        baseQuery.append(" catalogStructure.catalogId.catalogId = (:catalogId)");
        baseQuery.append(" and catalogStructure.catalogStructureCd = '"+RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT+"'");

        if (itemIds.size() > 0) {
 		    baseQuery.append("and item.itemId in (:itemIds)");
        }

        if (Utility.isTrue(criteria.getActiveOnly())){
        	baseQuery.append(" and item.itemStatusCd = (:activeStatusCd)");
        }

        if (Utility.isSet(criteria.getItemName() ) ) {
    		baseQuery.append( "and upper(item.shortDesc) like (:itemName) ");
        }

        if (Utility.isSet(criteria.getItemCategory())) {
        	baseQuery.append(" and upper(categ.shortDesc) like (:category)");
      	}

        boolean isSkuParameterSpecified = false;
        if (Utility.isSet(criteria.getItemSku())) {
        	String itemSkuTypeFilterType = Utility.strNN(criteria.getItemSkuTypeFilterType());
        	if (RefCodeNames.SKU_TYPE_CD.STORE.equalsIgnoreCase(itemSkuTypeFilterType)){
        		baseQuery.append(" and coalesce(catalogStructure.customerSkuNum, str(item.skuNum)) like (:sku)");
        		isSkuParameterSpecified = true;
        	} else if (RefCodeNames.SKU_TYPE_CD.MANUFACTURER.equalsIgnoreCase(itemSkuTypeFilterType)){
        		baseQuery.append(" and upper(manufItemMapping.itemNum) like (:sku)");
        		isSkuParameterSpecified = true;
        	} else if (RefCodeNames.SKU_TYPE_CD.DISTRIBUTOR.equalsIgnoreCase(itemSkuTypeFilterType)) {
        		baseQuery.append(" and upper(distItemMapping.itemNum) like (:sku)");
        		isSkuParameterSpecified = true;
        	}
        }

        if (Utility.isSet(criteria.getManufacturer())) {
            baseQuery.append(" and upper(manuf.shortDesc) like (:manufacturer)");
        }

        if (Utility.isSet(criteria.getDistributor())) {
            baseQuery.append(" and upper(dist.shortDesc) like (:distributor)");
        }

        if (Utility.isSet(criteria.getItemProperty()) && Utility.isSet(criteria.getItemPropertyCd())) {
            baseQuery.append(" and upper(im.value) like (:itemProperty) ");
        }
        
        if (Utility.isTrue(criteria.getGreenCertified())) {
        	baseQuery.append(" and item.itemId in (select im.itemId from ItemMappingData im");
        	baseQuery.append(" where im.itemMappingCd='");
        	baseQuery.append(RefCodeNames.ITEM_MAPPING_CD.ITEM_CERTIFIED_COMPANY);
        	baseQuery.append("')");
        }

        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("itemMetaSizeCode", RefCodeNames.ITEM_PROPERTY_CD.SIZE);
        q.setParameter("itemMetaPackCode", RefCodeNames.ITEM_PROPERTY_CD.PACK);
        q.setParameter("itemMetaUomCode", RefCodeNames.ITEM_PROPERTY_CD.UOM);
        q.setParameter("itemMetaColorCode", RefCodeNames.ITEM_PROPERTY_CD.COLOR);

        if (Utility.isSet(criteria.getItemProperty()) && Utility.isSet(criteria.getItemPropertyCd())) {
            q.setParameter("itemMetaPropertyCd", criteria.getItemPropertyCd().toUpperCase());
            q.setParameter("itemProperty",
                    QueryHelp.toFilterValue(
                            criteria.getItemProperty().toUpperCase(),
                            Constants.FILTER_TYPE.CONTAINS)
            );
        }

        q.setParameter("catalogId", criteria.getCatalogId());
       
        if (itemIds.size() > 0) {
            q.setParameter("itemIds", itemIds);
        }

        if (Utility.isTrue(criteria.getActiveOnly())){
        	q.setParameter("activeStatusCd", "ACTIVE");
        }

	    if (Utility.isSet(criteria.getItemName())) {
            q.setParameter("itemName",
                    QueryHelp.toFilterValue(
                            criteria.getItemName().toUpperCase(),
                            criteria.getItemNameFilterType())
            );
	    }

         if (Utility.isSet(criteria.getItemCategory())) {
            q.setParameter("category",
                    QueryHelp.toFilterValue(
                            criteria.getItemCategory().toUpperCase(),
                            criteria.getItemCategoryFilterType())
            );
        }

        if (isSkuParameterSpecified) {
                q.setParameter("sku",
                        QueryHelp.toFilterValue(
                                criteria.getItemSku().toUpperCase(),
                                criteria.getItemSkuFilterType())
                );

	    } 

        if (Utility.isSet(criteria.getManufacturer())) {
                q.setParameter("manufacturer",
                        QueryHelp.toFilterValue(
                                criteria.getManufacturer().toUpperCase(),
                                Constants.FILTER_TYPE.CONTAINS)
                );

        }

        if (Utility.isSet(criteria.getDistributor())) {
                q.setParameter("distributor",
                        QueryHelp.toFilterValue(
                                criteria.getDistributor().toUpperCase(),
                                Constants.FILTER_TYPE.CONTAINS)
                );

        }

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        List<ItemListView> r = q.getResultList();

        logger.info("findItemsByCriteria => END, fetched : " + r.size() + " rows");

        return r;
    }

    @Override
    public EntityHeaderView findItemHeader(Long itemId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.EntityHeaderView(item.itemId, item.shortDesc)" +
                " from  ItemData item where item.itemId = (:itemId) "
        );

        q.setParameter("itemId", itemId);

        List x = q.getResultList();

        return !x.isEmpty() ? (EntityHeaderView) x.get(0) : null;
    }

    @Override
    public ItemIdentView findItemToEdit(Long storeId, Long catalogId, Long itemId) {
        ItemIdentView item = null;
       
        // item data
        Query q = em.createQuery(
            "select item " +
                    "from ItemData item " +
                    "where item.itemId = (:itemId) "

        );

        q.setParameter("itemId", itemId);
        List x = q.getResultList();
        if (!x.isEmpty()) {
            item = new ItemIdentView();
            item.setItemData((ItemData)x.get(0));
        }
        if (item != null) {
            item.setItemMeta(getItemMeta(itemId));
            item.setItemMapping(getItemMapping(itemId));
            item.setItemAssocs(getItemAssoc(itemId, catalogId));

            // catalog structure
            q = em.createQuery(
                "select catalogStructure " +
                        "from CatalogStructureData catalogStructure " +
                        "where catalogStructure.itemId = (:itemId) and" +
                        "   catalogStructure.catalogId = (:catalogId) and " +
                        "   catalogStructure.catalogStructureCd = (:catalogStructureCd) and" +
                        "   catalogStructureCd.statusCd = (:status) "

            );
            q.setParameter("itemId", itemId);
            q.setParameter("catalogId", catalogId);
            q.setParameter("catalogStructureCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);
            q.setParameter("status", RefCodeNames.CATALOG_STATUS_CD.ACTIVE);
            x = q.getResultList();
            if (!x.isEmpty()) {
                item.setCatalogStructureData((CatalogStructureData)x.get(0));
            }
    
        }
        return item;
    }


    @Override
    public CatalogStructureData getItemCatalogStructure(ItemListViewCriteria criteria) {
        logger.info("findItems()=> BEGIN, criteria: "+criteria);

        String skuWhere = (Utility.isSet(criteria.getItemSku())? "catalogStructure.customerSkuNum = (:sku) and " : "");
        String itemIdWhere = (Utility.isSet(criteria.getItemId())? "catalogStructure.itemId = (:itemId) and " : "");
        String statusWhere = (Utility.isTrue(criteria.getActiveOnly()) ? "catalogStructureCd.statusCd = (:status)" : "");

         Query  q = em.createQuery(
                "select catalogStructure " +
                        "from CatalogStructureData catalogStructure " +
                        "where " +
                                itemIdWhere + 
                                skuWhere +
                                statusWhere +
                        "   catalogStructure.catalogId = (:catalogId) and " +
                        "   catalogStructure.catalogStructureCd = (:catalogStructureCd) "
            );
            if (Utility.isSet(criteria.getItemSku())) {
                q.setParameter("sku", criteria.getItemSku());
            }
            if (Utility.isSet(criteria.getItemId())) {
                q.setParameter("itemId", criteria.getItemId());
            }
            q.setParameter("catalogId", criteria.getCatalogId());
            q.setParameter("catalogStructureCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);

            if (Utility.isTrue(criteria.getActiveOnly())){
            	q.setParameter("status", RefCodeNames.CATALOG_STATUS_CD.ACTIVE);
            }

            List x = q.getResultList();
            if (!x.isEmpty()) {
                return ((CatalogStructureData)x.get(0));
            }
            return null;
    }


    private List<ItemMetaData> getItemMeta(Long itemId) {
        Query q = em.createQuery(
            "select itemMeta " +
                    "from ItemMetaData itemMeta " +
                    "where itemMeta.itemId = (:itemId) "

        );
        q.setParameter("itemId", itemId);
        return q.getResultList();
    }

    private List<ItemMappingData> getItemMapping(Long itemId) {
        Query q = em.createQuery(
            "select itemMapping " +
                    "from ItemMappingData itemMapping " +
                    "where itemMapping.itemId = (:itemId) "

        );
        q.setParameter("itemId", itemId);
        return q.getResultList();
    }
    
    @Override
    public List<ItemMappingData> getDistItemMapping(Long shoppingCatalogId, String sku, String uom) {
        Query q = em.createQuery(
            "select itemMapping " +
                    "from ItemMappingData itemMapping, CatalogStructureData catalogItem " +
                    "where itemMapping.itemId = catalogItem.itemId " +
                    "and itemMapping.busEntityId = catalogItem.busEntityId " +
                    "and catalogItem.catalogId = (:catalogId)" +
                    "and itemMapping.itemNum = (:sku) " +
                    "and itemMapping.itemMappingCd = (:itemMappingCd) " +
                    (Utility.isSet(uom) ? "and itemMapping.itemUom = (:uom)" : "")

        );
        q.setParameter("catalogId", shoppingCatalogId);
        q.setParameter("sku", sku);
        q.setParameter("itemMappingCd", RefCodeNames.ITEM_MAPPING_CD.ITEM_DISTRIBUTOR);
        if  (Utility.isSet(uom)) {
        	q.setParameter("uom", uom);
        }
        return q.getResultList();
    }

    private List<ItemAssocData> getItemAssoc(Long itemId, Long catalogId) {
        Query q = em.createQuery(
            "select itemAssoc " +
                    "from ItemAssocData itemAssoc " +
                    "where itemAssoc.item1Id = (:itemId) and" +
                    "       itemAssoc.catalogId = (:catalogId) "

        );
        q.setParameter("itemId", itemId);
        q.setParameter("catalogId", catalogId);
        return q.getResultList();

    }

    @Override
    public ItemIdentView saveItem(Long storeId, ItemIdentView itemIdentView) {
        // item data
        ItemData itemD = itemIdentView.getItemData();
        boolean isNewItem = itemD.getItemId() == null;
        if (isNewItem) {
            itemD.setSkuNum(new Long(-1));
            itemD = super.create(itemD);
        } else {
            itemD = super.update(itemD);
        }
        Long itemId = itemD.getItemId();
        if (isNewItem) {
            itemD.setSkuNum(itemId + SKU_MINIMUM);
            itemD = super.update(itemD);
        }

        // catalog structure
        CatalogStructureData catalogStructureD = itemIdentView.getCatalogStructureData();
        if (catalogStructureD.getCatalogStructureId() == null) {
            catalogStructureD.setItemId(itemId);
            catalogStructureD.setCustomerSkuNum(""+itemD.getSkuNum());
            super.create(catalogStructureD);
        } else {
            super.update(catalogStructureD);
        }
        
        //item meta
        List<ItemMetaData> metaList = itemIdentView.getItemMeta() ;
        updateItemMeta(metaList, itemIdentView.getItemData().getItemId());

        // item mapping
        List<ItemMappingData> mappingList = itemIdentView.getItemMapping() ;
        List<ItemMappingData> oldMappingList = getItemMapping(itemIdentView.getItemData().getItemId());
        for (ItemMappingData mappings : mappingList) {
            if (mappings.getItemMappingId() == null) {
                mappings.setItemId(itemId);
                super.create(mappings);
            } else {
                super.update(mappings);
                for (ItemMappingData mappingOld : oldMappingList) {
                    if (mappingOld.getItemMappingId().equals(mappings.getItemMappingId())) {
                        oldMappingList.remove(mappingOld);
                        break;
                    }
                }
            }
        }
        // removing itemMapping
        for (ItemMappingData mapping : oldMappingList) {
            super.remove(mapping);
        }

        // item assocs     
        List<ItemAssocData> assocList = itemIdentView.getItemAssocs() ;
        List<ItemAssocData> oldAssocList = getItemAssoc(itemIdentView.getItemData().getItemId(), itemIdentView.getCatalogStructureData().getCatalogId());
        for (ItemAssocData assocs : assocList) {
            if (assocs.getItemAssocId() == null) {
                assocs.setItem1Id(itemId);
                super.create(assocs);
            } else {
                super.update(assocs);
                for (ItemAssocData assocOld : oldAssocList) {
                    if (assocOld.getItemAssocId().equals(assocs.getItemAssocId())) {
                        oldAssocList.remove(assocOld);
                        break;
                    }
                }
            }
        }
        // removing assocs
        for (ItemAssocData assoc : oldAssocList) {
            super.remove(assoc);
        }

        return itemIdentView;
    }

    @Override
    public List<ItemMetaData> updateItemMeta(List<ItemMetaData> metaList, Long itemId) {
        List<ItemMetaData> oldMetaList = getItemMeta(itemId);
        for (ItemMetaData meta : metaList) {
            if (meta.getItemMetaId() == null) {
                meta.setItemId(itemId);
                super.create(meta);
            } else {
                super.update(meta);
                for (ItemMetaData metaOld : oldMetaList) {
                    if (metaOld.getItemMetaId().equals(meta.getItemMetaId())) {
                        oldMetaList.remove(metaOld);
                        break;
                    }
                }
            }
        }
        // removing itemMeta
        for (ItemMetaData meta : oldMetaList) {
            super.remove(meta);
        }
        return metaList;
    }

    private boolean getAutoSkuFlag(Long storeId) {
        List<PropertyData> storeProperties = (new PropertyDAOImpl(em)).findStoreProperties(storeId,
                                                Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.AUTO_SKU_ASSIGN));
        if (Utility.isSet(storeProperties)) {
            return Utility.isTrue(storeProperties.get(0).getValue());
        }
        return false;

    }
    
    @Override
    public CatalogStructureData getCatalogItemByStoreSku(Long catalogId, Long storeSkuNum) {
        logger.info("getItemCatalogStructure()=> BEGIN, catalogId: "+catalogId + ", storeSkuNum="+storeSkuNum);
        
        Query  q = em.createQuery(
                "select catalogStructure " +
                        "from CatalogStructureData catalogStructure " +
                        "where catalogStructure.catalogId = (:catalogId) " +
                        "and catalogStructure.catalogStructureCd = (:catalogStructureCd) " +
                        "and catalogStructure.itemId = (select item.itemId from ItemData item where skuNum= (:storeSkuNum)) "
            );
        q.setParameter("catalogId", catalogId);
        q.setParameter("catalogStructureCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);
        q.setParameter("storeSkuNum", storeSkuNum);

        List x = q.getResultList();
        if (!x.isEmpty()) {
            return ((CatalogStructureData)x.get(0));
        }
        return null;
    }

}
