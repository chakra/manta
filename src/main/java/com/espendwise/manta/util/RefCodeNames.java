package com.espendwise.manta.util;


public interface RefCodeNames {

    public interface  FISCAL_CALENDER_NAME {
        public static final String FISCAL_CALENDER = "FISCAL_CALENDER";
    }

    public interface BUS_ENTITY_TYPE_CD {
        public static final String STORE = "STORE";
        public static final String ACCOUNT = "ACCOUNT";
        public static final String MANUFACTURER = "MANUFACTURER";
        public static final String DISTRIBUTOR = "DISTRIBUTOR";
        public static final String DOMAIN_NAME = "DOMAIN_NAME";
        public static final String SITE = "SITE";
        public static final String SITE_HIERARCHY_LEVEL = "SITE_HIERARCHY_LEVEL";
        public static final String SITE_HIERARCHY_LEVEL_ELEMENT = "SITE_HIERARCHY_ELEMENT";
        public static final String CERTIFIED_COMPANY ="CERTIFIED_COMPANY";
    }

    public interface ADDRESS_TYPE_CD {
        public static final String BILLING = "BILLING";
        public static final String PRIMARY_CONTACT = "PRIMARY CONTACT";
        public static final String SHIPPING = "SHIPPING";
        public static final String CUSTOMER_SHIPPING = "CUSTOMER SHIPPING";
        public static final String CUSTOMER_BILLING = "CUSTOMER BILLING";
    }

    public interface BUS_ENTITY_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }

    public interface BUS_ENTITY_ASSOC_CD {
        public static final String DEFAULT_STORE_OF_DOMAIN = "DEFAULT STORE OF DOMAIN";
        public static final String ACCOUNT_OF_STORE = "ACCOUNT OF STORE";
        public static final String SITE_OF_ACCOUNT = "SITE OF ACCOUNT";
        public static final String SITE_HIERARCHY_LEVEL_OF_ACCOUNT= "SITE_HIER_LEVEL_OF_ACCOUNT";
        public static final String SITE_HIERARCHY_LEVEL_OF_LEVEL= "SITE_HIER_LEVEL_OF_LEVEL";
        public static final String SITE_HIERARCHY_ELEMENT_OF_LEVEL= "SITE_HIER_ELEMENT_OF_LEVEL";
        public static final String SITE_HIERARCHY_SITE_OF_ELEMENT= "SITE_HIER_SITE_OF_ELEMENT";
        public static final String MANUFACTURER_OF_STORE = "MANUFACTURER OF STORE";
        public static final String DISTRIBUTOR_OF_STORE = "DISTRIBUTOR OF STORE";
        public static final String FREIGHT_HANDLER_STORE = "FREIGHT HANDLER OF STORE";
    }

    public interface PROPERTY_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }

    public interface PROPERTY_TYPE_CD {

        public static final String DEFAULT = "DEFAULT";
        public static final String MANTA_UI_LOGO = "UI_LOGO1";
        public static final String UI_PAGE_TITLE = "UI_PAGE_TITLE";
        public static final String UI_FOOTER = "UI_FOOTER";
        public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE_CD";
        public static final String BUDGET_THRESHOLD_TYPE = "BUDGET_THRESHOLD_TYPE";
        public static final String DIST_ACCT_REF_NUM = "DIST_ACCT_REF_NUM";
        public static final String BUDGET_ACCRUAL_TYPE_CD = "BUDGET_ACCRUAL_TYPE_CD";
        // -------account properties ( hardcoded in Manta)------------
        public static final String ALLOWED_RUNTIME_ORD_ITM_ACT = "ALLOWED_RUNTIME_ORD_ITM_ACT";
        public static final String ALLOW_CUSTOMER_PO_NUMBER = "ALLOW_CUSTOMER_PO_NUMBER";
        public static final String AUTHORIZED_FOR_RESALE = "AUTHORIZED_FOR_RESALE";
        public static final String COMMENTS = "COMMENTS";
        public static final String CRC_SHOP = "CRC SHOP";
        public static final String CREDIT_LIMIT = "CREDIT LIMIT";
        public static final String CREDIT_RATING = "CREDIT RATING";
        public static final String CUSTOMER_SYSTEM_APPROVAL_CD = "CUSTOMER_SYSTEM_APPROVAL_CD";
        public static final String TRACK_PUNCHOUT_ORDER_MESSAGES = "TRACK_PUNCHOUT_ORDER_MESSAGES";
        public static final String DISTR_PO_TYPE = "DISTR_PO_TYPE";
        public static final String INVENTORY_LEDGER_SWITCH = "INVENTORY_LEDGER_SWITCH";
        public static final String SCHEDULE_CUTOFF_DAYS = "SCHEDULE_CUTOFF_DAYS";
        public static final String INVENTORY_OG_LIST_UI = "INVENTORY_OG_LIST_UI";
        public static final String INVENTORY_MISSING_NOTIFICATION = "INVENTORY_MISSING_NOTIFICATION";
        public static final String INVENTORY_CHECK_PLACED_ORDER ="INVENTORY_CHECK_PLACED_ORDER";
        public static final String CONNECTION_CUSTOMER = "CONNECTION_CUSTOMER";
        public static final String CART_REMINDER_INTERVAL="CART_REMINDER_INTERVAL";
        public static final String SHOW_SCHED_DELIVERY = "SHOW_SCHED_DELIVERY";
        public static final String ALLOW_ORDER_CONSOLIDATION = "ALLOW_ORDER_CONSOLIDATION";
        public static final String SHOW_DIST_SKU_NUM = "SHOW_DIST_SKU_NUM";
        public static final String SHOW_DIST_DELIVERY_DATE = "SHOW_DIST_DELIVERY_DATE";
        public static final String RUSH_ORDER_CHARGE = "RUSH_ORDER_CHARGE";
        public static final String AUTO_ORDER_FACTOR = "AUTO_ORDER_FACTOR";
        public static final String DIST_INVENTORY_DISPLAY = "DIST_INVENTORY_DISPLAY";
        public static final String SHOW_SPL = "SHOW_SPL";
        public static final String ADD_SERVICE_FEE = "ADD_SERVICE_FEE";
        public static final String HOLD_PO = "HOLD_PO";
        public static final String ALLOW_USER_CHANGE_PASSWORD = "ALLOW_USER_CHANGE_PASSWORD";
        public static final String ACCOUNT_FOLDER = "ACCOUNT_FOLDER";
        public static final String ACCOUNT_FOLDER_NEW = "ACCOUNT_FOLDER_NEW";
        public static final String ADJUST_QTY_BY_855 = "ADJUST_QTY_BY_855";
        public static final String CREATE_ORDER_BY_855 = "CREATE_ORDER_BY_855";
        public static final String CREATE_ORDER_ITEMS_BY_855 = "CREATE_ORDER_ITEMS_BY_855";
        public static final String MODIFY_CUST_PO_NUM_BY_855 = "MODIFY_CUST_PO_NUM_BY_855";
        public static final String SUPPORTS_BUDGET = "SUPPORTS_BUDGET";
        public static final String ALTERNATE_UI = "ALTERNATE_UI";
        public static final String ALLOW_MODERN_SHOPPING = "ALLOW_MODERN_SHOPPING";
        public static final String NOT_APP_TO_BUDGET_FOR_BT_ORDER = "NOT_APP_TO_BUDGET_FOR_BT_ORDER";
        public static final String SEND_ORDER_CONFRM_FOR_BT_ORDER = "SEND_ORDER_CONFRM_FOR_BT_ORDER";
//        public static final String ALLOW_SITE_LLC = "ALLOW_SITE_LLC";
        public static final String SHOW_EXPRESS_ORDER = "SHOW_EXPRESS_ORDER";
        public static final String ALLOW_ORDER_INV_ITEMS = "ALLOW_ORDER_INV_ITEMS";
        public static final String ALLOW_REORDER = "ALLOW_REORDER";
        public static final String USE_PHYSICAL_INVENTORY = "USE_PHYSICAL_INVENTORY";
        public static final String SHOW_INV_CART_TOTAL = "SHOW_INV_CART_TOTAL";
        public static final String SHOW_MY_SHOPPING_LISTS = "SHOW_MY_SHOPPING_LISTS";
//        public static final String SHOW_REBILL_ORDER = "SHOW_REBILL_ORDER";
        public static final String ALLOW_SET_WORKORDER_PO_NUMBER = "ALLOW_SET_WORKORDER_PO_NUMBER";
        public static final String USER_ASSIGNED_ASSET_NUMBER = "USER_ASSIGNED_ASSET_NUMBER";
        public static final String ALLOW_BUY_WORK_ORDER_PARTS = "ALLOW_BUY_WORK_ORDER_PARTS";
        public static final String CONTACT_INFORMATION_TYPE = "CONTACT_INFORMATION_TYPE";
//        public static final String WORK_ORDER_PO_NUM_REQUIRED = "WORK_ORDER_PO_NUM_REQUIRED";
        public static final String SHOP_UI_TYPE = "SHOP_UI_TYPE";
        public static final String FAQ_LINK = "FAQ_LINK";
        public static final String REMINDER_EMAIL_SUBJECT = "REMINDER_EMAIL_SUBJECT";
        public static final String REMINDER_EMAIL_MSG = "REMINDER_EMAIL_MSG";
        public static final String NOTIFY_ORDER_EMAIL_GENERATOR = "NOTIFY_ORDER_EMAIL_GENERATOR";
        public static final String PENDING_APPROV_EMAIL_GENERATOR = "PENDING_APPROV_EMAIL_GENERATOR";
        public static final String REJECT_ORDER_EMAIL_GENERATOR = "REJECT_ORDER_EMAIL_GENERATOR";
        public static final String CONFIRM_ORDER_EMAIL_GENERATOR = "CONFIRM_ORDER_EMAIL_GENERATOR";
//        public static final String ORDER_CONFIRMATION_EMAIL_TEMPLATE = "ORDER_CONFIRM_EMAIL_TEMPLATE";
//        public static final String SHIPPING_NOTIFICATION_EMAIL_TEMPLATE = "SHIP_NOTIFY_EMAIL_TEMPLATE";
//        public static final String PENDING_APPROVAL_EMAIL_TEMPLATE = "PEND_APPROVAL_EMAIL_TEMPLATE";
//        public static final String REJECTED_ORDER_EMAIL_TEMPLATE = "REJECTED_ORDER_EMAIL_TEMPLATE";
//        public static final String MODIFIED_ORDER_EMAIL_TEMPLATE = "MODIFIED_ORDER_EMAIL_TEMPLATE";
        public static final String CONTACT_US_CC_ADD = "CONTACT_US_CC_ADD";
        public static final String PDF_ORDER_CLASS="PDF_ORDER_CLASS";
        public static final String PDF_ORDER_STATUS_CLASS="PDF_ORDER_STATUS_CLASS";
        public static final String ALLOW_CC_PAYMENT  = "ALLOW_CC_PAYMENT";
        public static final String ALLOW_ALTERNATE_SHIP_TO_ADDRESS = "ALLOW_ALTERNATE_SHIP_TO_ADDRESS";
        public static final String INVENTORY_PO_SUFFIX="INVENTORY_PO_SUFFIX";
        public static final String FREIGHT_CHARGE_TYPE = "FREIGHT_CHARGE_TYPE";
        public static final String GL_TRANSFORMATION_TYPE = "GL_TRANSFORMATION_TYPE";
        public static final String MAKE_SHIP_TO_BILL_TO = "MAKE_SHIP_TO_BILL_TO";
        public static final String ORDER_MINIMUM = "ORDER MINIMUM";
        public static final String ORDER_GUIDE_NOTE = "ORDER_GUIDE_NOTE";
        public static final String PURCHASE_ORDER_ACCOUNT_NAME = "PURCHASE_ORDER_ACCOUNT_NAME";
        public static final String RESALE_ACCOUNT_ERP_NUM = "RESALE_ACCOUNT_ERP_NUM";
        public static final String SKU_TAG = "SKU_TAG";
        public static final String TARGET_MARGIN = "TARGET_MARGIN";
//        public static final String TAXABLE_INDICATOR = "TAXABLE_INDICATOR";
        //---------------------------------------------
        public static final String USE_FOR_SERVICE = "USE_FOR_SERVICE";

        public static final String ORDER_CONFIRMATION_EMAIL_TEMPLATE = "ORDER_CONFIRM_EMAIL_TEMPLATE";
        public static final String SHIPPING_NOTIFICATION_EMAIL_TEMPLATE = "SHIP_NOTIFY_EMAIL_TEMPLATE";
        public static final String PENDING_APPROVAL_EMAIL_TEMPLATE = "PEND_APPROVAL_EMAIL_TEMPLATE";
        public static final String REJECTED_ORDER_EMAIL_TEMPLATE = "REJECTED_ORDER_EMAIL_TEMPLATE";
        public static final String MODIFIED_ORDER_EMAIL_TEMPLATE = "MODIFIED_ORDER_EMAIL_TEMPLATE";
        public static final String SITE_REFERENCE_NUMBER  = "SITE_REFERENCE_NUMBER";
        public static final String EXTRA ="EXTRA";
        public static final String USER_ID_CODE = "USER_ID_CODE";
        public static final String CORPORATE_USER = "CORPORATE_USER";
        public static final String IVR_UIN = "IVR_UIN";
        public static final String IVR_PIN = "IVR_PIN";
        public static final String WORK_ORDER_PO_NUM_REQUIRED = "WORK_ORDER_PO_NUM_REQUIRED";
        public static final String ALLOW_CORPORATE_SCHED_ORDER = "ALLOW_CORPORATE_SCHED_ORDER";
        public static final String SHOW_REBILL_ORDER = "SHOW_REBILL_ORDER";
        public static final String BYPASS_ORDER_ROUTING = "BYPASS_ORDER_ROUTING";
        public static final String CONSOLIDATED_ORDER_WAREHOUSE = "CONSOLIDATED_ORDER_WAREHOUSE";
        public static final String INVENTORY_SHOPPING = "INVENTORY_SHOPPING";
        public static final String INVENTORY_SHOPPING_TYPE = "INVENTORY_SHOPPING_TYPE";
        public static final String SHARE_ORDER_GUIDES = "SHARE_ORDER_GUIDES";
        public static final String TAXABLE_INDICATOR = "TAXABLE_INDICATOR";
        public static final String DIST_SITE_REFERENCE_NUMBER = "DIST_SITE_REFERENCE_NUMBER";
        public static final String LOCATION_PRODUCT_BUNDLE = "SITE_PRODUCT_BUNDLE";
        public static final String TARGET_FACILITY_RANK = "TARGET_FACILITY_RANK";
        public static final String ALLOW_SITE_LLC = "ALLOW_SITE_LLC";
        public static final String LOCATION_COMMENTS = "SITE_COMMENTS";
        public static final String LOCATION_SHIP_MSG = "SITE_SHIP_MSG";
        public static final String ERP_SYSTEM = "ERP_SYSTEM";
        public static final String DEFAULT_SITE = "DEFAULT_SITE";
        public static final String DEFAULT_STORE = "DEFAULT_STORE";
        public static final String SITE_PRIMARY_USER = "SITE_PRIMARY_USER";

        public static final String BUDGET_THRESHOLD_FL = "BUDGET_THRESHOLD_FL";

        public static final String NOTIFCTN_SERVICE_ACCEPTED_DAYS = "NOTIFCTN_SERVICE_ACCEPTED_DAYS";
        public static final String NOTIFCTN_SCHEDULED_SERVICE = "NOTIFCTN_SCHEDULED_SERVICE";
        public static final String NOTIFCTN_SERVICE_ACCEPTED = "NOTIFCTN_SERVICE_ACCEPTED";
        public static final String NOTIFCTN_SERVICE_ACCEPTED_AUTO = "NOTIFCTN_SERVICE_ACCEPTED_AUTO";
        public static final String NOTIF_SERVICE_ACCEPTED_AUTO_V = "NOTIF_SERVICE_ACCEPTED_AUTO_V";
        public static final String NOTIF_SERVICE_REJ_BY_PROVIDER = "NOTIF_SERVICE_REJ_BY_PROVIDER";
        public static final String NOTIF_SERVICE_REJ_BY_STORE = "NOTIF_SERVICE_REJ_BY_STORE";
        public static final String NOTIF_SERVICE_COMPLETED = "NOTIF_SERVICE_COMPLETED";
        public static final String NOTIFCTN_RESCHEDULED_SERVICE = "NOTIFCTN_RESCHEDULED_SERVICE";
        public static final String OTIFCTN_SERVICE_REMINDER = "NOTIFCTN_SERVICE_REMINDER";
        public static final String NOTIF_REQ_SERVICE_APPROVAL = "NOTIF_REQ_SERVICE_APPROVAL";
        public static final String NOTIFCTN_SERVICE_REMINDER_DAYS_WEEKLY = "NOTIFCTN_SERVICE_REM_DAYS_W";
        public static final String NOTIFCTN_SERVICE_REMINDER_DAYS_DAILY = "NOTIFCTN_SERVICE_REM_DAYS_D";
        public static final String NOTIF_INVOICE_REJECTED="NOTIF_INVOICE_REJECTED";
        public static final String NOTIFCTN_NO_SHOW = "NOTIFCTN_NO_SHOW";
        public static final String NOTIFCTN_SMS_NO_SHOW = "NOTIFCTN_SMS_NO_SHOW";
        public static final String NOTIFCTN_INVOICE_REJECTED = "NOTIFCTN_INVOICE_REJECTED";
        public static final String CUTOFF_TIME_EMAIL_REMINDER_CNT = "CUTOFF_TIME_EMAIL_REMINDER_CNT";
        public static final String RECEIVE_INV_MISSING_EMAIL = "RECEIVE_INV_MISSING_EMAIL";

        public static final String DISPLAY_GENERIC_CONTENT = "DISPLAY_GENERIC_CONTENT";
        public static final String CUSTOM_CONTENT_URL = "CUSTOM_CONTENT_URL";
        public static final String CUSTOM_CONTENT_EDITOR = "CUSTOM_CONTENT_EDITOR";
        public static final String CUSTOM_CONTENT_VIEWER = "CUSTOM_CONTENT_VIEWER";

        public static final String  ACCESS_TOKEN = "ACCESS_TOKEN";

        public static final String MSDS_PLUGIN = "MSDS Plug-in";
        
        public static final String RUNTIME_DISPLAY_NAME = "RUNTIME_DISPLAY_NAME";
        public static final String CALL_HOURS = "CALL_HOURS";
        public static final String DISTRIBUTORS_COMPANY_CODE = "DISTRIBUTORS_COMPANY_CODE";
        public static final String CUSTOMER_REFERENCE_CODE = "CUSTOMER_REFERENCE_CODE";
        public static final String DIST_MAX_INVOICE_FREIGHT = "DIST_MAX_INVOICE_FREIGHT";
        public static final String EXCEPTION_ON_TAX_DIFFERENCE = "EXCEPTION_ON_TAX_DIFFERENCE";
        public static final String HOLD_INVOICE = "HOLD_INVOICE";
        public static final String IGNORE_ORDER_MIN_FOR_FREIGHT = "IGNORE_ORDER_MIN_FOR_FREIGHT";
        public static final String INVOICE_AMT_PERCNT_ALLOW_UPPER = "INVOICE_AMT_PERCNT_ALLOW_UPPER";
        public static final String INVOICE_AMT_PERCNT_ALLOW_LOWER = "INVOICE_AMT_PERCNT_ALLOW_LOWER";
        public static final String INVOICE_LOADING_PRICE_MODEL_CD = "INVOICE_LOADING_PRICE_MODEL_CD";
        public static final String RECEIVING_SYSTEM_INVOICE_CD = "RECEIVING_SYSTEM_INVOICE_CD";
        public static final String ERROR_ON_OVERCHARGED_FREIGHT = "ERROR_ON_OVERCHARGED_FREIGHT";
        public static final String ALLOW_FREIGHT_ON_BACKORDERS = "ALLOW_FREIGHT_ON_BACKORDERS";
        public static final String PURCHASE_ORDER_COMMENTS = "PURCHASE_ORDER_COMMENTS";
        public static final String PRINT_CUST_CONTACT_ON_PO = "PRINT_CUST_CONTACT_ON_PO";
        public static final String MAN_PO_ACK_REQUIERED = "MAN_PO_ACK_REQUIERED";
        public static final String CANCEL_BACKORDERED_LINES = "CANCEL_BACKORDERED_LINES";
        public static final String DO_NOT_ALLOW_INVOICE_EDITS = "DO_NOT_ALLOW_INVOICE_EDITS";
        public static final String RESET_PASSWORD_UPON_INIT_LOGIN = "RESET_PASSWORD_UPON_INIT_LOGIN";
        public static final String RESET_PASSWORD_WITHIN_DAYS = "RESET_PASSWORD_WITHIN_DAYS";
        public static final String NOTIFY_PASSWORD_EXPIRY_IN_DAYS = "NOTIFY_PASSWORD_EXPIRY_IN_DAYS";
        public static final String STORE_TYPE_CD = "STORE_TYPE_CD";
        public static final String FR_ON_INVOICE_CD = "FR_ON_INVOICE_CD";
        public static final String FR_ROUTING_CD = "FR_ROUTING_CD";
        public static final String SHOW_DISTR_NOTES_TO_CUSTOMER = "SHOW_DISTR_NOTES_TO_CUSTOMER";
        public static final String SITE_NOTE_TOPIC = "SITE_NOTE_TOPIC";

        public static final String AUTO_SKU_ASSIGN = "AUTO_SKU_ASSIGN";
        public static final String STORE_BUSINESS_NAME = "STORE_BUSINESS_NAME";
        public static final String STORE_PRIMARY_WEB_ADDRESS = "STORE_PRIMARY_WEB_ADDRESS";
        public static final String STORE_PREFIX_CODE = "STORE PREFIX";
        public static final String ALLOW_MIXED_CATEGORY_AND_ITEM = "ALLOW_MIXED_CATEGORY_AND_ITEM";
    }

    public interface BUDGET_THRESHOLD_TYPE {
        public static final String NONE = "None";
        public static final String ACCOUNT_BUDGET_THRESHOLD = "Account Budget Threshold";
        public static final String SITE_BUDGET_THRESHOLD = "Site Budget Threshold";
    }

    public interface COUNTRY_PROPERTY {
        public static final String USES_STATE = "USES_STATE";
    }

    public interface USER_ASSOC_CD {
        public static final String STORE = "STORE";
        public static final String ACCOUNT = "ACCOUNT";
        public static final String SITE = "SITE";
    }

    public interface USER_TYPE_CD {
        public static final String
                ADMINISTRATOR = "ADMINISTRATOR",
                SYSTEM_ADMINISTRATOR = "SYSTEM_ADMINISTRATOR",
                STORE_ADMINISTRATOR = "STORE ADMINISTRATOR",
                MULTI_SITE_BUYER = "MULTI-SITE BUYER",
                ASSET_ADMINISTRATOR = "ASSET ADMINISTRATOR",
                CUSTOMER = "CUSTOMER",
                REPORTING_USER = "REPORTING_USER",
                SERVICE_PROVIDER = "SERVICE_PROVIDER",
                DISTRIBUTOR = "DISTRIBUTOR";
    }
    
    public interface USER_ROLE_CD {
        public static final String
                UNKNOWN = "UNKNOWN",
                SERVICE_VENDOR = "SERVICE_VENDOR",
                SITE_MANAGER = "SITE_MANAGER";
    }

    public interface GROUP_ASSOC_CD {
        public static final String
                FUNCTION_OF_GROUP = "FUNCTION_OF_GROUP",
                STORE_OF_GROUP = "STORE_OF_GROUP",
                USER_OF_GROUP = "USER_OF_GROUP",
                BUS_ENTITY_OF_GROUP = "BUS_ENTITY_OF_GROUP",
                REPORT_OF_GROUP = "REPORT_OF_GROUP";
    }

    public interface GROUP_STATUS_CD {
        public static final String
                ACTIVE = "ACTIVE",
                INACTIVE = "INACTIVE";
    }

    public interface GROUP_TYPE_CD {
        public static final String
        ACCOUNT = "ACCOUNT",
        DISTRIBUTOR = "DISTRIBUTOR",
        MANUFACTURER = "MANUFACTURER",
        STORE = "STORE",
        USER = "USER";
    }

    public interface STORE_MESSAGE_STATUS_CD {
        public static final String INACTIVE = "INACTIVE";
        public static final String ACTIVE = "ACTIVE";
    }

    public interface STORE_MESSAGE_ASSOC_CD {
        public static final String MESSAGE_STORE = "MESSAGE_STORE";
        public static final String MESSAGE_ACCOUNT = "MESSAGE_ACCOUNT";
    }

    public interface USER_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }

    public interface EMAIL_TYPE_CD {
        public static final String PRIMARY_CONTACT = "PRIMARY";
        public static final String ESCALATION = "ESCALATION";
        public static final String SMS = "SMS";
        public static final String REJECTED_INVOICE = "REJECTED_INVOICE";
        public static final String CUSTOMER_SERVICE = "CUSTOMER SERVICE";
        public static final String CONTACT_US = "CONTACT US";
        public static final String DEFAULT = "DEFAULT";
        public static final String ORDER_MANAGER = "ORDER_MANAGER";
    }

    public interface PHONE_TYPE_CD {
        public static final String PHONE = "PHONE";
        public static final String FAX = "FAX";
        public static final String POFAX = "POFAX";
        public static final String MOBILE = "MOBILE";
    }

    public interface ACCOUNT_TYPE_CD {
        public static final String AIRPORT = "Airport";
        public static final String BSC = "BSC";
        public static final String BUILDING_SERVICE_CONTRACTOR = "Building Service Contractor";
        public static final String CHURCHES = "Churches";
        public static final String COMMERCIAL = "Commercial";
        public static final String CONVENTION_CENTER = "Convention Center";
        public static final String DISTRIBUTOR = "Distributor";
        public static final String EDUCATION = "Education";
        public static final String FOOD_INSTITUTIONAL = "Food - Institutional";
        public static final String FOOD_RETAIL = "Food - Retail";
        public static final String FOOD_SERVICE = "Food Service";
        public static final String GOVERNMENT = "Government";
        public static final String HEALTH_CARE = "Health Care";
        public static final String HOSPITALITY = "Hospitality";
        public static final String HOSPITAL_ACUTE_CARE = "Hospital - Acute Care";
        public static final String INDUSTRIAL = "Industrial";
        public static final String INDUSTRIAL_MANUFACTURING = "Industrial & Manufacturing";
        public static final String LODGING = "Lodging";
        public static final String MISCELLANEOUS = "Miscellaneous";
        public static final String NON_FOOD_RETAIL = "Non-Food - Retail";
        public static final String NURSING_HOME = "Nursing Home";
        public static final String OFFICE_BUILDING = "Office Building";
        public static final String RECREATION = "Recreation";
        public static final String REDISTRIBUTOR = "Redistributor";
        public static final String RETAIL = "Retail";
        public static final String SHOPPING_MALL = "Shopping Mall";
        public static final String OTHER = "Other";
    }

    public interface BUDGET_ACCRUAL_TYPE_CD {
        public static final String BY_PERIOD = "BY_PERIOD";
        public static final String BY_FISCAL_YEAR = "BY_FISCAL_YEAR";
    }

    public interface BUDGET_PERIOD_CD {
        public static final String  MONTHLY = "MONTHLY";
    }

    public interface WORKFLOW_ROLE_CD {
        public static final String CUSTOMER = "CUSTOMER";
        public static final String ORDER_APPROVER = "ORDER APPROVER";
        public static final String UNKNOWN = "UNKNOWN";
    }
 
    public interface WO_WORKFLOW_RULE_TYPE_CD {
        public static final String
                SERVICE_TICKET_SERVICE_TYPE = "STServiceType",
                SERVICE_TICKET_FREQUENCE_FOR_SERVICE_TYPE = "STFrequenceForServiceType",
                SERVICE_TICKET_NOTIFICATION = "STNotification",
                FREEFORM_SERVICE_TICKET_NOTIFICATION = "FreeformSTNotification";

    }
    public interface WORKFLOW_RULE_TYPE_CD {
        public static final String
		        BREAK_POINT = "BreakPoint",
		        ORDER_TOTAL = "OrderTotal",
		        ORDER_VELOCITY = "OrderVelocity",
		        ORDER_SKU = "OrderSku",
		        BUDGET_REMAINING_PER_CC = "CostCenterBudget",
		        BUDGET_YTD = "BudgetYTD",
		        ORDER_SKU_QTY = "OrderSkuQty",
		        DIST_SHIPTO_STATE = "DistShiptoState",
		        EVERY_ORDER = "EveryOrder",
		        NON_ORDER_GUIDE_ITEM = "NonOrderGuideItem",
		        FREIGHT_HANDLER = "FreightType",
		        RUSH_ORDER = "RushOrder",
		        CATEGORY_TOTAL = "CategoryTotal",
		        ITEM_CATEGORY = "ItemCategory",
		        SHOPPING_CONTROLS = "ShoppingControls",
//		        WORK_ORDER_BUDGET = "WorkOrderBudget",
//		        WORK_ORDER_TOTAL = "WorkOrderTotal",
//		        WORK_ORDER_ANY = "WorkOrderAny",
//		        WORK_ORDER_BUDGET_SPENDING_FOR_COST_CENTER = "WorkOrderBudgetSpendingForCC",
		        ITEM = "Item",
		        ITEM_PRICE = "ItemPrice",
		        USER_LIMIT = "UserLimit",
		        ORDER_BUDGET_PERIOD_CHANGED = "OrderBudgetPeriodChanged",
		        ORDER_EXCLUDED_FROM_BUDGET = "OrderExcludedFromBudget";

    }
    public interface WORKFLOW_ASSOC_CD {
        public static final String
                APPLY_FOR_GROUP_USERS = "Apply For Group Users",
                SKIP_FOR_GROUP_USERS = "Skip For Group Users";
    }
    public interface WORKFLOW_TYPE_CD {
        public static final String
                ORDER_WORKFLOW = "ORDER WORKFLOW",
        		SERVICE_WORKFLOW = "SERVICE_WORKFLOW";
    }
    public interface WORKFLOW_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String LIMITED = "LIMITED";
    }

    public interface WORKFLOW_RULE_ACTION {
        public static final String
                FWD_FOR_APPROVAL = "Forward for approval",
                STOP_ORDER = "Hold order for review",
                SEND_EMAIL = "Send Email",
                REJECT_ORDER = "Reject the order",
                APPROVE_ORDER = "Approve the order";
//                PENDING_REVIEW = "Pending review",
//                DISPLAY_MESSAGE = "Display Message";
     }
 
    public interface WO_WORKFLOW_RULE_ACTION {
        public static final String
        		FWD_FOR_APPROVAL = "Forward for approval";
   }

    public interface WO_WORKFLOW_NEXT_RULE_ACTION {
        public static final String
                APPROVE = "Approve the Service Ticket",
                REJECT = "Reject the Service Ticket";
    }
    public interface WORKFLOW_NEXT_RULE_ACTION {
        public static final String
                APPROVE = "Approve the order",
                REJECT = "Reject the order";
    }

    public interface WORKFLOW_ASSOC_ACTION_TYPE_CD {
        public static final String
                APPLY = "Apply",
                SKIP = "Skip";
    }

    public interface CUSTOMER_SYSTEM_APPROVAL_CD {
        public static final String
                NONE = "None",
                PUNCH_OUT_NON_ELEC_ORD = "Punch Out Non Elec Ord",
                PUNCH_OUT_NON_ELEC_ORD_ONLY = "ONLY Punch Out Non Elec Ord",
                PUNCH_OUT_INLIN_NON_E_ORD_ONLY = "ONLY Punch Out Inline NonElec",
                PUNCH_OUT_INLIN_ORD_SAP = "Punch Out Non Elec Ord SAP";
    }
    
    public interface WORKFLOW_RULE_EXPRESSION {
        public static final String
                GREATER = ">",
                GREATER_OR_EQUAL = ">=",
                LESS = "<",
                LESS_OR_EQUAL = "<=",
                EQUALS = "==",
                SKU_NUM = "SKU_NUM",
                CATEGORY_ID = "CATEGORY_ID",
                DISTR_ID = "DISTR_ID", 
                ITEM_ID = "ITEM_ID",
                SPLIT_ORDER="SPLIT_ORDER",
                INCLUDE_BUYER_LIST="INCLUDE_BUYER_LIST"
               ;
    }

    
    public interface TIME_ZONE_CD {
        public static final String
                ETC_GMT_P12 = "Etc/GMT+12",
                ACT = "ACT",
                AET = "AET",
                AFRICA_ABIDJAN = "Africa/Abidjan",
                AFRICA_ACCRA = "Africa/Accra",
                AFRICA_ADDIS_ABABA = "Africa/Addis_Ababa",
                AFRICA_ALGIERS = "Africa/Algiers",
                AFRICA_ASMARA = "Africa/Asmara",
                AFRICA_ASMERA = "Africa/Asmera",
                AFRICA_BAMAKO = "Africa/Bamako",
                AFRICA_BANGUI = "Africa/Bangui",
                AFRICA_BANJUL = "Africa/Banjul",
                AFRICA_BISSAU = "Africa/Bissau",
                AFRICA_BLANTYRE = "Africa/Blantyre",
                AFRICA_BRAZZAVILLE = "Africa/Brazzaville",
                AFRICA_BUJUMBURA = "Africa/Bujumbura",
                AFRICA_CAIRO = "Africa/Cairo",
                AFRICA_CASABLANCA = "Africa/Casablanca",
                AFRICA_CEUTA = "Africa/Ceuta",
                AFRICA_CONAKRY = "Africa/Conakry",
                AFRICA_DAKAR = "Africa/Dakar",
                AFRICA_DAR_ES_SALAAM = "Africa/Dar_es_Salaam",
                AFRICA_DJIBOUTI = "Africa/Djibouti",
                AFRICA_DOUALA = "Africa/Douala",
                AFRICA_EL_AAIUN = "Africa/El_Aaiun",
                AFRICA_FREETOWN = "Africa/Freetown",
                AFRICA_GABORONE = "Africa/Gaborone",
                AFRICA_HARARE = "Africa/Harare",
                AFRICA_JOHANNESBURG = "Africa/Johannesburg",
                AFRICA_KAMPALA = "Africa/Kampala",
                AFRICA_KHARTOUM = "Africa/Khartoum",
                AFRICA_KIGALI = "Africa/Kigali",
                AFRICA_KINSHASA = "Africa/Kinshasa",
                AFRICA_LAGOS = "Africa/Lagos",
                AFRICA_LIBREVILLE = "Africa/Libreville",
                AFRICA_LOME = "Africa/Lome",
                AFRICA_LUANDA = "Africa/Luanda",
                AFRICA_LUBUMBASHI = "Africa/Lubumbashi",
                AFRICA_LUSAKA = "Africa/Lusaka",
                AFRICA_MALABO = "Africa/Malabo",
                AFRICA_MAPUTO = "Africa/Maputo",
                AFRICA_MASERU = "Africa/Maseru",
                AFRICA_MBABANE = "Africa/Mbabane",
                AFRICA_MOGADISHU = "Africa/Mogadishu",
                AFRICA_MONROVIA = "Africa/Monrovia",
                AFRICA_NAIROBI = "Africa/Nairobi",
                AFRICA_NDJAMENA = "Africa/Ndjamena",
                AFRICA_NIAMEY = "Africa/Niamey",
                AFRICA_NOUAKCHOTT = "Africa/Nouakchott",
                AFRICA_OUAGADOUGOU = "Africa/Ouagadougou",
                AFRICA_PORTO_MNOVO = "Africa/Porto-Novo",
                AFRICA_SAO_TOME = "Africa/Sao_Tome",
                AFRICA_TIMBUKTU = "Africa/Timbuktu",
                AFRICA_TRIPOLI = "Africa/Tripoli",
                AFRICA_TUNIS = "Africa/Tunis",
                AFRICA_WINDHOEK = "Africa/Windhoek",
                AGT = "AGT",
                AMERICA_ADAK = "America/Adak",
                AMERICA_ANCHORAGE = "America/Anchorage",
                AMERICA_ANGUILLA = "America/Anguilla",
                AMERICA_ANTIGUA = "America/Antigua",
                AMERICA_ARAGUAINA = "America/Araguaina",
                AMERICA_ARGENTINA_BUENOS_AIRES = "America/Argentina/Buenos_Aires",
                AMERICA_ARGENTINA_CATAMARCA = "America/Argentina/Catamarca",
                //AMERICA_ARGENTINA_COMODRIVADAVIA="America/Argentina/ComodRivadavia",
                AMERICA_ARGENTINA_CORDOBA = "America/Argentina/Cordoba",
                AMERICA_ARGENTINA_JUJUY = "America/Argentina/Jujuy",
                AMERICA_ARGENTINA_LA_RIOJA = "America/Argentina/La_Rioja",
                AMERICA_ARGENTINA_MENDOZA = "America/Argentina/Mendoza",
                AMERICA_ARGENTINA_RIO_GALLEGOS = "America/Argentina/Rio_Gallegos",
                AMERICA_ARGENTINA_SAN_JUAN = "America/Argentina/San_Juan",
                AMERICA_ARGENTINA_TUCUMAN = "America/Argentina/Tucuman",
                AMERICA_ARGENTINA_USHUAIA = "America/Argentina/Ushuaia",
                AMERICA_ARUBA = "America/Aruba",
                AMERICA_ASUNCION = "America/Asuncion",
                AMERICA_ATIKOKAN = "America/Atikokan",
                AMERICA_ATKA = "America/Atka",
                AMERICA_BAHIA = "America/Bahia",
                AMERICA_BARBADOS = "America/Barbados",
                AMERICA_BELEM = "America/Belem",
                AMERICA_BELIZE = "America/Belize",
                AMERICA_BLANC_MSABLON = "America/Blanc-Sablon",
                AMERICA_BOA_VISTA = "America/Boa_Vista",
                AMERICA_BOGOTA = "America/Bogota",
                AMERICA_BOISE = "America/Boise",
                AMERICA_BUENOS_AIRES = "America/Buenos_Aires",
                AMERICA_CAMBRIDGE_BAY = "America/Cambridge_Bay",
                AMERICA_CAMPO_GRANDE = "America/Campo_Grande",
                AMERICA_CANCUN = "America/Cancun",
                AMERICA_CARACAS = "America/Caracas",
                AMERICA_CATAMARCA = "America/Catamarca",
                AMERICA_CAYENNE = "America/Cayenne",
                AMERICA_CAYMAN = "America/Cayman",
                AMERICA_CHICAGO = "America/Chicago",
                AMERICA_CHIHUAHUA = "America/Chihuahua",
                AMERICA_CORAL_HARBOUR = "America/Coral_Harbour",
                AMERICA_CORDOBA = "America/Cordoba",
                AMERICA_COSTA_RICA = "America/Costa_Rica",
                AMERICA_CUIABA = "America/Cuiaba",
                AMERICA_CURACAO = "America/Curacao",
                AMERICA_DANMARKSHAVN = "America/Danmarkshavn",
                AMERICA_DAWSON = "America/Dawson",
                AMERICA_DAWSON_CREEK = "America/Dawson_Creek",
                AMERICA_DENVER = "America/Denver",
                AMERICA_DETROIT = "America/Detroit",
                AMERICA_DOMINICA = "America/Dominica",
                AMERICA_EDMONTON = "America/Edmonton",
                AMERICA_EIRUNEPE = "America/Eirunepe",
                AMERICA_EL_SALVADOR = "America/El_Salvador",
                AMERICA_ENSENADA = "America/Ensenada",
                AMERICA_FORT_WAYNE = "America/Fort_Wayne",
                AMERICA_FORTALEZA = "America/Fortaleza",
                AMERICA_GLACE_BAY = "America/Glace_Bay",
                AMERICA_GODTHAB = "America/Godthab",
                AMERICA_GOOSE_BAY = "America/Goose_Bay",
                AMERICA_GRAND_TURK = "America/Grand_Turk",
                AMERICA_GRENADA = "America/Grenada",
                AMERICA_GUADELOUPE = "America/Guadeloupe",
                AMERICA_GUATEMALA = "America/Guatemala",
                AMERICA_GUAYAQUIL = "America/Guayaquil",
                AMERICA_GUYANA = "America/Guyana",
                AMERICA_HALIFAX = "America/Halifax",
                AMERICA_HAVANA = "America/Havana",
                AMERICA_HERMOSILLO = "America/Hermosillo",
                AMERICA_INDIANA_INDIANAPOLIS = "America/Indiana/Indianapolis",
                AMERICA_INDIANA_KNOX = "America/Indiana/Knox",
                AMERICA_INDIANA_MARENGO = "America/Indiana/Marengo",
                AMERICA_INDIANA_PETERSBURG = "America/Indiana/Petersburg",
                AMERICA_INDIANA_TELL_CITY = "America/Indiana/Tell_City",
                AMERICA_INDIANA_VEVAY = "America/Indiana/Vevay",
                AMERICA_INDIANA_VINCENNES = "America/Indiana/Vincennes",
                AMERICA_INDIANA_WINAMAC = "America/Indiana/Winamac",
                AMERICA_INDIANAPOLIS = "America/Indianapolis",
                AMERICA_INUVIK = "America/Inuvik",
                AMERICA_IQALUIT = "America/Iqaluit",
                AMERICA_JAMAICA = "America/Jamaica",
                AMERICA_JUJUY = "America/Jujuy",
                AMERICA_JUNEAU = "America/Juneau",
                AMERICA_KENTUCKY_LOUISVILLE = "America/Kentucky/Louisville",
                AMERICA_KENTUCKY_MONTICELLO = "America/Kentucky/Monticello",
                AMERICA_KNOX_IN = "America/Knox_IN",
                AMERICA_LA_PAZ = "America/La_Paz",
                AMERICA_LIMA = "America/Lima",
                AMERICA_LOS_ANGELES = "America/Los_Angeles",
                AMERICA_LOUISVILLE = "America/Louisville",
                AMERICA_MACEIO = "America/Maceio",
                AMERICA_MANAGUA = "America/Managua",
                AMERICA_MANAUS = "America/Manaus",
                AMERICA_MARTINIQUE = "America/Martinique",
                AMERICA_MAZATLAN = "America/Mazatlan",
                AMERICA_MENDOZA = "America/Mendoza",
                AMERICA_MENOMINEE = "America/Menominee",
                AMERICA_MERIDA = "America/Merida",
                AMERICA_MEXICO_CITY = "America/Mexico_City",
                AMERICA_MIQUELON = "America/Miquelon",
                AMERICA_MONCTON = "America/Moncton",
                AMERICA_MONTERREY = "America/Monterrey",
                AMERICA_MONTEVIDEO = "America/Montevideo",
                AMERICA_MONTREAL = "America/Montreal",
                AMERICA_MONTSERRAT = "America/Montserrat",
                AMERICA_NASSAU = "America/Nassau",
                AMERICA_NEW_YORK = "America/New_York",
                AMERICA_NIPIGON = "America/Nipigon",
                AMERICA_NOME = "America/Nome",
                AMERICA_NORONHA = "America/Noronha",
                AMERICA_NORTH_DAKOTA_CENTER = "America/North_Dakota/Center",
                AMERICA_NORTH_DAKOTA_NEW_SALEM = "America/North_Dakota/New_Salem",
                AMERICA_PANAMA = "America/Panama",
                AMERICA_PANGNIRTUNG = "America/Pangnirtung",
                AMERICA_PARAMARIBO = "America/Paramaribo",
                AMERICA_PHOENIX = "America/Phoenix",
                AMERICA_PORT_OF_SPAIN = "America/Port_of_Spain",
                AMERICA_PORT_MAU_MPRINCE = "America/Port-au-Prince",
                AMERICA_PORTO_ACRE = "America/Porto_Acre",
                AMERICA_PORTO_VELHO = "America/Porto_Velho",
                AMERICA_PUERTO_RICO = "America/Puerto_Rico",
                AMERICA_RAINY_RIVER = "America/Rainy_River",
                AMERICA_RANKIN_INLET = "America/Rankin_Inlet",
                AMERICA_RECIFE = "America/Recife",
                AMERICA_REGINA = "America/Regina",
                AMERICA_RESOLUTE = "America/Resolute",
                AMERICA_RIO_BRANCO = "America/Rio_Branco",
                AMERICA_ROSARIO = "America/Rosario",
                AMERICA_SANTIAGO = "America/Santiago",
                AMERICA_SANTO_DOMINGO = "America/Santo_Domingo",
                AMERICA_SAO_PAULO = "America/Sao_Paulo",
                AMERICA_SCORESBYSUND = "America/Scoresbysund",
                AMERICA_SHIPROCK = "America/Shiprock",
                AMERICA_ST_JOHNS = "America/St_Johns",
                AMERICA_ST_KITTS = "America/St_Kitts",
                AMERICA_ST_LUCIA = "America/St_Lucia",
                AMERICA_ST_THOMAS = "America/St_Thomas",
                AMERICA_ST_VINCENT = "America/St_Vincent",
                AMERICA_SWIFT_CURRENT = "America/Swift_Current",
                AMERICA_TEGUCIGALPA = "America/Tegucigalpa",
                AMERICA_THULE = "America/Thule",
                AMERICA_THUNDER_BAY = "America/Thunder_Bay",
                AMERICA_TIJUANA = "America/Tijuana",
                AMERICA_TORONTO = "America/Toronto",
                AMERICA_TORTOLA = "America/Tortola",
                AMERICA_VANCOUVER = "America/Vancouver",
                AMERICA_VIRGIN = "America/Virgin",
                AMERICA_WHITEHORSE = "America/Whitehorse",
                AMERICA_WINNIPEG = "America/Winnipeg",
                AMERICA_YAKUTAT = "America/Yakutat",
                AMERICA_YELLOWKNIFE = "America/Yellowknife",
                ANTARCTICA_CASEY = "Antarctica/Casey",
                ANTARCTICA_DAVIS = "Antarctica/Davis",
                ANTARCTICA_DUMONTDURVILLE = "Antarctica/DumontDUrville",
                ANTARCTICA_MAWSON = "Antarctica/Mawson",
                ANTARCTICA_MCMURDO = "Antarctica/McMurdo",
                ANTARCTICA_PALMER = "Antarctica/Palmer",
                ANTARCTICA_ROTHERA = "Antarctica/Rothera",
                ANTARCTICA_SOUTH_POLE = "Antarctica/South_Pole",
                ANTARCTICA_SYOWA = "Antarctica/Syowa",
                ANTARCTICA_VOSTOK = "Antarctica/Vostok",
                ARCTIC_LONGYEARBYEN = "Arctic/Longyearbyen",
                ART = "ART",
                ASIA_ADEN = "Asia/Aden",
                ASIA_ALMATY = "Asia/Almaty",
                ASIA_AMMAN = "Asia/Amman",
                ASIA_ANADYR = "Asia/Anadyr",
                ASIA_AQTAU = "Asia/Aqtau",
                ASIA_AQTOBE = "Asia/Aqtobe",
                ASIA_ASHGABAT = "Asia/Ashgabat",
                ASIA_ASHKHABAD = "Asia/Ashkhabad",
                ASIA_BAGHDAD = "Asia/Baghdad",
                ASIA_BAHRAIN = "Asia/Bahrain",
                ASIA_BAKU = "Asia/Baku",
                ASIA_BANGKOK = "Asia/Bangkok",
                ASIA_BEIRUT = "Asia/Beirut",
                ASIA_BISHKEK = "Asia/Bishkek",
                ASIA_BRUNEI = "Asia/Brunei",
                ASIA_CALCUTTA = "Asia/Calcutta",
                ASIA_CHOIBALSAN = "Asia/Choibalsan",
                ASIA_CHONGQING = "Asia/Chongqing",
                ASIA_CHUNGKING = "Asia/Chungking",
                ASIA_COLOMBO = "Asia/Colombo",
                ASIA_DACCA = "Asia/Dacca",
                ASIA_DAMASCUS = "Asia/Damascus",
                ASIA_DHAKA = "Asia/Dhaka",
                ASIA_DILI = "Asia/Dili",
                ASIA_DUBAI = "Asia/Dubai",
                ASIA_DUSHANBE = "Asia/Dushanbe",
                ASIA_GAZA = "Asia/Gaza",
                ASIA_HARBIN = "Asia/Harbin",
                ASIA_HONG_KONG = "Asia/Hong_Kong",
                ASIA_HOVD = "Asia/Hovd",
                ASIA_IRKUTSK = "Asia/Irkutsk",
                ASIA_ISTANBUL = "Asia/Istanbul",
                ASIA_JAKARTA = "Asia/Jakarta",
                ASIA_JAYAPURA = "Asia/Jayapura",
                ASIA_JERUSALEM = "Asia/Jerusalem",
                ASIA_KABUL = "Asia/Kabul",
                ASIA_KAMCHATKA = "Asia/Kamchatka",
                ASIA_KARACHI = "Asia/Karachi",
                ASIA_KASHGAR = "Asia/Kashgar",
                ASIA_KATMANDU = "Asia/Katmandu",
                ASIA_KRASNOYARSK = "Asia/Krasnoyarsk",
                ASIA_KUALA_LUMPUR = "Asia/Kuala_Lumpur",
                ASIA_KUCHING = "Asia/Kuching",
                ASIA_KUWAIT = "Asia/Kuwait",
                ASIA_MACAO = "Asia/Macao",
                ASIA_MACAU = "Asia/Macau",
                ASIA_MAGADAN = "Asia/Magadan",
                ASIA_MAKASSAR = "Asia/Makassar",
                ASIA_MANILA = "Asia/Manila",
                ASIA_MUSCAT = "Asia/Muscat",
                ASIA_NICOSIA = "Asia/Nicosia",
                ASIA_NOVOSIBIRSK = "Asia/Novosibirsk",
                ASIA_OMSK = "Asia/Omsk",
                ASIA_ORAL = "Asia/Oral",
                ASIA_PHNOM_PENH = "Asia/Phnom_Penh",
                ASIA_PONTIANAK = "Asia/Pontianak",
                ASIA_PYONGYANG = "Asia/Pyongyang",
                ASIA_QATAR = "Asia/Qatar",
                ASIA_QYZYLORDA = "Asia/Qyzylorda",
                ASIA_RANGOON = "Asia/Rangoon",
                ASIA_RIYADH = "Asia/Riyadh",
                ASIA_RIYADH87 = "Asia/Riyadh87",
                ASIA_RIYADH88 = "Asia/Riyadh88",
                ASIA_RIYADH89 = "Asia/Riyadh89",
                ASIA_SAIGON = "Asia/Saigon",
                ASIA_SAKHALIN = "Asia/Sakhalin",
                ASIA_SAMARKAND = "Asia/Samarkand",
                ASIA_SEOUL = "Asia/Seoul",
                ASIA_SHANGHAI = "Asia/Shanghai",
                ASIA_SINGAPORE = "Asia/Singapore",
                ASIA_TAIPEI = "Asia/Taipei",
                ASIA_TASHKENT = "Asia/Tashkent",
                ASIA_TBILISI = "Asia/Tbilisi",
                ASIA_TEHRAN = "Asia/Tehran",
                ASIA_TEL_AVIV = "Asia/Tel_Aviv",
                ASIA_THIMBU = "Asia/Thimbu",
                ASIA_THIMPHU = "Asia/Thimphu",
                ASIA_TOKYO = "Asia/Tokyo",
                ASIA_UJUNG_PANDANG = "Asia/Ujung_Pandang",
                ASIA_ULAANBAATAR = "Asia/Ulaanbaatar",
                ASIA_ULAN_BATOR = "Asia/Ulan_Bator",
                ASIA_URUMQI = "Asia/Urumqi",
                ASIA_VIENTIANE = "Asia/Vientiane",
                ASIA_VLADIVOSTOK = "Asia/Vladivostok",
                ASIA_YAKUTSK = "Asia/Yakutsk",
                ASIA_YEKATERINBURG = "Asia/Yekaterinburg",
                ASIA_YEREVAN = "Asia/Yerevan",
                AST = "AST",
                ATLANTIC_AZORES = "Atlantic/Azores",
                ATLANTIC_BERMUDA = "Atlantic/Bermuda",
                ATLANTIC_CANARY = "Atlantic/Canary",
                ATLANTIC_CAPE_VERDE = "Atlantic/Cape_Verde",
                ATLANTIC_FAEROE = "Atlantic/Faeroe",
                ATLANTIC_FAROE = "Atlantic/Faroe",
                ATLANTIC_JAN_MAYEN = "Atlantic/Jan_Mayen",
                ATLANTIC_MADEIRA = "Atlantic/Madeira",
                ATLANTIC_REYKJAVIK = "Atlantic/Reykjavik",
                ATLANTIC_SOUTH_GEORGIA = "Atlantic/South_Georgia",
                ATLANTIC_ST_HELENA = "Atlantic/St_Helena",
                ATLANTIC_STANLEY = "Atlantic/Stanley",
                AUSTRALIA_ACT = "Australia/ACT",
                AUSTRALIA_ADELAIDE = "Australia/Adelaide",
                AUSTRALIA_BRISBANE = "Australia/Brisbane",
                AUSTRALIA_BROKEN_HILL = "Australia/Broken_Hill",
                AUSTRALIA_CANBERRA = "Australia/Canberra",
                AUSTRALIA_CURRIE = "Australia/Currie",
                AUSTRALIA_DARWIN = "Australia/Darwin",
                AUSTRALIA_EUCLA = "Australia/Eucla",
                AUSTRALIA_HOBART = "Australia/Hobart",
                AUSTRALIA_LHI = "Australia/LHI",
                AUSTRALIA_LINDEMAN = "Australia/Lindeman",
                AUSTRALIA_LORD_HOWE = "Australia/Lord_Howe",
                AUSTRALIA_MELBOURNE = "Australia/Melbourne",
                AUSTRALIA_NORTH = "Australia/North",
                AUSTRALIA_NSW = "Australia/NSW",
                AUSTRALIA_PERTH = "Australia/Perth",
                AUSTRALIA_QUEENSLAND = "Australia/Queensland",
                AUSTRALIA_SOUTH = "Australia/South",
                AUSTRALIA_SYDNEY = "Australia/Sydney",
                AUSTRALIA_TASMANIA = "Australia/Tasmania",
                AUSTRALIA_VICTORIA = "Australia/Victoria",
                AUSTRALIA_WEST = "Australia/West",
                AUSTRALIA_YANCOWINNA = "Australia/Yancowinna",
                BET = "BET",
                BRAZIL_ACRE = "Brazil/Acre",
                BRAZIL_DENORONHA = "Brazil/DeNoronha",
                BRAZIL_EAST = "Brazil/East",
                BRAZIL_WEST = "Brazil/West",
                BST = "BST",
                CANADA_ATLANTIC = "Canada/Atlantic",
                CANADA_CENTRAL = "Canada/Central",
                CANADA_EASTERN = "Canada/Eastern",
                CANADA_EAST_MSASKATCHEWAN = "Canada/East-Saskatchewan",
                CANADA_MOUNTAIN = "Canada/Mountain",
                CANADA_NEWFOUNDLAND = "Canada/Newfoundland",
                CANADA_PACIFIC = "Canada/Pacific",
                CANADA_SASKATCHEWAN = "Canada/Saskatchewan",
                CANADA_YUKON = "Canada/Yukon",
                CAT = "CAT",
                CET = "CET",
                CHILE_CONTINENTAL = "Chile/Continental",
                CHILE_EASTERISLAND = "Chile/EasterIsland",
                CNT = "CNT",
                CST = "CST",
                CST6CDT = "CST6CDT",
                CTT = "CTT",
                CUBA = "Cuba",
                EAT = "EAT",
                ECT = "ECT",
                EET = "EET",
                EGYPT = "Egypt",
                EIRE = "Eire",
                EST = "EST",
                EST5EDT = "EST5EDT",
                ETC_GMT = "Etc/GMT",
                ETC_GMT_P0 = "Etc/GMT+0",
                ETC_GMT_P1 = "Etc/GMT+1",
                ETC_GMT_P10 = "Etc/GMT+10",
                ETC_GMT_P11 = "Etc/GMT+11",
                ETC_GMT_P2 = "Etc/GMT+2",
                ETC_GMT_P3 = "Etc/GMT+3",
                ETC_GMT_P4 = "Etc/GMT+4",
                ETC_GMT_P5 = "Etc/GMT+5",
                ETC_GMT_P6 = "Etc/GMT+6",
                ETC_GMT_P7 = "Etc/GMT+7",
                ETC_GMT_P8 = "Etc/GMT+8",
                ETC_GMT_P9 = "Etc/GMT+9",
                ETC_GMT0 = "Etc/GMT0",
                ETC_GMT_M0 = "Etc/GMT-0",
                ETC_GMT_M1 = "Etc/GMT-1",
                ETC_GMT_M10 = "Etc/GMT-10",
                ETC_GMT_M11 = "Etc/GMT-11",
                ETC_GMT_M12 = "Etc/GMT-12",
                ETC_GMT_M13 = "Etc/GMT-13",
                ETC_GMT_M14 = "Etc/GMT-14",
                ETC_GMT_M2 = "Etc/GMT-2",
                ETC_GMT_M3 = "Etc/GMT-3",
                ETC_GMT_M4 = "Etc/GMT-4",
                ETC_GMT_M5 = "Etc/GMT-5",
                ETC_GMT_M6 = "Etc/GMT-6",
                ETC_GMT_M7 = "Etc/GMT-7",
                ETC_GMT_M8 = "Etc/GMT-8",
                ETC_GMT_M9 = "Etc/GMT-9",
                ETC_GREENWICH = "Etc/Greenwich",
                ETC_UCT = "Etc/UCT",
                ETC_UNIVERSAL = "Etc/Universal",
                ETC_UTC = "Etc/UTC",
                ETC_ZULU = "Etc/Zulu",
                EUROPE_AMSTERDAM = "Europe/Amsterdam",
                EUROPE_ANDORRA = "Europe/Andorra",
                EUROPE_ATHENS = "Europe/Athens",
                EUROPE_BELFAST = "Europe/Belfast",
                EUROPE_BELGRADE = "Europe/Belgrade",
                EUROPE_BERLIN = "Europe/Berlin",
                EUROPE_BRATISLAVA = "Europe/Bratislava",
                EUROPE_BRUSSELS = "Europe/Brussels",
                EUROPE_BUCHAREST = "Europe/Bucharest",
                EUROPE_BUDAPEST = "Europe/Budapest",
                EUROPE_CHISINAU = "Europe/Chisinau",
                EUROPE_COPENHAGEN = "Europe/Copenhagen",
                EUROPE_DUBLIN = "Europe/Dublin",
                EUROPE_GIBRALTAR = "Europe/Gibraltar",
                EUROPE_GUERNSEY = "Europe/Guernsey",
                EUROPE_HELSINKI = "Europe/Helsinki",
                EUROPE_ISLE_OF_MAN = "Europe/Isle_of_Man",
                EUROPE_ISTANBUL = "Europe/Istanbul",
                EUROPE_JERSEY = "Europe/Jersey",
                EUROPE_KALININGRAD = "Europe/Kaliningrad",
                EUROPE_KIEV = "Europe/Kiev",
                EUROPE_LISBON = "Europe/Lisbon",
                EUROPE_LJUBLJANA = "Europe/Ljubljana",
                EUROPE_LONDON = "Europe/London",
                EUROPE_LUXEMBOURG = "Europe/Luxembourg",
                EUROPE_MADRID = "Europe/Madrid",
                EUROPE_MALTA = "Europe/Malta",
                EUROPE_MARIEHAMN = "Europe/Mariehamn",
                EUROPE_MINSK = "Europe/Minsk",
                EUROPE_MONACO = "Europe/Monaco",
                EUROPE_MOSCOW = "Europe/Moscow",
                EUROPE_NICOSIA = "Europe/Nicosia",
                EUROPE_OSLO = "Europe/Oslo",
                EUROPE_PARIS = "Europe/Paris",
                EUROPE_PODGORICA = "Europe/Podgorica",
                EUROPE_PRAGUE = "Europe/Prague",
                EUROPE_RIGA = "Europe/Riga",
                EUROPE_ROME = "Europe/Rome",
                EUROPE_SAMARA = "Europe/Samara",
                EUROPE_SAN_MARINO = "Europe/San_Marino",
                EUROPE_SARAJEVO = "Europe/Sarajevo",
                EUROPE_SIMFEROPOL = "Europe/Simferopol",
                EUROPE_SKOPJE = "Europe/Skopje",
                EUROPE_SOFIA = "Europe/Sofia",
                EUROPE_STOCKHOLM = "Europe/Stockholm",
                EUROPE_TALLINN = "Europe/Tallinn",
                EUROPE_TIRANE = "Europe/Tirane",
                EUROPE_TIRASPOL = "Europe/Tiraspol",
                EUROPE_UZHGOROD = "Europe/Uzhgorod",
                EUROPE_VADUZ = "Europe/Vaduz",
                EUROPE_VATICAN = "Europe/Vatican",
                EUROPE_VIENNA = "Europe/Vienna",
                EUROPE_VILNIUS = "Europe/Vilnius",
                EUROPE_VOLGOGRAD = "Europe/Volgograd",
                EUROPE_WARSAW = "Europe/Warsaw",
                EUROPE_ZAGREB = "Europe/Zagreb",
                EUROPE_ZAPOROZHYE = "Europe/Zaporozhye",
                EUROPE_ZURICH = "Europe/Zurich",
                GB = "GB",
                GB_MEIRE = "GB-Eire",
                GMT = "GMT",
                GMT0 = "GMT0",
                GREENWICH = "Greenwich",
                HONGKONG = "Hongkong",
                HST = "HST",
                ICELAND = "Iceland",
                IET = "IET",
                INDIAN_ANTANANARIVO = "Indian/Antananarivo",
                INDIAN_CHAGOS = "Indian/Chagos",
                INDIAN_CHRISTMAS = "Indian/Christmas",
                INDIAN_COCOS = "Indian/Cocos",
                INDIAN_COMORO = "Indian/Comoro",
                INDIAN_KERGUELEN = "Indian/Kerguelen",
                INDIAN_MAHE = "Indian/Mahe",
                INDIAN_MALDIVES = "Indian/Maldives",
                INDIAN_MAURITIUS = "Indian/Mauritius",
                INDIAN_MAYOTTE = "Indian/Mayotte",
                INDIAN_REUNION = "Indian/Reunion",
                IRAN = "Iran",
                ISRAEL = "Israel",
                IST = "IST",
                JAMAICA = "Jamaica",
                JAPAN = "Japan",
                JST = "JST",
                KWAJALEIN = "Kwajalein",
                LIBYA = "Libya",
                MET = "MET",
                MEXICO_BAJANORTE = "Mexico/BajaNorte",
                MEXICO_BAJASUR = "Mexico/BajaSur",
                MEXICO_GENERAL = "Mexico/General",
                MIDEAST_RIYADH87 = "Mideast/Riyadh87",
                MIDEAST_RIYADH88 = "Mideast/Riyadh88",
                MIDEAST_RIYADH89 = "Mideast/Riyadh89",
                MIT = "MIT",
                MST = "MST",
                MST7MDT = "MST7MDT",
                NAVAJO = "Navajo",
                NET = "NET",
                NST = "NST",
                NZ = "NZ",
                NZ_MCHAT = "NZ-CHAT",
                PACIFIC_APIA = "Pacific/Apia",
                PACIFIC_AUCKLAND = "Pacific/Auckland",
                PACIFIC_CHATHAM = "Pacific/Chatham",
                PACIFIC_EASTER = "Pacific/Easter",
                PACIFIC_EFATE = "Pacific/Efate",
                PACIFIC_ENDERBURY = "Pacific/Enderbury",
                PACIFIC_FAKAOFO = "Pacific/Fakaofo",
                PACIFIC_FIJI = "Pacific/Fiji",
                PACIFIC_FUNAFUTI = "Pacific/Funafuti",
                PACIFIC_GALAPAGOS = "Pacific/Galapagos",
                PACIFIC_GAMBIER = "Pacific/Gambier",
                PACIFIC_GUADALCANAL = "Pacific/Guadalcanal",
                PACIFIC_GUAM = "Pacific/Guam",
                PACIFIC_HONOLULU = "Pacific/Honolulu",
                PACIFIC_JOHNSTON = "Pacific/Johnston",
                PACIFIC_KIRITIMATI = "Pacific/Kiritimati",
                PACIFIC_KOSRAE = "Pacific/Kosrae",
                PACIFIC_KWAJALEIN = "Pacific/Kwajalein",
                PACIFIC_MAJURO = "Pacific/Majuro",
                PACIFIC_MARQUESAS = "Pacific/Marquesas",
                PACIFIC_MIDWAY = "Pacific/Midway",
                PACIFIC_NAURU = "Pacific/Nauru",
                PACIFIC_NIUE = "Pacific/Niue",
                PACIFIC_NORFOLK = "Pacific/Norfolk",
                PACIFIC_NOUMEA = "Pacific/Noumea",
                PACIFIC_PAGO_PAGO = "Pacific/Pago_Pago",
                PACIFIC_PALAU = "Pacific/Palau",
                PACIFIC_PITCAIRN = "Pacific/Pitcairn",
                PACIFIC_PONAPE = "Pacific/Ponape",
                PACIFIC_PORT_MORESBY = "Pacific/Port_Moresby",
                PACIFIC_RAROTONGA = "Pacific/Rarotonga",
                PACIFIC_SAIPAN = "Pacific/Saipan",
                PACIFIC_SAMOA = "Pacific/Samoa",
                PACIFIC_TAHITI = "Pacific/Tahiti",
                PACIFIC_TARAWA = "Pacific/Tarawa",
                PACIFIC_TONGATAPU = "Pacific/Tongatapu",
                PACIFIC_TRUK = "Pacific/Truk",
                PACIFIC_WAKE = "Pacific/Wake",
                PACIFIC_WALLIS = "Pacific/Wallis",
                PACIFIC_YAP = "Pacific/Yap",
                PLT = "PLT",
                PNT = "PNT",
                POLAND = "Poland",
                PORTUGAL = "Portugal",
                PRC = "PRC",
                PRT = "PRT",
                PST = "PST",
                PST8PDT = "PST8PDT",
                ROK = "ROK",
                SINGAPORE = "Singapore",
                SST = "SST",
                TURKEY = "Turkey",
                UCT = "UCT",
                UNIVERSAL = "Universal",
                US_ALASKA = "US/Alaska",
                US_ALEUTIAN = "US/Aleutian",
                US_ARIZONA = "US/Arizona",
                US_CENTRAL = "US/Central",
                US_EASTERN = "US/Eastern",
                US_EAST_MINDIANA = "US/East-Indiana",
                US_HAWAII = "US/Hawaii",
                US_INDIANA_MSTARKE = "US/Indiana-Starke",
                US_MICHIGAN = "US/Michigan",
                US_MOUNTAIN = "US/Mountain",
                US_PACIFIC = "US/Pacific",
                US_PACIFIC_MNEW = "US/Pacific-New",
                US_SAMOA = "US/Samoa",
                UTC = "UTC",
                VST = "VST",
                WET = "WET",
                W_MSU = "W-SU",
                ZULU = "Zulu";
    }


    public interface LOCALE_CD {
        public static final String
                EN_US = "en_US",
                EN_GB = "en_GB",
                FR_CA = "fr_CA",
                FR_FR = "fr_FR",
                EN_CA = "en_CA",
                ES_MX = "es_MX",
                ES_US = "es_US",
                NL_NL = "nl_NL", //Netherlands - Dutch
                NL_BE = "nl_BE",
                RU_RU = "ru_RU",
                TR_TR = "tr_TR",
                JA_JA = "ja_JP", //japan
                IT_IT = "it_IT", //italy
                ZH_CN = "zh_CN", //chinese (simplified)
                ES_ES = "es_ES", //spain
                es_CL = "es_CL",//Spanish Chilie
                DE_DE = "de_DE",//Germany German;
                DE_CH = "de_CH", //German Swiss;
                FR_CH = "fr_CH", //french Swiss...for currency codes
                EN_PH = "en_PH", //english Philippines
                PT_BR = "pt_BR", //portuguese brazil
                EN_AU = "en_AU", //english Australia
                EL_GR = "el_GR",  //greek Greece
                HU_HU = "hu_HU", //Hungarian Hungary
                PL_PL = "pl_PL", //Polish Poland
                en_IN = "en_IN", //English India
                XX_PIGLATIN = "xx-piglatin", //piglatin
                EN_XXPO = "en_XXPO", //pollock custom locale
                TS_US = "ts_US"; //test locale
    }


    public interface EMAIL_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }

    public interface PHONE_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }

    public interface PHONE_SHORT_DESC_CD {
        public static final String PRIMARY_PHONE = "Primary Phone";
        public static final String PRIMARY_FAX = "Primary Fax";
    }

    public interface ADDRESS_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }

    public interface TEMPLATE_PROPERTY_TYPE_CD {
        public static final String LOCALE = "LOCALE";
        public static final String SUBJECT = "SUBJECT";
        public static final String EMAIL_TYPE = "EMAIL_TYPE";
        public static final String EMAIL_OBJECT = "EMAIL_OBJECT";
    }

    public interface TEMPLATE_TYPE_CD {
        public static final String EMAIL = "EMAIL";
    }

    public interface CONTENT_TYPE_CD {
        public static final String IMAGE = "Image";
    }

    public interface CONTENT_USAGE_CD {
        public static final String LOGO_IMAGE = "LogoImage";
    }

    public interface CONTENT_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
    }

    public interface APPLICATION_WO_FUNCTIONS {
        public static final String STORE_MGR_TAB_EMAIL_TEMPLATES = "Store Mgr Email Templates Tab";
    }

    public interface APPLICATION_FUNCTIONS {
    	public static final String 
	    	//ADMIN MENU	
	    	FREIGHT_TABLE_ADMINISTRATION = "Access to Freight Tables",
	    	DISCOUNT_ADMINISTRATION = "Access to Discount Tables",
	    	ADMIN2_MGR_TAB_SITE = "Admin 2.0 Mgr Site Tab",
	    	ADMIN2_MGR_TAB_USER = "Admin 2.0 Mgr User Tab",
	    	ADMIN2_MGR_TAB_ACCOUNT = "Admin 2.0 Mgr Account Tab",
	    	ADMIN2_MGR_TAB_LOADER = "Admin 2.0 Mgr Loader Tab",
	    	ADMIN2_MGR_SITE_LOADER = "Admin 2.0 Mgr Site Loader Page",
	    	ADMIN2_MGR_BUDGET_LOADER = "Admin 2.0 Mgr Budget Loader Page",
	    	ADMIN2_MGR_SHOPPING_CONTROL_LOADER = "Admin 2.0 Mgr Shopping Control Loader Page",
	    	ADMIN2_MGR_TAB_PROFILE = "Admin 2.0 Mgr Profile Tab",	
	    	UI_MGR_TAB_GROUP = "Ui Mgr Group Tab",
	    	UI_MGR_TAB_SITE = "UI Mgr Site Tab",
	    	UI_MGR_TAB_USER = "Ui Mgr User Tab",
	    	UI_MGR_TAB_ACCOUNT = "Ui Mgr Account Tab",	
	    	MANTA_STORE_MGR_TAB_ACCOUNT = "New Admin - Accounts",
	    	MANTA_STORE_MGR_TAB_DISTRIBUTOR = "New Admin - Distributor",
	    	MANTA_STORE_MGR_TAB_SITE = "New Admin - Locations",
	    	MANTA_STORE_MGR_TAB_MANUFACTURER = "New Admin - Manufacturers",
	    	MANTA_STORE_MGR_TAB_MESSAGES = "New Admin - Messages",
	    	MANTA_STORE_MGR_TAB_USERS = "New Admin - Users",
	    	MANTA_STORE_MGR_TAB_GROUPS = "New Admin - Groups",
	    	MANTA_STORE_MGR_TAB_SETUP = "New Admin - Setup",
            MANTA_STORE_MGR_TAB_EMAIL_TEMPLATES = "New Admin - Email Templates",
            MANTA_STORE_MGR_TAB_ORDERS = "New Admin - Orders",
            
	    	MANTA_STORE_MGR_TAB_CATALOGS = "New Admin - Catalogs",
            MANTA_STORE_MGR_TAB_CATEGORIES = "New Admin - Categories",
	    	MANTA_STORE_MGR_TAB_COST_CENTERS = "New Admin - Cost Centers",
	    	MANTA_STORE_MGR_TAB_MASTER_ITEMS = "New Admin - Master Items",
	    	MANTA_STORE_MGR_TAB_ITEM_LOADER = "New Admin - Item Loader",
	    	MANTA_STORE_MGR_TAB_CORPORATE_SCHED = "New Admin - Corporate Schedules",
	    	MANTA_STORE_MGR_TAB_PROFILE = "New Admin - Profile",
	    	MANTA_STORE_MGR_TAB_BATCH_ORDERS = "New Admin - Batch Orders",
	    	
            STORE_MGR_TAB_ACCOUNT = "Store Mgr Account Tab",
	    	STORE_MGR_TAB_MESSAGES = "Store Mgr Messages Tab",
	    	STORE_MGR_TAB_EMAIL_TEMPLATES = "Store Mgr Email Templates Tab",
	    	STORE_MGR_TAB_USERS = "Store Mgr User Tab",
	    	STORE_MGR_TAB_SITE = "Store Mgr Site Tab",
	    	STORE_MGR_TAB_GROUP = "Store Mgr Group Tab",
	    	STORE_MGR_TAB_MANUFACTURER = "Store Mgr Manufacturer Tab",
	    	STORE_MGR_TAB_SETUP = "Store Mgr Setup Tab",
	    	STORE_MGR_TAB_DISTRIBUTOR = "Store Mgr Distributor Tab",
	    	STORE_MGR_TAB_CMS = "New Admin - Content Management",
	    	STORE_MGR_TAB_COST_CENTERS = "Store Mgr Cost Centers Tab",
	    		    	
	    	//CUSTOMER ACCESS
	    	NEW_UI_ACCESS = "Access to New UI",
	    	ACCESS_ASSETS = "Access to Assets",
	    	ACCESS_SERVICES = "Access to Services",
	    	ACCESS_SHOPPING = "Access to Shopping",
	    	ACCESS_CONTENT = "Access to Content",
			ACCESS_DASHBOARD = "Access to Dashboard",
            ACCESS_ORDERS = "Access to Orders",
	    	RUNTIME_INVOICE_TAB = "Runtime Invoice Tab",

	    	//SHOPPING
	    	EDIT_SITE_PAR_VALUES = "Edit Site Par Values",
	    	VIEW_SITE_PAR_VALUES = "View Site Par Values",
	    	EDIT_SITE_SHOPPING_CONTROLS = "Edit Shopping Controls",
	    	VIEW_SHOPPING_CONTROLS = "View Shopping Controls",
	    	EDIT_PROFILING = "Edit Profiling",
	    	ADD_CUSTOMER_ORDER_NOTES = "Add Customer Order Notes",
	    	ADD_SHIPPING_ORDER_COMMENTS = "Add Order Shipping Comments",
	    	VIEW_PARTIAL_ORD_CREDIT_CARD = "View Partial Ord Credit Card",
	    	MODIFY_ORD_CREDIT_CARD = "Modify Credit Card For Order",
	    	PO_MANIFESTING = "PO Manifesting Sub System",
	    	PO_ADDRESS_PRINTING = "PO Address Printing",
	    	PLACE_CONFIRMATION_ONLY_ORDER = "Place Confirmation Only Order",
	    	EXCLUDE_ORDER_FROM_BUDGET = "Exclude Order From Budget",
	    	VIEW_INVOICES = "View invoice in runtime pages",
	    	RUN_SPENDING_ESTIMATOR = "Run Spending Estimator",
	    	RECEIVING = "Receiving",
	    	ADD_RE_SALE_ITEMS = "Add Re Sale items at checkout",
	    	DISPLAY_COST_CENTER_DETAIL = "Display Cost Center Details",
	    	EDIT_USER_PROFILE_NAME = "Edit User Profile Name",
	    	PLACE_ORDER_REQUEST_SHIP_DATE = "Place Order Request Ship Date",
	    	PLACE_ORDER_MANDATORY_REQUEST_SHIP_DATE = "Place Order MANDATORY Request Ship Date",
	    	PLACE_ORDER_PROCESS_ORDER_ON = "Place Order Process Order On",
	    	OVERRIDE_SHOPPING_RESTRICTION = "Override Shopping Restriction",
	    	MODIFY_ORDER_ITEM_QTY = "Modify Order Item Qty",
	    	UPDATE_AUTO_DISTRO = "Update Auto Distro",
	    	VIEW_SPECIAL_ITEMS = "View Special Items",
	    	BYPASS_SMALL_ORDER_ROUTING = "Bypass small order routing",
	    	BYPASS_CUSTOMER_WORKFLOW = "Bypass customer workflow",
	    	CUST_REQ_RESHIPMENT_ORDER_NUM ="Cust Req Reshipment Order Num",
	    	TRACKING_MAINTENANCE = "Tracking Maintenance",
	    	CHANGE_ORDER_BUDGET_PERIOD = "Change Order Budget Period",
	    	EDIT_MESSAGES = "Edit Messages",

	    	//XPEDX
	    	SHOP_ACCESS = "Access to Shop Pages",
	    	AUTO_DISTRO_ACCESS = "Access to Auto Distro Pages",
	    	TRACK_ORDER_ACCESS = "Access to Track Order Pages",
	    	REPORTS_ACCESS = "Access to Reports Pages",
	    	USER_PROFILE_ACCESS = "Access to User Profile Pages",
	    	STORE_PROFILE_ACCESS = "Access to Store Profile Pages",
	    	PRODUCT_INFORMATION_ACCESS = "Access to Product Info Pages",
	    	MSDS_ACCESS = "Access to MSDS Pages",
	    	FAQ_ACCESS = "Access to FAQs Pages",
	    	CONTACT_US_ACCESS = "Access to Contact Us Pages",
	    	MAINTENANCE_ACCESS = "Access to Maintenance Pages",
	    	MAINTENANCE_NEWS = "Access to News Maint. Page",
	    	MAINTENANCE_TEMPLATE = "Access to Template Maint. Page",
	    	MAINTENANCE_FAQ = "Access to FAQ Maintenance Page",
	    	APPROVE_ORDERS_ACCESS = "Access to Approve Orders",
	    	CHANGE_LOCATION_ACCESS = "Access to Change Location",

	    	//SERVICES/ASSETS
	    	ASSET_WO_VIEW_ALL_FOR_STORE = "Asset WO - View All for Store",
	    	ASSET_ADMINISTRATOR ="Asset Administrator",
	    	ASSET_USER ="Asset User",
	    	WORK_ORDER_APPROVER = "Work Order Approver",

	    	//OTHER	        
	    	CRC_MANAGER = "CRC Manager",
	    	SPECIAL_PERMISSION_ITEMS = "Special Permission Items",
	    	INVENTORY_EARLY_RELEASE ="Inventory Early Release",
	    	ACCESS_HISTORY = "Access to History Records"; 
    }
    
    public interface APPLICATION_FUNCTIONS_TYPE {
        public static final String ADMIN_MENU = "ADMIN MENU";
        public static final String CUSTOMER_ACCESS = "CUSTOMER ACCESS";
        public static final String SHOPPING = "SHOPPING";
        public static final String XPEDX = "XPEDX";
        public static final String SERVICES_ASSETS = "SERVICES/ASSETS";
        public static final String OTHER = "OTHER";        
    }

    public interface LOCATION_PRODUCT_BUNDLE {
        public static final String CATALOG = "CATALOG";
        public static final String PRICE_LIST = "PRICE_LIST";
        public static final String ORDER_GUIDE = "ORDER_GUIDE";
    }

    public interface SCHEDULE_DETAIL_CD {
        public static final String
                ELEMENT = "ELEMENT",
                WEEK_DAY = "WEEK_DAY",
                MONTH_DAY = "MONTH_DAY",
                MONTH_WEEK = "MONTH_WEEK",
                ALSO_DATE = "ALSO_DATE",
                EXCEPT_DATE = "EXCEPT_DATE",
                HOLIDAY = "HOLIDAY",
                CUTOFF_DAY = "CUTOFF_DAY",
                CUTOFF_TIME = "CUTOFF_TIME",
                ACCOUNT_ID = "ACCOUNT_ID",
                SITE_ID = "SITE_ID",
                ZIP_CODE = "ZIP_CODE",
                INV_CART_ACCESS_INTERVAL="INV_CART_ACCESS_INTERVAL",
                PHYSICAL_INV_START_DATE = "PHYSICAL_INV_START_DATE",
                PHYSICAL_INV_END_DATE = "PHYSICAL_INV_END_DATE",
                PHYSICAL_INV_FINAL_DATE = "PHYSICAL_INV_FINAL_DATE";
    }

    public interface SCHEDULE_RULE_CD {
        public static final String
                WEEK = "WEEK",
                DAY_MONTH = "DAY_MONTH",
                WEEK_MONTH = "WEEK_MONTH",
                DATE_LIST = "DATE_LIST";
    }


    public interface SCHEDULE_TYPE_CD {
        public static final String
                DELIVERY = "DELIVERY",
                CORPORATE = "CORPORATE";
    }


    public interface BUDGET_TYPE_CD {
        public static final String
                SITE_BUDGET = "SITE BUDGET",
                ACCOUNT_BUDGET = "ACCOUNT BUDGET";
    }

    public interface BUDGET_STATUS_CD {
        public static final String
                ACTIVE = "ACTIVE",
                INACTIVE = "INACTIVE";
    }

    public interface COST_CENTER_STATUS_CD {
        public static final String
                ACTIVE = "ACTIVE",
                INACTIVE = "INACTIVE";
    }
    
    public interface CATALOG_ASSOC_CD {
        public static final String
                CATALOG_STORE ="CATALOG_STORE",
                CATALOG_ACCOUNT ="CATALOG_ACCOUNT",
                CATALOG_SITE ="CATALOG_SITE";
    }

    public interface CATALOG_TYPE_CD {
        public static final String STORE = "STORE";
        public static final String ACCOUNT = "ACCOUNT";
        public static final String SHOPPING = "SHOPPING";
        public static final String SYSTEM = "SYSTEM";
    }

    public interface CATALOG_STATUS_CD {
        public static final String
                ACTIVE = "ACTIVE",
                INACTIVE = "INACTIVE",
                LIVE = "LIMITED",
                LIMITED = "LIMITED";
    }
    public interface CATALOG_STRUCTURE_CD {
        public static final String
                CATALOG_PRODUCT ="CATALOG_PRODUCT",
                CATALOG_MULTI_PRODUCT = "CATALOG_MULTI_PRODUCT",
                CATALOG_CATEGORY ="CATALOG_CATEGORY",
                CATALOG_MAJOR_CATEGORY ="CATALOG_MAJOR_CATEGORY",
                CATALOG_SERVICE ="CATALOG_SERVICE" ;
    }

    public interface COST_CENTER_ASSOC_CD {
        public static final String COST_CENTER_ACCOUNT_CATALOG = "COST_CENTER_ACCOUNT_CATALOG";

    }

    public interface ADDRESS_COUNTRY_CD {
        public static final String UNITED_STATES = "UNITED STATES";
        public static final String UNITED_KINGDOM = "UNITED KINGDOM";
        public static final String CANADA = "CANADA";
        public static final String JAPAN = "JAPAN";
        public static final String NETHERLANDS = "NETHERLANDS";
        public static final String RUSSIAN_FEDERATION = "RUSSIAN FEDERATION";
        public static final String TURKEY = "TURKEY";
        public static final String COUNTRY_UNKNOWN = "COUNTRY UNKNOWN";
    }


    public interface COST_CENTER_TYPE_CD {
        public static final String
                SITE_BUDGET = "SITE BUDGET",
                ACCOUNT_BUDGET = "ACCOUNT BUDGET";
    }


    public interface COST_CENTER_TAX_TYPE {
        //sales tax should not be
        //alocated in this cost center.  This does not affect the taxability of the
        //item just how it is budgeted for
        public static final String DONT_ALLOCATE_SALES_TAX = "Dont allocate sales tax";
        //All sales tax
        public static final String MASTER_SALES_TAX_COST_CENTER = "Master sales tax cost center";
        //allocate sales tax for
        //items that are ordered that are a member of this cost center into this cost center
        public static final String ALLOCATE_PRODUCT_SALES_TAX = "Allocate Product Sales Tax";
    }

    public interface COST_CENTER_FILTER_ACC_CAT_TYPE {
        public static final String ACCOUNT = "ACCOUNT";
        public static final String CATALOG = "CATALOG";
    }
    /*
    public interface ORDER_STATUS_CD {
        public static final String CANCELLED = "Cancelled";                // Cancelled -- anywhere -- END
        public static final String ORDERED = "Ordered";                   // Order successfully placed
        public static final String INVOICED = "Invoiced";                 // -- END
        public static final String PROCESS_ERP_PO = "Process ERP PO";      // Order sent to Lawson, or order generated to send (JWP)
        public static final String ERP_RELEASED = "ERP Released";          // PO retrieved from Lawson

        public static final String REJECTED = "Rejected";                  // Workflow state -- END
        public static final String SENT_TO_CUST_SYSTEM = "Sent To Customer System";
        public static final String ERP_REJECTED = "ERP Rejected";          // Problem sending order to Lawson
        public static final String ERP_CANCELLED = "ERP Cancelled";        // PO cancelled in Lawson
    }
    */
    public interface ORDER_STATUS_CD {
        public static final String RECEIVED = "Received";                       // Order received, not processed
        public static final String CANCELLED = "Cancelled";                     // Cancelled -- anywhere -- END
        public static final String DUPLICATED = "Duplicated";                   // Duplicated -- anywhere -- END
        public static final String PENDING_APPROVAL = "Pending Approval";       // Workflow state
        public static final String PENDING_DATE = "Pending Date";               // Change to Ordered when date come
        public static final String PENDING_CONSOLIDATION = "Pending Consolidation";
        public static final String REJECTED = "Rejected";                       // Workflow state -- END
        public static final String ORDERED = "Ordered";                         // Order successfully placed
        public static final String PENDING_REVIEW = "Pending Review";           // Problem placing order
        public static final String PENDING_ORDER_REVIEW = "Pending Order Review"; // cw sku
        public static final String INVOICED = "Invoiced";                       // -- END
        public static final String PROCESS_ERP_PO = "Process ERP PO";           // Order sent to Lawson, or order generated to send (JWP)
        public static final String ERP_REJECTED = "ERP Rejected";               // Problem sending order to Lawson
        public static final String ERP_RELEASED = "ERP Released";               // PO retrieved from Lawson
        public static final String ERP_RELEASED_PO_ERROR = "ERP Released PO Error"; // Problem getting PO
        public static final String ERP_CANCELLED = "ERP Cancelled";             // PO cancelled in Lawson
        public static final String SENDING_TO_ERP = "Sending to Erp";           //Order has been selected out of database, but has not gone further (orders should not be in this state for very long (seconds))
        public static final String READY_TO_SEND_TO_CUST_SYS = "Ready To Send To Cust System"; //Means customer is set up for punch out ordering, and order is ready to send to them
        public static final String SENT_TO_CUST_SYSTEM = "Sent To Customer System"; //Means customer is set up for punch out ordering, and order has been sent to them
        public static final String WAITING_FOR_ACTION = "Waiting For Action";   //Means that the order is pending some other action.  Should replace the SENT_TO_CUST_SYSTEM action
        public static final String PRE_PROCESSED = "Pre Processed";             //any order that does not need to go through the full pipeline
        public static final String REFERENCE_ONLY = "REFERENCE ONLY";           //order is not a "real" order, it should not be picked up in UI, or reports

        // Addtional status codes that are not "real".  That is to say an order will
        // display as "Shipped" when all items have been shipped.  This is not a status that is stored in the database.
        // public static final String
        //	ORDERED_PROCESSING="Ordered-Processing",
        // 	SHIPPED="Ordered-Shipped",
        //	SHIPMENT_RECEIVED="Shipment Received",
        //	ON_HOLD="On Hold";
    }

    public interface ORDER_ASSOC_CD {
        public static final String CONSOLIDATED = "CONSOLIDATED";
    }
    
    public interface ORDER_ASSOC_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }

    public interface ORDER_TYPE_CD {
        public static final String CONSOLIDATED = "CONSOLIDATED";
        public static final String TO_BE_CONSOLIDATED = "TO_BE_CONSOLIDATED";
    }

    public interface ORDER_BUDGET_TYPE_CD {
        public static final String NON_APPLICABLE = "NON_APPLICABLE";
    }

    public interface LEDGER_ENTRY_TYPE_CD {
        public static final String PRIOR_PERIOD_BUDGET_ACTUAL = "PRIOR PERIOD BUDGET ACTUAL";
    }
    
    public interface MESSAGE_TYPE_CD{
    	public static final String REGULAR = "REGULAR";
    	public static final String FORCE_READ = "FORCE_READ";
    	public static final String ACKNOWLEDGEMENT_REQUIRED = "ACKNOWLEDGEMENT_REQUIRED";
    }
    
    public interface MESSAGE_DETAIL_TYPE_CD {
        public static final String DEFAULT = "DEFAULT";
    }
    
    public interface SERVICE_TYPE_CATEG_STATUS_CD {
    	public static final String ACTIVE = "ACTIVE";
    	public static final String INACTIVE = "INACTIVE";
    }

    public interface MSDS_PLUGIN_CD {
      public static final String DEFAULT = "Default";
      public static final String DIVERSEY_WEB_SERVICE = "DiverseyWebService";
    }

    public static final String DISTRIBUTOR_TYPE = "DistTypeLabel";

    public interface DISTRIBUTOR_TYPE_CD {
        public static final String NATIONAL = "NATIONAL";
        public static final String REGIONAL = "REGIONAL";
        public static final String SUB_DISTRIBUTOR = "SUB_DISTRIBUTOR";
    }
    
    public interface DISTRIBUTOR_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String LIMITED = "LIMITED";
    }
    
    public interface TRADING_PARTNER_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String LIMITED = "LIMITED";
    }

    public interface INVOICE_LOADING_PRICE_MODEL_CD {
        public static final String
                EXCEPTION = "EXCEPTION",
                LOWEST = "LOWEST",
                DISTRIBUTOR_INVOICE = "DISTRIBUTOR_INVOICE",
                PREDETERMINED = "PREDETERMINED",
                HOLD_ALL = "HOLD_ALL";
    }

    public interface RECEIVING_SYSTEM_INVOICE_CD {
        public static final String
                REQUIRE_ENTRY_FIRST_ONLY = "Require Entry First Only",
                DISABLED = "Disabled",
                ENTER_ERRORS_ONLY_FIRST_ONLY = "Enter Errors Only First Only";

    }
    public interface SKU_TYPE_CD {
        public static final String STORE = "STORE";
        public static final String DISTRIBUTOR = "DISTRIBUTOR";
        public static final String MANUFACTURER = "MANUFACTURER";
    }

    public interface ITEM_TYPE_CD {
        public static final String PRODUCT = "PRODUCT";
        public static final String CATEGORY = "CATEGORY";
        public static final String MAJOR_CATEGORY = "MAJOR_CATEGORY";
        public static final String SERVICE ="SERVICE";
        public static final String ITEM_GROUP = "ITEM_GROUP";
    }
    
    public interface ITEM_MAPPING_CD {
        public static final String
                ITEM_MANUFACTURER = "ITEM_MANUFACTURER",
                ITEM_SERVICE_PROVIDER = "ITEM_SERVICE_PROVIDER",
                ITEM_DISTRIBUTOR = "ITEM_DISTRIBUTOR",
                ITEM_GENERIC_MFG = "ITEM_GENERIC_MFG",
                ITEM_STORE = "ITEM_STORE",
                ITEM_CERTIFIED_COMPANY = "ITEM_CERTIFIED_COMPANY";
    }
    
    public interface ORDER_SOURCE_CD {
        public static final String EDI_850 = "EDI";
        public static final String FAX = "Fax";
        public static final String MAIL = "Mail";
        public static final String EMAIL = "Email";
        public static final String TELEPHONE = "Phone";
        public static final String WEB = "Web";
        public static final String LAW = "Law";
        public static final String INVENTORY = "Inventory";
        public static final String EXTERNAL = "External";  //came through a loader of some kind
        public static final String OTHER = "Other";
        public static final String SCHEDULER = "Scheduler";
    }

    public interface NAME_VALUE_CD {
        public static final String
                UNIT_OF_MEASURE = "UOM",
                PACK = "PACK",
                SIZE = "SIZE";
    }
    
    public interface SHOPPING_CONTROL_ACTION_CD {
        public static final String APPLY = "APPLY";
        public static final String WORKFLOW = "WORKFLOW";
    }
    
    public interface SHOPPING_CONTROL_STATUS_CD {
    	public static final String ACTIVE = "ACTIVE";
    	public static final String INACTIVE = "INACTIVE";
    }
    
    public interface HISTORY_TYPE_CD {
    	public static final String CREATED = "CREATED";
    	public static final String MODIFIED = "MODIFIED";
    }
    
    public interface HISTORY_OBJECT_TYPE_CD {
    	public static final String ACCOUNT = "ACCOUNT";
    	public static final String GROUP = "GROUP";
    	public static final String ITEM = "ITEM";
    	public static final String LOCATION = "LOCATION";
    	public static final String SHOPPING_CONTROL = "SHOPPING_CONTROL";
    	public static final String STORE = "STORE";
    	public static final String USER = "USER";
    	public static final String GENERIC_REPORT = "GENERIC_REPORT";
    }


    public interface STORE_PROFILE_FIELD {
        public static final String PROFILE_NAME = "PROFILE_NAME";
        public static final String LANGUAGE = "LANGUAGE";
        public static final String LANGUAGE_OPTIONS = "LANGUAGE_OPTIONS";
        public static final String COUNTRY = "COUNTRY";
        public static final String CONTACT_ADDRESS = "CONTACT_ADDRESS";
        public static final String PHONE = "PHONE";
        public static final String MOBILE = "MOBILE";
        public static final String FAX = "FAX";
        public static final String EMAIL = "EMAIL";
        public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";
    }

    public interface STORE_PROFILE_TYPE_CD{
    	public static final String FIELD_OPTION = "FIELD_OPTION";
    	public static final String LANGUAGE_OPTION = "LANGUAGE_OPTION";
    }

    public interface ITEM_STATUS_CD {
    	public static final String ACTIVE = "ACTIVE";
    	public static final String INACTIVE = "INACTIVE";
    }

    public interface ITEM_PROPERTY_CD {
        public static final String SIZE             = "SIZE";
        public static final String COLOR            = "COLOR";
        public static final String UOM              = "UOM";
        public static final String HAZMAT           = "HAZMAT";
        public static final String PACK             = "PACK";
        public static final String PKG_UPC_NUM      = "PKG_UPC_NUM";
        public static final String SCENT            = "SCENT";
        public static final String SHIP_WEIGHT      = "SHIP_WEIGHT";
        public static final String UNSPSC_CD        = "UNSPSC_CD";
        public static final String UPC_NUM          = "UPC_NUM";
    }

    public interface WEIGHT_UNIT_CD {
        public static final String OUNCE = "OUNCE";
        public static final String POUND = "POUND";
        public static final String KILOGRAMME = "KILOGRAMME";
        public static final String GRAMME = "GRAMME";
    }

    public interface ITEM_UOM_CD {
        public static final String
                UOM_CS = "CS",
                UOM_EA = "EA",
                UOM_PK = "PK",
                UOM_BX = "BX",
                UOM_DZ = "DZ",
                UOM_PR = "PR",
                UOM_DR = "DR",
                UOM_TB = "TB",
                UOM_RL = "RL",
                UOM_DP = "DP",
                UOM_BD = "BD",
                UOM_PL = "PL",
                UOM_BG = "BG",
                UOM_CT = "CT",
                UOM_KT = "KT",
                UOM_OTHER = "OTHER";
    }

    public interface ITEM_ASSOC_CD {
        public static final String
                CATEGORY_ANCESTOR_CATEGORY = "CATEGORY_ANCESTOR_CATEGORY",
                CATEGORY_PARENT_CATEGORY = "CATEGORY_PARENT_CATEGORY",
                PRODUCT_PARENT_CATEGORY = "PRODUCT_PARENT_CATEGORY",
                PRODUCT_PARENT_PRODUCT = "PRODUCT_PARENT_PRODUCT",
                CATEGORY_PARENT_PRODUCT = "CATEGORY_PARENT_PRODUCT",
                CATEGORY_MAJOR_CATEGORY = "CATEGORY_MAJOR_CATEGORY",
                MANAGED_ITEM_PARENT = "MANAGED_ITEM_PARENT",
                CROSS_STORE_ITEM_LINK = "CROSS_STORE_ITEM_LINK",
                SERVICE_PARENT_CATEGORY="SERVICE_PARENT_CATEGORY",
                ITEM_GROUP_ITEM = "ITEM_GROUP_ITEM";
    }
    
    public interface WORKFLOW_IND_CD {
        public static final String SKIP = "SKIP";
        public static final String INTERRUPTED = "INTERRUPTED"; // interrupted pipeline
        public static final String TO_RESUME = "TO_RESUME";     //ready to resume afer interruption
        public static final String TO_PROCESS = "TO_PROCESS";   //same as null value
        public static final String PROCESSED = "PROCESSED";
    }
    public interface SCHEDULE_STATUS_CD {
        public static final String
                ACTIVE = "ACTIVE",
                INACTIVE = "INACTIVE",
        		LIMITED = "LIMITED";
    }

    public interface ITEM_META_CD {
        public static final String
          COLOR         = "COLOR",
          SCENT         = "SCENT",
          SIZE          = "SIZE",
          SHIP_WEIGHT   = "SHIP_WEIGHT",
          WEIGHT_UNIT   = "WEIGHT_UNIT",
          UOM           = "UOM",
          PACK          = "PACK",
          UPC_NUM       = "UPC_NUM",
          PKG_UPC_NUM   = "PKG_UPC_NUM",

          IMAGE         = "IMAGE",
          THUMBNAIL     = "THUMBNAIL",
          MSDS          = "MSDS",
          DED           = "DED",
          SPEC          = "SPEC",
          
          LIST_PRICE    = "LIST_PRICE",
          COST_PRICE    = "COST_PRICE",
          UNSPSC_CD     = "UNSPSC_CD",
          OTHER_DESC    = "OTHER_DESC",
          PSN           = "PSN",
          NSN           = "NSN",
          HAZMAT        = "HAZMAT",
          CUBE_SIZE     = "CUBE_SIZE",
          PACK_PROBLEM_SKU = "PACK_PROBLEM_SKU",
          REBATE_BASE_COST = "REBATE_BASE_COST";
    }
    
    public interface UPLOAD_STATUS_CD {
        public static final String
                PROCESSING = "PROCESSING",
                PROCESSED = "PROCESSED";
    }

    public interface ITEM_LOADER_MATCH_FILTER_CD {
        public static final String
            MATCHED = "MATCHED",
            UNMATCHED = "UNMATCHED",
            ALL = "ALL";
    }

    

    public interface CHARGE_CD {
        public static final String FUEL_SURCHARGE = "FUEL_SURCHARGE";
        public static final String SMALL_ORDER_FEE = "SMALL_ORDER_FEE";
        public static final String DISCOUNT = "DISCOUNT";
    }
    
    public interface ORDER_PROPERTY_TYPE_CD {
        public static final String ORDER_NOTE = "Notes";
        public static final String CUSTOMER_ORDER_NOTE = "Order Request Note";
        public static final String CUSTOMER_ORDER_DATE = "Customer Order Date";
        public static final String PIPELINE_STEP = "PIPELINE_STEP";
        public static final String WORKFLOW_STEP = "WORKFLOW_STEP";
        public static final String CUSTOMER_BILLING_UNIT = "Customer Billing Unit";
        public static final String REQUESTED_SHIP_DATE = "Requested Ship Date";
        public static final String PENDING_DATE = "Pending Date";
        public static final String SHIPPING_STATUS = "Shipping Status";
        public static final String VOUCHER_NUMBER = "Voucher Number";
        public static final String VENDOR_ORDER_NUMBER = "Vendor Order Number";
        public static final String VENDOR_REQUESTED_FREIGHT = "VENDOR_REQUESTED_FREIGHT";
        public static final String VENDOR_REQUESTED_MISC_CHARGE = "VENDOR_REQUESTED_MISC_CHARGE";
        public static final String VENDOR_REQUESTED_TAX = "VENDOR_REQUESTED_TAX";
        public static final String VENDOR_REQUESTED_INV_DATE = "VENDOR_REQUESTED_INV_DATE";
        public static final String VENDOR_REQUESTED_DISCOUNT = "VENDOR_REQUESTED_DISCOUNT";
        public static final String QUANTITY_UPDATE = "QUANTITY_UPDATE";
        public static final String ERP_COMPANY = "ERP_COMPANY";  //JWP (but roughly equivilant to lawsons company field)
        public static final String ORDER_TYPE = "ORDER_TYPE";    //JWP
        public static final String BILLING_ORDER = "BILLING_ORDER";
        public static final String BILLING_ORIGINAL_PO_NUM = "BILLING_ORIGINAL_PO_NUM";
        public static final String BILLING_DISTRIBUTOR_INVOICE = "BILLING_DISTRIBUTOR_INVOICE";
        public static final String CUST_REQ_RESHIP_ORDER_NUM = "CUST_REQ_RESHIP_ORDER_NUM";
        public static final String OPEN_LINE_STATUS_CD = "Open Line Status Code";
        public static final String CUSTOMER_CART_COMMENTS = "CUSTOMER_CART_COMMENTS";
        public static final String PUNCH_OUT_ORDER_ORIG_ORDER_NUM = "Punch Out Order Orig Order Num";
        public static final String CUSTOMER_SYSTEM_ID = "custSysId";
        public static final String CUSTOMER_SYSTEM_URL = "custSysUrl";
        public static final String INVOICE_DIST_APPROVED = "Invoice Approved";
        public static final String OTHER_PAYMENT_INFO = "Other Payment Information";
        public static final String DISTRIBUTOR_PO_NOTE = "Distributor PO Note";
        public static final String ORDER_RECEIVED = "Order Received";
        public static final String INVENTORY_ORDER_HOLD = "INVENTORY_ORDER_HOLD";
        public static final String VENDOR_REQUESTED_TOTAL = "VENDOR_REQUESTED_TOTAL";
        public static final String EVENT = "EVENT";
        public static final String CUSTOMER_PO_NUM = "CUSTOMER_PO_NUM";
        public static final String NETWORK_INVOICE = "NETWORK_INVOICE";
        public static final String NETWORK_INVOICE_NOTE = "NETWORK_INVOICE_NOTE";
        public static final String JANPAK_INVOICE = "JANPAK_INVOICE";
        public static final String JANPAK_INVOICE_ITEM = "JANPAK_INVOICE_ITEM";
        public static final String BRANCH = "BRANCH";
        public static final String REP_NUM = "REP_NUM";
        public static final String REP_NAME = "REP_NAME";
        public static final String ORDER_SENT_TO_EXTERNAL_SYS = "ORDER_SENT_TO_EXTERNAL_SYS";
        public static final String HISTORICAL_ORDER = "Historical Order";
        public static final String SKIP_DUPLICATED_ORDER_VALIDATION = "Skip Dup Order Validation";
        public static final String CHECKOUT_FIELD_CD = "CHECKOUT_FIELD_CD";
        public static final String REBILL_ORDER = "REBILL_ORDER";
        public static final String EVENT_ID_OF_SEND_PROCESS = "EVENT_ID_OF_SEND_PROCESS";
        public static final String ERP_RELEASED_TIME = "ERP_RELEASED_TIME";
        public static final String BUDGET_YEAR_PERIOD = "BUDGET_YEAR_PERIOD";
        public static final String BUDGET_YEAR_PERIOD_LABEL = "BUDGET_YEAR_PERIOD_LABEL";
        public static final String SHIP_TO_OVERRIDE = "SHIP_TO_OVERRIDE";
    }
    
    public interface ORDER_ITEM_DETAIL_ACTION_CD {
        public static final String ACCEPTED = "Accepted";
        public static final String SYSTEM_ACCEPTED = "System Accepted";
        public static final String DELETED = "Deleted";
        public static final String REJECTED = "Rejected";
        public static final String CANCELED = "Canceled";
        public static final String CANCELED_BACKORDER = "Canceled Backorder";
        public static final String DIST_SHIPPED = "Dist Shipped";   // 810 received form distributor
        public static final String CUST_SHIPPED = "Cust Shipped";   // 856 sent out to customer
        public static final String TRACKING_NUMBER = "Tracking Number";
        public static final String SUBSTITUTED = "Substituted";
        public static final String DIST_INVOICED = "Dist Invoiced"; // 810 received form distributor
        public static final String CUST_INVOICED = "Cust Invoiced"; // 810 sent out to customer
        public static final String DIST_INVOICE_REJECTED = "Invoice Rejectd"; //User rejected the distributor invoice
        public static final String CIT_INVOICED = "CIT Invoiced";      // 810 sent out to CIT
        // PENDING_REVIEW = "PENDING_REVIEW",
        // following action inserted when parsing inbound 855
        public static final String ACK_ACCEPTED = "ACK Accepted";
        public static final String ACK_BACKORDERED = "ACK Backordered";
        public static final String ACK_BACKORDERED_ENHANCED = "ACK Backordered Enhanced";
        public static final String ACK_ACCEPTED_CHANGES_MADE = "ACK Accepted-Changes Made";
        public static final String ACK_DELETED = "ACK Deleted";
        public static final String ACK_ON_HOLD = "ACK On Hold";
        public static final String ACK_ACCEPTED_QUANTITY_CHANGED = "ACK Accepted-Quantity Changed";
        public static final String ACK_REJECTED = "ACK Rejected";
        public static final String ACK_ACCEPTED_SUBSTITUTION = "ACK Accepted-Substitution";
        // following action inserted by po interface
        public static final String BACKORDERED = "Backordered";
        public static final String SHIPPED = "Shipped";
        public static final String SCHEDULED  = "Scheduled";
        //following made by the returns(RGA/RMA) screens
        public static final String RETURNED = "Returned";
        //following made by the reciving system
        public static final String RECEIVED_AGAINST = "Received Against";
        public static final String QUANTITY_CHANGE = "Quantity Change";
        public static final String TAX_CHANGE = "Tax changed";
        public static final String DELIVERY_REF_NUMBER = "Delivery Ref Number";
    }
    
    public interface ORDER_PROPERTY_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }
    
    public interface INVOICE_STATUS_CD {
        public static final String CANCELLED = "CANCELLED";             // Cancelled -- END
        public static final String REJECTED = "REJECTED";               // Dist Invoice was examined and rejected -- END
        public static final String DIST_SHIPPED = "DIST_SHIPPED";       // Received invoice from distributor
        public static final String PENDING_REVIEW = "PENDING_REVIEW";   // Problem receiving invoice from distributor
        public static final String DUPLICATE = "DUPLICATE";             // This invoice was already recieved from the distributor
        public static final String PROCESS_ERP = "PROCESS_ERP";         // Distributor invoice sent to Lawson
        public static final String ERP_REJECTED = "ERP_REJECTED";       // Problem sending distributor invoice to L
        public static final String ERP_GENERATED = "ERP_GENERATED";     // Got incomplete cust. invoice from Lawson
        public static final String ERP_GENERATED_ERROR = "ERP_GENERATED_ERROR"; // Problem getting incomplete invoice
        public static final String ERP_RELEASED = "ERP_RELEASED";       // Got customer invoice from Lawson -- END
        public static final String ERP_RELEASED_ERROR = "ERP_RELEASED_ERROR";   // Problem getting cust. invoice from L
        public static final String MANUAL_INVOICE_RELEASE = "MANUAL_INVOICE_RELEASE"; // The invoice was examined and updated.
        public static final String CUST_INVOICED = "CUST_INVOICED";     // Sent invoice to customer -- END
        public static final String CUST_INVOICED_FAILED = "CUST_INVOICED_FAILED"; // Problem sending invoice to cust.
        public static final String CLW_ERP_PROCESSED = "CLW_ERP_PROCESSED"; // Pass thru store customer invoice generated
        public static final String PENDING = "PENDING";                 // Inbound invoice has a dist uom conversion and was not yet shipped complete
        public static final String CLW_ERP_RELEASED = "CLW_ERP_Released";
        public static final String INVOICE_HISTORY = "Invoice_History";
    }
    
    public interface ORDER_ITEM_META_NAME {
        public static final String CATEGORY_NAME = "CATEGORY_NAME";
        public static final String STANDARD_PRODUCT_LIST = "STANDARD_PRODUCT_LIST";
    }
    
    public interface ITEM_SALE_TYPE_CD {
        public static final String END_USE = "END_USE";
        public static final String RE_SALE = "RE_SALE";
    }
    
    public interface STORE_TYPE_CD {
        public static final String DISTRIBUTOR = "DISTRIBUTOR";
        public static final String MLA = "MLA";
        public static final String OTHER = "OTHER";
        public static final String ENTERPRISE = "ENTERPRISE";
    }
    
    public interface ORDER_ITEM_STATUS_CD {
        public static final String CANCELLED = "CANCELLED";             // Line item cancelled
        public static final String PENDING_ERP_PO = "PENDING_ERP_PO";   // Covers ORDERED+PROCESS_ERP_PO above
        public static final String PENDING_REVIEW = "PENDING_REVIEW";   // Problem placing order
        public static final String PENDING_FULFILLMENT = "PENDING_FULFILLMENT"; // PO retrieved from Lawson
        public static final String SENT_TO_DISTRIBUTOR = "SENT_TO_DISTRIBUTOR"; // PO sent to distributor
        public static final String PENDING_FULFILLMENT_PROCESSING = "PENDING_FULFILLMENT_PROCESSING"; // PO was picked up for transmittal and should be in the client program processing
        public static final String PO_ACK_SUCCESS = "PO_ACK_SUCCESS";   // 850 accepted
        public static final String PO_ACK_ERROR = "PO_ACK_ERROR";       // 850 accepted with error
        public static final String PO_ACK_REJECT = "PO_ACK_REJECT";     // 850 rejected
        public static final String SENT_TO_DISTRIBUTOR_FAILED = "SENT_TO_DISTRIBUTOR_FAILED"; // Problem sending PO
        public static final String INVOICED = "INVOICED";               // Cust invoice received from Lawson
    }
    
    public interface PURCHASE_ORDER_STATUS_CD {
        public static final String PENDING_FULFILLMENT = "PENDING_FULFILLMENT";  // PO retrieved from Lawson
        public static final String SENT_TO_DISTRIBUTOR = "SENT_TO_DISTRIBUTOR"; // PO sent to distributor
        public static final String PENDING_FULFILLMENT_PROCESSING = "PENDING_FULFILLMENT_PROCESSING"; //po was picked up for transmittal and should be in the client program processing
        public static final String SENT_TO_DISTRIBUTOR_FAILED = "SENT_TO_DISTRIBUTOR_FAILED"; // Problem sending PO
        public static final String DIST_ACKD_PURCH_ORDER = "DIST_ACKD_PO"; // Distributor has acknowledge they recieved data
        public static final String CANCELLED = "CANCELLED"; //indicates the po has been cancelled
    }

    public interface PROCESS_TYPE_CD {
        public static final String TEMPLATE = "TEMPLATE";
        public static final String ACTIVE = "ACTIVE";
    }
    
    public interface PROCESS_NAMES {
        public static final String OUTBOUND_850 = "OUTBOUND_850";
        public static final String ORDER_NOTIFICATION = "ORDER_NOTIFICATION";
        public static final String OUTBOUND_SERVICE = "OUTBOUND_SERVICE" ;
        public static final String INVOICE_PROCESS = "INVOICE_PROCESS" ;
        public static final String PROCESS_ORDER_850 = "PROCESS_ORDER_850";
        public static final String PROCESS_INBOUND_TRANSACTION = "PROCESS_INBOUND_TRANSACTION";
        public static final String PROCESS_OUTBOUND_TRANSACTION = "PROCESS_OUTBOUND_TRANSACTION";
        public static final String WORK_ORDER_UPDATE = "WORK_ORDER_UPDATE";
        public static final String WORK_ORDER_PROCESS = "WORK_ORDER_PROCESS";
        public static final String WORK_ORDER_SENT_TO_PROVIDER = "WORK_ORDER_SENT_TO_PROVIDER";
        public static final String EVENT_SYS_JOB = "EVENT_SYS_JOB";
        public static final String JAN_PACK_SITE_LOADER = "JAN_PACK_SITE_LOADER";
        public static final String JAN_PACK_ITEM_LOADER = "JAN_PACK_ITEM_LOADER";
        public static final String JAN_PACK_INVOICE_LOADER = "JAN_PACK_INVOICE_LOADER";
        public static final String DISTRIBUTOR_DIM_LOAD = "DISTRIBUTOR_DIM_LOAD";
        public static final String MANUFACTURER_DIM_LOAD = "MANUFACTURER_DIM_LOAD";
        public static final String REPORT_SCH_EVENT_GENERATOR = "REPORT_SCH_EVENT_GENERATOR";
        public static final String REPORT_SCHEDULE = "REPORT_SCHEDULE";
        public static final String EXEC_SQL_SEQUENCE = "EXEC_SQL_SEQUENCE";
        public static final String PROCESS_INBOUND_CHUNKS = "PROCESS_INBOUND_CHUNKS";
        public static final String PROCESS_CORPORATE_SCHEDULED_ORDER = "PROCESS_CORPORATE_SCHEDULED_ORDER";
        public static final Object PROCESS_FTP_JOB_OPERATIONS = "PROCESS_FTP_JOB_OPERATIONS";
        public static final Object PROCESS_REPORT_JOB_OPERATIONS = "PROCESS_REPORT_JOB_OPERATIONS";
        public static final Object PROCESS_APP_CMD_OPERATIONS = "PROCESS_APP_CMD_OPERATIONS";
        public static final String PROCESS_BATCH_ORDERS = "PROCESS_BATCH_ORDERS";
        public static final String PROCESS_UPDATE_CATALOGS = "PROCESS_UPDATE_CATALOGS";
    }
    
    public interface EVENT_STATUS_CD {
    	public static final String STATUS_READY          = "READY";
        public static final String STATUS_PENDING_REVIEW = "PENDING_REVIEW";
        public static final String STATUS_FAILED         = "FAILED";
        public static final String STATUS_IN_PROGRESS    = "IN_PROGRESS";
        public static final String STATUS_REJECTED       = "REJECTED";
        public static final String STATUS_PROCESSED      = "PROCESSED";
        public static final String STATUS_PROC_ERROR     = "PROC_ERROR";
        public static final String STATUS_DELETED        = "DELETED";
        public static final String STATUS_LIMITED        = "LIMITED";
        public static final String STATUS_IGNORE         = "IGNORE";
        public static final String STATUS_SYNC_CALL      = "SYNC_CALL";
        public static final String STATUS_HOLD           = "HOLD";
    }

    public interface CONTRACT_STATUS_CD {
        public static final String ACTIVE = "ACTIVE";
        public static final String DELETED = "DELETED";
        public static final String ROUTING = "ROUTING";
        public static final String INACTIVE = "INACTIVE";
        public static final String LIVE = "LIMITED";
    }
    
    public interface ASSET_STATUS_CD  {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }
    
    public interface ASSET_ASSOC_CD {
        public static final String ASSET_STORE= "ASSET_STORE";
        public static final String ASSET_SITE= "ASSET_SITE";
        public static final String ASSET_SERVICE= "ASSET_SERVICE";
    }

    public interface ORDER_GUIDE_TYPE_CD {
        public static final String
                ORDER_GUIDE_TEMPLATE = "ORDER_GUIDE_TEMPLATE",
                SITE_ORDER_GUIDE_TEMPLATE = "SITE_ORDER_GUIDE_TEMPLATE",
                BUYER_ORDER_GUIDE = "BUYER_ORDER_GUIDE",
                SHOPPING_CART = "SHOPPING_CART",
                MSDS_CLOSET = "MSDS_CLOSET",
                SPEC_CLOSET = "SPEC_CLOSET",
                DED_CLOSET = "DED_CLOSET",
                ESTIMATOR_ORDER_GUIDE = "ESTIMATOR_ORDER_GUIDE",
                DELETED = "DELETED",
                INVENTORY_CART = "INVENTORY_CART",
                PHYSICAL_CART = "PHYSICAL_CART",
                CUSTOM_ORDER_GUIDE = "CUSTOM_ORDER_GUIDE";
    }

    public interface RECORD_STATUS_CD {
        public static final String VALID = "VALID";
        public static final String INVALID = "INVALID";
    }
    
    public interface MESSAGE_MANAGED_BY {
        public static final String ADMINISTRATOR = "ADMINISTRATOR";
        public static final String CUSTOMER = "CUSTOMER";
    }
}
