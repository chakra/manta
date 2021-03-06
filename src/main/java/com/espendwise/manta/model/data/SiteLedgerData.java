package com.espendwise.manta.model.data;
// Generated by Hibernate Tools


import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.ValueObject;
import java.math.BigDecimal;
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
 * SiteLedgerData generated by hbm2java
*/
@Entity
@Table(name="CLW_SITE_LEDGER")
public class SiteLedgerData extends ValueObject implements TableObject,java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String SITE_LEDGER_ID = "siteLedgerId";
    public static final String ORDER_ID = "orderId";
    public static final String SITE_ID = "siteId";
    public static final String COST_CENTER_ID = "costCenterId";
    public static final String AMOUNT = "amount";
    public static final String ENTRY_TYPE_CD = "entryTypeCd";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String BUDGET_YEAR = "budgetYear";
    public static final String BUDGET_PERIOD = "budgetPeriod";
    public static final String FISCAL_CALENDER_ID = "fiscalCalenderId";
    public static final String COMMENTS = "comments";
    public static final String WORK_ORDER_ID = "workOrderId";

    private Long siteLedgerId;
    private Long orderId;
    private Long siteId;
    private Long costCenterId;
    private BigDecimal amount;
    private String entryTypeCd;
    private Date addDate;
    private String addBy;
    private Date modDate;
    private String modBy;
    private Integer budgetYear;
    private int budgetPeriod;
    private Long fiscalCalenderId;
    private String comments;
    private Long workOrderId;

    public SiteLedgerData() {
    }
	
    public SiteLedgerData(Long siteId, Long costCenterId, BigDecimal amount, String entryTypeCd, Date addDate, String addBy, Date modDate) {
        this.setSiteId(siteId);
        this.setCostCenterId(costCenterId);
        this.setAmount(amount);
        this.setEntryTypeCd(entryTypeCd);
        this.setAddDate(addDate);
        this.setAddBy(addBy);
        this.setModDate(modDate);
    }

    public SiteLedgerData(Long orderId, Long siteId, Long costCenterId, BigDecimal amount, String entryTypeCd, Date addDate, String addBy, Date modDate, String modBy, Integer budgetYear, int budgetPeriod, Long fiscalCalenderId, String comments, Long workOrderId) {
        this.setOrderId(orderId);
        this.setSiteId(siteId);
        this.setCostCenterId(costCenterId);
        this.setAmount(amount);
        this.setEntryTypeCd(entryTypeCd);
        this.setAddDate(addDate);
        this.setAddBy(addBy);
        this.setModDate(modDate);
        this.setModBy(modBy);
        this.setBudgetYear(budgetYear);
        this.setBudgetPeriod(budgetPeriod);
        this.setFiscalCalenderId(fiscalCalenderId);
        this.setComments(comments);
        this.setWorkOrderId(workOrderId);
    }

    @SequenceGenerator(name="generator", sequenceName="CLW_SITE_LEDGER_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="SITE_LEDGER_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getSiteLedgerId() {
        return this.siteLedgerId;
    }

    public void setSiteLedgerId(Long siteLedgerId) {
        this.siteLedgerId = siteLedgerId;
        setDirty(true);
    }

    
    @Column(name="ORDER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
        setDirty(true);
    }

    
    @Column(name="SITE_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
        setDirty(true);
    }

    
    @Column(name="COST_CENTER_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getCostCenterId() {
        return this.costCenterId;
    }

    public void setCostCenterId(Long costCenterId) {
        this.costCenterId = costCenterId;
        setDirty(true);
    }

    
    @Column(name="AMOUNT", nullable=false, columnDefinition="number", precision=20, scale=8)
    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        setDirty(true);
    }

    
    @Column(name="ENTRY_TYPE_CD", nullable=false, length=30)
    public String getEntryTypeCd() {
        return this.entryTypeCd;
    }

    public void setEntryTypeCd(String entryTypeCd) {
        this.entryTypeCd = entryTypeCd;
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

    
    @Column(name="ADD_BY", nullable=false)
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

    
    @Column(name="BUDGET_YEAR", columnDefinition="number", precision=5, scale=0)
    public Integer getBudgetYear() {
        return this.budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
        setDirty(true);
    }

    
    @Column(name="BUDGET_PERIOD", columnDefinition="number", precision=3, scale=0)
    public int getBudgetPeriod() {
        return this.budgetPeriod;
    }

    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
        setDirty(true);
    }

    
    @Column(name="FISCAL_CALENDER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getFiscalCalenderId() {
        return this.fiscalCalenderId;
    }

    public void setFiscalCalenderId(Long fiscalCalenderId) {
        this.fiscalCalenderId = fiscalCalenderId;
        setDirty(true);
    }

    
    @Column(name="COMMENTS", length=100)
    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
        setDirty(true);
    }

    
    @Column(name="WORK_ORDER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getWorkOrderId() {
        return this.workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
        setDirty(true);
    }




}


