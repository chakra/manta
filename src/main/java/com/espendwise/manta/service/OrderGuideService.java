package com.espendwise.manta.service;

import com.espendwise.manta.model.view.*;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.util.criteria.OrderGuideListViewCriteria;

import java.util.List;

public interface OrderGuideService {

    public List<OrderGuideListView> findOrderGuidesByCriteria(OrderGuideListViewCriteria criteria);

    public OrderGuideIdentView findOrderGuideIdentView(Long orderGuideId, Long siteId);

    public List<OrderGuideItemView> findOrderGuideItems(Long orderGuideId, Long siteId, Long catalogId);

    public OrderGuideIdentView updateOrderGuide(OrderGuideIdentView orderGuide) throws DatabaseUpdateException;

    public void deleteOrderGuide(OrderGuideIdentView orderGuide);

    public void deleteOrderGuideItems(List<OrderGuideItemView> items, Long orderGuideId);

    public List<OrderGuideItemView> fillOutOrderGuideItemsData(List<OrderGuideItemView> items, Long catalogId);

    public CatalogData getSiteCatalog(Long siteId);

    public PropertyData getShareOrderGuideProperty(Long siteId);

    public List<OrderScheduleData> findOrderGuideSchedules(Long orderGuideId, Long siteId);
    
    public List<OrderGuideData> findOrderGuidesByName(String orderGuideName, Long siteId, Long catalogId);
}
