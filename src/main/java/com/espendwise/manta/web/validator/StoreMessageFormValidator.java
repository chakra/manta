package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.StoreMessageDetailForm;
import com.espendwise.manta.web.forms.StoreMessageForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;
import org.springframework.validation.Validator;

import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class StoreMessageFormValidator extends AbstractFormValidator implements Validator, FormValidator {

    private static final Logger logger = Logger.getLogger(StoreMessageFormValidator.class);

    public StoreMessageFormValidator() {
        super();
    }

    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        String datePattern = AppI18nUtil.getDatePattern();
        Date now = Utility.setToMidnight(new Date());

        StoreMessageForm valueObj = (StoreMessageForm) obj;

        ValidationResult vr;

        if (valueObj.getMessageType().equals(RefCodeNames.MESSAGE_TYPE_CD.FORCE_READ)) {
            IntegerValidator integerValidator = Validators.getIntegerValidator();
            boolean mayBeZero = false;
            vr = integerValidator.validate(valueObj.getForcedReadCount(),
                            new NumberErrorWebResolver("admin.message.label.forcedReadCount"),
                            mayBeZero);
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if(!(valueObj.getMessageType().equals(RefCodeNames.MESSAGE_TYPE_CD.FORCE_READ))){
        	if(valueObj.getForcedReadCount() != null){
        		valueObj.setForcedReadCount("");
        	}
        }
        
        if (valueObj.getDisplayOrder() != null) {
            IntegerValidator integerValidator = Validators.getIntegerValidator();
            vr = integerValidator.validate(valueObj.getDisplayOrder(),
                            new NumberErrorWebResolver("admin.message.label.displayOrder"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        	
            if (Utility.isSetIgnorePattern(valueObj.getPostedDate(), AppI18nUtil.getDatePatternPrompt())
                    || Utility.isSetIgnorePattern(valueObj.getEndDate(), AppI18nUtil.getDatePatternPrompt())) {

                if (Utility.isSetIgnorePattern(valueObj.getPostedDate(), AppI18nUtil.getDatePatternPrompt())) {
                    if (!valueObj.getPublished()) {
                        DateValidator dateValidator = Validators.getDateValidator(datePattern);
                        vr = dateValidator.validate(valueObj.getPostedDate(), new DateErrorWebResolver("admin.message.label.postingDate"));
                        if (vr != null) {
                            errors.putErrors(vr.getResult());
                        }
                    }
                }

                if (Utility.isSetIgnorePattern(valueObj.getEndDate(), AppI18nUtil.getDatePatternPrompt())) {

                    DateValidator dateValidator = Validators.getDateValidator(datePattern);
                    vr = dateValidator.validate(valueObj.getEndDate(), new DateErrorWebResolver("admin.message.label.endDate"));
                    if (vr != null) {
                        errors.putErrors(vr.getResult());
                    }
                }


            }

           /* if (Utility.isSetIgnorePattern(valueObj.getPostedDate(), AppI18nUtil.getDatePatternPrompt())
                    && Utility.isSetIgnorePattern(valueObj.getEndDate(), AppI18nUtil.getDatePatternPrompt())) {
                    DateValidator dateValidator = Validators.getDateValidator(datePattern);
                    vr = dateValidator.validate(valueObj.getPostedDate(), new DateErrorWebResolver("admin.message.label.postingDate"));
            }     */
            
            {
                TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.NAME_LENGTH);
                vr = validator.validate(valueObj.getName(), new TextErrorWebResolver("admin.message.label.name"));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }
            }
            
            {
                TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);
                vr = validator.validate(valueObj.getStoreMessageStatusCd(), new TextErrorWebResolver("admin.message.label.status"));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }
            }

            for(StoreMessageDetailForm detailObj : valueObj.getDetail()){
	            {
	                TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.NAME_LENGTH);
	                vr = validator.validate(detailObj.getMessageTitle(), new TextErrorWebResolver("admin.message.label.storeMessageTitle"));
	                if (vr != null) {
	                    errors.putErrors(vr.getResult());
	                }
	            }
	
	            if(valueObj.getStoreMessageId()>0){
		            	
	            	if(detailObj.getMessageDetailTypeCd()==null ||
	            			!detailObj.getMessageDetailTypeCd().equals(RefCodeNames.MESSAGE_DETAIL_TYPE_CD.DEFAULT)){
	            		TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);
	            		vr = validator.validate(detailObj.getLanguageCd(), new TextErrorWebResolver("admin.message.label.language"));
	            		if (vr != null) {
	            			errors.putErrors(vr.getResult());
	            		}
	            	}
		           
	            }
	            
	            {
	                TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SPEC_60_LENGTH);
	                vr = validator.validate(detailObj.getMessageAuthor(), new TextErrorWebResolver("admin.message.label.author"));
	                if (vr != null) {
	                    errors.putErrors(vr.getResult());
	                }
	            }
	
	
	            {
	                TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SPEC_200_LENGTH);
	                vr = validator.validate(detailObj.getMessageAbstract(), new TextErrorWebResolver("admin.message.label.messageAbstract"));
	                if (vr != null) {
	                    errors.putErrors(vr.getResult());
	                }
	            }
	
	            {
	                if (Utility.isSet(detailObj.getMessageBody())) {
	                    TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.BIG_TEXT_LENGTH);
	                    vr = validator.validate(detailObj.getMessageBody(), new TextErrorWebResolver("admin.message.label.messageBody"));
	                    if (vr != null) {
	                        errors.putErrors(vr.getResult());
	                    }
	                }
	            }
            }

        return new MessageValidationResult(errors.get());

    }

}


