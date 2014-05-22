package com.espendwise.manta.history;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

public abstract class AbstractGenericReportHistoryRecord extends HistoryRecord {
	
	String buildLongDescription(boolean includeHtmlMarkup, String actionKey) {
		Locale locale = Auth.getAppUser().getLocale();
		StringBuilder returnValue = new StringBuilder(200);
		String objectType = I18nUtil.getMessage(locale, "history.objectType.genericReport");
		Object[] args = new Object[1];
		args[0] = objectType;
		returnValue.append(I18nUtil.getMessage(locale, actionKey, args));
		returnValue.append(" (");
		returnValue.append(I18nUtil.getMessage(locale, "history.description.id"));
		returnValue.append(" ");
		if (includeHtmlMarkup) {
			returnValue.append(getHistoryLink(getAttribute01(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.GENERIC_REPORT));
		}
		else {
			returnValue.append(getAttribute01());
		}
		returnValue.append(") ");
		returnValue.append(I18nUtil.getMessage(locale, "history.description.withValues"));
		returnValue.append(": ");
		if (includeHtmlMarkup) {
			returnValue.append("<br>");
		}
		boolean includeComma = false;
		returnValue.append(getLabelAndValue(getAttribute02(), includeComma, locale, "admin.global.filter.label.category"));
		includeComma = true;
		returnValue.append(getLabelAndValue(getAttribute03(), includeComma, locale, "admin.global.filter.label.reportName"));
		returnValue.append(getLabelAndValue(getAttribute04(), includeComma, locale, "admin.global.filter.label.parameterToken"));
		returnValue.append(getLabelAndValue(getAttribute05(), includeComma, locale, "admin.global.filter.label.reportSchemas"));
		returnValue.append(getLabelAndValue(getAttribute06(), includeComma, locale, "admin.global.filter.label.interfaceTable"));
		returnValue.append(getLabelAndValue(getAttribute07(), includeComma, locale, "admin.global.filter.label.reportType"));
		returnValue.append(getLabelAndValue(getAttribute08(), includeComma, locale, "admin.global.filter.label.className"));
		returnValue.append(getLabelAndValue(getAttribute09(), includeComma, locale, "admin.global.filter.label.supplementaryControls"));
		returnValue.append(getLabelAndValue(getAttribute10(), includeComma, locale, "admin.global.filter.label.runtimeEnabled"));
		returnValue.append(getLabelAndValue(getAttribute11(), includeComma, locale, "admin.global.filter.label.longDesc"));
		returnValue.append(getLabelAndValue(getAttribute12(), includeComma, locale, "admin.global.filter.label.userTypes"));
		returnValue.append(getLabelAndValue(getAttribute13(), includeComma, locale, "admin.global.filter.label.dbName"));
		String returnString = includeHtmlMarkup ? htmlEscape(returnValue.toString()) : returnValue.toString();
		if (includeHtmlMarkup && Utility.isSet(getClob1())){
			returnString += getClobData(getClob1(), locale, "history.description.genericReportSqlLink", 1);
		}
		if (includeHtmlMarkup && Utility.isSet(getClob2())){
			returnString += getClobData(getClob2(), locale, "history.description.genericReportPlSqlLink", 2);
		}
		
		return returnString;
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
	
	private String getClobData(String blobData, Locale locale, String linkTextKey, int index){
		String key = getHistoryId()+"_" + index;
		StringBuffer returnValue = new StringBuffer(200);
		returnValue.append("<br>");
		returnValue.append("<a id='historyRecordLink");
		returnValue.append(key);
		returnValue.append("' href='#'>");
		returnValue.append(I18nUtil.getMessage(locale, linkTextKey));
		returnValue.append("</a>");
		returnValue.append("<div id='historyRecordDiv");
		returnValue.append(key);
		returnValue.append("' style='display:none'>");
		returnValue.append("<textarea readonly name='sqlText' cols='90' rows='10'>" + htmlEscape(blobData) + "</textarea>");
		//returnValue.append("<textarea readonly name='sqlText' cols='90' rows='5'>xxx\nyyy\n</textarea>");
		returnValue.append("</div>");
		returnValue.append("<script>$(\"#historyRecordLink");
		returnValue.append(key);
		returnValue.append("\").click(function(){$(\"#historyRecordDiv");
		returnValue.append(key);
		returnValue.append("\").toggle(); return false;});</script>");
		return returnValue.toString();
	}

}
