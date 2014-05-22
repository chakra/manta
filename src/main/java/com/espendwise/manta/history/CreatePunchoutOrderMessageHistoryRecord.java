package com.espendwise.manta.history;

import java.util.HashSet;
import java.util.Set;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

public class CreatePunchoutOrderMessageHistoryRecord extends HistoryRecord {

	@Override
	public String getHistoryTypeCode() {
		return HistoryRecord.TYPE_CODE_CREATE_PUNCHOUT_ORDER_MESSAGE;
	}

	@Override
	public String getShortDescription() {
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.punchoutOrderMessage");
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
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.punchoutOrderMessage");
		Object[] args = new Object[1];
		args[0] = objectType;
		returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.createdObject", args));
		returnValue.append(" ");
		if (Utility.isSet(getAttribute01())) {
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.for"));
			returnValue.append(" ");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.location"));
			returnValue.append(" '");
			returnValue.append(getAttribute02());
			returnValue.append("' (");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
			returnValue.append(" ");
			if (includeHtmlMarkup) {
				returnValue.append(getHistoryLink(getAttribute01(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.LOCATION));
			}
			else {
				returnValue.append(getAttribute01());
			}
			returnValue.append(")");
		}
		if (Utility.isSet(getAttribute03())) {
			if (Utility.isSet(getAttribute01())) {
				returnValue.append(" ");
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.under"));
			}
			else {
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.for"));
			}
			returnValue.append(" ");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.account"));
			returnValue.append(" '");
			returnValue.append(getAttribute04());
			returnValue.append("' (");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.id"));
			returnValue.append(" ");
			if (includeHtmlMarkup) {
				returnValue.append(getHistoryLink(getAttribute03(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.ACCOUNT));
			}
			else {
				returnValue.append(getAttribute03());
			}
			returnValue.append(")");
		}
		if (Utility.isSet(getAttribute05())) {
			if (Utility.isSet(getAttribute01()) || Utility.isSet(getAttribute03())) {
				returnValue.append(" ");
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.under"));
			}
			else {
				returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.for"));
			}
			returnValue.append(" ");
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
			returnValue.append(")");
		}
		returnValue.append(".");
		if (includeHtmlMarkup && Utility.isSet(getClob1())) {
			returnValue.append("<br>");
			returnValue.append("<a id='historyRecordLink");
			returnValue.append(getHistoryId());
			returnValue.append("' href='#'>");
			returnValue.append(I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.description.punchoutMessageLink"));
			returnValue.append("</a>");
			returnValue = new StringBuilder(htmlEscape(returnValue.toString()));
			returnValue.append("<div id='historyRecordDiv");
			returnValue.append(getHistoryId());
			returnValue.append("' style=\"display:none\">");
			returnValue.append(htmlEscape(getClob1()));
			returnValue.append("</div>");
			returnValue.append("<script>$(\"#historyRecordLink");
			returnValue.append(getHistoryId());
			returnValue.append("\").click(function(){$(\"#historyRecordDiv");
			returnValue.append(getHistoryId());
			returnValue.append("\").toggle(); return false;});</script>");
		}
		return returnValue.toString();
	}

	@Override
	public Set<HistoryObjectData> getInvolvedObjects(Object... objects) {
		Set<HistoryObjectData> returnValue = new HashSet<HistoryObjectData>();
		//because this type of history record is created in the St.John application, this method 
		//just returns an empty Set<HistoryObjectData>.
		return returnValue;
	}

	@Override
	public Set<HistorySecurityData> getSecurityObjects(Object... objects) {
		Set<HistorySecurityData> returnValue = new HashSet<HistorySecurityData>();
		//because this type of history record is created in the St.John application, this method 
		//just returns an empty Set<HistorySecurityData>.
		return returnValue;
	}

	@Override
	public void describeIntoHistoryData(HistoryData historyData, Object... objects) {
		//because this type of history record is created in the St.John application, this method 
		//does nothing.
	}

}
