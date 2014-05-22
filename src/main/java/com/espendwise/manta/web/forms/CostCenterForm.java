package com.espendwise.manta.web.forms;


//import com.espendwise.manta.model.view.CostCenterHeaderView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.validator.CostCenterFormValidator;

@Validation(CostCenterFormValidator.class)
public class CostCenterForm extends WebForm implements Initializable {


    //base
    private Long costCenterId;
    private String costCenterName;
    private String costCenterCode;
    private String status;
    private String costCenterType;
    private String costCenterTaxType;

    private String allocateFreight;
    private String allocateDiscount;
    private String doNotApplyBudget;

    
    private boolean init;


/*
    public CostCenterHeaderView getCostCenterHeader() {
        return isNew()
                ? new CostCenterHeaderView()
                : new CostCenterHeaderView(costCenterId, costCenterName, Parse.parseLong(accountId), accountName);
    }  */

    public Long getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(Long costCenterId) {
        this.costCenterId = costCenterId;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCostCenterCode() {
        return costCenterCode;
    }

    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public String getCostCenterType() {
        return costCenterType;
    }

    public void setCostCenterType(String costCenterType) {
        this.costCenterType = costCenterType;
    }

    public String getCostCenterTaxType() {
        return costCenterTaxType;
    }

    public void setCostCenterTaxType(String costCenterTaxType) {
        this.costCenterTaxType = costCenterTaxType;
    }

    public String getAllocateFreight() {
        return allocateFreight;
    }

    public void setAllocateFreight(String allocateFreight) {
        this.allocateFreight = allocateFreight;
    }

    public String getAllocateDiscount() {
        return allocateDiscount;
    }

    public void setAllocateDiscount(String allocateDiscount) {
        this.allocateDiscount = allocateDiscount;
    }

    public String getDoNotApplyBudget() {
        return doNotApplyBudget;
    }

    public void setDoNotApplyBudget(String doNotApplyBudget) {
        this.doNotApplyBudget = doNotApplyBudget;
    }


    @Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

    public boolean isNew() {
        return isInitialized() && (costCenterId == null || costCenterId == 0);
    }


	@Override
    public String toString() {
        return "CostCenterForm{" +
                ", costCenterId=" + costCenterId +
                ", costCenterName='" + costCenterName + '\'' +
                ", status='" + status + '\'' +
                ", init=" + init +
                '}';
    }
}
