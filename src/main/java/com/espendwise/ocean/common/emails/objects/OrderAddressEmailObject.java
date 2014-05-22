package com.espendwise.ocean.common.emails.objects;


public interface OrderAddressEmailObject extends EmailObject {

    String getStreet1();
    String getState();
    String getPostalCode();
    String getCity();


}
