package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.model.view.ServiceListView;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.ServiceListViewCriteria;
import com.espendwise.manta.web.forms.LocateItemFilterForm;
import com.espendwise.manta.web.forms.LocateItemFilterResultForm;
import com.espendwise.manta.web.forms.LocateServiceFilterResultForm;
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
@RequestMapping(UrlPathKey.LOCATE.LOCATE_SERVICE)
@SessionAttributes({SessionKey.LOCATE_ITEM_FILTER, SessionKey.LOCATE_SERVICE_FILTER_RESULT})
@AutoClean(SessionKey.LOCATE_SERVICE_FILTER_RESULT)
public class LocateServiceFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateServiceFilterController.class);

    private CatalogService catalogService;

    @Autowired
    public LocateServiceFilterController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@RequestParam(value = "siteId", required = false) Long siteId,
                              @RequestParam(value = "assetId", required = false) Long assetId,
                              @RequestParam(value = "contractId", required = false) Long contractId,
                              @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER) LocateItemFilterForm filterForm,
                              @ModelAttribute(SessionKey.LOCATE_SERVICE_FILTER_RESULT) LocateServiceFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        if (siteId != null) {
            filterForm.setSiteId(siteId);
        }
        if (assetId != null) {
            filterForm.setAssetId(assetId);
        }
        if (contractId != null) {
            filterForm.setContractId(contractId);
        }
        filterResultForm.setMultiSelected(true);
        return "service/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@RequestParam(value = "siteId", required = false) Long siteId,
                               @RequestParam(value = "assetId", required = false) Long assetId,
                               @RequestParam(value = "contractId", required = false) Long contractId,
                               @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER) LocateItemFilterForm filterForm,
                               @ModelAttribute(SessionKey.LOCATE_SERVICE_FILTER_RESULT) LocateServiceFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        if (siteId != null) {
            filterForm.setSiteId(siteId);
        }
        if (assetId != null) {
            filterForm.setAssetId(assetId);
        }
        if (contractId != null) {
            filterForm.setContractId(contractId);
        }
        filterResultForm.setMultiSelected(false);
        return "service/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findServices(WebRequest request,
                                @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER) LocateItemFilterForm filterForm,
                                @ModelAttribute(SessionKey.LOCATE_SERVICE_FILTER_RESULT) LocateServiceFilterResultForm filterResultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "service/locate";
        }

        filterResultForm.reset();

        ServiceListViewCriteria criteria = new ServiceListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SERVICE);

        if (Utility.isSet(filterForm.getSiteId())) {
            criteria.setSiteId(filterForm.getSiteId());
        }
        if (Utility.isSet(filterForm.getAssetId())) {
            criteria.setAssetId(filterForm.getAssetId());
        }
        if (Utility.isSet(filterForm.getContractId())) {
            criteria.setContractId(filterForm.getContractId());
        }
        if (Utility.isSet(filterForm.getItemId())) {
            criteria.setServiceId(filterForm.getItemId());
        }
        if (Utility.isSet(filterForm.getItemName())) {
            criteria.setServiceName(filterForm.getItemName());
            criteria.setServiceNameFilterType(filterForm.getItemNameFilterType());
        }

        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        List<ServiceListView> services = catalogService.findServicesByCriteria(criteria);
        SelectableObjects<ServiceListView> selectableObj = new SelectableObjects<ServiceListView>(
                services,
                new ArrayList<ServiceListView>(),
                AppComparator.SERVICE_LIST_VIEW_COMPARATOR);
        filterResultForm.setSelectedServices(selectableObj);

        WebSort.sort(filterResultForm, ServiceListView.SERVICE_NAME);

        return "service/locate";
    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @RequestParam(value = "index", required = false) String index,
                                 @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER_RESULT) LocateItemFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<ProductListView> selected = filterResultForm.getSelectedItems().getSelected();
        if(!filterResultForm.getMultiSelected()){
            selected = Utility.toList(selected.get(0));
        }

        if (Utility.isSet(filter)) {

            List<String> filterKeys = Arrays.asList(Utility.split(filter, "."));

            WebForm targetForm = (WebForm) session.getAttribute(filterKeys.get(0));

            Method method = null;
            if (index != null) {
                method = BeanUtils.findMethod(targetForm.getClass(), Utility.javaBeanPath(filterKeys.subList(1, filterKeys.size()).toArray(new String[filterKeys.size() - 1])),
                                   Integer.class, List.class);
                logger.info("returnSelected()=> method:" + method);
                method.invoke(targetForm, Integer.valueOf(index), selected);
            } else {
                method = BeanUtils.findMethod(targetForm.getClass(),
                    Utility.javaBeanPath(filterKeys.subList(1, filterKeys.size()).toArray(new String[filterKeys.size() - 1])),
                    List.class);
                logger.info("returnSelected()=> method:" + method);
                method.invoke(targetForm, selected);
            }
        }

        logger.info("returnSelected()=> END.");

        return new Gson().toJson(selected);
    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.LOCATE_SERVICE_FILTER_RESULT) LocateServiceFilterResultForm form,
                       @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "service/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_SERVICE_FILTER_RESULT)
    public LocateServiceFilterResultForm init(HttpSession session) {

        LocateServiceFilterResultForm locateItemFilterResult = (LocateServiceFilterResultForm) session.getAttribute(SessionKey.LOCATE_SERVICE_FILTER_RESULT);

        if (locateItemFilterResult == null) {
            locateItemFilterResult = new LocateServiceFilterResultForm();
        }

        return locateItemFilterResult;
    }

    @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER)
    public LocateItemFilterForm initFilter(HttpSession session) {
        LocateItemFilterForm locateItemFilter = (LocateItemFilterForm) session.getAttribute(SessionKey.LOCATE_ITEM_FILTER);
        return initFilter(locateItemFilter, false);
    }

    private LocateItemFilterForm initFilter(LocateItemFilterForm locateItemFilter, boolean init) {

        if (locateItemFilter == null) {
            locateItemFilter = new LocateItemFilterForm();
        }

        // init at once
        if (init && !locateItemFilter.isInitialized()) {
            locateItemFilter.initialize();
        }

        return locateItemFilter;
    }

}


