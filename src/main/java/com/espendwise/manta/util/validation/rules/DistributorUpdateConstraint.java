package com.espendwise.manta.util.validation.rules;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.DistributorIdentView;
import com.espendwise.manta.service.DistributorService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.StoreDistributorCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;


public class DistributorUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(DistributorUpdateConstraint.class);

    private DistributorIdentView distributor;
    private Long storeId;

    public DistributorUpdateConstraint(Long storeId, DistributorIdentView distributor) {
        this.distributor = distributor;
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public DistributorIdentView getDistributor() {
        return distributor;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getDistributor()== null) {
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
        DistributorService distributorService = getDistributorService();

        StoreDistributorCriteria criteria = new StoreDistributorCriteria();
        criteria.setStoreId(getStoreId());
        criteria.setDistributorNames(Utility.toList(getDistributor().getBusEntityData().getShortDesc()));

        List<BusEntityData> distributors = distributorService.findDistributors(criteria);
        List<BusEntityData> existingDistributors = new ArrayList<BusEntityData>();
        
        //if the distributor being saved is in the list then remove it, since it should not be considered
        //for the uniqueness test.
        if (Utility.isSet(distributors)) {
        	Iterator<BusEntityData> distributorIterator = distributors.iterator();
        	while (distributorIterator.hasNext()) {
        		BusEntityData distributor = distributorIterator.next();
        		if (!distributor.getBusEntityId().equals(getDistributor().getBusEntityData().getBusEntityId())) {
        			existingDistributors.add(distributor);
        		}
        	}
        }

        if (existingDistributors.size() > 0) {
           BusEntityData distributor = existingDistributors.get(0);
            result.failed(ExceptionReason.DistributorUpdateReason.DISTRIBUTOR_MUST_BE_UNIQUE,
                    new ObjectArgument<BusEntityData>(distributor));
        }
    }

    public DistributorService getDistributorService() {
        return ServiceLocator.getDistributorService();
    }

}
