package com.espendwise.manta.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.data.AssetData;
import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.model.data.ContractData;
import com.espendwise.manta.model.data.ContractItemData;
import com.espendwise.manta.model.data.DistShippingOptionsData;
import com.espendwise.manta.model.data.InvoiceCustData;
import com.espendwise.manta.model.data.InvoiceCustDetailData;
import com.espendwise.manta.model.data.InvoiceDistData;
import com.espendwise.manta.model.data.InvoiceDistDetailData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.data.ItemSubstitutionData;
import com.espendwise.manta.model.data.NoteData;
import com.espendwise.manta.model.data.NoteTextData;
import com.espendwise.manta.model.data.OrderAddOnChargeData;
import com.espendwise.manta.model.data.OrderAddressData;
import com.espendwise.manta.model.data.OrderAssocData;
import com.espendwise.manta.model.data.OrderCreditCardData;
import com.espendwise.manta.model.data.OrderData;
import com.espendwise.manta.model.data.OrderFreightData;
import com.espendwise.manta.model.data.OrderItemActionData;
import com.espendwise.manta.model.data.OrderItemData;
import com.espendwise.manta.model.data.OrderItemMetaData;
import com.espendwise.manta.model.data.OrderMetaData;
import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.data.PreOrderData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.PurchaseOrderData;
import com.espendwise.manta.model.data.ShoppingInfoData;
import com.espendwise.manta.model.data.TradingProfileConfigData;
import com.espendwise.manta.model.view.DistOptionsForShippingView;
import com.espendwise.manta.model.view.FreightHandlerView;
import com.espendwise.manta.model.view.InvoiceCustInfoView;
import com.espendwise.manta.model.view.InvoiceCustView;
import com.espendwise.manta.model.view.ItemContractCostView;
import com.espendwise.manta.model.view.NoteJoinView;
import com.espendwise.manta.model.view.OrderChangeRequestView;
import com.espendwise.manta.model.view.OrderExtraInfoView;
import com.espendwise.manta.model.view.OrderHeaderView;
import com.espendwise.manta.model.view.OrderIdentView;
import com.espendwise.manta.model.view.OrderItemActionDescView;
import com.espendwise.manta.model.view.OrderItemIdentView;
import com.espendwise.manta.model.view.OrderListView;
import com.espendwise.manta.model.view.ReplacedOrderItemView;
import com.espendwise.manta.model.view.ReplacedOrderView;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.OrderUtil;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.OrderListViewCriteria;

/**
 * Sample Order DAO class for fetching orders.
 */
public class OrderDAOImpl extends DAOImpl implements OrderDAO {

    protected final Logger logger = Logger.getLogger(getClass());

    public OrderDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }
    
    @Override
    public List<OrderListView> findOrdersByCriteria(OrderListViewCriteria criteria) {
        List<OrderListView> orders = Utility.emptyList(OrderListView.class);
        
        logger.info("findOrdersByCriteria() => BEGIN, criteria : " + criteria);
        
        StringBuilder baseQuery = new StringBuilder("Select DISTINCT new com.espendwise.manta.model.view.OrderListView");

        /*  private Long orderId;
            private String accountName;
            private String distributerD;
            private String webOrderNum;
            private Date originalOrderDate;
            private Date revisedOrderDate;
            private String customerPoNum;
            private String siteName;
            private String address1;
            private String city;
            private String stateProvince;
            private String postalCode;
            private String orderStatus;
            private String orderSource;
            private String placedBy;
          */
        
        baseQuery.append("(orderInfo.orderId," +
                         " accountName.shortDesc," +
                         " items.distErpNum," +   // distributerD 
                         " orderInfo.orderNum," + // webOrderNum
                         " orderInfo.originalOrderDate," +
                         " orderInfo.revisedOrderDate," +
                         " orderInfo.originalOrderDate," +
                         " orderInfo.requestPoNum," + //customer PO #
                         " siteName.shortDesc," +
                         " address.address1," +
                         " address.city," +
                         " address.stateProvinceCd," +
                         " address.postalCode," +
                         " orderInfo.orderStatusCd," +
                         " orderInfo.orderSourceCd," +
                         " orderInfo.addBy)" +
                         " FROM OrderFullEntity orderInfo" +
                         " left outer join orderInfo.accountId accountName" +
                         " left outer join orderInfo.siteId siteName" +
                         " left outer join orderInfo.orderAddresses address with address.addressTypeCd = " + QueryHelp.toQuoted(RefCodeNames.ADDRESS_TYPE_CD.SHIPPING) +
                         " left outer join orderInfo.orderItems items");

                         /*
                          * SELECT distinct orderInfo.order_id,
                                   orderInfo.order_num,
                                   orderItemD.dist_erp_num,
                                   distributerD.short_desc

                                   FROM clw_order orderInfo left join clw_order_item orderItemD ON orderInfo.order_id = orderItemD.order_id 
                                                            left join clw_bus_entity distributerD ON orderItemD.dist_erp_num = distributerD.erp_num AND
                                        distributerD.bus_entity_type_cd = 'DISTRIBUTOR' order by orderInfo.order_id;
                          */
        
        baseQuery.append(" where orderInfo.storeId IN (:storeIds)");

        /*
        if (criteria.getOrderFromDate() != null) {
            baseQuery.append(" and orderInfo.originalOrderDate >= (:orderFromDate)");
        }
        
        if (criteria.getOrderToDate() != null) {
            baseQuery.append(" and orderInfo.originalOrderDate <= (:orderToDate)");
        }
        */
        
        if (criteria.getOrderFromDate() != null || criteria.getOrderToDate() != null) {
            String originCondition = "";
            String revisedCondition = "";

            if (criteria.getOrderFromDate() != null) {
                originCondition = " AND orderInfo.originalOrderDate >= (:orderFromDate)";
                revisedCondition = " AND orderInfo.revisedOrderDate >= (:orderFromDate)";
            }
            if (criteria.getOrderToDate() != null) {
                originCondition += " AND orderInfo.originalOrderDate <= (:orderToDate)";
                revisedCondition += " AND orderInfo.revisedOrderDate <= (:orderToDate)";
            }
            
            baseQuery.append(" AND (");
            baseQuery.append("(orderInfo.revisedOrderDate IS NULL " + originCondition + ")");
            baseQuery.append(" OR (orderInfo.revisedOrderDate IS NOT NULL " + revisedCondition + ")");
            baseQuery.append(")");
        }

        if (Utility.isSet(criteria.getErpPoNum())) {
            baseQuery.append(" and items.erpPoNum = " + QueryHelp.toQuoted(criteria.getErpPoNum()));
	}

        if (Utility.isSet(criteria.getWebOrderConfirmationNum())) {            
            baseQuery.append(" and (orderInfo.orderNum = " + QueryHelp.toQuoted(criteria.getWebOrderConfirmationNum()));
            baseQuery.append(" or orderInfo.orderId IN (Select refOrderId from OrderData where orderNum = " + QueryHelp.toQuoted(criteria.getWebOrderConfirmationNum()) + "))");
        }

        if (Utility.isSet(criteria.getOutboundPoNum())) {
            baseQuery.append(" and items.outboundPoNum = " + QueryHelp.toQuoted(criteria.getOutboundPoNum()));
	}
        
        if (Utility.isSet(criteria.getCustomerPoNum())) {
            baseQuery.append(" and UPPER(orderInfo.requestPoNum) like ");
            baseQuery.append(QueryHelp.toQuoted(
                                QueryHelp.startWith(criteria.getCustomerPoNum().trim().toUpperCase())));
	}
        
        if (Utility.isSet(criteria.getRefOrderNum())) {
            baseQuery.append(" and UPPER(orderInfo.refOrderNum) like ");
            baseQuery.append(QueryHelp.toQuoted(
                                QueryHelp.contains(criteria.getRefOrderNum().trim().toUpperCase())));
	}
        
        if (Utility.isSet(criteria.getAccountFilter())) {
            baseQuery.append(" and orderInfo.accountId.busEntityId").append(criteria.getAccountFilter().size() == 1 ? " = " : " in ").append("(:accountIds)");
        }
        
        if (Utility.isSet(criteria.getSiteFilter())) {
            baseQuery.append(" and orderInfo.siteId.busEntityId").append(criteria.getSiteFilter().size() == 1 ? " = " : " in ").append("(:siteIds)");
        }
        
        if (Utility.isSet(criteria.getMethod())) {
            baseQuery.append(" and orderInfo.orderSourceCd = (:method)");
        }
        
        if (Utility.isSet(criteria.getOrderStatuses())) {
            baseQuery.append(" and orderInfo.orderStatusCd").append(criteria.getOrderStatuses().size() == 1 ? " = " : " in ").append("(:statuses)");
        }
        
        if (Utility.isSet(criteria.getSiteZipCode())) {
            baseQuery.append(" and UPPER(address.postalCode) like ");
            baseQuery.append(QueryHelp.toQuoted(
                                QueryHelp.startWith(criteria.getSiteZipCode().trim().toUpperCase())));
        }
        
        if (Utility.isSet(criteria.getUserFilter())) {
            baseQuery.append(" and orderInfo.addBy").append(criteria.getUserFilter().size() == 1 ? " = " : " in ").append("(:userNames)");
        }
        
        //----------------------------------------------------------------------
        
        String query = baseQuery.toString();
        Query q = em.createQuery(query);
        
        q.setParameter("storeIds", criteria.getStoreIds());
        
        if (criteria.getOrderFromDate() != null) {
            q.setParameter("orderFromDate", criteria.getOrderFromDate());
        }
        
        if (criteria.getOrderToDate() != null) {
            q.setParameter("orderToDate", criteria.getOrderToDate());
        }
        
        if (Utility.isSet(criteria.getAccountFilter())) {
            q.setParameter("accountIds", criteria.getAccountFilter());
        }
        
        if (Utility.isSet(criteria.getSiteFilter())) {
            q.setParameter("siteIds", criteria.getSiteFilter());
        }
        
        if (Utility.isSet(criteria.getUserFilter())) {
            q.setParameter("userNames", criteria.getUserFilter());
        }
        
        if (Utility.isSet(criteria.getMethod())) {
            q.setParameter("method", criteria.getMethod());
        }
        
        if (Utility.isSet(criteria.getOrderStatuses())) {
            q.setParameter("statuses", criteria.getOrderStatuses());
        }

        if (Utility.isSet(criteria.getLimit())) {
            q.setMaxResults(criteria.getLimit());
        }

        orders = q.getResultList();
        orders = reworkDistributors(orders, criteria.getDistibutorFilter());
        
        logger.info("findOrdersByCriteria() => END, fetched : " + orders.size() + " rows");
        
        return orders;
    }
    
    private List<OrderListView> reworkDistributors(List<OrderListView> orders, List<Long> distributorFilter) {
        Map<Long, OrderListView> result = new HashMap<Long, OrderListView>();
        
        if (Utility.isSet(orders)) {
            StringBuilder baseQuery = new StringBuilder("Select DISTINCT object(distName) FROM BusEntityData distName, OrderItemData item");
                baseQuery.append(" WHERE distName.busEntityTypeCd = (:busEntityType)");
                baseQuery.append(" AND distName.erpNum = item.distErpNum");

            Query q = em.createQuery(baseQuery.toString());
            q.setParameter("busEntityType", RefCodeNames.BUS_ENTITY_TYPE_CD.DISTRIBUTOR);

            List<BusEntityData> distributors = q.getResultList();
            
            Map<String, BusEntityData> erpNumToData = new Hashtable<String, BusEntityData>();
            if (Utility.isSet(distributors)) {
                for (BusEntityData distributor : distributors) {
                    erpNumToData.put(distributor.getErpNum(), distributor);
                }
            }
            
            Set<Long> distributorIds = null;
            if (Utility.isSet(distributorFilter)) {
                distributorIds = new TreeSet<Long>(distributorFilter);
            }
            Collections.sort(orders, AppComparator.ORDER_LIST_VIEW_COMPARATOR);

            for (OrderListView order : orders) {
                BusEntityData distributerD = erpNumToData.get(Utility.strNN(order.getDistName())); //distName == distErpNum
                if (distributerD != null) {
                    String distName = distributerD.getShortDesc();
                    if (distributorIds == null || distributorIds.contains(distributerD.getBusEntityId())) {
                        if (result.containsKey(order.getOrderId())) {
                            OrderListView presentOrder = result.get(order.getOrderId());
                            String tokens[] = Utility.strNN(presentOrder.getDistName()).split(" / ");
                            boolean contains = false;
                            for (int i = 0; i < tokens.length; i++) {
                                if (distName.equals(tokens[i])) {
                                    contains = true;
                                    break;
                                }
                            }
                            if (!contains) {
                                presentOrder.setDistName(presentOrder.getDistName() + " / " + distName);
                                //result.put(presentOrder.getOrderId(), presentOrder);
                            }
                        } else {
                            order.setDistName(Utility.strNN(distName));
                            result.put(order.getOrderId(), order);
                        }
                    }
                } else {
                    if (distributorIds == null) { // no filter
                        if (!result.containsKey(order.getOrderId())) {
                            result.put(order.getOrderId(), order);
                        }
                    }
                }
            }
        }
        
        return new ArrayList<OrderListView>(result.values());
    }
    
    @Override
    public OrderHeaderView findOrderHeader(Long orderId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.OrderHeaderView(order.orderId)" +
                " from OrderData order where order.orderId = (:orderId) "
        );

        q.setParameter("orderId", orderId);

        List x = q.getResultList();

        return !x.isEmpty() ? (OrderHeaderView) x.get(0) : null;
    }

    @Override
    public OrderIdentView findOrderToEdit(Long storeId, Long orderId) {
        OrderIdentView order = null;
        
        logger.info("getOrderIdentByOrderId() => BEGIN, order ID: " + orderId);
        
        if (Utility.isSet(storeId) && Utility.isSet(orderId)) {
            
            StringBuilder baseQuery = new StringBuilder("Select object(orderInfo) from OrderData orderInfo");
            baseQuery.append(" WHERE orderInfo.orderId = (:orderId) AND orderInfo.storeId = (:storeId)");
        
            Query query = em.createQuery(baseQuery.toString());
                query.setParameter("orderId", orderId);
                query.setParameter("storeId", storeId);
        
            List<OrderData> orders = query.getResultList();
            if (Utility.isSet(orders)) {
                OrderData orderData = orders.get(0);
                if (orderData == null || !(Utility.longNN(orderData.getOrderId()) > 0)) {
                    return order;
                }

                order = new OrderIdentView();
                order.setOrderData(orderData);
                order.setAllowModification(calculateAllowModificationForOrder(orderData));
                
                baseQuery = new StringBuilder("Select new com.espendwise.manta.model.view.OrderExtraInfoView");
                baseQuery.append("(orderInfo.orderId," +
                                 " account.busEntityId," +
                                 " account.shortDesc," +
                                 " site.busEntityId," +
                                 " site.shortDesc," +
                                 " siteAddress.address1," +
                                 " siteAddress.address2," +
                                 " siteAddress.address3," +
                                 " siteAddress.address4," +
                                 " siteAddress.city," +
                                 " siteAddress.stateProvinceCd," +
                                 " siteAddress.countryCd," +
                                 " siteAddress.postalCode)" +
                                 " FROM OrderFullEntity orderInfo" +
                                 " left outer JOIN orderInfo.accountId account" +
                                 " left outer JOIN orderInfo.siteId site" +
                                 " inner JOIN site.addresses siteAddress with siteAddress.addressTypeCd in (:typeAddressCd)" + 
                                 " WHERE orderInfo.orderId = (:orderId) AND orderInfo.storeId = (:storeId)");

                query = em.createQuery(baseQuery.toString());

                query.setParameter("orderId", orderId);
                query.setParameter("storeId", storeId);
                query.setParameter("typeAddressCd", RefCodeNames.ADDRESS_TYPE_CD.SHIPPING);

                List<OrderExtraInfoView> orderExtras = query.getResultList();

                if (Utility.isSet(orderExtras)) {
                    order.setOrderExtras(orderExtras.get(0));
                }
                order.setOrderProperties(findOrderProperties(orderData.getOrderId(), null));
                order.setOrderMeta(findOrderMeta(orderData.getOrderId()));
                
                List<OrderItemData> orderItems = findOrderItemDataList(orderId);
                order.setOrderItems(orderItems);
                
                if (Utility.isSet(orderItems)) {
                    Set<Long> freightHandlersSet = new HashSet<Long>();
                            
                    initOrderItemActionProperties(order);
                    order.setErpOrderNum(orderItems.get(0).getErpOrderNum());

                    for (OrderItemData orderItem : orderItems) {
                        Long freightHandlerId = orderItem.getFreightHandlerId();
                        if (!freightHandlersSet.contains(freightHandlerId)) {
                            freightHandlersSet.add(freightHandlerId);
                        }
                    }
                    if (!freightHandlersSet.isEmpty()) {
                        List<Long> freightHandlerIds = new ArrayList<Long>();
                        freightHandlerIds.addAll(freightHandlersSet);
                        order.setShippingForThisOrder(getFreightHandlerDetails(freightHandlerIds));
                    }
                }
                
                baseQuery = new StringBuilder("Select object(creditCard) from OrderCreditCardData creditCard");
                baseQuery.append(" WHERE creditCard.orderId = (:orderId)");
                
                query = em.createQuery(baseQuery.toString());
                query.setParameter("orderId", orderId);

                List<OrderCreditCardData> creditCards = (List<OrderCreditCardData>) query.getResultList();
                if (Utility.isSet(creditCards)) {
                    order.setOrderCreditCard(creditCards.get(0));
                }
                
                List<OrderAddressData> addresses = findOrderAddresses(orderId);
                if (Utility.isSet(addresses)) {
                    for (OrderAddressData orderAddress : addresses) {
                        String addressType = orderAddress.getAddressTypeCd();
                        if (RefCodeNames.ADDRESS_TYPE_CD.SHIPPING.equals(addressType)) {
                            order.setShipTo(orderAddress);
                        } else if (RefCodeNames.ADDRESS_TYPE_CD.BILLING.equals(addressType)) {
                            order.setBillTo(orderAddress);
                        } else if (RefCodeNames.ADDRESS_TYPE_CD.CUSTOMER_BILLING.equals(addressType)) {
                            order.setCustomerBillTo(orderAddress);
                        } else if (RefCodeNames.ADDRESS_TYPE_CD.CUSTOMER_SHIPPING.equals(addressType)) {
                            order.setCustomerShipTo(orderAddress);
                        }
                    }
                }
                baseQuery = new StringBuilder("Select object(invoiceDist) from InvoiceDistData invoiceDist");
                baseQuery.append(" WHERE invoiceDist.orderId = (:orderId)");
                
                query = em.createQuery(baseQuery.toString());
                query.setParameter("orderId", orderId);
                
                List<InvoiceDistData> invoiceDistList = (List<InvoiceDistData>) query.getResultList();
                if (Utility.isSet(invoiceDistList)) {
                    Set<String> shipFromNamesSet = new HashSet<String>();
                    for (InvoiceDistData invoiceDist : invoiceDistList) {
                        String fromName = invoiceDist.getShipFromName();

                        shipFromNamesSet.add(fromName);                        
                    }
                    List<String> shipFromNames = new ArrayList<String>(shipFromNamesSet);
                    if (Utility.isSet(shipFromNames)) {
                        String shipFromName = "";
                        for (String name : shipFromNames) {
                            shipFromName += name + " / ";
                        }

                        if (shipFromName.length() > 3) {
                            order.setShipFromName(shipFromName.substring(0, shipFromName.length() - 3));
                        }
                    }
                }
                
                // Order reference number information.
                final List<String> orderPropertyTypes = new ArrayList<String>();
                orderPropertyTypes.add(RefCodeNames.ORDER_PROPERTY_TYPE_CD.ORDER_NOTE);
                orderPropertyTypes.add(RefCodeNames.ORDER_PROPERTY_TYPE_CD.CUSTOMER_ORDER_DATE);
                orderPropertyTypes.add(RefCodeNames.ORDER_PROPERTY_TYPE_CD.REQUESTED_SHIP_DATE);
                orderPropertyTypes.add(RefCodeNames.ORDER_PROPERTY_TYPE_CD.CUSTOMER_CART_COMMENTS);
                
                baseQuery = new StringBuilder("Select object(orderProperty) from OrderPropertyData orderProperty");
                baseQuery.append(" WHERE orderProperty.orderId = (:orderId)");
                baseQuery.append(" AND orderProperty.orderPropertyTypeCd NOT IN (:orderPropertyTypes)");
                
                query = em.createQuery(baseQuery.toString());
                query.setParameter("orderId", orderId);
                query.setParameter("orderPropertyTypes", orderPropertyTypes);
                
                List<OrderPropertyData> referenceNumList = (List<OrderPropertyData>) query.getResultList();

                if (Utility.isSet(referenceNumList)) {
                    order.setReferenceNums(referenceNumList);
                }

                Long refOrderId = order.getOrderData().getRefOrderId();
                if (Utility.longNN(refOrderId) > 0) {
                    baseQuery = new StringBuilder("Select object(orderD) from OrderData orderD");
                    baseQuery.append(" WHERE orderD.orderId = (:refOrderId)");

                    query = em.createQuery(baseQuery.toString());
                    query.setParameter("refOrderId", refOrderId);

                    List<OrderData> refOrders = (List<OrderData>) query.getResultList();
                    if (Utility.isSet(refOrders)) {
                        order.setRefOrder(refOrders.get(0));
                    }
                }
                
                //Get replaced orders if the order is a consolidated order
                String orderType = order.getOrderData().getOrderTypeCd();
                if (RefCodeNames.ORDER_TYPE_CD.CONSOLIDATED.equals(orderType)) {
                    order.setReplacedOrders(getReplacedOrders(orderId));
                }

                //Get consolidated order if the order was consolidated
                if (RefCodeNames.ORDER_STATUS_CD.CANCELLED.equals(order.getOrderData().getOrderStatusCd())) {
                    order.setConsolidatedOrder(getConsolidatedOrderForReplaced(orderId));
                }

                // If this order is in a state where it can be modified,
                // pull up the shipping options available
                if (OrderUtil.getAllowShipingModifications(order.getOrderData().getOrderStatusCd()) ) {
                    // Pull up the distributors in the order and freight
                    // handlers available for shipping.
                    Collection<String> distErpNums = OrderUtil.getDistERPNumsInItems(order.getOrderItems());
                    
                    if (Utility.isSet(distErpNums)) {
                        List<DistOptionsForShippingView> shippingOptions = null;
                        for (String distErpNum : distErpNums) {
                            shippingOptions = new ArrayList<DistOptionsForShippingView>();
                            
                            BusEntityData distributor = getDistributorByErpNum(distErpNum);
                            if (distributor != null) {
                                baseQuery = new StringBuilder("Select object(distShippingOptionsD) from DistShippingOptionsData distShippingOptionsD");
                                baseQuery.append(" WHERE distShippingOptionsD.distributorId = (:distributorId)");

                                query = em.createQuery(baseQuery.toString());
                                query.setParameter("distributorId", distributor.getBusEntityId());

                                List<DistShippingOptionsData> shippingOptionList = (List<DistShippingOptionsData>) query.getResultList();
                                if (Utility.isSet(shippingOptionList)) {
                                    DistShippingOptionsData shippingOption = shippingOptionList.get(0);
                                    // Look up the freight handler
                                    baseQuery = new StringBuilder("Select object(busEntity) from BusEntityData busEntity");
                                    baseQuery.append(" WHERE busEntity.busEntityId = (:freightHandlerId)");

                                    query = em.createQuery(baseQuery.toString());
                                    query.setParameter("freightHandlerId", shippingOption.getFreightHandlerId());

                                    List<BusEntityData> freightHandlers = (List<BusEntityData>) query.getResultList();
                                    if (Utility.isSet(freightHandlers)) {
                                        DistOptionsForShippingView shipOpt = new DistOptionsForShippingView();
                                        shipOpt.setDistributor(distributor);
                                        shipOpt.setFreightHandler(freightHandlers.get(0));
                                        shippingOptions.add(shipOpt);
                                    }
                                }
                            }
                        }
                        order.setDistShippingOptions(shippingOptions);
                    }
                }
            }
        }
        
        logger.info("findOrdersByCriteria() => END");
        
        return order;
    }
    
    @Override
    public List<OrderPropertyData> findOrderProperties(Long orderId, List<String> propertyTypes) {
        List<OrderPropertyData> properties;
        
        if (Utility.isSet(orderId)) {
            
            
            StringBuilder baseQuery = new StringBuilder("Select object(orderProperty) from OrderPropertyData orderProperty");
                baseQuery.append(" WHERE orderProperty.orderPropertyStatusCd = (:propertyStatusCd)");
                baseQuery.append(" AND orderProperty.orderId =(:orderId)");
                if (Utility.isSet(propertyTypes)) {
                    baseQuery.append(" AND orderProperty.orderPropertyTypeCd").append(propertyTypes.size() == 1 ? " = " : " in ").append("(:propertyTypes)");
                } else {
                    baseQuery.append(" ORDER by orderProperty.orderPropertyTypeCd");
                }

            Query q = em.createQuery(baseQuery.toString());
            q.setParameter("orderId", orderId);
            q.setParameter("propertyStatusCd", RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);
            if (Utility.isSet(propertyTypes)) {
                q.setParameter("propertyTypes", propertyTypes);
            }

            properties = (List<OrderPropertyData>) q.getResultList();
        } else {
            properties = new ArrayList<OrderPropertyData>();
        }
        
        return properties;
    }
    
    @Override
    public List<OrderMetaData> findOrderMeta(Long orderId) {
        List<OrderMetaData> meta;
        
        if (Utility.isSet(orderId)) {
            Query q = em.createQuery("Select object(meta) from OrderMetaData meta" +
                                    " WHERE meta.orderId =(:orderId)" + 
                                    " ORDER by meta.name");

            q.setParameter("orderId", orderId);

            meta = (List<OrderMetaData>) q.getResultList();
        } else {
            meta = new ArrayList<OrderMetaData>();
        }
        
        return meta;
    }
    
    @Override
    public List<OrderItemIdentView> findOrderItems(Long orderId, String erpPoNum, Long purchaseOrderId) {
        List<OrderItemIdentView> orderItemViews = new ArrayList<OrderItemIdentView>();
        
        if (Utility.isSet(orderId)) {
            // get Order Data
            StringBuilder baseQuery = new StringBuilder("Select object(orderInfo) from OrderData orderInfo");
            baseQuery.append(" WHERE orderInfo.orderId = (:orderId)");

            Query query = em.createQuery(baseQuery.toString());
            query.setParameter("orderId", orderId);
            
            List<OrderData> orderDataList = (List<OrderData>) query.getResultList();
            OrderData orderD = null;
            if (Utility.isSet(orderDataList)) {
                orderD = orderDataList.get(0);
            }
            
            if (orderD != null) {
                // get order item data list
                baseQuery = new StringBuilder("Select object(orderItem) from OrderItemData orderItem");
                baseQuery.append(" WHERE orderItem.orderId = (:orderId)");

                if (Utility.isSet(erpPoNum)) {
                    baseQuery.append(" AND orderItem.erpPoNum = (:erpPoNum)");
                }
                if (Utility.isSet(purchaseOrderId)) {
                    baseQuery.append(" AND orderItem.purchaseOrderId = (:purchaseOrderId)");
                }

                query = em.createQuery(baseQuery.toString());
                query.setParameter("orderId", orderId);

                if (Utility.isSet(erpPoNum)) {
                    query.setParameter("erpPoNum", erpPoNum);
                }
                if (Utility.isSet(purchaseOrderId)) {
                    query.setParameter("purchaseOrderId", purchaseOrderId);
                }
                List<OrderItemData> orderItemList = (List<OrderItemData>) query.getResultList();

                // get order item shopping history
                baseQuery = new StringBuilder("Select object(shoppingInfo) from ShoppingInfoData shoppingInfo");
                baseQuery.append(" WHERE shoppingInfo.orderId = (:orderId)");

                query = em.createQuery(baseQuery.toString());
                query.setParameter("orderId", orderId);

                List<ShoppingInfoData> orderHistory = (List<ShoppingInfoData>) query.getResultList();
                //==================================================================

                if (Utility.isSet(orderItemList)) {
                    Map<Long, AssetData> assetMap = new HashMap<Long, AssetData>();
                    Map<Long, ItemData> itemMap = new HashMap<Long, ItemData>();
                    Map<Long, InvoiceDistData> invoiceDistMap = new HashMap<Long, InvoiceDistData>();
                    Map<Long, PurchaseOrderData> purchaseOrderMap = new HashMap<Long, PurchaseOrderData>();
                    Map<String, BusEntityData> distributorBEMap = new HashMap<String, BusEntityData>();
                    Map<String, String> distributorPropMap = new HashMap<String, String>();
                    Map<String, OrderAddOnChargeData> orderDiscountMap = new HashMap<String, OrderAddOnChargeData>();

                    List<Long> orderItemIds = new ArrayList<Long>();
                    List<Long> itemIds = new ArrayList<Long>();
                    List<Long> assetIds = new ArrayList<Long>();

                    for (OrderItemData orderItem : orderItemList) {
                        orderItemIds.add(orderItem.getOrderItemId());
                        itemIds.add(orderItem.getItemId());
                        assetIds.add(orderItem.getAssetId());
                    }

                    if (Utility.isSet(itemIds)) {
                        baseQuery = new StringBuilder("Select object(item) from ItemData item");
                        baseQuery.append(" WHERE item.itemId").append(itemIds.size() == 1 ? " = " : " in ").append("(:itemIds)");

                        query = em.createQuery(baseQuery.toString());
                        query.setParameter("itemIds", itemIds);

                        List<ItemData> itemList = (List<ItemData>) query.getResultList();

                        if (Utility.isSet(itemList)) {
                            for (ItemData item : itemList) {
                                if (!itemMap.containsKey(item.getItemId())) {
                                    itemMap.put(item.getItemId(), item);
                                }
                            }

                            if (OrderUtil.isSimpleServiceOrder(itemList)) {
                                if (Utility.isSet(assetIds)) {
                                    baseQuery = new StringBuilder("Select object(asset) from AssetData asset");
                                    baseQuery.append(" WHERE asset.assetId").append(assetIds.size() == 1 ? " = " : " in ").append("(:assetIds)");

                                    query = em.createQuery(baseQuery.toString());

                                    query.setParameter("assetIds", assetIds);
                                    List<AssetData> assetList = (List<AssetData>) query.getResultList();

                                    if (Utility.isSet(assetList)) {
                                        for (AssetData asset : assetList) {
                                            if (!assetMap.containsKey(asset.getAssetId())) {
                                                assetMap.put(asset.getAssetId(), asset);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    List<OrderPropertyData> orderProperties = null;
                    //==============================================================
                    if (Utility.isSet(orderItemIds)) {
                        baseQuery = new StringBuilder("Select object(orderProperty) from OrderPropertyData orderProperty");
                        baseQuery.append(" WHERE orderProperty.orderItemId").append(orderItemIds.size() == 1 ? " = " : " in ").append("(:orderItemIds)");
                        baseQuery.append(" AND orderProperty.orderPropertyStatusCd = (:orderPropertyStatus)");

                        query = em.createQuery(baseQuery.toString());

                        query.setParameter("orderItemIds", orderItemIds);
                        query.setParameter("orderPropertyStatus", RefCodeNames.ORDER_PROPERTY_STATUS_CD.ACTIVE);
                        orderProperties = (List<OrderPropertyData>) query.getResultList();
                    }

                    // get the invoiceDists' Ids for this order
                    final List<String> invoiceStatuses = new ArrayList<String>();
                    invoiceStatuses.add(RefCodeNames.INVOICE_STATUS_CD.CANCELLED);
                    invoiceStatuses.add(RefCodeNames.INVOICE_STATUS_CD.DUPLICATE);
                    invoiceStatuses.add(RefCodeNames.INVOICE_STATUS_CD.REJECTED);

                    baseQuery = new StringBuilder("Select object(invoiceDist) from InvoiceDistData invoiceDist");
                    baseQuery.append(" WHERE invoiceDist.orderId = (:orderId)");
                    baseQuery.append(" AND invoiceDist.invoiceStatusCd NOT IN (:invoiceStatuses)");

                    query = em.createQuery(baseQuery.toString());
                    query.setParameter("orderId", orderId);
                    query.setParameter("invoiceStatuses", invoiceStatuses);

                    List<InvoiceDistData> invoiceDistList = (List<InvoiceDistData>) query.getResultList();

                    List<InvoiceDistDetailData> invoiceDistDetails = null;
                    //==============================================================

                    // get the invoiceDistDetails for this order
                    if (Utility.isSet(invoiceDistList)) {
                        List<Long> invoiceDistIds = new ArrayList<Long>();
                        for (InvoiceDistData invoiceDist : invoiceDistList) {
                            invoiceDistIds.add(invoiceDist.getInvoiceDistId());
                        }
                        baseQuery = new StringBuilder("Select object(invoiceDistDetail) from InvoiceDistDetailData invoiceDistDetail");
                        baseQuery.append(" WHERE invoiceDistDetail.invoiceDistId").append(invoiceDistIds.size() == 1 ? " = " : " in ").append("(:invoiceDistIds)");
                        baseQuery.append(" ORDER BY invoiceDistDetail.invoiceDistDetailId");

                        query = em.createQuery(baseQuery.toString());
                        query.setParameter("invoiceDistIds", invoiceDistIds);

                        invoiceDistDetails = (List<InvoiceDistDetailData>) query.getResultList();
                    }
                    
                    // get the invoiceCusts' Ids for this order
                    baseQuery = new StringBuilder("Select object(invoiceCust) from InvoiceCustData invoiceCust");
                    baseQuery.append(" WHERE invoiceCust.orderId = (:orderId)");
                    baseQuery.append(" AND invoiceCust.invoiceStatusCd NOT IN (:invoiceStatuses)");

                    query = em.createQuery(baseQuery.toString());
                    query.setParameter("orderId", orderId);
                    query.setParameter("invoiceStatuses", invoiceStatuses);

                    List<InvoiceCustData> invoiceCustList = (List<InvoiceCustData>) query.getResultList();

                    //==============================================================
                    List<InvoiceCustDetailData> invoiceCustDetails = null;
                    // get the invoiceCustDetails for this order
                    if (Utility.isSet(invoiceCustList)) {
                        List<Long> invoiceCustIds = new ArrayList<Long>();
                        for (InvoiceCustData invoiceCust : invoiceCustList) {
                            invoiceCustIds.add(invoiceCust.getInvoiceCustId());
                        }
                        baseQuery = new StringBuilder("Select object(invoiceCustDetail) from InvoiceCustDetailData invoiceCustDetail");
                        baseQuery.append(" WHERE invoiceCustDetail.invoiceCustId").append(invoiceCustIds.size() == 1 ? " = " : " in ").append("(:invoiceCustIds)");
                        baseQuery.append(" ORDER BY invoiceCustDetail.invoiceCustDetailId");

                        query = em.createQuery(baseQuery.toString());
                        query.setParameter("invoiceCustIds", invoiceCustIds);

                        invoiceCustDetails = (List<InvoiceCustDetailData>) query.getResultList();
                    }

                    // get the orderItemSubstitutions  for this order
                    baseQuery = new StringBuilder("Select object(itemSubstitution) from ItemSubstitutionData itemSubstitution");
                    baseQuery.append(" WHERE itemSubstitution.orderId = (:orderId)");

                    query = em.createQuery(baseQuery.toString());
                    query.setParameter("orderId", orderId);

                    List<ItemSubstitutionData> orderItemSubstitutionList = (List<ItemSubstitutionData>) query.getResultList();
                    //==============================================================

                    // get the orderItemActions  for this order
                    baseQuery = new StringBuilder("Select object(orderItemAction) from OrderItemActionData orderItemAction");
                    baseQuery.append(" WHERE orderItemAction.orderId = (:orderId)");

                    query = em.createQuery(baseQuery.toString());
                    query.setParameter("orderId", orderId);

                    List<OrderItemActionData> orderItemActionList = (List<OrderItemActionData>) query.getResultList();
                    //==============================================================

                    //get distributor po notes
                    baseQuery = new StringBuilder("Select object(orderProperty) from OrderPropertyData orderProperty");
                    baseQuery.append(" WHERE orderProperty.orderId = (:orderId)");
                    baseQuery.append(" AND orderProperty.orderPropertyTypeCd = (:orderPropertyType)");
                    baseQuery.append(" AND orderProperty.orderPropertyStatusCd = (:orderPropertyStatus)");

                    query = em.createQuery(baseQuery.toString());
                    query.setParameter("orderId", orderId);
                    query.setParameter("orderPropertyType", RefCodeNames.ORDER_PROPERTY_TYPE_CD.DISTRIBUTOR_PO_NOTE);
                    query.setParameter("orderPropertyStatus", RefCodeNames.ORDER_PROPERTY_STATUS_CD.ACTIVE);

                    List<OrderPropertyData> distributorPoNotes = (List<OrderPropertyData>) query.getResultList();
                    //==============================================================

                    //get standart product list info
                    baseQuery = new StringBuilder("Select object(orderItemMeta) from OrderItemMetaData orderItemMeta");
                    baseQuery.append(" WHERE orderItemMeta.orderItemId").append(orderItemIds.size() == 1 ? " = " : " in ").append("(:orderItemIds)");
                    baseQuery.append(" AND orderItemMeta.name = (:orderItemMetaName)");

                    query = em.createQuery(baseQuery.toString());
                    query.setParameter("orderItemIds", orderItemIds);
                    query.setParameter("orderItemMetaName", RefCodeNames.ORDER_ITEM_META_NAME.STANDARD_PRODUCT_LIST);

                    List<OrderItemMetaData> standartProductListInfos = (List<OrderItemMetaData>) query.getResultList();

                    //match up the selected data with the items
                    for (OrderItemData orderItemD : orderItemList) {
                        OrderItemIdentView orderItemDescD = new OrderItemIdentView();

                        if (RefCodeNames.ITEM_SALE_TYPE_CD.RE_SALE.equals(orderItemD.getSaleTypeCd())) {
                            orderItemDescD.setReSale(true);
                        } else {
                            orderItemDescD.setReSale(false);
                        }

                        orderItemDescD.setTaxExempt(Utility.isTrue(orderItemD.getTaxExempt()));

                        //match order property data
                        List<OrderPropertyData> tempOrderProperties = new ArrayList<OrderPropertyData>();
                        if (Utility.isSet(orderProperties)) {
                            for (OrderPropertyData orderProperty : orderProperties) {
                              if (orderItemD.getOrderItemId().equals(orderProperty.getOrderItemId())) {
                                if (Utility.isSet(orderProperty.getShortDesc()) &&
                                    RefCodeNames.ORDER_PROPERTY_TYPE_CD.OPEN_LINE_STATUS_CD.equals(orderProperty.getShortDesc())) {
                                    orderItemDescD.setOpenLineStatusCd(orderProperty.getValue());
                                } else if (RefCodeNames.ORDER_PROPERTY_TYPE_CD.ORDER_NOTE.equals(orderProperty.getOrderPropertyTypeCd())) {
                                    tempOrderProperties.add(orderProperty);
                                }
                              }
                            }
                        }
                        orderItemDescD.setOrderItemNotes(tempOrderProperties);

                        //match substitution data
                        //not in use, durval 2006-8-14
                        List<ItemSubstitutionData> tempOrderItemSubstitutions = new ArrayList<ItemSubstitutionData>();
                        if (Utility.isSet(orderItemSubstitutionList)) {
                            for (ItemSubstitutionData orderItemSubst : orderItemSubstitutionList) {
                                if (orderItemD.getOrderItemId().equals(orderItemSubst.getOrderItemId())) {
                                    tempOrderItemSubstitutions.add(orderItemSubst);
                                }
                            }
                        }

                        // match the invoiceCustDetail with rOrderItem or itemSubstitutions
                        List<InvoiceCustDetailData> tempInvoiceCustDetails = new ArrayList<InvoiceCustDetailData>();
                        if (Utility.isSet(invoiceCustDetails)) {
                            for (InvoiceCustDetailData invoiceCustDetailD : invoiceCustDetails) {
                                if (invoiceCustDetailD.getItemSkuNum().equals(orderItemD.getItemSkuNum())) {
                                    tempInvoiceCustDetails.add(invoiceCustDetailD);
                                } else { // match for the substitutions of this item
                                    if (Utility.isSet(tempOrderItemSubstitutions)) {
                                        for (ItemSubstitutionData orderItemSubstitutionD : tempOrderItemSubstitutions) {
                                            if (invoiceCustDetailD.getItemSkuNum().equals(orderItemSubstitutionD.getItemSkuNum())) {
                                                tempInvoiceCustDetails.add(invoiceCustDetailD);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // match the invoiceDistDetail with rOrderItem or itemSubstitutions
                        List<InvoiceDistData> tempInvoiceDists = new ArrayList<InvoiceDistData>();
                        List<InvoiceDistDetailData> tempInvoiceDistDetails = new ArrayList<InvoiceDistDetailData>();
                        if (Utility.isSet(invoiceDistDetails)) {
                            for (InvoiceDistDetailData invoiceDistDetailD : invoiceDistDetails) {
                                if (invoiceDistDetailD.getOrderItemId().equals(orderItemD.getOrderItemId())) {
                                    tempInvoiceDistDetails.add(invoiceDistDetailD);
                                    Long keyId = invoiceDistDetailD.getInvoiceDistId();
                                    InvoiceDistData invoiceDist = invoiceDistMap.get(keyId);
                                    if (invoiceDist == null) {
                                        for (InvoiceDistData invDist : invoiceDistList) {
                                            if (invDist.getInvoiceDistId().equals(keyId)) {
                                                invoiceDist = invDist;
                                                invoiceDistMap.put(keyId, invoiceDist);
                                                break;
                                            }
                                        }
                                    }
                                    if (invoiceDist != null) {
                                        tempInvoiceDists.add(invoiceDist);
                                    }
                                }
                            }
                        }

                        //get Order Item Actions
                        List<OrderItemActionData> tempOrderItemActions = new ArrayList<OrderItemActionData>();
                        if (Utility.isSet(orderItemActionList)) {
                            for (OrderItemActionData orderItemActionD : orderItemActionList) {
                                if (orderItemD.getOrderItemId().equals(orderItemActionD.getOrderItemId())) {
                                    tempOrderItemActions.add(orderItemActionD);
                                }
                            }
                        }

                        //get the po
                        PurchaseOrderData po = null;
                        if (Utility.longNN(orderItemD.getPurchaseOrderId()) > 0) {
                            Long keyId = orderItemD.getPurchaseOrderId();
                            po = (PurchaseOrderData) purchaseOrderMap.get(keyId);
                            if (po == null) {
                                baseQuery = new StringBuilder("Select object(purchaseOrder) from PurchaseOrderData purchaseOrder");
                                baseQuery.append(" WHERE purchaseOrder.purchaseOrderId = (:purchaseOrderId)");

                                query = em.createQuery(baseQuery.toString());
                                query.setParameter("purchaseOrderId", keyId);

                                List<PurchaseOrderData> poList = (List<PurchaseOrderData>) query.getResultList();
                                if (Utility.isSet(poList)) {
                                    po = poList.get(0);
                                }
                            }
                            if (Utility.isSet(po)) {
                                purchaseOrderMap.put(keyId, po);
                                orderItemDescD.setPurchaseOrder(po);
                            }
                        }

                        // get the dist name
                        String distErpNum = orderItemD.getDistErpNum();
                        if (null != distErpNum) {

                            BusEntityData distBE = (BusEntityData) distributorBEMap.get(distErpNum);
                            if (distBE == null) {
                                distBE = getDistributorByErpNum(distErpNum);
                                if (Utility.isSet(distBE)) {
                                    distributorBEMap.put(distErpNum, distBE);
                                }
                            }
                            String distDispName = distributorPropMap.get(distErpNum);
                            if (distDispName == null && distBE != null) {
                                baseQuery = new StringBuilder("Select object(property) from PropertyData property");
                                baseQuery.append(" WHERE property.busEntityId = (:distributorId)");
                                baseQuery.append(" AND property.userId IS NULL");
                                baseQuery.append(" AND property.shortDesc = (:propertyName)");

                                query = em.createQuery(baseQuery.toString());
                                query.setParameter("distributorId", distBE.getBusEntityId());
                                query.setParameter("propertyName", RefCodeNames.PROPERTY_TYPE_CD.RUNTIME_DISPLAY_NAME);

                                List<PropertyData> properties = (List<PropertyData>) query.getResultList();

                                if (Utility.isSet(properties)) {
                                    distDispName = properties.get(0).getValue();
                                }

                                if (!Utility.isSet(distDispName)) {
                                    distDispName = distBE.getShortDesc();
                                }
                                distributorPropMap.put(distErpNum , distDispName);
                            }
                            if (Utility.isSet(distDispName)) {
                                orderItemDescD.setDistRuntimeDisplayName(distDispName);
                            }
                            if (Utility.isSet(distBE)) {
                                orderItemDescD.setDistName(distBE.getShortDesc());
                                orderItemDescD.setDistId(distBE.getBusEntityId());
                            }
                        }

                        if (Utility.longNN(orderItemDescD.getDistId()) > 0) {
                            baseQuery = new StringBuilder("Select object(orderFreight) from OrderFreightData orderFreight");
                            baseQuery.append(" WHERE orderFreight.orderId = (:orderId)");
                            baseQuery.append(" AND orderFreight.busEntityId = (:distributorId)");

                            query = em.createQuery(baseQuery.toString());
                            query.setParameter("orderId", orderId);
                            query.setParameter("distributorId", orderItemDescD.getDistId());

                            List<OrderFreightData> orderFreights = (List<OrderFreightData>) query.getResultList();

                            if (Utility.isSet(orderFreights)) {
                                for (OrderFreightData frD : orderFreights) {
                                    if (frD.getShortDesc().equalsIgnoreCase("Freight")){
                                        orderItemDescD.setOrderFreight(frD);
                                    } else if(frD.getShortDesc().equalsIgnoreCase("Handling")){
                                        orderItemDescD.setOrderHandling(frD);
                                    }
                                }
                            }
                        }

                        // set Order Discount for each Distributor (new staff for Discounts)
                        if (Utility.longNN(orderItemDescD.getDistId()) > 0) {
                            String key = orderId + "::" + orderItemDescD.getDistId();

                            if (orderDiscountMap.containsKey(key)) {
                                orderItemDescD.setOrderDiscount(orderDiscountMap.get(key));
                            }

                            baseQuery = new StringBuilder("Select object(orderAddOnCharge) from OrderAddOnChargeData orderAddOnCharge");
                            baseQuery.append(" WHERE orderAddOnCharge.orderId = (:orderId)");
                            baseQuery.append(" AND orderAddOnCharge.busEntityId = (:distributorId)");

                            query = em.createQuery(baseQuery.toString());
                            query.setParameter("orderId", orderId);
                            query.setParameter("distributorId", orderItemDescD.getDistId());

                            List<OrderAddOnChargeData> orderAddOnCharges = (List<OrderAddOnChargeData>) query.getResultList();

                            if (Utility.isSet(orderAddOnCharges)) {
                                for (OrderAddOnChargeData charge : orderAddOnCharges) {
                                    if (charge.getDistFeeChargeCd().equals(RefCodeNames.CHARGE_CD.DISCOUNT)) {
                                        orderItemDescD.setOrderDiscount(charge);
                                        orderDiscountMap.put(key, orderItemDescD.getOrderDiscount());
                                    } else if (charge.getDistFeeChargeCd().equals(RefCodeNames.CHARGE_CD.FUEL_SURCHARGE)) {
                                        orderItemDescD.setOrderFuelSurcharge(charge);
                                    } else if (charge.getDistFeeChargeCd().equals(RefCodeNames.CHARGE_CD.SMALL_ORDER_FEE)) {
                                        orderItemDescD.setOrderSmallOrderFee(charge);
                                    }
                                }
                            }
                        }

                        // set the orderItemActionDescList, which including the substitution info to display
                        List<OrderItemActionData> tempOrderItemActionList = new ArrayList<OrderItemActionData>();

                        OrderItemActionData tempOIAD = null;

                        //Create cust invoice actions
                        if (Utility.isSet(tempInvoiceCustDetails)) {
                            for (InvoiceCustDetailData icdD : tempInvoiceCustDetails) {
                                tempOIAD = new OrderItemActionData();
                                tempOIAD.setOrderId(orderD.getOrderId());
                                tempOIAD.setAffectedOrderNum(orderD.getOrderNum());
                                tempOIAD.setOrderItemId(orderItemD.getOrderItemId());
                                tempOIAD.setAffectedSku(String.valueOf(orderItemD.getItemSkuNum()));
                                tempOIAD.setAffectedLineItem(orderItemD.getOrderLineNum());
                                // Is this invoice detail for this item
                                if (icdD.getItemSkuNum().equals(orderItemD.getItemSkuNum())) {

                                    InvoiceCustData icD = null;
                                    if (Utility.isSet(invoiceCustList)) {
                                        for (InvoiceCustData invoiceCustData : invoiceCustList) {
                                            icD = invoiceCustData;
                                            if (icD.getInvoiceCustId().equals(icdD.getInvoiceCustId())) {
                                                break;
                                            }
                                        }
                                    }

                                    // the assumption is that detail entries only exist
                                    // in case an invoice cust entry has been made
                                    tempOIAD.setActionCd(RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.CUST_INVOICED);
                                    tempOIAD.setActionDate(icD.getInvoiceDate());
                                    tempOIAD.setQuantity(icdD.getItemQuantity());
                                    tempOrderItemActionList.add(tempOIAD);
                                }
                            }
                        }

                        //Create dist invoice actions
                        if (Utility.isSet(tempInvoiceDistDetails) && Utility.isSet(tempInvoiceDists)) {
                            Iterator<InvoiceDistDetailData> iterIDistDetailD = tempInvoiceDistDetails.iterator();
                            Iterator<InvoiceDistData> iterIDistD = tempInvoiceDists.iterator();
                            for (;iterIDistDetailD.hasNext() && iterIDistD.hasNext();) {
                                InvoiceDistDetailData icdD = iterIDistDetailD.next();
                                InvoiceDistData icD = iterIDistD.next();

                                tempOIAD = new OrderItemActionData();
                                tempOIAD.setOrderId(orderD.getOrderId());
                                tempOIAD.setAffectedOrderNum(orderD.getOrderNum());
                                tempOIAD.setOrderItemId(orderItemD.getOrderItemId());
                                tempOIAD.setAffectedSku(String.valueOf(orderItemD.getItemSkuNum()));
                                tempOIAD.setAffectedLineItem(orderItemD.getOrderLineNum());

                                tempOIAD.setActionCd(RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.DIST_INVOICED);
                                tempOIAD.setActionDate(icD.getInvoiceDate());
                                Long qty = icdD.getItemQuantity();
                                if (qty == 0L) {
                                    qty = icdD.getDistItemQtyReceived();
                                }
                                tempOIAD.setQuantity(qty);
                                tempOrderItemActionList.add(tempOIAD);
                            }
                        }

                        //Set po date
                        if (po != null) {
                            tempOIAD = new OrderItemActionData();
                            tempOIAD.setOrderId(orderD.getOrderId());
                            tempOIAD.setAffectedOrderNum(orderD.getOrderNum());
                            tempOIAD.setOrderItemId(orderItemD.getOrderItemId());
                            tempOIAD.setAffectedSku(String.valueOf(orderItemD.getItemSkuNum()));
                            tempOIAD.setAffectedLineItem(orderItemD.getOrderLineNum());
                            tempOIAD.setActionCd(RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.SYSTEM_ACCEPTED);
                            tempOIAD.setActionDate(po.getPoDate());
                            tempOIAD.setQuantity(orderItemD.getTotalQuantityOrdered());
                            tempOrderItemActionList.add(tempOIAD);
                        }

                        final List<String> orderItemDetailActions = new ArrayList<String>();
                        orderItemDetailActions.add(RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.CUST_INVOICED);
                        orderItemDetailActions.add(RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.DIST_INVOICED);

                        baseQuery = new StringBuilder("Select object(orderItemAction) from OrderItemActionData orderItemAction");
                        baseQuery.append(" WHERE orderItemAction.orderItemId = (:orderItemId)");
                        baseQuery.append(" AND orderItemAction.actionCd NOT IN (:orderItemDetailActions)");

                        query = em.createQuery(baseQuery.toString());
                        query.setParameter("orderItemId", orderItemD.getOrderItemId());
                        query.setParameter("orderItemDetailActions", orderItemDetailActions);

                        List<OrderItemActionData> orderItemActions = (List<OrderItemActionData>) query.getResultList();

                        if (Utility.isSet(orderItemActions)) {
                            tempOrderItemActionList.addAll(orderItemActions);
                        }

                        List<OrderItemActionDescView> tempOrderItemActionDescList = new ArrayList<OrderItemActionDescView>();

                        if (Utility.isSet(tempOrderItemActionList)) {
                            for (OrderItemActionData orderItemAction : tempOrderItemActionList) {
                                if (orderItemAction.getAddDate() == null) {
                                    if (orderItemAction.getActionDate() != null) {
                                        orderItemAction.setAddDate(orderItemAction.getActionDate());
                                    } else {
                                        orderItemAction.setAddDate(new Date());
                                    }
                                }
                            }
                            //Order action by date (desc)
                            Collections.sort(tempOrderItemActionList, AppComparator.ORDER_ITEM_ACTION_COMPARATOR_DESC);
                            for (OrderItemActionData orderItemAction : tempOrderItemActionList) {
                                OrderItemActionDescView oiadView = new OrderItemActionDescView();
                                oiadView.setOrderItemAction(orderItemAction);
                                tempOrderItemActionDescList.add(oiadView);
                            }
                        }

                        ItemData itemInfo = itemMap.get(String.valueOf(orderItemD.getItemId()));
                        AssetData assetInfo = assetMap.get(String.valueOf(orderItemD.getAssetId()));

                        orderItemDescD.setOrderItemActions(tempOrderItemActionList);
                        orderItemDescD.setOrderItemActionDescs(tempOrderItemActionDescList);
                        orderItemDescD.setOrderItem(orderItemD);
                        orderItemDescD.setAssetInfo(assetInfo);
                        orderItemDescD.setItemInfo(itemInfo);
                        orderItemDescD.setShoppingHistory(orderHistory);
                        orderItemDescD.setOrderItemSubstitutions(tempOrderItemSubstitutions);
                        orderItemDescD.setInvoiceDistDetails(tempInvoiceDistDetails);
                        orderItemDescD.setInvoiceDistDatas(tempInvoiceDists);
                        orderItemDescD.setInvoiceCustDetails(tempInvoiceCustDetails);

                        orderItemDescD.setHasNote(Utility.isSet(orderItemDescD.getOrderItemNotes()));


                        //Set distributor po notes
                        Long orderItemId = orderItemD.getOrderItemId();
                        if (Utility.isSet(distributorPoNotes)) {
                            for (OrderPropertyData distNote : distributorPoNotes) {
                                if (orderItemId.equals(distNote.getOrderItemId())) {
                                    orderItemDescD.setDistPoNote(distNote);
                                    break;
                                }
                            }
                        }

                        // get standard product list flag
                        if (Utility.isSet(standartProductListInfos)) {
                            for (OrderItemMetaData oimD : standartProductListInfos) {
                                if (orderItemId.equals(oimD.getOrderItemId())) {
                                    orderItemDescD.setStandardProductListFlag((oimD.getValue()));
                                    break;
                                }
                            }
                        }

                        //==========================================================
                        orderItemViews.add(orderItemDescD);
                    }
                }
            }
        }
        
        return orderItemViews;
    }
    
    private boolean calculateAllowModificationForOrder(OrderData order) {
        boolean allowModification = false;

        String orderSource = order.getOrderSourceCd();
        
        if (RefCodeNames.ORDER_SOURCE_CD.EDI_850.equals(orderSource)) {
            Long tradingProfileId = order.getIncomingTradingProfileId();
            
            if (tradingProfileId > 0) {
                StringBuilder baseQuery = new StringBuilder("Select object(tpConfig) from TradingProfileConfigData tpConfig");
                baseQuery.append(" WHERE tpConfig.incomingTradingProfileId = (:tradingProfileId)");
                baseQuery.append(" AND tpConfig.direction = (:direction)");
                baseQuery.append(" AND tpConfig.setType = (:setType)");

                Query query = em.createQuery(baseQuery.toString());
                query.setParameter("tradingProfileId", tradingProfileId);
                query.setParameter("direction", "OUT");
                query.setParameter("setType", "855");

                List<TradingProfileConfigData> tradingProfileConfigs = (List<TradingProfileConfigData>) query.getResultList();
                
                if (Utility.isSet(tradingProfileConfigs)) {
                    allowModification = true;
                }
            }
        } else {
            allowModification = true;
        }
        
        return allowModification;
    }
    
    @Override
    public List<OrderItemData> findOrderItemDataList(Long orderId) {
        List<OrderItemData> items;
        
        if (Utility.isSet(orderId)) {
            Query q = em.createQuery("Select object(item) from OrderItemData item" +
                                    " WHERE item.orderId =(:orderId)");

            q.setParameter("orderId", orderId);

            items = (List<OrderItemData>) q.getResultList();
        } else {
            items = new ArrayList<OrderItemData>();
        }
        
        return items;
    }
    
    private void initOrderItemActionProperties(OrderIdentView order) {
    	boolean shipped = true;
    	int ackOnHold = 0;
        
        if (Utility.isSet(order.getOrderItems())) {
            for (OrderItemData orderItem : order.getOrderItems()) {
                if (OrderUtil.isGoodOrderItemStatus(orderItem.getOrderItemStatusCd())) {
                    OrderItemActionData onHoldAction = null;
                    int totalActionQty = 0;
                    
                    //final List<String> orderItemDetailActions = new ArrayList<String>();
                    //orderItemDetailActions.add(RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.CUST_INVOICED);
                    //orderItemDetailActions.add(RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.DIST_INVOICED);

                    StringBuilder baseQuery = new StringBuilder("Select object(orderItemAction) from OrderItemActionData orderItemAction");
                    baseQuery.append(" WHERE orderItemAction.orderItemId = (:orderItemId)");
                    //baseQuery.append(" AND orderItemAction.actionCd NOT IN (:orderItemDetailActions)");

                    Query query = em.createQuery(baseQuery.toString());
                    query.setParameter("orderItemId", orderItem.getOrderItemId());
                    //query.setParameter("orderItemDetailActions", orderItemDetailActions);

                    List<OrderItemActionData> orderItemActions = (List<OrderItemActionData>) query.getResultList();
                    
                    Collections.sort(orderItemActions, AppComparator.ORDER_ITEM_ACTION_ACK_ON_HOLD_COMPARATOR);

                    Iterator<OrderItemActionData> it = orderItemActions.iterator();
                    while (it.hasNext()) {
                        OrderItemActionData act = (OrderItemActionData) it.next();
                        if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.DIST_SHIPPED.equals(act.getActionCd())) {
                                totalActionQty += act.getQuantity();
                        } else if(!it.hasNext()) {
                            if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.ACK_ON_HOLD.equals(act.getActionCd())) {
                                    //if the last action code is
                                if (ackOnHold != -1) {
                                    ackOnHold = 1;
                                    onHoldAction = act;
                                }
                            } else {
                                if (onHoldAction == null) {
                                    ackOnHold = -1;
                                } else {
                                    Date a1T = act.getActionTime();
                                    Date a2T = onHoldAction.getActionTime();
                                    Date a1D = act.getActionDate();
                                    Date a2D = onHoldAction.getActionDate();
                                    if (a1D != null && a2D != null && a2T != null && a2D != null) {
                                        if (a1D.compareTo(a2D) != 0 || a1T.compareTo(a2T) != 0) {
                                            ackOnHold = -1;
                                        }
                                    } else if (a1D != null && a2D != null) {
                                        if (a1D.compareTo(a2D) != 0) {
                                            ackOnHold = -1;
                                        }
                                    } else {
                                        ackOnHold = -1;
                                    }
                                }
                            }
                        }
                    }

                    if (totalActionQty < orderItem.getTotalQuantityOrdered()) {
                        //no need to continue, if one is undershipped the order is undershipped
                        shipped = false;
                        break;
                    }
                }
            }
        }
        
        order.setShipped(shipped);
        order.setAckOnHold(ackOnHold == 1);
    }
    
    @Override
    public List<FreightHandlerView> getFreightHandlerDetails(List<Long> freightHandlerIds) {
        List<FreightHandlerView> freightHandlers = new ArrayList<FreightHandlerView>();

        if (Utility.isSet(freightHandlerIds)) {
            StringBuilder baseQuery = new StringBuilder("Select object(busEntity) from BusEntityData busEntity");
            baseQuery.append(" WHERE busEntity.busEntityId").append(freightHandlerIds.size() == 1 ? " = " : " in ").append("(:freightHandlerIds)");

            Query query = em.createQuery(baseQuery.toString());
            query.setParameter("freightHandlerIds", freightHandlerIds);

            List<BusEntityData> freightHandlerDatas = (List<BusEntityData>) query.getResultList();
            //==================================================================
            
            baseQuery = new StringBuilder("Select object(address) from AddressData address");
            baseQuery.append(" WHERE address.busEntityId").append(freightHandlerIds.size() == 1 ? " = " : " in ").append("(:freightHandlerIds)");
            baseQuery.append(" ORDER BY address.busEntityId, address.addressTypeCd");

            query = em.createQuery(baseQuery.toString());
            query.setParameter("freightHandlerIds", freightHandlerIds);

            List<AddressData> addressDatas = (List<AddressData>) query.getResultList();

            baseQuery = new StringBuilder("Select object(property) from PropertyData property");
            baseQuery.append(" WHERE property.busEntityId").append(freightHandlerIds.size() == 1 ? " = " : " in ").append("(:freightHandlerIds)");
            baseQuery.append(" ORDER BY property.busEntityId, property.propertyTypeCd");

            query = em.createQuery(baseQuery.toString());
            query.setParameter("freightHandlerIds", freightHandlerIds);

            List<PropertyData> propertyDatas = (List<PropertyData>) query.getResultList();

            baseQuery = new StringBuilder("Select object(beAssoc) from BusEntityAssocData beAssoc");
            baseQuery.append(" WHERE beAssoc.busEntity1Id").append(freightHandlerIds.size() == 1 ? " = " : " in ").append("(:freightHandlerIds)");
            baseQuery.append(" AND beAssoc.busEntityAssocCd = (:assocType)");

            query = em.createQuery(baseQuery.toString());
            query.setParameter("freightHandlerIds", freightHandlerIds);
            query.setParameter("assocType", RefCodeNames.BUS_ENTITY_ASSOC_CD.FREIGHT_HANDLER_STORE);

            List<BusEntityAssocData> beAssocDatas = (List<BusEntityAssocData>) query.getResultList();

            if (Utility.isSet(freightHandlerDatas)) {
                for (BusEntityData freightHandlerD : freightHandlerDatas) {
                    Long fhID = freightHandlerD.getBusEntityId();
                    FreightHandlerView freightHandlerView = new FreightHandlerView();
                    freightHandlerView.setBusEntityData(freightHandlerD);
                    
                    if (Utility.isSet(addressDatas)) {
                        for (AddressData addressD : addressDatas) {
                            if (fhID.equals(addressD.getBusEntityId())) {
                                freightHandlerView.setPrimaryAddress(addressD);
                                break;
                            }
                        }
                    }
                    
                    if (Utility.isSet(propertyDatas)) {
                        for (PropertyData propertyD : propertyDatas) {
                            if (RefCodeNames.PROPERTY_TYPE_CD.FR_ON_INVOICE_CD.equals(propertyD.getPropertyTypeCd())) {
                                freightHandlerView.setAcceptFreightOnInvoice(propertyD.getValue());
                            } else if (RefCodeNames.PROPERTY_TYPE_CD.FR_ROUTING_CD.equals(propertyD.getPropertyTypeCd())) {
                                freightHandlerView.setEdiRoutingCd(propertyD.getValue());
                            }
                        }
                    }
                    
                    if (Utility.isSet(beAssocDatas)) {
                        for (BusEntityAssocData beAssocD : beAssocDatas) {
                            if (fhID.equals(beAssocD.getBusEntity2Id())) {
                                freightHandlerView.setStoreId(beAssocD.getBusEntity2Id());
                            }
                        }
                    }
                    freightHandlers.add(freightHandlerView);
                }
            }
            
        }
        return freightHandlers;   
    }

    @Override
    public List<OrderAddressData> findOrderAddresses(Long orderId) {
        List<OrderAddressData> addresses = null;
        
        if (Utility.isSet(orderId)) {
            Query q = em.createQuery("Select object(address) from OrderAddressData address" +
                                    " WHERE address.orderId =(:orderId)");

            q.setParameter("orderId", orderId);

            addresses = (List<OrderAddressData>) q.getResultList();
        } else {
            addresses = new ArrayList<OrderAddressData>();
        }
        
        return addresses;
    }
    
    private List<ReplacedOrderView> getReplacedOrders(Long orderId) { // Get replaced orders if the order is a consolidated order
        List<ReplacedOrderView> replacedOrders = new ArrayList<ReplacedOrderView>();

        StringBuilder baseQuery = new StringBuilder("Select object(orderAssoc) from OrderAssocData orderAssoc");
        baseQuery.append(" WHERE orderAssoc.order2Id = (:orderId)");
        baseQuery.append(" AND orderAssoc.orderAssocCd = (:orderAssocType)");
        baseQuery.append(" AND orderAssoc.orderAssocStatusCd = (:orderAssocStatus)");

        Query query = em.createQuery(baseQuery.toString());
        query.setParameter("orderId", orderId);
        query.setParameter("orderAssocType", RefCodeNames.ORDER_ASSOC_CD.CONSOLIDATED);
        query.setParameter("orderAssocStatus", RefCodeNames.ORDER_ASSOC_STATUS_CD.ACTIVE);

        List<OrderAssocData> consolidatedAssocs = (List<OrderAssocData>) query.getResultList();
        
        if (Utility.isSet(consolidatedAssocs)) {
            Set<Long> consolidatedIds = new HashSet<Long>();
            for (OrderAssocData consolidatedAssoc : consolidatedAssocs) {
                consolidatedIds.add(consolidatedAssoc.getOrder1Id());
            }
            
            baseQuery = new StringBuilder("Select object(orderD) from OrderData orderD");
            baseQuery.append(" WHERE orderD.orderId").append(consolidatedIds.size() == 1 ? " = " : " in ").append("(:consolidatedIds)");
            baseQuery.append(" ORDER BY orderD.orderId");

            query = em.createQuery(baseQuery.toString());
            query.setParameter("consolidatedIds", consolidatedIds);

            List<OrderData> consolidatedOrders = (List<OrderData>) query.getResultList();
            List<Long> replacedOrderIds = new ArrayList<Long>();

            if (Utility.isSet(consolidatedOrders)) {
                ReplacedOrderView replacedOrder;
                for(OrderData consolidatedOrder : consolidatedOrders) {
                    replacedOrderIds.add(consolidatedOrder.getOrderId());
                    replacedOrder = new ReplacedOrderView();
                    replacedOrder.setOrderId(consolidatedOrder.getOrderId());
                    replacedOrder.setOrderNum(consolidatedOrder.getOrderNum());
                    replacedOrder.setRefOrderNum(consolidatedOrder.getRefOrderNum());
                    replacedOrder.setRequestPoNum(consolidatedOrder.getRequestPoNum());
                    replacedOrder.setOrderDate(consolidatedOrder.getOriginalOrderDate());
                    replacedOrder.setOrderSiteName(consolidatedOrder.getOrderSiteName());
                    //replacedOrder.setItems(new ArrayList<ReplacedOrderItemView>());
                    replacedOrders.add(replacedOrder);
                }
                
                baseQuery = new StringBuilder("Select object(orderItem) from OrderItemData orderItem");
                baseQuery.append(" WHERE orderItem.orderId").append(replacedOrderIds.size() == 1 ? " = " : " in ").append("(:replacedOrderIds)");
                baseQuery.append(" ORDER BY orderItem.orderId");
                
                query = em.createQuery(baseQuery.toString());
                query.setParameter("replacedOrderIds", replacedOrderIds);

                List<OrderItemData> replacedOrderItems = (List<OrderItemData>) query.getResultList();

                if (Utility.isSet(replacedOrderItems)) {
                    Map<Long, List<ReplacedOrderItemView>> replacedOrderItemsMap = new HashMap<Long, List<ReplacedOrderItemView>>();
                    for (OrderItemData rOrderItem : replacedOrderItems) {
                        Long rOrderId = rOrderItem.getOrderId();
                        List<ReplacedOrderItemView> oiList = replacedOrderItemsMap.get(rOrderId);
                        if (oiList == null) {
                            oiList = new ArrayList<ReplacedOrderItemView>();
                        }
                        ReplacedOrderItemView roiView = new ReplacedOrderItemView();
                        roiView.setOrderItemId(rOrderItem.getOrderItemId());
                        roiView.setOrderId(rOrderId);
                        roiView.setItemId(rOrderItem.getItemId());
                        roiView.setItemSkuNum(rOrderItem.getItemSkuNum());
                        roiView.setCustItemSkuNum(rOrderItem.getCustItemSkuNum());
                        roiView.setItemShortDesc(rOrderItem.getItemShortDesc());
                        roiView.setManuItemSkuNum(rOrderItem.getManuItemSkuNum());
                        roiView.setDistItemSkuNum(rOrderItem.getDistItemSkuNum());
                        roiView.setCustItemShortDesc(rOrderItem.getCustItemShortDesc());
                        roiView.setQuantity(rOrderItem.getTotalQuantityOrdered());
                        roiView.setQuantityS("" + rOrderItem.getTotalQuantityOrdered());
                        roiView.setOrderItemStatusCd(rOrderItem.getOrderItemStatusCd());
                        
                        oiList.add(roiView);
                        replacedOrderItemsMap.put(rOrderId, oiList);
                    }
                    
                    for (ReplacedOrderView rOrder : replacedOrders) {
                        rOrder.setItems(replacedOrderItemsMap.get(rOrder.getOrderId()));
                    }
                }
            }
            
        }
        return replacedOrders;
    }
    
    private OrderData getConsolidatedOrderForReplaced(Long orderId) {
        OrderData consolidatedOrderD = null;
        
        StringBuilder baseQuery = new StringBuilder("Select object(orderAssoc) from OrderAssocData orderAssoc");
        baseQuery.append(" WHERE orderAssoc.order1Id = (:orderId)");
        baseQuery.append(" AND orderAssoc.orderAssocCd = (:orderAssocType)");
        baseQuery.append(" AND orderAssoc.orderAssocStatusCd = (:orderAssocStatus)");

        Query query = em.createQuery(baseQuery.toString());
        query.setParameter("orderId", orderId);
        query.setParameter("orderAssocType", RefCodeNames.ORDER_ASSOC_CD.CONSOLIDATED);
        query.setParameter("orderAssocStatus", RefCodeNames.ORDER_ASSOC_STATUS_CD.ACTIVE);

        List<OrderAssocData> consolidatedAssocs = (List<OrderAssocData>) query.getResultList();
        
        if (Utility.isSet(consolidatedAssocs)) {
            OrderAssocData consolidatedAssoc = consolidatedAssocs.get(0);
            baseQuery = new StringBuilder("Select object(orderD) from OrderData orderD");
            baseQuery.append(" WHERE orderD.orderId = (:orderId)");

            query = em.createQuery(baseQuery.toString());
            query.setParameter("orderId", consolidatedAssoc.getOrder2Id());

            List<OrderData> consolidatedOrderList = (List<OrderData>) query.getResultList();
            if (Utility.isSet(consolidatedOrderList)) {
                consolidatedOrderD = consolidatedOrderList.get(0);
            }
        }
        return consolidatedOrderD;
    }
    
    private BusEntityData getDistributorByErpNum(String distributorErpNum) {
        BusEntityDAO dao = new BusEntityDAOImpl(em);
        
        return dao.getDistributorByErpNum(distributorErpNum);
    }
    
    public List<NoteJoinView> getSiteCrcNotes(Long siteId) {
        List<NoteJoinView> noteJoinList = new ArrayList<NoteJoinView>();
        
        if (Utility.isSet(siteId)) {
            StringBuilder baseQuery = new StringBuilder("Select object(note) FROM NoteData note");
            baseQuery.append(" WHERE note.busEntityId = (:siteId)");
            baseQuery.append(" AND note.propertyId IN");
                baseQuery.append(" (Select property.propertyId FROM PropertyData property");
                baseQuery.append(" WHERE property.propertyTypeCd = (:propertyType)");
                baseQuery.append(" AND property.propertyStatusCd = (:propertyStatus)");
                baseQuery.append(" AND property.value = (:propertyValue))");
            baseQuery.append(" ORDER BY note.noteId");

            Query query = em.createQuery(baseQuery.toString());
            query.setParameter("siteId", siteId);
            query.setParameter("propertyType", RefCodeNames.PROPERTY_TYPE_CD.SITE_NOTE_TOPIC);
            query.setParameter("propertyStatus", RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);
            query.setParameter("propertyValue", "CRC Site Notes");

            List<NoteData> siteCRCNotes = (List<NoteData>) query.getResultList();
            if (Utility.isSet(siteCRCNotes)) {
                List<Long> noteIds = new ArrayList<Long>();
                for (NoteData note : siteCRCNotes) {
                    noteIds.add(note.getNoteId());
                }

                baseQuery = new StringBuilder("Select object(noteText) from NoteTextData noteText");
                baseQuery.append(" WHERE noteText.noteId").append(noteIds.size() == 1 ? " = " : " in ").append("(:noteIds)");
                baseQuery.append(" ORDER BY noteText.noteId DESC, noteText.seqNum DESC, noteText.pageNum");

                query = em.createQuery(baseQuery.toString());
                query.setParameter("noteIds", noteIds);

                List<NoteTextData> noteTexts = (List<NoteTextData>) query.getResultList();

                if (Utility.isSet(noteTexts)) {
                    List<NoteTextData> noteTextsTMP = new ArrayList<NoteTextData>();
                    
                    long prevSeqNum = -1;
                    long prevNoteId = -1;
                    NoteTextData noteTextD = null;
                    for (Iterator<NoteTextData> iter = noteTexts.iterator();  iter.hasNext();) {
                        NoteTextData noteText = iter.next();
                        long noteId = noteText.getNoteId();
                        long seqNum = noteText.getSeqNum();

                        if (seqNum != prevSeqNum || noteId != prevNoteId ) {
                            prevSeqNum = seqNum;
                            if (noteTextD != null) {
                                noteTextsTMP.add(noteTextD);
                            }
                            noteTextD = noteText;
                        } else {
                            String paragraph = noteTextD.getNoteText();
                            String paragraph1 = noteText.getNoteText();
                            if (paragraph == null) paragraph = "";
                            if (paragraph1 != null) paragraph += paragraph1;
                            noteTextD.setNoteText(paragraph);
                        }
                    }
                    if (noteTextD != null) {
                        noteTextsTMP.add(noteTextD);
                    }

                    noteTextD = null;
                    for (Iterator iter = siteCRCNotes.iterator(), iter1 = noteTextsTMP.iterator();
                        iter.hasNext();) {
                        
                        NoteData nD = (NoteData) iter.next();
                        NoteJoinView noteJoinView = new NoteJoinView();
                        noteJoinList.add(noteJoinView);
                        noteJoinView.setNote(nD);
                        List<NoteTextData> noteTextDV = new ArrayList<NoteTextData>();
                        noteJoinView.setNoteText(noteTextDV);
                        long noteId = nD.getNoteId();
                        //noteIdV.add(noteId);
                        
                        while (noteTextD != null || iter1.hasNext()){
                            if (noteTextD == null) {
                                noteTextD = (NoteTextData) iter1.next();
                            }
                            long nId = noteTextD.getNoteId();
                            if (nId == noteId) {
                                noteTextDV.add(noteTextD);
                                noteTextD = null;
                                continue;
                            }
                            if (nId > noteId) {// should never happen
                                noteTextD = null;
                                continue;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return noteJoinList;
    }
    
    public List<InvoiceCustView> getInvoiceCustByWebOrderNum(String webOrderNum, Long storeId, Boolean fullInfo) {
        List<InvoiceCustView> invoiceCustViews = new ArrayList<InvoiceCustView>();

        if (Utility.isSet(webOrderNum) && Utility.isSet(storeId)) {
            Query q = em.createQuery("Select object(invoiceCustD) from InvoiceCustData invoiceCustD, OrderData orderInfo" +
                    " where invoiceCustD.storeId = (:storeId)" +
                    " and orderInfo.orderNum = (:webOrderNum)" +
                    " and invoiceCustD.orderId = orderInfo.orderId");
            q.setParameter("storeId", storeId);
            q.setParameter("webOrderNum", webOrderNum);

            List<InvoiceCustData> invoiceCustDatas = (List<InvoiceCustData>) q.getResultList();
            
            if (Utility.isSet(invoiceCustDatas)) {
                InvoiceCustView icv;
                List<InvoiceCustInfoView> icInfoView;
                for (InvoiceCustData invoiceCust : invoiceCustDatas) {
                    icv = new InvoiceCustView();
                    icInfoView =  new ArrayList<InvoiceCustInfoView>();

                    icv.setInvoiceCustData(invoiceCust);
                    icv.setInvoiceCustInfoViews(icInfoView);

                    if (fullInfo) {
                        StringBuilder baseQuery = new StringBuilder("Select object(invoiceCustDetail) from InvoiceCustDetailData invoiceCustDetail");
                        baseQuery.append(" WHERE invoiceCustDetail.invoiceCustId = (:invoiceCustId)");
                        baseQuery.append(" ORDER BY invoiceCustDetail.lineNumber");

                        Query query = em.createQuery(baseQuery.toString());
                        query.setParameter("invoiceCustId", invoiceCust.getInvoiceCustId());

                        List<InvoiceCustDetailData> invoiceCustDetails = (List<InvoiceCustDetailData>) query.getResultList();
                        icv.setInvoiceCustDetails(invoiceCustDetails);
                        if (Utility.isSet(invoiceCust.getOrderId()) && invoiceCust.getOrderId() > 0) {
                            baseQuery = new StringBuilder("Select object(orderInfo) from OrderData orderInfo");
                            baseQuery.append(" WHERE orderInfo.orderId = (:orderId)");

                            query = em.createQuery(baseQuery.toString());
                            query.setParameter("orderId", invoiceCust.getOrderId());
                            
                            List<OrderData> orders = (List<OrderData>) query.getResultList();
                            if (Utility.isSet(orders)) {
                                icv.setOrderData(orders.get(0));
                            }
                        }
                    }
                    invoiceCustViews.add(icv);
                }
            }
        }
        return invoiceCustViews;
    }
    
    public List<ItemContractCostView> getItemContractCost(Long accountId, Long itemSkuNum, Long distId, String distErpNum) {
        List<ItemContractCostView> itemContractCostViews = new ArrayList<ItemContractCostView>();
        
        
        if (accountId != null && itemSkuNum != null) {
            //Get Item Data
            StringBuilder baseQuery = new StringBuilder("Select object(itemInfo) FROM ItemData itemInfo");
                baseQuery.append(" WHERE itemInfo.skuNum = (:itemSkuNum)");

            Query query = em.createQuery(baseQuery.toString());
            query.setParameter("itemSkuNum", itemSkuNum);

            List<ItemData> itemDV = (List<ItemData>) query.getResultList();
            if (Utility.isSet(itemDV)) {
                Long itemID = itemDV.get(0).getItemId();

                //Distributor Data if distributor id or erp number provided
                Long distributorID = distId;
                if (distId == null && Utility.isSet(distErpNum)) {
                    baseQuery = new StringBuilder("Select object(busEntity) FROM BusEntityData busEntity");
                        baseQuery.append(" WHERE busEntity.erpNum = (:erpNum)");
                        baseQuery.append(" AND busEntity.busEntityTypeCd = (:busEntityTypeCd)");
                        baseQuery.append(" AND busEntity.busEntityStatusCd = (:busEntityStatusCd)");

                    query = em.createQuery(baseQuery.toString());
                    query.setParameter("erpNum", distErpNum);
                    query.setParameter("busEntityTypeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.DISTRIBUTOR);
                    query.setParameter("busEntityStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);

                    List<BusEntityData> busEntityDV = (List<BusEntityData>) query.getResultList();

                    if (Utility.isSet(busEntityDV)) {
                        distributorID = busEntityDV.get(0).getBusEntityId();
                    }
                }

                //Get catalogs
                baseQuery = new StringBuilder("Select object(catalogStructure) from CatalogStructureData catalogStructure, CatalogData catalog, CatalogAssocData catalogToAccount");
                    baseQuery.append(" WHERE catalogToAccount.busEntityId = (:accountId)");
                    baseQuery.append(" AND catalogToAccount.catalogAssocCd = (:catalogToAccountAssoc)");
                    baseQuery.append(" AND catalog.catalogId = catalogToAccount.catalogId");
                    baseQuery.append(" AND catalog.catalogTypeCd = (:catalogType)");
                    baseQuery.append(" AND catalog.catalogStatusCd = (:catalogStatus)");
                    baseQuery.append(" AND catalogStructure.catalogId = catalog.catalogId");
                    baseQuery.append(" AND catalogStructure.itemId = (:itemId)");
                    if (distributorID != null && distributorID > 0) {
                        baseQuery.append(" AND catalogStructure.busEntityId = (:distributorID)");
                    }
                
                query = em.createQuery(baseQuery.toString());
                query.setParameter("accountId", accountId);
                query.setParameter("catalogToAccountAssoc", RefCodeNames.CATALOG_ASSOC_CD.CATALOG_ACCOUNT);
                query.setParameter("catalogType", RefCodeNames.CATALOG_TYPE_CD.SHOPPING);
                query.setParameter("catalogStatus", RefCodeNames.CATALOG_STATUS_CD.ACTIVE);
                query.setParameter("itemId", itemID);
                if (distributorID != null && distributorID > 0) {
                    query.setParameter("distributorID", distributorID);
                }

                List<CatalogStructureData> catalogStructDV = (List<CatalogStructureData>) query.getResultList();
                List<Long> catalogIds = new ArrayList<Long>();
                if (Utility.isSet(catalogStructDV)) {
                    catalogIds = Utility.toIds(catalogStructDV);
                }

                //Get contract
                baseQuery = new StringBuilder("Select object(contract) from ContractData contract");
                    baseQuery.append(" WHERE contract.catalogId").append(catalogIds.size() == 1 ? " = " : " IN ").append("(:catalogIds)");
                    baseQuery.append(" AND contract.contractStatusCd = (:contractStatus)");
                    baseQuery.append(" ORDER BY contract.contractId");
               
                query = em.createQuery(baseQuery.toString());
                query.setParameter("catalogIds", catalogIds);
                query.setParameter("contractStatus", RefCodeNames.CONTRACT_STATUS_CD.ACTIVE);

                List<ContractData> contractDV = (List<ContractData>) query.getResultList();
                List<Long> contractIds = new ArrayList<Long>();
                if (Utility.isSet(contractDV)) {
                    contractIds = Utility.toIds(contractDV);
                }
               
                //Get contract items
                baseQuery = new StringBuilder("Select object(contractItem) from ContractItemData contractItem");
                    baseQuery.append(" WHERE contractItem.itemId = (:itemId)");
                    baseQuery.append(" AND contractItem.contractId").append(contractIds.size() == 1 ? " = " : " IN ").append("(:contractIds)");
                    baseQuery.append(" ORDER BY contractItem.contractId");
               
                query = em.createQuery(baseQuery.toString());
                query.setParameter("itemId", itemID);
                query.setParameter("contractIds", contractIds);

                List<ContractItemData> contractItemDV = (List<ContractItemData>) query.getResultList();

                //Combine data
                ItemContractCostView[] itemContrCostVwA = new ItemContractCostView[contractItemDV.size()];
                for (int ii = 0, jj = 0; ii < itemContrCostVwA.length; ii++) {
                    itemContrCostVwA[ii] = new ItemContractCostView();
                    ContractItemData ciD = contractItemDV.get(ii);
                    Long contractId = ciD.getContractId();
                    
                    itemContrCostVwA[ii].setItemId(ciD.getItemId());
                    itemContrCostVwA[ii].setContractId(contractId);
                    itemContrCostVwA[ii].setItemCost(ciD.getDistCost());
                    
                    for (; jj < contractDV.size(); jj++) {
                        ContractData cD = contractDV.get(jj);
                        if (cD.getContractId() < contractId) {
                            continue;
                        } else if (contractId.equals(cD.getContractId())) {
                            itemContrCostVwA[ii].setContractDesc(cD.getShortDesc());
                            Long catalogId = cD.getCatalogId();
                            itemContrCostVwA[ii].setCatalogId(catalogId);
                            
                            for (CatalogStructureData csD : catalogStructDV) {
                                if (catalogId.equals(csD.getCatalogId())) {
                                    itemContrCostVwA[ii].setDistId(csD.getBusEntityId());
                                    break;
                                }
                            }
                            jj++;
                            break;
                        } else {
                            break;
                        }
                    }
                }

                //Combine records for the same price and distributor
                for (int ii = 0; ii < itemContrCostVwA.length; ii++) {
                    ItemContractCostView iccVw = itemContrCostVwA[ii];
                    if (iccVw != null) {
                        itemContractCostViews.add(iccVw);
                        iccVw.setCatalogId(0L);
                        iccVw.setContractId(0L);
                        BigDecimal cost = iccVw.getItemCost();
                        cost.setScale(3, BigDecimal.ROUND_HALF_UP);
                        Long dId = iccVw.getDistId();
                   
                        for (int jj = ii + 1; jj < itemContrCostVwA.length; jj++) {
                            ItemContractCostView iccVw1 = itemContrCostVwA[jj];

                            if (iccVw1 != null && dId.equals(iccVw1.getDistId())) {
                                BigDecimal cost1 = iccVw1.getItemCost();
                                cost1.setScale(3, BigDecimal.ROUND_HALF_UP);

                                if (cost.equals(cost1)) {
                                    String contrStr = iccVw.getContractDesc() + " * " + iccVw1.getContractDesc();
                                    iccVw.setContractDesc(contrStr);
                                    itemContrCostVwA[jj] = null;
                                }
                            }
                        }
                    }
                }
                
                //Pick up distributors
                List<Long> distIds = new ArrayList<Long>();
                for (ItemContractCostView iccVw : itemContractCostViews) {
                    distIds.add(iccVw.getDistId());
                }
                
                baseQuery = new StringBuilder("Select object(busEntity) from BusEntityData busEntity");
                    baseQuery.append(" WHERE busEntity.busEntityId").append(distIds.size() == 1 ? " = " : " IN ").append("(:distIds)");
               
                query = em.createQuery(baseQuery.toString());
                query.setParameter("distIds", distIds);

                List<BusEntityData> busEntityDV = (List<BusEntityData>) query.getResultList();

                for (int ii = 0; ii < itemContractCostViews.size(); ii++) {
                    ItemContractCostView iccVw = (ItemContractCostView) itemContractCostViews.get(ii);
                    for (BusEntityData be : busEntityDV) {
                        if (be.getBusEntityId().equals(iccVw.getDistId())) {
                            iccVw.setDistDesc(be.getShortDesc());
                            break;
                        }
                    }
                }
            }
        }

        return itemContractCostViews;
    }
    
    @Override
    public List<ItemData> getItemDataList(Collection<Long> itemIds) {
        StringBuilder baseQuery = new StringBuilder("Select object(item) from ItemData item");
        baseQuery.append(" WHERE item.itemId").append(itemIds.size() == 1 ? " = " : " in ").append("(:itemIds)");

        Query query = em.createQuery(baseQuery.toString());
        query.setParameter("itemIds", itemIds);

        List<ItemData> itemList = (List<ItemData>) query.getResultList();
        
        return itemList;
    }
    
    @Override
    public OrderData cancelOrder(Long orderId) {
        OrderData updatedOrder = null;
        if (Utility.isSet(orderId)) {
            OrderData order = em.find(OrderData.class, orderId);
            
            if (order != null) {
                if (!RefCodeNames.ORDER_STATUS_CD.CANCELLED.equals(order.getOrderStatusCd())) {
                    order.setOrderStatusCd(RefCodeNames.ORDER_STATUS_CD.CANCELLED);
                    updatedOrder = super.update(order);
                }
            }
        }
        
        StringBuilder baseQuery = new StringBuilder("Select object(orderItem) from OrderItemData orderItem");
                    baseQuery.append(" WHERE orderItem.orderId = (:orderId)");
                    baseQuery.append(" AND (orderItem.orderItemStatusCd NOT IN (:orderItemStatus)");
                    baseQuery.append(" OR orderItem.orderItemStatusCd IS NULL)");

        Query query = em.createQuery(baseQuery.toString());
        query.setParameter("orderId", orderId);
        query.setParameter("orderItemStatus", Utility.toList(RefCodeNames.ORDER_ITEM_STATUS_CD.CANCELLED));

        List<OrderItemData> orderItems = (List<OrderItemData>) query.getResultList();
        if (Utility.isSet(orderItems)) {
            for (OrderItemData orderItem : orderItems) {
                orderItem.setOrderItemStatusCd(RefCodeNames.ORDER_ITEM_STATUS_CD.CANCELLED);
                super.update(orderItem);
            }
        }
        
        return updatedOrder;
    }
    
    @Override
    public void updateOrder(OrderChangeRequestView changeRequest) {
        
        if (changeRequest != null && changeRequest.getOrderInfo() != null) {
            if (changeRequest.getNewSiteId() != null) { // change Site
                while(true) {
                    BusEntityDAO beDAO = new BusEntityDAOImpl(em);
                    SiteDAO siteDAO = new SiteDAOImpl(em);
                    
                    Long preOrderId = changeRequest.getOrderInfo().getOrderData().getPreOrderId();
                    Long newSiteId = changeRequest.getNewSiteId();
                    Long oldSiteId = changeRequest.getOrderInfo().getOrderData().getSiteId();
                    String newSiteName = "";
                    String oldSiteName = "<Not Found>";
                    
                    if (newSiteId.equals(oldSiteId)) {
                        break;
                    }
                    PreOrderData preOrderD = em.find(PreOrderData.class, preOrderId);
                    
                    List<BusEntityData> sites = beDAO.find(Utility.toList(newSiteId));
                    if (Utility.isSet(sites)) {
                        newSiteName = sites.get(0).getShortDesc();
                    }
                    
                    sites = beDAO.find(Utility.toList(oldSiteId));
                    if (Utility.isSet(sites)) {
                        oldSiteName = sites.get(0).getShortDesc();
                    }
                    
                    List<BusEntityData> accounts = siteDAO.findSiteAccount(newSiteId);
                    Long newAccountId = null;
                    if (Utility.isSet(accounts)) {
                        newAccountId = accounts.get(0).getBusEntityId();
                    }
                    
                    List<ContractData> contracts = siteDAO.findSiteContract(newSiteId);
                    Long newContractId = null;
                    if (Utility.isSet(contracts)) {
                        newContractId = contracts.get(0).getContractId();
                    }
                    
                    preOrderD.setSiteId(newSiteId);
                    preOrderD.setSiteName(newSiteName);
                    preOrderD.setAccountId(newAccountId);
                    preOrderD.setContractId(newContractId);

                    em.merge(preOrderD);
                    
                    // Change OrderData
                    OrderData orderD = changeRequest.getOrderInfo().getOrderData();
                    orderD.setSiteId(preOrderD.getSiteId());
                    orderD.setAccountId(preOrderD.getAccountId());
                    orderD.setContractId(preOrderD.getContractId());
                    orderD.setOrderStatusCd(RefCodeNames.ORDER_STATUS_CD.RECEIVED);
                    
                    // Add order note
                    OrderPropertyData orderNote = new OrderPropertyData();
                    orderNote.setOrderId(orderD.getOrderId());
                    orderNote.setOrderPropertyTypeCd(RefCodeNames.ORDER_PROPERTY_TYPE_CD.ORDER_NOTE);
                    orderNote.setOrderPropertyStatusCd(RefCodeNames.ORDER_PROPERTY_STATUS_CD.ACTIVE);
                    orderNote.setShortDesc("Order Update");

                    StringBuilder noteText = new StringBuilder();
                    noteText.append("Order Site changed to: \"");
                    noteText.append(newSiteName);
                    noteText.append("\" (ID: ");
                    noteText.append(newSiteId);
                    noteText.append("), the previous was: \"");
                    noteText.append(oldSiteName);
                    noteText.append("\" (ID: ");
                    noteText.append(oldSiteId);
                    noteText.append(")");
                    orderNote.setValue(noteText.toString());
                    super.create(orderNote);    
                break;
                }
            }
            
            
            
        }
    }

}