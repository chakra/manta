package com.espendwise.manta.util.validation.rules;

import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;


public class UserAccountConfigurationConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(UserAccountConfigurationConstraint.class);
    private Long storeId;
    private Long userId;
    private List<Long> deselectedAccountIds;

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getStoreId() {
		return storeId;
	}

    public UserAccountConfigurationConstraint(Long storeId, Long userId, List<Long> deselectedAccountIds) {
    	this.storeId = storeId;
    	this.userId = userId;
		this.deselectedAccountIds = deselectedAccountIds;
	}

    public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setDeselectedAccountIds(List<Long> deselectedAccountIds) {
		this.deselectedAccountIds = deselectedAccountIds;
	}

	public List<Long> getDeselectedAccountIds() {
		return deselectedAccountIds;
	}
	
    public ValidationRuleResult apply() {
        logger.info("apply()=> BEGIN");
        if (getUserId()== null) {
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
    	UserService userService = getUserService();
    	SiteListViewCriteria criteria = new SiteListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.SITE);
    	criteria.setStoreId(getStoreId());
        criteria.setUserId(getUserId());
        criteria.setActiveOnly(false);
        criteria.setConfiguredOnly(true);
        
        for (Long accountId : deselectedAccountIds){
        	criteria.setAccountIds(Utility.toList(accountId));
        	criteria.setLimit(4);
        
	        List<SiteListView> accountSites = userService.findUserSitesByCriteria(criteria);
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
	            
	            result.failed(ExceptionReason.UserAccountConfigUpdateReason.USER_CONFIGURED_TO_LOCATION_OF_ACCOUNT,
	                    new NumberArgument(accountId), new StringArgument(str));
	            break;
	            
	        }
        }
    }

    public UserService getUserService() {
        return ServiceLocator.getUserService();
    }
}
