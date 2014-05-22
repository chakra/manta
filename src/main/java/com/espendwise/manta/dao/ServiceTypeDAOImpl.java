package com.espendwise.manta.dao;

import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.binder.PropertyBinder;
//import com.espendwise.manta.util.criteria.StoreAccountCriteria;
import com.espendwise.manta.util.parser.Parse;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServiceTypeDAOImpl extends DAOImpl implements ServiceTypeDAO {

    private static final Logger logger = Logger.getLogger(ServiceTypeDAOImpl.class);

    public ServiceTypeDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public WoServiceTypeData findServiceTypeById(Long serviceTypeId) {
    	
        WoServiceTypeData  result = new  WoServiceTypeData();
        if (serviceTypeId != null) {
        		result = em.find(WoServiceTypeData.class, serviceTypeId);
       }
  	
    	return result;
    }

 
 
    @Override
    public List<WoServiceTypeData> findServiceTypeCollection(List<Long> serviceTypeIds) {
        // WO
       	StringBuilder woQuery= new StringBuilder(
    			"Select serviceType from WoServiceTypeData serviceType" +
                " where serviceType.serviceTypeId in (:serviceTypeIds)" );
        Query q = em.createQuery(woQuery.toString());

        q.setParameter("serviceTypeIds", serviceTypeIds);
        List<WoServiceTypeData> result = (List<WoServiceTypeData>) q.getResultList();
     	logger.info("findServiceTypeCollection()===> found : "+ result.size());
        return result;

    }

}
