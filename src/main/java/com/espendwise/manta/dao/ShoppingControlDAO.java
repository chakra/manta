package com.espendwise.manta.dao;

import java.util.List;
import java.util.Map;

import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.util.criteria.AccountShoppingControlItemViewCriteria;

public interface ShoppingControlDAO extends DAO {
	
    public List<ShoppingControlItemView> findShoppingControls(AccountShoppingControlItemViewCriteria criteria);
    public Map<String,List<ShoppingControlItemView>> updateShoppingControls(List<ShoppingControlItemView> shoppingControls);

}
