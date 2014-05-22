package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.OrderItemData;
import com.espendwise.manta.model.view.DistributorInfoView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import java.util.List;

public class OrderPrintTempPoForm extends WebForm implements Resetable, Initializable {
    
    private Long orderId;
    private List<OrderItemData> distOrderItems;
    private Long selectedDistributorId;
    private DistributorInfoView distributorView;
    private List<BusEntityData> distributorsInfo;

    private boolean init;

    public OrderPrintTempPoForm() {
        super();
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    @Override
    public void initialize() {
        reset();
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;
    }

    public List<BusEntityData> getDistributorsInfo() {
        return distributorsInfo;
    }

    public void setDistributorsInfo(List<BusEntityData> distributorsInfo) {
        this.distributorsInfo = distributorsInfo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSelectedDistributorId() {
        return selectedDistributorId;
    }

    public void setSelectedDistributorId(Long selectedDistributorId) {
        this.selectedDistributorId = selectedDistributorId;
    }

    public DistributorInfoView getDistributorView() {
        return distributorView;
    }

    public void setDistributorView(DistributorInfoView distributorView) {
        this.distributorView = distributorView;
    }

    public List<OrderItemData> getDistOrderItems() {
        return distOrderItems;
    }

    public void setDistOrderItems(List<OrderItemData> distOrderItems) {
        this.distOrderItems = distOrderItems;
    }

    @Override
    public void reset() {
        this.orderId = null;
        this.distributorsInfo = null;
    }

}
