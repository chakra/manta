package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.LocateAssistant;
import com.espendwise.manta.web.validator.CostCenterFilterFormValidator;

import java.util.List;

@Validation(CostCenterFilterFormValidator.class)
public class CostCenterFilterForm extends WebForm implements Resetable, Initializable {

    private String costCenterId;
    private String costCenterName;
    private String costCenterNameFilterType;
    private Boolean showInactive;
    private String accountFilter;
    private String catalogFilter;

    private String costCenterAccCatFilterType;

    private boolean init;

    private List<AccountListView> filteredAccounts;
    private List<CatalogListView> filteredCatalogs;

    public CostCenterFilterForm() {
        super();
    }

    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
    }

    public List<AccountListView> getFilteredAccounts() {
        return filteredAccounts;
    }

    public void setFilteredAccounts(List<AccountListView> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }

    public List<CatalogListView> getFilteredCatalogs() {
        return filteredCatalogs;
    }

    public void setFilteredCatalogs(List<CatalogListView> filteredCatalogs) {
        this.filteredCatalogs = filteredCatalogs;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public String getCostCenterNameFilterType() {
        return costCenterNameFilterType;
    }

    public void setCostCenterNameFilterType(String costCenterNameFilterType) {
        this.costCenterNameFilterType = costCenterNameFilterType;
    }

    public String getCostCenterAccCatFilterType() {
        return costCenterAccCatFilterType;
    }

    public void setCostCenterAccCatFilterType(String costCenterAccCatFilterType) {
        this.costCenterAccCatFilterType = costCenterAccCatFilterType;
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
        this.costCenterNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.costCenterAccCatFilterType = RefCodeNames.COST_CENTER_FILTER_ACC_CAT_TYPE.ACCOUNT;
        init = true;
    }

    public String getFilteredAccountCommaNames() {
        return LocateAssistant.getFilteredAccountCommaNames(
                getFilteredAccounts()
        );
    }

    public String getFilteredAccountCommaIds() {
        return LocateAssistant.getFilteredAccountCommaIds(
                getFilteredAccounts()
        );
    }

    public String getFilteredCatalogCommaNames() {
        return LocateAssistant.getFilteredCatalogCommaNames(
                getFilteredCatalogs()
        );
    }

    public String getFilteredCatalogCommaIds() {
        return LocateAssistant.getFilteredCatalogCommaIds(
                getFilteredCatalogs()
        );
    }

    public String getAccountFilter() {
        return accountFilter;
    }

    public void setAccountFilter(String accountFilter) {
        this.accountFilter = accountFilter;
    }

    public String getCatalogFilter() {
        return catalogFilter;
    }

    public void setCatalogFilter(String catalogFilter) {
        this.catalogFilter = catalogFilter;
    }


    @Override
    public boolean isInitialized() {
        return init;
    }


    @Override
    public void reset() {
        this.costCenterId = null;
        this.costCenterName = null;
        this.costCenterNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.showInactive = false;
        this.filteredAccounts = null;
        this.filteredCatalogs = null;
        this.accountFilter = null;
        this.catalogFilter = null;
    }

}
