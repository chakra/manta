package com.espendwise.manta.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.data.EmailData;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.model.data.PhoneData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.AllStoreIdentificationView;
import com.espendwise.manta.model.view.UserIdentView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.UserRightsTool;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.web.util.AppI18nUtil;

public abstract class AbstractUserHistoryRecord extends HistoryRecord {
	
	String buildLongDescription(boolean includeHtmlMarkup, String actionKey) {
		StringBuilder returnValue = new StringBuilder(200);
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.user");
		Object[] args = new Object[1];
		args[0] = objectType;
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), actionKey, args));
		returnValue.append(" (");
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
		returnValue.append(" ");
		if (includeHtmlMarkup) {
			returnValue.append(getHistoryLink(getAttribute01(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.USER));
		}
		else {
			returnValue.append(getAttribute01());
		}
		returnValue.append(") ");
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.withValues"));
		returnValue.append(": ");
		if (includeHtmlMarkup) {
			returnValue.append("<br>");
		}
		boolean includeComma = false;
		if (Utility.isSet(getAttribute02())) {
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.global.filter.label.userType"));
			returnValue.append(": ");
			String userType = getAttribute02().replaceAll(" ", Constants.CHARS.UNDERLINE).replaceAll("-", Constants.CHARS.UNDERLINE);
			String messageKey = "refcodes.USER_TYPE_CD." + userType;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), messageKey));
			includeComma = true;
		}
		if (Utility.isSet(getAttribute03())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.userName"));
			returnValue.append(": ");
			returnValue.append(getAttribute03());
		}
		if (Utility.isSet(getAttribute04())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.userCode"));
			returnValue.append(": ");
			returnValue.append(getAttribute04());
		}
		String language = getAttribute05();
		if (Utility.isSet(language)) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.preferredLanguage"));
			returnValue.append(": ");
			if (!RefCodeNames.LOCALE_CD.XX_PIGLATIN.equalsIgnoreCase(language)) {
				returnValue.append(new Locale(language).getDisplayLanguage(Auth.getAppUser().getLocale()));
			}
			else {
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "refcodes.LOCALE_CD.XX_PIGLATIN"));
			}
		}
		if (Utility.isSet(getAttribute06())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.status"));
			returnValue.append(": ");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "refcodes.BUS_ENTITY_STATUS_CD." + getAttribute06()));
		}
		if (Utility.isSet(getAttribute07())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.userActiveDate"));
			returnValue.append(": ");
			returnValue.append(AppI18nUtil.formatDate(new AppLocale(Auth.getAppUser().getLocale()), new Date(new Long(getAttribute07()))));
		}
		if (Utility.isSet(getAttribute08())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.userInactiveDate"));
			returnValue.append(": ");
			returnValue.append(AppI18nUtil.formatDate(new AppLocale(Auth.getAppUser().getLocale()), new Date(new Long(getAttribute08()))));
		}
		if (Utility.isSet(getAttribute09())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.firstName"));
			returnValue.append(": ");
			returnValue.append(getAttribute09());
		}
		if (Utility.isSet(getAttribute10())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.lastName"));
			returnValue.append(": ");
			returnValue.append(getAttribute10());
		}
		if (Utility.isSet(getAttribute11())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.address1"));
			returnValue.append(": ");
			returnValue.append(getAttribute11());
		}
		if (Utility.isSet(getAttribute12())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.address2"));
			returnValue.append(": ");
			returnValue.append(getAttribute12());
		}
		if (Utility.isSet(getAttribute13())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.city"));
			returnValue.append(": ");
			returnValue.append(getAttribute13());
		}
		if (Utility.isSet(getAttribute14())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.postalCode"));
			returnValue.append(": ");
			returnValue.append(getAttribute14());
		}
		if (Utility.isSet(getAttribute15())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.stateProvince"));
			returnValue.append(": ");
			returnValue.append(getAttribute15());
		}
		if (Utility.isSet(getAttribute16())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.country"));
			returnValue.append(": ");
			returnValue.append(Utility.capitalizeFirstLetters(getAttribute16()));
		}
		if (Utility.isSet(getAttribute17())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.phone"));
			returnValue.append(": ");
			returnValue.append(getAttribute17());
		}
		if (Utility.isSet(getAttribute18())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.fax"));
			returnValue.append(": ");
			returnValue.append(getAttribute18());
		}
		if (Utility.isSet(getAttribute19())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.mobile"));
			returnValue.append(": ");
			returnValue.append(getAttribute19());
		}
		if (Utility.isSet(getAttribute20())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.email"));
			returnValue.append(": ");
			returnValue.append(getAttribute20());
		}
		if (Utility.isSet(getAttribute21())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.escalationEmail"));
			returnValue.append(": ");
			returnValue.append(getAttribute21());
		}
		if (Utility.isSet(getAttribute22())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.textingAddress"));
			returnValue.append(": ");
			returnValue.append(getAttribute22());
		}
		if (Utility.isSet(getAttribute23())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.userIdentificationNumber"));
			returnValue.append(": ");
			returnValue.append(getAttribute23());
		}
		if (Utility.isSet(getAttribute24()) || Utility.isSet(getAttribute25())) {
			String permissions = getPermissions(Utility.strNN(getAttribute24()), Utility.strNN(getAttribute25()));
			if (Utility.isSet(permissions)) {
				if (includeComma) {
					returnValue.append(", ");
				}
				includeComma = true;
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.permissions"));
				returnValue.append(": ");
				returnValue.append(permissions);
			}
			String purchasing = getPurchasing(Utility.strNN(getAttribute24()));
			if (Utility.isSet(purchasing)) {
				if (includeComma) {
					returnValue.append(", ");
				}
				includeComma = true;
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.purchasing"));
				returnValue.append(": ");
				returnValue.append(purchasing);
			}
		}
		Map<String, String> storeMap = getStoreMap(getClob1());
		if (Utility.isSet(storeMap)) {
			String defaultStore = Utility.strNN(getAttribute26());
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.primaryEntityAssociations"));
			returnValue.append(": ");
			Iterator<String> storeIterator = storeMap.keySet().iterator();
			boolean includeSeparator = false;
			String separator = "/";
			while (storeIterator.hasNext()) {
				if (includeSeparator) {
					returnValue.append(separator);
				}
				includeSeparator = true;
				String storeName = storeIterator.next();
				String storeId = storeMap.get(storeName);
				returnValue.append(storeName);
				returnValue.append(" (");
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
				returnValue.append(" ");
				if (includeHtmlMarkup) {
					returnValue.append(getHistoryLink(storeId, RefCodeNames.HISTORY_OBJECT_TYPE_CD.STORE));
				}
				else {
					returnValue.append(storeId);
				}
				returnValue.append(")");
				if (defaultStore.equalsIgnoreCase(storeId)) {
					returnValue.append(" (");
					returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.default"));
					returnValue.append(")");
				}
			}
		}
		if (includeHtmlMarkup) {
			return htmlEscape(returnValue.toString());
		}
		else {
			return returnValue.toString();
		}
	}
	
	private String getPermissions(String permissions, String corporateUser) {
		StringBuilder returnValue = new StringBuilder();
		UserRightsTool urt = new UserRightsTool(new UserData(), permissions);
		boolean includeSeparator = false;
		String separator = "/";
		if (permissions.indexOf(RefCodeNames.WORKFLOW_ROLE_CD.ORDER_APPROVER) >= 0) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.approveOrders"));
		}
		if (urt.getBrowseOnly()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.browseOnly"));
		}
		if (Utility.isSet(corporateUser) && Constants.TRUE.equalsIgnoreCase(corporateUser)) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.corporateUser"));
		}
		if (urt.getNoReporting()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.doesNotUseReporting"));
		}
		if (urt.canEditBillTo()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.updateBillToInformation"));
		}
		if (urt.canEditShipTo()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.updateShipToInformation"));
		}
		if (urt.isServiceVendorRole()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.serviceVendor"));
		}
		if (urt.isSiteManagerRole()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.siteManager"));
		}
		return returnValue.toString();
	}
	
	private String getPurchasing(String purchasing) {
		StringBuilder returnValue = new StringBuilder();
		UserRightsTool urt = new UserRightsTool(new UserData(), purchasing);
		boolean includeSeparator = false;
		String separator = "/";
		if (urt.getCreditCard()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.creditCard"));
		}
		if (urt.getOnAccount()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.onAccount"));
		}
		if (urt.getOtherPaymentFlag()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.otherPayment"));
		}
		if (urt.getPoNumRequired()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.poNumRequired"));
		}
		if (urt.getShowPrice()) {
			if (includeSeparator) {
				returnValue.append(separator);
			}
			includeSeparator = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "admin.user.label.showPrices"));
		}
		return returnValue.toString();
	}
	
	private Map<String, String> getStoreMap(String stores) {
		Map<String, String> returnValue = new HashMap<String, String>();
		if (Utility.isSet(stores)) {
			String[] tokens = stores.split(clobValueSeparator);
			for (int i=0; i < tokens.length; i=i+2) {
				returnValue.put(tokens[i+1], tokens[i]);
			}
		}
		return new TreeMap<String, String>(returnValue);
	}

	@Override
	public Set<HistoryObjectData> getInvolvedObjects(Object... objects) {
		Set<HistoryObjectData> returnValue = new HashSet<HistoryObjectData>();
		//this method should receive a single object - a UserIdentView - containing
		//the information to be logged in the history record
		UserIdentView user = (UserIdentView) objects[0];
		HistoryObjectData userObject = new HistoryObjectData();
		userObject.setObjectId(user.getUserData().getUserId());
		userObject.setObjectTypeCd(RefCodeNames.HISTORY_OBJECT_TYPE_CD.USER);
		returnValue.add(userObject);
		return returnValue;
	}

	@Override
	public Set<HistorySecurityData> getSecurityObjects(Object... objects) {
		Set<HistorySecurityData> returnValue = new HashSet<HistorySecurityData>();
		//this method should receive a single object - a UserIdentView - containing
		//the information to be logged in the history record
		UserIdentView user = (UserIdentView) objects[0];
		List<AllStoreIdentificationView> userStoreAssociations = user.getUserStoreAssocs();
		if (Utility.isSet(userStoreAssociations)) {
			Iterator<AllStoreIdentificationView> userStoreAssociationIterator = userStoreAssociations.iterator();
			while (userStoreAssociationIterator.hasNext()) {
				AllStoreIdentificationView userStoreAssociation = userStoreAssociationIterator.next();
				HistorySecurityData historySecurity = new HistorySecurityData();
				historySecurity.setBusEntityId(userStoreAssociation.getStoreId());
				historySecurity.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
				returnValue.add(historySecurity);
			}
		}
		return returnValue;
	}

	@Override
	public void describeIntoHistoryData(HistoryData historyData, Object... objects) {
		super.describeIntoHistoryData(historyData, objects);
		//this method should receive a single object - a UserIdentView - containing
		//the information to be logged in the history record
		UserIdentView user = (UserIdentView) objects[0];
		UserData userData = user.getUserData();
		if (userData == null) {
			userData = new UserData();
		}
		List<PropertyData> userProperties = user.getUserProperties();
		if (userProperties == null) {
			userProperties = new ArrayList<PropertyData>();
		}
		AddressData address = user.getAddressData();
		if (address == null) {
			address = new AddressData();
		}
		PhoneData phone = user.getPhoneData();
		if (phone == null) {
			phone = new PhoneData();
		}
		PhoneData fax = user.getFaxPhoneData();
		if (fax == null) {
			fax = new PhoneData();
		}
		PhoneData mobile = user.getMobilePhoneData();
		if (mobile == null) {
			mobile = new PhoneData();
		}
		EmailData email = user.getEmailData();
		if (email == null) {
			email = new EmailData();
		}
		EmailData escalationEmail = user.getEscalationEmailData();
		if (escalationEmail == null) {
			escalationEmail = new EmailData();
		}
		EmailData smsEmail = user.getSmsEmailData();
		if (smsEmail == null) {
			smsEmail = new EmailData();
		}
		
		//store the user id in attribute1
    	historyData.setAttribute01(userData.getUserId().toString());
		//store the user type in attribute2
    	historyData.setAttribute02(userData.getUserTypeCd());
		//store the user name in attribute3
    	historyData.setAttribute03(userData.getUserName());
		//store the user code in attribute4
    	PropertyData userCodeProperty = PropertyUtil.findP(userProperties, RefCodeNames.PROPERTY_TYPE_CD.USER_ID_CODE);
    	if (userCodeProperty != null) {
    		historyData.setAttribute04(userCodeProperty.getValue());
    	}
    	//store the preferred language in attribute 5
    	String userLocaleCd = userData.getPrefLocaleCd();
    	if (Utility.isSet(userLocaleCd)) {
    		int index = userLocaleCd.indexOf(Constants.CHARS.UNDERLINE);
    		if (index > 0) {
    			String language = userLocaleCd.substring(0, index);
        		historyData.setAttribute05(language);
    		}
    	}
    	//store the user status in attribute 6
    	historyData.setAttribute06(userData.getUserStatusCd());
    	//store the user active date in attribute 7
    	if (userData.getEffDate() != null) {
    		historyData.setAttribute07(userData.getEffDate().getTime() + "");
    	}
    	//store the user inactive date in attribute 8
    	if (userData.getExpDate() != null) {
    		historyData.setAttribute08(userData.getExpDate().getTime() + "");
    	}
    	//store the first name in attribute 9
    	historyData.setAttribute09(address.getName1());
    	//store the last name in attribute 10
    	historyData.setAttribute10(address.getName2());
    	//store the address 1 value in attribute 11
    	historyData.setAttribute11(address.getAddress1());
    	//store the address 2 value in attribute 12
    	historyData.setAttribute12(address.getAddress2());
    	//store the city value in attribute 13
    	historyData.setAttribute13(address.getCity());
    	//store the postal code in attribute 14
    	historyData.setAttribute14(address.getPostalCode());
    	//store the state in attribute 15
    	historyData.setAttribute15(address.getStateProvinceCd());
    	//store the country in attribute 16
    	historyData.setAttribute16(address.getCountryCd());
    	//store the phone number in attribute 17
    	historyData.setAttribute17(phone.getPhoneNum());
    	//store the fax number in attribute 18
    	historyData.setAttribute18(fax.getPhoneNum());
    	//store the mobile number in attribute 19
    	historyData.setAttribute19(mobile.getPhoneNum());
    	//store the email address in attribute 20
    	historyData.setAttribute20(email.getEmailAddress());
    	//store the escalation email address in attribute 21
    	historyData.setAttribute21(escalationEmail.getEmailAddress());
    	//store the texting email address in attribute 22
    	historyData.setAttribute22(smsEmail.getEmailAddress());
    	//store the IVR number in attribute 23
    	PropertyData ivrUinCodeProperty = PropertyUtil.findP(userProperties, RefCodeNames.PROPERTY_TYPE_CD.IVR_UIN);
    	if (ivrUinCodeProperty != null) {
    		historyData.setAttribute23(ivrUinCodeProperty.getValue());
    	}
    	//store the Permissions and Purchasing information in attribute 24
    	historyData.setAttribute24(userData.getUserRoleCd());
    	//store the "corporate user" property in attribute 25
        PropertyData corporateUserProperty = (PropertyData) PropertyUtil.find(userProperties, RefCodeNames.PROPERTY_TYPE_CD.CORPORATE_USER);
    	if (corporateUserProperty != null) {
    		historyData.setAttribute25(corporateUserProperty.getValue());
    	}
		//store the default store id in attribute26
    	PropertyData defaultStoreProperty = PropertyUtil.findP(userProperties, RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_STORE);
    	if (defaultStoreProperty != null) {
    		historyData.setAttribute26(defaultStoreProperty.getValue());
    	}
    	//store the associated stores in clob1
		List<AllStoreIdentificationView> userStoreAssociations = user.getUserStoreAssocs();
		if (Utility.isSet(userStoreAssociations)) {
			StringBuilder userStores = new StringBuilder(200);
			boolean includeSeparator = false; 
			Iterator<AllStoreIdentificationView> userStoreAssociationIterator = userStoreAssociations.iterator();
			while (userStoreAssociationIterator.hasNext()) {
				if (includeSeparator) {
					userStores.append(clobValueSeparator);
				}
				includeSeparator = true;
				AllStoreIdentificationView userStoreAssociation = userStoreAssociationIterator.next();
				userStores.append(userStoreAssociation.getStoreId());
				userStores.append(clobValueSeparator);
				userStores.append(userStoreAssociation.getStoreName());
			}
			historyData.setClob1(userStores.toString());
		}
	}

}
