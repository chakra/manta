package com.espendwise.manta.service;


import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ContractData;
import com.espendwise.manta.model.data.SiteWorkflowData;
import com.espendwise.manta.model.data.WorkflowData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreSiteCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.rules.SiteBudgetsUpdateConstraint;
import com.espendwise.manta.util.validation.rules.SiteUpdateConstraint;
import org.apache.log4j.Logger;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class SiteServiceImpl extends DataAccessService implements SiteService {

    private static final Logger logger = Logger.getLogger(SiteServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<SiteListView> findSitesByCriteria(SiteListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        return siteDao.findSitesByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public SiteHeaderView findSiteHeader(Long storeId, Long siteId) {

        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        return siteDao.findSiteHeader(storeId, siteId);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public SiteIdentView findSiteToEdit(Long storeId, Long siteId) {

        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        return siteDao.findSiteToEdit(storeId, siteId);
    }

    public SiteIdentView saveSiteIdent(Long storeId, Long userId, SiteIdentView siteView) throws ValidationException, DatabaseUpdateException, IllegalDataStateException {
        return saveSiteIdent(storeId, userId, siteView, null);
    }



    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public SiteIdentView saveSiteIdent(Long storeId,
                                       Long userId,
                                       SiteIdentView siteView,
                                       Long createAssocFromSiteId) throws ValidationException, DatabaseUpdateException, IllegalDataStateException {

        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new SiteUpdateConstraint(storeId, siteView));

        validation.validate();

        EntityManager entityManager = getEntityManager();

        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        SiteIdentView site = siteDao.saveSiteIdent(storeId, siteView);

        if (site.getBusEntityData().getBusEntityId() != null && createAssocFromSiteId != null) {

            siteDao.configureSiteWithUsersFromAnotherSite(createAssocFromSiteId,
                    site.getBusEntityData().getBusEntityId(),
                    storeId,
                    userId
            );

            siteDao.configureCatalogFromAnotherSite(createAssocFromSiteId,
                    site.getBusEntityData()
            );
            
            // find active workflow association and assign to cloned site. Should only have at most 1 assigned
            WorkflowDAO workflowDao = new WorkflowDAOImpl(entityManager);
            List<SiteWorkflowData> assigns = workflowDao.findWorkflowToSitesAssoc(null, createAssocFromSiteId, null);
            for (SiteWorkflowData siteWorkflowD : assigns){
            	WorkflowData workflowD = workflowDao.findWorkflowById(siteWorkflowD.getWorkflowId(), null);
            	if (workflowD.getWorkflowStatusCd().equals(RefCodeNames.WORKFLOW_STATUS_CD.ACTIVE)){
            		workflowDao.assignSiteWorkflow(site.getBusEntityData().getBusEntityId(), workflowD.getWorkflowId(), workflowD.getWorkflowTypeCd());
            		break;
            	}
            }
        }

        return site;
    }


    @Override
    public void deleteSite(Long storeId, Long siteId) throws DatabaseUpdateException {

        try {

            ServiceLocator
                    .getSiteService()
                    .deleteSiteIdent(storeId, siteId);

        } catch (JpaSystemException e) {

                throw new DatabaseUpdateException(
                        e.getMessage(),
                        e.getMostSpecificCause(),
                        ExceptionReason.SiteUpdateReason.SITE_CANT_BE_DELETED
                );


        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteSiteIdent(Long storeId, Long siteId) {

        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        SiteIdentView siteIdent = siteDao.findSiteToEdit(storeId, siteId);
        if (siteIdent != null) {
            siteDao.removeSiteIdent(siteIdent);
        }

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<SiteAccountView> findSites(StoreSiteCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        return siteDao.findSites(criteria);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public SortedMap<Integer, SiteBudgetYearView> findBudgetForSiteByYear(SiteHeaderView site,
                                                                          List<FiscalCalendarListView> calendars,
                                                                          List<Integer> budgetsForYears,
                                                                          Date date) throws IllegalDataStateException {


        logger.info("findBudgetForSiteByYear()=> BEGIN" +
                ", site: " + (site != null ? site.getSiteId() : "-") +
                ", budgetsForYears: " + budgetsForYears +
                ", date: " + date);

        SortedMap<Integer, SiteBudgetYearView> x = new TreeMap<Integer, SiteBudgetYearView>();

        EntityManager entityManager = getEntityManager();

        BudgetDAO budgetDao = new BudgetDAOImpl(entityManager);
        FiscalCalendarDAO calendarDao = new FiscalCalendarDAOImpl(entityManager);

        if (site != null) {

            Set<Integer> budgetYears = budgetsForYears == null ? new HashSet<Integer>() : new HashSet<Integer>(budgetsForYears);

            for (Integer year : budgetYears) {

                logger.info("findBudgetForSiteByYear()=> processing budget for year: " + year);

                if (budgetYears.contains(year)) {

                    logger.info("findBudgetForSiteByYear()=> find actual physical calendar ");

                    FiscalCalendarPhysicalView calendar = calendarDao.selectCalenderForYear(calendars, year, date);

                    logger.info("findBudgetForSiteByYear()=> calendar ->  " + (calendar != null ? calendar : " not found"));
                    logger.info("findBudgetForSiteByYear()=> find site budget(s)" +
                            ",\n accountId: "+site.getAccountId()  +
                            ",\n    siteId: "+site.getSiteId() +
                            ",\n      year: "+year
                    );

                    List<CostCenterBudgetView> budgets = budgetDao.findBudgetsForSite(site.getAccountId(),
                            site.getSiteId(),
                            year
                    );

                    x.put(year, new SiteBudgetYearView(
                            calendar,
                            site.getAccountId(),
                            site.getSiteId(),
                            budgets));

                } else {

                    logger.info("findBudgetForSiteByYear()=> ignore, year not be requested.");
                    x.put(year, null);


                }


            }

        }

        logger.info("findBudgetForSiteByYear()=> END") ;

        return x;

    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BudgetView> findSiteBudgets(Long siteId, List<Long> budgetIds) {


        logger.info("findSiteBudgets()=> BEGIN" +
                ", site: " + siteId +
                ", budgetIds: " + budgetIds
        );

        List<BudgetView> x = new ArrayList<BudgetView>();

        EntityManager entityManager = getEntityManager();

        BudgetDAO budgetDao = new BudgetDAOImpl(entityManager);

        if (siteId != null) {

                    x =  budgetDao.findBudgets(
                            siteId,
                            budgetIds,
                            RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET
                    );

                }



        logger.info("findSiteBudgets()=> END") ;

        return x;

    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public boolean isUsedSiteBudgetThreshold(Long storeId, Long accountId, Long siteId) {

        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        String allowBusgetThreshold = PropertyUtil.toFirstValueNN(
                propertyDao.findEntityNamedActiveProperties(
                        storeId,
                        Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.BUDGET_THRESHOLD_FL)
                )
        );

        String budgetThresholdType = PropertyUtil.toFirstValueNN(
                propertyDao.findEntityNamedActiveProperties(
                        accountId,
                        Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.BUDGET_THRESHOLD_TYPE)
                )
        );

        return BudgetUtil.isUsedSiteBudgetThreshold(allowBusgetThreshold, budgetThresholdType);

    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map<Long, BudgetSiteSpendView> calculateCostCentersBudgetSpentsForSite(SiteBudgetYearView budgetYearView, Integer period) {

        Map<Long, BudgetSiteSpendView> x = new HashMap<Long, BudgetSiteSpendView>();

        EntityManager entityManager = getEntityManager();
        BudgetDAO budgetDao = new BudgetDAOImpl(entityManager);

        for (CostCenterBudgetView costCenterBudget : budgetYearView.getCostCenterBudgets()) {
            x.put(costCenterBudget.getCostCenter().getCostCenterId(),
                    budgetDao.calculateBudgetSpentForSite(budgetYearView.getAccountId(),
                            budgetYearView.getSiteId(),
                            costCenterBudget,
                            budgetYearView.getFiscalCalendar(),
                            period
                    )
            );
        }

        return x;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<BudgetView> updateSiteBudgets(Long storeId,
                                              Long siteId,
                                              List<BudgetView> budgets) throws ValidationException {

        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new SiteBudgetsUpdateConstraint(storeId, siteId, budgets));

        validation.validate();

        EntityManager entityManager = getEntityManager();

        BudgetDAO budgetDao = new BudgetDAOImpl(entityManager);
        return budgetDao.updateBudgets(budgets);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureSiteUsersList(Long siteId, Long storeId, List<UserListView> selected, List<UserListView> deselected) {

        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        siteDao.configureSiteUsersList(siteId, storeId, selected, deselected);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureSiteCatalog(Long siteId, Long catalogId) {
        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        siteDao.configureSiteCatalog(siteId, catalogId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public BusEntityAssocData findSiteAccountAssoc(Long siteId) {

        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        return siteDao.findSiteAccountAssoc(siteId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ContractData> findSiteContract(Long siteId) {
        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        return siteDao.findSiteContract(siteId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findSiteAccount(Long siteId) {
        EntityManager entityManager = getEntityManager();
        SiteDAO siteDao = new SiteDAOImpl(entityManager);

        return siteDao.findSiteAccount(siteId);
    }
}
