package com.espendwise.manta.util.validation.rules;


import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.view.CorporateScheduleView;
import com.espendwise.manta.model.view.ScheduleView;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.criteria.CorporateScheduleDataCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;


public class CorporateScheduleUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(CorporateScheduleUpdateConstraint.class);

    private ScheduleView schedule;
    private Long storeId;

    public CorporateScheduleUpdateConstraint(Long storeId, ScheduleView schedule) {
        this.schedule = schedule;
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public ScheduleView getSchedule() {
        return schedule;
    }

    public ValidationRuleResult apply() {
        logger.info("apply()=> BEGIN");
        if (getSchedule()== null) {
            return null;
        }
        ValidationRuleResult result = new ValidationRuleResult();
        checkUnique(result);
        if (result.isFailed()) {
            return result;
        }
        logger.info("apply()=> END, SUCCESS");
        result.success();
        return result;
    }

    private void checkUnique(ValidationRuleResult result) {
    	ScheduleService scheduleService = getScheduleService();
    	CorporateScheduleDataCriteria criteria = new CorporateScheduleDataCriteria();
        criteria.setStoreId(getStoreId());
        criteria.setName(getSchedule().getSchedule().getShortDesc());
        criteria.setNameFilterType(Constants.FILTER_TYPE.EXACT_MATCH);
        List<CorporateScheduleView> schedules = scheduleService.findCorporateSchedulesByCriteria(criteria);
        
        //if the schedule being saved is in the list then remove it, since it should not be considered
        //for the uniqueness test.
        if (Utility.isSet(schedules)) {
        	Iterator<CorporateScheduleView> scheduleIterator = schedules.iterator();
        	while (scheduleIterator.hasNext()) {
        		CorporateScheduleView schedule = scheduleIterator.next();
        		if (schedule.getScheduleId().equals(getSchedule().getSchedule().getScheduleId())) {
        			scheduleIterator.remove();
        		}
        	}
        }

        if (schedules.size() > 0) {
           CorporateScheduleView schedule = schedules.get(0);
            result.failed(ExceptionReason.CorporateScheduleUpdateReason.CORPORATE_SCHEDULE_MUST_BE_UNIQUE,
                    new StringArgument(schedule.getScheduleName()));
        }
    }

    public ScheduleService getScheduleService() {
        return ServiceLocator.getScheduleService();
    }

}
