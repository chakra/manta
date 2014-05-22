package com.espendwise.manta.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.CMSView;
import com.espendwise.manta.model.view.ContentManagementView;
import com.espendwise.manta.model.view.LogonOptionView;
import com.espendwise.manta.model.view.UiOptionView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.StoreUiPropertyTypeCode;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.binder.PropertyBinder;

public class PropertyDAOImpl extends DAOImpl implements PropertyDAO {

    private static final Logger logger = Logger.getLogger(PropertyDAOImpl.class);

    public PropertyDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<PropertyData> findStoreUiProperties(Long storeId, Locale locale) {

        logger.info("findStoreUiProperties()=> BEGIN" +
                ", storeId: " + storeId +
                ", locale: " + locale);

        Map<String, PropertyData> propertyIMap = new HashMap<String, PropertyData>();
        Set<String> typeCodeSet = new HashSet<String>(Utility.typeCodes(StoreUiPropertyTypeCode.values()));

        Query q = em.createQuery("Select p from PropertyData p where p.busEntityId = (:storeId) " +
                " and p.localeCd = (:localeCd) " +
                " and p.propertyTypeCd in (:typeCodes)");

        q.setParameter("storeId", storeId);
        q.setParameter("localeCd", locale.toString());
        q.setParameter("typeCodes", typeCodeSet);

        List<PropertyData> x = (List<PropertyData>) q.getResultList();
        for (String typeCode : typeCodeSet) {
            for (PropertyData p : x) {
                if (typeCode.equals(p.getPropertyTypeCd()) && !propertyIMap.containsKey(typeCode)) {
                    propertyIMap.put(typeCode, p);
                }
            }
        }

        if (!propertyIMap.keySet().containsAll(typeCodeSet)) {

            typeCodeSet.removeAll(propertyIMap.keySet());

            Set<String> locales = new HashSet<String>(
                    Utility.toList(
                            locale.getLanguage(),
                            Constants.DEFAULT_LOCALE.toString(),
                            Constants.DEFAULT_LOCALE.getLanguage()
                    )
            );

            x = findEntityLocalizedProperties(storeId, typeCodeSet, locales);
            for (PropertyData p : x) {
                if (!propertyIMap.containsKey(p.getPropertyTypeCd())) {
                    propertyIMap.put(p.getPropertyTypeCd(), p);
                }
            }
        }

        logger.info("findStoreUiProperties()=> END.");

        return new ArrayList<PropertyData>(propertyIMap.values());

    }

    public List<PropertyData> findEntityLocalizedProperties(Long busEntityId, Collection<String> propertyTypeCds, Collection<String> localeCds) {

        logger.info("findEntityLocalizedProperties()=> BEGIN" +
                ", busEntityId: " + busEntityId +
                ", propertyTypeCds: " + propertyTypeCds +
                ", localeCds: " + localeCds);

        Map<String, PropertyData> propertyIMap = new HashMap<String, PropertyData>();
        Set<String> typeCodeSet = new HashSet<String>(propertyTypeCds);

        Query q = em.createQuery("Select p  from PropertyData p where p.busEntityId = (:busEntityId) " +
                " and p.localeCd in (:localeCds) " +
                " and p.propertyTypeCd in (:typeCodes)");

        q.setParameter("busEntityId", busEntityId);
        q.setParameter("localeCds", localeCds);
        q.setParameter("typeCodes", typeCodeSet);

        List<PropertyData> x = (List<PropertyData>) q.getResultList();

        for (String typeCode : typeCodeSet) {
            for (String locale : localeCds) {
                for (PropertyData p : x) {
                    if (typeCode.equals(p.getPropertyTypeCd()) && locale.equals(p.getLocaleCd()) && !propertyIMap.containsKey(typeCode)) {
                        propertyIMap.put(typeCode, p);
                    }

                }
            }
        }

        if (!propertyIMap.keySet().containsAll(typeCodeSet)) {

            typeCodeSet.removeAll(propertyIMap.keySet());

            q = em.createQuery("Select p  from PropertyData p where p.busEntityId = (:busEntityId) " +
                    " and p.localeCd is null" +
                    " and p.propertyTypeCd in (:typeCodes)");

            q.setParameter("busEntityId", busEntityId);
            q.setParameter("typeCodes", typeCodeSet);

            x = (List<PropertyData>) q.getResultList();
            for (PropertyData p : x) {
                if (!propertyIMap.containsKey(p.getPropertyTypeCd())) {
                    propertyIMap.put(p.getPropertyTypeCd(), p);
                }
            }


        }

        logger.info("findEntityLocalizedProperties()=> END.");

        return new ArrayList<PropertyData>(propertyIMap.values());

    }

    @Override
    public String getEntityPropertyValue(Long busEntityId, String propertyTypeCd) {
    	List<PropertyData> properties = findEntityProperties(busEntityId,
                Utility.toList(propertyTypeCd));
    	if (properties.size() > 0){
    		PropertyData property = properties.get(0);
    		return property.getValue();
    	}else
    		return null;
    }
    
    @Override
    public List<PropertyData> findEntityProperties(Long busEntityId, List<String> propertyTypeCds) {

        return findEntityProperties(Utility.toList(busEntityId),
                null,
                propertyTypeCds,
                false
        );

    }

    @Override
    public List<PropertyData> findEntityExtraProperties(Long busEntityId, List<String> names) {
        return findEntityProperties(Utility.toList(busEntityId),
                names,
                Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.EXTRA),
                false
        );
    }

    @Override
    public List<PropertyData> findEntityExtraActiveProperties(Long busEntityId, List<String> names) {
        return findEntityProperties(Utility.toList(busEntityId),
                names,
                Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.EXTRA),
                Utility.toList(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE),
                false
        );
    }

    @Override
    public List<PropertyData> findEntityNamedActiveProperties(Long busEntityId, List<String> names) {
        return findEntityProperties(Utility.toList(busEntityId),
                names,
                null,
                Utility.toList(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE),
                false
        );
    }

    @Override
    public List<PropertyData> findEntityNamedProperties(Long busEntityId, List<String> names) {
        return findEntityProperties(Utility.toList(busEntityId),
                names,
                null,
                false
        );
    }

    @Override
    public List<PropertyData> findEntityProperties(List<Long> busEntityIds,
                                                   List<String> propertyshortDescs,
                                                   List<String> propertyTypeCds,
                                                   boolean omitNullValues) {

        return findEntityProperties(busEntityIds,
                propertyshortDescs,
                propertyTypeCds,
                null,
                omitNullValues
        );

    }

    @Override
    public List<PropertyData> findEntityProperties(List<Long> busEntityIds,
                                                   List<String> propertyshortDescs,
                                                   List<String>  propertyTypeCds,
                                                   List<String>  statusList,
                                                   boolean omitNullValues) {

        if (busEntityIds == null) {
            return Utility.emptyList(PropertyData.class);
        }

        StringBuilder queryBuilder = new StringBuilder("Select p from PropertyData p ");
        queryBuilder.append("where p.busEntityId ").append((busEntityIds.size() != 1) ? "in" : "=").append("(:busEntityIds) and p.userId is null ");
        queryBuilder.append(( propertyTypeCds != null ? " and p.propertyTypeCd " + ( propertyTypeCds.size() != 1 ? "in" : "=") + "(:typeCodes)" : ""));
        queryBuilder.append(( statusList != null ? " and p.propertyStatusCd " + ( statusList.size() != 1 ? "in" : "=") + "(:statusList)" : ""));
        queryBuilder.append((propertyshortDescs != null ? " and p.shortDesc " + (propertyshortDescs.size() != 1 ? "in" : "=") + "(:shortDescs)" : ""));
        queryBuilder.append((Utility.isTrue(omitNullValues) ? " and p.value is not null" : ""));

        Query q = em.createQuery(queryBuilder.toString());

        q.setParameter("busEntityIds", busEntityIds.size() == 1 ? busEntityIds.get(0) : busEntityIds);

        if ( propertyTypeCds != null) {
            q.setParameter("typeCodes",  propertyTypeCds.size() == 1 ? propertyTypeCds.get(0) :  propertyTypeCds);
        }

        if ( statusList != null) {
            q.setParameter("statusList",  statusList.size() == 1 ?statusList.get(0) :  statusList);
        }

        if (propertyshortDescs != null) {
            q.setParameter("shortDescs", propertyshortDescs.size() == 1 ? propertyshortDescs.get(0) : propertyshortDescs);
        }

        return (List<PropertyData>) q.getResultList();

    }

    @Override
    public List<PropertyData> findEntityPropertiesByTypeOrName(List<Long> busEntityIds,
                                                   List<String> propertyshortDescs,
                                                   List<String>  propertyTypeCds,
                                                   List<String>  statusList,
                                                   boolean omitNullValues) {

        if (busEntityIds == null) {
            return Utility.emptyList(PropertyData.class);
        }

        StringBuilder queryBuilder = new StringBuilder("Select p from PropertyData p ");
        queryBuilder.append("where p.busEntityId ").append((busEntityIds.size() != 1) ? "in" : "=").append("(:busEntityIds) and p.userId is null ");

        queryBuilder.append(( propertyTypeCds != null && propertyshortDescs != null 
        		? " and ( p.propertyTypeCd " + ( propertyTypeCds.size() != 1 ? "in" : "=") + "(:typeCodes) " + " or p.shortDesc " + (propertyshortDescs.size() != 1 ? "in" : "=") + "(:shortDescs))" 
        		: ""));
        
        queryBuilder.append(( propertyTypeCds != null && propertyshortDescs == null ? " and p.propertyTypeCd " + ( propertyTypeCds.size() != 1 ? "in" : "=") + "(:typeCodes)" : ""));
        queryBuilder.append(( statusList != null ? " and p.propertyStatusCd " + ( statusList.size() != 1 ? "in" : "=") + "(:statusList)" : ""));
        queryBuilder.append((propertyshortDescs != null && propertyTypeCds == null ? " and p.shortDesc " + (propertyshortDescs.size() != 1 ? "in" : "=") + "(:shortDescs)" : ""));
        queryBuilder.append((Utility.isTrue(omitNullValues) ? " and p.value is not null" : ""));

        Query q = em.createQuery(queryBuilder.toString());

        q.setParameter("busEntityIds", busEntityIds.size() == 1 ? busEntityIds.get(0) : busEntityIds);

        if ( propertyTypeCds != null) {
            q.setParameter("typeCodes",  propertyTypeCds.size() == 1 ? propertyTypeCds.get(0) :  propertyTypeCds);
        }

        if ( statusList != null) {
            q.setParameter("statusList",  statusList.size() == 1 ?statusList.get(0) :  statusList);
        }

        if (propertyshortDescs != null) {
            q.setParameter("shortDescs", propertyshortDescs.size() == 1 ? propertyshortDescs.get(0) : propertyshortDescs);
        }

        return (List<PropertyData>) q.getResultList();

    }

    @Override
    public void removeProperties(List<PropertyData> properties) {
        if (Utility.isSet(properties)) {
            for (PropertyData p : properties) {
                if (p != null) {
                    em.remove(p);
                }
            }
        }
    }


    @Override
    public List<PropertyData> updateEntityProperties(Long busEntityId, List<PropertyData> properties) {

        logger.info("updateEntityProperties()=> BEGIN");

        if (Utility.isSet(properties) && Utility.longNN(busEntityId) > 0) {

            //if the user is not an admin or sys admin they are not allowed to update the TRACK_PUNCHOUT_ORDER_MESSAGES
            //account orderProperty so if it exists in the view, remove it
        	boolean removedTrackPunchoutOrderMessagesProperty = false;
            AppUser appUser = Auth.getAppUser();
            boolean canUpdateTrackPunchoutProperty = appUser.isAdmin() || appUser.isSystemAdmin();
            if (!canUpdateTrackPunchoutProperty) {
            	Iterator<PropertyData> propertyIterator = properties.iterator();
            	while (propertyIterator.hasNext()) {
            		PropertyData property = propertyIterator.next();
            		if (RefCodeNames.PROPERTY_TYPE_CD.TRACK_PUNCHOUT_ORDER_MESSAGES.equalsIgnoreCase(property.getPropertyTypeCd())) {
            			propertyIterator.remove();
            			removedTrackPunchoutOrderMessagesProperty = true;
            		}
            	}
            }

        	
            for (int index = 0;  index<properties.size();index++) {

                PropertyData p  = properties.get(index);

                logger.info("updateEntityProperties()=> property[" + index + "] - " + p);

                if (p != null) {

                    p.setBusEntityId(busEntityId);

                    logger.info("updateEntityProperties()=> property[" + index + "] propertyId: " + p.getPropertyId());

                    p = p.getPropertyId() == null
                            ? super.create(p)
                            : super.update(p);

                    properties.set(index, p);

                }
            }
            
            //if we removed the TRACK_PUNCHOUT_ORDER_MESSAGES account orderProperty, retrieve and return the existing value
            if (removedTrackPunchoutOrderMessagesProperty) {
            	List<String> typeCdList = new ArrayList<String>();
            	typeCdList.add(RefCodeNames.PROPERTY_TYPE_CD.TRACK_PUNCHOUT_ORDER_MESSAGES);
            	List<PropertyData> trackPunchoutProperties = findEntityProperties(busEntityId, typeCdList);
            	if (Utility.isSet(trackPunchoutProperties)) {
            		properties.add(trackPunchoutProperties.get(0));
            	}
            }

        }

        logger.info("updateEntityProperties()=> END.");

        return properties;
    }

    @Override
    public List<PropertyData> updateUserProperties(Long userId, List<PropertyData> properties) {

        logger.debug("updateUserProperties()=> BEGIN");

        if (Utility.isSet(properties) && Utility.longNN(userId) > 0) {

            for (int index = 0;  index<properties.size();index++) {

                PropertyData p  = properties.get(index);

                logger.debug("updateUserProperties()=> property[" + index + "] - " + p);

                if (p != null) {

                    p.setUserId(userId);

                    logger.debug("updateUserProperties()=> property[" + index + "] propertyId: " + p.getPropertyId());

                    p = p.getPropertyId() == null
                            ? super.create(p)
                            : super.update(p);

                    properties.set(index, p);

                }
            }

        }

        logger.debug("updateUserProperties()=> END.");

        return properties;
    }

    public LogonOptionView getLogonOptions(String datasource, String domainName, Long storeId, Locale locale) {

        LogonOptionView logonOptions = new LogonOptionView();

        logonOptions.setDatasource(datasource);
        logonOptions.setStoreId(storeId);
        logonOptions.setDomain(domainName);
        logonOptions.setUiOption(getUiOptions(storeId, locale));
        logonOptions.setLocale(locale);

        return logonOptions;

    }

    public UiOptionView getUiOptions(Long store, Locale userLocale) {

        UiOptionView uiOptions = new UiOptionView();
        if (store != null) {
            List<PropertyData> uiProperties = findStoreUiProperties(store, userLocale);
            if (uiProperties != null) {
                uiOptions = PropertyBinder.bindUiOptions(uiOptions, uiProperties);
            }
        }

        return uiOptions;
    }


    @Override
    public List<PropertyData> findUserProperties(Long userId, List<String> propertyTypeCds) {

        return findUserProperties(Utility.toList(userId),
                null,
                propertyTypeCds,
                false
        );

    }

    @Override
    public List<PropertyData> findUserActiveProperties(Long userId, List<String> propertyTypeCds) {

        return findUserProperties(Utility.toList(userId),
                null,
                propertyTypeCds,
                Utility.toList(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE),
                false
        );

    }


    @Override
    public List<PropertyData> findUserNamedActiveProperties(Long userId, List<String> names) {
        return findUserProperties(Utility.toList(userId),
                names,
                null,
                Utility.toList(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE),
                false
        );
    }

    @Override
    public List<PropertyData> findUserNamedProperties(Long userId, List<String> names) {
        return findUserProperties(Utility.toList(userId),
                names,
                null,
                false
        );
    }

    @Override
    public List<PropertyData> findUserProperties(List<Long> userIds,
                                                   List<String> propertyshortDescs,
                                                   List<String> propertyTypeCds,
                                                   boolean omitNullValues) {

        return findUserProperties(userIds,
                propertyshortDescs,
                propertyTypeCds,
                null,
                omitNullValues
        );

    }

    @Override
    public List<PropertyData> findUserAccessTokens(Long asUserId, Long originalUserId) {

        Query q = em.createQuery("select property from PropertyData  property " +
                " where property.userId = (:userId)" +
               (originalUserId !=null ? " and property.originalUserId = (:originalUserId)":"  and property.originalUserId is null ") +
                " and property.propertyTypeCd = (:propertyTypeCd)"
        );

        q.setParameter(PropertyData.USER_ID, asUserId);
        q.setParameter(PropertyData.PROPERTY_TYPE_CD, RefCodeNames.PROPERTY_TYPE_CD.ACCESS_TOKEN);
        if (originalUserId != null) {
            q.setParameter(PropertyData.ORIGINAL_USER_ID, originalUserId);
        }

        return findAccessTokens(
                asUserId,
                originalUserId,
                null,
                null
        );

    }


    @Override
    public List<PropertyData> findActiveAccessTokens(Long userId, Long originalUserId, String accessToken) {

        return findAccessTokens(
                userId,
                originalUserId,
                accessToken,
                RefCodeNames.PROPERTY_STATUS_CD.ACTIVE
        );

    }

    @Override
    public List<PropertyData> findActiveUserAccessTokens(Long userId,  String accessToken) {

        return findAccessTokens(
                userId,
                null,
                accessToken,
                RefCodeNames.PROPERTY_STATUS_CD.ACTIVE
        );

    }

    @Override
    public List<PropertyData> findActiveAccessTokens(String accessToken) {

        return findAccessTokens(
                null,
                null,
                accessToken,
                RefCodeNames.PROPERTY_STATUS_CD.ACTIVE
        );

    }


    private List<PropertyData> findAccessTokens(Long userId, Long originalUserId, String accessToken, String status) {

        Query q = em.createQuery("select property from PropertyData  property " +
                " where property.propertyTypeCd = (:propertyTypeCd) " +
                (accessToken != null ? " and property.value = (:value)" : "") +
                (userId != null ? " and property.userId = (:userId)" : "") +
                (originalUserId != null ? " and property.originalUserId = (:originalUserId)" : "") +
                (status != null ? " and property.propertyStatusCd = (:propertyStatusCd)" : "")
        );

        q.setParameter(PropertyData.PROPERTY_TYPE_CD, RefCodeNames.PROPERTY_TYPE_CD.ACCESS_TOKEN);

        if (accessToken != null) {
            q.setParameter(PropertyData.VALUE, accessToken);
        }

        if (userId != null) {
            q.setParameter(PropertyData.USER_ID, userId);
        }

        if (originalUserId != null) {
            q.setParameter(PropertyData.ORIGINAL_USER_ID, originalUserId);
        }

        if (status != null) {
            q.setParameter(PropertyData.PROPERTY_STATUS_CD, status);
        }


        return result(q, PropertyData.class);

    }


    @Override
    public List<PropertyData> findUserProperties(List<Long> userIds,
                                                 List<String> propertyshortDescs,
                                                 List<String>  propertyTypeCds,
                                                 List<String>  statusList,
                                                 boolean omitNullValues) {

        if (userIds == null) {
            return Utility.emptyList(PropertyData.class);
        }

        StringBuilder queryBuilder = new StringBuilder("Select p from PropertyData p ");
        queryBuilder.append("where p.userId ").append((userIds.size() != 1) ? "in" : "=").append("(:userIds) and p.busEntityId is null ");
        queryBuilder.append(( propertyTypeCds != null ? " and p.propertyTypeCd " + ( propertyTypeCds.size() != 1 ? "in" : "=") + "(:typeCodes)" : ""));
        queryBuilder.append(( statusList != null ? " and p.propertyStatusCd " + ( statusList.size() != 1 ? "in" : "=") + "(:statusList)" : ""));
        queryBuilder.append((propertyshortDescs != null ? " and p.shortDesc " + (propertyshortDescs.size() != 1 ? "in" : "=") + "(:shortDescs)" : ""));
        queryBuilder.append((Utility.isTrue(omitNullValues) ? " and p.value is not null" : ""));

        Query q = em.createQuery(queryBuilder.toString());

        q.setParameter("userIds", userIds.size() == 1 ? userIds.get(0) : userIds);

        if ( propertyTypeCds != null) {
            q.setParameter("typeCodes",  propertyTypeCds.size() == 1 ? propertyTypeCds.get(0) :  propertyTypeCds);
        }

        if (statusList != null) {
            q.setParameter("statusList", statusList.size() == 1 ? statusList.get(0) : statusList);
        }

        if (propertyshortDescs != null) {
            q.setParameter("shortDescs", propertyshortDescs.size() == 1 ? propertyshortDescs.get(0) : propertyshortDescs);
        }

        return (List<PropertyData>) q.getResultList();

    }


    public ContentManagementView findContentManagementProperties(Long busEntityId){

    	ContentManagementView content = new ContentManagementView();

    	//Content management properties
        List<String> cmsProps = new ArrayList<String>();
        cmsProps.add(RefCodeNames.PROPERTY_TYPE_CD.DISPLAY_GENERIC_CONTENT);
        cmsProps.add(RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_URL);
        cmsProps.add(RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_EDITOR);
        cmsProps.add(RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_VIEWER);

        List<PropertyData> properties =  findEntityNamedProperties(busEntityId, cmsProps);

        if(properties != null && properties.size()>0){
        	content = PropertyBinder.bindContentManagementProperties(new ContentManagementView(), busEntityId, properties);
        }
        return content;
    }

    private ContentManagementView createOrUpdateContentManagementProperties(Long busEntityId, ContentManagementView content) {

        List<PropertyData> properties = new ArrayList<PropertyData>();

        properties.add(content.getDisplayGenericContent());
        properties.add(content.getCustomContentURL());
        properties.add(content.getCustomContentEditor());
        properties.add(content.getCustomContentViewer());

        PropertyDAO propertyDAO = new PropertyDAOImpl(em);
        properties = propertyDAO.updateEntityProperties(busEntityId, properties);

        content.setDisplayGenericContent(properties.get(0));
        content.setCustomContentURL(properties.get(1));
        content.setCustomContentEditor(properties.get(2));
        content.setCustomContentViewer(properties.get(3));

        return content;
    }

    public CMSView saveCMS(Long primaryEntityId, CMSView cms) throws DatabaseUpdateException {

    	ContentManagementView content = cms.getContentManagementProperties();

    	if(content != null){
    		content = createOrUpdateContentManagementProperties(primaryEntityId, content);
    	}

    	cms.setContentManagementProperties(content);
    	return cms;
    }

    public ContentManagementView saveAccountContentManagement(Long accountId, ContentManagementView content) throws DatabaseUpdateException {

    	if(content != null){
    		content = createOrUpdateContentManagementProperties(accountId, content);
    	}
    	return content;
    }

    @Override
    public void configureEntityProperty(Long entityId, String propertyType, String propertyValue) {
        if (entityId != null && Utility.isSet(propertyType)) {

            List<PropertyData> oldProperties = findEntityProperties(entityId, Utility.toList(propertyType));

            Map<String, PropertyData> oldMap = new HashMap<String, PropertyData>();

            if (Utility.isSet(oldProperties)) {
                for (PropertyData el : oldProperties) {
                    oldMap.put(el.getValue(), el);
                }
            }

            List<PropertyData> newProperties = new ArrayList<PropertyData>();
            PropertyData property;
            if (Utility.isSet(propertyValue)) {
                property = new PropertyData();
                property.setBusEntityId(entityId);
                property.setValue(propertyValue);
                property.setPropertyStatusCd(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);
                property.setShortDesc(propertyType);
                property.setPropertyTypeCd(propertyType);
                newProperties.add(property);
            }

            for (PropertyData el : newProperties) {
                if (oldMap.containsKey(el.getValue()) &&
                    RefCodeNames.PROPERTY_STATUS_CD.ACTIVE.equals(oldMap.get(el.getValue()).getPropertyStatusCd())) {

                    oldMap.remove(el.getValue());
                } else { // create new orderProperty
                    super.create(el);
                }
            }

            if (oldMap.size() > 0) {
                Iterator<PropertyData> it = oldMap.values().iterator();
                while (it.hasNext()) { // remove unused old properties
                    property = it.next();
                    em.remove(property);
                }
            }
        }
    }
    
    @Override
    public List<PropertyData> saveUserProperties(Long userId, List<PropertyData> properties) {
        logger.debug("saveUserProperties()=> BEGIN");
        
        List<PropertyData> propertyNewList = new ArrayList<PropertyData>();
        if (userId != null) {
            PropertyDAO propertyDao = new PropertyDAOImpl(em);

            List<PropertyData> oldPropertyList = propertyDao.findUserActiveProperties(userId, null);
            
            Map<Long, PropertyData> oldMap = new HashMap<Long, PropertyData>();
            
            if (Utility.isSet(oldPropertyList)) {
                for (PropertyData el : oldPropertyList) {
                    oldMap.put(el.getPropertyId(), el);
                }
            }

            if (Utility.isSet(properties)) {
                for (PropertyData property : properties) {
                    if (oldMap.containsKey(property.getPropertyId())) {
                        oldMap.remove(property.getPropertyId());
                    }
                    property.setUserId(userId);
                    property = property.getPropertyId() == null ? super.create(property) : super.update(property);
                    propertyNewList.add(property);
                }
            }
            
            PropertyData property;
            if (oldMap.size() > 0) {
                Iterator<PropertyData> it = oldMap.values().iterator();
                while (it.hasNext()) { // remove unused properties
                    property = it.next();
                    em.remove(property);
                }
            }
        }
        
        logger.debug("saveUserProperties()=> END");
        return propertyNewList;
    }
    
    @Override
    public List<PropertyData> findStoreProperties(Long storeId, List<String> propertyTypes) {
        List<PropertyData> properties;
        
        if (Utility.isSet(storeId) && Utility.isSet(propertyTypes)) {
            StringBuilder baseQuery = new StringBuilder("Select object(property) from PropertyData property");
                          baseQuery.append(" WHERE property.busEntityId = (:storeId)");
                          baseQuery.append(" AND property.propertyTypeCd").append(propertyTypes.size() == 1 ? " = " : " in ").append("(:propertyTypes)");
            
            Query q = em.createQuery(baseQuery.toString());
            q.setParameter("storeId", storeId);
            q.setParameter("propertyTypes", propertyTypes);

            properties = (List<PropertyData>) q.getResultList();
        } else {
            properties = new ArrayList<PropertyData>();
        }
        
        return properties;
    }
    
    @Override
    public OrderPropertyData saveOrderProperty(Long orderId, OrderPropertyData orderProperty) {
        logger.debug("saveOrderProperty()=> BEGIN");
        OrderPropertyData savedProperty = null;
                
        if (orderId != null && orderProperty != null) {
            OrderDAO orderDao = new OrderDAOImpl(em);
            List<OrderPropertyData> oldOrderProperties = orderDao.findOrderProperties(orderId,
                                                                                      Utility.toList(orderProperty.getOrderPropertyTypeCd()));
            
            Map<Long, OrderPropertyData> oldMap = new HashMap<Long, OrderPropertyData>();
            if (Utility.isSet(oldOrderProperties)) {
                for (OrderPropertyData el : oldOrderProperties) {
                    oldMap.put(el.getOrderPropertyId(), el);
                }
            }

            if (oldMap.containsKey(orderProperty.getOrderPropertyId())) {
                savedProperty = super.update(orderProperty);
            } else {
                savedProperty = super.create(orderProperty);
            }
        }
        
        logger.debug("saveOrderProperty()=> END");

        return savedProperty;
    }

}
