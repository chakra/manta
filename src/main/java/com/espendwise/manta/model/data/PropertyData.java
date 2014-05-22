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
 * PropertyData generated by hbm2java
*/
@Entity
@Table(name="CLW_PROPERTY")
public class PropertyData extends ValueObject implements TableObject,java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String PROPERTY_ID = "propertyId";
    public static final String BUS_ENTITY_ID = "busEntityId";
    public static final String USER_ID = "userId";
    public static final String SHORT_DESC = "shortDesc";
    public static final String VALUE = "value";
    public static final String PROPERTY_STATUS_CD = "propertyStatusCd";
    public static final String PROPERTY_TYPE_CD = "propertyTypeCd";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String LOCALE_CD = "localeCd";
    public static final String ORIGINAL_USER_ID = "originalUserId";

    private Long propertyId;
    private Long busEntityId;
    private Long userId;
    private String shortDesc;
    private String value;
    private String propertyStatusCd;
    private String propertyTypeCd;
    private Date addDate;
    private String addBy;
    private Date modDate;
    private String modBy;
    private String localeCd;
    private Long originalUserId;

    public PropertyData() {
    }
	
    public PropertyData(String propertyStatusCd, String propertyTypeCd, Date addDate, Date modDate) {
        this.setPropertyStatusCd(propertyStatusCd);
        this.setPropertyTypeCd(propertyTypeCd);
        this.setAddDate(addDate);
        this.setModDate(modDate);
    }

    public PropertyData(Long busEntityId, Long userId, String shortDesc, String value, String propertyStatusCd, String propertyTypeCd, Date addDate, String addBy, Date modDate, String modBy, String localeCd, Long originalUserId) {
        this.setBusEntityId(busEntityId);
        this.setUserId(userId);
        this.setShortDesc(shortDesc);
        this.setValue(value);
        this.setPropertyStatusCd(propertyStatusCd);
        this.setPropertyTypeCd(propertyTypeCd);
        this.setAddDate(addDate);
        this.setAddBy(addBy);
        this.setModDate(modDate);
        this.setModBy(modBy);
        this.setLocaleCd(localeCd);
        this.setOriginalUserId(originalUserId);
    }

    @SequenceGenerator(name="generator", sequenceName="CLW_PROPERTY_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="PROPERTY_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getPropertyId() {
        return this.propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
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

    
    @Column(name="USER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        setDirty(true);
    }

    
    @Column(name="SHORT_DESC")
    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
        setDirty(true);
    }

    
    @Column(name="CLW_VALUE", length=4000)
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
        setDirty(true);
    }

    
    @Column(name="PROPERTY_STATUS_CD", nullable=false, length=30)
    public String getPropertyStatusCd() {
        return this.propertyStatusCd;
    }

    public void setPropertyStatusCd(String propertyStatusCd) {
        this.propertyStatusCd = propertyStatusCd;
        setDirty(true);
    }

    
    @Column(name="PROPERTY_TYPE_CD", nullable=false, length=30)
    public String getPropertyTypeCd() {
        return this.propertyTypeCd;
    }

    public void setPropertyTypeCd(String propertyTypeCd) {
        this.propertyTypeCd = propertyTypeCd;
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

    
    @Column(name="LOCALE_CD", length=5)
    public String getLocaleCd() {
        return this.localeCd;
    }

    public void setLocaleCd(String localeCd) {
        this.localeCd = localeCd;
        setDirty(true);
    }

    
    @Column(name="ORIGINAL_USER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getOriginalUserId() {
        return this.originalUserId;
    }

    public void setOriginalUserId(Long originalUserId) {
        this.originalUserId = originalUserId;
        setDirty(true);
    }




}


