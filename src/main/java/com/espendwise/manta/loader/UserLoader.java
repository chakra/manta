package com.espendwise.manta.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.espendwise.manta.dao.AccountDAO;
import com.espendwise.manta.dao.AccountDAOImpl;
import com.espendwise.manta.dao.GroupDAO;
import com.espendwise.manta.dao.GroupDAOImpl;
import com.espendwise.manta.dao.SiteDAO;
import com.espendwise.manta.dao.SiteDAOImpl;
import com.espendwise.manta.dao.UserAssocDAO;
import com.espendwise.manta.dao.UserAssocDAOImpl;
import com.espendwise.manta.dao.UserDAO;
import com.espendwise.manta.dao.UserDAOImpl;
import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.EmailData;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.LanguageData;
import com.espendwise.manta.model.data.PhoneData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserAssocData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.AllStoreIdentificationView;
import com.espendwise.manta.model.view.CountryView;
import com.espendwise.manta.model.view.SiteAccountView;
import com.espendwise.manta.model.view.UserIdentView;
import com.espendwise.manta.model.view.UserUploadView;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.DbConstantResource;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.PasswordUtil;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.RefCodeNamesKeys;
import com.espendwise.manta.util.UserEmailRightCodes;
import com.espendwise.manta.util.UserEmailRights;
import com.espendwise.manta.util.UserRightsTool;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.criteria.StoreSiteCriteria;
import com.espendwise.manta.web.util.WebFormUtil;
import com.espendwise.ocean.common.webaccess.ResponseError;
import com.espendwise.ocean.util.validation.EmailValidation;

public class UserLoader extends BaseLoader {
    private static final Logger logger = Logger.getLogger(UserLoader.class);
    private final static int VERSION = 0;
	private final static int ACTION = 1;
	private final static int STORE_ID = 2;
	private final static int STORE_NAME = 3;
	private final static int ACCOUNT_REF_NUM = 4;
	private final static int ACCOUNT_NAME = 5;
	private final static int LOCATION_NAME = 6;
	private final static int LOCATION_REF_NUM = 7;
	private final static int USERNAME = 8;
	private final static int PASSWORD = 9;
	private final static int UPDATE_PASSWORD = 10;
	private final static int PREFERRED_LANGUAGE = 11;
	private final static int FIRST_NAME = 12;
	private final static int LAST_NAME = 13;
	private final static int ADDRESS1 = 14;
	private final static int ADDRESS2 = 15;
	private final static int CITY = 16;
	private final static int STATE = 17;
	private final static int POSTAL_CODE = 18;
	private final static int COUNTRY = 19;
	private final static int PHONE = 20;
	private final static int EMAIL = 21;
	private final static int FAX = 22;
	private final static int MOBILE = 23;
	private final static int APPROVER = 24;
	private final static int NEEDS_APPROVAL = 25;
	private final static int WAS_APPROVED = 26;
	private final static int WAS_REJECTED = 27;
	private final static int WAS_MODIFIED = 28;
	private final static int ORDER_DETAIL_NOTIFICATION = 29;		
	private final static int SHIPPING_NOTIFICATION = 30;
	private final static int CUTOFF_TIME_REMINDER = 31;
	private final static int NUMBER_OF_TIMES = 32;
	private final static int PHY_INV_NON_COMP_LOCATION_LISTING = 33;
	private final static int PHY_INV_COUNTS_PAST_DUE = 34;	
	private final static int CORPORATE_USER = 35;
	private final static int ON_ACCOUNT = 36;
	private final static int CREDIT_CARD = 37;
	private final static int OTHER_PAYMENT = 38;
	private final static int PO_NUM_REQUIRED = 39;
	private final static int SHOW_PRICE = 40;
	private final static int BROWSE_ONLY = 41;
	private final static int NO_REPORTING = 42;
	private final static int GROUP_ID = 43;
	private final static int LOCATION_ID = 44;
					
	private int reqColumnCountForVersion1 = LOCATION_ID-2;
	private int reqColumnCountForVersion2 = LOCATION_ID+1;
	private List<UserUploadView> userList = new ArrayList<UserUploadView>();	
	private Map<String, List<Integer>> userNameMap = new LinkedHashMap<String, List<Integer>>();
	private Map<Long, Long> siteIdAccountIdMap = new HashMap<Long, Long>();
	private Map<String, Long> accountRefNumAccountIdMap = new HashMap<String, Long>();// dist account ref num to accountId
	private Map<String, String> siteRefNumSiteIdMap = new HashMap<String, String>();// dist site ref num to accountId
	private List<String> validSiteIds = new ArrayList<String>(); // hold valid site id from store to for better performance
	private List<LanguageData> storeLanguages = null;
	
    public UserLoader(EntityManager entityManager, Locale locale, Long currStoreId, InputStream inputStream,String streamType) {
    	super(entityManager, locale, currStoreId, inputStream, streamType);
    	storeLanguages = WebFormUtil.getStoreAvailableLanguages(ServiceLocator.getStoreService(), currStoreId);
    }	
	    
    public UserService getUserService() {
        return ServiceLocator.getUserService();
    }
    
    @Override
	protected void parseDetailLine(List parsedLine) throws Exception {
		log.info("Line# " + (currentLineNumber));
		UserUploadView userView = new UserUploadView();
		userList.add(userView);		
		userView.setLineNumber(currentLineNumber);
		validateAndPopulateProperty(VERSION, "versionNumber", parsedLine, userView, true);
		String version = userView.getVersionNumber();
		boolean isVersion1 = false;
		boolean isVersion2 = false;		
		if (version != null){
			if (version.equals("1"))
				isVersion1 = true;
			else if (version.equals("2"))
				isVersion2 = true;
			else{
				appendErrorsOnLine("validation.loader.error.wrongVersionNum", columnNumberMap.get(VERSION), version);
			}
		}
		
		validateAndPopulateProperty(ACTION, "action", parsedLine, userView, true);
		
		//validation.loader.error.incorrectAction=Incorrect Action = '{0}' . Should be A|C|D
		String action = userView.getAction();
		if (Utility.isSet(action)){
			action = action.trim().toUpperCase();
			userView.setAction(action);	
		}
		if ((action != null && (action.trim().length()>1 || !"ACD".contains(action)))){
			appendErrorsOnLine("validation.loader.error.incorrectAction", columnNumberMap.get(ACTION), action);
		}
		
		validateAndPopulateProperty(STORE_ID, "storeId", parsedLine, userView, true);	
		//validation.loader.error.columnIsDiffFromCurrStoreId=Column '{0}' need to be same as current store id: {1}
		Long storeId = userView.getStoreId();
		if (storeId != null && !getStoreId().equals(storeId)){
			appendErrorsOnLine("validation.loader.error.columnIsDiffFromCurrStoreId", columnNumberMap.get(STORE_ID), getStoreId());
		}
		
		// if action is D, will only need to validate store id and user name, so ignore other fields
		if (Utility.isEqual(userView.getAction(),actionDelete)){
			validateAndPopulateProperty(USERNAME, "userName", parsedLine, userView, true, null, -1, 30);
			return;
		}
				
		validateAndPopulateProperty(STORE_NAME, "storeName", parsedLine, userView, true);
		validateAndPopulateProperty(ACCOUNT_REF_NUM, "accountRefNumber", parsedLine, userView, false, null, -1, 0);
		validateAndPopulateProperty(ACCOUNT_NAME, "accountName", parsedLine, userView, false, null, -1, 30);
		validateAndPopulateProperty(LOCATION_NAME, "siteName", parsedLine, userView, false, null, -1, 30);
		
		boolean required = "1".equals(version);			
		validateAndPopulateProperty(LOCATION_REF_NUM, "distrSiteRefNumber", parsedLine, userView, required, null, -1, 0);	
		if (required && Utility.isSet(userView.getDistrSiteRefNumber())){
			List<String> siteRefNums = getStringList(userView.getDistrSiteRefNumber());
			if (siteRefNums.size() == 0){
				appendErrorsOnLine("validation.loader.error.emptyValue", columnNumberMap.get(LOCATION_REF_NUM));
			}else if ("*".equals(userView.getDistrSiteRefNumber())){
				if (!"*".equals(userView.getAccountRefNumber())){
					List<String> accountRefNums = getStringList(userView.getAccountRefNumber());
					if (accountRefNums.size() == 0){
						appendErrorsOnLine("validation.loader.error.emptyValue", columnNumberMap.get(ACCOUNT_REF_NUM));
					}
				}
			}
		}
		validateAndPopulateProperty(USERNAME, "userName", parsedLine, userView, true, null, -1, 30);
		if (userView.getUserName() != null){
			List<Integer> columnNums = userNameMap.get(userView.getUserName());
			if (columnNums == null){
				columnNums = new ArrayList<Integer>();
				userNameMap.put(userView.getUserName(), columnNums);
			}
			columnNums.add(currentLineNumber);
		}
		
		validateAndPopulateProperty(UPDATE_PASSWORD, "updatePassword", parsedLine, userView, false);			
		
		required = actionAdd.equals(action) || (actionChange.equals(action) && userView.isUpdatePassword());
		validateAndPopulateProperty(PASSWORD, "password", parsedLine, userView, required, null, -1, 30);
		if (validateAndPopulateProperty(PREFERRED_LANGUAGE, "preferredLanguage", parsedLine, userView, true))
			validateLanguage(columnNumberMap.get(PREFERRED_LANGUAGE), userView.getPreferredLanguage());
		validateAndPopulateProperty(FIRST_NAME, "firstName", parsedLine, userView, true);			
		validateAndPopulateProperty(LAST_NAME, "lastName", parsedLine, userView, true);			
		validateAndPopulateProperty(ADDRESS1, "address1", parsedLine, userView, true);			
		validateAndPopulateProperty(ADDRESS2, "address2", parsedLine, userView, false);
		validateAndPopulateProperty(CITY, "city", parsedLine, userView, true);
		boolean countryError = !validateAndPopulateProperty(COUNTRY, "country", parsedLine, userView, true);
		required = false;	
		String country = userView.getCountry();
		
		if (!countryError){
			CountryView countryView = AppResourceHolder.getAppResource().getDbConstantsResource().getCountry(country);
			if (countryView == null){
				appendErrorsOnLine("validation.loader.error.columnContainsInvalidValue", columnNumberMap.get(COUNTRY), country);
				countryError = true;
			}else{
				required = Utility.isTrue(countryView.getUsesState());
			}
		}		
		
		if (validateAndPopulateProperty(STATE, "state", parsedLine, userView, required, null, -1, 30)){
			if (!countryError && !required && Utility.isSet(userView.getState())){
				appendErrorsOnLine("validation.loader.error.columnMustBeEmpty", columnNumberMap.get(STATE), country);
			}
		}
		validateAndPopulateProperty(POSTAL_CODE, "postalCode", parsedLine, userView, required, null, -1, 15);
		validateAndPopulateProperty(PHONE, "phone", parsedLine, userView, false, null, -1, 60);
		if (validateAndPopulateProperty(EMAIL, "email", parsedLine, userView, true)){
			String email = userView.getEmail();
			if (Utility.isSet(email)){
				if (!EmailValidation.isValidEmailAddress(email)) {
					appendErrorsOnLine("validation.loader.error.wrongEmailAddressFormat", columnNumberMap.get(EMAIL), email, "name@domain.topleveldomain");
				}
			}
		}
		validateAndPopulateProperty(FAX, "fax", parsedLine, userView, false, null, -1, 60);
		validateAndPopulateProperty(MOBILE, "mobile", parsedLine, userView, false, null, -1, 60);
		validateAndPopulateProperty(APPROVER, "approver", parsedLine, userView, true);
		validateAndPopulateProperty(NEEDS_APPROVAL, "needsApproval", parsedLine, userView, true);
		validateAndPopulateProperty(WAS_APPROVED, "wasApproved", parsedLine, userView, true);
		validateAndPopulateProperty(WAS_REJECTED, "wasRejected", parsedLine, userView, true);
		validateAndPopulateProperty(WAS_MODIFIED, "wasModified", parsedLine, userView, true);
		validateAndPopulateProperty(ORDER_DETAIL_NOTIFICATION, "orderDetailNotification", parsedLine, userView, true);
		validateAndPopulateProperty(SHIPPING_NOTIFICATION, "shippingNotification", parsedLine, userView, true);
		validateAndPopulateProperty(CUTOFF_TIME_REMINDER, "cutoffTimeReminder", parsedLine, userView, true);
		required = userView.isCutoffTimeReminder();
		if (validateAndPopulateProperty(NUMBER_OF_TIMES, "cutoffTimeEmailReminderCnt", parsedLine, userView, required, null, -1, 10)){
			if (!userView.isCutoffTimeReminder() && userView.getCutoffTimeEmailReminderCnt() != null){
				userView.setCutoffTimeEmailReminderCnt(null);
			}
			if (userView.getCutoffTimeEmailReminderCnt() != null && userView.getCutoffTimeEmailReminderCnt() < 0){
				appendErrorsOnLine("validation.loader.error.wrongPositiveValue", columnNumberMap.get(NUMBER_OF_TIMES));
			}
		}
		validateAndPopulateProperty(PHY_INV_NON_COMP_LOCATION_LISTING, "phyInvNonCompLocations", parsedLine, userView, true);
		validateAndPopulateProperty(PHY_INV_COUNTS_PAST_DUE, "phyInvCountsPastDue", parsedLine, userView, true);
		validateAndPopulateProperty(CORPORATE_USER, "corporateUser", parsedLine, userView, true);
		validateAndPopulateProperty(ON_ACCOUNT, "onAccount", parsedLine, userView, true);
		validateAndPopulateProperty(CREDIT_CARD, "creditCard", parsedLine, userView, true);
		validateAndPopulateProperty(OTHER_PAYMENT, "otherPayment", parsedLine, userView, true);
		validateAndPopulateProperty(PO_NUM_REQUIRED, "poNumRequired", parsedLine, userView, true);
		validateAndPopulateProperty(SHOW_PRICE, "showPrice", parsedLine, userView, true);
		validateAndPopulateProperty(BROWSE_ONLY, "browseOnly", parsedLine, userView, false);
		validateAndPopulateProperty(NO_REPORTING, "noReporting", parsedLine, userView, false);
		validateAndPopulateProperty(GROUP_ID, "groupID", parsedLine, userView, false, null, -1, 0);
		required = isVersion2;	
		validateAndPopulateProperty(LOCATION_ID, "siteIds", parsedLine, userView, required, null, -1, 0);
		if (required && Utility.isSet(userView.getSiteIds())){
			List<String> siteIds = getStringList(userView.getSiteIds());
			if (siteIds.size() == 0){
				appendErrorsOnLine("validation.loader.error.emptyValue", columnNumberMap.get(LOCATION_ID));
			}else if ("*".equals(userView.getSiteIds())){
				if (!"*".equals(userView.getAccountRefNumber())){
					List<String> accountRefNums = getStringList(userView.getAccountRefNumber());
					if (accountRefNums.size() == 0){
						appendErrorsOnLine("validation.loader.error.emptyValue", columnNumberMap.get(ACCOUNT_REF_NUM));
					}
				}
			}
		}
	}

	private void validateLanguage(String columnName, String preferredLanguage) {
		boolean validLanguage = false;
		for (LanguageData lang : storeLanguages){
			if (lang.getUiName().equals(preferredLanguage)){
				validLanguage = true;
				break;
			}			
		}
		if (!validLanguage)
			appendErrorsOnLine("validation.loader.error.notValidValueForColumn", preferredLanguage, columnName);
		
	}

	@Override
	public void doPostProcessing() throws Exception {
		log.info("Start doPostProcessing()....");
		Iterator<String> it = userNameMap.keySet().iterator();		
		while (it.hasNext()){
			String userName = it.next();
			List<Integer> columnNums = userNameMap.get(userName);
			if (columnNums.size() > 1){
				ResponseError error = getResponseError("validation.loader.error.userNameOnMultipleLine",columnNumberMap.get(USERNAME), userName);
				appendErrorsOnLines(columnNums, error);
			}
		}
		
		if (getErrors().size()>0)
			return;
		logger.info("doPostProcessing()=> BEGIN");
        SiteDAO siteDao = new SiteDAOImpl(entityManager);
        AccountDAO accountDao = new AccountDAOImpl(entityManager);
        UserDAO userDao = new UserDAOImpl(entityManager);
        UserAssocDAO userAssocDao = new UserAssocDAOImpl(entityManager);
        List<Long> groupIdOptions = getUserGroupIdOptions();
        

        try{        	
        	for (UserUploadView userView : userList){
        		currentLineNumber = userView.getLineNumber();
        		if (userView.getAction().equals(actionAdd))
        			addedCount++;
        		else if (userView.getAction().equals(actionChange))
        			modifiedCount++;
        		else if (userView.getAction().equals(actionDelete)){ // set user status to INACTIVE for deleted user
        			deletedCount++; 
        		}
        		
        		if (!userView.getAction().equals(actionDelete)){
	        		// validate site id exists in store
	        		if (userView.getVersionNumber().equals("1")){
	        			if (userView.getDistrSiteRefNumber().equals("*")){
	        				userView.setSiteIds("*");
	        			}else{// get list of dist site ref number
		        			List<String> siteRefNums = getStringList(userView.getDistrSiteRefNumber());
		        			List<String> siteRefNumsProcessed = new ArrayList<String>();
		        			List<String> siteIds = new ArrayList<String>();
		        			for (String distSiteRefNum : siteRefNums){
		        				distSiteRefNum = distSiteRefNum.trim();
		    					if (siteRefNumsProcessed.contains(distSiteRefNum))
									appendErrorsOnLine("validation.loader.error.listContainsDuplicatedValue", columnNumberMap.get(LOCATION_REF_NUM), distSiteRefNum);
								else {
									siteRefNumsProcessed.add(distSiteRefNum);
									String siteId = siteRefNumSiteIdMap.get(distSiteRefNum);
									if (siteId == null){
			        					List<Long> sites = siteDao.findSiteByDistRefNumberAndStoreId(distSiteRefNum, getStoreId());
			        					if (sites == null || sites.size() == 0){
			        						appendErrorsOnLine("validation.loader.error.locationNotExistInStore", columnNumberMap.get(LOCATION_REF_NUM), distSiteRefNum, getStoreId());
			        					}else if (sites.size() > 1){
			        						appendErrorsOnLine("validation.loader.error.multipleLocationExistInStore", columnNumberMap.get(LOCATION_REF_NUM), distSiteRefNum, getStoreId());
			        					}else{
			        						siteIds.add(sites.get(0).toString());
			        						siteRefNumSiteIdMap.put(distSiteRefNum, sites.get(0).toString());
			        					}
									}else{
										siteIds.add(siteId);
									}
								}
		        			}
		        			userView.setSiteIds(getDelimilatedString(siteIds));
	        			}	        			
	        		}
	        		
	        		
	    			// validate site id exists in store
	        		if (userView.getVersionNumber().equals("2") && !userView.getSiteIds().equals("*")){
	        			// get list of dist site id
	        			List<String> siteIds = getStringList(userView.getSiteIds());
	        			List<String> siteIdsProcessed = new ArrayList<String>();
	    				for (String siteIdStr : siteIds){
	    					if (siteIdsProcessed.contains(siteIdStr))
								appendErrorsOnLine("validation.loader.error.listContainsDuplicatedValue", columnNumberMap.get(LOCATION_ID), siteIdStr);
							else{
								siteIdsProcessed.add(siteIdStr);		    					
		    					if (!validSiteIds.contains(siteIdStr)){
									try{
										Long siteId = new Long(siteIdStr);
										StoreSiteCriteria criteria = new StoreSiteCriteria();
								        criteria.setStoreId(getStoreId());
								        criteria.setSiteIds(Utility.toList(siteId));
			
								        List<SiteAccountView> sites = siteDao.findSites(criteria);
								        if(sites.isEmpty())  {						
											appendErrorsOnLine("validation.loader.error.locationNotExistInStore", columnNumberMap.get(LOCATION_ID), siteIdStr, getStoreId());
										}else{	
											validSiteIds.add(siteIdStr);
											if (!sites.get(0).getBusEntityData().getBusEntityStatusCd().equals(RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE))
		    									appendErrorsOnLine("validation.loader.error.locationNotExistInStore", columnNumberMap.get(LOCATION_ID), siteIdStr, getStoreId());
										}
									}catch(NumberFormatException e){
										appendErrorsOnLine("validation.loader.error.errorParsingData", columnNumberMap.get(LOCATION_ID), siteIdStr);
									}
		    					}
							}
	    				}
	    				userView.setSiteIds(getDelimilatedString(siteIdsProcessed));
	    			}
	        		
	        		// validate account ref number if need
	        		if (Utility.isEqual(userView.getSiteIds(),"*")){
	        			if (!userView.getAccountRefNumber().equals("*")){
	        				List<String> accountRefNums = getStringList(userView.getAccountRefNumber());
		        			List<String> accountRefNumsProcessed = new ArrayList<String>();
		        			for (String accountRefNum : accountRefNums){
		        				accountRefNum = accountRefNum.trim();
		    					if (accountRefNumsProcessed.contains(accountRefNum))
									appendErrorsOnLine("validation.loader.error.listContainsDuplicatedValue", columnNumberMap.get(LOCATION_REF_NUM), accountRefNum);
								else {
									accountRefNumsProcessed.add(accountRefNum);
									Long accountId = accountRefNumAccountIdMap.get(accountRefNum);
									if (accountId == null){
			        					List<Long> accounts = accountDao.findAccountByDistRefNumberAndStoreId(accountRefNum, getStoreId());
			        					if (accounts == null || accounts.size() == 0){
			        						appendErrorsOnLine("validation.loader.error.accountNotExistInStore", columnNumberMap.get(LOCATION_REF_NUM), accountRefNum, getStoreId());
			        					}else if (accounts.size() > 1){
			        						appendErrorsOnLine("validation.loader.error.multipleAccountExistInStore", columnNumberMap.get(LOCATION_REF_NUM), accountRefNum, getStoreId());
			        					}else{
			        						accountRefNumAccountIdMap.put(accountRefNum, accounts.get(0));
			        					}
									}
								}
		        			}
		        			userView.setAccountRefNumber(getDelimilatedString(accountRefNumsProcessed));
	        			}
	        		}
	        		
	        		// validate user group
	        		if (Utility.isSet(userView.getGroupID())){//
	        			List<String> groupIdsProcessed = new ArrayList<String>();
	        			List<String> userGroupIds = getStringList(userView.getGroupID());
	        			for (String userGroupIdStr : userGroupIds){
	        				userGroupIdStr = userGroupIdStr.trim();	    					
    						try{
    							Long userGroupId = new Long(userGroupIdStr);
    							if (!groupIdOptions.contains(userGroupId)){						
    								appendErrorsOnLine("validation.loader.error.notValidValueForColumn", userGroupId, columnNumberMap.get(GROUP_ID));
    							}else{
    								if (groupIdsProcessed.contains(userGroupIdStr))
    									appendErrorsOnLine("validation.loader.error.listContainsDuplicatedValue", columnNumberMap.get(GROUP_ID), userGroupIdStr);
    								else{
    									groupIdsProcessed.add(userGroupIdStr);
    								}
    							}
    						}catch(NumberFormatException e){
    							appendErrorsOnLine("validation.loader.error.errorParsingData", columnNumberMap.get(GROUP_ID), userGroupIds);
    						}
	    					userView.setGroupID(getDelimilatedString(groupIdsProcessed));
	    				}
	        		}
        		}
	        	
        		UserData userData = userDao.findByUserName(userView.getUserName(), false);
        		if (userData != null){        			
        			// check user and store association if user already exists         			
        			UserAssocData userAssocData = userAssocDao.findAssoc(userData.getUserId(), getStoreId(), RefCodeNames.USER_ASSOC_CD.STORE);
        			if (userAssocData == null){
        				if (actionAdd.equals(userView.getAction())){
            				// exception user found in system
            				appendErrorsOnLine("validation.loader.error.userNameNotUnique", columnNumberMap.get(USERNAME), userView.getUserName());
            			} else if (actionChange.equals(userView.getAction()) || actionDelete.equals(userView.getAction())){
        					appendErrorsOnLine("validation.loader.error.userNameNotExistsForUpdate", columnNumberMap.get(USERNAME), userView.getUserName(), getStoreId());
        				}
        			}else{
        				userView.setUserId(userData.getUserId());
        				// will update existing user if user status is INACTIVE and set status to ACTIVE
            			if (actionAdd.equals(userView.getAction()) && userData.getUserStatusCd().equals(RefCodeNames.USER_STATUS_CD.ACTIVE)){
            				appendErrorsOnLine("validation.loader.error.userNameNotUnique", columnNumberMap.get(USERNAME), userView.getUserName());
            				continue;
            			}
            			if (actionDelete.equals(userView.getAction())){
            				if (userData.getUserStatusCd().equals(RefCodeNames.USER_STATUS_CD.ACTIVE)){
    	    					// set user to INACTIVE
    	    					userData.setUserStatusCd(RefCodeNames.USER_STATUS_CD.INACTIVE);
    	    					userDao.updateUserData(userData);
    	    				}
            			}
        			}        			
        		} else {
        			if (actionChange.equals(userView.getAction()) || actionDelete.equals(userView.getAction())){
        				// exception user not found in system
        				appendErrorsOnLine("validation.loader.error.userNameNotExistsForUpdate", columnNumberMap.get(USERNAME), userView.getUserName(), getStoreId());
        			}
        		}        		
        	} 
        	
        	if (getErrors().size()>0)
    			return;
        	
        	for (UserUploadView userView : userList){
        		if (actionDelete.equals(userView.getAction()))
        			continue;
        		UserIdentView userInfo = null;
                if (userView != null && userView.getUserId() != null) {
                	userInfo = userDao.findUserToEdit(getStoreId(), userView.getUserId());
                } else {
                	userInfo = new UserIdentView(new UserData(),
                            new EmailData(),
                            new AddressData(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            new ArrayList<UserAssocData>(),
                            new ArrayList<PropertyData>(),
                            new ArrayList<AllStoreIdentificationView>(),
                            new ArrayList<GroupAssocData>());
                }

                userInfo = fillOutUserIdenView(userInfo,userView);
                if (userInfo.getUserData().getUserId() == null) {
                	userInfo = getUserService().createUserIdent(userInfo);
                } else {
                	userInfo = getUserService().modifyUserIdent(userInfo);
                }
        	}
        } catch (Exception e){
        	e.printStackTrace();
        	appendErrors("Unexpected Error - " + e.getMessage());
        } finally {
        }
	}
	
	public UserIdentView fillOutUserIdenView(UserIdentView userInfo, UserUploadView userUploadView) {
        if (userInfo == null) {
            userInfo = new UserIdentView(new UserData(),
                    new EmailData(),
                    new AddressData(),
                    new PhoneData(),
                    new PhoneData(),
                    new PhoneData(),
                    new EmailData(),
                    new EmailData(),
                    new ArrayList<UserAssocData>(),
                    new ArrayList<PropertyData>(),
                    new ArrayList<AllStoreIdentificationView>(),
                    new ArrayList<GroupAssocData>());
        }
        // ========================= USER DATA =========================
        UserData userData = userInfo.getUserData();
        if (userData == null) {
            userData = new UserData();
        }
        Long userId = userData.getUserId();
        
        
        userData.setUserId(userId);
        userData.setUserName(userUploadView.getUserName());
        userData.setFirstName(userUploadView.getFirstName());
        userData.setLastName(userUploadView.getLastName());

        if (Utility.isSet(userUploadView.getPassword()) && (userId == null || userUploadView.isUpdatePassword())) {
            String encodedPassword = PasswordUtil.getHash(userUploadView.getUserName(), userUploadView.getPassword());
            userData.setPassword(encodedPassword);
        }

        userData.setEffDate(runDate);
        
        String userLocaleStr = userData.getPrefLocaleCd();
        String languageCode = userUploadView.getPreferredLanguage();
        String countryCode = userUploadView.getCountry();
		DbConstantResource dbResource = AppResourceHolder.getAppResource().getDbConstantsResource();
		LanguageData langData =  dbResource.getLanguageByUIName(languageCode);
	    CountryView country = dbResource.getCountry(countryCode);
        if (langData != null && country != null) {
            userLocaleStr = langData.getLanguageCode().trim() + "_" + country.getCountryCode().trim();
        } else {
            userLocaleStr = locale.getLanguage() + "_" + locale.getCountry();
        }
        userData.setPrefLocaleCd(userLocaleStr);

        if (userUploadView.isApprover()) {
            userData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.ORDER_APPROVER);
        } else {
            userData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.UNKNOWN);
        }
        userData.setUserTypeCd(RefCodeNames.USER_TYPE_CD.MULTI_SITE_BUYER);
        userData.setUserStatusCd(RefCodeNames.USER_STATUS_CD.ACTIVE);

        UserRightsTool ert = new UserRightsTool(userData);
        ert.setOtherPaymentFlag(userUploadView.isOtherPayment());
        ert.setCreditCardFlag(userUploadView.isCreditCard());
        ert.setOnAccount(userUploadView.isOnAccount());
        if (userUploadView.isCreditCard() ||
            userUploadView.isOnAccount() ||
            userUploadView.isOtherPayment()) {
            ert.setContractItemsOnly(true);
        } else {
            ert.setContractItemsOnly(false);
        }
        ert.setBrowseOnly(userUploadView.isBrowseOnly());
        ert.setNoReporting(userUploadView.isNoReporting());
        ert.setShowPrice(userUploadView.isShowPrice());
        ert.setPoNumRequired(userUploadView.isPoNumRequired()); 

        String permissionToken = ert.makePermissionsToken();
        if (!Utility.isSet(permissionToken)) {
            permissionToken = RefCodeNames.USER_ROLE_CD.UNKNOWN;
        }
        userData.setUserRoleCd(permissionToken);
        
        UserEmailRights emailRights = new UserEmailRights(permissionToken);
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_ORDER_NEEDS_APPROVAL.name(), userUploadView.isNeedsApproval());
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_ORDER_WAS_APPROVED.name(), userUploadView.isWasApproved());;
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_ORDER_WAS_REJECTED.name(), userUploadView.isWasRejected());
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_ORDER_WAS_MODIFIED.name(), userUploadView.isWasModified());        
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_ORDER_DETAIL_APPROVED.name(), userUploadView.isOrderDetailNotification());
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_ORDER_SHIPPED.name(), userUploadView.isShippingNotification()); 
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_CUTOFF_TIME_REMINDER.name(), userUploadView.isCutoffTimeReminder());
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_PHYSICAL_INV_NON_COMPL_SITE_LISTING.name(), userUploadView.isPhyInvNonCompLocations());
        emailRights.put(UserEmailRightCodes.USER_GETS_EMAIL_PHYSICAL_INV_COUNTS_PAST_DUE.name(), userUploadView.isPhyInvCountsPastDue());
        emailRights.updateRole(userData);

        
        userInfo.setUserData(userData);

        // ========================= USER EMAIL =========================
        EmailData emailData = userInfo.getEmailData();
        if (Utility.isSet(userUploadView.getEmail())) {
            if (emailData == null) {
                emailData = new EmailData();
            }
            emailData.setEmailStatusCd(RefCodeNames.EMAIL_STATUS_CD.ACTIVE);
            emailData.setUserId(userId);
            emailData.setEmailAddress(userUploadView.getEmail());
            emailData.setEmailTypeCd(RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT);
            emailData.setPrimaryInd(true);
            userInfo.setEmailData(emailData);
        }       
        
        // ====================== USER PHONE =====================
        PhoneData phoneData = userInfo.getPhoneData();
        if (Utility.isSet(userUploadView.getPhone())) {
            if (phoneData == null) {
                phoneData = new PhoneData();
            }
            phoneData.setUserId(userId);
            phoneData.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.PHONE);
            phoneData.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
            phoneData.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_PHONE);
            phoneData.setPhoneNum(userUploadView.getPhone());
            phoneData.setPrimaryInd(true);                    
            userInfo.setPhoneData(phoneData);
        } 

        // ====================== USER FAX =====================
        phoneData = userInfo.getFaxPhoneData();
        if (Utility.isSet(userUploadView.getFax())) {
            if (phoneData == null) {
                phoneData = new PhoneData();
            }
            phoneData.setUserId(userId);
            phoneData.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.FAX);
            phoneData.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
            phoneData.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_FAX);
            phoneData.setPhoneNum(userUploadView.getFax());
            phoneData.setPrimaryInd(true);            
            userInfo.setFaxPhoneData(phoneData);
        } 

        // ====================== USER MOBILE =====================
        phoneData = userInfo.getMobilePhoneData();
        if (Utility.isSet(userUploadView.getMobile())) {
            if (phoneData == null) {
                phoneData = new PhoneData();
            }
            phoneData.setUserId(userId);
            phoneData.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.MOBILE);
            phoneData.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
            phoneData.setPhoneNum(userUploadView.getMobile());
            phoneData.setPrimaryInd(true);            
            userInfo.setMobilePhoneData(phoneData);
        } 

        // ====================== USER ADDRESS =====================
        AddressData userAddress = userInfo.getAddressData();
        if (userAddress == null) {
            userAddress = new AddressData();
        }
        userAddress.setUserId(userId);
        userAddress.setName1(userUploadView.getFirstName());
        userAddress.setName2(userUploadView.getLastName());
        userAddress.setAddress1(userUploadView.getAddress1());
        userAddress.setAddress2(userUploadView.getAddress2());
        if (Utility.isSet(userUploadView.getCountry())) {
            userAddress.setCountryCd(userUploadView.getCountry());
        } else {
            userAddress.setCountryCd(RefCodeNames.ADDRESS_COUNTRY_CD.COUNTRY_UNKNOWN);
        }
        userAddress.setPostalCode(userUploadView.getPostalCode());
        userAddress.setStateProvinceCd(userUploadView.getState());
        userAddress.setCity(userUploadView.getCity());
        userAddress.setAddressTypeCd(RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        userAddress.setAddressStatusCd(RefCodeNames.ADDRESS_STATUS_CD.ACTIVE);
        userAddress.setPrimaryInd(true);

        userInfo.setAddressData(userAddress);

        // ====================== USER PROPERTIES =====================
        List<PropertyData> toSaveProperties = userInfo.getUserProperties();
        updateProperty(toSaveProperties, userId, RefCodeNames.PROPERTY_TYPE_CD.CORPORATE_USER, userUploadView.isCorporateUser());
        updateProperty(toSaveProperties, userId, RefCodeNames.PROPERTY_TYPE_CD.CUTOFF_TIME_EMAIL_REMINDER_CNT, userUploadView.getCutoffTimeEmailReminderCnt());
        updateProperty(toSaveProperties, userId, RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_STORE, getStoreId());

        logger.info("fillOutUserIdenView()=> toSaveProperties: " + toSaveProperties);

        userInfo.setUserProperties(toSaveProperties);

        // ====================== USER STORE ASSOCS =====================   
        SiteDAO siteDao = new SiteDAOImpl(entityManager);
        AccountDAO accountDao = new AccountDAOImpl(entityManager);
        List<AllStoreIdentificationView> newStoreAssocs = new ArrayList<AllStoreIdentificationView>();
        AllStoreIdentificationView storeView = new AllStoreIdentificationView(
        		getStoreId(),
        		getStoreId(),
        		userUploadView.getStoreName(),
                "ds01",// hard coded for now
                true);
        newStoreAssocs.add(storeView);   
        List<UserAssocData> oldAssocs = userInfo.getUserAssocs();
        List<UserAssocData> newAssocs = new ArrayList<UserAssocData>();
        List<Long> storeIds = new ArrayList<Long>();
        storeIds.add(getStoreId());
        
        List<Long> accountIds = new ArrayList<Long>();
        List<Long> siteIds = new ArrayList<Long>();
        if (Utility.isEqual(userUploadView.getSiteIds(),"*")){
        	if (!userUploadView.getAccountRefNumber().equals("*")){
        		List<String> accountRefNums = getStringList(userUploadView.getAccountRefNumber());
        		for (String accountRefNum : accountRefNums){
        			accountIds.add(accountRefNumAccountIdMap.get(accountRefNum));
        		}
        	}
        	siteIds = siteDao.findSiteByAccountIdsAndStoreId(accountIds, getStoreId());
        	if (userUploadView.getAccountRefNumber().equals("*")){
	        	StoreAccountCriteria criteria = new StoreAccountCriteria();
	            criteria.setStoreId(getStoreId());
	            criteria.setActiveOnly(true);
	            List<BusEntityData> storeAccounts = accountDao.findAccounts(criteria);
	            accountIds = Utility.toIds(storeAccounts);
        	}
        }else{
	        String[] siteIdsStr = userUploadView.getSiteIds().split(",");        
	        for (String siteIdStr : siteIdsStr){
	        	Long siteId = new Long(siteIdStr);
	        	Long accountId = siteIdAccountIdMap.get(siteId);
	        	if (accountId == null){
	        		BusEntityAssocData accountAssoc = siteDao.findSiteAccountAssoc(siteId);
	        		siteIdAccountIdMap.put(siteId, accountAssoc.getBusEntity2Id());
	        		if (!accountIds.contains(accountAssoc.getBusEntity2Id()))
	        			accountIds.add(accountAssoc.getBusEntity2Id());
	        	}else{
	        		if (!accountIds.contains(accountId))
	        			accountIds.add(accountId);
	        	}
	        	siteIds.add(siteId);
	        }
        }

        if (Utility.isSet(oldAssocs)) {           
            for (UserAssocData assoc : oldAssocs) {
                if (RefCodeNames.USER_ASSOC_CD.STORE.equals(assoc.getUserAssocCd())) {
                	newAssocs.add(assoc);
                	storeIds.remove(assoc.getBusEntityId());
                } else if (RefCodeNames.USER_ASSOC_CD.ACCOUNT.equals(assoc.getUserAssocCd())) {
                	if (accountIds.contains(assoc.getBusEntityId())){
                		newAssocs.add(assoc);
                		accountIds.remove(assoc.getBusEntityId());
                	}                	
                } else if (RefCodeNames.USER_ASSOC_CD.SITE.equals(assoc.getUserAssocCd())) {
                	if (siteIds.contains(assoc.getBusEntityId())){
                		newAssocs.add(assoc);
                		siteIds.remove(assoc.getBusEntityId());
                	}                	
                }
            }
        }
        
        if (storeIds.size()>0){
        	UserAssocData userAssoc = new UserAssocData();
            userAssoc.setBusEntityId(storeIds.get(0));
            userAssoc.setUserId(userId);
            userAssoc.setUserAssocCd(RefCodeNames.USER_ASSOC_CD.STORE);
            newAssocs.add(userAssoc);
        }
        
        for (Long accountId : accountIds){
        	UserAssocData userAssoc = new UserAssocData();
            userAssoc.setBusEntityId(accountId);
            userAssoc.setUserId(userId);
            userAssoc.setUserAssocCd(RefCodeNames.USER_ASSOC_CD.ACCOUNT);
            newAssocs.add(userAssoc);
        }

        for (Long siteId : siteIds){
        	UserAssocData userAssoc = new UserAssocData();
            userAssoc.setBusEntityId(siteId);
            userAssoc.setUserId(userId);
            userAssoc.setUserAssocCd(RefCodeNames.USER_ASSOC_CD.SITE);
            newAssocs.add(userAssoc);
        }

        userInfo.setUserAssocs(newAssocs);
        userInfo.setUserStoreAssocs(newStoreAssocs);
        
        // ====================== USER GROUP =====================
        List<GroupAssocData> oldGroupAssoc = userInfo.getUserGroupAssocs();
        List<GroupAssocData> newGroupAssoc = new ArrayList<GroupAssocData>();
        List<Long> groupIds = new ArrayList<Long>();
        String groupIdsStr = userUploadView.getGroupID();
        if (Utility.isSet(groupIdsStr)){
        	String[] userGroupIdsStr = groupIdsStr.split(",");
			for (String userGroupIdStr : userGroupIdsStr){
				groupIds.add(new Long(userGroupIdStr));
			}
        }
			
		for (GroupAssocData existGroupAssoc : oldGroupAssoc){
			if (groupIds.contains(existGroupAssoc.getGroupId())){
				newGroupAssoc.add(existGroupAssoc);
				groupIds.remove(existGroupAssoc.getGroupId());
			}
		}
		
		for (Long groupId : groupIds){
			GroupAssocData groupAssocData = new GroupAssocData();
			groupAssocData.setGroupId(groupId);
			groupAssocData.setUserId(userUploadView.getUserId());
			groupAssocData.setGroupAssocCd(RefCodeNames.GROUP_ASSOC_CD.USER_OF_GROUP);
			newGroupAssoc.add(groupAssocData);
		}
		userInfo.setUserGroupAssocs(newGroupAssoc);
        return userInfo;
    }	

	// get list user group id associated with store
	private List<Long> getUserGroupIdOptions() {
		List<Long> groupIds = new ArrayList<Long>();
		GroupDAO groupDao = new GroupDAOImpl(entityManager);        
        GroupSearchCriteria criteria = new GroupSearchCriteria();
        criteria.setGroupStatus(RefCodeNames.GROUP_STATUS_CD.ACTIVE);
        criteria.setGroupType(RefCodeNames.GROUP_TYPE_CD.USER);
        criteria.setStoreIds(Utility.toList(getStoreId()));
        List<GroupData> optionalGroupList = groupDao.findGroupsByCriteria(criteria);
        for (GroupData groupD : optionalGroupList){
        	groupIds.add(groupD.getGroupId());
        }
        return groupIds;
	}

	@Override
	public List<String> getHeaderColumnsAbs() {
		List<Pair<String, String>> headerColumns = RefCodeNamesKeys.getRefCodeValues(Constants.USER_LOADER_PROPERTY.class, false);
		List<String> headers = new ArrayList<String>();
		for(int i=0; i<headerColumns.size(); i++) {
            Pair<String, String> h = headerColumns.get(i);
            headers.add(h.getObject2());
        }
			
		return headers;
	}	
	
	private void updateProperty(List<PropertyData> toSaveProperties, Long userId, String propertyTypeCd, Object propValue){
	
		PropertyData property = (PropertyData) PropertyUtil.find(toSaveProperties, propertyTypeCd);
		String value = propValue==null ? "" : propValue.toString();
		
		if (property != null) {
            property.setValue(value);
        } else {
            property = PropertyUtil.createProperty(null,
                    userId,
                    propertyTypeCd,
                    propertyTypeCd,
                    value,
                    RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                    null);
            toSaveProperties.add(property);
        }
	}

}
