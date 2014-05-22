package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.WorkflowSiteAssocFilterFormValidator;


@Validation(WorkflowSiteAssocFilterFormValidator.class)
public class WorkflowSiteAssocFilterForm extends WebForm implements Resetable, Initializable {

    private Long accountId;
    private Long workflowId;
    private String workflowName;
    private String workflowTypeCd;
    
    private String siteId;
    private String siteName;
    private String refNumber;
    private String siteNameFilterType=Constants.FILTER_TYPE.START_WITH;
    private String refNumberFilterType=Constants.FILTER_TYPE.START_WITH;
    private String city;
    private String state;
    private String postalCode;
    
    private Boolean showConfiguredOnly;
    private Boolean showInactive;

    private boolean init;

    public WorkflowSiteAssocFilterForm() {
        super();
    }
    
    public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getWorkflowTypeCd() {
		return workflowTypeCd;
	}

	public void setWorkflowTypeCd(String workflowTypeCd) {
		this.workflowTypeCd = workflowTypeCd;
	}

	public WorkflowSiteAssocFilterForm(Long workflowId) {
        super();
        this.workflowId = workflowId;
    }


    public Long getWorkflowId() {
		return workflowId;
	}


	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}


	public String getSiteId() {
		return siteId;
	}


	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public String getRefNumber() {
		return refNumber;
	}


	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getSiteNameFilterType() {
		return siteNameFilterType;
	}


	public void setSiteNameFilterType(String siteNameFilterType) {
		this.siteNameFilterType = siteNameFilterType;
	}


	public String getRefNumberFilterType() {
		return refNumberFilterType;
	}


	public void setRefNumberFilterType(String refNumberFilterType) {
		this.refNumberFilterType = refNumberFilterType;
	}


	public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public Boolean getShowConfiguredOnly() {
        return showConfiguredOnly;
    }

    public void setShowConfiguredOnly(Boolean showConfiguredOnly) {
        this.showConfiguredOnly = showConfiguredOnly;
    }

    @Override
    public void initialize() {
        reset();
        this.refNumberFilterType = Constants.FILTER_TYPE.START_WITH;
        this.siteNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        this.siteId = null;
        this.siteName = null;
        this.siteNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.refNumber = null;
        this.refNumberFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showConfiguredOnly = false;
        this.showInactive = false;
        this.city = null;
        this.state = null;
        this.postalCode = null;        
    }

}
