package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.data.LanguageData;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.LocateAssistant;
import com.espendwise.manta.web.validator.UserFilterFormValidator;

import java.util.List;

@Validation(UserFilterFormValidator.class)
public class UserFilterForm extends WebForm implements Resetable, Initializable {

    private String userId;
    private String firstName;
    private String lastName;
    private String userType;
    private String email;
    private String emailFilterType = Constants.FILTER_TYPE.START_WITH;
    private String userNameFilterType = Constants.FILTER_TYPE.START_WITH;
    private String userRole;
    private String language;
    private String userLoginName;
    private Boolean showInactive;

    private boolean init;

    private List<AccountListView>  filteredAccounts;
    private String accountFilter;

    //reference data
    private List<LanguageData> availableLanguages;
    private List<Pair<String, String>> userRoleChoices;

    public UserFilterForm() {
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserNameFilterType() {
        return userNameFilterType;
    }

    public void setUserNameFilterType(String userNameFilterType) {
        this.userNameFilterType = userNameFilterType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getEmailFilterType() {
        return emailFilterType;
    }

    public void setEmailFilterType(String emailFilterType) {
        this.emailFilterType = emailFilterType;
    }

    public String getAccountFilter() {
        return accountFilter;
    }

    public void setAccountFilter(String accountFilter) {
        this.accountFilter = accountFilter;
    }

    public List<AccountListView> getFilteredAccounts() {
        return filteredAccounts;
    }

    public void setFilteredAccounts(List<AccountListView> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
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

    @Override
    public void initialize() {
        reset();
        this.userNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.emailFilterType = Constants.FILTER_TYPE.START_WITH;
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        this.email = null;
        this.userNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.emailFilterType = Constants.FILTER_TYPE.START_WITH;
    }

    public void setAvailableLanguages(List<LanguageData> v) {
        availableLanguages = v;
    }

    public List<LanguageData> getAvailableLanguages() {
        return availableLanguages;
    }

	public List<Pair<String, String>> getUserRoleChoices() {
		return userRoleChoices;
	}

	public void setUserRoleChoices(List<Pair<String, String>> userRoleChoices) {
		this.userRoleChoices = userRoleChoices;
	}

}
