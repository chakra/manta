package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderGuideListViewCriteria implements Serializable {

    private List<String> orderStatuses;

    private Long siteId;

    private List<String> orderGuideTypeCds;

    private Integer limit;

    public OrderGuideListViewCriteria(Long siteId, Integer limit) {
        this.siteId = siteId;
        this.limit = limit;
    }

    public OrderGuideListViewCriteria(Long siteId) {
        this.siteId = siteId;
    }

    public OrderGuideListViewCriteria() {
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public List<String> getOrderGuideTypeCds() {
        return orderGuideTypeCds;
    }

    public void setOrderGuideTypeCds(List<String> orderGuideTypeCds) {
        this.orderGuideTypeCds = orderGuideTypeCds;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }


    @Override
    public String toString() {
        return "OrderListViewCriteria{" +
                ", siteId=" + siteId +
                ", limit=" + limit +
                '}';
    }
}
