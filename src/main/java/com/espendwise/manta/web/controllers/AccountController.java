package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.FiscalCalenderData;
import com.espendwise.manta.model.view.AccountIdentView;
import com.espendwise.manta.model.view.FiscalCalendarIdentView;
import com.espendwise.manta.model.view.FiscalCalendarListView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.AccountForm;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.resolver.AccountWebUpdateExceptionResolver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping(UrlPathKey.ACCOUNT.IDENTIFICATION)
public class AccountController extends BaseController {

    private static final Logger logger = Logger.getLogger(AccountController.class);

    private AccountService accountService;
    private Long accountClonedID;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new AccountWebUpdateExceptionResolver());

        return "account/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.ACCOUNT_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT) AccountForm accountForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, accountForm: "+accountForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(accountForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.ACCOUNT, accountForm);
            return "account/edit";
        }


        AccountIdentView accountView = new AccountIdentView();

        if (!accountForm.getIsNew()) {
            accountView = accountService.findAccountToEdit(getStoreId(),
                    getUserId(),
                    accountForm.getAccountId()
            );
        }                      

        accountView.setBusEntityData(WebFormUtil.createBusEntityData(accountView.getBusEntityData(), accountForm));
        accountView.setProperties(WebFormUtil.createAccountIdentProperties(accountView.getProperties(), accountForm));
        accountView.setAccountContact(WebFormUtil.createAccountContact(accountView.getAccountContact(), accountForm));                       

        try {

            accountView = accountService.saveAccountIdent(getStoreId(),
                    getUserId(),
                    accountView
            );
            

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }
        
        if (this.accountClonedID != null) {
        	logger.info("this.accountClonedID != null");
        	
        	List<FiscalCalendarListView> CalendarList= accountService.findFiscalCalendars(this.accountClonedID);
        	
        	logger.info("CalendarList.size() = "+String.valueOf(CalendarList.size()));
        	
            for (int i = 0; i < CalendarList.size(); i++) {
            	
            	FiscalCalendarIdentView CalendarIdentView = accountService.findCalendarIdent(this.accountClonedID, CalendarList.get(i).getFiscalCalendarId());
            	
            	FiscalCalenderData CalenderData = CalendarIdentView.getFiscalCalendarData();
            	CalenderData.setBusEntityId(accountView.getBusEntityData().getBusEntityId());
            	CalenderData.setFiscalCalenderId(null);
            	CalendarIdentView.setFiscalCalendarData(CalenderData);
            	
            	accountService.saveFiscalCalendarClone(CalendarIdentView);     
            	//accountService.saveFiscalCalendar(CalendarIdentView);
            	
            }
            this.accountClonedID = null;
        } else {
        	this.accountClonedID = null;
        	logger.info("this.accountClonedID == null");
        }         

       
        
        logger.info("redirect(()=> redirect to: " + accountView.getBusEntityData().getBusEntityId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(accountView.getBusEntityData().getBusEntityId()));
    }



    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    public String clone(@ModelAttribute(SessionKey.ACCOUNT) AccountForm form, Model model) throws Exception {

        logger.info("clone()=> BEGIN");

        String title = AppI18nUtil.getMessage("admin.global.text.cloned", new String[]{form.getAccountName()});

        //form.setCloneAccountID(Long.valueOf(form.getAccountId())); 
        this.accountClonedID = form.getAccountId();
        logger.info("this.accountClonedID="+String.valueOf(this.accountClonedID));
        logger.info("form.getAccountId()="+String.valueOf(form.getAccountId()));
        //logger.info("form.getCloneAccountID()="+String.valueOf(form.getCloneAccountID()));
        form.setAccountId(null);
        form.setAccountName(title);

        model.addAttribute(SessionKey.ACCOUNT, form);

        logger.info("clone()=> END.");

        return "account/edit";

    }

    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.ACCOUNT) AccountForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new AccountForm();
        form.initialize();
       // form.setCloneAccountID(null);

        model.addAttribute(SessionKey.ACCOUNT, form);

        logger.info("create()=> END.");

        return "account/edit";

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(HttpServletRequest request, @ModelAttribute(SessionKey.ACCOUNT) AccountForm form, @PathVariable("accountId") Long accountId, Model model) {

        logger.info("show()=> BEGIN");
        
        //MANTA-778 - don't include the user id in the criteria if the user is a system admin, since they do not have
        //associations with a store.
        AccountIdentView account = null;
        if (getAppUser().isSystemAdmin()) {
        	account =  accountService.findAccountToEdit(getStoreId(), null, accountId);
        }
        else {
        	account = accountService.findAccountToEdit(getStoreId(), getUserId(), accountId);
        }

        if (account != null) {

            form.setAccountId(account.getBusEntityData().getBusEntityId());
            form.setAccountName(account.getBusEntityData().getShortDesc());
            form.setAccountStatus(account.getBusEntityData().getBusEntityStatusCd());
            form.setTimeZone(account.getBusEntityData().getTimeZoneCd());
            form.setAccountType(PropertyUtil.toValueNN(account.getProperties().getAccountType()));
            form.setAccountBudgetType(PropertyUtil.toValueNN(account.getProperties().getBudgetType()));
            form.setDistributorReferenceNumber(PropertyUtil.toValueNN(account.getProperties().getDistributorReferenceNumber()));
            form.setBillingAddress(WebFormUtil.createAddressForm(account.getAccountContact().getBillingAddress()));
            form.setPrimaryContact(WebFormUtil.createContactForm(account.getAccountContact().getPrimaryContact()));

        }

        model.addAttribute(SessionKey.ACCOUNT, form);

        logger.info("show()=> END.");

        return "account/edit";

    }


    @ModelAttribute(SessionKey.ACCOUNT)
    public AccountForm initModel() {

        AccountForm form = new AccountForm();
        form.initialize();

        return form;

    }


}
