package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.SiteBudgetFormUtil;
import com.espendwise.manta.web.validator.SiteBudgetFormValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Validation(SiteBudgetFormValidator.class)
public class SiteBudgetForm  extends WebForm implements Initializable {




    public static interface ACTION {
        public static String SAVE_CHANGES = "/saveChanges";
        public static String SET_UNLIMITED = "/setUnlimited";
        public static String COPY_LAST_YEAR = "/copyLastYear";
    }


    private boolean init;
    private String selectedAction;
    private Integer year;

    private String currentPeriod;
    private boolean enableCopyBudgets = false;
    private Map<Long, CostCenterBudgetDisplayForm> costCenterBudget;
    private SortedMap<Integer, Integer> fiscalYears;
    private SortedMap<Integer, Integer> fiscalYearPeriods;
    private boolean usedSiteBudgetThreshold;


    @Override
    public void initialize() {
        this.selectedAction = ACTION.SAVE_CHANGES;
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public void setUsedSiteBudgetThreshold(boolean usedSiteBudgetThreshold) {
        this.usedSiteBudgetThreshold = usedSiteBudgetThreshold;
    }

    public boolean getUsedSiteBudgetThreshold() {
        return usedSiteBudgetThreshold;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public Integer getYear() {
        return year;
    }

    public void setEnableCopyBudgets(boolean enableCopyBudgets) {
        this.enableCopyBudgets = enableCopyBudgets;
    }

    public boolean getEnableCopyBudgets() {
        return enableCopyBudgets;
    }

    public Map<Long, CostCenterBudgetDisplayForm> getCostCenterBudget() {
        return costCenterBudget;
    }

    public void setCostCenterBudget(Map<Long, CostCenterBudgetDisplayForm> costCenterBudget) {
        this.costCenterBudget = costCenterBudget;
    }

    public String getSelectedAction() {
        return isEditable() ? selectedAction : null;
    }

    public String getSelectedActionValue() {
        return  selectedAction;
    }

    public void setSelectedAction(String selectedSelectedAction) {
        this.selectedAction = selectedSelectedAction;
    }

    public SortedMap<Integer, Integer> getFiscalYears() {
        return fiscalYears;
    }

    public void setFiscalYears(SortedMap<Integer, Integer> fiscalYears) {
        this.fiscalYears = fiscalYears;
    }

    public void setFiscalYearPeriods(SortedMap<Integer, Integer> fiscalYearPeriods) {
        this.fiscalYearPeriods = fiscalYearPeriods;
    }


    public SortedMap<Integer, Integer> getFiscalYearPeriods() {
        return fiscalYearPeriods;
    }

    public boolean getUnlimitedBudget() {
        return SiteBudgetFormUtil.isUnlimited(getCostCenterBudget());
    }

    public BigDecimal getBudgetsTotal() {
        return SiteBudgetFormUtil.getBudgetsTotal(getCostCenterBudget());
    }

    public List<Long> getDisplayCostCenters() {
        return SiteBudgetFormUtil.getSortedByNameCostCenterIds(getCostCenterBudget());
    }

    public boolean isEditable() {
        return SiteBudgetFormUtil.isEditable(getCostCenterBudget());
    }
}
