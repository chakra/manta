package com.espendwise.manta.web.forms;

import java.math.BigDecimal;
import java.util.Map;


public class BudgetDisplayForm {

    private Long budgetId;
    private String budgetType;
    private String thresholdPercent;
    private Map<Integer, BudgetPeriodAmountForm> periodAmount;
    private CostCenterBudgetSpentForm costCenterBudgetSpentForm;
    private String budgetTotal;

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public String getThresholdPercent() {
        return thresholdPercent;
    }

    public void setThresholdPercent(String thresholdPercent) {
        this.thresholdPercent = thresholdPercent;
    }

    public Map<Integer, BudgetPeriodAmountForm> getPeriodAmount() {
        return periodAmount;
    }

    public void setPeriodAmount(Map<Integer, BudgetPeriodAmountForm> periodAmount) {
        this.periodAmount = periodAmount;
    }

    public CostCenterBudgetSpentForm getCostCenterBudgetSpentForm() {
        return costCenterBudgetSpentForm;
    }

    public void setCostCenterBudgetSpentForm(CostCenterBudgetSpentForm costCenterBudgetSpentForm) {
        this.costCenterBudgetSpentForm = costCenterBudgetSpentForm;
    }

    public void setBudgetTotal(String budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    public String getBudgetTotal() {
        return budgetTotal;
    }


}
