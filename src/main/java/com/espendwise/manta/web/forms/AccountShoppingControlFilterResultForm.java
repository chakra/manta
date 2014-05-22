package com.espendwise.manta.web.forms;


import java.util.List;

import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.AccountShoppingControlFilterResultFormValidator;

@Validation(AccountShoppingControlFilterResultFormValidator.class)
public class AccountShoppingControlFilterResultForm  extends AbstractFilterResult<ShoppingControlItemView> {

    private List<ShoppingControlItemView> shoppingControls;
    
    //reference data
    private List<Pair<String, String>> shoppingControlActionChoices;

    public AccountShoppingControlFilterResultForm() {
    }

    public List<ShoppingControlItemView> getShoppingControls() {
        return shoppingControls;
    }

    public void setShoppingControls(List<ShoppingControlItemView> shoppingControls) {
        this.shoppingControls = shoppingControls;
    }

    @Override
    public List<ShoppingControlItemView> getResult() {
        return shoppingControls;
    }

    public List<Pair<String, String>> getShoppingControlActionChoices() {
		return shoppingControlActionChoices;
	}

	public void setShoppingControlActionChoices(
			List<Pair<String, String>> shoppingControlActionChoices) {
		this.shoppingControlActionChoices = shoppingControlActionChoices;
	}

	@Override
    public void reset() {
        super.reset();
        this.shoppingControls = null;
    }

}
