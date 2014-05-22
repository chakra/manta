package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.SiteWorkflowData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.SiteWorkflowListView;
import com.espendwise.manta.service.DatabaseUpdateException;

import java.util.List;

public interface WorkflowDAO {

    public WorkflowData findWorkflowById(Long workflowId, String pWorkflowType) ;

	public List<WorkflowData> findWorkflowCollection(Long pBusEntityId, String pTypeCd);

	public List<SiteWorkflowListView> findSiteWorkflowCollection(Long pAccountId, Long pSiteId, String pTypeCd);

    public void assignSiteWorkflow(Long siteId, Long workflowId, String workflowType) ;

	public WorkflowData saveWorkflow(WorkflowData wf)  throws DatabaseUpdateException ;

    public List<WorkflowRuleData> findWorkflowRuleById(Long workflowRuleId, String pWorkflowType) ;

    public List<WorkflowRuleData> findWorkflowRuleByType(Long workflowId, String pWorkflowType, String pWorkflowRuleType, String pRuleSeq) ;
    
//    public WorkflowData removeWorkflow(Long workflowId)  throws DatabaseUpdateException ;

    public WorkflowRuleData saveWorkflowRule(WorkflowRuleData wfr, String pWorkflowType)  throws DatabaseUpdateException ;

    public List<WorkflowRuleData> findWorkflowRules(Long workflowId, String pWorkflowType) ;

    public void deleteWorkflowRules(List<Long> ruleIds, String workflowType, Long workflowId) ;

    public void updateWorkflowToSites(Long workflowId, String workflowType, List<SiteListView> selected, List<SiteListView> deSelected) ;

    public List<SiteWorkflowData> findWorkflowToSitesAssoc(Long workflowId, Long siteId, String pWorkflowType) ;

    public List<WorkflowRuleData> findWorkflowQueueCollection(Long workflowId, String workflowType, List<Long> workflowRuleIds) ;

    public List<WorkflowAssocData> findWorkflowAssocCollection(Long workflowId, Long workflowRuleId, String pWorkflowType) ;
    
    public void updateWorkflowAssoc(List<WorkflowAssocData> ruleAssocList, Long workflowRuleId, String workflowType) ;

    public Long findMaxRuleSeq(Long workflowId, String pWorkflowType) ;

    public void deleteRuleRecords(List<WorkflowRuleData> pRuleRecords, String pWorkflowType ) ;

}
