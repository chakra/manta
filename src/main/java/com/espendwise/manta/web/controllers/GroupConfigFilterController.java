package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.GroupConfigAllListView;
import com.espendwise.manta.model.view.GroupConfigListView;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants.FILTER_RESULT_LIMIT;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.GroupConfigSearchCriteria;
import com.espendwise.manta.web.forms.GroupConfigAllFilterForm;
import com.espendwise.manta.web.forms.GroupConfigFilterForm;
import com.espendwise.manta.web.forms.GroupConfigFilterResultForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.GROUP.CONFIGURATION)
@SessionAttributes({SessionKey.GROUP_CONFIGURATION_FILTER_RESULT, SessionKey.GROUP_CONFIGURATION_FILTER, SessionKey.GROUP_CONFIGURATION_ALL_FILTER})
@AutoClean(SessionKey.GROUP_CONFIGURATION_FILTER_RESULT)
public class GroupConfigFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(GroupConfigFilterController.class);

    private GroupService groupService;

    @Autowired
    public GroupConfigFilterController(GroupService groupService) {
        this.groupService = groupService;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER) GroupConfigFilterForm filterForm,
    		@ModelAttribute(SessionKey.GROUP_CONFIGURATION_ALL_FILTER) GroupConfigAllFilterForm filterAllForm) {
        return "group/configuration";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findGroupConfigured(WebRequest request,
                            @ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER_RESULT) GroupConfigFilterResultForm resultForm,
                            @ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER) GroupConfigFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "group/configuration";
        }
        doSearch(filterForm, resultForm);
        
        return "group/configuration";

    }

    @RequestMapping(value = "/filterAllAssociations", method = RequestMethod.GET)
    public String findAllConfigured(WebRequest request,
                            @ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER_RESULT) GroupConfigFilterResultForm resultForm,
                            @ModelAttribute(SessionKey.GROUP_CONFIGURATION_ALL_FILTER) GroupConfigAllFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "group/configuration";
        }
        resultForm.setGroupAllEntityConfigs(null);
        String groupType = filterForm.getGroupType();
        GroupConfigSearchCriteria criteria = new GroupConfigSearchCriteria(filterForm.getGroupId(), groupType, getUserId(), FILTER_RESULT_LIMIT.GROUP_CONFIG);

        criteria.setSearchId(filterForm.getSearchId());
        criteria.setSearchName(filterForm.getSearchName());
        criteria.setSearchNameMatchType(filterForm.getSearchNameFilterType());
        criteria.setShowConfiguredOnly(true);
        criteria.setShowInactive(true);
        List<GroupConfigAllListView> result = groupService.findGroupConfigAllByCriteria(criteria);
        resultForm.setGroupAllEntityConfigs(result);
        
        sortAllAssociations(resultForm, "name");
        
        return "group/configuration";

    }
    
    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER_RESULT) GroupConfigFilterResultForm form, @PathVariable String field) throws Exception {
    	WebSort.sort(form, field);
    	return "group/configuration";
    }
    
    @RequestMapping(value = "/filterAllAssociations/sortby/{field}", method = RequestMethod.GET)
    public String sortAllAssociations(@ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER_RESULT) GroupConfigFilterResultForm form, @PathVariable String field) throws Exception {
    	List currentResult = form.getResult();
    	form.setResult(form.getGroupAllEntityConfigs());
    	if (field.equals("assocStoreName"))
    		WebSort.sort(form, field);
    	else
    		WebSort.sort(form, "groupConfigView."+field);
    	
    	form.setResult(currentResult);
    	return "group/configuration";
    }


    @ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER_RESULT)
    public GroupConfigFilterResultForm init(HttpSession session) {

    	GroupConfigFilterResultForm groupConfigFilterResult = (GroupConfigFilterResultForm) session.getAttribute(SessionKey.GROUP_CONFIGURATION_FILTER_RESULT);

        if (groupConfigFilterResult == null) {
        	groupConfigFilterResult = new GroupConfigFilterResultForm();
        }

        return groupConfigFilterResult;

    }

    @ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER)
    public GroupConfigFilterForm initFilter(HttpSession session, @PathVariable Long groupId) {

    	GroupConfigFilterForm groupConfigFilter = (GroupConfigFilterForm) session.getAttribute(SessionKey.GROUP_CONFIGURATION_FILTER);

        if (groupConfigFilter == null || !groupConfigFilter.isInitialized()) {
        	groupConfigFilter = new GroupConfigFilterForm(groupId);            
            groupConfigFilter.initialize();
        }

        return groupConfigFilter;

    }
    
    @ModelAttribute(SessionKey.GROUP_CONFIGURATION_ALL_FILTER)
    public GroupConfigFilterForm initFilterAll(HttpSession session, @PathVariable Long groupId) {

    	GroupConfigAllFilterForm groupConfigFilter = (GroupConfigAllFilterForm) session.getAttribute(SessionKey.GROUP_CONFIGURATION_ALL_FILTER);

        if (groupConfigFilter == null || !groupConfigFilter.isInitialized()) {
        	groupConfigFilter = new GroupConfigAllFilterForm(groupId);            
            groupConfigFilter.initialize();
        }

        return groupConfigFilter;

    }
    
    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@PathVariable(IdPathKey.GROUP_ID) Long groupId,
                         @ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER) GroupConfigFilterForm filterForm,
                         @ModelAttribute(SessionKey.GROUP_CONFIGURATION_FILTER_RESULT) GroupConfigFilterResultForm resultForm) throws Exception {

        logger.info("update()=> BEGIN");
        logger.info("update()=> groupId: " + groupId);

        List<Long> selected = new ArrayList<Long>();
        List<Long> deSelected = new ArrayList<Long>();        
        
    	for (GroupConfigListView config : resultForm.getGroupConfigs().getNewlySelected()){
    		selected.add(config.getId());
    	}
    	
    	for (GroupConfigListView config : resultForm.getGroupConfigs().getNewlyDeselected()){
    		deSelected.add(config.getId());
    	}
        
        if (!selected.isEmpty() || !deSelected.isEmpty()){
        	groupService.configureGroupAssocioation(groupId, selected, deSelected, filterForm.getGroupAssocCd());
        }

        doSearch(filterForm, resultForm);

        logger.info("update()=> END.");
        
        return "group/configuration";

    }
    
    private void doSearch (GroupConfigFilterForm filterForm, GroupConfigFilterResultForm resultForm) {
    	resultForm.reset();
    	
    	GroupConfigSearchCriteria criteria = new GroupConfigSearchCriteria(filterForm.getGroupId(), filterForm.getGroupType(), getUserId(), FILTER_RESULT_LIMIT.GROUP_CONFIG);

        criteria.setSearchId(filterForm.getSearchId());
        criteria.setStoreId(getStoreId());
        criteria.setSearchName(filterForm.getSearchName());
        criteria.setSearchNameMatchType(filterForm.getSearchNameFilterType());
        criteria.setShowConfiguredOnly(filterForm.getShowConfiguredOnly());
        criteria.setShowInactive(filterForm.isShowInactive());
        List<GroupConfigListView> result = groupService.findGroupConfigByCriteria(criteria);
        if (!filterForm.getShowConfiguredOnly()){
        	criteria.setShowConfiguredOnly(true);
        }
        
        List<GroupConfigListView> configuredResults = groupService.findGroupConfigByCriteria(criteria);            
        
        SelectableObjects<GroupConfigListView> selectableConfigs = new SelectableObjects<GroupConfigListView>(
        		result, configuredResults, AppComparator.GROUP_CONFIG_LIST_VIEW_COMPARATOR);
        resultForm.setGroupConfigs(selectableConfigs);
    	WebSort.sort(resultForm, "name");
    }

}


