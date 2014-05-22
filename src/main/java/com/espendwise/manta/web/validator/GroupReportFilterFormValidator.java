package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.GroupReportFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;


public class GroupReportFilterFormValidator extends AbstractFormValidator {

    public GroupReportFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        GroupReportFilterForm form = (GroupReportFilterForm) obj;

        WebErrors errors = new WebErrors();
        
        if (Utility.isSet(form.getReportId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getReportId(), new SearchByIdErrorResolver("admin.global.filter.label.reportId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getReportName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getReportName(), new TextErrorWebResolver("admin.global.filter.label.reportName", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }
}