package com.espendwise.manta.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.AccountDAO;
import com.espendwise.manta.dao.AccountDAOImpl;
import com.espendwise.manta.dao.PropertyDAO;
import com.espendwise.manta.dao.PropertyDAOImpl;
import com.espendwise.manta.dao.StoreDAO;
import com.espendwise.manta.dao.StoreDAOImpl;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.AccountIdentView;
import com.espendwise.manta.model.view.ContentManagementView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.CMSView;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.AccountUniqueConstraint;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CMSServiceImpl extends DataAccessService implements CMSService {
	
	@Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public CMSView findCMS(Long primaryId) {

        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);
 
        CMSView cms = new CMSView();
        ContentManagementView content = propertyDao.findContentManagementProperties(primaryId);
        
        if(content != null){
        	cms.setContentManagementProperties(content);
        }
        BusEntityData storeData = findPrimaryEntity(primaryId);
        cms.setPrimaryEntityId(primaryId);
        cms.setPrimaryEntityName(storeData.getShortDesc());
        
        return cms;
    }

	@Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EntityHeaderView findPrimaryEntityHeader(Long primaryId) {

        EntityManager entityManager = getEntityManager();
        StoreDAO storeDao = new StoreDAOImpl(entityManager);

        return storeDao.findStoreHeader(primaryId);
    }
	
	@Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BusEntityData findPrimaryEntity(Long primaryId){
		
		 EntityManager entityManager = getEntityManager();
	     StoreDAO storeDao = new StoreDAOImpl(entityManager);

	     return storeDao.findStoreById(primaryId);
	}
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CMSView saveCMS(Long primaryId, CMSView cmsView) throws DatabaseUpdateException {

        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        return propertyDao.saveCMS(primaryId, cmsView);

    }
}
