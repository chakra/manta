package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.FiscalCalendarListView;
import com.espendwise.manta.spi.Initializable;

import java.util.List;

public class AccountFiscalCalendarForm extends WebForm implements Initializable {

    private boolean initialize = false;

    private Long accountId;

    private FiscalCalendarForm calendarToEdit;
    private List<FiscalCalendarListView> calendars;

    private String numberOfBudgetPeriods;
    private boolean serviceScheduleCalendar;
    private Integer currentFiscalYear;
    private Boolean showCopyBudget = false;


    public AccountFiscalCalendarForm(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public void setServiceScheduleCalendar(boolean serviceScheduleCalendar) {
        this.serviceScheduleCalendar = serviceScheduleCalendar;
    }

    public boolean getIsServiceScheduleCalendar() {
        return serviceScheduleCalendar;
    }

    public boolean isNew() {
        return isInitialized() && (calendarToEdit != null && (calendarToEdit.getFiscalCalendarId() == null || calendarToEdit.getFiscalCalendarId() == 0));
    }

    public void setCalendarToEdit(FiscalCalendarForm calendarToEdit) {
        this.calendarToEdit = calendarToEdit;
    }

    public void setCalendars(List<FiscalCalendarListView> calendars) {
        this.calendars = calendars;
    }

    public FiscalCalendarForm getCalendarToEdit() {
        return calendarToEdit;
    }

    public List<FiscalCalendarListView> getCalendars() {
        return calendars;
    }

    public String getNumberOfBudgetPeriods() {
        return numberOfBudgetPeriods;
    }

    public void setNumberOfBudgetPeriods(String numberOfBudgetPeriods) {
        this.numberOfBudgetPeriods = numberOfBudgetPeriods;
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public Integer getCurrentFiscalYear() {
        return currentFiscalYear;
    }

    public void setCurrentFiscalYear(Integer currentFiscalYear) {
        this.currentFiscalYear = currentFiscalYear;
    }

    public Boolean getShowCopyBudget() {
        return showCopyBudget;
    }

    public void setShowCopyBudget(Boolean showCopyBudget) {
        this.showCopyBudget = showCopyBudget;
    }

    @Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return initialize;
    }

}
