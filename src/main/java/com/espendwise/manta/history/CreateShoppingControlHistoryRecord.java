package com.espendwise.manta.history;

import java.util.HashSet;
import java.util.Set;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.dao.ShoppingControlDAOImpl;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

public class CreateShoppingControlHistoryRecord extends HistoryRecord {

	@Override
	public String getHistoryTypeCode() {
		return HistoryRecord.TYPE_CODE_CREATE_SHOPPING_CONTROL;
	}

	@Override
	public String getShortDescription() {
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.shoppingControl");
		Object[] args = new Object[1];
		args[0] = objectType;
		return I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.createObject", args);
	}
	
	@Override
	public String getLongDescription() {
		return buildLongDescription(false);
	}
	
	@Override
	public String getLongDescriptionAsHtml() {
		return buildLongDescription(true);
	}
	
	private String buildLongDescription(boolean includeHtmlMarkup) {
		StringBuilder returnValue = new StringBuilder(200);
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.shoppingControl");
		Object[] args = new Object[1];
		args[0] = objectType;
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.createdObject", args));
		returnValue.append(" (");
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
		returnValue.append(" ");
		if (includeHtmlMarkup) {
			returnValue.append(getHistoryLink(getAttribute01(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.SHOPPING_CONTROL));
		}
		else {
			returnValue.append(getAttribute01());
		}
		returnValue.append(") ");
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.for"));
		returnValue.append(" ");
		if (Utility.isSet(getAttribute07())) {
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.account"));
			returnValue.append(" '");
			returnValue.append(getAttribute08());
			returnValue.append("' (");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
			returnValue.append(" ");
			if (includeHtmlMarkup) {
				returnValue.append(getHistoryLink(getAttribute07(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.ACCOUNT));
			}
			else {
				returnValue.append(getAttribute07());				
			}
			returnValue.append(") ");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.under"));
			returnValue.append(" ");
		}
		else if (Utility.isSet(getAttribute09())) {
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.location"));
			returnValue.append(" '");
			returnValue.append(getAttribute10());
			returnValue.append("' (");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
			returnValue.append(" ");
			if (includeHtmlMarkup) {
				returnValue.append(getHistoryLink(getAttribute09(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.LOCATION));
			}
			else {
				returnValue.append(getAttribute09());
			}
			returnValue.append(") ");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.under"));
			returnValue.append(" ");
		}
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.store"));
		returnValue.append(" '");
		returnValue.append(getAttribute06());
		returnValue.append("' (");
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
		returnValue.append(" ");
		if (includeHtmlMarkup) {
			returnValue.append(getHistoryLink(getAttribute05(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.STORE));
		}
		else {
			returnValue.append(getAttribute05());			
		}
		returnValue.append(") ");
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.withValues"));
		returnValue.append(": ");
		if (includeHtmlMarkup) {
			returnValue.append("<br>");
		}
		boolean includeComma = false;
		if (Utility.isSet(getAttribute02())) {
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.itemId"));
			returnValue.append(": ");
			if (includeHtmlMarkup) {
				returnValue.append(getHistoryLink(getAttribute02(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.ITEM));
			}
			else {
				returnValue.append(getAttribute02());				
			}
			includeComma = true;
		}
		if (Utility.isSet(getAttribute03())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.itemShortDescription"));
			returnValue.append(": ");
			returnValue.append(getAttribute03());
		}
		if (Utility.isSet(getAttribute04())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.itemSku"));
			returnValue.append(": ");
			returnValue.append(getAttribute04());
		}
		if (includeComma) {
			returnValue.append(", ");
		}
		includeComma = true;
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.maximumOrderQuantity"));
		returnValue.append(": ");
		if (Utility.isSet(getAttribute11())) {
			returnValue.append(getAttribute11());
		}
		else {
			returnValue.append(Constants.CHARS.ASTERISK);
		}
		if (includeComma) {
			returnValue.append(", ");
		}
		includeComma = true;
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.restrictionDays"));
		returnValue.append(": ");
		if (Utility.isSet(getAttribute12())) {
			returnValue.append(getAttribute12());
		}
		else {
			returnValue.append(Constants.CHARS.ASTERISK);
		}
		if (Utility.isSet(getAttribute13())) {
			if (includeComma) {
				returnValue.append(", ");
			}
			includeComma = true;
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.action"));
			returnValue.append(": ");
			returnValue.append(Utility.capitalizeFirstLetters(getAttribute13()));
		}
		if (includeHtmlMarkup) {
			return htmlEscape(returnValue.toString());
		}
		else {
			return returnValue.toString();
		}
	}

	@Override
	public Set<HistoryObjectData> getInvolvedObjects(Object... objects) {
		Set<HistoryObjectData> returnValue = new HashSet<HistoryObjectData>();
		//this method should receive a single object - a ShoppingControlItemView - containing
		//the information to be logged in the history record
		ShoppingControlItemView shoppingControl = (ShoppingControlItemView) objects[0];
		HistoryObjectData shoppingControlObject = new HistoryObjectData();
        shoppingControlObject.setObjectId(shoppingControl.getShoppingControlId());
        shoppingControlObject.setObjectTypeCd(RefCodeNames.HISTORY_OBJECT_TYPE_CD.SHOPPING_CONTROL);
		returnValue.add(shoppingControlObject);
		HistoryObjectData itemObject = new HistoryObjectData();
		itemObject.setObjectId(shoppingControl.getItemId());
		itemObject.setObjectTypeCd(RefCodeNames.HISTORY_OBJECT_TYPE_CD.ITEM);
		returnValue.add(itemObject);
		return returnValue;
	}

	@Override
	public Set<HistorySecurityData> getSecurityObjects(Object... objects) {
		Set<HistorySecurityData> returnValue = new HashSet<HistorySecurityData>();
		//this method should receive a single object - a ShoppingControlItemView - containing
		//the information to be logged in the history record
		ShoppingControlItemView shoppingControl = (ShoppingControlItemView) objects[0];
		//set the business entity id and business entity type on this history record.  This information
		//will be used to determine if a given user should see this history record
    	Long storeId = shoppingControl.getShoppingControlStoreId();
    	Long accountId = shoppingControl.getShoppingControlAccountId();
    	Long locationId = shoppingControl.getShoppingControlLocationId();
    	if (Utility.isSet(locationId) && !ShoppingControlDAOImpl.DEFAULT_BUS_ENTITY_ID.equals(locationId)) {
    		HistorySecurityData shoppingControlSecurity = new HistorySecurityData();
    		shoppingControlSecurity.setBusEntityId(locationId);
    		shoppingControlSecurity.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
    		returnValue.add(shoppingControlSecurity);
    	}
    	else if (Utility.isSet(accountId) && !ShoppingControlDAOImpl.DEFAULT_BUS_ENTITY_ID.equals(accountId)) {
    		HistorySecurityData shoppingControlSecurity = new HistorySecurityData();
    		shoppingControlSecurity.setBusEntityId(accountId);
    		shoppingControlSecurity.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
    		returnValue.add(shoppingControlSecurity);
    	}
    	else if (Utility.isSet(storeId)) {
    		HistorySecurityData shoppingControlSecurity = new HistorySecurityData();
    		shoppingControlSecurity.setBusEntityId(storeId);
    		shoppingControlSecurity.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
    		returnValue.add(shoppingControlSecurity);
    	}
		return returnValue;
	}

	@Override
	public void describeIntoHistoryData(HistoryData historyData, Object... objects) {
		super.describeIntoHistoryData(historyData, objects);
		//this method should receive a single object - a ShoppingControlItemView - containing
		//the information to be logged in the history record
		ShoppingControlItemView shoppingControl = (ShoppingControlItemView) objects[0];
		
		//set the business entity id and business entity type on this history record.  This information
		//will be used to determine if a given user should see this history record
    	Long storeId = shoppingControl.getShoppingControlStoreId();
    	Long accountId = shoppingControl.getShoppingControlAccountId();
    	Long locationId = shoppingControl.getShoppingControlLocationId();
		
		//store the shopping control id in attribute1
    	historyData.setAttribute01(shoppingControl.getShoppingControlId().toString());
		//store the item id in attribute2
    	historyData.setAttribute02(shoppingControl.getItemId().toString());
		//store the item short description in attribute3
    	historyData.setAttribute03(shoppingControl.getItemShortDesc());
		//store the item sku in attribute4
    	historyData.setAttribute04(shoppingControl.getItemSku());
    	if (Utility.isSet(storeId)) {
    		//store the store id in attribute5
    		historyData.setAttribute05(shoppingControl.getShoppingControlStoreId().toString());
    		//store the store name in attribute6
    		historyData.setAttribute06(shoppingControl.getShoppingControlStoreName());
    	}
    	if (Utility.isSet(accountId) && !ShoppingControlDAOImpl.DEFAULT_BUS_ENTITY_ID.equals(accountId)) {
    		//store the account id in attribute7
    		historyData.setAttribute07(shoppingControl.getShoppingControlAccountId().toString());
    		//store the account name in attribute8
    		historyData.setAttribute08(shoppingControl.getShoppingControlAccountName());
    	}
    	if (Utility.isSet(locationId) && !ShoppingControlDAOImpl.DEFAULT_BUS_ENTITY_ID.equals(locationId)) {
    		//store the location id in attribute9
    		historyData.setAttribute09(shoppingControl.getShoppingControlLocationId().toString());
    		//store the location name in attribute10
    		historyData.setAttribute10(shoppingControl.getShoppingControlLocationName());
    	}
    	String maxOrderQuantity = shoppingControl.getShoppingControlMaxOrderQty();
    	if (ShoppingControlDAOImpl.DEFAULT_QUANTITY.toString().equals(maxOrderQuantity)) {
    		//store the max quantity in attribute11
    		historyData.setAttribute11(Constants.CHARS.ASTERISK);
    	}
    	else {
    		//store the max quantity in attribute11
    		historyData.setAttribute11(maxOrderQuantity);
    	}
    	String restrictionDays = shoppingControl.getShoppingControlRestrictionDays();
    	if (ShoppingControlDAOImpl.DEFAULT_QUANTITY.toString().equals(restrictionDays)) {
    		//store the restriction days in attribute12
    		historyData.setAttribute12(Constants.CHARS.ASTERISK);
    	}
    	else {
    		//store the restriction days in attribute12
    		historyData.setAttribute12(restrictionDays);
    	}
    	String shoppingControlAction = shoppingControl.getShoppingControlAction();
    	if (Utility.isSet(shoppingControlAction)) {
    		//store the action in attribute13
    		historyData.setAttribute13(shoppingControlAction);
    	}
    	else {
    		//store the action in attribute13
    		historyData.setAttribute13("");
    	}
	}

}
