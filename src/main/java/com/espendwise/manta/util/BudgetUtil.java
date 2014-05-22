package com.espendwise.manta.util;


import com.espendwise.manta.model.data.BudgetDetailData;
import com.espendwise.manta.model.entity.BudgetEntity;
import com.espendwise.manta.model.view.BudgetSpendView;
import com.espendwise.manta.model.view.CostCenterBudgetView;
import com.espendwise.manta.model.view.SiteBudgetYearView;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetUtil {


    public static  BigDecimal evaluateABudgetPeriod(String accountBudgetAccrualCd,
                                             int currentBudgetPeriod,
                                             int evaluatingBudgetPeriod,
                                             BigDecimal startAmt,
                                             BigDecimal periodAmt) {

        if (RefCodeNames.BUDGET_ACCRUAL_TYPE_CD.BY_PERIOD.equals(accountBudgetAccrualCd)) {
            if (currentBudgetPeriod == evaluatingBudgetPeriod) {
                return Utility.addAmt(startAmt, periodAmt);
            }
        } else {
            if (currentBudgetPeriod >= evaluatingBudgetPeriod) {
                return Utility.addAmt(startAmt, periodAmt);
            }
        }

        return startAmt;
    }


    public static boolean isUsedSiteBudgetThreshold(String pAllowBudgetThreshold, String pThresholdType) {
        return isUsedSiteBudgetThreshold(Utility.isTrue(pAllowBudgetThreshold), pThresholdType);
    }

    public static boolean isUsedSiteBudgetThreshold(boolean pAllowBudgetThreshold, String pThresholdType) {
        return pAllowBudgetThreshold && RefCodeNames.BUDGET_THRESHOLD_TYPE.SITE_BUDGET_THRESHOLD.equals(pThresholdType);
    }

    public static boolean isUsedAccountBudgetThreshold(boolean pAllowBudgetThreshold, String pThresholdType) {
        return pAllowBudgetThreshold && RefCodeNames.BUDGET_THRESHOLD_TYPE.ACCOUNT_BUDGET_THRESHOLD.equals(pThresholdType);
    }

    public static boolean isUsedAccountBudgetThreshold(String pAllowBudgetThreshold, String pThresholdType) {
        return isUsedSiteBudgetThreshold(Utility.isTrue(pAllowBudgetThreshold), pThresholdType);
    }


    public static BudgetSpendView findBudgetSpent(List<BudgetSpendView> budgetSpends, Long budgetId) {
        if (Utility.isSet(budgetSpends)) {
            for (BudgetSpendView x : budgetSpends) {
                if (Utility.longNN(budgetId) == Utility.longNN(x.getBudgetId())) {
                    return x;
                }
            }
        }
        return null;
    }

    public static Map<Integer, BudgetDetailData> toMapByPeriod(Collection<BudgetDetailData> budgetDetails) {


        Map<Integer, BudgetDetailData> x = new HashMap<Integer, BudgetDetailData>();

        if (budgetDetails == null) {
            return x;
        }

        for (BudgetDetailData obj : budgetDetails) {
               x.put(obj.getPeriod(), obj);
        }

        return x;
    }

    public static BigDecimal calculateBudgetTotal(BudgetEntity budget) {
        BigDecimal amount = null; // unlimited
        if (Utility.isSet(budget.getDetails())) {
            for (BudgetDetailData period : budget.getDetails()) {
                amount = Utility.addAmtNullable(period.getAmount(), amount);
            }
        } else {
            amount = new BigDecimal(0);
        }
        return amount;
    }

    public static boolean hasSiteBudget(SiteBudgetYearView budget) {
        if(budget!=null){
            if(Utility.isSet(budget.getCostCenterBudgets())){
                for(CostCenterBudgetView ccbudget:budget.getCostCenterBudgets()) {
                    if(ccbudget!=null && ccbudget.getSiteBudget()!=null){
                        return true;
                    }
                }

            }
        }
        return false;
    }
}
