package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.FiscalCalenderData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.espendwise.manta.model.data.FiscalCalenderDetailData;
import com.espendwise.manta.model.view.FiscalCalendarIdentView;
import com.espendwise.manta.model.view.FiscalCalendarListView;
import com.espendwise.manta.model.view.FiscalCalendarPhysicalView;
import com.espendwise.manta.model.view.FiscalReportIntersectResView;
import com.espendwise.manta.util.*;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class FiscalCalendarDAOImpl extends DAOImpl implements FiscalCalendarDAO {

    private static final Logger logger = Logger.getLogger(FiscalCalendarDAOImpl.class);

    public FiscalCalendarDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public FiscalCalendarPhysicalView selectCalenderForYear(List<FiscalCalendarListView> calendars, Integer fiscalYear, Date forDate) {

        SortedMap<Date, FiscalCalendarListView> candidates = new TreeMap<Date, FiscalCalendarListView>();
        for (FiscalCalendarListView calendar : calendars) {
            if (Utility.intNN(calendar.getFiscalYear()) == 0 || Utility.intNN(calendar.getFiscalYear()) <= fiscalYear) {
                candidates.put(calendar.getEffDate(), calendar);
            }
        }

        Long calendarId = null;

        List<Date> indexes = new ArrayList<Date>(candidates.keySet());

        for (int i = 0; i < indexes.size(); i++) {

            FiscalCalendarListView calendar = candidates.get(indexes.get(i));
            FiscalCalendarListView nextCalendar = i + 1 < indexes.size() ? candidates.get(indexes.get(i + 1)) : null;

            if (Utility.intNN(calendar.getFiscalYear()) == 0) {

                int yearStart = FiscalCalendarUtility.getYearForDate(calendar.getEffDate());

                Integer yearEnd = nextCalendar == null
                        ? calendar.getExpDate() != null ? FiscalCalendarUtility.getYearForDate(calendar.getExpDate()) : null
                        : Integer.valueOf(FiscalCalendarUtility.getYearForDate(nextCalendar.getEffDate()));


                if (fiscalYear >= yearStart && (yearEnd == null || fiscalYear < yearEnd || yearEnd == yearStart)) {
                    calendarId = calendar.getFiscalCalendarId();
                    break;
                }

            }  else {

                if(calendar.getFiscalYear().intValue() == fiscalYear){
                    calendarId = calendar.getFiscalCalendarId();
                    break;
                }

            }


        }


        if (calendarId != null) {
            return findFiscalCalenderForDate(
                    calendarId,
                    forDate
            );
        }

        return null;


    }

    public FiscalCalendarPhysicalView findFiscalCalenderForDate(Long fiscalCalendarId, Date forDate) {

        FiscalCalendarPhysicalView calendarPhysicalView = null;

        if (Utility.longNN(fiscalCalendarId) > 0) {

            Query q = em.createQuery("Select calendar from FiscalCalenderData calendar " +
                    " where calendar.fiscalCalenderId = (:fiscalCalendarId) "
            );

            q.setParameter("fiscalCalendarId", fiscalCalendarId);
            List calendars = q.getResultList();

            if (!calendars.isEmpty()) {

                FiscalCalenderData calendar = ((FiscalCalenderData) calendars.get(0));
                q = em.createQuery("Select periods from FiscalCalenderDetailData periods " +
                        " where periods.fiscalCalenderId = (:fiscalCalendarId) order by periods.period"
                );

                q.setParameter("fiscalCalendarId", calendar.getFiscalCalenderId());

                List<FiscalCalenderDetailData> periods = q.getResultList();

                calendarPhysicalView = new FiscalCalendarPhysicalView(calendar.getFiscalCalenderId(),
                        calendar.getBusEntityId(),
                        calendar.getFiscalYear(),
                        FiscalCalendarUtility.getPhysicalYear(calendar.getFiscalYear(), forDate),
                        calendar.getEffDate(),
                        calendar.getExpDate(),
                        periods,
                        isServiceScheduleCalendar(calendar.getBusEntityId())
                );

            }

        }

        return calendarPhysicalView;
    }


    public FiscalCalendarPhysicalView findFiscalCalenderForDate(Long busEntityId, Integer fiscalYear, Date forDate) {

        logger.info("findFiscalCalenderForYear()=> BEGIN, busEntityId: " + busEntityId + ", forDate: " + forDate);

        FiscalCalendarPhysicalView calendarPhysicalView = null;

        if (busEntityId != null) {

            Query q = em.createQuery("Select calendar from FiscalCalenderData calendar " +
                    " where calendar.busEntityId = (:busEntityId) " +
                    " and calendar.effDate <= (:forDate) " +
                    (fiscalYear != null ? " and (calendar.fiscalYear in (:fiscalYear) or calendar.fiscalYear is null)" : "") +
                    " order by calendar.effDate desc"
            );

            q.setParameter("busEntityId", busEntityId);
            q.setParameter("forDate", forDate);

            if (fiscalYear != null) {
                q.setParameter("fiscalYear", Utility.toList(fiscalYear, 0));
            }

            List calendars = q.getResultList();

            if (!calendars.isEmpty()) {

                FiscalCalenderData calendar = ((FiscalCalenderData) calendars.get(0));
                q = em.createQuery("Select periods from FiscalCalenderDetailData periods " +
                        " where periods.fiscalCalenderId = (:fiscalCalendarId) order by periods.period"
                );

                q.setParameter("fiscalCalendarId", calendar.getFiscalCalenderId());

                List<FiscalCalenderDetailData> periods = q.getResultList();

                calendarPhysicalView = new FiscalCalendarPhysicalView(calendar.getFiscalCalenderId(),
                        calendar.getBusEntityId(),
                        calendar.getFiscalYear(),
                        FiscalCalendarUtility.getPhysicalYear(calendar.getFiscalYear(), forDate),
                        calendar.getEffDate(),
                        calendar.getExpDate(),
                        periods,
                        isServiceScheduleCalendar(busEntityId)
                );

            }

        }

        logger.info("findFiscalCalenderForYear()=> END,  " + (calendarPhysicalView == null ? " calendar not found" : "OK!, calendarPhysicalView: " + calendarPhysicalView));

        return calendarPhysicalView;
    }


    public List<FiscalCalendarListView> findFiscalCalenders(Long busEntityId) {

        logger.info("findFiscalCalenders()=> BEGIN, busEntityId: " + busEntityId);

        boolean isServiceScheduleCalendar = isServiceScheduleCalendar(busEntityId);

        Query q = em.createQuery("" +
                "Select new com.espendwise.manta.model.view.FiscalCalendarListView(" +
                "c.fiscalCalenderId,c.busEntityId, c.fiscalYear, (count(d.period) - "+(isServiceScheduleCalendar?"1":"0")+"), c.effDate,c.expDate)" +
                " from FiscalCalenderFullEntity c left join c.fiscalCalenderDetails d" +
                "    where c.busEntityId = (:busEntityId)" +
                "       group by c.fiscalCalenderId, c.busEntityId, c.fiscalYear, c.effDate, c.expDate " +
                "       order by c.fiscalYear desc, c.fiscalCalenderId desc"
        );

        q.setParameter("busEntityId", busEntityId);

        List<FiscalCalendarListView> fiscalCalenders = q.getResultList();

        logger.info("findFiscalCalenders()=> END, fetched : " + fiscalCalenders.size() + " rows");

        return fiscalCalenders;

    }

    @Override
    public FiscalCalendarIdentView findCalendarIdent(Long busEntityId, Long fiscalCalendarId) {

        logger.info("findFiscalCalenders()=> BEGIN, busEntityId: " + busEntityId + ", fiscalCalendarId: " + fiscalCalendarId);

        FiscalCalendarIdentView calendarIdent = null;

        if (fiscalCalendarId != null && busEntityId != null) {


            boolean isServiceScheduleCalendar = isServiceScheduleCalendar(busEntityId);


            Query q = em.createQuery("Select calendar from FiscalCalenderData calendar " +
                    " where calendar.busEntityId = (:busEntityId) and calendar.fiscalCalenderId = (:fiscalCalendarId)"
            );

            q.setParameter("busEntityId", busEntityId);
            q.setParameter("fiscalCalendarId", fiscalCalendarId);

            List calendars = q.getResultList();

            if (!calendars.isEmpty()) {

                FiscalCalenderData calendar = ((FiscalCalenderData) calendars.get(0));

                q = em.createQuery("Select periods from FiscalCalenderDetailData periods " +
                        " where periods.fiscalCalenderId = (:fiscalCalendarId) order by periods.period"
                );
                q.setParameter("fiscalCalendarId", calendar.getFiscalCalenderId());

                List<FiscalCalenderDetailData> periods = q.getResultList();

                calendarIdent = new FiscalCalendarIdentView(calendar, periods, isServiceScheduleCalendar);
            }

        }

        logger.info("findFiscalCalenders()=> END,  " + (calendarIdent == null ? " calendar not found" : "OK!"));

        return calendarIdent;
    }

    public boolean isServiceScheduleCalendar(Long busEntityId) {
        PropertyDAO propertyDAO = new PropertyDAOImpl(em);
        return Utility.isTrue(
                PropertyUtil.toFirstValueNN(
                        PropertyUtil.findActiveP(
                                propertyDAO.findEntityProperties(
                                        busEntityId,
                                        Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.USE_FOR_SERVICE)
                                )
                        )
                )
        );
    }
    
	public FiscalReportIntersectResView getFiscalCalendarIntersectYear(Long busEntityId, Long fiscalCalenderId, Date effDate, Date end_date) {
        
        logger.info("getFiscalCalendarIntersectYear()=> BEGIN, fiscalCalenderId: " + fiscalCalenderId + " busEntityId = " + busEntityId);

        FiscalReportIntersectResView fisRepIntResView = null;
        Query q3 = null;
        
        if (fiscalCalenderId != null) {
             		// update record
             		q3 = em.createQuery("select new com.espendwise.manta.model.view.FiscalReportIntersectResView(CFC_MAIN.busEntityId, CFCD_MAIN.mmdd, CFC_MAIN.effDate, CFC_MAIN.fiscalYear)"+
   						 " from FiscalCalenderData cfc_main,"+
   						 " FiscalCalenderDetailData cfcd_main"+
						 " where CFC_MAIN.busEntityId = (:resultBusEntityId)"+
						 " and CFC_MAIN.fiscalCalenderId = CFCD_MAIN.fiscalCalenderId"+
						 " and CFCD_MAIN.fiscalCalenderId <> (:fiscalCalendarId2)"+
						 " and CFCD_MAIN.fiscalCalenderDetailId in "+
						 " (select max(cfcd.fiscalCalenderDetailId)"+
						 " from FiscalCalenderData cfc, FiscalCalenderDetailData cfcd"+
						 " where cfc.fiscalCalenderId = cfcd.fiscalCalenderId"+
						 " and cfc.busEntityId = (:resultBusEntityId)"+
						 " and cfcd.fiscalCalenderId <> (:fiscalCalendarId2)"+
						 " group by cfcd.fiscalCalenderId)");

             		q3.setParameter("resultBusEntityId", busEntityId);
             		q3.setParameter("fiscalCalendarId2", fiscalCalenderId);
        } else {
        	// new record
     		    q3 = em.createQuery("select new com.espendwise.manta.model.view.FiscalReportIntersectResView(CFC_MAIN.busEntityId, CFCD_MAIN.mmdd, CFC_MAIN.effDate, CFC_MAIN.fiscalYear)"+
					 " from FiscalCalenderData cfc_main,"+
					 " FiscalCalenderDetailData cfcd_main"+
					 " where CFC_MAIN.busEntityId = (:resultBusEntityId)"+
					 " and CFC_MAIN.fiscalCalenderId = CFCD_MAIN.fiscalCalenderId"+
					 " and CFCD_MAIN.fiscalCalenderDetailId in "+
					 " (select max(cfcd.fiscalCalenderDetailId)"+
					 " from FiscalCalenderData cfc, FiscalCalenderDetailData cfcd"+
					 " where cfc.fiscalCalenderId = cfcd.fiscalCalenderId"+
					 " and cfc.busEntityId = (:resultBusEntityId)"+
     				" group by cfcd.fiscalCalenderId)");   
     		    
     		    q3.setParameter("resultBusEntityId", busEntityId);
     		
        }     		
             		
             		List resIntersectList = q3.getResultList();             		
             		
             		Date MainEffDate = null;
             		Date MainEndDate = null;
             		busEntityId = null;
             		String mmdd = null; 
             		             		
             		if (!resIntersectList.isEmpty()) {
                 		for (Iterator i = resIntersectList.iterator(); i.hasNext();) {
                 			fisRepIntResView = (FiscalReportIntersectResView)i.next();
                 			
                 			MainEffDate = fisRepIntResView.getEffDate();
                 			busEntityId = fisRepIntResView.getBusEntityId();
                     		mmdd = fisRepIntResView.getMmdd();
                     		String MainEffDateYear = MainEffDate.toString().substring(0, 4);
                     		mmdd = mmdd + "/"+MainEffDateYear;
                     		
                     	    try 
                     	    {  
                     	      DateFormat formatter; 
                     	      formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                     	      MainEndDate = (Date)formatter.parse(mmdd);  
                     	    } 
                     	    catch (Exception e)
                     	    {
                     	    	e.printStackTrace();
                     	    }    
                     	    
                     		//logger.info("getFiscalCalendarIntersectYear() effDate = "+effDate.toString());
                     		//logger.info("getFiscalCalendarIntersectYear() end_date = "+end_date.toString());
                     		//logger.info("getFiscalCalendarIntersectYear() MainEffDate = "+MainEffDate.toString());
                     		//logger.info("getFiscalCalendarIntersectYear() MainEndDate = "+MainEndDate.toString()); 
                     		//logger.info("effDate.compareTo(MainEffDate)="+effDate.compareTo(MainEffDate));
                     		//logger.info("effDate.compareTo(MainEndDate)="+effDate.compareTo(MainEndDate));
                     		//logger.info("end_date.compareTo(MainEffDate)="+end_date.compareTo(MainEffDate));
                     		//logger.info("end_date.compareTo(MainEndDate)="+end_date.compareTo(MainEndDate));  
                     		if (((effDate.compareTo(MainEffDate) == 1)||(effDate.compareTo(MainEffDate) == 0))
                     				&&((effDate.compareTo(MainEndDate) == -1)||(effDate.compareTo(MainEndDate) == 0))) {
                     			//logger.info("ERROR1 Intersect!!!!");
                     			return fisRepIntResView;
                     		}
                     		if (((end_date.compareTo(MainEffDate) == 1)||(end_date.compareTo(MainEffDate) == 0))
                     				&&((end_date.compareTo(MainEndDate) == -1)||(end_date.compareTo(MainEndDate) == 0))) {
                     			//logger.info("ERROR2 Intersect!!!!");
                     			return fisRepIntResView;
                     		}                     		
                     		
                     		fisRepIntResView = null;
                			
                 		}
             		}
             		             	
        return fisRepIntResView;
    }    
    
    public FiscalCalenderData findCurrentFiscalCalendar(Long accountId) {
        
        logger.info("findCurrentFiscalCalendar()=> BEGIN, accountId: " + accountId);

        FiscalCalenderData fiscalCalendar = null;
        Date currentDate = new Date();
        if (accountId != null) {
            Query q = em.createQuery("Select calendar from FiscalCalenderData calendar " +
                                    " where calendar.busEntityId = (:busEntityId) " +
                                    " and calendar.effDate <= (:forDate)" +
                                    " order by calendar.effDate desc"
            );

            q.setParameter("busEntityId", accountId);
            q.setParameter("forDate", currentDate);

            List calendars = q.getResultList();

            if (!calendars.isEmpty()) {
                fiscalCalendar = (FiscalCalenderData)calendars.get(0);
                // MANTA-417
                //int fiscalYear = fiscalCalendar.getFiscalYear();
                //if (fiscalYear <= 0) {
                //    GregorianCalendar cal = new GregorianCalendar();
                //    cal.setTime(currentDate);
                //    fiscalCalendar.setFiscalYear(cal.get(Calendar.YEAR));
                //}
            }
        }
        return fiscalCalendar;
    }

    public FiscalCalendarIdentView saveFiscalCalendar(FiscalCalendarIdentView calendar) {

        if (Utility.isNew(calendar)) {
            return createFiscalCalendar(calendar);
        } else {
            return updateFiscalCalendar(calendar);
        }

    }
    
    public FiscalCalendarIdentView saveFiscalCalendarClone(FiscalCalendarIdentView calendar) {

           return createFiscalCalendarClone(calendar);

    }    
  

    @Override
    public Long fiscalCalendarsCount(Long busEntityId) {

        if (!Utility.isSet(busEntityId)) {
            return (long) 0;
        }

        Query q = em.createQuery("Select count(calendar.fiscalCalenderId) from FiscalCalenderData calendar " +
                " where calendar.busEntityId = (:busEntityId)"
        ).setParameter("busEntityId", busEntityId);

        return (Long) q.getResultList().get(0);

    }

    private FiscalCalendarIdentView updateFiscalCalendar(FiscalCalendarIdentView calendar) {   	

        FiscalCalenderData calendarData = calendar.getFiscalCalendarData();
        List<FiscalCalenderDetailData> periods = calendar.getPeriods();

        calendarData = update(calendarData);

        for (int i = 0; i < periods.size(); i++) {
            FiscalCalenderDetailData period = periods.get(i);
            if (period.getPeriod() == periods.size()) {
                if (Utility.isSet(period.getMmdd()) && calendar.isServiceScheduleCalendar()) { //we must to set the tail period only if the Mmdd was set (for stjohn support)
                    period.setMmdd(FiscalCalendarUtility.getTailPeriodByDate(calendarData.getExpDate()));
                }
            }
            periods.set(i, update(period));
        }

        return new FiscalCalendarIdentView(calendarData, periods, calendar.isServiceScheduleCalendar()); 	
    	
    }

    private FiscalCalendarIdentView createFiscalCalendarClone(FiscalCalendarIdentView calendar) {
    	
        FiscalCalenderData calendarData = calendar.getFiscalCalendarData();
        List<FiscalCalenderDetailData> periods = calendar.getPeriods();

        calendarData = manyTransactionCreate(calendarData);

        if (Utility.longNN(calendarData.getFiscalCalenderId()) > 0) {
            for (int i = 0; i < periods.size(); i++) {
                FiscalCalenderDetailData period = periods.get(i);
                period.setFiscalCalenderId(calendarData.getFiscalCalenderId());
                period.setFiscalCalenderDetailId(null);
                periods.set(i, manyTransactionCreate(period));
            }

            if (calendar.isServiceScheduleCalendar()) {
                FiscalCalenderDetailData tailPeriod = FiscalCalendarUtility.createTailPeriod(calendarData, periods.size() + 1);
                periods.add(manyTransactionCreate(tailPeriod));
            }

        }

        return new FiscalCalendarIdentView(calendarData,
                periods,
                calendar.isServiceScheduleCalendar()
        );
    }



    private FiscalCalendarIdentView createFiscalCalendar(FiscalCalendarIdentView calendar) {

        FiscalCalenderData calendarData = calendar.getFiscalCalendarData();
        List<FiscalCalenderDetailData> periods = calendar.getPeriods();

        calendarData = create(calendarData);

        if (Utility.longNN(calendarData.getFiscalCalenderId()) > 0) {

            for (int i = 0; i < periods.size(); i++) {
                FiscalCalenderDetailData period = periods.get(i);
                period.setFiscalCalenderId(calendarData.getFiscalCalenderId());
                periods.set(i, create(period));
            }

            if (calendar.isServiceScheduleCalendar()) {
                FiscalCalenderDetailData tailPeriod = FiscalCalendarUtility.createTailPeriod(calendarData, periods.size() + 1);
                periods.add(create(tailPeriod));
            }

        }

        return new FiscalCalendarIdentView(calendarData,
                periods,
                calendar.isServiceScheduleCalendar()
        );
    }

}
