package com.espendwise.manta.web.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.BatchOrderLoaderForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.GenericErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;

public class BatchOrderLoaderFormValidator extends AbstractFormValidator {
	private static final Logger logger = Logger.getLogger(BatchOrderLoaderFormValidator.class);
		
    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        BatchOrderLoaderForm form = (BatchOrderLoaderForm) obj;

        ValidationResult vr;
        
        String fileName = form.getUploadedFile() == null ? "" : form.getUploadedFile().getOriginalFilename();        
        
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        shortDescValidator.setIsRequired(true);

        vr = shortDescValidator.validate(fileName, new TextErrorWebResolver("batchOrder.loader.label.selectFile"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }        
        
        String datePattern = I18nUtil.getDatePattern();
        String processOn = AppI18nUtil.getDatePatternPrompt().equals(form.getProcessOn()) ? "" : form.getProcessOn();
        vr = shortDescValidator.validate(processOn, new TextErrorWebResolver("batchOrder.loader.label.processOn"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }else{        	
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            vr = dateValidator.validate(processOn, new DateErrorWebResolver("batchOrder.loader.label.processOn"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }else{
            	// remove time field
            	Date currentTime = new Date();
            	SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
            	Date currentDate = dateValidator.parse(formatter.format(currentTime));
            	
            	dateValidator = Validators.getDateValidator(datePattern, currentDate, null);
            	vr = dateValidator.validate(processOn, new DateErrorWebResolver("batchOrder.loader.label.processOn"));
            	
            	if (vr != null) {
                    errors.putErrors(vr.getResult());
            	}
            	
            	String currentDateStr = formatter.format(currentDate);
            	if (currentDateStr.equals(processOn)){
            		SimpleDateFormat dateFormat = new SimpleDateFormat(I18nUtil.getDatePattern() + " " + I18nUtil.getTimePattern());
            		try {
						Date time = dateFormat.parse(processOn + " 17:00");
						if (currentTime.compareTo(time) >= 0){
							vr = new CodeValidationResult(
					                new GenericErrorWebResolver("validation.web.batchOrder.error.cannotProcessWithTodayDate"),
					                new ValidationCodeImpl(ValidationReason.GENERIC_ERROR));
							errors.putErrors(vr.getResult());
						}
					} catch (ParseException e) {}	
            	}
            }
        }
        
        vr = shortDescValidator.validate(form.getProcessWhen(), new TextErrorWebResolver("batchOrder.loader.label.processWhen"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }    
        
        return new MessageValidationResult(errors.get());

    }
    
	
}