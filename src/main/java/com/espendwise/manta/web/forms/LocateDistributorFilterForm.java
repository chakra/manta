package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.LocateDistributorFilterFormValidator;

import java.util.List;

@Validation(LocateDistributorFilterFormValidator.class)
public class LocateDistributorFilterForm extends WebForm implements Resetable, Initializable {

    private String distributorId;
    private String distributorName;
    private String distributorNameFilterType = Constants.FILTER_TYPE.START_WITH;

    private Boolean showInactive;

    private boolean init;

    public LocateDistributorFilterForm() {
        super();
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }


    public String getDistributorNameFilterType() {
        return distributorNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public void setDistributorNameFilterType(String distributorNameFilterType) {
        this.distributorNameFilterType = distributorNameFilterType;
    }


    @Override
    public void reset() {
        this.distributorNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.distributorId = null;
        this.distributorName = null;
    }

    @Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

}
