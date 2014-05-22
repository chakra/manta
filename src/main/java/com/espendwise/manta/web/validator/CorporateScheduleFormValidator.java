package com.espendwise.manta.web.validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationCodeImpl;
import com.espendwise.manta.util.validation.ValidationReason;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.controllers.CorporateScheduleController;
import com.espendwise.manta.web.forms.CorporateScheduleForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.resolver.TimeErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;

public class CorporateScheduleFormValidator extends AbstractFormValidator {
    private static final Logger logger = Logger.getLogger(CorporateScheduleController.class);

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        CorporateScheduleForm corporateScheduleForm = (CorporateScheduleForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);
        vr = shortDescValidator.validate(corporateScheduleForm.getScheduleName(), new TextErrorWebResolver("admin.corporateSchedule.label.corporateScheduleName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        Set<String> supportedStatusValues = new HashSet<String>();
        supportedStatusValues.add(RefCodeNames.SCHEDULE_STATUS_CD.ACTIVE);
        supportedStatusValues.add(RefCodeNames.SCHEDULE_STATUS_CD.INACTIVE);
        supportedStatusValues.add(RefCodeNames.SCHEDULE_STATUS_CD.LIMITED);
        TextValidator statusValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH, true, supportedStatusValues);
        vr = statusValidator.validate(corporateScheduleForm.getScheduleStatus(), new TextErrorWebResolver("admin.corporateSchedule.label.status"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        IntegerValidator intervalValidator = Validators.getIntegerValidator();
        vr = intervalValidator.validate(corporateScheduleForm.getScheduleIntervalHours(), new NumberErrorWebResolver("admin.corporateSchedule.label.intervalHours"), true);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        else {
        	TextValidator intervalLengthValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SCHEDULE_INTERVAL_LENGTH);
        	vr = intervalLengthValidator.validate(corporateScheduleForm.getScheduleIntervalHours(), new TextErrorWebResolver("admin.corporateSchedule.label.intervalHours"));
        	if (vr != null) {
        		errors.putErrors(vr.getResult());
        	}
        }

//        DateValidator cutoffTimeValidator = Validators.getDateValidator(Constants.SYSTEM_TIME_PATTERN);
        DateValidator cutoffTimeValidator = Validators.getDateValidator(I18nUtil.getTimePattern());
        vr = cutoffTimeValidator.validate(corporateScheduleForm.getScheduleCutoffTime(), new TimeErrorWebResolver("admin.corporateSchedule.label.cutoffTime", null, I18nUtil.getTimePattern()));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
		String datePattern = AppI18nUtil.getDatePattern();
		AppLocale locale = new AppLocale(Auth.getAppUser().getLocale());
		
		String alsoDates = corporateScheduleForm.getScheduleAlsoDates();		
		if (Utility.isSet(alsoDates)) {
			//SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN);
			SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
			dateFormatter.setLenient(false);
			Calendar calendar = Calendar.getInstance();
			String[] dates = alsoDates.split(",");
			for (String date: dates) {
//				try {
					
/*					ParsePosition position = new ParsePosition(0);
					Date parsedDate = dateFormatter.parse(date.trim(), position);
					if (position.getIndex() != date.trim().length() || Constants.SYSTEM_DATE_PATTERN.length() != date.trim().length()) {
						throw new ParseException("Remainder not parsed: " + date.substring(position.getIndex()), position.getIndex());
					}
				}
				catch (ParseException pe) {
		            vr = new CodeValidationResult(new DateErrorWebResolver("admin.corporateSchedule.label.scheduleDates", null, Constants.SYSTEM_DATE_PATTERN),
		                    new ValidationCodeImpl(ValidationReason.WRONG_DATE_FORMAT,
		                            new StringArgument(date)));
		            errors.putErrors(vr.getResult());
				}
*/
					DateValidator dateValidator = Validators.getDateValidator(datePattern);
		            vr = dateValidator.validate(date.trim(), new DateErrorWebResolver("admin.corporateSchedule.label.scheduleDates", "Schedule Dates"));
		            if (vr != null) {
		                errors.putErrors(vr.getResult());
		            } else {
			            Date parsedDate = dateValidator.getParsedDate();
						
						calendar.setTime(parsedDate);
						int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
						if (Calendar.SATURDAY == dayOfWeek || Calendar.SUNDAY == dayOfWeek) {
		            		errors.add(new WebError("validation.web.error.corporateScheduleWeekendScheduleDateNotAllowed", Args.i18nTyped(date)));
						}
		            }
			}
		}
		
		String originalPhysicalInventoryDates = corporateScheduleForm.getSchedulePhysicalInventoryDates();
		if (Utility.isSet(originalPhysicalInventoryDates)) {
			//make sure all physical inventory date sets are contained within parenthesis
			boolean isFormatIssue = false;
			List<String> physicalInventoryDateList = new ArrayList<String>();
			String physicalInventoryDates = originalPhysicalInventoryDates;
			while (Utility.isSet(physicalInventoryDates) && !isFormatIssue) {
				int parenthesisStart = physicalInventoryDates.indexOf("(");
				int parenthesisEnd = physicalInventoryDates.indexOf(")");
				if (parenthesisStart < 0 || parenthesisEnd < 0) {
					isFormatIssue = true;
				}
				else {
					physicalInventoryDateList.add(physicalInventoryDates.substring(parenthesisStart + 1, parenthesisEnd));
					physicalInventoryDates = physicalInventoryDates.substring(parenthesisEnd + 1);
				}
			}
			DateValidator validator = new DateValidator(AppI18nUtil.getDatePattern());
			if (isFormatIssue) {
				
				String formatMessage = (new MessageI18nArgument(new ArgumentedMessageImpl("admin.corporateSchedule.label.physicalInventoryDatesFormat", Args.i18nTyped(I18nUtil.getDatePatternPrompt())))).resolve();
	            vr = new CodeValidationResult(new DateErrorWebResolver("admin.corporateSchedule.label.physicalInventoryDates", null, formatMessage),
	                    new ValidationCodeImpl(ValidationReason.WRONG_DATE_FORMAT,
	                            new StringArgument(corporateScheduleForm.getSchedulePhysicalInventoryDates())));
	            errors.putErrors(vr.getResult());
			}
			else {
				
				for (String physicalInventoryDate : physicalInventoryDateList){
		      		//logger.info("validate() ====>physicalInventoryDate="+physicalInventoryDate);
		      		List<String> dates = Utility.toList(physicalInventoryDate.split(","));
		      		if (dates.size() != 3){
	    				String formatMessage = (new MessageI18nArgument(new ArgumentedMessageImpl("admin.corporateSchedule.label.physicalInventoryDatesFormat", Args.i18nTyped(I18nUtil.getDatePatternPrompt())))).resolve();
	    	            vr = new CodeValidationResult(new DateErrorWebResolver("admin.corporateSchedule.label.physicalInventoryDates", null, formatMessage),
	    	                    new ValidationCodeImpl(ValidationReason.WRONG_DATE_FORMAT_EXT,
	    	                            new StringArgument(physicalInventoryDate)));
	    	            errors.putErrors(vr.getResult());
		      		} else {
						for (String date : dates){
							vr = validator.validate(date.trim(), new DateErrorWebResolver("admin.corporateSchedule.label.physicalInventoryDates", "Physical Inventory Dates"));
			            	if (vr != null){
			            		//logger.info("validate() ====>date="+date+", vr.getResult()= " + vr.getResult());
			            		
			    				String formatMessage = (new MessageI18nArgument(new ArgumentedMessageImpl("admin.corporateSchedule.label.physicalInventoryDatesFormat", Args.i18nTyped(I18nUtil.getDatePatternPrompt())))).resolve();
			    	            vr = new CodeValidationResult(new DateErrorWebResolver("admin.corporateSchedule.label.physicalInventoryDates", null, formatMessage),
			    	                    new ValidationCodeImpl(ValidationReason.WRONG_DATE_FORMAT,
			    	                            new StringArgument(physicalInventoryDate)));
			    	            errors.putErrors(vr.getResult());
			    	            
			            		break;
			            	}
						}
		      		}
	            }
/*
				for (String physicalInventoryDate : physicalInventoryDateList) {
			        PhysicalInventoryPeriod period = PhysicalInventoryPeriod.parse(physicalInventoryDate);
			        if (period == null) {
						String dateFormat = new MessageI18nArgument("admin.corporateSchedule.label.physicalInventoryDatesFormat").resolve();
			            vr = new CodeValidationResult(new DateErrorWebResolver("admin.corporateSchedule.label.physicalInventoryDates", null, dateFormat),
			                    new ValidationCodeImpl(ValidationReason.WRONG_DATE_FORMAT,
			                            new StringArgument(corporateScheduleForm.getSchedulePhysicalInventoryDates())));
			            errors.putErrors(vr.getResult());
			        }
				}
*/				
			}
		}
        
        return new MessageValidationResult(errors.get());

    }

}
