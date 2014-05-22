package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.web.forms.SiteFilterForm;
import com.espendwise.manta.web.forms.SiteFilterResultForm;
import com.espendwise.manta.web.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(UrlPathKey.SITE.FILTER)
@SessionAttributes({SessionKey.SITE_FILTER_RESULT, SessionKey.SITE_FILTER})
@AutoClean(SessionKey.SITE_FILTER_RESULT)
public class SiteFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(SiteFilterController.class);

    private SiteService siteService;

    @Autowired
    public SiteFilterController(SiteService siteService) {
        this.siteService = siteService;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.SITE_FILTER) SiteFilterForm filterForm) {
        return "site/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findSites(WebRequest request,
                            @ModelAttribute(SessionKey.SITE_FILTER_RESULT) SiteFilterResultForm form,
                            @ModelAttribute(SessionKey.SITE_FILTER) SiteFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "site/filter";
        }

        form.reset();

        SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);

        criteria.setSiteId(parseNumberNN(filterForm.getSiteId()));
        criteria.setSiteName(filterForm.getSiteName(), filterForm.getSiteNameFilterType());
        criteria.setRefNumber(filterForm.getRefNumber(), filterForm.getRefNumberFilterType());
        criteria.setCity(filterForm.getCity(), Constants.FILTER_TYPE.START_WITH);
        criteria.setState(filterForm.getState(), Constants.FILTER_TYPE.START_WITH);
        criteria.setPostalCode(filterForm.getPostalCode(), Constants.FILTER_TYPE.START_WITH);
        criteria.setStoreId(getStoreId());
        criteria.setAccountIds(Utility.splitLong(filterForm.getAccountFilter()));
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        List<SiteListView> sites = siteService.findSitesByCriteria(criteria);

        form.setSites(sites);

        WebSort.sort(form, SiteListView.SITE_NAME);

        return "site/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.SITE_FILTER_RESULT) SiteFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "site/filter";
    }


    @RequestMapping(value = "/filter/clear", method = RequestMethod.POST)
    public String clear(WebRequest request, @ModelAttribute(SessionKey.SITE_FILTER) SiteFilterForm form) throws Exception {
        form.reset();
        return redirect(request, UrlPathKey.SITE.FILTER);
    }

    @RequestMapping(value = "/filter/clear/account", method = RequestMethod.POST)
    public String clearAccountFilter(WebRequest request, @ModelAttribute(SessionKey.SITE_FILTER) SiteFilterForm form) throws Exception {
        form.setFilteredAccounts(Utility.emptyList(AccountListView.class));
        form.setAccountFilter(null);
        return redirect(request, UrlPathKey.SITE.FILTER);
    }


    @ModelAttribute(SessionKey.SITE_FILTER_RESULT)
    public SiteFilterResultForm init(HttpSession session) {

        SiteFilterResultForm siteFilterResult = (SiteFilterResultForm) session.getAttribute(SessionKey.SITE_FILTER_RESULT);

        if (siteFilterResult == null) {
            siteFilterResult = new SiteFilterResultForm();
        }

        return siteFilterResult;

    }

    @ModelAttribute(SessionKey.SITE_FILTER)
    public SiteFilterForm initFilter(HttpSession session) {

        SiteFilterForm siteFilter = (SiteFilterForm) session.getAttribute(SessionKey.SITE_FILTER);

        if (siteFilter == null || !siteFilter.isInitialized()) {
            siteFilter = new SiteFilterForm();
            siteFilter.initialize();
        }

        return siteFilter;

    }

}


