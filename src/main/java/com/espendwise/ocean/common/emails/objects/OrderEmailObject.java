package com.espendwise.ocean.common.emails.objects;


import java.math.BigDecimal;
import java.util.Date;

public interface OrderEmailObject extends EmailObject {

    String getOrderNumber();
    String getContactName();
    OrderAddressEmailObject getShippingAddress();
    String getComments();
    BigDecimal getTotalCost();
    Date getOriginalOrderDate();
    AccountEmailObject getAccount();
    OrderSiteEmailObject getSite();
}
