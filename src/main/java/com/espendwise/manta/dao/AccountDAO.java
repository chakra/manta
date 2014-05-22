package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.CostCenterData;
import com.espendwise.manta.model.view.AccountIdentView;
import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.BudgetView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.util.criteria.AccountListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;

import java.util.List;

public interface AccountDAO {

    public List<BusEntityData> findAccounts(StoreAccountCriteria criteria);

    public List<AccountListView> findAccountsByCriteria(AccountListViewCriteria criteria);

    public AccountIdentView findAccountToEdit(Long storeId, Long accountId);

    public AccountIdentView findAccountToEdit(Long accountId);

    public AccountIdentView findAccountToEdit(Long storeId, Long userId,  Long accountId);

    public EntityHeaderView findAccountHeader(Long accountId);

    public AccountIdentView saveAccountIdent(Long storeId, AccountIdentView accountView) throws DatabaseUpdateException;

    public List<Long> findAccountIdsByStore(Long busEntityId);

    public List<CostCenterData> findCatalogCostCenters(Long accountId) throws IllegalDataStateException;

    public CatalogData findCatalog(Long accountId) throws IllegalDataStateException;
    
    public BusEntityData findAccountById(Long accountId);
    
    public Boolean isBudgetExistsForAccount(Long accountId, Integer fiscalYear);
    
    public List<BudgetView> findAccountSiteBudgets(Long accountId, Integer fiscalYear);
    
    public List<Long> findAccountByDistRefNumberAndStoreId(String distAcctRefNumber, Long storeId);
}
