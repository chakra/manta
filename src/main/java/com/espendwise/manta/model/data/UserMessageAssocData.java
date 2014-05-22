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
 * UserMessageAssocData generated by hbm2java
*/
@Entity
@Table(name="CLW_USER_MESSAGE_ASSOC")
public class UserMessageAssocData extends ValueObject implements TableObject,java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String USER_MESSAGE_ASSOC_ID = "userMessageAssocId";
    public static final String USER_ID = "userId";
    public static final String STORE_MESSAGE_ID = "storeMessageId";
    public static final String STORE_MESSAGE_DETAIL_ID = "storeMessageDetailId";
    public static final String READ_DATE = "readDate";
    public static final String ADD_BY = "addBy";
    public static final String ADD_DATE = "addDate";
    public static final String MOD_BY = "modBy";
    public static final String MOD_DATE = "modDate";

    private Long userMessageAssocId;
    private Long userId;
    private Long storeMessageId;
    private Long storeMessageDetailId;
    private Date readDate;
    private String addBy;
    private Date addDate;
    private String modBy;
    private Date modDate;

    public UserMessageAssocData() {
    }
	
    public UserMessageAssocData(Long userId, Long storeMessageId, Long storeMessageDetailId, Date readDate, Date addDate, Date modDate) {
        this.setUserId(userId);
        this.setStoreMessageId(storeMessageId);
        this.setStoreMessageDetailId(storeMessageDetailId);
        this.setReadDate(readDate);
        this.setAddDate(addDate);
        this.setModDate(modDate);
    }

    public UserMessageAssocData(Long userId, Long storeMessageId, Long storeMessageDetailId, Date readDate, String addBy, Date addDate, String modBy, Date modDate) {
        this.setUserId(userId);
        this.setStoreMessageId(storeMessageId);
        this.setStoreMessageDetailId(storeMessageDetailId);
        this.setReadDate(readDate);
        this.setAddBy(addBy);
        this.setAddDate(addDate);
        this.setModBy(modBy);
        this.setModDate(modDate);
    }

    @SequenceGenerator(name="generator", sequenceName="CLW_USER_MESSAGE_ASSOC_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="USER_MESSAGE_ASSOC_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getUserMessageAssocId() {
        return this.userMessageAssocId;
    }

    public void setUserMessageAssocId(Long userMessageAssocId) {
        this.userMessageAssocId = userMessageAssocId;
        setDirty(true);
    }

    
    @Column(name="USER_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        setDirty(true);
    }

    
    @Column(name="STORE_MESSAGE_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getStoreMessageId() {
        return this.storeMessageId;
    }

    public void setStoreMessageId(Long storeMessageId) {
        this.storeMessageId = storeMessageId;
        setDirty(true);
    }

    
    @Column(name="STORE_MESSAGE_DETAIL_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getStoreMessageDetailId() {
        return this.storeMessageDetailId;
    }

    public void setStoreMessageDetailId(Long storeMessageDetailId) {
        this.storeMessageDetailId = storeMessageDetailId;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="READ_DATE", nullable=false, length=7)
    public Date getReadDate() {
        return this.readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
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
    @Column(name="ADD_DATE", nullable=false, length=7)
    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_DATE", nullable=false, length=7)
    public Date getModDate() {
        return this.modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
        setDirty(true);
    }




}


