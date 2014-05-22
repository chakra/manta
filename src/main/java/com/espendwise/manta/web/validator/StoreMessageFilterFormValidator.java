package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.StoreMessageFilterForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;
import java.util.Date;
import org.apache.log4j.Logger;

public class StoreMessageFilterFormValidator extends AbstractFormValidator {

    private static final Logger logger = Logger.getLogger(StoreMessageFilterFormValidator.class);

    public StoreMessageFilterFormValidator() {
        super();
    }

    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();
		
		SimpleFilterFormFieldValidator filterValidator = new SimpleFilterFormFieldValidator(
				"admin.message.label.storeMessageName",
				"admin.message.label.storeMessageId", "Message Name");
		ValidationResult vr = filterValidator.validate(obj);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        String datePattern = AppI18nUtil.getDatePattern();

        StoreMessageFilterForm valueObj = (StoreMessageFilterForm) obj;
        Date fromDate = null;
        Date toDate = null;
        if (Utility.isSetIgnorePattern(valueObj.getPostedDateFrom(), AppI18nUtil.getDatePatternPrompt())) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            vr = dateValidator.validate(valueObj.getPostedDateFrom(), new DateErrorWebResolver("admin.message.label.datePostedFrom", "Date Posted From"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            fromDate = dateValidator.getParsedDate();
        }

        if (Utility.isSetIgnorePattern(valueObj.getPostedDateTo(), AppI18nUtil.getDatePatternPrompt())) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            vr = dateValidator.validate(valueObj.getPostedDateTo(), new DateErrorWebResolver("admin.message.label.datePostedTo", "Date Posted To"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            toDate = dateValidator.getParsedDate();
        }
        
        if (fromDate != null && toDate != null) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern, fromDate, toDate);
            vr = dateValidator.checkRanges(new DateErrorWebResolver("admin.message.label.datePostedFrom", "Posting \"From\" Date",
                                                                    "admin.message.label.datePostedTo", "Posting \"To\" Date"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }

}
