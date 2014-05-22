package com.espendwise.manta.dao;

import com.espendwise.manta.model.view.*;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.util.criteria.OrderGuideListViewCriteria;

import java.util.List;
import java.util.Set;

public interface OrderGuideDAO {

    public List<OrderGuideListView> findOrderGuidesByCriteria(OrderGuideListViewCriteria criteria);

    public OrderGuideIdentView findOrderGuideIdentView(Long orderGuideId);

    public List<OrderGuideItemView> findOrderGuideItems(Long orderGuideId, Long siteId, Long catalogId);

    public OrderGuideIdentView updateOrderGuide(OrderGuideIdentView orderGuide);

    public void deleteOrderGuide(OrderGuideIdentView orderGuide);

    public void deleteOrderGuideItems(List<OrderGuideItemView> items, Long orderGuideId) ;

    public List<OrderGuideItemView> fillOutOrderGuideItemsData(List<OrderGuideItemView> items, Long catalogId);

    public List<OrderGuideData> findOrderGuides(Set<String> pNames, List<String> pTypes) ;
    
    public List<OrderScheduleData> findOrderGuideSchedules(Long orderGuideId, Long siteId);
    
    public List<OrderGuideData> findOrderGuidesByName(String orderGuideName, Long siteId, Long catalogId);
}
