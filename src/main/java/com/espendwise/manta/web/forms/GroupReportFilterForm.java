package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.GroupReportFilterFormValidator;


@Validation(GroupReportFilterFormValidator.class)
public class GroupReportFilterForm extends WebForm implements Resetable, Initializable {

    private String reportId;
    private Long groupId;
    private String reportName;
    private String reportNameFilterType;
    private Boolean showConfiguredOnly;

    private boolean init;


    public GroupReportFilterForm(Long groupId) {
    	this.groupId = groupId;
    }


	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}


	public Long getGroupId() {
		return groupId;
	}


	public void setReportName(String reportName) {
		this.reportName = reportName;
	}


	public String getReportName() {
		return reportName;
	}


	public void setReportNameFilterType(String reportNameFilterType) {
		this.reportNameFilterType = reportNameFilterType;
	}


	public String getReportNameFilterType() {
		return reportNameFilterType;
	}


	public void setShowConfiguredOnly(Boolean showConfiguredOnly) {
		this.showConfiguredOnly = showConfiguredOnly;
	}


	public Boolean getShowConfiguredOnly() {
		return showConfiguredOnly;
	}	

	@Override
    public void initialize() {
        reset();
        init = true;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }

    @Override
    public void reset() {
    	this.reportId = null;
        this.groupId = null;
        this.reportName = null;
        this.reportNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showConfiguredOnly = false;
    }
}
