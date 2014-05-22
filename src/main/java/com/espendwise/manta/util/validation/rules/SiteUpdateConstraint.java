package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.SiteAccountView;
import com.espendwise.manta.model.view.SiteIdentView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.PropertyService;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.criteria.StoreSiteCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SiteUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(SiteUpdateConstraint.class);

    private Long storeId;
    private SiteIdentView site;

    public SiteUpdateConstraint(Long storeId, SiteIdentView site) {
        this.storeId = storeId;
        this.site = site;
    }

    public Long getStoreId() {
        return storeId;
    }

    public SiteIdentView getSite() {
        return site;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getSite() == null) {
            return null;
        }


        ValidationRuleResult result = new ValidationRuleResult();

        checkAccount(result);

        checkUnique(result);
        if (result.isFailed()) {
            return result;
        }

        businessRuleChecking(result);
        if (result.isFailed()) {
            return result;
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }

    private void checkAccount(ValidationRuleResult result) {

        if (Utility.longNN(getSite().getAccountId()) <= 0) {
            result.failed(ExceptionReason.SiteUpdateReason.NO_ACCOUNT_SET);
            return;
        }

        StoreAccountCriteria criteria = new StoreAccountCriteria();
        criteria.setStoreId(getStoreId());
        criteria.setName(getSite().getAccountId().toString());
        criteria.setFilterType(Constants.FILTER_TYPE.ID);

        List<BusEntityData> accounts = getAccountService().findStoreAccountBusDatas(criteria);
        if (!Utility.isSet(accounts)) {
            result.failed(
                    ExceptionReason.SiteUpdateReason.ACCOUNT_DOES_NOT_HAVE_ASSOCIATION_WITH_STORE,
                    new NumberArgument(getStoreId()), new NumberArgument(getSite().getAccountId())
            );
        }

    }

    private void businessRuleChecking(ValidationRuleResult result) {

        SiteIdentView site = getSite();

        Date effDate = site.getBusEntityData().getEffDate();
        Date expDate = site.getBusEntityData().getExpDate();

        if (effDate != null && expDate != null && expDate.before(effDate)) {

            result.failed(
                    ExceptionReason.SiteUpdateReason.EFFECTIVE_DATE_CANT_BY_AFTER_EXP_DATE,
                    new ObjectArgument<BusEntityData>(site.getBusEntityData())
            );

        }

        boolean inventoryShopping = Utility.isOn(PropertyUtil.toValueNN(site.getProperties(), SitePropertyTypeCode.INVENTORY_SHOPPING.getTypeCode()));
        boolean allowCorpSchedOrder = Utility.isTrue(PropertyUtil.toValueNN(site.getProperties(), SitePropertyTypeCode.ALLOW_CORPORATE_SCHED_ORDER.getTypeCode()));

        if (inventoryShopping && allowCorpSchedOrder) {
            result.failed(
                    ExceptionReason.SiteUpdateReason.CANT_SET_BOTH_ENABLE_INV_AND_ALLOW_CORP_SCH_ORDER,
                    new ObjectArgument<BusEntityData>(site.getBusEntityData())
            );

        }

        List<PropertyData> erpSystems = getPropertyService().findEntityPropertyValues(
                getStoreId(),
                RefCodeNames.PROPERTY_TYPE_CD.ERP_SYSTEM
        );

        Set<String> erpSystemsSet = toSetValues(erpSystems);

        if (!Utility.isSet(erpSystemsSet)) {
            result.failed(
                    ExceptionReason.SiteUpdateReason.ACCOUNT_DOES_NOT_HAVE_ERP_SYSTEMS,
                    new NumberArgument(getStoreId()),
                    new NumberArgument(site.getAccountId()),
                    new ObjectArgument<BusEntityData>(site.getBusEntityData())
            );
        } else if (erpSystemsSet.size() > 1) {
            result.failed(
                    ExceptionReason.SiteUpdateReason.ACCOUNT_HAS_MULTIPLE_ERP_SYSTEMS,
                    new NumberArgument(getStoreId()),
                    new NumberArgument(site.getAccountId()),
                    new ObjectArgument<BusEntityData>(site.getBusEntityData())
            );
        }

    }

    private Set<String> toSetValues(List<PropertyData> erpSystems) {

        Set<String> x = new HashSet<String>();

        if (Utility.isSet(erpSystems)) {
            for (PropertyData p : erpSystems) {
                if (Utility.isSet(p.getValue())) {
                    x.add(p.getValue());
                }
            }
        }

        return x;

    }

    private void checkUnique(ValidationRuleResult result) {

        SiteIdentView site = getSite();

        StoreSiteCriteria criteria = new StoreSiteCriteria();

        criteria.setStoreId(getStoreId());
        criteria.setAccountIds(Utility.toList(site.getAccountId()));
        criteria.setSiteNames(Utility.toList(site.getBusEntityData().getShortDesc()));

        SiteService siteService = getSiteService();

        List<SiteAccountView> dbSites = siteService.findSites(criteria);

        if (dbSites != null && dbSites.size() > 0) {

            for (SiteAccountView dbSite : dbSites) {

                if (!Utility.strNN(site.getBusEntityData().getShortDesc()).equalsIgnoreCase(dbSite.getBusEntityData().getShortDesc())) {
                    continue;
                }

                if (site.getBusEntityData().getBusEntityId() == null || (dbSite.getBusEntityData().getBusEntityId().longValue() != site.getBusEntityData().getBusEntityId())) {

                    result.failed(
                            ExceptionReason.SiteUpdateReason.SITE_MUST_BE_UNIQUE,
                            new ObjectArgument<BusEntityData>(site.getBusEntityData())
                    );

                }
            }
        }

    }

    public SiteService getSiteService() {
        return ServiceLocator.getSiteService();
    }

    public PropertyService getPropertyService() {
        return ServiceLocator.getPropertyService();
    }

    public AccountService getAccountService() {
        return ServiceLocator.getAccountService();
    }


}
