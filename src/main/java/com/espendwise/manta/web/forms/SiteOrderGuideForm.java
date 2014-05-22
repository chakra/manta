package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.OrderGuideListView;
import com.espendwise.manta.spi.Initializable;

import java.util.List;

public class SiteOrderGuideForm extends AbstractFilterResult<OrderGuideListView> implements Initializable {

    private boolean initialize;

    private Long siteId;

    private List<OrderGuideListView> orderGuides;

    public SiteOrderGuideForm() {
    }

    public SiteOrderGuideForm(Long siteId) {
        this.siteId = siteId;
    }

     public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }


    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public void setOrderGuides(List<OrderGuideListView> orderGuides) {
        this.orderGuides = orderGuides;
    }

    public List<OrderGuideListView> getOrderGuides() {
        return orderGuides;
    }

   @Override
    public List<OrderGuideListView> getResult() {
        return orderGuides;
    }

    public void reset() {
        orderGuides = null;
   }

   @Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

}
