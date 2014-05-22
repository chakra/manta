package com.espendwise.manta.web.forms;


import java.util.Set;
import java.util.SortedMap;

public class FiscalCalendarForm {

    private Long fiscalCalendarId;

    private SortedMap<Integer, String> periods;
    private String fiscalYear;
    private String expDate;
    private String effDate;
    private String periodCd;
    private boolean editable;
    private boolean serviceScheduleCalendar;

    public Long getFiscalCalendarId() {
        return fiscalCalendarId;
    }

    public void setFiscalCalendarId(Long fiscalCalendarId) {
        this.fiscalCalendarId = fiscalCalendarId;
    }

    public SortedMap<Integer, String> getPeriods() {
        return periods;
    }

    public Set<Integer> getPeriodNumbers() {
        return periods.keySet();
    }

    public void setPeriods(SortedMap<Integer, String> periods) {
        this.periods = periods;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getPeriodCd() {
        return periodCd;
    }

    public void setPeriodCd(String periodCd) {
        this.periodCd = periodCd;
    }

    public void setEditable(boolean  isEditable) {
        this.editable = isEditable;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean getIsEditable() {
        return fiscalCalendarId == null || fiscalCalendarId == 0 || editable;
    }

    public boolean getIsServiceScheduleCalendar() {
        return this.serviceScheduleCalendar;
    }

    public boolean isServiceScheduleCalendar() {
      return this.serviceScheduleCalendar;
    }

    public void setServiceScheduleCalendar(boolean serviceScheduleCalendar) {
        this.serviceScheduleCalendar = serviceScheduleCalendar;
    }
}
