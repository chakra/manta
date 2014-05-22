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
import com.espendwise.manta.web.forms.ManufacturerForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;

public class ManufacturerFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        ManufacturerForm valueObj = (ManufacturerForm) obj;

        ValidationResult vr;

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);

        vr = shortDescValidator.validate(valueObj.getManufacturerName(), new TextErrorWebResolver("admin.manufacturer.label.manufacturerName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getManufacturerStatus(), new TextErrorWebResolver("admin.manufacturer.label.status"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        return new MessageValidationResult(errors.get());

    }

}
