package com.espendwise.manta.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.data.ItemMetaData;
import com.espendwise.manta.model.data.ShoppingControlData;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.AccountShoppingControlItemViewCriteria;

@Repository
public class ShoppingControlDAOImpl extends DAOImpl implements ShoppingControlDAO {

    private static final Logger logger = Logger.getLogger(ShoppingControlDAOImpl.class);
    
    public static final Long DEFAULT_QUANTITY = new Long(-1);
    public static final Long DEFAULT_BUS_ENTITY_ID = new Long(0);

    public ShoppingControlDAOImpl() {
        this(null);
    } 

    public ShoppingControlDAOImpl(EntityManager entityManager) {
        super(entityManager);
    } 
 
    @Override
    public List<ShoppingControlItemView> findShoppingControls(AccountShoppingControlItemViewCriteria criteria) {
    	
    	//get the account catalog id
    	//JEE TODO - use the CatalogDAO for this
        StringBuilder accountCatalogQuery = new StringBuilder("select distinct new com.espendwise.manta.model.view.CatalogListView(");
        accountCatalogQuery.append(" catalog.catalogId) ");
        accountCatalogQuery.append(" from com.espendwise.manta.model.fullentity.CatalogFullEntity catalog ");
        accountCatalogQuery.append(" inner join catalog.catalogAssocs assoc ");
        accountCatalogQuery.append(" where catalog.catalogTypeCd = '");
        accountCatalogQuery.append(RefCodeNames.CATALOG_TYPE_CD.ACCOUNT);
        accountCatalogQuery.append("' and catalog.catalogStatusCd <> '");
        accountCatalogQuery.append(RefCodeNames.CATALOG_STATUS_CD.INACTIVE);
        accountCatalogQuery.append("' and catalog.catalogId = assoc.catalogId ");
        accountCatalogQuery.append(" and assoc.catalogAssocCd = '");
        accountCatalogQuery.append(RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT);
        accountCatalogQuery.append("' and assoc.busEntityId = ");
        accountCatalogQuery.append(criteria.getAccountId());
        Query q = em.createQuery(accountCatalogQuery.toString());
        List<CatalogListView> catalogs = q.getResultList();
        Long accountCatalogId = null;
        if (catalogs.size() == 0) {
        	throw new IllegalStateException("No active account catalog has been specified.");
        }
        else if (catalogs.size() > 1) {
        	throw new IllegalStateException("Multiple active account catalogs have been specified.");
        }
        else {
        	accountCatalogId = catalogs.get(0).getCatalogId();
        }
        
    	//now get the store catalog id
        //first we need to get the id of the store associated with the account
        BusEntityAssocDAO busEntityAssocDao = new BusEntityAssocDAOImpl(em);
        List<BusEntityAssocData> busEntityAssocs = busEntityAssocDao.findAssocs(criteria.getAccountId(), new ArrayList<Long>(), RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        Long storeId = null;
        if (busEntityAssocs.size() == 0) {
        	throw new IllegalStateException("No store is associated with the specified account.");
        }
        else if (busEntityAssocs.size() > 1) {
        	throw new IllegalStateException("Multiple stores are associated with the specified account.");
        }
        else {
        	storeId = busEntityAssocs.get(0).getBusEntity2Id();
        }
        //now we can get the catalog id
    	//JEE TODO - use the CatalogDAO for this
        StringBuilder storeCatalogQuery = new StringBuilder("select distinct new com.espendwise.manta.model.view.CatalogListView(");
        storeCatalogQuery.append(" catalog.catalogId) ");
        storeCatalogQuery.append(" from com.espendwise.manta.model.fullentity.CatalogFullEntity catalog ");
        storeCatalogQuery.append(" inner join catalog.catalogAssocs assoc ");
        storeCatalogQuery.append(" where catalog.catalogTypeCd = '");
        storeCatalogQuery.append(RefCodeNames.CATALOG_TYPE_CD.STORE);
        storeCatalogQuery.append("' and catalog.catalogStatusCd = '");
        storeCatalogQuery.append(RefCodeNames.CATALOG_STATUS_CD.ACTIVE);
        storeCatalogQuery.append("' and catalog.catalogId = assoc.catalogId ");
        storeCatalogQuery.append(" and assoc.catalogAssocCd = '");
        storeCatalogQuery.append(RefCodeNames.CATALOG_ASSOC_CD.CATALOG_STORE);
        storeCatalogQuery.append("' and assoc.busEntityId = ");
        storeCatalogQuery.append(storeId);
        q = em.createQuery(storeCatalogQuery.toString());
        catalogs = q.getResultList();
        Long storeCatalogId = null;
        if (catalogs.size() == 0) {
        	throw new IllegalStateException("No active store catalog has been specified.");
        }
        else if (catalogs.size() > 1) {
        	throw new IllegalStateException("Multiple active store catalogs have been specified.");
        }
        else {
        	storeCatalogId = catalogs.get(0).getCatalogId();
        }
        
        //now get the ids of the items in both the account and store catalogs.
        StringBuilder accountCatalogItemQuery = new StringBuilder("Select distinct catalogStructure.itemId from CatalogStructureData catalogStructure");
        accountCatalogItemQuery.append(" where catalogStructure.catalogId = ");
        accountCatalogItemQuery.append(accountCatalogId);
        accountCatalogItemQuery.append(" and catalogStructure.catalogStructureCd = '");
        accountCatalogItemQuery.append(RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);
        accountCatalogItemQuery.append("'");
        StringBuilder storeCatalogItemQuery = new StringBuilder("Select distinct catalogStructure.itemId from CatalogStructureData catalogStructure");
        storeCatalogItemQuery.append(" where catalogStructure.catalogId = ");
        storeCatalogItemQuery.append(storeCatalogId);
        storeCatalogItemQuery.append(" and catalogStructure.catalogStructureCd = '");
        storeCatalogItemQuery.append(RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);
        storeCatalogItemQuery.append("'");
        StringBuilder itemQuery = new StringBuilder("select item.itemId from ItemData item");
        itemQuery.append(" where item.itemTypeCd = '");
        itemQuery.append(RefCodeNames.ITEM_TYPE_CD.PRODUCT);
        itemQuery.append("'");
        itemQuery.append(" and item.itemId in (");
        itemQuery.append(accountCatalogItemQuery.toString());
        itemQuery.append(")");
        itemQuery.append(" and item.itemId in (");
        itemQuery.append(storeCatalogItemQuery.toString());
        itemQuery.append(")");
        //if any item ids were specified in the search criteria, restrict the search to just those items
        List<Long> criteriaIds = Utility.toIds(criteria.getItems());
        if (Utility.isSet(criteriaIds)) {
            //JEE TODO - handle situation where criteriaIds > 1000
            itemQuery.append(" and item.itemId in (:criteriaIds)");
        }
        q = em.createQuery(itemQuery.toString());
        if (Utility.isSet(criteriaIds)) {
            //JEE TODO - handle situation where criteriaIds > 1000
            q.setParameter("criteriaIds", criteriaIds);
        }
        List<Long> itemIds = q.getResultList();
        
        //MANTA-442
        if (!Utility.isSet(itemIds)) {
        	return new ArrayList<ShoppingControlItemView>();
        }
        
        //if a maximum number of results was specified in the search criteria, pare the item ids down to 
        //that maximum if necessary
        //JEE TODO - need to show a message somehow if more results were found than what we return
        if (criteria.getLimit() != null && itemIds.size() > criteria.getLimit().intValue()) {
        	itemIds = itemIds.subList(0, criteria.getLimit().intValue());
        }
        
        //If the user has requested to view uncontrolled items, create a ShoppingControlItemView object for each 
        //item id found above.  Populate it with the account id and item id.
        Map<Long, ShoppingControlItemView> shoppingControlItemViewMap = new HashMap<Long, ShoppingControlItemView>();
        if (criteria.getShowUncontrolledItems()) {
	        Iterator<Long> itemIdIterator = itemIds.iterator();
	        while (itemIdIterator.hasNext()) {
	        	Long itemId = itemIdIterator.next();
	        	ShoppingControlItemView shoppingControlItemView = new ShoppingControlItemView();
	        	shoppingControlItemView.setShoppingControlAccountId(criteria.getAccountId());
	        	shoppingControlItemView.setItemId(itemId);
	        	shoppingControlItemViewMap.put(itemId, shoppingControlItemView);
	        }
        }
        
        //get any existing shopping controls for the item ids retrieved above. if we retrieve any 
        //shopping controls, replace the corresponding empty ones created in the previous
        //step with the existing ones
        StringBuilder shoppingControlQuery = new StringBuilder("select object(shoppingControl) from ShoppingControlData shoppingControl");
        shoppingControlQuery.append(" where shoppingControl.accountId = ");
        shoppingControlQuery.append(criteria.getAccountId());
        shoppingControlQuery.append(" and shoppingControl.siteId = ");
        shoppingControlQuery.append(criteria.getLocationId());
        //JEE TODO - handle situation where itemIds > 1000
        shoppingControlQuery.append(" and shoppingControl.itemId in (:itemIds)");
        q = em.createQuery(shoppingControlQuery.toString());
        //JEE TODO - handle situation where itemIds > 1000
        q.setParameter("itemIds", itemIds);
        List<ShoppingControlData> shoppingControls = q.getResultList();
        if (Utility.isSet(shoppingControls)) {
        	Iterator<ShoppingControlData> shoppingControlIterator = shoppingControls.iterator();
        	while (shoppingControlIterator.hasNext()) {
        		ShoppingControlData shoppingControl = shoppingControlIterator.next();
            	ShoppingControlItemView shoppingControlItemView = shoppingControlItemViewMap.get(shoppingControl.getItemId());
            	if (shoppingControlItemView == null) {
                	shoppingControlItemView = new ShoppingControlItemView();
            		shoppingControlItemView.setShoppingControlAccountId(shoppingControl.getAccountId());
            		shoppingControlItemView.setItemId(shoppingControl.getItemId());
            		shoppingControlItemViewMap.put(shoppingControl.getItemId(), shoppingControlItemView);
            	}
            	shoppingControlItemView.setShoppingControlId(shoppingControl.getShoppingControlId());
            	shoppingControlItemView.setShoppingControlLocationId(shoppingControl.getSiteId());
            	shoppingControlItemView.setShoppingControlMaxOrderQty(shoppingControl.getMaxOrderQty().toString());
            	shoppingControlItemView.setShoppingControlRestrictionDays(shoppingControl.getRestrictionDays().toString());
            	shoppingControlItemView.setShoppingControlAction(shoppingControl.getActionCd());
        	}
        }

        //get item information about the specified items
        itemQuery = new StringBuilder("select object(item) from ItemData item where item.itemId in (:itemIds) order by item.itemId asc");
        //JEE TODO - handle situation where itemIds > 1000
        q = em.createQuery(itemQuery.toString());
        //JEE TODO - handle situation where itemIds > 1000
        q.setParameter("itemIds", itemIds);
        List<ItemData> items = q.getResultList();
        //populate the ShoppingControlItemViews created above with information about the items
        Iterator<ItemData> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
        	ItemData item = itemIterator.next();
        	ShoppingControlItemView shoppingControlItemView = shoppingControlItemViewMap.get(item.getItemId());
        	if (shoppingControlItemView != null) {
        		shoppingControlItemView.setItemId(item.getItemId());
        		shoppingControlItemView.setItemShortDesc(item.getShortDesc());
        		shoppingControlItemView.setItemSku(item.getSkuNum().toString());
        	}
        }
        
        //get item meta data information about the specified items
        StringBuilder itemMetaDataQuery = new StringBuilder("select object(itemMetaData) from ItemMetaData itemMetaData");
        itemMetaDataQuery.append(" where itemMetaData.itemId in (:itemIds)");
        itemMetaDataQuery.append(" and itemMetaData.nameValue in (:nameValues)");
        itemMetaDataQuery.append(" order by itemMetaData.itemId asc, itemMetaData.itemMetaId asc");
        //JEE TODO - handle situation where itemIds > 1000
        q = em.createQuery(itemMetaDataQuery.toString());
        //JEE TODO - handle situation where itemIds > 1000
        q.setParameter("itemIds", itemIds);
        List<String> nameValues = new ArrayList<String>();
        nameValues.add(RefCodeNames.NAME_VALUE_CD.PACK);
        nameValues.add(RefCodeNames.NAME_VALUE_CD.SIZE);
        nameValues.add(RefCodeNames.NAME_VALUE_CD.UNIT_OF_MEASURE);
        q.setParameter("nameValues", nameValues);
        List<ItemMetaData> itemMetaDatas = q.getResultList();
        //populate the ShoppingControlItemViews created above with meta information about the items
        Iterator<ItemMetaData> itemMetaDataIterator = itemMetaDatas.iterator();
        while (itemMetaDataIterator.hasNext()) {
        	ItemMetaData itemMetaData = itemMetaDataIterator.next();
        	ShoppingControlItemView shoppingControlItemView = shoppingControlItemViewMap.get(itemMetaData.getItemId());
        	if (shoppingControlItemView != null) {
	        	if (RefCodeNames.NAME_VALUE_CD.PACK.equalsIgnoreCase(itemMetaData.getNameValue())) {
	        		shoppingControlItemView.setItemPack(itemMetaData.getValue());
	        	}
	        	else if (RefCodeNames.NAME_VALUE_CD.SIZE.equalsIgnoreCase(itemMetaData.getNameValue())) {
	        		shoppingControlItemView.setItemSize(itemMetaData.getValue());
	        	}
	        	else if (RefCodeNames.NAME_VALUE_CD.UNIT_OF_MEASURE.equalsIgnoreCase(itemMetaData.getNameValue())) {
	        		shoppingControlItemView.setItemUom(itemMetaData.getValue());
	        	}
        	}
        }

        //return the list of ShoppingControlItemView objects. First though populate the original action, max quantity,
        //and restriction days values and then convert any unlimited values to asterisks
        List<ShoppingControlItemView> result = new ArrayList<ShoppingControlItemView>(shoppingControlItemViewMap.values());
        Iterator<ShoppingControlItemView> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
        	ShoppingControlItemView shoppingControl = resultIterator.next();
        	shoppingControl.setShoppingControlOriginalAction(shoppingControl.getShoppingControlAction());
        	shoppingControl.setShoppingControlOriginalMaxOrderQty(shoppingControl.getShoppingControlMaxOrderQty());
        	shoppingControl.setShoppingControlOriginalRestrictionDays(shoppingControl.getShoppingControlRestrictionDays());
        	if (DEFAULT_QUANTITY.toString().equals(shoppingControl.getShoppingControlMaxOrderQty())) {
        		shoppingControl.setShoppingControlMaxOrderQty(Constants.CHARS.ASTERISK);
        	}
        	if (DEFAULT_QUANTITY.toString().equals(shoppingControl.getShoppingControlRestrictionDays())) {
        		shoppingControl.setShoppingControlRestrictionDays(Constants.CHARS.ASTERISK);
        	}
        }
    	logger.info("findShoppingControls()===> found : "+ result.size());
        return result;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Map<String,List<ShoppingControlItemView>> updateShoppingControls(List<ShoppingControlItemView> shoppingControls) {
    	Map<String,List<ShoppingControlItemView>> returnValue = new HashMap<String,List<ShoppingControlItemView>>();
    	returnValue.put(RefCodeNames.HISTORY_TYPE_CD.CREATED, new ArrayList<ShoppingControlItemView>());
    	returnValue.put(RefCodeNames.HISTORY_TYPE_CD.MODIFIED, new ArrayList<ShoppingControlItemView>());
    	if (Utility.isSet(shoppingControls)) {
    		Map<Long, String> busEntityMap = new HashMap<Long,String>();
    		Iterator<ShoppingControlItemView> shoppingControlIterator = shoppingControls.iterator();
    		while (shoppingControlIterator.hasNext()) {
    			ShoppingControlItemView shoppingControl = shoppingControlIterator.next();
    			//convert any asterisks to their default values
	        	if (Constants.CHARS.ASTERISK.equals(shoppingControl.getShoppingControlMaxOrderQty())) {
	        		shoppingControl.setShoppingControlMaxOrderQty(ShoppingControlDAOImpl.DEFAULT_QUANTITY.toString());
	        	}
	        	if (Constants.CHARS.ASTERISK.equals(shoppingControl.getShoppingControlRestrictionDays())) {
	        		shoppingControl.setShoppingControlRestrictionDays(ShoppingControlDAOImpl.DEFAULT_QUANTITY.toString());
	        	}
    			if (shoppingControlIsModified(shoppingControl)) {
    				populateBusEntityInfo(shoppingControl, busEntityMap);
        			if (Utility.isNew(shoppingControl)) {
        				createShoppingControl(shoppingControl);
        				returnValue.get(RefCodeNames.HISTORY_TYPE_CD.CREATED).add(shoppingControl);
        			}
        			else {
        				updateShoppingControl(shoppingControl);
        				returnValue.get(RefCodeNames.HISTORY_TYPE_CD.MODIFIED).add(shoppingControl);
        			}
    			}
    		}
    	}
    	return returnValue;
    }
    
    private void populateBusEntityInfo(ShoppingControlItemView shoppingControl, Map<Long, String> busEntityMap) {
        Long locationId = shoppingControl.getShoppingControlLocationId();
        if (Utility.isSet(locationId) && !ShoppingControlDAOImpl.DEFAULT_BUS_ENTITY_ID.equals(locationId)) {
        	String busEntityName = busEntityMap.get(locationId);
        	if (!Utility.isSet(busEntityName)) {
        		busEntityName = getBusEntityName(locationId);
				busEntityMap.put(locationId, busEntityName);
        	}
    		shoppingControl.setShoppingControlLocationName(busEntityName);
        }
        Long accountId = shoppingControl.getShoppingControlAccountId();
        if (Utility.isSet(accountId) && !ShoppingControlDAOImpl.DEFAULT_BUS_ENTITY_ID.equals(accountId)) {
        	String busEntityName = busEntityMap.get(accountId);
        	if (!Utility.isSet(busEntityName)) {
        		busEntityName = getBusEntityName(accountId);
				busEntityMap.put(accountId, busEntityName);
        	}
    		shoppingControl.setShoppingControlAccountName(busEntityName);
        }
        //populate the store id and name.  Use the account id if it exists, otherwise use the location id
        Long storeId = null;
        String storeName = null;
        if (Utility.isSet(accountId) && !ShoppingControlDAOImpl.DEFAULT_BUS_ENTITY_ID.equals(accountId)) {
        	storeId = getStoreId(accountId, RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
        	storeName = busEntityMap.get(storeId);
        	if (!Utility.isSet(storeName)) {
        		storeName = getBusEntityName(storeId);
				busEntityMap.put(storeId, storeName);
        	}
        }
        else if (Utility.isSet(locationId) && !ShoppingControlDAOImpl.DEFAULT_BUS_ENTITY_ID.equals(locationId)) {
        	storeId = getStoreId(locationId, RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
        	storeName = busEntityMap.get(storeId);
        	if (!Utility.isSet(storeName)) {
        		storeName = getBusEntityName(storeId);
				busEntityMap.put(storeId, storeName);
        	}
        }
    	shoppingControl.setShoppingControlStoreId(storeId);
		shoppingControl.setShoppingControlStoreName(storeName);
    }
    
    private String getBusEntityName(Long busEntityId) {
    	String returnValue = "";
		try {
			BusEntityDAO busEntityDao = new BusEntityDAOImpl(em);
			List<Long> busEntityIds = new ArrayList<Long>();
			busEntityIds.add(busEntityId);
			List<BusEntityData> busEntities = busEntityDao.find(busEntityIds);
			if (Utility.isSet(busEntities)) {
				returnValue = busEntities.get(0).getShortDesc();
			}
		}
		catch (Exception e) {
			logger.error("Unable to retrieve bus entity name for id = " + busEntityId);
		}
    	return returnValue;
    }
    
    private Long getStoreId(Long busEntityId, String busEntityTypeCode) {
    	Long returnValue = null;
		try {
			BusEntityAssocDAO busEntityAssocDao = new BusEntityAssocDAOImpl(em);
			Long accountId = null;
			if (RefCodeNames.BUS_ENTITY_TYPE_CD.SITE.equalsIgnoreCase(busEntityTypeCode)) {
				List<Long> busEntities = new ArrayList<Long>();
				busEntities.add(busEntityId);
				accountId = busEntityAssocDao.findAssocs(busEntities, null, RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT).get(0).getBusEntity2Id();
			}
			else {
				accountId = busEntityId;
			}
			List<Long> busEntities = new ArrayList<Long>();
			busEntities.add(accountId);
			returnValue = busEntityAssocDao.findAssocs(busEntities, null, RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE).get(0).getBusEntity2Id();
		}
		catch (Exception e) {
			logger.error("Unable to retrieve store id for " + busEntityTypeCode + " with id = " + busEntityId);
		}
    	return returnValue;
    }
    
    private boolean shoppingControlIsModified(ShoppingControlItemView shoppingControl) {
    	boolean actionChanged = !Utility.isEqual(shoppingControl.getShoppingControlOriginalAction(), shoppingControl.getShoppingControlAction());
    	boolean maxQuantityChanged = !Utility.isEqual(shoppingControl.getShoppingControlOriginalMaxOrderQty(), shoppingControl.getShoppingControlMaxOrderQty());
    	boolean restrictionDaysChanged = !Utility.isEqual(shoppingControl.getShoppingControlOriginalRestrictionDays(), shoppingControl.getShoppingControlRestrictionDays());
    	boolean returnValue = actionChanged || maxQuantityChanged || restrictionDaysChanged;
    	return returnValue;
    }
    
    private void createShoppingControl(ShoppingControlItemView shoppingControl) {
        ShoppingControlData shoppingControlData = new ShoppingControlData();
        //account id or site id should be set but not both.  If a site id value is specified then use it,
        //otherwise use the account id value.  Also, since action values are only applicable to account
        //based shopping controls, set the value to be null if this is a site based shopping control and use
        //the specified value if it is an account based shopping control.
        Long locationId = shoppingControl.getShoppingControlLocationId();
        if (Utility.isSet(locationId)) {
            shoppingControlData.setAccountId(DEFAULT_BUS_ENTITY_ID);
        	shoppingControlData.setSiteId(locationId);
        	shoppingControlData.setActionCd(null);
        }
        else {
            shoppingControlData.setAccountId(shoppingControl.getShoppingControlAccountId());
        	shoppingControlData.setSiteId(DEFAULT_BUS_ENTITY_ID);
            String actionCode = shoppingControl.getShoppingControlAction();
            if (Utility.isSet(actionCode)) {
            	shoppingControlData.setActionCd(shoppingControl.getShoppingControlAction());
            }
            else {
            	shoppingControlData.setActionCd(null);
            }
        }        
        shoppingControlData.setActualMaxQty(DEFAULT_QUANTITY);
        //addDate and addBy are handled automatically
        //control status code
        shoppingControlData.setControlStatusCd(RefCodeNames.SHOPPING_CONTROL_STATUS_CD.ACTIVE);
        shoppingControlData.setExpDate(null);
        shoppingControlData.setHistoryOrderQty(DEFAULT_QUANTITY);
        //item id
        shoppingControlData.setItemId(shoppingControl.getItemId());
        //max order quantity
        String maxOrderQuantity = shoppingControl.getShoppingControlMaxOrderQty();
        if (Utility.isSet(maxOrderQuantity) && !Constants.CHARS.ASTERISK.equalsIgnoreCase(maxOrderQuantity)) {
	        shoppingControlData.setMaxOrderQty(new Long(maxOrderQuantity));
        }
        else {
        	shoppingControlData.setMaxOrderQty(DEFAULT_QUANTITY);
        }
        //modDate and modBy are handled automatically
        //restriction days
        String restrictionDays = shoppingControl.getShoppingControlRestrictionDays();
        if (Utility.isSet(restrictionDays) && !Constants.CHARS.ASTERISK.equalsIgnoreCase(restrictionDays)) {
	        shoppingControlData.setRestrictionDays(new Long(restrictionDays));
        }
        else {
        	shoppingControlData.setRestrictionDays(DEFAULT_QUANTITY);
        }
        ShoppingControlData newShoppingControlData = super.create(shoppingControlData);
        shoppingControl.setShoppingControlId(newShoppingControlData.getShoppingControlId());
    }
    
    private void updateShoppingControl(ShoppingControlItemView shoppingControl) {
        StringBuilder query = new StringBuilder("Select shoppingControl from ShoppingControlData shoppingControl ");
        query.append(" where shoppingControl.shoppingControlId = (:shoppingControlId)");
        String queryString = query.toString();
        Query q = em.createQuery(queryString);
        q.setParameter("shoppingControlId", shoppingControl.getShoppingControlId());
        List<ShoppingControlData> shoppingControls = (List<ShoppingControlData>) q.getResultList();
        if (Utility.isSet(shoppingControls)) {
        	ShoppingControlData scd = shoppingControls.get(0);
            String maxOrderQuantity = shoppingControl.getShoppingControlMaxOrderQty();
            if (Utility.isSet(maxOrderQuantity) && !Constants.CHARS.ASTERISK.equalsIgnoreCase(maxOrderQuantity)) {
            	scd.setMaxOrderQty(new Long(maxOrderQuantity));
            }
            else {
            	scd.setMaxOrderQty(DEFAULT_QUANTITY);
            }
            String restrictionDays = shoppingControl.getShoppingControlRestrictionDays();
            if (Utility.isSet(restrictionDays) && !Constants.CHARS.ASTERISK.equalsIgnoreCase(restrictionDays)) {
    	        scd.setRestrictionDays(new Long(restrictionDays));
            }
            else {
            	scd.setRestrictionDays(DEFAULT_QUANTITY);
            }
        	//if the shopping control is account based then use the specified action, and if not then set
        	//it to be null since action is not applicable to site based shopping controls.
        	if (scd.getAccountId() > DEFAULT_BUS_ENTITY_ID) {
            	scd.setActionCd(shoppingControl.getShoppingControlAction());
        	}
        	else {
            	scd.setActionCd(null);
        	}
        	ShoppingControlData modifiedShoppingControlData = super.update(scd);
        }
    }

}
