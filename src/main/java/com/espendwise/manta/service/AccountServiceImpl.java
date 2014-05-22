package com.espendwise.manta.service;


import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.FiscalCalenderData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.AccountListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.AccountUniqueConstraint;
import com.espendwise.manta.util.validation.rules.FiscalCalendarDependanciesRule;
import com.espendwise.manta.util.validation.rules.FiscalCalendarUpdateConstraint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AccountServiceImpl extends DataAccessService implements AccountService {

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findStoreAccountBusDatas(StoreAccountCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        AccountDAO accountDao = new AccountDAOImpl(entityManager);

        return accountDao.findAccounts(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<AccountListView> findAccountsByCriteria(AccountListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        AccountDAO accountDao = new AccountDAOImpl(entityManager);

        return accountDao.findAccountsByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public AccountIdentView findAccountToEdit(Long storeId, Long userId, Long accountId) {

        EntityManager entityManager = getEntityManager();
        AccountDAO accountDao = new AccountDAOImpl(entityManager);

        return accountDao.findAccountToEdit(storeId, userId, accountId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<GroupData> findAccountGroups(Long storeId) {

        EntityManager entityManager = getEntityManager();
        GroupDAO groupDao = new GroupDAOImpl(entityManager);

        return groupDao.findAccountsGroups(storeId, Utility.toList(RefCodeNames.GROUP_STATUS_CD.ACTIVE));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EntityHeaderView findAccountHeader(Long accountId) {

        EntityManager entityManager = getEntityManager();
        AccountDAO accountDao = new AccountDAOImpl(entityManager);

        return accountDao.findAccountHeader(accountId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public AccountIdentView saveAccountIdent(Long storeId, Long userId, AccountIdentView accountView) throws DatabaseUpdateException {

        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new AccountUniqueConstraint(storeId, accountView.getBusEntityData()));

        validation.validate();

        EntityManager entityManager = getEntityManager();
        AccountDAO accountDao = new AccountDAOImpl(entityManager);

        return accountDao.saveAccountIdent(storeId, accountView);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<FiscalCalendarListView> findFiscalCalendars(Long accountId) {

        EntityManager entityManager = getEntityManager();

        FiscalCalendarDAO fiscalCalendarDao = new FiscalCalendarDAOImpl(entityManager);
        return fiscalCalendarDao.findFiscalCalenders(accountId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public FiscalCalendarIdentView findCalendarIdent(Long accountId, Long fiscalCalendarId) {

        EntityManager entityManager = getEntityManager();

        FiscalCalendarDAO fiscalCalendarDao = new FiscalCalendarDAOImpl(entityManager);
        return fiscalCalendarDao.findCalendarIdent(accountId, fiscalCalendarId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public FiscalCalendarPhysicalView findCalendarForDate(Long accountId, Date forDate) {

        EntityManager entityManager = getEntityManager();

        FiscalCalendarDAO fiscalCalendarDao = new FiscalCalendarDAOImpl(entityManager);
        return fiscalCalendarDao.findFiscalCalenderForDate(accountId, null, forDate);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public FiscalCalendarIdentView saveFiscalCalendarClone(FiscalCalendarIdentView calendar) {
    	
    	EntityManager entityManager = getEntityManager();
    	
        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new FiscalCalendarUpdateConstraint(calendar,entityManager));
        validation.addRule(new FiscalCalendarDependanciesRule(calendar));        

        FiscalCalendarDAO fiscalCalendarDao = new FiscalCalendarDAOImpl(entityManager);
        return fiscalCalendarDao.saveFiscalCalendarClone(calendar);
    }    

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public FiscalCalendarIdentView saveFiscalCalendar(FiscalCalendarIdentView calendar) {

        EntityManager entityManager = getEntityManager();
        
        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new FiscalCalendarUpdateConstraint(calendar,entityManager));
        validation.addRule(new FiscalCalendarDependanciesRule(calendar));

        validation.validate();

        FiscalCalendarDAO fiscalCalendarDao = new FiscalCalendarDAOImpl(entityManager);
        return fiscalCalendarDao.saveFiscalCalendar(calendar);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ContentManagementView findContentManagement(Long accountId) {

        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);
 
        ContentManagementView content = propertyDao.findContentManagementProperties(accountId);
        content.setBusEntityId(accountId);
        
        return content;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ContentManagementView saveAccountContentManagement(Long accountId, ContentManagementView content) throws DatabaseUpdateException {

        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        return propertyDao.saveAccountContentManagement(accountId, content);

    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public boolean isServiceScheduleCalendar(Long accountId) {

        EntityManager entityManager = getEntityManager();

        FiscalCalendarDAO fiscalCalendarDao = new FiscalCalendarDAOImpl(entityManager);
        return fiscalCalendarDao.isServiceScheduleCalendar(accountId);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public CatalogData findAccountCatalog (Long accountId) throws IllegalDataStateException {
        EntityManager entityManager = getEntityManager();
        AccountDAO accountDao = new AccountDAOImpl(entityManager);

        return accountDao.findCatalog(accountId);
    	
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public FiscalCalenderData findCurrentFiscalCalendar (Long accountId) {
        EntityManager entityManager = getEntityManager();
        FiscalCalendarDAO fiscalCalendarDao = new FiscalCalendarDAOImpl(entityManager);

        return fiscalCalendarDao.findCurrentFiscalCalendar(accountId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Boolean isBudgetExistsForAccount(Long accountId, Integer fiscalYear) {
        EntityManager entityManager = getEntityManager();
        AccountDAO accountDao = new AccountDAOImpl(entityManager);

        return accountDao.isBudgetExistsForAccount(accountId, fiscalYear);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BudgetView> findAccountSiteBudgets(Long accountId, Integer fiscalYear) {
        EntityManager entityManager = getEntityManager();
        AccountDAO accountDao = new AccountDAOImpl(entityManager);

        return accountDao.findAccountSiteBudgets(accountId, fiscalYear);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<BudgetView> updateAccountSiteBudgets(List<BudgetView> budgets) {
        EntityManager entityManager = getEntityManager();
        BudgetDAO budgetDao = new BudgetDAOImpl(entityManager);

        return budgetDao.updateBudgets(budgets);
    }

}
