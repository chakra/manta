package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;

import com.espendwise.manta.web.validator.LocateAssetFilterFormValidator;

@Validation(LocateAssetFilterFormValidator.class)
public class LocateAssetFilterForm extends WebForm implements Resetable, Initializable {

    private Long siteId;
    private String assetId;
    private String assetName;
    private String assetNameFilterType = Constants.FILTER_TYPE.START_WITH;

    private Boolean showInactive;

    private boolean init;

    public LocateAssetFilterForm() {
        super();
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetNameFilterType() {
        return assetNameFilterType;
    }

    public void setAssetNameFilterType(String assetNameFilterType) {
        this.assetNameFilterType = assetNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }
    

    @Override
    public void reset() {
        this.assetNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.assetId = null;
        this.assetName = null;
        this.siteId = null;
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
