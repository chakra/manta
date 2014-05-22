package com.espendwise.manta.web.controllers;

import javax.servlet.http.HttpSession;

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
import com.espendwise.manta.web.forms.UploadFileFilterForm;
import com.espendwise.manta.web.forms.UploadFileFilterResultForm;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.criteria.UploadFileListViewCriteria;
import com.espendwise.manta.util.*;
import com.espendwise.manta.model.view.UploadFileListView;
import com.espendwise.manta.service.UploadFileService;

import java.util.List;


@Controller
@RequestMapping(UrlPathKey.UPLOAD_FILE.FILTER)
@SessionAttributes({SessionKey.UPLOAD_FILE_FILTER_RESULT, SessionKey.UPLOAD_FILE_FILTER})
@AutoClean(SessionKey.UPLOAD_FILE_FILTER_RESULT)
public class UploadFileFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(UploadFileFilterController.class);

    private UploadFileService uploadService;

    @Autowired
    public UploadFileFilterController(UploadFileService uploadService) {
        this.uploadService = uploadService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.UPLOAD_FILE_FILTER)UploadFileFilterForm filterForm) {
        return "uploadFile/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findItems(WebRequest request,
                               @ModelAttribute(SessionKey.UPLOAD_FILE_FILTER_RESULT)UploadFileFilterResultForm form,
                               @ModelAttribute(SessionKey.UPLOAD_FILE_FILTER) UploadFileFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "uploadFile/filter";
        }

        form.reset();

        UploadFileListViewCriteria criteria = new UploadFileListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.UPLOAD_FILE);

        AppLocale locale = getAppLocale();

        criteria.setStoreId(getStoreId());
        criteria.setUploadId(parseNumberNN(filterForm.getUploadId()));
        criteria.setUploadFileName(filterForm.getUploadFileName());
        criteria.setUploadFileNameFilterType(filterForm.getUploadFileNameFilterType());
        criteria.setAddDate(parseDateNN(locale, filterForm.getAddDate()));
        criteria.setModifiedDate(parseDateNN(locale, filterForm.getModifiedDate()));
        criteria.setProcessing(Utility.isTrue(filterForm.getProcessingStatus()));
        criteria.setProcessed(Utility.isTrue(filterForm.getProcessedStatus()));

        List<UploadFileListView> files = uploadService.findUploadFilesByCriteria(criteria);

        form.setUploadFiles(files);

        WebSort.sort(form, UploadFileListView.UPLOAD_FILE_NAME);

        return "uploadFile/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.UPLOAD_FILE_FILTER_RESULT) UploadFileFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "uploadFile/filter";
    }

    @ModelAttribute(SessionKey.UPLOAD_FILE_FILTER_RESULT)
    public UploadFileFilterResultForm init(HttpSession session) {

        UploadFileFilterResultForm filterResult = (UploadFileFilterResultForm) session.getAttribute(SessionKey.UPLOAD_FILE_FILTER_RESULT);

        if (filterResult == null) {
            filterResult = new UploadFileFilterResultForm();
        }

        return filterResult;

    }

    @ModelAttribute(SessionKey.UPLOAD_FILE_FILTER)
    public UploadFileFilterForm initFilter(HttpSession session) {

        UploadFileFilterForm filter = (UploadFileFilterForm) session.getAttribute(SessionKey.UPLOAD_FILE_FILTER);

        if (filter == null || !filter.isInitialized()) {
            filter = new UploadFileFilterForm();
            filter.initialize();
        }

        return filter;

    }


}
