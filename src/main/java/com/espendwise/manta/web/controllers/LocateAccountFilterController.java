package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.AccountListViewCriteria;
import com.espendwise.manta.web.forms.LocateAccountFilterForm;
import com.espendwise.manta.web.forms.LocateAccountFilterResultForm;
import com.espendwise.manta.web.forms.WebForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller @Locate
@RequestMapping(UrlPathKey.LOCATE.LOCATE_ACCOUNT)
@SessionAttributes({SessionKey.LOCATE_ACCOUNT_FILTER_RESULT, SessionKey.LOCATE_ACCOUNT_FILTER})
@AutoClean(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT)
public class LocateAccountFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateAccountFilterController.class);

    private AccountService accountService;

    @Autowired
    public LocateAccountFilterController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER) LocateAccountFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT) LocateAccountFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(true);
        return "account/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER) LocateAccountFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT) LocateAccountFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(false);
        return "account/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findAccounts(WebRequest request,
                               @ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT) LocateAccountFilterResultForm form,
                               @ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER) LocateAccountFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "account/locate";
        }

        form.reset();

        AccountListViewCriteria criteria = new AccountListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.ACCOUNT);

        if (Utility.isSet(filterForm.getAccountId())) {
//            criteria.setAccountName(filterForm.getAccountId());
            criteria.setAccountId(filterForm.getAccountId());
//            criteria.setAccountIdFilterType(Constants.FILTER_TYPE.ID);
        }
        if (Utility.isSet(filterForm.getAccountName())) {
            criteria.setAccountName(filterForm.getAccountName());
            criteria.setAccountNameFilterType(filterForm.getAccountNameFilterType());
        }

        criteria.setDistrRefNumber(filterForm.getDistrRefNumber());
        criteria.setDistrRefNumberFilterType(filterForm.getDistrRefNumberFilterType());
        criteria.setStoreId(getStoreId());
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
        criteria.setAccountsGroups(Utility.splitLong(filterForm.getFilteredAccountsGroups()));

        List<AccountListView> accounts = accountService.findAccountsByCriteria(criteria);

        SelectableObjects<AccountListView> selectableObj = new SelectableObjects<AccountListView>(
                accounts,
                new ArrayList<AccountListView>(),
                AppComparator.ACCOUNT_LIST_VIEW_COMPARATOR
        );
        form.setSelectedAccounts(selectableObj);

        WebSort.sort(form, AccountListView.ACCOUNT_NAME);

        return "account/locate";

    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT) LocateAccountFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<AccountListView> selected = filterResultForm.getSelectedAccounts().getSelected();
        if(!filterResultForm.getMultiSelected()){
            selected = Utility.toList(selected.get(0));
        }

        if (Utility.isSet(filter)) {

            List<String> filterKeys = Arrays.asList(Utility.split(filter, "."));

            WebForm targetForm = (WebForm) session.getAttribute(filterKeys.get(0));

            Method method = BeanUtils.findMethod(targetForm.getClass(),
                    Utility.javaBeanPath(filterKeys.subList(1, filterKeys.size()).toArray(new String[filterKeys.size() - 1])),
                    List.class);

            logger.info("returnSelected()=> method:" + method);


            method.invoke(targetForm, selected);
        }

        logger.info("returnSelected()=> END.");

        return new Gson().toJson(selected);
    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT) LocateAccountFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "account/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT)
    public LocateAccountFilterResultForm init(HttpSession session) {

        LocateAccountFilterResultForm locateAccountFilterResult = (LocateAccountFilterResultForm) session.getAttribute(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT);

        if (locateAccountFilterResult == null) {
            locateAccountFilterResult = new LocateAccountFilterResultForm();
        }

        return locateAccountFilterResult;

    }

    @ModelAttribute(SessionKey.LOCATE_ACCOUNT_FILTER)
    public LocateAccountFilterForm initFilter(HttpSession session) {
        LocateAccountFilterForm locateAccountFilter = (LocateAccountFilterForm) session.getAttribute(SessionKey.LOCATE_ACCOUNT_FILTER);
        return initFilter(locateAccountFilter, false);
    }

    private LocateAccountFilterForm initFilter(LocateAccountFilterForm locateAccountFilter, boolean init) {

        if (locateAccountFilter == null) {
            locateAccountFilter = new LocateAccountFilterForm();
        }

        // init at once
        if (init && !locateAccountFilter.isInitialized()) {
            locateAccountFilter.initialize();
        }

        //gets only when ececuted from filter method
        if (locateAccountFilter.isInitialized()) {
            List<GroupData> groups = accountService.findAccountGroups(getStoreId());
            WebSort.sort(groups, GroupData.SHORT_DESC);
            locateAccountFilter.setAccountsGroups(Utility.toPairs(groups));
        }

        return locateAccountFilter;
    }


}


