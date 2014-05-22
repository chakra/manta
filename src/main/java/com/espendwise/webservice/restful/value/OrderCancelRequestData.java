package com.espendwise.webservice.restful.value;

import com.espendwise.ocean.common.webaccess.LoginData;

import java.io.Serializable;


public class OrderCancelRequestData extends BaseRequestData implements Serializable, Cloneable {
    private static final long serialVersionUID = -5660836847301793035L;
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    @Override
    public Object clone() {
        if (this instanceof OrderCancelRequestData) {
            OrderCancelRequestData myClone = new OrderCancelRequestData();
            myClone.setAccessToken(this.accessToken);
            myClone.setLoginData(this.loginData);
            myClone.setOrderId(this.orderId);

            return myClone;
        } else {
            try {
                return super.clone();
            } catch (CloneNotSupportedException cnse) {
                throw new RuntimeException(cnse);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public Object expand(Class clazz) {
        Object exp = null;
        Class superClass = clazz.getSuperclass();
        if (superClass.equals(this.getClass())) {
            try {
                exp = clazz.newInstance();
                
                ((OrderCancelRequestData)exp).setAccessToken(this.accessToken);
                ((OrderCancelRequestData)exp).setLoginData(this.loginData);
                ((OrderCancelRequestData)exp).setOrderId(this.orderId);
            
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        
        return exp;
    }

}
