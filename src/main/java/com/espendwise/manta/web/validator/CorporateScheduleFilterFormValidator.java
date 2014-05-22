package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.util.validation.rules.CorporateScheduleFilterConstraint;
import com.espendwise.manta.web.forms.CorporateScheduleFilterForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;


public class CorporateScheduleFilterFormValidator extends AbstractFormValidator {

    private static final Logger logger = Logger.getLogger(CorporateScheduleFilterFormValidator.class);

    public CorporateScheduleFilterFormValidator() {
        super();
    }

    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();
		
		SimpleFilterFormFieldValidator filterValidator = new SimpleFilterFormFieldValidator(
				"admin.corporateSchedule.label.corporateScheduleName",
				"admin.corporateSchedule.label.corporateScheduleId", "Corporate Schedule Name");
		ValidationResult vr = filterValidator.validate(obj);
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        String datePattern = AppI18nUtil.getDatePattern();

        CorporateScheduleFilterForm valueObj = (CorporateScheduleFilterForm) obj;

        if (Utility.isSetIgnorePattern(valueObj.getCorporateScheduleDateFrom(), AppI18nUtil.getDatePatternPrompt())) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            vr = dateValidator.validate(valueObj.getCorporateScheduleDateFrom(), new DateErrorWebResolver("admin.corporateSchedule.label.dateFrom", "Corporate Schedule 'From' Date"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSetIgnorePattern(valueObj.getCorporateScheduleDateTo(), AppI18nUtil.getDatePatternPrompt())) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            vr = dateValidator.validate(valueObj.getCorporateScheduleDateTo(), new DateErrorWebResolver("admin.corporateSchedule.label.dateTo", "Corporate Schedule 'To' Date"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        if (Utility.isSetIgnorePattern(valueObj.getCorporateScheduleDateFrom(), AppI18nUtil.getDatePatternPrompt()) ||
        	Utility.isSetIgnorePattern(valueObj.getCorporateScheduleDateTo(), AppI18nUtil.getDatePatternPrompt()) ) {
	        if (errors.isEmpty()) {
		        ServiceLayerValidation validation = new ServiceLayerValidation();
		        validation.addRule(new CorporateScheduleFilterConstraint(valueObj.getStoreId(), valueObj.getFilterId(), valueObj.getFilterValue(), valueObj.getFilterType()));
		        validation.validate();
	        }
        }

        if (Utility.isSetIgnorePattern(valueObj.getCorporateScheduleDateFrom(), AppI18nUtil.getDatePatternPrompt()) &&
        	Utility.isSetIgnorePattern(valueObj.getCorporateScheduleDateTo(), AppI18nUtil.getDatePatternPrompt()) ) {
	        if (errors.isEmpty()) {
		        DateValidator dateValidator = Validators.getDateValidator(datePattern);
		        vr = dateValidator.validateDates(valueObj.getCorporateScheduleDateFrom(), valueObj.getCorporateScheduleDateTo(), new DateErrorWebResolver("admin.corporateSchedule.label.dateTo",  ""));
		        if (vr != null) {
                    errors.putErrors(vr.getResult());
                }
            }
        }

        return new MessageValidationResult(errors.get());

    }

}
