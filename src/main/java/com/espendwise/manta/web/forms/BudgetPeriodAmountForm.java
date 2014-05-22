package com.espendwise.manta.web.forms;


import java.io.Serializable;

public class BudgetPeriodAmountForm  implements Serializable {

    private Integer period;
    private String mmdd;
    private String inputForm;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getInputForm() {
        return inputForm;
    }

    public void setInputForm(String inputForm) {
        this.inputForm = inputForm;
    }

    public String getMmdd() {
        return mmdd;
    }

    public void setMmdd(String mmdd) {
        this.mmdd = mmdd;
    }
}
