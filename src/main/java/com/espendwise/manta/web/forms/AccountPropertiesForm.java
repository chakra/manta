package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.RefCodeNames;

//@Validation(AccountPropertiesFormValidator.class)
public class AccountPropertiesForm extends WebForm implements Initializable {
	
    public static String[] PROPERTY_EXTRA_TYPE_CDS = new String[]{
    	RefCodeNames.PROPERTY_TYPE_CD.ALLOW_USER_CHANGE_PASSWORD,
    	RefCodeNames.PROPERTY_TYPE_CD.ALLOW_CC_PAYMENT,
    	RefCodeNames.PROPERTY_TYPE_CD.SUPPORTS_BUDGET
    };
    
    public static String[] PROPERTY_TYPE_CDS = new String[]{
    	RefCodeNames.PROPERTY_TYPE_CD.ALLOW_CUSTOMER_PO_NUMBER,
    	RefCodeNames.PROPERTY_TYPE_CD.TAXABLE_INDICATOR,
    	RefCodeNames.PROPERTY_TYPE_CD.AUTHORIZED_FOR_RESALE,
    	RefCodeNames.PROPERTY_TYPE_CD.CUSTOMER_SYSTEM_APPROVAL_CD,
        RefCodeNames.PROPERTY_TYPE_CD.TRACK_PUNCHOUT_ORDER_MESSAGES,
    	RefCodeNames.PROPERTY_TYPE_CD.ALTERNATE_UI
    };

	private boolean initialize;
	
	private Long accountId;
	private boolean allowToChangePassword;
	private boolean allowToEnterPurchaseNum;
	private boolean allowToPayCreditCard;
	private boolean useEstimatedSalesTax;
	private boolean useResaleItems;
	private boolean useBudgets;
	private boolean useAlternateUserInterface;
	
	private String purchasingSystem;
	private boolean trackPunchoutOrderMessages;

	public AccountPropertiesForm() {
    }

	public AccountPropertiesForm(Long accountId) {
        this.accountId = accountId;
    }
	
	/**
	 * @return the accountId
	 */
	public final Long getAccountId() {
		return accountId;
	}


	/**
	 * @param accountId the accountId to set
	 */
	public final void setAccountId(Long accountId) {
		this.accountId = accountId;
	}


	/**
	 * @return the initialize
	 */
	public final boolean isInitialize() {
		return initialize;
	}

	/**
	 * @param initialize the initialize to set
	 */
	public final void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}


	public boolean isAllowToChangePassword() {
		return allowToChangePassword;
	}

	public void setAllowToChangePassword(boolean allowToChangePassword) {
		this.allowToChangePassword = allowToChangePassword;
	}

	public boolean isAllowToEnterPurchaseNum() {
		return allowToEnterPurchaseNum;
	}

	public void setAllowToEnterPurchaseNum(boolean allowToEnterPurchaseNum) {
		this.allowToEnterPurchaseNum = allowToEnterPurchaseNum;
	}

	public boolean isAllowToPayCreditCard() {
		return allowToPayCreditCard;
	}

	public void setAllowToPayCreditCard(boolean allowToPayCreditCard) {
		this.allowToPayCreditCard = allowToPayCreditCard;
	}

	public boolean isUseEstimatedSalesTax() {
		return useEstimatedSalesTax;
	}

	public void setUseEstimatedSalesTax(boolean useEstimatedSalesTax) {
		this.useEstimatedSalesTax = useEstimatedSalesTax;
	}

	public boolean isUseResaleItems() {
		return useResaleItems;
	}

	public void setUseResaleItems(boolean useResaleItems) {
		this.useResaleItems = useResaleItems;
	}

	public boolean isUseBudgets() {
		return useBudgets;
	}

	public void setUseBudgets(boolean useBudgets) {
		this.useBudgets = useBudgets;
	}

	public boolean isUseAlternateUserInterface() {
		return useAlternateUserInterface;
	}

	public void setUseAlternateUserInterface(boolean useAlternateUserInterface) {
		this.useAlternateUserInterface = useAlternateUserInterface;
	}

	public String getPurchasingSystem() {
		return purchasingSystem;
	}

	public void setPurchasingSystem(String purchasingSystem) {
		this.purchasingSystem = purchasingSystem;
	}

	public boolean isTrackPunchoutOrderMessages() {
		return trackPunchoutOrderMessages;
	}

	public void setTrackPunchoutOrderMessages(boolean trackPunchoutOrderMessages) {
		this.trackPunchoutOrderMessages = trackPunchoutOrderMessages;
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

}
