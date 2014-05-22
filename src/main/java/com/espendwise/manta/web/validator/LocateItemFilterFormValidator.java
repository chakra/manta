package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.LocateItemFilterForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class LocateItemFilterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        LocateItemFilterForm form = (LocateItemFilterForm) obj;

        WebErrors errors = new WebErrors();



        if (Utility.isSet(form.getItemName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getItemName(), new TextErrorWebResolver("admin.global.filter.label.itemName"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        if (Utility.isSet(form.getItemSku())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getItemSku(), new TextErrorWebResolver("admin.global.filter.label.itemSku"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        if (Utility.isSet(form.getItemCategory())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getItemCategory(), new TextErrorWebResolver("admin.global.filter.label.itemCategory"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        if (Utility.isSet(form.getItemId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getItemId(), new SearchByIdErrorResolver("admin.global.filter.label.itemId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }


        return new MessageValidationResult(errors.get());

    }


}
