package com.espendwise.manta.model.data;
// Generated by Hibernate Tools


import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.ValueObject;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * ScheduleData generated by hbm2java
*/
@Entity
@Table(name="CLW_SCHEDULE")
public class ScheduleData extends ValueObject implements TableObject,java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String SCHEDULE_ID = "scheduleId";
    public static final String SHORT_DESC = "shortDesc";
    public static final String BUS_ENTITY_ID = "busEntityId";
    public static final String SCHEDULE_STATUS_CD = "scheduleStatusCd";
    public static final String SCHEDULE_TYPE_CD = "scheduleTypeCd";
    public static final String SCHEDULE_RULE_CD = "scheduleRuleCd";
    public static final String CYCLE = "cycle";
    public static final String EFF_DATE = "effDate";
    public static final String EXP_DATE = "expDate";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";

    private Long scheduleId;
    private String shortDesc;
    private Long busEntityId;
    private String scheduleStatusCd;
    private String scheduleTypeCd;
    private String scheduleRuleCd;
    private Long cycle;
    private Date effDate;
    private Date expDate;
    private Date addDate;
    private String addBy;
    private Date modDate;
    private String modBy;

    public ScheduleData() {
    }
	
    public ScheduleData(String scheduleStatusCd, String scheduleTypeCd, String scheduleRuleCd, Date effDate, Date addDate, Date modDate) {
        this.setScheduleStatusCd(scheduleStatusCd);
        this.setScheduleTypeCd(scheduleTypeCd);
        this.setScheduleRuleCd(scheduleRuleCd);
        this.setEffDate(effDate);
        this.setAddDate(addDate);
        this.setModDate(modDate);
    }

    public ScheduleData(String shortDesc, Long busEntityId, String scheduleStatusCd, String scheduleTypeCd, String scheduleRuleCd, Long cycle, Date effDate, Date expDate, Date addDate, String addBy, Date modDate, String modBy) {
        this.setShortDesc(shortDesc);
        this.setBusEntityId(busEntityId);
        this.setScheduleStatusCd(scheduleStatusCd);
        this.setScheduleTypeCd(scheduleTypeCd);
        this.setScheduleRuleCd(scheduleRuleCd);
        this.setCycle(cycle);
        this.setEffDate(effDate);
        this.setExpDate(expDate);
        this.setAddDate(addDate);
        this.setAddBy(addBy);
        this.setModDate(modDate);
        this.setModBy(modBy);
    }

    @SequenceGenerator(name="generator", sequenceName="CLW_SCHEDULE_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="SCHEDULE_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
        setDirty(true);
    }

    
    @Column(name="SHORT_DESC", length=30)
    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
        setDirty(true);
    }

    
    @Column(name="BUS_ENTITY_ID", columnDefinition="number", precision=38, scale=0)
    public Long getBusEntityId() {
        return this.busEntityId;
    }

    public void setBusEntityId(Long busEntityId) {
        this.busEntityId = busEntityId;
        setDirty(true);
    }

    
    @Column(name="SCHEDULE_STATUS_CD", nullable=false, length=30)
    public String getScheduleStatusCd() {
        return this.scheduleStatusCd;
    }

    public void setScheduleStatusCd(String scheduleStatusCd) {
        this.scheduleStatusCd = scheduleStatusCd;
        setDirty(true);
    }

    
    @Column(name="SCHEDULE_TYPE_CD", nullable=false, length=30)
    public String getScheduleTypeCd() {
        return this.scheduleTypeCd;
    }

    public void setScheduleTypeCd(String scheduleTypeCd) {
        this.scheduleTypeCd = scheduleTypeCd;
        setDirty(true);
    }

    
    @Column(name="SCHEDULE_RULE_CD", nullable=false, length=30)
    public String getScheduleRuleCd() {
        return this.scheduleRuleCd;
    }

    public void setScheduleRuleCd(String scheduleRuleCd) {
        this.scheduleRuleCd = scheduleRuleCd;
        setDirty(true);
    }

    
    @Column(name="CYCLE", columnDefinition="number", precision=38, scale=0)
    public Long getCycle() {
        return this.cycle;
    }

    public void setCycle(Long cycle) {
        this.cycle = cycle;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EFF_DATE", nullable=false, length=7)
    public Date getEffDate() {
        return this.effDate;
    }

    public void setEffDate(Date effDate) {
        this.effDate = effDate;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXP_DATE", length=7)
    public Date getExpDate() {
        return this.expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ADD_DATE", nullable=false, length=7)
    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
        setDirty(true);
    }

    
    @Column(name="ADD_BY")
    public String getAddBy() {
        return this.addBy;
    }

    public void setAddBy(String addBy) {
        this.addBy = addBy;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_DATE", nullable=false, length=7)
    public Date getModDate() {
        return this.modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
        setDirty(true);
    }

    
    @Column(name="MOD_BY")
    public String getModBy() {
        return this.modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
        setDirty(true);
    }




}

