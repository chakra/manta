package com.espendwise.manta.service;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ContractData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreSiteCriteria;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface SiteService {

    public List<SiteListView> findSitesByCriteria(SiteListViewCriteria criteria);

    public SiteHeaderView findSiteHeader(Long storeId, Long siteId);

    public SiteIdentView findSiteToEdit(Long storeId, Long siteId);

    public SiteIdentView saveSiteIdent(Long storeId, Long userId, SiteIdentView siteView, Long createAssocFrom) throws DatabaseUpdateException, IllegalDataStateException;

    public SiteIdentView saveSiteIdent(Long storeId, Long userId, SiteIdentView siteView) throws ValidationException, DatabaseUpdateException, IllegalDataStateException;

    public void deleteSite(Long storeId, Long siteId) throws DatabaseUpdateException;

    public void deleteSiteIdent(Long storeId, Long siteId);

    public List<SiteAccountView> findSites(StoreSiteCriteria criteria);

    public SortedMap<Integer, SiteBudgetYearView> findBudgetForSiteByYear(SiteHeaderView site, List<FiscalCalendarListView> calendars, List<Integer> budgetsForYears, Date date) throws IllegalDataStateException;

    public Map<Long, BudgetSiteSpendView> calculateCostCentersBudgetSpentsForSite(SiteBudgetYearView budgetYearView, Integer period);

    public List<BudgetView> updateSiteBudgets(Long storeId, Long siteId, List<BudgetView> budgets) throws ValidationException;

    public List<BudgetView> findSiteBudgets(Long siteId, List<Long> budgetIds);

    boolean isUsedSiteBudgetThreshold(Long storeId, Long accountId, Long siteId);
    
    public void configureSiteUsersList(Long siteId, Long storeId, List<UserListView> selected, List<UserListView> deselected);
    
    public void configureSiteCatalog(Long siteId, Long catalogId);
    
    public BusEntityAssocData findSiteAccountAssoc(Long siteId);
    
    public List<ContractData> findSiteContract(Long siteId);
    
    public List<BusEntityData> findSiteAccount(Long siteId);

}
