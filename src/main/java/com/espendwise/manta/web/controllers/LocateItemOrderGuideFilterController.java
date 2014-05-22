package com.espendwise.manta.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.apache.log4j.Logger;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.forms.LocateItemOrderGuideFilterForm;
import com.espendwise.manta.web.forms.LocateItemOrderGuideFilterResultForm;
import com.espendwise.manta.web.forms.WebForm;
import com.espendwise.manta.service.OrderGuideService;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.model.view.OrderGuideItemView;
import com.espendwise.manta.model.view.ProductListView;
import com.google.gson.Gson;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Method;

@Controller
@Locate
@RequestMapping(UrlPathKey.LOCATE.LOCATE_ITEM_ORDER_GUIDE)
@SessionAttributes({SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT, SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER})
@AutoClean(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT)
public class LocateItemOrderGuideFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateItemOrderGuideFilterController.class);

    private OrderGuideService orderGuideService;
    private CatalogService catalogService;

    @Autowired
    public LocateItemOrderGuideFilterController(OrderGuideService orderGuideService, CatalogService catalogService) {
        this.orderGuideService = orderGuideService;
        this.catalogService = catalogService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@RequestParam(value = "catalogId", required = false) String catalogId,
                              @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER)LocateItemOrderGuideFilterForm filterForm,
                              @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT)LocateItemOrderGuideFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(true);
        if (Utility.isSet(catalogId)) {
            filterForm.setCatalogId(Long.valueOf(catalogId));
        }
        return "itemOrderGuide/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter( @RequestParam(value = "catalogId", required = false) String catalogId,
                                @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER) LocateItemOrderGuideFilterForm filterForm,
                                @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT) LocateItemOrderGuideFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        if (Utility.isSet(catalogId)) {
            filterForm.setCatalogId(Long.valueOf(catalogId));
        }
        filterResultForm.setMultiSelected(false);
        return "itemOrderGuide/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findItems(WebRequest request,
                            @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT) LocateItemOrderGuideFilterResultForm form,
                            @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER) LocateItemOrderGuideFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "itemOrderGuide/locate";
        }

        form.reset();

        ProductListViewCriteria criteria = new ProductListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.ITEM);

        if (Utility.isSet(filterForm.getItemId())) {
            criteria.setItemId(filterForm.getItemId());
        }
        if (Utility.isSet(filterForm.getItemName())) {
            criteria.setItemName(filterForm.getItemName());
            criteria.setItemNameFilterType(filterForm.getItemNameFilterType());
        }
        if (Utility.isSet(filterForm.getItemCategory())) {
            criteria.setItemCategory(filterForm.getItemCategory());
            criteria.setItemCategoryFilterType(filterForm.getItemCategoryFilterType());
        }
        if (Utility.isSet(filterForm.getItemSku())) {
            criteria.setItemSku(filterForm.getItemSku());
            criteria.setItemSkuFilterType(filterForm.getItemSkuFilterType());
            criteria.setItemSkuFilterSubType(filterForm.getItemSkuFilterSubType());
        }

        criteria.setStoreId(getStoreId());
        criteria.setCatalogId(filterForm.getCatalogId());

        criteria.setSubstituteItemSKUbyCustSKU(false);
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.ITEM);

        List<ProductListView> items = catalogService.findProductsByCriteria(criteria);
        SelectableObjects<ProductListView> selectableObj = new SelectableObjects<ProductListView>(
                items,
                new ArrayList<ProductListView>(),
                AppComparator.ITEM_LIST_VIEW_COMPARATOR
        );
        form.setSelectedItems(selectableObj);
        form.setCatalogId(filterForm.getCatalogId());

        validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
        }

        WebSort.sort(form, ProductListView.ITEM_NAME);

        return "itemOrderGuide/locate";

    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @RequestParam(value = "index", required = false) String index,
                                 @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT) LocateItemOrderGuideFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<ProductListView> selected = filterResultForm.getSelectedItems().getSelected();
        if(!filterResultForm.getMultiSelected()){
            selected = Utility.toList(selected.get(0));
        }
        List<OrderGuideItemView> ogItems = WebFormUtil.fillOutOrderGuideItemsList(orderGuideService, filterResultForm.getCatalogId(), selected);
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
                method.invoke(targetForm, ogItems);
            }
        }

        logger.info("returnSelected()=> END.");

        return new Gson().toJson(ogItems);
    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT) LocateItemOrderGuideFilterResultForm form,
                @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "itemOrderGuide/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT)
    public LocateItemOrderGuideFilterResultForm init(HttpSession session) {

        LocateItemOrderGuideFilterResultForm locateItemFilterResult = (LocateItemOrderGuideFilterResultForm) session.getAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT);

        if (locateItemFilterResult == null) {
            locateItemFilterResult = new LocateItemOrderGuideFilterResultForm();
        }

        return locateItemFilterResult;

    }

    @ModelAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER)
    public LocateItemOrderGuideFilterForm initFilter(HttpSession session) {
        LocateItemOrderGuideFilterForm locateItemFilter = (LocateItemOrderGuideFilterForm) session.getAttribute(SessionKey.LOCATE_ITEM_ORDER_GUIDE_FILTER);
        return initFilter(locateItemFilter, false);
    }

    private LocateItemOrderGuideFilterForm initFilter(LocateItemOrderGuideFilterForm locateItemFilter, boolean init) {

        if (locateItemFilter == null) {
            locateItemFilter = new LocateItemOrderGuideFilterForm();
        }

        // init at once
        if (init && !locateItemFilter.isInitialized()) {
            locateItemFilter.initialize();
        }

        return locateItemFilter;
    }


}
