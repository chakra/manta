package com.espendwise.manta.web.controllers;

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

import com.espendwise.manta.model.view.GroupListView;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.web.forms.GroupFilterForm;
import com.espendwise.manta.web.forms.GroupFilterResultForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.GROUP.FILTER)
@SessionAttributes({SessionKey.GROUP_FILTER_RESULT, SessionKey.GROUP_FILTER})
@AutoClean(SessionKey.GROUP_FILTER_RESULT)
public class GroupFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(GroupFilterController.class);

    private GroupService groupService;

    @Autowired
    public GroupFilterController(GroupService groupService) {
        this.groupService = groupService;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.GROUP_FILTER) GroupFilterForm filterForm) {
        return "group/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findGroup(WebRequest request,
                            @ModelAttribute(SessionKey.GROUP_FILTER_RESULT) GroupFilterResultForm form,
                            @ModelAttribute(SessionKey.GROUP_FILTER) GroupFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "group/filter";
        }

        form.reset();

        GroupSearchCriteria criteria = new GroupSearchCriteria();

        criteria.setGroupId(parseNumberNN(filterForm.getGroupId()));
        criteria.setGroupName(filterForm.getGroupName());
        criteria.setGroupType(filterForm.getGroupType());
        criteria.setGroupNameMatchType(filterForm.getGroupNameFilterType());
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
        
        if (getAppUser().getIsStoreAdmin() || !Utility.isTrue(filterForm.getShowAll())) {
			criteria.setStoreIds(Utility.toList(getStoreId()));
		}else if (getAppUser().isAdmin() && Utility.isTrue(filterForm.getShowAll())){
			criteria.setUserId(getUserId());
		}
        
        filterForm.setAssocStoreName(getStoreName());
        try {
            List<GroupListView> groupList = groupService.findGroupViewsByCriteria(criteria);
            form.setGroups(groupList);
            WebSort.sort(form, GroupListView.GROUP_NAME);
        }
        catch (IllegalStateException ise) {
        	webErrors.putError("exception.web.error.illegalStateException", Args.i18nTyped(ise.getMessage()));
        }
        return "group/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.GROUP_FILTER_RESULT) GroupFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "group/filter";
    }

    @ModelAttribute(SessionKey.GROUP_FILTER_RESULT)
    public GroupFilterResultForm init(HttpSession session) {

    	GroupFilterResultForm groupFilterResult = (GroupFilterResultForm) session.getAttribute(SessionKey.GROUP_FILTER_RESULT);

        if (groupFilterResult == null) {
            groupFilterResult = new GroupFilterResultForm();
        }

        return groupFilterResult;

    }

    @ModelAttribute(SessionKey.GROUP_FILTER)
    public GroupFilterForm initFilter(HttpSession session) {

        GroupFilterForm groupFilter = (GroupFilterForm) session.getAttribute(SessionKey.GROUP_FILTER);

        if (groupFilter == null || !groupFilter.isInitialized()) {
            groupFilter = new GroupFilterForm();
            groupFilter.initialize();
        }

        return groupFilter;

    }

}


