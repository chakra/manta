package com.espendwise.manta.util.validation.rules;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ContractData;
import com.espendwise.manta.service.BusEntityService;
import com.espendwise.manta.service.SiteService;
import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;


public class OrderUpdateNewSiteConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(OrderUpdateNewSiteConstraint.class);
    private Long siteId;

    public OrderUpdateNewSiteConstraint(Long siteId) {
        this.siteId = siteId;
    }

    @Override
    public ValidationRuleResult apply() {
        logger.info("apply()=> BEGIN");
        
        if (siteId == null) {
            return null;
        }

        ValidationRuleResult result = new ValidationRuleResult();
        
        checkSiteIsValidAsNewForOrder(result);
        
        if (result.isFailed()) {
            return result;
        }
        
        result.success();
        
        logger.info("apply()=> END, SUCCESS");

        return result;
    }

    private void checkSiteIsValidAsNewForOrder(ValidationRuleResult result) {
    	SiteService siteService = getSiteService();
        BusEntityService busEntityService = getBusEntityService();
        
        List<BusEntityData> sites = busEntityService.find(Utility.toList(siteId), RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);

        if (!Utility.isSet(sites)) {
            result.failed(ExceptionReason.OrderUpdateCheckNewSiteReason.SITE_NOT_FOUND,
	                  new NumberArgument(siteId));
	    return;
        } else if (sites.size() > 1) {
            result.failed(ExceptionReason.OrderUpdateCheckNewSiteReason.FEW_SITES_FOUND,
	                  new NumberArgument(siteId));
            return;
        } else {
            BusEntityData siteD = (BusEntityData) sites.get(0);
            if (!RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE.equals(siteD.getBusEntityStatusCd())) {
                result.failed(ExceptionReason.OrderUpdateCheckNewSiteReason.NOT_ACTIVE_SITE_FOUND,
                              new NumberArgument(siteId));
                return;
            }
        }
        
        List<BusEntityData> accounts = siteService.findSiteAccount(siteId);
        
        if (!Utility.isSet(accounts)) {
            result.failed(ExceptionReason.OrderUpdateCheckNewSiteReason.NO_ACCOUNT_FOR_SITE_FOUND,
	                  new NumberArgument(siteId));
	    return;
        } else if (accounts.size() > 1) {
            StringBuilder accountNames = new StringBuilder();
            for (BusEntityData accountD : accounts) {
                accountNames.append(accountD.getShortDesc());
                accountNames.append(", ");
            }
            accountNames.setLength(accountNames.length() - 2);
            
            result.failed(ExceptionReason.OrderUpdateCheckNewSiteReason.FEW_ACCOUNTS_FOR_SITE_FOUND,
	                  new NumberArgument(siteId), new StringArgument(accountNames.toString()));
            return;
        }
        
        List<ContractData> contracts = siteService.findSiteContract(siteId);
        
        if (!Utility.isSet(contracts)) {
            result.failed(ExceptionReason.OrderUpdateCheckNewSiteReason.NO_CONTRACT_FOR_SITE_FOUND,
	                  new NumberArgument(siteId));
        } else if (contracts.size() > 1) {
            StringBuilder contractNames = new StringBuilder();
            if (Utility.isSet(contracts)) {
                for (ContractData contractD : contracts) {
                    contractNames.append(contractD.getShortDesc());
                    contractNames.append(", ");
                }
                contractNames.setLength(contractNames.length() - 2);
            }
            result.failed(ExceptionReason.OrderUpdateCheckNewSiteReason.FEW_CONTRACTS_FOR_SITE_FOUND,
	                  new NumberArgument(siteId), new StringArgument(contractNames.toString()));         
        }
    }

    public Long getSiteId() {
        return siteId;
    }

    public SiteService getSiteService() {
        return ServiceLocator.getSiteService();
    }
    
    public BusEntityService getBusEntityService() {
        return ServiceLocator.getBusEntityService();
    }
}
