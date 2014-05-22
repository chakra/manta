package com.espendwise.manta.service;

import java.util.List;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ScheduleData;
import com.espendwise.manta.model.data.ScheduleDetailData;
import com.espendwise.manta.model.view.CorporateScheduleView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.ScheduleView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.criteria.CorporateScheduleDataCriteria;

public interface ScheduleService {
	
	public EntityHeaderView findScheduleHeader(Long scheduleId);

	public ScheduleView findSchedule(Long scheduleId);
	
	public ScheduleView saveCorporateSchedule(ScheduleView schedule);
	
	public boolean deleteCorporateSchedule(Long scheduleId);
	
    public ScheduleData findCorporateSchedule(Long storeId, Long corporateScheduleId);

    public List<CorporateScheduleView> findCorporateSchedulesByCriteria(CorporateScheduleDataCriteria criteria);

    public List<ScheduleDetailData> findCorporateScheduleDetailsByCriteria(CorporateScheduleDataCriteria criteria) ;
	
	public List<BusEntityData> findScheduleAccountsByCriteria(StoreAccountCriteria criteria);
	
	public void configureScheduleAccounts(Long scheduleId, Long storeId, List<BusEntityData> selected, List<BusEntityData> deselected, Boolean removeSiteAssocToo);
    
    public void configureAllScheduleAccounts(Long scheduleId, Long storeId, Boolean associateAllAccounts, Boolean associateAllSites);
    
    public List<SiteListView> findScheduleSitesByCriteria(SiteListViewCriteria criteria);
    
    public void configureScheduleSites(Long scheduleId, Long storeId, List<SiteListView> selected, List<SiteListView> deselected);    
    
}
