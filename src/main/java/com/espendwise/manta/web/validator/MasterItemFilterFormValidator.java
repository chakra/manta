package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.web.forms.MasterItemFilterForm;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;

public class MasterItemFilterFormValidator extends AbstractFormValidator {

    public MasterItemFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        MasterItemFilterForm form = (MasterItemFilterForm) obj;

        WebErrors errors = new WebErrors();

        if (Utility.isSet(form.getItemId())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            CodeValidationResult vr = validator.validate(form.getItemId(), new SearchByIdErrorResolver("admin.global.filter.label.itemId", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getItemName())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getItemName(), new TextErrorWebResolver("admin.global.filter.label.itemName", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getItemCategory())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getItemCategory(), new TextErrorWebResolver("admin.global.filter.label.itemCategory", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getManufacturer())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getManufacturer(), new TextErrorWebResolver("admin.global.filter.label.manufacturer", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getDistributor())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getDistributor(), new TextErrorWebResolver("admin.global.filter.label.distributor", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getItemSku())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SKU_NUM_LENGTH);
            CodeValidationResult vr = validator.validate(form.getItemSku(), new TextErrorWebResolver("admin.global.filter.label.itemSku", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getItemProperty())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
            CodeValidationResult vr = validator.validate(form.getItemProperty(), new TextErrorWebResolver("admin.global.filter.label.itemProperty", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            if (!Utility.isSet(form.getItemPropertyCd())) {
                errors.putError("validation.web.error.noItemPropertyCdSelected");
            }
        }



        return new MessageValidationResult(errors.get());

    }
}
