package com.espendwise.manta.model.fullentity;

// Generated by Hibernate Tools

import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.ValueObject;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CountryFullEntity generated by hbm2java
*/
@Entity
@Table(name="CLW_COUNTRY")
public interface CountryFullEntity  {

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
    public static final String COUNTRY_PROPERTIES = "countryProperties";


    public void setCountryId(Long countryId);
    @SequenceGenerator(name="generator", sequenceName="CLW_COUNTRY_SEQ", allocationSize=1)
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="COUNTRY_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getCountryId();

    public void setShortDesc(String shortDesc);
    
    @Column(name="SHORT_DESC", nullable=false, length=64)
    public String getShortDesc();

    public void setUiName(String uiName);
    
    @Column(name="UI_NAME", nullable=false, length=128)
    public String getUiName();

    public void setCountryCode(String countryCode);
    
    @Column(name="COUNTRY_CODE", nullable=false, length=12)
    public String getCountryCode();

    public void setAddDate(Date addDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ADD_DATE", nullable=false, length=7)
    public Date getAddDate();

    public void setAddBy(String addBy);
    
    @Column(name="ADD_BY")
    public String getAddBy();

    public void setModDate(Date modDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_DATE", nullable=false, length=7)
    public Date getModDate();

    public void setModBy(String modBy);
    
    @Column(name="MOD_BY")
    public String getModBy();

    public void setLocaleCd(String localeCd);
    
    @Column(name="LOCALE_CD", length=5)
    public String getLocaleCd();

    public void setInputDateFormat(String inputDateFormat);
    
    @Column(name="INPUT_DATE_FORMAT", length=12)
    public String getInputDateFormat();

    public void setInputTimeFormat(String inputTimeFormat);
    
    @Column(name="INPUT_TIME_FORMAT", length=12)
    public String getInputTimeFormat();

    public void setCountryProperties(Set<CountryPropertyFullEntity> countryProperties);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="countryId")
    public Set<CountryPropertyFullEntity> getCountryProperties();

}

