package com.espendwise.manta.service;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.FiscalCalenderData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.AccountListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;

import java.util.Date;
import java.util.List;

public interface AccountService {

    public List<BusEntityData> findStoreAccountBusDatas(StoreAccountCriteria criteria);

    public List<AccountListView> findAccountsByCriteria(AccountListViewCriteria criteria);

    public AccountIdentView findAccountToEdit(Long storeId, Long userId, Long accountId);

    public EntityHeaderView findAccountHeader(Long accountId);

    public AccountIdentView saveAccountIdent(Long storeId, Long userId, AccountIdentView accountView) throws DatabaseUpdateException;

    public List<FiscalCalendarListView> findFiscalCalendars(Long accountId);

    public FiscalCalendarIdentView findCalendarIdent(Long accountId, Long fiscalCalendarId);

    public FiscalCalendarIdentView saveFiscalCalendar(FiscalCalendarIdentView calendar);
    
    public FiscalCalendarIdentView saveFiscalCalendarClone(FiscalCalendarIdentView calendar);

    public List<GroupData> findAccountGroups(Long storeId);

    public FiscalCalendarPhysicalView findCalendarForDate(Long accountId, Date forDate);
    
    public ContentManagementView findContentManagement(Long accountId);
    
    public ContentManagementView saveAccountContentManagement(Long accountId, ContentManagementView content) throws DatabaseUpdateException;

    public boolean isServiceScheduleCalendar(Long accountId);

    public CatalogData findAccountCatalog (Long accountId) throws IllegalDataStateException;
    
    public FiscalCalenderData findCurrentFiscalCalendar (Long accountId);
    
    public Boolean isBudgetExistsForAccount(Long accountId, Integer fiscalYear);
    
    public List<BudgetView> findAccountSiteBudgets(Long accountId, Integer fiscalYear);
    
    public List<BudgetView> updateAccountSiteBudgets(List<BudgetView> budgets);
 }