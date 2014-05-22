package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.service.StoreService;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.DbConstantResource;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.RefCodeNamesKeys;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.criteria.UserListViewCriteria;
import com.espendwise.manta.web.forms.UserFilterForm;
import com.espendwise.manta.web.forms.UserFilterResultForm;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.USER.FILTER)
@SessionAttributes({SessionKey.USER_FILTER_RESULT, SessionKey.USER_FILTER})
@AutoClean(SessionKey.USER_FILTER_RESULT)
public class UserFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(UserFilterController.class);

    private UserService userService;
    private StoreService storeService;

    @Autowired
    public UserFilterController(UserService userService, StoreService storeService) {
        this.userService = userService;
        this.storeService = storeService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }   
    
    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String filter(@ModelAttribute(SessionKey.USER_FILTER) UserFilterForm filterForm) {
    	populateFormReferenceData(filterForm);
        return "user/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findUsers(WebRequest request,
                            @ModelAttribute(SessionKey.USER_FILTER_RESULT) UserFilterResultForm form,
                            @ModelAttribute(SessionKey.USER_FILTER) UserFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "user/filter";
        }

        form.reset();
        
        UserListViewCriteria criteria = new UserListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.USER);

        criteria.setUserName(filterForm.getUserLoginName());
        criteria.setUserNameFilterType(filterForm.getUserNameFilterType());
        criteria.setUserId(filterForm.getUserId());
        criteria.setAllowedUserTypes(Utility.toObject2(getAllowedUserTypes()));
        criteria.setLastName(filterForm.getLastName());
        criteria.setFirstName(filterForm.getFirstName());
        criteria.setUserType(filterForm.getUserType());
        criteria.setEmail(filterForm.getEmail());
        criteria.setEmailFilterType(filterForm.getEmailFilterType());
        criteria.setRole(filterForm.getUserRole());
        criteria.setLanguage(filterForm.getLanguage());
        criteria.setStoreId(getStoreId());
        criteria.setAccountFilter(Utility.splitLong(filterForm.getAccountFilter()));
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        List<UserListView> users = userService.findUsersByCriteria(criteria);
        
        toI18n(users);
                
        form.setUsers(users);

        WebSort.sort(form, UserListView.USER_NAME);

        return "user/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.USER_FILTER_RESULT) UserFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "user/filter";
    }

    @RequestMapping(value = "/filter/clear/account", method = RequestMethod.POST)
    public String clearAccountFilter(WebRequest request, @ModelAttribute(SessionKey.USER_FILTER) UserFilterForm form) throws Exception {
        form.setFilteredAccounts(Utility.emptyList(AccountListView.class));
        form.setAccountFilter(null);
        return redirect(request, UrlPathKey.USER.FILTER);
    }

    @ModelAttribute(SessionKey.USER_FILTER_RESULT)
    public UserFilterResultForm init(HttpSession session) {

        UserFilterResultForm userFilterResult = (UserFilterResultForm) session.getAttribute(SessionKey.USER_FILTER_RESULT);

        if (userFilterResult == null) {
            userFilterResult = new UserFilterResultForm();
        }
        
        return userFilterResult;

    }

    @ModelAttribute(SessionKey.USER_FILTER)
    public UserFilterForm initFilter(HttpSession session) {

        UserFilterForm userFilter = (UserFilterForm) session.getAttribute(SessionKey.USER_FILTER);

        if (userFilter == null || !userFilter.isInitialized()) {
            userFilter = new UserFilterForm();
            userFilter.initialize();
        }

        populateFormReferenceData(userFilter);
        return userFilter;

    }
    
    private void toI18n (List<UserListView> users) {

        DbConstantResource resource = AppResourceHolder.getAppResource().getDbConstantsResource();

        Map statuses =  RefCodeNamesKeys.toMap(resource.getBusEntityStatuseCds(), true);
        Map userTypes =  RefCodeNamesKeys.toMap(resource.getUserTypeCds(), true);
        
        if (Utility.isSet(users)) {

            String role;
            String type;
            String status;

            for (UserListView user : users) {

                role = user.getRole();
                type = user.getType();
                status = user.getStatus();

                if (Utility.strNN(role).contains(Constants.USER_ROLE.SERVICE_VENDOR_ROLE)) {
                    role = AppI18nUtil.getMessage("refcodes.USER_ROLE_CD.SERVICE_VENDOR");
                } else if (Utility.strNN(role).contains(Constants.USER_ROLE.SITE_MANAGER_ROLE)) {
                    role = AppI18nUtil.getMessage("refcodes.USER_ROLE_CD.SITE_MANAGER");
                } else {
                    role = AppI18nUtil.getMessage("refcodes.USER_ROLE_CD.NOT_SPECIFIED");
                }

                user.setRole(role);
                
                user.setType(AppI18nUtil.getMessageOrNull("refcodes.USER_TYPE_CD." + userTypes.get(type)));
                user.setStatus(AppI18nUtil.getMessageOrNull("refcodes.BUS_ENTITY_STATUS_CD." + statuses.get(status)));
            }
        }
    }

    public List<Pair<String, String>> getAllowedUserTypes() {
        return AppResourceHolder
                .getAppResource()
                .getDbConstantsResource()
                .getAllowedUserTypes(getAppUser().getUserTypeCd());
    }


    private void populateFormReferenceData(UserFilterForm filterForm) {
        filterForm.setAvailableLanguages(WebFormUtil.getStoreAvailableLanguages(storeService, getStoreId()));
		DbConstantResource dbResource = AppResourceHolder.getAppResource().getDbConstantsResource();
        List<Pair<String, String>> userRoleCds = dbResource.getUserRoleCds();
        List<Pair<String, String>> userRoleChoices = new ArrayList<Pair<String,String>>();
        Iterator<Pair<String, String>> userRoleCdIterator = userRoleCds.iterator();
        while (userRoleCdIterator.hasNext()) {
        	Pair<String, String> userRoleCd = userRoleCdIterator.next();
        	userRoleChoices.add(new Pair<String, String>(userRoleCd.getObject1(), new MessageI18nArgument("refcodes.USER_ROLE_CD.UI." + userRoleCd.getObject1()).resolve()));
        }
        Collections.sort(userRoleChoices, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                return Utility.strNN(o1.getObject2()).compareTo(Utility.strNN(o2.getObject2()));
            }
        });
        filterForm.setUserRoleChoices(userRoleChoices);
    }

}


