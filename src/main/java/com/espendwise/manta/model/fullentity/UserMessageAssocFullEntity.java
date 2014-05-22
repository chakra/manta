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
 * UserMessageAssocFullEntity generated by hbm2java
*/
@Entity
@Table(name="CLW_USER_MESSAGE_ASSOC")
public interface UserMessageAssocFullEntity  {

    public static final String USER_MESSAGE_ASSOC_ID = "userMessageAssocId";
    public static final String USER = "userId";
    public static final String STORE_MESSAGE_DETAIL = "storeMessageDetailId";
    public static final String STORE_MESSAGE_ID = "storeMessageId";
    public static final String READ_DATE = "readDate";
    public static final String ADD_BY = "addBy";
    public static final String ADD_DATE = "addDate";
    public static final String MOD_BY = "modBy";
    public static final String MOD_DATE = "modDate";

    public static final String USER_ID = "userId.userId";
    public static final String STORE_MESSAGE_DETAIL_ID = "storeMessageDetailId.storeMessageDetailId";

    public void setUserMessageAssocId(Long userMessageAssocId);
    @SequenceGenerator(name="generator", sequenceName="CLW_USER_MESSAGE_ASSOC_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="USER_MESSAGE_ASSOC_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getUserMessageAssocId();

    public void setUserId(UserFullEntity userId);
    @ManyToOne(fetch=FetchType.LAZY)    @JoinColumn(name="USER_ID", nullable=false, columnDefinition="number")
    public UserFullEntity getUserId();

    public void setStoreMessageDetailId(StoreMessageDetailFullEntity storeMessageDetailId);
    @ManyToOne(fetch=FetchType.LAZY)    @JoinColumn(name="STORE_MESSAGE_DETAIL_ID", nullable=false, columnDefinition="number")
    public StoreMessageDetailFullEntity getStoreMessageDetailId();

    public void setStoreMessageId(Long storeMessageId);
    
    @Column(name="STORE_MESSAGE_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getStoreMessageId();

    public void setReadDate(Date readDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="READ_DATE", nullable=false, length=7)
    public Date getReadDate();

    public void setAddBy(String addBy);
    
    @Column(name="ADD_BY")
    public String getAddBy();

    public void setAddDate(Date addDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ADD_DATE", nullable=false, length=7)
    public Date getAddDate();

    public void setModBy(String modBy);
    
    @Column(name="MOD_BY")
    public String getModBy();

    public void setModDate(Date modDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_DATE", nullable=false, length=7)
    public Date getModDate();

}
