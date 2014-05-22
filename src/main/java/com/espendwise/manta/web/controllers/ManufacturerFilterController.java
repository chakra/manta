package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.ManufacturerListView;
import com.espendwise.manta.service.ManufacturerService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.ManufacturerListViewCriteria;
import com.espendwise.manta.web.forms.ManufacturerFilterForm;
import com.espendwise.manta.web.forms.ManufacturerFilterResultForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(UrlPathKey.MANUFACTURER.FILTER)
@SessionAttributes({SessionKey.MANUFACTURER_FILTER_RESULT, SessionKey.MANUFACTURER_FILTER})
@AutoClean(SessionKey.MANUFACTURER_FILTER_RESULT)
public class ManufacturerFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(ManufacturerFilterController.class);

    private ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerFilterController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.MANUFACTURER_FILTER) ManufacturerFilterForm filterForm) {
        return "manufacturer/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findManufacturers(WebRequest request,
                               @ModelAttribute(SessionKey.MANUFACTURER_FILTER_RESULT) ManufacturerFilterResultForm form,
                               @ModelAttribute(SessionKey.MANUFACTURER_FILTER) ManufacturerFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "manufacturer/filter";
        }

        form.reset();
        
        ManufacturerListViewCriteria criteria = new ManufacturerListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.MANUFACTURER);

        criteria.setManufacturerName(filterForm.getManufacturerName());
        criteria.setManufacturerNameFilterType(filterForm.getManufacturerNameFilterType());
        criteria.setManufacturerId(filterForm.getManufacturerId());
        criteria.setStoreId(getStoreId());
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        List<ManufacturerListView> manufacturers = manufacturerService.findManufacturersByCriteria(criteria);
        
        form.setManufacturers(manufacturers);

        WebSort.sort(form, ManufacturerListView.MANUFACTURER_NAME);

        return "manufacturer/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.MANUFACTURER_FILTER_RESULT) ManufacturerFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "manufacturer/filter";
    }

    @ModelAttribute(SessionKey.MANUFACTURER_FILTER_RESULT)
    public ManufacturerFilterResultForm init(HttpSession session) {

        ManufacturerFilterResultForm manufacturerFilterResult = (ManufacturerFilterResultForm) session.getAttribute(SessionKey.MANUFACTURER_FILTER_RESULT);

        if (manufacturerFilterResult == null) {
            manufacturerFilterResult = new ManufacturerFilterResultForm();
        }

        return manufacturerFilterResult;

    }

    @ModelAttribute(SessionKey.MANUFACTURER_FILTER)
    public ManufacturerFilterForm initFilter(HttpSession session) {

        ManufacturerFilterForm manufacturerFilter = (ManufacturerFilterForm) session.getAttribute(SessionKey.MANUFACTURER_FILTER);

        if (manufacturerFilter == null || !manufacturerFilter.isInitialized()) {
            manufacturerFilter = new ManufacturerFilterForm();
            manufacturerFilter.initialize();
        }

        return manufacturerFilter;

    }

}


