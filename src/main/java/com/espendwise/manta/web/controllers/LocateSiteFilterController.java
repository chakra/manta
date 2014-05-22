package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.web.forms.LocateSiteFilterForm;
import com.espendwise.manta.web.forms.LocateSiteFilterResultForm;
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
@RequestMapping(UrlPathKey.LOCATE.LOCATE_SITE)
@SessionAttributes({SessionKey.LOCATE_SITE_FILTER_RESULT, SessionKey.LOCATE_SITE_FILTER})
@AutoClean(SessionKey.LOCATE_SITE_FILTER_RESULT)
public class LocateSiteFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateSiteFilterController.class);

    private SiteService siteService;

    @Autowired
    public LocateSiteFilterController(SiteService siteService) {
        this.siteService = siteService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@ModelAttribute(SessionKey.LOCATE_SITE_FILTER) LocateSiteFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_SITE_FILTER_RESULT) LocateSiteFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(true);
        return "site/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@ModelAttribute(SessionKey.LOCATE_SITE_FILTER) LocateSiteFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_SITE_FILTER_RESULT) LocateSiteFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(false);
        return "site/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findSites(WebRequest request,
                            @ModelAttribute(SessionKey.LOCATE_SITE_FILTER_RESULT) LocateSiteFilterResultForm form,
                            @ModelAttribute(SessionKey.LOCATE_SITE_FILTER) LocateSiteFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "site/locate";
        }

        form.reset();     

        SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);

        if (Utility.isSet(filterForm.getLocationId())) {
            criteria.setSiteId(Long.valueOf(filterForm.getLocationId()));
        }
        criteria.setSiteNameFilterType(filterForm.getSiteNameFilterType());
        criteria.setSiteName(filterForm.getSiteName());
        criteria.setRefNumberFilterType(filterForm.getReferenceNumberFilterType());
        criteria.setRefNumber(filterForm.getReferenceNumber());
        criteria.setCity(filterForm.getCity(), Constants.FILTER_TYPE.CONTAINS);
        criteria.setState(filterForm.getStateProvince(), Constants.FILTER_TYPE.CONTAINS);
        criteria.setPostalCode(filterForm.getPostalCode(), Constants.FILTER_TYPE.CONTAINS);
        criteria.setActiveOnly(!filterForm.getShowInactive());
        
        List<SiteListView> sites = siteService.findSitesByCriteria(criteria);

        logger.info("findSites()=> sites: " + sites.size());

        SelectableObjects<SiteListView> selectableObj = new SelectableObjects<SiteListView>(
                                        sites,
                                        new ArrayList<SiteListView>(),
                                        AppComparator.SITE_LIST_VIEW_COMPARATOR);
        form.setSelectedSites(selectableObj);

        WebSort.sort(form, SiteListView.SITE_NAME);

        return "site/locate";
    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @ModelAttribute(SessionKey.LOCATE_SITE_FILTER_RESULT) LocateSiteFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<SiteListView> selected = filterResultForm.getSelectedSites().getSelected();
        if(!filterResultForm.getMultiSelected()){
            selected = Utility.toList(selected.get(0));
        }

        if (Utility.isSet(filter)) {

            List<String> filterKeys = Arrays.asList(Utility.split(filter, "."));

            WebForm targetForm = (WebForm) session.getAttribute(filterKeys.get(0));

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
    public String sort(@ModelAttribute(SessionKey.LOCATE_SITE_FILTER_RESULT) LocateSiteFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "site/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_SITE_FILTER_RESULT)
    public LocateSiteFilterResultForm init(HttpSession session) {

        LocateSiteFilterResultForm locateSiteFilterResult = (LocateSiteFilterResultForm) session.getAttribute(SessionKey.LOCATE_SITE_FILTER_RESULT);

        if (locateSiteFilterResult == null) {
            locateSiteFilterResult = new LocateSiteFilterResultForm();
        }

        return locateSiteFilterResult;

    }

    @ModelAttribute(SessionKey.LOCATE_SITE_FILTER)
    public LocateSiteFilterForm initFilter(HttpSession session) {
        LocateSiteFilterForm locateSiteFilter = (LocateSiteFilterForm) session.getAttribute(SessionKey.LOCATE_SITE_FILTER);
        return initFilter(locateSiteFilter, false);
    }

    private LocateSiteFilterForm initFilter(LocateSiteFilterForm locateSiteFilter, boolean init) {

        if (locateSiteFilter == null) {
            locateSiteFilter = new LocateSiteFilterForm();
        }

        // init at once
        if (init && !locateSiteFilter.isInitialized()) {
            locateSiteFilter.initialize();
        }

        return locateSiteFilter;
    }
}


