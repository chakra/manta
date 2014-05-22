package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.LocateAssetFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class LocateAssetFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        LocateAssetFilterForm form = (LocateAssetFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getAssetName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getAssetName(), new TextErrorWebResolver("admin.global.filter.label.assetName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        if (Utility.isSet(form.getAssetId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getAssetId(), new SearchByIdErrorResolver("admin.global.filter.label.assetId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        return new MessageValidationResult(errors.get());

    }


}
