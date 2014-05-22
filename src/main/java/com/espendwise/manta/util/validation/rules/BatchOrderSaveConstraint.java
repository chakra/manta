package com.espendwise.manta.util.validation.rules;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.espendwise.manta.dao.AccountDAO;
import com.espendwise.manta.dao.AccountDAOImpl;
import com.espendwise.manta.dao.CatalogDAO;
import com.espendwise.manta.dao.CatalogDAOImpl;
import com.espendwise.manta.dao.ItemDAO;
import com.espendwise.manta.dao.ItemDAOImpl;
import com.espendwise.manta.dao.SiteDAO;
import com.espendwise.manta.dao.SiteDAOImpl;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.model.data.ItemMappingData;
import com.espendwise.manta.model.view.BatchOrderView;
import com.espendwise.manta.model.view.CatalogView;
import com.espendwise.manta.model.view.SiteAccountView;
import com.espendwise.manta.service.EventService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.DelimitedParser;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.criteria.CatalogSearchCriteria;
import com.espendwise.manta.util.criteria.StoreSiteCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;

public class BatchOrderSaveConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(BatchOrderSaveConstraint.class);

    private BatchOrderView batchOrderView;
    private EntityManager entityManager;
    private Locale locale;
    public interface COLUMN {
        public static final int
                ACCOUNT_ID = 0,
                SITE_REF_NUM = 1,
                DIST_SKU = 2,
                QUANTITY = 3,
                PO_NUM = 4,
                DIST_UOM = 5,
                VERSION = 6,
                SITE_ID = 7,
                STORE_SKU = 8;
    }
	/** Holds value of property sepertorChar. */
	static char sepertorChar = ',';

	/** Holds value of property quoteChar. */
	static char quoteChar = '\"';
	private Map<InboundBatchOrderData, String> lineNumberMap = new HashMap<InboundBatchOrderData, String>();
	private List<InboundBatchOrderData> parsedObjects = new ArrayList<InboundBatchOrderData>();
	private ValidationRuleResult result;
	private boolean isVersion1 = true;
	private int columnCount = 0;

    public BatchOrderSaveConstraint(BatchOrderView batchOrderView, EntityManager entityManager, Locale locale) {
        this.batchOrderView = batchOrderView;
        this.entityManager = entityManager;
        this.locale = locale;
    }

    public ValidationRuleResult apply() {
        logger.info("apply()=> BEGIN");

        result = new ValidationRuleResult();
        InputStream inputStream = null;
        try{
        	boolean fileFormatIssue = true;
        	int i = batchOrderView.getFileName().lastIndexOf('.');
        	if (i > 0){
        		String fileExtension = batchOrderView.getFileName().substring(i).toLowerCase();
        		if (fileExtension.equals(".txt") || fileExtension.equals(".csv")){
        			fileFormatIssue = false;        			
        		}
        	}
        	if (fileFormatIssue){
        		String message = I18nUtil.getMessage(locale, "validation.web.batchOrder.error.mustBeCsvOrTxtFileFormat");
        		appendErrors(message);        		
    			return result;
        	}
	        inputStream = new BufferedInputStream(new ByteArrayInputStream(batchOrderView.getFileBinaryData()));
	        if (inputStream.available() == 0){
	        	appendErrors("Stream size is 0 - " +batchOrderView.getFileName());
	        	return result;
	        }	
	        Reader reader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader r = new BufferedReader(reader);
			
	        int lineCount = 0;
			String line = null;
			while ((line = r.readLine()) != null) {
				if (lineCount++ == 0) {// skip header
					if (line.length() < 4){
						appendErrors("Data has less than 4 required columns");
					}
					continue;
				}
					
				List l = DelimitedParser.parse(line, sepertorChar,quoteChar,true);
				if (lineCount == 2){
					columnCount = l.size();
					if (columnCount > COLUMN.VERSION && l.get(COLUMN.VERSION) != null){
						String version = (String)l.get(COLUMN.VERSION);
						if (version.equals("1"))
							isVersion1 = true;
						else if (version.equals("2"))
							isVersion1 = false;
						else{
							appendErrors("Version Num (" + version + ") should be 1 or 2");
							break;
						}
					}
				}
				parseLine(l, lineCount);
			}
	
	        if (result.isFailed()) {
	            return result;
	        }
	        
	        validateBusEntitiesAndItems();
	        
	        if (result.isFailed()) {
	            return result;
	        }
	
	        logger.info("apply()=> END, SUCCESS");
	
	        result.success();
        } catch (Exception e){
        	e.printStackTrace();
        	appendErrors("Unexpected Error - " + e.getMessage());
        	return result;
        } finally {
        	if (inputStream != null){
				try{
					inputStream.close();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
        }

        return result;
    }    
       
    private void validateBusEntitiesAndItems() {    	
        List<Long> accountIdList = new ArrayList<Long>();
        List<Long> accountNotExistList = new ArrayList<Long>();
        Map<String, Long> siteMap = new HashMap<String, Long>();
    	Map<String, Long> shoppingCatalogMap = new HashMap<String, Long>();        	
        String preSiteKey = null;
        
        // get list of account ids that associated with the store and use it to validate input account id
        AccountDAO accountDao = new AccountDAOImpl(entityManager);
        List<Long> acctIdsAssocWithStore = null;
        if (isVersion1)
        	acctIdsAssocWithStore = accountDao.findAccountIdsByStore(batchOrderView.getStoreId());
        
        SiteDAO siteDao = new SiteDAOImpl(entityManager);
        CatalogDAO catalogDAO = new CatalogDAOImpl(entityManager);
        ItemDAO itemDAO = new ItemDAOImpl(entityManager);
        
        Map<String, InboundBatchOrderData> orderItems = null;			
        List<Map<String, InboundBatchOrderData>> orders = new ArrayList<Map<String, InboundBatchOrderData>>();
        
        Iterator<InboundBatchOrderData> it = parsedObjects.iterator();            
		while(it.hasNext()){
			InboundBatchOrderData flat =(InboundBatchOrderData) it.next();
			if (isVersion1){
				if (accountNotExistList.contains(flat.getAccountId()))
					continue;
				else if (!accountIdList.contains(flat.getAccountId())){
					if (!acctIdsAssocWithStore.contains(flat.getAccountId())){
						accountNotExistList.add(flat.getAccountId());
						appendErrors(flat, "validation.web.batchOrder.error.accountIdNotExistInPrimaryEntity", flat.getAccountId(), batchOrderView.getStoreId());
						continue;
					}
					BusEntityData accounD = accountDao.findAccountById(flat.getAccountId());
	    			if (accounD.getBusEntityStatusCd().equals(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE)){
	    				accountNotExistList.add(flat.getAccountId());
	    				appendErrors(flat, "validation.web.batchOrder.error.noneActiveAccountFoundForAccountId", flat.getAccountId());
	    				continue;
	    			}
	    			accountIdList.add(flat.getAccountId());
	    		}
    		}
			
			String siteKey = flat.getSiteKey();
			if (!siteMap.containsKey(siteKey)){
				StoreSiteCriteria siteCriteria = new StoreSiteCriteria();
		        siteCriteria.setStoreId(batchOrderView.getStoreId());
		        siteCriteria.setActiveOnly(true);
		        
				if (isVersion1){
					siteCriteria.setAccountIds(Utility.toList(flat.getAccountId()));			        
			        siteCriteria.setRefNums(Utility.toList(flat.getSiteRefNum()));			        
		        } else {
		        	siteCriteria.setSiteIds(Utility.toList(flat.getSiteId()));
		        }
				List<SiteAccountView> sites = siteDao.findSites(siteCriteria);
		        if (sites.isEmpty()){
		        	appendErrors(flat, "validation.web.batchOrder.error.failedToFindActiveLocation");
		        }else if (sites.size() > 1){
		        	appendErrors(flat, "validation.web.batchOrder.error.multipleLocationsMatched");
		        }else{
		        	Long siteId = sites.get(0).getBusEntityData().getBusEntityId();
		        	siteMap.put(siteKey, siteId);
		        	
		        	CatalogSearchCriteria catalogCriteria = new CatalogSearchCriteria(2);
		        	catalogCriteria.setConfiguredOnly(true);
		            catalogCriteria.setSiteId(siteId);
		            catalogCriteria.setActiveOnly(true);
		            List<CatalogView> catalogs = catalogDAO.findCatalogsByCriteria(catalogCriteria);
		            if (catalogs.isEmpty()){
    		        	appendErrors(flat, "validation.web.batchOrder.error.failedToFindActiveShoppingCatalog");
    		        }else if (catalogs.size() > 1){
    		        	appendErrors(flat, "validation.web.batchOrder.error.multipleShoppingCatalogMatched");
    		        }else{
    		        	shoppingCatalogMap.put(siteKey, catalogs.get(0).getCatalogId());
    		        }
		        }
			}
			if (siteMap.containsKey(siteKey) && shoppingCatalogMap.containsKey(siteKey)){
				List<Long> itemIds = new ArrayList<Long>();
				if (isVersion1){
					List<ItemMappingData> items = itemDAO.getDistItemMapping((Long)shoppingCatalogMap.get(siteKey), flat.distSku, flat.distUom);
					for (ItemMappingData item : items){
						itemIds.add(item.getItemId());
					}
				}else{
					CatalogStructureData item = itemDAO.getCatalogItemByStoreSku((Long)shoppingCatalogMap.get(siteKey), flat.storeSku);
					if (item != null)
						itemIds.add(item.getItemId());
					
				}

		        if (itemIds.size() == 0)
		        	appendErrors(flat, "validation.web.batchOrder.error.itemNotInShoppingCatalog");
		        else if (itemIds.size() > 1)
		        	appendErrors(flat, "validation.web.batchOrder.error.multipleItemsMatched");
		        else {    	        	
    				if (!siteKey.equals(preSiteKey)){
						preSiteKey = siteKey;
						orderItems = new HashMap<String, InboundBatchOrderData>();
						orders.add(orderItems);
    				}
    				InboundBatchOrderData existItem = orderItems.get(flat.getItemKey());
					if (existItem != null){ // consolidate the same items in a order
						existItem.qty += flat.qty;
					}else{
						orderItems.put(flat.getItemKey(), flat);
					}
				}
			}
		}
    	batchOrderView.setOrderCount(orders.size());		
	}
        
	private void parseLine(List pParsedLine, int lineNumber) {
		InboundBatchOrderData parsedObj = new InboundBatchOrderData();
		lineNumberMap.put(parsedObj, lineNumber+"");
		int columnCount = pParsedLine.size();		
				
		if (columnCount > COLUMN.ACCOUNT_ID && pParsedLine.get(COLUMN.ACCOUNT_ID) != null){
			try{
				parsedObj.setAccountId(new Integer((String)pParsedLine.get(COLUMN.ACCOUNT_ID)).intValue());
			}catch(Exception e){
				appendErrors(parsedObj, "validation.web.batchOrder.error.errorParsingAccountId", pParsedLine.get(COLUMN.ACCOUNT_ID));
				parsedObj.setAccountId(-1);
			}
		}
		if (columnCount > COLUMN.SITE_REF_NUM && pParsedLine.get(COLUMN.SITE_REF_NUM) != null)
			parsedObj.setSiteRefNum((String)pParsedLine.get(COLUMN.SITE_REF_NUM));
		if (columnCount > COLUMN.DIST_SKU && pParsedLine.get(COLUMN.DIST_SKU) != null)
			parsedObj.setDistSku((String)pParsedLine.get(COLUMN.DIST_SKU));
		if (columnCount > COLUMN.QUANTITY){
			if (pParsedLine.get(COLUMN.QUANTITY) == null){
				appendErrors(parsedObj, "validation.web.batchOrder.error.positiveQtyExpectedOnColumn", (COLUMN.QUANTITY +1));
			}else{
				try{
					parsedObj.setQty(new Integer((String)pParsedLine.get(COLUMN.QUANTITY)).intValue());
					if (parsedObj.qty <= 0){
						appendErrors(parsedObj, "validation.web.batchOrder.error.positiveQtyExpectedOnColumn", (COLUMN.QUANTITY +1));
					}
				}catch(Exception e){
					appendErrors(parsedObj, "validation.web.batchOrder.error.errorParsingQty", pParsedLine.get(COLUMN.QUANTITY));
					parsedObj.setQty(-1);
				}
			}
		}
			
		if (columnCount > COLUMN.PO_NUM &&  pParsedLine.get(COLUMN.PO_NUM) != null){
			String poNum = (String) pParsedLine.get(COLUMN.PO_NUM); 
			if (Utility.isSet(poNum) && poNum.length() > 22){
				appendErrors(parsedObj, "validation.web.batchOrder.error.poNumShouldNotExceed22Characters", pParsedLine.get(COLUMN.PO_NUM));
			}else{
				parsedObj.setPoNum(((String)pParsedLine.get(COLUMN.PO_NUM)));
			}
		}
		if (columnCount > COLUMN.DIST_UOM &&  pParsedLine.get(COLUMN.DIST_UOM) != null)
			parsedObj.setDistUom(((String)pParsedLine.get(COLUMN.DIST_UOM)));
		
		if (columnCount > COLUMN.SITE_ID &&  pParsedLine.get(COLUMN.SITE_ID) != null){
			try{
				parsedObj.setSiteId(new Long((String)pParsedLine.get(COLUMN.SITE_ID)).longValue());
			}catch(Exception e){
				appendErrors(parsedObj, "validation.web.batchOrder.error.errorParsingSiteId", pParsedLine.get(COLUMN.SITE_ID));
				parsedObj.setSiteId(-1);
			}
		}
		if (columnCount > COLUMN.STORE_SKU &&  pParsedLine.get(COLUMN.STORE_SKU) != null){
			try{
				parsedObj.setStoreSku(new Long((String)pParsedLine.get(COLUMN.STORE_SKU)).longValue());
			}catch(Exception e){
				appendErrors(parsedObj, "validation.web.batchOrder.error.errorParsingStoreSku", pParsedLine.get(COLUMN.STORE_SKU));
				parsedObj.setStoreSku(-1);
			}
		}
		
				
	     processParsedObject(parsedObj);
	}

	
	protected void processParsedObject(Object pParsedObject) {
		InboundBatchOrderData parsedObj = (InboundBatchOrderData) pParsedObject;
		if (isVersion1){
			if (parsedObj.getAccountId()==0){
				appendErrors(parsedObj, "validation.web.batchOrder.error.accountIdExpectedOnColumn", (COLUMN.ACCOUNT_ID+1));
			}			
			if (!Utility.isSet(parsedObj.siteRefNum)){
				appendErrors(parsedObj, "validation.web.batchOrder.error.locationRefNumExpectedOnColumn", (COLUMN.SITE_REF_NUM +1));
			}
			if (!Utility.isSet(parsedObj.distSku)){
				appendErrors(parsedObj, "validation.web.batchOrder.error.distSkuExpectedOnColumn", (COLUMN.DIST_SKU +1));
			}
		} else {
			if (parsedObj.siteId==0){
				appendErrors(parsedObj, "validation.web.batchOrder.error.siteIdExpectedOnColumn", (COLUMN.SITE_ID +1));
			}
			if (parsedObj.storeSku==0){
				appendErrors(parsedObj, "validation.web.batchOrder.error.storeSkuExpectedOnColumn", (COLUMN.STORE_SKU +1));
			}			
		}				
		
		parsedObjects.add(parsedObj);
	}
	
	// add error message
	private void appendErrors(InboundBatchOrderData flat, String messageKey, Object... args) {
		String lineNum = lineNumberMap.get(flat);
		String message = I18nUtil.getMessage(locale, messageKey, args);
		String errMsg = "Error at line# " +lineNum + " : " + message;
		logger.info(errMsg);
		result.failed(ExceptionReason.BatchOrderReason.ERROR_ON_LINE,
				Args.typed(lineNum, message));
		
	}
	
	private void appendErrors(String message) {
		String errMsg = "";
		errMsg += "Error: " + message;
		logger.info(errMsg);
		result.failed(ExceptionReason.BatchOrderReason.GENERAL_ERROR,
				Args.typed(message));
	}
		
	public class InboundBatchOrderData {
		private long accountId;
		private String siteRefNum;
		private String poNum;		
		private String distSku;
		private int qty;
		private String distUom = null;
		private long siteId;
		private long storeSku;		
		
		public String toString(){
			return "InboundBatchOrderData: accountId="+	accountId + ", siteRefNum=" + siteRefNum + 
				", poNum=" + poNum + ", distSku=" + distSku + ", distUom=" + distUom + ", qty="+ qty + ", siteId="+ siteId + 
				", storeSku="+ storeSku;
		}
		private String getSiteKey(){
			String key = accountId+":";
			
			if (isVersion1){
				key += siteRefNum;
			}else{
				key += siteId;
			}
			
			if (Utility.isSet(poNum))
				return key + ":" + poNum;
			else
				return key;			
		}		
		private String getItemKey(){
			if (isVersion1){
				if (Utility.isSet(distUom))
					return distSku+":"+ distUom;
				else 
					return distSku;
			}else{
				return storeSku+"";
			}
		}
		public void setAccountId(long accountId) {
			this.accountId = accountId;
		}
		public long getAccountId() {
			return accountId;
		}
		public void setSiteRefNum(String siteRefNum) {
			this.siteRefNum = siteRefNum;
		}
		public String getSiteRefNum() {
			return siteRefNum;
		}
		public void setPoNum(String poNum) {
			this.poNum = poNum;
		}
		public String getPoNum() {
			return poNum;
		}
		public void setDistSku(String distSku) {
			this.distSku = distSku;
		}
		public String getDistSku() {
			return distSku;
		}
		public void setQty(int qty) {
			this.qty = qty;
		}
		public int getQty() {
			return qty;
		}
		public void setDistUom(String distUom) {
			this.distUom = distUom;
		}
		public String getDistUom() {
			return distUom;
		}
		public void setSiteId(long siteId) {
			this.siteId = siteId;
		}
		public long getSiteId() {
			return siteId;
		}
		public void setStoreSku(long storeSku) {
			this.storeSku = storeSku;
		}
		public long getStoreSku() {
			return storeSku;
		}
	}
	
    
    public EventService getEventService() {
        return ServiceLocator.getEventService();
    }

}
