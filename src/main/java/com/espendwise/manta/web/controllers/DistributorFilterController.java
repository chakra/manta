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

import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.service.DistributorService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.DistributorListViewCriteria;
import com.espendwise.manta.web.forms.DistributorFilterForm;
import com.espendwise.manta.web.forms.DistributorFilterResultForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.DISTRIBUTOR.FILTER)
@SessionAttributes({SessionKey.DISTRIBUTOR_FILTER_RESULT, SessionKey.DISTRIBUTOR_FILTER})
@AutoClean(SessionKey.DISTRIBUTOR_FILTER_RESULT)
public class DistributorFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(DistributorFilterController.class);

    private DistributorService distributorService;

    @Autowired
    public DistributorFilterController(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.DISTRIBUTOR_FILTER) DistributorFilterForm filterForm) {
        return "distributor/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findDistributors(WebRequest request,
                               @ModelAttribute(SessionKey.DISTRIBUTOR_FILTER_RESULT) DistributorFilterResultForm form,
                               @ModelAttribute(SessionKey.DISTRIBUTOR_FILTER) DistributorFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "distributor/filter";
        }

        form.reset();
        
        DistributorListViewCriteria criteria = new DistributorListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.DISTRIBUTOR);

        criteria.setDistributorName(filterForm.getDistributorName());
        criteria.setDistributorNameFilterType(filterForm.getDistributorNameFilterType());
        criteria.setDistributorId(filterForm.getDistributorId());
        criteria.setStoreId(getStoreId());
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        List<DistributorListView> distributors = distributorService.findDistributorsByCriteria(criteria);
        
        form.setDistributors(distributors);

        WebSort.sort(form, DistributorListView.DISTRIBUTOR_NAME);

        return "distributor/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.DISTRIBUTOR_FILTER_RESULT) DistributorFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "distributor/filter";
    }

    @ModelAttribute(SessionKey.DISTRIBUTOR_FILTER_RESULT)
    public DistributorFilterResultForm init(HttpSession session) {

    	DistributorFilterResultForm distributorFilterResult = (DistributorFilterResultForm) session.getAttribute(SessionKey.DISTRIBUTOR_FILTER_RESULT);

        if (distributorFilterResult == null) {
        	distributorFilterResult = new DistributorFilterResultForm();
        }

        return distributorFilterResult;

    }

    @ModelAttribute(SessionKey.DISTRIBUTOR_FILTER)
    public DistributorFilterForm initFilter(HttpSession session) {

    	DistributorFilterForm distributorFilter = (DistributorFilterForm) session.getAttribute(SessionKey.DISTRIBUTOR_FILTER);

        if (distributorFilter == null || !distributorFilter.isInitialized()) {
        	distributorFilter = new DistributorFilterForm();
        	distributorFilter.initialize();
        }

        return distributorFilter;

    }

}


