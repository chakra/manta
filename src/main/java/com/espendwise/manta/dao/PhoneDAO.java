package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.PhoneData;

public interface PhoneDAO {

    public PhoneData create(PhoneData phoneData);

    public PhoneData update(PhoneData phoneData);

    public PhoneData updateEntityPhone(Long busEntityId, PhoneData phoneData);
    
    public PhoneData updateUserPhone(Long userId, PhoneData phoneData);
    
    public PhoneData findByPhoneId(Long phoneId);
}
