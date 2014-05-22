package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.UserLocationFilterFormValidator;


@Validation(UserLocationFilterFormValidator.class)
public class UserLocationFilterForm extends WebForm implements Resetable, Initializable {

    private Long userId;
    private String siteId;
    private String siteName;
    private String siteNameFilterType = Constants.FILTER_TYPE.START_WITH;
    private String referenceNumber;
    private String refNumberFilterType;
    private String city;
    private String stateProvince;
    private String postalCode;
    private Boolean showConfiguredOnly;
    private Boolean showInactive;

    private boolean init;


    public UserLocationFilterForm() {
        super();
    }

    public UserLocationFilterForm(Long userId) {
        super();
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public String getRefNumberFilterType() {
        return refNumberFilterType;
    }

    public void setRefNumberFilterType(String refNumberFilterType) {
        this.refNumberFilterType = refNumberFilterType;
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

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
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
        this.siteId = null;
        this.siteName = null;
        this.siteNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.referenceNumber = null;
        this.refNumberFilterType = Constants.FILTER_TYPE.START_WITH;
        this.city = null;
        this.stateProvince = null;
        this.postalCode = null;
        this.showConfiguredOnly = false;
        this.showInactive = false;
        
    }

}
