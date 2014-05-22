package com.espendwise.manta.util.trace;


public interface ExceptionReason {

    public static enum SystemReason implements ApplicationReason {
        APPLICATION_ILLEGAL_ACCESS_EXCEPTION,
        USER_DOES_NOT_HAVE_ACCESS_TO_STORE,
        USER_DOES_NOT_HAVE_ACCESS_TO_INSTANCE,
        ILLEGAL_VALIDATION_RESULT,
        CANT_RETRIEVE_LOGON_UI_OPTIONS
    }

    public static enum StoreMessageUpdateReason implements ApplicationReason {
    	STORE_MESSAGE_NAME_MUST_BE_UNIQUE,
    	STORE_MESSAGE_TITLE_MUST_BE_UNIQUE,
        STORE_MESSAGE_POSTED_DATE_BEFORE_CURRENT_DATE,
        STORE_MESSAGE_END_DATE_BEFORE_CURRENT_DATE,
        STORE_MESSAGE_END_DATE_BEFORE_POSTED_DATE,
        FORCE_READ_COUNT_LESS_THAN_ORIGINAL_VALUE
    }

    public static enum AccountUpdateReason implements ApplicationReason {
        ACCOUNT_MUST_BE_UNIQUE,
    }

    public static enum EmailTemplateUpdateReason implements ApplicationReason {
        EMAIL_TEMPLATE_CANT_BE_RENAMED, EMAIL_TEMPLATE_MUST_BE_UNIQUE
    }
    
    public static enum UserUpdateReason implements ApplicationReason {
        USER_MUST_BE_UNIQUE,
        USER_MUST_BE_UNIQUE1,
    }

    public static enum SiteUpdateReason implements ApplicationReason {
        SITE_CANT_BE_DELETED,
        NO_ACCOUNT_SET ,
        EFFECTIVE_DATE_CANT_BY_AFTER_EXP_DATE,
        CANT_SET_BOTH_ENABLE_INV_AND_ALLOW_CORP_SCH_ORDER,
        ACCOUNT_DOES_NOT_HAVE_ERP_SYSTEMS,
        ACCOUNT_HAS_MULTIPLE_ERP_SYSTEMS,
        ACCOUNT_DOES_NOT_HAVE_ASSOCIATION_WITH_STORE,
        SITE_MUST_BE_UNIQUE,
    }

    public static enum FiscalCalendarUpdateReason implements ApplicationReason {
        YEAR_UPDATE_NOT_ALLOWED,
        YEAR_OF_ZERO_RULE,
        WRONG_YEAR_VALUE,
        WRONG_YEAR_VALUE_1,
        EXCEPTION_WHEN_CHECK_WRONG_YEAR_VALUE_1,
        LAST_PERIOD_RULE,
        OUT_OF_RANGE_PERIODS,
        OUT_OF_SEQ_PERIODS,
        DUPLICATED_EFF_DATE,
        GAP,
        OVERLAP,
        WRONG_DATE_INTERVAL,
        FIRST_PERIOD_RULE,
        YEAR_EXIST,
        YEAR_OF_ZERO_FIRST_PERIOD_START_RULE,
        YEAR_OF_ZERO_LAST_PERIOD_START_RULE,
        YEAR_OF_ZERO_PERIOD_EXEED_YEAR_END,
        YEAR_OF_ZERO_CALENDAR_EXIST,
        CALENDAR_INTERSECT
    }

    public static enum IllegalDataStateReason implements ApplicationReason {
        SITE_NOT_FOUND,
         FISCAL_CALENDAR_NOT_FOUND,
        MULTIPLE_ACCOUNT_CATALOGS,
        MULTIPLE_RNTITY_CATALOG_CONFIGURATION
    }

    public static enum SiteHierarchyUpdateReason implements ApplicationReason {
        MULTIPLE_SITE_CONFIGURATION,
        DUPLICATED_NAME,
        SITE_HIER_ERROR_TOP_LEVEL_CONFIG,
        SITE_HIER_ERROR_SUB_LEVEL_CONFIG,
        SITE_HIER_ERROR_HIGHER_LEVEL_CONFIG,
        SITE_HIER_ERROR_OVERLAPING_SITE_CONFIG;
    }
    public static enum WorflowRuleUpdateReason implements ApplicationReason {
        WORKFLOW_MUST_BE_UNIQUE,
        WORKFLOW_RULE_IN_USE_DEL,
        WORKFLOW_RULE_IN_USE_UPD,
        WORKFLOW_NO_RULES_SELECTED,
        WORKFLOW_RULE_SKU_NOT_FOUND;
    }

    public static enum ManufacturerUpdateReason implements ApplicationReason {
        MANUFACTURER_MUST_BE_UNIQUE
    }

    public static enum DistributorUpdateReason implements ApplicationReason {
        DISTRIBUTOR_MUST_BE_UNIQUE
    }

    public static enum CostCenterUpdateReason implements ApplicationReason {
        COST_CENTER_MUST_BE_UNIQUE
    }
    
    public static enum GroupUpdateReason implements ApplicationReason {
        GROUP_MUST_BE_UNIQUE,
        GROUP_HAS_MULTI_STORE_ASSOCIATION
    }
    
    public static enum CorporateScheduleUpdateReason implements ApplicationReason {
        CORPORATE_SCHEDULE_MUST_BE_UNIQUE
    }
    public static enum CorporateScheduleFilterRuleReason implements ApplicationReason {
    	INCORRECT_SCHEDULE_DATE_FOUND
    }
    
    public static enum CorporateScheduleDeleteReason implements ApplicationReason {
        CORPORATE_SCHEDULE_CONFIGURED_TO_ACCOUNT,
        CORPORATE_SCHEDULE_CONFIGURED_TO_LOCATION,
    }
    
    public static enum CorporateScheduleAccountConfigUpdateReason implements ApplicationReason {
    	CORPORATE_SCHEDULE_CONFIGURED_TO_LOCATION_OF_ACCOUNT
    }

    public static enum MasterItemUpdateReason implements ApplicationReason {
        ITEM_MUST_BE_UNIQUE,
        CUSTOMER_SKU_NUM_MUST_BE_UNIQ
    }
    
    public static enum UserAccountConfigUpdateReason implements ApplicationReason {
    	USER_CONFIGURED_TO_LOCATION_OF_ACCOUNT
    }
    
    public static enum BatchOrderReason implements ApplicationReason {
    	ERROR_ON_LINE,
    	GENERAL_ERROR
    }
    
    public static enum OrderUpdateCheckNewSiteReason implements ApplicationReason {
    	SITE_NOT_FOUND,
    	FEW_SITES_FOUND,
        NOT_ACTIVE_SITE_FOUND,
        NO_ACCOUNT_FOR_SITE_FOUND,
        FEW_ACCOUNTS_FOR_SITE_FOUND,
        NO_CONTRACT_FOR_SITE_FOUND,
        FEW_CONTRACTS_FOR_SITE_FOUND
    }
    
    public static enum LoaderReason implements ApplicationReason {
    	GENERAL_ERROR
    }

}
