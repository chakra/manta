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

/**
 * UserStoreFullEntity generated by hbm2java
*/
@Entity
@Table(name="ESW_USER_STORE")
public interface UserStoreFullEntity  {

    public static final String USER_STORE_ID = "userStoreId";
    public static final String ALL_USER = "allUserId";
    public static final String ALL_STORE = "allStoreId";
    public static final String LAST_LOGIN_DATE = "lastLoginDate";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String LOCALE_CD = "localeCd";

    public static final String ALL_USER_ID = "allUserId.allUserId";
    public static final String ALL_STORE_ID = "allStoreId.allStoreId";

    public void setUserStoreId(Long userStoreId);
    @SequenceGenerator(name="generator", sequenceName="ESW_USER_STORE_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="USER_STORE_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getUserStoreId();

    public void setAllUserId(AllUserFullEntity allUserId);
    @ManyToOne(fetch=FetchType.LAZY)    @JoinColumn(name="ALL_USER_ID", nullable=false, columnDefinition="number")
    public AllUserFullEntity getAllUserId();

    public void setAllStoreId(AllStoreFullEntity allStoreId);
    @ManyToOne(fetch=FetchType.LAZY)    @JoinColumn(name="ALL_STORE_ID", nullable=false, columnDefinition="number")
    public AllStoreFullEntity getAllStoreId();

    public void setLastLoginDate(Date lastLoginDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_LOGIN_DATE", length=7)
    public Date getLastLoginDate();

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

}
