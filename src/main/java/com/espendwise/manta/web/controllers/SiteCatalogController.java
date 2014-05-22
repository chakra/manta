package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.CatalogView;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.CatalogSearchCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.SiteCatalogFilterForm;
import com.espendwise.manta.web.forms.SiteCatalogFilterResultForm;
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
@RequestMapping(UrlPathKey.SITE.CATALOG)
@SessionAttributes({SessionKey.SITE_CATALOG_FILTER, SessionKey.SITE_CATALOG_FILTER_RESULT})
@AutoClean(SessionKey.SITE_CATALOG_FILTER)
public class SiteCatalogController extends BaseController {

    private static final Logger logger = Logger.getLogger(SiteCatalogController.class);

    private SiteService siteService;
    private CatalogService catalogService;

    @Autowired
    public SiteCatalogController(SiteService siteService,
                                 CatalogService catalogService) {
        this.siteService = siteService;
        this.catalogService = catalogService;
    }

    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {
        return "site/catalog";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "site/catalog";
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.SITE_CATALOG_FILTER) SiteCatalogFilterForm filterForm) {
        return "site/catalog";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findCatalogs(WebRequest request,
                            @ModelAttribute(SessionKey.SITE_CATALOG_FILTER) SiteCatalogFilterForm filterForm,
                            @ModelAttribute(SessionKey.SITE_CATALOG_FILTER_RESULT) SiteCatalogFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "site/catalog";
        }

        doSearch(filterForm, resultForm);

        return "site/catalog";

    }

    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@PathVariable(IdPathKey.LOCATION_ID) Long locationId,
                         @ModelAttribute(SessionKey.SITE_CATALOG_FILTER) SiteCatalogFilterForm filterForm,
                         @ModelAttribute(SessionKey.SITE_CATALOG_FILTER_RESULT) SiteCatalogFilterResultForm resultForm) throws Exception {

        logger.info("site/catalog/update()=> BEGIN");

        logger.info("site/catalog/update()=> siteId: " + locationId);
        
        if (Utility.isSet(resultForm.getCatalogId())) {
            siteService.configureSiteCatalog(locationId, resultForm.getCatalogId());
        }
        
        doSearch(filterForm, resultForm);

        logger.info("site/catalog/update()=> END.");
        
        return "site/catalog";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.SITE_CATALOG_FILTER_RESULT) SiteCatalogFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "site/catalog";
    }

    @ModelAttribute(SessionKey.SITE_CATALOG_FILTER_RESULT)
    public SiteCatalogFilterResultForm initFilterResult(HttpSession session) {

        logger.info("initFilterResult()=> init....");

        SiteCatalogFilterResultForm form = (SiteCatalogFilterResultForm) session.getAttribute(SessionKey.SITE_CATALOG_FILTER_RESULT);

        logger.info("initFilterResult()=> form: " + form);

        if (form == null) {
            form = new SiteCatalogFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("initFilterResult()=> init.OK!");

        return form;

    }
    
    @ModelAttribute(SessionKey.SITE_CATALOG_FILTER)
    public SiteCatalogFilterForm initFilter(HttpSession session, @PathVariable(IdPathKey.LOCATION_ID) Long locationId) {

        logger.info("initFilter()=> init....");

        SiteCatalogFilterForm form = (SiteCatalogFilterForm) session.getAttribute(SessionKey.SITE_CATALOG_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new SiteCatalogFilterForm(locationId);
            form.initialize();
        }

        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }
    
    private void doSearch (SiteCatalogFilterForm filterForm, SiteCatalogFilterResultForm resultForm) {
        resultForm.reset();

        Long siteId = null;
        if (Utility.isSet(filterForm.getSiteId())) {
            siteId = filterForm.getSiteId();
        }
        
        CatalogSearchCriteria criteria = new CatalogSearchCriteria(Constants.FILTER_RESULT_LIMIT.CATALOG);
        criteria.setSiteId(siteId);
        criteria.setCatalogName(filterForm.getCatalogName());
        criteria.setCatalogNameFilterType(filterForm.getCatalogNameFilterType());
        criteria.setActiveOnly(!filterForm.getShowInactive());
        criteria.setConfiguredOnly(true);
        
        List<CatalogView> configured = catalogService.findCatalogsByCriteria(criteria);
                
        criteria.setConfiguredOnly(false);
        
        List<CatalogView> catalogs = filterForm.getShowConfiguredOnly()
                           ? configured : catalogService.findCatalogsByCriteria(criteria);
        resultForm.setCatalogs(catalogs);
        WebSort.sort(resultForm, CatalogView.CATALOG_NAME);
        
        resultForm.setCatalogId(null);
        if (Utility.isSet(configured)) {
            resultForm.setCatalogId(configured.get(0).getCatalogId());
        }
    }

}
