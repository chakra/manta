package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.*;

public class AccountSiteHierarchyUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(AccountSiteHierarchyUpdateConstraint.class);

    private Long storeId;
    private Long parentLevelId;
    private Long endLevelId;
    private Object levelAssocCd;
    private String elementTypeCode;
    private UpdateRequest<BusEntityData> updateRequest;

    public AccountSiteHierarchyUpdateConstraint(Long storeId,
                                                Long parentLevelId,
                                                Long endLevelId,
                                                String levelAssocCd,
                                                String itemAssocCode,
                                                UpdateRequest<BusEntityData> updateRequest) {
        this.storeId = storeId;
        this.parentLevelId = parentLevelId;
        this.endLevelId = endLevelId;
        this.levelAssocCd = levelAssocCd;
        this.elementTypeCode = itemAssocCode;
        this.updateRequest = updateRequest;

    }

    @Override
    public  ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN, " +
                ",\n storeId: "+storeId +
                ",\n parentLevelId: "+parentLevelId +
                ",\n endLevelId: "+endLevelId +
                ",\n levelAssocCd: "+levelAssocCd +
                ",\n elementTypeCode: "+elementTypeCode +
                ",\n updateRequest: "+updateRequest
        );

        ValidationRuleResult result = new ValidationRuleResult();


        checkDuplicated(
                result,
                getParentLevelId(),
                getUpdateRequest()
        );

        if(result.isFailed()){
            return result;
        }

        checkLowerLevelConfig(
                result,
                getUpdateRequest(),
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_ACCOUNT.equals(getElementTypeCode())
        );

        if(result.isFailed()){
            return result;
        }

        if (RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_ACCOUNT.equals(getElementTypeCode()) && Utility.isSet(getUpdateRequest().getToCreate())){
            checkHigherLevelConfig(result, getEndLevelId());
        }

        if(result.isFailed()){
            return result;
        }


        result.success();

        logger.info("apply()=> END");

        return result;
    }

    private void checkHigherLevelConfig(ValidationRuleResult result, Long levelId) {

        logger.info("checkHigherLevelConfig()=> BEGIN, levelId: "+levelId);

        List<Long> configured = ServiceLocator.getSiteHierarchyService().findConfiguredHierarchyHigh(levelId);

        if(Utility.isSet(configured)){

            logger.info("checkHigherLevelConfig()=> failed: " +
                    ExceptionReason.SiteHierarchyUpdateReason.SITE_HIER_ERROR_HIGHER_LEVEL_CONFIG + ", " +
                    " configured: " + configured
            );

            result.failed(
                    ExceptionReason.SiteHierarchyUpdateReason.SITE_HIER_ERROR_HIGHER_LEVEL_CONFIG,
                    new StringArgument(Utility.toCommaString(configured)));
        }

        logger.info("checkHigherLevelConfig()=> END.");

    }

    private void checkLowerLevelConfig(ValidationRuleResult result, UpdateRequest<BusEntityData> updateRequest, boolean topLevel) {

        logger.info("checkLowerLevelConfig()=> BEGIN");

        if (updateRequest.getToDelete() == null || updateRequest.getToDelete().isEmpty()){
            logger.info("checkLowerLevelConfig()=> END, there is  nothing to check");
            return;
        }

        List<Long> configured = ServiceLocator.getSiteHierarchyService().findConfiguredHierarchy(
                Utility.toIds(updateRequest.getToDelete()),
                topLevel
        );

        logger.info("checkLowerLevelConfig()=> configured: "+configured);


        if (!configured.isEmpty()) {

            if (topLevel) {

                Map<Long, BusEntityData> m = Utility.toMap(updateRequest.getToDelete());


                result.failed(
                        ExceptionReason.SiteHierarchyUpdateReason.SITE_HIER_ERROR_TOP_LEVEL_CONFIG,
                        new StringArgument(Utility.toCommaString(configured)));

            } else {

                result.failed(
                        ExceptionReason.SiteHierarchyUpdateReason.SITE_HIER_ERROR_SUB_LEVEL_CONFIG,
                        new StringArgument(Utility.toCommaString(configured)));


            }

        }

        logger.info("checkLowerLevelConfig()=> END");

    }

    private void checkDuplicated(ValidationRuleResult result, Long levelId, UpdateRequest<BusEntityData> updateRequest) {

        Set<String> errors = new HashSet<String>();
        Set<String> names = new HashSet<String>();

        String typeCd = Constants.EMPTY;

        if (updateRequest.getToCreate() != null) {
            for (BusEntityData siteHierarchyD : updateRequest.getToCreate()) {
                names.add(siteHierarchyD.getShortDesc().toUpperCase());
                typeCd = siteHierarchyD.getBusEntityTypeCd();
            }
        }

        Map<String, Long> namesMap = new HashMap<String, Long>();
        if (updateRequest.getToUpdate() != null) {
            for (BusEntityData siteHierarchyD : updateRequest.getToUpdate()) {
                namesMap.put(siteHierarchyD.getShortDesc().toUpperCase(), siteHierarchyD.getBusEntityId());
                typeCd = siteHierarchyD.getBusEntityTypeCd();
            }
        }

        List<String> namesL = new ArrayList<String>();
        namesL.addAll(names);

        namesL.addAll(namesMap.keySet());
        if (namesL.isEmpty()) {
            return;
        }


        List<BusEntityData> duplicated = ServiceLocator
                .getSiteHierarchyService()
                .findSiteHierarchyChildItems(levelId, typeCd, namesL);

        if (duplicated != null) {
            for (BusEntityData d : duplicated) {
                String dName = (d.getShortDesc() != null) ? d.getShortDesc().toUpperCase() : d.getShortDesc();
                if ((namesMap.containsKey(dName) && !namesMap.get(dName).equals(d.getBusEntityId())) || names.contains(dName)) {
                    errors.add(dName);
                }
            }
        }

        if (!errors.isEmpty()) {
            for (String errot : errors) {
                result.failed(ExceptionReason.SiteHierarchyUpdateReason.DUPLICATED_NAME, new StringArgument(errot));
            }
        }

    }

    private Long getStoreId() {
        return storeId;
    }

    private Long getParentLevelId() {
        return parentLevelId;
    }

    private Long getEndLevelId() {
        return endLevelId;
    }

    public Object getLevelAssocCd() {
        return levelAssocCd;
    }

    private String getElementTypeCode() {
        return elementTypeCode;
    }

    private UpdateRequest<BusEntityData> getUpdateRequest() {
        return updateRequest;
    }
}
