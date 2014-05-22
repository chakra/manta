package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.service.ShoppingControlService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.criteria.AccountShoppingControlItemViewCriteria;
import com.espendwise.manta.web.forms.AccountShoppingControlFilterForm;
import com.espendwise.manta.web.forms.AccountShoppingControlFilterResultForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.SHOPPING_CONTROL_FILTER)
@SessionAttributes({SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER_RESULT, SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER})
@AutoClean(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER_RESULT)
public class AccountShoppingControlFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(AccountShoppingControlFilterController.class);

    private ShoppingControlService shoppingControlService;

    @Autowired
    public AccountShoppingControlFilterController(ShoppingControlService shoppingControlService) {
        this.shoppingControlService = shoppingControlService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER) AccountShoppingControlFilterForm filterForm) {
        return "account/shoppingControlFilter";
    }

    @RequestMapping(value = "/clear/item", method = RequestMethod.POST)
    public String clearItemFilter(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER) AccountShoppingControlFilterForm form) throws Exception {
        form.setItems(Utility.emptyList(ProductListView.class));
        return redirect(request, UrlPathKey.ACCOUNT.SHOPPING_CONTROL_FILTER);
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findShoppingControls(WebRequest request,
            @ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER) AccountShoppingControlFilterForm filterForm,
            @ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER_RESULT) AccountShoppingControlFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            populateFormReferenceData(resultForm);
            return "account/shoppingControlFilter";
        }

        try {
        	doSearch(filterForm, resultForm);
        }
        catch (IllegalStateException ise) {
        	webErrors.add(new WebError("admin.account.shoppingcontrol.error.unconfiguredAccount"));
        	List<ShoppingControlItemView> shoppingControls = new ArrayList<ShoppingControlItemView>();
        	resultForm.setShoppingControls(shoppingControls);
        }
        return "account/shoppingControlFilter";
    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER_RESULT) AccountShoppingControlFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "account/shoppingControlFilter";
    }

    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(WebRequest request,
    		@ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER) AccountShoppingControlFilterForm filterForm,
            @ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER_RESULT) AccountShoppingControlFilterResultForm resultForm) throws Exception {

        logger.info("/update()=> BEGIN");

        WebErrors webErrors = new WebErrors(request);
        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(resultForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            populateFormReferenceData(resultForm);
            return "account/shoppingControlFilter";
        }
        
        List<ShoppingControlItemView> shoppingControls = resultForm.getShoppingControls();
        shoppingControlService.updateShoppingControls(shoppingControls);

        doSearch(filterForm, resultForm);

        logger.info("/update()=> END.");
        
        return "account/shoppingControlFilter";

    }
    
    private void doSearch(AccountShoppingControlFilterForm filterForm,AccountShoppingControlFilterResultForm resultForm) {
        resultForm.reset();
        populateFormReferenceData(resultForm);
        Long accountId = filterForm.getAccountId();
        Long locationId = filterForm.getLocationId();
        if (locationId == null) {
        	locationId = new Long(0);
        }
    	AccountShoppingControlItemViewCriteria criteria = new AccountShoppingControlItemViewCriteria(accountId, locationId, Constants.FILTER_RESULT_LIMIT.SHOPPING_CONTROLS);
    	criteria.setItems(filterForm.getItems());
    	criteria.setShowUncontrolledItems(filterForm.getShowUncontrolledItems());
    	List<ShoppingControlItemView> shoppingControls = shoppingControlService.findShoppingControlsByCriteria(criteria);
    	resultForm.setShoppingControls(shoppingControls);
        WebSort.sort(resultForm, ShoppingControlItemView.ITEM_ID);
    }

    @ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER_RESULT)
    public AccountShoppingControlFilterResultForm init(HttpSession session) {
    	AccountShoppingControlFilterResultForm shoppingControlFilterResult = (AccountShoppingControlFilterResultForm) session.getAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER_RESULT);
        if (shoppingControlFilterResult == null) {
        	shoppingControlFilterResult = new AccountShoppingControlFilterResultForm();
        }
        return shoppingControlFilterResult;
    }

    @ModelAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER)
    public AccountShoppingControlFilterForm initFilter(HttpSession session) {
    	AccountShoppingControlFilterForm shoppingControlFilter = (AccountShoppingControlFilterForm) session.getAttribute(SessionKey.ACCOUNT_SHOPPING_CONTROL_FILTER);
        if (shoppingControlFilter == null || !shoppingControlFilter.isInitialized()) {
        	shoppingControlFilter = new AccountShoppingControlFilterForm();
        	shoppingControlFilter.initialize();
        }
        return shoppingControlFilter;
    }

    private void populateFormReferenceData(AccountShoppingControlFilterResultForm form) {
        //populate the form with reference information (invoice shopping control action choices)
        List<Pair<String, String>> shoppingControlActions = new ArrayList<Pair<String, String>>();
        shoppingControlActions.add(new Pair<String, String>(new MessageI18nArgument("admin.shoppingControl.action.option.apply").resolve(), RefCodeNames.SHOPPING_CONTROL_ACTION_CD.APPLY));
        shoppingControlActions.add(new Pair<String, String>(new MessageI18nArgument("admin.shoppingControl.action.option.workflow").resolve(), RefCodeNames.SHOPPING_CONTROL_ACTION_CD.WORKFLOW));
        form.setShoppingControlActionChoices(shoppingControlActions);
    }

}
