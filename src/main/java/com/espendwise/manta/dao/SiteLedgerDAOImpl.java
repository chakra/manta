package com.espendwise.manta.dao;


import com.espendwise.manta.util.GoodOrderStatusCodes;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;

public class SiteLedgerDAOImpl extends DAOImpl implements SiteLedgerDAO {

    private static final Logger logger = Logger.getLogger(SiteLedgerDAOImpl.class);


    public SiteLedgerDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public BigDecimal calculateAmountSpent(Long accountId,
                                           Long siteId,
                                           Long costCenterId,
                                           Integer budgetYear,
                                           Integer budgetPeriod,
                                           String budgetAccrualCd,
                                           String budgetTypeCd) {

        logger.info("calculateAmountSpent()=> BEGIN, " +
                "\n       accountId: " + accountId +
                "\n          siteId: " + siteId +
                "\n    costCenterId: " + costCenterId +
                "\n      budgetYear: " + budgetYear +
                "\n    budgetPeriod: " + budgetPeriod +
                "\n budgetAccrualCd: " + budgetAccrualCd +
                "\n    budgetTypeCd: " + budgetTypeCd
        );

        boolean isAccountBudget = RefCodeNames.BUDGET_TYPE_CD.ACCOUNT_BUDGET.equals(budgetTypeCd);
        boolean isSiteBudget = RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET.equals(budgetTypeCd);
        boolean byPeriod = RefCodeNames.BUDGET_ACCRUAL_TYPE_CD.BY_PERIOD.equalsIgnoreCase(budgetAccrualCd);


        logger.info("calculateAmountSpent()=> calculateFinancialActivityAmount...");
        BigDecimal financialActivityAmount = calculateFinancialActivityAmount(accountId,
                siteId,
                costCenterId,
                budgetYear,
                budgetPeriod,
                isAccountBudget,
                isSiteBudget,
                byPeriod);
        logger.info("calculateAmountSpent()=> financialActivityAmount = "+financialActivityAmount);

        logger.info("calculateAmountSpent()=> calculatePriorBudgetAmount...");
        BigDecimal priorBudgetAmount = calculatePriorBudgetAmount(accountId,
                siteId,
                costCenterId,
                budgetYear,
                budgetPeriod,
                isAccountBudget,
                isSiteBudget,
                byPeriod);
        logger.info("calculateAmountSpent()=> priorBudgetAmount = "+priorBudgetAmount);

        logger.info("calculateAmountSpent()=> calculateConsolidatedSpendAmount...");
        BigDecimal consolidatedSpendAmount = calculateConsolidatedSpendAmount(accountId,
                siteId,
                costCenterId,
                budgetYear,
                budgetPeriod,
                isAccountBudget,
                isSiteBudget,
                byPeriod);
        logger.info("calculateAmountSpent()=> consolidatedSpendAmount = "+consolidatedSpendAmount);


        BigDecimal sum = Utility.addAmt(
                financialActivityAmount,
                Utility.addAmt(consolidatedSpendAmount, priorBudgetAmount)
        );

        logger.info("calculateAmountSpent()=> END, Sum = "+sum);

        return sum;

    }

    private BigDecimal calculateConsolidatedSpendAmount(Long accountId,
                                                        Long siteId,
                                                        Long costCenterId,
                                                        Integer budgetYear,
                                                        Integer budgetPeriod,
                                                        boolean isAccountBudget,
                                                        boolean isSiteBudget,
                                                        boolean byPeriod) {

        if (budgetYear == null
                || isAccountBudget && accountId == null
                || isSiteBudget && siteId == null
                || byPeriod && budgetPeriod == null) {

            return null;

        }

        Query query = em.createQuery("Select sum(ledger.amount) " +
                " from SiteLedgerData ledger, OrderData primaryOrder, OrderAssocData orderAssoc, OrderData refOrder " +
                "where ledger.orderId = primaryOrder.orderId  " +
                "  and primaryOrder.orderStatusCd = (:canceled)" +
                "  and primaryOrder.orderTypeCd = (:toBeConsolidated) " +
                "  and primaryOrder.orderId = orderAssoc.order1Id " +
                "  and refOrder.orderId  = orderAssoc.order2Id " +
                "  and orderAssoc.orderAssocCd= (:consolidated)" +
                "  and refOrder.orderStatusCd in (:goodOrderStatusList)" +
                "  and (primaryOrder.orderBudgetTypeCd != (:nonApplicable) or primaryOrder.orderBudgetTypeCd is null)" +
                " and ledger.budgetYear = (:budgetYear) " +
                (isAccountBudget ? " and primaryOrder.accountId = (:accountId)" : "") +
                (isSiteBudget ? " and primaryOrder.siteId = (:siteId)" : "") +
                (Utility.longNN(costCenterId) > 0 ? " and ledger.costCenterId = (:costCenterId)" : " and ledger.costCenterId > 0") +
                (byPeriod ? " and ledger.budgetPeriod = (:budgetPeriod )" : "")
        );

        query.setParameter("canceled", RefCodeNames.ORDER_STATUS_CD.CANCELLED);
        query.setParameter("toBeConsolidated", RefCodeNames.ORDER_TYPE_CD.TO_BE_CONSOLIDATED);
        query.setParameter("consolidated", RefCodeNames.ORDER_ASSOC_CD.CONSOLIDATED);
        query.setParameter("goodOrderStatusList", Utility.statusCodes(GoodOrderStatusCodes.values()));
        query.setParameter("nonApplicable", RefCodeNames.ORDER_BUDGET_TYPE_CD.NON_APPLICABLE);
        query.setParameter("budgetYear", budgetYear);

        if (isAccountBudget) {
            query.setParameter("accountId", accountId);
        }

        if (isSiteBudget) {
            query.setParameter("siteId", siteId);
        }

        if (Utility.longNN(costCenterId) > 0) {
            query.setParameter("costCenterId", costCenterId);
        }

        if (byPeriod) {
            query.setParameter("budgetPeriod", budgetPeriod);
        }

        return (BigDecimal) query.getResultList().get(0);
    }

    private BigDecimal calculatePriorBudgetAmount(Long accountId,
                                                  Long siteId,
                                                  Long costCenterId,
                                                  Integer budgetYear,
                                                  Integer budgetPeriod,
                                                  boolean isAccountBudget,
                                                  boolean isSiteBudget,
                                                  boolean byPeriod) {

        if (budgetYear == null
                || isAccountBudget && accountId == null
                || isSiteBudget && siteId == null
                || byPeriod && budgetPeriod == null) {

            return null;

        }

        Query query = em.createQuery("Select sum(ledger.amount) from SiteLedgerData ledger" +
                (isAccountBudget ? ", BusEntityAssocData siteAccount " : "") +
                " where ledger.entryTypeCd =(:priorPeriodBudgetAccrual) " +
                " and ledger.budgetYear = (:budgetYear) " +
                (isAccountBudget ? " and ledger.siteId = siteAccount.busEntity1Id " : "") +
                (isAccountBudget ? " and siteAccount.busEntity2Id = (:accountId)" : "") +
                (isSiteBudget ? " and ledger.siteId = (:siteId)" : "") +
                (Utility.longNN(costCenterId) > 0 ? " and ledger.costCenterId = (:costCenterId)" : " and ledger.costCenterId >= 0") +
                (byPeriod ? " and ledger.budgetPeriod = (:budgetPeriod )" : "")
        );

        query.setParameter("budgetYear", budgetYear);
        query.setParameter("priorPeriodBudgetAccrual", RefCodeNames.LEDGER_ENTRY_TYPE_CD.PRIOR_PERIOD_BUDGET_ACTUAL);

        if (isAccountBudget) {
            query.setParameter("accountId", accountId);
        }

        if (isSiteBudget) {
            query.setParameter("siteId", siteId);
        }

        if (Utility.longNN(costCenterId) > 0) {
            query.setParameter("costCenterId", costCenterId);
        }

        if (byPeriod) {
            query.setParameter("budgetPeriod", budgetPeriod);
        }

        return (BigDecimal) query.getResultList().get(0);
    }

    private BigDecimal calculateFinancialActivityAmount(Long accountId,
                                                        Long siteId,
                                                        Long costCenterId,
                                                        Integer budgetYear,
                                                        Integer budgetPeriod,
                                                        boolean isAccountBudget,
                                                        boolean isSiteBudget,
                                                        boolean byPeriod) {


        if (budgetYear == null
                || isAccountBudget && accountId == null
                || isSiteBudget && siteId == null
                || byPeriod && budgetPeriod == null) {

            return null;

        }


        Query query = em.createQuery("Select sum(ledger.amount) from SiteLedgerData ledger, OrderData orderData" +
                " where ledger.orderId = orderData.orderId " +
                " and orderData.orderStatusCd in (:goodOrderStatusList) " +
                " and (orderData.orderBudgetTypeCd != (:nonApplicable) or orderData.orderBudgetTypeCd is null)" +
                " and ledger.budgetYear = (:budgetYear) " +
                (isAccountBudget ? " and orderData.accountId = (:accountId)" : "") +
                (isSiteBudget ? " and orderData.siteId = (:siteId)" : "") +
                (Utility.longNN(costCenterId) > 0 ? " and ledger.costCenterId = (:costCenterId)" : " and ledger.costCenterId > 0") +
                (byPeriod ? " and ledger.budgetPeriod = (:budgetPeriod)" : "")
        );

        query.setParameter("goodOrderStatusList", Utility.statusCodes(GoodOrderStatusCodes.values()));
        query.setParameter("nonApplicable", RefCodeNames.ORDER_BUDGET_TYPE_CD.NON_APPLICABLE);
        query.setParameter("budgetYear", budgetYear);

        if (isAccountBudget) {
            query.setParameter("accountId", accountId);
        }

        if (isSiteBudget) {
            query.setParameter("siteId", siteId);
        }

        if (Utility.longNN(costCenterId) > 0) {
            query.setParameter("costCenterId", costCenterId);
        }

        if (byPeriod) {
            query.setParameter("budgetPeriod", budgetPeriod);
        }

        return (BigDecimal) query.getResultList().get(0);
    }


}
