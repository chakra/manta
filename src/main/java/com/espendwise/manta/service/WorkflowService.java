package com.espendwise.manta.service;


import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.data.SiteWorkflowData;
import com.espendwise.manta.model.view.*;

import java.util.List;

public interface WorkflowService {


	    public WorkflowData updateWorkflow(WorkflowData pUpdateWorkflowData)  throws DatabaseUpdateException;

	    public List<WorkflowData> findWorkflowCollection(Long pBusEntityId);

        public List<WorkflowData> findWorkflowCollection(Long busEntityId, String typeCd);

        public List<SiteWorkflowListView> findWorkflowCollection(Long accountId, Long siteId, String typeCd);

	    public WorkflowData findWorkflow(Long pWorkflowId, String pWorkflowType) ;

//	    public WorkflowData createWorkflow(WorkflowData pData) throws DatabaseUpdateException;

	    public List<WorkflowRuleData> findWorkflowRules(Long pWorkflowId, String pWorkflowType);

	    public void deleteWorkflow(Long pWorkflowId)  throws DatabaseUpdateException ;

//	    public WorkflowRuleData findWorkflowRule(Long pWorkflowRuleId, String pWorkflowType);

	    public List<WorkflowRuleData> findWorkflowRuleRecords(Long pWorkflowId, String pWorkflowType);

	    public WorkflowRuleData updateWorkflowRule(WorkflowRuleData pRule, String pWorkflowType)  throws DatabaseUpdateException  ;

	    public void deleteWorkflowRules(List<WorkflowRuleData> selected, String pWorkflowType, Long pWorkflowId)  ;

	    public void deleteRuleRecords(List<WorkflowRuleData> pRuleRecords, String pWorkflowType);
	    
	    public void configureWorkflowToSites(Long workflowId, String workflowType, List<SiteListView> selected, List<SiteListView> deSelected);

	    public List<WorkflowRuleData> findRulesInUse(Long pWorkflowId, String pWorkflowType, List<Long> pRuleIds) ;

//	    public List<WorkflowAssocData> findWorkflowRuleAssoc( Long pWorkflowId, Long pWorkflowRuleId, String pWorkflowType)  ;

	    public List<WorkflowAssocData> findWorkflowAssocCollection( Long pWorkflowId, Long pWorkflowRuleId, String pWorkflowType)  ;

	    public void updateWorkflowAssoc(List<WorkflowAssocData> pRuleAssocList, Long pWorkflowRuleId, String pWorkflowType)  throws DatabaseUpdateException  ;
	    
	    public Long findMaxRuleSeq(Long pWorkflowId, String pWorkflowType);

	    public List<SiteWorkflowData> findWorkflowToSitesAssoc(Long workflowId, Long siteId, String pWorkflowType) ;

        public void assignSiteWorkflow(Long siteId, Long workflowId, String workflowType);

}