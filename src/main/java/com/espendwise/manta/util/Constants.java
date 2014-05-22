package com.espendwise.manta.util;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public interface Constants {

    public static final int ZERO  = 0;
    
    public static final String EMPTY = "";
    public static final String NULLS = "null";
    public static final String SPACE = " ";
    public static final String UNK = "unk";
    public static final String ERP_NUM_PREFIX = "#";
    public static final String ANY = "Any";
    public static final String ON = "on";
    public static final String OFF = "off";
    public static final String Y = "Y";
    public static final String N = "N";
    public static final String NONE = "NONE";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String YES = "Yes";
    public static final String NO = "No";

    public static final Locale DEFAULT_LOCALE = Locale.US;
    public static final Locale SYSTEM_LOCALE = Locale.US;
    public static final String SYSTEM_DATE_PATTERN = "MM/dd/yyyy";
    public static final String SYSTEM_TIME_PATTERN = "HH:mm";
    public static final String SYSTEM_MONTH_WITH_DAY_PATTERN = "M/d";

    public static final Integer NOT_LEAP_YEAR = 2000;
    public static final Integer LEAP_YEAR = 2001;
    public static final int ACTUAL_BUDGER_FISCAL_YEAR_THRESHOLD = 2;

    public static final String MANTA_BUILD_VERSION_PROPERTY = "build.number.manta";
    public static final String MANTA_BRANCH_VERSION_PROPERTY = "build.branch.manta";
    public static final String MANTA_BUILD_DATE_PROPERTY = "build.date.manta";

    public static final String CONTENT_DEFAULT_LOCALE_CD = "x";

    public static final String DATE_PATTERN = "format.input.dateFormat";
    public static final String TIME_PATTERN  = "format.input.timeFormat";
    public static final String MONTH_WITH_DAY_PATTERN = "format.input.monthWithDay";
    public static final String DATE_PICKER_PATTERN = "format.input.datePicker";

    public static final String DATE_PATTERN_PROMPT  = "format.prompt.dateFormat";
    public static final String TIME_PATTERN_PROMPT  = "format.prompt.timeFormat";
    public static final String MONTH_WITH_DAY_PATTERN_PROMPT  = "format.prompt.monthWithDay";


    public static final String  BEAN_GETTER_PREFFIX = "get";
    public static final String EXCEPTION_I18N_RESOLVER_KEY_PREFIX = "exception.type.";

    public static final String LOCALE_DEFAULT_CD = "Default";
    public static final String DEFAULT_IMAGE_PATH = "images";
    public static final String DEFAULT_LOGO_PATH = "/logo/logo.png";
    public static final String POINT = ".";
    public static final String ROOT = "root";


    public static final String SITE_HIERARCHY_LONG_NAME = "";
    public static final int MAX_SITE_HIERARCHY_LEVELS = 4;

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String DATA_SOURCE_UNIT_NAME = "dsunit";

    public interface CHARS {
        public static final String  Y  = "y";
        public static final String  T  = "t";
        public static final String _1  = "1";
        public static final String UNDERLINE = "_";
        public static final String POUND = "#";
        public static final String HYPHEN = "-";
        public static final String ASTERISK = "*";
    }

    public interface FILE_EXT {
        public static final String TXT = ".txt";
        public static final String PROPERTIES = ".properties";
    }

    public interface ENCODING  {
        public static final String UTF8 = "UTF-8";
    }

    public static final String UNDEFINED_MESSAGE_KEY = "??? ";

    public static final String WIDE_SCREEN_STYLE =" wideScreen";


    public interface FILTER_TYPE {
        public static final String ID = "ID";
        public static final String START_WITH = "START_WITH";
        public static final String CONTAINS = "CONTAINS";
        public static final String DEFAULT = START_WITH;
        public static final String EXACT_MATCH ="EXACT_MATCH";
    }

    public interface SKU_FILTER_TYPE {
        public static final String STORE = "STORE";
        public static final String DISTRIBUTOR = "DISTRIBUTOR";
        public static final String MANUFACTURER = "MANUFACTURER";
        public static final String DEFAULT = STORE;
    }

    public static final String APPLICATION_FUNCTIONS = "APPLICATION_FUNCTIONS";
    public static final String DEFAULT_GROUP = "EVERYONE";

    public interface ORDER_TYPE {
        public final static int ORDER_BY_ID = 1;
        public static final int ORDER_BY_NAME = 2;
    }

    public interface APPLICATION_SETTINGS {
        public static final String CONTENT_DEFAULT_PATH = "content.default.path";
        public static final String STORAGE_URL = "storage.url";
        public static final String STORAGE_USER = "storage.user";
        public static final String STORAGE_PASSWORD = "storage.password";
        public static final String STORAGE__LOGO1_CONTENT_PREFIX = "storage.logo1.content.prefix";
        public static final String PROCUREMENT_ENTRY_POINT = "stjohn.entryPoint";
        public static final String SERVICES_ENTRY_POINT = "orca.entryPoint";
        public static final String ORCA_HOST_ADDRESS = "orca.hostAddress";
        public static final String STJOHN_HOST_ADDRESS = "stjohn.hostAddress";
        public static final String ACCESS_TOKEN_MAX_AGE = "accessToken.maxAge";
        public static final String MAIN_DB_JDBC_UTL = "database.dsmain.datasource.jdbcUrl";
    }

    public static final String APP_USER_JSP_SCOPE = "appUser";
    public static final String STORE_CTX_JSP_SCOPE = "storeContext";
    public static final String APPLICATION_RESOURCE_JSP_SCOPE = "appResource";
    public static final String DISPLAY_SETTINGS_JSP_SCOPE = "displaySettings";
    public static final String WEB_MESSAGES_JSP_SCOPE ="webMessages";
    public static final String LOGIN_ERROR_JSP_SCOPE ="loginError";

    public interface FILTER_RESULT_LIMIT {
        public static final Integer ACCOUNT = 1000;
        public static final Integer STORE_MESSAGE_ACCOUNT = 1000;
        public static final Integer STORE_MESSAGE = 1000;
        public static final Integer STORE = 1000;
        public static final Integer TEMPLATE = 1000;
        public static final Integer USER = 1000;
        public static final Integer SITE = 1000;
        public static final Integer USER_GROUP = 1000;
        public static final Integer CATALOG = 1000;
        public static final Integer SITE_HIERARCHY_SITE = 1000;
        public static final Integer MANUFACTURER = 1000;
        public static final Integer DISTRIBUTOR = 1000;
        public static final Integer COST_CENTER = 1000;
        public static final Integer ITEM = 500;
        public static final Integer SERVICE = 500;
        public static final Integer SHOPPING_CONTROLS = 500;
        public static final Integer GROUP_CONFIG= 1000;
        public static final Integer HISTORY_RECORDS = 500;
        public static final Integer ORDER = 1000;
        public static final Integer CORPORATE_SCHEDULE = 1000;
        public static final Integer UPLOAD_FILE = 1000;
        public static final Integer ASSET = 1000;
    }

    public interface VALIDATION_FIELD_CRITERIA {
        public static final int SPEC_50_LENGTH           = 50;
        public static final int SPEC_200_LENGTH          = 200;
        public static final int SPEC_60_LENGTH           = 60;
        public static final int SPEC_400_LENGTH          = 400;
        public static final int SHORT_DESC_LENGTH        = 255;
        public static final int NAME_LENGTH              = 128;
        public static final int SHORT_DB_CODE_LENGTH     = 15;
        public static final int DB_CODE_LENGTH           = 30;
        public static final int LONG_DB_CODE_LENGTH      = 60;
        public static final int TEXT_LENGTH              = 2000;
        public static final int LONG_DESC_LENGTH         = 3000;
        public static final int BIG_TEXT_LENGTH          = 4000;
        public static final int TELEPHONE_NUMBER_LENGTH  = 60;
        public static final GregorianCalendar MIN_DATE   = new GregorianCalendar(1900, 0, 1);
        public static final GregorianCalendar MAX_DATE   = new GregorianCalendar(3000, 0, 1);
        public static final int MIN_YEAR                 = MIN_DATE.get(Calendar.YEAR);
        public static final int MAX_YEAR                 = MAX_DATE.get(Calendar.YEAR);
        public static final int MAX_FISCAL_MONTH         = 64;
        public static final int REF_NUMBER_LENGTH        = 255;
        public static final int SKU_NUM_LENGTH           = 38;
        public static final int SCHEDULE_INTERVAL_LENGTH = 3;
    }

    public interface USER_ROLE {
        public static final String CREDIT_CARD = "CC^";
        public static final String OTHER_PAYMENT = "OPmt^";  // ex: HHS record of call
        public static final String CAN_EDIT_SHIPTO = "eST^";
        public static final String CAN_EDIT_BILLTO = "eBT^";
        public static final String SERVICE_VENDOR_ROLE = "STmSVr";
        public static final String SITE_MANAGER_ROLE = "STmSMr";
        public static final String CONTRACT_ITEMS_ONLY = "CI^";
        public static final String SHOW_PRICE = "SP^";
        public static final String BROWSE_ONLY = "BO^";
        public static final String ON_ACCOUNT = "OA^";
        public static final String PO_NUM_REQUIRED = "PR^";
        public static final String NO_REPORTING = "NR^";
    }

     public static final int DEFAULT_SQL_PACKAGE_SIZE = 1000;

    public interface LOGIN_AS_TARGET {
        public static final String PROCUREMENT = "PROCUREMENT";
        public static final String SERVICES = "SERVICES";
    }

    public static final String LOCALE_PIG_LATIN = "xx-piglatin";
    
    public static final String TRUE_FOR_RESALE = "true for resale";

	public static final String EMAIL_FORMAT_PLAIN_TEXT = "text/plain";

    public interface ITEM_ATTACHMENT_TYPE {
        public static final String IMAGE = "images";
        public static final String THUMBNAIL = "thumbnails";
        public static final String MSDS = "msds";
        public static final String DED = "ded";
        public static final String SPEC = "spec"; 
    };

    public static final String IMAGE_CONTENT_TYPE = "image";


    public interface ITEM_LOADER_PROPERTY {
        public static final String
            SKU_NUM = "Sku Num",
            SHORT_DESC = "Short Desc",
            LONG_DESC = "Long Desc",
            OTHER_DESC = "Other Desc",
            SIZE = "Size",
            PACK = "Pack",
            UOM = "UOM",
            COLOR = "Color",
            CATEGORY = "Category",
            UNSPSC = "UNSPSC",
            NSN = "NSN",
            PSN = "PSN",
            MANUFACTURER = "Manufacturer",
            MANUF_SKU = "Manuf. Sku",
            MANUF_UOM = "Manuf. UOM",
            MANUF_PACK = "Manuf. Pack",
            DISTRIBUTOR = "Distributor",
            DIST_SKU = "Dist. Sku",
            DIST_UOM = "Dist. UOM",
            DIST_PACK = "Dist. Pack",
            DIST_UOM_MULTI = "Dist. UOM. Mult",
            GEN_MANUFACTURER = "Gen. Manufacturer",
            GEN_MANUF_SKU = "Gen. Manuf. Sku",
            LIST_PRICE = "List Price",
            CATALOG_PRICE = "Catalog Price",
            DIST_COST = "Dist. Cost",
            MFCP = "MFCP",
            BASE_COST = "Base Cost",
            SPL = "SPL",
            TAX_EXEMPT = "Tax Exempt",
            GREEN_CERTIFIED = "Green Certified",
            CUSTOMER_SKU = "Cust. Sku",
            CUSTOMER_DESC = "Cust. Desc",
            IMAGE_URL = "Image URL",
            MSDS_URL = "MSDS URL",
            DED_URL = "DED URL",
            PROD_SPEC_URL = "Prod Spec URL",
            SHIPPING_WEIGHT = "Shipping Weight",
            WEIGHT_UNIT = "Weight Unit",
            THUMBNAIL_URL = "Thumbnail URL",
            SERVICE_FEE_CODE = "Service Fee Code";
    }
    
    public interface USER_LOADER_PROPERTY {
        public static final String
	        VERSION = "Version Number",
	        ACTION = "Action",
	        STORE_ID = "Store ID",
	        STORE_NAME = "Store/Primary Entity Name",
	        ACCOUNT_REF_NUM = "Account Reference Number",
	        ACCOUNT_NAME = "Account Name",
	        LOCATION_NAME = "Location Name",
	        LOCATION_REF_NUM = "Distributor Location Reference Number",
	        USERNAME = "Username",
	        PASSWORD = "Password",
	        UPDATE_PASSWORD = "Update Password",
	        PREFERRED_LANGUAGE = "Preferred Language",
	        FIRST_NAME = "First Name",
	        LAST_NAME = "Last Name",
	        ADDRESS1 = "Address 1",
	        ADDRESS2 = "Address 2",
	        CITY = "City",
	        STATE = "State",
	        POSTAL_CODE = "Postal Code",
	        COUNTRY = "Country",
	        PHONE = "Phone",
	        EMAIL = "Email",
	        FAX = "Fax",
	        MOBILE = "Mobile",
	        APPROVER = "Approver",
	        NEEDS_APPROVAL = "Needs Approval",
	        WAS_APPROVED = "Was Approved",
	        WAS_REJECTED = "Was Rejected",
	        WAS_MODIFIED = "Was Modified",
	        ORDER_DETAIL_NOTIFICATION = "Order Detail Notification",
	        SHIPPING_NOTIFICATION = "Shipping Notification",
	        CUTOFF_TIME_REMINDER = "Cutoff Time Reminder",
	        NUMBER_OF_TIMES = "Number of Times",
	        PHY_INV_NON_COMP_LOCATION_LISTING = "Physical Inventory Non-Compliant Location Listing",
	        PHY_INV_COUNTS_PAST_DUE = "Physical Inventory Counts Past Due",	    	
	        CORPORATE_USER = "Corporate User",
	        ON_ACCOUNT = "On Account",
	        CREDIT_CARD = "Credit Card",
	        OTHER_PAYMENT = "Other Payment",
	        PO_NUM_REQUIRED = "PO# Required",
	        SHOW_PRICE = "Show Price",
	        BROWSE_ONLY = "Browse Only",
	        NO_REPORTING = "No Reporting",
	        GROUP_ID = "Group ID",
	        LOCATION_ID = "Location ID";
    }
    public interface UPDATE_CATALOG_LOADER_PROPERTY {
        public static final String
	        VERSION = "Version Number",
	        ACTION = "Action",
	        LINE_NUMBER = "Line Number",
	        CATALOG_ID = "Catalog Id",
	        STORE_SKU = "Store SKU",
	        PRICE = "Price",
	        COST = "Cost",
	        CATEGORY = "Category",
	        DIST_NAME = "Distributor Name",
	        DIST_SKU = "Dist SKU",
	        DIST_UOM = "Dist UOM",
	        DIST_PACK = "Dist Pack",
	        ORDER_GUIDE_NAME = "Order Guide Name",
	        TAX_EXEMPT = "Tax Exempt",
	        CUSTOMER_SKU = "Customer SKU",
	        STANDARD_PRODUCT_LIST = "Standard Product List";
    }
    
}
