package com.espendwise.manta.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.DataBinder;
import org.apache.log4j.Logger;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebSort;
import com.espendwise.manta.web.forms.MasterItemFilterForm;
import com.espendwise.manta.web.forms.MasterItemFilterResultForm;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.service.ItemService;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.ItemListViewCriteria;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.model.view.ItemListView;
import com.espendwise.manta.model.view.CatalogListView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(UrlPathKey.MASTER_ITEM.FILTER)
@SessionAttributes({SessionKey.MASTER_ITEM_FILTER_RESULT, SessionKey.MASTER_ITEM_FILTER})
@AutoClean(SessionKey.MASTER_ITEM_FILTER_RESULT)
public class MasterItemFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(MasterItemFilterController.class);

    private ItemService itemService;
    private CatalogService catalogService;

    @Autowired
    public MasterItemFilterController(ItemService itemService, CatalogService catalogService) {
        this.itemService = itemService;
        this.catalogService = catalogService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.MASTER_ITEM_FILTER)MasterItemFilterForm filterForm) {
        return "masterItem/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findItems(WebRequest request,
                               @ModelAttribute(SessionKey.MASTER_ITEM_FILTER_RESULT)MasterItemFilterResultForm form,
                               @ModelAttribute(SessionKey.MASTER_ITEM_FILTER) MasterItemFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "masterItem/filter";
        }

        form.reset();

        ItemListViewCriteria criteria = new ItemListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.ITEM);

        criteria.setStoreId(getStoreId());
        criteria.setCatalogId(getStoreCatalogId(getStoreId())) ;

        criteria.setItemId(filterForm.getItemId());
        criteria.setItemName(filterForm.getItemName());
        criteria.setItemNameFilterType(filterForm.getItemNameFilterType());
        criteria.setItemCategory(filterForm.getItemCategory());
        criteria.setItemCategoryFilterType(filterForm.getItemCategoryFilterType());
        criteria.setDistributor(filterForm.getDistributor());
        criteria.setManufacturer(filterForm.getManufacturer());
        criteria.setItemSku(filterForm.getItemSku());
        criteria.setItemSkuFilterType(filterForm.getItemSkuFilterType());
        criteria.setItemSkuTypeFilterType(filterForm.getItemSkuTypeFilterType());
        criteria.setGreenCertified(Utility.isTrue(filterForm.getGreenCertified()));
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        criteria.setShowMfgInfo(Utility.isTrue(filterForm.getShowMfgInfo()));
        criteria.setShowDistInfo(Utility.isTrue(filterForm.getShowDistInfo()));

        criteria.setItemProperty(filterForm.getItemProperty());
        criteria.setItemPropertyCd(filterForm.getItemPropertyCd());

        List<ItemListView> items = itemService.findItemsByCriteria(criteria);

        form.setItems(items);

        WebSort.sort(form, ItemListView.ITEM_NAME);

        return "masterItem/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.MASTER_ITEM_FILTER_RESULT) MasterItemFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "masterItem/filter";
    }

    @ModelAttribute(SessionKey.MASTER_ITEM_FILTER_RESULT)
    public MasterItemFilterResultForm init(HttpSession session) {

        MasterItemFilterResultForm masterItemFilterResult = (MasterItemFilterResultForm) session.getAttribute(SessionKey.MASTER_ITEM_FILTER_RESULT);

        if (masterItemFilterResult == null) {
            masterItemFilterResult = new MasterItemFilterResultForm();
        }

        return masterItemFilterResult;

    }

    @ModelAttribute(SessionKey.MASTER_ITEM_FILTER)
    public MasterItemFilterForm initFilter(HttpSession session) {

        MasterItemFilterForm masterItemFilter = (MasterItemFilterForm) session.getAttribute(SessionKey.MASTER_ITEM_FILTER);

        if (masterItemFilter == null || !masterItemFilter.isInitialized()) {
            masterItemFilter = new MasterItemFilterForm();
            masterItemFilter.initialize();
        }

        return masterItemFilter;

    }

    private Long getStoreCatalogId(Long storeId) {
        CatalogListViewCriteria catalogCriteria = new CatalogListViewCriteria(storeId, Constants.FILTER_RESULT_LIMIT.CATALOG);
        catalogCriteria.setCatalogType(RefCodeNames.CATALOG_TYPE_CD.STORE);
        catalogCriteria.setActiveOnly(true);
        List<CatalogListView> catalogs = catalogService.findCatalogsByCriteria(catalogCriteria);

        if (catalogs != null) {
            return catalogs.get(0).getCatalogId();
        } else {
            return new Long(0);
        }
    }



}
