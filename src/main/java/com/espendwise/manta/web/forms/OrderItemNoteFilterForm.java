package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.view.OrderItemIdentView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.OrderItemNoteFilterFormValidator;
import java.util.List;


@Validation(OrderItemNoteFilterFormValidator.class)
public class OrderItemNoteFilterForm extends WebForm implements Resetable, Initializable {

    private String orderItemNoteField;
    
    private String type;
    private List<OrderPropertyData> orderNotes;
    private List<OrderItemIdentView> orderItemViews;

    private Long orderId;
    private Long orderItemId;
    private boolean init;
    private boolean view;

    public OrderItemNoteFilterForm() {
        super();
    }

    public String getOrderItemNoteField() {
        return orderItemNoteField;
    }

    public void setOrderItemNoteField(String orderItemNoteField) {
        this.orderItemNoteField = orderItemNoteField;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    @Override
    public void reset() {
        this.orderItemNoteField = null;
    }

    @Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

    public boolean getView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public List<OrderPropertyData> getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(List<OrderPropertyData> orderNotes) {
        this.orderNotes = orderNotes;
    }

    public List<OrderItemIdentView> getOrderItemViews() {
        return orderItemViews;
    }

    public void setOrderItemViews(List<OrderItemIdentView> orderItemViews) {
        this.orderItemViews = orderItemViews;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
