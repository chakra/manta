package com.espendwise.manta.web.controllers;

import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.SiteHierarchyService;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.web.forms.AccountSiteHierarchyConfigurationForm;
import com.espendwise.manta.web.forms.AccountSiteHierarchyManageFilterForm;
import com.espendwise.manta.web.forms.AccountSiteHierarchyManageForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.SITE_HIERARCHY_LEVEL_3)
public class AccountSiteHierarchy3Controller extends SiteHierarchyBaseController {

    private static final Logger logger = Logger.getLogger(AccountSiteHierarchy3Controller.class);

    @Autowired
    public AccountSiteHierarchy3Controller(AccountService accountService, SiteHierarchyService siteHierarchyService, SiteService siteService) {
        super(accountService, siteHierarchyService, siteService);
    }

    @Clean(before = true, value = {SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION, SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE, SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER})
    @RequestMapping(value = "/configure/{siteHierarchyId}", method = RequestMethod.GET)
    public String configure(WebRequest request,
                                          @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE) AccountSiteHierarchyManageForm form,
                                          @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION) AccountSiteHierarchyConfigurationForm configurationForm,
                                          @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                                          @PathVariable("level1Id") Long level1Id,
                                          @PathVariable("level2Id") Long level2Id,
                                          @PathVariable("level3Id") Long level3Id,
                                          @PathVariable(IdPathKey.SITE_HIERARCHY_ID) Long siteHierarchyId) {

        configureHierarchy(
                siteHierarchyId,
                accountId,
                configurationForm,
                level1Id,
                level2Id,
                level3Id
        );

        return "account/locationHierarchy/configuration";

    }

    @RequestMapping(value = "/manage/{siteHierarchyId}/filter", method = RequestMethod.GET)
    public String filter(WebRequest request,
                         @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE) AccountSiteHierarchyManageForm form,
                         @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER) AccountSiteHierarchyManageFilterForm filterForm,
                         @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                         @PathVariable("level1Id") Long level1Id,
                         @PathVariable("level2Id") Long level2Id,
                         @PathVariable("level3Id") Long level3Id,
                         @PathVariable(IdPathKey.SITE_HIERARCHY_ID) Long siteHierarchyId) throws Exception {


        super.filter(
                request,
                siteHierarchyId,
                accountId,
                form,
                filterForm,
                level1Id,
                level2Id,
                level3Id
        );

        return "account/locationHierarchy/manage";

    }

    @RequestMapping(value = "/manage/{siteHierarchyId}", method = RequestMethod.GET)
    @Clean(before = true, value = {SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION, SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE, SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER})
    public String manage(WebRequest request,
                         @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE) AccountSiteHierarchyManageForm form,
                         @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER) AccountSiteHierarchyManageFilterForm filterForm,
                         @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                         @PathVariable("level1Id") Long level1Id,
                         @PathVariable("level2Id") Long level2Id,
                         @PathVariable("level3Id") Long level3Id,
                         @PathVariable(IdPathKey.SITE_HIERARCHY_ID) Long siteHierarchyId) {


        manageLayer(
                siteHierarchyId,
                accountId,
                form,
                filterForm,
                level1Id,
                level2Id,
                level3Id
        );

        return "account/locationHierarchy/manage";

    }

    @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_CONFIGRATION)
    public AccountSiteHierarchyConfigurationForm initFilter(HttpSession session) {
        AccountSiteHierarchyConfigurationForm configurationForm = new AccountSiteHierarchyConfigurationForm();
        configurationForm.initialize();
        return configurationForm;
    }

    @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE)
    public AccountSiteHierarchyManageForm initManage(@PathVariable(IdPathKey.ACCOUNT_ID) Long accountId) {
        return new AccountSiteHierarchyManageForm();
    }

    @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER)
    public AccountSiteHierarchyManageFilterForm initManageFilter(@ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY_MANAGE) AccountSiteHierarchyManageForm manageForm) {
        AccountSiteHierarchyManageFilterForm filterForm = new AccountSiteHierarchyManageFilterForm();
        filterForm.initialize();
        return filterForm;
    }
}
