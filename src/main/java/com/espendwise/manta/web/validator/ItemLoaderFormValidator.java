package com.espendwise.manta.web.validator;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.forms.ItemLoaderForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;

public class ItemLoaderFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(ItemLoaderFormValidator.class);


    public ItemLoaderFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        ItemLoaderForm form = (ItemLoaderForm) obj;

        ValidationResult vr;
        TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        TextValidator skuValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SKU_NUM_LENGTH);

        if (Utility.isSet(form.getCategory())) {
            vr = validator.validate(form.getCategory(), new TextErrorWebResolver("admin.global.filter.label.category", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getSkuName())) {
            vr = validator.validate(form.getSkuName(), new TextErrorWebResolver("admin.global.filter.label.skuName", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getManufName())) {
            vr = validator.validate(form.getManufName(), new TextErrorWebResolver("admin.global.filter.label.manufacturer", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getDistName())) {
            vr = validator.validate(form.getDistName(), new TextErrorWebResolver("admin.global.filter.label.distributor", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(form.getSkuNumber())) {
            vr = skuValidator.validate(form.getSkuNumber(), new TextErrorWebResolver("admin.global.filter.label.skuNumber", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());
    }




}
