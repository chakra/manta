package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.List;

public class UserNotificationConfigurationConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(UserNotificationConfigurationConstraint.class);

    private Long storeId;
    private UserData userData;
    private List<PropertyData> propertyDataList;

    public UserNotificationConfigurationConstraint(Long storeId, UserData userData, List<PropertyData> propertyDataList) {
        this.storeId = storeId;
        this.userData = userData;
        this.propertyDataList = propertyDataList;
    }

    public Long getStoreId() {
        return storeId;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");


        ValidationRuleResult result = new ValidationRuleResult();


        result.success();

        logger.info("apply()=>END");

        return result;
    }



}

