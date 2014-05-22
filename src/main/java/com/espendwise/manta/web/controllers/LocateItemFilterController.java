package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;
import com.espendwise.manta.util.validation.ApplicationValidator;
import com.espendwise.manta.web.forms.LocateItemFilterForm;
import com.espendwise.manta.web.forms.LocateItemFilterResultForm;
import com.espendwise.manta.web.forms.WebForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import com.espendwise.manta.web.util.WorkflowUtil;
import com.espendwise.manta.web.validator.SimpleFilterFormFieldValidator;
import com.espendwise.manta.web.validator.WorkflowWebUpdateExceptionResolver;
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
import java.util.Set;

@Controller @Locate
@RequestMapping(UrlPathKey.LOCATE.LOCATE_ITEM)
@SessionAttributes({SessionKey.LOCATE_ITEM_FILTER_RESULT, SessionKey.LOCATE_ITEM_FILTER})
@AutoClean(SessionKey.LOCATE_ITEM_FILTER_RESULT)
public class LocateItemFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateItemFilterController.class);

    private CatalogService itemService;

    @Autowired
    public LocateItemFilterController(CatalogService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@ModelAttribute(SessionKey.LOCATE_ITEM_FILTER) LocateItemFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER_RESULT) LocateItemFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(true);
        return "item/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@ModelAttribute(SessionKey.LOCATE_ITEM_FILTER) LocateItemFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER_RESULT) LocateItemFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(false);
        return "item/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findItems(WebRequest request,
                               @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER_RESULT) LocateItemFilterResultForm form,
                               @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER) LocateItemFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "item/locate";
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
        criteria.setCatalogId(WorkflowUtil.getStoreCatalogId(getStoreId()));
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.ITEM);

        List<ProductListView> items = itemService.findProductsByCriteria(criteria);
        SelectableObjects<ProductListView> selectableObj = new SelectableObjects<ProductListView>(
                items,
                new ArrayList<ProductListView>(),
                AppComparator.ITEM_LIST_VIEW_COMPARATOR
        );
        form.setSelectedItems(selectableObj);

        validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
        }

        WebSort.sort(form, ProductListView.ITEM_NAME);

        return "item/locate";

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
    public String sort(@ModelAttribute(SessionKey.LOCATE_ITEM_FILTER_RESULT) LocateItemFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "item/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_ITEM_FILTER_RESULT)
    public LocateItemFilterResultForm init(HttpSession session) {

        LocateItemFilterResultForm locateItemFilterResult = (LocateItemFilterResultForm) session.getAttribute(SessionKey.LOCATE_ITEM_FILTER_RESULT);

        if (locateItemFilterResult == null) {
            locateItemFilterResult = new LocateItemFilterResultForm();
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


