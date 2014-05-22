package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.OrderListView;
import java.util.List;

public class OrderFilterResultForm  extends AbstractFilterResult<OrderListView> {

    private List<OrderListView> orders;

    public List<OrderListView> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderListView> orders) {
        this.orders = orders;
    }

    @Override
    public List<OrderListView> getResult() {
        return orders;
    }

    @Override
    public void reset() {
        super.reset();
        this.orders = null;
    }


}
