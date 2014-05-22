package com.espendwise.manta.history;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.i18n.I18nUtil;

public class CreateGenericReportHistoryRecord extends AbstractGenericReportHistoryRecord {

	@Override
	public String getHistoryTypeCode() {
		return HistoryRecord.TYPE_CODE_CREATE_GENERIC_REPORT;
	}

	@Override
	public String getShortDescription() {
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.genericReport");
		Object[] args = new Object[1];
		args[0] = objectType;
		return I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.createObject", args);
	}
	
	@Override
	public String getLongDescription() {
		return buildLongDescription(false, "history.description.createdObject");
	}
	
	@Override
	public String getLongDescriptionAsHtml() {
		return buildLongDescription(true, "history.description.createdObject");
	}
}
