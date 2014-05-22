package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.UserAccountFilterForm;
import com.espendwise.manta.web.forms.UserAccountFilterResultForm;
import com.espendwise.manta.web.resolver.UserWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(UrlPathKey.USER.ACCOUNT)
@SessionAttributes({SessionKey.USER_ACCOUNT_FILTER, SessionKey.USER_ACCOUNT_FILTER_RESULT})
@AutoClean(SessionKey.USER_ACCOUNT_FILTER)
public class UserAccountController extends BaseController {

    private static final Logger logger = Logger.getLogger(UserAccountController.class);

    private UserService userService;
    private AccountService accountService;

    @Autowired
    public UserAccountController(UserService userService,
                                 AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @InitBinder
    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.USER_ACCOUNT_FILTER) UserAccountFilterForm filterForm) {
        return "user/account";
    }
    
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findAccounts(WebRequest request,
                               @ModelAttribute(SessionKey.USER_ACCOUNT_FILTER) UserAccountFilterForm filterForm,
                               @ModelAttribute(SessionKey.USER_ACCOUNT_FILTER_RESULT) UserAccountFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "user/account";
        }

        resultForm.reset();
        
        Long accountId = null;
        if (Utility.isSet(filterForm.getAccountId())) {
            accountId = Long.valueOf(filterForm.getAccountId());
        }
        StoreAccountCriteria criteria = new StoreAccountCriteria();
        criteria.setStoreId(getStoreId());
        criteria.setUserId(filterForm.getUserId());
        criteria.setAccountId(accountId);
        criteria.setActiveOnly(!filterForm.getShowInactive());
        criteria.setFilterType(filterForm.getAccountNameFilterType());
        criteria.setName(filterForm.getAccountName());
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.ACCOUNT);
        
        List<BusEntityData> configured = userService.findUserAccounts(criteria);
        
        criteria = new StoreAccountCriteria();

        criteria.setStoreId(getStoreId());
        criteria.setAccountId(accountId);
        criteria.setActiveOnly(!filterForm.getShowInactive());
        criteria.setFilterType(filterForm.getAccountNameFilterType());
        criteria.setName(filterForm.getAccountName());
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.ACCOUNT);

        List<BusEntityData> accounts = filterForm.getShowConfiguredOnly()
                            ? configured : accountService.findStoreAccountBusDatas(criteria);

        logger.info("findAccounts()=>  accounts: " + accounts.size());
        logger.info("findAccounts()=>  configured: " + configured.size());

        SelectableObjects<BusEntityData> selectableObj = new SelectableObjects<BusEntityData>(
                                        accounts,
                                        configured,
                                        AppComparator.BUS_ENTITY_ID_COMPARATOR);
        resultForm.setAccounts(selectableObj);

        WebSort.sort(resultForm, BusEntityData.SHORT_DESC);

        return "user/account";

    }

    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(WebRequest request, @PathVariable Long userId, 
                         @ModelAttribute(SessionKey.USER_ACCOUNT_FILTER_RESULT) UserAccountFilterResultForm resultForm) throws Exception {

        logger.info("user/account/update()=> BEGIN");
        Long storeId = getStoreId();

        logger.info("user/account/update()=> userId: " + userId);
        logger.info("user/account/update()=> storeId: " + storeId);

        List<BusEntityData> selected = resultForm.getAccounts().getSelected();
        List<BusEntityData> deselected = resultForm.getAccounts().getNewlyDeselected();
        try{
        	userService.configureUserAccounts(userId,
                                          storeId,
                                          selected,
                                          deselected,
                                          resultForm.getRemoveSitesWithAccounts());
        } 
        catch (ValidationException e) {
        	e.printStackTrace();
            return handleValidationException(e, request);
        }

        logger.info("user/account/update()=> END.");
        
        return "user/account";

    }

    @SuccessMessage
    @RequestMapping(value = "/update/all", method = RequestMethod.POST)
    public String updateAll(@PathVariable Long userId,
                            @ModelAttribute(SessionKey.USER_ACCOUNT_FILTER) UserAccountFilterForm filterForm,
                            @ModelAttribute(SessionKey.USER_ACCOUNT_FILTER_RESULT) UserAccountFilterResultForm resultForm) throws Exception {

        logger.info("user/account/update/all()=> BEGIN");
        Long storeId = getStoreId();

        logger.info("user/account/update/all()=> userId: " + userId);
        logger.info("user/account/update/all()=> storeId: " + storeId);
        
        userService.configureAllUserAccounts(userId,
                                             storeId,
                                             filterForm.getAssociateAllAccounts(),
                                             filterForm.getAssociateAllSites());
          
        if (filterForm.getAssociateAllAccounts()) {
            if (resultForm != null && Utility.isSet(resultForm.getResult())) {
                for (SelectableObjects.SelectableObject<BusEntityData> account : resultForm.getAccounts().getSelectableObjects()) {
                    account.setSelected(true);
                }
            }
        }

        logger.info("user/account/update/all()=> END.");
        
        return "user/account";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.USER_ACCOUNT_FILTER_RESULT) UserAccountFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "user/account";
    }
    

    @ModelAttribute(SessionKey.USER_ACCOUNT_FILTER_RESULT)
    public UserAccountFilterResultForm init(HttpSession session) {

        logger.info("init()=> init....");

        UserAccountFilterResultForm form = (UserAccountFilterResultForm) session.getAttribute(SessionKey.USER_ACCOUNT_FILTER_RESULT);

        logger.info("init()=> form: " + form);        

        if (form == null) {
            form = new UserAccountFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("init()=> init.OK!");

        return form;

    }
    
    @ModelAttribute(SessionKey.USER_ACCOUNT_FILTER)
    public UserAccountFilterForm initFilter(HttpSession session, @PathVariable Long userId) {

        logger.info("initFilter()=> init....");

        UserAccountFilterForm form = (UserAccountFilterForm) session.getAttribute(SessionKey.USER_ACCOUNT_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new UserAccountFilterForm(userId);
            form.initialize();
        }

        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }
    
    public String handleValidationException(ValidationException ex, WebRequest request) {
        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new UserWebUpdateExceptionResolver());
        return "user/account";
    }

}
