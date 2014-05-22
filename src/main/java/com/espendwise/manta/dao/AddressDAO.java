package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.view.ContactView;

public interface AddressDAO {

    public AddressData create(AddressData addressData);

    public AddressData update(AddressData addressData);

    public AddressData updateEntityAddress(Long busEntityId, AddressData addressData);
    
    public AddressData updateUserAddress(Long userId, AddressData addressData);

    public void removeContact(ContactView contact);
}
