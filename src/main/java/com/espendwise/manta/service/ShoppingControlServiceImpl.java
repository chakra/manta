package com.espendwise.manta.service;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.ShoppingControlDAO;
import com.espendwise.manta.model.view.ShoppingControlItemView;
import com.espendwise.manta.util.criteria.AccountShoppingControlItemViewCriteria;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ShoppingControlServiceImpl extends DataAccessService implements ShoppingControlService {
	
    @Autowired
	private ShoppingControlDAO shoppingControlDao;

    public ShoppingControlServiceImpl() {
    }
    
    public ShoppingControlServiceImpl(ShoppingControlDAO shoppingControlDao) {
        this.shoppingControlDao = shoppingControlDao;
    }
	
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ShoppingControlItemView> findShoppingControlsByCriteria(AccountShoppingControlItemViewCriteria criteria) {
    	EntityManager entityManager = getEntityManager();
    	shoppingControlDao.setEntityManager(entityManager);
		List<ShoppingControlItemView> result = shoppingControlDao.findShoppingControls(criteria);
	    return result;
	}

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Map<String,List<ShoppingControlItemView>> updateShoppingControls(List<ShoppingControlItemView> shoppingControls) {
    	EntityManager entityManager = getEntityManager();
    	shoppingControlDao.setEntityManager(entityManager);
    	Map<String,List<ShoppingControlItemView>> result = shoppingControlDao.updateShoppingControls(shoppingControls);
	    return result;
    }

}
