package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.SiteHeaderView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.SiteOptionsForm;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.validator.SiteFormValidator;

@Validation(SiteFormValidator.class)
public class SiteForm extends WebForm implements Initializable {

    private DeliveryScheduleForm corporateSchedule;

    public static interface ACTION {
        public static String CREATE = "/create";
        public static String DELETE = "/delete";
        public static String CLONE = "/clone";
        public static String CLONE_WITH_ASSOC = "/cloneWAssoc";
        public static String GET_QRCODE = "/getQRCode";
    }

    private String selectedAction =  ACTION.CLONE;
    private String cloneCode;
    private String cloneId;
    //base
    private Long siteId;
    private String siteName;
    private String accountId;
    private String accountName;
    private String status;
    private String effDate;
    private String expDate;


    //properties
    private String locationBudgetRefNum;
    private String locationDistrRefNum;
    private String targetFicilityRank;
    private String locationLineLevelCode;
    private String productBundle;
    private String locationComments;
    private String locationShipMsg;

    //contact
    private ContactInputForm contact;

    //options
    private SiteOptionsForm options;

    private boolean init;



    public SiteHeaderView getSiteHeader() {
        return isNew()
                ? new SiteHeaderView()
                : new SiteHeaderView(siteId, siteName, Parse.parseLong(accountId), accountName);
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getStatus() {
        return status;
    }

    public void setCloneCode(String cloneCode) {
        this.cloneCode = cloneCode;
    }

    public String getCloneCode() {
        return cloneCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    @Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

    public String getLocationLineLevelCode() {
        return locationLineLevelCode;
    }

    public void setLocationLineLevelCode(String locationLineLevelCode) {
        this.locationLineLevelCode = locationLineLevelCode;
    }

    public String getLocationDistrRefNum() {
        return locationDistrRefNum;
    }

    public void setLocationDistrRefNum(String locationDistrRefNum) {
        this.locationDistrRefNum = locationDistrRefNum;
    }

    public String getTargetFicilityRank() {
        return targetFicilityRank;
    }

    public void setTargetFicilityRank(String targetFicilityRank) {
        this.targetFicilityRank = targetFicilityRank;
    }

    public String getLocationBudgetRefNum() {
        return locationBudgetRefNum;
    }

    public void setLocationBudgetRefNum(String locationBudgetRefNum) {
        this.locationBudgetRefNum = locationBudgetRefNum;
    }

    public void setCorporateSchedule(DeliveryScheduleForm corporateSchedule) {
        this.corporateSchedule = corporateSchedule;
    }

    public DeliveryScheduleForm getCorporateSchedule() {
        return corporateSchedule;
    }

    public String getProductBundle() {
        return productBundle;
    }

    public void setProductBundle(String productBundle) {
        this.productBundle = productBundle;
    }

    public ContactInputForm getContact() {
        return contact;
    }

    public void setContact(ContactInputForm contact) {
        this.contact = contact;
    }

    public String getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    public SiteOptionsForm getOptions() {
        return options;
    }

    public void setOptions(SiteOptionsForm options) {
        this.options = options;
    }

    public void setLocationComments(String locationComments) {
        this.locationComments = locationComments;
    }


    public void setLocationShipMsg(String locationShipMsg) {
        this.locationShipMsg = locationShipMsg;
    }

    public String getLocationComments() {
        return locationComments;
    }

    public String getLocationShipMsg() {
        return locationShipMsg;
    }


    public boolean isNew() {
        return isInitialized() && (siteId == null || siteId == 0);
    }

    public String getCloneId() {
        return cloneId;
    }

    public boolean isClonedWithAssoc() {
        return isNew()
                && Utility.strNN(getCloneCode()).equals(SiteForm.ACTION.CLONE_WITH_ASSOC)
                && Utility.longNN(AppI18nUtil.parseNumberNN(cloneId)) > 0;
    }

    public void setCloneId(String cloneId) {
        this.cloneId = cloneId;
    }



	@Override
    public String toString() {
        return "SiteForm{" +
                "corporateSchedule=" + corporateSchedule +
                ", selectedAction='" + selectedAction + '\'' +
                ", cloneCode='" + cloneCode + '\'' +
                ", cloneId='" + cloneId + '\'' +
                ", siteId=" + siteId +
                ", siteName='" + siteName + '\'' +
                ", accountId='" + accountId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", status='" + status + '\'' +
                ", effDate='" + effDate + '\'' +
                ", expDate='" + expDate + '\'' +
                ", locationBudgetRefNum='" + locationBudgetRefNum + '\'' +
                ", locationDistrRefNum='" + locationDistrRefNum + '\'' +
                ", targetFicilityRank='" + targetFicilityRank + '\'' +
                ", locationLineLevelCode='" + locationLineLevelCode + '\'' +
                ", productBundle='" + productBundle + '\'' +
                ", locationComments='" + locationComments + '\'' +
                ", locationShipMsg='" + locationShipMsg + '\'' +
                ", contact=" + contact +
                ", options=" + options +
                ", init=" + init +
                '}';
    }
}
