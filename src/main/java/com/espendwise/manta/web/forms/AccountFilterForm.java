package com.espendwise.manta.web.forms;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.SimpleFilterFormValidator;

@Validation(SimpleFilterFormValidator.class)
public class AccountFilterForm extends SimpleFilterForm {

    private String distrRefNumber;
    private String distrRefNumberFilterType = Constants.FILTER_TYPE.START_WITH;

    public AccountFilterForm() {
        super();
    }

    public String getDistrRefNumber() {
        return distrRefNumber;
    }

    public void setDistrRefNumber(String distrRefNumber) {
        this.distrRefNumber = distrRefNumber;
    }


    public String getDistrRefNumberFilterType() {
        return distrRefNumberFilterType;
    }

    public void setDistrRefNumberFilterType(String distrRefNumberFilterType) {
        this.distrRefNumberFilterType = distrRefNumberFilterType;
    }

    @Override
    public void reset() {
        super.reset();
        this.distrRefNumber = null;
        this.distrRefNumberFilterType = Constants.FILTER_TYPE.START_WITH;
    }

    @Override
    public String getFilterKey() {  return "admin.account.label.accountName";  }
    @Override
    public String getFilterIdKey() {  return "admin.account.label.accountId";  }
}
