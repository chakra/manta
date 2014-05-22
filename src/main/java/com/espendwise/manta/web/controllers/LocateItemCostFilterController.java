package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.ItemContractCostView;
import com.espendwise.manta.service.OrderService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.web.forms.*;
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
@RequestMapping(UrlPathKey.LOCATE.LOCATE_ITEM_COST)
@SessionAttributes({SessionKey.LOCATE_ITEM_COST_FILTER, SessionKey.LOCATE_ITEM_COST_FILTER_RESULT})
@AutoClean(SessionKey.LOCATE_ITEM_COST_FILTER_RESULT)
public class LocateItemCostFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateItemCostFilterController.class);

    private OrderService orderService;

    @Autowired
    public LocateItemCostFilterController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@RequestParam(value = "accountId", required = false) String accountId,
                              @RequestParam(value = "itemSkuNum", required = false) String itemSkuNum,
                              @RequestParam(value = "distId", required = false) String distId,
                              @RequestParam(value = "distErpNum", required = false) String distErpNum,
                              @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER) LocateItemCostFilterForm filterForm,
                              @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER_RESULT) LocateItemCostFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        
        if (Utility.isSet(accountId)) {
            filterForm.setAccountId(Long.valueOf(accountId));
        }
        if (Utility.isSet(itemSkuNum)) {
            filterForm.setItemSkuNum(Long.valueOf(itemSkuNum));
        }
        if (Utility.isSet(distId)) {
            filterForm.setDistId(Long.valueOf(distId));
        }
        if (Utility.isSet(distErpNum)) {
            filterForm.setDistErpNum(distErpNum);
        }
        
        filterResultForm.setMultiSelected(true);
        return "itemCost/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@RequestParam(value = "accountId", required = false) String accountId,
                               @RequestParam(value = "itemSkuNum", required = false) String itemSkuNum,
                               @RequestParam(value = "distId", required = false) String distId,
                               @RequestParam(value = "distErpNum", required = false) String distErpNum,
                               @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER) LocateItemCostFilterForm filterForm,
                               @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER_RESULT) LocateItemCostFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        
        if (Utility.isSet(accountId)) {
            filterForm.setAccountId(Long.valueOf(accountId));
        }
        if (Utility.isSet(itemSkuNum)) {
            filterForm.setItemSkuNum(Long.valueOf(itemSkuNum));
        }
        if (Utility.isSet(distId)) {
            filterForm.setDistId(Long.valueOf(distId));
        }
        if (Utility.isSet(distErpNum)) {
            filterForm.setDistErpNum(distErpNum);
        }
        
        filterResultForm.setMultiSelected(false);
        return "itemCost/locate";
    }

    @RequestMapping(value = "/filter/{searchType}", method = RequestMethod.POST)
    public String findItemCosts(WebRequest request,
                            @PathVariable("searchType") String searchType,
                            @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER) LocateItemCostFilterForm filterForm,
                            @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER_RESULT) LocateItemCostFilterResultForm filterResultForm
                            ) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        filterResultForm.reset();     

        if (Utility.isSet(searchType)) {
            List<ItemContractCostView> itemCosts = null;
            if ("assignedDist".equals(searchType)) {
                itemCosts = orderService.getItemContractCost(filterForm.getAccountId(),
                                                             filterForm.getItemSkuNum(),
                                                             filterForm.getDistId(),
                                                             filterForm.getDistErpNum());
            } else if ("allDist".equals(searchType)) {
                itemCosts = orderService.getItemContractCost(filterForm.getAccountId(),
                                                             filterForm.getItemSkuNum(),
                                                             null,
                                                             null);
            }
                        
            logger.info("findItemCosts()=> itemCosts: " + itemCosts.size());

            SelectableObjects<ItemContractCostView> selectableObj = new SelectableObjects<ItemContractCostView> (
                                                                itemCosts,
                                                                new ArrayList<ItemContractCostView>(),
                                                                AppComparator.ITEM_CONTRACT_COST_VIEW_COMPARATOR);
                                                                filterResultForm.setSelectedItemCosts(selectableObj);

            WebSort.sort(filterResultForm, ItemContractCostView.DIST_ID);
        }

        return "itemCost/locate";
    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @RequestParam(value = "index", required = false) String index,
                                 @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER_RESULT) LocateItemCostFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<ItemContractCostView> selected = filterResultForm.getSelectedItemCosts().getSelected();
        if(!filterResultForm.getMultiSelected()){
            selected = Utility.toList(selected.get(0));
        }

        if (Utility.isSet(filter)) {

            List<String> filterKeys = Arrays.asList(Utility.split(filter, "."));

            WebForm targetForm = (WebForm) session.getAttribute(filterKeys.get(0));

            Method method;
            if (index != null) {
                method = BeanUtils.findMethod(targetForm.getClass(),
                                              Utility.javaBeanPath(filterKeys.subList(1, filterKeys.size()).toArray(new String[filterKeys.size() - 1])),
                                              Integer.class,
                                              List.class);
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
    public String sort(@ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER_RESULT) LocateItemCostFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "itemCost/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER_RESULT)
    public LocateItemCostFilterResultForm initFilterResult(HttpSession session) {

        LocateItemCostFilterResultForm locateItemCostFilterResult = (LocateItemCostFilterResultForm) session.getAttribute(SessionKey.LOCATE_ITEM_COST_FILTER_RESULT);

        if (locateItemCostFilterResult == null) {
            locateItemCostFilterResult = new LocateItemCostFilterResultForm();
        }

        return locateItemCostFilterResult;
    }

    @ModelAttribute(SessionKey.LOCATE_ITEM_COST_FILTER)
    public LocateItemCostFilterForm initFilter(HttpSession session) {
        LocateItemCostFilterForm locateItemCostFilter = (LocateItemCostFilterForm) session.getAttribute(SessionKey.LOCATE_ITEM_COST_FILTER);
        return initFilter(locateItemCostFilter, false);
    }

    private LocateItemCostFilterForm initFilter(LocateItemCostFilterForm locateItemCostFilter, boolean init) {

        if (locateItemCostFilter == null) {
            locateItemCostFilter = new LocateItemCostFilterForm();
        }

        if (init && !locateItemCostFilter.isInitialized()) {
            locateItemCostFilter.initialize();
        }

        return locateItemCostFilter;
    }

}


