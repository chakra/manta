package com.espendwise.manta.dao;


import com.espendwise.manta.model.view.BudgetSiteSpendView;
import com.espendwise.manta.model.view.BudgetView;
import com.espendwise.manta.model.view.CostCenterBudgetView;
import com.espendwise.manta.model.view.FiscalCalendarPhysicalView;
import com.espendwise.manta.service.IllegalDataStateException;

import java.util.Collection;
import java.util.List;

public interface BudgetDAO {

    public List<CostCenterBudgetView> findBudgetsForSite(Long accountId, Long siteId, Integer budgetYear) throws IllegalDataStateException;

    public BudgetSiteSpendView calculateBudgetSpentForSite(Long accountId,  Long siteId,  CostCenterBudgetView costCenterBudget, FiscalCalendarPhysicalView calendarPhysical, Integer budgetPeriod);

    public BudgetView updateBudget(BudgetView budget);

    public List<BudgetView> updateBudgets(List<BudgetView> budgets);

    public List<BudgetView> findBudgets(Long entityId,  Collection<Long> budgetIds, String budgetType);
}
