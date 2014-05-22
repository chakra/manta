package com.espendwise.manta.history;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.service.HistoryService;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

@Component
@Aspect
public class ShoppingControlHistorian {
	
    private static final Logger logger = Logger.getLogger(ShoppingControlHistorian.class);
	
    @Autowired
	private HistoryService historyService;

    public ShoppingControlHistorian() {
    }
    
    public ShoppingControlHistorian(HistoryService historyService) {
        this.historyService = historyService;
    }
	
    @Pointcut("execution(* com.espendwise.manta.dao.ShoppingControlDAOImpl.updateShoppingControls(..))")
    public void handleUpdateShoppingControls() {
    }

    @AfterReturning(pointcut="handleUpdateShoppingControls()", returning="returnValue")
    public void recordUpdateShoppingControlsSuccess(JoinPoint jp, Map<String,List<ShoppingControlItemView>> returnValue) throws Throwable {
    	if (Utility.isSet(returnValue)) {
    		List<ShoppingControlItemView> createdControls = returnValue.get(RefCodeNames.HISTORY_TYPE_CD.CREATED);
    		if (Utility.isSet(createdControls)) {
    			Iterator<ShoppingControlItemView> createdControlIterator = createdControls.iterator();
    			while (createdControlIterator.hasNext()) {
    				ShoppingControlItemView createdControl = createdControlIterator.next();
    				HistoryRecord historyRecord = HistoryRecord.getInstance(HistoryRecord.TYPE_CODE_CREATE_SHOPPING_CONTROL);
    		    	HistoryData historyData = historyRecord.describeIntoHistoryData(createdControl);
    		    	try {
    		    		historyService.createHistoryRecord(historyData, historyRecord.getInvolvedObjects(createdControl), historyRecord.getSecurityObjects(createdControl));
    		    	}
    		    	catch (Exception e) {
    		    		logger.error("Exception occurred in ShoppingControlHistorian.recordUpdateShoppingControlsSuccess: " + e.toString());
    		    	}
    			}
    		}
    		List<ShoppingControlItemView> modifiedControls = returnValue.get(RefCodeNames.HISTORY_TYPE_CD.MODIFIED);
    		if (Utility.isSet(modifiedControls)) {
    			Iterator<ShoppingControlItemView> modifiedControlIterator = modifiedControls.iterator();
    			while (modifiedControlIterator.hasNext()) {
    				ShoppingControlItemView modifiedControl = modifiedControlIterator.next();
    				HistoryRecord historyRecord = HistoryRecord.getInstance(HistoryRecord.TYPE_CODE_MODIFY_SHOPPING_CONTROL);
    		    	HistoryData historyData = historyRecord.describeIntoHistoryData(modifiedControl);
    		    	try {
    		    		historyService.createHistoryRecord(historyData, historyRecord.getInvolvedObjects(modifiedControl), historyRecord.getSecurityObjects(modifiedControl));
    		    	}
    		    	catch (Exception e) {
    		    		logger.error("Exception occurred in ShoppingControlHistorian.recordUpdateShoppingControlsSuccess: " + e.toString());
    		    	}
    			}
    		}
    	}
    }

    @AfterThrowing(pointcut="handleUpdateShoppingControls()", throwing="ex")
    public void recordUpdateShoppingControlsException(JoinPoint jp, Throwable ex) throws Throwable {
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
