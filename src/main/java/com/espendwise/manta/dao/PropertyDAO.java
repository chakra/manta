package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.ContentManagementView;
import com.espendwise.manta.model.view.LogonOptionView;
import com.espendwise.manta.model.view.CMSView;
import com.espendwise.manta.model.view.UiOptionView;
import com.espendwise.manta.service.DatabaseUpdateException;

import java.util.List;
import java.util.Locale;

public interface PropertyDAO {

    public List<PropertyData> findStoreUiProperties(Long storeId, Locale locale);
    
    public LogonOptionView getLogonOptions(String datasource, String domainName, Long storeId, Locale locale);

    public UiOptionView getUiOptions(Long store, Locale userLocale);

    public List<PropertyData> findEntityProperties(Long busEntityId, List<String> propertyTypeCds);

    public List<PropertyData> updateEntityProperties(Long busEntityId, List<PropertyData> properties);
    
    public List<PropertyData> updateUserProperties(Long userId, List<PropertyData> properties);
    
    public String getEntityPropertyValue(Long busEntityId, String propertyTypeCd);

    public List<PropertyData> findEntityProperties(List<Long> busEntityIds, List<String> propertyshortDescs,List<String>  propertyTypeCds, boolean omitNullValues);

    public List<PropertyData> findEntityProperties(List<Long> busEntityIds, List<String> propertyshortDescs, List<String>  propertyTypeCds,  List<String>  statusList, boolean omitNullValues);

    public List<PropertyData> findEntityExtraProperties(Long busEntityId, List<String> names);

    public List<PropertyData> findEntityNamedProperties(Long busEntityId, List<String> names);

    public void removeProperties(List<PropertyData> properties);

    public List<PropertyData> findEntityExtraActiveProperties(Long busEntityId, List<String> names);

    public List<PropertyData> findEntityNamedActiveProperties(Long busEntityId, List<String> names);

    public List<PropertyData> findUserProperties(List<Long> userId, List<String> propertyshortDescs, List<String>  propertyTypeCds, boolean omitNullValues);

    public List<PropertyData> findUserProperties(List<Long> userId, List<String> propertyshortDescs, List<String>  propertyTypeCds,  List<String>  statusList, boolean omitNullValues);

    public List<PropertyData> findUserNamedProperties(Long userId, List<String> names);

    public List<PropertyData> findUserNamedActiveProperties(Long userId, List<String> names);

    public List<PropertyData> findUserProperties(Long userId, List<String> propertyTypeCds);

    public List<PropertyData> findUserActiveProperties(Long userId, List<String> propertyTypeCds);
    
    public ContentManagementView findContentManagementProperties(Long busEntityId);
    
    public CMSView saveCMS(Long primaryEntityId, CMSView cms) throws DatabaseUpdateException;
    
    public ContentManagementView saveAccountContentManagement(Long accountId, ContentManagementView content) throws DatabaseUpdateException ;

    public void configureEntityProperty(Long entityId, String propertyType, String propertyValue);

    public List<PropertyData> findUserAccessTokens(Long asUserId, Long originalUserId);

    public List<PropertyData> findActiveAccessTokens(String accessToken);

    public List<PropertyData> findActiveAccessTokens(Long userId, Long originalUserId, String accessToken);

    public List<PropertyData> findActiveUserAccessTokens(Long userId,  String accessToken);

    public List<PropertyData> saveUserProperties(Long userId, List<PropertyData> properties);

    public List<PropertyData> findEntityPropertiesByTypeOrName(List<Long> busEntityIds, List<String> propertyshortDescs,  List<String>  propertyTypeCds,  List<String>  statusList,  boolean omitNullValues) ;
    
    public List<PropertyData> findStoreProperties(Long storeId, List<String> propertyTypes);
    
    public OrderPropertyData saveOrderProperty(Long orderId, OrderPropertyData orderProperty);
    
}
