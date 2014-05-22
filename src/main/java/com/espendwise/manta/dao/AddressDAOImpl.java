package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.view.ContactView;
import com.espendwise.manta.util.Utility;

import javax.persistence.EntityManager;

public class AddressDAOImpl extends DAOImpl implements AddressDAO{


    public AddressDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public AddressData create(AddressData address) {
        return super.create(address);
    }

    @Override
    public AddressData update(AddressData addressData) {
        return super.update(addressData);  
    }

    @Override
    public AddressData updateEntityAddress(Long busEntityId, AddressData addressData) {

        if (addressData != null && Utility.longNN(busEntityId) > 0) {

            addressData.setBusEntityId(busEntityId);

            if (addressData.getAddressId() == null) {
                addressData = super.create(addressData);
            } else {
                addressData = super.update(addressData);
            }

        }

        return addressData;

    }
    
    @Override
    public AddressData updateUserAddress(Long userId, AddressData addressData) {

        if (addressData != null && Utility.longNN(userId) > 0) {

            addressData.setUserId(userId);

            if (addressData.getAddressId() == null) {
                addressData = super.create(addressData);
            } else {
                addressData = super.update(addressData);
            }

        }

        return addressData;

    }

    @Override
    public void removeContact(ContactView contact) {

        if (contact != null) {

            if (contact.getAddress() != null) {
                em.remove(contact.getAddress());
            }

            if (contact.getEmail() != null) {
                em.remove(contact.getEmail());
            }

            if (contact.getFaxPhone() != null) {
                em.remove(contact.getFaxPhone());
            }

            if (contact.getMobilePhone() != null) {
                em.remove(contact.getMobilePhone());
            }

            if (contact.getPhone() != null) {
                em.remove(contact.getPhone());
            }

        }
    }
}
