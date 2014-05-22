package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.OrderItemNoteFilterForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

import org.apache.log4j.Logger;

public class OrderItemNoteFilterFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(OrderItemNoteFilterFormValidator.class);

    public OrderItemNoteFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        OrderItemNoteFilterForm valueObj = (OrderItemNoteFilterForm) obj;

        ValidationResult vr;
        logger.info("OrderItemNoteFilterFormValidator() ====>validate : valueObj.getItemNote() ="+ valueObj.getOrderItemNoteField());  

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        vr = shortDescValidator.validate(valueObj.getOrderItemNoteField(), new TextErrorWebResolver("admin.order.label.note"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

      logger.info("OrderItemNoteFilterFormValidator() ====>validate : errors.size ="+ errors.size());  
        return new MessageValidationResult(errors.get());
    }


}
