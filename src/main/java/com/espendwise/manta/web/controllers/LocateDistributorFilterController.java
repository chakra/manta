package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.service.DistributorService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.Locate;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.DistributorListViewCriteria;
import com.espendwise.manta.web.forms.LocateDistributorFilterForm;
import com.espendwise.manta.web.forms.LocateDistributorFilterResultForm;
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
@RequestMapping(UrlPathKey.LOCATE.LOCATE_DISTRIBUTOR)
@SessionAttributes({SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT, SessionKey.LOCATE_DISTRIBUTOR_FILTER})
@AutoClean(SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT)
public class LocateDistributorFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(LocateDistributorFilterController.class);

    private DistributorService distributorService;

    @Autowired
    public LocateDistributorFilterController(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    @RequestMapping(value = {"", "/multi"}, method = RequestMethod.GET)
    public String multiFilter(@ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER) LocateDistributorFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT) LocateDistributorFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(true);
        return "distributor/locate";
    }

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String singleFilter(@ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER) LocateDistributorFilterForm filterForm, @ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT) LocateDistributorFilterResultForm filterResultForm) {
        initFilter(filterForm, true);
        filterResultForm.setMultiSelected(false);
        return "distributor/locate";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findDistributors(WebRequest request,
                               @ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT) LocateDistributorFilterResultForm form,
                               @ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER) LocateDistributorFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "distributor/locate";
        }

        form.reset();

        DistributorListViewCriteria criteria = new DistributorListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.DISTRIBUTOR);

        if (Utility.isSet(filterForm.getDistributorId())) {
            criteria.setDistributorId(filterForm.getDistributorId());
        }
        if (Utility.isSet(filterForm.getDistributorName())) {
            criteria.setDistributorName(filterForm.getDistributorName());
            criteria.setDistributorNameFilterType(filterForm.getDistributorNameFilterType());
        }

        criteria.setStoreId(getStoreId());
        criteria.setActiveOnly(!Utility.isTrue(filterForm.getShowInactive()));

        List<DistributorListView> distributors = distributorService.findDistributorsByCriteria(criteria);

        SelectableObjects<DistributorListView> selectableObj = new SelectableObjects<DistributorListView>(
                distributors,
                new ArrayList<DistributorListView>(),
                AppComparator.DISTRIBUTOR_LIST_VIEW_COMPARATOR
        );
        form.setSelectedDistributors(selectableObj);

        WebSort.sort(form, DistributorListView.DISTRIBUTOR_NAME);

        return "distributor/locate";

    }

    @ResponseBody
    @RequestMapping(value = "/selected", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String returnSelected(HttpSession session,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @RequestParam(value = "index", required = false) String index,
                                 @ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT) LocateDistributorFilterResultForm filterResultForm) throws Exception {

        logger.info("returnSelected()=> BEGIN, filter:" + filter);

        List<DistributorListView> selected = filterResultForm.getSelectedDistributors().getSelected();
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
    public String sort(@ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT) LocateDistributorFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "distributor/locate";
    }

    @ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT)
    public LocateDistributorFilterResultForm init(HttpSession session) {

        LocateDistributorFilterResultForm locateDistributorFilterResult = (LocateDistributorFilterResultForm) session.getAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER_RESULT);

        if (locateDistributorFilterResult == null) {
            locateDistributorFilterResult = new LocateDistributorFilterResultForm();
        }

        return locateDistributorFilterResult;

    }

    @ModelAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER)
    public LocateDistributorFilterForm initFilter(HttpSession session) {
        LocateDistributorFilterForm locateDistributorFilter = (LocateDistributorFilterForm) session.getAttribute(SessionKey.LOCATE_DISTRIBUTOR_FILTER);
        return initFilter(locateDistributorFilter, false);
    }

    private LocateDistributorFilterForm initFilter(LocateDistributorFilterForm locateDistributorFilter, boolean init) {

        if (locateDistributorFilter == null) {
            locateDistributorFilter = new LocateDistributorFilterForm();
        }

        // init at once
        if (init && !locateDistributorFilter.isInitialized()) {
            locateDistributorFilter.initialize();
        }

        return locateDistributorFilter;
    }


}


