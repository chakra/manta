package com.espendwise.manta.service;

import com.espendwise.manta.dao.PropertyDAO;
import com.espendwise.manta.dao.PropertyDAOImpl;
import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.EntityPropertiesView;
import com.espendwise.manta.util.Utility;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyServiceImpl  extends  DataAccessService implements PropertyService{
    @Override
    public List<PropertyData> findEntityPropertyValues(Long entityId, String typeCode) {
        PropertyDAO propertyDao = new PropertyDAOImpl(getEntityManager());
        return propertyDao.findEntityProperties(entityId, Utility.toList(typeCode));
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureEntityProperty(Long entityId, String propertyType, String propertyValue) {

        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        propertyDao.configureEntityProperty(entityId, propertyType, propertyValue);
    }
    
    @Override
    public EntityPropertiesView findEntityProperties(Long entityId, List<String> propertyExtraTypeCds, List<String> propertyTypeCds) {
        PropertyDAO propertyDao = new PropertyDAOImpl(getEntityManager());
        List<PropertyData> properties = propertyDao.findEntityPropertiesByTypeOrName(Utility.toList(entityId), propertyExtraTypeCds, propertyTypeCds, null, false);
        EntityPropertiesView view = new EntityPropertiesView ();
        view.setBusEntityId(entityId);
        view.setProperties(properties);
        return view;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EntityPropertiesView saveEntityProperties(Long entityId, EntityPropertiesView pView) {
        PropertyDAO propertyDao = new PropertyDAOImpl(getEntityManager());
        
        List<PropertyData>  properties = propertyDao.updateEntityProperties(entityId, pView.getProperties());
        EntityPropertiesView view = new EntityPropertiesView ();
        view.setBusEntityId(entityId);
        view.setProperties(properties);
        return view;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrderPropertyData saveOrderProperty(Long orderId, OrderPropertyData property) {
        PropertyDAO propertyDao = new PropertyDAOImpl(getEntityManager());
        
        OrderPropertyData savedProperty = propertyDao.saveOrderProperty(orderId, property);
        
        return savedProperty;
    }

}
