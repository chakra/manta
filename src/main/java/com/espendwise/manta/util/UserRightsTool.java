package com.espendwise.manta.util;

import com.espendwise.manta.model.data.UserData;
import java.io.Serializable;

/**
 * Encapsulates the logic around user rights based off both the user type code and the user
 * role code.  Provides methods to manipulate the user role code encoded String. For example:
 * userRightsTool.setBrowseOnly(true);
 * userRightsTool.setPoNumRequiered(false);
 * Sting newRoleCode = userRightsTool.makePermissionsToken();
 * will modify the permissions so that browse only is true and the po number is not requiered
 */
public class UserRightsTool implements Serializable {
    public static final String USER_GETS_EMAIL_WORK_ORDER_COMPLETED = "WOCe";
    public static final String USER_GETS_EMAIL_WORK_ORDER_ACCEPTED = "WOABPe";
    public static final String USER_GETS_EMAIL_WORK_ORDER_REJECTED = "WORBPe";
    public static final String USER_GETS_EMAIL_ORDER_NEEDS_APPROVAL = "ONAe";
    
    private UserData _user = null;
    private String _userRights = "";

    private boolean _showPrice = false,
                    _browseOnly = false,
                    _contractItemsOnly = false,
                    _onAccount = false,
                    _otherPmt = false,
                    _creditCard = false,
                    _poNumRequired = false,
                    _noReporting = false,
                    _canApproveOrders = false,
                    _siteManagerRole = false,
                    _serviceVendorRole = false,
                    _canEditBillTo = false,
                    _canEditShipTo = false,
                    _workOrderCompletedNotification = false,
                    _workOrderAcceptedByProviderNotification = false,
                    _workOrderRejectedByProviderNotification = false;
    private String  _unselectable = "";

    /**
     * Called by the constructor
     *
     * @param pUserData      a user data object to use as the source of permissions
     * @param pRightsOverlay the user rights to be used if not present uses the rights
     *                       String present in the user object (the user role code
     *                       property).
     */
    private void init(UserData pUserData, String pRightsOverlay) {
        _user = pUserData;
        if (null != pRightsOverlay && pRightsOverlay.length() > 0) {
            _userRights = pRightsOverlay;
        } else {
            _userRights = _user.getUserRoleCd();
        }

        if (null == _userRights)
            _userRights = "";

        _otherPmt = parseOtherPaymentFlag();
        _creditCard = parseCreditCardFlag();
        _canApproveOrders = parseCanApprovePurchases();
        _canEditShipTo = parseCanEditShipTo();
        _canEditBillTo = parseCanEditBillTo();
        _serviceVendorRole = parseServiceVendorRole();
        _siteManagerRole = parseSiteManagerRole();
        _workOrderCompletedNotification = parseGetsEmailWorkOrderCompleted();
        _workOrderAcceptedByProviderNotification = parseGetsEmailWorkOrderAccepted();
        _workOrderRejectedByProviderNotification = parseGetsEmailWorkOrderRejected();
        _showPrice = parseShowPrice();
        _browseOnly = parseBrowseOnly();
        _contractItemsOnly = parseUserOnContract();
        _onAccount = parseOnAccount();
        _poNumRequired = parsePoNumRequired();
        _noReporting = parseNoReporting();
        _unselectable = parseUnselectable();
        
//       System.out.println("UserRightTool.init() ==> _unselectable = "+ _unselectable);

    }

    /**
     * Constructor for the UserRightsTool object
     *
     * @param pUserData a user data object to use as the source of permissions
     */
    public UserRightsTool(UserData pUserData) {
        init(pUserData, pUserData.getUserRoleCd());
    }

    /**
     * Constructor for the UserRightsTool object
     *
     * @param pUserData      a user data object to use as the source of permissions
     * @param pRightsOverlay the user rights to be used if not present uses the rights
     *                       String present in the user object.
     */
    public UserRightsTool(UserData pUserData, String pRightsOverlay) {
        init(pUserData, pRightsOverlay);
    }


    public boolean isaMSB() {
        String utype = _user.getUserTypeCd();
        return (utype.equals(RefCodeNames.USER_TYPE_CD.MULTI_SITE_BUYER));
    }

    public boolean hasAccounts() {
        return isaMSB();
    }

    /**
     * Creates a permissions token suitable for storage in the userRoleCode
     * property of the user object.
     */
    public String makePermissionsToken() {

//        String permissionToken = "";
        String permissionToken = getUnselectable();

        if (getOtherPaymentFlag()) {
            permissionToken += Constants.USER_ROLE.OTHER_PAYMENT;
        }
        if (getCreditCardFlag()) {
            permissionToken += Constants.USER_ROLE.CREDIT_CARD;
        }
        if (getShowPrice()) {
            permissionToken += Constants.USER_ROLE.SHOW_PRICE;
	}
        if (getBrowseOnly()) {
            permissionToken += Constants.USER_ROLE.BROWSE_ONLY;
        }
        if (getContractItemsOnly()) {
            permissionToken += Constants.USER_ROLE.CONTRACT_ITEMS_ONLY;
        }
        if (getOnAccount()) {
            permissionToken += Constants.USER_ROLE.ON_ACCOUNT;
        }        
        if (getPoNumRequired()) {
            permissionToken += Constants.USER_ROLE.PO_NUM_REQUIRED;
        }
        if (getNoReporting()) {
            permissionToken += Constants.USER_ROLE.NO_REPORTING;
        }

        //end aj

        if (getWorkOrderCompletedNotification()) {
            permissionToken += USER_GETS_EMAIL_WORK_ORDER_COMPLETED
                    + "^";
        }
        if (getWorkOrderAcceptedByProviderNotification()) {
            permissionToken += USER_GETS_EMAIL_WORK_ORDER_ACCEPTED
                    + "^";
        }
        if (getWorkOrderRejectedByProviderNotification()) {
            permissionToken += USER_GETS_EMAIL_WORK_ORDER_REJECTED
                    + "^";
        }
        if (canEditShipTo()) {
            permissionToken += Constants.USER_ROLE.CAN_EDIT_SHIPTO;
        }
        if (canEditBillTo()) {
            permissionToken += Constants.USER_ROLE.CAN_EDIT_BILLTO;
        }

        if (canApprovePurchases()) {
            permissionToken += RefCodeNames.WORKFLOW_ROLE_CD.ORDER_APPROVER
                    + "^";
        }

        if (isServiceVendorRole()) {
            permissionToken += Constants.USER_ROLE.SERVICE_VENDOR_ROLE;
        }

        if (isSiteManagerRole()) {
            permissionToken += Constants.USER_ROLE.SITE_MANAGER_ROLE;
        }

        if (null == permissionToken) {
            permissionToken = "";
        }
        
//        System.out.println("UserRightsTool.makePermissionsToken() ==> permissionToken= "+permissionToken);
       return permissionToken;
    }


    public boolean getOtherPaymentFlag() {
        return _otherPmt;
    }

    public void setOtherPaymentFlag(boolean pValue) {
        _otherPmt = pValue;
    }

    private boolean parseOtherPaymentFlag() {
        return _user != null && Utility.strNN(_userRights).indexOf(Constants.USER_ROLE.OTHER_PAYMENT) >= 0;
    }

    private boolean parseShowPrice() {
        if (_user == null) {
            return false;
        }
        if (_userRights.indexOf(Constants.USER_ROLE.SHOW_PRICE) < 0) {
            return false;
        }
        return true;
    }
    
    private boolean parseBrowseOnly() {
        if (_user == null) {
            return true;
        }
        if (_userRights == null) {
            return true;
        }
        return (_userRights.indexOf(Constants.USER_ROLE.BROWSE_ONLY) >= 0);
    }
    
    private boolean parseUserOnContract() {
        if (_user == null) {
            return false;
        }
        return (_userRights.indexOf(Constants.USER_ROLE.CONTRACT_ITEMS_ONLY) >= 0);
    }
    
    private boolean parseOnAccount() {
        if (_user == null) {
            return false;
        }
        return (_userRights.indexOf(Constants.USER_ROLE.ON_ACCOUNT) >= 0);
    }

    private boolean parsePoNumRequired() {
        if (_user == null) {
            return false;
        }
        return (_userRights.indexOf(Constants.USER_ROLE.PO_NUM_REQUIRED) >= 0);
    }
    
    private boolean parseNoReporting() {
        if (_user == null) {
            return false;
        }
        return (_userRights.indexOf(Constants.USER_ROLE.NO_REPORTING) >= 0);
    }

    public boolean canApprovePurchases() {
        return _canApproveOrders;
    }

    public void setCanApprovePurchases(boolean pValue) {
        _canApproveOrders = pValue;
    }

    public boolean parseCanApprovePurchases() {
        if (null == _user) return false;
        String appWorkflowRole = Utility.strNN(_user.getWorkflowRoleCd());
        return appWorkflowRole.indexOf(RefCodeNames.WORKFLOW_ROLE_CD.ORDER_APPROVER) >= 0;
    }

    public boolean getCreditCardFlag() {
        return _creditCard;
    }

    public void setCreditCardFlag(boolean pValue) {
        _creditCard = pValue;
    }

    private boolean parseCreditCardFlag() {
//        return _user != null && (Constants.UserRole.CREDIT_CARD.indexOf(Utility.strNN(_userRights)) >= 0);
        return _user != null && (Utility.strNN(_userRights).indexOf(Constants.USER_ROLE.CREDIT_CARD) >= 0);
    }


    public boolean canEditShipTo() {
        return _canEditShipTo;
    }

    public void setCanEditShipTo(boolean pValue) {
        _canEditShipTo = pValue;
    }

    private boolean parseCanEditShipTo() {
        return (Utility.strNN(_userRights).indexOf(Constants.USER_ROLE.CAN_EDIT_SHIPTO) >= 0);

    }

    public boolean canEditBillTo() {
        return _canEditBillTo;
    }

    public void setCanEditBillTo(boolean pValue) {
        _canEditBillTo = pValue;
    }

    private boolean parseCanEditBillTo() {
        return (Utility.strNN(_userRights).indexOf(Constants.USER_ROLE.CAN_EDIT_BILLTO) >= 0);
    }

    private boolean parseServiceVendorRole() {
        return (Utility.strNN(_userRights).indexOf(Constants.USER_ROLE.SERVICE_VENDOR_ROLE) >= 0);
    }

    private boolean parseSiteManagerRole() {
        return (Utility.strNN(_userRights).indexOf(Constants.USER_ROLE.SITE_MANAGER_ROLE) >= 0);
    }

    public boolean getWorkOrderAcceptedByProviderNotification() {
        return _workOrderAcceptedByProviderNotification;
    }

    public void setWorkOrderAcceptedByProviderNotification(boolean workOrderAcceptedByProviderNotification) {
        this._workOrderAcceptedByProviderNotification = workOrderAcceptedByProviderNotification;
    }

    public boolean getWorkOrderCompletedNotification() {
        return _workOrderCompletedNotification;
    }

    public void setWorkOrderCompletedNotification(boolean workOrderCompletedNotification) {
        this._workOrderCompletedNotification = workOrderCompletedNotification;
    }

    public boolean getWorkOrderRejectedByProviderNotification() {
        return _workOrderRejectedByProviderNotification;
    }

    public void setWorkOrderRejectedByProviderNotification(boolean workOrderRejectedByProviderNotification) {
        this._workOrderRejectedByProviderNotification = workOrderRejectedByProviderNotification;
    }

    public boolean isServiceVendorRole() {
        return _serviceVendorRole;
    }

    public void setServiceVendorRole(boolean serviceVendorRole) {
        _serviceVendorRole = serviceVendorRole;
    }

    public boolean isSiteManagerRole() {
        return _siteManagerRole;
    }

    public void setSiteManagerRole(boolean siteManagerRole) {
        _siteManagerRole = siteManagerRole;
    }

    private boolean parseGetsEmailWorkOrderCompleted() {
        if (_user == null) {
            return false;
        }
        boolean flag;
        flag = (Utility.strNN(_userRights).indexOf(USER_GETS_EMAIL_WORK_ORDER_COMPLETED) >= 0);
        return flag;
    }

    private boolean parseGetsEmailWorkOrderAccepted() {
        if (_user == null) {
            return false;
        }
        boolean flag;
        flag = (Utility.strNN(_userRights).indexOf(USER_GETS_EMAIL_WORK_ORDER_ACCEPTED) >= 0);
        return flag;
    }

    private boolean parseGetsEmailWorkOrderRejected() {
        if (_user == null) {
            return false;
        }
        boolean flag;
        flag = (Utility.strNN(_userRights).indexOf(USER_GETS_EMAIL_WORK_ORDER_REJECTED) >= 0);
        return flag;
    }

    public String getUnselectable() {
        return _unselectable;
    }

    public void setUnselectable(String unselectable) {
        this._unselectable = unselectable;
    }

    public boolean getBrowseOnly() {
        return _browseOnly;
    }

    public void setBrowseOnly(boolean browseOnly) {
        _browseOnly = browseOnly;
    }

    public boolean getCreditCard() {
        return _creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        _creditCard = creditCard;
    }

    public boolean getNoReporting() {
        return _noReporting;
    }

    public void setNoReporting(boolean noReporting) {
        _noReporting = noReporting;
    }

    public boolean getOnAccount() {
        return _onAccount;
    }

    public void setOnAccount(boolean onAccount) {
        _onAccount = onAccount;
    }

    public boolean getPoNumRequired() {
        return _poNumRequired;
    }

    public void setPoNumRequired(boolean poNumRequired) {
        _poNumRequired = poNumRequired;
    }

    public boolean getShowPrice() {
        return _showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        _showPrice = showPrice;
    }

    public boolean getContractItemsOnly() {
        return _contractItemsOnly;
    }

    public void setContractItemsOnly(boolean contractItemsOnly) {
        _contractItemsOnly = contractItemsOnly;
    }
    
    public String parseUnselectable() {
        String _unselectable = "";
        if (_user != null && Utility.isSet(_userRights)){
            _unselectable = _userRights;
            _unselectable= _unselectable.replace(USER_GETS_EMAIL_WORK_ORDER_COMPLETED +"^", "");
            _unselectable= _unselectable.replace(USER_GETS_EMAIL_WORK_ORDER_ACCEPTED +"^", "");
            _unselectable= _unselectable.replace(USER_GETS_EMAIL_WORK_ORDER_REJECTED +"^", "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.CAN_EDIT_SHIPTO, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.CAN_EDIT_BILLTO, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.SERVICE_VENDOR_ROLE, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.SITE_MANAGER_ROLE, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.CREDIT_CARD, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.OTHER_PAYMENT, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.SHOW_PRICE, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.BROWSE_ONLY, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.CONTRACT_ITEMS_ONLY, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.ON_ACCOUNT, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.PO_NUM_REQUIRED, "");
            _unselectable= _unselectable.replace(Constants.USER_ROLE.NO_REPORTING, "");
            _unselectable= _unselectable.replace(RefCodeNames.WORKFLOW_ROLE_CD.ORDER_APPROVER +"^", "");

        }
         return _unselectable;
    }


}
