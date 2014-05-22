package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.DistributorService;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.service.WorkflowService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;
import com.espendwise.manta.util.criteria.StoreDistributorCriteria;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.rules.SkuNotFoundRule;
import com.espendwise.manta.util.validation.rules.WorkflowRuleInUseConstraint;
import com.espendwise.manta.web.forms.SiteFilterForm;
import com.espendwise.manta.web.forms.WorkflowRuleForm;
import com.espendwise.manta.web.forms.WorkflowRuleTypeFilterForm;
import com.espendwise.manta.web.forms.WorkflowForm;
import com.espendwise.manta.web.forms.WorkflowRuleFilterResultForm;
import com.espendwise.manta.web.forms.WorkflowSiteAssocFilterForm;
import com.espendwise.manta.web.forms.WorkflowSiteAssocFilterResultForm;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.validator.WorkflowFormValidator;
import com.espendwise.manta.web.validator.WorkflowRuleFormValidator;
import com.espendwise.manta.web.validator.WorkflowWebUpdateExceptionResolver;
//import com.espendwise.manta.web.resolver.TextErrorWebResolver;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.WORKFLOW_RULE_DETAIL)
@SessionAttributes(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL)
public class WorkflowRuleController extends BaseController {

    private static final Logger logger = Logger.getLogger(WorkflowRuleController.class);

    private WorkflowService workflowService;
    private GroupService groupService;
    private DistributorService distrService;
    private CatalogService catService;

    @Autowired
    public WorkflowRuleController(WorkflowService workflowService,
    							  GroupService groupService, 
    							  DistributorService distrService,
    							  CatalogService catService) {
        this.workflowService = workflowService;
        this.groupService = groupService;
        this.distrService = distrService;
        this.catService = catService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new WorkflowWebUpdateExceptionResolver());

        return "account/workflow/rule";
    }
 
 
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(WebRequest request, HttpSession session,
                       @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL) WorkflowRuleForm ruleForm,
                       @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                       @PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
                       @PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
                       @PathVariable(IdPathKey.WORKFLOW_RULE_ID) Long workflowRuleId,
                       Model model)  throws Exception {

    	WorkflowForm form = (WorkflowForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL);
         logger.info("show()=> BEGIN" +
        		", storeId: "+ getStoreId()+
                ", accountId: " + accountId +
                ", workflowId: " + workflowId +
                ", workflowType: " + workflowType +
                ", ruleType(new): " + form.getWorkflowRuleTypeCdToEdit() +
                ", ruleType(edit): " + ruleForm.getWorkflowRuleTypeCd());

    //    logger.info("show()=> =====,  detailForm.hashCode()="+ form.hashCode());
        
    //    logger.info("show()=> =====,  ruleForm.hashCode()="+ ruleForm.hashCode());
         
         
//        WorkflowRuleData rule = workflowService.findWorkflowRule(workflowRuleId, workflowType);
        List<WorkflowRuleData> ruleRecords = workflowService.findWorkflowRuleRecords(workflowRuleId, workflowType);
        
        List<WorkflowAssocData> ruleAssocList = workflowService.findWorkflowAssocCollection(workflowId, workflowRuleId, workflowType);
        WorkflowAssocData ruleAssoc = (Utility.isSet(ruleAssocList) && !ruleAssocList.isEmpty()) ? (WorkflowAssocData)ruleAssocList.get(0) : new WorkflowAssocData();
        //------------search Groups---------------------------
        List<GroupData> groups = form.getUserGroups();
        WebSort.sort(groups, GroupData.SHORT_DESC, new Boolean(true));
        //------------search Categories---------------------------
 
        List<ItemView> categories = WorkflowUtil.getItemCategories(accountId);
        WebSort.sort(categories, ItemView.SHORT_DESC, new Boolean(true));
        
    	ruleForm.reset();
        if (Utility.isSet(ruleRecords)){
        	processRuleRecords(ruleForm, ruleRecords, ruleForm.getWorkflowRuleTypeCd(), workflowType );
        	ruleForm.setApplySkipActionTypeCd(parseWorkflowAssocCd(ruleAssoc.getWorkflowAssocCd()));
        	ruleForm.setApplySkipGroupId(ruleAssoc.getGroupId());
    
        } else {
        	ruleForm.setWorkflowRuleId(new Long(0));
        	ruleForm.setRuleNumber("0");
        	ruleForm.setWorkflowRuleTypeCd(form.getWorkflowRuleTypeCdToEdit());
        }
        
    	ruleForm.setAccountId(accountId);
    	ruleForm.setWorkflowId(workflowId);
        ruleForm.setWorkflowTypeCd(workflowType);
     	ruleForm.setUserGroups(groups);
     	ruleForm.setItemCategories(categories);
     	
        EntityHeaderView header = (EntityHeaderView)request.getAttribute(SessionKey.WORKFLOW_HEADER, WebRequest.SCOPE_SESSION);
        ruleForm.setWorkflowName((header!=null) ? header.getShortDesc() : "");
 
        model.addAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL, ruleForm);
        
        logger.info("show()=> END. ruleForm.getEmailUserId()=" + ruleForm.getEmailUserId());

        return "account/workflow/rule";


    }

    @SuccessMessage
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(WebRequest request, HttpSession session,
			            @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL) WorkflowRuleForm ruleForm,
			            @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
			            @PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
			            @PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
			            @PathVariable(IdPathKey.WORKFLOW_RULE_ID) Long workflowRuleId,
                       Model model) {

        logger.info("save()=> BEGIN" +
                ", accountId: " + accountId );
               // ", workflowId: " + workflowId +
               // ", workflowType: " + workflowType +
                
       	WorkflowForm form = (WorkflowForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL);

        logger.info("save()=> form.getWorkflowRuleTypeCdToEdit()  = " + form.getWorkflowRuleTypeCdToEdit() );
        logger.info("save()=> ruleForm.getWorkflowRuleTypeCd()  = " + ruleForm.getWorkflowRuleTypeCd() );
        		
        WebErrors webErrors = new WebErrors(request);

       	try {
	        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(ruleForm, new WorkflowRuleFormValidator());
	        if (!validationErrors.isEmpty()) {
	            webErrors.putErrors(validationErrors);
	            model.addAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL, ruleForm);
	            logger.info("save()=> ERRORS: " + webErrors);
	            return "account/workflow/rule";
	        }
        } catch (ValidationException e) {
            return handleValidationException(e, request);
        }    
        
        
        logger.info("save()=> ruleForm.getWorkflowRuleId()  = " + ruleForm.getWorkflowRuleId() );

        WorkflowRuleData rule = new WorkflowRuleData();
        List<WorkflowRuleData> ruleRecords = Utility.toList(rule);
        if (workflowRuleId.intValue() != 0) {
        	ruleRecords = workflowService.findWorkflowRuleRecords( workflowRuleId, ruleForm.getWorkflowTypeCd());
            WebSort.sort(ruleRecords, WorkflowRuleData.WORKFLOW_RULE_ID, new Boolean(true));
        	rule = (WorkflowRuleData)ruleRecords.get(0);
        }

//	    Long maxSeq = new Long(0);
	    Long maxSeq = workflowService.findMaxRuleSeq(workflowId, workflowType) ;
        Long originalRuleSeq = rule.getRuleSeq();
        if (!Utility.isSet(originalRuleSeq) || originalRuleSeq.longValue()==0) {
//    		maxSeq = workflowService.findMaxRuleSeq(workflowId, workflowType) ;
    		originalRuleSeq = new Long(maxSeq.intValue()+1);
        }
        if (Utility.isSet(rule.getWorkflowRuleId())) {
	        if (Utility.isSet(rule.getRuleAction()) && !Utility.isEqual(rule.getRuleAction(), ruleForm.getRuleAction()) ||
	        	isChangedRuleActionExp(ruleRecords, ruleForm)) {
	            // check if rule in the queue
	            // and order is not approve or reject manually
	      		try {
	    			ServiceLayerValidation validation = new ServiceLayerValidation();
		            validation.addRule(new WorkflowRuleInUseConstraint(workflowId, workflowType, Utility.toIds(ruleRecords), false));
		            validation.validate();
	    	    } catch (ValidationException e) {
	    	        return handleValidationException(e, request);
	   	        }    
	        	
	        }
        }
        
        rule.setRuleSeq((Utility.isSet(ruleForm.getRuleNumber()))? new Long (ruleForm.getRuleNumber()): new Long(0));
        rule.setRuleGroup(ruleForm.getRuleGroup());
        rule.setApproverGroupId(ruleForm.getApproverGroupId());
        rule.setEmailGroupId(ruleForm.getEmailGroupId());
        rule.setNextActionCd(prepareNextAction(ruleForm));
        rule.setRuleAction((Utility.isSet(ruleForm.getRuleAction())) ? ruleForm.getRuleAction() : "-");
        rule.setRuleExp((Utility.isSet(ruleForm.getTotalExp())) ? ruleForm.getTotalExp() : "-");
        rule.setRuleExpValue((Utility.isSet(ruleForm.getTotalValue())) ? ruleForm.getTotalValue() : "-");
        rule.setRuleTypeCd(ruleForm.getWorkflowRuleTypeCd());
//        rule.setRuleTypeCd(form.getWorkflowRuleTypeCdToEdit()); 
        rule.setWarningMessage(ruleForm.getRuleMessage());
        rule.setWorkflowId(workflowId);
       
        rule.setWorkflowRuleStatusCd(RefCodeNames.WORKFLOW_STATUS_CD.ACTIVE);
        rule.setShortDesc(String.valueOf(workflowId));
        
        // Prepare Workflow Assoc data
        List<WorkflowAssocData> ruleAssocList = null;
	    if (Utility.isSet(ruleForm.getApplySkipGroupId()) && ruleForm.getApplySkipGroupId().intValue() > 0) {
	    	ruleAssocList = new ArrayList<WorkflowAssocData>();
			WorkflowAssocData ruleAssoc = new WorkflowAssocData();
			ruleAssocList.add(ruleAssoc);
			ruleAssoc.setGroupId(ruleForm.getApplySkipGroupId());
			ruleAssoc.setWorkflowId(workflowId);
			ruleAssoc.setWorkflowRuleId(workflowRuleId);
			if (RefCodeNames.WORKFLOW_ASSOC_ACTION_TYPE_CD.APPLY.equals(ruleForm.getApplySkipActionTypeCd())) {
			    ruleAssoc.setWorkflowAssocCd(RefCodeNames.WORKFLOW_ASSOC_CD.APPLY_FOR_GROUP_USERS);
			} else {
			    ruleAssoc.setWorkflowAssocCd(RefCodeNames.WORKFLOW_ASSOC_CD.SKIP_FOR_GROUP_USERS);
			}
	    } else {
	    	ruleAssocList = new ArrayList<WorkflowAssocData>();
			WorkflowAssocData ruleAssoc = new WorkflowAssocData();
			ruleAssocList.add(ruleAssoc);
			ruleAssoc.setGroupId(new Long(0));
			ruleAssoc.setWorkflowId(workflowId);
			ruleAssoc.setWorkflowRuleId(workflowRuleId);
	    	
	    }

//	    List<WorkflowRuleData> ruleRecords = Utility.toList(rule);

	    try {
		    if (ruleForm.getIsMultyExpRuleType()){
		    	if (workflowRuleId.intValue() == 0) {
		    		ruleRecords.addAll(createRuleRecords(ruleForm, rule));
		    	} else {
		    		
		    		updateRuleRecords(ruleForm, ruleRecords, rule);
		    	}
		    } 
	    	
		    logger.info("save()=> rule.getWorkflowRuleId()  = " + rule.getWorkflowRuleId() + ", ruleRecords: " + ruleRecords);
	    	
		    List<WorkflowRuleData> rulesToChangeSeq  =  WorkflowUtil.getRulesToChangeSeq(originalRuleSeq, ruleRecords, workflowId, workflowType);
           	
		    Long workflowRuleRecordId = null;
		    logger.info("save()=> Loop 1 to update curr rule records ::::: ruleRecords.size()=" + ruleRecords.size() );
		    for (WorkflowRuleData r : ruleRecords){
		    	if (r.getRuleSeq().longValue() == 0 ) {
		    		r.setRuleSeq(new Long(maxSeq.intValue()+ ((originalRuleSeq.equals(maxSeq))? 0 : 1)));
		    	}
			    logger.info("save()=====> Loop 1 ==> r.getWorkflowRuleId() = " + r.getWorkflowRuleId() + ", r.getRuleTypeCd=" + r.getRuleTypeCd()+ ", r.getRuleSeq=" + r.getRuleSeq());
           		r = workflowService.updateWorkflowRule( r, workflowType);
           		workflowRuleRecordId = (!Utility.isSet(workflowRuleRecordId)) ? r.getWorkflowRuleId() : workflowRuleRecordId;
		    }
		    
		    if (Utility.isSet(rulesToChangeSeq)) {
			    logger.info("save()=> Loop 2 to change seq ::::: rulesToChangeSeq.size()="+ rulesToChangeSeq.size());
            	for (WorkflowRuleData r : rulesToChangeSeq){
    			    logger.info("save()=====> Loop 2 ==>1r.getWorkflowRuleId() = " + r.getWorkflowRuleId() + ", r.getRuleTypeCd=" + r.getRuleTypeCd()+ ", r.getRuleSeq=" + r.getRuleSeq());
            		workflowService.updateWorkflowRule(r, workflowType);
            	}
            }
            if ( ruleAssocList != null && !ruleAssocList.isEmpty() && workflowRuleRecordId.intValue() > 0) {
            	// save ruleAssoc	
            	workflowService.updateWorkflowAssoc(ruleAssocList, workflowRuleRecordId, workflowType );
            }
        } catch (ValidationException e) {

            return handleValidationException(e, request);

        } catch (DatabaseUpdateException e1) {
        	e1.printStackTrace();
        	//return handleValidationException(e1, request);
        }
        logger.info("save()=> END, redirect to " + rule.getWorkflowRuleId());

        return redirect("../" + rule.getWorkflowRuleId());

    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel(WebRequest request,
                       @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                       @PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
                       @PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
                       Model model) {

        logger.info("cancel()=> BEGIN" +
                ", accountId: " + accountId +
                ", workflowId: " + workflowId +
                ", workflowType: " + workflowType );

//        return redirect("../../" + workflow.getWorkflowId()+"/"+workflow.getWorkflowTypeCd());
        return redirect("../../");

    }
    @RequestMapping(value = "/clear/user", method = RequestMethod.POST)
    public String clearUserFilter(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL) WorkflowRuleForm form) throws Exception {
        logger.info("clearUserFilter()=> BEGIN" );

    	form.setEmailUsers(Utility.emptyList(UserListView.class));
        form.setEmailUserId(null);
        form.setEmailUserName(null);
        return redirect(request, UrlPathKey.ACCOUNT.WORKFLOW_RULE_DETAIL);
    }
 
    @RequestMapping(value = "/clear/distr", method = RequestMethod.POST)
    public String clearDistributorFilter(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL) WorkflowRuleForm form) throws Exception {
        form.setFilteredDistr(Utility.emptyList(DistributorListView.class));
        form.setDistrFilter(null);
//        logger.info("clearDistributorFilter()=> END : form.getFilteredDistrCommaNames()=" + form.getFilteredDistrCommaNames());
        return "account/workflow/rule";
//        return redirect(request, UrlPathKey.ACCOUNT.WORKFLOW_RULE_DETAIL);

    }

    @RequestMapping(value = "/clear/item", method = RequestMethod.POST)
    public String clearItemFilter(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL) WorkflowRuleForm form) throws Exception {
        form.setFilteredItem(Utility.emptyList(ProductListView.class));
        form.setItemFilter(null);
        return "account/workflow/rule";
//        return redirect(request, UrlPathKey.ACCOUNT.WORKFLOW_RULE_DETAIL);
    }
 
    private String prepareNextAction(WorkflowRuleForm ruleForm) {
     	if (!ruleForm.getIsNextRuleSectionRequired()) {
     		return "-";
     	} 
        boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(ruleForm.getWorkflowTypeCd());
         String finalAction = "Done";
         String nextAction = ruleForm.getNextActionCd();
         if (isOrca) {
		    	finalAction = " After " + ruleForm.getDaysUntilNextAction() + " days ";
		    	finalAction += nextAction + ". ";
        } else {
		    if (  RefCodeNames.WORKFLOW_RULE_ACTION.FWD_FOR_APPROVAL.equals(ruleForm.getRuleAction()) ||
		    	  RefCodeNames.WORKFLOW_RULE_ACTION.STOP_ORDER.equals(ruleForm.getRuleAction())) {
		    	finalAction = " After " + ruleForm.getDaysUntilNextAction() + " days ";
		    	finalAction += nextAction + ". ";
		    } else if (RefCodeNames.WORKFLOW_RULE_ACTION.SEND_EMAIL.equals(ruleForm.getRuleAction())) {
		    	finalAction = " To: " + ruleForm.getEmailUserName() + " Id: " + ruleForm.getEmailUserId() + ". ";
		    }
        }
	    return finalAction;
    }

    @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL)
    public WorkflowRuleForm initModel() {
    	
        WorkflowRuleForm form = new WorkflowRuleForm();
        form.initialize();

        return form;

    }

    @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL)
    public WorkflowRuleForm init(HttpSession session) {
        logger.info("init()=>  BEGIN.");
          	
        WorkflowRuleForm form = (WorkflowRuleForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL);

        if (form == null || !form.isInitialized()) {
            form = new WorkflowRuleForm();
            form.initialize();
        }
       session.setAttribute(SessionKey.ACCOUNT_WORKFLOW_RULE_DETAIL, form);
       logger.info("init()=>  END. ruleForm.hashCode() = "+ form.hashCode());
        
        return form;

    }
 
    private String parseWorkflowAssocCd(String pWorkflowAssocCd) {
    	String ruleApplySkipActionTypeCd = "";
    	if (RefCodeNames.WORKFLOW_ASSOC_CD.APPLY_FOR_GROUP_USERS.equals(pWorkflowAssocCd)){
    		ruleApplySkipActionTypeCd = RefCodeNames.WORKFLOW_ASSOC_ACTION_TYPE_CD.APPLY;
    	} else if (RefCodeNames.WORKFLOW_ASSOC_CD.SKIP_FOR_GROUP_USERS.equals(pWorkflowAssocCd)){
    		ruleApplySkipActionTypeCd = RefCodeNames.WORKFLOW_ASSOC_ACTION_TYPE_CD.SKIP;
    	}
    	return ruleApplySkipActionTypeCd ;
    }
    
//    private void parseNextActionCd(WorkflowRuleForm pForm, String pNextAction) {
    private Pair parseNextActionCdByFwa(String pNextAction, String workflowType) {
        boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(workflowType);
		if (pNextAction==null) {
			return null;
		}
		Integer interval = 0;
		String nextActionCd ="";
		logger.info("parseNextActionCdByFwa()=> BEGIN. pNextAction=" + pNextAction);
		if(pNextAction.trim().startsWith("After")) {
			int ind1 = pNextAction.indexOf("days");
			if(ind1>6) {
				String intervalS = pNextAction.substring(6,ind1).trim();
				try {
					interval = new Integer(intervalS);
				} catch(Exception exc) {
					exc.printStackTrace();
				} //Dump exception - nothing serious
			}
			if (isOrca) {
				if(pNextAction.indexOf(RefCodeNames.WO_WORKFLOW_NEXT_RULE_ACTION.APPROVE) != -1) {
					nextActionCd = RefCodeNames.WO_WORKFLOW_NEXT_RULE_ACTION.APPROVE;
				} else if(pNextAction.indexOf(RefCodeNames.WO_WORKFLOW_NEXT_RULE_ACTION.REJECT) != -1) {
					nextActionCd = RefCodeNames.WO_WORKFLOW_NEXT_RULE_ACTION.REJECT;
				}
			} else {
				if(pNextAction.indexOf(RefCodeNames.WORKFLOW_NEXT_RULE_ACTION.APPROVE) != -1) {
					nextActionCd = RefCodeNames.WORKFLOW_NEXT_RULE_ACTION.APPROVE;
				} else if(pNextAction.indexOf(RefCodeNames.WORKFLOW_NEXT_RULE_ACTION.REJECT) != -1) {
					nextActionCd = RefCodeNames.WORKFLOW_NEXT_RULE_ACTION.REJECT;
				}
			}
		}	
	    Pair obj = new Pair(interval, nextActionCd );
	    return obj;

    }			
    private Pair parseNextActionCdBySendEmail(String pNextAction) {
		String userIdS = "";
		String userNameS ="";
		if (pNextAction.trim().startsWith("To:")) {
			int ind0 = pNextAction.indexOf("Id:");
			logger.info("parseNextActionCdBySendEmail=> BEGIN. starts with <To:> ind0=" +ind0);
			if(ind0>-1) {
				ind0 += "Id:".length();
				int ind1 = pNextAction.indexOf(".",ind0);
				if(ind1>ind0) {
					userIdS = pNextAction.substring(ind0,ind1).trim();
					userNameS = pNextAction.substring(pNextAction.indexOf("To:")+"To:".length(), pNextAction.indexOf("Id:")).trim() ;
					logger.info("parseNextActionCdBySendEmail=> END. userNameS =" +userNameS + ", userIdS=" + userIdS);
				}
			}
		}
	    Pair obj = new Pair(userIdS, userNameS);
	    return obj;
		
    }
    // Parse rule's details
    private void processRuleRecords(WorkflowRuleForm ruleForm, List<WorkflowRuleData> ruleRecords, String ruleTypeCd, String workflowType ) throws Exception {
    	if (ruleForm != null && Utility.isSet(ruleRecords)){
	    //	WorkflowRuleData rule = (WorkflowRuleData)ruleRecords.get(0);
		//	processRule (ruleForm, rule, ruleTypeCd, workflowType );
//			if (ruleForm.getIsMultyExpRuleType()){
    		    StringBuffer skus = new StringBuffer();
    		    List<Long> distributorIds = new ArrayList<Long>();
    		    List<Long> itemIds = new ArrayList<Long>();
				for (WorkflowRuleData rec : ruleRecords){
logger.info("processRuleRecords()===> rec.getRuleTypeCd() = " + rec.getRuleTypeCd() + ", rec.getWorkflowRuleId()=" + rec.getWorkflowRuleId() + ", rec.getRuleExp()=" + rec.getRuleExp());					
					if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM_CATEGORY.equals(rec.getRuleTypeCd())) {
						if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID.equals(rec.getRuleExp()) ) {
							ruleForm.setItemCategoryId(rec.getRuleExpValue());
						} else if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(rec.getRuleExp())) {
							distributorIds.add((new Long(rec.getRuleExpValue())));
						} else if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp()) ) {
							ruleForm.setSplitOrder(new Boolean(Utility.isTrue(rec.getRuleExpValue())));
						} else {
							processRule (ruleForm, rec, rec.getRuleTypeCd(), workflowType );
						}
					} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_TOTAL.equals(rec.getRuleTypeCd())) {
						if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(rec.getRuleExp())) {
							distributorIds.add((new Long(rec.getRuleExpValue())));
						} else if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp()) ) {
							ruleForm.setSplitOrder(new Boolean(Utility.isTrue(rec.getRuleExpValue())));
						} else {
							processRule (ruleForm, rec, rec.getRuleTypeCd(), workflowType );
						}
					} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.CATEGORY_TOTAL.equals(rec.getRuleTypeCd())) {
						if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID.equals(rec.getRuleExp()) ) {
							ruleForm.setItemCategoryId(rec.getRuleExpValue());
						} else if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(rec.getRuleExp())) {
							distributorIds.add((new Long(rec.getRuleExpValue())));
						} else if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp()) ) {
							ruleForm.setSplitOrder(new Boolean(Utility.isTrue(rec.getRuleExpValue())));
						} else {
							processRule (ruleForm, rec, rec.getRuleTypeCd(), workflowType );
						}
					} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM.equals(rec.getRuleTypeCd())) {
						if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.ITEM_ID.equals(rec.getRuleExp())) {
							itemIds.add((new Long(rec.getRuleExpValue())));
						} else if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp()) ) {
							ruleForm.setSplitOrder(new Boolean(Utility.isTrue(rec.getRuleExpValue())));
						} else {
							processRule (ruleForm, rec, rec.getRuleTypeCd(), workflowType );
						}
					} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.SHOPPING_CONTROLS.equals(rec.getRuleTypeCd())) {
						if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp()) ) {
							ruleForm.setSplitOrder(new Boolean(Utility.isTrue(rec.getRuleExpValue())));
						} else {
							processRule (ruleForm, rec, rec.getRuleTypeCd(), workflowType );
						}
					} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.NON_ORDER_GUIDE_ITEM.equals(rec.getRuleTypeCd())) {
						if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.INCLUDE_BUYER_LIST.equals(rec.getRuleExp()) ) {
							ruleForm.setIncludeBuyerList(new Boolean(Utility.isTrue(rec.getRuleExpValue())));
						} else {
							processRule (ruleForm, rec, rec.getRuleTypeCd(), workflowType );
						}
					} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU_QTY.equals(rec.getRuleTypeCd()) && 
							   RefCodeNames.WORKFLOW_RULE_EXPRESSION.SKU_NUM.equals(rec.getRuleExp())) {
						skus.append((skus.length()!= 0) ? ", " : "");
						skus.append(rec.getRuleExpValue());
						ruleForm.setSkuNames(skus.toString());
					} else {
						processRule (ruleForm, rec, rec.getRuleTypeCd(), workflowType );
					}
				}
				if (Utility.isSet(distributorIds)) {
					StoreDistributorCriteria criteria = new StoreDistributorCriteria ();
					criteria.setDistributorIds(distributorIds);
					ruleForm.setFilteredDistr((List<DistributorListView>)convertToView(distrService.findDistributors(criteria), DistributorListView.class));
				}
				if (Utility.isSet(itemIds)) {
					ProductListViewCriteria criteria = new ProductListViewCriteria();
					criteria.setItemIds(itemIds);
					criteria.setCatalogId(WorkflowUtil.getStoreCatalogId(getStoreId()));
					ruleForm.setFilteredItem((List<ProductListView>)convertToView(catService.findItems(criteria), ProductListView.class));
				}

//	    	} else {
//				processRule (ruleForm, (WorkflowRuleData)ruleRecords.get(0), ruleTypeCd, workflowType );
//	    	}
    	}
    }
    private void processRule (WorkflowRuleForm ruleForm, WorkflowRuleData rule, String ruleTypeCd, String workflowType ) {
        if (rule != null) {
        	ruleForm.setWorkflowRuleId(rule.getWorkflowRuleId());
        	ruleForm.setWorkflowRuleTypeCd(rule.getRuleTypeCd());
        	ruleForm.setRuleNumber(""+rule.getRuleSeq());
        	ruleForm.setRuleGroup(rule.getRuleGroup());
        	ruleForm.setRuleAction(rule.getRuleAction());
        	
        	ruleForm.setTotalExp(rule.getRuleExp());
        	ruleForm.setTotalValue(rule.getRuleExpValue());
        	ruleForm.setApproverGroupId(rule.getApproverGroupId());
        	ruleForm.setEmailGroupId(rule.getEmailGroupId());
        	ruleForm.setRuleMessage(rule.getWarningMessage());

        	Pair obj = null;
			if (rule.getRuleAction().equals(RefCodeNames.WORKFLOW_RULE_ACTION.SEND_EMAIL)) {
				obj = parseNextActionCdBySendEmail(rule.getNextActionCd());
				ruleForm.setEmailUserId((String)obj.getObject1());
	        	ruleForm.setEmailUserName((String)obj.getObject2());
			} else {
				obj = parseNextActionCdByFwa(rule.getNextActionCd(), workflowType);
	        	ruleForm.setDaysUntilNextAction((Integer)obj.getObject1());
	        	ruleForm.setNextActionCd((String)obj.getObject2());
			}
        }
   	
    }
    
    private List<WorkflowRuleData> createRuleRecords(WorkflowRuleForm ruleForm, WorkflowRuleData rule){
    	List<WorkflowRuleData> records = new ArrayList<WorkflowRuleData>();
    	if (ruleForm != null && rule != null ) {
    		String ruleTypeCd = rule.getRuleTypeCd();
    		WorkflowRuleData rec = new WorkflowRuleData();
    		copy(rec, rule);
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM_CATEGORY.equals(ruleTypeCd)) {
            	rec.setRuleExp(RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID);
            	rec.setRuleExpValue(ruleForm.getItemCategoryId());
            	records.add(rec);
            	records.addAll(createRuleRecordsForSet(rule, Utility.anyDelimitedListToSet(ruleForm.getFilteredDistrCommaIds())));
            	records.addAll(createRuleRecordForProp(rule, RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER, Utility.isTrueStrOf(ruleForm.getSplitOrder())) );
			} 
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_TOTAL.equals(ruleTypeCd)) {
            	records.addAll(createRuleRecordsForSet(rule, Utility.anyDelimitedListToSet(ruleForm.getFilteredDistrCommaIds())));
            	records.addAll(createRuleRecordForProp(rule, RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER, Utility.isTrueStrOf(ruleForm.getSplitOrder())) );
            } 
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.CATEGORY_TOTAL.equals(ruleTypeCd)) {
            	rec.setRuleExp(RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID);
            	rec.setRuleExpValue(ruleForm.getItemCategoryId());
            	records.add(rec);
            	records.addAll(createRuleRecordsForSet(rule, Utility.anyDelimitedListToSet(ruleForm.getFilteredDistrCommaIds())));
            	records.addAll(createRuleRecordForProp(rule, RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER, Utility.isTrueStrOf(ruleForm.getSplitOrder())) );
            } 
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM.equals(ruleTypeCd)) {
            	records.addAll(createRuleRecordsForSet(rule, Utility.anyDelimitedListToSet(ruleForm.getFilteredItemCommaIds())));
            	records.addAll(createRuleRecordForProp(rule, RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER, Utility.isTrueStrOf(ruleForm.getSplitOrder())) );
            } 
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.SHOPPING_CONTROLS.equals(ruleTypeCd)) {
            	records.addAll(createRuleRecordForProp(rule, RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER, Utility.isTrueStrOf(ruleForm.getSplitOrder())) );
            } 
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.NON_ORDER_GUIDE_ITEM.equals(ruleTypeCd)) {
            	records.addAll(createRuleRecordForProp(rule, RefCodeNames.WORKFLOW_RULE_EXPRESSION.INCLUDE_BUYER_LIST, Utility.isTrueStrOf(ruleForm.getIncludeBuyerList())) );
            } 
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU_QTY.equals(ruleTypeCd)) {
	    		Set<String> skus = Utility.anyDelimitedListToSet(ruleForm.getSkuNames());
	    		logger.info("createRuleRecords()===> skus =" + skus);
				//records.addAll(createSkuRuleRecords(rule, skus));
				records.addAll(createRuleRecordsForSet(rule, skus));
			             }
            //records.add(rec);
           
    	}
    	return records;
    }
 
 /*   private List<WorkflowRuleData> createSkuRuleRecords(WorkflowRuleData rule, Set<String> skus ){
    	List<WorkflowRuleData> records = new ArrayList<WorkflowRuleData>();
    	if (rule != null ) {
    		String ruleTypeCd = rule.getRuleTypeCd();
    		logger.info("createSkuRuleRecords() ====> ruleTypeCd: " + ruleTypeCd);
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU_QTY.equals(ruleTypeCd)) {
	    		for (String sku : skus){
	        		WorkflowRuleData rec = copy(rule);
	            	rec.setRuleExp(RefCodeNames.WORKFLOW_RULE_EXPRESSION.SKU_NUM);
	            	rec.setRuleExpValue(sku);
	    			records.add(rec);
	    		}
			}
    	}
		logger.info("createSkuRuleRecords() ====> NEW records: " + records.size());

    	return records;
    }*/
    private List<WorkflowRuleData> createRuleRecordsForSet(WorkflowRuleData rule, Set<String> items ){
    	List<WorkflowRuleData> records = new ArrayList<WorkflowRuleData>();
    	if (rule != null ) {
    		String ruleTypeCd = rule.getRuleTypeCd();
    		logger.info("createRuleRecordsForSet() ====> ruleTypeCd: " + ruleTypeCd);
			String expName = "";
    		if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU_QTY.equals(ruleTypeCd)) {
    			expName =RefCodeNames.WORKFLOW_RULE_EXPRESSION.SKU_NUM;
			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM_CATEGORY.equals(ruleTypeCd)) {
    			expName =RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID;
			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_TOTAL.equals(ruleTypeCd)) {
    			expName =RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID;
			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.CATEGORY_TOTAL.equals(ruleTypeCd)) {
    			expName =RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID;
			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM.equals(ruleTypeCd)) {
    			expName =RefCodeNames.WORKFLOW_RULE_EXPRESSION.ITEM_ID;
			}
			for (String item : items){
        		//WorkflowRuleData rec = copy(null, rule);
        		WorkflowRuleData rec = new WorkflowRuleData();
        		copy(rec, rule);
            	rec.setRuleExp(expName);
            	rec.setRuleExpValue(item);
    			records.add(rec);
    		}
    	}
		logger.info("createRuleRecordsForSet() ====> NEW records: " + records.size());

    	return records;
    }
    private List<WorkflowRuleData> createRuleRecordForProp(WorkflowRuleData rule, String propName, String propValue ){
    	List<WorkflowRuleData> records = new ArrayList<WorkflowRuleData>();
      	if (rule != null ) {
    		String ruleTypeCd = rule.getRuleTypeCd();
    		logger.info("createRuleRecordsForProp() ====> ruleTypeCd: " + ruleTypeCd);
//    		WorkflowRuleData rec = copy(null, rule);
    		WorkflowRuleData rec = new WorkflowRuleData();
    		copy(rec, rule);
        	rec.setRuleExp(propName);
        	rec.setRuleExpValue(propValue);
        	records.add(rec);
       	}
       	return records;
    }
    
    private void updateRuleRecords(WorkflowRuleForm ruleForm, List<WorkflowRuleData> records, WorkflowRuleData baseRule){
    	if (ruleForm != null && records != null ) {
    		WorkflowRuleData mainRuleRec = null; 
    		Set<WorkflowRuleData> toDeleteRuleRecords = new HashSet<WorkflowRuleData>();

    		Set<String> items = null;
    		if (ruleForm.getIsItemCategoryRuleType()) {
    			items = Utility.anyDelimitedListToSet(ruleForm.getFilteredDistrCommaIds());
    		} else if (ruleForm.getIsOrderSkuQtyRuleType()){
    			items = Utility.anyDelimitedListToSet(ruleForm.getSkuNames());
    		} else if (ruleForm.getIsOrderTotalRuleType()) {
       			items = Utility.anyDelimitedListToSet(ruleForm.getFilteredDistrCommaIds());
    		} else if (ruleForm.getIsCategoryTotalRuleType()) {
       			items = Utility.anyDelimitedListToSet(ruleForm.getFilteredDistrCommaIds());
    		} else if (ruleForm.getIsItemRuleType()) {
       			items = Utility.anyDelimitedListToSet(ruleForm.getFilteredItemCommaIds());
    		}
			
    		boolean isUpdateSplitOrderRec = false;
    		boolean isUpdateExtraRec = false;
    		//Set<String> skus = Utility.anyDelimitedListToSet(ruleForm.getSkuNames());

    		logger.info("updateRuleRecords() =====> ALL items: " + items + ", records.size()="+ records.size());
    		for (WorkflowRuleData rec : records){
     			//logger.info("updateRuleRecords() =====> FOR rec.getWorkflowRuleId() = "+ rec.getWorkflowRuleId()+ ", rec.getRuleExpValue()=" + rec.getRuleExpValue()+ ".");
    			//rec.setRuleSeq(new Long(Utility.isSet(ruleForm.getRuleNumber()) ? ruleForm.getRuleNumber() : "0"));
    			String ruleTypeCd = rec.getRuleTypeCd();
    			copy(rec, baseRule);
    			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM_CATEGORY.equals(ruleTypeCd)) {
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(rec.getRuleExp())){
    					collectToDeleteRuleRecords(items, rec, toDeleteRuleRecords );
             		} else
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp())){
    					isUpdateSplitOrderRec = true;
    					rec.setRuleExpValue(Utility.isTrueStrOf(ruleForm.getSplitOrder()));
             		} else
    				if  (RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID.equals(rec.getRuleExp())) {
                    	rec.setRuleExpValue(ruleForm.getItemCategoryId());
                	} else {
                    	mainRuleRec = rec;
                    } 
    			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_TOTAL.equals(ruleTypeCd)) {
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp())){
    					isUpdateSplitOrderRec = true;
    					rec.setRuleExpValue(Utility.isTrueStrOf(ruleForm.getSplitOrder()));
             		} else
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(rec.getRuleExp())){
                 		collectToDeleteRuleRecords(items, rec, toDeleteRuleRecords );
                	} else {
                    	mainRuleRec = rec;
                    }
    			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.CATEGORY_TOTAL.equals(ruleTypeCd)) {
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(rec.getRuleExp())){
    					collectToDeleteRuleRecords(items, rec, toDeleteRuleRecords );
             		} else
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp())){
    					isUpdateSplitOrderRec = true;
    					rec.setRuleExpValue(Utility.isTrueStrOf(ruleForm.getSplitOrder()));
             		} else
    				if  (RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID.equals(rec.getRuleExp())) {
                    	rec.setRuleExpValue(ruleForm.getItemCategoryId());
                	} else {
                    	mainRuleRec = rec;
                    } 
    			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM.equals(ruleTypeCd)) {
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp())){
    					isUpdateSplitOrderRec = true;
    					rec.setRuleExpValue(Utility.isTrueStrOf(ruleForm.getSplitOrder()));
             		} else
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.ITEM_ID.equals(rec.getRuleExp())){
                 		collectToDeleteRuleRecords(items, rec, toDeleteRuleRecords );
                	} else {
                    	mainRuleRec = rec;
                    }
    			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.SHOPPING_CONTROLS.equals(ruleTypeCd)) {
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(rec.getRuleExp())){
    					isUpdateSplitOrderRec = true;
    					rec.setRuleExpValue(Utility.isTrueStrOf(ruleForm.getSplitOrder()));
             		} else {
                    	mainRuleRec = rec;
                    }
    			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.NON_ORDER_GUIDE_ITEM.equals(ruleTypeCd)) {
    				if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.INCLUDE_BUYER_LIST.equals(rec.getRuleExp())){
    					isUpdateExtraRec = true;
    					rec.setRuleExpValue(Utility.isTrueStrOf(ruleForm.getIncludeBuyerList()));
             		} else {
                    	mainRuleRec = rec;
                    }
                } else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU_QTY.equals(ruleTypeCd)) {
    				if  (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SKU_NUM.equals(rec.getRuleExp())) {
                 		collectToDeleteRuleRecords(items, rec, toDeleteRuleRecords );
                	} else {
                    	mainRuleRec = rec;
                    }
                }  
    		}
//    		logger.info("updateRuleRecords() =====> Only NEW items: " + items);
//    		logger.info("updateRuleRecords() == 1 ==> records: " + records.size());
    		if (items != null && !items.isEmpty()){
    			//records.addAll(createSkuRuleRecords(mainRuleRec, items));
    			records.addAll(createRuleRecordsForSet(mainRuleRec, items));
    		}
    		if (!isUpdateSplitOrderRec && Utility.isTrue(ruleForm.getSplitOrder())){
    			records.addAll(createRuleRecordForProp(mainRuleRec, RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER, Utility.isTrueStrOf(ruleForm.getSplitOrder())) );
    		}
    		if (!isUpdateExtraRec && Utility.isTrue(ruleForm.getIncludeBuyerList())){
    			records.addAll(createRuleRecordForProp(mainRuleRec, RefCodeNames.WORKFLOW_RULE_EXPRESSION.INCLUDE_BUYER_LIST, Utility.isTrueStrOf(ruleForm.getIncludeBuyerList())) );
    		}
//     		logger.info("updateRuleRecords() === 2 (after Add) => records: " + records.size());
    		if (!toDeleteRuleRecords.isEmpty()) {
    			workflowService.deleteRuleRecords(new ArrayList<WorkflowRuleData>(toDeleteRuleRecords), ruleForm.getWorkflowTypeCd());
    			records.removeAll(toDeleteRuleRecords);
    		}
//    		logger.info("updateRuleRecords() === 3 (after Delete)=> records: " + records.size());
    		
    	}
    }
   
    private void copy(WorkflowRuleData rec, WorkflowRuleData  rule) {
		//WorkflowRuleData rec = (origRec == null) ? new WorkflowRuleData() : origRec; 
        if (rec == null){
        	rec= new WorkflowRuleData();
        }
		rec.setRuleSeq(rule.getRuleSeq());
        rec.setRuleGroup(rule.getRuleGroup());
        rec.setApproverGroupId(rule.getApproverGroupId());
        rec.setEmailGroupId(rule.getEmailGroupId());
        rec.setNextActionCd(rule.getNextActionCd());
        rec.setRuleAction(rule.getRuleAction());
        rec.setRuleTypeCd(rule.getRuleTypeCd());
        rec.setWarningMessage(rule.getWarningMessage());
        rec.setWorkflowId(rule.getWorkflowId());
        rec.setWorkflowRuleStatusCd(rule.getWorkflowRuleStatusCd());
        rec.setShortDesc(rule.getShortDesc());
   	    //return rec;
    }
    
    private boolean isChangedRuleActionExp(List<WorkflowRuleData>  ruleRecords, WorkflowRuleForm ruleForm) {
      	boolean isChanged = false;
  
      	List<String> skus = new LinkedList<String>();
      	List<String> formSkus = new LinkedList<String>();
		for (WorkflowRuleData rec : ruleRecords){
			logger.info("isChangedRuleActionExp()===> rec.getRuleTypeCd() = " + rec.getRuleTypeCd() + ", rec.getWorkflowRuleId()=" + rec.getWorkflowRuleId() + ", rec.getRuleExp()=" + rec.getRuleExp());					
			if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM_CATEGORY.equals(rec.getRuleTypeCd()) &&
				RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID.equals(rec.getRuleExp()) ) {
				isChanged = !Utility.isEqual(rec.getRuleExpValue(), ruleForm.getItemCategoryId());
			} else if (RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU_QTY.equals(rec.getRuleTypeCd()) && 
					   RefCodeNames.WORKFLOW_RULE_EXPRESSION.SKU_NUM.equals(rec.getRuleExp())) {
				skus.add(rec.getRuleExpValue());
			} else {
				isChanged = !Utility.isEqual(rec.getRuleExp(), ruleForm.getTotalExp()) ||
	    					!Utility.isEqual(rec.getRuleExpValue(), ruleForm.getTotalValue());
			}
			if (isChanged) {
				break;
			}
		}
		if (ruleForm.getIsOrderSkuQtyRuleType()) {
    		formSkus.addAll(Utility.anyDelimitedListToSet(ruleForm.getSkuNames()));
    		logger.info("isChangedRuleActionExp()===> formSkus = " + formSkus.toString());					
    		logger.info("isChangedRuleActionExp()===> Original skus = " + skus.toString());					
    		isChanged = !skus.containsAll(formSkus) || !formSkus.containsAll(skus);
		}
		logger.info("isChangedRuleActionExp()===> isChanged = " + isChanged);					
		return isChanged;
    }
    
    private void collectToDeleteRuleRecords(Set<String> items, WorkflowRuleData rec, Set<WorkflowRuleData> toDeleteRuleRecords ) {
		if (Utility.isSet(items) && items.contains(rec.getRuleExpValue())) {
			logger.info("collectToDeleteRuleRecords =====> Exists rule record: id= " + rec.getWorkflowRuleId() + ", rec.getRuleExpValue()=" + rec.getRuleExpValue()+ ".");
			items.remove(rec.getRuleExpValue());
		} else {
			logger.info("collectToDeleteRuleRecords =====> will be REMOVED rule record: id= " + rec.getWorkflowRuleId() + ", rec.getRuleExpValue()=" + rec.getRuleExpValue()+ ".");
			toDeleteRuleRecords.add(rec);
		}
    }
    
    private List convertToView (List list, Class pType ) {   	
    		List listView = new ArrayList();
	    		
	        	for (Object el : list){
	        		if (el instanceof BusEntityData){
	        	    	if (pType.equals(DistributorListView.class)){
				    		DistributorListView v = new DistributorListView();
				    		v.setDistributorId(((BusEntityData)el).getBusEntityId());
				    		v.setDistributorName(((BusEntityData)el).getShortDesc());
				    		listView.add(v);
	        	    	}
	        		} else
	        		if (el instanceof ItemView){
	        	    	if (pType.equals(ProductListView.class)){
	        	    		ProductListView v = new ProductListView();
				    		v.setItemId((((ItemView)el).getItemId()));
				    		v.setItemSku((((ItemView)el).getItemSkuNum()));
				    		listView.add(v);
	        	    	}
	        		}
		        }
    	return listView;
    }
    
}   