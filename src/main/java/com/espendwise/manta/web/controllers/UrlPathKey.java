package com.espendwise.manta.web.controllers;


public interface UrlPathKey {

    public static final String INSTANCE_PREFIX = "/instance";
    public static final String INSTANCE = "/instance/{globalStoreId}";

    public static final String DISPLAY_CONTROL = "/display";

    public interface STORE_MESSAGE {
        public static final String FILTER          = UrlPathKey.INSTANCE + "/storeMessage";
        public static final String IDENTIFICATION  = UrlPathKey.INSTANCE + "/storeMessage/{storeMessageId}";
        public static final String CONFIGURATION   = UrlPathKey.INSTANCE + "/storeMessage/{storeMessageId}/configuration";
    }

    public interface ACCOUNT {
        public static final String FILTER                          = UrlPathKey.INSTANCE + "/account";
        public static final String IDENTIFICATION                  = UrlPathKey.INSTANCE + "/account/{accountId}";
        public static final String FISCAL_CALENDAR                 = UrlPathKey.INSTANCE + "/account/{accountId}/fiscalCalendar";
        public static final String SITE_HIERARCHY                  = UrlPathKey.INSTANCE + "/account/{accountId}/locationHierarchy";
        public static final String SITE_HIERARCHY_LEVEL_1          = UrlPathKey.INSTANCE + "/account/{accountId}/locationHierarchy/{level1Id}";
        public static final String SITE_HIERARCHY_LEVEL_2          = UrlPathKey.INSTANCE + "/account/{accountId}/locationHierarchy/{level1Id}/{level2Id}";
        public static final String SITE_HIERARCHY_LEVEL_3          = UrlPathKey.INSTANCE + "/account/{accountId}/locationHierarchy/{level1Id}/{level2Id}/{level3Id}";
         public static final String SITE_HIERARCHY_LEVEL_4          = UrlPathKey.INSTANCE + "/account/{accountId}/locationHierarchy/{level1Id}/{level2Id}/{level3Id}/{level4Id}";
        public static final String SITE_HIERARCHY_CONFIGURATION    = UrlPathKey.INSTANCE + "/account/{accountId}/locationHierarchy/configuration/{siteHierarchyId}";
        public static final String CONTENT_MANAGEMENT              = UrlPathKey.INSTANCE + "/account/{accountId}/contentManagement";
        public static final String WORKFLOW                        = UrlPathKey.INSTANCE + "/account/{accountId}/workflow";
        public static final String WORKFLOW_DETAIL                 = UrlPathKey.INSTANCE + "/account/{accountId}/workflow/{workflowId}/{workflowType}";
        public static final String WORKFLOW_SITES                  = UrlPathKey.INSTANCE + "/account/{accountId}/workflow/{workflowId}/{workflowType}/sites";
        public static final String WORKFLOW_RULE_TYPE              = UrlPathKey.INSTANCE + "/account/{accountId}/workflow/{workflowId}/{workflowType}/ruleType";
        public static final String WORKFLOW_RULE_DETAIL            = UrlPathKey.INSTANCE + "/account/{accountId}/workflow/{workflowId}/{workflowType}/rule/{ruleId}";
        public static final String SHOPPING_CONTROL_FILTER         = UrlPathKey.INSTANCE + "/account/{accountId}/shoppingControl";
        public static final String SHOPPING_CONTROL_IDENTIFICATION = UrlPathKey.INSTANCE + "/account/{accountId}/shoppingControl/{shoppingControlId}";
        public static final String PROPERTIES                      = UrlPathKey.INSTANCE + "/account/{accountId}/properties";
    }

    public interface SITE {
        public static final String FILTER          = UrlPathKey.INSTANCE + "/location";
        public static final String IDENTIFICATION  = UrlPathKey.INSTANCE + "/location/{locationId}";
        public static final String BUDGETS         = UrlPathKey.INSTANCE + "/location/{locationId}/budgets";
        public static final String USERS           = UrlPathKey.INSTANCE + "/location/{locationId}/users";
        public static final String CATALOG         = UrlPathKey.INSTANCE + "/location/{locationId}/catalog";
        public static final String SITE_HIERARCHY  = UrlPathKey.INSTANCE + "/location/{locationId}/locationHierarchy";
        public static final String WORKFLOW        = UrlPathKey.INSTANCE + "/location/{locationId}/workflow";
        public static final String ORDER_GUIDE     = UrlPathKey.INSTANCE + "/location/{locationId}/orderGuides";
        public static final String ORDER_GUIDE_DETAIL     = UrlPathKey.INSTANCE + "/location/{locationId}/orderGuides/{orderGuideId}";
    }

    public interface EMAIL_TEMPLATE {
        public static final String FILTER         = UrlPathKey.INSTANCE + "/emailTemplate";
        public static final String IDENTIFICATION = UrlPathKey.INSTANCE + "/emailTemplate/{emailTemplateId}";
    }

    public interface USER {
        public static final String FILTER         = UrlPathKey.INSTANCE + "/user";
        public static final String IDENTIFICATION = UrlPathKey.INSTANCE + "/user/{userId}";
        public static final String ACCOUNT        = UrlPathKey.INSTANCE + "/user/{userId}/account";
        public static final String GROUP          = UrlPathKey.INSTANCE + "/user/{userId}/group";
        public static final String LOCATION       = UrlPathKey.INSTANCE + "/user/{userId}/location";
        public static final String NOTIFICATION   = UrlPathKey.INSTANCE + "/user/{userId}/notification";
        public static final String LOADER         = UrlPathKey.INSTANCE + "/user/loader";
    }

    public interface MANUFACTURER {
        public static final String FILTER                       = UrlPathKey.INSTANCE + "/manufacturer";
        public static final String IDENTIFICATION               = UrlPathKey.INSTANCE + "/manufacturer/{manufacturerId}";
    }

    public interface DISTRIBUTOR {
        public static final String FILTER                       = UrlPathKey.INSTANCE + "/distributor";
        public static final String IDENTIFICATION               = UrlPathKey.INSTANCE + "/distributor/{distributorId}";
        public static final String CONFIGURATION	            = UrlPathKey.INSTANCE + "/distributor/{distributorId}/configuration";
    }

    public interface LOCATE {
        public static final String LOCATE_ACCOUNT = UrlPathKey.INSTANCE + "/locate/account";
        public static final String LOCATE_USER = UrlPathKey.INSTANCE + "/locate/user";
        public static final String LOCATE_CATALOG = UrlPathKey.INSTANCE + "/locate/catalog";
        public static final String LOCATE_DISTRIBUTOR = UrlPathKey.INSTANCE + "/locate/distributor";
        public static final String LOCATE_ITEM = UrlPathKey.INSTANCE + "/locate/item";
        public static final String LOCATE_ITEM_COST = UrlPathKey.INSTANCE + "/locate/itemCost";
        public static final String LOCATE_SERVICE = UrlPathKey.INSTANCE + "/locate/service";
        public static final String LOCATE_SITE = UrlPathKey.INSTANCE + "/locate/site";
        public static final String LOCATE_ASSET = UrlPathKey.INSTANCE + "/locate/asset";
        public static final String LOCATE_ITEM_ORDER_GUIDE = UrlPathKey.INSTANCE + "/locate/itemOrderGuide";
    }
    
    public interface CMS{
    	public static final String CMS = UrlPathKey.INSTANCE + "/cms/{storeId}";
    }

    public interface COST_CENTER {
        public static final String FILTER          = UrlPathKey.INSTANCE + "/costCenter";
        public static final String IDENTIFICATION  = UrlPathKey.INSTANCE + "/costCenter/{costCenterId}";
        public static final String CATALOG           = UrlPathKey.INSTANCE + "/costCenter/{costCenterId}/catalog";
    }

    public interface CATALOG {
         public static final String FILTER          = UrlPathKey.INSTANCE + "/catalog";
    }
    
    //public interface CORPORATE_SCHEDULE {
    //    public static final String IDENTIFICATION  = UrlPathKey.INSTANCE + "/corporateSchedule/{corporateScheduleId}";
    //}
    
    public interface GROUP {
        public static final String FILTER          = UrlPathKey.INSTANCE + "/group";
        public static final String IDENTIFICATION  = UrlPathKey.INSTANCE + "/group/{groupId}";
        public static final String CONFIGURATION   = UrlPathKey.INSTANCE + "/group/{groupId}/configuration";
        public static final String REPORT   = UrlPathKey.INSTANCE + "/group/{groupId}/report";
        public static final String FUNCTION   = UrlPathKey.INSTANCE + "/group/{groupId}/function";
    }

    public interface HISTORY {
         public static final String FILTER          = UrlPathKey.INSTANCE + "/history";
    }

    public interface MASTER_ITEM {
        public static final String FILTER                       = UrlPathKey.INSTANCE + "/masterItem";
        public static final String IDENTIFICATION               = UrlPathKey.INSTANCE + "/masterItem/{itemId}";
    }


    public interface ORDER {
        public static final String FILTER         = UrlPathKey.INSTANCE + "/order";
        public static final String IDENTIFICATION = UrlPathKey.INSTANCE + "/order/{orderId}";
    }
    
    public interface PROFILE {
    	public static final String PASSWORD_MANAGEMENT = UrlPathKey.INSTANCE + "/profile/passwordManagement";
    }

    public interface CORPORATE_SCHEDULE {
        public static final String FILTER          = UrlPathKey.INSTANCE + "/corporateSchedule";
        public static final String IDENTIFICATION  = UrlPathKey.INSTANCE + "/corporateSchedule/{corporateScheduleId}";
        public static final String ACCOUNT   = UrlPathKey.INSTANCE + "/corporateSchedule/{corporateScheduleId}/account";
        public static final String LOCATION   = UrlPathKey.INSTANCE + "/corporateSchedule/{corporateScheduleId}/location";
    }

    public interface UPLOAD_FILE {
        public static final String FILTER                       = UrlPathKey.INSTANCE + "/uploadFile";
        public static final String IDENTIFICATION               = UrlPathKey.INSTANCE + "/uploadFile/{uploadId}";
    }

    public interface ITEM_LOADER {
        public static final String FILTER                       = UrlPathKey.INSTANCE + "/uploadFile";
        public static final String IDENTIFICATION               = UrlPathKey.INSTANCE + "/uploadFile/{uploadId}";
    }


    
    public interface BATCH_ORDER {
    	public static final String LOADER = UrlPathKey.INSTANCE + "/batchOrder/loader";
    }
    public interface CATALOG_MANAGER {
    	public static final String LOADER = UrlPathKey.INSTANCE + "/catalogManager/loader";
    }
   
}
