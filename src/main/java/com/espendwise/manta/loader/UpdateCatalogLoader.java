package com.espendwise.manta.loader;

import java.util.Date;

import java.util.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.lang.Long;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;


import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;

import com.espendwise.manta.dao.CatalogDAO;
import com.espendwise.manta.dao.CatalogDAOImpl;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.dao.OrderGuideDAO;
import com.espendwise.manta.dao.OrderGuideDAOImpl;
import com.espendwise.manta.dao.DistributorDAO;
import com.espendwise.manta.dao.DistributorDAOImpl;

import com.espendwise.manta.model.data.OrderGuideData;
import com.espendwise.manta.model.view.UpdateCatalogUploadView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.model.view.ItemLoaderView;
import com.espendwise.manta.model.view.CatalogAssocView;
import com.espendwise.manta.service.CatalogService;
//import com.espendwise.manta.service.CatalogSearchCriteria;
//import com.espendwise.manta.service.StoreListEntity;

//import com.espendwise.manta.dao.AccountDAO;
//import com.espendwise.manta.dao.AccountDAOImpl;
//import com.espendwise.manta.dao.SiteDAO;
//import com.espendwise.manta.dao.SiteDAOImpl;
//import com.espendwise.manta.model.data.BusEntityAssocData;
//import com.espendwise.manta.model.data.BusEntityData;
//import com.espendwise.manta.model.data.LanguageData;
//import com.espendwise.manta.model.data.PropertyData;
//import com.espendwise.manta.model.view.AllStoreIdentificationView;
//import com.espendwise.manta.model.view.SiteAccountView;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.RefCodeNamesKeys;
import com.espendwise.manta.util.Utility;
//import com.espendwise.manta.util.criteria.StoreAccountCriteria;
//import com.espendwise.manta.util.criteria.StoreSiteCriteria;
import com.espendwise.manta.util.criteria.StoreDistributorCriteria;


public class UpdateCatalogLoader extends BaseLoader {
	//private List<InboundBatchOrderData> parsedObjects = new ArrayList<InboundBatchOrderData>();
	//protected int currentLineNumber = 0; // index start with 1

	private final static int VERSION_NUMBER = 0;
	private final static int ACTION = 1;
	private final static int LINE_NUMBER = 2;
	private final static int CATALOG_ID = 3;
	private final static int STORE_SKU = 4;
	private final static int PRICE = 5;
	private final static int COST = 6;
	private final static int CATEGORY = 7;
	private final static int DIST_NAME = 8;
	private final static int DIST_SKU = 9;
	private final static int DIST_UOM = 10;
	private final static int DIST_PACK = 11;
	private final static int ORDER_GUIDE_NAME = 12;
	private final static int TAX_EXEMPT = 13;
	private final static int CUSTOMER_SKU = 14;
	private final static int STANDARD_PRODUCT_LIST = 15;

	private int reqColumnCountForVersion = STANDARD_PRODUCT_LIST+1;
	private final static String tempTable = "CLT_UPDATE_CATALOG_LOADER";
	private final static String catalogloader= "updateCatalogLoader";
	
	private List<UpdateCatalogUploadView> uploadList = new ArrayList<UpdateCatalogUploadView>();	
	
	private Map<String, Set<String>> eItemKeyToCatalogsMap = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> categoryNameToCatalogsMap = new HashMap<String, Set<String>>();
	private Map<String, Set<Long>> eItemKeyToItemIdMap = new HashMap<String, Set<Long>>();
	private Map<String, Set<Long>> eItemCatalogKeyToDistIdMap = new HashMap<String, Set<Long>>();
	private Map<String, Set<Long>> sItemKeyToItemIdMap = new HashMap<String, Set<Long>>();
	String storeCatalogId="";

	private int storeNum = 0;
	private Date runDate = new Date();
	private boolean isVersion1 =false;
	private boolean isVersion2 =false;
	
	
/*	private static final String insertSql = 
		"insert into " + tempTable + "(" +
				" VERSION_NUMBER, ACTION, LINE_NUMBER, CATALOG_ID, STORE_SKU, PRICE, COST, CATEGORY, DIST_NAME, DIST_SKU, DIST_UOM, DIST_PACK, ORDER_GUIDE_NAME, TAX_EXEMPT, CUSTOMER_SKU, STANDARD_PRODUCT_LIST " +
				" ,ADD_BY,ADD_DATE)" +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
				",?,sysdate)";


    private static final String trimDataSql = 
    	    "update " + tempTable +" set " +
			"VERSION_NUMBER	= TRIM(VERSION_NUMBER)," +
			"ACTION = TRIM(ACTION)," +
			"LINE_NUMBER = TRIM(LINE_NUMBER)," +
			"CATALOG_ID = TRIM(CATALOG_ID), " +
			"STORE_SKU = TRIM(STORE_SKU), " +
			"PRICE = TRIM(PRICE)," +
			"COST = TRIM(COST), " +
			"CATEGORY = TRIM(CATEGORY), " +
			"DIST_NAME = TRIM(DIST_NAME), " +
			"DIST_SKU = TRIM(DIST_SKU), " +
			"DIST_UOM = TRIM(DIST_UOM), " +
			"DIST_PACK = TRIM(DIST_PACK), " +
			"ORDER_GUIDE_NAME = TRIM(ORDER_GUIDE_NAME), " +
			"TAX_EXEMPT = TRIM(TAX_EXEMPT), " +
			"CUSTOMER_SKU = TRIM(CUSTOMER_SKU), " +
			"STANDARD_PRODUCT_LIST = TRIM(STANDARD_PRODUCT_LIST)";
*/
    public UpdateCatalogLoader(EntityManager entityManager, Locale locale, Long currStoreId, InputStream inputStream,String streamType) {
    	super(entityManager, locale, currStoreId, inputStream, streamType);
    	useHeaderLine=false;
    }	
	    
    public CatalogService getCatalogService() {
        return ServiceLocator.getCatalogService();
    }

	protected Logger log = Logger.getLogger(UpdateCatalogLoader.class);

	/**
	 *passed in the parsed line will preform the necessary logic of populating the object
	 */
	@Override
	public void parseLine(List pParsedLine) throws Exception{
		if(currentLineNumber == 1){
			List<String> headers = getHeaderColumns();
			for (int i = 0; i < headers.size(); i++) {
				columnNumberMap.put(new Integer(i), headers.get(i));
			}	
		}
		parseDetailLine(pParsedLine);
		currentLineNumber++;
	}
	@Override
    protected void parseDetailLine(List parsedLine) throws Exception {
		log.info("Line# " + (currentLineNumber) );
		boolean isActionAdd = false;
		int columnIndex = 0;
		UpdateCatalogUploadView uploadView = new UpdateCatalogUploadView();
		uploadList.add(uploadView);		
		uploadView.setLineNumber(currentLineNumber);
		
    	validateAndPopulateProperty(VERSION_NUMBER, "versionNumber", parsedLine, uploadView, true);
		String version = uploadView.getVersionNumber();
		if (version != null){
			if (version.equals("1")){
				isVersion1 = true;
			    reqColumnCountForVersion = 6;
			}else if (version.equals("2")) {
				isVersion2 = true;
		    	reqColumnCountForVersion = 7;
			} else{
				reqColumnCountForVersion = 0;
				appendErrorsOnLine("validation.loader.error.wrongVersionNum",columnNumberMap.get(VERSION_NUMBER), version);
			}
		}
		
    	validateAndPopulateProperty(ACTION, "action", parsedLine, uploadView, true);
		String action = uploadView.getAction();
		if ((action != null && (action.trim().length()>1 || !"ACD".contains(action.trim().toUpperCase())))){
			String columnName = columnNumberMap.get(ACTION);
			appendErrorsOnLine("validation.loader.error.incorrectAction",columnName, action);
		}
		if (actionAdd.equals(action)){
			reqColumnCountForVersion= reqColumnCountForVersion+2;
			isActionAdd = true;
		}
		
    	validateAndPopulateProperty(LINE_NUMBER, "lineNumber", parsedLine, uploadView, true);
    	// validate list of catalog Ids
    	validateAndPopulateProperty(CATALOG_ID, "catalogIds", parsedLine, uploadView, true);
   	
    	validateAndPopulateProperty(STORE_SKU, "storeSku", parsedLine, uploadView, isVersion1);
    	validateAndPopulateProperty(PRICE, "price", parsedLine, uploadView, true);
    	validateAndPopulateProperty(COST, "cost", parsedLine, uploadView, false);
    	validateAndPopulateProperty(CATEGORY, "category", parsedLine, uploadView, isActionAdd);
    	validateAndPopulateProperty(DIST_NAME, "distName", parsedLine, uploadView, isActionAdd);
    	validateAndPopulateProperty(DIST_SKU, "distSku", parsedLine, uploadView, isVersion2);
    	validateAndPopulateProperty(DIST_UOM, "distUom", parsedLine, uploadView, isVersion2);
       	validateAndPopulateProperty(DIST_PACK, "distPack", parsedLine, uploadView, false);
       	validateAndPopulateProperty(ORDER_GUIDE_NAME, "orderGuideNames", parsedLine, uploadView, false);
       	validateAndPopulateProperty(TAX_EXEMPT, "taxExempt", parsedLine, uploadView, false);
       	validateAndPopulateProperty(CUSTOMER_SKU, "customerSku", parsedLine, uploadView, false);
       	validateAndPopulateProperty(STANDARD_PRODUCT_LIST, "standardProductList", parsedLine, uploadView, false);


		
		int columnCount = parsedLine.size();
		if ( columnCount < reqColumnCountForVersion){
			//appendErrorsOnLine("validation.loader.error.columnCountLessThanMinimum", columnCount, reqColumnCountForVersion);
			//return;
		}
		//validate store sku
		IdValidator idValidator = Validators.getIdValidator();
		ValidationResult vr;
		String value = null;
		if (isVersion1 ){
        	value = (Utility.isSet(uploadView.getStoreSku())) ? uploadView.getStoreSku().toString() : ""; 
            vr = idValidator.validate( value , new NumberErrorWebResolver("validation.catalog.loader.error.wrongPositiveValue"));
            if (vr != null){
            	appendErrorsOnLine("validation.catalog.loader.error.wrongPositiveValue", columnNumberMap.get(STORE_SKU), value);            	
            }
        }
		//validate Price and Cost are positive
		AmountValidator amountValidator = Validators.getAmountValidator(20,8);
		if (uploadView.getPrice()!= null) {
	        vr = amountValidator.validate(uploadView.getPrice().toString(), new NumberErrorWebResolver("validation.catalog.loader.error.wrongPositiveValue"));
	        if (vr != null) {
	           	appendErrorsOnLine("validation.catalog.loader.error.wrongPositiveValue", columnNumberMap.get(PRICE), uploadView.getPrice());            	
	        }
		}
        if (uploadView.getCost() != null) {
	        vr = amountValidator.validate(uploadView.getCost().toString(), new NumberErrorWebResolver("validation.catalog.loader.error.wrongPositiveValue"));
	        if (vr != null) {
	           	appendErrorsOnLine("validation.catalog.loader.error.wrongPositiveValue", columnNumberMap.get(COST), uploadView.getCost());            	
	        }
        }
		
		if (Utility.isSet(uploadView.getOrderGuideNames())&& uploadView.getAction().equalsIgnoreCase("C")) {
			appendErrorsOnLine("validation.catalog.loader.error.columnMustBeEmptyForAction", columnNumberMap.get(ORDER_GUIDE_NAME), uploadView.getAction().toUpperCase());
		}

	   	//validate catalog ids format
		if (Utility.isSet(uploadView.getCatalogIds())){
			log.info("parseDetailLine()===> uploadView.getCatalogIds().size()= "+ uploadView.getCatalogIds().size());
    		StringBuffer errs = new StringBuffer();
			for (int i=0; i<uploadView.getCatalogIds().size(); i++){
    	        if (Utility.isSet(uploadView.getCatalogIds().get(i))) {
    	        	value = ((String)uploadView.getCatalogIds().get(i)).trim(); 
    	        	vr = idValidator.validate( value , new NumberErrorWebResolver("validation.loader.error.columnMustBeListOfNumbers"));
    	            if (vr != null){
	    	            errs.append((errs.length() == 0)? "" : ",") ;
	    	            errs.append(value);
    	            }
     	        }
    		}
            if (errs.length() > 0) {
            	appendErrorsOnLine("validation.catalog.loader.error.columnMustBeListOfNumbers", columnNumberMap.get(CATALOG_ID), errs.toString());
            }
    	}

    }	
	   
	@Override
	public void doPostProcessing() throws Exception {
		if (getErrors().size()>0) {
			return;
		}	
		log.info("Start doPostProcessing()....");
		
		CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);	
 /*       Collections.sort(uploadList, new Comparator<UpdateCatalogUploadView>() {
            @Override
            public int compare(UpdateCatalogUploadView o1, UpdateCatalogUploadView o2) {
                if (o1.getCatalogId() != null && o2.getCatalogId() != null) {
                    return o1.getCatalogId().compareTo(o2.getCatalogId());
                }
                return 0;
            }
        });		
*/		
		
		Set<Long> catalogIds = new HashSet<Long>();
		Set<String> ogNames =  new HashSet<String>();
		Set<String> distNames = new HashSet<String>();
		for (UpdateCatalogUploadView uploadView : uploadList){
			for (int i=0; uploadView.getCatalogIds()!=null && i<uploadView.getCatalogIds().size(); i++){
				Long cId = null;
				try {
					cId = new Long(((String)(uploadView.getCatalogIds().get(i))).trim());
				} catch (Exception e) {}
				if (cId != null){
					catalogIds.add (cId);
				}
			}
			for (int i=0; uploadView.getOrderGuideNames()!=null && i<uploadView.getOrderGuideNames().size(); i++){
				ogNames.add(((String)(uploadView.getOrderGuideNames().get(i))).trim());
			}
			if (Utility.isSet(uploadView.getDistName())){
				distNames.add(uploadView.getDistName());
			}
		}
		log.info("doPostProcessing()===> 1)Validate  catalogIds = "+ catalogIds);
		log.info("doPostProcessing()===> 2)Validate  orderGuideNames= "+ ogNames);
		log.info("doPostProcessing()===> 3)Validate  distNames= "+ distNames);
		List<String> activeCatalogIds = getActiveCatalogIds(catalogIds);
		if (!Utility.isSet(activeCatalogIds)) {
			appendErrors("validation.catalog.loader.error.noActiveCatalogs", catalogIds.toString(),getStoreId() );
			return;
		}
		storeCatalogId = getStoreCatalogId();
		Map<String, List<String>> ogMap = getOrderGuides(ogNames);
		Map<String, List<Long>> distMap = getDistributors(distNames);
		
		/*
		List<ItemLoaderView> storeCatalogItems = getExistingItems(Utility.toList(storeCatalogId));
		List<ItemLoaderView> existingItems = getExistingItems(activeCatalogIds);
		List<ItemView> existingCategories = getExistingCategories(activeCatalogIds);
		*/
		
		Map<String, Boolean> splMap = new HashMap<String, Boolean>();
		prepareItemMaps(activeCatalogIds);
		
		Map<String, String> allKeyMap = new HashMap<String, String>(); 
		for (UpdateCatalogUploadView uploadView : uploadList){
			currentLineNumber = uploadView.getLineNumber();
//			if (actionAdd.equals(uploadView.getAction())){
//				toAddMap.put(key,uploadView);
//			} else if (actionChange.equals(uploadView.getAction())) {
//				toChangeMap.put(key,uploadView);
//			} else if (actionDelete.equals(uploadView.getAction())){
//				toDeleteMap.put(key,uploadView);
//			}

			//---------- Validate that catalog Ids belong to the current store 
			validateCatalogs(activeCatalogIds, uploadView);
			//---------- Validation to find records with duplicate keys
			validateCatalogsAcrossRows(allKeyMap, uploadView);

			//-------- Validation that all provided order guides belong to provided catalogs
			if (isAction(actionAdd,uploadView.getAction())){
				validateOrderGuide( ogMap, activeCatalogIds, uploadView);
			}
			//--------- Validation distributors
			if ( isAction(actionAdd, uploadView.getAction()) || isVersion2 ||
					Utility.isSet(uploadView.getDistName())|| 
					Utility.isSet(uploadView.getDistPack())||
					Utility.isSet(uploadView.getStandardProductList())){
				validateDistributors(distMap, activeCatalogIds, uploadView);
			}	
			//--------- Validation to find existing items that have action 'A'
			if (!activeCatalogIds.isEmpty()){
				validateItems( uploadView);
			}

			//-------- Validation inconsistency SPL 
			//(probably this wouldn't work because validation  on unique of item key covers this one)
			if (Utility.isSet(uploadView.getStandardProductList())) {
				//log.info("Start doPostProcessing()....uploadView.getStandardProductList()="+ uploadView.getStandardProductList());

				String keyName = (String)(getLineKey(uploadView)).getObject1();
				String keyVal =(String)(getLineKey(uploadView)).getObject2();
				if (isUnknownDistributor(uploadView )){
					appendErrorsOnLine("validation.catalog.loader.error.columnCannotBeEmptyIf", columnNumberMap.get(DIST_NAME), columnNumberMap.get(STANDARD_PRODUCT_LIST));
				} /*else  {
					String distName = (Utility.isSet(uploadView.getDistName())) ? uploadView.getDistName() : getDistNameById(uploadView.getDistId());
					String itemDistKey = keyVal+"~" + distName;
					if (uploadView.getStandardProductList() != null){
						Boolean spl = (Boolean)splMap.get(itemDistKey);
						if ( spl == null ) {
							splMap.put(itemDistKey, uploadView.getStandardProductList());
						} else if ( !uploadView.getStandardProductList().equals(spl)){
							appendErrorsOnLine("validation.catalog.loader.error.inconsistencySPL", keyName, keyVal, distName );
						}
					}
				}*/
			}
		}
		
		
         log.info("doPostProcessing() ===> END ");
  
    }	
    

    private List<String> getActiveCatalogIds(Set<Long> catalogIds){
		List<String> activeCatalogs = new ArrayList<String>();
    	if (catalogIds.isEmpty()){
    		return activeCatalogs;
    	}
    	CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
		List<CatalogData> catalogs = catalogDao.findCatalogs (getStoreId(), new ArrayList(catalogIds));
		for (CatalogData cat : catalogs){
			activeCatalogs.add(cat.getCatalogId().toString());
		}
		return activeCatalogs;
    }
    
    private String getStoreCatalogId() throws Exception{
    	CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
    	CatalogAssocView caV = catalogDao.findEntityCatalog (getStoreId(), RefCodeNames.CATALOG_ASSOC_CD.CATALOG_STORE);
    	Long storeCatalogId = new Long(0);
    	if (caV != null && caV.getCatalog() != null) {
    		storeCatalogId = caV.getCatalog().getCatalogId();
    	}
    	log.info("getStoreCatalogId() ===> storeCatalogId = "+ storeCatalogId);
    	return storeCatalogId.toString();
    }
    
    private List<ItemView> getExistingCategories(List<String> activeCatalogIds) throws Exception{
    	CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
    	List<ItemView> categories = catalogDao.findCatalogCategories(toIds(activeCatalogIds)); 
    	return categories;
    }
    
    private Map<String, List<String>> getOrderGuides(Set<String> ogNames){
        Map<String, List<String>> ogMap = new HashMap<String, List<String>>();
    	if (ogNames.isEmpty()){
    		return ogMap;
    	}
    	OrderGuideDAO ogDao = new OrderGuideDAOImpl(entityManager);
    	List<String> ogTypes = new ArrayList<String>();
    	ogTypes.add(RefCodeNames.ORDER_GUIDE_TYPE_CD.ORDER_GUIDE_TEMPLATE);
    	ogTypes.add(RefCodeNames.ORDER_GUIDE_TYPE_CD.SITE_ORDER_GUIDE_TEMPLATE);
    	ogTypes.add(RefCodeNames.ORDER_GUIDE_TYPE_CD.BUYER_ORDER_GUIDE);
    	List<OrderGuideData> ogs = ogDao.findOrderGuides(ogNames, ogTypes );
        if (ogs != null) {
        	for (OrderGuideData og : ogs){
        		List<String> ogDs = (List<String>)ogMap.get(og.getShortDesc());
        		if (ogDs ==null) {
        			ogDs = new ArrayList();
        			ogMap.put(og.getShortDesc(), ogDs);
        		}
    			ogDs.add(og.getCatalogId().toString());
         	}
        }
    	return ogMap;
    }
    
    private List<ItemLoaderView> getExistingItems(List<String> activeCatalogIds){
    	CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
		List<ItemLoaderView> eItemKeys = catalogDao.findItems (toIds(activeCatalogIds) );

    	return eItemKeys;
    }
    
    private void prepareItemMaps( List<String> activeCatalogIds) throws Exception {
		List<ItemLoaderView> storeCatalogItems = getExistingItems(Utility.toList(storeCatalogId));
		List<ItemLoaderView> existingItems = getExistingItems(activeCatalogIds);
		List<ItemView> existingCategories = getExistingCategories(activeCatalogIds);

    	// Map : active catalog to list of categories 
    	if (Utility.isSet(existingCategories)) {
			for(ItemView categ: existingCategories){
				String key = categ.getShortDesc();
				Set<String> catIds = (Set<String>)categoryNameToCatalogsMap.get(key);
				if (!Utility.isSet(catIds)) {
					catIds = new HashSet<String>();
					categoryNameToCatalogsMap.put(key, catIds);
				}
				catIds.add(categ.getCatalogId().toString());
			}
    		
    	}
    	
    	// Map : Item key to Item Id for store catalog
    	if (Utility.isSet(storeCatalogItems)) {
			for(ItemLoaderView sItem: storeCatalogItems){
				String key = sItem.getStoreSku();
				if (isVersion2){
					key = sItem.getDistSku()+","+ sItem.getDistUom();
				}
				Set<Long> storeItemIds = (Set<Long>)sItemKeyToItemIdMap.get(key);
				if (!Utility.isSet(storeItemIds)) {
					storeItemIds = new HashSet<Long>();
					sItemKeyToItemIdMap.put(key, storeItemIds);
				}
				storeItemIds.add(sItem.getItemId());
			}
    		
    	}
    	// Map : Item key to Item Id for active account and shopping catalogs that are listed in the file
    	if (!existingItems.isEmpty()){
			for(ItemLoaderView eItem: existingItems){
				String key = eItem.getStoreSku();
				if (isVersion2){
					key = eItem.getDistSku()+","+ eItem.getDistUom();
				}
				
				Set<String> catIds = (Set<String>)eItemKeyToCatalogsMap.get(key);
				Set<Long> itemIds = (Set<Long>)eItemKeyToItemIdMap.get(key);
				
				Set<Long> distIds = null;
				if (eItem.getDistId() != null){
					String distKey = key+"~"+eItem.getCatalogId(); 
				    distIds = (Set<Long>)eItemCatalogKeyToDistIdMap.get(distKey);
					if (!Utility.isSet(distIds)) {
						distIds = new HashSet<Long>();
						eItemCatalogKeyToDistIdMap.put(distKey, distIds);
					}
					distIds.add(eItem.getDistId());
				}
				
				if (!Utility.isSet(catIds)) {
					catIds = new HashSet<String>();
					eItemKeyToCatalogsMap.put(key, catIds);
				}
				if (!Utility.isSet(itemIds)) {
					itemIds = new HashSet<Long>();
					eItemKeyToItemIdMap.put(key, itemIds);
				}
				
				//eItemMap.put(key , eItem);
				catIds.add(eItem.getCatalogId().toString());
				itemIds.add(eItem.getItemId());
				
			}
		}
		log.info("prepareItemMaps() ===> eItemKeyToCatalogsMap= " + eItemKeyToCatalogsMap);
    	
    	//

    }
    
    private void validateItems(   UpdateCatalogUploadView uploadView){

        	Pair keyObj = getLineKey(uploadView);
    		String itemKey = (String)keyObj.getObject2();
    		String keyName = (String)keyObj.getObject1();
    		
    		String category = uploadView.getCategory();
    		
    		List<String> catalogIds = toList( eItemKeyToCatalogsMap.get(itemKey));
    		
    		List<String> categoryCatalogIds = toList( categoryNameToCatalogsMap.get(uploadView.getCategory()));
			List<Long> itemIds =toList( eItemKeyToItemIdMap.get(itemKey));
			List<Long> storeCatalogItemIds = toList( sItemKeyToItemIdMap.get(itemKey));
			//List<String> categoryNames =toList( catalogToCategoryNameMap.get(itemKey));
			if (catalogIds==null){
    			catalogIds = new ArrayList<String>();
    		}
        	log.info("validateItems() ===> uploadView.getCatalogIds()= " + uploadView.getCatalogIds());
        	log.info("validateItems() ===> search in catalogIds= " + catalogIds);
        	log.info("validateItems() ===> search in storeCatalogItemIds= " + storeCatalogItemIds);
			
        	// check catagory for active catalogs
    		List<String> hasInCategCatalogs = new ArrayList<String>(uploadView.getCatalogIds());	
    		boolean b = hasInCategCatalogs.removeAll(categoryCatalogIds);
        	log.info("validateItems() ===> hasInCatalogs= " + hasInCategCatalogs);
       		if (hasInCategCatalogs.size() != 0){
       			appendErrorsOnLine("validation.catalog.loader.error.categoryNotExists", hasInCategCatalogs, category );
    		} 
       		
        	// check item in the store catalog
			if (storeCatalogItemIds != null && storeCatalogItemIds.size()>1 ){
    			appendErrorsOnLine("validation.catalog.loader.error.multipleStoreItemsForItemKey", storeCatalogId, keyName, itemKey );
    			return;//if more than 1 item in the store catalog we should not validate shopping catalogs
			} else if (!Utility.isSet(storeCatalogItemIds)){
       			appendErrorsOnLine("validation.catalog.loader.error.itemKeyNotExistsForStore", storeCatalogId, keyName, itemKey );
				return; //if no item in the store catalog we should not validate shopping catalogs
			} else {
    			uploadView.setItemId((Long)(storeCatalogItemIds.get(0)));
			}

			// check item in the active catalogs
        	if (isAction(actionAdd, uploadView.getAction())){
        		//if (itemView != null ){
        		List<String> hasInCatalogs = new ArrayList<String>(uploadView.getCatalogIds());	
        		b = hasInCatalogs.retainAll(catalogIds);
            	log.info("validateItems() ===> hasInCatalogs= " + hasInCatalogs);
           		if (hasInCatalogs.size() > 0){
        			appendErrorsOnLine("validation.catalog.loader.error.itemKeyExists", hasInCatalogs, keyName, itemKey );
        		}
        	} else if (isAction(actionChange, uploadView.getAction()) || isAction(actionDelete, uploadView.getAction()) ){
        		//if (itemView == null){
        		List<String> hasInCatalogs = new ArrayList<String>(uploadView.getCatalogIds());	
        		b = hasInCatalogs.removeAll(catalogIds);
           		if (hasInCatalogs.size() != 0){
           			appendErrorsOnLine("validation.catalog.loader.error.itemKeyNotExists", hasInCatalogs, keyName, itemKey );
        		} else {
        			if (itemIds != null && itemIds.size()>1 ){
            			appendErrorsOnLine("validation.catalog.loader.error.multipleItemsForItemKey", keyName, itemKey );
        			} else if (!Utility.isSet(itemIds)){
        				appendErrorsOnLine("validation.catalog.loader.error.noItemsForItemKey",   keyName, itemKey );
        			} else {
            			uploadView.setItemId((Long)(itemIds.get(0)));
            			
        			}
        		}
        	}
    		
    }
  
/*    private void validateItems(Map<String, UpdateCatalogUploadView> uploadMap, Map<String, List<String>> itemToCatalogsMap, String action){
    	Iterator it = uploadMap.keySet().iterator();
    	while (it.hasNext()){
    		String itemKey = (String)it.next(); 
    		UpdateCatalogUploadView uploadView = (UpdateCatalogUploadView)uploadMap.get(itemKey);
    		//ItemLoaderView itemView = (ItemLoaderView)itemMap.get(itemKey);
    		List<String> catalogIds = (List<String>)itemToCatalogsMap.get(itemKey);
    		if (catalogIds==null){
    			catalogIds = new ArrayList<String>();
    		}
        	log.info("validateItems() ===> uploadView.getCatalogIds()= " + uploadView.getCatalogIds());
        	log.info("validateItems() ===> find in CatalogIds= " + catalogIds);
    		
        	if (actionAdd.equals(action)){
        		//if (itemView != null ){
        		List<String> hasInCatalogs = new ArrayList<String>(uploadView.getCatalogIds());	
        		boolean b = hasInCatalogs.retainAll(catalogIds);
            	log.info("validateItems() ===> hasInCatalogs= " + hasInCatalogs);
           		if (hasInCatalogs.size() > 0){
        			appendErrors("validation.catalog.loader.error.itemKeyNotUnique", uploadView.getLineNumber(), hasInCatalogs, getLineKey(uploadView).getObject1(),getLineKey(uploadView).getObject2() );
        		}
        	} else if (actionChange.equals(action) || actionDelete.equals(action) ){
        		//if (itemView == null){
        		List<String> hasInCatalogs = new ArrayList<String>(uploadView.getCatalogIds());	
        		boolean b = hasInCatalogs.removeAll(catalogIds);
           		if (hasInCatalogs.size() != 0){
        			appendErrors("validation.catalog.loader.error.itemKeyNotExists", uploadView.getLineNumber(), hasInCatalogs, getLineKey(uploadView).getObject1(),getLineKey(uploadView).getObject2() );
        		}
        	}
    		
    	}
    }
*/    
    private void validateOrderGuide ( Map<String, List<String>> ogMap, List<String> activeCatalogIds, UpdateCatalogUploadView uploadView ){
		//-------- Validation that all provided order guides belong to provided catalogs
		List<String> lineOGNames = uploadView.getOrderGuideNames();
		if (lineOGNames == null) {
			return;
		}
    	Set<String> errOGNames = new HashSet<String>();
    	log.info("validateOrderGuide ==> lineOGNames="+lineOGNames);
		for (String ogName : lineOGNames) {
	    	log.info("validateOrderGuide ==> ogName="+ogName);
			boolean isFoundCatalogForOG = false; 
			if (ogMap!= null && !ogMap.isEmpty() && Utility.isSet(ogName)) {
				 List<String> catalogsForOGName = (List<String>)ogMap.get(ogName);			
		    	log.info("validateOrderGuide ==> catalogsForOGName="+catalogsForOGName);
				 if (catalogsForOGName != null ){
					for (String catId : catalogsForOGName){
						if (uploadView.getCatalogIds().contains(catId) &&
							activeCatalogIds!= null && 
							activeCatalogIds.contains(catId)) {
						  isFoundCatalogForOG=true;
						  break;
						} 
					}
				 }
			}
			if (!isFoundCatalogForOG){
				 errOGNames.add(ogName);
			}
		}
		log.info("validateOrderGuide ==> errOGNames="+errOGNames);
		if (!errOGNames.isEmpty()){
			appendErrorsOnLine("validation.catalog.loader.error.ogListNotBelongToCatalogList", errOGNames, uploadView.getCatalogIds());
		}
    }
    
    private void validateCatalogs (List<String> activeCatalogIds, UpdateCatalogUploadView uploadView){
		currentLineNumber= uploadView.getLineNumber();
		//---------- Validate that catalog Ids belong to the current store 
		StringBuffer errIds = new StringBuffer();
		List<String> validIds = new ArrayList<String>();
		
		for (Object catalogId : uploadView.getCatalogIds()){
			if (!activeCatalogIds.contains((String)catalogId)){
				errIds.append((errIds.length() != 0) ? "," : "");
				errIds.append(catalogId);
			} else {
				validIds.add((String)catalogId);
			}
		}
		if (errIds.length() != 0){
			appendErrorsOnLine("validation.catalog.loader.error.catalogNotExist", uploadView.getCatalogIds(), getStoreId());
		}
		if (validIds.size() != 0){
			uploadView.setCatalogIds(validIds);
		}
		
    }
    
    private void validateCatalogsAcrossRows(Map<String, String> allKeyMap ,UpdateCatalogUploadView uploadView){
		//---------- Validation to find records with duplicate keys

    	Pair keyObj = getLineKey(uploadView);
		String key = (String)keyObj.getObject2();
		String keyName = (String)keyObj.getObject1();
		for (int i = 0; i < uploadView.getCatalogIds().size(); i++){
			String cat =  ((String)uploadView.getCatalogIds().get(i)).trim() ;
			String catEntry = (String)allKeyMap.get(key);
			//log.info("doPostProcessing()===> 2) Validation to find duplicates; key = "+ key + ", catEntry = " +catEntry);
			if (Utility.isSet(catEntry) && catEntry.equals(cat)){
				//appendErrorsOnLine("validation.catalog.loader.error.duplicateKeyExist", keyName, key);
			} else {
				allKeyMap.put(key, cat);
			}
		}

    }
    
    private void validateDistributors (Map<String, List<Long>> distMap, List<String> activeCatalogIds, UpdateCatalogUploadView uploadView) {	
		//--------- Validation distributors
    	String distName = uploadView.getDistName();
    	List<Long> distIds = null ;//new ArrayList<Long>();
    	if (distMap != null && Utility.isSet(distName)){
    		distIds = (List<Long>)distMap.get(distName);
    		log.info("validateDistributors ==> distName="+ distName + ", distIds ="+distIds );
    		if (!Utility.isSet(distIds) ){
    			appendErrorsOnLine("validation.catalog.loader.error.notExistDistName", distName );
    		} else if (distIds.size() > 1) {
    			appendErrorsOnLine("validation.catalog.loader.error.multipleDistName", distName );
    		} else {
    			uploadView.setDistId(distIds.get(0));
    		}
    	} else if (!Utility.isSet(distName) ){
    		if (!eItemCatalogKeyToDistIdMap.isEmpty() && !uploadView.getCatalogIds().isEmpty() ) {
    			for (String catalogId : (List<String>)uploadView.getCatalogIds()) {
	    			String distKey = (String)getLineKey(uploadView).getObject2()+"~" + catalogId;
	    			distIds =toList(eItemCatalogKeyToDistIdMap.get(distKey));
	    			
	    			if (!Utility.isSet(distIds)){
	        			appendErrorsOnLine("validation.catalog.loader.error.undefindDistributor", getLineKey(uploadView).getObject1(),getLineKey(uploadView).getObject2(), catalogId  );
	    			} else if (distIds.size() > 1){
	        			appendErrorsOnLine("validation.catalog.loader.error.multipleDistributor", getLineKey(uploadView).getObject1(),getLineKey(uploadView).getObject2(), catalogId  );
	    			} else {
	    				uploadView.setDistId(distIds.get(0));
	    			}
    			}
    		} else {
    			appendErrorsOnLine("validation.catalog.loader.error.undefindDistributorAndCatalog", getLineKey(uploadView).getObject1(),getLineKey(uploadView).getObject2() );
    		} 
    	}

    }

    private Map<String, List<Long>> getDistributors(Set<String> distNames){
    	Map<String, List<Long>> distributorsMap = new HashMap<String, List<Long>>() ;
    	if (distNames.isEmpty()){
    		return distributorsMap;
    	}
    	List<String> distNameList = new ArrayList<String>();
    	for (String name : distNames){
    		distNameList.add(name.toUpperCase());
    	}
    	//distNameList.addAll(distNames);
    	DistributorDAO distDao = new DistributorDAOImpl(entityManager);
    	StoreDistributorCriteria crit = new StoreDistributorCriteria();
    	crit.setStoreId(getStoreId());
    	crit.setDistributorNames(distNameList);
    	List<BusEntityData> distributors = distDao.findDistributors(crit);
    	if (distributors != null){
    		List<Long> distIds = null;
    		for (BusEntityData dist : distributors){
    			if (RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE.equals(dist.getBusEntityStatusCd())) {
	    			distIds = (List<Long>)distributorsMap.get(dist.getShortDesc());
	    			if (distIds == null){
	    				distIds = new ArrayList<Long>();
	    				distributorsMap.put(dist.getShortDesc(), distIds);
	    			}
	    			distIds.add(dist.getBusEntityId());
	    		}	
    		}
    		
    	}
    	
    	return distributorsMap;
    }
    
    private boolean isUnknownDistributor(UpdateCatalogUploadView uploadView){
		//validate that Distributor Name is required if SPL exists
		boolean b = !Utility.isSet(uploadView.getDistName()) && uploadView.getDistId() == null; 
		//if action 'C' validate SPL  Name consistency (between 2 line with equal Distributor)
    	return b;
    }
    
    private Pair getLineKey (UpdateCatalogUploadView uploadView){
		String key = uploadView.getStoreSku().toString();
		String keyName = columnNumberMap.get(STORE_SKU);
		if (isVersion2){
			keyName = columnNumberMap.get(DIST_SKU) + "," + columnNumberMap.get(DIST_UOM);;
			key = uploadView.getDistSku().trim()+ "," + uploadView.getDistUom().trim();
		} 
		return new Pair(keyName, key);
    }
    
	@Override
	public List<String> getHeaderColumnsAbs() {
		List<Pair<String, String>> headerColumns = RefCodeNamesKeys.getRefCodeValues(Constants.UPDATE_CATALOG_LOADER_PROPERTY.class, false);
		List<String> headers = new ArrayList<String>();
		for(int i=0; i<headerColumns.size(); i++) {
            Pair<String, String> h = headerColumns.get(i);
            headers.add(h.getObject2());
        }
		log.info("getHeaderColumnsAbs() headers = "+ headers);	
		return headers;
	}
    private <T> List<Long> toIds(Collection list) {
        List<Long> ids = Utility.emptyList(Long.class);
        if (list == null) {
            return ids;
        }
        for (Object obj : list) {
            if (obj != null && obj instanceof String ) {
                Long id = new Long((String)obj);
                ids.add(id);
            }
        }
        return ids;
    }
    private boolean isAction(String actionType, String action){
    		return actionType.equalsIgnoreCase(action);
    }
    
    private  List toList(Collection pCollection) {
        List list = new ArrayList();
        if (pCollection != null){
        	list.addAll(pCollection);
        }
        return list;
    }
    public int getLineCount (){
    	return currentLineNumber;
    }
}
