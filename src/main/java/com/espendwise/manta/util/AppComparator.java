package com.espendwise.manta.util;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.OrderItemActionData;
import com.espendwise.manta.model.data.OrderPropertyData;
import com.espendwise.manta.model.data.StoreMessageDetailData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.data.UploadSkuData;
import com.espendwise.manta.model.entity.BudgetEntity;
import com.espendwise.manta.model.view.*;

import java.util.Comparator;
import java.util.Date;

public class AppComparator {
    public static final Comparator<BusEntityData> BUS_ENTITY_ID_COMPARATOR = new Comparator<BusEntityData>() {
        @Override
        public int compare(BusEntityData o1, BusEntityData o2) {
            return o1.getBusEntityId().compareTo(o2.getBusEntityId());
        }
    };

    public static final Comparator<CatalogData> CATALOG_ID_COMPARATOR = new Comparator<CatalogData>() {
        @Override
        public int compare(CatalogData o1, CatalogData o2) {
            return o1.getCatalogId().compareTo(o2.getCatalogId());
        }
    };

    public static final Comparator<AccountListView> ACCOUNT_LIST_VIEW_COMPARATOR = new Comparator<AccountListView>() {
        @Override
        public int compare(AccountListView o1, AccountListView o2) {
            return o1.getAccountId().compareTo(o2.getAccountId());
        }
    };
    
    public static final Comparator<AssetListView> ASSET_LIST_VIEW_COMPARATOR = new Comparator<AssetListView>() {
        @Override
        public int compare(AssetListView o1, AssetListView o2) {
            return o1.getAssetId().compareTo(o2.getAssetId());
        }
    };

    public static final Comparator<SiteListView> SITE_LIST_VIEW_COMPARATOR = new Comparator<SiteListView>() {
        @Override
        public int compare(SiteListView o1, SiteListView o2) {
            return o1.getSiteId().compareTo(o2.getSiteId());
        }
    };

    public static final Comparator<UserListView> USER_LIST_VIEW_COMPARATOR = new Comparator<UserListView>() {
        @Override
        public int compare(UserListView o1, UserListView o2) {
            return o1.getUserId().compareTo(o2.getUserId());
        }
    };

    public static final Comparator<CatalogListView> CATALOG_LIST_VIEW_COMPARATOR = new Comparator<CatalogListView>() {
        @Override
        public int compare(CatalogListView o1, CatalogListView o2) {
            return o1.getCatalogId().compareTo(o2.getCatalogId());
        }
    };
    public static final Comparator<DistributorListView> DISTRIBUTOR_LIST_VIEW_COMPARATOR = new Comparator<DistributorListView>() {
        @Override
        public int compare(DistributorListView o1, DistributorListView o2) {
            return o1.getDistributorId().compareTo(o2.getDistributorId());
        }
    };
    public static final Comparator<ProductListView> ITEM_LIST_VIEW_COMPARATOR = new Comparator<ProductListView>() {
        @Override
        public int compare(ProductListView o1, ProductListView o2) {
            return o1.getItemId().compareTo(o2.getItemId());
        }
    };
    
    public static final Comparator<ServiceListView> SERVICE_LIST_VIEW_COMPARATOR = new Comparator<ServiceListView>() {
        @Override
        public int compare(ServiceListView o1, ServiceListView o2) {
            return o1.getServiceId().compareTo(o2.getServiceId());
        }
    };

    public static final Comparator<InstanceView> INSTANCE_VIEW_COMPARATOR = new Comparator<InstanceView>() {
        @Override
        public int compare(InstanceView o1, InstanceView o2) {
            return o1.getPrimaryEntityId().compareTo(o2.getPrimaryEntityId());
        }
    };

    public static final Comparator<AllStoreIdentificationView> ALL_STORE_IDENTIFICATION_COMPARATOR = new Comparator<AllStoreIdentificationView>() {
        @Override
        public int compare(AllStoreIdentificationView o1, AllStoreIdentificationView o2) {
            return o1.getMainStoreId().compareTo(o2.getMainStoreId());
        }
    };

    public static final Comparator<? super Pair<String, String>> PAIR_OBJ1_STR_COMPARATOR = new Comparator<Pair<String, String>>() {

        @Override
        public int compare(Pair<String, String> o1, Pair<String, String> o2) {

            String v1 = o1 == null ? null : o1.getObject1().toLowerCase();
            String v2 = o2 == null ? null : o2.getObject1().toLowerCase();

            return v1 == null && v2 == null
                    ? 0
                    : v1 == null ? -1 : v2 == null ? 1 : v1.compareTo(v2);
        }
    };
    public static final Comparator<Pair<Long, String>> PAIR_OBJ2_STR_COMPARATOR = new Comparator<Pair<Long, String>>() {

        @Override
        public int compare(Pair<Long, String> o1, Pair<Long, String> o2) {

            String v1 = o1 == null ? null : o1.getObject2().toLowerCase();
            String v2 = o2 == null ? null : o2.getObject2().toLowerCase();

            return v1 == null && v2 == null
                    ? 0
                    : v1 == null ? -1 : v2 == null ? 1 : v1.compareTo(v2);
        }
    };


    public static final Comparator<Pair<Long, String>> PAIR_OBJ1_LONG_COMPARATOR = new Comparator<Pair<Long, String>>() {


        @Override
        public int compare(Pair<Long,  String> o1, Pair<Long,  String> o2) {

            Long v1 = o1 == null ? null : o1.getObject1();
            Long v2 = o2 == null ? null : o2.getObject1();

            return v1 == null && v2 == null
                    ? 0
                    : v1 == null ? -1 : v2 == null ? 1 : v1.compareTo(v2);
        }
    };

    public static final Comparator<BudgetEntity> BUDGET_ENTITY_ADD_DATE_COMPARE = new Comparator<BudgetEntity>() {
        public int compare(BudgetEntity o1, BudgetEntity o2) {
            Date date1 = o1.getBudget().getAddDate();
            Date date2 = o2.getBudget().getAddDate();
            return date1.compareTo(date2);
        }
    };


    public static final Comparator<SiteHierarchyTotalReportView  > SITE_HIERARCHY_COMPARE = new Comparator<SiteHierarchyTotalReportView>() {
        public int compare(SiteHierarchyTotalReportView  o1, SiteHierarchyTotalReportView  o2) {
            String i1 = o1.getSiteHierarchyLevel1Name()+o1.getSiteHierarchyLevel2Name()+o1.getSiteHierarchyLevel3Name()+o1.getSiteHierarchyLevel4Name();
            String i2 = o2.getSiteHierarchyLevel1Name()+o2.getSiteHierarchyLevel2Name()+o2.getSiteHierarchyLevel3Name()+o2.getSiteHierarchyLevel4Name();
            return i1.compareTo(i2);
        }
    };
    public static final Comparator<WorkflowRuleData> WORKFLOW_RULE_DATA_COMPARATOR = new Comparator<WorkflowRuleData>() {
        @Override
        public int compare(WorkflowRuleData o1, WorkflowRuleData o2) {
            return o1.getWorkflowRuleId().compareTo(o2.getWorkflowRuleId());
        }
    };

    public static final Comparator<StoreMessageDetailData> STORE_MESSAGE_DETAIL_DATA_COMPARATOR = new Comparator<StoreMessageDetailData>() {
        @Override
        public int compare(StoreMessageDetailData o1, StoreMessageDetailData o2) {
            return o1.getStoreMessageDetailId().compareTo(o2.getStoreMessageDetailId());
        }
    };
    
    public static final Comparator<GroupReportListView> GROUP_REPORT_LIST_VIEW_COMPARATOR = new Comparator<GroupReportListView>() {
        @Override
        public int compare(GroupReportListView o1, GroupReportListView o2) {
            return o1.getReportId().compareTo(o2.getReportId());
        }
    };
    
    public static final Comparator<GroupFunctionListView> GROUP_FUNCTION_LIST_VIEW_COMPARATOR = new Comparator<GroupFunctionListView>() {
        @Override
        public int compare(GroupFunctionListView o1, GroupFunctionListView o2) {
            return o1.getFunctionName().compareTo(o2.getFunctionName());
        }
    };
    
    public static final Comparator<GroupConfigListView> GROUP_CONFIG_LIST_VIEW_COMPARATOR = new Comparator<GroupConfigListView>() {
        @Override
        public int compare(GroupConfigListView o1, GroupConfigListView o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    
    public static final Comparator<OrderListView> ORDER_LIST_VIEW_COMPARATOR = new Comparator<OrderListView>() {
        @Override
        public int compare(OrderListView o1, OrderListView o2) {
            return o1.getOrderId().compareTo(o2.getOrderId());
        }
    };
    
    public static final Comparator<OrderItemActionData> ORDER_ITEM_ACTION_COMPARATOR_DESC = new Comparator<OrderItemActionData>() {
        @Override
        public int compare(OrderItemActionData o1, OrderItemActionData o2) {
            Date date1 = o1.getAddDate();
            Date date2 = o2.getAddDate();
            return date1.compareTo(date2) * -1;
        }
    };
    
    public static final Comparator<OrderItemActionData> ORDER_ITEM_ACTION_COMPARATOR_LAST_ACTION_DATE = new Comparator<OrderItemActionData>() {
        @Override
        public int compare(OrderItemActionData o1, OrderItemActionData o2) {
            Date date1 = o1.getActionDate();
            Date date2 = o2.getActionDate();
            return date1.compareTo(date2) * -1;
        }
    };
    
    public static final Comparator<OrderPropertyData> ORDER_PROPERTY_COMPARATOR_DATE_DESC = new Comparator<OrderPropertyData>() {
        @Override
        public int compare(OrderPropertyData o1, OrderPropertyData o2) {
            Date date1 = o1.getAddDate();
            Date date2 = o2.getAddDate();
            return date1.compareTo(date2) * -1;
        }
    };
    
    public static final Comparator<OrderPropertyData> ORDER_PROPERTY_COMPARATOR = new Comparator<OrderPropertyData>() {
        @Override
        public int compare(OrderPropertyData o1, OrderPropertyData o2) {
            Long id1 = o1.getOrderPropertyId();
            Long id2 = o2.getOrderPropertyId();
            return id1.compareTo(id2);
        }
    };
    
    public static final Comparator ORDER_ITEM_ACTION_ACK_ON_HOLD_COMPARATOR = new Comparator() {
        public int compare(Object o2, Object o1) { // note the reversal of 01 and o2, this will effectivly sort this as a reversed order
            Date d1 = ((OrderItemActionData)o2).getActionDate();//note reversal, sorts backwards
            Date d2 = ((OrderItemActionData)o1).getActionDate();//note reversal, sorts backwards

            if (d1 == null) {//note reversal, sorts backwards
                d1 = ((OrderItemActionData)o2).getAddDate();
            }
            if (d2 == null) {//note reversal, sorts backwards
                d2 = ((OrderItemActionData)o1).getAddDate();
            }
            if ((d1 == null && d2 == null) || d1.compareTo(d2) == 0) {
            	d1 = ((OrderItemActionData)o2).getActionTime();//note reversal, sorts backwards
                d2 = ((OrderItemActionData)o1).getActionTime();//note reversal, sorts backwards
                if ((d1 == null || d2 == null) || d1.compareTo(d2) == 0) { //note or as sometime time is null
                	d1 = ((OrderItemActionData)o2).getAddDate();//note reversal, sorts backwards
                    d2 = ((OrderItemActionData)o1).getAddDate();//note reversal, sorts backwards
                    if((d1 == null && d2 == null) || d1.compareTo(d2) == 0){
                        String s1 = ((OrderItemActionData)o2).getActionCd();//note reversal , sorts backwards
                        String s2 = ((OrderItemActionData)o1).getActionCd();//note reversal, sorts backwards
                        if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.ACK_ON_HOLD.equals(s1)) {
                            return 1;
                        }
                        if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.ACK_ON_HOLD.equals(s2)) {
                            return -1;
                        }
                        if (s1 == null) {
                            return 1;
                        }
                        if (s2 == null) {
                            return -1;
                        }
                        return s1.compareTo(s2);
                    } else {
                    	return d1.compareTo(d2);
                    }
                } else {
                    return d1.compareTo(d2);
                }
            } else {
            	return d1.compareTo(d2);
            }
        }
    };

    public static final Comparator<OrderItemIdentView> ORDER_ITEM_ERP_PO_NUM_COMPARATOR = new Comparator<OrderItemIdentView>() {
        @Override
        public int compare(OrderItemIdentView orderItem1, OrderItemIdentView orderItem2) {
            String orderItemErpPoNum1 = orderItem1.getOrderItem().getErpPoNum();
            String orderItemErpPoNum2 = orderItem2.getOrderItem().getErpPoNum();
            orderItemErpPoNum1 = (orderItemErpPoNum1 != null) ? orderItemErpPoNum1 : "" ;
            orderItemErpPoNum2 = (orderItemErpPoNum2 != null) ? orderItemErpPoNum2 : "" ;
            return orderItemErpPoNum1.compareTo(orderItemErpPoNum2);
        }
    };

    public static final Comparator<BatchOrderView> BATCH_ORDER_VIEW_COMPARATOR = new Comparator<BatchOrderView>() {
        @Override
        public int compare(BatchOrderView o1, BatchOrderView o2) {
            return o1.getEventId().compareTo(o2.getEventId());
        }
    };
    public static final Comparator<CatalogManagerView> CATALOG_VIEW_COMPARATOR = new Comparator<CatalogManagerView>() {
        @Override
        public int compare(CatalogManagerView o1, CatalogManagerView o2) {
            return o1.getEventId().compareTo(o2.getEventId());
        }
    };   
    public static final Comparator<ItemContractCostView> ITEM_CONTRACT_COST_VIEW_COMPARATOR = new Comparator<ItemContractCostView>() {
        @Override
        public int compare(ItemContractCostView o1, ItemContractCostView o2) {
            return o1.getDistId().compareTo(o2.getDistId());
        }
    };

    public static final Comparator<UploadSkuData> UPLOAD_SKU_DATA_SKU_NUM_COMPARATOR = new Comparator<UploadSkuData>() {
        @Override
        public int compare(UploadSkuData o1, UploadSkuData o2) {
            return o1.getSkuNum().compareTo(o2.getSkuNum());
        }
    };

    public static final Comparator<UploadSkuView> UPLOAD_SKU_VIEW_SKU_NUM_COMPARATOR = new Comparator<UploadSkuView>() {
        @Override
        public int compare(UploadSkuView o1, UploadSkuView o2) {
            return o1.getSkuNum().compareTo(o2.getSkuNum());
        }
    };

    public static final Comparator<UploadSkuView> UPLOAD_SKU_VIEW_SKU_DATA_COMPARATOR = new Comparator<UploadSkuView>() {
        @Override
        public int compare(UploadSkuView o1, UploadSkuView o2) {
            String skuNum1 = (o1.getUploadSkuData() != null ? o1.getUploadSkuData().getSkuNum() : "");
            String skuNum2 = (o2.getUploadSkuData() != null ? o2.getUploadSkuData().getSkuNum() : "");
            return skuNum1.compareTo(skuNum2);
        }
    };


    public static final Comparator<OrderGuideItemView> ORDER_GUIDE_ITEMS_COMPARATOR = new Comparator<OrderGuideItemView>() {
        @Override
        public int compare(OrderGuideItemView o1, OrderGuideItemView o2) {
            return o1.getProductName().compareTo(o2.getProductName());
        }
    };

}
