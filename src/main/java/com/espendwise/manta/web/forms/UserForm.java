package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.model.view.AllStoreIdentificationView;
import com.espendwise.manta.model.view.UserIdentView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.UserFormValidator;
import com.espendwise.manta.model.data.LanguageData;

import java.util.List;

@Validation(UserFormValidator.class)
public class UserForm extends WebForm implements Resetable, Initializable {

    private boolean initialize;
    
    private UserIdentView userInfo;
    private AllUserData allUserData;
    
    // User Information
    private Long   userId;
    private String userLogonName;
    private String userType;
    private String userPassword;
    private String userConfirmPassword;
    private String userCode;
    private String userLanguage;
    private String userStatus;
    private String userActiveDate;
    private String userInactiveDate;
    private String loginAsTarget = Constants.LOGIN_AS_TARGET.PROCUREMENT;

    // User Contact Information
    private UserContactInputForm userContact = new UserContactInputForm();

    // Interactive Voice Response(IVR)
    private String ivrUserIdentificationNumber;
    private String ivrPIN;
    private String ivrConfirmPIN;
    
    // User Permissions
    private Boolean approveOrders;
    private Boolean browseOnly;
    private Boolean corporateUser;

    private Boolean doesNotUseReporting;
    private Boolean updateBillToInformation;
    private Boolean updateShipToInformation;
    
    private String  userRole;

    // Payment
    private Boolean creditCard;
    private Boolean onAccount;
    private Boolean otherPayment;
    
    // Restrictions
    private Boolean showPrices;
    private Boolean poNumRequired;
    
    private String defaultStoreId;
    
    
    private SelectableObjects entities;
    private List<AllStoreIdentificationView> allAdminStores;
    private List<AllStoreIdentificationView> userStores;
    private List<AllStoreIdentificationView> currentStore;
    
    private boolean isClone;
    private Long sourceUserId;
    private boolean mainDbAlive;
    private boolean multiStoreDb;

    private List<LanguageData> availableLanguages;

    public UserForm() {
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogonName() {
        return userLogonName;
    }

    public void setUserLogonName(String userLogonName) {
        this.userLogonName = userLogonName;
    }

    public String getUserConfirmPassword() {
        return userConfirmPassword;
    }

    public void setUserConfirmPassword(String userConfirmPassword) {
        this.userConfirmPassword = userConfirmPassword;
    }

    public Boolean getApproveOrders() {
        return approveOrders;
    }

    public void setApproveOrders(Boolean approveOrders) {
        this.approveOrders = approveOrders;
    }

    public Boolean getBrowseOnly() {
        return browseOnly;
    }

    public void setBrowseOnly(Boolean browseOnly) {
        this.browseOnly = browseOnly;
    }

    public Boolean getCorporateUser() {
        return corporateUser;
    }

    public void setCorporateUser(Boolean corporateUser) {
        this.corporateUser = corporateUser;
    }

    public Boolean getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Boolean creditCard) {
        this.creditCard = creditCard;
    }

    public Boolean getDoesNotUseReporting() {
        return doesNotUseReporting;
    }

    public void setDoesNotUseReporting(Boolean doesNotUseReporting) {
        this.doesNotUseReporting = doesNotUseReporting;
    }

    public Boolean getOnAccount() {
        return onAccount;
    }

    public void setOnAccount(Boolean onAccount) {
        this.onAccount = onAccount;
    }

    public Boolean getOtherPayment() {
        return otherPayment;
    }

    public void setOtherPayment(Boolean otherPayment) {
        this.otherPayment = otherPayment;
    }

    public Boolean getPoNumRequired() {
        return poNumRequired;
    }

    public void setPoNumRequired(Boolean poNumRequired) {
        this.poNumRequired = poNumRequired;
    }

    public Boolean getShowPrices() {
        return showPrices;
    }

    public void setShowPrices(Boolean showPrices) {
        this.showPrices = showPrices;
    }

    public Boolean getUpdateBillToInformation() {
        return updateBillToInformation;
    }

    public void setUpdateBillToInformation(Boolean updateBillToInformation) {
        this.updateBillToInformation = updateBillToInformation;
    }

    public Boolean getUpdateShipToInformation() {
        return updateShipToInformation;
    }

    public void setUpdateShipToInformation(Boolean updateShipToInformation) {
        this.updateShipToInformation = updateShipToInformation;
    }

    public SelectableObjects getEntities() {
        return entities;
    }

    public void setEntities(SelectableObjects entities) {
        this.entities = entities;
    }

    public boolean isIsClone() {
        return isClone;
    }

    public void setIsClone(boolean isClone) {
        this.isClone = isClone;
    }

    public Long getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(Long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public String getIvrConfirmPIN() {
        return ivrConfirmPIN;
    }

    public void setIvrConfirmPIN(String ivrConfirmPIN) {
        this.ivrConfirmPIN = ivrConfirmPIN;
    }

    public String getIvrPIN() {
        return ivrPIN;
    }

    public void setIvrPIN(String ivrPIN) {
        this.ivrPIN = ivrPIN;
    }

    public String getIvrUserIdentificationNumber() {
        return ivrUserIdentificationNumber;
    }

    public void setIvrUserIdentificationNumber(String ivrUserIdentificationNumber) {
        this.ivrUserIdentificationNumber = ivrUserIdentificationNumber;
    }

    public String getUserActiveDate() {
        return userActiveDate;
    }

    public void setUserActiveDate(String userActiveDate) {
        this.userActiveDate = userActiveDate;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserInactiveDate() {
        return userInactiveDate;
    }

    public void setUserInactiveDate(String userInactiveDate) {
        this.userInactiveDate = userInactiveDate;
    }

    public UserIdentView getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserIdentView userInfo) {
        this.userInfo = userInfo;
    }

    public UserContactInputForm getUserContact() {
        return userContact;
    }

    public void setUserContact(UserContactInputForm userContact) {
        this.userContact = userContact;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<AllStoreIdentificationView> getAllAdminStores() {
        return allAdminStores;
    }

    public void setAllAdminStores(List<AllStoreIdentificationView> allAdminStores) {
        this.allAdminStores = allAdminStores;
    }

    public List<AllStoreIdentificationView> getCurrentStore() {
        return currentStore;
    }

    public void setCurrentStore(List<AllStoreIdentificationView> currentStore) {
        this.currentStore = currentStore;
    }

    public List<AllStoreIdentificationView> getUserStores() {
        return userStores;
    }

    public void setUserStores(List<AllStoreIdentificationView> userStores) {
        this.userStores = userStores;
    }

    public AllUserData getAllUserData() {
        return allUserData;
    }

    public void setAllUserData(AllUserData allUserData) {
        this.allUserData = allUserData;
    }
    
    @Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public boolean isNew() {
      return isInitialized() && (userId  == null || userId == 0);
    }

    public String getDefaultStoreId() {
        return defaultStoreId;
    }

    public void setDefaultStoreId(String defaultStoreId) {
        this.defaultStoreId = defaultStoreId;
    }

    public void setMainDbAlive(boolean mainDbAlive) {
        this.mainDbAlive = mainDbAlive;
    }

    public boolean isMainDbAlive() {
        return mainDbAlive;
    }

    public String getLoginAsTarget() {
        return loginAsTarget;
    }

    public void setLoginAsTarget(String loginAsTarget) {
        this.loginAsTarget = loginAsTarget;
    }

    public boolean isCanUseLoginAs() {
        // bug MANTA-151
        //return !isNew()
        //       && !RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR.equals(userType)
        //        && !RefCodeNames.USER_TYPE_CD.ADMINISTRATOR.equals(userType)
        //        && !RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR.equals(userType);
        
        return !isNew() && RefCodeNames.USER_TYPE_CD.MULTI_SITE_BUYER.equals(userType);
    }

    @Override
    public String toString() {
        return "UserForm{)";
    }

    public void setMultiStoreDb(boolean mainDbOn) {
        this.multiStoreDb = mainDbOn;
    }

    public boolean isMultiStoreDb() {
        return multiStoreDb;
    }

    public void setAvailableLanguages(List<LanguageData> v) {
        availableLanguages = v;
    }

    public List<LanguageData> getAvailableLanguages() {
        return availableLanguages;
    }

   @Override
   public void reset() {
    approveOrders = false;
    browseOnly = false;
    corporateUser = false;

    doesNotUseReporting = false;
    updateBillToInformation = false;
    updateShipToInformation = false;

    ivrUserIdentificationNumber = null;
    ivrPIN = null;
    ivrConfirmPIN = null;

    loginAsTarget = Constants.LOGIN_AS_TARGET.PROCUREMENT;
    userCode = null;
   }
}
