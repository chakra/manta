package com.espendwise.manta.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.CatalogDAO;
import com.espendwise.manta.dao.CatalogDAOImpl;
import com.espendwise.manta.dao.OrderGuideDAO;
import com.espendwise.manta.dao.OrderGuideDAOImpl;
import com.espendwise.manta.dao.PropertyDAO;
import com.espendwise.manta.dao.PropertyDAOImpl;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.OrderGuideData;
import com.espendwise.manta.model.data.OrderScheduleData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.ScheduleData;
import com.espendwise.manta.model.view.CatalogAssocView;
import com.espendwise.manta.model.view.OrderGuideIdentView;
import com.espendwise.manta.model.view.OrderGuideItemView;
import com.espendwise.manta.model.view.OrderGuideListView;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SitePropertyExtraCode;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.OrderGuideListViewCriteria;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class OrderGuideServiceImpl extends DataAccessService implements OrderGuideService {

    private static final Logger logger = Logger.getLogger(OrderGuideServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderGuideListView> findOrderGuidesByCriteria(OrderGuideListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);

        return orderGuideDao.findOrderGuidesByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public OrderGuideIdentView findOrderGuideIdentView(Long orderGuideId, Long siteId) {

        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);


        OrderGuideIdentView orderGuide = orderGuideDao.findOrderGuideIdentView(orderGuideId);
        orderGuide.setShareBuyerOrderGuides(getShareOrderGuideProperty(siteId));

        return orderGuide;        
    }




    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderGuideItemView> findOrderGuideItems(Long orderGuideId, Long siteId, Long catalogId) {

        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);

        return orderGuideDao.findOrderGuideItems(orderGuideId, siteId, catalogId);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OrderGuideIdentView updateOrderGuide(OrderGuideIdentView orderGuide)   throws DatabaseUpdateException{

        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);

        return orderGuideDao.updateOrderGuide(orderGuide);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteOrderGuide(OrderGuideIdentView orderGuide) {

        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);

        orderGuideDao.deleteOrderGuide(orderGuide);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteOrderGuideItems(List<OrderGuideItemView> items, Long orderGuideId) {

        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);

        orderGuideDao.deleteOrderGuideItems(items, orderGuideId);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<OrderGuideItemView> fillOutOrderGuideItemsData(List<OrderGuideItemView> items, Long catalogId) {

        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);

        return orderGuideDao.fillOutOrderGuideItemsData(items, catalogId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public CatalogData getSiteCatalog(Long siteId) {
        EntityManager entityManager = getEntityManager();
        CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
        CatalogData catalog = null;
        try {
         CatalogAssocView cV = catalogDao.findEntityCatalog(siteId, RefCodeNames.CATALOG_ASSOC_CD.CATALOG_SITE);
         if (cV != null) {
             catalog  = cV.getCatalog();
         }
        } catch (IllegalDataStateException e) {
            return null;
        }
        return catalog;             
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public PropertyData getShareOrderGuideProperty(Long siteId) {
        EntityManager entityManager = getEntityManager();
        PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

        List<PropertyData> properties = propertyDao.findEntityNamedActiveProperties(siteId,
                Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.SHARE_ORDER_GUIDES));

        PropertyData shareBuyerProperty = PropertyUtil.findP(properties, RefCodeNames.PROPERTY_TYPE_CD.SHARE_ORDER_GUIDES);
        //STJSCR-65
        if (shareBuyerProperty == null) {
        	shareBuyerProperty = PropertyUtil.findP(properties, SitePropertyExtraCode.SHARE_BUYER_GUIDES);
        }

        return shareBuyerProperty;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderScheduleData> findOrderGuideSchedules(Long orderGuideId, Long siteId) {
        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);

        List<OrderScheduleData> schedules = orderGuideDao.findOrderGuideSchedules(orderGuideId, siteId);

        return schedules;
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderGuideData> findOrderGuidesByName(String orderGuideName, Long siteId, Long catalogId) {
    
        EntityManager entityManager = getEntityManager();
        OrderGuideDAO orderGuideDao = new OrderGuideDAOImpl(entityManager);

        List<OrderGuideData> orderGuides = orderGuideDao.findOrderGuidesByName(orderGuideName, siteId, catalogId);

        return orderGuides;
    
    }

}
