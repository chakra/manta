package com.espendwise.manta.service;

import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.OrderListViewCriteria;
import java.util.Collection;

import java.util.List;

public interface OrderService {
    
    public List<OrderListView> findOrdersByCriteria(OrderListViewCriteria criteria);
    
    public OrderHeaderView findOrderHeader(Long orderId);
    
    public OrderIdentView findOrderToEdit(Long storeId, Long orderId);
    
    public List<OrderPropertyData> findOrderProperties(Long orderId, List<String> propertyTypes);
    
    public List<OrderMetaData> findOrderMeta(Long orderId);
    
    public List<OrderItemIdentView> findOrderItems(Long orderId, String erpPoNum, Long purchaseOrderId);
    
    public List<OrderItemData> findOrderItemDataList(Long orderId);
    
    public List<FreightHandlerView> getFreightHandlerDetails(List<Long> freightHandlerId);

    public List<OrderAddressData> findOrderAddresses(Long orderId);
    
    public List<NoteJoinView> getSiteCrcNotes(Long siteId);
    
    public List<InvoiceCustView> getInvoiceCustByWebOrderNum(String webOrderNum, Long storeId, Boolean fullInfo);
    
    public List<ItemContractCostView> getItemContractCost(Long accountId, Long itemSkuNum, Long distId, String distErpNum);
    
    public List<ItemData> getItemDataList(Collection<Long> itemIds);
    
    public OrderData cancelOrder(Long orderId);
    
    public void updateOrder(OrderChangeRequestView changeRequest);
}
