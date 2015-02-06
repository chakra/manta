package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
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

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.StoreMessageService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.SelectableObjects.SelectableObject;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.criteria.StoreMsgAccountConfigCriteria;
import com.espendwise.manta.web.forms.SimpleConfigurationFilterForm;
import com.espendwise.manta.web.forms.StoreMsgAccountconfigForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.STORE_MESSAGE.CONFIGURATION)
@SessionAttributes({SessionKey.STORE_MESSAGE_CONFIGURATION, SessionKey.STORE_MESSAGE_CONFIGURATION_FILTER})
@AutoClean(SessionKey.STORE_MESSAGE_CONFIGURATION)
public class StoreMessageConfigurationController extends BaseController {

    private static final Logger logger = Logger.getLogger(StoreMessageConfigurationController.class);

    private StoreMessageService storeMessageService;
    private AccountService accountService;

    @Autowired
    public StoreMessageConfigurationController(StoreMessageService storeMessageService, AccountService accountService) {
        this.storeMessageService = storeMessageService;
        this.accountService = accountService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /*@RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION_FILTER) SimpleConfigurationFilterForm filterForm) {
        return "storeMessage/configuration";
    }*/
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(WebRequest request,
            @PathVariable Long storeMessageId,
            @ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION) StoreMsgAccountconfigForm form,
            @ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION_FILTER) SimpleConfigurationFilterForm filterForm) throws Exception {
        if(storeMessageId > 0){
            StoreMessageData message = storeMessageService.findStoreMessage(getStoreId(), storeMessageId);
            if (RefCodeNames.MESSAGE_MANAGED_BY.CUSTOMER.equals(message.getMessageManagedBy())){
                return findAccounts(request, storeMessageId, form, filterForm);
            }
        }
        return "storeMessage/configuration";
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String findAccounts(WebRequest request,
                           @PathVariable Long storeMessageId,
                           @ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION) StoreMsgAccountconfigForm form,
                           @ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION_FILTER) SimpleConfigurationFilterForm filterForm) throws Exception {

        logger.info("findAccounts()=> BEGIN");

        WebErrors webErrors = new WebErrors(request);
        if (!webErrors.isEmpty()) {
            return "storeMessage/configuration";
        }

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "storeMessage/configuration";
        }

        form.reset();

        Long storeId = getStoreId();
        Long userId = getUserId();

        StoreMsgAccountConfigCriteria criteria = new StoreMsgAccountConfigCriteria(storeId, storeMessageId);
        StoreMessageData message = storeMessageService.findStoreMessage(getStoreId(), storeMessageId);
        boolean messageManagedByCustomer = RefCodeNames.MESSAGE_MANAGED_BY.CUSTOMER.equals(message.getMessageManagedBy());
        
        form.setMessageManagedByCustomer(messageManagedByCustomer);
        logger.info("findAccounts()=>  MessageManagedBy: " + message.getMessageManagedBy());
        if (messageManagedByCustomer){
            List<BusEntityData> configured = storeMessageService.findConfiguratedAccounts(criteria);
            SelectableObjects<BusEntityData> selableobj = new SelectableObjects<BusEntityData>(
                    configured,
                    configured,
                    AppComparator.BUS_ENTITY_ID_COMPARATOR
            );

            form.setAccounts(selableobj);
            logger.info("findAccounts()=> END.");
            return "storeMessage/configuration";
        } 

        criteria.setActiveOnly(!filterForm.getShowInactive());
        criteria.setFilterType(filterForm.getFilterType());
        criteria.setName(filterForm.getFilterValue());
        if (filterForm.getShowConfiguredOnly()) {
            criteria.setLimit(Constants.FILTER_RESULT_LIMIT.STORE_MESSAGE_ACCOUNT);
        }

        logger.info("findAccounts()=> find store message accounts by criteria: " + criteria);
        logger.info("findAccounts()=> showConfiguredOnly: " + filterForm.getShowConfiguredOnly());

        List<BusEntityData> configured = storeMessageService.findConfiguratedAccounts(criteria);

        StoreAccountCriteria accountCriteria = new StoreAccountCriteria();
        accountCriteria.setStoreId(storeId);
        accountCriteria.setUserId(userId);
        accountCriteria.setActiveOnly(!filterForm.getShowInactive());
        accountCriteria.setFilterType(filterForm.getFilterType());
        accountCriteria.setName(filterForm.getFilterValue());
        accountCriteria.setLimit(Constants.FILTER_RESULT_LIMIT.STORE_MESSAGE_ACCOUNT);

        List<BusEntityData> accounts = filterForm.getShowConfiguredOnly()
                ? new ArrayList<BusEntityData>(configured)
                : accountService.findStoreAccountBusDatas(accountCriteria);

        logger.info("findAccounts()=>  accounts: " + accounts.size());
        logger.info("findAccounts()=>  configured: " + configured.size());

        SelectableObjects<BusEntityData> selableobj = new SelectableObjects<BusEntityData>(
                accounts,
                configured,
                AppComparator.BUS_ENTITY_ID_COMPARATOR
        );

        form.setAccounts(selableobj);
        WebSort.sort(form, BusEntityData.SHORT_DESC);
        logger.info("findAccounts()=> END.");
        return "storeMessage/configuration";

    }



    @SuccessMessage
    @RequestMapping(value = "/accounts/update", method = RequestMethod.POST)
    public String update(@PathVariable Long storeMessageId, @ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION) StoreMsgAccountconfigForm form) throws Exception {

        logger.info("accounts/update()=> BEGIN");

        Long storeId = getStoreId();

        UpdateRequest<Long> updateRequest = new UpdateRequest<Long>();
        
        List<Long> selectedIds = Utility.toIds(form.getAccounts().getNewlySelected());
        List<Long> deselectedIds = Utility.toIds(form.getAccounts().getNewlyDeselected());

        updateRequest.setToCreate(selectedIds);
        updateRequest.setToDelete(deselectedIds);

        logger.info("accounts/update()=> storeMessageId: " + storeMessageId);
        logger.info("accounts/update()=> storeId: " + storeId);

        logger.debug("accounts/update()=> updateRequest: " + updateRequest);

        storeMessageService.configureAccounts(storeId, storeMessageId, updateRequest);
        
        //MANTA-706 - update the form to reflect the creations/deletions
        Iterator<SelectableObject<BusEntityData>> accountIterator = form.getAccounts().getIterator();
        while (accountIterator.hasNext()) {
        	SelectableObject<BusEntityData> selectableObject = accountIterator.next();
        	Long accountId = selectableObject.getValue().getBusEntityId();
        	if (selectedIds.contains(accountId)) {
        		selectableObject.setOriginallySelected(true);
        		selectableObject.setSelected(true);
        	}
        	if (deselectedIds.contains(accountId)) {
        		selectableObject.setOriginallySelected(false);
        		selectableObject.setSelected(false);
        	}
        }
        

        logger.info("accounts/update()=> END.");

        return "storeMessage/configuration";

    }

    @SuccessMessage
    @RequestMapping(value = "/accounts/update/all", method = RequestMethod.POST)
    public String updateAll(@PathVariable Long storeMessageId) throws Exception {

        logger.info("accounts/updateAll()=> BEGIN");


        Long storeId = getStoreId();
        storeMessageService.configureAllAccounts(storeId, storeMessageId, false);

        logger.info("accounts/updateAll()=> END.");

        return "storeMessage/configuration";

    }

    @ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION_FILTER)
    public SimpleConfigurationFilterForm initFilter(HttpSession session) {

        logger.info("initFilter()=> init....");

        SimpleConfigurationFilterForm form = (SimpleConfigurationFilterForm) session.getAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new SimpleConfigurationFilterForm() {
                public String getFilterKey() {
                    return "admin.message.configuration.label.accounts";
                }
            };
            form.initialize();
        }

        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }

    @ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION)
    public StoreMsgAccountconfigForm init(HttpSession session, @PathVariable Long storeMessageId) {

        logger.info("init()=> init....");

        StoreMsgAccountconfigForm form = (StoreMsgAccountconfigForm) session.getAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION);

        logger.info("init()=> form: " + form);
        logger.info("init()=> storeMessageId: " + storeMessageId);

        if (form == null) {
            form = new StoreMsgAccountconfigForm(storeMessageId);
        }

        if (Utility.isSet(form.getAccounts())) {
            form.getAccounts().resetState();
        }

        logger.info("init()=> init.OK!");

        return form;

    }

    @RequestMapping(value = "/accounts/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.STORE_MESSAGE_CONFIGURATION) StoreMsgAccountconfigForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "storeMessage/configuration";
    }
}
