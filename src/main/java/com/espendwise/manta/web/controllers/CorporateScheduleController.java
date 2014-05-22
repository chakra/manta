package com.espendwise.manta.web.controllers;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.ScheduleData;
import com.espendwise.manta.model.data.ScheduleDetailData;
import com.espendwise.manta.model.view.ScheduleView;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.PhysicalInventoryPeriod;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.CorporateScheduleForm;
import com.espendwise.manta.web.forms.ScheduleForm;
import com.espendwise.manta.web.resolver.CorporateScheduleWebExceptionResolver;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;


@Controller
@RequestMapping(UrlPathKey.CORPORATE_SCHEDULE.IDENTIFICATION)
public class CorporateScheduleController extends BaseController {

    private static final Logger logger = Logger.getLogger(CorporateScheduleController.class);
    private ScheduleService scheduleService;

    @Autowired
    public CorporateScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {
        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new CorporateScheduleWebExceptionResolver());
        return "corporateSchedule/edit";
    }
          
    @Clean(SessionKey.CORPORATE_SCHEDULE_HEADER)
    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.CORPORATE_SCHEDULE) CorporateScheduleForm corporateScheduleForm, Model model) throws Exception {
        logger.info("create()=> BEGIN");
        corporateScheduleForm = new CorporateScheduleForm();
        corporateScheduleForm.initialize();
        populateFormReferenceData(corporateScheduleForm);
        model.addAttribute(SessionKey.CORPORATE_SCHEDULE, corporateScheduleForm);
        logger.info("create()=> END.");
        return "corporateSchedule/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.CORPORATE_SCHEDULE_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.CORPORATE_SCHEDULE) CorporateScheduleForm corporateScheduleForm, Model model) throws Exception {
        logger.info("save()=> BEGIN, scheduleForm: " + corporateScheduleForm);
        populateFormReferenceData(corporateScheduleForm);
        WebErrors webErrors = new WebErrors(request);
        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(corporateScheduleForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.CORPORATE_SCHEDULE, corporateScheduleForm);
            return "corporateSchedule/edit";
        }
        ScheduleView scheduleView = null;
        if (!corporateScheduleForm.isNew()) {
        	scheduleView = scheduleService.findSchedule(corporateScheduleForm.getScheduleId());
        	//if the schedule cannot be found or doesn't belong to the current store, 
        	//return an error and bring the user to the search page
        	if (scheduleView == null || scheduleView.getSchedule() == null ||
        			!getStoreId().equals(scheduleView.getSchedule().getBusEntityId())) {
            	webErrors.putMessage("exception.web.error.corporateScheduleNotFound", Args.i18nTyped(corporateScheduleForm.getScheduleId().toString()));
                String url = UrlPathAssistent.basePath() + "/corporateSchedule";
            	return "forward:/" + url;
        	}
        }
        else {
        	scheduleView = new ScheduleView(new ScheduleData(), null);
        }
        
        //populate the schedule
        //schedule id will be handled by the dao
        scheduleView.getSchedule().setShortDesc(corporateScheduleForm.getScheduleName().trim());
        //associated store will be handled by the dao
        scheduleView.getSchedule().setScheduleStatusCd(corporateScheduleForm.getScheduleStatus().trim());
        //schedule type will be handled by the dao
        //schedule rule will be handled by the dao
        //schedule cycle will be handled by the dao
        //schedule effective date will be handled by the dao
        //schedule expiration date will be handled by the dao
        
        //populate the schedule details.  For every detail, create a new ScheduleDetailData.  The dao will handle 
        //creating/updating/deleting details.
        List<ScheduleDetailData> scheduleDetails = new ArrayList<ScheduleDetailData>();
        scheduleView.setScheduleDetails(scheduleDetails);
        //interval
        String interval = corporateScheduleForm.getScheduleIntervalHours();
        if (Utility.isSet(interval)) {
    		ScheduleDetailData scheduleDetail = new ScheduleDetailData();
    		scheduleDetail.setScheduleDetailCd(RefCodeNames.SCHEDULE_DETAIL_CD.INV_CART_ACCESS_INTERVAL);
    		scheduleDetail.setValue(interval.trim());
    		scheduleDetails.add(scheduleDetail);
        }
        //cutoff day
        {
	    	ScheduleDetailData scheduleDetail = new ScheduleDetailData();
	    	scheduleDetail.setScheduleDetailCd(RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_DAY);
	    	scheduleDetail.setValue("0");
			scheduleDetails.add(scheduleDetail);
        }
		//cutoff time
        String cutoffTime = corporateScheduleForm.getScheduleCutoffTime();
        if (Utility.isSet(cutoffTime)) {
    		ScheduleDetailData scheduleDetail = new ScheduleDetailData();
    		scheduleDetail.setScheduleDetailCd(RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_TIME);
    		scheduleDetail.setValue(cutoffTime.trim());
    		scheduleDetails.add(scheduleDetail);
        }
        //also dates - these values must be converted from the date format of the user's locale to the system date
        //format
        String alsoDates = corporateScheduleForm.getScheduleAlsoDates();
        if (Utility.isSet(alsoDates)) {
        	SimpleDateFormat systemDateFormatter = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN);
        	SimpleDateFormat userLocaleDateFormatter = new SimpleDateFormat(I18nUtil.getDatePattern());
			String[] dates = alsoDates.split(",");
			for (String date: dates) {
	    		ScheduleDetailData scheduleDetail = new ScheduleDetailData();
	    		scheduleDetail.setScheduleDetailCd(RefCodeNames.SCHEDULE_DETAIL_CD.ALSO_DATE);
	    		scheduleDetail.setValue(systemDateFormatter.format(userLocaleDateFormatter.parse(date.trim())));
	    		scheduleDetails.add(scheduleDetail);
			}
		}
        //physical inventory dates - these values must be converted from the date format of the user's locale to the 
        //system date format
        String physicalInventoryDates = corporateScheduleForm.getSchedulePhysicalInventoryDates();
		if (Utility.isSet(physicalInventoryDates)) {
        	SimpleDateFormat systemDateFormatter = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN);
        	SimpleDateFormat userLocaleDateFormatter = new SimpleDateFormat(I18nUtil.getDatePattern());
			List<String> physicalInventoryDateList = new ArrayList<String>();
			while (Utility.isSet(physicalInventoryDates)) {
				int parenthesisStart = physicalInventoryDates.indexOf("(");
				int parenthesisEnd = physicalInventoryDates.indexOf(")");
				physicalInventoryDateList.add(physicalInventoryDates.substring(parenthesisStart + 1, parenthesisEnd));
				physicalInventoryDates = physicalInventoryDates.substring(parenthesisEnd + 1);
			}
			for (String physicalInventoryDate : physicalInventoryDateList) {
				PhysicalInventoryPeriod period = PhysicalInventoryPeriod.parse(physicalInventoryDate, new ParsePosition(0), I18nUtil.getDatePattern());
				{
					ScheduleDetailData scheduleDetail = new ScheduleDetailData();
					scheduleDetail.setScheduleDetailCd(RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_START_DATE);
					scheduleDetail.setValue(systemDateFormatter.format(userLocaleDateFormatter.parse(period.getStartDateAsString(I18nUtil.getDatePattern()))));
					scheduleDetails.add(scheduleDetail);
				}
				{
					ScheduleDetailData scheduleDetail = new ScheduleDetailData();
					scheduleDetail.setScheduleDetailCd(RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_END_DATE);
					scheduleDetail.setValue(systemDateFormatter.format(userLocaleDateFormatter.parse(period.getEndDateAsString(I18nUtil.getDatePattern()))));
					scheduleDetails.add(scheduleDetail);
				}
				{
					ScheduleDetailData scheduleDetail = new ScheduleDetailData();
					scheduleDetail.setScheduleDetailCd(RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_FINAL_DATE);
					scheduleDetail.setValue(systemDateFormatter.format(userLocaleDateFormatter.parse(period.getAbsoluteFinishDateAsString(I18nUtil.getDatePattern()))));
					scheduleDetails.add(scheduleDetail);
				}
			}
		}
        
		//save the schedule and details
        try {
        	scheduleView = scheduleService.saveCorporateSchedule(scheduleView);
        } 
        catch (ValidationException e) {
        	e.printStackTrace();
            return handleValidationException(e, request);
        }

        logger.info("redirect(()=> redirect to: " + scheduleView.getSchedule().getScheduleId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(scheduleView.getSchedule().getScheduleId()));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(WebRequest request, @ModelAttribute(SessionKey.CORPORATE_SCHEDULE) ScheduleForm corporateScheduleForm, @PathVariable(IdPathKey.CORPORATE_SCHEDULE_ID) Long scheduleId, Model model) {
        logger.info("show()=> BEGIN");
        populateFormReferenceData(corporateScheduleForm);
        ScheduleView scheduleView = scheduleService.findSchedule(scheduleId);
        logger.info("show()=> scheduleView: " + scheduleView);
        if (scheduleView != null && scheduleView.getSchedule() != null && scheduleView.getSchedule().getBusEntityId().equals(getStoreId())) {
            ScheduleData schedule = scheduleView.getSchedule();
            corporateScheduleForm.setScheduleId(schedule.getScheduleId());
            corporateScheduleForm.setScheduleName(schedule.getShortDesc());
            corporateScheduleForm.setScheduleStatus(schedule.getScheduleStatusCd());
            List<ScheduleDetailData> details = scheduleView.getScheduleDetails();
            if (Utility.isSet(details)) {
            	List<String> alsoDates = new ArrayList<String>();
            	List<String> physicalInventoryStartDates = new ArrayList<String>();
            	List<String> physicalInventoryEndDates = new ArrayList<String>();
            	List<String> physicalInventoryFinalDates = new ArrayList<String>();
            	for (ScheduleDetailData detail : details) {
            		String detailCode = detail.getScheduleDetailCd();
            		if (RefCodeNames.SCHEDULE_DETAIL_CD.INV_CART_ACCESS_INTERVAL.equalsIgnoreCase(detailCode)) {
            			corporateScheduleForm.setScheduleIntervalHours(detail.getValue());
            		}
            		else if (RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_TIME.equalsIgnoreCase(detailCode)) {
            			corporateScheduleForm.setScheduleCutoffTime(detail.getValue());
            		}
            		else if (RefCodeNames.SCHEDULE_DETAIL_CD.ALSO_DATE.equalsIgnoreCase(detailCode)) {
            			alsoDates.add(detail.getValue());
            		}
            		else if (RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_START_DATE.equalsIgnoreCase(detailCode)) {
            			physicalInventoryStartDates.add(detail.getValue());
            		}
            		else if (RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_END_DATE.equalsIgnoreCase(detailCode)) {
            			physicalInventoryEndDates.add(detail.getValue());
            		}
            		else if (RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_FINAL_DATE.equalsIgnoreCase(detailCode)) {
            			physicalInventoryFinalDates.add(detail.getValue());
            		}
            	}
                if (Utility.isSet(alsoDates)) {
                	//sort the dates chronologically.  The dates are stored in the database and returned in the
                	//system date format, so they must be converted to the date format of the user's locale.
                	Map<Date,String> alsoDateMap = new HashMap<Date,String>();
                	SimpleDateFormat systemDateFormatter = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN);
                	SimpleDateFormat userLocaleDateFormatter = new SimpleDateFormat(I18nUtil.getDatePattern());
                	for (String alsoDate : alsoDates) {
                		try {
                			String convertedAlsoDate = userLocaleDateFormatter.format(systemDateFormatter.parse(alsoDate));
                			Date alsoDateDate = userLocaleDateFormatter.parse(convertedAlsoDate);
                			alsoDateMap.put(alsoDateDate, convertedAlsoDate);
                		}
                		catch (ParseException pe) {
                			logger.error("Parse exception occurred when parsing corporate schedule also date (" + alsoDate+ ")");
                			throw new RuntimeException(pe);
                		}
                	}
                	TreeMap<Date,String> sortedAlsoDateMap = new TreeMap<Date,String>(alsoDateMap);
                	StringBuilder sb = new StringBuilder();
                	boolean includeSeparator = false;
                	for (Date sortedAlsoDate : sortedAlsoDateMap.keySet()) {
                		if (includeSeparator) {
                			sb.append(", ");
                		}
                		includeSeparator = true;
                		sb.append(sortedAlsoDateMap.get(sortedAlsoDate));
                	}
                	corporateScheduleForm.setScheduleAlsoDates(sb.toString());
                }
                if (Utility.isSet(physicalInventoryStartDates) && Utility.isSet(physicalInventoryEndDates)
                		 && Utility.isSet(physicalInventoryFinalDates)) {
                	String[] physicalInventoryStartDateArray = getPhysicalInventoryDatesAsArray(physicalInventoryStartDates, "start");
                	String[] physicalInventoryEndDateArray = getPhysicalInventoryDatesAsArray(physicalInventoryEndDates, "end");
                	String[] physicalInventoryFinalDateArray = getPhysicalInventoryDatesAsArray(physicalInventoryFinalDates, "final");
                	//all of the date arrays should be of the same size.  If not, return an error.
                	if (physicalInventoryStartDateArray.length != physicalInventoryEndDateArray.length ||
                			physicalInventoryEndDateArray.length != physicalInventoryFinalDateArray.length) {
                        WebErrors webErrors = new WebErrors(request);
                        webErrors.putMessage("exception.web.error.corporateScheduleHasInvalidPhysicalInventoryData", Args.i18nTyped(scheduleId.toString()));
                        String url = UrlPathAssistent.basePath() + "/corporateSchedule";
                    	return "forward:/" + url;
                	}
            		StringBuilder sb = new StringBuilder();
                	int loopMax = physicalInventoryStartDateArray.length;
                	for (int i=0; i<loopMax; i++) {
                		sb.append("(");
                		sb.append(physicalInventoryStartDateArray[i]);
                		sb.append(", ");
                		sb.append(physicalInventoryEndDateArray[i]);
                		sb.append(", ");
                		sb.append(physicalInventoryFinalDateArray[i]);
                		sb.append(") ");
                	}
                	corporateScheduleForm.setSchedulePhysicalInventoryDates(sb.toString());
                }
            }
        }
        else {
            WebErrors webErrors = new WebErrors(request);
            webErrors.putMessage("exception.web.error.corporateScheduleNotFound", Args.i18nTyped(scheduleId.toString()));
            String url = UrlPathAssistent.basePath() + "/corporateSchedule";
        	return "forward:/" + url;
        }
        model.addAttribute(SessionKey.CORPORATE_SCHEDULE, corporateScheduleForm);
        logger.info("show()=> END.");
        return "corporateSchedule/edit";
    }
    
    private String[] getPhysicalInventoryDatesAsArray(List<String> physicalInventoryDates, String physicalInventoryDateType) {
    	//sort the dates chronologically.  The dates are stored in the database and returned in the
    	//system date format, so they must be converted to the date format of the user's locale.
    	SimpleDateFormat systemDateFormatter = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN);
    	SimpleDateFormat userLocaleDateFormatter = new SimpleDateFormat(I18nUtil.getDatePattern());
    	Map<Date,String> physicalInventoryDateMap = new HashMap<Date,String>();
    	for (String physicalInventoryDate : physicalInventoryDates) {
    		try {
    			String convertedPhysicalInventoryDate = userLocaleDateFormatter.format(systemDateFormatter.parse(physicalInventoryDate));
    			Date physicalInventoryDateDate = userLocaleDateFormatter.parse(convertedPhysicalInventoryDate);
    			//if there is already an entry in the map for this date, add a millisecond to the date until we 
    			//get a unique value
    			while (physicalInventoryDateMap.containsKey(physicalInventoryDateDate)) {
    				physicalInventoryDateDate.setTime(physicalInventoryDateDate.getTime() + 1);
    			}
    			physicalInventoryDateMap.put(physicalInventoryDateDate, convertedPhysicalInventoryDate);
    		}
    		catch (ParseException pe) {
    			logger.error("Parse exception occurred when parsing corporate schedule " + physicalInventoryDateType + " date (" + physicalInventoryDate + ")");
    			throw new RuntimeException(pe);
    		}
    	}
    	TreeMap<Date,String> sortedPhysicalInventoryDateMap = new TreeMap<Date,String>(physicalInventoryDateMap);
    	String[] physicalInventoryDateArray = new String[sortedPhysicalInventoryDateMap.keySet().size()];
    	int index = 0;
    	for (Date sortedPhysicalInventoryDate : sortedPhysicalInventoryDateMap.keySet()) {
    		physicalInventoryDateArray[index++] = (sortedPhysicalInventoryDateMap.get(sortedPhysicalInventoryDate));
    	}
    	return physicalInventoryDateArray;
    }

    @SuccessMessage
    @Clean(SessionKey.CORPORATE_SCHEDULE_HEADER)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(WebRequest request, @ModelAttribute(SessionKey.CORPORATE_SCHEDULE) ScheduleForm corporateScheduleForm) throws Exception {

        logger.info("delete()=> BEGIN, scheduleForm: " + corporateScheduleForm);

        WebErrors webErrors = new WebErrors(request);

        if (!corporateScheduleForm.getIsNew()) {
            ScheduleView scheduleView = scheduleService.findSchedule(corporateScheduleForm.getScheduleId());
            if (scheduleView != null && scheduleView.getSchedule() != null && scheduleView.getSchedule().getBusEntityId().equals(getStoreId())) {
            	try {
            		if (!scheduleService.deleteCorporateSchedule(corporateScheduleForm.getScheduleId())) {
            			webErrors.putError("validation.web.error.corporateScheduleCantBeDeleted");
            			return "corporateSchedule/edit";
            		}
            	}
                catch (ValidationException e) {
                	e.printStackTrace();
                    return handleValidationException(e, request);
                }
            	WebFormUtil.removeObjectFromFilterResult(request,
                    SessionKey.CORPORATE_SCHEDULE_FILTER_RESULT, corporateScheduleForm.getScheduleId());
            	logger.info("delete()=> END. forward to filter");
                String url = UrlPathAssistent.basePath() + "/corporateSchedule";
            	return "forward:/" + url;
            }
            else {
                webErrors.putMessage("exception.web.error.corporateScheduleNotFound", Args.i18nTyped(corporateScheduleForm.getScheduleId().toString()));
                String url = UrlPathAssistent.basePath() + "/corporateSchedule";
            	return "forward:/" + url;
            }
        }
        logger.info("delete()=> END.");
        return "corporateSchedule/edit";
    }

    @ModelAttribute(SessionKey.CORPORATE_SCHEDULE)
    public ScheduleForm initModel() {
    	CorporateScheduleForm corporateScheduleForm = new CorporateScheduleForm();
    	corporateScheduleForm.initialize();
        return corporateScheduleForm;
    }

    private void populateFormReferenceData(ScheduleForm form) {
        //populate the form with reference information (status choices)
        List<Pair<String, String>> statuses = new ArrayList<Pair<String, String>>();
        statuses.add(new Pair<String, String>(new MessageI18nArgument("refcodes.CORPORATE_SCHEDULE_STATUS_CD.ACTIVE").resolve(), RefCodeNames.SCHEDULE_STATUS_CD.ACTIVE));
        statuses.add(new Pair<String, String>(new MessageI18nArgument("refcodes.CORPORATE_SCHEDULE_STATUS_CD.INACTIVE").resolve(), RefCodeNames.SCHEDULE_STATUS_CD.INACTIVE));
        statuses.add(new Pair<String, String>(new MessageI18nArgument("refcodes.CORPORATE_SCHEDULE_STATUS_CD.LIMITED").resolve(), RefCodeNames.SCHEDULE_STATUS_CD.LIMITED));
        form.setStatusChoices(statuses);
    }

}
