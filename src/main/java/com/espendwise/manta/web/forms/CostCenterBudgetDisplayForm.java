package com.espendwise.manta.web.forms;


import com.espendwise.manta.util.Utility;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

public class CostCenterBudgetDisplayForm implements Serializable {

    private Long costCenterId;
    private String costCenterName;
    private String costCenterType;

    private Map<String, BudgetDisplayForm> budgets;
    public static final Comparator<CostCenterBudgetDisplayForm> COMPARE_BY_NAME = new Comparator<CostCenterBudgetDisplayForm>(){
        @Override
        public int compare(CostCenterBudgetDisplayForm o1, CostCenterBudgetDisplayForm o2) {
           return Utility.strNN(o1.getCostCenterName()).compareToIgnoreCase(Utility.strNN(o2.getCostCenterName()));
        }
    };

    public Long getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(Long costCenterId) {
        this.costCenterId = costCenterId;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public Map<String, BudgetDisplayForm> getBudgets() {
        return budgets;
    }

    public void setBudgets(Map<String, BudgetDisplayForm> budgets) {
        this.budgets = budgets;
    }

    public String getCostCenterType() {
        return costCenterType;
    }

    public void setCostCenterType(String costCenterType) {
        this.costCenterType = costCenterType;
    }
}
