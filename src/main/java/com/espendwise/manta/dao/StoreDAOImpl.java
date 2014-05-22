package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CountryPropertyData;
import com.espendwise.manta.model.data.EmailData;
import com.espendwise.manta.model.data.PhoneData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.StoreProfileData;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.StoreIdentView;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class StoreDAOImpl extends DAOImpl implements StoreDAO {

    private static final Logger logger = Logger.getLogger(StoreDAOImpl.class);

    public StoreDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public BusEntityData findStoreById(Long storeId) {
        return em.find(BusEntityData.class, storeId);
    }

    @Override
    public List<BusEntityData> findUserStores(Long userId) {

        logger.info("findUserStores()=> BEGIN,  userId: "+userId);

        Query q = em.createQuery("select stores from BusEntityData stores, UserAssocData userStores " +
                " where stores.busEntityTypeCd = (:storeCd)" +
                " and stores.busEntityId = userStores.busEntityId " +
                " and userStores.userAssocCd = (:userAssocd)" +
                " and userStores.userId = (:userId)");

        q.setParameter("storeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
        q.setParameter("userAssocd", RefCodeNames.USER_ASSOC_CD.STORE);
        q.setParameter("userId", userId);

        List<BusEntityData> r = (List<BusEntityData>) q.getResultList();

        logger.info("findUserStores()=> END, fetched " + r.size() +" rows");

        return r;
    }
    
    @Override
    public List<BusEntityData> findStores() {

        logger.info("findStores()=> BEGIN");

        Query q = em.createQuery("select stores from BusEntityData stores" +
                " where stores.busEntityTypeCd = (:storeCd)");

        q.setParameter("storeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);

        List<BusEntityData> r = (List<BusEntityData>) q.getResultList();

        logger.info("findStores()=> END, fetched " + r.size() +" rows");

        return r;
    }
    
    @Override
    public EntityHeaderView findStoreHeader(Long storeId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.EntityHeaderView(store.busEntityId, store.shortDesc)" +
                " from  BusEntityData store where store.busEntityId = (:storeId) "
        );

        q.setParameter("storeId", storeId);

        List x = q.getResultList();

        return !x.isEmpty() ? (EntityHeaderView) x.get(0) : null;
    }


    @Override
    public List<StoreProfileData> findStoreProfile(Long storeId) {
        Query q = em.createQuery("select storeProfiles from StoreProfileData storeProfiles " +
                " where storeProfiles.storeId = (:storeId)");

        q.setParameter("storeId", storeId);

        List<StoreProfileData> r = (List<StoreProfileData>) q.getResultList();

        logger.info("findStoreProfile()=> END, fetched " + r.size() +" rows");

        return r;
    }
    
    @Override
    public StoreIdentView findStoreIdent(Long storeId) {
        StoreIdentView storeIdent = null;
        
        if (Utility.isSet(storeId)) {
            Query q = em.createQuery("Select object(store) from BusEntityData store" +
                    " where store.busEntityId = (:storeId)");

            q.setParameter("storeId", storeId);

            List<BusEntityData> stores = (List<BusEntityData>) q.getResultList();

            if (Utility.isSet(stores)) {
                storeIdent = pickupStoreIdentView(stores.get(0));
            }
        }
    
        return storeIdent;
    }
    
    private StoreIdentView pickupStoreIdentView(BusEntityData storeData) {

        if ( storeData == null || !(Utility.longNN(storeData.getBusEntityId()) > 0)) {
            return null;
        }        
        
        StoreIdentView storeIdentView = new StoreIdentView();
        storeIdentView.setStoreData(storeData);

        List<EmailData> emails = findStoreEmails(storeData.getBusEntityId());
        if (Utility.isSet(emails)) {
            for (EmailData email : emails) {
                if (RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT.equals(email.getEmailTypeCd())) {
                    if (storeIdentView.getPrimaryEmail() == null) {
                        storeIdentView.setPrimaryEmail(email);
                    }
                } else if (RefCodeNames.EMAIL_TYPE_CD.DEFAULT.equals(email.getEmailTypeCd())) {
                    if (storeIdentView.getDefaultEmail() == null) {
                        storeIdentView.setDefaultEmail(email);
                    }
                } else if (RefCodeNames.EMAIL_TYPE_CD.CUSTOMER_SERVICE.equals(email.getEmailTypeCd())) {
                    if (storeIdentView.getCustomerServiceEmail() == null) {
                        storeIdentView.setCustomerServiceEmail(email);
                    }
                } else if (RefCodeNames.EMAIL_TYPE_CD.CONTACT_US.equals(email.getEmailTypeCd())) {
                    if (storeIdentView.getContactUsEmail() == null) {
                        storeIdentView.setContactUsEmail(email);
                    }
                }
            }
        }

        List<PhoneData> phones = findStorePhones(storeData.getBusEntityId());
        if (Utility.isSet(phones)) {
            for (PhoneData phone : phones) {
                if (RefCodeNames.PHONE_TYPE_CD.PHONE.equals(phone.getPhoneTypeCd())) {
                    if (storeIdentView.getPrimaryPhone() == null) {
                        storeIdentView.setPrimaryPhone(phone);
                    }
                } else if (RefCodeNames.PHONE_TYPE_CD.FAX.equals(phone.getPhoneTypeCd())) {
                    if (storeIdentView.getPrimaryFax() == null) {
                        storeIdentView.setPrimaryFax(phone);
                    }
                }
            }
        }

        List<AddressData> addresses = findStoreAddresses(storeData.getBusEntityId());
        if (Utility.isSet(addresses)) {
            storeIdentView.setPrimaryAddress(addresses.get(0));
        }

        storeIdentView.setCountryProperties(findCountryProperties(storeIdentView.getPrimaryAddress()));
        
        List<PropertyData> properties = findStoreProperties(storeData.getBusEntityId());
        if (Utility.isSet(properties)) {
            Iterator<PropertyData> it = properties.iterator();
            PropertyData prop;
            while (it.hasNext()) {
                prop = it.next();
                if (RefCodeNames.PROPERTY_TYPE_CD.STORE_PREFIX_CODE.equals(prop.getPropertyTypeCd())) {
                    if (storeIdentView.getStorePrefix() == null) {
                        storeIdentView.setStorePrefix(prop);
                        it.remove();
                    }
                } else if (RefCodeNames.PROPERTY_TYPE_CD.STORE_TYPE_CD.equals(prop.getPropertyTypeCd())) {
                    if (storeIdentView.getStoreType() == null) {
                        storeIdentView.setStoreType(prop);
                        it.remove();
                    }
                } else if (RefCodeNames.PROPERTY_TYPE_CD.STORE_PRIMARY_WEB_ADDRESS.equals(prop.getPropertyTypeCd())) {
                    if (storeIdentView.getStorePrimaryWebAddress() == null) {
                        storeIdentView.setStorePrimaryWebAddress(prop);
                        it.remove();
                    }
                } else if (RefCodeNames.PROPERTY_TYPE_CD.STORE_BUSINESS_NAME.equals(prop.getPropertyTypeCd())) {
                    if (storeIdentView.getStoreBusinessName() == null) {
                        storeIdentView.setStoreBusinessName(prop);
                        it.remove();
                    }
                } else if (RefCodeNames.PROPERTY_TYPE_CD.CALL_HOURS.equals(prop.getPropertyTypeCd())) {
                    if (storeIdentView.getCallHours() == null) {
                        storeIdentView.setCallHours(prop);
                        it.remove();
                    }
                }
            }
            if (Utility.isSet(properties)) {
                storeIdentView.setMiscProperties(properties);
            }
        }

        return storeIdentView;
    }
    
    private List<EmailData> findStoreEmails(Long storeId) {
        Query q = em.createQuery("Select object(email) from EmailData email" +
                " where email.emailStatusCd = (:emailStatusCd)" +
                " and email.busEntityId =(:storeId)");
        
        q.setParameter("storeId", storeId);
        q.setParameter("emailStatusCd", RefCodeNames.EMAIL_STATUS_CD.ACTIVE);

        List<EmailData> emails = (List<EmailData>) q.getResultList();
        
        return emails;
    }
    
    private List<PhoneData> findStorePhones(Long storeId) {
        Query q = em.createQuery("Select object(phone) from PhoneData phone" +
                " where phone.phoneStatusCd = (:phoneStatusCd)" +
                " and phone.busEntityId =(:storeId)");
        
        q.setParameter("storeId", storeId);
        q.setParameter("phoneStatusCd", RefCodeNames.PHONE_STATUS_CD.ACTIVE);

        List<PhoneData> phones = (List<PhoneData>) q.getResultList();
        
        return phones;
    }
    
    private List<AddressData> findStoreAddresses(Long storeId) {
        Query q = em.createQuery("Select object(address) from AddressData address" +
                " where address.addressStatusCd = (:addressStatusCd)" +
                " and address.busEntityId =(:storeId)" + 
                " and address.addressTypeCd in (:addressTypeCd)" +
                " order by address.addressTypeCd");
        
        q.setParameter("storeId", storeId);

        List<String> addressTypes = new ArrayList<String>();
        addressTypes.add(RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        addressTypes.add(RefCodeNames.ADDRESS_TYPE_CD.SHIPPING);
        q.setParameter("addressTypeCd", addressTypes);
        q.setParameter("addressStatusCd", RefCodeNames.ADDRESS_STATUS_CD.ACTIVE);

        List<AddressData> addresses = (List<AddressData>) q.getResultList();
        
        return addresses;
    }
     
    private List<PropertyData> findStoreProperties(Long storeId) {
        Query q = em.createQuery("Select object(property) from PropertyData property" +
                " where property.busEntityId =(:storeId)");
        
        q.setParameter("storeId", storeId);
        //q.setParameter("propertyStatusCd", RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);

        List<PropertyData> properties = (List<PropertyData>) q.getResultList();
        
        return properties;
    }
     
    private List<CountryPropertyData> findCountryProperties(AddressData primaryAddress) {
        List<CountryPropertyData> countryProps = null;
        if (Utility.isSet(primaryAddress) && Utility.isSet(primaryAddress.getCountryCd())) {
            Query q = em.createQuery("Select object(countryProperty) from CountryData country, CountryPropertyData countryProperty" +
                    " WHERE country.shortDesc = (:countryCd)" +
                    " AND countryProperty.countryId = country.countryId");

            q.setParameter("countryCd", primaryAddress.getCountryCd());

            countryProps = (List<CountryPropertyData>) q.getResultList();
        }
        
        return countryProps;
    }
}
