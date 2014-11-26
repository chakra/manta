package com.espendwise.manta.util;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.model.data.LanguageData;
import com.espendwise.manta.model.data.CurrencyData;
import com.espendwise.manta.model.view.CountryView;
import com.espendwise.manta.service.CountryService;
import com.espendwise.manta.service.CurrencyService;
import com.espendwise.manta.service.EntityQueryService;
import com.espendwise.manta.service.MainDbService;
import com.espendwise.manta.util.trace.ApplicationDataNotFoundException;
import com.espendwise.manta.util.criteria.CurrencyCriteria;
import com.espendwise.ocean.common.emails.EmailMeta;
import com.espendwise.ocean.common.emails.EmailType;


@Resource(mappedName = ResourceNames.DB_CONSTANT_RESOURCE)
public class DbConstantResource implements InitializingBean, BeanFactoryAware {

    private static Log logger = LogFactory.getLog(DbConstantResource .class);

    private EntityQueryService queryService;
    private CountryService countryService;
    private MainDbService mainDbService;
    private CurrencyService currencyService;

   private Map<String, ConstantResource> resourceUnitMap;
   private static Map<String, String> applicationFunctionsToTypeMap;

    @Autowired
    public DbConstantResource(EntityQueryService queryService,
                              CountryService countryService,
                              CurrencyService currencyService,
                              MainDbService mainDbService) {
        this.queryService = queryService;
        this.countryService = countryService;
        this.currencyService = currencyService;
        this.mainDbService =  mainDbService;
    }


    public synchronized void init() throws IOException {

        logger.info("init()=> BEGIN, "+this.queryService);

        try {

            resourceUnitMap = new HashMap<String, ConstantResource>();

            for (String unit : mainDbService.getAliveUnits()) {
                initDbResources(unit);
            }

        } catch (Exception e) {

            logger.error(e.getMessage(), e);

        }

        logger.info("init()=> END.");

    }

    private void initDbResources(String unit) {

        try {

            ConstantResource resource = new ConstantResource();

            {
                logger.info("init()=> datasource: " + unit + ", loading countries ...");
                List<CountryView> countries = countryService.findAllCountries(unit, CountryView.UI_NAME);
                resource.put(CountryView.class, new CountryResourceDecoration(countries));
                logger.info("init()=> datasource: " + unit + ", OK!");

            }


            {
                logger.info("init()=> datasource: " + unit + ", loading languages ...");
                List<LanguageData> languages = queryService.findEntities(unit, LanguageData.class, LanguageData.UI_NAME, true);
                resource.put(LanguageData.class, new LanguageResourceDecoration(languages));
                logger.info("init()=> datasource: " + unit + ", OK!");
            }

            {
                logger.info("init()=> datasource: " + unit + ", loading currencies ...");
                List<CurrencyData> currencyList = currencyService.findCurrenciesByCriteria(new CurrencyCriteria());
                resource.put(CurrencyData.class, new CurrencyResourceDecoration(currencyList));
                logger.info("init()=> datasource: " + unit + ", OK!");
            }

            logger.info("init()=> datasource: " + unit + ", resource.size: " + resource.size() + ", (" + resource.keySet() + ")");

            resourceUnitMap.put(unit, resource);

        } catch (Exception e) {

            logger.info("init()=> ERROR, datasource: " + unit + ", error: " + e.getMessage());

        }
    }


    public <T> List<T> get(Class<T> clazz) {

        List<T>  result = new ArrayList<T>();

        if (resourceUnitMap != null) {

            String unit = Auth.getApplicationDataSource().getDataSourceIdent().getDataSourceName();
            if (!resourceUnitMap.containsKey(unit)) {
                initDbResources(unit);
            }

            ConstantResource resources = resourceUnitMap.get(unit);
            if (resources == null) {
                return result;
            }

            ConstantResourceObject classResource = resources.get(clazz);
            if (classResource == null) {
                return result;
            }

            for (ConstantResourceFields v : classResource) {
                result.add((T) v.getValue());
            }

        }

        return result;
    }


    public List<CountryView> getCountries()  {
        return get(CountryView.class);
    }

    public List<LanguageData> getLanguages()  {
        return get(LanguageData.class);
    }

    public List<CurrencyData> getCurrencies()  {
        return get(CurrencyData.class);
    }

    // we need in future to create DbconsantsService and move utility method to service class

    public List<Pair<String, String>> getAccountTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.ACCOUNT_TYPE_CD.class);
    }

    public List<Pair<String, String>> getBudgetAccrualTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.BUDGET_ACCRUAL_TYPE_CD.class);
    }

    public List<Pair<String, String>> getTimeZones() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.TIME_ZONE_CD.class);
    }

    public List<Pair<String, String>> getLocales() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.LOCALE_CD.class);
    }

    public List<Pair<String, String>> getBusEntityStatuseCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.BUS_ENTITY_STATUS_CD.class);
    }

    public List<Pair<String, String>> getDistributorStatusCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.DISTRIBUTOR_STATUS_CD.class);
    }

    public List<Pair<String, String>> getTradingPartnerStatusCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.TRADING_PARTNER_STATUS_CD.class);
    }

    public List<Pair<String, String>> getDistributorTypeCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.DISTRIBUTOR_TYPE_CD.class);
    }

    public List<Pair<String, String>> getLocationProductBundles() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.LOCATION_PRODUCT_BUNDLE.class);
    }

    public List<Pair<String, String>> getUserTypeCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.USER_TYPE_CD.class);
    }
    
    public List<Pair<String, String>> getAddressCountryCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.ADDRESS_COUNTRY_CD.class);
    }
    
    public List<Pair<String, String>> getUserRoleCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.USER_ROLE_CD.class);
    }
    
    public List<Pair<String, String>> getLocaleCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.LOCALE_CD.class);
    }

    public Pair<String, String> getLocaleDefaultCd() {
        return new Pair<String, String>(Constants.LOCALE_DEFAULT_CD, Constants.LOCALE_DEFAULT_CD);
    }

    public List<Pair<String, String>> getWorkflowStatusCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WORKFLOW_STATUS_CD.class);
    }
    public List<Pair<String, String>> getWorkflowTypeCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WORKFLOW_TYPE_CD.class);
    }
    public List<Pair<String, String>> getWorkflowRuleTypeCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WORKFLOW_RULE_TYPE_CD.class);
    }
    public List<Pair<String, String>> getWoWorkflowRuleTypeCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WO_WORKFLOW_RULE_TYPE_CD.class);
    }
    public List<Pair<String, String>> getWorkflowAssocActionTypeCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WORKFLOW_ASSOC_ACTION_TYPE_CD.class);
    }
    public List<Pair<String, String>> getWorkflowRuleExpressions() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WORKFLOW_RULE_EXPRESSION.class);
    }
    public List<Pair<String, String>> getWorkflowRuleActions() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WORKFLOW_RULE_ACTION.class);
    }
    public List<Pair<String, String>> getWoWorkflowRuleActions() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WO_WORKFLOW_RULE_ACTION.class);
    }
    public List<Pair<String, String>> getWoWorkflowNextRuleActions() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WO_WORKFLOW_NEXT_RULE_ACTION.class);
    }
    public List<Pair<String, String>> getWorkflowNextRuleActions() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WORKFLOW_NEXT_RULE_ACTION.class);
    }

    public List<Pair<String, String>> getMSDSPlugIns() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.MSDS_PLUGIN_CD.class);
    }

    public List<Pair<String, String>> getBudgetTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.BUDGET_TYPE_CD.class);
    }

    public List<Pair<String, String>> getCostCenterTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.COST_CENTER_TYPE_CD.class);
    }

    public List<Pair<String, String>> getCostCenterTaxTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.COST_CENTER_TAX_TYPE.class);
    }

    public List<Pair<String, String>> getCostCenterAccCatFilterType() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.COST_CENTER_FILTER_ACC_CAT_TYPE.class);
    }
    
    public List<Pair<String, String>> getGroupTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.GROUP_TYPE_CD.class);
    }
    
    public List<Pair<String, String>> getApplicationFunctionTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.APPLICATION_FUNCTIONS_TYPE.class);
    }
    
    public List<Pair<String, String>> getGroupStatuses() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.GROUP_STATUS_CD.class);
    }

    public List<Pair<String, String>> getHistoryObjectTypeCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.HISTORY_OBJECT_TYPE_CD.class);
    }

    public List<Pair<String, String>> getItemPropertyTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.ITEM_PROPERTY_CD.class);
    }

    public List<Pair<String, String>> getWeightUnitTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.WEIGHT_UNIT_CD.class);
    }

    public List<Pair<String, String>> getItemUomTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.ITEM_UOM_CD.class);
    }
    
    public List<Pair<String, String>> getPurchasingSystemCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.CUSTOMER_SYSTEM_APPROVAL_CD.class);
    }

    public List<Pair<String, String>> getUploadStatusesCds() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.UPLOAD_STATUS_CD.class);
    }

    public List<Pair<String, String>> getUploadItemProperties() {
        return RefCodeNamesKeys.getRefCodeValues(Constants.ITEM_LOADER_PROPERTY.class);
    }


    public String getDefaultUserGroup(){
       return Constants.DEFAULT_GROUP;
    }

    public int getMaxSizeHierarchyLevels(){
        return Constants.MAX_SITE_HIERARCHY_LEVELS;
    }

    public String getSiteHierarchyLongName() {
        return Constants.SITE_HIERARCHY_LONG_NAME;
    }

    public List<Class> getEmailObjects() {
        List<Class> x = new ArrayList<Class>();
        for (EmailMeta m : EmailMeta.values()) {
            x.add(m.getServiceClass());
        }
        return x;
    }

    public Map<String, List<String>> getEmailTypesByMetaObject() {

        Map<String, List<String>> x = new HashMap<String, List<String>>();

        List<String> allList = new ArrayList<String>();

        for (EmailMeta m : EmailMeta.values()) {
            List<String> l = new ArrayList<String>();
            for (EmailType emailType : m.getEmailTypes()) {
                l.add(emailType.name());
            }
            Collections.sort(l);
            x.put(m.getServiceClass().getName(), l);
            allList.addAll(l);
        }

        Collections.sort(allList);

        x.put(null, allList);
        x.put(Constants.EMPTY, allList);

        return x;
    }

    public LanguageData getLanguageByCd(String languageCd) {
        List<LanguageData> languages = getLanguages();
        for (LanguageData lang : languages) {
            if (lang.getLanguageCode().equals(languageCd)) {
                return lang;
            }
        }
        return null;
    }

    public LanguageData getLanguageByUIName(String countryName) {
        List<LanguageData> languages = getLanguages();
        for (LanguageData lang : languages) {
            if (lang.getUiName().equals(countryName)) {
                return lang;
            }
        }
        return null;
    }

    public List<LanguageData> getLangueagesByIds(List<Long> languageIds) {

        List<LanguageData> allLanguages = getSupportedLanguages();
        List<LanguageData> languages = new ArrayList<LanguageData>();
        for (Long languageId : languageIds) {
            for (LanguageData lang : allLanguages) {
                if (lang.getLanguageId().equals(languageId)) {
                    languages.add(lang);
                    break;
                }
            }
        }
        return languages;
    }

    public List<LanguageData> getSupportedLanguages() {
        List<LanguageData> allLanguages = getLanguages();
        List<LanguageData> supportedLanguages = new ArrayList<LanguageData>();
        for (LanguageData lang : allLanguages) {
            if (Utility.isTrue(lang.getSupported())) {
                supportedLanguages.add(lang);
            }
        }
        return supportedLanguages;

    }
    
    public CountryView getCountryByCd(String countryCd) {
        List<CountryView> countries = getCountries();
        for (CountryView country : countries) {
            if (country.getCountryCode().equals(countryCd)) {
                return country;
            }
        }
        return null;
    }
    
    public CountryView getCountry(String countryCoge) {
        List<CountryView> countries = getCountries();
        for (CountryView country : countries) {
            if (country.getShortDesc().equals(countryCoge)) {
                return country;
            }
        }
        return null;
    }

    public boolean getIsStateRequiredForCountry(String countryCode) {
        return getIsStateRequiredForCountry(countryCode, true);
    }

    private Boolean getIsStateRequiredForCountry(String countryCode, Boolean defaultResult) {

        CountryView country = getCountry(countryCode);
        if (country != null) {
           return Utility.isTrue(country.getUsesState());
        } else {
             if(defaultResult == null){
                 throw new ApplicationDataNotFoundException();
             }
        }

        return defaultResult;

    }


    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

    public List<String> getManagableUserTypes(String userType) {

        if (RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR.equals(userType)) {

            Map userTypeCodes = RefCodeNamesKeys.toMap(getUserTypeCds(), true);
            return new ArrayList<String>(userTypeCodes.keySet());

        } else if (RefCodeNames.USER_TYPE_CD.ADMINISTRATOR.equals(userType)) {

            Map userTypeCodes = RefCodeNamesKeys.toMap(getUserTypeCds(), true);
            userTypeCodes.remove(RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR);

            return new ArrayList<String>(userTypeCodes.keySet());

        } else if (RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR.equals(userType)) {

            return Utility.toList(
                    RefCodeNames.USER_TYPE_CD.MULTI_SITE_BUYER,
                    RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR
            );

        } else {

            return Utility.emptyList(String.class);

        }

    }

    public List<Pair<String, String>> getAllowedUserTypes(String userType) {
        List<Pair<String, String>> types = getUserTypeCds();
            
        Iterator<Pair<String, String>> it = types.iterator();
        Pair<String, String> element;
            
        if (RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR.equals(userType)) {
            return getUserTypeCds();
        } else if (RefCodeNames.USER_TYPE_CD.ADMINISTRATOR.equals(userType)) {
            while (it.hasNext()) {
                element = it.next();
                if (RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR.equals(element.getObject2())) {
                    it.remove();
                }
            }
            return types;
        } else if (RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR.equals(userType)) {
            while (it.hasNext()) {
                element = it.next();
                if (RefCodeNames.USER_TYPE_CD.MULTI_SITE_BUYER.equals(element.getObject2()) ||
                    RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR.equals(element.getObject2())) {
                    continue;
                } else {
                    it.remove();
                }
            }
            return types;
        } else {
            return new ArrayList<Pair<String, String>>();
        }

    }

    public List<Pair<String, String>> getMessageTypes() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.MESSAGE_TYPE_CD.class);
    }
    
    public List<Pair<String, String>> getOrderSources() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.ORDER_SOURCE_CD.class, false);
    }
    
    public List<Pair<String, String>> getOrderStatuses() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.ORDER_STATUS_CD.class, false);
    }
    
    public List<Pair<String, String>> getOrderItemStatuses() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.ORDER_ITEM_STATUS_CD.class, false);
    }
    
    public List<Pair<String, String>> getPurchaseOrderStatuses() {
        return RefCodeNamesKeys.getRefCodeValues(RefCodeNames.PURCHASE_ORDER_STATUS_CD.class, false);
    }
    
    public static Map<String, String> getApplicationFuntionsToTypeMap(){
    	if (applicationFunctionsToTypeMap != null)
    		return applicationFunctionsToTypeMap;
    	applicationFunctionsToTypeMap = new HashMap<String, String>();
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.FREIGHT_TABLE_ADMINISTRATION, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.DISCOUNT_ADMINISTRATION, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.DISCOUNT_ADMINISTRATION, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADMIN2_MGR_TAB_SITE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADMIN2_MGR_TAB_USER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADMIN2_MGR_TAB_ACCOUNT, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADMIN2_MGR_TAB_LOADER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADMIN2_MGR_SITE_LOADER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADMIN2_MGR_BUDGET_LOADER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADMIN2_MGR_SHOPPING_CONTROL_LOADER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADMIN2_MGR_TAB_PROFILE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.UI_MGR_TAB_GROUP, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.UI_MGR_TAB_SITE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.UI_MGR_TAB_USER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.UI_MGR_TAB_ACCOUNT, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_ACCOUNT, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_DISTRIBUTOR, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_SITE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_MANUFACTURER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_MESSAGES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_USERS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_GROUPS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_SETUP, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_EMAIL_TEMPLATES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_ORDERS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_CATALOGS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_CATEGORIES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_MASTER_ITEMS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_ITEM_LOADER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_COST_CENTERS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_PROFILE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);    	
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_CORPORATE_SCHED, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_BATCH_ORDERS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_ACCOUNT, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_MESSAGES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_EMAIL_TEMPLATES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_USERS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_SITE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_GROUP, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_MANUFACTURER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_SETUP, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_DISTRIBUTOR, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_CMS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_COST_CENTERS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.ADMIN_MENU);
    	
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.NEW_UI_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.CUSTOMER_ACCESS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_ASSETS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.CUSTOMER_ACCESS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_SERVICES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.CUSTOMER_ACCESS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_SHOPPING, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.CUSTOMER_ACCESS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_CONTENT, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.CUSTOMER_ACCESS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_DASHBOARD, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.CUSTOMER_ACCESS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_ORDERS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.CUSTOMER_ACCESS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.RUNTIME_INVOICE_TAB, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.CUSTOMER_ACCESS);
    	
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.EDIT_SITE_PAR_VALUES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.VIEW_SITE_PAR_VALUES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.EDIT_SITE_SHOPPING_CONTROLS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.VIEW_SHOPPING_CONTROLS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.EDIT_PROFILING, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADD_CUSTOMER_ORDER_NOTES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADD_SHIPPING_ORDER_COMMENTS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.VIEW_PARTIAL_ORD_CREDIT_CARD, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MODIFY_ORD_CREDIT_CARD, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.PO_MANIFESTING, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.PO_ADDRESS_PRINTING, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.PLACE_CONFIRMATION_ONLY_ORDER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.EXCLUDE_ORDER_FROM_BUDGET, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.VIEW_INVOICES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.RUN_SPENDING_ESTIMATOR, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.RECEIVING, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ADD_RE_SALE_ITEMS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.DISPLAY_COST_CENTER_DETAIL, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.EDIT_USER_PROFILE_NAME, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.PLACE_ORDER_REQUEST_SHIP_DATE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.PLACE_ORDER_MANDATORY_REQUEST_SHIP_DATE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.PLACE_ORDER_PROCESS_ORDER_ON, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.OVERRIDE_SHOPPING_RESTRICTION, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MODIFY_ORDER_ITEM_QTY, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.UPDATE_AUTO_DISTRO, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.VIEW_SPECIAL_ITEMS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.BYPASS_SMALL_ORDER_ROUTING, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.BYPASS_CUSTOMER_WORKFLOW, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.CUST_REQ_RESHIPMENT_ORDER_NUM, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.TRACKING_MAINTENANCE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.CHANGE_ORDER_BUDGET_PERIOD, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.EDIT_MESSAGES, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SHOPPING);
        
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.SHOP_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.AUTO_DISTRO_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.TRACK_ORDER_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.REPORTS_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.USER_PROFILE_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.STORE_PROFILE_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.PRODUCT_INFORMATION_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MSDS_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.FAQ_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.CONTACT_US_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MAINTENANCE_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MAINTENANCE_NEWS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MAINTENANCE_TEMPLATE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.MAINTENANCE_FAQ, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.APPROVE_ORDERS_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.CHANGE_LOCATION_ACCESS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.XPEDX);
    	
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ASSET_WO_VIEW_ALL_FOR_STORE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SERVICES_ASSETS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ASSET_ADMINISTRATOR, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SERVICES_ASSETS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ASSET_USER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SERVICES_ASSETS);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.WORK_ORDER_APPROVER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.SERVICES_ASSETS);
    	
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.CRC_MANAGER, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.OTHER);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.SPECIAL_PERMISSION_ITEMS, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.OTHER);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.INVENTORY_EARLY_RELEASE, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.OTHER);
    	applicationFunctionsToTypeMap.put(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_HISTORY, RefCodeNames.APPLICATION_FUNCTIONS_TYPE.OTHER);

    	return applicationFunctionsToTypeMap;
    }

    private class ConstantResource extends HashMap<Class, ConstantResourceObject> {


    }


    private class CountryResourceDecoration extends  ConstantResourceObject {
        public CountryResourceDecoration(List<CountryView> list) throws Exception {
            super(list, CountryView.COUNTRY_ID);
        }
    }

    private class LanguageResourceDecoration extends  ConstantResourceObject {
        public LanguageResourceDecoration(List<LanguageData> list) throws Exception {
            super(list, LanguageData.LANGUAGE_ID);
        }
    }

    private class CurrencyResourceDecoration extends  ConstantResourceObject {
        public CurrencyResourceDecoration(List<CurrencyData> list) throws Exception {
            super(list, CurrencyData.CURRENCY_ID);
        }
    }

    private class ConstantResourceObject extends ArrayList<ConstantResourceFields> {

        public ConstantResourceObject(List list, String fieldId) throws Exception {
            super(list.size());
            for (Object x : list) {
                ConstantResourceFields fields = new ConstantResourceFields(x);
                add(fields);
            }
        }

        public Collection<ConstantResourceFields> sortedValues(final String field, final boolean asc) {

            List<ConstantResourceFields> l = new ArrayList<ConstantResourceFields>(this);

            Comparator<ConstantResourceFields> c = new Comparator<ConstantResourceFields>() {
                @Override
                public int compare(ConstantResourceFields o1, ConstantResourceFields o2) {

                    Object v1 = o1.get(field);
                    Object v2 = o2.get(field);

                    String v1Str = Utility.isSet(v1) ? v1.toString() : Constants.EMPTY;
                    String v2Str = Utility.isSet(v1) ? v1.toString() : Constants.EMPTY;

                    return (asc ? 1 : -1) * (v1Str.compareTo(v2Str));
                }
            };

            Collections.sort(l, c);

            return l;
        }

    }


    private class ConstantResourceFields extends HashMap<String, Object> {

        private Object value;

        public Object getValue() {
            return value;
        }

        public ConstantResourceFields(Object object) throws Exception {
            super();
            this.value = object;
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                put(field.getName(), field.get(object));
            }
        }

    }
}
