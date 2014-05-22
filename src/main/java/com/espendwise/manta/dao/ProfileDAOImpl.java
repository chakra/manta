package com.espendwise.manta.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.StoreProfileData;
import com.espendwise.manta.model.view.ProfilePasswordMgrView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

import javax.persistence.EntityManager;
import javax.persistence.Query;



public class ProfileDAOImpl extends DAOImpl implements ProfileDAO {

    private static final Logger logger = Logger.getLogger(ProfileDAOImpl.class);

    public ProfileDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public ProfilePasswordMgrView getPasswordManagementInfo(Long storeId){
    	ProfilePasswordMgrView view = new ProfilePasswordMgrView(storeId);
    	Query q = em.createQuery("select property.display from StoreProfileData property " +
	        " where property.storeId = (:storeId)" +
	        " and property.shortDesc = (:shortDesc) ")
	        .setParameter("storeId", storeId)
	        .setParameter("shortDesc", RefCodeNames.STORE_PROFILE_FIELD.CHANGE_PASSWORD);
    	List v = q.getResultList();
    	if (v.size() > 0){
    		view.setAllowChangePassword(Utility.isTrue((String) v.get(0)));
    	}
    	
    	String temp = getStorePropertyValue(storeId, RefCodeNames.PROPERTY_TYPE_CD.RESET_PASSWORD_UPON_INIT_LOGIN);    	
    	if (Utility.isSet(temp)){
    		view.setReqPasswordResetUponInitLogin(Utility.isTrue(temp));
    	}
    	
    	temp = getStorePropertyValue(storeId, RefCodeNames.PROPERTY_TYPE_CD.RESET_PASSWORD_WITHIN_DAYS);
    	if (Utility.isSet(temp)){
	    	Integer value = new Integer(temp);
	    	view.setExpiryInDays(temp);
	    	view.setReqPasswordResetInDays(true);
	    }
	    
	    if (view.isReqPasswordResetInDays()){ // if RESET_PASSWORD_WITHIN_DAYS > 0
	    	temp = getStorePropertyValue(storeId, RefCodeNames.PROPERTY_TYPE_CD.NOTIFY_PASSWORD_EXPIRY_IN_DAYS);
		    q.setParameter("shortDesc", RefCodeNames.PROPERTY_TYPE_CD.NOTIFY_PASSWORD_EXPIRY_IN_DAYS);		    
    		if (Utility.isSet(temp)){
		    	view.setNotifyExpiryInDays(temp);
		    }
	    }
	    
    	return view;
    }
    
    private String getStorePropertyValue(Long storeId, String propertyTypeCd){
    	Query q = em.createQuery("select property.value from PropertyData property " +
    	        " where property.busEntityId = (:storeId)" +
    	        " and property.propertyTypeCd = (:propertyTypeCd) ")
    	        .setParameter("storeId", storeId)
    	        .setParameter("propertyTypeCd", propertyTypeCd);
        List v = q.getResultList();
    	if (v.size() > 0){
    		return (String) v.get(0);
    	}
    	return null;
    }
    
    @Override
    public ProfilePasswordMgrView savePasswordManagementInfo(ProfilePasswordMgrView passwordView) {
    	Query q = em.createQuery("select storeProfiles from StoreProfileData storeProfiles " +
    	        " where storeProfiles.storeId = (:storeId)" +
    	        " and storeProfiles.shortDesc = (:shortDesc) ")
    	        .setParameter("storeId", passwordView.getStoreId())
    	        .setParameter("shortDesc", RefCodeNames.STORE_PROFILE_FIELD.CHANGE_PASSWORD);
    	List<StoreProfileData> v = q.getResultList();
    	StoreProfileData profileProperty = null;
    	if (v.size() > 0){
    		profileProperty = v.get(0);
    	}else{
    		profileProperty = new StoreProfileData();
            profileProperty.setStoreId(passwordView.getStoreId());
            profileProperty.setShortDesc(RefCodeNames.STORE_PROFILE_FIELD.CHANGE_PASSWORD);
            profileProperty.setOptionTypeCd(RefCodeNames.STORE_PROFILE_TYPE_CD.FIELD_OPTION);
    	}
    	if (passwordView.isAllowChangePassword())
        	profileProperty.setDisplay(Constants.TRUE);
        else
        	profileProperty.setDisplay(Constants.FALSE);
    	
    	if (profileProperty.getStoreProfileId() == null){
    		profileProperty = super.create(profileProperty);
        } else {
        	profileProperty = super.update(profileProperty);
        }
    	
    	String propertyValue = passwordView.isReqPasswordResetUponInitLogin() ? Constants.TRUE : Constants.FALSE;
    	updateStoreProperty(passwordView.getStoreId(), RefCodeNames.PROPERTY_TYPE_CD.RESET_PASSWORD_UPON_INIT_LOGIN, propertyValue);
    	updateStoreProperty(passwordView.getStoreId(), RefCodeNames.PROPERTY_TYPE_CD.RESET_PASSWORD_WITHIN_DAYS, passwordView.getExpiryInDays());
    	updateStoreProperty(passwordView.getStoreId(), RefCodeNames.PROPERTY_TYPE_CD.NOTIFY_PASSWORD_EXPIRY_IN_DAYS, passwordView.getNotifyExpiryInDays());
        	
    	return null;
    }

	private PropertyData updateStoreProperty(Long storeId, String propertyTypeCd, String value) {
		Query q = em.createQuery("select property from PropertyData property " +
    	        " where property.busEntityId = (:storeId)" +
    	        " and property.shortDesc = (:shortDesc) ")
    	        .setParameter("storeId", storeId)
    	        .setParameter("shortDesc", propertyTypeCd);
		List<PropertyData> v = q.getResultList();
		PropertyData property = null;
		if (v.size() > 0){
			property = v.get(0);
		}else{
    		property = new PropertyData();
            property.setBusEntityId(storeId);
            property.setPropertyStatusCd(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);
            property.setShortDesc(propertyTypeCd);
            property.setPropertyTypeCd(propertyTypeCd);
    	}
    	property.setValue(value);
    	if (property.getPropertyId()==null){
    		property = super.create(property);
        } else {
        	property = super.update(property);
        }
    	return property;
	}
}
