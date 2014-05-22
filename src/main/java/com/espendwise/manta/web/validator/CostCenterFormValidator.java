package com.espendwise.manta.web.validator;


import com.espendwise.manta.spi.IAddress;
import com.espendwise.manta.spi.IContact;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractContactValidationCodeResolver;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.AddressInputForm;
import com.espendwise.manta.web.forms.CostCenterForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;

public class CostCenterFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        CostCenterForm valueObj = (CostCenterForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);

        vr = shortDescValidator.validate(valueObj.getCostCenterName(), new TextErrorWebResolver("admin.costCenter.label.costCenterName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        shortDescValidator.setIsRequired(false);
        vr = shortDescValidator.validate(valueObj.getCostCenterCode(), new TextErrorWebResolver("admin.costCenter.label.costCenterCode"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        shortDescValidator.setIsRequired(true);
        vr = shortDescValidator.validate(valueObj.getCostCenterType(), new TextErrorWebResolver("admin.costCenter.label.type"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getCostCenterTaxType(), new TextErrorWebResolver("admin.costCenter.label.costCenterTaxType"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getStatus(), new TextErrorWebResolver("admin.costCenter.label.status"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        return new MessageValidationResult(errors.get());

    }

}
