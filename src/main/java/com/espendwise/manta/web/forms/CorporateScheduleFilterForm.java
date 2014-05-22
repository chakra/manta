package com.espendwise.manta.web.forms;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.CorporateScheduleFilterFormValidator;

@Validation(CorporateScheduleFilterFormValidator.class)
public class CorporateScheduleFilterForm extends AbstractSimpleFilterForm {

	private String corporateScheduleDateFrom;
    private String corporateScheduleDateTo;
    private Long storeId;
 
    

    public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getCorporateScheduleDateFrom() {
		return corporateScheduleDateFrom;
	}

	public void setCorporateScheduleDateFrom(String corporateScheduleDateFrom) {
		this.corporateScheduleDateFrom = corporateScheduleDateFrom;
	}

	public String getCorporateScheduleDateTo() {
		return corporateScheduleDateTo;
	}

	public void setCorporateScheduleDateTo(String corporateScheduleDateTo) {
		this.corporateScheduleDateTo = corporateScheduleDateTo;
	}

	

	@Override
    public void reset() {
        super.reset();
        this.corporateScheduleDateTo = null;
        this.corporateScheduleDateFrom = null;
    }

 
}
