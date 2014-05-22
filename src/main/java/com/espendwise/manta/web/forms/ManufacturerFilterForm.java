package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.LocateAssistant;
import com.espendwise.manta.web.validator.ManufacturerFilterFormValidator;

import java.util.List;

@Validation(ManufacturerFilterFormValidator.class)
public class ManufacturerFilterForm extends WebForm implements Resetable, Initializable {

    private String manufacturerId;
    private String manufacturerName;
    private String manufacturerNameFilterType;
    private Boolean showInactive;

    private boolean init;

    public ManufacturerFilterForm() {
        super();
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerNameFilterType() {
        return manufacturerNameFilterType;
    }

    public void setManufacturerNameFilterType(String manufacturerNameFilterType) {
        this.manufacturerNameFilterType = manufacturerNameFilterType;
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
        this.manufacturerNameFilterType = Constants.FILTER_TYPE.START_WITH;
        init = true;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }


    @Override
    public void reset() {
        this.manufacturerId = null;
        this.manufacturerName = null;
        this.manufacturerNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showInactive = false;
    }

}
