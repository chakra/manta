package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.web.forms.CorporateScheduleLocationFilterForm;
import com.espendwise.manta.web.forms.CorporateScheduleLocationFilterResultForm;
import com.espendwise.manta.web.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(UrlPathKey.CORPORATE_SCHEDULE.LOCATION)
@SessionAttributes({SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER, SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER_RESULT})
@AutoClean(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER)
public class CorporateScheduleLocationController extends BaseController {

    private static final Logger logger = Logger.getLogger(CorporateScheduleLocationController.class);

    private ScheduleService scheduleService;

    @Autowired
    public CorporateScheduleLocationController(ScheduleService scheduleService,
                                 AccountService accountService) {
        this.scheduleService = scheduleService;
    }

    @InitBinder
    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER) CorporateScheduleLocationFilterForm filterForm,
            @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER_RESULT) CorporateScheduleLocationFilterResultForm resultForm) {
    	filterForm.reset();
    	resultForm.reset();
    	return "corporateSchedule/location";
    }
    
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findSites(WebRequest request,
                            @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER) CorporateScheduleLocationFilterForm filterForm,
                            @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER_RESULT) CorporateScheduleLocationFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "corporateSchedule/location";
        }

        doSearch(filterForm, resultForm);

        return "corporateSchedule/location";

    }
    
    private void doSearch(CorporateScheduleLocationFilterForm filterForm, CorporateScheduleLocationFilterResultForm resultForm){
    	resultForm.reset();
        
        Long siteId = null;
        if (Utility.isSet(filterForm.getSiteId())) {
            siteId = Long.valueOf(filterForm.getSiteId());
        }
        SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);
        criteria.setScheduleId(filterForm.getScheduleId());
        criteria.setSiteId(siteId);
        criteria.setActiveOnly(!filterForm.getShowInactive());
        criteria.setSiteNameFilterType(filterForm.getSiteNameFilterType());
        criteria.setSiteName(filterForm.getSiteName());
        criteria.setRefNumber(filterForm.getReferenceNumber(), filterForm.getRefNumberFilterType());
        criteria.setCity(filterForm.getCity(), Constants.FILTER_TYPE.CONTAINS);
        criteria.setState(filterForm.getStateProvince(), Constants.FILTER_TYPE.CONTAINS);
        criteria.setPostalCode(filterForm.getPostalCode(), Constants.FILTER_TYPE.START_WITH);
        criteria.setConfiguredOnly(true);
        
        List<SiteListView> configured = scheduleService.findScheduleSitesByCriteria(criteria);
        
        criteria.setConfiguredOnly(false);

        List<SiteListView> sites = filterForm.getShowConfiguredOnly()
                            ? configured : scheduleService.findScheduleSitesByCriteria(criteria);

        logger.info("findSites()=> sites: " + sites.size());
        logger.info("findSites()=> configured: " + configured.size());

        SelectableObjects<SiteListView> selectableObj = new SelectableObjects<SiteListView>(
                                        sites,
                                        configured,
                                        AppComparator.SITE_LIST_VIEW_COMPARATOR);
        resultForm.setSites(selectableObj);
        
        WebSort.sort(resultForm, SiteListView.SITE_NAME);
    }
    		

    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@PathVariable(IdPathKey.CORPORATE_SCHEDULE_ID) Long scheduleId,
    					@ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER) CorporateScheduleLocationFilterForm filterForm,
                         @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER_RESULT) CorporateScheduleLocationFilterResultForm resultForm) throws Exception {

        logger.info("corporateSchedule/location/update()=> BEGIN");
        Long storeId = getStoreId();

        logger.info("corporateSchedule/location/update()=> scheduleId: " + scheduleId);
        logger.info("corporateSchedule/location/update()=> storeId: " + storeId);
        
        List<SiteListView> selected = resultForm.getSites().getNewlySelected();
        List<SiteListView> deselected = resultForm.getSites().getNewlyDeselected();
        
        scheduleService.configureScheduleSites(scheduleId,
                                           storeId,
                                           selected,
                                           deselected);
        
        // update the selectable list
        List<SiteListView> sites = resultForm.getSites().getValues();
        List<SiteListView> configured = resultForm.getSites().getSelected();
        SelectableObjects<SiteListView> selectableObj = new SelectableObjects<SiteListView>(
                sites,
                configured,
                AppComparator.SITE_LIST_VIEW_COMPARATOR);
        resultForm.setSites(selectableObj);
        
        //doSearch(filterForm, resultForm);

        logger.info("corporateSchedule/location/update()=> END.");
        
        return "corporateSchedule/location";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER_RESULT) CorporateScheduleLocationFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "corporateSchedule/location";
    }
    

    @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER_RESULT)
    public CorporateScheduleLocationFilterResultForm init(HttpSession session) {

        logger.info("init()=> init....");

        CorporateScheduleLocationFilterResultForm form = (CorporateScheduleLocationFilterResultForm) session.getAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER_RESULT);

        logger.info("init()=> form: " + form);        

        if (form == null) {
            form = new CorporateScheduleLocationFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }

        logger.info("init()=> init.OK!");

        return form;

    }
    
    @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER)
    public CorporateScheduleLocationFilterForm initFilter(HttpSession session, @PathVariable(IdPathKey.CORPORATE_SCHEDULE_ID) Long scheduleId) {

        logger.info("initFilter()=> init....");

        CorporateScheduleLocationFilterForm form = (CorporateScheduleLocationFilterForm) session.getAttribute(SessionKey.CORPORATE_SCHEDULE_LOCATION_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new CorporateScheduleLocationFilterForm(scheduleId);
            form.initialize();
        }

        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }

}
