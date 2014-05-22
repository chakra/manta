package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;

public abstract class AbstractSimpleFilterForm extends WebForm implements Resetable, Initializable {

    private boolean init = false;

    private String filterId;
    private String filterValue;
    private String filterType;
    private Boolean showInactive;

    protected AbstractSimpleFilterForm() {
        this.filterType = Constants.FILTER_TYPE.START_WITH;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public String getFilterId() {
		return filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}

	public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
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
       filterValue = "";
    }


}
