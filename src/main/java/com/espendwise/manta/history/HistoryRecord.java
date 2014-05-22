package com.espendwise.manta.history;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.auth.AuthUser;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.util.Escaper;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

/**
 * This is the abstract base class for all classes that describe the details of a 
 * history record.
 * 
 * <p>Derived classes must:
 * <ul>
 * <li>Define {@link #getHistoryTypeCode() getHistoryTypeCode} to define the type code
 *  for the history record they represent.</li>
 * <li>Define {@link #getShortDescription() getShortDescription} to define the 
 *  short description of the history record they represent.</li>
 * <li>Define {@link #getLongDescription() getLongDescription}  to define the 
 *  long description of the history record they represent.</li>
 * <li>Define {@link #getInvolvedObjects() getInvolvedObjects} to define the 
 *  set of objects directly involved in the transaction this history object represents.</li>
 * <li>Override {@link #describeIntoHistoryData(Object...objects)
 *     describeIntoHistoryData} to define how to represent the history record
 *     in the database.</li>
 * </ul>
 * 
 * <p>This class also provides some utility methods and constants that are
 * useful in manipulating history details.
 */

public abstract class HistoryRecord extends HistoryData implements java.io.Serializable {
	
	protected static String clobValueSeparator = ";;";
	
	/**
	 * Application context needed to generate the links in the long description
	 */
	private String _historyLinkBase;

	/**
	 * The value returned by {@link #getHistoryTypeCode() getHistoryTypeCode} for a
	 * create shopping control transaction.
	 */
	public static final String TYPE_CODE_CREATE_SHOPPING_CONTROL = "CreateShoppingControl";
	
	/**
	 * The value returned by {@link #getHistoryTypeCode() getHistoryTypeCode} for a
	 * modify shopping control transaction.
	 */
	public static final String TYPE_CODE_MODIFY_SHOPPING_CONTROL = "ModifyShoppingControl";

	/**
	 * The value returned by {@link #getHistoryTypeCode() getHistoryTypeCode} for a
	 * create user transaction.
	 */
	public static final String TYPE_CODE_CREATE_USER = "CreateUser";
	
	/**
	 * The value returned by {@link #getHistoryTypeCode() getHistoryTypeCode} for a
	 * modify user transaction.
	 */
	public static final String TYPE_CODE_MODIFY_USER = "ModifyUser";
	
	/**
	 * The value returned by {@link #getHistoryTypeCode() getHistoryTypeCode} for a
	 * update user groups transaction.
	 */
	public static final String TYPE_CODE_UPDATE_USER_GROUPS= "UpdateUserGroups";

	/**
	 * The value returned by {@link #getHistoryTypeCode() getHistoryTypeCode} for a
	 * create punchout order message transaction.  NOTE - this transaction is created in the
	 * St.John application, not Manta.  However, we display the history records for such
	 * transactions here
	 */
	public static final String TYPE_CODE_CREATE_PUNCHOUT_ORDER_MESSAGE = "CreatePunchoutOrderMessage";
	
	/**
	 * The value returned by {@link #getHistoryTypeCode() getHistoryTypeCode} for a
	 * create generic report transaction.
	 */
	public static final String TYPE_CODE_CREATE_GENERIC_REPORT = "CreateGenericReport";
	
	/**
	 * The value returned by {@link #getHistoryTypeCode() getHistoryTypeCode} for a
	 * modify generic report transaction. NOTE - this transaction is created in the
	 * St.John application, not Manta.  However, we display the history records for such
	 * transactions here
	 */
	public static final String TYPE_CODE_MODIFY_GENERIC_REPORT = "ModifyGenericReport";

	//********* NOTE TO DEVELOPERS ***************************************
	// When you add new TYPE_CODE_* constants above, also add corresponding
	// entries to the _historyRecordMetaData HashMap in the static
	// initialization block below as well as to the HistoryController.populateFormReferenceData
	// method
	//********************************************************************

	/**
	 * A map of history type codes (TYPE_CODE_*) to meta-data information relating 
	 * that history record type.
	 * The entries in the map are HistoryRecordMetaData objects (an inner class of 
	 * HistoryRecord).
	 */
	private static final transient Map<String, HistoryRecordMetaData> _historyRecordMetaData;

	//Static class initialization:
	static {
		_historyRecordMetaData = new HashMap<String, HistoryRecordMetaData>();
		_historyRecordMetaData.put(
			TYPE_CODE_CREATE_SHOPPING_CONTROL,
			new HistoryRecordMetaData(CreateShoppingControlHistoryRecord.class));
		_historyRecordMetaData.put(
			TYPE_CODE_MODIFY_SHOPPING_CONTROL,
			new HistoryRecordMetaData(ModifyShoppingControlHistoryRecord.class));
		_historyRecordMetaData.put(
			TYPE_CODE_CREATE_USER,
			new HistoryRecordMetaData(CreateUserHistoryRecord.class));
		_historyRecordMetaData.put(
			TYPE_CODE_MODIFY_USER,
			new HistoryRecordMetaData(ModifyUserHistoryRecord.class));
		_historyRecordMetaData.put(
				TYPE_CODE_UPDATE_USER_GROUPS,
				new HistoryRecordMetaData(UpdateUserGroupsHistoryRecord.class));
		_historyRecordMetaData.put(
				TYPE_CODE_CREATE_PUNCHOUT_ORDER_MESSAGE,
				new HistoryRecordMetaData(CreatePunchoutOrderMessageHistoryRecord.class));
		_historyRecordMetaData.put(
				TYPE_CODE_CREATE_GENERIC_REPORT,
				new HistoryRecordMetaData(CreateGenericReportHistoryRecord.class));
		_historyRecordMetaData.put(
				TYPE_CODE_MODIFY_GENERIC_REPORT,
				new HistoryRecordMetaData(ModifyGenericReportHistoryRecord.class));
	}

	public HistoryRecord() {
		super();
	}

	public String getHistoryLinkBase() {
		return _historyLinkBase;
	}

	public void setHistoryLinkBase(String historyLinkBase) {
		_historyLinkBase = historyLinkBase;
	}

	/**
	 * Return the history type code for the history record that this
	 * class represents.  Derived classes must override this method.
	 * @return the history type code.
	 */
	public abstract String getHistoryTypeCode();

	/**
	 * Return the short description of the history record that this
	 * class represents.  Derived classes must override this method.
	 * @return the history type key.
	 */
	public abstract String getShortDescription();

	/**
	 * Return the long description of history record that this
	 * class represents.  Derived classes must override this method.
	 * @return the history description key.
	 */
	public abstract String getLongDescription();

	/**
	 * Return the long description of history record that this
	 * class represents as HTML.  Derived classes must override this method.
	 * @return the history description key.
	 */
	public abstract String getLongDescriptionAsHtml();

	/**
	 * Return a set of <code>HistoryObjectData</code> instances describing the objects
	 * involved in the history record that this instance represents.  
	 * Derived classes must override this method.
	 * @return the set of involved objects.
	 */
	public abstract Set<HistoryObjectData> getInvolvedObjects(Object... objects);

	/**
	 * Return a set of <code>HistorySecurityData</code> instances describing the business entities
	 * that should have access to this history record.  
	 * Derived classes must override this method.
	 * @return the set of security objects.
	 */
	public abstract Set<HistorySecurityData> getSecurityObjects(Object... objects);

	/**
	 * Create a history data from information related to some specific
	 * business activity.
	 * @param objects objects containing the information necessary to populate the
	 * 		history record.
	 * @return a HistoryData object.
	 */
	public HistoryData describeIntoHistoryData(Object... objects) {
		HistoryData historyData = new HistoryData();
		describeIntoHistoryData(historyData, objects);
		return historyData;
	}

	/**
	 * Populate a history data from information related to some specific
	 * business activity.  Derived classes should call the corresponding method
	 * on their superclass, to make sure information populated there is populated
	 * for those methods as well
	 * @param objects objects containing the information necessary to populate the
	 * 		history record.
	 * @return a HistoryData object.
	 */
	public void describeIntoHistoryData(HistoryData historyData, Object... objects) {
        AuthUser authUser = Auth.getAuthUser();
        Date date = new Date();
    	historyData.setHistoryTypeCd(getHistoryTypeCode());
        historyData.setUserId(authUser.getUsername());
        historyData.setActivityDate(date);
	}

	/**
	 * Populate the fields of this object with information contained in a
	 * history data object.  Derived classes should call the corresponding method
	 * on their superclass, to make sure information populated there is populated
	 * for those methods as well
	 * A runtime exception is thrown if the transaction type represented by 
	 * the history record doesn't match the transaction type represented by this object.
	 *
	 * @param history the history data object that will be used as the
	 *    information source.  A runtime exception is thrown if this is null.
	 */
	public final void populateFromHistoryData(HistoryData history) {
	    if (history == null) {
		      throw new IllegalArgumentException("HistoryRecorder.populateFromHistoryData: history data must not be null.");
		    }

		if (!getHistoryTypeCode().equals(history.getHistoryTypeCd())) {
		      throw new IllegalArgumentException(
		        "HistoryRecorder.populateFromHistoryData: expected a history record of type "
		          + getHistoryTypeCode()
		          + " but got a record of type "
		          + history.getHistoryTypeCd()
		          + ".");
		}
		//populate this object from the HistoryData passed in
		BeanUtils.copyProperties(history, this);
	}

	/**
	 * Return the meta-data information corresponding to the
	 * specified history record type code.
	 * 
	 * @param historyTypeCode the history record type code
	 * @return the history record meta data object.
	 */
	private static final HistoryRecordMetaData getHistoryRecordMetaData(String historyTypeCode) {
		if (!Utility.isSet(historyTypeCode)) {
			throw new IllegalArgumentException("HistoryRecord.getHistoryRecordMetaData: historyTypeCode must  be specified.");
	    }

	    HistoryRecordMetaData metaData = _historyRecordMetaData.get(historyTypeCode);

	    if (metaData == null) {
	      throw new RuntimeException("HistoryRecord.getHistoryRecordMetaData: unexpected type code '" + historyTypeCode + "'.");
	    }

	    return metaData;
	}

	/**
	 * Return an instance of the history record subclass that represents
	 * transactions of the specified type.  The instance is constructed
	 * using the type's zero-argument constructor.  A runtime exception
	 * is thrown if the specified history type code is null, if
	 * there's no class registered to correspond to the specified type code,
	 * or if the registered class isn't a subclass of HistoryRecord.
	 * 
	 * @param historyTypeCode the history type code
	 * @return the HistoryRecord subclass instance.
	 */
	public static final HistoryRecord getInstance(String historyTypeCode) {
	    Class clazz = getHistoryRecordMetaData(historyTypeCode).getHistoryRecordSubclass();

	    if (clazz == null) {
	      throw new RuntimeException("HistoryRecord.getInstance: unexpected type code '" + historyTypeCode + "'.");
	    }

	    HistoryRecord instance = null;
	    try {
	      instance = (HistoryRecord) clazz.newInstance();
	    }
	    catch (Exception e) {
	      throw new RuntimeException(e);
	    }

	    return instance;
	}

	public String getHistoryLink(String id, String objectType) {
		String returnValue = null;
		boolean includeLink = Utility.isSet(getHistoryLinkBase());
		if (includeLink && (RefCodeNames.HISTORY_OBJECT_TYPE_CD.ITEM.equals(objectType) ||
				RefCodeNames.HISTORY_OBJECT_TYPE_CD.SHOPPING_CONTROL.equals(objectType) ||
				RefCodeNames.HISTORY_OBJECT_TYPE_CD.USER.equals(objectType) ||
				RefCodeNames.HISTORY_OBJECT_TYPE_CD.GROUP.equals(objectType) ||
				RefCodeNames.HISTORY_OBJECT_TYPE_CD.GENERIC_REPORT.equals(objectType))) {
			StringBuffer buff = new StringBuffer(50);
			buff.append("<a href='");
			buff.append(getHistoryLinkBase());
			buff.append("/history/filter?objectId=");
			buff.append(id);
			buff.append("&objectType=");
			buff.append(objectType);
			buff.append("' target='_self'>");
			buff.append(id);
			buff.append("</a>");
			returnValue = buff.toString();
		}
		else {
			returnValue = id;
		}
		return returnValue;
	}
	
	/**
	 * Method to escape any non-html markup in the history record long description
	 * @param value  - the history record long description, possibly containing html markup
	 * @return  - a string in which all non-html markup has been escaped for rendering on an html page
	 * Note - the only markup currently in use in the long description is anchor tags (<a>, </a>) and break tags (<br>).
	 * If additional markup is used then this method will need to be updated accordingly.
	 */
	public String htmlEscape(String value) {
		String returnValue = null;
		int beginStartAnchor = value.indexOf("<a ");
		int beginStartBreak = value.indexOf("<br>");
		//if the value contains no html markup, escape all of it
		if (beginStartAnchor < 0 && beginStartBreak < 0) {
			returnValue = Escaper.htmlEscape(value);
		}
		//otherwise, escape the non-html markup
		else {
			boolean isAnchor = (beginStartAnchor >= 0 && (beginStartBreak < 0 || beginStartAnchor < beginStartBreak));
			boolean isBreak = (beginStartBreak >= 0 && (beginStartAnchor < 0 || beginStartBreak < beginStartAnchor));
			//if an anchor tag is the next markup handle it
			//escape everything up to the start of the link tag, the link text, and recursively call this method on
			//everything after the end of the link tag
			if (isAnchor) {
				//extract everything up to the start of the anchor tag so that any > in the text doesn't cause an issue
				String subString = value.substring(0, beginStartAnchor);
				value = value.substring(beginStartAnchor);
				int endStartAnchor = value.indexOf(">");
				int beginEndAnchor = value.indexOf("</a");
				int endEndAnchor = beginEndAnchor + 3;
				returnValue = Escaper.htmlEscape(subString) 
						+ value.substring(0, endStartAnchor + 1) 
						+ Escaper.htmlEscape(value.substring(endStartAnchor + 1, beginEndAnchor))
						+ value.substring(beginEndAnchor, endEndAnchor + 1)
						+ htmlEscape(value.substring(endEndAnchor + 1));
			}
			//if a break tag is the next markup handle it
			//escape everything up to the start of the break tag, and recursively call this method on
			//everything after the end of the break tag
			if (isBreak) {
				//extract everything up to the start of the break tag so that any > in the text doesn't cause an issue
				String subString = value.substring(0, beginStartBreak);
				value = value.substring(beginStartBreak);
				int endStartBreak = value.indexOf(">");
				returnValue = Escaper.htmlEscape(subString) 
						+ value.substring(0, endStartBreak + 1) 
						+ htmlEscape(value.substring(endStartBreak + 1));
			}
		}
		return returnValue;
	}
		
	/**
	  * Represent a variety of meta-data information about a history record.  The
	  * information this represents for each history record type is:
	  * <ul>
	  * <li>historyRecordSubclass: The subclass of HistoryRecord that represents details for that
	  *     type of history record.</li>
	  * </ul>
	 */
	private static class HistoryRecordMetaData {
		private Class historyRecordSubclass;
		
		HistoryRecordMetaData(Class historyRecordSubclass) {
			super();
			if (historyRecordSubclass == null) {
				throw new IllegalArgumentException("HistoryRecordMetaData: The history record subclass must not be null.");
			}
			this.historyRecordSubclass = historyRecordSubclass;
		}
		
		public Class getHistoryRecordSubclass() {
			return historyRecordSubclass;
		}
		
	}
	
	protected String getLabelAndValue(String attribute, boolean includeComma, Locale locale, String labelKey){
		String returnValue = "";
		if (Utility.isSet(attribute)) {
			if (includeComma) {
				returnValue += ", ";
			}
			includeComma = true;
			returnValue += I18nUtil.getMessage(locale, labelKey);
			returnValue += ": ";
			returnValue+= attribute;
		}
		return returnValue;
		
	}
}
