package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.AccountListViewCriteria;
import com.espendwise.manta.web.forms.AccountFilterForm;
import com.espendwise.manta.web.forms.AccountFilterResultForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.FILTER)
@SessionAttributes({SessionKey.ACCOUNT_FILTER_RESULT, SessionKey.ACCOUNT_FILTER})
@AutoClean(SessionKey.ACCOUNT_FILTER_RESULT)
public class AccountFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(AccountFilterController.class);

    private AccountService accountService;

    @Autowired
    public AccountFilterController(AccountService accountService) {
        this.accountService = accountService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.ACCOUNT_FILTER) AccountFilterForm filterForm) {
        return "account/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findAccounts(WebRequest request,
                               @ModelAttribute(SessionKey.ACCOUNT_FILTER_RESULT) AccountFilterResultForm form,
                               @ModelAttribute(SessionKey.ACCOUNT_FILTER) AccountFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "account/filter";
        }

        form.reset();
        
        AccountListViewCriteria criteria = new AccountListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.ACCOUNT);

        criteria.setAccountName(filterForm.getFilterValue());
        criteria.setAccountNameFilterType(filterForm.getFilterType());
        criteria.setAccountId(filterForm.getFilterId());
        criteria.setDistrRefNumber(filterForm.getDistrRefNumber());
        criteria.setDistrRefNumberFilterType(filterForm.getDistrRefNumberFilterType());
        criteria.setStoreId(getStoreId());
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        List<AccountListView> accounts = accountService.findAccountsByCriteria(criteria);
        
        form.setAccounts(accounts);

        WebSort.sort(form, AccountListView.ACCOUNT_NAME);

        return "account/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.ACCOUNT_FILTER_RESULT) AccountFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "account/filter";
    }

    @ModelAttribute(SessionKey.ACCOUNT_FILTER_RESULT)
    public AccountFilterResultForm init(HttpSession session) {

        AccountFilterResultForm accountFilterResult = (AccountFilterResultForm) session.getAttribute(SessionKey.ACCOUNT_FILTER_RESULT);

        if (accountFilterResult == null) {
            accountFilterResult = new AccountFilterResultForm();
        }

        return accountFilterResult;

    }

    @ModelAttribute(SessionKey.ACCOUNT_FILTER)
    public AccountFilterForm initFilter(HttpSession session) {

        AccountFilterForm accountFilter = (AccountFilterForm) session.getAttribute(SessionKey.ACCOUNT_FILTER);

        if (accountFilter == null || !accountFilter.isInitialized()) {
            accountFilter = new AccountFilterForm();
            accountFilter.initialize();
        }

        return accountFilter;

    }

}


