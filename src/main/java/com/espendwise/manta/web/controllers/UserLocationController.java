package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.web.forms.UserLocationFilterForm;
import com.espendwise.manta.web.forms.UserLocationFilterResultForm;
import com.espendwise.manta.web.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(UrlPathKey.USER.LOCATION)
@SessionAttributes({SessionKey.USER_LOCATION_FILTER, SessionKey.USER_LOCATION_FILTER_RESULT})
@AutoClean(SessionKey.USER_LOCATION_FILTER)
public class UserLocationController extends BaseController {

    private static final Logger logger = Logger.getLogger(UserLocationController.class);

    private UserService userService;
    private SiteService siteService;

    @Autowired
    public UserLocationController(UserService userService,
                                  SiteService siteService) {
        this.userService = userService;
        this.siteService = siteService;
    }

    @InitBinder
    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.USER_LOCATION_FILTER) UserLocationFilterForm filterForm) {
        return "user/location";
    }
    
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findSites(WebRequest request,
                            @ModelAttribute(SessionKey.USER_LOCATION_FILTER) UserLocationFilterForm filterForm,
                            @ModelAttribute(SessionKey.USER_LOCATION_FILTER_RESULT) UserLocationFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "user/location";
        }

        resultForm.reset();
        
        Long siteId = null;
        if (Utility.isSet(filterForm.getSiteId())) {
            siteId = Long.valueOf(filterForm.getSiteId());
        }
        SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);
        criteria.setUserId(filterForm.getUserId());
        criteria.setSiteId(siteId);
        criteria.setActiveOnly(!filterForm.getShowInactive());
        criteria.setSiteNameFilterType(filterForm.getSiteNameFilterType());
        criteria.setSiteName(filterForm.getSiteName());
        criteria.setRefNumber(filterForm.getReferenceNumber(), filterForm.getRefNumberFilterType());
        criteria.setCity(filterForm.getCity(), Constants.FILTER_TYPE.CONTAINS);
        criteria.setState(filterForm.getStateProvince(), Constants.FILTER_TYPE.CONTAINS);
        criteria.setPostalCode(filterForm.getPostalCode(), Constants.FILTER_TYPE.START_WITH);
        criteria.setUseUserToAccountAssoc(true);
        criteria.setConfiguredOnly(true);
        
        List<SiteListView> configured = userService.findUserSitesByCriteria(criteria);
        
        criteria.setConfiguredOnly(false);

        List<SiteListView> sites = filterForm.getShowConfiguredOnly()
                            ? configured : userService.findUserSitesByCriteria(criteria);

        logger.info("findSites()=> sites: " + sites.size());
        logger.info("findSites()=> configured: " + configured.size());

        SelectableObjects<SiteListView> selectableObj = new SelectableObjects<SiteListView>(
                                        sites,
                                        configured,
                                        AppComparator.SITE_LIST_VIEW_COMPARATOR);
        resultForm.setSites(selectableObj);
        
        List<PropertyData> properties = userService.findUserProperty(filterForm.getUserId(), RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_SITE);
        if (Utility.isSet(properties)) {
            resultForm.setDefaultLocation(properties.get(0).getValue());
        }

        WebSort.sort(resultForm, SiteListView.SITE_NAME);

        return "user/location";

    }

    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@PathVariable Long userId, 
                         @ModelAttribute(SessionKey.USER_LOCATION_FILTER_RESULT) UserLocationFilterResultForm resultForm) throws Exception {

        logger.info("user/location/update()=> BEGIN");
        Long storeId = getStoreId();

        logger.info("user/location/update()=> userId: " + userId);
        logger.info("user/location/update()=> storeId: " + storeId);
        
        List<SiteListView> selected = resultForm.getSites().getSelected();
        List<SiteListView> deselected = resultForm.getSites().getNewlyDeselected();
        
        userService.configureUserSitesList(userId,
                                           storeId,
                                           selected,
                                           deselected);
        
        userService.configureUserProperty(userId, RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_SITE, resultForm.getDefaultLocation());

        logger.info("user/location/update()=> END.");
        
        return "user/location";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.USER_LOCATION_FILTER_RESULT) UserLocationFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "user/location";
    }
    

    @ModelAttribute(SessionKey.USER_LOCATION_FILTER_RESULT)
    public UserLocationFilterResultForm init(HttpSession session) {

        logger.info("init()=> init....");

        UserLocationFilterResultForm form = (UserLocationFilterResultForm) session.getAttribute(SessionKey.USER_LOCATION_FILTER_RESULT);

        logger.info("init()=> form: " + form);        

        if (form == null) {
            form = new UserLocationFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("init()=> init.OK!");

        return form;

    }
    
    @ModelAttribute(SessionKey.USER_LOCATION_FILTER)
    public UserLocationFilterForm initFilter(HttpSession session, @PathVariable Long userId) {

        logger.info("initFilter()=> init....");

        UserLocationFilterForm form = (UserLocationFilterForm) session.getAttribute(SessionKey.USER_LOCATION_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new UserLocationFilterForm(userId);
            form.initialize();
        }

        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }

}
