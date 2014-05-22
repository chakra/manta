package com.espendwise.manta.dao;

import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.util.*;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowDAOImpl extends DAOImpl implements WorkflowDAO {

    private static final Logger logger = Logger.getLogger(WorkflowDAOImpl.class);

    public WorkflowDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public WorkflowData findWorkflowById(Long workflowId, String workflowType) {
    	
        boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(workflowType);
        WorkflowData  result = new  WorkflowData();
        if (workflowId != null) {
        	if (isOrca){
        		result = (WorkflowData)convert(em.find(WoWorkflowData.class, workflowId), new WorkflowData() );
        	} else {
        		result = em.find(WorkflowData.class, workflowId);
        	}	
       }
  	
    	return result;
    }

 
 
    @Override
    public List<WorkflowData> findWorkflowCollection(Long accountId, String workflowTypeCd) {
    	//STjohn
    	StringBuilder stQuery= new StringBuilder(
    			"Select workflow from WorkflowData workflow" +
                " where workflow.busEntityId = (:accountId)" );
        if (Utility.isSet(workflowTypeCd)){
        	stQuery.append (" and workflow.workflowTypeCd = (:workflowTypeCd) " );
        }        
        Query stQ = em.createQuery(stQuery.toString());

        stQ.setParameter("accountId", accountId);
        if (Utility.isSet(workflowTypeCd)){
        	stQ.setParameter("workflowTypeCd", RefCodeNames.WORKFLOW_TYPE_CD.ORDER_WORKFLOW);
        }
        // WO
       	StringBuilder woQuery= new StringBuilder(
    			"Select workflow from WoWorkflowData workflow" +
                " where workflow.busEntityId = (:accountId)" );
        if (Utility.isSet(workflowTypeCd)){
        	woQuery.append (" and workflow.workflowTypeCd = (:workflowTypeCd) " );
        }        
        Query woQ = em.createQuery(woQuery.toString());

        woQ.setParameter("accountId", accountId);
        if (Utility.isSet(workflowTypeCd)){
        	woQ.setParameter("workflowTypeCd", RefCodeNames.WORKFLOW_TYPE_CD.ORDER_WORKFLOW);
        }
        List<WorkflowData> stResult = (List<WorkflowData>) stQ.getResultList();
        List<WoWorkflowData> woResult = (List<WoWorkflowData>) woQ.getResultList();
        if (stResult == null) {
        	stResult = new ArrayList<WorkflowData>();
        }
    	if (woResult != null) {
    		for (WoWorkflowData woItem : woResult) {
	    		WorkflowData rec = new WorkflowData();
	    		stResult.add(rec);
	    		rec.setWorkflowId(woItem.getWorkflowId());
	    		rec.setBusEntityId(woItem.getBusEntityId());
	    		rec.setShortDesc(woItem.getShortDesc());
	    		rec.setWorkflowStatusCd(woItem.getWorkflowStatusCd());
	    		rec.setWorkflowTypeCd(woItem.getWorkflowTypeCd());
    		}
    	}
    	logger.info("findWorkflowCollection()===> found : "+ stResult.size());
        return stResult;

    }

    @Override
    public List<SiteWorkflowListView> findSiteWorkflowCollection(Long pAccountId, Long pSiteId, String pTypeCd) {
        List<SiteWorkflowListView> result = new ArrayList<SiteWorkflowListView>();
        List<WorkflowData> wDataList = findWorkflowCollection(pAccountId, pTypeCd);
        if (wDataList != null) {
            for (WorkflowData wData : wDataList) {
                SiteWorkflowListView siteWorkflowV = new SiteWorkflowListView();

                siteWorkflowV.setWorkflowId(wData.getWorkflowId());
                siteWorkflowV.setWorkflowName(wData.getShortDesc());
                siteWorkflowV.setStatus(wData.getWorkflowStatusCd());
                siteWorkflowV.setType(wData.getWorkflowTypeCd());
                List<WorkflowRuleData> rules = findWorkflowRules(wData.getWorkflowId(), wData.getWorkflowTypeCd());
                siteWorkflowV.setRules(rules);
                result.add(siteWorkflowV);
            }
        }
        return result;
    }

    @Override
    public WorkflowData saveWorkflow(WorkflowData wf)  throws DatabaseUpdateException  {
        boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(wf.getWorkflowTypeCd());
        if (wf.getWorkflowId() == null) {
        	if (isOrca){
        		wf = (WorkflowData)convert(super.create(convert(wf, new  WoWorkflowData())), new WorkflowData()); 
        	} else {
        		wf = super.create(wf);
        	}	

        } else {

        	if (isOrca){
        		wf = (WorkflowData)convert(super.update(convert(wf, new  WoWorkflowData())), new WorkflowData()); 
        	} else {
        		wf = super.update(wf);
        	}	
        }

        return wf;
    }  

    
    @Override
    public List<WorkflowRuleData> findWorkflowRuleById(Long pWorkflowRuleId, String pWorkflowType) {
    	
        boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(pWorkflowType);
        StringBuilder baseQuery= new StringBuilder(
    		 "Select ruleRecord from " +
    			((isOrca) ? "Wo" : "") + "WorkflowRuleData ruleRecord," +
    			((isOrca) ? "Wo" : "") + "WorkflowRuleData rule" +
             " where ruleRecord.workflowId = rule.workflowId" +
             "  and ruleRecord.ruleTypeCd = rule.ruleTypeCd" +
             "  and ruleRecord.ruleSeq = rule.ruleSeq" +    			
             "  and rule.workflowRuleId = (:workflowRuleId)" 
        	);
        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("workflowRuleId", pWorkflowRuleId);
        
        List<WorkflowRuleData> result = new ArrayList<WorkflowRuleData>();
        if (isOrca){
 			List<WoWorkflowRuleData> woRules = (List<WoWorkflowRuleData>) q.getResultList();
 			if (Utility.isSet(woRules)) {
 				logger.info("findWorkflowRuleById() =====> woRules.size()=" + woRules.size());
 				for (WoWorkflowRuleData rule: woRules){
 					result.add((WorkflowRuleData)convert(rule, new WorkflowRuleData() ));
 				}
 			}
 		} else {
 			result = (List<WorkflowRuleData>) q.getResultList();
 		}	      
        logger.info("findWorkflowRuleById() END. =====> Rules :" + result.size());
       
        return result;
    }

    @Override
    public List<WorkflowRuleData> findWorkflowRuleByType(Long pWorkflowId, String pWorkflowType, String pWorkflowRuleType, String pRuleSeq) {
        boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(pWorkflowType);
        StringBuilder baseQuery= new StringBuilder(
    			"Select workflowRule from " +
    			((isOrca) ? "Wo" : "") + "WorkflowRuleData workflowRule" +
             " where workflowRule.workflowId = (:workflowId)" +
             "  and workflowRule.workflowTuleTypeCd = (:workflowRuleType)" +
             "  and workflowRule.ruleSeq = (:ruleSeq)"     			
        	);
        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("workflowId", pWorkflowId);
        q.setParameter("workflowRuleType", pWorkflowRuleType);
        q.setParameter("ruleSeq", pRuleSeq);
        
        List<WorkflowRuleData> result = new ArrayList<WorkflowRuleData>();
        if (isOrca){
 			List<WoWorkflowRuleData> woRules = (List<WoWorkflowRuleData>) q.getResultList();
 			if (Utility.isSet(woRules)) {
 				logger.info("findWorkflowRuleByType() =====> woRules.size()=" + woRules.size());
 				for (WoWorkflowRuleData rule: woRules){
 					result.add((WorkflowRuleData)convert(rule, new WorkflowRuleData() ));
 				}
 			}
 		} else {
 			result = (List<WorkflowRuleData>) q.getResultList();
 		}	      

        logger.info("findWorkflowRuleByType() END. =====> Rules :" + result.size());
      	
    	
    	return result;
	}
   
   @Override
    public WorkflowRuleData saveWorkflowRule(WorkflowRuleData wfr, String pWorkflowType)  throws DatabaseUpdateException {
	   
       boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(pWorkflowType);
	   if (wfr.getWorkflowRuleId() == null) {
	       	if (isOrca){
	       		wfr = (WorkflowRuleData)convert(super.create(convert(wfr, new  WoWorkflowRuleData())), new WorkflowRuleData()); 
	       	} else {
	       		wfr = super.create(wfr);
	       	}	
        } else  {
        	if (Utility.isSet(wfr.getRuleExpValue())) {
	           	if (isOrca){
	           		wfr = (WorkflowRuleData)convert(super.update(convert(wfr, new  WoWorkflowRuleData())), new WorkflowRuleData()); 
	           	} else {
	           		wfr = super.update(wfr);
	           	}	
        	} else { // removing rule records which has ruleExp == null (need for skus type rule)
            	if (isOrca){
               		super.remove(convert(wfr, new  WoWorkflowRuleData())); 
               	} else {
               		super.remove(wfr);
               	}	
            	wfr = null;
        	}	
         }

        return wfr;
   }  
   
   @Override
   public List<WorkflowRuleData> findWorkflowRules(Long workflowId, String pWorkflowType) {
       boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(pWorkflowType);
       StringBuilder baseQuery= new StringBuilder(
   			"Select workflowRule from " +
   			((isOrca) ? "Wo" : "") + "WorkflowRuleData workflowRule" +
            " where workflowRule.workflowId = (:workflowId)" );
       Query q = em.createQuery(baseQuery.toString());

       q.setParameter("workflowId", workflowId);
       
       List<WorkflowRuleData> result = new ArrayList<WorkflowRuleData>();
       if (isOrca){
			List<WoWorkflowRuleData> woRules = (List<WoWorkflowRuleData>) q.getResultList();
			if (Utility.isSet(woRules)) {
				logger.info("findWorkflowRules() =====> woRules.size()=" + woRules.size());
				for (WoWorkflowRuleData rule: woRules){
					result.add((WorkflowRuleData)convert(rule, new WorkflowRuleData() ));
				}
			}
		} else {
			result = (List<WorkflowRuleData>) q.getResultList();
		}	      

       logger.info("findWorkflowRules() END. =====> Rules :" + result.size());
       return result;

   }
   
   @Override
   public void deleteWorkflowRules(List<Long> ruleIds, String workflowType, Long workflowId) {
	   if (workflowType != null) {
           boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(workflowType);
           StringBuilder assocQuery= new StringBuilder(
	 			"Delete from " +
	  			((isOrca) ? "Wo" : "") + "WorkflowAssocData workflowAssoc" +
	           " where workflowAssoc.workflowRuleId in (:ruleIds)" );
           Query q1 = em.createQuery(assocQuery.toString());
           
           StringBuilder baseQuery= new StringBuilder(
          			"Delete from " +
          			((isOrca) ? "Wo" : "") + "WorkflowRuleData workflowRule" +
                   " where workflowRule.workflowRuleId in (" +
	                   " Select ruleRecord.workflowRuleId from " +
	                   	((isOrca) ? "Wo" : "") + "WorkflowRuleData ruleRecord," +
	                   	((isOrca) ? "Wo" : "") + "WorkflowRuleData rule" +
	                   " where ruleRecord.workflowId = rule.workflowId" +
	                   "  and ruleRecord.ruleTypeCd = rule.ruleTypeCd" +
	                   "  and ruleRecord.ruleSeq = rule.ruleSeq" +    			
	                   "  and rule.workflowRuleId in (:ruleIds)" +
                   ")" );
           Query q2 = em.createQuery(baseQuery.toString());
           if (Utility.isSet(ruleIds)) {
        	   logger.info("deleteWorkflowRules BEGIN. =====> delete :" + ruleIds);
               q1.setParameter("ruleIds", ruleIds);
               q1.executeUpdate();
               q2.setParameter("ruleIds", ruleIds);
               q2.executeUpdate();              
           }
		   
	   }
   }
      
   @Override
   public void updateWorkflowToSites(Long workflowId, String workflowType, List<SiteListView> selected, List<SiteListView> deSelected) {
	   if (workflowId != null && workflowType != null) {
		   boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(workflowType);

		   SiteWorkflowData assoc;
		   if (Utility.isSet(selected)) {
			   for (SiteListView el : selected) {
				   // create new assoc
				   List<SiteWorkflowData> currAssocList = findWorkflowToSitesAssoc(null, el.getSiteId(), workflowType);
				   if (Utility.isSet(currAssocList)){
					   assoc = (SiteWorkflowData)currAssocList.get(0);
					   assoc.setWorkflowId(workflowId);
					   if (isOrca){
						   super.update(convert(assoc, new  WoSiteWorkflowData())) ;
					   } else{
						   super.update(assoc);
					   }
				   } else {
					   assoc = new SiteWorkflowData();
					   assoc.setSiteId(el.getSiteId());
					   assoc.setWorkflowId(workflowId);
					   if (isOrca){
						   super.create(convert(assoc, new  WoSiteWorkflowData())) ;
					   } else{
						   super.create(assoc);
					   }
				   }
			   }
		   }

		   for (SiteListView el : deSelected) { // remove unused old assocs
			   List<SiteWorkflowData> currAssocList = findWorkflowToSitesAssoc(workflowId, el.getSiteId(), workflowType);
			   if (Utility.isSet(currAssocList)){
				   assoc = (SiteWorkflowData)currAssocList.get(0);

				   if (isOrca){                             
					   em.remove(convert(assoc, new  WoSiteWorkflowData()));
				   } else {
					   em.remove(assoc);
				   } 
			   }
		   }
	   }
   }
  
   @Override
   public List<SiteWorkflowData> findWorkflowToSitesAssoc(Long workflowId, Long siteId, String pWorkflowType) {
       boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(pWorkflowType);
       StringBuilder baseQuery= new StringBuilder(
   			"Select siteWorkflow from " +
   			((isOrca) ? "Wo" : "") + "SiteWorkflowData siteWorkflow" +
            " where 1=1 " + 
            " and " +((Utility.isSet(workflowId)) ? ("siteWorkflow.workflowId = (:workflowId)" ) : "2=2") +
            " and " +((Utility.isSet(siteId)) ? ("siteWorkflow.siteId = (:siteId)" ) : "3=3" )
          
       );
       Query q = em.createQuery(baseQuery.toString());

       if (Utility.isSet(workflowId)){
    	   q.setParameter("workflowId", workflowId);
//       } else if (Utility.isSet(siteId)) {
       } 
       if (Utility.isSet(siteId)) {
    	   q.setParameter("siteId", siteId);
       }
       
       List<SiteWorkflowData> result = new ArrayList<SiteWorkflowData>();
       if (isOrca){
			List<WoSiteWorkflowData> assocList = (List<WoSiteWorkflowData>) q.getResultList();
			if (Utility.isSet(assocList)) {
				logger.info("findSiteWorkflowAssoc() =====> assocList.size()=" + assocList.size());
				for (WoSiteWorkflowData assoc: assocList){
					result.add((SiteWorkflowData)convert(assoc, new SiteWorkflowData() ));
				}
			}
		} else {
			result = (List<SiteWorkflowData>) q.getResultList();
		}	      

       logger.info("findWorkflowToSiteAssoc() END. =====> Workflow To Sites :" + result.size());
       return result;

   }
   
   @Override
   public List<WorkflowRuleData> findWorkflowQueueCollection(Long workflowId, String workflowType, List<Long> workflowRuleIds) {
      List<WorkflowRuleData> result = new ArrayList<WorkflowRuleData>();
      if (workflowId.longValue() == 0 || !Utility.isSet(workflowRuleIds)) {
    	  return result;
      }
      boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(workflowType);
       StringBuilder baseQuery= new StringBuilder(
  			"Select rule from " +
  			((isOrca) ? "WoWorkflowSt" : "Workflow") + "QueueData workflowQueue" +
            ", OrderData ord, WorkflowRuleData rule " +
  			" where " +
            " workflowQueue.orderId = ord.orderId " +
            " and workflowQueue.workflowRuleId = rule.workflowRuleId " +
  			" and workflowQueue.workflowId = (:workflowId)" +
 // 			" and workflowQueue.workflowRuleId in (:ruleIds)" +
  			" and workflowQueue.workflowRuleId in ("+
	            " Select ruleRecord.workflowRuleId from " +
	           	((isOrca) ? "Wo" : "") + "WorkflowRuleData ruleRecord," +
	           	((isOrca) ? "Wo" : "") + "WorkflowRuleData rule" +
	           " where ruleRecord.workflowId = rule.workflowId" +
	           "  and ruleRecord.ruleTypeCd = rule.ruleTypeCd" +
	           "  and ruleRecord.ruleSeq = rule.ruleSeq" +    			
	           "  and rule.workflowRuleId in (:ruleIds)" +
  			")" +
            " and ord.orderStatusCd not in (:statuses)");
     
   	   Query q = em.createQuery(baseQuery.toString());

      q.setParameter("workflowId", workflowId);
      q.setParameter("ruleIds", workflowRuleIds);
      q.setParameter("statuses", getOrderStatuses());


      
      if (isOrca){
			List<WoWorkflowRuleData> woRules = (List<WoWorkflowRuleData>) q.getResultList();
			if (Utility.isSet(woRules)) {
				logger.info("findWorkflowQueueCollection() =====> woRules.size()=" + woRules.size());
				for (WoWorkflowRuleData rule: woRules){
					result.add((WorkflowRuleData)convert(rule, new WorkflowRuleData() ));
				}
			}
		} else {
			result = (List<WorkflowRuleData>) q.getResultList();
		}	      

      logger.info("findWorkflowQueueCollection() END. =====> Queue Rules :" + result.size());
      return result;

   }

   @Override
   public void updateWorkflowAssoc(List<WorkflowAssocData> ruleAssocList, Long workflowRuleId,  String workflowType) {
       if (workflowRuleId != null && workflowType != null) {
           boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(workflowType);

           List<WorkflowAssocData> oldAssocList = findWorkflowAssocCollection(null, workflowRuleId, workflowType);
           Map<String, WorkflowAssocData> oldMap = new HashMap<String, WorkflowAssocData>();
           logger.info("updateWorkflowAssoc() ===> oldMap = " + oldMap)   ;
           
           if (Utility.isSet(oldAssocList)) {
               for (WorkflowAssocData el : oldAssocList) {
                   oldMap.put(el.getGroupId()+ el.getWorkflowAssocCd(), el);
               }
           }
           
           WorkflowAssocData assoc;
           if (Utility.isSet(ruleAssocList)) {
               for (WorkflowAssocData el : ruleAssocList) {
            	   if (el.getGroupId().longValue()==0) {
            		  continue; 
            	   }
            	   String key = el.getGroupId()+el.getWorkflowAssocCd();
                   if (oldMap.containsKey(el.getGroupId()+el.getWorkflowAssocCd())) {
                       oldMap.remove(key);
                   } else  { // create new assoc
                       assoc = new WorkflowAssocData();
                       assoc.setGroupId(el.getGroupId());
                       assoc.setWorkflowId(el.getWorkflowId());
                       assoc.setWorkflowRuleId(workflowRuleId);
                       assoc.setWorkflowAssocCd(el.getWorkflowAssocCd());
                       if (isOrca){
                    	   super.create(convert(assoc, new  WoWorkflowAssocData())) ;
                       } else{
                    	   super.create(assoc);
                       }
                   }
               }
               
           }
           
          logger.info("updateWorkflowAssoc() ===> oldMap = " + oldMap)   ;
          if (oldMap.size() > 0) {
               for (WorkflowAssocData assocData : oldMap.values()) { // remove unused old assocs
                   assoc = assocData;
                   if (isOrca){                             
                	   em.remove(convert(assoc, new  WoWorkflowAssocData()));
                   } else {
                	   em.remove(assoc);
                       logger.info("updateWorkflowAssoc() ==> Remove : assoc.getGroupId() = " + assoc.getGroupId())   ;
                   }
                   
               }
           }
       }
   }

   @Override
   public List<WorkflowAssocData> findWorkflowAssocCollection(Long pWorkflowId, Long pWorkflowRuleId, String pWorkflowType) {
       boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(pWorkflowType);
       StringBuilder baseQuery= new StringBuilder(
   			"Select workflowAssoc from " +
   			((isOrca) ? "Wo" : "") + "WorkflowAssocData workflowAssoc" +
            " where 1=1 " +
            ((pWorkflowId != null) ? " and workflowAssoc.workflowId = (:workflowId)" :"")+
            ((pWorkflowRuleId != null) ? " and  workflowAssoc.workflowRuleId = (:workflowRuleId)" :"")
   			);
       Query q = em.createQuery(baseQuery.toString());

       if (pWorkflowId != null){
    	   q.setParameter("workflowId", pWorkflowId);
       }
       if (pWorkflowRuleId != null){
           q.setParameter("workflowRuleId", pWorkflowRuleId);
       }
       
       List<WorkflowAssocData> result = new ArrayList<WorkflowAssocData>();
       if (isOrca){
			List<WoWorkflowAssocData> assocList = (List<WoWorkflowAssocData>) q.getResultList();
			if (Utility.isSet(assocList)) {
				logger.info("findWorkflowAssocCollection() =====> assocList.size()=" + assocList.size());
				for (WoWorkflowAssocData assoc: assocList){
					result.add((WorkflowAssocData)convert(assoc, new WorkflowAssocData() ));
				}
			}
		} else {
			result = (List<WorkflowAssocData>) q.getResultList();
		}	      

       logger.info("findWorkflowAssocCollectionc() END. =====> Workflow To Apply/Skip Groups :" + result.size());
       return result;

   }
   
   @Override
   public Long findMaxRuleSeq(Long pWorkflowId, String pWorkflowType) {
       boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(pWorkflowType);
	   StringBuilder baseQuery= new StringBuilder(
	     			"Select max(workflowRule.ruleSeq) from " +
	     			((isOrca) ? "Wo" : "") + "WorkflowRuleData workflowRule" +
	              " where workflowRule.workflowId = (:workflowId)" );
	         Query q = em.createQuery(baseQuery.toString());

	         q.setParameter("workflowId", pWorkflowId);
	         
	         Long result =  (Long)q.getSingleResult();
	         if (result == null) {
	        	 result = new Long(0);
	         }
	        	 
	         logger.info("findMaxRuleSeq() ===> END maxRuleSeq =" + result);
	         
	         return result;
   }

   
   private <T extends TableObject> T convert (T fromObj, T toObj) {
	  if (fromObj == null) { return toObj;} 
	  logger.info("================convert() =====> BEGIN fromObj =" + fromObj.getClass().getSimpleName() + ", toObj =" + toObj.getClass().getSimpleName());
	  try {
		  Method[] ms = fromObj.getClass().getMethods();
		  if (ms != null) {
		   for (int i = 0; i< ms.length; i++){
			  Method m = ms[i];
			  String toMethodName = "";
			  if (m.getName().startsWith("get")){
				  toMethodName = m.getName().replaceFirst("get", "set");
				  logger.info("convert() =====> fromMethodName= " +m.getName() + ", toMethodName = " + toMethodName);
				  try {
					  Method  toMethod = toObj.getClass().getMethod(toMethodName, m.getReturnType());
					  toMethod.invoke(toObj, m.invoke(fromObj));
				  } catch (NoSuchMethodException e) {
					  logger.info("convert()===== NoSuchMethodException : =====> " + e.getMessage());
				  }
	
			  }
		   }
		  } 
	  } catch (Exception ex) {
		  ex.printStackTrace();
	  }
      logger.info("===============convert() =====> END " );
      return toObj;
   }
   @Override
   public void deleteRuleRecords(List<WorkflowRuleData> pRuleRecords, String pWorkflowType ) {
       boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(pWorkflowType);
       if (Utility.isSet(pRuleRecords)) {
    	   List<Long> recIds = Utility.toIds(pRuleRecords);
    	   if (!isOrca) {
	           for (List<Long> ids : Utility.createPackages(recIds, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {
	               Query q = em.createQuery("Delete from WorkflowRuleData where workflowRuleId in (:workflowRuleId )");
	               q.setParameter(WorkflowRuleData.WORKFLOW_RULE_ID, ids);
	               q.executeUpdate();
	           }
    	   } else {
	           for (List<Long> ids : Utility.createPackages(recIds, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {
	               Query q = em.createQuery("Delete from WoWorkflowRuleData where workflowRuleId in (:workflowRuleId )");
	               q.setParameter(WoWorkflowRuleData.WORKFLOW_RULE_ID, ids);
	               q.executeUpdate();
	           }
    		   
    	   }

       }
   }

   @Override
   public void assignSiteWorkflow(Long siteId, Long newWorkflowId, String workflowType) {
       if (siteId != null) {
           boolean isOrca = RefCodeNames.WORKFLOW_TYPE_CD.SERVICE_WORKFLOW.equals(workflowType);

           List<SiteWorkflowData> oldAssocList = findWorkflowToSitesAssoc(null, siteId, workflowType);

           if (oldAssocList != null && oldAssocList.size() > 0) {
                SiteWorkflowData assoc = oldAssocList.get(0);
                if (assoc.getWorkflowId() == newWorkflowId) {
                    return; // nothing changed
                }
                if (newWorkflowId > 0) {
                    // update assoc
                    assoc.setWorkflowId(newWorkflowId);
	                if (isOrca){
	                    super.update(convert(assoc, new  WoSiteWorkflowData())) ;
	                } else{
	                    super.update(assoc);
	                }
                } else {
                    // remove  assoc
                   if (isOrca){
                	   em.remove(convert(assoc, new  WoSiteWorkflowData()));
                   } else {
                	   em.remove(assoc);
                   }
                }
           } else { // create new assoc
                if (newWorkflowId > 0) {
                    SiteWorkflowData assoc = new SiteWorkflowData();
                    assoc.setSiteId(siteId);
                    assoc.setWorkflowId(newWorkflowId);
                    if (isOrca){
                        super.create(convert(assoc, new  WoSiteWorkflowData())) ;
                    } else{
                        super.create(assoc);
                    }
                }
           }
       }

   }
   
   private List<String> getOrderStatuses (){
	      List<String> orderStatuses = new ArrayList<String>();
	      
	      //check 4 criterias below: was here before Bug # 4849 fix.
	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.REJECTED);
	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.ORDERED); // is it correct (it is an itermediate Order state) ???
	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.SENT_TO_CUST_SYSTEM); // is it correct (it is an itermediate Order state) ???
//	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.SHIPPED); // is it correct (it is an itermediate Order state) ???
	      
	      // Bug # 4849: additional RefCodeNames, which represent the "End State' of the Order: Begin
	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.ERP_REJECTED);
	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.ERP_RELEASED);
	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.INVOICED);
	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.ERP_CANCELLED);
	      orderStatuses.add(RefCodeNames.ORDER_STATUS_CD.CANCELLED);
	      // additional RefCodeNames, which represent the "End State' of the Order: End
	      return orderStatuses;
   }



}
