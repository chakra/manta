package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.espendwise.manta.model.view.GroupFunctionListView;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.ApplicationFunctionSearchCriteria;
import com.espendwise.manta.web.forms.GroupFunctionFilterForm;
import com.espendwise.manta.web.forms.GroupFunctionFilterResultForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.GROUP.FUNCTION)
@SessionAttributes({SessionKey.GROUP_FUNCTION_FILTER_RESULT, SessionKey.GROUP_FUNCTION_FILTER})
@AutoClean(SessionKey.GROUP_FUNCTION_FILTER_RESULT)
public class GroupFunctionFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(GroupFunctionFilterController.class);

    private GroupService groupService;

    @Autowired
    public GroupFunctionFilterController(GroupService groupService) {
        this.groupService = groupService;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.GROUP_FUNCTION_FILTER) GroupFunctionFilterForm filterForm) {
        return "group/function";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findGroup(WebRequest request,
                            @ModelAttribute(SessionKey.GROUP_FUNCTION_FILTER_RESULT) GroupFunctionFilterResultForm resultForm,
                            @ModelAttribute(SessionKey.GROUP_FUNCTION_FILTER) GroupFunctionFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "group/function";
        }
        doSearch(filterForm, resultForm);
        
        return "group/function";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.GROUP_FUNCTION_FILTER_RESULT) GroupFunctionFilterResultForm form, @PathVariable String field) throws Exception {
    	if (field.equals("SERVICES_ASSETS")){
    		field = "SERVICES/ASSETS";
    	}
    	form.setResult(form.getFunctionsByType().get(field).getSelectableObjects());  
    	WebSort.sort(form, "functionName");
    	return "group/function";
    }

    @ModelAttribute(SessionKey.GROUP_FUNCTION_FILTER_RESULT)
    public GroupFunctionFilterResultForm init(HttpSession session) {

    	GroupFunctionFilterResultForm groupFunctionFilterResult = (GroupFunctionFilterResultForm) session.getAttribute(SessionKey.GROUP_FUNCTION_FILTER_RESULT);

        if (groupFunctionFilterResult == null) {
        	groupFunctionFilterResult = new GroupFunctionFilterResultForm();
        }

        return groupFunctionFilterResult;

    }

    @ModelAttribute(SessionKey.GROUP_FUNCTION_FILTER)
    public GroupFunctionFilterForm initFilter(HttpSession session, @PathVariable Long groupId) {

    	GroupFunctionFilterForm groupFunctionFilter = (GroupFunctionFilterForm) session.getAttribute(SessionKey.GROUP_FUNCTION_FILTER);

        if (groupFunctionFilter == null || !groupFunctionFilter.isInitialized()) {
            groupFunctionFilter = new GroupFunctionFilterForm(groupId);
            groupFunctionFilter.initialize();
        }

        return groupFunctionFilter;

    }
    
    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@PathVariable(IdPathKey.GROUP_ID) Long groupId,
                         @ModelAttribute(SessionKey.GROUP_FUNCTION_FILTER) GroupFunctionFilterForm filterForm,
                         @ModelAttribute(SessionKey.GROUP_FUNCTION_FILTER_RESULT) GroupFunctionFilterResultForm resultForm) throws Exception {

        logger.info("update()=> BEGIN");
        logger.info("update()=> groupId: " + groupId);

        List<String> selected = new ArrayList<String>();
        List<String> deSelected = new ArrayList<String>();
        
        for (SelectableObjects<GroupFunctionListView> objs : resultForm.getFunctionsByType().values()){
        	for (GroupFunctionListView function : objs.getNewlySelected()){
        		selected.add(function.getFunctionName());
        	}
        	
        	for (GroupFunctionListView function : objs.getNewlyDeselected()){
        		deSelected.add(function.getFunctionName());
        	}
        }
        
        if (!selected.isEmpty() || !deSelected.isEmpty()){
        	groupService.configureGroupAssocioation(groupId, selected, deSelected, RefCodeNames.GROUP_ASSOC_CD.FUNCTION_OF_GROUP);
        }

        doSearch(filterForm, resultForm);

        logger.info("update()=> END.");
        
        return "group/function";

    }
    
    private void doSearch (GroupFunctionFilterForm filterForm, GroupFunctionFilterResultForm resultForm) {
    	resultForm.reset();
    	ApplicationFunctionSearchCriteria criteria = new ApplicationFunctionSearchCriteria();

        criteria.setGroupId(filterForm.getGroupId());
        criteria.setUserId(getUserId());
        criteria.setFunctionName(filterForm.getFunctionName());
        criteria.setFunctionType(filterForm.getFunctionType());
        criteria.setFunctionNameMatchType(filterForm.getFunctionNameFilterType());
        criteria.setShowConfiguredOnly(filterForm.getShowConfiguredOnly());
        Map<String,List<GroupFunctionListView>> functions = groupService.findGroupFunctionMapByCriteria(criteria);
        if (!filterForm.getShowConfiguredOnly()){
        	criteria.setShowConfiguredOnly(true);
        }
        Map<String,List<GroupFunctionListView>> configuredFunctions = groupService.findGroupFunctionMapByCriteria(criteria);            
        
        Map<String,SelectableObjects<GroupFunctionListView>> functionsByType = new TreeMap<String,SelectableObjects<GroupFunctionListView>>();
        for (String key : functions.keySet()){
        	List<GroupFunctionListView> functionNames = functions.get(key);
        	List<GroupFunctionListView> functionNamesConfigured = configuredFunctions.get(key);
        	if (functionNamesConfigured == null){
        		functionNamesConfigured = Utility.emptyList(GroupFunctionListView.class);
        	}
        		
        	SelectableObjects<GroupFunctionListView> selectableFunctions = new SelectableObjects<GroupFunctionListView>(
        			functionNames, functionNamesConfigured, AppComparator.GROUP_FUNCTION_LIST_VIEW_COMPARATOR);
        	functionsByType.put(key, selectableFunctions); 
        	resultForm.setResult(selectableFunctions.getSelectableObjects());
        	resultForm.setSortHistory(null);
        	WebSort.sort(resultForm, "functionName");
        }
                
        resultForm.setFunctionsByType(functionsByType);
    }

}


