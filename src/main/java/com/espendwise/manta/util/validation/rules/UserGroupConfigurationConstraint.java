package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

public class UserGroupConfigurationConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(UserGroupConfigurationConstraint.class);

    private Long storeId;
    private Long userId;
    private UpdateRequest<Long> updateRequest;

    public UserGroupConfigurationConstraint(Long userId, Long storeId, UpdateRequest<Long> updateRequest) {
        this.userId = userId;
        this.storeId = storeId;
        this.updateRequest = updateRequest;
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

