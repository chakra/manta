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
 * BusEntityFullEntity generated by hbm2java
*/
@Entity
@Table(name="CLW_BUS_ENTITY")
public interface BusEntityFullEntity  {

    public static final String BUS_ENTITY_ID = "busEntityId";
    public static final String SHORT_DESC = "shortDesc";
    public static final String LONG_DESC = "longDesc";
    public static final String EFF_DATE = "effDate";
    public static final String EXP_DATE = "expDate";
    public static final String BUS_ENTITY_TYPE_CD = "busEntityTypeCd";
    public static final String BUS_ENTITY_STATUS_CD = "busEntityStatusCd";
    public static final String WORKFLOW_ROLE_CD = "workflowRoleCd";
    public static final String LOCALE_CD = "localeCd";
    public static final String ERP_NUM = "erpNum";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String TIME_ZONE_CD = "timeZoneCd";
    public static final String PROPERTIES = "properties";
    public static final String SITE_LEDGERS = "siteLedgers";
    public static final String CONTENTS = "contents";
    public static final String WO_WORKFLOW_ST_QUEUES = "woWorkflowStQueues";
    public static final String WO_SERVICE_TYPE_CATEGORIES = "woServiceTypeCategories";
    public static final String PHONES = "phones";
    public static final String EMAILS = "emails";
    public static final String WO_SITE_WORKFLOWS = "woSiteWorkflows";
    public static final String WORKFLOW_QUEUES = "workflowQueues";
    public static final String BUDGETS = "budgets";
    public static final String BUS_ENTITY_ASSOCS_FOR_BUS_ENTITY2 = "busEntityAssocsForBusEntity2Id";
    public static final String STORE_MESSAGE_ASSOCS = "storeMessageAssocs";
    public static final String SCHEDULES = "schedules";
    public static final String WO_SERVICE_TYPES = "woServiceTypes";
    public static final String BUS_ENTITY_ASSOCS_FOR_BUS_ENTITY1 = "busEntityAssocsForBusEntity1Id";
    public static final String CATALOG_STRUCTURES = "catalogStructures";
    public static final String WORKFLOWS = "workflows";
    public static final String WO_WORKFLOWS = "woWorkflows";
    public static final String CATALOG_ASSOCS = "catalogAssocs";
    public static final String USER_ASSOCS = "userAssocs";
    public static final String SITE_WORKFLOWS = "siteWorkflows";
    public static final String COST_CENTERS = "costCenters";
    public static final String ADDRESSES = "addresses";
    public static final String TEMPLATES = "templates";


    public void setBusEntityId(Long busEntityId);
    @SequenceGenerator(name="generator", sequenceName="CLW_BUS_ENTITY_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="BUS_ENTITY_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getBusEntityId();

    public void setShortDesc(String shortDesc);
    
    @Column(name="SHORT_DESC", nullable=false)
    public String getShortDesc();

    public void setLongDesc(String longDesc);
    
    @Column(name="LONG_DESC")
    public String getLongDesc();

    public void setEffDate(Date effDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EFF_DATE", length=7)
    public Date getEffDate();

    public void setExpDate(Date expDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXP_DATE", length=7)
    public Date getExpDate();

    public void setBusEntityTypeCd(String busEntityTypeCd);
    
    @Column(name="BUS_ENTITY_TYPE_CD", nullable=false, length=30)
    public String getBusEntityTypeCd();

    public void setBusEntityStatusCd(String busEntityStatusCd);
    
    @Column(name="BUS_ENTITY_STATUS_CD", nullable=false, length=30)
    public String getBusEntityStatusCd();

    public void setWorkflowRoleCd(String workflowRoleCd);
    
    @Column(name="WORKFLOW_ROLE_CD", nullable=false, length=30)
    public String getWorkflowRoleCd();

    public void setLocaleCd(String localeCd);
    
    @Column(name="LOCALE_CD", nullable=false, length=30)
    public String getLocaleCd();

    public void setErpNum(String erpNum);
    
    @Column(name="ERP_NUM", length=30)
    public String getErpNum();

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

    public void setTimeZoneCd(String timeZoneCd);
    
    @Column(name="TIME_ZONE_CD", length=30)
    public String getTimeZoneCd();

    public void setProperties(Set<PropertyFullEntity> properties);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<PropertyFullEntity> getProperties();

    public void setSiteLedgers(Set<SiteLedgerFullEntity> siteLedgers);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="siteId")
    public Set<SiteLedgerFullEntity> getSiteLedgers();

    public void setContents(Set<ContentFullEntity> contents);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<ContentFullEntity> getContents();

    public void setWoWorkflowStQueues(Set<WoWorkflowStQueueFullEntity> woWorkflowStQueues);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<WoWorkflowStQueueFullEntity> getWoWorkflowStQueues();

    public void setWoServiceTypeCategories(Set<WoServiceTypeCategoryFullEntity> woServiceTypeCategories);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="storeId")
    public Set<WoServiceTypeCategoryFullEntity> getWoServiceTypeCategories();

    public void setPhones(Set<PhoneFullEntity> phones);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<PhoneFullEntity> getPhones();

    public void setEmails(Set<EmailFullEntity> emails);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<EmailFullEntity> getEmails();

    public void setWoSiteWorkflows(Set<WoSiteWorkflowFullEntity> woSiteWorkflows);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="siteId")
    public Set<WoSiteWorkflowFullEntity> getWoSiteWorkflows();

    public void setWorkflowQueues(Set<WorkflowQueueFullEntity> workflowQueues);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<WorkflowQueueFullEntity> getWorkflowQueues();

    public void setBudgets(Set<BudgetFullEntity> budgets);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<BudgetFullEntity> getBudgets();

    public void setBusEntityAssocsForBusEntity2Id(Set<BusEntityAssocFullEntity> busEntityAssocsForBusEntity2Id);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntity2Id")
    public Set<BusEntityAssocFullEntity> getBusEntityAssocsForBusEntity2Id();

    public void setStoreMessageAssocs(Set<StoreMessageAssocFullEntity> storeMessageAssocs);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<StoreMessageAssocFullEntity> getStoreMessageAssocs();

    public void setSchedules(Set<ScheduleFullEntity> schedules);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<ScheduleFullEntity> getSchedules();

    public void setWoServiceTypes(Set<WoServiceTypeFullEntity> woServiceTypes);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="storeId")
    public Set<WoServiceTypeFullEntity> getWoServiceTypes();

    public void setBusEntityAssocsForBusEntity1Id(Set<BusEntityAssocFullEntity> busEntityAssocsForBusEntity1Id);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntity1Id")
    public Set<BusEntityAssocFullEntity> getBusEntityAssocsForBusEntity1Id();

    public void setCatalogStructures(Set<CatalogStructureFullEntity> catalogStructures);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<CatalogStructureFullEntity> getCatalogStructures();

    public void setWorkflows(Set<WorkflowFullEntity> workflows);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<WorkflowFullEntity> getWorkflows();

    public void setWoWorkflows(Set<WoWorkflowFullEntity> woWorkflows);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<WoWorkflowFullEntity> getWoWorkflows();

    public void setCatalogAssocs(Set<CatalogAssocFullEntity> catalogAssocs);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<CatalogAssocFullEntity> getCatalogAssocs();

    public void setUserAssocs(Set<UserAssocFullEntity> userAssocs);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<UserAssocFullEntity> getUserAssocs();

    public void setSiteWorkflows(Set<SiteWorkflowFullEntity> siteWorkflows);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="siteId")
    public Set<SiteWorkflowFullEntity> getSiteWorkflows();

    public void setCostCenters(Set<CostCenterFullEntity> costCenters);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="storeId")
    public Set<CostCenterFullEntity> getCostCenters();

    public void setAddresses(Set<AddressFullEntity> addresses);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<AddressFullEntity> getAddresses();

    public void setTemplates(Set<TemplateFullEntity> templates);
    @OneToMany(fetch=FetchType.LAZY, mappedBy="busEntityId")
    public Set<TemplateFullEntity> getTemplates();

}

