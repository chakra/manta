package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.DistributorConfigurationView;
import com.espendwise.manta.service.DistributorService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.DistributorConfigurationForm;
import com.espendwise.manta.web.resolver.DistributorConfigurationWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;

@Controller
@RequestMapping(UrlPathKey.DISTRIBUTOR.CONFIGURATION)
public class DistributorConfigurationController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(DistributorConfigurationController.class);

    private DistributorService distributorService;
    
    @Autowired
    public DistributorConfigurationController(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {
        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DistributorConfigurationWebUpdateExceptionResolver());
        return "distributor/configuration";
    }
    
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String show(HttpServletRequest request, @ModelAttribute(SessionKey.DISTRIBUTOR_CONFIGURATION) DistributorConfigurationForm form, @PathVariable("distributorId") Long distributorId, Model model) {

        logger.info("show()=> BEGIN");

        DistributorConfigurationView configuration = distributorService.findDistributorConfigurationInformation(distributorId);
        	
        if (configuration != null) {
	        	
        	form.setDistributorId(configuration.getDistributorId());
        	
        	if(Utility.isSet(configuration.getPerformSalesTaxCheck())){
        		form.setPerformSalesTaxCheck(PropertyUtil.toValueNN(configuration.getPerformSalesTaxCheck()));
        	}
        	
        	if(Utility.isSet(configuration.getExceptionOnOverchargedFreight())){
        		form.setExceptionOnOverchargedFreight(PropertyUtil.toValueNN(configuration.getExceptionOnOverchargedFreight()));
        	}
        	
        	if(Utility.isSet(configuration.getInvoiceLoadingPricingModel())){
        		form.setInvoiceLoadingPricingModel(PropertyUtil.toValueNN(configuration.getInvoiceLoadingPricingModel()));
        	}
        	
        	if(Utility.isSet(configuration.getAllowFreightOnBackOrders())){
        		form.setAllowFreightOnBackOrders(PropertyUtil.toValueNN(configuration.getAllowFreightOnBackOrders()));
        	}
        	
        	if(Utility.isSet(configuration.getCancelBackorderedLines())){
        		form.setCancelBackorderedLines(PropertyUtil.toValueNN(configuration.getCancelBackorderedLines()));
        	}
        	
        	if(Utility.isSet(configuration.getDisallowInvoiceEdits())){
        		form.setDisallowInvoiceEdits(PropertyUtil.toValueNN(configuration.getDisallowInvoiceEdits()));
        	}
        	
        	if(Utility.isSet(configuration.getReceivingSystemTypeCode())){
        		form.setReceivingSystemTypeCode(PropertyUtil.toValueNN(configuration.getReceivingSystemTypeCode()));
        	}
        	
        	if(Utility.isSet(configuration.getRejectedInvoiceEmailNotification())){
        		form.setRejectedInvoiceEmailNotification(Utility.strNN(configuration.getRejectedInvoiceEmailNotification().getEmailAddress()));
        	}
        	
        	if(Utility.isSet(configuration.getIgnoreOrderMinimumForFreight())){
        		form.setIgnoreOrderMinimumForFreight(PropertyUtil.toValueNN(configuration.getIgnoreOrderMinimumForFreight()));
        	}
        	
        	if(Utility.isSet(configuration.getInvoiceAmountPercentUndercharge())){
        		form.setInvoiceAmountPercentUndercharge(PropertyUtil.toValueNN(configuration.getInvoiceAmountPercentUndercharge()));
        	}
        	
        	if(Utility.isSet(configuration.getInvoiceAmountPercentOvercharge())){
        		form.setInvoiceAmountPercentOvercharge(PropertyUtil.toValueNN(configuration.getInvoiceAmountPercentOvercharge()));
        	}
        	
        	if(Utility.isSet(configuration.getInvoiceMaximumFreightAllowance())){
        		form.setInvoiceMaximumFreightAllowance(PropertyUtil.toValueNN(configuration.getInvoiceMaximumFreightAllowance()));
        	}
        	
        	if(Utility.isSet(configuration.getInboundInvoiceHoldDays())){
        		form.setInboundInvoiceHoldDays(PropertyUtil.toValueNN(configuration.getInboundInvoiceHoldDays()));
        	}
        	
        	if(Utility.isSet(configuration.getPrintCustomerContactInfoOnPurchaseOrder())){
        		form.setPrintCustomerContactInfoOnPurchaseOrder(PropertyUtil.toValueNN(configuration.getPrintCustomerContactInfoOnPurchaseOrder()));
        	}
        	
        	if(Utility.isSet(configuration.getRequireManualPurchaseOrderAcknowledgement())){
        		form.setRequireManualPurchaseOrderAcknowledgement(PropertyUtil.toValueNN(configuration.getRequireManualPurchaseOrderAcknowledgement()));
        	}
        	
        	if(Utility.isSet(configuration.getPurchaseOrderComments())){
        		form.setPurchaseOrderComments(PropertyUtil.toValueNN(configuration.getPurchaseOrderComments()));
        	}
        	
        }

        populateFormReferenceData(form);
        model.addAttribute(SessionKey.DISTRIBUTOR_CONFIGURATION, form);

        logger.info("show()=> END.");
        
        return "distributor/configuration";

    }
    
    @ModelAttribute(SessionKey.DISTRIBUTOR_CONFIGURATION)
    public DistributorConfigurationForm initModel(@PathVariable("distributorId") Long distributorId) {

    	DistributorConfigurationForm form = new DistributorConfigurationForm(distributorId);
        if (!form.isInitialized()) {
            form.initialize();
        }
        return form;
    }
    
    @SuccessMessage
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.DISTRIBUTOR_CONFIGURATION) DistributorConfigurationForm distributorConfigurationForm,
    		@PathVariable("distributorId") Long distributorId, Model model) throws Exception {

        logger.info("save()=> BEGIN, DistributorConfigurationForm: " + distributorConfigurationForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(distributorConfigurationForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
        }

        //validate values for fields that are restricted to an acceptable set (i.e. only true or false is allowed)
        String performSalesTaxCheck = distributorConfigurationForm.getPerformSalesTaxCheck();
        if (Utility.isSet(performSalesTaxCheck)) {
        	if (!Constants.TRUE.equalsIgnoreCase(performSalesTaxCheck) &&
        			!Constants.FALSE.equalsIgnoreCase(performSalesTaxCheck) &&
        			!Constants.TRUE_FOR_RESALE.equalsIgnoreCase(performSalesTaxCheck)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.performSalesTaxCheck"));
        	}
        }
        String exceptionOnOverchargedFreight = distributorConfigurationForm.getExceptionOnOverchargedFreight();
        if (Utility.isSet(exceptionOnOverchargedFreight)) {
        	if (!Constants.TRUE.equalsIgnoreCase(exceptionOnOverchargedFreight) &&
        			!Constants.FALSE.equalsIgnoreCase(exceptionOnOverchargedFreight)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.exceptionOnOverchargedFreight"));
        	}
        }
        String invoiceLoadingPricingModel = distributorConfigurationForm.getInvoiceLoadingPricingModel();
        if (Utility.isSet(invoiceLoadingPricingModel)) {
        	if (!RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.DISTRIBUTOR_INVOICE.equalsIgnoreCase(invoiceLoadingPricingModel) &&
        			!RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.EXCEPTION.equalsIgnoreCase(invoiceLoadingPricingModel) &&
        			!RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.HOLD_ALL.equalsIgnoreCase(invoiceLoadingPricingModel) &&
        			!RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.LOWEST.equalsIgnoreCase(invoiceLoadingPricingModel) &&
        			!RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.PREDETERMINED.equalsIgnoreCase(invoiceLoadingPricingModel)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.invoiceLoadingPricingModel"));
        	}
        }
        String allowFreightOnBackOrders = distributorConfigurationForm.getAllowFreightOnBackOrders();
        if (Utility.isSet(allowFreightOnBackOrders)) {
        	if (!Constants.TRUE.equalsIgnoreCase(allowFreightOnBackOrders) &&
        			!Constants.FALSE.equalsIgnoreCase(allowFreightOnBackOrders)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.allowFreightOnBackorders"));
        	}
        }
        String cancelBackorderedLines = distributorConfigurationForm.getCancelBackorderedLines();
        if (Utility.isSet(cancelBackorderedLines)) {
        	if (!Constants.TRUE.equalsIgnoreCase(cancelBackorderedLines) &&
        			!Constants.FALSE.equalsIgnoreCase(cancelBackorderedLines)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.cancelBackorderedLines"));
        	}
        }
        String disallowInvoiceEdits = distributorConfigurationForm.getDisallowInvoiceEdits();
        if (Utility.isSet(disallowInvoiceEdits)) {
        	if (!Constants.TRUE.equalsIgnoreCase(disallowInvoiceEdits) &&
        			!Constants.FALSE.equalsIgnoreCase(disallowInvoiceEdits)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.disallowInvoiceEdits"));
        	}
        }
        String receivingSystemTypeCode = distributorConfigurationForm.getReceivingSystemTypeCode();
        if (Utility.isSet(receivingSystemTypeCode)) {
        	if (!RefCodeNames.RECEIVING_SYSTEM_INVOICE_CD.DISABLED.equalsIgnoreCase(receivingSystemTypeCode) &&
        			!RefCodeNames.RECEIVING_SYSTEM_INVOICE_CD.ENTER_ERRORS_ONLY_FIRST_ONLY.equalsIgnoreCase(receivingSystemTypeCode) &&
        			!RefCodeNames.RECEIVING_SYSTEM_INVOICE_CD.REQUIRE_ENTRY_FIRST_ONLY.equalsIgnoreCase(receivingSystemTypeCode)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.receivingSystemTypeCode"));
        	}
        }
        String ignoreOrderMinimumForFreight = distributorConfigurationForm.getIgnoreOrderMinimumForFreight();
        if (Utility.isSet(ignoreOrderMinimumForFreight)) {
        	if (!Constants.TRUE.equalsIgnoreCase(ignoreOrderMinimumForFreight) &&
        			!Constants.FALSE.equalsIgnoreCase(ignoreOrderMinimumForFreight)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.ignoreOrderMinimumForFreight"));
        	}
        }
        String printCustomerContactInfoOnPurchaseOrder = distributorConfigurationForm.getPrintCustomerContactInfoOnPurchaseOrder();
        if (Utility.isSet(printCustomerContactInfoOnPurchaseOrder)) {
        	if (!Constants.TRUE.equalsIgnoreCase(printCustomerContactInfoOnPurchaseOrder) &&
        			!Constants.FALSE.equalsIgnoreCase(printCustomerContactInfoOnPurchaseOrder)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.printCustomerContactInfo"));
        	}
        }
        String requireManualPurchaseOrderAcknowledgement = distributorConfigurationForm.getRequireManualPurchaseOrderAcknowledgement();
        if (Utility.isSet(requireManualPurchaseOrderAcknowledgement)) {
        	if (!Constants.TRUE.equalsIgnoreCase(requireManualPurchaseOrderAcknowledgement) &&
        			!Constants.FALSE.equalsIgnoreCase(requireManualPurchaseOrderAcknowledgement)) {
	            webErrors.putError("validation.web.error.invalidValue", new MessageI18nArgument("admin.distributor.label.configuration.manualPurchaseOrderAcknowledgementRequired"));
        	}
        }

        //if validation errors occurred then return the user to the input page so they can correct the problem(s)
    	if (!webErrors.isEmpty()) {
            populateFormReferenceData(distributorConfigurationForm);
            model.addAttribute(SessionKey.DISTRIBUTOR_CONFIGURATION, distributorConfigurationForm);
            return "distributor/configuration";
    	}

        DistributorConfigurationView configuration = new DistributorConfigurationView();

        if (!distributorConfigurationForm.getIsNew()) {
        	configuration = distributorService.findDistributorConfigurationInformation(distributorId);
        }

        configuration = WebFormUtil.createDistributorConfigurationView(configuration, distributorConfigurationForm);
        
        try {

        	configuration = distributorService.saveDistributorConfigurationInformation(distributorId, configuration);

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }

        logger.info("save()=> END, redirect to " + configuration.getDistributorId());

        return redirect("../configuration");
        
    }

    private void populateFormReferenceData(DistributorConfigurationForm form) {
        //populate the form with reference information (invoice loading pricing model choices,
    	//receiving system type code choices)
        List<Pair<String, String>> invoiceLoadingPricingModels = new ArrayList<Pair<String, String>>();
        invoiceLoadingPricingModels.add(new Pair<String, String>(new MessageI18nArgument("admin.distributor.configuration.invoiceLoadingPricingModel.option.exceptionOnCostDifference").resolve(), RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.EXCEPTION));
        invoiceLoadingPricingModels.add(new Pair<String, String>(new MessageI18nArgument("admin.distributor.configuration.invoiceLoadingPricingModel.option.holdAllInvoicesForReview").resolve(), RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.HOLD_ALL));
        invoiceLoadingPricingModels.add(new Pair<String, String>(new MessageI18nArgument("admin.distributor.configuration.invoiceLoadingPricingModel.option.useDistributorInvoiceCost").resolve(), RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.DISTRIBUTOR_INVOICE));
        invoiceLoadingPricingModels.add(new Pair<String, String>(new MessageI18nArgument("admin.distributor.configuration.invoiceLoadingPricingModel.option.useLowestCost").resolve(), RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.LOWEST));
        invoiceLoadingPricingModels.add(new Pair<String, String>(new MessageI18nArgument("admin.distributor.configuration.invoiceLoadingPricingModel.option.useOurCost").resolve(), RefCodeNames.INVOICE_LOADING_PRICE_MODEL_CD.PREDETERMINED));
        form.setInvoiceLoadingPricingModelChoices(invoiceLoadingPricingModels);
        List<Pair<String, String>> receivingSystemTypeCodes = new ArrayList<Pair<String, String>>();
        receivingSystemTypeCodes.add(new Pair<String, String>(new MessageI18nArgument("admin.distributor.configuration.receivingSystemTypeCode.option.disabled").resolve(), RefCodeNames.RECEIVING_SYSTEM_INVOICE_CD.DISABLED));
        receivingSystemTypeCodes.add(new Pair<String, String>(new MessageI18nArgument("admin.distributor.configuration.receivingSystemTypeCode.option.enterErrorsOnlyFirstOnly").resolve(), RefCodeNames.RECEIVING_SYSTEM_INVOICE_CD.ENTER_ERRORS_ONLY_FIRST_ONLY));
        receivingSystemTypeCodes.add(new Pair<String, String>(new MessageI18nArgument("admin.distributor.configuration.receivingSystemTypeCode.option.requireEntryFirstOnly").resolve(), RefCodeNames.RECEIVING_SYSTEM_INVOICE_CD.REQUIRE_ENTRY_FIRST_ONLY));
        form.setReceivingSystemTypeCodeChoices(receivingSystemTypeCodes);
    }
    
    
}
