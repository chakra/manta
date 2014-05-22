package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BudgetData;
import com.espendwise.manta.model.data.BudgetDetailData;
import com.espendwise.manta.model.data.CostCenterData;
import com.espendwise.manta.model.entity.BudgetEntity;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.wrapper.BudgetEntityWrapper;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class BudgetDAOImpl extends DAOImpl implements BudgetDAO {

    private static final Logger logger = Logger.getLogger(BudgetDAOImpl.class);


    public BudgetDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }


    public List<CostCenterBudgetView> findBudgetsForSite(Long accountId, Long siteId, Integer budgetYear) throws IllegalDataStateException {


        List<CostCenterBudgetView> x = Utility.emptyList(CostCenterBudgetView.class);


        AccountDAO accountDao = new AccountDAOImpl(em);
        List<CostCenterData> costCenters = accountDao.findCatalogCostCenters(accountId);

        Map<Long, CostCenterData> costCentersMap = Utility.toMap(costCenters);

        List<BudgetEntity> budgets = new ArrayList<BudgetEntity>();

        List<BudgetEntity> siteBudgets = findBudgets(
                siteId,
                costCentersMap.keySet(),
                budgetYear,
                RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET,
                Utility.toList(RefCodeNames.BUDGET_STATUS_CD.ACTIVE)
        );

        budgets.addAll(siteBudgets);

        List<BudgetEntity> accountBudgets = findBudgets(
                accountId,
                costCentersMap.keySet(),
                budgetYear,
                RefCodeNames.BUDGET_TYPE_CD.ACCOUNT_BUDGET,
                Utility.toList(RefCodeNames.BUDGET_STATUS_CD.ACTIVE));

        budgets.addAll(accountBudgets);


        Map<Long, List<BudgetEntity>> budgetsMapByCostCenter = new HashMap<Long, List<BudgetEntity>>();
        for (BudgetEntity budget : budgets) {
            List<BudgetEntity> l = budgetsMapByCostCenter.get(budget.getBudget().getCostCenterId());
            if (l == null) {
                l = Utility.emptyList(BudgetEntity.class);
                budgetsMapByCostCenter.put(budget.getBudget().getCostCenterId(), l);
            }
            l.add(budget);
        }

        for (CostCenterData costCenter : costCentersMap.values()) {

            CostCenterBudgetView v = new CostCenterBudgetView(costCenter);

            List<BudgetEntity> cosCenterBudgets = budgetsMapByCostCenter.get(costCenter.getCostCenterId());

            if (Utility.isSet(cosCenterBudgets)) {

                for (BudgetEntity costCenterBudget : cosCenterBudgets) {
                    if (RefCodeNames.BUDGET_TYPE_CD.ACCOUNT_BUDGET.equals(costCenterBudget.getBudget().getBudgetTypeCd())) {
                        v.setAccountBudget(costCenterBudget);
                    } else if (RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET.equals(costCenterBudget.getBudget().getBudgetTypeCd())) {
                        v.setSiteBudget(costCenterBudget);
                    }
                }


            }

            x.add(v);


        }


        return x;
    }

    public List<BudgetView> findBudgets(Long entityId,  Collection<Long> budgetIds, String budgetType) {

        logger.info("findBudgets()=> BEGIN" +
                ", entityId: " + entityId +
                ", budgetIds: " + budgetIds +
                ", budgetType: " + budgetType
        );

        List<BudgetView> x = new ArrayList<BudgetView>();

        if (!Utility.isSet(budgetIds) || !Utility.isSet(entityId) || !Utility.isSet(budgetType)){
            return x;

        }

        Query q = em.createQuery("Select budgetEntity from BudgetEntity budgetEntity " +
                "where budgetEntity.budget.busEntityId = (:entityId)" +
                " and  budgetEntity.budget.budgetId in (:budgetIds) " +
                " and  budgetEntity.budget.budgetTypeCd = (:budgetType) "

        );

        q.setParameter("entityId", entityId);
        q.setParameter("budgetIds", budgetIds);
        q.setParameter("budgetType", budgetType);

        List<BudgetEntity> r = q.getResultList();
        for(BudgetEntity entity:r) {
            x.add(new BudgetView(entity.getBudget(), entity.getDetails()));
        }

        logger.info("findBudgets()=> END, fetched "+r.size()+" rows");

        return x;
    }

    public List<BudgetEntity> findBudgets(Long busEntityId,
                                          Collection<Long> costCenterIds,
                                          Integer budgetYear,
                                          String budgetType,
                                          List<String> statusListOpt) {


        logger.info("findBudgets()=> BEGIN" +
                ",\n busEntityId: " + busEntityId +
                ",\n costCenterIds: " + costCenterIds +
                ",\n budgetYear: " + budgetYear +
                ",\n budgetType: " + budgetType +
                ",\n satusListOpt: " + statusListOpt
        );

        List<BudgetEntity> x = new ArrayList<BudgetEntity>();

        if (!Utility.isSet(costCenterIds)
                || !Utility.isSet(busEntityId)
                || !Utility.isSet(budgetType)
                || !Utility.isSet(budgetYear)) {

            return x;

        }


        Query q = em.createQuery("Select budgetEntity from BudgetEntity budgetEntity " +
                "where budgetEntity. budget.busEntityId = (:entityId)" +
                " and  budgetEntity.budget.costCenterId in (:costCenters) " +
                " and  budgetEntity.budget.budgetYear = (:year) " +
                " and  budgetEntity.budget.budgetTypeCd = (:budgetType)" +
                (Utility.isSet(statusListOpt) ? " and  budgetEntity.budget.budgetStatusCd  " + (statusListOpt.size() == 1 ? "= (:status)" : "in (:status)") : "")
        );

        q.setParameter("entityId", busEntityId);
        q.setParameter("costCenters", costCenterIds);
        q.setParameter("year", budgetYear);
        q.setParameter("budgetType", budgetType);

        if (Utility.isSet(statusListOpt)) {
            q.setParameter("status", statusListOpt.size() == 1 ? statusListOpt.get(0) : statusListOpt);
        }

        List<BudgetEntity> r = q.getResultList();

        logger.info("findBudgets()=> END, fetched "+r.size()+" rows");

        return r;
    }


    public BudgetSiteSpendView calculateBudgetSpentForSite(Long accountId,
                                                           Long siteId,
                                                           CostCenterBudgetView costCenterBudget,
                                                           FiscalCalendarPhysicalView calendar,
                                                           Integer budgetPeriod) {

        logger.info("calculateBudgetSpentForSite()=> BEGIN, budgetPeriod -> " + budgetPeriod);

        BudgetSiteSpendView budgetSiteSpend = new BudgetSiteSpendView();
        budgetSiteSpend.setBudgetSpends(Utility.emptyList(BudgetSpendView.class));

        if (costCenterBudget != null) {

            PropertyDAO propertyDao = new PropertyDAOImpl(em);
            SiteLedgerDAO siteLedgerDao = new SiteLedgerDAOImpl(em);

            String budgetAccrualCode = PropertyUtil.toFirstValue(
                    propertyDao.findEntityNamedProperties(
                            accountId,
                            Utility.toList(AccountIdentPropertyTypeCode.BUDGET_ACCRUAL_TYPE_CD.getTypeCode())
                    )
            );

            BigDecimal allocated = new BigDecimal(0);
            BigDecimal spent = new BigDecimal(0);

            List<BudgetEntity> siteBudgets = Utility.toListNN(
                    costCenterBudget.getSiteBudget(),
                    costCenterBudget.getAccountBudget()
            );


            for (BudgetEntity budget : siteBudgets) {

                if (budget != null && budget.getBudget() != null) {

                    BudgetSpendView spendView = new BudgetSpendView();

                    // Now get the total allocated up to this period.
                    BudgetEntityWrapper budgetWrapper = new BudgetEntityWrapper(budget);

                    for (int period = 1; period <= calendar.getPeriods().size() - 1; period++) {

                        allocated = BudgetUtil.evaluateABudgetPeriod(budgetAccrualCode,
                                budgetPeriod,
                                period,
                                allocated,
                                budgetWrapper.getAmount(period)
                        );

                    }


                    BigDecimal currPeriodAlloc = budgetWrapper.getAmount(budgetPeriod);
                    boolean unlimitedBudget = currPeriodAlloc == null;

                    if (!unlimitedBudget) {

                        spent = spent.add(
                                siteLedgerDao.calculateAmountSpent(
                                        accountId,
                                        siteId,
                                        budget.getBudget().getCostCenterId(),
                                        calendar.getPhysicalFiscalYear(),
                                        budgetPeriod,
                                        budgetAccrualCode,
                                        budget.getBudget().getBudgetTypeCd()
                                )
                        );

                    }


                    spendView.setBudgetId(budget.getBudget().getBudgetId());
                    spendView.setAmountSpent(spent);
                    spendView.setAmountAllocated(allocated);
                    spendView.setUnlimitedBudget(currPeriodAlloc == null);

                    budgetSiteSpend.getBudgetSpends().add(spendView);

                }
            }


            budgetSiteSpend.setSiteId(siteId);
            budgetSiteSpend.setAccountId(accountId);
            budgetSiteSpend.setAllocateFreight(Utility.isTrue(costCenterBudget.getCostCenter().getAllocateFreight()));
            budgetSiteSpend.setCostCenterTaxType(Utility.strNN(costCenterBudget.getCostCenter().getCostCenterTaxType(), RefCodeNames.COST_CENTER_TAX_TYPE.DONT_ALLOCATE_SALES_TAX));
            budgetSiteSpend.setCostCenterId(costCenterBudget.getCostCenter().getCostCenterId());
            budgetSiteSpend.setCostCenterName(costCenterBudget.getCostCenter().getShortDesc());
            budgetSiteSpend.setBudgetYear(calendar.getPhysicalFiscalYear());
            budgetSiteSpend.setBudgetPeriod(budgetPeriod);
            budgetSiteSpend.setCurrentBudgetPeriod(FiscalCalendarUtility.getPeriodOrNull(calendar, new Date()));
            budgetSiteSpend.setCurrentBudgetYear(calendar.getPhysicalFiscalYear());

        }


        logger.info("calculateBudgetSpentForSite()=> END.");

        return budgetSiteSpend;

    }


    public BudgetView updateBudget(BudgetView budget) {

        logger.info("updateBudget()=> BEGIN");

        if (readyForSave(budget)) {

            BudgetData budgetData = budget.getBudgetData();

            if (Utility.longNN(budgetData.getBudgetId()) > 0) {

                BudgetEntity currentBudget = em.find(BudgetEntity.class, budgetData.getBudgetId());

                logger.info("updateBudget()=> updating budget" +
                        ",\n   budgetId: " + budgetData.getBudgetId() + "" +
                        ",\n   entityId: " + budgetData.getBusEntityId() +
                        ",\n costCenter: " + budgetData.getCostCenterId() +
                        ",\n budgetYear: " + budgetData.getBudgetYear() +
                        ",\n budgetType: " + budgetData.getBudgetTypeCd()
                );

                budget = updateBudget(
                        budget,
                        new BudgetView(currentBudget.getBudget(), new ArrayList<BudgetDetailData>(currentBudget.getDetails()))
                );

            } else {
            
                List<String> activeStatus = new ArrayList<String>();
                activeStatus.add(RefCodeNames.BUDGET_STATUS_CD.ACTIVE);
                
                List<BudgetEntity> currentBudgets = findBudgets(
                        budgetData.getBusEntityId(),
                        Utility.toList(budgetData.getCostCenterId()),
                        budgetData.getBudgetYear(),
                        budgetData.getBudgetTypeCd(),
                        activeStatus
                );

                if (currentBudgets.isEmpty()) {

                    logger.info("updateBudget()=> creating new budget" +
                            ",\n   entityId: " + budgetData.getBusEntityId() +
                            ",\n costCenter: " + budgetData.getCostCenterId() +
                            ",\n budgetYear: " + budgetData.getBudgetYear() +
                            ",\n budgetType: " + budgetData.getBudgetTypeCd()
                    );

                    budget = createBudget(budget);

                } else {

                    Collections.sort(currentBudgets, AppComparator.BUDGET_ENTITY_ADD_DATE_COMPARE);

                    //refer to the last created budget and refresh it
                    BudgetEntity lastCreatedBudget = currentBudgets.remove(0);

                    logger.info("updateBudget()=> updating last created budget" +
                            ",\n   budgetId: " + lastCreatedBudget.getBudget().getBudgetId() + "" +
                            ",\n   entityId: " + budgetData.getBusEntityId() +
                            ",\n costCenter: " + budgetData.getCostCenterId() +
                            ",\n budgetYear: " + budgetData.getBudgetYear() +
                            ",\n budgetType: " + budgetData.getBudgetTypeCd()
                    );

                    budget = updateBudget(budget,
                            new BudgetView(
                                    lastCreatedBudget.getBudget(),
                                    new ArrayList<BudgetDetailData>(lastCreatedBudget.getDetails())
                            )
                    );

                    //remove any other budgets
                    logger.info("updateBudget()=> removing any other budgets(set to 'INACTIVE' status)");
                    for (BudgetEntity o : currentBudgets) {
                        if (RefCodeNames.BUDGET_STATUS_CD.ACTIVE.equals(o.getBudget().getBudgetStatusCd())) {
                            o.getBudget().setBudgetStatusCd(RefCodeNames.BUDGET_STATUS_CD.INACTIVE);
                            update(o.getBudget());
                        }
                    }
                }
            }
        }

        logger.info("updateBudget => END. budgetId:" + budget.getBudgetData().getBudgetId());

        return budget;
    }

    @Override
    public List<BudgetView> updateBudgets(List<BudgetView> budgets) {
        if (Utility.isSet(budgets)) {
            int i = 0;
            for (BudgetView budget : budgets) {
                budgets.set(i++, updateBudget(budget));
            }
        }

        return budgets;
    }

    private BudgetView createBudget(BudgetView budget) {

        BudgetData budgetData = budget.getBudgetData();
        List<BudgetDetailData> details = new ArrayList<BudgetDetailData>(budget.getBudgetDetails());

        if (!Utility.isSet(budgetData.getBudgetStatusCd())) {
            budgetData.setBudgetStatusCd(RefCodeNames.BUDGET_STATUS_CD.ACTIVE);
        }

        budgetData = create(budgetData);

        int i = 0;

        for (Object o : details) {
            BudgetDetailData detail = (BudgetDetailData) o;
            detail.setBudgetId(budgetData.getBudgetId());
            details.set(i++, create(detail));
        }

        budget.setBudgetData(budgetData);
        budget.setBudgetDetails(details);

        return budget;
    }

    private BudgetView updateBudget(BudgetView budgetNew, BudgetView budgetFound) {

        BudgetData budgetData = budgetNew.getBudgetData();

        budgetData.setBudgetId(budgetFound.getBudgetData().getBudgetId());
        budgetData.setAddBy(budgetFound.getBudgetData().getAddBy());
        budgetData.setAddDate(budgetFound.getBudgetData().getAddDate());


        budgetData = update(budgetData);

        budgetNew.setBudgetData(budgetData);
        budgetNew.setBudgetDetails(
                updateBudgetDetails(
                        budgetData,
                        new ArrayList<BudgetDetailData>(budgetNew.getBudgetDetails()),
                        new ArrayList<BudgetDetailData>(budgetFound.getBudgetDetails() )
                )
        );


        return budgetNew;
    }

    private List<BudgetDetailData> updateBudgetDetails(BudgetData budget,
                                                       List<BudgetDetailData> budgetPeriods,
                                                       List<BudgetDetailData> existsPeriods) {

        Iterator existsDetailsIt;
        Iterator newDetailsIt;

        existsDetailsIt = existsPeriods.iterator();
        while (existsDetailsIt.hasNext()) {

            BudgetDetailData existsDetailData = (BudgetDetailData) existsDetailsIt.next();

            newDetailsIt = budgetPeriods.iterator();

            boolean found = false;
            while (newDetailsIt.hasNext()) {

                BudgetDetailData periodData = (BudgetDetailData) newDetailsIt.next();
                if (existsDetailData.getPeriod() == periodData.getPeriod()) {
                    periodData.setBudgetDetailId(existsDetailData.getBudgetDetailId());
                    periodData.setAddBy(existsDetailData.getAddBy());
                    periodData.setAddDate(existsDetailData.getAddDate());
                    found = true;
                    break;
                }
            }

            if (found) {
                existsDetailsIt.remove();
            }

        }

        existsDetailsIt = existsPeriods.iterator();
        while (existsDetailsIt.hasNext()) {
            BudgetDetailData existsDetailData = (BudgetDetailData) existsDetailsIt.next();
            remove(existsDetailData);
        }


        newDetailsIt = budgetPeriods.iterator();

        while (newDetailsIt.hasNext()) {

            BudgetDetailData periodData = (BudgetDetailData) newDetailsIt.next();

            if (periodData.isDirty()) {

                if (Utility.longNN(periodData.getBudgetDetailId()) > 0) {
                    periodData.setBudgetId(budget.getBudgetId());
                    update(periodData);
                } else {
                    periodData.setBudgetId(budget.getBudgetId());
                    create(periodData);
                }
            }


        }

        return budgetPeriods;
    }


    private boolean readyForSave(BudgetView budget) {

        logger.info("readyForSave()=> BEGIN");

        boolean readyForSave =  budget.getBudgetData().getBusEntityId() > 0 &&
                budget.getBudgetData().getCostCenterId() > 0 &&
                budget.getBudgetData().getBudgetYear() > 0 &&
                Utility.isSet(budget.getBudgetData().getBudgetTypeCd());

        if(!readyForSave) {
            logger.info("updateBudget()=> object is not ready for save: "+budget.getBudgetData());
        }

        logger.info("updateBudget()=> END.");
        return readyForSave;
    }

}
