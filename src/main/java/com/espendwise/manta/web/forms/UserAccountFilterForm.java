package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.UserAccountFilterFormValidator;


@Validation(UserAccountFilterFormValidator.class)
public class UserAccountFilterForm extends WebForm implements Resetable, Initializable {

    private Long userId;
    private String accountId;
    private String accountName;
    private String accountNameFilterType = Constants.FILTER_TYPE.START_WITH;
    private Boolean associateAllAccounts;
    private Boolean associateAllSites;
    private Boolean showConfiguredOnly;
    private Boolean showInactive;

    private boolean init;


    public UserAccountFilterForm() {
        super();
    }

    public UserAccountFilterForm(Long userId) {
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNameFilterType() {
        return accountNameFilterType;
    }

    public void setAccountNameFilterType(String accountNameFilterType) {
        this.accountNameFilterType = accountNameFilterType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public Boolean getAssociateAllAccounts() {
        return associateAllAccounts;
    }

    public void setAssociateAllAccounts(Boolean associateAllAccounts) {
        this.associateAllAccounts = associateAllAccounts;
    }

    public Boolean getAssociateAllSites() {
        return associateAllSites;
    }

    public void setAssociateAllSites(Boolean associateAllSites) {
        this.associateAllSites = associateAllSites;
    }

    @Override
    public void initialize() {
        reset();
        this.accountNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        this.accountId = null;
        this.accountName = null;
        this.accountNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.associateAllAccounts = false;
        this.associateAllSites = false;
        this.showConfiguredOnly = false;
        this.showInactive = false;
    }

}
