package com.espendwise.manta.util;



import com.espendwise.manta.model.data.CountryPropertyData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.data.OrderItemData;
import com.espendwise.manta.model.data.OrderMetaData;
import com.espendwise.manta.model.view.DistOptionsForShippingView;
import com.espendwise.manta.model.view.FreightHandlerView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <code>OrderUtil</code> is a order and orderItem utility class with helper methods
 */
public class OrderUtil {
    private static ArrayList<String> goodOrderItemStatusCodes = new ArrayList<String>();
    
    static {
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.INVOICED);
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.PENDING_ERP_PO);
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.PENDING_FULFILLMENT);
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.PENDING_FULFILLMENT_PROCESSING);
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.SENT_TO_DISTRIBUTOR);
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.SENT_TO_DISTRIBUTOR_FAILED);
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.PO_ACK_SUCCESS);
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.PO_ACK_ERROR);
        goodOrderItemStatusCodes.add(RefCodeNames.ORDER_ITEM_STATUS_CD.PO_ACK_REJECT);
        goodOrderItemStatusCodes.add(null);
    }

    public static boolean isGoodOrderItemStatus(String orderItemStatusCd) {
        if (orderItemStatusCd == null) {
            return true;
        }
        return goodOrderItemStatusCodes.contains(orderItemStatusCd);
    }
    
    public static boolean isSimpleServiceOrder(List<ItemData> items) {
        boolean isService = false;
        boolean isOther = false;
        if (Utility.isSet(items)) {
            for (ItemData item : items) {
                if (RefCodeNames.ITEM_TYPE_CD.SERVICE.equals(item.getItemTypeCd())) {
                    isService = true;
                } else {
                    isOther = true;
                }
                if (isService == isOther) return false;
            }
        }
        return isService;
    }
    
    public static FreightHandlerView getShipVia(List<FreightHandlerView> freightHandlers, Long freightHandlerId) {
        FreightHandlerView shipVia = null;
        
	if (Utility.isSet(freightHandlers)) {
            for (FreightHandlerView freightHandler : freightHandlers) {
                if (freightHandlerId == freightHandler.getBusEntityData().getBusEntityId()) {
                    shipVia = freightHandler;
                    break;
                }
            }
        }

	return shipVia;
    }

    public static String getShipViaName(List<FreightHandlerView> freightHandlers, Long freightHandlerId) {
	String shipViaName = "";
        
	FreightHandlerView shipVia = getShipVia(freightHandlers, freightHandlerId);
        if (shipVia != null) {
            shipViaName = shipVia.getBusEntityData().getShortDesc();
        }
	
        return shipViaName;
    }
    
    public static boolean getAllowShipingModifications(String orderStatus) {
        
        return (RefCodeNames.ORDER_STATUS_CD.PENDING_ORDER_REVIEW.equals(orderStatus) ||
                RefCodeNames.ORDER_STATUS_CD.PENDING_REVIEW.equals(orderStatus));
    }
    
    public static Collection<String> getDistERPNumsInItems(List<OrderItemData> orderItems) {
	Set<String> distErpNums = new HashSet<String>();
        if (Utility.isSet(orderItems)) {
            for (OrderItemData orderItem : orderItems) {
                String erpNum = orderItem.getDistErpNum();
                if (Utility.isSet(erpNum)) {
                    distErpNums.add(erpNum);
                }
            }
        }
	return distErpNums;
    }
    
    public static boolean isOrderStatusValid( String orderStatus, boolean fullControl ) {

        if (null == orderStatus) {
            return false;
        }
        if (RefCodeNames.ORDER_STATUS_CD.PENDING_DATE.equals(orderStatus)  ||
            RefCodeNames.ORDER_STATUS_CD.PENDING_CONSOLIDATION.equals(orderStatus)  ||
            RefCodeNames.ORDER_STATUS_CD.PENDING_APPROVAL.equals(orderStatus)  ||
            RefCodeNames.ORDER_STATUS_CD.PENDING_ORDER_REVIEW.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.ERP_REJECTED.equals(orderStatus)
            ) {
            return true;
        }
        if (fullControl && (
            RefCodeNames.ORDER_STATUS_CD.CANCELLED.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.DUPLICATED.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.PENDING_REVIEW.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.INVOICED.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.ERP_RELEASED_PO_ERROR.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.ERP_CANCELLED.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.ERP_REJECTED.equals(orderStatus))) {
            return true;
        }
        return false;
    }
    
    public static OrderMetaData getMetaObject(List<OrderMetaData> metaList, String name, Date orderModDate) {
        OrderMetaData meta = null;
        
        if (Utility.isSet(metaList) && Utility.isSet(name)) {
            Date modDate = null;
            for (OrderMetaData metaObj : metaList) {
                String objName = metaObj.getName();
                if (name.equals(objName)) {
                    if (modDate == null) {
                        modDate = orderModDate;
                        meta = metaObj;
                    } else {
                        Date mD = orderModDate;
                        if (mD == null) continue;
                        if (modDate.before(mD)) {
                            modDate = mD;
                            meta = metaObj;
                        }
                    }
                }
            }
        }
        return meta;
    }

    public static DistOptionsForShippingView getDistShipOption(List<DistOptionsForShippingView> distShipOptions, String distErpNum) {
        DistOptionsForShippingView foundDistShipOption = null;
	if (Utility.isSet(distShipOptions) && Utility.isSet(distErpNum)) {
            for (DistOptionsForShippingView distShipOption : distShipOptions) {
                if (distErpNum.equals(distShipOption.getDistributor().getErpNum())) {
                    foundDistShipOption = distShipOption;
                    break;
                }
            }
        }
        return foundDistShipOption;
    }
    
    public static boolean isStateProvinceRequired(List<CountryPropertyData> countryProperties) {
        String strValue = null;
        if (Utility.isSet(countryProperties)) {
            for (CountryPropertyData countryProperty : countryProperties) {
                if (RefCodeNames.COUNTRY_PROPERTY.USES_STATE.equals(countryProperty.getCountryPropertyCd())) {
                    strValue = countryProperty.getValue();
                    break;
                }
            }
        }

        return Utility.isTrue(strValue);
    }
}
