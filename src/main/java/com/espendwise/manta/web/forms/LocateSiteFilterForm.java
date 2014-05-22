package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.LocateSiteFilterFormValidator;


@Validation(LocateSiteFilterFormValidator.class)
public class LocateSiteFilterForm extends WebForm implements Resetable, Initializable {

    private String locationId;
    private String siteName;
    private String siteNameFilterType = Constants.FILTER_TYPE.START_WITH;
    private String referenceNumber;
    private String referenceNumberFilterType = Constants.FILTER_TYPE.START_WITH;
    private String city;
    private String stateProvince;
    private String postalCode;
    private Boolean showInactive;

    private boolean init;


    public LocateSiteFilterForm() {
        super();
    }

    public LocateSiteFilterForm(String locationId) {
        super();
        this.locationId = locationId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSiteNameFilterType() {
        return siteNameFilterType;
    }

    public void setSiteNameFilterType(String siteNameFilterType) {
        this.siteNameFilterType = siteNameFilterType;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getReferenceNumberFilterType() {
        return referenceNumberFilterType;
    }

    public void setReferenceNumberFilterType(String referenceNumberFilterType) {
        this.referenceNumberFilterType = referenceNumberFilterType;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        this.siteName = null;
        this.locationId = null;
        this.siteNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.referenceNumber = null;
        this.referenceNumberFilterType = Constants.FILTER_TYPE.START_WITH;
        this.city = null;
        this.stateProvince = null;
        this.postalCode = null;
        this.showInactive = false;
    }

}
