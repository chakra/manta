package com.espendwise.manta.dao;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.espendwise.manta.model.view.*;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.util.criteria.OrderGuideListViewCriteria;
import com.espendwise.manta.util.criteria.OrderGuideItemListViewCriteria;
import com.espendwise.manta.util.*;
import com.espendwise.manta.service.DatabaseUpdateException;

import com.espendwise.manta.web.util.WebSort;
import java.util.*;
import java.math.BigDecimal;

/**
 * Sample Order DAO class for fetching orders.
 */
public class OrderGuideDAOImpl extends DAOImpl implements OrderGuideDAO {

    protected final Logger logger = Logger.getLogger(getClass());

    public OrderGuideDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<OrderGuideListView> findOrderGuidesByCriteria(OrderGuideListViewCriteria criteria) {
        List<OrderGuideListView> orderGuides = Utility.emptyList(OrderGuideListView.class);

        logger.info("findOrderGuidesByCriteria() => BEGIN, criteria : " + criteria);

        StringBuilder baseQuery =
            new StringBuilder(
                    "Select DISTINCT new com.espendwise.manta.model.view.OrderGuideListView (" +
                            "og.orderGuideId," +
                            "og.shortDesc," +
                            "cat.shortDesc," +
                            "cat.catalogStatusCd, " +
                            "og.orderGuideTypeCd) " +
                       " from OrderGuideData og, CatalogData cat  " +
                       " where cat.catalogId = og.catalogId " +
                            "and og.busEntityId = (:siteId) " +
                            "and og.orderGuideTypeCd in (:orderGuideTypeCds) " +
                            " order by og.shortDesc "
                            );


        String query = baseQuery.toString();
        Query q = em.createQuery(query);

        q.setParameter("siteId", criteria.getSiteId());
        q.setParameter("orderGuideTypeCds", criteria.getOrderGuideTypeCds());

        if (Utility.isSet(criteria.getLimit())) {
            q.setMaxResults(criteria.getLimit());
        }

        orderGuides = q.getResultList();

        logger.info("findOrderGuidesByCriteria() => END, fetched : " + orderGuides.size() + " rows");
        

        return orderGuides;
    }

    @Override
    public OrderGuideIdentView findOrderGuideIdentView(Long orderGuideId) {
        OrderGuideIdentView orderGuide = new OrderGuideIdentView() ;

        String query = "Select og from OrderGuideData og where og.orderGuideId = (:orderGuideId)";
        Query q = em.createQuery(query);

        q.setParameter("orderGuideId", orderGuideId);

        List x = q.getResultList();


        OrderGuideData orderGuideData = !x.isEmpty() ? (OrderGuideData) x.get(0) : null;

        if (orderGuideData == null) return orderGuide;

        orderGuide.setOrderGuideData(orderGuideData);

        if (Utility.isSet(orderGuideData.getCatalogId())) {
            query = "Select catalog.shortDesc from CatalogData catalog where catalog.catalogId = (:catalogId)";
            q = em.createQuery(query);
            q.setParameter("catalogId", orderGuideData.getCatalogId());
            x = q.getResultList();
            String catalogName = !x.isEmpty() ? (String) x.get(0) : null;
            orderGuide.setCatalogName(catalogName);
        }
        
        /*
        Long orderGuideStructureId;
        Long orderGuideId;
        Long itemId;
        Long categoryItemId;
        String custCategory;
        Long sortOrder;
        Long quantity;
        String quantityStr;
        BigDecimal itemPrice;
        String productName;
        String itemSkuNum;
        String custItemSkuNum;
        String itemSize;
        String pack;
        String uom;
        String mfg;
        String mfgSku;
        String distSku;
        String category;
        */

        Long catalogId = orderGuideData.getCatalogId();
        Long contractId = getContractId(catalogId);

        query = "Select distinct new com.espendwise.manta.model.view.OrderGuideItemView (" +
                "   ogStructure.orderGuideStructureId," +
                "   ogStructure.orderGuideId.orderGuideId," +
                "   ogStructure.itemId.itemId," +
                "   ogStructure.categoryItemId," +
                "   ogStructure.custCategory," +
                "   ogStructure.sortOrder," +
                "   ogStructure.quantity, " +
                "   str(ogStructure.quantity), " +
                "   contractItem.amount, " +
                "   contractItem.amount * ogStructure.quantity, " +
                "   item.shortDesc, " +
                "   str(item.skuNum), " +
                "   catalogStructure.customerSkuNum, " +
                "   itemMetaSize.value, " +
                "   itemMetaPack.value, " +
                "   itemMetaUom.value, " +
                "   manuf.shortDesc, " +
                "   manufItemMapping.itemNum, " +
                "   catalogStructure.busEntityId.busEntityId, " + // distributorId from catalog structure table
                "   distItemMapping.busEntityId.busEntityId, " + // distributorId from item mapping table
                "   distItemMapping.itemNum, " +
                "   categ.shortDesc )" +

                "  from OrderGuideStructureFullEntity ogStructure " +

                "  inner join ogStructure.itemId item " +
                "  inner join item.catalogStructuresForItemId catalogStructure " +
                
                "  left join item.itemMetas itemMetaSize with itemMetaSize.nameValue = 'SIZE' " +
                "  left join item.itemMetas itemMetaPack with itemMetaPack.nameValue = 'PACK' " +
                "  left join item.itemMetas itemMetaUom with itemMetaUom.nameValue = 'UOM' " +
                "  left join item.contractItems contractItem with contractItem.contractId.contractId = (:contractId) " +
                "  left join item.itemMappings manufItemMapping with manufItemMapping.itemMappingCd = 'ITEM_MANUFACTURER' " +
                "  left join manufItemMapping.busEntityId manuf " +
                "  left join item.itemMappings distItemMapping with distItemMapping.itemMappingCd = 'ITEM_DISTRIBUTOR' " +
                "  left join item.itemAssocsForItem1Id itemAssoc " +
                "      with itemAssoc.itemAssocCd = 'PRODUCT_PARENT_CATEGORY' and " +
                "      itemAssoc.catalogId.catalogId = (:catalogId)" +
                "  left join itemAssoc.item2Id categ " +

                "  where ogStructure.orderGuideId.orderGuideId = (:orderGuideId) and " +
                "     catalogStructure.catalogId.catalogId = (:catalogId) and " +
                "     catalogStructure.catalogStructureCd = '" + RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT + "'";

        q = em.createQuery(query);
        q.setParameter("orderGuideId", orderGuideId);
        q.setParameter("catalogId", catalogId);
        q.setParameter("contractId", contractId);

        List<OrderGuideItemView> items = q.getResultList();

        Map<Long, OrderGuideItemView> itemMap = new Hashtable<Long, OrderGuideItemView>();
        if (Utility.isSet(items)) {
            for (OrderGuideItemView item : items) {
                Long catalogDistId = item.getCatalogDistId();
                Long itemMappingDistId = item.getItemMappingDistId();
                if (!itemMap.containsKey(item.getItemId())) {
                    if (!(catalogDistId != null && catalogDistId.equals(itemMappingDistId))) {
                        item.setDistSku(null);
                    }
                    itemMap.put(item.getItemId(), item);
                } else {
                    OrderGuideItemView storedItem = itemMap.get(item.getItemId());
                    if (storedItem.getDistSku() == null) {
                        if (storedItem.getCatalogDistId() != null && storedItem.getCatalogDistId().equals(itemMappingDistId)) {
                            storedItem.setDistSku(item.getDistSku());
                        }
                    }
                }
            }
            items = new ArrayList(itemMap.values());
            WebSort.sort(items, "custItemSkuNum");
        }
        
        orderGuide.setItems(items);
        
        return orderGuide;
     }

    @Override
    public List<OrderGuideItemView> findOrderGuideItems(Long orderGuideId, Long siteId, Long catalogId) {
        Long contractId = getContractId(catalogId);
        String query = "Select distinct new com.espendwise.manta.model.view.OrderGuideItemView (" +
                "   ogStructure.orderGuideStructureId," +
                "   ogStructure.orderGuideId.orderGuideId," +
                "   ogStructure.itemId.itemId," +
                "   ogStructure.categoryItemId," +
                "   ogStructure.custCategory," +
                "   ogStructure.sortOrder," +
                "   ogStructure.quantity, " +
                "   str(ogStructure.quantity), " +
                "   contractItem.amount, " +
                "   contractItem.amount * ogStructure.quantity, " +
                "   item.shortDesc, " +
               // "   coalesce(catalogStructure.customerSkuNum, str(item.skuNum)), " +
                "   str(item.skuNum), " +
                "   catalogStructure.customerSkuNum, " +
                "   itemMetaSize.value, " +
                "   itemMetaPack.value, " +
                "   itemMetaUom.value, " +
                "   manuf.shortDesc, " +
                "   manufItemMapping.itemNum, " +
                "   catalogStructure.busEntityId.busEntityId, " + // distributorId from catalog structure table
                "   distItemMapping.busEntityId.busEntityId, " + // distributorId from item mapping table
                "   distItemMapping.itemNum, " +
                "   categ.shortDesc " +
                "  )" +
                "  from OrderGuideStructureFullEntity ogStructure " +
                "  inner join ogStructure.itemId item " +
                "  inner join item.catalogStructuresForItemId catalogStructure " +
                "  left join item.itemMetas itemMetaSize with itemMetaSize.nameValue = 'SIZE' " +
                "  left join item.itemMetas itemMetaPack with itemMetaPack.nameValue = 'PACK' " +
                "  left join item.itemMetas itemMetaUom with itemMetaUom.nameValue = 'UOM' " +

                "  left join item.contractItems contractItem with contractItem.contractId.contractId = (:contractId) " +

                "  left outer join item.itemMappings manufItemMapping with manufItemMapping.itemMappingCd = 'ITEM_MANUFACTURER' " +
                "  left outer join manufItemMapping.busEntityId manuf " +

                "  left outer join item.itemMappings distItemMapping with distItemMapping.itemMappingCd = 'ITEM_DISTRIBUTOR' " +
                "           " +

                "  left outer join item.itemAssocsForItem1Id itemAssoc " +
                   "  with itemAssoc.itemAssocCd = 'PRODUCT_PARENT_CATEGORY' and " +
                   "  itemAssoc.catalogId.catalogId = (:catalogId)" +
                "  left outer join itemAssoc.item2Id categ " +

                "  where ogStructure.orderGuideId.orderGuideId = (:orderGuideId) and" +
                "     catalogStructure.catalogId.catalogId = (:catalogId)" +
                "     and catalogStructure.catalogStructureCd = '"+RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT+"'  " +
                "     and distItemMapping.busEntityId in " +
                "               (select catAss.busEntityId from CatalogAssocData catAss " +
                "                     where catAss.catalogId = (:catalogId) and " +
                "                           catAss.catalogAssocCd in ( 'CATALOG_DISTRIBUTOR', 'CATALOG_MAIN_DISTRIBUTOR') ) "
                ;

        Query q = em.createQuery(query);
        q.setParameter("orderGuideId", orderGuideId);
        q.setParameter("catalogId", catalogId);
        q.setParameter("contractId", contractId);
        List<OrderGuideItemView> items = q.getResultList();

        return items;
    }

    @Override
    public OrderGuideIdentView updateOrderGuide(OrderGuideIdentView orderGuide) {
        OrderGuideData ogD = orderGuide.getOrderGuideData();
        boolean isNew = ogD.getOrderGuideId() == null;

        if (isNew) {
            ogD = super.create(ogD);
        } else {
            ogD = super.update(ogD);
        }

        Long orderGuideId = ogD.getOrderGuideId();

        List<OrderGuideItemView> items = orderGuide.getItems();
        if (items != null) {
            List<OrderGuideStructureData> itemsData = selectOrderGuideStructureForUpdate(ogD.getOrderGuideId());
            for (OrderGuideItemView itemV: items) {
                boolean found = false;
                for (OrderGuideStructureData itemD : itemsData) {
                    if (itemD.getOrderGuideStructureId().equals(itemV.getOrderGuideStructureId())) {
                        itemD.setQuantity(itemV.getQuantity() != null ? itemV.getQuantity() : 0);
                        itemD = super.update(itemD);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // add new item
                    OrderGuideStructureData newItem = new OrderGuideStructureData();
                    newItem.setOrderGuideId(orderGuideId);
                    newItem.setItemId(itemV.getItemId());
                    newItem.setQuantity(itemV.getQuantity() != null ? itemV.getQuantity() : 0);
                    newItem.setCategoryItemId(itemV.getCategoryItemId());
                    newItem.setCustCategory(itemV.getCustCategory());
                    newItem = super.create(newItem);
                    itemV.setOrderGuideStructureId(newItem.getOrderGuideStructureId());
                }

            }
        }
        orderGuide.setOrderGuideData(ogD);
        orderGuide.setItems(items);
        return orderGuide;
    }


    @Override
    public void deleteOrderGuide(OrderGuideIdentView orderGuide) {
        //OrderGuideData ogD = orderGuide.getOrderGuideData();
        //super.remove(ogD);
        Long orderGuideId = orderGuide.getOrderGuideData().getOrderGuideId();
        if (Utility.isSet(orderGuideId)) {
            // STJSCR-60
            //Query q = em.createQuery("delete from OrderGuideStructureData orgerGuideStructure" +
            //        " where orgerGuideStructure.orderGuideId = (:orderGuideId)");
            //q.setParameter("orderGuideId", orderGuideId);
            //q.executeUpdate();

            //q = em.createQuery("delete  from OrderGuideData orderGuide where orderGuide.orderGuideId = (:orderGuideId)");
            //q.setParameter("orderGuideId", orderGuideId);
            //q.executeUpdate();
            
            // STJSCR-60
            OrderGuideData orderGuideObj = em.find(OrderGuideData.class, orderGuideId);
            if (Utility.isSet(orderGuideObj)) {
                orderGuideObj.setOrderGuideTypeCd(RefCodeNames.ORDER_GUIDE_TYPE_CD.DELETED);
                super.update(orderGuideObj);
            }
        }
    }



    @Override
    public void deleteOrderGuideItems(List<OrderGuideItemView> items, Long orderGuideId) {
        List<Long> itemIds = Utility.toIds(items);
        if (itemIds.size() > 0) {
            Query q = em.createQuery("delete from OrderGuideStructureData orgerGuideStructure" +
                    " where orgerGuideStructure.orderGuideStructureId in (:orderGuideStructureIds)");
            q.setParameter("orderGuideStructureIds", itemIds);
            q.executeUpdate();

        }
    }

    @Override
    public List<OrderGuideItemView> fillOutOrderGuideItemsData(List<OrderGuideItemView> items, Long catalogId) {
        if (items == null || items.size() == 0) {
            return items;
        }
        Long contractId = getContractId(catalogId);

        List<Long>  itemIds = new ArrayList<Long>();
        for (OrderGuideItemView item : items) {
            itemIds.add(item.getItemId());
        }

        String query = "Select distinct " +
                "   item.itemId, " +
                "   contractItem.amount, " +
                "   itemMetaSize.value, " +
                "   itemMetaUom.value, " +
                "   catalogStructure.customerSkuNum, " +
                "   distItemMapping.itemNum," +
                "   catalogStructure.busEntityId.busEntityId, " + // distributorId from catalogStructure table
                "   distItemMapping.busEntityId.busEntityId" + // distributorId from itemMapping table
                "  from ItemFullEntity item " +
                "  inner join item.catalogStructuresForItemId catalogStructure " +
                "  left join item.itemMetas itemMetaSize with itemMetaSize.nameValue = 'SIZE' " +
                "  left join item.itemMetas itemMetaUom with itemMetaUom.nameValue = 'UOM' " +
                "  left join item.contractItems contractItem with contractItem.contractId.contractId = (:contractId) " +
                "  left join item.itemMappings distItemMapping with distItemMapping.itemMappingCd = 'ITEM_DISTRIBUTOR' " +

                "  where item.itemId.itemId in (:itemIds) " +
                "     and catalogStructure.catalogId.catalogId = (:catalogId)" +
                "     and catalogStructure.catalogStructureCd = '" + RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT + "'";

        Query q = em.createQuery(query);
        q.setParameter("contractId", contractId);
        q.setParameter("catalogId", catalogId);
        q.setParameter("itemIds", itemIds);

        List itemsInfo = q.getResultList();

        Map<Long, Object[]> itemsInfoMap = new Hashtable<Long, Object[]>();
        if (Utility.isSet(itemsInfo)) {
            for (Object obj : itemsInfo) {
                Object[] tmpItemInfo = (Object[]) obj;
                Long catalogDistId = (Long) tmpItemInfo[6];
                Long itemMappingDistId = (Long) tmpItemInfo[7];
                if (!itemsInfoMap.containsKey((Long)tmpItemInfo[0])) {
                    if (!(catalogDistId != null && catalogDistId.equals(itemMappingDistId))) {
                        tmpItemInfo[5] = null;
                    }
                    itemsInfoMap.put((Long)tmpItemInfo[0], tmpItemInfo);
                } else {
                    Object[] storedItemInfo = itemsInfoMap.get((Long)tmpItemInfo[0]);
                    if (storedItemInfo[5] == null) {
                        if (storedItemInfo[6] != null && ((Long)storedItemInfo[6]).equals(itemMappingDistId)) {
                            storedItemInfo[5] = tmpItemInfo[5];
                        }
                    }
                }
            }
        }
            
        for (OrderGuideItemView item : items) {
            if (itemsInfoMap.containsKey(item.getItemId())) {
                Object[] c = (Object[]) itemsInfoMap.get(item.getItemId());
                item.setItemPrice((BigDecimal)c[1]);
                item.setItemSize((String)c[2]);
                item.setUom((String)c[3]);
                item.setCustItemSkuNum((String)c[4]);
                item.setDistSku((String)c[5]);
            }
        }
        
        return items;
    }


    private List<ContractData> findContractsByCatalog(Long catalogId) {
        List<ContractData> contracts = null;
        if (catalogId != null) {
            String baseQuery = "Select object(contract) from ContractData contract where contract.catalogId = (:catalogId)";
            Query q = em.createQuery(baseQuery);
            q.setParameter("catalogId", catalogId);
            contracts = (List<ContractData>) q.getResultList();
        }
        return contracts;
    }


    private Long getContractId(Long catalogId) {
        List<ContractData> contracts = findContractsByCatalog(catalogId);

        if (contracts == null || contracts.size() == 0) {
            return new Long(0);
        }
        if (contracts.size() == 1) {
            return (contracts.get(0)).getContractId();
        }
        Map<String, List<ContractData>> contractsMap = new HashMap<String, List<ContractData>>();
        for (ContractData cD : contracts) {
            String status = cD.getContractStatusCd();
            if (contractsMap.containsKey(status)) {
                List<ContractData> c =  contractsMap.get(status);
                c.add(cD);
            } else {
                List<ContractData> c = new ArrayList<ContractData>();
                c.add(cD);
                contractsMap.put(status, c);
            }
        }

        List<ContractData> c = contractsMap.get(RefCodeNames.CONTRACT_STATUS_CD.ACTIVE);
        if (c != null && c.size() == 1) {
            return c.get(0).getContractId();
        }
        c = contractsMap.get(RefCodeNames.CONTRACT_STATUS_CD.LIVE);
        if (c != null && c.size() == 1) {
            return c.get(0).getContractId();
        }
        c = contractsMap.get(RefCodeNames.CONTRACT_STATUS_CD.ROUTING);
        if (c != null && c.size() == 1) {
            return c.get(0).getContractId();
        }
        return new Long(0);
        
    }

    private List<OrderGuideStructureData> selectOrderGuideStructureForUpdate(Long orderGuideId) {
        List<OrderGuideStructureData> items = null;
        if (orderGuideId != null) {
            String baseQuery = "Select object(ogStructureData) from OrderGuideStructureData ogStructureData where ogStructureData.orderGuideId = (:orderGuideId)";
            Query q = em.createQuery(baseQuery);
            q.setParameter("orderGuideId", orderGuideId);
            items = (List<OrderGuideStructureData>) q.getResultList();
        }
        return items;

    }
    @Override
    public List<OrderGuideData> findOrderGuides(Set<String> pNames, List<String> pTypes) {

        String query = "Select " +
                " DISTINCT new com.espendwise.manta.model.data.OrderGuideData "+
        		" ( "+
        		"  og.shortDesc, " +
        		"  coalesce(og.catalogId, c.catalogId), "+
        		"  og.busEntityId, "+
        		"  og.userId, " +
        		"  og.orderGuideTypeCd, "+
        		"  og.addDate, "+
        		"  og.addBy, " +
        		"  og.modDate, " +
        		"  og.modBy) " +
        		" from OrderGuideData og," +
        		"      CatalogData c " +
        		"where 1=1 " +
        		"  and og.orderGuideTypeCd in (:orderGuideTypes) " +
        		"  and og.shortDesc in (:orderGuideNames)" +
        		"  and( (og.catalogId = c.catalogId) or "+
        		"       (og.catalogId is null and og.busEntityId in (select ca.busEntityId from CatalogAssocData ca where ca.catalogId = c.catalogId and ca.catalogAssocCd ='CATALOG_SITE'))"+ 
        		"	  )  "+    
        		"	and c.catalogTypeCd ='SHOPPING' " ;
        Query q = em.createQuery(query);

        q.setParameter("orderGuideNames", pNames);
        q.setParameter("orderGuideTypes", pTypes);

        List<OrderGuideData> ogs = (List<OrderGuideData>)q.getResultList();
        return ogs;
     }
    
    @Override
    public List<OrderScheduleData> findOrderGuideSchedules(Long orderGuideId, Long siteId) {
        List<OrderScheduleData> result = new ArrayList<OrderScheduleData>();
        
        if (Utility.isSet(orderGuideId)) { 
            StringBuilder query = new StringBuilder("Select object(orderSchedule) from OrderScheduleData orderSchedule where");
            query.append(" orderSchedule.orderGuideId = (:orderGuideId)");
            query.append(" and orderSchedule.recordStatusCd = (:recordStatusCd)");
            
            if (Utility.isSet(siteId)) { 
                query.append(" and orderSchedule.busEntityId = (:siteId)");
            }

            Query q = em.createQuery(query.toString());
            q.setParameter("orderGuideId", orderGuideId);
            q.setParameter("recordStatusCd", RefCodeNames.RECORD_STATUS_CD.VALID);
            if (Utility.isSet(siteId)) { 
                q.setParameter("siteId", siteId);
            }
            result = q.getResultList();
        }

        return result;
    }
    
    @Override
    public List<OrderGuideData> findOrderGuidesByName(String orderGuideName, Long siteId, Long catalogId) {
        List<OrderGuideData> result = new ArrayList<OrderGuideData>();
        
        if (Utility.isSet(orderGuideName)) { 
            StringBuilder query = new StringBuilder("Select object(orderGuide) from OrderGuideData orderGuide where");
            query.append(" orderGuide.shortDesc = (:orderGuideName)");
            if (Utility.isSet(siteId)) {
                query.append(" and orderGuide.busEntityId = (:siteId)");
            }
            if (Utility.isSet(catalogId)) {
                query.append(" and orderGuide.catalogId = (:catalogId)");
            }
            query.append(" and orderGuide.orderGuideTypeCd in (:orderGuideTypes)");
            
            Query q = em.createQuery(query.toString());
            q.setParameter("orderGuideName", orderGuideName);
            if (Utility.isSet(siteId)) { 
                q.setParameter("siteId", siteId);
            }
            if (Utility.isSet(catalogId)) { 
                q.setParameter("catalogId", catalogId);
            }
            q.setParameter("orderGuideTypes", Utility.toList(RefCodeNames.ORDER_GUIDE_TYPE_CD.SITE_ORDER_GUIDE_TEMPLATE,
                                                             RefCodeNames.ORDER_GUIDE_TYPE_CD.BUYER_ORDER_GUIDE));
            
            result = q.getResultList();
        }
        
        return result;
    }

}
