package com.espendwise.webservice.restful.value;

import com.espendwise.manta.model.data.OrderItemData;
import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.data.ItemSubstitutionData;
import com.espendwise.manta.model.data.OrderItemActionData;
import com.espendwise.manta.model.data.InvoiceDistDetailData;
import com.espendwise.manta.model.data.InvoiceCustDetailData;
import com.espendwise.manta.model.data.AssetData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.data.PurchaseOrderData;
import com.espendwise.manta.model.data.InvoiceDistData;
import com.espendwise.manta.model.data.ShoppingInfoData;
import com.espendwise.manta.model.data.OrderFreightData;
import com.espendwise.manta.model.data.OrderAddOnChargeData;
import com.espendwise.manta.model.data.ReturnRequestDetailData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderItemDescData implements Serializable {  
    private static final long serialVersionUID = -7816140406663611385L;

    private OrderItemData orderItem;
    private List<ItemSubstitutionData> orderItemSubstitutionList;
    private List<OrderItemActionData> orderItemActionList;
    private List<OrderItemActionDescData> orderItemActionDescList;
    private List<InvoiceDistDetailData> invoiceDistDetailList;
    private List<InvoiceCustDetailData> invoiceCustDetailList;
    private List<OrderPropertyData> orderItemNotes;
    private PurchaseOrderData purchaseOrderData;
    private List<InvoiceDistData> invoiceDistDataList;
    private OrderPropertyData distPoNote;
    private AssetData assetInfo;
    private ItemData itemInfo;
    private String openLineStatusCd;
    private OrderFreightData orderFreight;
    private OrderFreightData orderHandling;
    private OrderAddOnChargeData orderDiscount;
    private OrderAddOnChargeData orderFuelSurcharge;
    private OrderAddOnChargeData orderSmallOrderFee;

    //for the invoice page to add invoice dist details.
    private InvoiceDistDetailData newInvoiceDistDetail;
    private InvoiceDistDetailData workingInvoiceDistDetailData;
    
    private String itemQuantityS;
    private String cwCostS;
    private String distLineNumS;
    private String itemSkuNumS;
    private String itemIdS;
    private String orderItemIdS;
    private String lineTotalS;
    private String itemPriceS;
    private boolean reSale = false;

    private String distName;
    private String newDistName;
    private String distIdS;
    private String newAssetName;
    private String assetIdS;
    private String newServiceName;

    private Long distId;
    private boolean hasNote = false;
    private String targetShipDateString;
    private String shipStatus;
    private String qtyReturnedString;
    private ReturnRequestDetailData returnRequestDetailData;
    private Date deliveryDate;

    private String itemStatus = "";
    private String poItemStatus = "";

    private String distRuntimeDisplayName;
    private BigDecimal calculatedSalesTax;
    private String standardProductList;
    private String catalogItemSkuNum;
    private boolean taxExempt = false;
    private List<ShoppingInfoData> shoppingHistory;
    private String newOrderItemActionQtyS;
    private Date targetShipDate;
    private String itemQuantityRecvdS;
    private BigDecimal actualCost;
    private int actualQty;
    private int newOrderItemActionQty;

    public OrderItemDescData() {}

    public List getShoppingHistory() {
        if (shoppingHistory == null) {
            shoppingHistory = new ArrayList();
        }
        return shoppingHistory;
    }

    public void setShoppingHistory(List<ShoppingInfoData> v) {
        shoppingHistory = new ArrayList();
        if (v != null && v.size() > 0) {
            for (ShoppingInfoData sinfo : v) {
                if (sinfo.getItemId().equals(orderItem.getItemId())) {
                    shoppingHistory.add(sinfo);
                }
            }
        }
    }

    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public int getActualQty() {
        return actualQty;
    }

    public void setActualQty(int actualQty) {
        this.actualQty = actualQty;
    }

    public BigDecimal getCalculatedSalesTax() {
        return calculatedSalesTax;
    }

    public void setCalculatedSalesTax(BigDecimal calculatedSalesTax) {
        this.calculatedSalesTax = calculatedSalesTax;
    }

    public String getDistRuntimeDisplayName() {
        return distRuntimeDisplayName;
    }

    public void setDistRuntimeDisplayName(String distRuntimeDisplayName) {
        this.distRuntimeDisplayName = distRuntimeDisplayName;
    }

    public List<InvoiceDistData> getInvoiceDistDataList() {
        return invoiceDistDataList;
    }

    public void setInvoiceDistDataList(List<InvoiceDistData> invoiceDistDataList) {
        this.invoiceDistDataList = invoiceDistDataList;
    }

    public String getItemQuantityRecvdS() {
        return itemQuantityRecvdS;
    }

    public void setItemQuantityRecvdS(String itemQuantityRecvdS) {
        this.itemQuantityRecvdS = itemQuantityRecvdS;
    }

    public String getAssetIdS() {
        return assetIdS;
    }

    public void setAssetIdS(String assetIdS) {
        this.assetIdS = assetIdS;
    }

    public AssetData getAssetInfo() {
        return assetInfo;
    }

    public void setAssetInfo(AssetData assetInfo) {
        this.assetInfo = assetInfo;
    }

    public String getCatalogItemSkuNum() {
        return catalogItemSkuNum;
    }

    public void setCatalogItemSkuNum(String catalogItemSkuNum) {
        this.catalogItemSkuNum = catalogItemSkuNum;
    }

    public String getCwCostS() {
        return cwCostS;
    }

    public void setCwCostS(String cwCostS) {
        this.cwCostS = cwCostS;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getDistId() {
        return distId;
    }

    public void setDistId(Long distId) {
        this.distId = distId;
    }

    public String getDistIdS() {
        return distIdS;
    }

    public void setDistIdS(String distIdS) {
        this.distIdS = distIdS;
    }

    public String getDistLineNumS() {
        return distLineNumS;
    }

    public void setDistLineNumS(String distLineNumS) {
        this.distLineNumS = distLineNumS;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String mDistName) {
        this.distName = mDistName;
    }

    public OrderPropertyData getDistPoNote() {
        return distPoNote;
    }

    public void setDistPoNote(OrderPropertyData distPoNote) {
        this.distPoNote = distPoNote;
    }

    public boolean getHasNote() {
        return hasNote;
    }

    public void setHasNote(boolean hasNote) {
        this.hasNote = hasNote;
    }

    public List<InvoiceCustDetailData> getInvoiceCustDetailList() {
        return invoiceCustDetailList;
    }

    public void setInvoiceCustDetailList(List<InvoiceCustDetailData> invoiceCustDetailList) {
        this.invoiceCustDetailList = invoiceCustDetailList;
    }

    public List<InvoiceDistDetailData> getInvoiceDistDetailList() {
        return invoiceDistDetailList;
    }

    public void setInvoiceDistDetailList(List<InvoiceDistDetailData> invoiceDistDetailList) {
        this.invoiceDistDetailList = invoiceDistDetailList;
    }

    public String getItemIdS() {
        return itemIdS;
    }

    public void setItemIdS(String itemIdS) {
        this.itemIdS = itemIdS;
    }

    public ItemData getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemData itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getItemPriceS() {
        return itemPriceS;
    }

    public void setItemPriceS(String itemPriceS) {
        this.itemPriceS = itemPriceS;
    }

    public String getItemQuantityS() {
        return itemQuantityS;
    }

    public void setItemQuantityS(String itemQuantityS) {
        this.itemQuantityS = itemQuantityS;
    }

    public String getItemSkuNumS() {
        return itemSkuNumS;
    }

    public void setItemSkuNumS(String itemSkuNumS) {
        this.itemSkuNumS = itemSkuNumS;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getLineTotalS() {
        return lineTotalS;
    }

    public void setLineTotalS(String lineTotalS) {
        this.lineTotalS = lineTotalS;
    }

    public String getNewAssetName() {
        return newAssetName;
    }

    public void setNewAssetName(String newAssetName) {
        this.newAssetName = newAssetName;
    }

    public String getNewDistName() {
        return newDistName;
    }

    public void setNewDistName(String newDistName) {
        this.newDistName = newDistName;
    }

    public InvoiceDistDetailData getNewInvoiceDistDetail() {
        return newInvoiceDistDetail;
    }

    public void setNewInvoiceDistDetail(InvoiceDistDetailData newInvoiceDistDetail) {
        this.newInvoiceDistDetail = newInvoiceDistDetail;
    }

    public String getNewServiceName() {
        return newServiceName;
    }

    public void setNewServiceName(String newServiceName) {
        this.newServiceName = newServiceName;
    }

    public OrderItemData getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemData orderItem) {
        this.orderItem = orderItem;
    }

    public List<OrderItemActionDescData> getOrderItemActionDescList() {
        return orderItemActionDescList;
    }

    public void setOrderItemActionDescList(List<OrderItemActionDescData> orderItemActionDescList) {
        this.orderItemActionDescList = orderItemActionDescList;
    }

    public List<OrderItemActionData> getOrderItemActionList() {
        return orderItemActionList;
    }

    public void setOrderItemActionList(List<OrderItemActionData> orderItemActionList) {
        this.orderItemActionList = orderItemActionList;
    }

    public String getOrderItemIdS() {
        return orderItemIdS;
    }

    public void setOrderItemIdS(String orderItemIdS) {
        this.orderItemIdS = orderItemIdS;
    }

    public List<ItemSubstitutionData> getOrderItemSubstitutionList() {
        return orderItemSubstitutionList;
    }

    public void setOrderItemSubstitutionList(List<ItemSubstitutionData> orderItemSubstitutionList) {
        this.orderItemSubstitutionList = orderItemSubstitutionList;
    }

    public String getPoItemStatus() {
        return poItemStatus;
    }

    public void setPoItemStatus(String poItemStatus) {
        this.poItemStatus = poItemStatus;
    }

    public String getQtyReturnedString() {
        return qtyReturnedString;
    }

    public void setQtyReturnedString(String qtyReturnedString) {
        this.qtyReturnedString = qtyReturnedString;
    }

    public ReturnRequestDetailData getReturnRequestDetailData() {
        return returnRequestDetailData;
    }

    public void setReturnRequestDetailData(ReturnRequestDetailData returnRequestDetailData) {
        this.returnRequestDetailData = returnRequestDetailData;
    }

    public boolean getReSale() {
        return reSale;
    }

    public void setReSale(boolean reSale) {
        this.reSale = reSale;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }

    public String getStandardProductList() {
        return standardProductList;
    }

    public void setStandardProductList(String standardProductList) {
        this.standardProductList = standardProductList;
    }

    public String getTargetShipDateString() {
        return targetShipDateString;
    }

    public void setTargetShipDateString(String targetShipDateString) {
        this.targetShipDateString = targetShipDateString;
    }

    public boolean getTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    public int getNewOrderItemActionQty() {
        return newOrderItemActionQty;
    }

    public void setNewOrderItemActionQty(int newOrderItemActionQty) {
        this.newOrderItemActionQty = newOrderItemActionQty;
    }

    public String getNewOrderItemActionQtyS() {
        return newOrderItemActionQtyS;
    }

    public void setNewOrderItemActionQtyS(String newOrderItemActionQtyS) {
        this.newOrderItemActionQtyS = newOrderItemActionQtyS;
    }

    public String getOpenLineStatusCd() {
        return openLineStatusCd;
    }

    public void setOpenLineStatusCd(String openLineStatusCd) {
        this.openLineStatusCd = openLineStatusCd;
    }

    public OrderAddOnChargeData getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(OrderAddOnChargeData orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public OrderFreightData getOrderFreight() {
        return orderFreight;
    }

    public void setOrderFreight(OrderFreightData orderFreight) {
        this.orderFreight = orderFreight;
    }

    public OrderAddOnChargeData getOrderFuelSurcharge() {
        return orderFuelSurcharge;
    }

    public void setOrderFuelSurcharge(OrderAddOnChargeData orderFuelSurcharge) {
        this.orderFuelSurcharge = orderFuelSurcharge;
    }

    public OrderFreightData getOrderHandling() {
        return orderHandling;
    }

    public void setOrderHandling(OrderFreightData orderHandling) {
        this.orderHandling = orderHandling;
    }

    public List<OrderPropertyData> getOrderItemNotes() {
        return orderItemNotes;
    }

    public void setOrderItemNotes(List<OrderPropertyData> orderItemNotes) {
        this.orderItemNotes = orderItemNotes;
    }

    public OrderAddOnChargeData getOrderSmallOrderFee() {
        return orderSmallOrderFee;
    }

    public void setOrderSmallOrderFee(OrderAddOnChargeData orderSmallOrderFee) {
        this.orderSmallOrderFee = orderSmallOrderFee;
    }

    public PurchaseOrderData getPurchaseOrderData() {
        return purchaseOrderData;
    }

    public void setPurchaseOrderData(PurchaseOrderData purchaseOrderData) {
        this.purchaseOrderData = purchaseOrderData;
    }

    public Date getTargetShipDate() {
        return targetShipDate;
    }

    public void setTargetShipDate(Date targetShipDate) {
        this.targetShipDate = targetShipDate;
    }

    public InvoiceDistDetailData getWorkingInvoiceDistDetailData() {
        return workingInvoiceDistDetailData;
    }

    public void setWorkingInvoiceDistDetailData(InvoiceDistDetailData workingInvoiceDistDetailData) {
        this.workingInvoiceDistDetailData = workingInvoiceDistDetailData;
    }
    
}