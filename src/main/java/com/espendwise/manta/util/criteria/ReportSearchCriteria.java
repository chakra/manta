package com.espendwise.manta.util.criteria;


import java.io.Serializable;
import java.util.List;

public class ReportSearchCriteria implements Serializable {

    private Long groupId;
    private Long userId;
    private Long reportId;
    private String reportName;
    private String  reportNameMatchType;
    private boolean showConfiguredOnly;


    @Override
    public String toString() {
        return "ReportSearchCriteria{" +
                "groupId='" + groupId + '\'' +
                ", userId='" + getUserId() + '\'' +
                ", reportId=" + reportId +
                ", reportName=" + reportName +
                ", reportNameMatchType='" + reportNameMatchType + '\'' +
                ", showConfiguredOnly='" + showConfiguredOnly +
                '}';
    }

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportNameMatchType(String reportNameMatchType) {
		this.reportNameMatchType = reportNameMatchType;
	}

	public String getReportNameMatchType() {
		return reportNameMatchType;
	}

	public void setShowConfiguredOnly(boolean showConfiguredOnly) {
		this.showConfiguredOnly = showConfiguredOnly;
	}

	public boolean isShowConfiguredOnly() {
		return showConfiguredOnly;
	}
}
