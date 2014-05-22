package com.espendwise.manta.util.validation.rules;


import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.data.ScheduleDetailData;
import com.espendwise.manta.model.view.ScheduleView;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;


public class CorporateScheduleDeleteConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(CorporateScheduleDeleteConstraint.class);

    private Long scheduleId;

    public CorporateScheduleDeleteConstraint(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public ValidationRuleResult apply() {
        logger.info("apply()=> BEGIN");
        if (getScheduleId()== null) {
            return null;
        }
        ValidationRuleResult result = new ValidationRuleResult();
        checkIfConfiguredToLocationsOrAccounts(result);
        if (result.isFailed()) {
            return result;
        }
        logger.info("apply()=> END, SUCCESS");
        result.success();
        return result;
    }

    private void checkIfConfiguredToLocationsOrAccounts(ValidationRuleResult result) {
    	ScheduleService scheduleService = getScheduleService();
    	ScheduleView schedule = scheduleService.findSchedule(Utility.longNN(getScheduleId()));
    	if (schedule != null && Utility.isSet(schedule.getScheduleDetails())) {
    		List<ScheduleDetailData> scheduleDetails = schedule.getScheduleDetails();
    		boolean isConfiguredToAccount = false;
    		boolean isConfiguredToLocation = false;
    		for (ScheduleDetailData scheduleDetail : scheduleDetails) {
    			if (RefCodeNames.SCHEDULE_DETAIL_CD.ACCOUNT_ID.equals(scheduleDetail.getScheduleDetailCd())) {
    				isConfiguredToAccount = true;
    			}
    			if (RefCodeNames.SCHEDULE_DETAIL_CD.SITE_ID.equals(scheduleDetail.getScheduleDetailCd())) {
    				isConfiguredToLocation = true;
    			}
    		}
    		if (isConfiguredToLocation) {
    			result.failed(ExceptionReason.CorporateScheduleDeleteReason.CORPORATE_SCHEDULE_CONFIGURED_TO_LOCATION);
    		}
    		if (isConfiguredToAccount) {
    			result.failed(ExceptionReason.CorporateScheduleDeleteReason.CORPORATE_SCHEDULE_CONFIGURED_TO_ACCOUNT);
    		}
        }
    }

    public ScheduleService getScheduleService() {
        return ServiceLocator.getScheduleService();
    }

}
