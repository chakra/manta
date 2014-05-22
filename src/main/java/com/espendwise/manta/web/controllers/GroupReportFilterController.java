package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.GroupReportListView;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.ReportSearchCriteria;
import com.espendwise.manta.web.forms.GroupReportFilterForm;
import com.espendwise.manta.web.forms.GroupReportFilterResultForm;
import com.espendwise.manta.web.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(UrlPathKey.GROUP.REPORT)
@SessionAttributes({SessionKey.GROUP_REPORT_FILTER_RESULT, SessionKey.GROUP_REPORT_FILTER})
@AutoClean(SessionKey.GROUP_REPORT_FILTER_RESULT)
public class GroupReportFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(GroupReportFilterController.class);

    private GroupService groupService;

    @Autowired
    public GroupReportFilterController(GroupService reportService) {
        this.groupService = reportService;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.GROUP_REPORT_FILTER) GroupReportFilterForm filterForm) {
        return "group/report";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findGroup(WebRequest request,
                            @ModelAttribute(SessionKey.GROUP_REPORT_FILTER_RESULT) GroupReportFilterResultForm resultForm,
                            @ModelAttribute(SessionKey.GROUP_REPORT_FILTER) GroupReportFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "group/report";
        }
        doSearch(filterForm, resultForm);
        
        return "group/report";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.GROUP_REPORT_FILTER_RESULT) GroupReportFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "group/report";
    }

    @ModelAttribute(SessionKey.GROUP_REPORT_FILTER_RESULT)
    public GroupReportFilterResultForm init(HttpSession session) {

    	GroupReportFilterResultForm groupReportFilterResult = (GroupReportFilterResultForm) session.getAttribute(SessionKey.GROUP_REPORT_FILTER_RESULT);

        if (groupReportFilterResult == null) {
        	groupReportFilterResult = new GroupReportFilterResultForm();
        }

        return groupReportFilterResult;

    }

    @ModelAttribute(SessionKey.GROUP_REPORT_FILTER)
    public GroupReportFilterForm initFilter(HttpSession session, @PathVariable Long groupId) {

    	GroupReportFilterForm groupReportFilter = (GroupReportFilterForm) session.getAttribute(SessionKey.GROUP_REPORT_FILTER);

        if (groupReportFilter == null || !groupReportFilter.isInitialized()) {
            groupReportFilter = new GroupReportFilterForm(groupId);
            groupReportFilter.initialize();
        }

        return groupReportFilter;

    }
    
    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@PathVariable(IdPathKey.GROUP_ID) Long groupId,
                         @ModelAttribute(SessionKey.GROUP_REPORT_FILTER) GroupReportFilterForm filterForm,
                         @ModelAttribute(SessionKey.GROUP_REPORT_FILTER_RESULT) GroupReportFilterResultForm resultForm) throws Exception {

        logger.info("update()=> BEGIN");
        logger.info("update()=> groupId: " + groupId);

        List<GroupReportListView> selected =  resultForm.getGroupReports().getNewlySelected();
        List<GroupReportListView> deSelected =  resultForm.getGroupReports().getNewlyDeselected();

        if (!selected.isEmpty() || !deSelected.isEmpty()){
        	groupService.configureGroupAssocioation(groupId, Utility.toIds(selected), Utility.toIds(deSelected), RefCodeNames.GROUP_ASSOC_CD.REPORT_OF_GROUP);
        }
        
        doSearch(filterForm, resultForm);

        logger.info("update()=> END.");
        
        return "group/report";

    }
    
    private void doSearch (GroupReportFilterForm filterForm, GroupReportFilterResultForm resultForm) {
    	resultForm.reset();

        ReportSearchCriteria criteria = new ReportSearchCriteria();

        criteria.setGroupId(filterForm.getGroupId());
        criteria.setUserId(getUserId());
        criteria.setReportId(parseNumberNN(filterForm.getReportId()));
        criteria.setReportName(filterForm.getReportName());
        criteria.setReportNameMatchType(filterForm.getReportNameFilterType());
        criteria.setShowConfiguredOnly(filterForm.getShowConfiguredOnly());
        List<GroupReportListView> reports = groupService.findGroupReportViewsByCriteria(criteria);
        if (!filterForm.getShowConfiguredOnly()){
        	criteria.setShowConfiguredOnly(true);
        }
        List<GroupReportListView> configuredReports = groupService.findGroupReportViewsByCriteria(criteria);            
        
        SelectableObjects<GroupReportListView> selableobj = new SelectableObjects<GroupReportListView>(
        		reports,
        		configuredReports,
                AppComparator.GROUP_REPORT_LIST_VIEW_COMPARATOR
        );
                
        resultForm.setGroupReports(selableobj);
        WebSort.sort(resultForm, GroupReportListView.REPORT_NAME);
    }

}


