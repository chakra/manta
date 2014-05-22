package com.espendwise.manta.dao;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ScheduleData;
import com.espendwise.manta.model.data.ScheduleDetailData;
import com.espendwise.manta.model.view.CorporateScheduleView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.ScheduleJoinView;
import com.espendwise.manta.model.view.ScheduleView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.ScheduleProc;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.criteria.CorporateScheduleDataCriteria;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.parser.Parse;

public class ScheduleDAOImpl extends DAOImpl implements ScheduleDAO {

    private static final Logger logger = Logger.getLogger(ScheduleDAOImpl.class);

    public ScheduleDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public CorporateScheduleView findCorporateSiteSchedule(Long storeId, Long siteId) {

        Query q = em.createQuery("Select schedule from ScheduleData schedule, ScheduleDetailData details" +
                " where schedule.scheduleId = details.scheduleId " +
                " and schedule.busEntityId=(:storeId)" +
                " and schedule.scheduleTypeCd=(:corporate)" +
                " and details.scheduleDetailCd = (:siteIdCd) " +
                " and details.value = (:siteId)");

        q.setParameter("storeId", storeId);
        q.setParameter("siteId", new NumberArgument(siteId).resolve());
        q.setParameter("siteIdCd", RefCodeNames.SCHEDULE_DETAIL_CD.SITE_ID);
        q.setParameter("corporate", RefCodeNames.SCHEDULE_TYPE_CD.CORPORATE);

        List<ScheduleData> x = q.getResultList();

        if(!x.isEmpty()){

            ScheduleData schedule = x.get(0);

            q = em.createQuery("select details from ScheduleDetailData details where details.scheduleId = (:scheduleId)");
            q.setParameter("scheduleId", schedule.getScheduleId());

            List<ScheduleDetailData> details = q.getResultList();

            return getCorporateSchedule(schedule, details);

        }

        return null;

    }

    public List<CorporateScheduleView> getCorporateSchedules(List<ScheduleJoinView> schedules) {

        List<CorporateScheduleView> scheduleListCorporate = new ArrayList<CorporateScheduleView>();

        if (schedules == null || schedules.isEmpty()) {
            return scheduleListCorporate;
        }

        for (ScheduleJoinView schedule : schedules) {

            CorporateScheduleView x = new CorporateScheduleView();

            x.setScheduleName(schedule.getSchedule().getShortDesc());
            x.setNextDeliveryDate(getNextDeliveryDate(schedule, new Date()));
            x.setScheduleId(schedule.getSchedule().getScheduleId());
            x.setAlsoDates(new ArrayList<String>());

            for (ScheduleDetailData scheduleDetail : schedule.getScheduleDetail()) {
                if (RefCodeNames.SCHEDULE_DETAIL_CD.ALSO_DATE.equals(scheduleDetail.getScheduleDetailCd())) {
                    x.getAlsoDates().add(scheduleDetail.getValue());
                } else if (RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_TIME.equals(scheduleDetail.getScheduleDetailCd())) {
                    x.setCutoffTime(scheduleDetail.getValue());
                } else if (RefCodeNames.SCHEDULE_DETAIL_CD.INV_CART_ACCESS_INTERVAL.equals(scheduleDetail.getScheduleDetailCd())) {
                    x.setIntervalHour(scheduleDetail.getValue());
                }
            }

            scheduleListCorporate.add(x);

        }


        return scheduleListCorporate;

    }

    @Override
    public EntityHeaderView findScheduleHeader(Long scheduleId) {
        Query q = em.createQuery("Select new com.espendwise.manta.model.view.EntityHeaderView(schedule.scheduleId, schedule.shortDesc)" +
                " from ScheduleData schedule where schedule.scheduleId = (:scheduleId)");
        q.setParameter("scheduleId", scheduleId);
        List x = q.getResultList();
        return !x.isEmpty() ? (EntityHeaderView) x.get(0) : null;
    }

    @Override
    public ScheduleView findSchedule(Long scheduleId) {
    	ScheduleView schedule = new ScheduleView();
    	
        Query q = em.createQuery("Select schedule from ScheduleData schedule where schedule.scheduleId = (:scheduleId)");
        q.setParameter("scheduleId", scheduleId);
        List<ScheduleData> x = q.getResultList();
        if (!x.isEmpty()) {
        	schedule.setSchedule((ScheduleData) x.get(0));
        	schedule.setScheduleDetails(getScheduleDetails(scheduleId));
        }
        return schedule;
    }
    
    private List<ScheduleDetailData> getScheduleDetails(Long scheduleId) {
    	List<ScheduleDetailData> returnValue = null;
        Query q = em.createQuery("select scheduleDetail from ScheduleDetailData scheduleDetail " +
                    "where scheduleDetail.scheduleId = (:scheduleId)");
        q.setParameter("scheduleId", scheduleId);
    	returnValue = q.getResultList();
        if (returnValue == null) {
        	returnValue = new ArrayList<ScheduleDetailData>();
        }
        return returnValue;
    }
    
    public ScheduleView saveCorporateSchedule(ScheduleView schedule) {
    	if (schedule != null && schedule.getSchedule() != null) {
    		Long scheduleId = schedule.getSchedule().getScheduleId();
	        if (scheduleId == null || scheduleId == 0) {
	            return createCorporateSchedule(schedule);
	        } else {
	            return modifyCorporateSchedule(schedule);
	        }
    	}
    	return schedule;
    }
    
    private ScheduleView createCorporateSchedule(ScheduleView schedule) {
        ScheduleData scheduleData = schedule.getSchedule();
        //set the business entity to the users current store
        scheduleData.setBusEntityId(Auth.getAuthUser().getAppUser().getContext(AppCtx.STORE).getStoreId());
    	//set the schedule type cd
        scheduleData.setScheduleTypeCd(RefCodeNames.SCHEDULE_TYPE_CD.CORPORATE);
    	//set the schedule rule cd
        scheduleData.setScheduleRuleCd(RefCodeNames.SCHEDULE_RULE_CD.DATE_LIST);
    	//set the schedule cycle
        scheduleData.setCycle(new Long(1));
    	//set the effective date
        scheduleData.setEffDate(new Date());
        //create the schedule
        scheduleData = super.create(scheduleData);
        schedule.setSchedule(scheduleData);
        createOrUpdateCorporateScheduleDetails(schedule);
        return schedule;
    }
    
    private ScheduleView modifyCorporateSchedule(ScheduleView schedule) {
        ScheduleData scheduleData = schedule.getSchedule();
        scheduleData = super.update(scheduleData);
        schedule.setSchedule(scheduleData);
        createOrUpdateCorporateScheduleDetails(schedule);
        return schedule;
    }
    
    private void createOrUpdateCorporateScheduleDetails(ScheduleView schedule) {
    	//get the existing schedule details (excluding those for site/account associations - those details
    	//are handled elsewhere). 
    	List<ScheduleDetailData> existingDetails = getScheduleDetails(schedule.getSchedule().getScheduleId());
    	Iterator<ScheduleDetailData> existingDetailIterator = existingDetails.iterator();
    	while (existingDetailIterator.hasNext()) {
    		ScheduleDetailData existingDetail = existingDetailIterator.next();
    		String detailCode = existingDetail.getScheduleDetailCd();
    		if (RefCodeNames.SCHEDULE_DETAIL_CD.ACCOUNT_ID.equals(detailCode) ||
    				RefCodeNames.SCHEDULE_DETAIL_CD.SITE_ID.equalsIgnoreCase(detailCode)) {
    			existingDetailIterator.remove();
    		}
    	}
    	//process the details passed in, updating existing details or creating new details
    	//as necessary
    	List<ScheduleDetailData> updatedDetails = new ArrayList<ScheduleDetailData>(); 
    	List<ScheduleDetailData> details = schedule.getScheduleDetails();
        if (Utility.isSet(details)) {
        	//for every detail passed in...
            for (ScheduleDetailData detail : details) {
            	boolean existingDetailUpdated = false;
            	existingDetailIterator = existingDetails.iterator();
            	while (existingDetailIterator.hasNext() && !existingDetailUpdated) {
            		ScheduleDetailData existingDetail = existingDetailIterator.next();
            		//update an existing detail if possible
            		if (existingDetail.getScheduleDetailCd().equals(detail.getScheduleDetailCd())) {
            			existingDetail.setValue(detail.getValue());
            			super.update(existingDetail);
            			updatedDetails.add(existingDetail);
            			existingDetailUpdated = true;
            			existingDetailIterator.remove();
            		}
            	}
            	//create a new detail if an existing one could not be located
            	if (!existingDetailUpdated) {
            		ScheduleDetailData newDetail = new ScheduleDetailData();
            		newDetail.setScheduleDetailCd(detail.getScheduleDetailCd());
            		newDetail.setScheduleId(schedule.getSchedule().getScheduleId());
            		newDetail.setValue(detail.getValue());
            		newDetail = super.create(newDetail);
        			updatedDetails.add(newDetail);
            	}
            }
        }
        //update the schedule view with the updated details
        schedule.setScheduleDetails(updatedDetails);
    	//If there are any remaining existing details, delete them.  Such a scenario indicates that the schedule
        //was updated and certain details were removed (i.e. also dates, physical inventory dates, etc).
        super.remove(existingDetails);
    }

    @Override
    public boolean deleteCorporateSchedule(Long scheduleId) {
    	boolean returnValue = false;
    	ScheduleView schedule = findSchedule(scheduleId);
    	super.remove(schedule.getScheduleDetails());
    	super.remove(schedule.getSchedule());
    	returnValue = true;
        return returnValue;
    }

    private CorporateScheduleView getCorporateSchedule(ScheduleData schedule, List<ScheduleDetailData> details) {
        return getCorporateSchedules(Utility.toList(new ScheduleJoinView(schedule, details))).get(0);
    }

    private Date getNextDeliveryDate(ScheduleJoinView schedule, Date date) {
        try {
            ScheduleProc scheduleProc = new ScheduleProc(schedule, null);
            scheduleProc.initSchedule();
            GregorianCalendar nextDeliveryDate = scheduleProc.getOrderDeliveryDate(date, date);
            if (nextDeliveryDate != null) {
                return nextDeliveryDate.getTime();
            }
        } catch (Exception e) {
            //error
        	e.printStackTrace();
        }
        return null;
    }
    //=========//
    
    public ScheduleData findCorporateSchedule(Long storeId, Long corporateScheduleId) {

        logger.info(" findCorporateSchedule()=> BEGIN");
        logger.info(" findCorporateSchedule()=> storeId: " + storeId);
        logger.info(" findCorporateSchedule()=> corporateScheduleId: " + corporateScheduleId);

        Query q = em.createQuery("select object(m) from ScheduleData m  " +
                "where m.scheduleId=:corporateScheduleId" +
                " and m.busEntityId=(:storeId) " );
       
        q.setParameter("corporateScheduleId", corporateScheduleId);
        q.setParameter("storeId", storeId);
 
        List<ScheduleData> schedules = (List<ScheduleData>) q.getResultList();

        logger.info("findCorporateSchedule()=> END,  " + (schedules.isEmpty() ? "message not found" : "OK!"));

        return schedules.isEmpty() ? null : schedules.get(0);
    }

    public List<CorporateScheduleView> findCorporateSchedulesByCriteria(CorporateScheduleDataCriteria criteria) {

        logger.info("findCorporateSchedulesByCriteria()=> BEGIN");
        logger.info("findCorporateSchedulesByCriteria()=> criteria: " + criteria);

        
        
        StringBuilder baseSql = new StringBuilder("Select sched " +
        		" from ScheduleData sched " +
                " where sched.busEntityId = (:storeId)" +
                " and sched.scheduleTypeCd = (:schedTypeCd) " );
        
        if (Utility.isSet(criteria.getCorporateScheduleId())) {
        	baseSql.append(" and sched.scheduleId = ").append(Parse.parseLong(criteria.getCorporateScheduleId()));
        }
        
        
        if (Utility.isSet(criteria.getName())) {

            if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(sched.shortDesc) like '")
                         .append(QueryHelp.startWith(criteria.getName().toUpperCase()))
                         .append("'");

             } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(sched.shortDesc) like '")
                         .append(QueryHelp.contains(criteria.getName().toUpperCase()))
                         .append("'");


             } else if (Constants.FILTER_TYPE.EXACT_MATCH.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(sched.shortDesc) = '")
                         .append(QueryHelp.escapeQuotes(criteria.getName().toUpperCase()))
                         .append("'");


             }   
         }
 
        StringBuilder subSql = new StringBuilder("Select " +
        		" detail.scheduleId from  ScheduleDetailData detail" +
                " where sched.scheduleId = detail.scheduleId" +
                " and detail.scheduleDetailCd = 'ALSO_DATE' " );        
        
        if (criteria.getCorporateScheduleDateFrom() != null) {
            subSql.append("and to_date(detail.value,'mm/dd/yyyy') >= (:dateFrom)");
        }
        
        if (criteria.getCorporateScheduleDateTo() != null) {
            subSql.append(" and to_date(detail.value,'mm/dd/yyyy') <= (:dateTo)");
        }
        if (criteria.getCorporateScheduleDateTo() != null ||
        	criteria.getCorporateScheduleDateFrom() != null	) {
        	baseSql.append(" and exists ( "+ subSql.toString() + ")");
        }
        
 
        Query q = em.createQuery(baseSql.toString());

        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("schedTypeCd", RefCodeNames.SCHEDULE_TYPE_CD.CORPORATE);

        if (criteria.getCorporateScheduleDateFrom() != null) {
            q.setParameter("dateFrom", criteria.getCorporateScheduleDateFrom());
        }

        if (criteria.getCorporateScheduleDateTo()!= null) {
            q.setParameter("dateTo", criteria.getCorporateScheduleDateTo());
        }
        

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

//        List<CorporateScheduleView> r = (List<CorporateScheduleView>) q.getResultList();
        
        List<ScheduleData> s = (List<ScheduleData>) q.getResultList();
        logger.info("findCorporateSchedulesByCriteria()=> END, fetched " + s.size() + " rows");
         
        List<CorporateScheduleView> r = getCorporateSchedules(s, true); 
        logger.info("findCorporateSchedulesByCriteria()=> END, CorporateSchedules " + r.size() + " rows");

        return r;
    }
    
    public List<ScheduleDetailData> findCorporateScheduleDetailsByCriteria(CorporateScheduleDataCriteria criteria) {

        logger.info("findCorporateScheduleDetailsByCriteria()=> BEGIN");
        logger.info("findCorporateScheduleDetailsByCriteria()=> criteria: " + criteria);

        
        
        StringBuilder baseSql = new StringBuilder("Select detail " +
        		" from ScheduleData sched, ScheduleDetailData detail" +
                " where sched.busEntityId = (:storeId)" +
                " and sched.scheduleTypeCd = (:schedTypeCd) " +
                " and sched.scheduleId = detail.scheduleId ");
        
        if (Utility.isSet(criteria.getCorporateScheduleDetailCd())) {
        	baseSql.append(" and detail.scheduleDetailCd = '").append(criteria.getCorporateScheduleDetailCd()).append("'");;
        }
       
        if (Utility.isSet(criteria.getCorporateScheduleId())) {
        	baseSql.append(" and sched.scheduleId = ").append(Parse.parseLong(criteria.getCorporateScheduleId()));
        }
        
        
        if (Utility.isSet(criteria.getName())) {

            if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(sched.shortDesc) like '")
                         .append(QueryHelp.startWith(criteria.getName().toUpperCase()))
                         .append("'");

             } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(sched.shortDesc) like '")
                         .append(QueryHelp.contains(criteria.getName().toUpperCase()))
                         .append("'");


             } else if (Constants.FILTER_TYPE.EXACT_MATCH.equals(criteria.getNameFilterType())) {

                 baseSql.append(" and UPPER(sched.shortDesc) = '")
                         .append(QueryHelp.escapeQuotes(criteria.getName().toUpperCase()))
                         .append("'");


             }   
         }
  
        Query q = em.createQuery(baseSql.toString());

        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("schedTypeCd", RefCodeNames.SCHEDULE_TYPE_CD.CORPORATE);
       

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }
        
        List<ScheduleDetailData> s = (List<ScheduleDetailData>) q.getResultList();
        logger.info("findCorporateScheduleDetailsByCriteria()=> END, fetched " + s.size() + " rows");
         

        return s;
    }

    
    private List<CorporateScheduleView> getCorporateSchedules (List<ScheduleData> scheduleDV, boolean cutLongDates) {
		List<CorporateScheduleView> scheduleList = new ArrayList <CorporateScheduleView>();
        if(scheduleDV.isEmpty()){
    		return scheduleList;
    	}
        for (ScheduleData sD : scheduleDV) {
            CorporateScheduleView dsVv = new CorporateScheduleView();
            Long scheduleId = sD.getScheduleId();
            dsVv.setScheduleId(scheduleId);
            Long distId = sD.getBusEntityId();
           // dsVv.setBusEntityId(distId);
            dsVv.setScheduleName(sD.getShortDesc());
            dsVv.setScheduleStatus(sD.getScheduleStatusCd());
            dsVv.setNextDeliveryDate(getNextDeliveryDate(scheduleId));
            
            //Details
            ArrayList detailTypes = new ArrayList();
            detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.ALSO_DATE);
            detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_TIME);

            String deliveryDates = "";
            int deliveryDatesCount = -1;
           
            StringBuilder baseSql = new StringBuilder("Select detail " +
            		" from ScheduleDetailData detail " +
                    " where detail.scheduleId = (:scheduleId)" +
                    " and detail.scheduleDetailCd in (:detailTypes) " +
                    " order by detail.scheduleDetailId " );

            Query q = em.createQuery(baseSql.toString());

            q.setParameter("scheduleId", scheduleId);
            q.setParameter("detailTypes", detailTypes);
            
            List<ScheduleDetailData> scheduleDetailDV = q.getResultList();
           
            for (ScheduleDetailData sdD : scheduleDetailDV) {            	
                String detailCd = sdD.getScheduleDetailCd();
                if (RefCodeNames.SCHEDULE_DETAIL_CD.ALSO_DATE.equals(detailCd)) {
                    deliveryDatesCount++;
                    if (deliveryDatesCount == 12 && cutLongDates) {
                        deliveryDates += " ... ";
                        continue;
                    }
                    if (deliveryDatesCount > 0) {
                        deliveryDates += ", ";
                    }
                    deliveryDates += sdD.getValue();
                }else if (RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_TIME.equals(detailCd)) {
                	dsVv.setCutoffTime(sdD.getValue()); ;
                }
            }
            
            //dsVv.setScheduleInfo("On dates: " + deliveryDates);
            scheduleList.add(dsVv);
        }            
        return scheduleList;
    }
    public Date getNextDeliveryDate(Long pScheduleId)   {
    	try {
		   Date currDate = new Date();
		   ScheduleJoinView   sjVw = getScheduleById(pScheduleId);
		   return getNextDeliveryDate(sjVw , currDate);
    	} catch (Exception e) {
 		   e.printStackTrace();
     	}
    	return null;
//    	try {
//		   Date currDate = new Date();
//		   ScheduleProc scheduleProc = createScheduleProc (pScheduleId);
//		   GregorianCalendar deliveryCal = scheduleProc.getOrderDeliveryDate(currDate,currDate);
//		   if (deliveryCal != null) {
//		   	return deliveryCal.getTime();
//		   }	
//    	} catch (Exception e) {
//		   e.printStackTrace();
//    	}
//    	return null;
	}
    
    private ScheduleProc createScheduleProc ( Long pScheduleId) throws Exception   {
    	//get corporate schedule
    	ScheduleJoinView   sjVw = getScheduleById(pScheduleId);

    	ScheduleProc scheduleProc = new ScheduleProc(sjVw,null);
    	scheduleProc.initSchedule();
    	return scheduleProc;
    }
    
	public ScheduleJoinView getScheduleById(Long pScheduleId)  throws RemoteException {
		ScheduleJoinView scheduleJoin = new ScheduleJoinView();
		ArrayList detailTypes = new ArrayList();
		detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.ALSO_DATE);
		detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_DAY);
		detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_TIME);
		detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.INV_CART_ACCESS_INTERVAL);
		detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_START_DATE);
		detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_END_DATE);
		detailTypes.add(RefCodeNames.SCHEDULE_DETAIL_CD.PHYSICAL_INV_FINAL_DATE);
	
	   ScheduleData scheduleD = em.find(ScheduleData.class, pScheduleId);
	   
	
   		StringBuilder query= new StringBuilder(
			"Select detail from ScheduleDetailData detail" +
            " where detail.scheduleId = (:scheduleId)" +
            "    and detail.scheduleDetailCd in (:scheduleDetailCds)");
        Query q = em.createQuery(query.toString());

        q.setParameter("scheduleId", pScheduleId);
        if (Utility.isSet(detailTypes)){
        	q.setParameter("scheduleDetailCds", detailTypes);
        }
        List<ScheduleDetailData> scheduleDetailDV = (List<ScheduleDetailData>) q.getResultList();
	
 	   scheduleJoin.setSchedule((ScheduleData) scheduleD);
	   scheduleJoin.setScheduleDetail(scheduleDetailDV);
	
	   return scheduleJoin;
	}

	@Override
	public void configureAllScheduleAccounts(Long scheduleId, Long storeId,
			Boolean associateAllAccounts, Boolean associateAllSites) {
		AccountDAO accountDao = new AccountDAOImpl(em);
        
        if (scheduleId != null && storeId != null) {
            if (associateAllAccounts) {
                StoreAccountCriteria criteria = new StoreAccountCriteria();
                criteria.setStoreId(storeId);
                criteria.setActiveOnly(true);

                List<BusEntityData> storeAccounts = accountDao.findAccounts(criteria);
                if (Utility.isSet(storeAccounts)) {
                    configureScheduleAccounts(scheduleId, storeId, storeAccounts, null, false);
                }
            }
            if (associateAllSites) {
            	List<Long> accountsSites = getSiteIdsForScheduledAccounts(scheduleId, storeId);
                if (Utility.isSet(accountsSites)) {
                	addAssociation(scheduleId, false, accountsSites);
                }
            }
        }
		
	}

	@Override
	public void configureScheduleAccounts(Long scheduleId, Long storeId,
			List<BusEntityData> selected, List<BusEntityData> deselected,
			Boolean removeSiteAssocToo) {
		if (scheduleId != null && storeId != null) {
			// add newly selected items only
            if (Utility.isSet(selected)) {
                addAssociation(scheduleId, true, Utility.toIds(selected));
            }
            
            // remove newly deselected items only
            if (Utility.isSet(deselected)) {
                removeAssociation(scheduleId, true, Utility.toIds(deselected), removeSiteAssocToo);                
            }            
		}
	}

	@Override
	public void configureScheduleSites(Long scheduleId, Long storeId,
			List<SiteListView> selected, List<SiteListView> deselected) {
		if (scheduleId != null && storeId != null) {
			// add newly selected items only
            if (Utility.isSet(selected)) {
                addAssociation(scheduleId, false, Utility.toIds(selected));
            }
            
            // remove newly deselected items only
            if (Utility.isSet(deselected)) {
                removeAssociation(scheduleId, false, Utility.toIds(deselected), null);
            }            
		}
		
	}

	@Override
	public List<BusEntityData> findScheduleAccountsByCriteria(
			StoreAccountCriteria criteria) {
		if (criteria.getScheduleId() == null || criteria.getStoreId() == null) {
            return new ArrayList<BusEntityData>();
        }
        
        StringBuilder baseQuery = new StringBuilder(
                "Select object(account) from BusEntityData account, BusEntityAssocData accountToStore, ScheduleDetailData scheduleToAccount" +
                " where scheduleToAccount.scheduleId = (:scheduleId)" +
                " and scheduleToAccount.scheduleDetailCd =(:scheduleDetailCd)" +
                " and account.busEntityId = accountToStore.busEntity1Id" +
                " and accountToStore.busEntityAssocCd = (:accountToStoreAssocCd)" +
                " and accountToStore.busEntity2Id = (:storeId)" +
                " and scheduleToAccount.value = to_char(accountToStore.busEntity1Id)" +
                (Utility.isSet(criteria.getAccountId()) ? " and account.busEntityId = :accountId" : "") +
                (Utility.isSet(criteria.getName()) ? " and UPPER(account.shortDesc) like :shortDesc" : "")
        );
        
        
        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and account.busEntityStatusCd <> ")
                    .append(QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE));
        }
        
        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("scheduleId", criteria.getScheduleId());
        q.setParameter("scheduleDetailCd", RefCodeNames.SCHEDULE_DETAIL_CD.ACCOUNT_ID);
        q.setParameter("accountToStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        if (Utility.isSet(criteria.getAccountId())) {
        	q.setParameter("accountId", criteria.getAccountId());
        }
        if (Utility.isSet(criteria.getName())) {
        	q.setParameter("shortDesc", QueryHelp.toFilterValue(criteria.getName().toUpperCase(), criteria.getFilterType()));
        }        

        if (criteria.getLimit() != null) { q.setMaxResults(criteria.getLimit()); }

        List<BusEntityData> resultList = q.getResultList();
        
        return resultList;
	}

	@Override
	public List<SiteListView> findScheduleSitesByCriteria(
			SiteListViewCriteria criteria) {
		List<SiteListView> sites = new ArrayList<SiteListView>();

        logger.info("findScheduleSitesByCriteria() => BEGIN, criteria : " + criteria);

        if (criteria.getScheduleId() != null) {
        	StringBuilder queryString = new StringBuilder();
            queryString.append("Select distinct new com.espendwise.manta.model.view.SiteListView (" +
                    " site.busEntityId," +
                    " site.shortDesc," +
                    " account.busEntityId," +
                    " account.shortDesc," +
                    " site.busEntityStatusCd," +
                    " address.address1," +
                    " address.address2," +
                    " address.address2," +
                    " address.address4," +
                    " address.countryCd," +
                    " address.countyCd," +
                    " address.city," +
                    " address.stateProvinceCd," +
                    " address.postalCode )" +
                    " from BusEntityFullEntity site, ScheduleDetailData schedAccountAssoc" +(criteria.isConfiguredOnly() ? ", ScheduleDetailData schedSiteAssoc" : "") +
                    " left outer join site.addresses address with address.addressTypeCd in (:typeAddressCd)" +
                    " left outer join site.properties referenceNumber with referenceNumber.shortDesc = (:refNumberCode) and referenceNumber.value is not null");
 
            queryString.append(" inner join site.busEntityAssocsForBusEntity1Id siteAccount" +
                               " inner join siteAccount.busEntity2Id account" +
                               " inner join account.busEntityAssocsForBusEntity1Id accountStore");

            
            queryString.append(" where site.busEntityTypeCd = (:typeCdOfSite)" +
                               " and accountStore.busEntity2Id.busEntityId = (:storeId)" +
                               " and accountStore.busEntityAssocCd = (:accountStoreAssocCd)" +
                               " and siteAccount.busEntityAssocCd = (:siteAccountAssocCd)" +
                               " and account.busEntityTypeCd = (:typeCdOfAccount) " +
                               " and schedAccountAssoc.scheduleId = (:scheduleId)" +
       		                   " and schedAccountAssoc.scheduleDetailCd = (:ACCOUNT_ID) " +
       		                   " and schedAccountAssoc.value = to_char(account.busEntityId)");
            if (criteria.isConfiguredOnly()) {
            	queryString.append(" and schedSiteAssoc.scheduleId = (:scheduleId)" +
       		                   	   " and schedSiteAssoc.scheduleDetailCd = (:SITE_ID) " +
       		                   	   " and schedSiteAssoc.value = to_char(site.busEntityId)");
            }
                    
            queryString.append((Utility.isSet(criteria.getAccountIds()) ? " and account.busEntityId in :accountIds)" : "") +
            		(Utility.isSet(criteria.getSiteId()) ? " and site.busEntityId = (:siteId)" : "") +
                    (Utility.isSet(criteria.getSiteName()) ? " and Upper(site.shortDesc)  like :siteName" : "") +
                    (Utility.isTrue(criteria.isActiveOnly()) ? " and site.busEntityStatusCd <> (:inactiveStatusCd)" : "") +
                    (Utility.isSet(criteria.getRefNumber()) ? " and Upper(referenceNumber.value) like :referenceNumber" : "") +
                    (Utility.isSet(criteria.getCity()) ? " and Upper(address.city) like :city" : "") +
                    (Utility.isSet(criteria.getState()) ? " and Upper(address.stateProvinceCd) like :state" : "") +
                    (Utility.isSet(criteria.getPostalCode()) ? " and Upper(address.postalCode) like :postalCode" : ""));
            
            String query = queryString.toString();
            Query q = em.createQuery(query);
            
            q.setParameter("siteAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
            q.setParameter("typeCdOfAccount", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
            q.setParameter("typeCdOfSite", RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
            q.setParameter("refNumberCode", RefCodeNames.PROPERTY_TYPE_CD.SITE_REFERENCE_NUMBER);
            q.setParameter("typeAddressCd", Utility.toList(RefCodeNames.ADDRESS_TYPE_CD.CUSTOMER_SHIPPING, RefCodeNames.ADDRESS_TYPE_CD.SHIPPING));
            q.setParameter("storeId", criteria.getStoreId());
            q.setParameter("scheduleId", criteria.getScheduleId());
            q.setParameter("ACCOUNT_ID", RefCodeNames.SCHEDULE_DETAIL_CD.ACCOUNT_ID);

            if (criteria.isConfiguredOnly()) {
                q.setParameter("SITE_ID", RefCodeNames.SCHEDULE_DETAIL_CD.SITE_ID);
            }

            if (Utility.isSet(criteria.getAccountIds())) {
                q.setParameter("accountIds", criteria.getAccountIds());
            }
            if (Utility.isSet(criteria.getSiteId())) {
                q.setParameter("siteId", criteria.getSiteId());
            }

            if (Utility.isSet(criteria.getSiteName())) {
                q.setParameter("siteName",
                        QueryHelp.toFilterValue(
                                criteria.getSiteName().toUpperCase(),
                                criteria.getSiteNameFilterType()
                        )
                );
            }

            if (Utility.isSet(criteria.getRefNumber())) {
                q.setParameter("referenceNumber",
                        QueryHelp.toFilterValue(
                                criteria.getRefNumber().toUpperCase(),
                                criteria.getRefNumberFilterType()
                        )
                );
            }

            if (Utility.isSet(criteria.getCity())) {
                q.setParameter("city",
                        QueryHelp.toFilterValue(
                                criteria.getCity().toUpperCase(),
                                criteria.getCityFilterType()
                        )
                );
            }

            if (Utility.isSet(criteria.getState())) {
                q.setParameter("state",
                        QueryHelp.toFilterValue(
                                criteria.getState().toUpperCase(),
                                criteria.getStateFilterType()
                        )
                );
            }
            
            if (Utility.isSet(criteria.getPostalCode())) {
                q.setParameter("postalCode",
                        QueryHelp.toFilterValue(
                                criteria.getPostalCode().toUpperCase(),
                                criteria.getPostalCodeFilterType()
                        )
                );
            }

            if (Utility.isTrue(criteria.isActiveOnly())) {
                q.setParameter("inactiveStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE);
            }

            if (Utility.isSet(criteria.getLimit())) {
                q.setMaxResults(criteria.getLimit());
            }

            sites = q.getResultList();

        }

        logger.info("findScheduleSitesByCriteria() => END, fetched : " + sites.size() + " rows");

        return sites;

	}
	
	private void addAssociation(Long scheduleId, boolean isAccount, List<Long> addList){
		if (addList.isEmpty())
			return;
		String scheduleDetailCd = isAccount? RefCodeNames.SCHEDULE_DETAIL_CD.ACCOUNT_ID : RefCodeNames.SCHEDULE_DETAIL_CD.SITE_ID;
		for (Long id : addList){
			Query q = em.createQuery("select count(*) from ScheduleDetailData assoc" +
                    " where assoc.scheduleId = (:scheduleId)" +
                    " and assoc.scheduleDetailCd = (:scheduleDetailCd)" +
                    " and assoc.value = to_char(:id)");

            q.setParameter("scheduleId", scheduleId);
            q.setParameter("scheduleDetailCd", scheduleDetailCd);
            q.setParameter("id", id);
            Long count = (Long)(q.getResultList().get(0));
            
            if(count.intValue()==0 ) {
            	ScheduleDetailData schedDetail = new ScheduleDetailData();
            	schedDetail.setScheduleId(scheduleId);
            	schedDetail.setScheduleDetailCd(scheduleDetailCd);
            	schedDetail.setValue(id.toString());
            	create(schedDetail);
            }
		}
	}
	
	private void removeAssociation(Long scheduleId, boolean isAccount, List<Long> removeList, Boolean removeSiteAssocToo){
		if (removeList.isEmpty())
			return;
		String scheduleDetailCd = isAccount? RefCodeNames.SCHEDULE_DETAIL_CD.ACCOUNT_ID : RefCodeNames.SCHEDULE_DETAIL_CD.SITE_ID;
		List<String> removeStringList = new ArrayList<String>();
		for (Long id : removeList){
			removeStringList.add(id.toString());            
			
			if (isAccount && removeSiteAssocToo){
				// find associated site with scheduleId
				Long accountId = id;
				Query q = em.createQuery("select schedAssoc.value from ScheduleDetailData schedAssoc" +
		                " where schedAssoc.scheduleId = (:scheduleId)" +
		                " and schedAssoc.scheduleDetailCd = (:scheduleDetailCd))"
		        );
				
				q.setParameter("scheduleId", scheduleId);
	            q.setParameter("scheduleDetailCd", RefCodeNames.SCHEDULE_DETAIL_CD.SITE_ID);		
	            List<String> siteIds = q.getResultList();
	            
	            q = em.createQuery("Select to_char(assoc.busEntity1Id) from BusEntityAssocData assoc" +
		                " where assoc.busEntityAssocCd = (:busEntityAssocCd)" +
		                " and assoc.busEntity2Id = (:accountId))" 
		        );
				q.setParameter("busEntityAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
				q.setParameter("accountId", accountId);
				
				List<String> siteIds2 = q.getResultList();
				List<String> intersect = new ArrayList(siteIds);
				intersect.retainAll(siteIds2);
				for (int i = 0, j = Math.min(1000, intersect.size()); i < intersect.size(); i+=1000, j = Math.min(j+1000, intersect.size())){
					removeAssociation(scheduleId, RefCodeNames.SCHEDULE_DETAIL_CD.SITE_ID, intersect.subList(i, j));	
				}
			}
		}
		removeAssociation(scheduleId, scheduleDetailCd, removeStringList);	
	}
	
	private void removeAssociation(Long scheduleId, String scheduleDetailCd, List<String> removeList){
		if (removeList.isEmpty())
			return;
		Query q = em.createQuery("delete from ScheduleDetailData assoc" +
                " where assoc.scheduleId = (:scheduleId)" +
                " and assoc.scheduleDetailCd = (:scheduleDetailCd)" +
                " and assoc.value in :removeList");
		q.setParameter("scheduleId", scheduleId);
        q.setParameter("scheduleDetailCd", scheduleDetailCd);
        q.setParameter("removeList", removeList);
        int updated = q.executeUpdate();
	}
	
	private List<Long> getSiteIdsForScheduledAccounts(Long scheduleId, Long storeId) {		
		logger.info("getSiteIdsForScheduledAccounts() => BEGIN, scheduleId : " + scheduleId + ", storeId : " + storeId);

		StringBuilder queryString = new StringBuilder();
		queryString.append("Select distinct site.busEntityId" +
				" from BusEntityFullEntity site, ScheduleDetailData schedAccountAssoc" +
				" inner join site.busEntityAssocsForBusEntity1Id siteAccount" +
				" inner join siteAccount.busEntity2Id account" +
				" inner join account.busEntityAssocsForBusEntity1Id accountStore" +
				" where site.busEntityTypeCd = (:typeCdOfSite) " +
				" and site.busEntityStatusCd <> (:inactiveStatusCd)" +
				" and accountStore.busEntity2Id.busEntityId = (:storeId)" +
				" and accountStore.busEntityAssocCd = (:accountStoreAssocCd)" +
				" and siteAccount.busEntityAssocCd = (:siteAccountAssocCd)" +
				" and account.busEntityTypeCd = (:typeCdOfAccount) " +
				" and schedAccountAssoc.scheduleId = (:scheduleId)" +
				" and schedAccountAssoc.scheduleDetailCd = (:ACCOUNT_ID) " +
				" and schedAccountAssoc.value = to_char(account.busEntityId)");


		String query = queryString.toString();
		Query q = em.createQuery(query);

		q.setParameter("siteAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
		q.setParameter("accountStoreAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
		q.setParameter("typeCdOfAccount", RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
		q.setParameter("typeCdOfSite", RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
		q.setParameter("storeId", storeId);
		q.setParameter("scheduleId", scheduleId);
		q.setParameter("ACCOUNT_ID", RefCodeNames.SCHEDULE_DETAIL_CD.ACCOUNT_ID);
		q.setParameter("inactiveStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE);


		List<Long> sites = q.getResultList();

		logger.info("getSiteIdsForScheduledAccounts() => END, fetched : " + sites.size() + " rows");

		return sites;

	}
    
    
}
