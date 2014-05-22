package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.LocateAccountFilterFormValidator;

import java.util.List;

@Validation(LocateAccountFilterFormValidator.class)
public class LocateAccountFilterForm extends WebForm implements Resetable, Initializable {

    private String accountId;
    private String accountName;
    private String distrRefNumber;
    private String distrRefNumberFilterType = Constants.FILTER_TYPE.CONTAINS;
    private String accountNameFilterType = Constants.FILTER_TYPE.START_WITH;
    private List<Pair<Long, String>> accountsGroups;
    private String filteredAccountsGroups;

    private Boolean showInactive;

    private boolean init;

    public LocateAccountFilterForm() {
        super();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDistrRefNumber() {
        return distrRefNumber;
    }

    public void setDistrRefNumber(String distrRefNumber) {
        this.distrRefNumber = distrRefNumber;
    }


    public String getDistrRefNumberFilterType() {
        return distrRefNumberFilterType;
    }

    public void setDistrRefNumberFilterType(String distrRefNumberFilterType) {
        this.distrRefNumberFilterType = distrRefNumberFilterType;
    }

    public void setAccountsGroups(List<Pair<Long, String>> accountsGroups) {
        this.accountsGroups = accountsGroups;
    }

    public List<Pair<Long, String>> getAccountsGroups() {
        return accountsGroups;
    }

    public String getAccountNameFilterType() {
        return accountNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public void setAccountNameFilterType(String accountNameFilterType) {
        this.accountNameFilterType = accountNameFilterType;
    }

    public String getFilteredAccountsGroups() {
        return filteredAccountsGroups;
    }

    public void setFilteredAccountsGroups(String filteredAccountsGroups) {
        this.filteredAccountsGroups = filteredAccountsGroups;
    }

    @Override
    public void reset() {
        this.distrRefNumber = null;
        this.distrRefNumberFilterType = Constants.FILTER_TYPE.CONTAINS;
        this.accountNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.accountId = null;
        this.accountName = null;
        this.accountsGroups = null;
        this.filteredAccountsGroups= null;
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
