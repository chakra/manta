package com.espendwise.manta.util.binder;


import java.util.ArrayList;
import java.util.List;

import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.AccountIdentPropertiesView;
import com.espendwise.manta.model.view.ContentManagementView;
import com.espendwise.manta.model.view.DistributorIdentPropertiesView;
import com.espendwise.manta.model.view.ManufacturerPropertiesView;
import com.espendwise.manta.model.view.SiteIdentPropertiesView;
import com.espendwise.manta.model.view.StoreUiOptionView;
import com.espendwise.manta.model.view.UiOptionView;
import com.espendwise.manta.util.AccountIdentPropertyTypeCode;
import com.espendwise.manta.util.DistributorPropertyExtraCode;
import com.espendwise.manta.util.DistributorPropertyTypeCode;
import com.espendwise.manta.util.HomeViewType;
import com.espendwise.manta.util.ManufacturerPropertyTypeCode;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SitePropertyExtraCode;
import com.espendwise.manta.util.SitePropertyTypeCode;
import com.espendwise.manta.util.StoreUiPropertyTypeCode;
import org.apache.log4j.Logger;

public class PropertyBinder {
 private static final Logger logger = Logger.getLogger(PropertyBinder.class);


    public static StoreUiOptionView bindUiOptions(StoreUiOptionView uiOptions,
                                                  Long storeId,
                                                  List<PropertyData> uiProperties,
                                                  HomeViewType homeViewType) {

        uiOptions.setStoreId(storeId);
        uiOptions.setHomeViewType(homeViewType);

        return bindUiOptions(uiOptions, uiProperties);

    }

    public static <T extends UiOptionView> T bindUiOptions(T uiOptions, List<PropertyData> uiProperties) {

        if (uiProperties != null) {
            for (PropertyData prop : uiProperties) {
                if (StoreUiPropertyTypeCode.MANTA_UI_LOGO.toString().equals(prop.getPropertyTypeCd()) ) {
                    uiOptions.setLogo(prop.getValue());
                } else if (StoreUiPropertyTypeCode.UI_PAGE_TITLE.toString().equals(prop.getPropertyTypeCd())) {
                    uiOptions.setTitle(prop.getValue());
                } else if (StoreUiPropertyTypeCode.UI_FOOTER.toString().equals(prop.getPropertyTypeCd())) {
                    uiOptions.setFooter(prop.getValue());
                }
            }
        }

        return uiOptions;

    }

    public static AccountIdentPropertiesView bindAccountIdentProperties(AccountIdentPropertiesView value,
                                                                        Long accountId,
                                                                        List<PropertyData> properties) {
        if (value != null) {

            List<PropertyData> defaultProperties = new ArrayList<PropertyData>();
            value.setAccountId(accountId);
        	value.setDefaultProperties(defaultProperties);
            if (properties != null) {
                for (PropertyData prop : properties) {
                    if (AccountIdentPropertyTypeCode.ACCOUNT_TYPE.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setAccountType(prop);
                    } else if (AccountIdentPropertyTypeCode.BUDGET_ACCRUAL_TYPE_CD.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setBudgetType(prop);
                    } else if (AccountIdentPropertyTypeCode.DIST_ACCT_REF_NUM.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setDistributorReferenceNumber(prop);
                    } else {
                    	defaultProperties.add(prop);
                    }
                }
            }
        }

        return value;
    }

    public static ContentManagementView bindContentManagementProperties(ContentManagementView value, Long busEntityId,
    																List<PropertyData> properties){
    	
    	if(value != null){
    		value.setBusEntityId(busEntityId);
    		
    		if(properties != null){
    			for (PropertyData prop : properties) {
                    if (RefCodeNames.PROPERTY_TYPE_CD.DISPLAY_GENERIC_CONTENT.equals(prop.getShortDesc())) {
                        value.setDisplayGenericContent(prop);
                    } else if (RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_URL.equals(prop.getShortDesc())) {
                        value.setCustomContentURL(prop);
                    } else if (RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_EDITOR.equals(prop.getShortDesc())) {
                        value.setCustomContentEditor(prop);
                    } else if (RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_VIEWER.equals(prop.getShortDesc())) {
                        value.setCustomContentViewer(prop);
                    }
                }
    		}
    	}
    	return value;
    }

    public static SiteIdentPropertiesView bindSiteIdentProperties(SiteIdentPropertiesView value,
                                                                  Long siteId,
                                                                  List<PropertyData> properties) {
        if (value != null) {

            value.setSiteId(siteId);

            if (properties != null) {
                for (PropertyData prop : properties) {

                    if (SitePropertyTypeCode.ALLOW_CORPORATE_SCHED_ORDER.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setAllowCorporateScheduledOrder(prop);
                    } else if (SitePropertyTypeCode.BYPASS_ORDER_ROUTING.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setByPassOrderRouting(prop);
                    } else if (SitePropertyTypeCode.CONSOLIDATED_ORDER_WAREHOUSE.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setConsolidatedOrderWarehouse(prop);
                    } else if (SitePropertyTypeCode.DIST_SITE_REFERENCE_NUMBER.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setDistrSiteRefNumber(prop);
                    } else if (SitePropertyTypeCode.INVENTORY_SHOPPING.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setEnableInventory(prop);
                    } else if (SitePropertyTypeCode.INVENTORY_SHOPPING_TYPE.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setInventoryShoppingType(prop);
                    } else if (SitePropertyExtraCode.SHARE_BUYER_GUIDES.getNameExtraCode().equals(prop.getShortDesc())) {
                        value.setShareBuyerOrderGuides(prop);
                    } else if (SitePropertyTypeCode.SHOW_REBILL_ORDER.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setShowRebillOrder(prop);
                    } else if (SitePropertyExtraCode.SITE_REFERENCE_NUMBER.getNameExtraCode().equals(prop.getShortDesc())) {
                        value.setSiteReferenceNumber(prop);
                    } else if (SitePropertyTypeCode.TAXABLE_INDICATOR.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setTaxable(prop);
                    } else if (SitePropertyTypeCode.TARGET_FACILITY_RANK.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setTargetFicilityRank(prop);
                    } else if (SitePropertyTypeCode.LOCATION_COMMENTS.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setLocationComments(prop);
                    } else if (SitePropertyTypeCode.LOCATION_LINE_LEVEL_CODE.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setLocationLineLevelCode(prop);
                    } else if (SitePropertyTypeCode.LOCATION_PRODUCT_BUNDLE.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setProductBundle(prop);
                    } else if (SitePropertyTypeCode.LOCATION_SHIP_MSG.getTypeCode().equals(prop.getPropertyTypeCd())) {
                        value.setLocationShipMsg(prop);
                    }
                }
            }
        }

        return value;
    }

        public static ManufacturerPropertiesView bindManufacturerProperties(ManufacturerPropertiesView value,
                                                                        Long manufacturerId,
                                                                        List<PropertyData> properties) {
        if (value != null) {

            value.setManufacturerId(manufacturerId);

            if (properties != null) {
                for (PropertyData prop : properties) {
                    if (ManufacturerPropertyTypeCode.MSDS_PLUGIN.getTypeCode().equals(prop.getShortDesc())) {
                        value.setMsdsPlugin(prop);
                    }
                }
            }
        }

        return value;
    }


        public static DistributorIdentPropertiesView bindDistributorProperties(DistributorIdentPropertiesView value,
                                                                        Long distributorId,
                                                                        List<PropertyData> properties) {
        if (value != null) {

            value.setDistributorId(distributorId);

            if (properties != null) {
                for (PropertyData property : properties) {
                    if (DistributorPropertyTypeCode.RUNTIME_DISPLAY_NAME.getTypeCode().equals(property.getPropertyTypeCd())) {
                        value.setDisplayName(property);
                    }
                    else if (DistributorPropertyTypeCode.CALL_HOURS.getTypeCode().equals(property.getPropertyTypeCd())) {
                        value.setCallCenterHours(property);
                    }
                    else if (DistributorPropertyTypeCode.DISTRIBUTORS_COMPANY_CODE.getTypeCode().equals(property.getPropertyTypeCd())) {
                        value.setCompanyCode(property);
                    }
                    else if (DistributorPropertyTypeCode.CUSTOMER_REFERENCE_CODE.getTypeCode().equals(property.getPropertyTypeCd())) {
                        value.setCustomerReferenceCode(property);
                    }
                    else if (DistributorPropertyExtraCode.DISTRIBUTOR_TYPE.getNameExtraCode().equals(property.getShortDesc())) {
                    	value.setType(property);
                    }
                }
            }
        }

        return value;
    }


}
