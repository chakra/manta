package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;


public class UserUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(UserUpdateConstraint.class);

    private Long userId;
    private String userName;

    public UserUpdateConstraint(Long userId, String userName) {
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
 /*       if (getUserName()== null) {
            return null;
        }
*/

        ValidationRuleResult result = new ValidationRuleResult();
        
        UserService userService = getUserService();
        UserData user = null;
        try {
        	user = userService.findByUserName(getUserName(), false);
        } catch (Exception e) {
        	logger.info("apply()=> Exception : " + e.getMessage());
            result.failed(ExceptionReason.UserUpdateReason.USER_MUST_BE_UNIQUE1,
                    new ObjectArgument<String>(getUserName())
            );
            return result; 
        }
        if (user != null) {
            if (getUserName().equals(user.getUserName())) {
                if (!user.getUserId().equals(getUserId())) {
                    result.failed(ExceptionReason.UserUpdateReason.USER_MUST_BE_UNIQUE1,
                                  new ObjectArgument<String>(getUserName())
                    );
                    return result;              

                }
            }
        }


        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }


    public UserService getUserService() {
        return ServiceLocator.getUserService();
    }

}
