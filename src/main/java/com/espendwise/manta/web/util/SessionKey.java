package com.espendwise.manta.web.util;

/**
 * holder for name attributes of model !!!
 */

public class SessionKey {

    public static final String INSTANCE  = "instance";

    //storemessage
    public static final String STORE_MESSAGE_FILTER_RESULT = "storeMessageFilterResult";
    public static final String STORE_MESSAGE_FILTER = "storeMessageFilter";
    public static final String STORE_MESSAGE  = "storeMessage";
    public static final String STORE_MESSAGE_CONFIGURATION  = "storeMessageConfiguration";
    public static final String STORE_MESSAGE_CONFIGURATION_FILTER  = "storeMessageConfigurationFilter";
    public static final String STORE_MESSAGE_HEADER = "storeMessageHeader";

    //account
    public static final String ACCOUNT_FILTER = "accountFilter";
    public static final String ACCOUNT_FILTER_RESULT = "accountFilterResult";
    public static final String ACCOUNT = "account";
    public static final String ACCOUNT_HEADER = "accountHeader";
    public static final String ACCOUNT_FISCAL_CALENDAR="accountFiscalCalendar";
    public static final String ACCOUNT_CONTENT_MANAGEMENT="accountContentManagement";
    public static final String WORKFLOW_HEADER = "workflowHeader";
    public static final String ACCOUNT_WORKFLOW="accountWorkflow";
    public static final String ACCOUNT_WORKFLOW_DETAIL = "accountWorkflowDetail";
    public static final String ACCOUNT_WORKFLOW_RULE_FILTER_RESULT = "accountWorkflowRuleFilterResult";
    public static final String ACCOUNT_WORKFLOW_SITE_FILTER = "accountWorkflowSiteFilter";
    public static final String ACCOUNT_WORKFLOW_SITE_FILTER_RESULT = "accountWorkflowSiteFilterResult";
    public static final String ACCOUNT_WORKFLOW_RULE_TYPE_FILTER = "accountWorkflowRuleTypeFilter";
    public static final String ACCOUNT_WORKFLOW_RULE_DETAIL = "accountWorkflowRuleDetail";
    public static final String ACCOUNT_SHOPPING_CONTROL = "accountShoppingControl";
    public static final String ACCOUNT_SHOPPING_CONTROL_FILTER = "accountShoppingControlFilter";
    public static final String ACCOUNT_SHOPPING_CONTROL_FILTER_RESULT = "accountShoppingControlFilterResult";
    public static final String ACCOUNT_PROPERTIES = "accountProperties";

    //site
    public static final String SITE_FILTER = "siteFilter";
    public static final String SITE_FILTER_RESULT = "siteFilterResult";
    public static final String SITE = "site";
    public static final String SITE_BUDGET = "siteBudget";
    public static final String SITE_USER_FILTER = "siteUserFilter";
    public static final String SITE_USER_FILTER_RESULT = "siteUserFilterResult";
    public static final String SITE_CATALOG_FILTER = "siteCatalogFilter";
    public static final String SITE_CATALOG_FILTER_RESULT = "siteCatalogFilterResult";
    public static final String SITE_HEADER = "siteHeader";
    public static final String SITE_SITE_HIERARCHY = "siteSiteHierarchy";
    public static final String SITE_WORKFLOW = "siteWorkflow";

    public static final String SITE_ORDER_GUIDE = "siteOrderGuide";
    public static final String SITE_ORDER_GUIDE_DETAIL = "siteOrderGuideDetail";
    public static final String SITE_ORDER_GUIDE_ITEMS_FILTER_RESULT = "siteOrderGuideItemsFilterResult";
    public static final String ORDER_GUIDE_HEADER = "orderGuideHeader" ;

    //emailTemplate
    public static final String EMAIL_TEMPALTE_FILTER = "emailTemplateFilter";
    public static final String EMAIL_TEMPALTE_FILTER_RESULT = "emailTemplateFilterResult";
    public static final String EMAIL_TEMPLATE_HEADER = "emailTemplateHeader";
    public static final String EMAIL_TEMPLATE = "emailTemplate";

    //user
    public static final String USER_FILTER = "userFilter";
    public static final String USER_FILTER_RESULT = "userFilterResult";
    public static final String USER = "user";
    public static final String USER_HEADER = "userHeader";
    public static final String USER_ACCOUNT_FILTER = "userAccountFilter";
    public static final String USER_ACCOUNT_FILTER_RESULT = "userAccountFilterResult";
    public static final String USER_GROUP_FILTER = "userGroupFilter";
    public static final String USER_GROUP_FILTER_RESULT = "userGroupFilterResult";
    public static final String USER_LOCATION_FILTER = "userLocationFilter";
    public static final String USER_LOCATION_FILTER_RESULT = "userLocationFilterResult";
    public static final String USER_NOTIFICATION = "userNotification";
    public static final String ACCOUNT_SITE_HIERARCHY = "accountSiteHierarchy";
    public static final String ACCOUNT_SITE_HIERARCHY_MANAGE = "accountSiteHierarchyManage";
    public static final String ACCOUNT_SITE_HIERARCHY_MANAGE_FILTER = "accountSiteHierarchyManageFilter";
    public static final String ACCOUNT_SITE_HIERARCHY_CONFIGRATION = "accountSiteHierarchyConfiguration";
    public static final String USER_LOADER = "userLoader";

    //manufacturer
    public static final String MANUFACTURER_FILTER = "manufacturerFilter";
    public static final String MANUFACTURER_FILTER_RESULT = "manufacturerFilterResult";
    public static final String MANUFACTURER = "manufacturer";
    public static final String MANUFACTURER_HEADER = "manufacturerHeader";

    //distributor
    public static final String DISTRIBUTOR_FILTER = "distributorFilter";
    public static final String DISTRIBUTOR_FILTER_RESULT = "distributorFilterResult";
    public static final String DISTRIBUTOR = "distributor";
    public static final String DISTRIBUTOR_HEADER = "distributorHeader";
    public static final String DISTRIBUTOR_CONFIGURATION="distributorConfiguration";
    
    //locates
    public static final String LOCATE_ACCOUNT_FILTER = "locateAccountFilter";
    public static final String LOCATE_ACCOUNT_FILTER_RESULT = "locateAccountFilterResult";
    public static final String LOCATE_USER_FILTER = "locateUserFilter";
    public static final String LOCATE_USER_FILTER_RESULT = "locateUserFilterResult";
    public static final String LOCATE_CATALOG_FILTER = "locateCatalogFilter";
    public static final String LOCATE_CATALOG_FILTER_RESULT = "locateCatalogFilterResult";
    public static final String LOCATE_DISTRIBUTOR_FILTER = "locateDistributorFilter";
    public static final String LOCATE_DISTRIBUTOR_FILTER_RESULT = "locateDistributorFilterResult";
    public static final String LOCATE_ITEM_FILTER = "locateItemFilter";
    public static final String LOCATE_ITEM_FILTER_RESULT = "locateItemFilterResult";
    public static final String LOCATE_ITEM_COST_FILTER = "locateItemCostFilter";
    public static final String LOCATE_ITEM_COST_FILTER_RESULT = "locateItemCostFilterResult";
    public static final String LOCATE_ITEM_ORDER_GUIDE_FILTER = "locateItemOrderGuideFilter";
    public static final String LOCATE_ITEM_ORDER_GUIDE_FILTER_RESULT = "locateItemOrderGuideFilterResult";
    public static final String LOCATE_SERVICE_FILTER_RESULT = "locateServiceFilterResult";
    public static final String LOCATE_SITE_FILTER = "locateSiteFilter";
    public static final String LOCATE_SITE_FILTER_RESULT = "locateSiteFilterResult";
    public static final String LOCATE_ASSET_FILTER = "locateAssetFilter";
    public static final String LOCATE_ASSET_FILTER_RESULT = "locateAssetFilterResult";

    public static final String WEB_MESSAGES = "webErrors";

    //cms
    public static final String CMS = "cms";
    public static final String CMS_HEADER = "cmsHeader";

    //costCenter
    public static final String COST_CENTER_FILTER = "costCenterFilter";
    public static final String COST_CENTER_FILTER_RESULT = "costCenterFilterResult";
    public static final String COST_CENTER = "costCenter";
    public static final String COST_CENTER_HEADER = "costCenterHeader";
    public static final String COST_CENTER_CATALOG_FILTER = "costCenterCatalogFilter";
    public static final String COST_CENTER_CATALOG_FILTER_RESULT = "costCenterCatalogFilterResult";
    
    // group
    public static final String GROUP_FILTER = "groupFilter";
    public static final String GROUP_FILTER_RESULT = "groupFilterResult";
    public static final String GROUP = "group";
    public static final String GROUP_HEADER = "groupHeader";
    public static final String GROUP_CONFIGURATION_FILTER = "groupConfiguration";
    public static final String GROUP_CONFIGURATION_ALL_FILTER = "groupConfigurationAll";
    public static final String GROUP_CONFIGURATION_FILTER_RESULT = "groupConfigurationResult";
    public static final String GROUP_REPORT_FILTER = "groupReportFilter";
    public static final String GROUP_REPORT_FILTER_RESULT = "groupReportFilterResult";
    public static final String GROUP_FUNCTION_FILTER = "groupFunctionFilter";
    public static final String GROUP_FUNCTION_FILTER_RESULT = "groupFunctionFilterResult";
    

    //history
    public static final String HISTORY_FILTER = "historyFilter";
    public static final String HISTORY_FILTER_RESULT = "historyFilterResult";
    
    //passwordManagement
    public static final String PROFILE_PASSWORD_MANAGEMENT = "profilePasswordManagement";
    public static final String PROFILE_HEADER = "profileHeader";
    
    //order
    public static final String ORDER_FILTER = "orderFilter";
    public static final String ORDER_FILTER_RESULT = "orderFilterResult";
    public static final String ORDER = "order";
    public static final String ORDER_HEADER = "orderHeader";
    public static final String ORDER_ITEM_NOTE_FILTER = "orderItemNoteFilter";
    public static final String ORDER_PRINT_TEMP_PO = "orderPrintTempPo";

    //masterItem
    public static final String MASTER_ITEM_FILTER = "masterItemFilter";
    public static final String MASTER_ITEM_FILTER_RESULT = "masterItemFilterResult";
    public static final String MASTER_ITEM = "masterItem";
    public static final String MASTER_ITEM_HEADER = "masterItemHeader";

    //corporateSchedule
    public static final String CORPORATE_SCHEDULE_FILTER_RESULT = "corporateScheduleFilterResult";
    public static final String CORPORATE_SCHEDULE_FILTER = "corporateScheduleFilter";
    public static final String CORPORATE_SCHEDULE = "corporateSchedule";
    public static final String CORPORATE_SCHEDULE_HEADER = "corporateScheduleHeader";
    public static final String CORPORATE_SCHEDULE_ACCOUNT_FILTER = "corporateScheduleAccountFilter";
    public static final String CORPORATE_SCHEDULE_ACCOUNT_FILTER_RESULT = "corporateScheduleAccountFilterResult";
    public static final String CORPORATE_SCHEDULE_LOCATION_FILTER = "corporateScheduleLocationFilter";
    public static final String CORPORATE_SCHEDULE_LOCATION_FILTER_RESULT = "corporateScheduleLocationFilterResult";

    // upload file
    public static final String UPLOAD_FILE_FILTER = "uploadFileFilter";
    public static final String UPLOAD_FILE_FILTER_RESULT = "uploadFileFilterResult";
    public static final String UPLOAD_FILE = "uploadFile";
    public static final String UPLOAD_FILE_HEADER = "uploadFileHeader";

    
    //batchOrder
    public static final String BATCH_ORDER_LOADER = "batchOrderLoader";
    public static final String BATCH_ORDER_LOADER_RESULT = "batchOrderLoaderResult";
        
    //catalogManager
    public static final String CATALOG_LOADER = "catalogLoader";
    public static final String CATALOG_LOADER_RESULT = "catalogLoaderResult";
    
}
