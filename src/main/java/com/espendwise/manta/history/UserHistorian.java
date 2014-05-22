package com.espendwise.manta.history;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.view.UserGroupsView;
import com.espendwise.manta.model.view.UserIdentView;
import com.espendwise.manta.service.HistoryService;
import com.espendwise.manta.util.Utility;

@Component
@Aspect
public class UserHistorian {
	
    private static final Logger logger = Logger.getLogger(UserHistorian.class);
	
    @Autowired
	private HistoryService historyService;

    public UserHistorian() {
    }
    
    public UserHistorian(HistoryService historyService) {
        this.historyService = historyService;
    }
    
    //NOTE - this class currently handles the case of user creation/modification against a single database.
    //The case of user creation/modification against multiple databases is not implemented.  To do so, please
    //see the save() method of UserController.java, examine the classes and methods involved in that functionality,
    //and add the appropriate pointcut definitions and advice to this class.
	
    @Pointcut("execution(* com.espendwise.manta.dao.UserDAOImpl.createUserIdent(..))")
    public void handleCreateUserIdent() {
    }
	
    @Pointcut("execution(* com.espendwise.manta.dao.UserDAOImpl.modifyUserIdent(..))")
    public void handleModifyUserIdent() {
    }
	
    @Pointcut("execution(* com.espendwise.manta.dao.GroupDAOImpl.configureUserGroups(..))")
    public void handleConfigureUserGroups() {
    }

    @AfterReturning(pointcut="handleCreateUserIdent()", returning="returnValue")
    public void recordCreateUserIdentSuccess(JoinPoint jp, UserIdentView returnValue) throws Throwable {
    	if (Utility.isSet(returnValue)) {
    		HistoryRecord historyRecord = HistoryRecord.getInstance(HistoryRecord.TYPE_CODE_CREATE_USER);
    		HistoryData historyData = historyRecord.describeIntoHistoryData(returnValue);
    		try {
    		    historyService.createHistoryRecord(historyData, historyRecord.getInvolvedObjects(returnValue), historyRecord.getSecurityObjects(returnValue));
    		}
    		catch (Exception e) {
    		    logger.error("Exception occurred in UserHistorian.recordCreateUserIdentSuccess: " + e.toString());
    		}
    	}
    }

    @AfterThrowing(pointcut="handleCreateUserIdent()", throwing="ex")
    public void recordCreateUserIdentException(JoinPoint jp, Throwable ex) throws Throwable {
    	StringBuilder builder = new StringBuilder(100);
    	builder.append("Exception \"");
    	builder.append(ex.getClass());
    	builder.append("\" occurred in method \"");
    	builder.append(jp.getSignature());
    	builder.append("\" - message = ");
    	builder.append(ex.getMessage());
    	builder.append(". No history record was recorded.");
    	logger.error(builder.toString());
    }

    @AfterReturning(pointcut="handleModifyUserIdent()", returning="returnValue")
    public void recordModifyUserIdentSuccess(JoinPoint jp, UserIdentView returnValue) throws Throwable {
    	if (Utility.isSet(returnValue)) {
    		HistoryRecord historyRecord = HistoryRecord.getInstance(HistoryRecord.TYPE_CODE_MODIFY_USER);
    		HistoryData historyData = historyRecord.describeIntoHistoryData(returnValue);
    		try {
    		    historyService.createHistoryRecord(historyData, historyRecord.getInvolvedObjects(returnValue), historyRecord.getSecurityObjects(returnValue));
    		}
    		catch (Exception e) {
    		    logger.error("Exception occurred in UserHistorian.recordModifyUserIdentSuccess: " + e.toString());
    		}
    	}
    }

    @AfterThrowing(pointcut="handleModifyUserIdent()", throwing="ex")
    public void recordModifyUserIdentException(JoinPoint jp, Throwable ex) throws Throwable {
    	StringBuilder builder = new StringBuilder(100);
    	builder.append("Exception \"");
    	builder.append(ex.getClass());
    	builder.append("\" occurred in method \"");
    	builder.append(jp.getSignature());
    	builder.append("\" - message = ");
    	builder.append(ex.getMessage());
    	builder.append(". No history record was recorded.");
    	logger.error(builder.toString());
    }

    @AfterReturning(pointcut="handleConfigureUserGroups()", returning="returnValue")
    public void recordConfigureUserGroupsSuccess(JoinPoint jp, UserGroupsView returnValue) throws Throwable {
    	if (Utility.isSet(returnValue)) {
    		HistoryRecord historyRecord = HistoryRecord.getInstance(HistoryRecord.TYPE_CODE_UPDATE_USER_GROUPS);
    		HistoryData historyData = historyRecord.describeIntoHistoryData(returnValue);
    		try {
    		    historyService.createHistoryRecord(historyData, historyRecord.getInvolvedObjects(returnValue), historyRecord.getSecurityObjects(returnValue));
    		}
    		catch (Exception e) {
    		    logger.error("Exception occurred in UserHistorian.recordConfigureUserGroupsSuccess: " + e.toString());
    		}
    	}
    }

    @AfterThrowing(pointcut="handleConfigureUserGroups()", throwing="ex")
    public void recordConfigureUserGroupsException(JoinPoint jp, Throwable ex) throws Throwable {
    	StringBuilder builder = new StringBuilder(100);
    	builder.append("Exception \"");
    	builder.append(ex.getClass());
    	builder.append("\" occurred in method \"");
    	builder.append(jp.getSignature());
    	builder.append("\" - message = ");
    	builder.append(ex.getMessage());
    	builder.append(". No history record was recorded.");
    	logger.error(builder.toString());
    }

}
