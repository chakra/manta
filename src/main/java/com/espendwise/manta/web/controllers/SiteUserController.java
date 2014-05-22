package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.PropertyService;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.UserListViewCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.SiteUserFilterForm;
import com.espendwise.manta.web.forms.SiteUserFilterResultForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping(UrlPathKey.SITE.USERS)
@SessionAttributes({SessionKey.SITE_USER_FILTER, SessionKey.SITE_USER_FILTER_RESULT})
@AutoClean(SessionKey.SITE_USER_FILTER)
public class SiteUserController extends BaseController {

    private static final Logger logger = Logger.getLogger(SiteUserController.class);

    private UserService userService;
    private SiteService siteService;
    private PropertyService propertyService;

    @Autowired
    public SiteUserController(UserService userService,
                              SiteService siteService,
                              PropertyService propertyService) {
        this.userService = userService;
        this.siteService = siteService;
        this.propertyService = propertyService;
    }

    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        /*       webErrors.putErrors(ex, new SiteWebUpdateExceptionResolver());*/

        return "site/users";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "site/users";
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.SITE_USER_FILTER) SiteUserFilterForm filterForm) {
        return "site/users";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findUsers(WebRequest request,
                            @ModelAttribute(SessionKey.SITE_USER_FILTER) SiteUserFilterForm filterForm,
                            @ModelAttribute(SessionKey.SITE_USER_FILTER_RESULT) SiteUserFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "site/users";
        }

        doSearch(filterForm, resultForm);

        return "site/users";

    }

    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@PathVariable(IdPathKey.LOCATION_ID) Long locationId,
                         @ModelAttribute(SessionKey.SITE_USER_FILTER) SiteUserFilterForm filterForm,
                         @ModelAttribute(SessionKey.SITE_USER_FILTER_RESULT) SiteUserFilterResultForm resultForm) throws Exception {

        logger.info("site/users/update()=> BEGIN");
        Long storeId = getStoreId();

        logger.info("site/users/update()=> siteId: " + locationId);
        logger.info("site/users/update()=> storeId: " + storeId);
        
        List<UserListView> selected = resultForm.getUsers().getSelected();
        List<UserListView> deselected = resultForm.getUsers().getNewlyDeselected();
        
        siteService.configureSiteUsersList(locationId,
                                           storeId,
                                           selected,
                                           deselected);
        
        propertyService.configureEntityProperty(locationId, RefCodeNames.PROPERTY_TYPE_CD.SITE_PRIMARY_USER, resultForm.getPrimaryUser());

        doSearch(filterForm, resultForm);

        logger.info("site/users/update()=> END.");
        
        return "site/users";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.SITE_USER_FILTER_RESULT) SiteUserFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "site/users";
    }

    @ModelAttribute(SessionKey.SITE_USER_FILTER_RESULT)
    public SiteUserFilterResultForm initFilterResult(HttpSession session) {

        logger.info("initFilterResult()=> init....");

        SiteUserFilterResultForm form = (SiteUserFilterResultForm) session.getAttribute(SessionKey.SITE_USER_FILTER_RESULT);

        logger.info("initFilterResult()=> form: " + form);

        if (form == null) {
            form = new SiteUserFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("initFilterResult()=> init.OK!");

        return form;

    }

    @ModelAttribute(SessionKey.SITE_USER_FILTER_RESULT)
    public SiteUserFilterResultForm init(HttpSession session) {

        logger.info("initFilterResult()=> init....");

        SiteUserFilterResultForm form = (SiteUserFilterResultForm) session.getAttribute(SessionKey.SITE_USER_FILTER_RESULT);

        logger.info("initFilterResult()=> form: " + form);

        if (form == null) {
            form = new SiteUserFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("initFilterResult()=> init.OK!");

        return form;

    }  

    @ModelAttribute(SessionKey.SITE_USER_FILTER)
    public SiteUserFilterForm initFilter(HttpSession session, @PathVariable(IdPathKey.LOCATION_ID) Long locationId) {

        logger.info("initFilter()=> init....");

        SiteUserFilterForm form = (SiteUserFilterForm) session.getAttribute(SessionKey.SITE_USER_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new SiteUserFilterForm(locationId);
            form.initialize();
        }


        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }
    
    private void doSearch (SiteUserFilterForm filterForm, SiteUserFilterResultForm resultForm) {
        resultForm.reset();

        Long siteId = null;
        if (Utility.isSet(filterForm.getSiteId())) {
            siteId = Long.valueOf(filterForm.getSiteId());
        }
        
        UserListViewCriteria criteria = new UserListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.USER);
        criteria.setUserId(filterForm.getUserId());
        criteria.setUserName(filterForm.getUserName());
        criteria.setSiteId(siteId);
        criteria.setActiveOnly(!filterForm.getShowInactive());
        criteria.setUserNameFilterType(filterForm.getUserNameFilterType());
        
        BusEntityAssocData accountAssoc = siteService.findSiteAccountAssoc(siteId);
        if (accountAssoc != null) {
            criteria.setAccountFilter(Utility.toList(accountAssoc.getBusEntity2Id()));
        }
        
        List<UserListView> configured = userService.findUsersByCriteria(criteria);
        
        criteria.setSiteId(null);

        List<UserListView> users = filterForm.getShowConfiguredOnly()
                           ? configured : userService.findUsersByCriteria(criteria);

        logger.info("findUsers()=> user: " + users.size());
        logger.info("findUsers()=> configured: " + configured.size());

        SelectableObjects<UserListView> selectableObj = new SelectableObjects<UserListView>(
                                        users,
                                        configured,
                                        AppComparator.USER_LIST_VIEW_COMPARATOR);
        resultForm.setUsers(selectableObj);

        List<PropertyData> properties = propertyService.findEntityPropertyValues(siteId, RefCodeNames.PROPERTY_TYPE_CD.SITE_PRIMARY_USER);
        
        resultForm.setPrimaryUser(PropertyUtil.toFirstValue(PropertyUtil.findActiveP(properties)));

        WebSort.sort(resultForm, UserListView.USER_NAME);
    }

}
