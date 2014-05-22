package com.espendwise.manta.web.forms;

import java.io.Serializable;
import java.math.BigDecimal;

public class CostCenterBudgetSpentForm implements Serializable {

  private BigDecimal spent;
  private BigDecimal allocated;
  private BigDecimal remaining;
  private Integer period;
    private Integer year;

    private boolean unlimitedBudget;

    public BigDecimal getSpent() {
        return spent;
    }

    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }

    public BigDecimal getAllocated() {
        return allocated;
    }

    public void setAllocated(BigDecimal allocated) {
        this.allocated = allocated;
    }

    public boolean isUnlimitedBudget() {
        return unlimitedBudget;
    }

    public void setUnlimitedBudget(boolean unlimitedBudget) {
        this.unlimitedBudget = unlimitedBudget;
    }

    public BigDecimal getRemaining() {
        return remaining;
    }

    public void setRemaining(BigDecimal remaining) {
        this.remaining = remaining;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
