package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.view.EmailTemplateListView;
import com.espendwise.manta.service.TemplateService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.StoreTemplateCriteria;
import com.espendwise.manta.web.forms.EmailTemplateFilterForm;
import com.espendwise.manta.web.forms.EmailTemplateFilterResultForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.SortHistory;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import com.espendwise.manta.web.util.WebFormUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Controller
@RequestMapping(UrlPathKey.EMAIL_TEMPLATE.FILTER)
@SessionAttributes({SessionKey.EMAIL_TEMPALTE_FILTER_RESULT, SessionKey.EMAIL_TEMPALTE_FILTER})
@AutoClean(SessionKey.EMAIL_TEMPALTE_FILTER_RESULT)
public class EmailTemplateFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(EmailTemplateFilterController.class);

    private TemplateService templateService;

    @Autowired
    public EmailTemplateFilterController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.EMAIL_TEMPALTE_FILTER) EmailTemplateFilterForm filterForm) {
        return "emailTemplate/filter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findEmailTemplates(WebRequest request,
                                    @ModelAttribute(SessionKey.EMAIL_TEMPALTE_FILTER_RESULT) EmailTemplateFilterResultForm form,
                                    @ModelAttribute(SessionKey.EMAIL_TEMPALTE_FILTER) EmailTemplateFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "emailTemplate/filter";
        }

        form.reset();

        AppLocale locale = getAppLocale();

        StoreTemplateCriteria criteria = new StoreTemplateCriteria(getStoreId());

        criteria.setTemplateId(filterForm.getFilterId());
        criteria.setFilterValue(filterForm.getFilterValue());
        criteria.setFilterType(filterForm.getFilterType());
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.TEMPLATE);

        List<EmailTemplateListView> templates = templateService.findEmailTemplatesByCriteria(criteria);
        WebFormUtil.fillOutEmailTemplateTypes(getAppLocale(), templates);
        form.setTemplates(templates);

        WebSort.sort(form, EmailTemplateListView.NAME);

        return "emailTemplate/filter";

    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.EMAIL_TEMPALTE_FILTER_RESULT) EmailTemplateFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "emailTemplate/filter";
    }
    
    @RequestMapping(value = "/filter/sortbyType/", method = RequestMethod.GET)
    public String sortByEmailType(@ModelAttribute(SessionKey.EMAIL_TEMPALTE_FILTER_RESULT) EmailTemplateFilterResultForm form) throws Exception {
    	return "emailTemplate/filter";
    }
    
    @RequestMapping(value = "/filter/sortbyType/{emailTypeMap}", method = RequestMethod.GET)
    /**
     * sortByEmailType is actually sort the translation of emailType from resource file.
     * For example with emailType=REJECTED_ORDER_EMAIL_TEMPLATE, message resource has admin.template.email.emailType.REJECTED_ORDER_EMAIL_TEMPLATE=Rejected Order Email Template
     * Parse following string into map of templateId:emailTypeValue. emailTypeValue is actually the resource translation of emailType. 
     * 421:Service Reminder,422:Scheduled Service,304:Rejected Order Email Template,428:Pending Approval Email Template,364:,401:,181:,361:,363:,161:,402:,362:,
     */
    public String sortByEmailType(@ModelAttribute(SessionKey.EMAIL_TEMPALTE_FILTER_RESULT) EmailTemplateFilterResultForm form, @PathVariable String emailTypeMap) throws Exception {
    	String[] els = emailTypeMap.split(",");
    	final Map emailsMap = new TreeMap();
    	for (String emailType :  els){
    		String[] idAndType = emailType.split(":");
    		if (idAndType.length >=2){
    			emailsMap.put(idAndType[0], idAndType[1]);
    		}
    	}
    	
    	String field = "emailType";
    	SortHistory history = form.getSortHistory();
    	boolean asc = history == null || (!Utility.strNN(history.getSortField()).equals(field) || !history.isAsc());
    	final int dir = asc ? 1 : -1;  //asc or desc
    	Collections.sort(form.getResult(), new Comparator<EmailTemplateListView>() {
            @Override
            public int compare(EmailTemplateListView o1, EmailTemplateListView o2) {                
            	Long id1 = o1.getTemplateId();
            	Long id2 = o2.getTemplateId();
                
            	String v1 = (String) emailsMap.get(id1.toString());
            	String v2 = (String) emailsMap.get(id2.toString());
                if (v1 == null && v2 == null) {
                    return 0;
                } else if (v1 == null) {
                    return dir * -1;
                } else if (v2 == null) {
                    return dir;
                } else {                    	
            		System.out.println(v1+"\t"+ v2+"\t"+dir * ((String) v1).toLowerCase().compareTo(((String) v2).toLowerCase()));
                    return dir * ((String) v1).toLowerCase().compareTo(((String) v2).toLowerCase());
                }
            }
        });
    	
    	if (history == null)
    		history = new SortHistory();
    	
    	history.setSortField(field);
        history.setAsc(asc);
        form.setSortHistory(history);
        return "emailTemplate/filter";
    }

    @ModelAttribute(SessionKey.EMAIL_TEMPALTE_FILTER_RESULT)
    public EmailTemplateFilterResultForm init(HttpSession session) {

        EmailTemplateFilterResultForm emailTemplateFilterResult = (EmailTemplateFilterResultForm) session.getAttribute(SessionKey.EMAIL_TEMPALTE_FILTER_RESULT);

        if (emailTemplateFilterResult == null) {
            emailTemplateFilterResult = new EmailTemplateFilterResultForm();
        }

        return emailTemplateFilterResult;

    }

    @ModelAttribute(SessionKey.EMAIL_TEMPALTE_FILTER)
    public EmailTemplateFilterForm initFilter(HttpSession session) {

        EmailTemplateFilterForm emailTemplateFilter = (EmailTemplateFilterForm) session.getAttribute(SessionKey.EMAIL_TEMPALTE_FILTER);

        if (emailTemplateFilter == null || !emailTemplateFilter.isInitialized()) {
            emailTemplateFilter = new EmailTemplateFilterForm();
            emailTemplateFilter.initialize();
        }

        return emailTemplateFilter;

    }

}


