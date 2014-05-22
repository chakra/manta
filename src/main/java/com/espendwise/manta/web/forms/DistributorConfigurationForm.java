package com.espendwise.manta.web.forms;

import java.util.List;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.DistributorConfigurationFormValidator;

@Validation(DistributorConfigurationFormValidator.class)
public class DistributorConfigurationForm extends WebForm implements Initializable {
	
	private boolean initialize;
	
	private Long distributorId;
	private String performSalesTaxCheck;
	private String exceptionOnOverchargedFreight;
	private String invoiceLoadingPricingModel;
	private String allowFreightOnBackOrders;
	private String cancelBackorderedLines;
	private String disallowInvoiceEdits;
	private String receivingSystemTypeCode;
	private String rejectedInvoiceEmailNotification;
	private String ignoreOrderMinimumForFreight;
	private String invoiceAmountPercentUndercharge;
	private String invoiceAmountPercentOvercharge;
	private String invoiceMaximumFreightAllowance;
	private String inboundInvoiceHoldDays;
	private String printCustomerContactInfoOnPurchaseOrder;
	private String requireManualPurchaseOrderAcknowledgement;
	private String purchaseOrderComments;
    
    //reference data
    private List<Pair<String, String>> invoiceLoadingPricingModelChoices;
    private List<Pair<String, String>> receivingSystemTypeCodeChoices;

	public DistributorConfigurationForm() {
    }

	public DistributorConfigurationForm(Long distributorId) {
        this.distributorId = distributorId;
    }

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public String getPerformSalesTaxCheck() {
		return performSalesTaxCheck;
	}

	public void setPerformSalesTaxCheck(String performSalesTaxCheck) {
		this.performSalesTaxCheck = performSalesTaxCheck;
	}

	public String getExceptionOnOverchargedFreight() {
		return exceptionOnOverchargedFreight;
	}

	public void setExceptionOnOverchargedFreight(
			String exceptionOnOverchargedFreight) {
		this.exceptionOnOverchargedFreight = exceptionOnOverchargedFreight;
	}

	public String getInvoiceLoadingPricingModel() {
		return invoiceLoadingPricingModel;
	}

	public void setInvoiceLoadingPricingModel(String invoiceLoadingPricingModel) {
		this.invoiceLoadingPricingModel = invoiceLoadingPricingModel;
	}

	public String getAllowFreightOnBackOrders() {
		return allowFreightOnBackOrders;
	}

	public void setAllowFreightOnBackOrders(String allowFreightOnBackOrders) {
		this.allowFreightOnBackOrders = allowFreightOnBackOrders;
	}

	public String getCancelBackorderedLines() {
		return cancelBackorderedLines;
	}

	public void setCancelBackorderedLines(String cancelBackorderedLines) {
		this.cancelBackorderedLines = cancelBackorderedLines;
	}

	public String getDisallowInvoiceEdits() {
		return disallowInvoiceEdits;
	}

	public void setDisallowInvoiceEdits(String disallowInvoiceEdits) {
		this.disallowInvoiceEdits = disallowInvoiceEdits;
	}

	public String getReceivingSystemTypeCode() {
		return receivingSystemTypeCode;
	}

	public void setReceivingSystemTypeCode(String receivingSystemTypeCode) {
		this.receivingSystemTypeCode = receivingSystemTypeCode;
	}

	public String getRejectedInvoiceEmailNotification() {
		return rejectedInvoiceEmailNotification;
	}

	public void setRejectedInvoiceEmailNotification(
			String rejectedInvoiceEmailNotification) {
		this.rejectedInvoiceEmailNotification = rejectedInvoiceEmailNotification;
	}

	public String getIgnoreOrderMinimumForFreight() {
		return ignoreOrderMinimumForFreight;
	}

	public void setIgnoreOrderMinimumForFreight(String ignoreOrderMinimumForFreight) {
		this.ignoreOrderMinimumForFreight = ignoreOrderMinimumForFreight;
	}

	public String getInvoiceAmountPercentUndercharge() {
		return invoiceAmountPercentUndercharge;
	}

	public void setInvoiceAmountPercentUndercharge(
			String invoiceAmountPercentUndercharge) {
		this.invoiceAmountPercentUndercharge = invoiceAmountPercentUndercharge;
	}

	public String getInvoiceAmountPercentOvercharge() {
		return invoiceAmountPercentOvercharge;
	}

	public void setInvoiceAmountPercentOvercharge(
			String invoiceAmountPercentOvercharge) {
		this.invoiceAmountPercentOvercharge = invoiceAmountPercentOvercharge;
	}

	public String getInvoiceMaximumFreightAllowance() {
		return invoiceMaximumFreightAllowance;
	}

	public void setInvoiceMaximumFreightAllowance(
			String invoiceMaximumFreightAllowance) {
		this.invoiceMaximumFreightAllowance = invoiceMaximumFreightAllowance;
	}

	public String getInboundInvoiceHoldDays() {
		return inboundInvoiceHoldDays;
	}

	public void setInboundInvoiceHoldDays(String inboundInvoiceHoldDays) {
		this.inboundInvoiceHoldDays = inboundInvoiceHoldDays;
	}

	public String getPrintCustomerContactInfoOnPurchaseOrder() {
		return printCustomerContactInfoOnPurchaseOrder;
	}

	public void setPrintCustomerContactInfoOnPurchaseOrder(
			String printCustomerContactInfoOnPurchaseOrder) {
		this.printCustomerContactInfoOnPurchaseOrder = printCustomerContactInfoOnPurchaseOrder;
	}

	public String getRequireManualPurchaseOrderAcknowledgement() {
		return requireManualPurchaseOrderAcknowledgement;
	}

	public void setRequireManualPurchaseOrderAcknowledgement(
			String requireManualPurchaseOrderAcknowledgement) {
		this.requireManualPurchaseOrderAcknowledgement = requireManualPurchaseOrderAcknowledgement;
	}

	public String getPurchaseOrderComments() {
		return purchaseOrderComments;
	}

	public void setPurchaseOrderComments(String purchaseOrderComments) {
		this.purchaseOrderComments = purchaseOrderComments;
	}

	public List<Pair<String, String>> getInvoiceLoadingPricingModelChoices() {
		return invoiceLoadingPricingModelChoices;
	}

	public void setInvoiceLoadingPricingModelChoices(
			List<Pair<String, String>> invoiceLoadingPricingModelChoices) {
		this.invoiceLoadingPricingModelChoices = invoiceLoadingPricingModelChoices;
	}

	public List<Pair<String, String>> getReceivingSystemTypeCodeChoices() {
		return receivingSystemTypeCodeChoices;
	}

	public void setReceivingSystemTypeCodeChoices(
			List<Pair<String, String>> receivingSystemTypeCodeChoices) {
		this.receivingSystemTypeCodeChoices = receivingSystemTypeCodeChoices;
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
      return isInitialized() && (distributorId  == null || distributorId == 0);
    }

}
