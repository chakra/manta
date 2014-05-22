package com.espendwise.manta.web.validator;

import org.apache.log4j.Logger;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.DoubleValidator;
import com.espendwise.manta.util.validation.EmailAddressValidator;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.DistributorConfigurationForm;
import com.espendwise.manta.web.resolver.EmailAddressErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class DistributorConfigurationFormValidator extends AbstractFormValidator {
	
	private static final Logger logger = Logger.getLogger(DistributorConfigurationFormValidator.class);

    public DistributorConfigurationFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {
    	
    	WebErrors errors = new WebErrors();

    	DistributorConfigurationForm valueObj = (DistributorConfigurationForm) obj;

        ValidationResult vr = null;
        
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        TextValidator lengthValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.BIG_TEXT_LENGTH);
    	EmailAddressValidator emailValidator = Validators.getEmailAddressValidator(true);
    	DoubleValidator doubleValidator = Validators.getDoubleValidator();
    	IntegerValidator integerValidator = Validators.getIntegerValidator();
        
        if(Utility.isSet(valueObj.getPerformSalesTaxCheck())){
	        vr = lengthValidator.validate(valueObj.getPerformSalesTaxCheck(), new TextErrorWebResolver("admin.distributor.label.configuration.performSalesTaxCheck"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getExceptionOnOverchargedFreight())){
	        vr = lengthValidator.validate(valueObj.getExceptionOnOverchargedFreight(), new TextErrorWebResolver("admin.distributor.label.configuration.exceptionOnOverchargedFreight"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getInvoiceLoadingPricingModel())){
	        vr = lengthValidator.validate(valueObj.getInvoiceLoadingPricingModel(), new TextErrorWebResolver("admin.distributor.label.configuration.invoiceLoadingPricingModel"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getAllowFreightOnBackOrders())){
	        vr = lengthValidator.validate(valueObj.getAllowFreightOnBackOrders(), new TextErrorWebResolver("admin.distributor.label.configuration.allowFreightOnBackorders"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getCancelBackorderedLines())){
	        vr = lengthValidator.validate(valueObj.getCancelBackorderedLines(), new TextErrorWebResolver("admin.distributor.label.configuration.cancelBackorderedLines"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getDisallowInvoiceEdits())){
	        vr = lengthValidator.validate(valueObj.getDisallowInvoiceEdits(), new TextErrorWebResolver("admin.distributor.label.configuration.disallowInvoiceEdits"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getReceivingSystemTypeCode())){
	        vr = lengthValidator.validate(valueObj.getReceivingSystemTypeCode(), new TextErrorWebResolver("admin.distributor.label.configuration.receivingSystemTypeCode"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        //check to make sure the reject invoice email address(es) are valid
        if (valueObj.getRejectedInvoiceEmailNotification() != null) {
        	//MANTA-369 (length of rejected email address not checked)
	        vr = shortDescValidator.validate(valueObj.getRejectedInvoiceEmailNotification(), new TextErrorWebResolver("admin.distributor.label.configuration.rejectedInvoiceEmailNotification"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        	vr = emailValidator.validate(valueObj.getRejectedInvoiceEmailNotification(),
        				new EmailAddressErrorWebResolver("admin.distributor.label.configuration.rejectedInvoiceEmailNotification"));
    		if (vr != null) {
    			errors.putErrors(vr.getResult());
    		}
        }
        
        if(Utility.isSet(valueObj.getIgnoreOrderMinimumForFreight())){
	        vr = lengthValidator.validate(valueObj.getIgnoreOrderMinimumForFreight(), new TextErrorWebResolver("admin.distributor.label.configuration.ignoreOrderMinimumForFreight"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getInvoiceAmountPercentUndercharge())){
	        vr = lengthValidator.validate(valueObj.getInvoiceAmountPercentUndercharge(), new TextErrorWebResolver("admin.distributor.label.configuration.invoiceAmountPercentAllowanceUndercharge"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
	        vr = doubleValidator.validate(valueObj.getInvoiceAmountPercentUndercharge(), new NumberErrorWebResolver("admin.distributor.label.configuration.invoiceAmountPercentAllowanceUndercharge"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getInvoiceAmountPercentOvercharge())){
	        vr = lengthValidator.validate(valueObj.getInvoiceAmountPercentOvercharge(), new TextErrorWebResolver("admin.distributor.label.configuration.invoiceAmountPercentAllowanceOvercharge"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
	        vr = doubleValidator.validate(valueObj.getInvoiceAmountPercentOvercharge(), new NumberErrorWebResolver("admin.distributor.label.configuration.invoiceAmountPercentAllowanceOvercharge"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getInvoiceMaximumFreightAllowance())){
	        vr = lengthValidator.validate(valueObj.getInvoiceMaximumFreightAllowance(), new TextErrorWebResolver("admin.distributor.label.configuration.maximumInvoiceFreightAllowed"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
	        vr = doubleValidator.validate(valueObj.getInvoiceMaximumFreightAllowance(), new NumberErrorWebResolver("admin.distributor.label.configuration.maximumInvoiceFreightAllowed"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getInboundInvoiceHoldDays())){
	        vr = lengthValidator.validate(valueObj.getInboundInvoiceHoldDays(), new TextErrorWebResolver("admin.distributor.label.configuration.holdInboundInvoice"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
	        vr = integerValidator.validate(valueObj.getInboundInvoiceHoldDays(), new TextErrorWebResolver("admin.distributor.label.configuration.holdInboundInvoice"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getPrintCustomerContactInfoOnPurchaseOrder())){
	        vr = lengthValidator.validate(valueObj.getPrintCustomerContactInfoOnPurchaseOrder(), new TextErrorWebResolver("admin.distributor.label.configuration.printCustomerContactInfo"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getRequireManualPurchaseOrderAcknowledgement())){
	        vr = lengthValidator.validate(valueObj.getRequireManualPurchaseOrderAcknowledgement(), new TextErrorWebResolver("admin.distributor.label.configuration.manualPurchaseOrderAcknowledgementRequired"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if(Utility.isSet(valueObj.getPurchaseOrderComments())){
	        vr = lengthValidator.validate(valueObj.getPurchaseOrderComments(), new TextErrorWebResolver("admin.distributor.label.configuration.purchaseOrderComments"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        return new MessageValidationResult(errors.get());
    }
}
