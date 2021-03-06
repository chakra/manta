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
 * CountryData generated by hbm2java
*/
@Entity
@Table(name="CLW_COUNTRY")
public class CountryData extends ValueObject implements TableObject,java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String COUNTRY_ID = "countryId";
    public static final String SHORT_DESC = "shortDesc";
    public static final String UI_NAME = "uiName";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String LOCALE_CD = "localeCd";
    public static final String INPUT_DATE_FORMAT = "inputDateFormat";
    public static final String INPUT_TIME_FORMAT = "inputTimeFormat";

    private Long countryId;
    private String shortDesc;
    private String uiName;
    private String countryCode;
    private Date addDate;
    private String addBy;
    private Date modDate;
    private String modBy;
    private String localeCd;
    private String inputDateFormat;
    private String inputTimeFormat;

    public CountryData() {
    }
	
    public CountryData(String shortDesc, String uiName, String countryCode, Date addDate, Date modDate) {
        this.setShortDesc(shortDesc);
        this.setUiName(uiName);
        this.setCountryCode(countryCode);
        this.setAddDate(addDate);
        this.setModDate(modDate);
    }

    public CountryData(String shortDesc, String uiName, String countryCode, Date addDate, String addBy, Date modDate, String modBy, String localeCd, String inputDateFormat, String inputTimeFormat) {
        this.setShortDesc(shortDesc);
        this.setUiName(uiName);
        this.setCountryCode(countryCode);
        this.setAddDate(addDate);
        this.setAddBy(addBy);
        this.setModDate(modDate);
        this.setModBy(modBy);
        this.setLocaleCd(localeCd);
        this.setInputDateFormat(inputDateFormat);
        this.setInputTimeFormat(inputTimeFormat);
    }

    @SequenceGenerator(name="generator", sequenceName="CLW_COUNTRY_SEQ", allocationSize=1)
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="COUNTRY_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
        setDirty(true);
    }

    
    @Column(name="SHORT_DESC", nullable=false, length=64)
    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
        setDirty(true);
    }

    
    @Column(name="UI_NAME", nullable=false, length=128)
    public String getUiName() {
        return this.uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
        setDirty(true);
    }

    
    @Column(name="COUNTRY_CODE", nullable=false, length=12)
    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    
    @Column(name="INPUT_DATE_FORMAT", length=12)
    public String getInputDateFormat() {
        return this.inputDateFormat;
    }

    public void setInputDateFormat(String inputDateFormat) {
        this.inputDateFormat = inputDateFormat;
        setDirty(true);
    }

    
    @Column(name="INPUT_TIME_FORMAT", length=12)
    public String getInputTimeFormat() {
        return this.inputTimeFormat;
    }

    public void setInputTimeFormat(String inputTimeFormat) {
        this.inputTimeFormat = inputTimeFormat;
        setDirty(true);
    }




}


