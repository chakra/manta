package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.GroupForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class GroupFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        GroupForm valueObj = (GroupForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);

        vr = shortDescValidator.validate(valueObj.getGroupName(), new TextErrorWebResolver("admin.global.filter.label.groupName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        shortDescValidator.setIsRequired(true);
        vr = shortDescValidator.validate(valueObj.getGroupType(), new TextErrorWebResolver("admin.global.filter.label.groupType"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getGroupStatus(), new TextErrorWebResolver("admin.group.label.groupStatus"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        return new MessageValidationResult(errors.get());

    }

}
