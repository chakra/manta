package com.espendwise.manta.service;

import java.util.List;
import java.util.Map;

import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.util.criteria.AccountShoppingControlItemViewCriteria;

public interface ShoppingControlService {
	
    public List<ShoppingControlItemView> findShoppingControlsByCriteria(AccountShoppingControlItemViewCriteria criteria);
    public Map<String,List<ShoppingControlItemView>> updateShoppingControls(List<ShoppingControlItemView> shoppingControls);

}