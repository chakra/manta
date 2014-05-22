package com.espendwise.manta.web.validator;


import org.apache.log4j.Logger;

import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.LongValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.AccountShoppingControlFilterResultForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;

public class AccountShoppingControlFilterResultFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(AccountShoppingControlFilterResultFormValidator.class);

    public AccountShoppingControlFilterResultFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        AccountShoppingControlFilterResultForm form = (AccountShoppingControlFilterResultForm) obj;
        LongValidator longValidator = Validators.getLongValidator();
        TextValidator textValidator = Validators.getTextValidator(99, true);

        for (ShoppingControlItemView shoppingControl : form.getShoppingControls()) {
        	String maxOrderQuantity = shoppingControl.getShoppingControlMaxOrderQty();
            if (Utility.isSet(maxOrderQuantity) && !Constants.CHARS.ASTERISK.equalsIgnoreCase(maxOrderQuantity)) {
                CodeValidationResult vr = longValidator.validate(shoppingControl.getShoppingControlMaxOrderQty(), new NumberErrorWebResolver("admin.account.shoppingcontrol.label.maxOrderQuantity"));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }
            }
            String restrictionDays = shoppingControl.getShoppingControlRestrictionDays();
            if (Utility.isSet(restrictionDays) && !Constants.CHARS.ASTERISK.equalsIgnoreCase(restrictionDays)) {
                CodeValidationResult vr = longValidator.validate(shoppingControl.getShoppingControlRestrictionDays(), new NumberErrorWebResolver("admin.account.shoppingcontrol.label.restrictionDays"));
                if (vr != null) {
                    errors.putErrors(vr.getResult());
                }
            }
            if (Utility.isSet(shoppingControl.getShoppingControlAction())) {
            	String action = shoppingControl.getShoppingControlAction();
            	if (!RefCodeNames.SHOPPING_CONTROL_ACTION_CD.APPLY.equalsIgnoreCase(action) &&
            		!RefCodeNames.SHOPPING_CONTROL_ACTION_CD.WORKFLOW.equalsIgnoreCase(action)) {
            		errors.add(new WebError("validation.web.error.invalidValue", Args.i18nTyped("admin.account.shoppingcontrol.label.action")));
            	}
            }
            //account id is required
            Long accountId = shoppingControl.getShoppingControlAccountId();
            if (!Utility.isSet(accountId) || accountId.longValue() == 0) {
            	CodeValidationResult vr = textValidator.validate("", new TextErrorWebResolver("admin.account.label.accountId"));
                errors.putErrors(vr.getResult());
            }
            //item id is required
            Long itemId = shoppingControl.getItemId();
            if (!Utility.isSet(itemId) || itemId.longValue() == 0) {
            	CodeValidationResult vr = textValidator.validate("", new TextErrorWebResolver("admin.global.filter.label.itemId"));
                errors.putErrors(vr.getResult());
            }
        }

        return new MessageValidationResult(errors.get());

    }


}
