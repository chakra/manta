package com.espendwise.manta.web.forms;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.StoreMessageFilterFormValidator;

@Validation(StoreMessageFilterFormValidator.class)
public class StoreMessageFilterForm extends AbstractSimpleFilterForm {

	private String postedDateFrom;
    private String postedDateTo;
    private String country;
    private String languageCd;
    private String title;
    private String titleFilterType = Constants.FILTER_TYPE.START_WITH;
    
    private boolean published;
    private boolean unpublished;
    private boolean inactive;
    
    private Boolean showPublished;

    public String getPostedDateFrom() {
        return postedDateFrom;
    }

    public void setPostedDateFrom(String postedDateFrom) {
        this.postedDateFrom = postedDateFrom;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd;
    }

    public String getPostedDateTo() {
        return postedDateTo;
    }

    public void setPostedDateTo(String postedDateTo) {
        this.postedDateTo = postedDateTo;
    }
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleFilterType() {
		return titleFilterType;
	}

	public void setTitleFilterType(String titleFilterType) {
		this.titleFilterType = titleFilterType;
	}
	
	

	public boolean getPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public boolean getUnpublished() {
		return unpublished;
	}

	public void setUnpublished(boolean unpublished) {
		this.unpublished = unpublished;
	}

	public boolean getInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	
	/**
	 * @return the showPublished
	 */
	public final Boolean getShowPublished() {
		return showPublished;
	}

	/**
	 * @param showPublished the showPublished to set
	 */
	public final void setShowPublished(Boolean showPublished) {
		this.showPublished = showPublished;
	}

	@Override
    public void reset() {
        super.reset();
        this.postedDateTo = null;
        this.postedDateFrom = null;
        this.country = null;
        this.languageCd = null;
        this.title = null;
        this.titleFilterType = Constants.FILTER_TYPE.START_WITH;
        this.published = false;
        this.unpublished = false;
        this.inactive = false;
        this.showPublished = null;
    }

 
}
