package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.ScheduleDetailData;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.CorporateScheduleDataCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import com.espendwise.manta.web.util.AppI18nUtil;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CorporateScheduleFilterConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(CorporateScheduleFilterConstraint.class);

    private Long storeId;
    private String filterId;
    private String filterValue;
    private String filterTypeCd;

    public CorporateScheduleFilterConstraint(Long storeId, String filterId, String filterValue, String filterTypeCd) {
        this.storeId = storeId;
        this.filterId = filterId;
        this.filterValue = filterValue;
        this.filterTypeCd = filterTypeCd;

    }

	public Long getStoreId() {
		return storeId;
	}
 
	public String getFilterId() {
		return filterId;
	}


	public String getFilterValue() {
		return filterValue;
	}


	public String getFilterTypeCd() {
		return filterTypeCd;
	}


	public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
 
        ValidationRuleResult result = new ValidationRuleResult();


        ScheduleService scheduleService = getScheduleService();

        CorporateScheduleDataCriteria criteria = new CorporateScheduleDataCriteria();

        criteria.setCorporateScheduleId(getFilterId());
        criteria.setName(getFilterValue());
        criteria.setNameFilterType(getFilterTypeCd());
        criteria.setStoreId(getStoreId());
        criteria.setCorporateScheduleDetailCd(RefCodeNames.SCHEDULE_DETAIL_CD.ALSO_DATE);
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.CORPORATE_SCHEDULE);
        
        List<ScheduleDetailData> scheduleDetails = scheduleService.findCorporateScheduleDetailsByCriteria(criteria);

        
        if (scheduleDetails != null && scheduleDetails.size() > 0) {
        	List<Long> errScheduleIds = new ArrayList<Long>();
        	AppLocale locale = AppLocale.SYSTEM_LOCALE;
    		String pattern = I18nUtil.getDatePatternPrompt(locale);
    		DateValidator validator = new DateValidator(pattern);
            for (ScheduleDetailData det : scheduleDetails) {
            	try {
            		validator.validateFormat(pattern, det.getValue());
            		java.util.Date date = AppI18nUtil.parseDateNN(locale, det.getValue());
              		logger.info("apply()=>  det.getValue()= " + det.getValue() + " => " + date );
                    
            	} catch (Exception e) {
              		logger.info("apply()=>  det.getValue()= " + det.getValue() + ", id: " + det.getScheduleId() );
            		errScheduleIds.add(det.getScheduleId());
             	}
            }
            if (!errScheduleIds.isEmpty()){
                result.failed(
                        ExceptionReason.CorporateScheduleFilterRuleReason.INCORRECT_SCHEDULE_DATE_FOUND,
                        new ObjectArgument<String>(errScheduleIds.toString()));
                return result;        
            }
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }


    public ScheduleService getScheduleService() {
        return ServiceLocator.getScheduleService();
    }
}
