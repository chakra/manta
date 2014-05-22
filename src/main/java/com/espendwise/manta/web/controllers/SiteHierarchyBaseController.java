package com.espendwise.manta.web.controllers;

import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.SiteHierarchyService;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.web.forms.AccountSiteHierarchyConfigurationForm;
import com.espendwise.manta.web.forms.AccountSiteHierarchyManageFilterForm;
import com.espendwise.manta.web.forms.AccountSiteHierarchyManageForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebSort;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@Controller
@SessionAttributes({SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION, SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE, SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER})
@AutoClean(value = {SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION, SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE, SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER}, controller = AccountSiteHierarchy1Controller.class)
public class SiteHierarchyBaseController extends SiteHierarchySupportController {

    private static final Logger logger = Logger.getLogger(SiteHierarchyBaseController.class);

    public SiteHierarchyBaseController() {
    }

    @Autowired
    public SiteHierarchyBaseController(AccountService accountService, SiteHierarchyService siteHierarchyService, SiteService siteService) {
        super(accountService, siteHierarchyService, siteService);
    }

    @RequestMapping(value = "/manage/{siteHierarchyId}", method = RequestMethod.POST)
    public String manage(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE) AccountSiteHierarchyManageForm form, @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER) AccountSiteHierarchyManageFilterForm filterForm) throws Exception {
        return AccountSiteHierarchyManageForm.ACTION_CD.ADD_LINE.equals(form.getAction())
                ? super.addline(form)
                : super.save(request, form, filterForm);

    }


    @RequestMapping(value = "/configure/{siteHierarchyId}/filter", method = RequestMethod.POST)
    public String configurationFilter(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION) AccountSiteHierarchyConfigurationForm configurationForm) throws Exception {
        return findLocations(request, configurationForm);
    }


    @RequestMapping(value = "/configure/{siteHierarchyId}/save", method = RequestMethod.POST)
    public String configurationHierarchy4(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION) AccountSiteHierarchyConfigurationForm configurationForm) throws Exception {
        return this.saveConfiguration(request, configurationForm);
    }

    @RequestMapping(value = "/configure/{siteHierarchyId}/filter/sortby/{field}", method ={RequestMethod.GET, RequestMethod.POST})
    public String sort(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION) AccountSiteHierarchyConfigurationForm configurationForm, @PathVariable String field) throws Exception {
        WebSort.sort(configurationForm, field);
        return "account/locationHierarchy/configuration";
    }



}
