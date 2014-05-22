package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.AllUserData;
import com.espendwise.manta.service.AllUserService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;


public class UserUpdateMainConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(UserUpdateMainConstraint.class);

    private Long userId;
    private String userName;

    public UserUpdateMainConstraint(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getUserName()== null) {
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
        AllUserService allUserService = getAllUserService();

        AllUserData allUser = allUserService.findByUserName(getUserName());

        if (allUser != null) {
            if (getUserName().equals(allUser.getUserName())) {
                if (!allUser.getAllUserId().equals(getUserId())) {
                    result.failed(ExceptionReason.UserUpdateReason.USER_MUST_BE_UNIQUE,
                                  new ObjectArgument<AllUserData>(allUser)
                    );
                }
            }
        }
    }

    public AllUserService getAllUserService() {
        return ServiceLocator.getAllUserService();
    }

}
