package com.espendwise.manta.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.ShoppingControlService;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.AccountShoppingControlForm;
import com.espendwise.manta.web.resolver.AccountShoppingControlWebUpdateExceptionResolver;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebErrors;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.SHOPPING_CONTROL_IDENTIFICATION)
public class AccountShoppingControlController extends BaseController {

    private static final Logger logger = Logger.getLogger(AccountShoppingControlController.class);

    private ShoppingControlService shoppingControlService;

    @Autowired
    public AccountShoppingControlController(ShoppingControlService shoppingControlService) {
        this.shoppingControlService = shoppingControlService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {
        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new AccountShoppingControlWebUpdateExceptionResolver());
        return "account/shoppingControl";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {
        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());
        return "account/shoppingControl";
    }

    @ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL)
    public AccountShoppingControlForm initModel() {
        AccountShoppingControlForm form = new AccountShoppingControlForm();
        form.initialize();
        return form;
    }

}
