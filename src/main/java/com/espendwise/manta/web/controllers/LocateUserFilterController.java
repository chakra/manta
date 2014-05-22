package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.UserListViewCriteria;
import com.espendwise.manta.web.forms.LocateUserFilterForm;
import com.espendwise.manta.web.forms.LocateUserFilterResultForm;
import com.espendwise.manta.web.forms.WebForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
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
@RequestMapping(UrlPathKey.LOCATE.LOCATE_USER)
@SessionAttributes({SessionKey.LOCATE_USER_FILTER_RESULT, SessionKey.LOCATE_USER_FILTER})
@AutoClean(SessionKey.LOCATE_USER_FILTER_RESULT)
public class LocateUserFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateUserFilterController.class);

    private UserService userService;

    @Autowired
    public LocateUserFilterController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@ModelAttribute(SessionKey.LOCATE_USER_FILTER) LocateUserFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_USER_FILTER_RESULT) LocateUserFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(true);
        return "user/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@ModelAttribute(SessionKey.LOCATE_USER_FILTER) LocateUserFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_USER_FILTER_RESULT) LocateUserFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(false);
        return "user/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findUsers(WebRequest request,
                               @ModelAttribute(SessionKey.LOCATE_USER_FILTER_RESULT) LocateUserFilterResultForm form,
                               @ModelAttribute(SessionKey.LOCATE_USER_FILTER) LocateUserFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "user/locate";
        }

        form.reset();

        UserListViewCriteria criteria = new UserListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.USER);

        if (Utility.isSet(filterForm.getUserId())) {
//            criteria.setUserName(filterForm.getUserId());
            criteria.setUserId(filterForm.getUserId());
//            criteria.setUserNameFilterType(Constants.FILTER_TYPE.ID);
        }
        if (Utility.isSet(filterForm.getUserName())) {
            criteria.setUserName(filterForm.getUserName());
            criteria.setUserNameFilterType(filterForm.getUserNameFilterType());
        }

//        criteria.setDistrRefNumber(filterForm.getDistrRefNumber());
//        criteria.setDistrRefNumberFilterType(filterForm.getDistrRefNumberFilterType());
        criteria.setStoreId(getStoreId());
        criteria.setFirstName(filterForm.getFirstName());
        criteria.setLastName(filterForm.getLastName());
        criteria.setUserType(filterForm.getUserType())  ;
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
//        criteria.setUsersGroups(Utility.splitLong(filterForm.getFilteredUsersGroups()));

        List<UserListView> users = userService.findUsersByCriteria(criteria);

        SelectableObjects<UserListView> selectableObj = new SelectableObjects<UserListView>(
                users,
                new ArrayList<UserListView>(),
                AppComparator.USER_LIST_VIEW_COMPARATOR
        );
        form.setSelectedUsers(selectableObj);

        WebSort.sort(form, UserListView.USER_NAME);

        return "user/locate";

    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @ModelAttribute(SessionKey.LOCATE_USER_FILTER_RESULT) LocateUserFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<UserListView> selected = filterResultForm.getSelectedUsers().getSelected();
        logger.info("returnSelected()=>  selected:" + selected);

        if(!filterResultForm.getMultiSelected()){
            selected = Utility.toList(selected.get(0));
            logger.info("returnSelected()=> SINGLE: selected:" + selected);
        }

        if (Utility.isSet(filter)) {

            List<String> filterKeys = Arrays.asList(Utility.split(filter, "."));

            WebForm targetForm = (WebForm) session.getAttribute(filterKeys.get(0));
            logger.info("returnSelected()=> BEGIN, targetForm:" + targetForm);

            Method method = BeanUtils.findMethod(targetForm.getClass(),
                    Utility.javaBeanPath(filterKeys.subList(1, filterKeys.size()).toArray(new String[filterKeys.size() - 1])),
                    List.class);

            logger.info("returnSelected()=> method:" + method);


            method.invoke(targetForm, selected);
        }

        logger.info("returnSelected()=> END.");

        return new Gson().toJson(selected);
    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.LOCATE_USER_FILTER_RESULT) LocateUserFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "user/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_USER_FILTER_RESULT)
    public LocateUserFilterResultForm init(HttpSession session) {

        LocateUserFilterResultForm locateUserFilterResult = (LocateUserFilterResultForm) session.getAttribute(SessionKey.LOCATE_USER_FILTER_RESULT);

        if (locateUserFilterResult == null) {
            locateUserFilterResult = new LocateUserFilterResultForm();
        }

        return locateUserFilterResult;

    }

    @ModelAttribute(SessionKey.LOCATE_USER_FILTER)
    public LocateUserFilterForm initFilter(HttpSession session) {
        LocateUserFilterForm locateUserFilter = (LocateUserFilterForm) session.getAttribute(SessionKey.LOCATE_USER_FILTER);
        return initFilter(locateUserFilter, false);
    }

    private LocateUserFilterForm initFilter(LocateUserFilterForm locateUserFilter, boolean init) {

        if (locateUserFilter == null) {
            locateUserFilter = new LocateUserFilterForm();
        }

        // init at once
        if (init && !locateUserFilter.isInitialized()) {
            locateUserFilter.initialize();
        }


        return locateUserFilter;
    }


}


