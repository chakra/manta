package com.espendwise.manta.model.fullentity;

// Generated by Hibernate Tools

import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.ValueObject;
import java.math.BigDecimal;
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
 * SiteLedgerFullEntity generated by hbm2java
*/
@Entity
@Table(name="CLW_SITE_LEDGER")
public interface SiteLedgerFullEntity  {

    public static final String SITE_LEDGER_ID = "siteLedgerId";
    public static final String ORDER = "orderId";
    public static final String SITE = "siteId";
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

    public static final String ORDER_ID = "orderId.orderId";
    public static final String SITE_ID = "siteId.busEntityId";

    public void setSiteLedgerId(Long siteLedgerId);
    @SequenceGenerator(name="generator", sequenceName="CLW_SITE_LEDGER_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="SITE_LEDGER_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getSiteLedgerId();

    public void setOrderId(OrderFullEntity orderId);
    @ManyToOne(fetch=FetchType.LAZY)    @JoinColumn(name="ORDER_ID", columnDefinition="number")
    public OrderFullEntity getOrderId();

    public void setSiteId(BusEntityFullEntity siteId);
    @ManyToOne(fetch=FetchType.LAZY)    @JoinColumn(name="SITE_ID", nullable=false, columnDefinition="number")
    public BusEntityFullEntity getSiteId();

    public void setCostCenterId(Long costCenterId);
    
    @Column(name="COST_CENTER_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getCostCenterId();

    public void setAmount(BigDecimal amount);
    
    @Column(name="AMOUNT", nullable=false, columnDefinition="number", precision=20, scale=8)
    public BigDecimal getAmount();

    public void setEntryTypeCd(String entryTypeCd);
    
    @Column(name="ENTRY_TYPE_CD", nullable=false, length=30)
    public String getEntryTypeCd();

    public void setAddDate(Date addDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ADD_DATE", nullable=false, length=7)
    public Date getAddDate();

    public void setAddBy(String addBy);
    
    @Column(name="ADD_BY", nullable=false)
    public String getAddBy();

    public void setModDate(Date modDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_DATE", nullable=false, length=7)
    public Date getModDate();

    public void setModBy(String modBy);
    
    @Column(name="MOD_BY")
    public String getModBy();

    public void setBudgetYear(Integer budgetYear);
    
    @Column(name="BUDGET_YEAR", columnDefinition="number", precision=5, scale=0)
    public Integer getBudgetYear();

    public void setBudgetPeriod(int budgetPeriod);
    
    @Column(name="BUDGET_PERIOD", columnDefinition="number", precision=3, scale=0)
    public int getBudgetPeriod();

    public void setFiscalCalenderId(Long fiscalCalenderId);
    
    @Column(name="FISCAL_CALENDER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getFiscalCalenderId();

    public void setComments(String comments);
    
    @Column(name="COMMENTS", length=100)
    public String getComments();

    public void setWorkOrderId(Long workOrderId);
    
    @Column(name="WORK_ORDER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getWorkOrderId();

}
