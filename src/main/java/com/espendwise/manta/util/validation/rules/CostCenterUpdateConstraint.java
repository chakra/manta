package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.view.CostCenterView;
import com.espendwise.manta.model.view.CostCenterListView;
import com.espendwise.manta.model.data.CostCenterData;
import com.espendwise.manta.service.CostCenterService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.criteria.CostCenterListViewCriteria;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;
import java.util.*;


public class CostCenterUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(CostCenterUpdateConstraint.class);

    private CostCenterView costCenter;
    private Long storeId;

    public CostCenterUpdateConstraint(Long storeId, CostCenterView costCenter) {
        this.costCenter = costCenter;
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public CostCenterView getCostCenter() {
        return costCenter;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getCostCenter()== null) {
            return null;
        }


        ValidationRuleResult result = new ValidationRuleResult();

        //checkUnique(result);  MANTA-354 Not need check uniq cost center
        if (result.isFailed()) {
            return result;
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }

    private void checkUnique(ValidationRuleResult result) {
        CostCenterService costCenterService = getCostCenterService();

        CostCenterView costCenter =  getCostCenter();
        CostCenterListViewCriteria criteria = new CostCenterListViewCriteria(getStoreId(), 1000);
        criteria.setStoreId(getStoreId());
        criteria.setCostCenterName(costCenter.getCostCenterData().getShortDesc());
        criteria.setFilterType(Constants.FILTER_TYPE.EXACT_MATCH);

        List<CostCenterListView> costCenters = costCenterService.findCostCentersByCriteria(criteria);
        if (Utility.isSet(costCenters) && costCenters.size() > 0) {

            for (CostCenterListView dbCostCenter : costCenters) {
                if (!Utility.strNN(dbCostCenter.getCostCenterName()).equalsIgnoreCase(costCenter.getCostCenterData().getShortDesc())) {
                    continue;
                }

                if ( costCenter.getCostCenterData().getCostCenterId() == null ||
                    (costCenter.getCostCenterData().getCostCenterId().longValue() !=
                                dbCostCenter.getCostCenterId())) {
                    result.failed(
                        ExceptionReason.CostCenterUpdateReason.COST_CENTER_MUST_BE_UNIQUE,
                            new ObjectArgument<CostCenterData>(costCenter.getCostCenterData())
                    );
                    break;
                }
            }
        }
    }

    public CostCenterService getCostCenterService() {
        return ServiceLocator.getCostCenterService();
    }

}
