package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.ServiceTypeService;
import com.espendwise.manta.service.WorkflowService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.WorkflowForm;
import com.espendwise.manta.web.forms.WorkflowRuleFilterResultForm;
import com.espendwise.manta.web.forms.WorkflowRuleTypeFilterForm;
import com.espendwise.manta.web.forms.WorkflowSiteAssocFilterForm;
import com.espendwise.manta.web.forms.WorkflowSiteAssocFilterResultForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import com.espendwise.manta.web.util.WorkflowUtil;
import com.espendwise.manta.web.validator.WorkflowFormValidator;
import com.espendwise.manta.web.validator.WorkflowWebUpdateExceptionResolver;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.WORKFLOW_DETAIL)
@SessionAttributes({SessionKey.ACCOUNT_WORKFLOW_RULE_FILTER_RESULT, SessionKey.ACCOUNT_WORKFLOW_DETAIL})
public class WorkflowController extends BaseController {

    private static final Logger logger = Logger.getLogger(WorkflowController.class);

    private WorkflowService workflowService;
    private ServiceTypeService serviceTypeService;

    @Autowired
    public WorkflowController(WorkflowService workflowService,
    						  ServiceTypeService serviceTypeService) {
        this.workflowService = workflowService;
        this.serviceTypeService = serviceTypeService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new WorkflowWebUpdateExceptionResolver());

        return "account/workflow/edit";
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(WebRequest webRequest,
                       @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL) WorkflowForm form,
                       @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_FILTER_RESULT) WorkflowRuleFilterResultForm ruleForm,
                       @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                       @PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
                       @PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
                       Model model) {

        logger.info("show()=> BEGIN" +
                ", accountId: " + accountId +
                ", workflowId: " + workflowId +
                ", workflowType: " + workflowType );

        logger.info("show()=> BEGIN");
        WorkflowData workflow = workflowService.findWorkflow(workflowId, workflowType);

        //------------search Groups---------------------------
        List<GroupData> groups = WorkflowUtil.getGroups(getAppUser(), getStoreId());
        form.setUserGroups(groups);
        form.setUserGroupMap(WorkflowUtil.toMap(groups));
        //------------search Apply/Skip Associations---------------------------
        List<WorkflowAssocData> associations = workflowService.findWorkflowAssocCollection(workflowId, null, workflowType);
        form.setApplySkipGroupMap(WorkflowUtil.toMap(associations, form.getUserGroupMap()));
 
        if (workflow != null) {
        	form.setAccountId(accountId);
            form.setWorkflowId(workflow.getWorkflowId());
            form.setWorkflowName(workflow.getShortDesc());
            form.setWorkflowTypeCd(workflow.getWorkflowTypeCd());
            form.setWorkflowStatus(workflow.getWorkflowStatusCd());

        }

        showRules(form.getWorkflowId(),workflowType,  ruleForm);

        model.addAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL, form);
        model.addAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_FILTER_RESULT, ruleForm);
        logger.info("show()=> detailForm.hashCode() = " + form.hashCode());
        
//        Object header = webRequest.getAttribute(SessionKey.WORKFLOW_HEADER, WebRequest.SCOPE_SESSION);
//        if (header == null) {
//            header = new EntityHeaderView(workflow.getWorkflowId(), workflow.getShortDesc());
//            webRequest.setAttribute(SessionKey.WORKFLOW_HEADER, header, WebRequest.SCOPE_SESSION);
//        }
        EntityHeaderView header = new EntityHeaderView(workflow.getWorkflowId(), workflow.getShortDesc());
        webRequest.setAttribute(SessionKey.WORKFLOW_HEADER, header, WebRequest.SCOPE_SESSION);
     
        logger.info("show()=> END.");

        return "account/workflow/edit";


    }
    private void showRules(Long workflowId, String workflowType, WorkflowRuleFilterResultForm ruleForm ){
        logger.info("show()==>showRules()=> SET Rules. BEGIN." );
        if (workflowId > 0){
        	List<WorkflowRuleData> rules = workflowService.findWorkflowRules(workflowId, workflowType);
            
            if (!Utility.isSet(rules)) {
        		logger.info("show()==>showRules()=> END:  NO Rules. " );
        		ruleForm.setRules(null);
        		return;
        	}
    
            Map<Long, List<WorkflowRuleData>> ruleRecMap = createRuleRecordMap(rules);
        	List<WorkflowRuleData> rulesList = new ArrayList<WorkflowRuleData>();
        	
        	Iterator it = ruleRecMap.keySet().iterator();
        	while (it.hasNext()) {
        		List<WorkflowRuleData> ruleRecords = (List<WorkflowRuleData>)ruleRecMap.get(it.next());
        		WorkflowRuleData ruleToShow = (WorkflowRuleData)ruleRecords.get(0);
				try {
	                WorkflowUtil.decodeRuleExpValue(ruleRecords, ruleToShow);
				} catch (Exception e) {
					e.printStackTrace();
				}
                rulesList.add(ruleToShow);
        	}

        	List<WorkflowRuleData> configured = new ArrayList<WorkflowRuleData>();	
            SelectableObjects<WorkflowRuleData> selectableObj = new SelectableObjects<WorkflowRuleData>(
                    rulesList,
                    configured,
                    AppComparator.WORKFLOW_RULE_DATA_COMPARATOR);
           
        	ruleForm.setRules(selectableObj);
            logger.info("show()==>showRules()=> Rules : " + rulesList.size());
        }
        WebSort.sort(ruleForm, WorkflowRuleData.RULE_SEQ);

        logger.info("show()==>showRules()=> SET Rules. END." );

    }

    @SuccessMessage
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(WebRequest request,
                       @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL) WorkflowForm form,
                       @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_FILTER_RESULT) WorkflowRuleFilterResultForm ruleForm,
                       @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                       Model model) {

        logger.info("save()=> BEGIN" +
                ", accountId: " + accountId );
               // ", workflowId: " + workflowId +
               // ", workflowType: " + workflowType +
                

        logger.info("save()=> form.getWorkflowTypeCd() = " + form.getWorkflowTypeCd());
        		
        WebErrors webErrors = new WebErrors(request);

       	try {
	        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form, new WorkflowFormValidator());
	        if (!validationErrors.isEmpty()) {
	            webErrors.putErrors(validationErrors);
	            model.addAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL, form);
	            logger.info("save()=> ERRORS: " + webErrors);
	            return "account/workflow/edit";
	        }
        } catch (ValidationException e) {
            return handleValidationException(e, request);
        }    
       
        WorkflowData workflow = new WorkflowData();
        if (!form.isNew()) {
        	workflow = workflowService.findWorkflow( form.getWorkflowId(), form.getWorkflowTypeCd());
        }
        workflow.setBusEntityId(accountId);
        workflow.setShortDesc(form.getWorkflowName());
        workflow.setWorkflowStatusCd(form.getWorkflowStatus());
        workflow.setWorkflowTypeCd(form.getWorkflowTypeCd());

        try {
            workflow = workflowService.updateWorkflow(workflow);
            
        } catch (ValidationException e) {

            return handleValidationException(e, request);

        } catch (DatabaseUpdateException e1) {
        	e1.printStackTrace();
        	//return handleValidationException(e1, request);
        }
        logger.info("save()=> END, redirect to " + workflow.getWorkflowId());

        return redirect("../../" + workflow.getWorkflowId()+"/"+workflow.getWorkflowTypeCd());

    }


    @RequestMapping(value = "/addRule", method = RequestMethod.GET)
    public String addRule(WebRequest webRequest, HttpSession session, 
            @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
            @PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
            @PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
 //   		@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL) WorkflowForm form,
    		@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_TYPE_FILTER) WorkflowRuleTypeFilterForm locateForm,
    		Model model) {

        logger.info("addRule()=> BEGIN");
        locateForm = (WorkflowRuleTypeFilterForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_TYPE_FILTER);

        if (locateForm == null) {
        	locateForm = new WorkflowRuleTypeFilterForm();
        }
        locateForm.setAccountId(accountId);
        locateForm.setWorkflowId(workflowId);
        locateForm.setWorkflowType(workflowType);
        
         
        logger.info("addRule()=> END. " );

        return "account/workflow/ruleType";

    }
    

    @RequestMapping(value = "/sites", method = RequestMethod.POST)
    public String config(WebRequest request, HttpSession session,
    		@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL) WorkflowForm form,
    		@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER) WorkflowSiteAssocFilterForm filterForm,
    		@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT) WorkflowSiteAssocFilterResultForm filterResultForm,
    		@PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
    		@PathVariable(IdPathKey.WORKFLOW_ID	) Long workflowId,
    		@PathVariable(IdPathKey.WORKFLOW_TYPE	) String workflowType,
    		Model model) {

//        WebErrors webErrors = new WebErrors(request);
        logger.info("config()=> BEGIN");

        filterForm = (WorkflowSiteAssocFilterForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER);
        if (filterForm == null || !filterForm.isInitialized()) {
            filterForm = new WorkflowSiteAssocFilterForm(workflowId);
            filterForm.initialize();
        }
    	filterForm.setWorkflowTypeCd(workflowType);
    	filterForm.setWorkflowId(form.getWorkflowId());
    	filterForm.setWorkflowName(form.getWorkflowName());
    	filterForm.setAccountId(accountId);

        model.addAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER, filterForm);

        filterResultForm = (WorkflowSiteAssocFilterResultForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT);

        logger.info("config()=> filterResultForm: " + filterResultForm);

        if (filterResultForm == null) {
        	filterResultForm = new WorkflowSiteAssocFilterResultForm();
        }

        if (Utility.isSet(filterResultForm.getResult())) {
        	filterResultForm.reset();
        }

        logger.info("config()=> END.");

        return "account/workflow/sites";

    }

    @SuccessMessage
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(WebRequest request,
			@PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
    		@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL) WorkflowForm form,
            @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_FILTER_RESULT) WorkflowRuleFilterResultForm ruleForm,
    		Model model) {

        logger.info("delete()=> BEGIN");
        List<WorkflowRuleData> selected = ruleForm.getRules().getSelected();
        logger.info("delete()=> selected :" + selected);

 //       if (selected != null && !selected.isEmpty()){
        	try {
        		workflowService.deleteWorkflowRules(selected, workflowType, form.getWorkflowId());
            } catch (ValidationException e) {
                return handleValidationException(e, request);
            }    
	        showRules(form.getWorkflowId(), workflowType, ruleForm);
	        
//        }
        
        logger.info("delete()=> END.");

        return redirect("../../" + form.getWorkflowId()+"/"+workflowType);
//        return "account/workflow/edit";

    }

    @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL)
    public WorkflowForm initModel(@PathVariable(IdPathKey.WORKFLOW_ID) Long pWorkflowId) {

        WorkflowForm form = new WorkflowForm();
        form.initialize();

        return form;

    }
    
    @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_FILTER_RESULT)
    public WorkflowRuleFilterResultForm init(HttpSession session) {
        logger.info("init()=> BEGIN." );

        WorkflowRuleFilterResultForm ruleFilterResult = (WorkflowRuleFilterResultForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_FILTER_RESULT);

        if (ruleFilterResult == null) {
        	ruleFilterResult = new WorkflowRuleFilterResultForm();
        	
        }
        ruleFilterResult.reset();
        logger.info("init()=> END." );

        return ruleFilterResult;

    }

    private Map<Long, List<WorkflowRuleData>> createRuleRecordMap(List<WorkflowRuleData> pRules){
    	Map<Long, List<WorkflowRuleData>> map = new HashMap<Long, List<WorkflowRuleData>>();
    	List<WorkflowRuleData> records = null;
    	for (WorkflowRuleData r : pRules) {
    		records = (List<WorkflowRuleData>)map.get(r.getRuleSeq());
    		if (!Utility.isSet(records)) {
    			records = new ArrayList<WorkflowRuleData>();
    			map.put(r.getRuleSeq(), records);
    		}
    		records.add(r);
    	}
        logger.info(" createRuleRecordMap()=> END. map.size="+ map.size() );
    	return map;
    }
}
