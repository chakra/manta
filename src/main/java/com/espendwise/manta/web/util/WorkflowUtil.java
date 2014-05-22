package com.espendwise.manta.web.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.service.WorkflowService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.ServiceTypeUtil;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.WorkflowRuleInUseConstraint;


public class WorkflowUtil {


    private static final Logger logger = Logger.getLogger(WorkflowUtil.class);

    public static String decodeRuleExpValue (List<WorkflowRuleData> ruleRecords, WorkflowRuleData dest) throws Exception {
     	String ruleStr = "";
    	if (Utility.isSet(ruleRecords)) {
    		for ( WorkflowRuleData rule : ruleRecords ){
     			decodeRuleExpValue (rule, dest);
    		}
    	}
    	return ruleStr;
    }	

    public static String decodeRuleExpValue (WorkflowRuleData rule, WorkflowRuleData dest) throws Exception {
	        if (rule == null ){
	            return "";
	        }
	      
	        String ruleTypeCd = rule.getRuleTypeCd();
	        String ruleExp = rule.getRuleExp();
	        String ruleExpValue = rule.getRuleExpValue();
	        String ruleStr = ruleTypeCd;
	        
            if (dest != null ){

               	if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM_CATEGORY)){
		            if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID.equals(ruleExp)){
		            	ruleExp =  AppI18nUtil.getMessage("refcodes.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID",  new Object[]{}) + "=" + ruleExpValue;
		            	dest.setRuleExp(ruleExp);
		            } else if (!RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(ruleExp) &&
	                  		   !RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(ruleExp)	) {
		            	ruleExpValue = AppI18nUtil.getMessage("admin.account.workflowRuleEdit.label.hasPrice1",  new Object[]{ruleExp, ruleExpValue});
		            	dest.setRuleExpValue(ruleExpValue);
		            }	
		            //distributors will not be displayed in the table of rules(aggreed with YK) 
		        } else
              	if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU_QTY)){
		            if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.SKU_NUM.equals(ruleExp)){
//logger.info("decodeRuleExpValue()===> dest.getRuleExp()=" + dest.getRuleExp() );
						String mess = AppI18nUtil.getMessage("refcodes.WORKFLOW_RULE_EXPRESSION.SKU_NUM",  new Object[]{}) + ": ";
		            	ruleExp = (dest.getRuleExp().startsWith(mess)) ? (dest.getRuleExp() + ", " + ruleExpValue) : mess + ruleExpValue ;
		            	dest.setRuleExp(ruleExp);
		            } else {
		            	ruleExpValue = AppI18nUtil.getMessage("admin.account.workflowRuleEdit.label.isThenItems",  new Object[]{ruleExp, ruleExpValue});
		            	dest.setRuleExpValue(ruleExpValue);
		            }	
		        } else
              	if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.USER_LIMIT)){
		            String total =  AppI18nUtil.getMessage("admin.account.workflowRuleEdit.label.orderTotal",  new Object[]{RefCodeNames.WORKFLOW_RULE_EXPRESSION.GREATER_OR_EQUAL, ruleExpValue});
		            String days = AppI18nUtil.getMessage("admin.account.workflowRuleEdit.label.withinDays",  new Object[]{ruleExp});
	            	dest.setRuleExp(total);
	            	dest.setRuleExpValue(days);
		        } else
              	if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_TOTAL)){
              		if (!RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(ruleExp) &&
              			!RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(ruleExp)	) {
    	            	dest.setRuleExp(ruleExp);
    	            	dest.setRuleExpValue(ruleExpValue);
              		}
		        } else
             	if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.CATEGORY_TOTAL)){
    	            if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID.equals(ruleExp)){
		            	ruleExp =  AppI18nUtil.getMessage("refcodes.WORKFLOW_RULE_EXPRESSION.CATEGORY_ID",  new Object[]{}) + "=" + ruleExpValue;
		            	dest.setRuleExp(ruleExp);
		            } else if (!RefCodeNames.WORKFLOW_RULE_EXPRESSION.DISTR_ID.equals(ruleExp) &&
	                  		   !RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(ruleExp)	) {
		            	ruleExpValue = AppI18nUtil.getMessage("admin.account.workflowRuleEdit.label.hasOrderTotal1",  new Object[]{ruleExp, ruleExpValue});
		            	dest.setRuleExpValue(ruleExpValue);
		            }	
		        } else
            	if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM)){
		            if (RefCodeNames.WORKFLOW_RULE_EXPRESSION.ITEM_ID.equals(ruleExp)){
						String mess = AppI18nUtil.getMessage("refcodes.WORKFLOW_RULE_EXPRESSION.ITEM_ID",  new Object[]{}) + ": ";
		            	ruleExp = (dest.getRuleExp().startsWith(mess)) ? (dest.getRuleExp() + ", " + ruleExpValue) : mess + ruleExpValue ;
		            	dest.setRuleExp(ruleExp);
		            } else if (!RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(ruleExp)	) {
    	            	dest.setRuleExp(ruleExp);
    	            	dest.setRuleExpValue(ruleExpValue);
		            }	
		        } else
		        if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.SHOPPING_CONTROLS)){
		            if (!RefCodeNames.WORKFLOW_RULE_EXPRESSION.SPLIT_ORDER.equals(ruleExp)	) {
    	            	dest.setRuleExp(ruleExp);
    	            	dest.setRuleExpValue(ruleExpValue);
		            }	
		        } else
		        if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.NON_ORDER_GUIDE_ITEM)){
		            if (!RefCodeNames.WORKFLOW_RULE_EXPRESSION.INCLUDE_BUYER_LIST.equals(ruleExp)	) {
    	            	dest.setRuleExp(ruleExp);
    	            	dest.setRuleExpValue(ruleExpValue);
		            }	
		        } else
			    if (ruleTypeCd.equals(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_EXCLUDED_FROM_BUDGET)){
	    	            	dest.setRuleExp("");
	    	            	dest.setRuleExpValue("");
		        } else
		        if (ruleTypeCd.equals(RefCodeNames.WO_WORKFLOW_RULE_TYPE_CD.FREEFORM_SERVICE_TICKET_NOTIFICATION)){
		            String serviceTypeIds =   ruleExpValue;
		            String serviceTypeNames = ServiceTypeUtil.getServiceTypeNames(serviceTypeIds);
		            ruleStr = AppI18nUtil.getMessage("admin.workflow.text.ruleExpressionMultiTypes",  new Object[]{serviceTypeNames});
	            	dest.setRuleExpValue(ruleStr);
		        } else
		        if (ruleTypeCd.equals(RefCodeNames.WO_WORKFLOW_RULE_TYPE_CD.SERVICE_TICKET_SERVICE_TYPE)){
		            String serviceTypeId =   ruleExpValue;
		            String serviceTypeName = ServiceTypeUtil.getServiceTypeName(serviceTypeId);
		            ruleStr = AppI18nUtil.getMessage("admin.workflow.text.ruleExpression",  new Object[]{serviceTypeName+"("+serviceTypeId+")"});
	            	dest.setRuleExpValue(ruleStr);
		        } else
		        if (ruleTypeCd.equals(RefCodeNames.WO_WORKFLOW_RULE_TYPE_CD.SERVICE_TICKET_FREQUENCE_FOR_SERVICE_TYPE)){
		            if (Utility.isSet(ruleExpValue) ){
		                int idxOf = ruleExpValue.indexOf(":");
		                String daysNum = (idxOf >=1 ) ? ruleExpValue.substring(0, idxOf) :"";
		                String serviceType = ruleExpValue.substring(idxOf+1);
		                String serviceTypeId = serviceType;
		                String serviceTypeName = ServiceTypeUtil.getServiceTypeName(serviceTypeId);
		                ruleStr = AppI18nUtil.getMessage("admin.workflow.text.ruleExpressionWithDays",  new Object[]{serviceTypeName+"("+serviceTypeId+")", ruleExp, daysNum});
		            	dest.setRuleExpValue(ruleStr);
		            }
		        } else {
	            	dest.setRuleExp(ruleExp);
	            	dest.setRuleExpValue(ruleExpValue);
		        }
               	dest.setNextActionCd(rule.getNextActionCd().replace("Done", "").replace("-", ""));
            }
            logger.info("decodeRuleExpValue()==> ruleStr = " + dest.getRuleExpValue());
	        return ruleStr;
	    }
   	public static List<GroupData> getGroups(AppUser pAppUser, Long pStoreId){
	    GroupSearchCriteria criteria = new GroupSearchCriteria();
/*  Bug Manta-595 ( decision : only groups associated to current store should be displayed  for all administrators )
  		if (pAppUser.getIsStoreAdmin()) {
			criteria.setStoreIds(Utility.toList(pStoreId));
		}
*/		criteria.setStoreIds(Utility.toList(pStoreId));
		criteria.setGroupStatus(RefCodeNames.GROUP_STATUS_CD.ACTIVE);
		criteria.setGroupType(RefCodeNames.GROUP_TYPE_CD.USER);
	
	    List<GroupData> groups = getGroupService().findGroupsByCriteria(criteria);
	
	    return groups;
    } 
	
   	public static Map<Long, String> toMap(List<GroupData> groups){
	                
	        Map<Long, String> groupMap = new HashMap<Long, String>();
	        if (groups != null){
		        for (GroupData g : groups) {
		        	groupMap.put(g.getGroupId(), g.getShortDesc());
		        }
	        }
	        return groupMap;
  	}        

   	public static Map<Long, String> toMap(List<WorkflowAssocData> associations, Map<Long, String> groupMap){

	        Map<Long, WorkflowAssocData> ruleAssocMap = Utility.toMap(associations);
	        Map<Long, String> applySkipGroupMap = new HashMap<Long, String>();
	        if (ruleAssocMap != null){
		        Iterator it = ruleAssocMap.keySet().iterator();
		        while(it.hasNext()){
		        	Long key = (Long)it.next();
		        	WorkflowAssocData d = (WorkflowAssocData)ruleAssocMap.get(key);
		        	if (RefCodeNames.WORKFLOW_ASSOC_CD.APPLY_FOR_GROUP_USERS.equals(d.getWorkflowAssocCd())) {
		        		applySkipGroupMap.put(key, AppI18nUtil.getMessage("refcodes.WORKFLOW_ASSOC_CD.APPLY_FOR_GROUP_USERS", new Object[]{(String)groupMap.get(d.getGroupId())}));
		        	} else if (RefCodeNames.WORKFLOW_ASSOC_CD.SKIP_FOR_GROUP_USERS.equals(d.getWorkflowAssocCd())) {
		        		applySkipGroupMap.put(key, AppI18nUtil.getMessage("refcodes.WORKFLOW_ASSOC_CD.SKIP_FOR_GROUP_USERS", new Object[]{(String)groupMap.get(d.getGroupId())}));
		        	}
		        }
	        }
	   		return applySkipGroupMap;
	   	}

	   	public static List<ItemView> getItemCategories(Long pAccountId){

			CatalogData acctCat = null;
			try {
				acctCat = getAccountService().findAccountCatalog(pAccountId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    logger.info("getItemCategories()==> acctCat= " + acctCat);
		    List<ItemView> categories = new ArrayList<ItemView>();
		    if (Utility.isSet(acctCat) ){
		    	List<Long> catalogIds = Utility.toList(acctCat.getCatalogId());
		    	categories = getCatalogService().findCatalogCategories(catalogIds);
		    }
//			logger.info("getItemCategories()==> categories = " + categories);
		    
		    return categories;
		}
	   	public static Long getStoreCatalogId(Long pStoreId) throws Exception{
	   		CatalogListViewCriteria criteria = new CatalogListViewCriteria(pStoreId, Constants.FILTER_RESULT_LIMIT.CATALOG);
	   		criteria.setCatalogType(RefCodeNames.CATALOG_TYPE_CD.STORE);
	   		List<CatalogListView> catList = getCatalogService().findCatalogsByCriteria(criteria);
	    	return (Utility.isSet(catList) ) ? ((CatalogListView)catList.get(0)).getCatalogId() : new Long(0);
	   	}	
	   	
	   	public static List<WorkflowRuleData> getRulesToChangeSeq(Long pRuleSeqOrig, List<WorkflowRuleData> currRuleRecords, Long workflowId, String workflowType) {
			logger.info("getRulesToChangeSeq() ==> BEGIN. pRuleSeqOrig=" + pRuleSeqOrig +  ", workflowId="+ workflowId+ ", workflowType="+ workflowType);
			List<WorkflowRuleData> resultRulesToChangeSeq = new ArrayList<WorkflowRuleData>();
	   		List<WorkflowRuleData> ruleRecordsToValidate = new ArrayList<WorkflowRuleData>();

	   		Long newRuleSeq = ((WorkflowRuleData)currRuleRecords.get(0)).getRuleSeq();
			logger.info("getRulesToChangeSeq() ==> newRuleSeq = " + newRuleSeq);
	 
	   		// get other rules if ruleSeq for current rule has been changed
	   		
			if(newRuleSeq.longValue() > 0 && !newRuleSeq.equals(pRuleSeqOrig)) {
	   			List<WorkflowRuleData> allRules = getWorkflowService().findWorkflowRules(workflowId, workflowType);
	   			if (Utility.isSet(allRules)){
		   			logger.info("getRulesToChangeSeq() ==> allRules.size = " + allRules.size());
		   			WebSort.sort(allRules, WorkflowRuleData.RULE_SEQ, new Boolean(true));
		   			for (WorkflowRuleData r : allRules){
		   				long ruleSeq = r.getRuleSeq().longValue();
			   			long moveSeqInd = getMoveSeqIndex(newRuleSeq.longValue(), pRuleSeqOrig.longValue(), ruleSeq );
			   			logger.info("getRulesToChangeSeq() ==> ruleSeq = " + ruleSeq + ", moveSeqInd="+moveSeqInd+" =>: r.getWorkflowRuleId="+ r.getWorkflowRuleId() + ", r.getRuleTypeCd="+r.getRuleTypeCd());
			   			if (moveSeqInd != 0){
			   				r.setRuleSeq(new Long(ruleSeq + moveSeqInd));
			   				resultRulesToChangeSeq.add(r);
			   			}
		   				if (pRuleSeqOrig.longValue() > 0 && ruleSeq == pRuleSeqOrig.longValue()){
		   					ruleRecordsToValidate.add(r);
		   				}
		   			}
	   			}
	        } 
			logger.info("getRulesToChangeSeq() ==> resultRulesToChangeSeq = " + resultRulesToChangeSeq);
	   		logger.info("getRulesToChangeSeq() ==> currRuleRecords = " + currRuleRecords);
			logger.info("getRulesToChangeSeq() ==> ruleRecordsToValidate.size="+ ruleRecordsToValidate.size()+", ruleRecordsToValidate = " + ruleRecordsToValidate);

			// process validation for current rule : are there unprocessed workflow actions related to the rule
            WebSort.sort(ruleRecordsToValidate, WorkflowRuleData.WORKFLOW_RULE_ID, true);
            WebSort.sort(currRuleRecords, WorkflowRuleData.WORKFLOW_RULE_ID, true);           
            
	   		List<Long> toValidateRuleIds = new ArrayList<Long>();
            for (WorkflowRuleData vr : ruleRecordsToValidate) {
            	int index = ruleRecordsToValidate.indexOf(vr);
//    			logger.info("getRulesToChangeSeq() ==> index = " + index);
            	WorkflowRuleData cr = (WorkflowRuleData)currRuleRecords.get(index);
            	if ( vr.getWorkflowRuleId().equals(cr.getWorkflowRuleId()) &&
            		!Utility.isEqual(vr.getRuleAction(), cr.getRuleAction()) ){
            		toValidateRuleIds.add(vr.getWorkflowRuleId());
            	}
            }
			ServiceLayerValidation validation = new ServiceLayerValidation();
	        validation.addRule(new WorkflowRuleInUseConstraint(workflowId, workflowType, toValidateRuleIds, false));
	        validation.validate();

	        logger.info("getRulesToChangeSeq() ==> END.");
	   		
	   		return resultRulesToChangeSeq;
	   	}
	    
	   	private static long getMoveSeqIndex(long newSeq, long origSeq, long currSeq){
	   		boolean isMoveUp = newSeq < origSeq;
	   		boolean isMoveDown = newSeq > origSeq;
	   		if (isMoveUp) {
	   			if (currSeq < newSeq && currSeq >= origSeq ) return 0; 
	   			if (currSeq >= newSeq && currSeq < origSeq ) return 1; 
	   		}
	   		if (isMoveDown) {
	   			if (currSeq <= origSeq && currSeq > newSeq ) return 0; 
	   			if (currSeq > origSeq && currSeq <= newSeq ) return -1; 
	   		}
	   		return 0;
	   		
	   	}
	   	
	   	public static CatalogService getCatalogService() {
	        return ServiceLocator.getCatalogService();
	    }
	    public static AccountService getAccountService() {
	        return ServiceLocator.getAccountService();
	    }
	    public static WorkflowService getWorkflowService() {
	        return ServiceLocator.getWorkflowService();
	    }
	    public static GroupService getGroupService() {
	        return ServiceLocator.getGroupService();
	    }

	   	
}