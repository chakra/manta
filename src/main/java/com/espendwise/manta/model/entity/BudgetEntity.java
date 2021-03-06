package com.espendwise.manta.model.entity;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
import com.espendwise.manta.model.data.BudgetData;
import com.espendwise.manta.model.data.BudgetDetailData;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * BudgetEntity generated by hbm2java
*/
@Entity
@Table(name="CLW_BUDGET")
public class BudgetEntity extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String BUDGET_ID = "budgetId";
    public static final String DETAILS = "details";
    public static final String BUDGET = "budget";

    private Long budgetId;
    private Collection<BudgetDetailData> details = new ArrayList<BudgetDetailData>(0);
    private BudgetData budget;

    public BudgetEntity() {
    }
	
    public BudgetEntity(Long budgetId) {
        this.setBudgetId(budgetId);
    }

    public BudgetEntity(Long budgetId, Collection<BudgetDetailData> details, BudgetData budget) {
        this.setBudgetId(budgetId);
        this.setDetails(details);
        this.setBudget(budget);
    }

    @Id      
    @Column(name="BUDGET_ID", nullable=false)
    public Long getBudgetId() {
        return this.budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
        setDirty(true);
    }

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="BUDGET_ID", updatable=false, columnDefinition="number")
    public Collection<BudgetDetailData> getDetails() {
        return this.details;
    }

    public void setDetails(Collection<BudgetDetailData> details) {
        this.details = details;
        setDirty(true);
    }

    @ManyToOne(fetch=FetchType.EAGER)    @JoinColumn(name="BUDGET_ID", unique=true, insertable=false, updatable=false)
    public BudgetData getBudget() {
        return this.budget;
    }

    public void setBudget(BudgetData budget) {
        this.budget = budget;
        setDirty(true);
    }




}


