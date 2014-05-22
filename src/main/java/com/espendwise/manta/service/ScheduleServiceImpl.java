package com.espendwise.manta.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.dao.ScheduleDAO;
import com.espendwise.manta.dao.ScheduleDAOImpl;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ScheduleData;
import com.espendwise.manta.model.data.ScheduleDetailData;
import com.espendwise.manta.model.view.CorporateScheduleView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.ScheduleView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.CorporateScheduleDataCriteria;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.CorporateScheduleAccountConfigUpdateConstraint;
import com.espendwise.manta.util.validation.rules.CorporateScheduleDeleteConstraint;
import com.espendwise.manta.util.validation.rules.CorporateScheduleUpdateConstraint;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ScheduleServiceImpl extends DataAccessService implements ScheduleService {

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public EntityHeaderView findScheduleHeader(Long scheduleId) {
        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);
        return scheduleDao.findScheduleHeader(scheduleId);
	}
	
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ScheduleView findSchedule(Long scheduleId) {
        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);
        return scheduleDao.findSchedule(scheduleId);
	}
	
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ScheduleView saveCorporateSchedule(ScheduleView schedule) {
        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new CorporateScheduleUpdateConstraint(Auth.getAuthUser().getAppUser().getContext(AppCtx.STORE).getStoreId(), schedule));
        validation.validate();
        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);
        return scheduleDao.saveCorporateSchedule(schedule);
	}
	
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean deleteCorporateSchedule(Long scheduleId) {
        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new CorporateScheduleDeleteConstraint(scheduleId));
        validation.validate();
        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);
        return scheduleDao.deleteCorporateSchedule(scheduleId);
	}
    @Override
    @Transactional(readOnly = true)
    public ScheduleData findCorporateSchedule(Long storeId, Long corporateScheduleId) {

        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);

        return scheduleDao.findCorporateSchedule(storeId, corporateScheduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CorporateScheduleView> findCorporateSchedulesByCriteria(CorporateScheduleDataCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);

        return scheduleDao.findCorporateSchedulesByCriteria(criteria);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDetailData> findCorporateScheduleDetailsByCriteria(CorporateScheduleDataCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);

        return scheduleDao.findCorporateScheduleDetailsByCriteria(criteria);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findScheduleAccountsByCriteria(StoreAccountCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);

        return scheduleDao.findScheduleAccountsByCriteria(criteria);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureScheduleAccounts(Long scheduleId, Long storeId, List<BusEntityData> selected, List<BusEntityData> deselected, Boolean removeSiteAssocToo) {
    	if (!deselected.isEmpty() && !removeSiteAssocToo){
	    	ServiceLayerValidation validation = new ServiceLayerValidation();
	        validation.addRule(new CorporateScheduleAccountConfigUpdateConstraint(storeId, scheduleId, Utility.toIds(deselected)));
	        validation.validate();
    	}
    	
        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);

        scheduleDao.configureScheduleAccounts(scheduleId, storeId, selected, deselected, removeSiteAssocToo);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureAllScheduleAccounts(Long scheduleId, Long storeId, Boolean associateAllAccounts, Boolean associateAllSites) {

        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);

        scheduleDao.configureAllScheduleAccounts(scheduleId, storeId, associateAllAccounts, associateAllSites);
    }
        
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureScheduleSites(Long scheduleId, Long storeId, List<SiteListView> selected, List<SiteListView> deselected) {

        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);

        scheduleDao.configureScheduleSites(scheduleId, storeId, selected, deselected);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<SiteListView> findScheduleSitesByCriteria(SiteListViewCriteria criteria) {
        EntityManager entityManager = getEntityManager();
        ScheduleDAO scheduleDao = new ScheduleDAOImpl(entityManager);

        return scheduleDao.findScheduleSitesByCriteria(criteria);
    }

}