package com.espendwise.manta.service;

import com.espendwise.manta.dao.GroupAssocDAO;
import com.espendwise.manta.dao.GroupAssocDAOImpl;
import com.espendwise.manta.dao.GroupDAO;
import com.espendwise.manta.dao.GroupDAOImpl;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.GroupConfigAllListView;
import com.espendwise.manta.model.view.GroupConfigListView;
import com.espendwise.manta.model.view.GroupFunctionListView;
import com.espendwise.manta.model.view.GroupHeaderView;
import com.espendwise.manta.model.view.GroupListView;
import com.espendwise.manta.model.view.GroupReportListView;
import com.espendwise.manta.model.view.GroupView;
import com.espendwise.manta.util.criteria.ApplicationFunctionSearchCriteria;
import com.espendwise.manta.util.criteria.GroupConfigSearchCriteria;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.criteria.ReportSearchCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.GroupUpdateConstraint;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.Map;

@Service
public class GroupServiceImpl extends DataAccessService implements GroupService {

    @Override
    public List<GroupData> findGroupsByCriteria(GroupSearchCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);

        return groupDao.findGroupsByCriteria(criteria);
    }
    
    @Override
    public List<GroupListView> findGroupViewsByCriteria(GroupSearchCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);

        return groupDao.findGroupViewsByCriteria(criteria);
    }
	
	public GroupView getGroupView(Long groupId){
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);

        return groupDao.getGroupView(groupId);
	}

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)    
	public GroupView saveGroup(GroupView groupView)
			throws DatabaseUpdateException, IllegalDataStateException {
		
        // validate unique if Group Name
		// validate group to store association
		ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new GroupUpdateConstraint(groupView));
        validation.validate();

		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
		return groupDao.saveGroup(groupView);
	}

	@Override
	public GroupHeaderView findGroupHeader(Long groupId) {
		Query q = getEntityManager().createQuery("Select new com.espendwise.manta.model.view.GroupHeaderView(group.groupId, group.shortDesc, group.groupTypeCd)" +
                " from  GroupData group where group.groupId = (:groupId) "
        );

        q.setParameter("groupId", groupId);
        List x = q.getResultList();
        return !x.isEmpty() ? (GroupHeaderView) x.get(0) : null;
	}
	
	@Override
	public List<GroupReportListView> findGroupReportViewsByCriteria(ReportSearchCriteria criteria) {
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
		return groupDao.findGroupReportViewsByCriteria(criteria);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void configureGroupReports(Long groupId, List<Long> configuredIds, List<Long> notConfiguredIds){
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
		groupDao.configureGroupReports(groupId, configuredIds, notConfiguredIds);
	}
	
	@Override
	public Map<String, List<GroupFunctionListView>> findGroupFunctionMapByCriteria(ApplicationFunctionSearchCriteria criteria){
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
		return groupDao.findGroupFunctionMapByCriteria(criteria);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)	
	public void configureGroupFunctions(Long groupId, List<String> configuredFunctions, List<String> notConfiguredFunctions){
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
		groupDao.configureGroupFunctions(groupId, configuredFunctions, notConfiguredFunctions);
	}
	
	@Override
	public List<GroupConfigListView> findGroupConfigByCriteria(GroupConfigSearchCriteria criteria){
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
		return groupDao.findGroupConfigByCriteria(criteria);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)	
	public void configureGroupAssocioation(Long groupId, List configured,List unConfigured, String groupAssocCd){
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
		groupDao.configureGroupAssocioation(groupId, configured, unConfigured, groupAssocCd);
	}
	
	@Override	
	public List<GroupConfigAllListView> findGroupConfigAllByCriteria(GroupConfigSearchCriteria criteria){
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
		return groupDao.findGroupConfigAllByCriteria(criteria);
	}
	
	@Override	
	public List<String> getStoreAssociations(Long groupId, String groupTypeCd, Long storeIdExcluded){
		EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);
        return groupDao.getStoreAssociations(groupId, groupTypeCd, storeIdExcluded);
	}
	
	@Override	
	public List<GroupAssocData> getGroupAssociation(Long groupId, String groupAssocCd) {
		EntityManager entityManager = getEntityManager();
        GroupAssocDAO groupAssocDao = new GroupAssocDAOImpl(entityManager);
        return groupAssocDao.getGroupAssociation(groupId, groupAssocCd);
	}
}
