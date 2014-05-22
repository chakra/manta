package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.CostCenterListView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.service.CostCenterService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.CostCenterListViewCriteria;
import com.espendwise.manta.web.forms.CostCenterFilterForm;
import com.espendwise.manta.web.forms.CostCenterFilterResultForm;
import com.espendwise.manta.web.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(UrlPathKey.COST_CENTER.FILTER)
@SessionAttributes({SessionKey.COST_CENTER_FILTER_RESULT, SessionKey.COST_CENTER_FILTER})
@AutoClean(SessionKey.COST_CENTER_FILTER_RESULT)
public class CostCenterFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(CostCenterFilterController.class);

    private CostCenterService costCenterService;

    @Autowired
    public CostCenterFilterController(CostCenterService costCenterService) {
        this.costCenterService = costCenterService;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.COST_CENTER_FILTER) CostCenterFilterForm filterForm) {
        return "costCenter/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findCostCenters(WebRequest request,
                            @ModelAttribute(SessionKey.COST_CENTER_FILTER_RESULT) CostCenterFilterResultForm form,
                            @ModelAttribute(SessionKey.COST_CENTER_FILTER) CostCenterFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "costCenter/filter";
        }

        form.reset();

        CostCenterListViewCriteria criteria = new CostCenterListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.COST_CENTER);

        criteria.setCostCenterId(parseNumberNN(filterForm.getCostCenterId()));
        criteria.setCostCenterName(filterForm.getCostCenterName(), filterForm.getCostCenterNameFilterType());
        criteria.setStoreId(getStoreId());
        criteria.setFilterAccCatType(filterForm.getCostCenterAccCatFilterType());

        if (filterForm.getCostCenterAccCatFilterType().equalsIgnoreCase(
                RefCodeNames.COST_CENTER_FILTER_ACC_CAT_TYPE.ACCOUNT)) {
            criteria.setAccountIds(Utility.splitLong(filterForm.getFilteredAccountCommaIds()));
            criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
        } else if (filterForm.getCostCenterAccCatFilterType().equalsIgnoreCase(
                RefCodeNames.COST_CENTER_FILTER_ACC_CAT_TYPE.CATALOG)) {
            criteria.setCatalogIds(Utility.splitLong(filterForm.getFilteredCatalogCommaIds()));
            criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
        }
        List<CostCenterListView> costCenters = costCenterService.findCostCentersByCriteria(criteria);

        for (CostCenterListView ccV : costCenters) {
            ccV.setAllocateDiscount(Utility.isTrue(ccV.getAllocateDiscount()) ? ccV.getAllocateDiscount() : "false");
            ccV.setAllocateFreight(Utility.isTrue(ccV.getAllocateFreight()) ? ccV.getAllocateFreight() : "false");
        }
        form.setCostCenters(costCenters);

        WebSort.sort(form, CostCenterListView.COST_CENTER_NAME);

        return "costCenter/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.COST_CENTER_FILTER_RESULT) CostCenterFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "costCenter/filter";
    }


    @RequestMapping(value = "/filter/clear", method = RequestMethod.POST)
    public String clear(WebRequest request, @ModelAttribute(SessionKey.COST_CENTER_FILTER) CostCenterFilterForm form) throws Exception {
        form.reset();
        return redirect(request, UrlPathKey.COST_CENTER.FILTER);
    }

    @RequestMapping(value = "/filter/clear/account", method = RequestMethod.POST)
    public String clearAccountFilter(WebRequest request, @ModelAttribute(SessionKey.COST_CENTER_FILTER) CostCenterFilterForm form) throws Exception {
        form.setFilteredAccounts(Utility.emptyList(AccountListView.class));
        form.setAccountFilter(null);
        return redirect(request, UrlPathKey.COST_CENTER.FILTER);
    }


    @RequestMapping(value = "/filter/clear/catalog", method = RequestMethod.POST)
    public String clearCatalogFilter(WebRequest request, @ModelAttribute(SessionKey.COST_CENTER_FILTER) CostCenterFilterForm form) throws Exception {
        form.setFilteredCatalogs(Utility.emptyList(CatalogListView.class));
        form.setCatalogFilter(null);
        return redirect(request, UrlPathKey.COST_CENTER.FILTER);
    }


    @ModelAttribute(SessionKey.COST_CENTER_FILTER_RESULT)
    public CostCenterFilterResultForm init(HttpSession session) {

        CostCenterFilterResultForm costCenterFilterResult = (CostCenterFilterResultForm) session.getAttribute(SessionKey.COST_CENTER_FILTER_RESULT);

        if (costCenterFilterResult == null) {
            costCenterFilterResult = new CostCenterFilterResultForm();
        }

        return costCenterFilterResult;

    }

    @ModelAttribute(SessionKey.COST_CENTER_FILTER)
    public CostCenterFilterForm initFilter(HttpSession session) {

        CostCenterFilterForm costCenterFilter = (CostCenterFilterForm) session.getAttribute(SessionKey.COST_CENTER_FILTER);

        if (costCenterFilter == null || !costCenterFilter.isInitialized()) {
            costCenterFilter = new CostCenterFilterForm();
            costCenterFilter.initialize();
        }

        return costCenterFilter;

    }

}


