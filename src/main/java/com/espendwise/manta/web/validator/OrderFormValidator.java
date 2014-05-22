package com.espendwise.manta.web.validator;



import com.espendwise.manta.model.view.OrderItemIdentView;
import org.apache.log4j.Logger;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.AmountEmptyAllowedValidator;
import com.espendwise.manta.util.validation.AmountValidator;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.OrderForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorInLineWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;
import java.util.List;

public class OrderFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(OrderFormValidator.class);

    public OrderFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        OrderForm valueObj = (OrderForm) obj;

        ValidationResult vr;
        DateValidator dateValidator = Validators.getDateValidator(AppI18nUtil.getDatePattern());
        AmountEmptyAllowedValidator amountValidator = Validators.getAmountEmptyAllowedValidator();
        IntegerValidator integerValidator = Validators.getIntegerValidator();
        
        vr = amountValidator.validate(valueObj.getTotalFreightCost(), new NumberErrorWebResolver("admin.order.label.freight"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        vr = amountValidator.validate(valueObj.getTotalMiscCost(), new NumberErrorWebResolver("admin.order.label.handling"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        vr = amountValidator.validate(valueObj.getSmallOrderFee(), new NumberErrorWebResolver("admin.order.label.smallOrderFee"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        vr = amountValidator.validate(valueObj.getFuelSurCharge(), new NumberErrorWebResolver("admin.order.label.fuelSurcharge"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        vr = amountValidator.validate(valueObj.getDiscount(), new NumberErrorWebResolver("admin.order.label.discount"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        vr = amountValidator.validate(valueObj.getTotalTaxCost(), new NumberErrorWebResolver("admin.order.label.tax"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        if (Utility.isSetIgnorePattern(valueObj.getNewOrderDate(), AppI18nUtil.getDatePatternPrompt())) {
            vr = dateValidator.validate(valueObj.getNewOrderDate(), new DateErrorWebResolver("admin.order.label.newOrderDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        List<OrderItemIdentView> orderItems = valueObj.getOrderItems();
        if (Utility.isSet(orderItems)) {
            for (OrderItemIdentView orderItem : orderItems) {
                if (Utility.isSet(orderItem.getNewItemPrice())) {
                    vr = amountValidator.validate(orderItem.getNewItemPrice(),
                                                  new NumberErrorInLineWebResolver(orderItem.getOrderItem().getOrderLineNum().toString(),
                                                                                   "admin.order.label.customerPrice"));
                    if (vr != null) {
                        errors.putErrors(vr.getResult());
                    }
                }
                if (Utility.isSet(orderItem.getItemQuantity())) {
                    vr = integerValidator.validate(orderItem.getItemQuantity(),
                                                   new NumberErrorInLineWebResolver(orderItem.getOrderItem().getOrderLineNum().toString(),
                                                                                   "admin.order.label.qty"));
                    if (vr != null) {
                        errors.putErrors(vr.getResult());
                    }
                }
            }
        }

        return new MessageValidationResult(errors.get());
    }

}
