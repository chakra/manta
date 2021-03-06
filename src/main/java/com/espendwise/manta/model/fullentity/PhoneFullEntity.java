package com.espendwise.manta.model.fullentity;

// Generated by Hibernate Tools

import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.ValueObject;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Type;

/**
 * PhoneFullEntity generated by hbm2java
*/
@Entity
@Table(name="CLW_PHONE")
public interface PhoneFullEntity  {

    public static final String PHONE_ID = "phoneId";
    public static final String USER = "userId";
    public static final String BUS_ENTITY = "busEntityId";
    public static final String PHONE_COUNTRY_CD = "phoneCountryCd";
    public static final String PHONE_AREA_CODE = "phoneAreaCode";
    public static final String PHONE_NUM = "phoneNum";
    public static final String SHORT_DESC = "shortDesc";
    public static final String PHONE_TYPE_CD = "phoneTypeCd";
    public static final String PHONE_STATUS_CD = "phoneStatusCd";
    public static final String PRIMARY_IND = "primaryInd";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String CONTACT_ID = "contactId";

    public static final String USER_ID = "userId.userId";
    public static final String BUS_ENTITY_ID = "busEntityId.busEntityId";

    public void setPhoneId(Long phoneId);
    @SequenceGenerator(name="generator", sequenceName="CLW_PHONE_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="PHONE_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getPhoneId();

    public void setUserId(UserFullEntity userId);
    @ManyToOne(fetch=FetchType.LAZY)    @JoinColumn(name="USER_ID", columnDefinition="number")
    public UserFullEntity getUserId();

    public void setBusEntityId(BusEntityFullEntity busEntityId);
    @ManyToOne(fetch=FetchType.LAZY)    @JoinColumn(name="BUS_ENTITY_ID", columnDefinition="number")
    public BusEntityFullEntity getBusEntityId();

    public void setPhoneCountryCd(String phoneCountryCd);
    
    @Column(name="PHONE_COUNTRY_CD", length=30)
    public String getPhoneCountryCd();

    public void setPhoneAreaCode(String phoneAreaCode);
    
    @Column(name="PHONE_AREA_CODE", length=3)
    public String getPhoneAreaCode();

    public void setPhoneNum(String phoneNum);
    
    @Column(name="PHONE_NUM", length=60)
    public String getPhoneNum();

    public void setShortDesc(String shortDesc);
    
    @Column(name="SHORT_DESC", length=30)
    public String getShortDesc();

    public void setPhoneTypeCd(String phoneTypeCd);
    
    @Column(name="PHONE_TYPE_CD", nullable=false, length=30)
    public String getPhoneTypeCd();

    public void setPhoneStatusCd(String phoneStatusCd);
    
    @Column(name="PHONE_STATUS_CD", nullable=false, length=30)
    public String getPhoneStatusCd();

    public void setPrimaryInd(Boolean primaryInd);
    @Type(type="com.espendwise.manta.support.hibernate.NumberBooleanType")
    @Column(name="PRIMARY_IND", columnDefinition="number", precision=1, scale=0)
    public Boolean getPrimaryInd();

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

    public void setContactId(Long contactId);
    
    @Column(name="CONTACT_ID", columnDefinition="number", precision=38, scale=0)
    public Long getContactId();

}

