package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.AccountFormValidator;

@Validation(AccountFormValidator.class)
public class AccountForm extends WebForm implements  Initializable {

    private boolean initialize;

    private Long accountId;
    private String accountName;
    private String accountStatus;
    private String accountType;
    private String accountBudgetType;
    private String timeZone;
    private String distributorReferenceNumber;
    private AddressInputForm billingAddress = new AddressInputForm();
    private ContactInputForm primaryContact = new ContactInputForm();

    public AccountForm() {
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    } 

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountBudgetType() {
        return accountBudgetType;
    }

    public void setAccountBudgetType(String accountBudgetType) {
        this.accountBudgetType = accountBudgetType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDistributorReferenceNumber() {
        return distributorReferenceNumber;
    }

    public void setDistributorReferenceNumber(String distributorReferenceNumber) {
        this.distributorReferenceNumber = distributorReferenceNumber;
    }

    public ContactInputForm getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(ContactInputForm primaryContact) {
        this.primaryContact = primaryContact;
    }

    public AddressInputForm getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressInputForm billingAddress) {
        this.billingAddress = billingAddress;
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
      return isInitialized() && (accountId  == null || accountId == 0);
    }

    @Override
    public String toString() {
        return "AccountForm{" +
                "initialize=" + initialize +
                ", accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", accountType='" + accountType + '\'' +
                ", accountBudgetType='" + accountBudgetType + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", distributorReferenceNumber='" + distributorReferenceNumber + '\'' +
                ", billingAddress=" + billingAddress +
                ", primaryContact=" + primaryContact +
                '}';
    }
}
