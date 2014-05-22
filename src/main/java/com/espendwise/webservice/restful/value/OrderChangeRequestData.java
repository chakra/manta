package com.espendwise.webservice.restful.value;

import com.espendwise.manta.model.data.OrderData;
import com.espendwise.manta.model.data.OrderItemData;
import com.espendwise.manta.model.data.OrderMetaData;

import com.espendwise.ocean.common.webaccess.LoginData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class OrderChangeRequestData extends BaseRequestData implements Serializable, Cloneable {
    private static final long serialVersionUID = -5660836848901793035L;
    private OrderData orderData;
    private List<OrderMetaData> orderMeta;
    private List<OrderItemData> orderItems;
    private List<OrderItemDescData> orderItemsDesc;
    private List<Long> deleteOrderItemIds;
    private Date newOrderDate;
    private BigDecimal totalFreightCostValue;
    private BigDecimal totalMiscCostValue;
    private BigDecimal smallOrderFeeValue;
    private BigDecimal fuelSurChargeValue;
    private BigDecimal discountValue;
    private Long newSiteId;
    private Boolean rebillOrder;
    

    public OrderChangeRequestData() {}
    
    public Date getNewOrderDate() {
        return newOrderDate;
    }

    public void setNewOrderDate(Date newOrderDate) {
        this.newOrderDate = newOrderDate;
    }

    public List<Long> getDeleteOrderItemIds() {
        return deleteOrderItemIds;
    }

    public void setDeleteOrderItemIds(List<Long> deleteOrderItemIds) {
        this.deleteOrderItemIds = deleteOrderItemIds;
    }

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    public List<OrderItemData> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemData> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderMetaData> getOrderMeta() {
        return orderMeta;
    }

    public void setOrderMeta(List<OrderMetaData> orderMeta) {
        this.orderMeta = orderMeta;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getFuelSurChargeValue() {
        return fuelSurChargeValue;
    }

    public void setFuelSurChargeValue(BigDecimal fuelSurChargeValue) {
        this.fuelSurChargeValue = fuelSurChargeValue;
    }

    public Long getNewSiteId() {
        return newSiteId;
    }

    public void setNewSiteId(Long newSiteId) {
        this.newSiteId = newSiteId;
    }

    public Boolean getRebillOrder() {
        return rebillOrder;
    }

    public void setRebillOrder(Boolean rebillOrder) {
        this.rebillOrder = rebillOrder;
    }

    public BigDecimal getSmallOrderFeeValue() {
        return smallOrderFeeValue;
    }

    public void setSmallOrderFeeValue(BigDecimal smallOrderFeeValue) {
        this.smallOrderFeeValue = smallOrderFeeValue;
    }

    public BigDecimal getTotalFreightCostValue() {
        return totalFreightCostValue;
    }

    public void setTotalFreightCostValue(BigDecimal totalFreightCostValue) {
        this.totalFreightCostValue = totalFreightCostValue;
    }

    public BigDecimal getTotalMiscCostValue() {
        return totalMiscCostValue;
    }

    public void setTotalMiscCostValue(BigDecimal totalMiscCostValue) {
        this.totalMiscCostValue = totalMiscCostValue;
    }

    public List<OrderItemDescData> getOrderItemsDesc() {
        return orderItemsDesc;
    }

    public void setOrderItemsDesc(List<OrderItemDescData> orderItemsDesc) {
        this.orderItemsDesc = orderItemsDesc;
    }
    
    @Override
    public Object clone() {
        if (this instanceof OrderChangeRequestData) {
            OrderChangeRequestData myClone = new OrderChangeRequestData();
            myClone.setAccessToken(this.accessToken);
            myClone.setLoginData(this.loginData);
            myClone.setOrderData(this.orderData);
            myClone.setOrderMeta(this.orderMeta);
            myClone.setOrderItems(this.orderItems);
            myClone.setOrderItemsDesc(this.orderItemsDesc);
            myClone.setDeleteOrderItemIds(this.deleteOrderItemIds);
            myClone.setNewOrderDate(this.newOrderDate);
            myClone.setTotalFreightCostValue(this.totalFreightCostValue);
            myClone.setTotalMiscCostValue(this.totalMiscCostValue);
            myClone.setSmallOrderFeeValue(this.smallOrderFeeValue);
            myClone.setFuelSurChargeValue(this.fuelSurChargeValue);
            myClone.setDiscountValue(this.discountValue);
            myClone.setNewSiteId(this.newSiteId);
            myClone.setRebillOrder(this.rebillOrder);

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
                
                ((OrderChangeRequestData)exp).setAccessToken(this.accessToken);
                ((OrderChangeRequestData)exp).setLoginData(this.loginData);
                ((OrderChangeRequestData)exp).setOrderData(this.orderData);
                ((OrderChangeRequestData)exp).setOrderMeta(this.orderMeta);
                ((OrderChangeRequestData)exp).setOrderItems(this.orderItems);
                ((OrderChangeRequestData)exp).setOrderItemsDesc(this.orderItemsDesc);
                ((OrderChangeRequestData)exp).setDeleteOrderItemIds(this.deleteOrderItemIds);
                ((OrderChangeRequestData)exp).setNewOrderDate(this.newOrderDate);
                ((OrderChangeRequestData)exp).setTotalFreightCostValue(this.totalFreightCostValue);
                ((OrderChangeRequestData)exp).setTotalMiscCostValue(this.totalMiscCostValue);
                ((OrderChangeRequestData)exp).setSmallOrderFeeValue(this.smallOrderFeeValue);
                ((OrderChangeRequestData)exp).setFuelSurChargeValue(this.fuelSurChargeValue);
                ((OrderChangeRequestData)exp).setDiscountValue(this.discountValue);
                ((OrderChangeRequestData)exp).setNewSiteId(this.newSiteId);
                ((OrderChangeRequestData)exp).setRebillOrder(this.rebillOrder);
            
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        
        return exp;
    }

}


