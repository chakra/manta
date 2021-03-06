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
 * AllStoreData generated by hbm2java
*/
@Entity
@Table(name="ESW_ALL_STORE")
public class AllStoreData extends ValueObject implements TableObject,java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String ALL_STORE_ID = "allStoreId";
    public static final String STORE_ID = "storeId";
    public static final String STORE_NAME = "storeName";
    public static final String DOMAIN = "domain";
    public static final String DATASOURCE = "datasource";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";

    private Long allStoreId;
    private Long storeId;
    private String storeName;
    private String domain;
    private String datasource;
    private Date addDate;
    private String addBy;
    private Date modDate;
    private String modBy;

    public AllStoreData() {
    }
	
    public AllStoreData(Long storeId, Date addDate, Date modDate) {
        this.setStoreId(storeId);
        this.setAddDate(addDate);
        this.setModDate(modDate);
    }

    public AllStoreData(Long storeId, String storeName, String domain, String datasource, Date addDate, String addBy, Date modDate, String modBy) {
        this.setStoreId(storeId);
        this.setStoreName(storeName);
        this.setDomain(domain);
        this.setDatasource(datasource);
        this.setAddDate(addDate);
        this.setAddBy(addBy);
        this.setModDate(modDate);
        this.setModBy(modBy);
    }

    @SequenceGenerator(name="generator", sequenceName="ESW_ALL_STORE_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="ALL_STORE_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getAllStoreId() {
        return this.allStoreId;
    }

    public void setAllStoreId(Long allStoreId) {
        this.allStoreId = allStoreId;
        setDirty(true);
    }

    
    @Column(name="STORE_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getStoreId() {
        return this.storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
        setDirty(true);
    }

    
    @Column(name="STORE_NAME")
    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
        setDirty(true);
    }

    
    @Column(name="DOMAIN")
    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
        setDirty(true);
    }

    
    @Column(name="DATASOURCE")
    public String getDatasource() {
        return this.datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
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


