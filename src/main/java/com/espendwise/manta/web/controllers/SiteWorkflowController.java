package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.SiteWorkflowData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.view.SiteWorkflowListView;
import com.espendwise.manta.service.WorkflowService;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.WorkflowForm;
import com.espendwise.manta.web.forms.SiteWorkflowForm;
import com.espendwise.manta.web.forms.WorkflowSiteAssocFilterForm;
import com.espendwise.manta.web.forms.WorkflowSiteAssocFilterResultForm;
import com.espendwise.manta.web.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.ui.Model;
import com.espendwise.manta.model.view.SiteHeaderView;
import com.espendwise.manta.model.view.SiteListView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping(UrlPathKey.SITE.WORKFLOW)
@SessionAttributes(SessionKey.SITE_WORKFLOW)
@AutoClean(SessionKey.SITE_WORKFLOW)
public class SiteWorkflowController extends BaseController {

    private static final Logger logger = Logger.getLogger(SiteWorkflowController.class);

    private WorkflowService workflowService;
     private SiteService siteService;

    @Autowired
    public SiteWorkflowController(WorkflowService workflowService, SiteService siteService) {
        this.workflowService = workflowService;
        this.siteService = siteService;
    }


    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String initShow( HttpServletRequest request, 
                            @ModelAttribute(SessionKey.SITE_WORKFLOW) SiteWorkflowForm form,
                            @PathVariable(IdPathKey.LOCATION_ID) Long siteId,
                            Model model) {
        logger.info("initShow()=> BEGIN " );

        SiteHeaderView location = siteService.findSiteHeader(getStoreId(), siteId);

        List<SiteWorkflowListView> workflows =
            workflowService.findWorkflowCollection(location.getAccountId(), siteId, null);
        //------------search Groups---------------------------
        List<GroupData> groups = WorkflowUtil.getGroups(getAppUser(), getStoreId());
        form.setUserGroupMap(WorkflowUtil.toMap(groups));
        //------------search Apply/Skip Associations---------------------------
        List<WorkflowAssocData> associations = workflowService.findWorkflowAssocCollection(null, null, null);
        form.setApplySkipGroupMap(WorkflowUtil.toMap(associations, form.getUserGroupMap()));
        List<SiteWorkflowData> assigns = workflowService.findWorkflowToSitesAssoc(null, siteId, null);
        if (assigns != null && assigns.size() > 0) {
            SiteWorkflowData sd = assigns.get(0);
            form.setSelectedWorkflow(""+sd.getWorkflowId());
        }

        WebSort.sort(workflows, SiteWorkflowListView.WORKFLOW_NAME, new Boolean(true));
        form.setWorkflows(workflows);

        model.addAttribute(SessionKey.SITE_WORKFLOW, form);
 
        logger.info("initShow()=> END " );
        return "site/workflow";
    }


    @ModelAttribute(SessionKey.SITE_WORKFLOW)
    public SiteWorkflowForm initModel(@PathVariable(IdPathKey.LOCATION_ID) Long siteId) {
        logger.info("initModel()=> BEGIN " );
        SiteWorkflowForm form = new SiteWorkflowForm(siteId);
        form.initialize();
        logger.info("initModel()=> END " );
        return form;

    }
    @RequestMapping(value = "/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.SITE_WORKFLOW) SiteWorkflowForm form, @PathVariable String field) throws Exception {
        logger.info("sort()=> BEGIN " );
        WebSort.sort(form, field);
        logger.info("sort()=> END " );
        return "site/workflow";
    }

    @SuccessMessage
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String update(WebRequest request,
    			@ModelAttribute(SessionKey.SITE_WORKFLOW) SiteWorkflowForm form) throws Exception {

        logger.info("update()=> BEGIN");

        logger.info("update()=> set assign workflowId: " + form.getSelectedWorkflow() + " for site: " + form.getSiteId());

        Long workflowId = new Long(0);
        try {
            workflowId = Long.parseLong(form.getSelectedWorkflow());
        } catch (Exception e) {
        }
        String workflowType = null;
        if (workflowId > 0) {
            for (SiteWorkflowListView workflow : form.getWorkflows()) {
                if (workflow.getWorkflowId() == workflowId) {
                    workflowType = workflow.getType();
                    break;
                }
            }
        }

        workflowService.assignSiteWorkflow(form.getSiteId(), workflowId, workflowType);

        logger.info("update()=> END.");

        return "site/workflow";

    }


    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public String clear(WebRequest request, @ModelAttribute(SessionKey.SITE_WORKFLOW) SiteWorkflowForm form) throws Exception {
         form.reset();
         return "site/workflow" ;
    }

}
