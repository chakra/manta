package com.espendwise.manta.model.view;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
import com.espendwise.manta.model.data.FiscalCalenderData;
import com.espendwise.manta.model.data.FiscalCalenderDetailData;
import java.util.List;
/**
 * FiscalCalendarIdentView generated by hbm2java
*/
public class FiscalCalendarIdentView extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String FISCAL_CALENDAR_DATA = "fiscalCalendarData";
    public static final String PERIODS = "periods";
    public static final String SERVICE_SCHEDULE_CALENDAR = "serviceScheduleCalendar";

    private FiscalCalenderData fiscalCalendarData;
    private List<com.espendwise.manta.model.data.FiscalCalenderDetailData> periods;
    private boolean serviceScheduleCalendar;

    public FiscalCalendarIdentView() {
    }
	
    public FiscalCalendarIdentView(FiscalCalenderData fiscalCalendarData) {
        this.setFiscalCalendarData(fiscalCalendarData);
    }

    public FiscalCalendarIdentView(FiscalCalenderData fiscalCalendarData, List<com.espendwise.manta.model.data.FiscalCalenderDetailData> periods, boolean serviceScheduleCalendar) {
        this.setFiscalCalendarData(fiscalCalendarData);
        this.setPeriods(periods);
        this.setServiceScheduleCalendar(serviceScheduleCalendar);
    }

    public FiscalCalenderData getFiscalCalendarData() {
        return this.fiscalCalendarData;
    }

    public void setFiscalCalendarData(FiscalCalenderData fiscalCalendarData) {
        this.fiscalCalendarData = fiscalCalendarData;
        setDirty(true);
    }

    public List<com.espendwise.manta.model.data.FiscalCalenderDetailData> getPeriods() {
        return this.periods;
    }

    public void setPeriods(List<com.espendwise.manta.model.data.FiscalCalenderDetailData> periods) {
        this.periods = periods;
        setDirty(true);
    }

    public boolean isServiceScheduleCalendar() {
        return this.serviceScheduleCalendar;
    }

    public void setServiceScheduleCalendar(boolean serviceScheduleCalendar) {
        this.serviceScheduleCalendar = serviceScheduleCalendar;
        setDirty(true);
    }




}


