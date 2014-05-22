package com.espendwise.manta.web.validator;


import java.util.Date;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.LongValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.HistoryFilterForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;

public class HistoryFilterFormValidator extends AbstractFormValidator {

    public HistoryFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

    	HistoryFilterForm form = (HistoryFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getObjectId())) {
            LongValidator validator = Validators.getLongValidator();
            CodeValidationResult vr = validator.validate(form.getObjectId(), new SearchByIdErrorResolver("admin.global.filter.label.objectId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        Date startDate = null;
        Date endDate = null;

        if (Utility.isSetIgnorePattern(form.getStartDate(), AppI18nUtil.getDatePatternPrompt())) {
            DateValidator dateValidator = Validators.getDateValidator(AppI18nUtil.getDatePattern());
            ValidationResult vr = dateValidator.validate(form.getStartDate(), new DateErrorWebResolver("admin.global.filter.label.startDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            else {
            	startDate = dateValidator.getParsedDate();
            }
        }

        if (Utility.isSetIgnorePattern(form.getEndDate(), AppI18nUtil.getDatePatternPrompt())) {
            DateValidator dateValidator = Validators.getDateValidator(AppI18nUtil.getDatePattern());
            ValidationResult vr = dateValidator.validate(form.getEndDate(), new DateErrorWebResolver("admin.global.filter.label.endDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            else {
            	endDate = dateValidator.getParsedDate();
            }
        }
        
        if (Utility.isSet(startDate) && Utility.isSet(endDate)) {
        	if (endDate.before(startDate)) {
                errors.putError("validation.web.error.wrongDateInterval",
                        new MessageI18nArgument("admin.global.filter.label.startDate"),
                        new MessageI18nArgument("admin.global.filter.label.endDate"));
        	}
        }

        return new MessageValidationResult(errors.get());

    }
}
