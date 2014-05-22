package com.espendwise.manta.web.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
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

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.history.HistoryRecord;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.view.GenericReportColumnView;
import com.espendwise.manta.model.view.GenericReportResultView;
import com.espendwise.manta.service.HistoryService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.ReportWriter;
import com.espendwise.manta.util.ReportingUtil;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.criteria.HistoryCriteria;
import com.espendwise.manta.util.format.AppI18nFormatter;
import com.espendwise.manta.web.forms.HistoryFilterForm;
import com.espendwise.manta.web.forms.HistoryFilterResultForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.HISTORY.FILTER)
@SessionAttributes({SessionKey.HISTORY_FILTER_RESULT, SessionKey.HISTORY_FILTER})
@AutoClean(SessionKey.HISTORY_FILTER_RESULT)
public class HistoryController extends BaseController {

    private static final Logger logger = Logger.getLogger(HistoryController.class);

    private HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String filter(@ModelAttribute(SessionKey.HISTORY_FILTER) HistoryFilterForm filterForm) {
    	populateFormReferenceData(filterForm);
        return "historyFilter";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findHistoryRecords(WebRequest request,
            @ModelAttribute(SessionKey.HISTORY_FILTER) HistoryFilterForm filterForm,
            @ModelAttribute(SessionKey.HISTORY_FILTER_RESULT) HistoryFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);
        
        //limit this functionality to admin and system admin users, or users with the history
        //access group function  
        boolean accessAllowed = getAppUser().getIsAdmin() || getAppUser().getIsSystemAdmin()
        		|| Auth.isAuthorizedForFunction(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_HISTORY);
        if (!accessAllowed) {
            webErrors.putError("validation.web.error.unauthorizedAccess");
            populateFormReferenceData(filterForm);
            return "historyFilter";
        }

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            populateFormReferenceData(filterForm);
            return "historyFilter";
        }

        doSearch(request, filterForm, resultForm);
        
        return "historyFilter";
    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.HISTORY_FILTER_RESULT) HistoryFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field, null, HistoryRecord.class);
        return "historyFilter";
    }

    @RequestMapping(value = "/filter/excelExport", method = RequestMethod.GET)
    public String exportHistoryRecords(WebRequest request, HttpServletResponse response,
            @ModelAttribute(SessionKey.HISTORY_FILTER) HistoryFilterForm filterForm,
            @ModelAttribute(SessionKey.HISTORY_FILTER_RESULT) HistoryFilterResultForm resultForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);
        
        //limit this functionality to admin and system admin users, or users with the history
        //access group function
        boolean accessAllowed = getAppUser().getIsAdmin() || getAppUser().getIsSystemAdmin()
        		|| Auth.isAuthorizedForFunction(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_HISTORY);
        if (!accessAllowed) {
            webErrors.putError("validation.web.error.unauthorizedAccess");
            populateFormReferenceData(filterForm);
            return "historyFilter";
        }

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            populateFormReferenceData(filterForm);
            return "historyFilter";
        }

        doSearch(request, filterForm, resultForm);
        
        ReportingUtil.initializeResponseForExcelExport(request, response, "HistoryRecords.xls");
    	ArrayList<GenericReportResultView> reportLines = new ArrayList<GenericReportResultView>();
    	reportLines.add(generateExcelItems(resultForm.getHistoryRecords()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
	    try {
	    	ReportWriter.writeExcelReportMulti(reportLines, out);
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }

        try {
	        response.setContentLength(out.size());
	        out.writeTo(response.getOutputStream());
	        response.flushBuffer();
	        response.getOutputStream().close();
        }
        catch(IOException e){
        	response.getOutputStream().close();
        }
        
        return null;
    }
    
    private void doSearch(WebRequest request, HistoryFilterForm filterForm, HistoryFilterResultForm resultForm) {
        resultForm.reset();
        populateFormReferenceData(filterForm);
        AppLocale locale = getAppLocale();
        HistoryCriteria criteria = new HistoryCriteria();
        if (Utility.isSet(filterForm.getObjectId())) {
        	criteria.setObjectId(new Long(filterForm.getObjectId()).longValue());
        }
        criteria.setObjectType(filterForm.getObjectType());
        criteria.setTransactionType(filterForm.getTransactionType());
        Date startDate = parseDateNN(locale, filterForm.getStartDate());
        if (Utility.isSet(startDate)) {
        	//set the starting date time to midnight
        	Calendar c = Calendar.getInstance();
        	c.setTime(startDate);
        	c.set(Calendar.HOUR_OF_DAY, 0);
        	c.set(Calendar.MINUTE, 0);
        	c.set(Calendar.SECOND, 0);
        	startDate = c.getTime();
        }
        criteria.setStartDate(startDate);
        Date endDate = parseDateNN(locale, filterForm.getEndDate());
        if (Utility.isSet(endDate)) {
        	//set the ending date time to 11:59:59pm
        	Calendar c = Calendar.getInstance();
        	c.setTime(endDate);
        	c.set(Calendar.HOUR_OF_DAY, 23);
        	c.set(Calendar.MINUTE, 59);
        	c.set(Calendar.SECOND, 59);
        	endDate = c.getTime();
        }
        criteria.setEndDate(endDate);
        //Don't restrict the number of history records returned, since we're using this functionality
        //to track punchout order messages and we need to return all of the records.
        //criteria.setLimit(Constants.FILTER_RESULT_LIMIT.HISTORY_RECORDS);
    	List<HistoryData> historyDatas = historyService.findHistoryRecordsByCriteria(criteria);
    	List<HistoryRecord> historyRecords = new ArrayList<HistoryRecord>();
    	if (Utility.isSet(historyDatas)) {
    		String historyLinkBase = request.getContextPath() + UrlPathAssistent.basePath();
    		Iterator<HistoryData> historyDataIterator = historyDatas.iterator();
    		while (historyDataIterator.hasNext()) {
    			HistoryData historyData = historyDataIterator.next();
				HistoryRecord historyRecord = HistoryRecord.getInstance(historyData.getHistoryTypeCd());
				historyRecord.populateFromHistoryData(historyData);
				historyRecord.setHistoryLinkBase(historyLinkBase);
				historyRecords.add(historyRecord);
    		}
    	}
    	resultForm.setHistoryRecords(historyRecords);
        WebSort.sort(resultForm, HistoryData.ACTIVITY_DATE, null, HistoryRecord.class);
    }

    @ModelAttribute(SessionKey.HISTORY_FILTER_RESULT)
    public HistoryFilterResultForm init(HttpSession session) {
    	HistoryFilterResultForm historyFilterResult = (HistoryFilterResultForm) session.getAttribute(SessionKey.HISTORY_FILTER_RESULT);
        if (historyFilterResult == null) {
        	historyFilterResult = new HistoryFilterResultForm();
        }
        return historyFilterResult;
    }

    @ModelAttribute(SessionKey.HISTORY_FILTER)
    public HistoryFilterForm initFilter(HttpSession session) {
    	HistoryFilterForm historyFilter = (HistoryFilterForm) session.getAttribute(SessionKey.HISTORY_FILTER);
        if (historyFilter == null || !historyFilter.isInitialized()) {
        	historyFilter = new HistoryFilterForm();
        	historyFilter.initialize();
        }
        return historyFilter;
    }

    private void populateFormReferenceData(HistoryFilterForm form) {
        //populate the form with reference information (object type choices, transaction type choices)
        List<Pair<String, String>> objectTypes = new ArrayList<Pair<String, String>>();
        objectTypes.add(new Pair<String, String>(new MessageI18nArgument("history.objectType.group").resolve(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.GROUP));
        objectTypes.add(new Pair<String, String>(new MessageI18nArgument("history.objectType.item").resolve(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.ITEM));
        objectTypes.add(new Pair<String, String>(new MessageI18nArgument("history.objectType.shoppingControl").resolve(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.SHOPPING_CONTROL));
        objectTypes.add(new Pair<String, String>(new MessageI18nArgument("history.objectType.user").resolve(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.USER));
        if (Auth.getAppUser().isSystemAdmin() || Auth.getAppUser().isAdmin())
        	objectTypes.add(new Pair<String, String>(new MessageI18nArgument("history.objectType.genericReport").resolve(), RefCodeNames.HISTORY_OBJECT_TYPE_CD.GENERIC_REPORT));
        form.setObjectTypeChoices(objectTypes);
        
		String objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.shoppingControl");
		Object[] args = new Object[1];
		args[0] = objectType;
		String createShoppingControl =  I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.createObject", args);
		String modifyShoppingControl =  I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.modifyObject", args);
		objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.user");
		args = new Object[1];
		args[0] = objectType;
		String createUser =  I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.createObject", args);
		String modifyUser =  I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.modifyObject", args);
		objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.userGroups");
		args = new Object[1];
		args[0] = objectType;
		String modifyUserGroups =  I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.modifyObject", args);
		objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.punchoutOrderMessage");
		args = new Object[1];
		args[0] = objectType;
		String createPunchoutOrderMessage =  I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.createObject", args);
		objectType = I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.objectType.genericReport");
		args = new Object[1];
		args[0] = objectType;
		
        List<Pair<String, String>> transactionTypes = new ArrayList<Pair<String, String>>();
        transactionTypes.add(new Pair<String, String>(createPunchoutOrderMessage, HistoryRecord.TYPE_CODE_CREATE_PUNCHOUT_ORDER_MESSAGE));
        transactionTypes.add(new Pair<String, String>(createShoppingControl, HistoryRecord.TYPE_CODE_CREATE_SHOPPING_CONTROL));
        transactionTypes.add(new Pair<String, String>(createUser, HistoryRecord.TYPE_CODE_CREATE_USER));
        transactionTypes.add(new Pair<String, String>(modifyShoppingControl, HistoryRecord.TYPE_CODE_MODIFY_SHOPPING_CONTROL));
        transactionTypes.add(new Pair<String, String>(modifyUser, HistoryRecord.TYPE_CODE_MODIFY_USER));
        transactionTypes.add(new Pair<String, String>(modifyUserGroups, HistoryRecord.TYPE_CODE_UPDATE_USER_GROUPS));
        if (Auth.getAppUser().isSystemAdmin() || Auth.getAppUser().isAdmin()){
        	String createGenericReport =  I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.createObject", args);
    		String modifyGenericReport =  I18nUtil.getMessage(Auth.getAppUser().getLocale(), "history.type.modifyObject", args);
    		transactionTypes.add(new Pair<String, String>(createGenericReport, HistoryRecord.TYPE_CODE_CREATE_GENERIC_REPORT));
    		transactionTypes.add(new Pair<String, String>(modifyGenericReport, HistoryRecord.TYPE_CODE_MODIFY_GENERIC_REPORT));    		
        }
        form.setTransactionTypeChoices(transactionTypes);
    }

    private GenericReportResultView generateExcelItems(List<HistoryRecord> historyRecords) {

        GenericReportResultView xlog = new GenericReportResultView();

        try {
            String tabName = new MessageI18nArgument("admin.account.tabs.shoppingControl").resolve();
            xlog.setName(tabName);

            ArrayList<Serializable> r = null;
            ArrayList<ArrayList<Serializable>> restable = new ArrayList<ArrayList<Serializable>>();

    		AppI18nFormatter formatter = new AppI18nFormatter(new AppLocale(Auth.getAppUser().getLocale()));
            for (int i = 0; null != historyRecords && i < historyRecords.size(); i++) {
                HistoryRecord historyRecord = (HistoryRecord) historyRecords.get(i);
                r = new ArrayList<Serializable>();
                r.add(formatter.formatDate(historyRecord.getActivityDate()) + " " + formatter.formatTime(historyRecord.getActivityDate()));
                r.add(historyRecord.getUserId());
                r.add(historyRecord.getShortDescription());
                r.add(historyRecord.getLongDescription());
                restable.add(r);
                r = null;
            }

            xlog.setTable(restable);

            ArrayList<GenericReportColumnView> itemHeader = new ArrayList<GenericReportColumnView>();
            itemHeader.add(ReportingUtil.createGenericReportColumnView("java.lang.String", 
            		new MessageI18nArgument("history.label.activityDate").resolve(), 
            		0, 255, "VARCHAR2"));
            itemHeader.add(ReportingUtil.createGenericReportColumnView("java.lang.String", 
            		new MessageI18nArgument("history.label.userId").resolve(), 
            		0, 255, "VARCHAR2"));
            itemHeader.add(ReportingUtil.createGenericReportColumnView("java.lang.String", 
            		new MessageI18nArgument("history.label.shortDescription").resolve(), 
            		0, 255, "VARCHAR2"));
            itemHeader.add(ReportingUtil.createGenericReportColumnView("java.lang.String", 
            		new MessageI18nArgument("history.label.longDescription").resolve(), 
            		0, 255, "VARCHAR2"));
            xlog.setHeader(itemHeader);
            xlog.setColumnCount(itemHeader.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xlog;
    }
}
