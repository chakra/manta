package com.espendwise.manta.model.view;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
/**
 * SiteDeliveryScheduleView generated by hbm2java
*/
public class SiteDeliveryScheduleView extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String SITE_ID = "siteId";
    public static final String SITE_NAME = "siteName";
    public static final String SITE_STATUS_CD = "siteStatusCd";
    public static final String SITE_SCHEDULE_TYPE = "siteScheduleType";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String POSTAL_CODE = "postalCode";
    public static final String COUNTY = "county";
    public static final String WEEK1OF_MONTH = "week1ofMonth";
    public static final String WEEK2OF_MONTH = "week2ofMonth";
    public static final String WEEK3OF_MONTH = "week3ofMonth";
    public static final String WEEK4OF_MONTH = "week4ofMonth";
    public static final String LAST_WEEKOF_MONTH = "lastWeekofMonth";
    public static final String INTERV_WEEK = "intervWeek";

    private Long siteId;
    private String siteName;
    private String siteStatusCd;
    private String siteScheduleType;
    private String city;
    private String state;
    private String postalCode;
    private String county;
    private boolean week1ofMonth;
    private boolean week2ofMonth;
    private boolean week3ofMonth;
    private boolean week4ofMonth;
    private boolean lastWeekofMonth;
    private String intervWeek;

    public SiteDeliveryScheduleView() {
    }
	
    public SiteDeliveryScheduleView(Long siteId) {
        this.setSiteId(siteId);
    }

    public SiteDeliveryScheduleView(Long siteId, String siteName, String siteStatusCd, String siteScheduleType, String city, String state, String postalCode, String county, boolean week1ofMonth, boolean week2ofMonth, boolean week3ofMonth, boolean week4ofMonth, boolean lastWeekofMonth, String intervWeek) {
        this.setSiteId(siteId);
        this.setSiteName(siteName);
        this.setSiteStatusCd(siteStatusCd);
        this.setSiteScheduleType(siteScheduleType);
        this.setCity(city);
        this.setState(state);
        this.setPostalCode(postalCode);
        this.setCounty(county);
        this.setWeek1ofMonth(week1ofMonth);
        this.setWeek2ofMonth(week2ofMonth);
        this.setWeek3ofMonth(week3ofMonth);
        this.setWeek4ofMonth(week4ofMonth);
        this.setLastWeekofMonth(lastWeekofMonth);
        this.setIntervWeek(intervWeek);
    }

    public Long getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
        setDirty(true);
    }

    public String getSiteName() {
        return this.siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
        setDirty(true);
    }

    public String getSiteStatusCd() {
        return this.siteStatusCd;
    }

    public void setSiteStatusCd(String siteStatusCd) {
        this.siteStatusCd = siteStatusCd;
        setDirty(true);
    }

    public String getSiteScheduleType() {
        return this.siteScheduleType;
    }

    public void setSiteScheduleType(String siteScheduleType) {
        this.siteScheduleType = siteScheduleType;
        setDirty(true);
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
        setDirty(true);
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
        setDirty(true);
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        setDirty(true);
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
        setDirty(true);
    }

    public boolean isWeek1ofMonth() {
        return this.week1ofMonth;
    }

    public void setWeek1ofMonth(boolean week1ofMonth) {
        this.week1ofMonth = week1ofMonth;
        setDirty(true);
    }

    public boolean isWeek2ofMonth() {
        return this.week2ofMonth;
    }

    public void setWeek2ofMonth(boolean week2ofMonth) {
        this.week2ofMonth = week2ofMonth;
        setDirty(true);
    }

    public boolean isWeek3ofMonth() {
        return this.week3ofMonth;
    }

    public void setWeek3ofMonth(boolean week3ofMonth) {
        this.week3ofMonth = week3ofMonth;
        setDirty(true);
    }

    public boolean isWeek4ofMonth() {
        return this.week4ofMonth;
    }

    public void setWeek4ofMonth(boolean week4ofMonth) {
        this.week4ofMonth = week4ofMonth;
        setDirty(true);
    }

    public boolean isLastWeekofMonth() {
        return this.lastWeekofMonth;
    }

    public void setLastWeekofMonth(boolean lastWeekofMonth) {
        this.lastWeekofMonth = lastWeekofMonth;
        setDirty(true);
    }

    public String getIntervWeek() {
        return this.intervWeek;
    }

    public void setIntervWeek(String intervWeek) {
        this.intervWeek = intervWeek;
        setDirty(true);
    }




}


