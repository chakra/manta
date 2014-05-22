package com.espendwise.manta.web.forms;


import java.io.Serializable;
import java.util.Map;

public class BudgetPeriodSpentForm implements Serializable  {

    private Integer budgetPeriod;
    private Integer budgetYear;
    private Map<Long, Map<Long, CostCenterBudgetSpentForm>> costCenterBudgetSpent;


    public BudgetPeriodSpentForm(Integer budgetYear, Integer budgetPeriod, Map<Long, Map<Long, CostCenterBudgetSpentForm>> costCenterBudgetSpent) {
        this.budgetYear = budgetYear;
        this.budgetPeriod = budgetPeriod;
        this.costCenterBudgetSpent = costCenterBudgetSpent;
    }

    public Integer getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(Integer budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public Integer getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }

    public Map<Long, Map<Long, CostCenterBudgetSpentForm>> getCostCenterBudgetSpent() {
        return costCenterBudgetSpent;
    }

    public void setCostCenterBudgetSpent(Map<Long, Map<Long, CostCenterBudgetSpentForm>> costCenterBudgetSpent) {
        this.costCenterBudgetSpent = costCenterBudgetSpent;
    }
}
