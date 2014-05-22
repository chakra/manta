package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.DistributorFilterFormValidator;

@Validation(DistributorFilterFormValidator.class)
public class DistributorFilterForm extends WebForm implements Resetable, Initializable {

    private String distributorId;
    private String distributorName;
    private String distributorNameFilterType;
    private Boolean showInactive;

    private boolean init;

    public DistributorFilterForm() {
        super();
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorNameFilterType() {
        return distributorNameFilterType;
    }

    public void setDistributorNameFilterType(String distributorNameFilterType) {
        this.distributorNameFilterType = distributorNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }


    @Override
    public void initialize() {
        reset();
        this.distributorNameFilterType = Constants.FILTER_TYPE.START_WITH;
        init = true;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }


    @Override
    public void reset() {
        this.distributorId = null;
        this.distributorName = null;
        this.distributorNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showInactive = false;
    }

}
