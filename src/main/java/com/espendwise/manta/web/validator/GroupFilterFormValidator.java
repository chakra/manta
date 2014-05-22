package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.GroupFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class GroupFilterFormValidator extends AbstractFormValidator {

    public GroupFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        GroupFilterForm form = (GroupFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getGroupId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getGroupId(), new SearchByIdErrorResolver("admin.global.filter.label.groupId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getGroupName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getGroupName(), new TextErrorWebResolver("admin.global.filter.label.groupName", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }
}
