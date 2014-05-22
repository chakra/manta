package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.entity.StoreMessageListEntity;
import com.espendwise.manta.model.view.StoreMessageListView;
import com.espendwise.manta.service.StoreMessageService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.StoreMessageDataCriteria;
import com.espendwise.manta.web.forms.StoreMessageFilterForm;
import com.espendwise.manta.web.forms.StoreMessageFilterResultForm;
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
@RequestMapping(UrlPathKey.STORE_MESSAGE.FILTER)
@SessionAttributes({SessionKey.STORE_MESSAGE_FILTER_RESULT, SessionKey.STORE_MESSAGE_FILTER})
@AutoClean(SessionKey.STORE_MESSAGE_FILTER_RESULT)
public class StoreMessageFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(StoreMessageFilterController.class);

    private StoreMessageService storeMessageService;

    @Autowired
    public StoreMessageFilterController(StoreMessageService storeMessageService) {
        this.storeMessageService = storeMessageService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.STORE_MESSAGE_FILTER) StoreMessageFilterForm filterForm) {
        return "storeMessage/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findStoreMessages(WebRequest request,
            @ModelAttribute(SessionKey.STORE_MESSAGE_FILTER_RESULT) StoreMessageFilterResultForm form,
            @ModelAttribute(SessionKey.STORE_MESSAGE_FILTER) StoreMessageFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        logger.info("findStoreMessages()=> attemp validate form: " + filterForm);
        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "storeMessage/filter";
        }

        form.reset();

        AppLocale locale = getAppLocale();

        StoreMessageDataCriteria criteria = new StoreMessageDataCriteria();

        criteria.setMessageId(filterForm.getFilterId());
        criteria.setName(filterForm.getFilterValue());
        criteria.setNameFilterType(filterForm.getFilterType());
        //criteria.setStatus(filterForm.getStatus());
        //criteria.setInactive(filterForm.getInactive());
        //criteria.setPublished(filterForm.getPublished());
        //criteria.setUnpublished(filterForm.getUnpublished());
        criteria.setPublished(filterForm.getShowPublished());
        criteria.setMessageTitle(filterForm.getTitle());
        criteria.setMessageTitleFilterType(filterForm.getTitleFilterType());
        criteria.setCountry(filterForm.getCountry());
        criteria.setLanguageCd(filterForm.getLanguageCd());
        criteria.setPostedDateFrom(parseDateNN(locale, filterForm.getPostedDateFrom()));
        criteria.setPostedDateTo(parseDateNN(locale, filterForm.getPostedDateTo()));
        criteria.setStoreId(getStoreId());
        criteria.setShowInactive(filterForm.getShowInactive());
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.STORE_MESSAGE);

        List<StoreMessageListView> userMessages = storeMessageService.findMessagesByCriteria(criteria);

        form.setStoreMessages(userMessages);

        WebSort.sort(form, StoreMessageListEntity.MESSAGE_TITLE);

        return "storeMessage/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.STORE_MESSAGE_FILTER_RESULT) StoreMessageFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "storeMessage/filter";
    }

    @ModelAttribute(SessionKey.STORE_MESSAGE_FILTER_RESULT)
    public StoreMessageFilterResultForm init(HttpSession session) {

        logger.info("init()=> init....");

        StoreMessageFilterResultForm storeMessageFilterResult = (StoreMessageFilterResultForm) session.getAttribute(SessionKey.STORE_MESSAGE_FILTER_RESULT);

        logger.info("init()=> storeMessageFilterResult: " + storeMessageFilterResult);

        if (storeMessageFilterResult == null) {
            storeMessageFilterResult = new StoreMessageFilterResultForm();
        }

        return storeMessageFilterResult;

    }

    @ModelAttribute(SessionKey.STORE_MESSAGE_FILTER)
    public StoreMessageFilterForm initFilter(HttpSession session) {

        logger.info("initFilter(()=>  init....");

        StoreMessageFilterForm storeMessageFilter = (StoreMessageFilterForm) session.getAttribute(SessionKey.STORE_MESSAGE_FILTER);

        logger.info("initFilter()=> storeMessageFilter: " + storeMessageFilter);

        if (storeMessageFilter == null || !storeMessageFilter.isInitialized()) {
            storeMessageFilter = new StoreMessageFilterForm();
            storeMessageFilter.initialize();
        }

        return storeMessageFilter;

    }

}


