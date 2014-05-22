package com.espendwise.manta.dao;


import java.math.BigDecimal;

public interface SiteLedgerDAO {

    public BigDecimal calculateAmountSpent(Long accountId,
                                           Long siteId,
                                           Long costCenterId,
                                           Integer budgetYear,
                                           Integer budgetPeriod,
                                           String budgetAccrualCd,
                                           String budgetTypeCd);
}
