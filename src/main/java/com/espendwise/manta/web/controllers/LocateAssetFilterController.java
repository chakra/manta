package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.AssetListView;
import com.espendwise.manta.service.AssetService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.AssetListViewCriteria;
import com.espendwise.manta.web.forms.LocateAssetFilterForm;
import com.espendwise.manta.web.forms.LocateAssetFilterResultForm;
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
@RequestMapping(UrlPathKey.LOCATE.LOCATE_ASSET)
@SessionAttributes({SessionKey.LOCATE_ASSET_FILTER, SessionKey.LOCATE_ASSET_FILTER_RESULT})
@AutoClean(SessionKey.LOCATE_ASSET_FILTER_RESULT)
public class LocateAssetFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateAssetFilterController.class);

    private AssetService assetService;

    @Autowired
    public LocateAssetFilterController(AssetService assetService) {
        this.assetService = assetService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@RequestParam(value = "siteId", required = false) Long siteId,
                              @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER) LocateAssetFilterForm filterForm,
                              @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER_RESULT) LocateAssetFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        if (siteId != null) {
            filterForm.setSiteId(siteId);
        }
        filterResultForm.setMultiSelected(true);
        return "asset/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@RequestParam(value = "siteId", required = false) Long siteId,
                               @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER) LocateAssetFilterForm filterForm,
                               @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER_RESULT) LocateAssetFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        if (siteId != null) {
            filterForm.setSiteId(siteId);
        }
        filterResultForm.setMultiSelected(false);
        return "asset/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findAssets(WebRequest request,
                             @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER) LocateAssetFilterForm filterForm,
                             @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER_RESULT) LocateAssetFilterResultForm filterResultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "asset/locate";
        }

        filterResultForm.reset();

        AssetListViewCriteria criteria = new AssetListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.ASSET);

        if (Utility.isSet(filterForm.getAssetId())) {
            criteria.setAssetId(filterForm.getAssetId());
        }
        if (Utility.isSet(filterForm.getAssetName())) {
            criteria.setAssetName(filterForm.getAssetName());
            criteria.setAssetNameFilterType(filterForm.getAssetNameFilterType());
        }

        if (filterForm.getSiteId() != null) {
            criteria.setSiteId(filterForm.getSiteId());
        }

        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        List<AssetListView> assets = assetService.findAssetsByCriteria(criteria);

        SelectableObjects<AssetListView> selectableObj = new SelectableObjects<AssetListView>(
                assets,
                new ArrayList<AssetListView>(),
                AppComparator.ASSET_LIST_VIEW_COMPARATOR);

        filterResultForm.setSelectedAssets(selectableObj);

        WebSort.sort(filterResultForm, AssetListView.ASSET_NAME);

        return "asset/locate";

    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @RequestParam(value = "index", required = false) String index,
                                 @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER_RESULT) LocateAssetFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<AssetListView> selected = filterResultForm.getSelectedAssets().getSelected();
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
    public String sort(@ModelAttribute(SessionKey.LOCATE_ASSET_FILTER_RESULT) LocateAssetFilterResultForm form,
                       @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "asset/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER_RESULT)
    public LocateAssetFilterResultForm init(HttpSession session) {

        LocateAssetFilterResultForm locateAssetFilterResult = (LocateAssetFilterResultForm) session.getAttribute(SessionKey.LOCATE_ASSET_FILTER_RESULT);

        if (locateAssetFilterResult == null) {
            locateAssetFilterResult = new LocateAssetFilterResultForm();
        }

        return locateAssetFilterResult;

    }

    @ModelAttribute(SessionKey.LOCATE_ASSET_FILTER)
    public LocateAssetFilterForm initFilter(HttpSession session) {
        LocateAssetFilterForm locateAssetFilter = (LocateAssetFilterForm) session.getAttribute(SessionKey.LOCATE_ASSET_FILTER);
        return initFilter(locateAssetFilter, false);
    }

    private LocateAssetFilterForm initFilter(LocateAssetFilterForm locateAssetFilter, boolean init) {

        if (locateAssetFilter == null) {
            locateAssetFilter = new LocateAssetFilterForm();
        }

        // init at once
        if (init && !locateAssetFilter.isInitialized()) {
            locateAssetFilter.initialize();
        }

        return locateAssetFilter;
    }


}


