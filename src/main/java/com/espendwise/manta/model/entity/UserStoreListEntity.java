package com.espendwise.manta.model.entity;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * UserStoreListEntity generated by hbm2java
*/
@Entity
@Table(name="CLW_USER_ASSOC")
public class UserStoreListEntity extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String USER_ASSOC_ID = "userAssocId";
    public static final String USER_ID = "userId";
    public static final String USER_ASSOC_CD = "userAssocCd";
    public static final String STORES = "stores";

    private Long userAssocId;
    private Long userId;
    private String userAssocCd;
    private StoreListEntity stores;

    public UserStoreListEntity() {
    }
	
    public UserStoreListEntity(Long userAssocId) {
        this.setUserAssocId(userAssocId);
    }

    public UserStoreListEntity(Long userAssocId, Long userId, String userAssocCd, StoreListEntity stores) {
        this.setUserAssocId(userAssocId);
        this.setUserId(userId);
        this.setUserAssocCd(userAssocCd);
        this.setStores(stores);
    }

    @Id      
    @Column(name="USER_ASSOC_ID", nullable=false)
    public Long getUserAssocId() {
        return this.userAssocId;
    }

    public void setUserAssocId(Long userAssocId) {
        this.userAssocId = userAssocId;
        setDirty(true);
    }

    
    @Column(name="USER_ID", unique=true)
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        setDirty(true);
    }

    
    @Column(name="USER_ASSOC_CD")
    public String getUserAssocCd() {
        return this.userAssocCd;
    }

    public void setUserAssocCd(String userAssocCd) {
        this.userAssocCd = userAssocCd;
        setDirty(true);
    }

    @ManyToOne(fetch=FetchType.EAGER)    @JoinColumn(name="BUS_ENTITY_ID")
    public StoreListEntity getStores() {
        return this.stores;
    }

    public void setStores(StoreListEntity stores) {
        this.stores = stores;
        setDirty(true);
    }




}


