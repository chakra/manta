package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.LocateAssistant;
import com.espendwise.manta.web.validator.SiteFilterFormValidator;

import java.util.List;

@Validation(SiteFilterFormValidator.class)
public class SiteFilterForm extends WebForm implements Resetable, Initializable {

    private String siteId;
    private String siteName;
    private String refNumber;
    private String siteNameFilterType;
    private String refNumberFilterType;
    private String city;
    private String state;
    private String postalCode;
    private Boolean showInactive;
    private String accountFilter;

    private boolean init;

    private List<AccountListView> filteredAccounts;

    public SiteFilterForm() {
        super();
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public List<AccountListView> getFilteredAccounts() {
        return filteredAccounts;
    }

    public void setFilteredAccounts(List<AccountListView> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getSiteNameFilterType() {
        return siteNameFilterType;
    }

    public void setSiteNameFilterType(String siteNameFilterType) {
        this.siteNameFilterType = siteNameFilterType;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
        this.siteNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.refNumberFilterType = Constants.FILTER_TYPE.START_WITH;
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

    public String getAccountFilter() {
        return accountFilter;
    }

    public void setAccountFilter(String accountFilter) {
        this.accountFilter = accountFilter;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }


    @Override
    public void reset() {
        this.siteId = null;
        this.siteName = null;
        this.refNumber = null;
        this.siteNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.refNumberFilterType = Constants.FILTER_TYPE.START_WITH;
        this.city = null;
        this.state = null;
        this.postalCode = null;
        this.showInactive = false;
        this.filteredAccounts = null;
        this.accountFilter = null;
    }

}
