package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.CatalogView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.CostCenterService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.CatalogSearchCriteria;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.CostCenterCatalogFilterForm;
import com.espendwise.manta.web.forms.CostCenterCatalogFilterResultForm;
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
import java.util.ArrayList;


@Controller
@RequestMapping(UrlPathKey.COST_CENTER.CATALOG)
@SessionAttributes({SessionKey.COST_CENTER_CATALOG_FILTER, SessionKey.COST_CENTER_CATALOG_FILTER_RESULT})
@AutoClean(SessionKey.COST_CENTER_CATALOG_FILTER)
public class CostCenterCatalogController extends BaseController {

    private static final Logger logger = Logger.getLogger(CostCenterCatalogController.class);

    private CostCenterService costCenterService;
    private CatalogService catalogService;

    @Autowired
    public CostCenterCatalogController(CostCenterService costCenterService,
                                 CatalogService catalogService) {
        this.costCenterService = costCenterService;
        this.catalogService = catalogService;
    }

    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {
        return "costCenter/catalog";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "costCenter/catalog";
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.COST_CENTER_CATALOG_FILTER) CostCenterCatalogFilterForm filterForm) {
        return "costCenter/catalog";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findCatalogs(WebRequest request,
                            @ModelAttribute(SessionKey.COST_CENTER_CATALOG_FILTER) CostCenterCatalogFilterForm filterForm,
                            @ModelAttribute(SessionKey.COST_CENTER_CATALOG_FILTER_RESULT) CostCenterCatalogFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "costCenter/catalog";
        }

        doSearch(filterForm, resultForm);

        return "costCenter/catalog";

    }

    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@PathVariable(IdPathKey.COST_CENTER_ID) Long costCenterId,
                         @ModelAttribute(SessionKey.COST_CENTER_CATALOG_FILTER) CostCenterCatalogFilterForm filterForm,
                         @ModelAttribute(SessionKey.COST_CENTER_CATALOG_FILTER_RESULT) CostCenterCatalogFilterResultForm resultForm) throws Exception {

        logger.info("costCenter/catalog/update()=> BEGIN");

        logger.info("costCenter/catalog/update()=> costCenterId: " + costCenterId);

        List<CatalogListView> selected =  resultForm.getCatalogs().getSelected();
        List<CatalogListView> catalogs = resultForm.getCatalogs().getValues();

        costCenterService.configureCostCenterCatalogs(costCenterId, getStoreId(), selected, catalogs);

        doSearch(filterForm, resultForm);

        logger.info("costCenter/catalog/update()=> END.");
        
        return "costCenter/catalog";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.COST_CENTER_CATALOG_FILTER_RESULT) CostCenterCatalogFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "costCenter/catalog";
    }

    @ModelAttribute(SessionKey.COST_CENTER_CATALOG_FILTER_RESULT)
    public CostCenterCatalogFilterResultForm initFilterResult(HttpSession session) {

        logger.info("initFilterResult()=> init....");

        CostCenterCatalogFilterResultForm form = (CostCenterCatalogFilterResultForm) session.getAttribute(SessionKey.COST_CENTER_CATALOG_FILTER_RESULT);

        logger.info("initFilterResult()=> form: " + form);

        if (form == null) {
            form = new CostCenterCatalogFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("initFilterResult()=> init.OK!");

        return form;

    }
    
    @ModelAttribute(SessionKey.COST_CENTER_CATALOG_FILTER)
    public CostCenterCatalogFilterForm initFilter(HttpSession session, @PathVariable(IdPathKey.COST_CENTER_ID) Long locationId) {

        logger.info("initFilter()=> init....");

        CostCenterCatalogFilterForm form = (CostCenterCatalogFilterForm) session.getAttribute(SessionKey.COST_CENTER_CATALOG_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new CostCenterCatalogFilterForm(locationId);
            form.initialize();
        }

        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }
    
    private void doSearch (CostCenterCatalogFilterForm filterForm, CostCenterCatalogFilterResultForm resultForm) {
        resultForm.reset();

        Long costCenterId = null;
        if (Utility.isSet(filterForm.getCostCenterId())) {
            costCenterId = filterForm.getCostCenterId();
        }
        
        CatalogListViewCriteria criteria =
            new CatalogListViewCriteria(Constants.FILTER_RESULT_LIMIT.CATALOG);
        criteria.setCostCenterId(costCenterId);

        criteria.setCatalogName(filterForm.getCatalogName());
        criteria.setCatalogNameFilterType(filterForm.getCatalogNameFilterType());

        criteria.setActiveOnly(!filterForm.getShowInactive());

        criteria.setCatalogType(RefCodeNames.CATALOG_TYPE_CD.ACCOUNT);
        criteria.setStoreId(getStoreId());

        // configured catalogs
        criteria.setConfiguredOnly(true);
        List<CatalogListView> configured = catalogService.findCatalogsByCriteria(criteria);

        // all catalogs
        List<CatalogListView> catalogs;
        if (filterForm.getShowConfiguredOnly()) {
            catalogs = configured;
        } else {
            criteria.setConfiguredOnly(false);
            catalogs = catalogService.findCatalogsByCriteria(criteria);
        }

         SelectableObjects<CatalogListView> selectableObj = new SelectableObjects<CatalogListView>(
                catalogs,
                configured,
                AppComparator.CATALOG_LIST_VIEW_COMPARATOR
        );

        resultForm.setCatalogs(selectableObj);
        WebSort.sort(resultForm, CatalogView.CATALOG_NAME);
        
    }

}
