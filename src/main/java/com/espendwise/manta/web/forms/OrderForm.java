package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.data.OrderData;
import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.web.util.LocateAssistant;
import com.espendwise.manta.web.validator.OrderFormValidator;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import java.math.BigDecimal;
import java.util.List;

@Validation(OrderFormValidator.class)
public class OrderForm extends WebForm implements Resetable, Initializable {

    private boolean initialize;

    private Long orderId;
    private OrderIdentView orderInfo;
    private List<OrderItemIdentView> orderItems;

    private OrderPropertyData orderNote;
    private CumulativeSummaryView cumulativeSummary;
    private List<NoteJoinView> siteNotes;
    private List<OrderPropertyData> customerOrderNotes;
    private DistributionSummaryView distributionSummary;

    private List<Pair<String, String>> orderStatuses;
    private List<Pair<String, String>> workflowStatuses;
    private List<Pair<String, String>> orderItemStatuses;
    private List<Pair<String, String>> poItemStatuses;
    private List<InvoiceCustView> invoices;
    private SelectableObjects selectToCancel;
    
    private String receivedDate;                // ERP Order Date
    private String orderedDate;
    private String newOrderDate;
    private String newContractId;
    private String locationAddress;
    private String orderStatus;
    private String workflowStatus;

    private String[] selectItems;
    
    private String orderPlacedBy;

    private String totalAmount;
    private String totalFreightCost;
    private String totalMiscCost;
    private String totalTaxCost;
    private String subTotal;
    private String smallOrderFee;
    private String fuelSurCharge;
    private String rushOrderCharge;
    private String discount;

    private BigDecimal totalAmountValue;
    private BigDecimal totalFreightCostValue;
    private BigDecimal totalMiscCostValue;
    private BigDecimal totalTaxCostValue;
    private BigDecimal subTotalValue;
    private BigDecimal smallOrderFeeValue;
    private BigDecimal fuelSurChargeValue;
    private BigDecimal rushOrderChargeValue;
    private BigDecimal discountValue;

    private String newSiteId;
    private String orderItemIdToView;
    private String customerComment;

    private Boolean processCustomerWorkflow;
    private Boolean simpleServiceOrder;
    private Boolean showCancelButton = false;
    private Boolean applyBudget = true;
    private Boolean rebillOrder = false;
    private Boolean applyBudgetCopy;
    private Boolean reBillOrderCopy;    
    private Boolean showDistNote = false;
    private Boolean fullControl = false;
    private Boolean handlingChanged;
    private Boolean bypassOrderRouting;

    private Boolean[] reSaleCopy;
    private Boolean[] taxExemptCopy;
    
    private Integer returnedNum;
    private Integer handlingChoice = 2;
    
    private List<SiteListView> filteredLocations;
    private String locationFilter;
    
    private String storeType;
    
    /**
     * Based on the current totals for this orderInfo
     * calculate the total amount.  This include all product
     * charges and various freight, handling, rush, and tax charges.
     */
    public void calculateTotalAmount() {
        totalAmountValue = new BigDecimal(0);
        
        if (totalFreightCostValue != null) {
            totalAmountValue = totalAmountValue.add(totalFreightCostValue);
        }
        if (totalMiscCostValue != null) {
            totalAmountValue = totalAmountValue.add(totalMiscCostValue);
        }
        if (rushOrderChargeValue != null) {
            totalAmountValue = totalAmountValue.add(rushOrderChargeValue);
        }
        if (fuelSurChargeValue != null ) {
            totalAmountValue = totalAmountValue.add(fuelSurChargeValue);
        }
        if (smallOrderFeeValue != null ) {
            totalAmountValue = totalAmountValue.add(smallOrderFeeValue);
        }
        
        if (discountValue != null) {
            totalAmountValue = totalAmountValue.add((discountValue.compareTo(BigDecimal.ZERO) == 1) ? discountValue.negate() : discountValue);
        }
        
        if (totalTaxCostValue != null) {
            totalAmountValue = totalAmountValue.add(totalTaxCostValue);
        }
        if (subTotalValue != null) {
            totalAmountValue = totalAmountValue.add(subTotalValue);
        }
        totalAmount = totalAmountValue.toPlainString();
    }

    @Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public boolean isNew() {
      return isInitialized() && (orderId  == null || orderId == 0);
    }

    @Override
    public String toString() {
        return "OrderForm{)";
    }

    @Override
    public void reset() {
    }

    public Boolean getApplyBudget() {
        return applyBudget;
    }

    public void setApplyBudget(Boolean applyBudget) {
        this.applyBudget = applyBudget;
    }

    public BigDecimal getFuelSurChargeValue() {
        return fuelSurChargeValue;
    }

    public void setFuelSurChargeValue(BigDecimal fuelSurChargeValue) {
        this.fuelSurChargeValue = fuelSurChargeValue;
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public List<SiteListView> getFilteredLocations() {
        return filteredLocations;
    }

    public void setFilteredLocations(List<SiteListView> filteredLocations) {
        this.filteredLocations = filteredLocations;
    }

    public String getLocationFilter() {
        return locationFilter;
    }

    public void setLocationFilter(String locationFilter) {
        this.locationFilter = locationFilter;
    }
    
    public String getFilteredLocationCommaNames() {
        return LocateAssistant.getFilteredSiteCommaNames(getFilteredLocations());
    }

    public String getFilteredLocationCommaIds() {
        return LocateAssistant.getFilteredSiteCommaIds(getFilteredLocations());
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getNewOrderDate() {
        return newOrderDate;
    }

    public void setNewOrderDate(String newOrderDate) {
        this.newOrderDate = newOrderDate;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public OrderIdentView getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderIdentView orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public List<OrderItemIdentView> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemIdentView> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Pair<String, String>> getOrderStatuses() {
        return orderStatuses;
    }

    public void setOrderStatuses(List<Pair<String, String>> orderStatuses) {
        this.orderStatuses = orderStatuses;
    }

    public List<Pair<String, String>> getWorkflowStatuses() {
        return workflowStatuses;
    }

    public void setWorkflowStatuses(List<Pair<String, String>> workflowStatuses) {
        this.workflowStatuses = workflowStatuses;
    }

    public Boolean getRebillOrder() {
        return rebillOrder;
    }

    public void setRebillOrder(Boolean rebillOrder) {
        this.rebillOrder = rebillOrder;
    }

    public String getSmallOrderFee() {
        return smallOrderFee;
    }

    public void setSmallOrderFee(String smallOrderFee) {
        this.smallOrderFee = smallOrderFee;
    }

    public BigDecimal getSubTotalValue() {
        return subTotalValue;
    }

    public void setSubTotalValue(BigDecimal subTotalValue) {
        this.subTotalValue = subTotalValue;
    }

    public BigDecimal getSmallOrderFeeValue() {
        return smallOrderFeeValue;
    }

    public void setSmallOrderFeeValue(BigDecimal smallOrderFeeValue) {
        this.smallOrderFeeValue = smallOrderFeeValue;
    }

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(String workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public OrderPropertyData getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(OrderPropertyData orderNote) {
        this.orderNote = orderNote;
    }

    public Boolean getSimpleServiceOrder() {
        return simpleServiceOrder;
    }

    public void setSimpleServiceOrder(Boolean simpleServiceOrder) {
        this.simpleServiceOrder = simpleServiceOrder;
    }

    public CumulativeSummaryView getCumulativeSummary() {
        return cumulativeSummary;
    }

    public void setCumulativeSummary(CumulativeSummaryView cumulativeSummary) {
        this.cumulativeSummary = cumulativeSummary;
    }

    public Boolean getShowCancelButton() {
        return showCancelButton;
    }

    public void setShowCancelButton(Boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
    }

    public Boolean getApplyBudgetCopy() {
        return applyBudgetCopy;
    }

    public void setApplyBudgetCopy(Boolean applyBudgetCopy) {
        this.applyBudgetCopy = applyBudgetCopy;
    }

    public Boolean getBypassOrderRouting() {
        return bypassOrderRouting;
    }

    public void setBypassOrderRouting(Boolean bypassOrderRouting) {
        this.bypassOrderRouting = bypassOrderRouting;
    }

    public Boolean getFullControl() {
        return fullControl;
    }

    public void setFullControl(Boolean fullControl) {
        this.fullControl = fullControl;
    }

    public Boolean getHandlingChanged() {
        return handlingChanged;
    }

    public void setHandlingChanged(Boolean handlingChanged) {
        this.handlingChanged = handlingChanged;
    }

    public Boolean getProcessCustomerWorkflow() {
        return processCustomerWorkflow;
    }

    public void setProcessCustomerWorkflow(Boolean processCustomerWorkflow) {
        this.processCustomerWorkflow = processCustomerWorkflow;
    }

    public Boolean getReBillOrderCopy() {
        return reBillOrderCopy;
    }

    public void setReBillOrderCopy(Boolean reBillOrderCopy) {
        this.reBillOrderCopy = reBillOrderCopy;
    }

    public Boolean[] getReSaleCopy() {
        return reSaleCopy;
    }

    public void setReSaleCopy(Boolean[] reSaleCopy) {
        this.reSaleCopy = reSaleCopy;
    }

    public Boolean getShowDistNote() {
        return showDistNote;
    }

    public void setShowDistNote(Boolean showDistNote) {
        this.showDistNote = showDistNote;
    }

    public List<NoteJoinView> getSiteNotes() {
        return siteNotes;
    }

    public void setSiteNotes(List<NoteJoinView> siteNotes) {
        this.siteNotes = siteNotes;
    }

    public Boolean[] getTaxExemptCopy() {
        return taxExemptCopy;
    }

    public void setTaxExemptCopy(Boolean[] taxExemptCopy) {
        this.taxExemptCopy = taxExemptCopy;
    }

    public String getCustomerComment() {
        return customerComment;
    }

    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }

    public List<OrderPropertyData> getCustomerOrderNotes() {
        return customerOrderNotes;
    }

    public void setCustomerOrderNotes(List<OrderPropertyData> customerOrderNotes) {
        this.customerOrderNotes = customerOrderNotes;
    }

    public String getFuelSurCharge() {
        return fuelSurCharge;
    }

    public void setFuelSurCharge(String fuelSurCharge) {
        this.fuelSurCharge = fuelSurCharge;
    }

    public Integer getHandlingChoice() {
        return handlingChoice;
    }

    public void setHandlingChoice(Integer handlingChoice) {
        this.handlingChoice = handlingChoice;
    }

    public String getOrderItemIdToView() {
        return orderItemIdToView;
    }

    public void setOrderItemIdToView(String orderItemIdToView) {
        this.orderItemIdToView = orderItemIdToView;
    }

    public List<Pair<String, String>> getOrderItemStatuses() {
        return orderItemStatuses;
    }

    public void setOrderItemStatuses(List<Pair<String, String>> orderItemStatuses) {
        this.orderItemStatuses = orderItemStatuses;
    }

    public String getOrderPlacedBy() {
        return orderPlacedBy;
    }

    public void setOrderPlacedBy(String orderPlacedBy) {
        this.orderPlacedBy = orderPlacedBy;
    }

    public List<Pair<String, String>> getPoItemStatuses() {
        return poItemStatuses;
    }

    public void setPoItemStatuses(List<Pair<String, String>> poItemStatuses) {
        this.poItemStatuses = poItemStatuses;
    }

    public Integer getReturnedNum() {
        return returnedNum;
    }

    public void setReturnedNum(Integer returnedNum) {
        this.returnedNum = returnedNum;
    }

    public String getRushOrderCharge() {
        return rushOrderCharge;
    }

    public void setRushOrderCharge(String rushOrderCharge) {
        this.rushOrderCharge = rushOrderCharge;
    }

    public BigDecimal getRushOrderChargeValue() {
        return rushOrderChargeValue;
    }

    public void setRushOrderChargeValue(BigDecimal rushOrderChargeValue) {
        this.rushOrderChargeValue = rushOrderChargeValue;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public String getNewSiteId() {
        return newSiteId;
    }

    public void setNewSiteId(String newSiteId) {
        this.newSiteId = newSiteId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountValue() {
        return totalAmountValue;
    }

    public void setTotalAmountValue(BigDecimal totalAmountValue) {
        this.totalAmountValue = totalAmountValue;
    }

    public String getTotalFreightCost() {
        return totalFreightCost;
    }

    public void setTotalFreightCost(String totalFreightCost) {
        this.totalFreightCost = totalFreightCost;
    }

    public BigDecimal getTotalFreightCostValue() {
        return totalFreightCostValue;
    }

    public void setTotalFreightCostValue(BigDecimal totalFreightCostValue) {
        this.totalFreightCostValue = totalFreightCostValue;
    }

    public String getTotalMiscCost() {
        return totalMiscCost;
    }

    public void setTotalMiscCost(String totalMiscCost) {
        this.totalMiscCost = totalMiscCost;
    }

    public BigDecimal getTotalMiscCostValue() {
        return totalMiscCostValue;
    }

    public void setTotalMiscCostValue(BigDecimal totalMiscCostValue) {
        this.totalMiscCostValue = totalMiscCostValue;
    }

    public BigDecimal getTotalTaxCostValue() {
        return totalTaxCostValue;
    }

    public void setTotalTaxCostValue(BigDecimal totalTaxCostValue) {
        this.totalTaxCostValue = totalTaxCostValue;
    }

    public String getTotalTaxCost() {
        return totalTaxCost;
    }

    public void setTotalTaxCost(String totalTaxCost) {
        this.totalTaxCost = totalTaxCost;
    }

    public String getNewContractId() {
        return newContractId;
    }

    public void setNewContractId(String newContractId) {
        this.newContractId = newContractId;
    }

    public String[] getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(String[] selectItems) {
        this.selectItems = selectItems;
    }

    public List<InvoiceCustView> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceCustView> invoices) {
        this.invoices = invoices;
    }

    public DistributionSummaryView getDistributionSummary() {
        return distributionSummary;
    }

    public void setDistributionSummary(DistributionSummaryView distributionSummary) {
        this.distributionSummary = distributionSummary;
    }
    
    public void setFilteredItem(Integer index, List<ProductListView> filteredItems) {
        OrderItemIdentView orderItem = (OrderItemIdentView) orderItems.get(index);
        if (Utility.isSet(filteredItems)) {
            orderItem.setNewItemView(filteredItems.get(0));
        }
    }
    
    public void setFilteredAsset(Integer index, List<AssetListView> filteredAssets) {
        OrderItemIdentView orderItem = (OrderItemIdentView) orderItems.get(index);
        if (Utility.isSet(filteredAssets)) {
            orderItem.setNewAssetView(filteredAssets.get(0));
        }
    }
    
    public void setFilteredDist(Integer index, List<DistributorListView> filteredDists) {
        OrderItemIdentView orderItem = (OrderItemIdentView) orderItems.get(index);
        if (Utility.isSet(filteredDists)) {
            orderItem.setNewDistView(filteredDists.get(0));
        }
    }
    
    public void setFilteredCwCost(Integer index, List<ItemContractCostView> filteredCwCosts) {
        OrderItemIdentView orderItem = (OrderItemIdentView) orderItems.get(index);
        if (Utility.isSet(filteredCwCosts)) {
            orderItem.setNewCwCostView(filteredCwCosts.get(0));
        }
    }
    
    public void setFilteredService(Integer index, List<ServiceListView> filteredServices) {
        OrderItemIdentView orderItem = (OrderItemIdentView) orderItems.get(index);
        if (Utility.isSet(filteredServices)) {
            orderItem.setNewServiceView(filteredServices.get(0));
        }
    }

    public SelectableObjects getSelectToCancel() {
        return selectToCancel;
    }

    public void setSelectToCancel(SelectableObjects selectToCancel) {
        this.selectToCancel = selectToCancel;
    }

}
