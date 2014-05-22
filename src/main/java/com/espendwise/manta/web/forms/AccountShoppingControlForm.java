package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;

public class AccountShoppingControlForm extends WebForm implements Initializable {

    private Long shoppingControlId;
    private boolean initialize;

    public AccountShoppingControlForm() {
    }

	public Long getShoppingControlId() {
		return shoppingControlId;
	}

	public void setShoppingControlId(Long shoppingControlId) {
		this.shoppingControlId = shoppingControlId;
	}

	@Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public boolean isNew() {
      return isInitialized() && (shoppingControlId  == null || shoppingControlId == 0);
    }

    @Override
    public String toString() {
        return "AccountShoppingControlForm{" +
                "initialize=" + initialize +
                ", shoppingControlId=" + shoppingControlId +
                '}';
    }
}
