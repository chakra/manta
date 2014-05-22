package com.espendwise.manta.web.validator;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.AmountValidator;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.IdValidator;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.LongValidator;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.util.validation.rules.SkuNotFoundRule;
import com.espendwise.manta.web.forms.WorkflowRuleForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;

public class WorkflowRuleFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(WorkflowRuleFormValidator.class);

    public WorkflowRuleFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        WorkflowRuleForm valueObj = (WorkflowRuleForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        TextValidator shortDBCodeValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DB_CODE_LENGTH);
        TextValidator expressionValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);
        TextValidator longTxtValidator =Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SPEC_400_LENGTH);

        LongValidator longValidator = Validators.getLongValidator();
        IdValidator idValidator = Validators.getIdValidator();
        AmountValidator amountValidator = Validators.getAmountValidator();
        IntegerValidator intValidator = Validators.getIntegerValidator();

        logger.info("validate() ===> valueObj.getWorkflowRuleTypeCd()= "+ valueObj.getWorkflowRuleTypeCd());
        vr = shortDescValidator.validate(valueObj.getWorkflowRuleTypeCd(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.ruleType"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        if (Utility.isSet(valueObj.getRuleNumber())){
	        NumberErrorWebResolver resolver = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.ruleNumber");
	        CodeValidationResult vr1 = intValidator.validate(valueObj.getRuleNumber(), resolver);
	        if (vr1 != null) {
	        	errors.putErrors(vr1.getResult());
	        }
        }
        
        if (Utility.isSet(valueObj.getRuleGroup())){
	        vr = shortDBCodeValidator.validate(valueObj.getRuleGroup(), new TextErrorWebResolver("admin.account.workflowRuleEdit.label.ruleGroup"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        if (errors.isEmpty()){
        // ORDER TOTAL Rule type validation       
	        if (!valueObj.getIsBreakWorkflowRuleType()) {
	
	        	vr = shortDescValidator.validate(valueObj.getRuleAction(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.action"));
		        if (vr != null) {
		            errors.putErrors(vr.getResult());
		        } else if (RefCodeNames.WORKFLOW_RULE_ACTION.FWD_FOR_APPROVAL.equals(valueObj.getRuleAction()) ||
		        		   RefCodeNames.WORKFLOW_RULE_ACTION.STOP_ORDER.equals(valueObj.getRuleAction())) {
			        vr = shortDescValidator.validate(valueObj.getNextActionCd(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.nextAction"));
			        if (vr != null) {
			            errors.putErrors(vr.getResult());
			        }
			    }  else if (RefCodeNames.WORKFLOW_RULE_ACTION.SEND_EMAIL.equals(valueObj.getRuleAction())) {
			        logger.info("validate() ===> valueObj.getEmailUserId()= "+ valueObj.getEmailUserId());
			        vr = idValidator.validate(valueObj.getEmailUserId(), new TextErrorWebResolver("admin.account.workflowRuleEdit.label.userId"));
			        if (vr != null) {
			            errors.putErrors(vr.getResult());
			        }

			    }
	
	        }
	        if (   	valueObj.getIsOrderTotalRuleType() ||
	            	valueObj.getIsBudgetYTDSpendingRuleType() ||
	            	valueObj.getIsCostCenterBudgetRuleType() ||
	            	valueObj.getIsItemCategoryRuleType() ||
	            	valueObj.getIsCategoryTotalRuleType() ||
	            	valueObj.getIsOrderSkuQtyRuleType()  ) {
	        	
	 	        vr = expressionValidator.validate(valueObj.getTotalExp(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.expression"));
		        if (vr != null) {
		            errors.putErrors(vr.getResult());
                }
            }

	        if ( // valueObj.getIsOrderTotalRuleType() ||
	             // valueObj.getIsBudgetYTDSpendingRuleType() ||
	             // valueObj.getIsOrderSkuRuleType() ||
	             // valueObj.getIsOrderSkuQtyRuleType() ||
	             // valueObj.getIsOrderVelocityRuleType() ||
	             //	valueObj.getIsCostCenterBudgetRuleType() ||
	             //	valueObj.getIsItemCategoryRuleType() ||
	             // valueObj.getIsCategoryTotalRuleType() ||
	             //	valueObj.getIsUserLimitRuleType()  ||
	                valueObj.getIsOrderExcludedFromBudgetRuleType() ) {

	 	        vr = expressionValidator.validate(valueObj.getTotalValue(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.expression"));
		        if (vr != null) {
		            errors.putErrors(vr.getResult());
		        }


	        }	
	        
	//        if (errors.isEmpty()) {
		        if (valueObj.getIsOrderTotalRuleType() ||
		        	valueObj.getIsBudgetYTDSpendingRuleType() ||
		        	valueObj.getIsCostCenterBudgetRuleType() ||
		        	valueObj.getIsCategoryTotalRuleType() ||
		        	valueObj.getIsItemCategoryRuleType() ) {
			
			        NumberErrorWebResolver resolver = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValue");
			        CodeValidationResult vr1 = amountValidator.validate(valueObj.getTotalValue(), resolver);
			        if (vr1 != null) {
			        	errors.putErrors(vr1.getResult());
			        }
			        else {
			        	//MANTA-676 make sure the length doesn't exceed 30 characters
			        	if (Utility.isSet(valueObj.getTotalValue())) {
				 	        vr = expressionValidator.validate(valueObj.getTotalValue(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValue"));
					        if (vr != null) {
					            errors.putErrors(vr.getResult());
					        }
			        		
			        	}
			        }
		        }
	
		        if (valueObj.getIsItemCategoryRuleType()) {
			        vr = longValidator.validate(valueObj.getItemCategoryId(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueItemCategory"));
			        if (vr != null) {
			            errors.putErrors(vr.getResult());
			        }
	
		        }
		        if (valueObj.getIsCategoryTotalRuleType()) {
				        vr = longValidator.validate(valueObj.getItemCategoryId(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueCategory"));
				        if (vr != null) {
				            errors.putErrors(vr.getResult());
				        }
		
			    }
		        
		        if (valueObj.getIsOrderVelocityRuleType()) {
			        NumberErrorWebResolver resolver = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValue");
			        CodeValidationResult vr1 = intValidator.validate(valueObj.getTotalValue(), resolver, false);
			        if (vr1 != null) {
			        	errors.putErrors(vr1.getResult());
			        }
		        }
	
		        if ( valueObj.getIsOrderSkuRuleType() ) {
		        
//			        vr = longValidator.validate(valueObj.getTotalValue(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueSystemSku"));
//			        if (vr != null) {
//			            errors.putErrors(vr.getResult());
//			        }
		        	NumberErrorWebResolver resolver = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueSystemSku");
			        CodeValidationResult vr1 = intValidator.validate(valueObj.getTotalValue(), resolver);
			        if (vr1 != null) {
			        	errors.putErrors(vr1.getResult());
			        } else {
			        	Long longVal = null;
			        	try {
			        		longVal = new Long( Long.parseLong(valueObj.getTotalValue()));
			        	} catch (Exception e) {
			        		// already catched
			        	}
				        //  validate TotalValue that SKU exist
			    		ServiceLayerValidation validation = new ServiceLayerValidation();
			            validation.addRule(new SkuNotFoundRule(null, Utility.toList(longVal)));
			            validation.validate();
			        }
	
		        }
		        
		        if ( valueObj.getIsItemRuleType() ) {
		      		//Set<String> skus = Utility.anyDelimitedListToSet(valueObj.getFilteredItemCommaIds());
		        	
		        	// Code change for bug MANTA-879
		        	Set<String> skus = Utility.anyDelimitedListToSet(valueObj.getFilteredItemCommaSkus());
		      		if (!Utility.isSet(skus)) {
	            		errors.add(new WebError("validation.web.error.workflow.error.rulesSkuNotSpecified"));
		      		}
		      		else {
			        	List<Long> skuList = new ArrayList<Long>();
			        	Set<String> errSkus = new HashSet<String>();
			        	for (String sku : skus ){
					        Long longVal = null;
				        	try {
				        		longVal = new Long( Long.parseLong(sku));
				        		skuList.add(longVal);
				        	} catch (Exception e) {
				        		errSkus.add(sku);
				        	}
				        }
			        	if (!errSkus.isEmpty()){
			        		NumberErrorWebResolver resolver = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueCombinedNumSkus");
					        CodeValidationResult vr2 = intValidator.validate(errSkus.toString(), resolver);
					        if (vr2 != null) {
					        	errors.putErrors(vr2.getResult());
					        }	
					     }else if (/*errors.isEmpty() &&*/  !skuList.isEmpty()){
						        //  validate TotalValue that SKU exist
					    		ServiceLayerValidation validation = new ServiceLayerValidation();
					            validation.addRule(new SkuNotFoundRule(null, skuList));
					            validation.validate();
				        }		        	
		      		}
		        }
	
		        if ( valueObj.getIsOrderSkuQtyRuleType() ) {
	
			        NumberErrorWebResolver resolver1 = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueItems");
			        CodeValidationResult vr1 = intValidator.validate(valueObj.getTotalValue(), resolver1);
			        if (vr1 != null) {
			        	errors.putErrors(vr1.getResult());
			        }
		 	        vr = shortDescValidator.validate(valueObj.getSkuNames(), new TextErrorWebResolver("admin.account.workflowRuleEdit.label.ifCombinedOfSku"));
			        if (vr != null) {
			            errors.putErrors(vr.getResult());
			        } else {
			      		Set<String> skus = Utility.anyDelimitedListToSet(valueObj.getSkuNames());
			      		if (!Utility.isSet(skus)) {
		            		errors.add(new WebError("validation.web.error.workflow.error.rulesSkuNotSpecified"));
			      		}
			      		else {
				        	List<Long> skuList = new ArrayList<Long>();
				        	Set<String> errSkus = new HashSet<String>();
				        	for (String sku : skus ){
						        Long longVal = null;
					        	try {
					        		longVal = new Long( Long.parseLong(sku));
					        		skuList.add(longVal);
					        	} catch (Exception e) {
					        		errSkus.add(sku);
					        	}
					        }
				        	if (!errSkus.isEmpty()){
				        		NumberErrorWebResolver resolver = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueCombinedNumSkus");
						        CodeValidationResult vr2 = intValidator.validate(errSkus.toString(), resolver);
						        if (vr2 != null) {
						        	errors.putErrors(vr2.getResult());
						        }	
						     } else if (/*errors.isEmpty() &&*/  !skuList.isEmpty()){
							        //  validate TotalValue that SKU exist
						    		ServiceLayerValidation validation = new ServiceLayerValidation();
						            validation.addRule(new SkuNotFoundRule(null, skuList));
						            validation.validate();
					        }
			      		}
			        }    
			    }
	
		        if ( valueObj.getIsUserLimitRuleType() ) {
	
			        NumberErrorWebResolver resolver1 = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueOrderTotal");
			        CodeValidationResult vr1 = amountValidator.validate(valueObj.getTotalValue(), resolver1);
			        if (vr1 != null) {
			        	errors.putErrors(vr1.getResult());
			        }
			        else {
			        	//MANTA-713 make sure the length doesn't exceed 30 characters
			        	if (Utility.isSet(valueObj.getTotalValue())) {
				 	        vr1 = expressionValidator.validate(valueObj.getTotalValue(), new TextErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueOrderTotal"));
					        if (vr1 != null) {
					            errors.putErrors(vr1.getResult());
					        }
			        	}
			        }
			        NumberErrorWebResolver resolver2 = new NumberErrorWebResolver("admin.account.workflowRuleEdit.text.expressionValueDays");
			        CodeValidationResult vr2 = intValidator.validate(valueObj.getTotalExp(), resolver2);
			        if (vr2 != null) {
			        	errors.putErrors(vr2.getResult());
			        }
				        
		        }

		    if (Utility.isSet(valueObj.getRuleMessage())) {
		        vr = longTxtValidator.validate(valueObj.getRuleMessage(),
		                    new TextErrorWebResolver("admin.account.workflowRuleEdit.label.warningMessage"));
		        if (vr != null) {
		            errors.putErrors(vr.getResult());
		        }
		    }
	
	//        }
        }  
        return new MessageValidationResult(errors.get());
    }


}
