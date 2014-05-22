package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.WorkflowService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.AccountFilterResultForm;
import com.espendwise.manta.web.forms.FilterResult;
import com.espendwise.manta.web.forms.WorkflowForm;
import com.espendwise.manta.web.forms.AccountWorkflowForm;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.validator.WorkflowFormValidator;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.TreeMap;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.WORKFLOW)
@SessionAttributes(SessionKey.ACCOUNT_WORKFLOW)
@AutoClean(SessionKey.ACCOUNT_WORKFLOW)
public class AccountWorkflowController extends BaseController {

    private static final Logger logger = Logger.getLogger(AccountWorkflowController.class);

    private WorkflowService workflowService;

    @Autowired
    public AccountWorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
//       webErrors.putErrors(ex, new TextErrorWebResolver(""));

        return "account/workflow";
    }


    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String initShow(HttpServletRequest request, @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW) AccountWorkflowForm form, @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId) {
        logger.info("initShow()=> BEGIN " );
        
//        AccountWorkflowForm form = new AccountWorkflowForm(accountId);

        List<WorkflowData> workflows = workflowService.findWorkflowCollection(accountId);
        WebSort.sort(workflows, WorkflowData.SHORT_DESC, new Boolean(true));
        
        form.setWorkflows(workflows);
 
        logger.info("initShow()=> END " );
        return "account/workflow";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(HttpServletRequest request, @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW) AccountWorkflowForm form, Model model) {
        logger.info("create()=> BEGIN " );

        WebErrors webErrors = new WebErrors(request);

//        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form, new WorkflowFormValidator());
//        if (!validationErrors.isEmpty()) {
//            webErrors.putErrors(validationErrors);
//            return "account/workflow";
//        }
        WorkflowForm formDetail = new WorkflowForm();
        formDetail.setAccountId(form.getAccountId());
        formDetail.initialize();

        model.addAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL, formDetail);

        return "account/workflow/edit";

    }


    @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW)
    public AccountWorkflowForm initModel(@PathVariable(IdPathKey.ACCOUNT_ID) Long accountId) {
        logger.info("initModel()=> BEGIN " );

        AccountWorkflowForm form = new AccountWorkflowForm(accountId);
/*
        List<WorkflowData> workflows = workflowService.findWorkflowCollection(accountId);
        form.setWorkflows(workflows);
        WebSort.sort(form, WorkflowData.SHORT_DESC);
*/        
        logger.info("initModel()=> END " );
        return form;

    }
    @RequestMapping(value = "/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW) AccountWorkflowForm form, @PathVariable String field) throws Exception {
        logger.info("sort()=> BEGIN " );
        WebSort.sort(form, field);
//        WebSort.sort(form, field, form.getSortHistory());
        logger.info("sort()=> END " );
        return "account/workflow";
    }

}
