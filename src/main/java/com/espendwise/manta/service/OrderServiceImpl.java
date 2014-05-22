package com.espendwise.manta.service;

import com.espendwise.manta.dao.OrderDAO;
import com.espendwise.manta.dao.OrderDAOImpl;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.OrderListViewCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.OrderUpdateNewSiteConstraint;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class OrderServiceImpl extends DataAccessService implements OrderService {

    private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderListView> findOrdersByCriteria(OrderListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.findOrdersByCriteria(criteria);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public OrderHeaderView findOrderHeader(Long orderId) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.findOrderHeader(orderId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public OrderIdentView findOrderToEdit(Long storeId, Long orderId) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.findOrderToEdit(storeId, orderId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderPropertyData> findOrderProperties(Long orderId, List<String> propertyTypes) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.findOrderProperties(orderId, propertyTypes);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderMetaData> findOrderMeta(Long orderId) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.findOrderMeta(orderId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderItemIdentView> findOrderItems(Long orderId, String erpPoNum, Long purchaseOrderId) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.findOrderItems(orderId, erpPoNum, purchaseOrderId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderItemData> findOrderItemDataList(Long orderId) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.findOrderItemDataList(orderId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<FreightHandlerView> getFreightHandlerDetails(List<Long> freightHandlerIds) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.getFreightHandlerDetails(freightHandlerIds);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderAddressData> findOrderAddresses(Long orderId) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.findOrderAddresses(orderId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<NoteJoinView> getSiteCrcNotes(Long siteId) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);
        
        return orderDao.getSiteCrcNotes(siteId);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<InvoiceCustView> getInvoiceCustByWebOrderNum(String webOrderNum, Long storeId, Boolean fullInfo) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);

        return orderDao.getInvoiceCustByWebOrderNum(webOrderNum, storeId, fullInfo);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemContractCostView> getItemContractCost(Long accountId, Long itemSkuNum, Long distId, String distErpNum) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);

        return orderDao.getItemContractCost(accountId, itemSkuNum, distId, distErpNum);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemData> getItemDataList(Collection<Long> itemIds) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);

        return orderDao.getItemDataList(itemIds);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrderData cancelOrder(Long orderId) {
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);

        return orderDao.cancelOrder(orderId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateOrder(OrderChangeRequestView changeRequest) {

        if (changeRequest.getNewSiteId() != null) {
            ServiceLayerValidation validation = new ServiceLayerValidation();
            validation.addRule(new OrderUpdateNewSiteConstraint(changeRequest.getNewSiteId()));
            validation.validate();
        }
    	
        EntityManager entityManager = getEntityManager();
        OrderDAO orderDao = new OrderDAOImpl(entityManager);

        orderDao.updateOrder(changeRequest);
    }
}
