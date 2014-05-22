package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.view.ManufacturerIdentView;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.service.ManufacturerService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.criteria.StoreManufacturerCriteria;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;
import java.util.*;


public class ManufacturerUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(ManufacturerUpdateConstraint.class);

    private ManufacturerIdentView manufacturer;
    private Long storeId;

    public ManufacturerUpdateConstraint(Long storeId, ManufacturerIdentView manufacturer) {
        this.manufacturer = manufacturer;
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public ManufacturerIdentView getManufacturer() {
        return manufacturer;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getManufacturer()== null) {
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
        ManufacturerService manufacturerService = getManufacturerService();

        ManufacturerIdentView manuf =  getManufacturer();

        StoreManufacturerCriteria criteria = new StoreManufacturerCriteria();
        criteria.setStoreId(getStoreId());
        criteria.setName(manuf.getBusEntityData().getShortDesc());
        criteria.setFilterType(Constants.FILTER_TYPE.EXACT_MATCH);

        List<BusEntityData> manufacturers = manufacturerService.findManufacturers(criteria);

        if (Utility.isSet(manufacturers) && manufacturers.size() > 0) {

            boolean errorReported = false;

            for (BusEntityData dbManuf : manufacturers) {

                if (!Utility.strNN(dbManuf.getShortDesc()).equalsIgnoreCase(manuf.getBusEntityData().getShortDesc())) {
                    continue;
                }
                
                if ((manuf.getBusEntityData().getBusEntityId() == null || 
                		dbManuf.getBusEntityId().longValue() != manuf.getBusEntityData().getBusEntityId()) &&
                    !errorReported) {

                	result.failed(ExceptionReason.ManufacturerUpdateReason.MANUFACTURER_MUST_BE_UNIQUE,
                				new ObjectArgument<BusEntityData>(manuf.getBusEntityData()));
                	errorReported = true;
                }
            }
        }
    }

    public ManufacturerService getManufacturerService() {
        return ServiceLocator.getManufacturerService();
    }

}
