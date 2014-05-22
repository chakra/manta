package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.PhoneData;
import com.espendwise.manta.util.Utility;

import javax.persistence.EntityManager;

public class PhoneDAOImpl extends DAOImpl implements PhoneDAO{

    public PhoneDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public PhoneData findByPhoneId(Long phoneId) {
        return em.find(PhoneData.class, phoneId);
    }
    
    @Override
    public PhoneData create(PhoneData phoneData) {
        return super.create(phoneData);
    }

    @Override
    public PhoneData update(PhoneData phoneData) {
        return super.create(phoneData);
    }

    @Override
    public PhoneData updateEntityPhone(Long busEntityId, PhoneData phoneData) {

        if (phoneData != null && Utility.longNN(busEntityId) > 0) {

            phoneData.setBusEntityId(busEntityId);

            if (phoneData.getPhoneId() == null) {
                phoneData = super.create(phoneData);
            } else {
                phoneData = super.update(phoneData);
            }

        }

        return phoneData;

    }
    
    @Override
    public PhoneData updateUserPhone(Long userId, PhoneData phoneData) {

        if (phoneData != null && Utility.longNN(userId) > 0) {
            
            if (phoneData.getPhoneId() != null && phoneData.getPhoneNum() == null) {
                PhoneData presentPhone = findByPhoneId(phoneData.getPhoneId());
                em.remove(presentPhone);
                
                return null;
            }

            phoneData.setUserId(userId);

            if (phoneData.getPhoneId() == null) {
                phoneData = super.create(phoneData);
            } else {
                phoneData = super.update(phoneData);
            }

        }

        return phoneData;

    }
}
