package com.espendwise.manta.util;

import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.TemplatePropertyData;
import com.espendwise.manta.spi.ICommonProperty;
import org.apache.log4j.Logger;

import java.util.*;

public class PropertyUtil {

    private static final Logger logger = Logger.getLogger(PropertyUtil.class);

    public static String toValue(PropertyData property) {
        return property != null ? property.getValue() : null;
    }

    public static String toFirstValueNN(List<PropertyData> properties) {
        return Utility.strNN(toFirstValue(properties));
    }

    public static String toFirstValue(List<PropertyData> properties) {
        return Utility.isSet(properties) ? toValue(properties.get(0)) : null;
    }

    public static String toValue(TemplatePropertyData property) {
        return property != null ? property.getValue() : null;
    }

    public static String toValueNN(TemplatePropertyData property) {
        return Utility.strNN(toValue(property));
    }

    public static String toValueNN(List properties, String typeCd) {

        if (properties != null) {
            for (Object o : properties) {
                ICommonProperty commonProperty = toCommonProperty(o);
                if (commonProperty != null) {
                    if (Utility.isTrue(containsIn(commonProperty, typeCd))) {
                        return Utility.strNN(commonProperty.getValue());
                    }
                }
            }
        }

        return Constants.EMPTY;

    }

    public static Object find(List properties, String typeCd) {

        if (properties != null) {
            for (Object o : properties) {
                ICommonProperty commonProperty = toCommonProperty(o);
                if (commonProperty != null) {
                    if (Utility.isTrue(containsIn(commonProperty, typeCd))) {
                        return o;
                    }
                }
            }
        }

        return null;

    }

    public static PropertyData findP(List<PropertyData> properties, PropertyTypeCode typeCd) {

        if (properties != null) {
            for (PropertyData o : properties) {
                if (o != null) {
                    if (o.getPropertyTypeCd().equalsIgnoreCase(typeCd.getTypeCode())) {
                        return o;
                    }
                }
            }
        }

        return null;

    }

    public static PropertyData findP(List<PropertyData> properties, String typeCd) {

        if (properties != null) {
            for (PropertyData o : properties) {
                if (o != null) {
                    if (o.getPropertyTypeCd().equalsIgnoreCase(typeCd)) {
                        return o;
                    }
                }
            }
        }

        return null;

    }


    public static PropertyData findP(List<PropertyData> properties, PropertyExtraCode extraCode) {

        if (properties != null) {
            for (PropertyData o : properties) {
                if (o != null) {
                    if (extraCode.getNameExtraCode().equalsIgnoreCase(o.getShortDesc())) {
                        return o;
                    }
                }
            }
        }

        return null;

    }
    
    public static List<PropertyData> findActiveP(List<PropertyData> properties) {
        List<PropertyData> active = new ArrayList<PropertyData>();
        if (properties != null) {
            for (PropertyData o : properties) {
                if (RefCodeNames.PROPERTY_STATUS_CD.ACTIVE.equals(o.getPropertyStatusCd())) {
                    active.add(o);
                }
            }
        }

        return active;
    }
    
    private static Boolean containsIn(ICommonProperty commonProperty, String typeCd, String shortDesc, String status) {
        return containsIn(commonProperty, typeCd, shortDesc, status, null);
    }

    private static Boolean containsIn(ICommonProperty commonProperty, String typeCd, String shortDesc) {
        return containsIn(commonProperty, typeCd, shortDesc, null);
    }

    private static Boolean containsIn(ICommonProperty commonProperty, String typeCd) {
        return containsIn(commonProperty, typeCd, null, null, null);
    }

    private static Boolean containsIn(ICommonProperty commonProperty,
                                      String typeCd,
                                      String shortDesc,
                                      String status,
                                      String locale) {

        if (commonProperty == null || (typeCd == null && shortDesc == null && status == null && locale == null)) {
            return null;
        }

        boolean b = true;

        if (typeCd != null) {
            b = typeCd.equalsIgnoreCase(commonProperty.getTypeCd()) ;
        }

        if (b && shortDesc != null) {
            b = shortDesc.equalsIgnoreCase(commonProperty.getShortDesc());
        }

        if (b && status != null) {
            b = status.equalsIgnoreCase(commonProperty.getStatus());
        }

        if (b && locale != null) {
            b = locale.equalsIgnoreCase(commonProperty.getLocale());
        }

        return b;
    }

    private static ICommonProperty toCommonProperty(Object o) {

        if (o instanceof PropertyData) {

            PropertyData property = (PropertyData) o;

            return new CommonProperty(property.getPropertyTypeCd(),
                    property.getShortDesc(),
                    property.getValue(),
                    property.getPropertyStatusCd(),
                    property.getLocaleCd()

            );

        } else if (o instanceof TemplatePropertyData) {

            TemplatePropertyData property = (TemplatePropertyData) o;

            return new CommonProperty(property.getTemplatePropertyCd(),
                    null,
                    property.getValue(),
                    null,
                    null
            );
        }

        return null;
    }

    public static String toValueNN(PropertyData property) {
        return Utility.strNN(toValue(property));
    }

    public static PropertyData toProperty(Long busEntityId, String shortDesc, String value) {
        return createProperty(busEntityId,
                null,
                shortDesc,
                RefCodeNames.PROPERTY_TYPE_CD.EXTRA,
                value,
                RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                null
        );
    }
    
    public static PropertyData toProperty(Long busEntityId, PropertyTypeCode propertyTypeCode, String value) {
        return createProperty(busEntityId,
                null,
                propertyTypeCode.getTypeCode(),
                propertyTypeCode.getTypeCode(),
                value,
                RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                null
        );
    }

    public static PropertyData toProperty(Long busEntityId, PropertyExtraCode extraCode, String value) {
        return createProperty(busEntityId,
                null,
                extraCode.getNameExtraCode(),
                RefCodeNames.PROPERTY_TYPE_CD.EXTRA,
                value,
                RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                null
        );
    }
    public static TemplatePropertyData toTemplateProperty(Long templateId, String propertyTypeCode, String value) {
        return createTemplateProperty(templateId,
                propertyTypeCode,
                value
        );
    }


    public static PropertyData createProperty(Long busEntityId,
                                              Long userId,
                                              String shortDesc,
                                              String propertyTypeCode,
                                              String value,
                                              String statusCd,
                                              String localeCd) {


        return createProperty(busEntityId,
                userId,
                null,
                shortDesc,
                propertyTypeCode,
                value,
                statusCd,
                localeCd
        );

    }

    public static PropertyData createProperty(Long busEntityId,
                                              Long userId,
                                              Long orginalUserId,
                                              String shortDesc,
                                              String propertyTypeCode,
                                              String value,
                                              String statusCd,
                                              String localeCd) {

        PropertyData propertyData = new PropertyData();

        propertyData.setShortDesc(shortDesc);
        propertyData.setPropertyTypeCd(propertyTypeCode);
        propertyData.setOriginalUserId(orginalUserId);
        propertyData.setPropertyStatusCd(statusCd);
        propertyData.setValue(value);
        propertyData.setBusEntityId(busEntityId);
        propertyData.setUserId(userId);
        propertyData.setLocaleCd(localeCd);

        return propertyData;

    }

    public static TemplatePropertyData createTemplateProperty(Long templateId, String propertyTypeCode,  String value) {

        TemplatePropertyData  propertyData = new TemplatePropertyData();

        propertyData.setTemplateId(templateId);
        propertyData.setTemplatePropertyCd(propertyTypeCode);
        propertyData.setValue(value);

        return propertyData;

    }
    
    public static Map<Long, List<PropertyData>> toBusEntityPropMap(Collection<PropertyData> pList) {

        if (pList == null) {
            return null;
        }

        Map<Long, List<PropertyData>> map = new HashMap<Long, List<PropertyData>>();

        for (PropertyData o : pList) {
            Long key = (Long) o.getBusEntityId();
            List<PropertyData> list = map.get(key);
            if (list == null) {
                list = new ArrayList<PropertyData>();
                map.put(key, list);
            }
            list.add(o);
        }

        return map;

    }

    public static List<PropertyData> joins(List<PropertyData> properties1, List<PropertyData> properties2) {

        List<PropertyData> x = new ArrayList<PropertyData>();

        if (properties1 != null || properties2 != null) {

            if (properties1 != null && Utility.isSet(properties1)) {
                x.addAll(properties1);
            }

            if (properties2 != null && Utility.isSet(properties2)) {
               x.addAll(properties2);
            }

        }

        return x;
    }

    public static CommonLinkedProperty createCommonLinkedProperty(String typeCd, String value) {

        return new CommonLinkedProperty(
                typeCd,
                null,
                value,
                null,
                null
        );

    }
    public static List<PropertyData> toDefaultProperties(Long busEntityId) {
    	List<PropertyData> properties = new ArrayList<PropertyData>();
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.ALLOWED_RUNTIME_ORD_ITM_ACT, "System Accepted"));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.ALLOWED_RUNTIME_ORD_ITM_ACT, "Canceled"));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.ALLOWED_RUNTIME_ORD_ITM_ACT, "Dist Shipped"));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.ALLOWED_RUNTIME_ORD_ITM_ACT, "Dist Invoiced"));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.ALLOW_CUSTOMER_PO_NUMBER, "true"));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.AUTHORIZED_FOR_RESALE, "false"));   	
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.COMMENTS, ""));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.CRC_SHOP, "false"));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.CREDIT_LIMIT, "0"));       
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.CREDIT_RATING, ""));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.CUSTOMER_SYSTEM_APPROVAL_CD, "None"));
    	properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.DISTR_PO_TYPE, "SYSTEM"));
    	
    	properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.INVENTORY_LEDGER_SWITCH , "ON"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SCHEDULE_CUTOFF_DAYS, "0"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.INVENTORY_OG_LIST_UI , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.INVENTORY_MISSING_NOTIFICATION , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.INVENTORY_CHECK_PLACED_ORDER , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.CONNECTION_CUSTOMER, "OFF"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.CART_REMINDER_INTERVAL, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOW_SCHED_DELIVERY, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_ORDER_CONSOLIDATION , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOW_DIST_SKU_NUM , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOW_DIST_DELIVERY_DATE, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.RUSH_ORDER_CHARGE, "0"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.AUTO_ORDER_FACTOR , "0.500001"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.DIST_INVENTORY_DISPLAY , "Do not show"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOW_SPL, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ADD_SERVICE_FEE , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.HOLD_PO , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_USER_CHANGE_PASSWORD, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ACCOUNT_FOLDER , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ACCOUNT_FOLDER_NEW, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ADJUST_QTY_BY_855 , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.CREATE_ORDER_BY_855, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.CREATE_ORDER_ITEMS_BY_855, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.MODIFY_CUST_PO_NUM_BY_855, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SUPPORTS_BUDGET , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_MODERN_SHOPPING, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.NOT_APP_TO_BUDGET_FOR_BT_ORDER, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SEND_ORDER_CONFRM_FOR_BT_ORDER , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_SITE_LLC, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOW_EXPRESS_ORDER, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_ORDER_INV_ITEMS, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_REORDER , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.USE_PHYSICAL_INVENTORY, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOW_INV_CART_TOTAL , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOW_MY_SHOPPING_LISTS, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOW_REBILL_ORDER, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_SET_WORKORDER_PO_NUMBER, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.USER_ASSIGNED_ASSET_NUMBER, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_BUY_WORK_ORDER_PARTS , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.CONTACT_INFORMATION_TYPE, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.WORK_ORDER_PO_NUM_REQUIRED, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHOP_UI_TYPE, "B2C"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.FAQ_LINK , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.REMINDER_EMAIL_SUBJECT, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.REMINDER_EMAIL_MSG , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.NOTIFY_ORDER_EMAIL_GENERATOR, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.PENDING_APPROV_EMAIL_GENERATOR , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.REJECT_ORDER_EMAIL_GENERATOR, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.CONFIRM_ORDER_EMAIL_GENERATOR, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ORDER_CONFIRMATION_EMAIL_TEMPLATE, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.SHIPPING_NOTIFICATION_EMAIL_TEMPLATE, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.PENDING_APPROVAL_EMAIL_TEMPLATE, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.REJECTED_ORDER_EMAIL_TEMPLATE , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.MODIFIED_ORDER_EMAIL_TEMPLATE , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.CONTACT_US_CC_ADD, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.PDF_ORDER_CLASS, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.PDF_ORDER_STATUS_CLASS, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_CC_PAYMENT, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.ALLOW_ALTERNATE_SHIP_TO_ADDRESS, "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyExtraCode.INVENTORY_PO_SUFFIX, ""));
        
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.FREIGHT_CHARGE_TYPE , "CC"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.GL_TRANSFORMATION_TYPE, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.MAKE_SHIP_TO_BILL_TO , "false"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.ORDER_MINIMUM , "0"));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.ORDER_GUIDE_NOTE , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.PURCHASE_ORDER_ACCOUNT_NAME , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.RESALE_ACCOUNT_ERP_NUM, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.SKU_TAG, ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.TARGET_MARGIN , ""));
        properties.add(toProperty(busEntityId, AccountIdentPropertyTypeCode.TAXABLE_INDICATOR , "false"));
    	
    	return properties;
    }

}
