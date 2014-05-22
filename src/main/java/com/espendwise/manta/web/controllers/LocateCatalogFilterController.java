package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.web.forms.LocateCatalogFilterForm;
import com.espendwise.manta.web.forms.LocateCatalogFilterResultForm;
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
@RequestMapping(UrlPathKey.LOCATE.LOCATE_CATALOG)
@SessionAttributes({SessionKey.LOCATE_CATALOG_FILTER_RESULT, SessionKey.LOCATE_CATALOG_FILTER})
@AutoClean(SessionKey.LOCATE_CATALOG_FILTER_RESULT)
public class LocateCatalogFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateCatalogFilterController.class);

    private CatalogService catalogService;

    @Autowired
    public LocateCatalogFilterController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER) LocateCatalogFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER_RESULT) LocateCatalogFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(true);
        return "catalog/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER) LocateCatalogFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER_RESULT) LocateCatalogFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(false);
        return "catalog/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findCatalogs(WebRequest request,
                               @ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER_RESULT) LocateCatalogFilterResultForm form,
                               @ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER) LocateCatalogFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "catalog/locate";
        }

        form.reset();

        CatalogListViewCriteria criteria = new CatalogListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.CATALOG);

        if (Utility.isSet(filterForm.getCatalogId())) {
//            criteria.setCatalogName(filterForm.getCatalogId());
            criteria.setCatalogId(filterForm.getCatalogId());
//            criteria.setCatalogIdFilterType(Constants.FILTER_TYPE.ID);
        }
        if (Utility.isSet(filterForm.getCatalogName())) {
            criteria.setCatalogName(filterForm.getCatalogName());
            criteria.setCatalogNameFilterType(filterForm.getCatalogNameFilterType());
        }

        criteria.setStoreId(getStoreId());
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
        criteria.setCatalogType(filterForm.getCatalogType());

        List<CatalogListView> catalogs = catalogService.findCatalogsByCriteria(criteria);

        SelectableObjects<CatalogListView> selectableObj = new SelectableObjects<CatalogListView>(
                catalogs,
                new ArrayList<CatalogListView>(),
                AppComparator.CATALOG_LIST_VIEW_COMPARATOR
        );
        form.setSelectedCatalogs(selectableObj);

        WebSort.sort(form, CatalogListView.CATALOG_NAME);

        return "catalog/locate";

    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER_RESULT) LocateCatalogFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<CatalogListView> selected = filterResultForm.getSelectedCatalogs().getSelected();
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
    public String sort(@ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER_RESULT) LocateCatalogFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "catalog/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER_RESULT)
    public LocateCatalogFilterResultForm init(HttpSession session) {

        LocateCatalogFilterResultForm locateCatalogFilterResult = (LocateCatalogFilterResultForm) session.getAttribute(SessionKey.LOCATE_CATALOG_FILTER_RESULT);

        if (locateCatalogFilterResult == null) {
            locateCatalogFilterResult = new LocateCatalogFilterResultForm();
        }

        return locateCatalogFilterResult;

    }

    @ModelAttribute(SessionKey.LOCATE_CATALOG_FILTER)
    public LocateCatalogFilterForm initFilter(HttpSession session) {
        LocateCatalogFilterForm locateCatalogFilter = (LocateCatalogFilterForm) session.getAttribute(SessionKey.LOCATE_CATALOG_FILTER);
        return initFilter(locateCatalogFilter, false);
    }

    private LocateCatalogFilterForm initFilter(LocateCatalogFilterForm locateCatalogFilter, boolean init) {

        if (locateCatalogFilter == null) {
            locateCatalogFilter = new LocateCatalogFilterForm();
        }

        // init at once
        if (init && !locateCatalogFilter.isInitialized()) {
            locateCatalogFilter.initialize();
        }

        return locateCatalogFilter;
    }


}


