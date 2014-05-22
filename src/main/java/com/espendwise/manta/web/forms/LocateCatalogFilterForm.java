package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.LocateCatalogFilterFormValidator;

import java.util.List;

@Validation(LocateCatalogFilterFormValidator.class)
public class LocateCatalogFilterForm extends WebForm implements Resetable, Initializable {

    private String catalogId;
    private String catalogName;
    private String catalogNameFilterType = Constants.FILTER_TYPE.START_WITH;

    private String catalogType = RefCodeNames.CATALOG_TYPE_CD.ACCOUNT;

    private Boolean showInactive;

    private boolean init;

    public LocateCatalogFilterForm() {
        super();
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogNameFilterType() {
        return catalogNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public void setCatalogNameFilterType(String catalogNameFilterType) {
        this.catalogNameFilterType = catalogNameFilterType;
    }

    public String getCatalogType() {
        return this.catalogType;
    }

    public void setCatalogType(String catalogType) {
        this.catalogType = catalogType;
    }

    @Override
    public void reset() {
        this.catalogNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.catalogId = null;
        this.catalogName = null;
        this.catalogType = RefCodeNames.CATALOG_TYPE_CD.ACCOUNT;
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
