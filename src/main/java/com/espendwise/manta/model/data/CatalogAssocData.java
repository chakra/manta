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
 * CatalogAssocData generated by hbm2java
*/
@Entity
@Table(name="CLW_CATALOG_ASSOC")
public class CatalogAssocData extends ValueObject implements TableObject,java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String CATALOG_ASSOC_ID = "catalogAssocId";
    public static final String CATALOG_ID = "catalogId";
    public static final String BUS_ENTITY_ID = "busEntityId";
    public static final String CATALOG_ASSOC_CD = "catalogAssocCd";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String NEW_CATALOG_ID = "newCatalogId";

    private Long catalogAssocId;
    private Long catalogId;
    private Long busEntityId;
    private String catalogAssocCd;
    private Date addDate;
    private String addBy;
    private Date modDate;
    private String modBy;
    private Long newCatalogId;

    public CatalogAssocData() {
    }
	
    public CatalogAssocData(Long catalogId, Long busEntityId, String catalogAssocCd, Date addDate, Date modDate) {
        this.setCatalogId(catalogId);
        this.setBusEntityId(busEntityId);
        this.setCatalogAssocCd(catalogAssocCd);
        this.setAddDate(addDate);
        this.setModDate(modDate);
    }

    public CatalogAssocData(Long catalogId, Long busEntityId, String catalogAssocCd, Date addDate, String addBy, Date modDate, String modBy, Long newCatalogId) {
        this.setCatalogId(catalogId);
        this.setBusEntityId(busEntityId);
        this.setCatalogAssocCd(catalogAssocCd);
        this.setAddDate(addDate);
        this.setAddBy(addBy);
        this.setModDate(modDate);
        this.setModBy(modBy);
        this.setNewCatalogId(newCatalogId);
    }

    @SequenceGenerator(name="generator", sequenceName="CLW_CATALOG_ASSOC_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="CATALOG_ASSOC_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getCatalogAssocId() {
        return this.catalogAssocId;
    }

    public void setCatalogAssocId(Long catalogAssocId) {
        this.catalogAssocId = catalogAssocId;
        setDirty(true);
    }

    
    @Column(name="CATALOG_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getCatalogId() {
        return this.catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
        setDirty(true);
    }

    
    @Column(name="BUS_ENTITY_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getBusEntityId() {
        return this.busEntityId;
    }

    public void setBusEntityId(Long busEntityId) {
        this.busEntityId = busEntityId;
        setDirty(true);
    }

    
    @Column(name="CATALOG_ASSOC_CD", nullable=false, length=30)
    public String getCatalogAssocCd() {
        return this.catalogAssocCd;
    }

    public void setCatalogAssocCd(String catalogAssocCd) {
        this.catalogAssocCd = catalogAssocCd;
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

    
    @Column(name="NEW_CATALOG_ID", columnDefinition="number", precision=38, scale=0)
    public Long getNewCatalogId() {
        return this.newCatalogId;
    }

    public void setNewCatalogId(Long newCatalogId) {
        this.newCatalogId = newCatalogId;
        setDirty(true);
    }




}


