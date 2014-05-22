package com.espendwise.manta.web.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.service.WorkflowService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.WorkflowForm;
import com.espendwise.manta.web.forms.WorkflowSiteAssocFilterForm;
import com.espendwise.manta.web.forms.WorkflowSiteAssocFilterResultForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;


@Controller
@RequestMapping(UrlPathKey.ACCOUNT.WORKFLOW_SITES)
@SessionAttributes({SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT, SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER})
//@AutoClean(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT)
public class WorkflowSiteAssocController extends BaseController {

    private static final Logger logger = Logger.getLogger(WorkflowSiteAssocController.class);

    private WorkflowService workflowService;
    private SiteService siteService;
 
    @Autowired
    public WorkflowSiteAssocController(WorkflowService workflowService, SiteService siteService ) {
        this.workflowService = workflowService;
        this.siteService = siteService;
    }

    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {
        return "account/workflow/sites";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "account/workflow/sites";
    }

/*    @RequestMapping(value = "", method = RequestMethod.POST)
    public String view(
    		@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER) WorkflowSiteAssocFilterForm filterForm) {
    		
    	return "account/workflow/sites";
    }
*/
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findSites(WebRequest request,
    						@PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
    						@PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
    						@PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
                            @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER) WorkflowSiteAssocFilterForm filterForm,
                            @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT) WorkflowSiteAssocFilterResultForm resultForm
                            ) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "account/workflow/sites";
        }

        doSearch(accountId, workflowId, workflowType, filterForm, resultForm);

        return "account/workflow/sites";

    }

    @SuccessMessage
    @RequestMapping(value = "/assignAll", method = RequestMethod.GET)
    public String assignAll(
    			@PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
    			@PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
    			@PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
                @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER) WorkflowSiteAssocFilterForm filterForm,
                @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT) WorkflowSiteAssocFilterResultForm resultForm) throws Exception {

        logger.info("assignAll()=> BEGIN");

        logger.info("assignAll()=> workflowId: " + workflowId);
        //logger.info("update()=> storeId: " + storeId);
        SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);
        criteria.setWorkflowId(null);
        criteria.setWorkflowFilterType(workflowType);
        criteria.setAccountIds(Utility.toList(accountId));
        criteria.setActiveOnly(false);
        List<SiteListView> selected =  siteService.findSitesByCriteria(criteria);
        List<SiteListView> deSelected = Utility.emptyList(SiteListView.class);
              
        workflowService.configureWorkflowToSites(workflowId, workflowType, selected, deSelected);
 
        doSearch(accountId, workflowId, workflowType, filterForm, resultForm);

        logger.info("assignAll()=> END.");
        
        return "account/workflow/sites";

    }
    
    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(
    			@PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
    			@PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId,
    			@PathVariable(IdPathKey.WORKFLOW_TYPE) String workflowType,
                @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER) WorkflowSiteAssocFilterForm filterForm,
                @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT) WorkflowSiteAssocFilterResultForm resultForm) throws Exception {

        logger.info("update()=> BEGIN");

        logger.info("update()=> workflowId: " + workflowId);
        //logger.info("update()=> storeId: " + storeId);
        
        List<SiteListView> selected = resultForm.getSites().getSelected();
        List<SiteListView> deSelected = resultForm.getSites().getDeselected();
        
        workflowService.configureWorkflowToSites(workflowId, workflowType, selected, deSelected);
 
        doSearch(accountId, workflowId, workflowType, filterForm, resultForm);

        logger.info("update()=> END.");
        
        return "account/workflow/sites";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT) WorkflowSiteAssocFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "account/workflow/sites";
    }

    @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT)
    public WorkflowSiteAssocFilterResultForm initFilterResult(HttpSession session) {

        logger.info("initFilterResult()=> init....");

        WorkflowSiteAssocFilterResultForm form = (WorkflowSiteAssocFilterResultForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER_RESULT);

        logger.info("initFilterResult()=> form: " + form);

        if (form == null) {
            form = new WorkflowSiteAssocFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("initFilterResult()=> init.OK!");

        return form;

    }
    
    @ModelAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER)
    public WorkflowSiteAssocFilterForm initFilter(HttpSession session,
    			@PathVariable(IdPathKey.WORKFLOW_ID) Long workflowId ) {

        logger.info("initFilter()=> init....workflowId="+ workflowId);

        WorkflowSiteAssocFilterForm form = (WorkflowSiteAssocFilterForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_SITE_FILTER);
        logger.info("initFilter()=> form: " + form);

        if (form == null || !form.isInitialized()) {
            form = new WorkflowSiteAssocFilterForm(workflowId);
            form.initialize();
        }
    	WorkflowForm detailForm = (WorkflowForm) session.getAttribute(SessionKey.ACCOUNT_WORKFLOW_DETAIL);
    	form.setWorkflowId(detailForm.getWorkflowId());
    	form.setWorkflowName(detailForm.getWorkflowName());
//    	form.setWorkflowTypeCd(detailForm.getWorkflowTypeCd());
    	
        logger.info("initFilter()=> init.OK!");

        return form;

    }
    
    private void doSearch (Long accountId, Long workflowId, String workflowType, WorkflowSiteAssocFilterForm filterForm, WorkflowSiteAssocFilterResultForm resultForm) {
        resultForm.reset();

/*        Long workflowId = null;
        if (Utility.isSet(filterForm.getWorkflowId())) {
            workflowId = Long.valueOf(filterForm.getWorkflowId());
        }
*/      
        logger.info("findSites()==>doSearch()=> workflowId: " + workflowId + ", workflowType: " + workflowType);
        
        SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);

        criteria.setSiteId(parseNumberNN(filterForm.getSiteId()));
        criteria.setSiteName(filterForm.getSiteName(), filterForm.getSiteNameFilterType());
        criteria.setRefNumber(filterForm.getRefNumber(), filterForm.getRefNumberFilterType());
        criteria.setCity(filterForm.getCity(), Constants.FILTER_TYPE.START_WITH);
        criteria.setState(filterForm.getState(), Constants.FILTER_TYPE.START_WITH);
        criteria.setPostalCode(filterForm.getPostalCode(), Constants.FILTER_TYPE.START_WITH);
        criteria.setStoreId(getStoreId());
        criteria.setAccountIds(Utility.toList(accountId));
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
        
        criteria.setWorkflowId(workflowId);
        criteria.setWorkflowFilterType(workflowType);
        
        List<SiteListView> configured = siteService.findSitesByCriteria(criteria);
        
        criteria.setWorkflowId(null);
        List<SiteListView> sites = filterForm.getShowConfiguredOnly()
                           ? configured : siteService.findSitesByCriteria(criteria);

        logger.info("findSites()==>doSearch()=> sites: " + sites.size());
        logger.info("findSites()==>doSearch()=> configured: " + configured.size());

        SelectableObjects<SiteListView> selectableObj = new SelectableObjects<SiteListView>(
                                        sites,
                                        configured,
                                        AppComparator.SITE_LIST_VIEW_COMPARATOR);
        resultForm.setSites(selectableObj);

 
        WebSort.sort(resultForm, SiteListView.SITE_NAME);
    }

}
