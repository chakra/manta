package com.espendwise.manta.dao; 

import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.entity.BudgetEntity;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.binder.PropertyBinder;
import com.espendwise.manta.util.criteria.AccountListViewCriteria;
import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.parser.Parse;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl extends DAOImpl implements AccountDAO {

    private static final Logger logger = Logger.getLogger(AccountDAOImpl.class);

    public AccountDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public BusEntityData findAccountBusData(Long accountId) {

        StoreAccountCriteria criteria = new StoreAccountCriteria();

        criteria.setName(String.valueOf(accountId));
        criteria.setFilterType(Constants.FILTER_TYPE.ID);

        List x = findAccounts(criteria);

        return !x.isEmpty() ? (BusEntityData) x.get(0) : null;

    }

    public BusEntityData findAccountBusData(Long storeId, Long accountId) {

        StoreAccountCriteria criteria = new StoreAccountCriteria();

        criteria.setStoreId(storeId);
        criteria.setName(String.valueOf(accountId));
        criteria.setFilterType(Constants.FILTER_TYPE.ID);

        List x = findAccounts(criteria);

        return !x.isEmpty() ? (BusEntityData) x.get(0) : null;
    }

    public BusEntityData findAccountBusData(Long storeId, Long userId, Long accountId) {

        StoreAccountCriteria criteria = new StoreAccountCriteria();

        criteria.setStoreId(storeId);
        criteria.setUserId(userId);
        criteria.setName(String.valueOf(accountId));
        criteria.setFilterType(Constants.FILTER_TYPE.ID);

        List x = findAccounts(criteria);

        return !x.isEmpty() ? (BusEntityData) x.get(0) : null;

    }

    @Override
    public List<Long> findAccountIdsByStore(Long storeId) {

        Query q = em.createQuery("Select account.busEntityId from BusEntityData account, BusEntityAssocData accountStore" +
                " where accountStore.busEntity1Id = account.busEntityId" +
                " and accountStore.busEntity2Id = (:storeId)" +
                " and accountStore.busEntityAssocCd =(:storeAccountAssocCd) " +
                "");


        q.setParameter("storeId", storeId);
        q.setParameter("storeAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);

        return (List<Long>) q.getResultList();

    }

    @Override
    public List<CostCenterData> findCatalogCostCenters(Long accountId) throws IllegalDataStateException {

        //if no fiscal calender return.
        List<CostCenterData> x = new ArrayList<CostCenterData>();

        FiscalCalendarDAO calendarDao = new FiscalCalendarDAOImpl(em);

        if (calendarDao.fiscalCalendarsCount(accountId) > 0) {

            CatalogData catalog = findCatalog(accountId);
            if (catalog == null) {
                logger.info("findCatalogCostCenters()=> Could not find account catalog data");
                return x;
            }

            CostCenterDAO costCenterDao = new CostCenterDAOImpl(em);
            x = costCenterDao.findCatalogCostCenters(
                    catalog.getCatalogId(),
                    RefCodeNames.COST_CENTER_ASSOC_CD.COST_CENTER_ACCOUNT_CATALOG
            );

        }

        return x;
    }

    @Override
    public CatalogData findCatalog(Long accountId) throws IllegalDataStateException {

        Query q = em.createQuery("Select catalog from CatalogData catalog, CatalogAssocData assoc " +
                "where catalog.catalogId = assoc.catalogId" +
                " and assoc.busEntityId = (:accountId) " +
                " and assoc.catalogAssocCd = (:catalogAccount)" +
                " and catalog.catalogTypeCd = (:catalogTypeAccount)" +
                " and catalog.catalogStatusCd <> (:intactive)"
        );


        q.setParameter("accountId", accountId);
        q.setParameter("catalogAccount", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT);
        q.setParameter("catalogTypeAccount", RefCodeNames.CATALOG_TYPE_CD.ACCOUNT);
        q.setParameter("intactive", RefCodeNames.CATALOG_STATUS_CD.INACTIVE);


        List<CatalogData> catalogs = q.getResultList();
        if (catalogs.size() > 1) {
            throw DatabaseError.mutipleAccountCatalogs(accountId);
        }

        return catalogs.size() == 1 ? catalogs.get(0) : null;

    }


    @Override
    public List<BusEntityData> findAccounts(StoreAccountCriteria criteria) {

        logger.info("findAccounts()=> BEGIN, criteria "+criteria);

        StringBuilder baseQuery = new StringBuilder("Select account from BusEntityData account");


        if (criteria.getStoreId() != null) {

            baseQuery
                    .append(",BusEntityAssocData accountStore")
                    .append((criteria.getUserId() != null ? ", UserAssocData userStore" : ""))
                    .append(" where account.busEntityId = accountStore.busEntity1Id")
                    .append(" and accountStore.busEntity2Id = (:storeId)")
                    .append(" and accountStore.busEntityAssocCd = (:accountStoreCd)")
                    .append((criteria.getUserId() != null ? " and userStore.busEntityId = accountStore.busEntity2Id" : ""))
                    .append((criteria.getUserId() != null ? " and userStore.userAssocCd =(:userAssocCd)" : ""))
                    .append((criteria.getUserId() != null ? " and userStore.userId = (:userId)" : ""));

        }

        if (Utility.isSet(criteria.getName())) {

            if (Constants.FILTER_TYPE.ID.equals(criteria.getFilterType())) {

                baseQuery.append(" and account.busEntityId = ").append(Parse.parseLong(criteria.getName()));

            } else if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getFilterType())) {

                baseQuery.append(" and UPPER(account.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getName().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getFilterType())) {

                baseQuery.append(" and UPPER(account.shortDesc) like '")
                        .append(QueryHelp.contains(criteria.getName().toUpperCase()))
                        .append("'");
            }
        }
        
        if (!Utility.isSet(criteria.getName()) || !Constants.FILTER_TYPE.ID.equals(criteria.getFilterType())) {
            if (criteria.getAccountId() != null && criteria.getAccountId() > 0) {
                baseQuery.append(" and account.busEntityId = ").append(criteria.getAccountId());
            }
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and account.busEntityStatusCd <> ").
                    append(QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE));
        }

        Query q = em.createQuery(baseQuery.toString());

        if (criteria.getStoreId() != null) {
            
            q.setParameter("storeId", criteria.getStoreId());
            q.setParameter("accountStoreCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
           
            if (criteria.getUserId() != null) {
                q.setParameter("userAssocCd", RefCodeNames.USER_ASSOC_CD.STORE);
                q.setParameter("userId", criteria.getUserId());
            }
        }

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        return (List<BusEntityData>) q.getResultList();
    }

    @Override
    public List<AccountListView> findAccountsByCriteria(AccountListViewCriteria criteria) {

        logger.info("findAccountsByCriteria()=> BEGIN, criteria: "+criteria);

        StringBuilder baseQuery = new StringBuilder("Select " +
                "new com.espendwise.manta.model.view.AccountListView(" +
                "   account.busEntityId," +
                "   account.shortDesc, " +
                "   type.value," +
                "   account.busEntityStatusCd," +
                "   address.address1," +
                "   address.city," +
                "   address.stateProvinceCd," +
                "   distrRefNumber.value " +
                ") " +
                " from " +
                (criteria.getAccountsGroups() != null ? "GroupAssocData groupAssoc, " : "") +
                "com.espendwise.manta.model.fullentity.BusEntityFullEntity account " +
                " left join account.properties distrRefNumber with distrRefNumber.propertyTypeCd = (:distrRefTypeProperty)" +
                " left join account.properties type with type.propertyTypeCd  = (:accountTypeProperty)" +
                " left join account.addresses address with address.addressTypeCd  = (:addressTypeProperty)" +
                " inner join account.busEntityAssocsForBusEntity1Id storeAccount" +
                " where storeAccount.busEntity2Id.busEntityId = (:storeId) and storeAccount.busEntityAssocCd = (:storeAccountAssocCd)"
        );

        if (Utility.isSet(criteria.getAccountId())) {
            baseQuery.append(" and account.busEntityId = ").append(Parse.parseLong(criteria.getAccountId()));
        }
        
        if (Utility.isSet(criteria.getAccountName())) {

        	if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getAccountNameFilterType())) {

                baseQuery.append(" and UPPER(account.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getAccountName().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getAccountNameFilterType())) {

                baseQuery.append(" and UPPER(account.shortDesc) like '")
                        .append(QueryHelp.contains(criteria.getAccountName().toUpperCase()))
                        .append("'");


            }
        }

        if (Utility.isSet(criteria.getDistrRefNumber())) {

            if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getDistrRefNumberFilterType())) {

                baseQuery.append(" and UPPER(distrRefNumber.value) like '")
                        .append(QueryHelp.startWith(criteria.getDistrRefNumber().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getDistrRefNumberFilterType())) {

                baseQuery.append(" and UPPER(distrRefNumber.value) like '%")
                        .append(QueryHelp.contains(criteria.getDistrRefNumber().toUpperCase()))
                        .append("'");


            }
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and account.busEntityStatusCd <> ")
                    .append(QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE));
        }


        if (criteria.getAccountsGroups() != null) {
            baseQuery.append(" and account.busEntityId = groupAssoc.busEntityId");
            baseQuery.append(" and groupAssoc.groupId").append(criteria.getAccountsGroups().size() == 1 ? " = " : " in").append("(:groupIds)");
        }


        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("addressTypeProperty", RefCodeNames.ADDRESS_TYPE_CD.BILLING);
        q.setParameter("accountTypeProperty", RefCodeNames.PROPERTY_TYPE_CD.ACCOUNT_TYPE);
        q.setParameter("distrRefTypeProperty", RefCodeNames.PROPERTY_TYPE_CD.DIST_ACCT_REF_NUM);
        q.setParameter("storeAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        if (criteria.getAccountsGroups() != null) {
            q.setParameter("groupIds", criteria.getAccountsGroups().size() == 1 ? criteria.getAccountsGroups().get(0) : criteria.getAccountsGroups());
        }

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        List<AccountListView> r = q.getResultList();

        logger.info("findSitesByCriteria)=> END, fetched : " + r.size() + " rows");

        return r;

    }

    @Override
    public AccountIdentView findAccountToEdit(Long storeId, Long accountId) {
        return findAccountToEdit(storeId, null, accountId);
    }

    @Override
    public AccountIdentView findAccountToEdit(Long accountId) {
        return findAccountToEdit(null, null, accountId);
    }

    @Override
    public AccountIdentView findAccountToEdit(Long storeId, Long userId,  Long accountId) {

        StoreAccountCriteria  criteria = new StoreAccountCriteria();
        criteria.setStoreId(storeId);
        criteria.setUserId(userId);
        
        criteria.setName(Long.toString(accountId));
        criteria.setFilterType(Constants.FILTER_TYPE.ID);

        List<BusEntityData> accounts = findAccounts(criteria);

        if(!accounts.isEmpty())  {
            return  pickupAccountIdentView(accounts.get(0));
        } else {
           return null;
        }

    }

    @Override
    public EntityHeaderView findAccountHeader(Long accountId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.EntityHeaderView(account.busEntityId, account.shortDesc)" +
                " from  BusEntityData account where account.busEntityId = (:accountId) "
        );

        q.setParameter("accountId", accountId);

        List x = q.getResultList();

        return !x.isEmpty() ? (EntityHeaderView) x.get(0) : null;
    }

    @Override
    public AccountIdentView saveAccountIdent(Long storeId, AccountIdentView accountView) throws DatabaseUpdateException {

        BusEntityData be = accountView.getBusEntityData();
        AccountContactView accountContact = accountView.getAccountContact();
        AccountIdentPropertiesView accountIdentProperties = accountView.getProperties();

        if (be.getBusEntityId() == null) {

            be = saveAccountBusEntity(be);

            if (Utility.longNN(be.getBusEntityId()) > 0) {

                if (createAccountOfStoreAssoc(storeId, be.getBusEntityId())) {

                    accountContact = createOrUpdateAccountContact(be.getBusEntityId(),
                            accountContact
                    );

                    accountIdentProperties = createOrUpdateAccountIdentProperties(be.getBusEntityId(),
                            accountIdentProperties
                    );

                } else {

                    throw new DatabaseUpdateException();

                }

            } else {

                throw new DatabaseUpdateException();

            }

        } else {

            be = saveAccountBusEntity(be);

            accountContact = createOrUpdateAccountContact(be.getBusEntityId(), accountContact);
            accountIdentProperties = createOrUpdateAccountIdentProperties(be.getBusEntityId(), accountIdentProperties);

        }


        accountView.setBusEntityData(be);
        accountView.setAccountContact(accountContact);
        accountView.setProperties(accountIdentProperties);

        return accountView;
    }

    private BusEntityData saveAccountBusEntity(BusEntityData be) {

        if (be.getBusEntityId() == null) {

            be = super.create(be);

            if (Utility.longNN(be.getBusEntityId()) > 0) {
                if (!Utility.isSet(be.getErpNum())) {
                    be = updateErpNumber(be, Constants.ERP_NUM_PREFIX + be.getBusEntityId());
                }
            }

        } else {

            if (!Utility.isSet(be.getErpNum())) {
                be.setErpNum(Constants.ERP_NUM_PREFIX + be.getBusEntityId());
            }

            be = super.update(be);

        }

        return be;
    }

    private BusEntityData updateErpNumber(BusEntityData be, String erp) {
        be.setErpNum(erp);
        return super.update(be);
    }

    private AccountIdentPropertiesView createOrUpdateAccountIdentProperties(Long busEntityId, AccountIdentPropertiesView accountIdentProperties) {

        List<PropertyData> properties = new ArrayList<PropertyData>();

        properties.add(accountIdentProperties.getBudgetType());
        properties.add(accountIdentProperties.getAccountType());
        properties.add(accountIdentProperties.getDistributorReferenceNumber());
        properties.addAll(accountIdentProperties.getDefaultProperties());

        PropertyDAO propertyDAO = new PropertyDAOImpl(em);
        properties = propertyDAO.updateEntityProperties(busEntityId, properties);

        accountIdentProperties.setAccountId(busEntityId);
        accountIdentProperties.setBudgetType(properties.get(0));
        accountIdentProperties.setAccountType(properties.get(1));
        accountIdentProperties.setDistributorReferenceNumber(properties.get(2));
        List<PropertyData> defaultProperties = new ArrayList<PropertyData>();
        accountIdentProperties.setDefaultProperties(defaultProperties);
        for (int index = 3;  index<properties.size();index++) {
            PropertyData p  = (PropertyData)properties.get(index);
            defaultProperties.add(p);
        }    

        return accountIdentProperties;
    }

    private AccountContactView createOrUpdateAccountContact(Long busEntityId, AccountContactView accountContact) {

        AddressData billiingAddress = accountContact.getBillingAddress();
        AddressData primaryAddress = accountContact.getPrimaryContact().getAddress();
        PhoneData fax = accountContact.getPrimaryContact().getFaxPhone();
        PhoneData mobile = accountContact.getPrimaryContact().getMobilePhone();
        PhoneData phone = accountContact.getPrimaryContact().getPhone();
        EmailData email = accountContact.getPrimaryContact().getEmail();

        AddressDAO addressDao = new AddressDAOImpl(em);
        EmailDAO emailDao = new EmailDAOImpl(em);
        PhoneDAO phoneDao = new PhoneDAOImpl(em);

        billiingAddress = addressDao.updateEntityAddress(busEntityId, billiingAddress);
        primaryAddress = addressDao.updateEntityAddress(busEntityId, primaryAddress);
        phone = phoneDao.updateEntityPhone(busEntityId, phone);
        mobile = phoneDao.updateEntityPhone(busEntityId, mobile);
        fax = phoneDao.updateEntityPhone(busEntityId, fax);
        email = emailDao.updateEntityAddress(busEntityId, email);


        accountContact.setBillingAddress(billiingAddress);

        accountContact.getPrimaryContact().setAddress(primaryAddress);
        accountContact.getPrimaryContact().setPhone(phone);
        accountContact.getPrimaryContact().setEmail(email);
        accountContact.getPrimaryContact().setFaxPhone(fax);
        accountContact.getPrimaryContact().setMobilePhone(mobile);

        return accountContact;

    }

    private boolean createAccountOfStoreAssoc(Long storeId, Long busEntityId) {

        BusEntityAssocDAO busEntityAssocDao = new BusEntityAssocDAOImpl(em);

        BusEntityAssocData assoc = busEntityAssocDao.createAssoc(busEntityId,
                storeId,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE
        );

        return assoc != null && Utility.longNN(assoc.getBusEntityAssocId()) > 0;
    }

    private AccountIdentView pickupAccountIdentView(BusEntityData busEntityData) {

        if (busEntityData == null || !(Utility.longNN(busEntityData.getBusEntityId()) > 0)) {
            return null;
        }        
        
        AccountIdentView accountIdentView = new AccountIdentView();

        AccountContactView contactView = findContact(busEntityData.getBusEntityId());
        AccountIdentPropertiesView propertiesView = findAccountIdentProperty(busEntityData.getBusEntityId());

        accountIdentView.setBusEntityData(busEntityData);
        accountIdentView.setAccountContact(contactView);
        accountIdentView.setProperties(propertiesView);

        return accountIdentView;

    }

    private AccountIdentPropertiesView findAccountIdentProperty(Long busEntityId) {
        PropertyDAO propertyDao = new PropertyDAOImpl(em);
        List<PropertyData> properties = propertyDao.findEntityProperties(busEntityId, Utility.typeCodes(AccountIdentPropertyTypeCode.values()));
        return PropertyBinder.bindAccountIdentProperties(new AccountIdentPropertiesView(),
                busEntityId,
                properties
        );
    }

    private AccountContactView findContact(Long accountId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.ContactView(" +
                "primaryAddress," +
                "phone," +
                "faxPhone," +
                "mobilePhone," +
                "email) " +
                "from  EntityContactEntity contact" +
                " left join contact.address primaryAddress  with primaryAddress.addressTypeCd = (:primaryAddressTypeProperty)" +
                " left join contact.phone phone with phone.phoneTypeCd  = (:phoneTypeProperty) and phone.primaryInd = (:primaryInd)" +
                " left join contact.phone mobilePhone with mobilePhone.phoneTypeCd  = (:mobilePhoneTypeProperty)" +
                " left join contact.phone faxPhone with faxPhone.phoneTypeCd  = (:faxPhoneTypeProperty)" +
                " left join contact.email email with email.emailTypeCd  = (:emailTypeProperty) and email.primaryInd = (:primaryInd)" +
                " where contact.busEntityId = (:accountId) "
        );

        q.setParameter("accountId", accountId);
        q.setParameter("primaryAddressTypeProperty", RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        q.setParameter("phoneTypeProperty", RefCodeNames.PHONE_TYPE_CD.PHONE);
        q.setParameter("mobilePhoneTypeProperty", RefCodeNames.PHONE_TYPE_CD.MOBILE);
        q.setParameter("faxPhoneTypeProperty", RefCodeNames.PHONE_TYPE_CD.FAX);
        q.setParameter("emailTypeProperty", RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT);
        q.setParameter("primaryInd", true);

        List contacts = q.getResultList();
        ContactView primaryContact = !contacts.isEmpty() ? (ContactView) contacts.get(0) : null;

        q = em.createQuery("Select address from  EntityContactEntity contact  " +
                "left join contact.address address  " +
                "with address.addressTypeCd = (:addressTypeProperty)" +
                " where address.busEntityId = (:accountId) "
        );

        q.setParameter("accountId", accountId);
        q.setParameter("addressTypeProperty", RefCodeNames.ADDRESS_TYPE_CD.BILLING);

        List billingAddresses = q.getResultList();
        AddressData billingAddress = !billingAddresses.isEmpty() ? (AddressData) billingAddresses.get(0) : null;

        return new AccountContactView(accountId,
                billingAddress,
                primaryContact
        );
    }
    
    @Override
    public BusEntityData findAccountById(Long accountId) {
        return em.find(BusEntityData.class, accountId);
    }
    
    @Override
    public Boolean isBudgetExistsForAccount(Long accountId, Integer fiscalYear) {
        if (accountId != null && fiscalYear != null) {

            if (existAccountBudgets(accountId, fiscalYear)) {
                return true;
            }

            if (existSiteBudgetsForAccount(accountId, fiscalYear)) {
                return true;
            }
        }

        return false;
    }
    
    private boolean existAccountBudgets(Long accountId, Integer fiscalYear) {
        if (accountId != null && fiscalYear != null) {
            //account budgets
            Query q = em.createQuery("Select object(budgetEntity) from BudgetEntity budgetEntity" +

                    " where budgetEntity.budget.busEntityId = (:accountId)" +
                    " and budgetEntity.budget.budgetTypeCd = (:budgetTypeCd)" +
                    " and budgetEntity.budget.budgetStatusCd <> (:budgetStatusCd)" +
                    " and budgetEntity.budget.budgetYear = (:fiscalYear)"
                    );

            q.setParameter("accountId", accountId);
            q.setParameter("fiscalYear", fiscalYear);
            
            q.setParameter("budgetTypeCd", RefCodeNames.BUDGET_TYPE_CD.ACCOUNT_BUDGET);
            q.setParameter("budgetStatusCd", RefCodeNames.BUDGET_STATUS_CD.INACTIVE);

            q.setMaxResults(1);

            List<BudgetEntity> budgets = (List<BudgetEntity>) q.getResultList();
            
            return !budgets.isEmpty();
        }

        return false;
    }
    
    private boolean existSiteBudgetsForAccount(Long accountId, Integer fiscalYear) {
        if (accountId != null && fiscalYear != null) {
            //account budgets
            Query q = em.createQuery("Select object(budgetEntity) from BudgetEntity budgetEntity, " +
                                                          "BusEntityAssocData siteToAccount, " +
                                                          "BusEntityData site" +

                    " where siteToAccount.busEntity2Id = (:accountId)" +
                    " and siteToAccount.busEntityAssocCd = (:siteToAccountCd)" +
                    
                    " and site.busEntityId = siteToAccount.busEntity1Id" +
                    " and site.busEntityStatusCd <> (:siteStatusCd)" +

                    " and budgetEntity.budget.busEntityId = siteToAccount.busEntity1Id" +
                    " and budgetEntity.budget.budgetTypeCd = (:budgetTypeCd)" +
                    " and budgetEntity.budget.budgetStatusCd <> (:budgetStatusCd)" +
                    " and budgetEntity.budget.budgetYear = (:fiscalYear)"
                    );

            q.setParameter("accountId", accountId);
            q.setParameter("fiscalYear", fiscalYear);
            
            q.setParameter("siteToAccountCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            q.setParameter("siteStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE);
            
            q.setParameter("budgetTypeCd", RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);
            q.setParameter("budgetStatusCd", RefCodeNames.BUDGET_STATUS_CD.INACTIVE);
            
            q.setMaxResults(1);
            
            List<BudgetEntity> budgets = (List<BudgetEntity>) q.getResultList();
            
            return !budgets.isEmpty();
        }

        return false;
    }
    
    
    @Override
    public List<BudgetView> findAccountSiteBudgets(Long accountId, Integer fiscalYear) {
        List<BudgetView> allBudgets = new ArrayList<BudgetView>();
        if (accountId != null && fiscalYear != null) {
            List<BudgetEntity> budgets;

            budgets = findAccountBudgets(accountId, fiscalYear);
            if (!budgets.isEmpty()) {
                for (BudgetEntity entity : budgets) {
                    allBudgets.add(new BudgetView(entity.getBudget(), entity.getDetails()));
                }
            }
            
            budgets = findSiteBudgetsForAccount(accountId, fiscalYear);
            if (!budgets.isEmpty()) {
                for (BudgetEntity entity : budgets) {
                    allBudgets.add(new BudgetView(entity.getBudget(), entity.getDetails()));
                }
            }
        }

        return allBudgets;
    }
    
    private List<BudgetEntity> findAccountBudgets(Long accountId, Integer fiscalYear) {
        List<BudgetEntity> budgets = new ArrayList<BudgetEntity>();
        if (accountId != null && fiscalYear != null) {
            //account budgets
            Query q = em.createQuery("Select object(budgetEntity) from BudgetEntity budgetEntity, " +
                                                                 "CatalogData catalog, " +
                                                                 "CostCenterData costcenter, " +
                                                                 "CatalogAssocData catalogToAccount, " +
                                                                 "CostCenterAssocData costcenterToCatalog" +

                    " where catalogToAccount.busEntityId = (:accountId)" +
                    " and catalogToAccount.catalogAssocCd = (:catalogToAccountCd)" +
                    " and catalog.catalogId = catalogToAccount.catalogId" +
                    " and catalog.catalogTypeCd = (:catalogTypeCd)" + 
                    " and catalog.catalogStatusCd <> (:catalogStatusCd)" +
                    
                    " and costcenterToCatalog.catalogId = catalogToAccount.catalogId" +
                    " and costcenterToCatalog.costCenterAssocCd = (:costcenterToCatalogCd)" +
                    " and costcenter.costCenterId = costcenterToCatalog.costCenterId" +
                    " and costcenter.costCenterStatusCd <> (:costcenterStatusCd)" +

                    " and budgetEntity.budget.costCenterId = costcenterToCatalog.costCenterId" +
                    " and budgetEntity.budget.busEntityId = (:accountId)" +
                    " and budgetEntity.budget.budgetTypeCd = (:budgetTypeCd)" +
                    " and budgetEntity.budget.budgetStatusCd <> (:budgetStatusCd)" +
                    " and budgetEntity.budget.budgetYear = (:fiscalYear)"
                    );

            q.setParameter("accountId", accountId);
            q.setParameter("fiscalYear", fiscalYear);
            q.setParameter("catalogToAccountCd", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT);
            q.setParameter("catalogTypeCd", RefCodeNames.CATALOG_TYPE_CD.ACCOUNT);
            q.setParameter("catalogStatusCd", RefCodeNames.CATALOG_STATUS_CD.INACTIVE);
            
            q.setParameter("costcenterToCatalogCd", RefCodeNames.COST_CENTER_ASSOC_CD.COST_CENTER_ACCOUNT_CATALOG);
            q.setParameter("costcenterStatusCd", RefCodeNames.COST_CENTER_STATUS_CD.INACTIVE);
            
            q.setParameter("budgetTypeCd", RefCodeNames.BUDGET_TYPE_CD.ACCOUNT_BUDGET);
            q.setParameter("budgetStatusCd", RefCodeNames.BUDGET_STATUS_CD.INACTIVE);

            budgets = (List<BudgetEntity>) q.getResultList();
        }

        return budgets;
    }
    
    private List<BudgetEntity> findSiteBudgetsForAccount(Long accountId, Integer fiscalYear) {
        List<BudgetEntity> budgets = new ArrayList<BudgetEntity>();
        if (accountId != null && fiscalYear != null) {
            //account budgets
            Query q = em.createQuery("Select object(budgetEntity) from BudgetEntity budgetEntity, " +
                                                          "CatalogData catalog, " +
                                                          "CostCenterData costcenter, " +
                                                          "CatalogAssocData catalogToAccount, " +
                                                          "CostCenterAssocData costcenterToCatalog, " +
                                                          "BusEntityAssocData siteToAccount, " +
                                                          "BusEntityData site" +

                    " where catalogToAccount.busEntityId = (:accountId)" +
                    " and catalogToAccount.catalogAssocCd = (:catalogToAccountCd)" +
                    " and catalog.catalogId = catalogToAccount.catalogId" +
                    " and catalog.catalogTypeCd = (:catalogTypeCd)" + 
                    " and catalog.catalogStatusCd <> (:catalogStatusCd)" +
                    
                    " and costcenterToCatalog.catalogId = catalogToAccount.catalogId" +
                    " and costcenterToCatalog.costCenterAssocCd = (:costcenterToCatalogCd)" +
                    " and costcenter.costCenterId = costcenterToCatalog.costCenterId" +
                    " and costcenter.costCenterStatusCd <> (:costcenterStatusCd)" +

                    " and siteToAccount.busEntity2Id = (:accountId)" +
                    " and siteToAccount.busEntityAssocCd = (:siteToAccountCd)" +
                    
                    " and site.busEntityId = siteToAccount.busEntity1Id" +
                    " and site.busEntityStatusCd <> (:siteStatusCd)" +

                    " and budgetEntity.budget.costCenterId = costcenterToCatalog.costCenterId" +
                    " and budgetEntity.budget.busEntityId = siteToAccount.busEntity1Id" +
                    " and budgetEntity.budget.budgetTypeCd = (:budgetTypeCd)" +
                    " and budgetEntity.budget.budgetStatusCd <> (:budgetStatusCd)" +
                    " and budgetEntity.budget.budgetYear = (:fiscalYear)"
                    );

            q.setParameter("accountId", accountId);
            q.setParameter("fiscalYear", fiscalYear);
            q.setParameter("catalogToAccountCd", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT);
            q.setParameter("catalogTypeCd", RefCodeNames.CATALOG_TYPE_CD.ACCOUNT);
            q.setParameter("catalogStatusCd", RefCodeNames.CATALOG_STATUS_CD.INACTIVE);
            
            q.setParameter("costcenterToCatalogCd", RefCodeNames.COST_CENTER_ASSOC_CD.COST_CENTER_ACCOUNT_CATALOG);
            q.setParameter("costcenterStatusCd", RefCodeNames.COST_CENTER_STATUS_CD.INACTIVE);
            
            q.setParameter("siteToAccountCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);
            q.setParameter("siteStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE);
            
            q.setParameter("budgetTypeCd", RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);
            q.setParameter("budgetStatusCd", RefCodeNames.BUDGET_STATUS_CD.INACTIVE);
            
            budgets = (List<BudgetEntity>) q.getResultList();
        }

        return budgets;
    }
    
    public List<Long> findAccountByDistRefNumberAndStoreId(String distAcctRefNumber, Long storeId) {
    	StringBuilder baseQuery = new StringBuilder("Select account.busEntityId " +
                " from " +
                "com.espendwise.manta.model.fullentity.BusEntityFullEntity account " +
                " left join account.properties distrRefNumber with distrRefNumber.propertyTypeCd = (:distrRefTypeProperty)" +
                " inner join account.busEntityAssocsForBusEntity1Id storeAccount" +
                " where storeAccount.busEntity2Id.busEntityId = (:storeId)" +
                " and storeAccount.busEntityAssocCd = (:storeAccountAssocCd)" +
                " and distrRefNumber.value = (:distAcctRefNumber)" +
                " and account.busEntityStatusCd = " + QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE));


        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("storeId", storeId);
        q.setParameter("distAcctRefNumber", distAcctRefNumber);
        q.setParameter("distrRefTypeProperty", RefCodeNames.PROPERTY_TYPE_CD.DIST_ACCT_REF_NUM);
        q.setParameter("storeAccountAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.ACCOUNT_OF_STORE);
        
        List<Long> r = q.getResultList();

        logger.info("findAccountByDistRefNumberAndStoreId)=> END, fetched : " + r.size() + " rows");

        return r;
    }
}
