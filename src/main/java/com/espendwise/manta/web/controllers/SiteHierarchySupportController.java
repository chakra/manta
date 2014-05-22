package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.SiteHierarchyService;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.FilterValue;
import com.espendwise.manta.util.criteria.SiteHierarchySearchCriteria;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.*;
import com.espendwise.manta.web.util.SiteHierarchyLevelFormBinder;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import com.espendwise.manta.web.validator.SiteHierarchyWebUpdateExceptionResolver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SiteHierarchySupportController  extends BaseController{

    private static final Logger logger = Logger.getLogger(SiteHierarchySupportController.class);


    protected SiteHierarchyService siteHierarchyService;
    protected AccountService accountService;
    protected SiteService siteService;

    public  SiteHierarchySupportController() {
    }


    @Autowired
    public  SiteHierarchySupportController(AccountService accountService, SiteHierarchyService siteHierarchyService, SiteService siteService) {
        this.siteHierarchyService = siteHierarchyService;
        this.accountService = accountService;
        this.siteService = siteService;
    }



    protected String addline(AccountSiteHierarchyManageForm form) {
        form.setItems(Utility.listNN(form.getItems()));
        form.getItems().add(SiteHierarchyLevelFormBinder.createHierarhyEmptyLevel(form.getItems().size()));
        return "account/locationHierarchy/manage";
    }


    private String handleSaveValidationException(WebRequest request, ValidationException ex) {
        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new SiteHierarchyWebUpdateExceptionResolver());
        return "account/locationHierarchy/manage";
    }


    public String save(WebRequest request, AccountSiteHierarchyManageForm form, AccountSiteHierarchyManageFilterForm filterForm) throws Exception {

        logger.info("save()=> BEGIN, form: " + form);

        WebErrors errors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if(!validationErrors.isEmpty()) {
            errors.putErrors(validationErrors);
            return "account/locationHierarchy/manage";
        }


        BusEntityData account = findAccount(form.getAccountId());

        List<BusEntityData> dbSiteHierLevels = siteHierarchyService.findSiteHierarchyChildItems(
                form.getLevelId(),
                RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL);

        UpdateRequest<BusEntityData> updateRequest = SiteHierarchyLevelFormBinder.createUpdateObject(
                false,
                form.getItems(),
                dbSiteHierLevels
        );

        logger.info("save()=> updateRequest: " + updateRequest);

        try {

            siteHierarchyService.configureSiteHierarchy(
                    getStoreId(),
                    form.getLevelId(),
                    form.getLayerId(),
                    RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_LEVEL,
                    RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL,
                    updateRequest
            );

        } catch (ValidationException e) {

            return handleSaveValidationException(request, e);

        }

        WebAction.success(request);

        Long[] levels = Utility.isSet(form.getLevels()) ? form.getLevels().toArray(new Long[form.getLevels().size()]) : new Long[0];

        manageLayer(
                form.getLevelId(),
                account.getBusEntityId(),
                form,
                filterForm,
                levels
        );

        logger.info("save()=> END, OK.");

        return "account/locationHierarchy/manage";

    }

    public BusEntityData findAccount(Long accountId) {

        StoreAccountCriteria criteria = new StoreAccountCriteria();
        criteria.setStoreId(getStoreId());
        criteria.setAccountId(accountId);

        List<BusEntityData> acconuts = accountService.findStoreAccountBusDatas(criteria);
        if (!acconuts.isEmpty()) {
            return acconuts.get(0);
        } else {
            throw SystemError.accountNotFound(getStoreId(), accountId);
        }
    }


    public void configureHierarchy(Long hierarchyId, Long accountId, AccountSiteHierarchyConfigurationForm form, Long... levels) {

        logger.info("configureHierarchy()=> BEGIN" +
                ",\n accountID: " + accountId +
                ",\n levelItemId: " + hierarchyId +
                ",\n levels: " + Arrays.toString(levels)
        );


        BusEntityData account = findAccount(accountId);

        List<BusEntityData> siteHierLayers = siteHierarchyService.findSiteHierarchyLevelsOfAccount(account.getBusEntityId());

        WebSort.sort(siteHierLayers, BusEntityData.LONG_DESC);

        BusEntityData level = siteHierarchyService.findSiteHierarchyData(
                hierarchyId,
                levels.length == 0 ? RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL : RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT
        );

        List<SiteHierarchyLayerLevelForm> layerLayerLevels = new ArrayList<SiteHierarchyLayerLevelForm>();

        for (int i = 1; i < levels.length; i++) {

            BusEntityData item = siteHierarchyService.findSiteHierarchyData(
                    levels[i],
                    i == 0? RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL : RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT
            );

            layerLayerLevels.add(
                    new SiteHierarchyLayerLevelForm(
                            siteHierLayers.get(i-1).getBusEntityId(),
                            siteHierLayers.get(i-1).getShortDesc(),
                            item.getBusEntityId(),
                            item.getShortDesc()
                    )
            );

        }


        BusEntityData item = siteHierarchyService.findSiteHierarchyData(
                hierarchyId,
                RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT
        );


        layerLayerLevels.add(
                new SiteHierarchyLayerLevelForm(
                        siteHierLayers.get(levels.length - 1).getBusEntityId(),
                        siteHierLayers.get(levels.length - 1).getShortDesc(),
                        item.getBusEntityId(),
                        item.getShortDesc()
                )
        );


        form.setLayerLevels(layerLayerLevels);
        form.setLevelNum(levels.length);
        form.setLevels(Arrays.asList(levels));
        form.setLevelId(hierarchyId);
        form.setLevelName(level != null ? level.getShortDesc() : null);
        form.setLayerName(levels.length > 0 ? siteHierLayers.get(levels.length - 1).getShortDesc() : null);
        form.setLayerId(levels.length > 0 ? siteHierLayers.get(levels.length - 1).getBusEntityId() : null);
        form.setAccountId(account.getBusEntityId());



        logger.info("configureHierarchy()=> END.");

    }


    public void manageLayer(Long manageLevel, Long accountId, AccountSiteHierarchyManageForm form, AccountSiteHierarchyManageFilterForm filterForm, Long... levels) {


        logger.info("manageLayer()=> BEGIN" +
                ",\n accountID: " + accountId +
                ",\n manageLevel: " + manageLevel +
                ",\n levels: " + Arrays.toString(levels)
        );

        BusEntityData account = findAccount(accountId);

        List<BusEntityData> siteHierLayers = siteHierarchyService.findSiteHierarchyLevelsOfAccount(account.getBusEntityId());

        WebSort.sort(siteHierLayers, BusEntityData.LONG_DESC);

        BusEntityData level = siteHierarchyService.findSiteHierarchyData(
                manageLevel,
                levels.length == 0 ? RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL : RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT
        );


        SiteHierarchySearchCriteria criteria = new SiteHierarchySearchCriteria();
        if (filterForm != null) {
            criteria.setId(parseNumberNN(filterForm.getFilterId()));
            criteria.setName(new FilterValue(filterForm.getFilterValue(), filterForm.getFilterType()));
        }

        List<BusEntityData> siteHierLevelItems = siteHierarchyService.findSiteHierarchyChildItems(
                manageLevel,
                RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL,
                criteria
        );

        List<SiteHierarchyLayerLevelForm> layerLayerLevels = new ArrayList<SiteHierarchyLayerLevelForm>();

        for (int i = 0; i < levels.length; i++) {

            layerLayerLevels.add(
                    new SiteHierarchyLayerLevelForm(
                            siteHierLayers.get(i).getBusEntityId(),
                            siteHierLayers.get(i).getShortDesc(),
                            levels[i],
                            null
                    )
            );

        }

        form.setLayerLevels(layerLayerLevels);
        form.setConfigure(levels.length == siteHierLayers.size()-1);
        form.setLevelNum(levels.length);
        form.setLevels(Arrays.asList(levels));
        form.setLevelId(manageLevel);
        form.setLevelName(level != null ? level.getShortDesc() : null);
        form.setLayerName(siteHierLayers.get(levels.length).getShortDesc());
        form.setLayerId(siteHierLayers.get(levels.length).getBusEntityId());
        form.setParentLevelId(levels.length == 0 ? accountId : levels[levels.length - 1]);
        form.setItems(SiteHierarchyLevelFormBinder.obj2Form(levels.length + 1, siteHierLevelItems, new ArrayList<SiteHierarchyLevelForm>()));
        form.setAccountId(account.getBusEntityId());

        logger.info("manageLayer()=> END.");

    }

    public void filter(WebRequest request, Long manageLevel, Long accountId, AccountSiteHierarchyManageForm form, AccountSiteHierarchyManageFilterForm filterForm, Long... levels) throws Exception {


        WebErrors errors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if(!validationErrors.isEmpty()) {
            errors.putErrors(validationErrors);
            return;
        }

        manageLayer(
                manageLevel,
                accountId,
                form,
                filterForm,
                levels
        );


    }


    public String findLocations(WebRequest request, AccountSiteHierarchyConfigurationForm form) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "account/locationHierarchy/configuration";
        }

        form.reset();

        SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);

        criteria.setSiteId(parseNumberNN(form.getSiteId()));
        criteria.setSiteName(form.getSiteName(), form.getSiteNameFilterType());
        criteria.setRefNumber(form.getRefNumber(), form.getRefNumberFilterType());
        criteria.setCity(form.getCity(), Constants.FILTER_TYPE.START_WITH);
        criteria.setState(form.getState(), Constants.FILTER_TYPE.START_WITH);
        criteria.setPostalCode(form.getPostalCode(), Constants.FILTER_TYPE.START_WITH);
        criteria.setStoreId(getStoreId());
        criteria.setAccountIds(Utility.toList(form.getAccountId()));
        criteria.setActiveOnly(!Utility.isTrue(form.getShowInactive()));
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.SITE_HIERARCHY_SITE);

        List<SiteListView> sites = siteService.findSitesByCriteria(criteria);

        List<BusEntityAssocData> configuredSites = siteHierarchyService.findConfigurationFor(form.getLevelId(),
                Utility.toIds(sites),
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT
        );

        SiteListViewSelectableObjects selObjs = new SiteListViewSelectableObjects(sites,
                new ArrayList<SiteListView>(),
                AppComparator.SITE_LIST_VIEW_COMPARATOR
        );

        Iterator<SiteListViewSelectableObjects.SelectableObject> it = selObjs.getSelectableObjects().iterator();
        while (it.hasNext()) {

            SiteListViewSelectableObjects.SelectableObject x = it.next();

            for (BusEntityAssocData y : configuredSites) {
                if (y.getBusEntity1Id() == x.getValue().getSiteId().longValue()) {
                    x.setSelected(true);
                    x.setOriginallySelected(true);
                    break;
                }
            }

            if (Utility.isTrue(form.getShowConfiguredOnly()) && !Utility.isTrue(x.isSelected())) {
                it.remove();
            }
        }

        WebSort.sort(sites, SiteListView.SITE_NAME);

        form.setSites(selObjs);

        return "account/locationHierarchy/configuration";

    }

    protected String saveConfiguration(WebRequest request, AccountSiteHierarchyConfigurationForm configurationForm) throws Exception {

        logger.info("saveConfiguration()=> configurationForm: " + configurationForm.getSites().getSelected());

        try{

            siteHierarchyService.configureSiteHierarchyWithSites(
                    getStoreId(),
                    configurationForm.getAccountId(),
                    configurationForm.getLevelId(),
                    new UpdateRequest<Long>(
                            Utility.toIds(configurationForm.getSites().getNewlySelected()),
                            Utility.emptyList(Long.class),
                            Utility.toIds(configurationForm.getSites().getNewlyDeselected())
                    )
            );


        } catch (ValidationException e) {

           handleSaveValidationException(request, e);

        }  finally {

            configurationForm.getSites().resetState();

        }

        return "account/locationHierarchy/configuration";

    }

}
