package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.FiscalCalenderData;
import com.espendwise.manta.model.data.FiscalCalenderDetailData;
import com.espendwise.manta.model.view.FiscalCalendarIdentView;
import com.espendwise.manta.model.view.FiscalCalendarListView;
import com.espendwise.manta.model.view.FiscalCalendarPhysicalView;
import com.espendwise.manta.model.view.FiscalReportIntersectResView;

import java.util.Date;
import java.util.List;

public interface FiscalCalendarDAO {
	
	public FiscalReportIntersectResView getFiscalCalendarIntersectYear(Long busEntityId, Long fiscalCalenderId, Date effDate, Date end_date);

    public List<FiscalCalendarListView> findFiscalCalenders(Long busEntityId);

    public FiscalCalendarIdentView findCalendarIdent(Long busEntityId, Long fiscalCalendarId);

    public FiscalCalendarIdentView saveFiscalCalendar(FiscalCalendarIdentView calendar);
    
    public FiscalCalendarIdentView saveFiscalCalendarClone(FiscalCalendarIdentView calendar);

    public Long fiscalCalendarsCount(Long accountId);

    public FiscalCalendarPhysicalView selectCalenderForYear(List<FiscalCalendarListView> calendars, Integer fiscalYear, Date forDate);

    public FiscalCalendarPhysicalView findFiscalCalenderForDate(Long busEntityId, Integer fiscalYear, Date forDate);

    public boolean isServiceScheduleCalendar(Long busEntityId);
    
    public FiscalCalenderData findCurrentFiscalCalendar(Long accountId);
 }
