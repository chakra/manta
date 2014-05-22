package com.espendwise.webservice.restful.value;

import java.io.Serializable;

import com.espendwise.manta.model.data.OrderItemActionData;
import java.math.BigDecimal;

public class OrderItemActionDescData implements Serializable {

    private static final long serialVersionUID = -7816138406663611385L;

    private OrderItemActionData orderItemAction;
    private Integer itemSkuNum;
    private String itemPack;
    private String distItemSkuNum;
    private String itemShortDesc;
    private String itemUom;
    private BigDecimal itemDistCost;

    public OrderItemActionDescData() {}

    public OrderItemActionDescData(OrderItemActionData orderItemAction) {
        this.setOrderItemAction(orderItemAction);
    }

    public OrderItemActionDescData(OrderItemActionData orderItemAction,
                                   Integer itemSkuNum,
                                   String itemPack,
                                   String distItemSkuNum,
                                   String itemShortDesc,
                                   String itemUom,
                                   BigDecimal itemDistCost) {
        this.setOrderItemAction(orderItemAction);
        this.setItemSkuNum(itemSkuNum);
        this.setItemPack(itemPack);
        this.setDistItemSkuNum(distItemSkuNum);
        this.setItemShortDesc(itemShortDesc);
        this.setItemUom(itemUom);
        this.setItemDistCost(itemDistCost);
    }

    public OrderItemActionData getOrderItemAction() {
        return this.orderItemAction;
    }

    public void setOrderItemAction(OrderItemActionData orderItemAction) {
        this.orderItemAction = orderItemAction;
    }

    public Integer getItemSkuNum() {
        return this.itemSkuNum;
    }

    public void setItemSkuNum(Integer itemSkuNum) {
        this.itemSkuNum = itemSkuNum;
    }

    public String getItemPack() {
        return this.itemPack;
    }

    public void setItemPack(String itemPack) {
        this.itemPack = itemPack;
    }

    public String getDistItemSkuNum() {
        return this.distItemSkuNum;
    }

    public void setDistItemSkuNum(String distItemSkuNum) {
        this.distItemSkuNum = distItemSkuNum;
    }

    public String getItemShortDesc() {
        return this.itemShortDesc;
    }

    public void setItemShortDesc(String itemShortDesc) {
        this.itemShortDesc = itemShortDesc;
    }

    public String getItemUom() {
        return this.itemUom;
    }

    public void setItemUom(String itemUom) {
        this.itemUom = itemUom;
    }

    public BigDecimal getItemDistCost() {
        return this.itemDistCost;
    }

    public void setItemDistCost(BigDecimal itemDistCost) {
        this.itemDistCost = itemDistCost;
    }
}