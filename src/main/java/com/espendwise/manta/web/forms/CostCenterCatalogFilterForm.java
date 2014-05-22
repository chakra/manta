package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.CostCenterCatalogFilterFormValidator;


@Validation(CostCenterCatalogFilterFormValidator.class)
public class CostCenterCatalogFilterForm extends WebForm implements Resetable, Initializable {

    private Long costCenterId;
    private String catalogName;
    private String catalogNameFilterType = Constants.FILTER_TYPE.START_WITH;
    private Boolean showConfiguredOnly;
    private Boolean showInactive;

    private boolean init;

    public CostCenterCatalogFilterForm() {
        super();
    }

    public CostCenterCatalogFilterForm(Long costCenterId) {
        super();
        this.costCenterId = costCenterId;
    }

    public Long getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(Long costCenterId) {
        this.costCenterId = costCenterId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogNameFilterType() {
        return catalogNameFilterType;
    }

    public void setCatalogNameFilterType(String catalogNameFilterType) {
        this.catalogNameFilterType = catalogNameFilterType;
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

    public Boolean getShowConfiguredOnly() {
        return showConfiguredOnly;
    }

    public void setShowConfiguredOnly(Boolean showConfiguredOnly) {
        this.showConfiguredOnly = showConfiguredOnly;
    }

    @Override
    public void initialize() {
        reset();
        this.catalogNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        this.catalogName = null;
        this.catalogNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showConfiguredOnly = false;
        this.showInactive = false;
    }

}
