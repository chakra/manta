package com.espendwise.manta.util.validation.rules;

import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.List;


public class SiteHierarchyConfigurationDuplicateConstraint implements ValidationRule {
    private static final Logger logger = Logger.getLogger(SiteHierarchyConfigurationDuplicateConstraint.class);


    private Long levelId;
    private Long locationId;

    public SiteHierarchyConfigurationDuplicateConstraint(Long levelId, Long locationId) {
       this.levelId = levelId;
       this.locationId = locationId;
    }

    @Override
    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getLevelId() == null) {
            return null;
        }

        ValidationRuleResult result = new ValidationRuleResult();


        if (Utility.longNN(getLocationId()) > 0) {

            List<BusEntityAssocData> beaList = ServiceLocator.getSiteHierarchyService().findAssoc(
                    getLocationId(),
                    null,
                    RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT
            );


            if (beaList.size() > 1) {

               result.failed( ExceptionReason.SiteHierarchyUpdateReason.MULTIPLE_SITE_CONFIGURATION,
                       new NumberArgument(getLocationId()));


            }


        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }

    public Long getLevelId() {
        return levelId;
    }

    public Long getLocationId() {
        return locationId;
    }
}
