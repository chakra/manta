package com.espendwise.manta.service;


import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.SiteWorkflowData;
import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.WorkflowRuleInUseConstraint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class WorkflowServiceImpl extends DataAccessService implements WorkflowService {

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public WorkflowData findWorkflow(Long pWorkflowId, String pWorkflowType) {
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        
        WorkflowData w = dao.findWorkflowById(pWorkflowId, pWorkflowType);
	    return w;
    }

/*    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public WorkflowData createWorkflow(WorkflowData pData) throws  DatabaseUpdateException  {

    	
    	if (pData == null) {
//		    throw new RemoteException("Workflow.createWorkflow error: "  + " empty object.");
		}  	

//		if (Utility.isSet(pData.getShortDesc()) == false) {
//		    throw new RemoteException("Workflow.createWorkflow error: "
//			    + " name required.");
//		}
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
	    dao.saveWorkflow(pData);
	    return pData;
	}
*/
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteWorkflow(Long pWorkflowId) throws   DatabaseUpdateException  {
 // TODO          WorkflowValidator.checkSites(pWorkflowId, getSession());

            EntityManager entityManager = getEntityManager();
            WorkflowDAO dao = new WorkflowDAOImpl(entityManager);

 /*           DBCriteria dbc = new DBCriteria();
    	    dbc.addEqualTo(WorkflowAssocData.WORKFLOW_ID, pWorkflowId);
    	    dao.delete(getSession(), WorkflowAssocData.class, dbc);

    	    dbc = new DBCriteria();
    	    dbc.addEqualTo(WorkflowRuleData.WORKFLOW_ID, pWorkflowId);
    	    dao.delete(getSession(), WorkflowRuleData.class, dbc);

    	    dao.delete(getSession(), WorkflowData.class, pWorkflowId);
*/    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public WorkflowData updateWorkflow(WorkflowData pUpdateWorkflowData)  throws DatabaseUpdateException  {
    	if (pUpdateWorkflowData == null) {
//		    throw new RemoteException("Workflow.createWorkflow error: "  + " empty object.");
		}
   	
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        WorkflowData workflowData = dao.saveWorkflow(pUpdateWorkflowData);
	    return workflowData;
	}
  
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<WorkflowData> findWorkflowCollection(Long pBusEntityId) {
		return findWorkflowCollection(pBusEntityId, null);
	}
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<WorkflowData> findWorkflowCollection( Long pBusEntityId, String pTypeCd) {
	        EntityManager entityManager = getEntityManager();
	        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
		    List<WorkflowData> result = dao.findWorkflowCollection(pBusEntityId, pTypeCd);
		    
	       return result;
	}

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<SiteWorkflowListView> findWorkflowCollection(Long accountId, Long siteId, String typeCd) {
	        EntityManager entityManager = getEntityManager();
	        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
		    List<SiteWorkflowListView> result = dao.findSiteWorkflowCollection(accountId, siteId, typeCd);
		    
	       return result;

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<WorkflowRuleData> findWorkflowRules(Long pWorkflowId, String pWorkflowType) {
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        List<WorkflowRuleData> rules = dao.findWorkflowRules(pWorkflowId, pWorkflowType);
        return rules;
    };

//    @Override
//    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
//    public WorkflowRuleData findWorkflowRule(Long pWorkflowRuleId, String pWorkflowType) {
//	        EntityManager entityManager = getEntityManager();
//	        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
//	        WorkflowRuleData wr =  dao.findWorkflowRuleById(pWorkflowRuleId, pWorkflowType);
//        return wr;
//    }
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<WorkflowRuleData> findWorkflowRuleRecords(Long pWorkflowId, String pWorkflowType){
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        List<WorkflowRuleData> wr =  dao.findWorkflowRuleById(pWorkflowId, pWorkflowType);
        return wr;
   	
    }

    
 /*   @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<WorkflowAssocData> findWorkflowRuleAssoc(Long pWorkflowId, Long pWorkflowRuleId, String pWorkflowType) {
	        EntityManager entityManager = getEntityManager();
	        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
	        List<WorkflowAssocData> wr =  dao.findWorkflowAssocCollection(pWorkflowId, pWorkflowRuleId, pWorkflowType);
        return wr;
    }
*/
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<WorkflowAssocData> findWorkflowAssocCollection(Long pWorkflowId, Long pWorkflowRuleId, String pWorkflowType) {
	        EntityManager entityManager = getEntityManager();
	        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
	        List<WorkflowAssocData> wr =  dao.findWorkflowAssocCollection(pWorkflowId,  pWorkflowRuleId, pWorkflowType);
        return wr;
    }

    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public WorkflowRuleData updateWorkflowRule(WorkflowRuleData pRule, String pWorkflowType)  throws DatabaseUpdateException  {

        if (pRule == null) {
//		    throw new RemoteException("Workflow.createWorkflowRule error: " + " empty object.");
		}
        
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        WorkflowRuleData rule = dao.saveWorkflowRule(pRule, pWorkflowType);
		return rule;
	}
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteWorkflowRules(List<WorkflowRuleData> selected, String pWorkflowType, Long pWorkflowId) {

    	List<Long> toDeleteRuleIds = new ArrayList<Long>();
		if (selected != null )	{
			for (WorkflowRuleData del : selected) { 
	 		   toDeleteRuleIds.add(del.getWorkflowRuleId()) ;
	    	}
		}

		ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new WorkflowRuleInUseConstraint(pWorkflowId, pWorkflowType, toDeleteRuleIds, true));
        validation.validate();

        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        dao.deleteWorkflowRules(toDeleteRuleIds, pWorkflowType, pWorkflowId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteRuleRecords(List<WorkflowRuleData> pRuleRecords, String pWorkflowType) {
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        dao.deleteRuleRecords(pRuleRecords, pWorkflowType);
    }
        
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureWorkflowToSites(Long workflowId, String workflowType, List<SiteListView> selected, List<SiteListView> deSelected){
   	
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        dao.updateWorkflowToSites(workflowId, workflowType, selected, deSelected);
   	
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<WorkflowRuleData> findRulesInUse(Long pWorkflowId, String pWorkflowType, List<Long> pRuleIds) {
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        return dao.findWorkflowQueueCollection(pWorkflowId, pWorkflowType, pRuleIds);
         

    }
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateWorkflowAssoc(List<WorkflowAssocData> pRuleAssocList, Long pWorkflowRuleId, String pWorkflowType)  throws DatabaseUpdateException  {

        if (pRuleAssocList == null) {
//		    throw new RemoteException("Workflow.createWorkflowRule error: " + " empty object.");
		}
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        dao.updateWorkflowAssoc(pRuleAssocList, pWorkflowRuleId, pWorkflowType);
		
	}
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Long findMaxRuleSeq(Long pWorkflowId, String pWorkflowType){
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        return dao.findMaxRuleSeq(pWorkflowId, pWorkflowType);
        
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<SiteWorkflowData> findWorkflowToSitesAssoc(Long workflowId, Long siteId, String pWorkflowType) {
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        return dao.findWorkflowToSitesAssoc(workflowId, siteId, pWorkflowType);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void assignSiteWorkflow(Long siteId, Long workflowId, String workflowType)  {
        EntityManager entityManager = getEntityManager();
        WorkflowDAO dao = new WorkflowDAOImpl(entityManager);
        dao.assignSiteWorkflow(siteId, workflowId, workflowType);
    }


 }
