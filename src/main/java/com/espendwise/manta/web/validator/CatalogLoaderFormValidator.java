package com.espendwise.manta.web.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import org.apache.log4j.Logger;

import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.CatalogLoaderForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.GenericErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;
import org.springframework.web.multipart.MultipartFile;

public class CatalogLoaderFormValidator extends AbstractFormValidator {
	private static final Logger logger = Logger.getLogger(CatalogLoaderFormValidator.class);
		
    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        CatalogLoaderForm form = (CatalogLoaderForm) obj;

        ValidationResult vr;
        
        String fileName = form.getUploadedFile() == null ? "" : form.getUploadedFile().getOriginalFilename();        
        
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH, true);
        shortDescValidator.setIsRequired(true);

        vr = shortDescValidator.validate(fileName, new TextErrorWebResolver("catalogManager.loader.label.selectFile"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        } 
        if (Utility.isSet(fileName)){
        	// validation Extention
        	int idx = fileName.lastIndexOf(".");
        	String fileExt = (idx < 0) ? "" : fileName.substring(idx).toLowerCase();
        	Set<String> supportedExts = new HashSet<String>();
        	supportedExts.add(".xls");
        	//supportedExts.add(".xlsx");
        	
            //TextValidator extValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH, true, supportedExts);
            //vr = extValidator.validate(fileExt, new TextErrorWebResolver("validation.web.catalogManager.error.incorrectFileExtention"));
            if (!supportedExts.contains(fileExt)) {
				vr = new CodeValidationResult(
		                new GenericErrorWebResolver("validation.web.catalogManager.error.incorrectFileExtention"),
		                new ValidationCodeImpl(ValidationReason.GENERIC_ERROR));
                errors.putErrors(vr.getResult());
            }
            // validation file name mask
            String NAME_MASK = "catalog";
                         
			if (!fileName.toLowerCase().contains(NAME_MASK)){
				vr = new CodeValidationResult(
		                new GenericErrorWebResolver("validation.web.catalogManager.error.incorrectFileName"),
		                new ValidationCodeImpl(ValidationReason.GENERIC_ERROR));
				errors.putErrors(vr.getResult());
			}

        }
        	
        String datePattern = I18nUtil.getDatePattern();
        String effectiveDate = AppI18nUtil.getDatePatternPrompt().equals(form.getEffectiveDate()) ? "" : form.getEffectiveDate();
        vr = shortDescValidator.validate(effectiveDate, new TextErrorWebResolver("catalogManager.loader.label.effectiveDate"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }else{        	
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            vr = dateValidator.validate(effectiveDate, new DateErrorWebResolver("catalogManager.loader.label.effectiveDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }else{
            	// remove time field
            	Date currentTime = new Date();
            	SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
            	Date currentDate = dateValidator.parse(formatter.format(currentTime));
            	
            	dateValidator = Validators.getDateValidator(datePattern, currentDate, null);
            	vr = dateValidator.validate(effectiveDate, new DateErrorWebResolver("catalogManager.loader.label.effectiveDate"));
            	
            	if (vr != null) {
                    errors.putErrors(vr.getResult());
            	}
            	
            	if (!form.isProcessNow()){
	            	String currentDateStr = formatter.format(currentDate);
	            	if (currentDateStr.equals(effectiveDate)){
	            		SimpleDateFormat dateFormat = new SimpleDateFormat(I18nUtil.getDatePattern() + " " + I18nUtil.getTimePattern());
	            		try {
							Date time = dateFormat.parse(effectiveDate + " 00:00");
							if (currentTime.compareTo(time) >= 0){
								vr = new CodeValidationResult(
						                new GenericErrorWebResolver("validation.web.catalogManager.error.cannotProcessWithTodayDate"),
						                new ValidationCodeImpl(ValidationReason.GENERIC_ERROR));
								errors.putErrors(vr.getResult());
							}
						} catch (ParseException e) {}	
	            	}
	            }
            }
        }
        
        vr = shortDescValidator.validate(form.getTimeZone(), new TextErrorWebResolver("catalogManager.loader.label.timeZone"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }    
 
        return new MessageValidationResult(errors.get());

    }
    
	
}