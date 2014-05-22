package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.SiteHeaderView;
import com.espendwise.manta.model.view.SiteHierarchyView;
import com.espendwise.manta.service.SiteHierarchyService;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.*;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.validator.SiteHierarchyWebUpdateExceptionResolver;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Controller
@RequestMapping(UrlPathKey.SITE.SITE_HIERARCHY)
public class SiteSiteHierarchyController extends BaseController {

    private static final Logger logger = Logger.getLogger(SiteSiteHierarchyController.class);

    private SiteService siteService;
    private SiteHierarchyService siteHierarchyService;

    @Autowired
    public SiteSiteHierarchyController(SiteHierarchyService siteHierarchyService, SiteService siteService) {
        this.siteService = siteService;
        this.siteHierarchyService = siteHierarchyService;
    }

    @Override
    public void initBinder(DataBinder binder) {
        super.initBinder(binder);
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new SiteHierarchyWebUpdateExceptionResolver());

        return "site/locationHierarchy";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.SITE_SITE_HIERARCHY) SiteSiteHierarchyForm form, @PathVariable(IdPathKey.LOCATION_ID) Long locationId) {

        logger.info("show()=> BEGIN, LocationID: " + locationId);

        SiteHeaderView location = findLocation(locationId);

        SiteHierarchyLevelItemsForm levelItemsMap = new SiteHierarchyLevelItemsForm();
        SortedMap<Integer, Long> selectedLevelItems = new TreeMap<Integer, Long>();

        List<BusEntityData> siteHierLevels = siteHierarchyService.findSiteHierarchyLevelsOfAccount(location.getAccountId());

        if (siteHierLevels.size() > 0) {

            WebSort.sort(siteHierLevels, BusEntityData.LONG_DESC);

            List<Long> siteHierarchyLevels = siteHierarchyService.findSiteConfiguration(locationId, siteHierLevels.size());

            logger.info("show()=> siteHierarchyLevels: " + siteHierarchyLevels);

            List<SiteHierarchyView> siteHierarchyViews = siteHierarchyService.findSiteConfiguration(siteHierarchyLevels, siteHierLevels.get(0));

            List<SiteHierarchyLevelForm> levels = SiteHierarchyLevelFormBinder.obj2Form(0,
                    siteHierLevels,
                    Utility.emptyList(SiteHierarchyLevelForm.class)
            );

            logger.info("show()=> levels: " +levels);

            WebSort.sort(levels, SiteHierarchyLevelForm.LONG_DESC);
            WebSort.sort(siteHierarchyViews, SiteHierarchyView.SITE_HIERARCHY_NUM);

            List<BusEntityData> items = siteHierarchyService.findSiteHierarchyChildItems(siteHierLevels.get(0).getBusEntityId(),
                    RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT,
                    RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL
            );

            levelItemsMap.put(0, Utility.toPairs(items));
            selectedLevelItems.put(0, !siteHierarchyViews.isEmpty()?siteHierarchyViews.get(0).getValueId():null);

            for (int i = 1; i < siteHierarchyViews.size(); i++) {

                items = siteHierarchyService.findSiteHierarchyChildItems(siteHierarchyViews.get(i - 1).getValueId(),
                        RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT,
                        RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL);

                levelItemsMap.put(i, Utility.toPairs(items));
                selectedLevelItems.put(i, siteHierarchyViews.get(i).getValueId());

            }

            form.setSelectedLevelItems(selectedLevelItems);
            form.setLocationId(locationId);
            form.setLevelItems(levelItemsMap);
            form.setLevels(levels);
            form.setSiteHierarchy(siteHierarchyViews);

        }

        logger.info("show()=> selectedLevelItems: " + selectedLevelItems);
        logger.info("show()=> levelItemsMap: " + levelItemsMap);
        logger.info("show()=> locationId: " + locationId);
        logger.info("show()=> levels: " + form.getLevels());
        logger.info("show()=> siteHierarchy: " + form.getSiteHierarchy());


        logger.info("show()=> END.");

        return "site/locationHierarchy";

    }

    private SiteHeaderView findLocation(Long locationId) {
        return siteService.findSiteHeader(getStoreId(), locationId);
    }

    @SuccessMessage
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.SITE_SITE_HIERARCHY) SiteSiteHierarchyForm form) throws Exception {

        logger.info("save()=> BEGIN, form: " + form);

        WebErrors webErrors = new WebErrors(request);

        SiteHeaderView location = findLocation(form.getLocationId());

        logger.info("save()=> location: " + location);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "site/locationHierarchy";
        }

        try {

            siteHierarchyService.createOrReplaceSiteHierarchySiteAssoc(
                    form.getSelectedLevelItems().get(form.getSelectedLevelItems().lastKey()),
                    form.getLocationId()

            );

        } catch (ValidationException e) {

            return  handleValidationException(e, request);

        }

        logger.info("save()=> END.");

        return redirect(UrlPathAssistent.createPath(request, UrlPathKey.SITE.SITE_HIERARCHY));

    }


    @ResponseBody
    @RequestMapping(value ="/change-level/{levelNumber}", method = RequestMethod.POST, produces="application/json; charset=UTF-8")
    public String changeLevel(@RequestParam("level") Long level, @PathVariable Integer levelNumber) {

        logger.info("changeLevel()=> BEGIN, levelNumber: " + levelNumber+", level: "+level);

        List<BusEntityData> items = siteHierarchyService.findSiteHierarchyChildItems(level,
                RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL);

        WebSort.sort(items, BusEntityData.LONG_DESC);

        SiteHierarchyAsynchForm form = new SiteHierarchyAsynchForm();
        form.setSubLevels(new SiteHierarchySubLevelsAsynchForm());

        form.getSubLevels().setLevel(levelNumber);
        form.getSubLevels().setRedraw(true);
        form.getSubLevels().setSelectedValue(null);
        form.getSubLevels().setList(Utility.toPairs(items));

        logger.info("treetop()=> form: " + form);

        return new Gson().toJson(form);
    }

    @ResponseBody
    @RequestMapping(value ="/tree/top", method = RequestMethod.POST, produces="application/json; charset=UTF-8")
    public String treetop(@RequestParam("levelId") Long levelId) {

        logger.info("treetop()=> BEGIN, levelId: " + levelId);

        List<BusEntityData> items = siteHierarchyService.findSiteHierarchyChildItems(levelId,
                RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_ACCOUNT);

        WebSort.sort(items, BusEntityData.LONG_DESC);

        logger.info("treetop()=> items: " + items);

        return new Gson().toJson(Utility.toPairs(Utility.toList(items.get(0))));
    }


    @ResponseBody
    @RequestMapping(value ="/tree", method = RequestMethod.POST, produces="application/json; charset=UTF-8")
    public String tree(@RequestParam("levelId") Long levelId) {

        logger.info("tree()=> BEGIN, levelId: " + levelId);

        List<BusEntityData> items = siteHierarchyService.findSiteHierarchyChildItems(levelId,
                RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL);

        logger.info("tree()=> items: " + items);

        return new Gson().toJson(Utility.toPairs(items));

    }


    @ModelAttribute(SessionKey.SITE_SITE_HIERARCHY)
    public SiteSiteHierarchyForm initFilter() {
        return new SiteSiteHierarchyForm();
    }


}

