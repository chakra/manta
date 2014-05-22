package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.forms.OrderGuideForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.model.view.OrderGuideItemView;

import com.espendwise.manta.web.resolver.NumberErrorElementWebResolver;
import java.util.List;

public class OrderGuideFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        OrderGuideForm valueObj = (OrderGuideForm) obj;

        ValidationResult vr;

        TextValidator textValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);

        vr = textValidator.validate(valueObj.getOrderGuideName(), new TextErrorWebResolver("admin.site.orderGuideEdit.label.orderGuideName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = textValidator.validate(valueObj.getOrderGuideTypeCd(), new TextErrorWebResolver("admin.site.orderGuideEdit.label.orderGuideType"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        if (valueObj.getItems() != null) {
            LongValidator longValidator = Validators.getLongValidator();
            List<OrderGuideItemView> items = valueObj.isNew() ? valueObj.getItems().getSelected() : valueObj.getItems().getValues();
            for (OrderGuideItemView item : items) {
                vr = longValidator.validate(item.getQuantityStr(), new NumberErrorElementWebResolver(item.getProductName(), "admin.site.orderGuide.item.label.quantity"));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }
            }
        }

        return new MessageValidationResult(errors.get());

    }
}
