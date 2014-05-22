package com.espendwise.manta.service;


import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.WoServiceTypeData;
import com.espendwise.manta.model.data.WoServiceTypeAssocData;
import com.espendwise.manta.model.data.WoServiceTypeCategoryData;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.Validators;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ServiceTypeServiceImpl extends DataAccessService implements ServiceTypeService {

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public WoServiceTypeData findServiceType(Long pServiceTypeId) {
        EntityManager entityManager = getEntityManager();
        ServiceTypeDAO dao = new ServiceTypeDAOImpl(entityManager);
        
        WoServiceTypeData st = dao.findServiceTypeById(pServiceTypeId);
	    return st;
    }

      
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<WoServiceTypeData> findServiceTypeCollection(List<Long> pServiceTypeIds) {
	        EntityManager entityManager = getEntityManager();
	        ServiceTypeDAO dao = new ServiceTypeDAOImpl(entityManager);
		    List<WoServiceTypeData> result = dao.findServiceTypeCollection(pServiceTypeIds);
		    
	       return result;
	}
 
  }
