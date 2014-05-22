package com.espendwise.manta.service;


import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.EntityPropertiesView;

import java.util.List;

public interface PropertyService {

    public List<PropertyData> findEntityPropertyValues(Long entityId, String typeCode);

    public void configureEntityProperty(Long entityId, String propertyType, String propertyValue);

    public EntityPropertiesView findEntityProperties(Long entityId,  List<String> propertyExtraTypeCds,  List<String> propertyTypeCds);

    public EntityPropertiesView saveEntityProperties(Long entityId, EntityPropertiesView view);
    
    public OrderPropertyData saveOrderProperty(Long orderId, OrderPropertyData property);
}
