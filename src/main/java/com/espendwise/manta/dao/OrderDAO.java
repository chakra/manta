package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.*;

import com.espendwise.manta.model.view.FreightHandlerView;
import com.espendwise.manta.model.view.InvoiceCustView;
import com.espendwise.manta.model.view.ItemContractCostView;
import com.espendwise.manta.model.view.NoteJoinView;
import com.espendwise.manta.model.view.OrderChangeRequestView;
import com.espendwise.manta.model.view.OrderHeaderView;
import com.espendwise.manta.model.view.OrderIdentView;
import com.espendwise.manta.model.view.OrderItemIdentView;
import com.espendwise.manta.model.view.OrderListView;
import com.espendwise.manta.util.criteria.OrderListViewCriteria;
import java.util.Collection;

import java.util.List;

public interface OrderDAO {
    
    public List<OrderListView> findOrdersByCriteria(OrderListViewCriteria criteria);
    
    public OrderHeaderView findOrderHeader(Long orderId);
    
    public OrderIdentView findOrderToEdit(Long storeId, Long orderId);
    
    public List<OrderPropertyData> findOrderProperties(Long orderId, List<String> propertyTypes);
    
    public List<OrderMetaData> findOrderMeta(Long orderId);

    public List<OrderItemIdentView> findOrderItems(Long orderId, String erpPoNum, Long purchaseOrderId);
    
    public List<OrderItemData> findOrderItemDataList(Long orderId);
    
    public List<FreightHandlerView> getFreightHandlerDetails(List<Long> freightHandlerIds);
    
    public List<OrderAddressData> findOrderAddresses(Long orderId);
    
    public List<NoteJoinView> getSiteCrcNotes(Long siteId);
    
    public List<InvoiceCustView> getInvoiceCustByWebOrderNum(String webOrderNum, Long storeId, Boolean fullInfo);
    
    public List<ItemContractCostView> getItemContractCost(Long accountId, Long itemSkuNum, Long distId, String distErpNum);
    
    public List<ItemData> getItemDataList(Collection<Long> itemIds);
    
    public OrderData cancelOrder(Long orderId);
    
    public void updateOrder(OrderChangeRequestView changeRequest);
}
