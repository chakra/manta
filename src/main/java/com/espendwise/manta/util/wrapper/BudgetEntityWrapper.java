package com.espendwise.manta.util.wrapper;


import com.espendwise.manta.model.data.BudgetData;
import com.espendwise.manta.model.data.BudgetDetailData;
import com.espendwise.manta.model.entity.BudgetEntity;

import java.math.BigDecimal;

public class BudgetEntityWrapper {

    private BudgetEntity budget;

    public BudgetEntityWrapper(BudgetEntity budget) {
        this.budget = budget;
    }

    public BudgetData getBudgetData() {
        return budget.getBudget();
    }

    public BudgetEntity getBudget() {
        return budget;
    }

    public void setAmount(int pPeriod, BigDecimal pAmount) {

        if (budget.getDetails() != null) {

            boolean isSet = false;

            for (BudgetDetailData budgetDetail : budget.getDetails()) {
                if (budgetDetail.getPeriod() == pPeriod) {
                    budgetDetail.setAmount(pAmount);
                    isSet = true;
                    break;
                }
            }

            if (!isSet) {
                BudgetDetailData budgetDetail = new BudgetDetailData();
                budgetDetail.setPeriod(pPeriod);
                budgetDetail.setAmount(pAmount);
                budget.getDetails().add(budgetDetail);
            }

        }
    }

    public BigDecimal getAmount(int pPeriod) {
        BigDecimal toReturn = null;
        if (budget.getDetails() != null) {
            for (BudgetDetailData budgetDetail : budget.getDetails()) {
                if (budgetDetail.getPeriod() == pPeriod) {
                    toReturn = budgetDetail.getAmount();
                    break;
                }
            }
        }
        return toReturn;
    }


    public BigDecimal getCurrentPeriodAmount(int pPeriod) {
        BigDecimal toReturn = new BigDecimal(-1);
        if (budget.getDetails() != null) {
            for (BudgetDetailData budgetDetail : budget.getDetails()) {
                if (budgetDetail.getPeriod() == pPeriod) {
                    toReturn = budgetDetail.getAmount();
                    break;
                }
            }
        }
        return toReturn;
    }


}
