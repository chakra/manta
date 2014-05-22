package com.espendwise.manta.web.forms;


import java.io.Serializable;

public class DeliveryScheduleForm implements Serializable {

    private String scheduleId;
    private String scheduleName;
    private String nextDelivery;
    private String intervalHour;
    private String cutoffTime;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }


    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getNextDelivery() {
        return nextDelivery;
    }

    public void setNextDelivery(String nextDelivery) {
        this.nextDelivery = nextDelivery;
    }


    public String getIntervalHour() {
        return intervalHour;
    }

    public void setIntervalHour(String intervalHour) {
        this.intervalHour = intervalHour;
    }

    public String getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(String cutoffTime) {
        this.cutoffTime = cutoffTime;
    }
}
