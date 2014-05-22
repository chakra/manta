package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.CorporateScheduleAccountFilterForm;
import com.espendwise.manta.web.forms.CorporateScheduleAccountFilterResultForm;
import com.espendwise.manta.web.resolver.CorporateScheduleWebExceptionResolver;
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
@RequestMapping(UrlPathKey.CORPORATE_SCHEDULE.ACCOUNT)
@SessionAttributes({SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER, SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT})
@AutoClean(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER)
public class CorporateScheduleAccountController extends BaseController {

    private static final Logger logger = Logger.getLogger(CorporateScheduleAccountController.class);

    private ScheduleService scheduleService;
    private AccountService accountService;

    @Autowired
    public CorporateScheduleAccountController(ScheduleService scheduleService,
                                 AccountService accountService) {
        this.scheduleService = scheduleService;
        this.accountService = accountService;
    }

    @InitBinder
    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    public String handleValidationException(ValidationException ex, WebRequest request) {
        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new CorporateScheduleWebExceptionResolver());
        return "corporateSchedule/account";
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER) CorporateScheduleAccountFilterForm filterForm,
            @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT) CorporateScheduleAccountFilterResultForm resultForm) {
    	filterForm.reset();
    	resultForm.reset();
    	return "corporateSchedule/account";
    }
    
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findAccounts(WebRequest request,
                               @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER) CorporateScheduleAccountFilterForm filterForm,
                               @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT) CorporateScheduleAccountFilterResultForm resultForm) throws Exception {
    	logger.info("corporateSchedule/account/findAccounts()=> BEGIN");
        logger.info("findAccounts()=> scheduleId: " + filterForm.getScheduleId());
        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "corporateSchedule/account";
        }

        doSearch(filterForm, resultForm);
        logger.info("corporateSchedule/account/findAccounts()=> END");
        return "corporateSchedule/account";

    }

    private void doSearch(CorporateScheduleAccountFilterForm filterForm, CorporateScheduleAccountFilterResultForm resultForm) throws Exception {
    	resultForm.reset();
        
        Long accountId = null;
        if (Utility.isSet(filterForm.getAccountId())) {
            accountId = Long.valueOf(filterForm.getAccountId());
        }
        StoreAccountCriteria criteria = new StoreAccountCriteria();
        criteria.setStoreId(getStoreId());
        criteria.setScheduleId(filterForm.getScheduleId());
        criteria.setAccountId(accountId);
        criteria.setActiveOnly(!filterForm.getShowInactive());
        criteria.setFilterType(filterForm.getAccountNameFilterType());
        criteria.setName(filterForm.getAccountName());
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.ACCOUNT);
        
        List<BusEntityData> configured = scheduleService.findScheduleAccountsByCriteria(criteria);
        
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

    }

    	
    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(WebRequest request,@PathVariable(IdPathKey.CORPORATE_SCHEDULE_ID) Long scheduleId, 
    					 @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER) CorporateScheduleAccountFilterForm filterForm,
                         @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT) CorporateScheduleAccountFilterResultForm resultForm) throws Exception {

    	logger.info("corporateSchedule/account/update()=> BEGIN");
        logger.info("corporateSchedule/account/update()=> scheduleId: " + scheduleId);
        Long storeId = getStoreId();

        List<BusEntityData> selected = resultForm.getAccounts().getNewlySelected();
        List<BusEntityData> deselected = resultForm.getAccounts().getNewlyDeselected();
        try {
        	scheduleService.configureScheduleAccounts(scheduleId,
                                          storeId,
                                          selected,
                                          deselected,
                                          resultForm.getRemoveSitesWithAccounts());
        } 
        catch (ValidationException e) {
        	e.printStackTrace();
            return handleValidationException(e, request);
        }
        
        // update the selectable list
        List<BusEntityData> accounts = resultForm.getAccounts().getValues();
        List<BusEntityData> configured = resultForm.getAccounts().getSelected();
        
        SelectableObjects<BusEntityData> selectableObj = new SelectableObjects<BusEntityData>(
                accounts,
                configured,
                AppComparator.BUS_ENTITY_ID_COMPARATOR);
        resultForm.setAccounts(selectableObj);

        //doSearch(filterForm, resultForm);
        logger.info("corporateSchedule/account/update()=> END.");
        logger.info("corporateSchedule/account/update()=> BEGIN");
        return "corporateSchedule/account";

    }

    @SuccessMessage
    @RequestMapping(value = "/update/all", method = RequestMethod.POST)
    public String updateAll(@PathVariable(IdPathKey.CORPORATE_SCHEDULE_ID) Long scheduleId,
                            @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER) CorporateScheduleAccountFilterForm filterForm,
                            @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT) CorporateScheduleAccountFilterResultForm resultForm) throws Exception {

        logger.info("corporateSchedule/account/update/all()=> BEGIN");
        Long storeId = getStoreId();

        logger.info("corporateSchedule/account/update/all()=> scheduleId: " + scheduleId);
        logger.info("corporateSchedule/account/update/all()=> storeId: " + storeId);
        
        scheduleService.configureAllScheduleAccounts(scheduleId,
                                             storeId,
                                             filterForm.getAssociateAllAccounts(),
                                             filterForm.getAssociateAllSites());
          
        if (filterForm.getAssociateAllAccounts()) {
            if (resultForm != null && Utility.isSet(resultForm.getResult())) {
            	// update the selectable list
                List<BusEntityData> accounts = resultForm.getAccounts().getValues();
                
                SelectableObjects<BusEntityData> selectableObj = new SelectableObjects<BusEntityData>(
                        accounts,
                        accounts,
                        AppComparator.BUS_ENTITY_ID_COMPARATOR);
                resultForm.setAccounts(selectableObj);
            }
        }

        //doSearch(filterForm, resultForm);
        logger.info("corporateSchedule/account/update/all()=> END.");
        
        return "corporateSchedule/account";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT) CorporateScheduleAccountFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "corporateSchedule/account";
    }
    

    @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT)
    public CorporateScheduleAccountFilterResultForm init(HttpSession session) {

        logger.info("init()=> init....");

        CorporateScheduleAccountFilterResultForm form = (CorporateScheduleAccountFilterResultForm) session.getAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT);

        logger.info("init()=> form: " + form);        

        if (form == null) {
            form = new CorporateScheduleAccountFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("init()=> init.OK!");

        return form;

    }
    
    @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER)
    public CorporateScheduleAccountFilterForm initFilter(HttpSession session, @PathVariable(IdPathKey.CORPORATE_SCHEDULE_ID) Long scheduleId) {

        logger.info("initFilter()=> init....");

        CorporateScheduleAccountFilterForm form = (CorporateScheduleAccountFilterForm) session.getAttribute(SessionKey.CORPORATE_SCHEDULE_ACCOUNT_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new CorporateScheduleAccountFilterForm(scheduleId);
            form.initialize();
        }

        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }

}
