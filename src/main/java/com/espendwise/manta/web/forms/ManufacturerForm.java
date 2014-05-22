package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.ManufacturerFormValidator;
import com.espendwise.manta.util.RefCodeNames;


@Validation(ManufacturerFormValidator.class)
public class ManufacturerForm extends WebForm implements  Initializable {

    private boolean initialize;

    private Long manufacturerId;
    private String manufacturerName;
    private String manufacturerStatus;
    private String manufacturerMSDSPlugIn;




    public ManufacturerForm() {
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerStatus() {
        return manufacturerStatus;
    }

    public void setManufacturerStatus(String manufacturerStatus) {
        this.manufacturerStatus = manufacturerStatus;
    }

    public String getManufacturerMSDSPlugIn() {
        return manufacturerMSDSPlugIn;
    }

    public void setManufacturerMSDSPlugIn(String manufacturerMSDSPlugIn) {
        this.manufacturerMSDSPlugIn = manufacturerMSDSPlugIn;
    }

    @Override
    public void initialize() {
        initialize = true;
        setManufacturerMSDSPlugIn(RefCodeNames.MSDS_PLUGIN_CD.DEFAULT);
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public boolean isNew() {
      return isInitialized() && (manufacturerId  == null || manufacturerId == 0);
    }

    @Override
    public String toString() {
        return "ManufacturerForm{" +
                "initialize=" + initialize +
                ", manufacturerId=" + manufacturerId +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", manufacturerStatus='" + manufacturerStatus + '\'' +
                ", manufacturerMSDSPlugIn='" + manufacturerMSDSPlugIn + '\'' +
                '}';
    }
}
