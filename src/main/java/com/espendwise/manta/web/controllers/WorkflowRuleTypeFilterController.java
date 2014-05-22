package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.service.WorkflowService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.web.forms.LocateAccountFilterResultForm;
import com.espendwise.manta.web.forms.WorkflowForm;
import com.espendwise.manta.web.forms.WorkflowRuleTypeFilterForm;
import com.espendwise.manta.web.forms.WebForm;
import com.espendwise.manta.web.forms.WorkflowRuleForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
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
@RequestMapping(UrlPathKey.ACCOUNT.WORKFLOW_RULE_TYPE)
//@SessionAttributes({SessionKey.ACCOUNT_WORKFLOW_DETAIL, SessionKey.ACCOUNT_WORKFLOW_RULE_TYPE_FILTER})
//@AutoClean(SessionKey.LOCATE_ACCOUNT_FILTER_RESULT)
public class WorkflowRuleTypeFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(WorkflowRuleTypeFilterController.class);

    private WorkflowService workflowService;

    @Autowired
    public WorkflowRuleTypeFilterController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @ResponseBody
    @RequestMapping(value = "/createRule", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session, Model model,WebRequest request,
    							 @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
    							 @PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
    							 @PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
                                 @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_TYPE_FILTER) WorkflowRuleTypeFilterForm form) throws Exception {

    	logger.info("returnSelected()=> BEGIN, === WorkflowRuleTypeFilterForm === form.hashCode() = "+ form.hashCode() );
    	WorkflowForm detailForm = (WorkflowForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL);
    	WebErrors webErrors = new WebErrors(request);
    	
    	logger.info("returnSelected()=> BEGIN, form.getRuleType() = "+ form.getRuleType() );
        String ruleType = form.getRuleType();
   
        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);
    	logger.info("returnSelected()=>   validationErrors.isEmpty() = "+ validationErrors.isEmpty());
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return null;//"account/workflow/ruleType";
        }

        
        if (detailForm != null ) {
        	detailForm.setWorkflowRuleTypeCdToEdit(ruleType);
        	session.setAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL, new WorkflowRuleForm());
            logger.info("returnSelected()=> detailForm.getWorkflowRuleTypeCdToEdit()=" + detailForm.getWorkflowRuleTypeCdToEdit() + ", detailForm.hashCode()="+ detailForm.hashCode());
        }
        logger.info("returnSelected()=> END.");

        return "";
    }
    

    
    @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_TYPE_FILTER)
    public WorkflowRuleTypeFilterForm initFilter(HttpSession session,
    					@PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType) {
        WorkflowRuleTypeFilterForm locateForm = (WorkflowRuleTypeFilterForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_TYPE_FILTER);
        return initFilter(locateForm, workflowType, false );
    }

    private WorkflowRuleTypeFilterForm initFilter(WorkflowRuleTypeFilterForm locateForm, String workflowType, boolean init) {
        logger.info("initFilter()=> BEGIN. workflowType = " + workflowType);

        if (locateForm == null) {
            locateForm = new WorkflowRuleTypeFilterForm();
        }

        // init at once
        if (init && !locateForm.isInitialized()) {
            locateForm.initialize();
        }

        //gets only when executed from filter method
        if (locateForm.isInitialized()) {
        	locateForm.setWorkflowType(workflowType);
        }
        logger.info("initFilter()=> END.");

        return locateForm;
    }


}


