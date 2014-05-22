package com.espendwise.manta.web.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.CorporateScheduleView;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.CorporateScheduleDataCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.CorporateScheduleFilterForm;
import com.espendwise.manta.web.forms.CorporateScheduleFilterResultForm;
import com.espendwise.manta.web.resolver.CorporateScheduleWebExceptionResolver;
import com.espendwise.manta.web.resolver.UserWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.CORPORATE_SCHEDULE.FILTER)
@SessionAttributes({SessionKey.CORPORATE_SCHEDULE_FILTER_RESULT, SessionKey.CORPORATE_SCHEDULE_FILTER})
@AutoClean(SessionKey.CORPORATE_SCHEDULE_FILTER_RESULT)
public class CorporateScheduleFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(CorporateScheduleFilterController.class);

    private ScheduleService corporateScheduleService;

    @Autowired
    public CorporateScheduleFilterController(ScheduleService corporateScheduleService) {
        this.corporateScheduleService = corporateScheduleService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String filter(@ModelAttribute(SessionKey.CORPORATE_SCHEDULE_FILTER) CorporateScheduleFilterForm filterForm) {
        logger.info("filter()=> attemp validate form: " + filterForm);
        return "corporateSchedule/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findCorporateSchedules(WebRequest request,
            @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_FILTER_RESULT) CorporateScheduleFilterResultForm form,
            @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_FILTER) CorporateScheduleFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        logger.info("findCorporateSchedules()=> attemp validate form: " + filterForm);
    	filterForm.setStoreId(getStoreId());
        try {
	        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
	        if (!validationErrors.isEmpty()) {
	            webErrors.putErrors(validationErrors);
	            return "corporateSchedule/filter";
	        } 
        } catch (ValidationException e) {
        	logger.info("filter() =======> handleValidationException ");
            return handleValidationException(e, request);
        }        	

        form.reset();

        AppLocale locale = getAppLocale();

        CorporateScheduleDataCriteria criteria = new CorporateScheduleDataCriteria();

        criteria.setCorporateScheduleId(filterForm.getFilterId());
        criteria.setName(filterForm.getFilterValue());
        criteria.setNameFilterType(filterForm.getFilterType());
        criteria.setCorporateScheduleDateFrom(parseDateNN(locale, filterForm.getCorporateScheduleDateFrom()));
        criteria.setCorporateScheduleDateTo(parseDateNN(locale, filterForm.getCorporateScheduleDateTo()));
        criteria.setStoreId(getStoreId());
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.CORPORATE_SCHEDULE);
        
        List<CorporateScheduleView> schedules = corporateScheduleService.findCorporateSchedulesByCriteria(criteria);

        form.setCorporateSchedules(schedules);

        WebSort.sort(form, CorporateScheduleView.SCHEDULE_NAME);

        return "corporateSchedule/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.CORPORATE_SCHEDULE_FILTER_RESULT) CorporateScheduleFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "corporateSchedule/filter";
    }

    @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_FILTER_RESULT)
    public CorporateScheduleFilterResultForm init(HttpSession session) {

        logger.info("init()=> init....");

        CorporateScheduleFilterResultForm corporateScheduleFilterResult = (CorporateScheduleFilterResultForm) session.getAttribute(SessionKey.CORPORATE_SCHEDULE_FILTER_RESULT);

        logger.info("init()=> corporateScheduleFilterResult: " + corporateScheduleFilterResult);

        if (corporateScheduleFilterResult == null) {
            corporateScheduleFilterResult = new CorporateScheduleFilterResultForm();
        }

        return corporateScheduleFilterResult;

    }

    @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_FILTER)
    public CorporateScheduleFilterForm initFilter(HttpSession session) {

        logger.info("initFilter(()=>  init....");

        CorporateScheduleFilterForm corporateScheduleFilter = (CorporateScheduleFilterForm) session.getAttribute(SessionKey.CORPORATE_SCHEDULE_FILTER);

        logger.info("initFilter()=> corporateScheduleFilter: " + corporateScheduleFilter);

        if (corporateScheduleFilter == null || !corporateScheduleFilter.isInitialized()) {
            corporateScheduleFilter = new CorporateScheduleFilterForm();
            corporateScheduleFilter.initialize();
        }

        return corporateScheduleFilter;

    }
    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new CorporateScheduleWebExceptionResolver());
        
        return "corporateSchedule/filter";
    }

}


