package com.espendwise.manta.util.validation.rules;

import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;


public class CorporateScheduleAccountConfigUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(CorporateScheduleAccountConfigUpdateConstraint.class);
    private Long storeId;
    private Long scheduleId;
    private List<Long> deselectedAccountIds;

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getStoreId() {
		return storeId;
	}

    public CorporateScheduleAccountConfigUpdateConstraint(Long storeId, Long scheduleId, List<Long> deselectedAccountIds) {
    	this.storeId = storeId;
    	this.scheduleId = scheduleId;
		this.deselectedAccountIds = deselectedAccountIds;
	}

    public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setDeselectedAccountIds(List<Long> deselectedAccountIds) {
		this.deselectedAccountIds = deselectedAccountIds;
	}

	public List<Long> getDeselectedAccountIds() {
		return deselectedAccountIds;
	}
	
    public ValidationRuleResult apply() {
        logger.info("apply()=> BEGIN");
        if (getScheduleId()== null) {
            return null;
        }
        ValidationRuleResult result = new ValidationRuleResult();
        checkSiteAssocExistForAccount(result);
        if (result.isFailed()) {
            return result;
        }
        logger.info("apply()=> END, SUCCESS");
        result.success();
        return result;
    }

    private void checkSiteAssocExistForAccount(ValidationRuleResult result) {
    	ScheduleService scheduleService = getScheduleService();
    	SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);
    	criteria.setStoreId(getStoreId());
        criteria.setScheduleId(getScheduleId());
        criteria.setActiveOnly(false);
        criteria.setConfiguredOnly(true);
        
        for (Long accountId : deselectedAccountIds){
        	criteria.setAccountIds(Utility.toList(accountId));
        	criteria.setLimit(4);
        
	        List<SiteListView> accountSites = scheduleService.findScheduleSitesByCriteria(criteria);
	        if (!accountSites.isEmpty()) {	
	            String str = "";
	            for (int i = 0; i < (accountSites.size() > 3 ? 3 : accountSites.size()); i++) {
	            	SiteListView siteView = (SiteListView) accountSites.get(i);
	                str += " <" + siteView.getSiteName() + "> ";
	            }
	
	            if (accountSites.size() > 3) {
	                str += " ...";
	            }
	            logger.info("checkSiteAssocExistForAccount()=> str: " + str);
	            
	            result.failed(ExceptionReason.CorporateScheduleAccountConfigUpdateReason.CORPORATE_SCHEDULE_CONFIGURED_TO_LOCATION_OF_ACCOUNT,
	                    new NumberArgument(accountId), new StringArgument(str));
	            break;
	            
	        }
        }
    }

    public ScheduleService getScheduleService() {
        return ServiceLocator.getScheduleService();
    }
}
