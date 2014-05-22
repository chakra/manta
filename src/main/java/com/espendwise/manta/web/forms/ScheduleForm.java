package com.espendwise.manta.web.forms;


import java.util.List;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.Pair;

public abstract class ScheduleForm extends WebForm implements Initializable {
    //base
    private Long scheduleId;
    private String scheduleName;
    private Long assocStoreId = new Long(0);
    private String scheduleStatus;
    private String scheduleType;
    private String scheduleRule;
    private Long scheduleCycle;
    private String scheduleEffectiveDate;
    private String scheduleExpirationDate;
    
    //details
    private String scheduleIntervalHours;
    private String scheduleCutoffTime;
    private String scheduleAlsoDates;
    private String schedulePhysicalInventoryDates;
    
    //reference data
    private List<Pair<String, String>> statusChoices;
    
    private boolean initialize;

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public Long getAssocStoreId() {
		return assocStoreId;
	}

	public void setAssocStoreId(Long assocStoreId) {
		this.assocStoreId = assocStoreId;
	}

	public String getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(String scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getScheduleRule() {
		return scheduleRule;
	}

	public void setScheduleRule(String scheduleRule) {
		this.scheduleRule = scheduleRule;
	}

	public Long getScheduleCycle() {
		return scheduleCycle;
	}

	public void setScheduleCycle(Long scheduleCycle) {
		this.scheduleCycle = scheduleCycle;
	}

	public String getScheduleEffectiveDate() {
		return scheduleEffectiveDate;
	}

	public void setScheduleEffectiveDate(String scheduleEffectiveDate) {
		this.scheduleEffectiveDate = scheduleEffectiveDate;
	}

	public String getScheduleExpirationDate() {
		return scheduleExpirationDate;
	}

	public void setScheduleExpirationDate(String scheduleExpirationDate) {
		this.scheduleExpirationDate = scheduleExpirationDate;
	}

	public String getScheduleIntervalHours() {
		return scheduleIntervalHours;
	}

	public void setScheduleIntervalHours(String scheduleIntervalHours) {
		this.scheduleIntervalHours = scheduleIntervalHours;
	}

	public String getScheduleCutoffTime() {
		return scheduleCutoffTime;
	}

	public void setScheduleCutoffTime(String scheduleCutoffTime) {
		this.scheduleCutoffTime = scheduleCutoffTime;
	}

	public String getScheduleAlsoDates() {
		return scheduleAlsoDates;
	}

	public void setScheduleAlsoDates(String scheduleAlsoDates) {
		this.scheduleAlsoDates = scheduleAlsoDates;
	}

	public String getSchedulePhysicalInventoryDates() {
		return schedulePhysicalInventoryDates;
	}

	public void setSchedulePhysicalInventoryDates(
			String schedulePhysicalInventoryDates) {
		this.schedulePhysicalInventoryDates = schedulePhysicalInventoryDates;
	}

	public List<Pair<String, String>> getStatusChoices() {
		return statusChoices;
	}

	public void setStatusChoices(List<Pair<String, String>> statusChoices) {
		this.statusChoices = statusChoices;
	}

	public boolean isInitialize() {
		return initialize;
	}

	public void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}

	@Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public boolean isNew() {
      return isInitialized() && (scheduleId  == null || scheduleId == 0);
    }

    @Override
    public String toString() {
        return "ScheduleForm{" +
                "initialize=" + initialize +
                ", scheduleId=" + scheduleId +
                ", scheduleName='" + scheduleName + '\'' +
                ", assocStoreId='" + assocStoreId + '\'' +
                ", scheduleStatus='" + scheduleStatus + '\'' +
                ", scheduleType='" + scheduleType + '\'' +
                ", scheduleRule='" + scheduleRule + '\'' +
                ", scheduleCycle='" + scheduleCycle + '\'' +
                ", scheduleEffectiveDate='" + scheduleEffectiveDate + '\'' +
                ", scheduleExpirationDate='" + scheduleExpirationDate + '\'' +
                ", scheduleIntervalHours='" + scheduleIntervalHours + '\'' +
                ", scheduleCutoffTime='" + scheduleCutoffTime + '\'' +
                ", scheduleAlsoDates='" + scheduleAlsoDates + '\'' +
                ", schedulePhysicalInventoryDates='" + schedulePhysicalInventoryDates + '\'' +
                '}';
    }
}

//details
