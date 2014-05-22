package com.espendwise.manta.web.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.auth.AuthDatabaseAccessUnit;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.service.MainDbService;
import com.espendwise.manta.service.StoreService;
import com.espendwise.manta.service.UserLogonService;
import com.espendwise.manta.service.OrderGuideService;
import com.espendwise.manta.util.*;
import com.espendwise.manta.i18n.I18nUtil;

import com.espendwise.manta.util.FiscalCalendarUtility;
import com.espendwise.manta.util.PasswordUtil;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.UserRightsTool;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.ArgumentType;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.format.AppI18nFormatter;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.web.SiteOptionsForm;
import com.espendwise.manta.web.forms.AccountContentManagementForm;
import com.espendwise.manta.web.forms.AccountFiscalCalendarForm;
import com.espendwise.manta.web.forms.AccountForm;
import com.espendwise.manta.web.forms.AccountPropertiesForm;
import com.espendwise.manta.web.forms.AddressInputForm;
import com.espendwise.manta.web.forms.ContactInputForm;
import com.espendwise.manta.web.forms.DeliveryScheduleForm;
import com.espendwise.manta.web.forms.DistributorConfigurationForm;
import com.espendwise.manta.web.forms.DistributorForm;
import com.espendwise.manta.web.forms.EmailTemplateForm;
import com.espendwise.manta.web.forms.FilterResult;
import com.espendwise.manta.web.forms.FiscalCalendarForm;
import com.espendwise.manta.web.forms.ManufacturerForm;
import com.espendwise.manta.web.forms.CMSForm;
import com.espendwise.manta.web.forms.OrderForm;
import com.espendwise.manta.web.forms.SiteForm;
import com.espendwise.manta.web.forms.UserContactInputForm;
import com.espendwise.manta.web.forms.UserForm;
import com.espendwise.manta.web.forms.WorkflowForm;
import com.espendwise.manta.model.view.CumulativeSummaryView;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class WebFormUtil {

    private static final Logger logger = Logger.getLogger(WebFormUtil.class);

    public static AddressInputForm createAddressForm(AddressData address) {

        AddressInputForm form = new AddressInputForm();

        if (address != null) {

            form.setAddress1(address.getAddress1());
            form.setAddress2(address.getAddress2());
            form.setAddress3(address.getAddress3());
            form.setAddress4(address.getAddress4());
            form.setCity(address.getCity());
            form.setCountryCd(address.getCountryCd());
            form.setStateProvinceCd(address.getStateProvinceCd());
            form.setPostalCode(address.getPostalCode());
            form.setCountyCd(address.getCountyCd());
            form.setPrimaryInd(address.getPrimaryInd());

        }

        return form;
    }


    public static ContactInputForm createContactForm(ContactView contact) {

        ContactInputForm form = new ContactInputForm();

        form.setFirstName(contact.getAddress().getName1());
        form.setLastName(contact.getAddress().getName2());
        form.setAddress(createAddressForm(contact.getAddress()));
        form.setEmail(Utility.strNN(contact.getEmail() != null ? contact.getEmail().getEmailAddress() : null));
        form.setTelephone(Utility.strNN(contact.getPhone() != null ? contact.getPhone().getPhoneNum() : null));
        form.setMobile(Utility.strNN(contact.getMobilePhone() != null ? contact.getMobilePhone().getPhoneNum() : null));
        form.setFax(Utility.strNN(contact.getFaxPhone() != null ? contact.getFaxPhone().getPhoneNum() : null));

        return form;

    }

    public static AccountIdentPropertiesView createAccountIdentProperties(AccountIdentPropertiesView properties, AccountForm accountForm) {

        if (properties == null) {
            properties = new AccountIdentPropertiesView();
        }

        properties.setAccountId(accountForm.getAccountId());
        if (properties.getBudgetType() != null) {
            properties.getBudgetType().setValue(accountForm.getAccountBudgetType());
        } else {
            properties.setBudgetType(
                    PropertyUtil.toProperty(
                            accountForm.getAccountId(),
                            AccountIdentPropertyTypeCode.BUDGET_ACCRUAL_TYPE_CD,
                            accountForm.getAccountBudgetType()
                    )
            );
        }

        if (properties.getAccountType() != null) {

            properties.getAccountType().setValue(accountForm.getAccountType());

        } else {

            properties.setAccountType(
                    PropertyUtil.toProperty(
                            accountForm.getAccountId(),
                            AccountIdentPropertyTypeCode.ACCOUNT_TYPE,
                            accountForm.getAccountType()
                    )
            );
        }

        if (properties.getDistributorReferenceNumber() != null) {

            properties.getDistributorReferenceNumber().setValue(accountForm.getDistributorReferenceNumber());

        } else {

            properties.setDistributorReferenceNumber(
                    PropertyUtil.toProperty(
                            accountForm.getAccountId(),
                            AccountIdentPropertyTypeCode.DIST_ACCT_REF_NUM,
                            accountForm.getDistributorReferenceNumber()
                    )
            );
        }
        if (!Utility.isSet(properties.getDefaultProperties())){
        	properties.setDefaultProperties(
        			PropertyUtil.toDefaultProperties(accountForm.getAccountId())
        	);
        }
        return properties;
    }

    public static AccountContactView createAccountContact(AccountContactView accountContact, AccountForm accountForm) {

        if (accountContact == null) {
            accountContact = new AccountContactView();
        }

        accountContact.setAccountId(accountForm.getAccountId());

        AddressData billingAddress = accountContact.getBillingAddress();
        if (billingAddress == null) {
            billingAddress = new AddressData();
        }

        billingAddress.setAddressStatusCd(RefCodeNames.PROPERTY_STATUS_CD.ACTIVE);
        billingAddress.setBusEntityId(accountForm.getAccountId());
        billingAddress.setPrimaryInd(true);
        billingAddress.setAddress1(accountForm.getBillingAddress().getAddress1());
        billingAddress.setAddress2(accountForm.getBillingAddress().getAddress2());
        billingAddress.setAddress3(accountForm.getBillingAddress().getAddress3());
        billingAddress.setCity(accountForm.getBillingAddress().getCity());
        billingAddress.setCountryCd(accountForm.getBillingAddress().getCountryCd());
        billingAddress.setPostalCode(accountForm.getBillingAddress().getPostalCode());
        billingAddress.setStateProvinceCd(accountForm.getBillingAddress().getStateProvinceCd());
        billingAddress.setPrimaryInd(accountForm.getBillingAddress().getPrimaryInd());
        billingAddress.setAddressTypeCd(RefCodeNames.ADDRESS_TYPE_CD.BILLING);

        accountContact.setBillingAddress(billingAddress);

        ContactView primaryContact = accountContact.getPrimaryContact();
        if (primaryContact == null) {
            primaryContact = new ContactView();
        }

        EmailData email = primaryContact.getEmail();

        if (email == null) {
            email = new EmailData();
        }

        email.setEmailStatusCd(RefCodeNames.EMAIL_STATUS_CD.ACTIVE);
        email.setBusEntityId(accountForm.getAccountId());
        email.setEmailAddress(accountForm.getPrimaryContact().getEmail());
        email.setEmailTypeCd(RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT);
        email.setPrimaryInd(true);

        primaryContact.setEmail(email);


        PhoneData phone = primaryContact.getPhone();
        if (phone == null) {
            phone = new PhoneData();
        }

        phone.setBusEntityId(accountForm.getAccountId());
        phone.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.PHONE);
        phone.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
        phone.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_PHONE);
        phone.setPhoneNum(accountForm.getPrimaryContact().getTelephone());
        phone.setPrimaryInd(true);

        primaryContact.setPhone(phone);

        PhoneData fax = primaryContact.getFaxPhone();
        if (fax == null) {
            fax = new PhoneData();
        }

        fax.setBusEntityId(accountForm.getAccountId());
        fax.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.FAX);
        fax.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
        fax.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_FAX);
        fax.setPhoneNum(accountForm.getPrimaryContact().getFax());
        fax.setPrimaryInd(true);

        primaryContact.setFaxPhone(fax);

        PhoneData mobilePhone = primaryContact.getMobilePhone();
        if (mobilePhone == null) {
            mobilePhone = new PhoneData();
        }

        mobilePhone.setBusEntityId(accountForm.getAccountId());
        mobilePhone.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.MOBILE);
        mobilePhone.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
        mobilePhone.setPhoneNum(accountForm.getPrimaryContact().getMobile());

        primaryContact.setMobilePhone(mobilePhone);

        AddressData primaryAddress = primaryContact.getAddress();
        if (primaryAddress == null) {
            primaryAddress = new AddressData();
        }

        primaryAddress.setBusEntityId(accountForm.getAccountId());
        primaryAddress.setName1(accountForm.getPrimaryContact().getFirstName());
        primaryAddress.setName2(accountForm.getPrimaryContact().getLastName());
        primaryAddress.setAddress1(accountForm.getPrimaryContact().getAddress().getAddress1());
        primaryAddress.setAddress2(accountForm.getPrimaryContact().getAddress().getAddress2());
        primaryAddress.setAddress3(accountForm.getPrimaryContact().getAddress().getAddress3());
        primaryAddress.setAddress4(accountForm.getPrimaryContact().getAddress().getAddress4());
        primaryAddress.setCountryCd(accountForm.getPrimaryContact().getAddress().getCountryCd());
        primaryAddress.setPostalCode(accountForm.getPrimaryContact().getAddress().getPostalCode());
        primaryAddress.setStateProvinceCd(accountForm.getPrimaryContact().getAddress().getStateProvinceCd());
        primaryAddress.setAddressTypeCd(RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        primaryAddress.setAddressStatusCd(RefCodeNames.ADDRESS_STATUS_CD.ACTIVE);
        primaryAddress.setCity(accountForm.getPrimaryContact().getAddress().getCity());
        primaryAddress.setPrimaryInd(true);

        primaryContact.setAddress(primaryAddress);

        accountContact.setPrimaryContact(primaryContact);

        return accountContact;

    }

    public static BusEntityData createBusEntityData(BusEntityData busEntityData, AccountForm accountForm) {

        if (busEntityData == null) {
            busEntityData = new BusEntityData();
        }
        busEntityData.setLocaleCd(Constants.UNK);
        busEntityData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.UNKNOWN);
        busEntityData.setBusEntityStatusCd(accountForm.getAccountStatus());
        busEntityData.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.ACCOUNT);
        busEntityData.setShortDesc(accountForm.getAccountName());
        busEntityData.setTimeZoneCd(accountForm.getTimeZone());

        return busEntityData;

    }

    public static BusEntityData createBusEntityData(BusEntityData busEntityData, ManufacturerForm manufacturerForm) {

        if (busEntityData == null) {
            busEntityData = new BusEntityData();
        }
        busEntityData.setLocaleCd(Constants.UNK);
        busEntityData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.UNKNOWN);
        busEntityData.setBusEntityStatusCd(manufacturerForm.getManufacturerStatus());
        busEntityData.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.MANUFACTURER);
        busEntityData.setShortDesc(manufacturerForm.getManufacturerName());

        return busEntityData;

    }

    public static BusEntityData createBusEntityData(BusEntityData busEntityData, DistributorForm distributorForm) {

        if (busEntityData == null) {
            busEntityData = new BusEntityData();
        }
        String locale = distributorForm.getDistributorLocale();
        if (!Utility.isSet(locale)) {
        	locale = Constants.UNK;
        }
        busEntityData.setLocaleCd(locale);
        busEntityData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.UNKNOWN);
        busEntityData.setBusEntityStatusCd(distributorForm.getDistributorStatus());
        busEntityData.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.DISTRIBUTOR);
        busEntityData.setShortDesc(distributorForm.getDistributorName());

        return busEntityData;

    }

    public static BusEntityData createBusEntityData(AppLocale locale, BusEntityData busEntityData, SiteForm siteForm) {

        if (busEntityData == null) {
            busEntityData = new BusEntityData();
        }

        busEntityData.setLocaleCd(Constants.UNK);
        busEntityData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.UNKNOWN);
        busEntityData.setBusEntityStatusCd(siteForm.getStatus());
        busEntityData.setBusEntityTypeCd(RefCodeNames.BUS_ENTITY_TYPE_CD.SITE);
        busEntityData.setShortDesc(siteForm.getSiteName());
        busEntityData.setEffDate(AppI18nUtil.parseDateNN(locale, siteForm.getEffDate()));
        busEntityData.setExpDate(AppI18nUtil.parseDateNN(locale, siteForm.getExpDate()));

        return busEntityData;

    }

    public static TemplateData createTemplateData(Long storeId, TemplateData templateData, EmailTemplateForm emailTemplateForm) {

        if (templateData == null) {
            templateData = new TemplateData();
        }

        templateData.setBusEntityId(storeId);
        templateData.setName(emailTemplateForm.getTemplateName());
        templateData.setType(RefCodeNames.TEMPLATE_TYPE_CD.EMAIL);
        templateData.setContent(emailTemplateForm.getTemplateContent());

        return templateData;
    }

    public static List<TemplatePropertyData> createEmailTemplateIdentProperties(List<TemplatePropertyData> properties, EmailTemplateForm emailTemplateForm) {

        if (properties == null) {
            properties = Utility.emptyList(TemplatePropertyData.class);
        }

        TemplatePropertyData localeProperty = (TemplatePropertyData) PropertyUtil.find(properties, RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.LOCALE);
        if (localeProperty != null) {
            localeProperty.setValue(emailTemplateForm.getTemplateLocaleCode());
        } else {
            localeProperty = PropertyUtil.createTemplateProperty(emailTemplateForm.getTemplateId(),
                    RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.LOCALE,
                    emailTemplateForm.getTemplateLocaleCode()
            );
            properties.add(localeProperty);
        }


        TemplatePropertyData subjectProperty = (TemplatePropertyData) PropertyUtil.find(properties, RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.SUBJECT);
        if (subjectProperty != null) {
            subjectProperty.setValue(emailTemplateForm.getTemplateSubject());
        } else {
            subjectProperty = PropertyUtil.createTemplateProperty(emailTemplateForm.getTemplateId(),
                    RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.SUBJECT,
                    emailTemplateForm.getTemplateSubject()
            );
            properties.add(subjectProperty);
        }

        TemplatePropertyData emailTypeProperty = (TemplatePropertyData) PropertyUtil.find(properties, RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.EMAIL_TYPE);
        if (emailTypeProperty != null) {
            emailTypeProperty.setValue(emailTemplateForm.getEmailTypeCode());
        } else {
            emailTypeProperty = PropertyUtil.createTemplateProperty(emailTemplateForm.getTemplateId(),
                    RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.EMAIL_TYPE,
                    emailTemplateForm.getEmailTypeCode()
            );
            properties.add(emailTypeProperty);
        }

        TemplatePropertyData emailMetaObjProperty = (TemplatePropertyData) PropertyUtil.find(properties, RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.EMAIL_OBJECT);
        if (emailMetaObjProperty != null) {
            emailMetaObjProperty.setValue(emailTemplateForm.getEmailObject());
        } else {
            emailMetaObjProperty = PropertyUtil.createTemplateProperty(emailTemplateForm.getTemplateId(),
                    RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.EMAIL_OBJECT,
                    emailTemplateForm.getEmailObject()
            );
            properties.add(emailMetaObjProperty);
        }

        return properties;
    }

    public static void removeObjectFromFilterResult(FilterResult filterResult, Long templateId) {

        if (Utility.isSet(filterResult)) {
            if (Utility.isSet(filterResult.getResult())) {
                Iterator it = filterResult.getResult().iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (Utility.longNN(Utility.getId(o)) == templateId) {
                        it.remove();
                        break;
                    }
                }
            }
        }

    }

    public static void removeObjectFromFilterResult(WebRequest request, String key, Long templateId) {
        Object filterResultForm = request.getAttribute(key, WebRequest.SCOPE_SESSION);
        if (filterResultForm instanceof FilterResult) {
            WebFormUtil.removeObjectFromFilterResult((FilterResult) filterResultForm, templateId);
        }
    }

    public static void refreshHeader(WebRequest request, String key, Long id, String name) {

        Object obj = request.getAttribute(key, WebRequest.SCOPE_SESSION);
        if (obj != null && obj instanceof EntityHeaderView) {
            EntityHeaderView header = (EntityHeaderView) obj;
            header.setId(id);
            header.setShortDesc((name));
        }

    }

    public static void removeHeader(WebRequest request, String key) {
        request.removeAttribute(key, WebRequest.SCOPE_SESSION);
    }

    public static FiscalCalendarForm createFiscalCalendarForm(AppLocale locale, FiscalCalendarIdentView calendarToEdit) {

        FiscalCalendarForm f = new FiscalCalendarForm();

        Integer fiscalYear = Utility.intNN(calendarToEdit.getFiscalCalendarData().getFiscalYear());

        Date effDate = calendarToEdit.getFiscalCalendarData().getEffDate();
        Date expDate = calendarToEdit.getFiscalCalendarData().getExpDate();

        f.setFiscalCalendarId(calendarToEdit.getFiscalCalendarData().getFiscalCalenderId());
        f.setEffDate(Args.toViewValue(effDate, ArgumentType.DATE, locale));
        f.setExpDate(Args.toViewValue(expDate, ArgumentType.DATE, locale));
        f.setFiscalYear(Args.toViewValue(fiscalYear, ArgumentType.NUMBER, locale));
        f.setPeriodCd(calendarToEdit.getFiscalCalendarData().getPeriodCd());
        f.setServiceScheduleCalendar(calendarToEdit.isServiceScheduleCalendar());
        f.setEditable( new Date().before(FiscalCalendarUtility.calcExpDate(fiscalYear, expDate)));

        f.setPeriods(new TreeMap<Integer, String>());
        for (FiscalCalenderDetailData p : calendarToEdit.getPeriods()) {
            f.getPeriods().put(p.getPeriod(),
                    Args.toViewValue(
                            Utility.isSet(p.getMmdd()) ? Parse.parseMonthWithDay(p.getMmdd(), fiscalYear, Constants.SYSTEM_MONTH_WITH_DAY_PATTERN) : null,
                            ArgumentType.MONTH_WITH_DAY,
                            locale
                    )
            );
        }

        return f;
    }

    public static FiscalCalenderData createCalendarData(AppLocale locale, FiscalCalenderData fiscalCalendarData, AccountFiscalCalendarForm form) {


        if (fiscalCalendarData == null) {
            fiscalCalendarData = new FiscalCalenderData();
            fiscalCalendarData.setBusEntityId(form.getAccountId());
            fiscalCalendarData.setPeriodCd(RefCodeNames.BUDGET_PERIOD_CD.MONTHLY);
            fiscalCalendarData.setShortDesc(RefCodeNames.FISCAL_CALENDER_NAME.FISCAL_CALENDER);
        }

        int fiscalYear = Utility.strNN(form.getCalendarToEdit().getFiscalYear()).equalsIgnoreCase(AppI18nUtil.getMessage("admin.account.fiscalCalendar.all"))
                ? 0
                : Parse.parseInt(form.getCalendarToEdit().getFiscalYear());

        fiscalCalendarData.setFiscalYear(fiscalYear);
        fiscalCalendarData.setEffDate(Parse.parseDate(form.getCalendarToEdit().getEffDate(), locale));
        fiscalCalendarData.setExpDate(Parse.parseDateNN(form.getCalendarToEdit().getExpDate(), locale));

        return fiscalCalendarData;

    }

    public static List<FiscalCalenderDetailData> createFiscalCalendarPeriods(AppLocale locale, FiscalCalenderData calendar, List<FiscalCalenderDetailData> periods, AccountFiscalCalendarForm form) {

        if (periods == null) {
            periods = new ArrayList<FiscalCalenderDetailData>();
        }

        Map<Integer, FiscalCalenderDetailData> periodsMap = FiscalCalendarUtility.toPeriodMap(periods);

        for (Map.Entry<Integer, String> formPeriods : form.getCalendarToEdit().getPeriods().entrySet()) {

            Integer periodNumber = formPeriods.getKey();
            String periodMmdd = formPeriods.getValue();

            FiscalCalenderDetailData period = periodsMap.get(periodNumber);

            if (period == null) {
                period = new FiscalCalenderDetailData();
                period.setPeriod(periodNumber);
                periodsMap.put(periodNumber, period);
            }

            Date mmddDate = Utility.isSet(periodMmdd) ? AppI18nUtil.parseMonthWithDay(periodMmdd, Constants.NOT_LEAP_YEAR) : null;
            period.setMmdd(Args.toDbValue(mmddDate, ArgumentType.MONTH_WITH_DAY));

        }

        return new ArrayList<FiscalCalenderDetailData>(periodsMap.values());

    }

    public static void fillOutUserForm(UserLogonService userLogonService,
                                       StoreService storeService,
                                       Long globalEntityId,
                                       Long storeId,
                                       UserForm form,
                                       UserIdentView userInfo,
                                       AppLocale locale,
                                       boolean mainDbAlive,
                                       boolean multiStoreDb) {

        if (form == null) {
            form = new UserForm();
        }

        if (userInfo.getUserData().getUserId() != null &&  userInfo.getUserData().getUserId() > 0) {

            form.setUserId(userInfo.getUserData().getUserId());
            form.setUserLogonName(userInfo.getUserData().getUserName());
            form.setUserType(userInfo.getUserData().getUserTypeCd());
            form.setUserPassword(null);
            form.setUserConfirmPassword(null);
            if (userInfo.getUserData().getPrefLocaleCd() != null) {
                StringTokenizer st = new StringTokenizer(userInfo.getUserData().getPrefLocaleCd(), "_");
                String lang = "";
                for (int i = 0; st.hasMoreTokens() && i < 2; i++) {
                    if (i == 0) lang = st.nextToken();
                }
                form.setUserLanguage(lang);
            }

            form.setUserStatus(userInfo.getUserData().getUserStatusCd());
            form.setUserActiveDate(AppI18nUtil.formatDate(locale, userInfo.getUserData().getEffDate()));
            form.setUserInactiveDate(AppI18nUtil.formatDate(locale, userInfo.getUserData().getExpDate()));
            form.setApproveOrders(RefCodeNames.WORKFLOW_ROLE_CD.ORDER_APPROVER.equals(userInfo.getUserData().getWorkflowRoleCd()));
            
            AddressInputForm addressForm = new AddressInputForm();

            ContactInputForm contactForm = new ContactInputForm();
            contactForm.setAddress(addressForm);

            UserContactInputForm userContactForm = new UserContactInputForm();
            userContactForm.setContact(contactForm);

            userContactForm.getContact().setFirstName(Utility.strNN(userInfo.getUserData().getFirstName()));
            userContactForm.getContact().setLastName(Utility.strNN(userInfo.getUserData().getLastName()));

            if (userInfo.getPhoneData() != null) {
                userContactForm.getContact().setTelephone(userInfo.getPhoneData().getPhoneNum());
            }
            if (userInfo.getFaxPhoneData() != null) {
                userContactForm.getContact().setFax(userInfo.getFaxPhoneData().getPhoneNum());
            }
            if (userInfo.getMobilePhoneData() != null) {
                userContactForm.getContact().setMobile(userInfo.getMobilePhoneData().getPhoneNum());
            }
            if (userInfo.getEmailData() != null) {
                userContactForm.getContact().setEmail(userInfo.getEmailData().getEmailAddress());
            }

            if (userInfo.getAddressData() != null) {

                userContactForm.getContact().getAddress().setAddress1(Utility.strNN(userInfo.getAddressData().getAddress1()));
                userContactForm.getContact().getAddress().setAddress2(Utility.strNN(userInfo.getAddressData().getAddress2()));
                userContactForm.getContact().getAddress().setCity(Utility.strNN(userInfo.getAddressData().getCity()));
                userContactForm.getContact().getAddress().setPostalCode(Utility.strNN(userInfo.getAddressData().getPostalCode()));
                userContactForm.getContact().getAddress().setStateProvinceCd(Utility.strNN(userInfo.getAddressData().getStateProvinceCd()));

                Long addressId = userInfo.getAddressData().getAddressId();
                if (addressId != null && addressId > 0) {
                    userContactForm.getContact().getAddress().setCountryCd(userInfo.getAddressData().getCountryCd());
                }
            }

            if (userInfo.getEscalationEmailData() != null) {
                userContactForm.setEscalationEmail(userInfo.getEscalationEmailData().getEmailAddress());
            }
            
            if (userInfo.getSmsEmailData() != null) {
                userContactForm.setTextingAddress(userInfo.getSmsEmailData().getEmailAddress());
            }

            UserRightsTool urt = new UserRightsTool(userInfo.getUserData());

            form.setApproveOrders(RefCodeNames.WORKFLOW_ROLE_CD.ORDER_APPROVER.equals(userInfo.getUserData().getWorkflowRoleCd()));
            form.setUpdateBillToInformation(urt.canEditBillTo());
            form.setUpdateShipToInformation(urt.canEditShipTo());
            form.setCreditCard(urt.getCreditCardFlag());
            form.setOtherPayment(urt.getOtherPaymentFlag());
            form.setOnAccount(urt.getOnAccount());
            form.setBrowseOnly(urt.getBrowseOnly());
            form.setDoesNotUseReporting(urt.getNoReporting());
            form.setShowPrices(urt.getShowPrice());
            form.setPoNumRequired(urt.getPoNumRequired());

            if (Utility.isSet(userInfo.getUserProperties())) {
                for (PropertyData property : userInfo.getUserProperties()) {
                    if (RefCodeNames.PROPERTY_TYPE_CD.USER_ID_CODE.equals(property.getPropertyTypeCd())) {
                        form.setUserCode(property.getValue());
                    } else if (RefCodeNames.PROPERTY_TYPE_CD.CORPORATE_USER.equals(property.getPropertyTypeCd())) {
                        form.setCorporateUser(Boolean.parseBoolean(property.getValue()));
                    } else if (RefCodeNames.PROPERTY_TYPE_CD.IVR_UIN.equals(property.getPropertyTypeCd())) {
                        form.setIvrUserIdentificationNumber(property.getValue());
                    } else if (RefCodeNames.PROPERTY_TYPE_CD.IVR_PIN.equals(property.getPropertyTypeCd())) {
                        form.setIvrPIN(property.getValue());
                        form.setIvrConfirmPIN(property.getValue());
                    }
                }
            }

            if (urt.isServiceVendorRole()) {
                form.setUserRole(RefCodeNames.USER_ROLE_CD.SERVICE_VENDOR);
            } else if (urt.isSiteManagerRole()) {
                form.setUserRole(RefCodeNames.USER_ROLE_CD.SITE_MANAGER);
            } else {
                form.setUserRole(RefCodeNames.USER_ROLE_CD.UNKNOWN);
            }

            form.setUserContact(userContactForm);

        } else {
            form.setUserId((long) 0);
            form.setUserRole(RefCodeNames.USER_ROLE_CD.UNKNOWN);
        }
        // user languages
        form.setAvailableLanguages(getStoreAvailableLanguages(storeService, storeId));

        AddressData addressData = userInfo.getAddressData();
        if (addressData == null || ((addressData.getAddressId() == null || addressData.getAddressId() == 0))) {

            InstanceView store = userLogonService.findInstanceByGlobalId(globalEntityId);
            Collection<AddressData> addresses = store.getStore().getAddresses();

            if (Utility.isSet(addresses)) {

                UserContactInputForm userContactForm = form.getUserContact();
                if (userContactForm == null) {
                    userContactForm = new UserContactInputForm();
                }
                if (userContactForm.getContact() == null) {
                    userContactForm.setContact(new ContactInputForm());
                }
                if (userContactForm.getContact().getAddress() == null) {
                    userContactForm.getContact().setAddress(new AddressInputForm());
                }

                for (AddressData address : addresses) {
                    if (Utility.isSet(address.getCountryCd())) {
                        userContactForm.getContact().getAddress().setCountryCd(address.getCountryCd());
                        break;
                    }
                }
            }
        }

        form.setMainDbAlive(mainDbAlive);
        form.setMultiStoreDb(multiStoreDb);

        form.setUserInfo(userInfo);

    }

    public static List<LanguageData> getStoreAvailableLanguages(StoreService storeService, Long storeId) {
            List<StoreProfileData> storeProfileList = storeService.findStoreProfile(storeId);
            List<Long> langIds = new ArrayList<Long>();
            if (storeProfileList != null) {
                for (StoreProfileData profile : storeProfileList) {
                    if (profile.getOptionTypeCd().equals(RefCodeNames.STORE_PROFILE_TYPE_CD.LANGUAGE_OPTION))  {
                        try {
                            Long langId = Long.parseLong(profile.getShortDesc());
                            langIds.add(langId);
                        }   catch (Exception e) {
                        }
                    }
                }
            }
            List<LanguageData> result = null;
            DbConstantResource resource = AppResourceHolder.getAppResource().getDbConstantsResource();
            if (langIds.size() > 0) {
                result =  resource.getLangueagesByIds(langIds);
            }
            if (result == null || result.size() == 0) {
                result =  resource.getSupportedLanguages();
            }
            LanguageData piglatinLanguage = resource.getLanguageByCd(RefCodeNames.LOCALE_CD.XX_PIGLATIN);
            result.add(piglatinLanguage);
            WebSort.sort(result, LanguageData.UI_NAME);
            return result;
    }

    public static void setUserStoreAssocLists(UserLogonService userLogonService,
                                              MainDbService mainDbService,
                                              StoreService storeServoice,
                                              UserForm form,
                                              AppStoreContext storeContext,
                                              Long currentUserId,
                                              String currentUserName,
                                              boolean overwrite,
                                              boolean  multiStoreDb) {

        if (!Utility.isSet(form.getAllAdminStores()) || overwrite) {
            List<InstanceView> adminAvailableStores = userLogonService.getUserStoreDataSources();
            form.setAllAdminStores(ConvX.convertToAllStoreIdent(adminAvailableStores, multiStoreDb));
        }

        if (!Utility.isSet(form.getUserStores()) || overwrite) {

            if (Utility.isSet(currentUserName)) {

                if (multiStoreDb) {
                    Map<String, AuthDatabaseAccessUnit> userStores = mainDbService.getUserDataAccessMainUnits(currentUserName);
                    form.setUserStores(ConvX.convertToAllStoreIdent(userStores));
                } else {
                    List<BusEntityData> currentUserStores = storeServoice.findUserStores(currentUserId);
                    form.setUserStores(ConvX.convertToAllStoreIdent(storeContext.getDataSource(), currentUserStores));
                }

            } else {

                form.setUserStores(new ArrayList<AllStoreIdentificationView>());

            }

        }

        if (!Utility.isSet(form.getCurrentStore()) || overwrite) {

            form.setCurrentStore(new ArrayList<AllStoreIdentificationView>());
            form.getCurrentStore().add(new AllStoreIdentificationView(
                    multiStoreDb?storeContext.getMainStoreIdent().getId():storeContext.getStoreId(),
                    storeContext.getStoreId(),
                    storeContext.getStoreName(),
                    storeContext.getDataSource().getDataSourceIdent().getDataSourceName(),
                    true));

        }


    }


    public static UserIdentView fillOutUserIdenView(UserIdentView userInfo, UserForm userForm, AppLocale locale,boolean multiStoreDb) {

        if (userInfo == null) {
            userInfo = new UserIdentView(new UserData(),
                    new EmailData(),
                    new AddressData(),
                    new PhoneData(),
                    new PhoneData(),
                    new PhoneData(),
                    new EmailData(),
                    new EmailData(),
                    new ArrayList<UserAssocData>(),
                    new ArrayList<PropertyData>(),
                    new ArrayList<AllStoreIdentificationView>(),
                    new ArrayList<GroupAssocData>());
        }
        // ========================= USER DATA =========================
        UserData userData = userInfo.getUserData();
        if (userData == null) {
            userData = new UserData();
        }
        
        userData.setUserId(userForm.getUserId() == 0 ? null : userForm.getUserId());
        userData.setUserName(userForm.getUserLogonName());
        userData.setFirstName(userForm.getUserContact().getContact().getFirstName());
        userData.setLastName(userForm.getUserContact().getContact().getLastName());

        if (Utility.isSet(userForm.getUserPassword())) {
            String encodedPassword = PasswordUtil.getHash(userForm.getUserLogonName(), userForm.getUserPassword());
            userData.setPassword(encodedPassword);
        }

        userData.setEffDate(AppI18nUtil.parseDateNN(locale, userForm.getUserActiveDate()));
        userData.setExpDate(AppI18nUtil.parseDateNN(locale, userForm.getUserInactiveDate()));

        String userLocaleStr = userData.getPrefLocaleCd();
        String languageCode = userForm.getUserLanguage();
        String countryCode = userForm.getUserContact().getContact().getAddress().getCountryCd();
		DbConstantResource dbResource = AppResourceHolder.getAppResource().getDbConstantsResource();
		LanguageData langData =  dbResource.getLanguageByCd(languageCode);
	    CountryView country = dbResource.getCountry(countryCode);
        if (langData != null && country != null) {
            userLocaleStr = langData.getLanguageCode().trim() + "_" + country.getCountryCode().trim();
        } else {
            userLocaleStr = locale.getLocale().getLanguage() + "_" + locale.getLocale().getCountry();
        }
        userData.setPrefLocaleCd(userLocaleStr);

        if (userForm.getApproveOrders()) {
            userData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.ORDER_APPROVER);
        } else {
            userData.setWorkflowRoleCd(RefCodeNames.WORKFLOW_ROLE_CD.UNKNOWN);
        }
        userData.setUserTypeCd(userForm.getUserType());
        userData.setUserStatusCd(userForm.getUserStatus());

        UserRightsTool ert = new UserRightsTool(userData);
        /*
            _otherPmt = false,
            _creditCard = false,
            _canApproveOrders = false,
            _siteManagerRole = false,
            _serviceVendorRole = false,
            _canEditBillTo = false,
            _canEditShipTo = false,
            _workOrderCompletedNotification = false,
            _workOrderAcceptedByProviderNotification = false,
            _workOrderRejectedByProviderNotification = false;
         */
        ert.setOtherPaymentFlag(userForm.getOtherPayment());
        ert.setCreditCardFlag(userForm.getCreditCard());
        ert.setOnAccount(userForm.getOnAccount());
        if (userForm.getCreditCard() ||
            userForm.getOnAccount() ||
            userForm.getOtherPayment()) {
            ert.setContractItemsOnly(true);
        } else {
            ert.setContractItemsOnly(false);
        }
        ert.setBrowseOnly(userForm.getBrowseOnly());
        ert.setNoReporting(userForm.getDoesNotUseReporting());
        ert.setShowPrice(userForm.getShowPrices());
        ert.setPoNumRequired(userForm.getPoNumRequired());

        if (RefCodeNames.USER_ROLE_CD.SERVICE_VENDOR.equals(userForm.getUserRole())) {
            ert.setServiceVendorRole(true);
        } else {
            ert.setServiceVendorRole(false);
        }

        if (RefCodeNames.USER_ROLE_CD.SITE_MANAGER.equals(userForm.getUserRole())) {
            ert.setSiteManagerRole(true);
        } else {
            ert.setSiteManagerRole(false);
        }

        ert.setCanEditBillTo(userForm.getUpdateBillToInformation());
        ert.setCanEditShipTo(userForm.getUpdateShipToInformation());

        String permissionToken = ert.makePermissionsToken();
        if (!Utility.isSet(permissionToken)) {
            permissionToken = RefCodeNames.USER_ROLE_CD.UNKNOWN;
        }

        userData.setUserRoleCd(permissionToken);

        userInfo.setUserData(userData);


        // ========================= USER EMAIL =========================
        EmailData emailData = userInfo.getEmailData();
        if (Utility.isSet(userForm.getUserContact().getContact().getEmail())) {
            if (emailData == null) {
                emailData = new EmailData();
            }
            emailData.setEmailStatusCd(RefCodeNames.EMAIL_STATUS_CD.ACTIVE);
            emailData.setUserId(userForm.getUserId());
            emailData.setEmailAddress(userForm.getUserContact().getContact().getEmail());
            emailData.setEmailTypeCd(RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT);
            emailData.setPrimaryInd(true);

            if (userForm.isIsClone()) {
                emailData.setEmailId(null);
            }
            userInfo.setEmailData(emailData);
        } else {
            if (userInfo.getEmailData() != null) {
                userInfo.getEmailData().setEmailAddress(null);
            }
        }

        // ====================== USER ESCALATION EMAIL =====================
        emailData = userInfo.getEscalationEmailData();
        if (Utility.isSet(userForm.getUserContact().getEscalationEmail())) {
            if (emailData == null) {
                emailData = new EmailData();
            }
            emailData.setEmailStatusCd(RefCodeNames.EMAIL_STATUS_CD.ACTIVE);
            emailData.setUserId(userForm.getUserId());
            emailData.setEmailAddress(userForm.getUserContact().getEscalationEmail());
            emailData.setEmailTypeCd(RefCodeNames.EMAIL_TYPE_CD.ESCALATION);
            emailData.setPrimaryInd(true);

            if (userForm.isIsClone()) {
                emailData.setEmailId(null);
            }

            userInfo.setEscalationEmailData(emailData);
        } else {
            if (userInfo.getEscalationEmailData() != null) {
                userInfo.getEscalationEmailData().setEmailAddress(null);
            }
        }
        
        // ====================== USER TEXTING ADDRESS =====================
        EmailData smsEmailData = userInfo.getSmsEmailData();
        if (Utility.isSet(userForm.getUserContact().getTextingAddress())) {
            if (smsEmailData == null) {
                smsEmailData = new EmailData();
            }
            smsEmailData.setEmailStatusCd(RefCodeNames.EMAIL_STATUS_CD.ACTIVE);
            smsEmailData.setUserId(userForm.getUserId());
            smsEmailData.setEmailAddress(userForm.getUserContact().getTextingAddress());
            smsEmailData.setEmailTypeCd(RefCodeNames.EMAIL_TYPE_CD.SMS);
            smsEmailData.setPrimaryInd(true);

            if (userForm.isIsClone()) {
                smsEmailData.setEmailId(null);
            }
            
            userInfo.setSmsEmailData(smsEmailData);
        } else {
            if (userInfo.getSmsEmailData() != null) {
                userInfo.getSmsEmailData().setEmailAddress(null);
            }
        }

        // ====================== USER PHONE =====================
        PhoneData phoneData = userInfo.getPhoneData();
        if (Utility.isSet(userForm.getUserContact().getContact().getTelephone())) {
            if (phoneData == null) {
                phoneData = new PhoneData();
            }
            phoneData.setUserId(userForm.getUserId());
            phoneData.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.PHONE);
            phoneData.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
            phoneData.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_PHONE);
            phoneData.setPhoneNum(userForm.getUserContact().getContact().getTelephone());
            phoneData.setPrimaryInd(true);

            if (userForm.isIsClone()) {
                phoneData.setPhoneId(null);
            }
                    
            userInfo.setPhoneData(phoneData);
        } else {
            if (userInfo.getPhoneData() != null) {
                userInfo.getPhoneData().setPhoneNum(null);
            }
        }

        // ====================== USER FAX =====================
        phoneData = userInfo.getFaxPhoneData();
        if (Utility.isSet(userForm.getUserContact().getContact().getFax())) {
            if (phoneData == null) {
                phoneData = new PhoneData();
            }
            phoneData.setUserId(userForm.getUserId());
            phoneData.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.FAX);
            phoneData.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
            phoneData.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_FAX);
            phoneData.setPhoneNum(userForm.getUserContact().getContact().getFax());
            phoneData.setPrimaryInd(true);

            if (userForm.isIsClone()) {
                phoneData.setPhoneId(null);
            }
            
            userInfo.setFaxPhoneData(phoneData);
        } else {
            if (userInfo.getFaxPhoneData() != null) {
                userInfo.getFaxPhoneData().setPhoneNum(null);
            }
        }

        // ====================== USER MOBILE =====================
        phoneData = userInfo.getMobilePhoneData();
        if (Utility.isSet(userForm.getUserContact().getContact().getMobile())) {
            if (phoneData == null) {
                phoneData = new PhoneData();
            }
            phoneData.setUserId(userForm.getUserId());
            phoneData.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.MOBILE);
            phoneData.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
            phoneData.setPhoneNum(userForm.getUserContact().getContact().getMobile());
            phoneData.setPrimaryInd(true);

            if (userForm.isIsClone()) {
                phoneData.setPhoneId(null);
            }

            userInfo.setMobilePhoneData(phoneData);
        } else {
            if (userInfo.getMobilePhoneData() != null) {
                userInfo.getMobilePhoneData().setPhoneNum(null);
            }
        }

        // ====================== USER ADDRESS =====================
        AddressData userAddress = userInfo.getAddressData();
        if (userAddress == null) {
            userAddress = new AddressData();
        }
        userAddress.setUserId(userForm.getUserId());
        userAddress.setName1(userForm.getUserContact().getContact().getFirstName());
        userAddress.setName2(userForm.getUserContact().getContact().getLastName());
        userAddress.setAddress1(userForm.getUserContact().getContact().getAddress().getAddress1());
        userAddress.setAddress2(userForm.getUserContact().getContact().getAddress().getAddress2());
        if (Utility.isSet(userForm.getUserContact().getContact().getAddress().getCountryCd())) {
            userAddress.setCountryCd(userForm.getUserContact().getContact().getAddress().getCountryCd());
        } else {
            userAddress.setCountryCd(RefCodeNames.ADDRESS_COUNTRY_CD.COUNTRY_UNKNOWN);
        }
        userAddress.setPostalCode(userForm.getUserContact().getContact().getAddress().getPostalCode());
        userAddress.setStateProvinceCd(userForm.getUserContact().getContact().getAddress().getStateProvinceCd());
        userAddress.setCity(userForm.getUserContact().getContact().getAddress().getCity());
        userAddress.setAddressTypeCd(RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        userAddress.setAddressStatusCd(RefCodeNames.ADDRESS_STATUS_CD.ACTIVE);
        userAddress.setPrimaryInd(true);

        if (userForm.isIsClone()) {
            userAddress.setAddressId(null);
        }
        userInfo.setAddressData(userAddress);

        // ====================== USER PROPERTIES =====================
        List<PropertyData> toSaveProperties = userInfo.getUserProperties();

        if (toSaveProperties != null) {
            if (userForm.isIsClone()) {
                for (PropertyData property : toSaveProperties) {
                    property.setPropertyId(null);
                }
            }
        } else {
            toSaveProperties = new ArrayList<PropertyData>();
        }

        /*
        public static final String CORPORATE_USER = "CORPORATE_USER";
        public static final String IVR_UIN = "IVR_UIN";
        public static final String IVR_PIN = "IVR_PIN";
        public static final String WORK_ORDER_PO_NUM_REQUIRED = "WORK_ORDER_PO_NUM_REQUIRED";
        */

        PropertyData property = (PropertyData) PropertyUtil.find(toSaveProperties, RefCodeNames.PROPERTY_TYPE_CD.CORPORATE_USER);
        if (userForm.getCorporateUser()) {
            if (property != null) {
                property.setValue("true");
            } else {
                property = PropertyUtil.createProperty(null,
                        userForm.getUserId(),
                        RefCodeNames.PROPERTY_TYPE_CD.CORPORATE_USER,
                        RefCodeNames.PROPERTY_TYPE_CD.CORPORATE_USER,
                        "true",
                        RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                        null);
                toSaveProperties.add(property);
            }
        } else {
            if (property != null) {
                property.setValue("false");
            }
        }

        property = (PropertyData) PropertyUtil.find(toSaveProperties, RefCodeNames.PROPERTY_TYPE_CD.IVR_UIN);
        if (Utility.isSet(userForm.getIvrUserIdentificationNumber())) {
            if (property != null) {
                property.setValue(userForm.getIvrUserIdentificationNumber());
            } else {
                property = PropertyUtil.createProperty(null,
                        userForm.getUserId(),
                        RefCodeNames.PROPERTY_TYPE_CD.IVR_UIN,
                        RefCodeNames.PROPERTY_TYPE_CD.IVR_UIN,
                        userForm.getIvrUserIdentificationNumber(),
                        RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                        null);
                toSaveProperties.add(property);
            }
        } else {
            if (property != null) {
                toSaveProperties.remove(property);
            }
        }

        property = (PropertyData) PropertyUtil.find(toSaveProperties, RefCodeNames.PROPERTY_TYPE_CD.IVR_PIN);
        if (Utility.isSet(userForm.getIvrPIN())) {
            if (property != null) {
                property.setValue(userForm.getIvrPIN());
            } else {
                property = PropertyUtil.createProperty(null,
                        userForm.getUserId(),
                        RefCodeNames.PROPERTY_TYPE_CD.IVR_PIN,
                        RefCodeNames.PROPERTY_TYPE_CD.IVR_PIN,
                        userForm.getIvrPIN(),
                        RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                        null);
            }
            toSaveProperties.add(property);
        } else {
            if (property != null) {
                toSaveProperties.remove(property);
            }
        }
        
        property = (PropertyData) PropertyUtil.find(toSaveProperties, RefCodeNames.PROPERTY_TYPE_CD.USER_ID_CODE);
        if (Utility.isSet(userForm.getUserCode())) {
            if (property != null) {
                property.setValue(userForm.getUserCode());
            } else {
                property = PropertyUtil.createProperty(null,
                        userForm.getUserId(),
                        RefCodeNames.PROPERTY_TYPE_CD.USER_ID_CODE,
                        RefCodeNames.PROPERTY_TYPE_CD.USER_ID_CODE,
                        userForm.getUserCode(),
                        RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                        null);
            }
            toSaveProperties.add(property);
        } else {
            if (property != null) {
                toSaveProperties.remove(property);
            }
        }

        property = (PropertyData) PropertyUtil.find(toSaveProperties, RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_STORE);

        if (!multiStoreDb && Utility.isSet(userForm.getDefaultStoreId())) {

            if (property != null) {

                property.setValue(userForm.getDefaultStoreId());

            } else {

                property = PropertyUtil.createProperty(null,
                        userForm.getUserId(),
                        RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_STORE,
                        RefCodeNames.PROPERTY_TYPE_CD.DEFAULT_STORE,
                        userForm.getDefaultStoreId(),
                        RefCodeNames.PROPERTY_STATUS_CD.ACTIVE,
                        null
                );
            }

            toSaveProperties.add(property);

        } else {

            if (property != null) {
                toSaveProperties.remove(property);
            }

        }
        logger.info("fillOutUserIdenView()=> toSaveProperties: " + toSaveProperties);

        userInfo.setUserProperties(toSaveProperties);

        // ====================== USER STORE ASSOCS =====================        
        List<AllStoreIdentificationView> newStoreAssocs = userForm.getEntities().getSelected();

        List<UserAssocData> oldAssocs = userInfo.getUserAssocs();
        List<UserAssocData> newAssocs = new ArrayList<UserAssocData>();

        Map<Long, UserAssocData> oldStoreAssocsMap = new Hashtable<Long, UserAssocData>();
        if (Utility.isSet(oldAssocs)) {

            if (userForm.isIsClone()) {
                for (UserAssocData assoc : oldAssocs) {
                    assoc.setUserAssocId(null);
                    assoc.setLastUserVisitDateTime(null);
                }
            }
            
            for (UserAssocData assoc : oldAssocs) {
                if (RefCodeNames.USER_ASSOC_CD.STORE.equals(assoc.getUserAssocCd())) {
                    oldStoreAssocsMap.put(assoc.getBusEntityId(), assoc);
                } else {
                    newAssocs.add(assoc);
                }
            }
        }

        for (AllStoreIdentificationView el : newStoreAssocs) {
            if (oldStoreAssocsMap.containsKey(el.getStoreId())) {
                newAssocs.add(oldStoreAssocsMap.get(el.getStoreId()));
            } else {
                UserAssocData userAssoc = new UserAssocData();
                userAssoc.setBusEntityId(el.getStoreId());
                userAssoc.setUserId(userForm.getUserId());
                userAssoc.setUserAssocCd(RefCodeNames.USER_ASSOC_CD.STORE);
                newAssocs.add(userAssoc);
            }
        }

        userInfo.setUserAssocs(newAssocs);
        userInfo.setUserStoreAssocs(newStoreAssocs);
        
        if (userForm.isIsClone()) {
        	for (GroupAssocData el : userInfo.getUserGroupAssocs()) {
        		el.setGroupAssocId(null);
        		el.setUserId(userForm.getUserId());
        	}
        }

        return userInfo;
    }

    public static EmailData createUserEmail(EmailData emailData, UserForm userForm, String emailType) {

        if (emailData == null) {
            emailData = new EmailData();
        }

        if (RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT.equals(emailType)) {

        } else if (RefCodeNames.EMAIL_TYPE_CD.ESCALATION.equals(emailType)) {

        }
        emailData.setEmailStatusCd(RefCodeNames.EMAIL_STATUS_CD.ACTIVE);
        emailData.setUserId(userForm.getUserId());
        emailData.setEmailAddress(userForm.getUserContact().getContact().getEmail());
        emailData.setEmailTypeCd(emailType);
        emailData.setPrimaryInd(true);

        return emailData;
    }

    public static AllUserIdentView fillOutAllUserView(AllUserIdentView allUserView, UserForm form, AppLocale locale) {

        if (allUserView == null) {
            allUserView = new AllUserIdentView();
            allUserView.setAllUserData(new AllUserData());
            allUserView.setUserStoreAssoc(new ArrayList<UserStoreData>());
        }

        AllUserData allUser = allUserView.getAllUserData();

        /*
       ALL_USER_ID                          NUMBER(38) NOT NULL,
       USER_ID                              NUMBER(38) NOT NULL,
       USER_NAME                            VARCHAR2(255) NOT NULL,
       FIRST_NAME                           VARCHAR2(255),
       LAST_NAME                            VARCHAR2(255),
       PASSWORD                             VARCHAR2(35),
       EFF_DATE                             DATE,
       EXP_DATE                             DATE,
       USER_STATUS_CD                       VARCHAR2(30) NOT NULL,
       USER_TYPE_CD                         VARCHAR2(30) NOT NULL,
       DEFAULT_STORE_ID                     NUMBER(38),
       ADD_DATE                             DATE NOT NULL,
       ADD_BY                               VARCHAR2(255),
       MOD_DATE                             DATE NOT NULL,
       MOD_BY                               VARCHAR2(255),
        */

        allUser.setUserId(form.getUserId());
        allUser.setUserName(form.getUserLogonName());
        allUser.setFirstName(form.getUserContact().getContact().getFirstName());
        allUser.setLastName(form.getUserContact().getContact().getLastName());
        if (Utility.isSet(form.getUserPassword())) {
            String encodedPassword = PasswordUtil.getHash(form.getUserLogonName(), form.getUserPassword());
            allUser.setPassword(encodedPassword);
        }

        allUser.setEffDate(AppI18nUtil.parseDateNN(locale, form.getUserActiveDate()));
        if (Utility.isSet(form.getUserInactiveDate())) {
            allUser.setExpDate(AppI18nUtil.parseDateNN(locale, form.getUserInactiveDate()));
        }
        allUser.setDefaultStoreId(Long.parseLong(form.getDefaultStoreId()));
        allUser.setUserTypeCd(form.getUserType());
        allUser.setUserStatusCd(form.getUserStatus());

        allUser.setFirstName(form.getUserContact().getContact().getFirstName());

        List<AllStoreIdentificationView> newStoreAssocs = form.getEntities().getSelected();
        List<UserStoreData> storeAssocs = allUserView.getUserStoreAssoc();
        List<UserStoreData> newAssocs = new ArrayList<UserStoreData>();

        Map<Long, UserStoreData> oldAssocsMap = new Hashtable<Long, UserStoreData>();
        if (Utility.isSet(storeAssocs)) {
            for (UserStoreData assoc : storeAssocs) {
                oldAssocsMap.put(assoc.getUserStoreId(), assoc);
            }
        }

        for (AllStoreIdentificationView el : newStoreAssocs) {
            if (oldAssocsMap.containsKey(el.getStoreId())) {
                newAssocs.add(oldAssocsMap.get(el.getStoreId()));
            } else {
                UserStoreData userAssoc = new UserStoreData();
                userAssoc.setAllStoreId(el.getMainStoreId());
                //userAssoc.setAllUserId(form.getUserId());
                newAssocs.add(userAssoc);
            }
        }

        allUserView.setUserStoreAssoc(newAssocs);

        return allUserView;
    }

    public static SiteOptionsForm createSiteOptions(SiteIdentPropertiesView properties) {

        SiteOptionsForm options = new SiteOptionsForm();

        options.setAllowCorporateScheduledOrder(PropertyUtil.toValueNN(properties.getAllowCorporateScheduledOrder()));
        options.setByPassOrderRouting(PropertyUtil.toValueNN(properties.getByPassOrderRouting()));
        options.setConsolidatedOrderWarehouse(PropertyUtil.toValueNN(properties.getConsolidatedOrderWarehouse()));
        options.setEnableInventory(String.valueOf(Utility.isOn(PropertyUtil.toValueNN(properties.getEnableInventory()))));
        options.setShareBuyerOrderGuides(PropertyUtil.toValueNN(properties.getShareBuyerOrderGuides()));
        options.setShowRebillOrder(PropertyUtil.toValueNN(properties.getShowRebillOrder()));
        options.setTaxable(Utility.isTrueStrOf(PropertyUtil.toValueNN(properties.getTaxable())));

        return options;
    }

    public static DeliveryScheduleForm createDeliveryScheduleForm(AppLocale locale, CorporateScheduleView corporateScheduleCorporate) {

        if (corporateScheduleCorporate == null) {
            return null;
        }

        DeliveryScheduleForm form = new DeliveryScheduleForm();

        try {
            Date time = AppI18nUtil.parseTime(corporateScheduleCorporate.getCutoffTime(), Constants.SYSTEM_TIME_PATTERN);
            form.setCutoffTime(AppI18nUtil.formatTime(locale, time));
        } catch (AppParserException e) {
            form.setCutoffTime(
                    AppI18nUtil.getMessage("admin.global.text.wrongValue",
                            new Object[]{corporateScheduleCorporate.getCutoffTime()})
            );
        }

        try {
            Long hours = AppI18nUtil.parseNumberNN(corporateScheduleCorporate.getIntervalHour());
            form.setIntervalHour(AppI18nUtil.formatNumber(locale, hours));
        } catch (AppParserException e) {
            form.setCutoffTime(
                    AppI18nUtil.getMessage("admin.global.text.wrongValue",
                            new Object[]{corporateScheduleCorporate.getIntervalHour()}
                    )
            );
        }

        form.setNextDelivery(AppI18nUtil.formatDate(locale, corporateScheduleCorporate.getNextDeliveryDate()));
        form.setScheduleId(new NumberArgument(corporateScheduleCorporate.getScheduleId()).resolve(locale));
        form.setScheduleName(corporateScheduleCorporate.getScheduleName());

        return form;

    }

    public static List<PropertyData> createSiteIdentProperties(List<PropertyData> properties, SiteForm siteForm) {

        if (properties == null) {
            properties = new ArrayList<PropertyData>();
        }

        SiteOptionsForm options = siteForm.getOptions();


        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.ALLOW_CORPORATE_SCHED_ORDER);
            if (property != null) {
                property.setValue(Utility.isTrueStrOf(options.getAllowCorporateScheduledOrder()));
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.ALLOW_CORPORATE_SCHED_ORDER,
                                Utility.isTrueStrOf(options.getAllowCorporateScheduledOrder())
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.BYPASS_ORDER_ROUTING);
            if (property != null) {
                property.setValue(Utility.isTrueStrOf(options.getByPassOrderRouting()));
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.BYPASS_ORDER_ROUTING,
                                Utility.isTrueStrOf(options.getByPassOrderRouting())
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.TAXABLE_INDICATOR);
            if (property != null) {
                property.setValue(Utility.isYStrOf(siteForm.getOptions().getTaxable()));
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.TAXABLE_INDICATOR,
                                Utility.isYStrOf(siteForm.getOptions().getTaxable())
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.CONSOLIDATED_ORDER_WAREHOUSE);
            if (property != null) {
                property.setValue(Utility.isTrueStrOf(options.getConsolidatedOrderWarehouse()));
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.CONSOLIDATED_ORDER_WAREHOUSE,
                                Utility.isTrueStrOf(options.getConsolidatedOrderWarehouse())
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.INVENTORY_SHOPPING);
            if (property != null) {
                property.setValue(Utility.isTrueOnStrOf(options.getEnableInventory()));
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.INVENTORY_SHOPPING,
                                Utility.isTrueOnStrOf(options.getEnableInventory())
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.INVENTORY_SHOPPING_TYPE);
            if (property != null) {
                property.setValue(Utility.isTrueOnStrOf(options.getEnableInventory()));
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.INVENTORY_SHOPPING_TYPE,
                                Utility.isTrueOnStrOf(options.getEnableInventory())
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyExtraCode.SHARE_BUYER_GUIDES);
            if (property != null) {
                property.setValue(Utility.isTrueStrOf(options.getShareBuyerOrderGuides()));
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyExtraCode.SHARE_BUYER_GUIDES,
                                Utility.isTrueStrOf(options.getShareBuyerOrderGuides())
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.SHOW_REBILL_ORDER);
            if (property != null) {
                property.setValue(Utility.isTrueStrOf(options.getShowRebillOrder()));
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.SHOW_REBILL_ORDER,
                                Utility.isTrueStrOf(options.getShowRebillOrder())
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.LOCATION_COMMENTS);
            if (property != null) {
                property.setValue(siteForm.getLocationComments());
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.LOCATION_COMMENTS,
                                siteForm.getLocationComments()
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.LOCATION_LINE_LEVEL_CODE);
            if (property != null) {
                property.setValue(siteForm.getLocationLineLevelCode());
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.LOCATION_LINE_LEVEL_CODE,
                                siteForm.getLocationLineLevelCode()
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.LOCATION_SHIP_MSG);
            if (property != null) {
                property.setValue(siteForm.getLocationShipMsg());
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.LOCATION_SHIP_MSG,
                                siteForm.getLocationShipMsg()
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.LOCATION_PRODUCT_BUNDLE);
            if (property != null) {
                property.setValue(siteForm.getProductBundle());
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.LOCATION_PRODUCT_BUNDLE,
                                siteForm.getProductBundle()
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyExtraCode.SITE_REFERENCE_NUMBER);
            if (property != null) {
                property.setValue(siteForm.getLocationBudgetRefNum());
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyExtraCode.SITE_REFERENCE_NUMBER,
                                siteForm.getLocationBudgetRefNum()
                        )
                );
            }
        }


        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.DIST_SITE_REFERENCE_NUMBER);
            if (property != null) {
                property.setValue(siteForm.getLocationDistrRefNum());
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.DIST_SITE_REFERENCE_NUMBER,
                                siteForm.getLocationDistrRefNum()
                        )
                );
            }
        }

        {
            PropertyData property = PropertyUtil.findP(properties, SitePropertyTypeCode.TARGET_FACILITY_RANK);
            if (property != null) {
                property.setValue(siteForm.getTargetFicilityRank());
            } else {
                properties.add(
                        PropertyUtil.toProperty(
                                siteForm.getSiteId(),
                                SitePropertyTypeCode.TARGET_FACILITY_RANK,
                                siteForm.getTargetFicilityRank()
                        )
                );
            }
        }

        return properties;
    }

    public static ContactView createContact(ContactView contact, SiteForm siteForm) {

        if (contact == null) {
            contact = new ContactView();
        }

        PhoneData phone = contact.getPhone();
        if (phone == null) {
            phone = new PhoneData();
        }

        phone.setBusEntityId(siteForm.getSiteId());
        phone.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.PHONE);
        phone.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
        phone.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_PHONE);
        phone.setPhoneNum(siteForm.getContact().getTelephone());
        phone.setPrimaryInd(true);

        contact.setPhone(phone);


        AddressData address = contact.getAddress();
        if (address == null) {
            address = new AddressData();
        }

        address.setBusEntityId(siteForm.getSiteId());
        address.setName1(siteForm.getContact().getFirstName());
        address.setName2(siteForm.getContact().getLastName());
        address.setAddress1(siteForm.getContact().getAddress().getAddress1());
        address.setAddress2(siteForm.getContact().getAddress().getAddress2());
        address.setAddress3(siteForm.getContact().getAddress().getAddress3());
        address.setAddress4(siteForm.getContact().getAddress().getAddress4());
        address.setCountryCd(siteForm.getContact().getAddress().getCountryCd());
        address.setPostalCode(siteForm.getContact().getAddress().getPostalCode());
        address.setStateProvinceCd(siteForm.getContact().getAddress().getStateProvinceCd());
        address.setAddressTypeCd(RefCodeNames.ADDRESS_TYPE_CD.SHIPPING);
        address.setAddressStatusCd(RefCodeNames.ADDRESS_STATUS_CD.ACTIVE);
        address.setCity(siteForm.getContact().getAddress().getCity());
        address.setPrimaryInd(true);

        contact.setAddress(address);

        return contact;
    }
    
    public static ContentManagementView createContentManagementProperties(ContentManagementView properties, CMSForm cmsForm) {
    	
    	if (properties == null) {
    		properties = new ContentManagementView();
        }
    	
    	if(Utility.isSet(properties.getDisplayGenericContent())){
    		properties.getDisplayGenericContent().setValue(Utility.isTrueStrOf(cmsForm.isDisplayGenericContent()));
    	}else{
    		properties.setDisplayGenericContent(PropertyUtil.toProperty(cmsForm.getPrimaryEntityId(), 
        			RefCodeNames.PROPERTY_TYPE_CD.DISPLAY_GENERIC_CONTENT, 
        			Utility.isTrueStrOf(cmsForm.isDisplayGenericContent())));       	
    	}
    	
    	if(Utility.isSet(properties.getCustomContentURL())){
    		properties.getCustomContentURL().setValue(cmsForm.getCustomContentURL());
    	}else{
    		properties.setCustomContentURL(PropertyUtil.toProperty(cmsForm.getPrimaryEntityId(), 
        			RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_URL, 
        			cmsForm.getCustomContentURL()));
    	}
    	
    	if(Utility.isSet(properties.getCustomContentEditor())){
    		properties.getCustomContentEditor().setValue(cmsForm.getCustomContentEditor());
    	}else{
    		properties.setCustomContentEditor(PropertyUtil.toProperty(cmsForm.getPrimaryEntityId(), 
        			RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_EDITOR, 
        			cmsForm.getCustomContentEditor()));
    	}
    	
    	if(Utility.isSet(properties.getCustomContentViewer())){
    		properties.getCustomContentViewer().setValue(cmsForm.getCustomContentViewer());
    	}else{
    		properties.setCustomContentViewer(PropertyUtil.toProperty(cmsForm.getPrimaryEntityId(), 
        			RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_VIEWER, 
        			cmsForm.getCustomContentViewer()));
    	}
    	
    	return properties;
    }
    
    public static ContentManagementView createContentManagementProperties(ContentManagementView properties, AccountContentManagementForm accountContentMgmtForm) {
    	
    	if (properties == null) {
    		properties = new ContentManagementView();
        }
    	
    	if(Utility.isSet(properties.getDisplayGenericContent())){
    		properties.getDisplayGenericContent().setValue(Utility.isTrueStrOf(accountContentMgmtForm.isDisplayGenericContent()));
    	}else{
    		properties.setDisplayGenericContent(PropertyUtil.toProperty(accountContentMgmtForm.getAccountId(), 
        			RefCodeNames.PROPERTY_TYPE_CD.DISPLAY_GENERIC_CONTENT, 
        			Utility.isTrueStrOf(accountContentMgmtForm.isDisplayGenericContent())));       	
    	}
    	
    	if(Utility.isSet(properties.getCustomContentURL())){
    		properties.getCustomContentURL().setValue(accountContentMgmtForm.getCustomContentURL());
    	}else{
    		properties.setCustomContentURL(PropertyUtil.toProperty(accountContentMgmtForm.getAccountId(), 
        			RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_URL, 
        			accountContentMgmtForm.getCustomContentURL()));
    	}
    	
    	if(Utility.isSet(properties.getCustomContentEditor())){
    		properties.getCustomContentEditor().setValue(accountContentMgmtForm.getCustomContentEditor());
    	}else{
    		properties.setCustomContentEditor(PropertyUtil.toProperty(accountContentMgmtForm.getAccountId(), 
        			RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_EDITOR, 
        			accountContentMgmtForm.getCustomContentEditor()));
    	}
    	
    	if(Utility.isSet(properties.getCustomContentViewer())){
    		properties.getCustomContentViewer().setValue(accountContentMgmtForm.getCustomContentViewer());
    	}else{
    		properties.setCustomContentViewer(PropertyUtil.toProperty(accountContentMgmtForm.getAccountId(), 
        			RefCodeNames.PROPERTY_TYPE_CD.CUSTOM_CONTENT_VIEWER, 
        			accountContentMgmtForm.getCustomContentViewer()));
    	}
    	
    	return properties;
    }
    public static WorkflowForm createWorkflowForm(WorkflowData workflow) {

        WorkflowForm form = new WorkflowForm();

        form.setWorkflowName(workflow.getShortDesc());
        form.setWorkflowTypeCd(workflow.getWorkflowTypeCd());
        form.setWorkflowStatus(workflow.getWorkflowStatusCd());
        
        return form;

    }

    public static EntityPropertiesView createAccountProperties(EntityPropertiesView view, AccountPropertiesForm form) {
    	
    	if (view == null) {
    		view = new EntityPropertiesView();
        }
		List<PropertyData> properties = view.getProperties();
    	if (!Utility.isSet(properties)){
    		properties = new ArrayList<PropertyData>();
    		view.setProperties(properties);
    	}
    	
    	PropertyExtraCode propExtraType=null;
    	PropertyTypeCode propType = null;
    	propExtraType =AccountIdentPropertyExtraCode.ALLOW_USER_CHANGE_PASSWORD;
	    String val = Utility.isTrueStrOf(form.isAllowToChangePassword());
	    PropertyData prop = (PropertyData)PropertyUtil.findP(properties, propExtraType ) ;
	    if ( prop != null )	{
			prop.setValue(val);
		} else {
			prop = PropertyUtil.toProperty(form.getAccountId(), propExtraType, val);
			properties.add(prop);
		}
		logger.info("createAccountProperties() ===> 1---  val = " + val);
		
	    propType= AccountIdentPropertyTypeCode.ALLOW_CUSTOMER_PO_NUMBER;
	    val = Utility.isTrueStrOf(form.isAllowToEnterPurchaseNum());
	    prop = (PropertyData)PropertyUtil.findP(properties, propType) ;
		if (prop != null){
			prop.setValue(val);
		} else {
			prop = PropertyUtil.toProperty(form.getAccountId(), propType, val);
			properties.add(prop);
		}
		logger.info("createAccountProperties() ===> 2---  val = " + val);
		
		propExtraType = AccountIdentPropertyExtraCode.ALLOW_CC_PAYMENT;
		val = Utility.isTrueStrOf(form.isAllowToPayCreditCard());   		
		prop = (PropertyData)PropertyUtil.findP(properties, propExtraType) ;
		if (prop != null){
			prop.setValue(val);
		} else {
			prop = PropertyUtil.toProperty(form.getAccountId(), propExtraType, val);
			properties.add(prop);
		}
		
	    propType= AccountIdentPropertyTypeCode.TAXABLE_INDICATOR;
	    val = Utility.isTrueStrOf(form.isUseEstimatedSalesTax());   		    		
		prop = (PropertyData)PropertyUtil.findP(properties, propType) ;    		
		if (prop != null){
			prop.setValue(val);
		} else {
			prop = PropertyUtil.toProperty(form.getAccountId(), propType, val);
			properties.add(prop);
		}
		
	    propType = AccountIdentPropertyTypeCode.AUTHORIZED_FOR_RESALE;
	    val = Utility.isTrueStrOf(form.isUseResaleItems());   		    		
		prop = (PropertyData)PropertyUtil.findP(properties, propType) ;		
		if (prop != null){
			prop.setValue(val);
		} else {
			prop = PropertyUtil.toProperty(form.getAccountId(), propType, val);
			properties.add(prop);
		}
		
		propExtraType = AccountIdentPropertyExtraCode.SUPPORTS_BUDGET;
	    val = Utility.isTrueStrOf(form.isUseBudgets());   		    		
		prop = (PropertyData)PropertyUtil.findP(properties, propExtraType) ;		
		if (prop != null){
			prop.setValue(val);
		} else {
			prop = PropertyUtil.toProperty(form.getAccountId(), propExtraType, val);
			properties.add(prop);
		}
		
		propType = AccountIdentPropertyTypeCode.USE_ALTERNATE_UI;
	    val = Utility.isTrueStrOf(form.isUseAlternateUserInterface());  
	    prop = (PropertyData)PropertyUtil.findP(properties, propType) ;		
		if (prop != null){
			prop.setValue(val);
		} else {
			prop = PropertyUtil.toProperty(form.getAccountId(), propType, val);
			properties.add(prop);
		}

		propType = AccountIdentPropertyTypeCode.CUSTOMER_SYSTEM_APPROVAL_CD;
	    val = form.getPurchasingSystem();   		    		
		prop = (PropertyData)PropertyUtil.findP(properties, propType) ;	
		if (prop != null){
			prop.setValue(val);
		} else {
			prop = PropertyUtil.toProperty(form.getAccountId(), propType, val);
			properties.add(prop);
		}
		
        //only admin or sys admin users are allowed to update the TRACK_PUNCHOUT_ORDER_MESSAGES account property,
		//so only include it for those user types and remove it otherwise
        AppUser appUser = Auth.getAppUser();
        boolean canUpdateTrackPunchoutProperty = appUser.isAdmin() || appUser.isSystemAdmin();
        if (canUpdateTrackPunchoutProperty) {
    		propType = AccountIdentPropertyTypeCode.TRACK_PUNCHOUT_ORDER_MESSAGES;
    	    val = Utility.isTrueStrOf(form.isTrackPunchoutOrderMessages());   		    		
    		prop = (PropertyData)PropertyUtil.findP(properties, propType) ;		
    		if (prop != null) {
    			prop.setValue(val);
    		} 
    		else {
    			prop = PropertyUtil.toProperty(form.getAccountId(), propType, val);
    			properties.add(prop);
    		}
        }
        else {
        	Iterator<PropertyData> propertyIterator = properties.iterator();
        	while (propertyIterator.hasNext()) {
        		PropertyData property = propertyIterator.next();
        		if (RefCodeNames.PROPERTY_TYPE_CD.TRACK_PUNCHOUT_ORDER_MESSAGES.equalsIgnoreCase(property.getPropertyTypeCd())) {
        			propertyIterator.remove();
        		}
        	}
        }
		
		logger.info("createAccountProperties() ===> view.getProperties() = " + view.getProperties());

    	return view;
    }    

    public static ManufacturerPropertiesView
        createManufacturerProperties(ManufacturerPropertiesView properties, ManufacturerForm manufacturerForm) {
        if (properties == null) {
            properties = new ManufacturerPropertiesView();
        }

        properties.setManufacturerId(manufacturerForm.getManufacturerId());
        if (properties.getMsdsPlugin() != null) {
            properties.getMsdsPlugin().setValue(manufacturerForm.getManufacturerMSDSPlugIn());
        } else {
            properties.setMsdsPlugin(
                    PropertyUtil.toProperty(
                            manufacturerForm.getManufacturerId(),
                            RefCodeNames.PROPERTY_TYPE_CD.MSDS_PLUGIN,
                            manufacturerForm.getManufacturerMSDSPlugIn()
                    )
            );
        }
        return properties;
    }


    public static List<PropertyData> createDistributorProperties(List<PropertyData> properties, DistributorForm distributorForm) {

        if (properties == null) {
            properties = new ArrayList<PropertyData>();
        }
        
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyTypeCode.RUNTIME_DISPLAY_NAME);
	        if (property != null) {
	            property.setValue(distributorForm.getDistributorDisplayName());
	        } else {
	            properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyTypeCode.RUNTIME_DISPLAY_NAME,
                		distributorForm.getDistributorDisplayName())));
	        }
        }
        
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyTypeCode.CALL_HOURS);
	        if (property != null) {
	            property.setValue(distributorForm.getDistributorCallCenterHours());
	        } else {
	            properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyTypeCode.CALL_HOURS,
                		distributorForm.getDistributorCallCenterHours())));
	        }
        }
        
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyTypeCode.DISTRIBUTORS_COMPANY_CODE);
	        if (property != null) {
	            property.setValue(distributorForm.getDistributorCompanyCode());
	        } else {
	            properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyTypeCode.DISTRIBUTORS_COMPANY_CODE,
                		distributorForm.getDistributorCompanyCode())));
	        }
        }
        
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyTypeCode.CUSTOMER_REFERENCE_CODE);
	        if (property != null) {
	            property.setValue(distributorForm.getDistributorCustomerReferenceCode());
	        } else {
                properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyTypeCode.CUSTOMER_REFERENCE_CODE,
                		distributorForm.getDistributorCustomerReferenceCode())));
	        }
        }
        
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyExtraCode.DISTRIBUTOR_TYPE);
	        if (property != null) {
	            property.setValue(distributorForm.getDistributorType());
	        } else {
                properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyExtraCode.DISTRIBUTOR_TYPE,
                		distributorForm.getDistributorType())));
	        }
        }
        
        //if they don't already exist, create the four properties that St.John created by default for distributors to 
        //make sure that St.John code continues to work with distributors created in Manta
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyTypeCode.DIST_MAX_INVOICE_FREIGHT);
	        if (property == null) {
	            properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyTypeCode.DIST_MAX_INVOICE_FREIGHT,
                		null)));
	        }
        }
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyTypeCode.EXCEPTION_ON_TAX_DIFFERENCE);
	        if (property == null) {
	            properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyTypeCode.EXCEPTION_ON_TAX_DIFFERENCE,
                		Constants.TRUE)));
	        }
        }
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyTypeCode.HOLD_INVOICE);
	        if (property == null) {
	            properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyTypeCode.HOLD_INVOICE,
                		String.valueOf(Constants.ZERO))));
	        }
        }
        {
	        PropertyData property = PropertyUtil.findP(properties, DistributorPropertyTypeCode.IGNORE_ORDER_MIN_FOR_FREIGHT);
	        if (property == null) {
	            properties.add(updateDistributorProperty(PropertyUtil.toProperty(distributorForm.getDistributorId(),
                		DistributorPropertyTypeCode.IGNORE_ORDER_MIN_FOR_FREIGHT,
                		Constants.FALSE)));
	        }
        }

        return properties;
    }
    
    public static DistributorConfigurationView createDistributorConfigurationView(DistributorConfigurationView view, DistributorConfigurationForm distributorConfigurationForm) {
    	
    	if (view == null) {
    		view = new DistributorConfigurationView();
    	}
    	
    	if (Utility.isSet(view.getPerformSalesTaxCheck())){
    		view.getPerformSalesTaxCheck().setValue(distributorConfigurationForm.getPerformSalesTaxCheck());
    	}else{
    		view.setPerformSalesTaxCheck(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.EXCEPTION_ON_TAX_DIFFERENCE, 
        			distributorConfigurationForm.getPerformSalesTaxCheck())));       	
    	}
    	
    	if(Utility.isSet(view.getExceptionOnOverchargedFreight())){
    		view.getExceptionOnOverchargedFreight().setValue(distributorConfigurationForm.getExceptionOnOverchargedFreight());
    	}else{
    		view.setExceptionOnOverchargedFreight(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.ERROR_ON_OVERCHARGED_FREIGHT, 
        			distributorConfigurationForm.getExceptionOnOverchargedFreight())));
    	}
    	
    	if(Utility.isSet(view.getInvoiceLoadingPricingModel())){
    		view.getInvoiceLoadingPricingModel().setValue(distributorConfigurationForm.getInvoiceLoadingPricingModel());
    	}else{
    		view.setInvoiceLoadingPricingModel(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.INVOICE_LOADING_PRICE_MODEL_CD, 
        			distributorConfigurationForm.getInvoiceLoadingPricingModel())));
    	}
    	
    	if(Utility.isSet(view.getAllowFreightOnBackOrders())){
    		view.getAllowFreightOnBackOrders().setValue(distributorConfigurationForm.getAllowFreightOnBackOrders());
    	}else{
    		view.setAllowFreightOnBackOrders(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.ALLOW_FREIGHT_ON_BACKORDERS, 
        			distributorConfigurationForm.getAllowFreightOnBackOrders())));
    	}
    	
    	if(Utility.isSet(view.getCancelBackorderedLines())){
    		view.getCancelBackorderedLines().setValue(distributorConfigurationForm.getCancelBackorderedLines());
    	}else{
    		view.setCancelBackorderedLines(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.CANCEL_BACKORDERED_LINES, 
        			distributorConfigurationForm.getCancelBackorderedLines())));
    	}
    	
    	if(Utility.isSet(view.getDisallowInvoiceEdits())){
    		view.getDisallowInvoiceEdits().setValue(distributorConfigurationForm.getDisallowInvoiceEdits());
    	}else{
    		view.setDisallowInvoiceEdits(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.DO_NOT_ALLOW_INVOICE_EDITS, 
        			distributorConfigurationForm.getDisallowInvoiceEdits())));
    	}
    	
    	if(Utility.isSet(view.getReceivingSystemTypeCode())){
    		view.getReceivingSystemTypeCode().setValue(distributorConfigurationForm.getReceivingSystemTypeCode());
    	}else{
    		view.setReceivingSystemTypeCode(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.RECEIVING_SYSTEM_INVOICE_CD, 
        			distributorConfigurationForm.getReceivingSystemTypeCode())));
    	}
    	
    	if(Utility.isSet(view.getRejectedInvoiceEmailNotification())){
    		view.getRejectedInvoiceEmailNotification().setEmailAddress(distributorConfigurationForm.getRejectedInvoiceEmailNotification());
    	}else{
    		view.setRejectedInvoiceEmailNotification(new EmailData(distributorConfigurationForm.getDistributorId(), 
        			null, null, RefCodeNames.EMAIL_TYPE_CD.REJECTED_INVOICE, RefCodeNames.EMAIL_STATUS_CD.ACTIVE,
        			distributorConfigurationForm.getRejectedInvoiceEmailNotification(),
        			new Boolean(false), null, null, null, null, null));
    	}
    	
    	if(Utility.isSet(view.getIgnoreOrderMinimumForFreight())){
    		view.getIgnoreOrderMinimumForFreight().setValue(distributorConfigurationForm.getIgnoreOrderMinimumForFreight());
    	}else{
    		view.setIgnoreOrderMinimumForFreight(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.IGNORE_ORDER_MIN_FOR_FREIGHT, 
        			distributorConfigurationForm.getIgnoreOrderMinimumForFreight())));
    	}
    	
    	if(Utility.isSet(view.getInvoiceAmountPercentUndercharge())){
    		view.getInvoiceAmountPercentUndercharge().setValue(distributorConfigurationForm.getInvoiceAmountPercentUndercharge());
    	}else{
    		view.setInvoiceAmountPercentUndercharge(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.INVOICE_AMT_PERCNT_ALLOW_LOWER, 
        			distributorConfigurationForm.getInvoiceAmountPercentUndercharge())));
    	}
    	
    	if(Utility.isSet(view.getInvoiceAmountPercentOvercharge())){
    		view.getInvoiceAmountPercentOvercharge().setValue(distributorConfigurationForm.getInvoiceAmountPercentOvercharge());
    	}else{
    		view.setInvoiceAmountPercentOvercharge(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.INVOICE_AMT_PERCNT_ALLOW_UPPER, 
        			distributorConfigurationForm.getInvoiceAmountPercentOvercharge())));
    	}
    	
    	if(Utility.isSet(view.getInvoiceMaximumFreightAllowance())){
    		view.getInvoiceMaximumFreightAllowance().setValue(distributorConfigurationForm.getInvoiceMaximumFreightAllowance());
    	}else{
    		view.setInvoiceMaximumFreightAllowance(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.DIST_MAX_INVOICE_FREIGHT, 
        			distributorConfigurationForm.getInvoiceMaximumFreightAllowance())));
    	}
    	
    	if(Utility.isSet(view.getInboundInvoiceHoldDays())){
    		view.getInboundInvoiceHoldDays().setValue(distributorConfigurationForm.getInboundInvoiceHoldDays());
    	}else{
    		view.setInboundInvoiceHoldDays(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.HOLD_INVOICE, 
        			distributorConfigurationForm.getInboundInvoiceHoldDays())));
    	}
    	
    	if(Utility.isSet(view.getPrintCustomerContactInfoOnPurchaseOrder())){
    		view.getPrintCustomerContactInfoOnPurchaseOrder().setValue(distributorConfigurationForm.getPrintCustomerContactInfoOnPurchaseOrder());
    	}else{
    		view.setPrintCustomerContactInfoOnPurchaseOrder(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.PRINT_CUST_CONTACT_ON_PO, 
        			distributorConfigurationForm.getPrintCustomerContactInfoOnPurchaseOrder())));
    	}
    	
    	if(Utility.isSet(view.getRequireManualPurchaseOrderAcknowledgement())){
    		view.getRequireManualPurchaseOrderAcknowledgement().setValue(distributorConfigurationForm.getRequireManualPurchaseOrderAcknowledgement());
    	}else{
    		view.setRequireManualPurchaseOrderAcknowledgement(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.MAN_PO_ACK_REQUIERED, 
        			distributorConfigurationForm.getRequireManualPurchaseOrderAcknowledgement())));
    	}
    	
    	if(Utility.isSet(view.getPurchaseOrderComments())){
    		view.getPurchaseOrderComments().setValue(distributorConfigurationForm.getPurchaseOrderComments());
    	}else{
    		view.setPurchaseOrderComments(updateDistributorProperty(PropertyUtil.toProperty(distributorConfigurationForm.getDistributorId(), 
    				DistributorPropertyTypeCode.PURCHASE_ORDER_COMMENTS, 
        			distributorConfigurationForm.getPurchaseOrderComments())));
    	}
    	
    	return view;
    }
    
	//For non-extra properties St.John specified a hyphen as the value of the property status, and for all properties specified
    //0 as the original user id.  Do the same here to make sure that a distributor created in Manta will work as if it had been 
    //created in St.John
    public static PropertyData updateDistributorProperty(PropertyData property) {
    	property.setOriginalUserId(new Long(0));
    	if (!property.getPropertyTypeCd().equalsIgnoreCase(RefCodeNames.PROPERTY_TYPE_CD.EXTRA)) {
        	property.setPropertyStatusCd(Constants.CHARS.HYPHEN);
    	}
    	return property;
    }

    public static ContactView createContact(ContactView contact, DistributorForm distributorForm) {

        if (contact == null) {
            contact = new ContactView();
        }

        PhoneData phone = contact.getPhone();
        if (phone == null) {
            phone = new PhoneData();
        }
        phone.setBusEntityId(distributorForm.getDistributorId());
        phone.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.PHONE);
        phone.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
        //phone.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_PHONE);
        phone.setPhoneNum(distributorForm.getContact().getTelephone());
        phone.setPrimaryInd(true);
        contact.setPhone(phone);

        PhoneData fax = contact.getFaxPhone();
        if (fax == null) {
        	fax = new PhoneData();
        }
        fax.setBusEntityId(distributorForm.getDistributorId());
        fax.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.FAX);
        fax.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
        //fax.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_FAX);
        fax.setPhoneNum(distributorForm.getContact().getFax());
        fax.setPrimaryInd(true);
        contact.setFaxPhone(fax);

        /*
        PhoneData mobile = contact.getMobilePhone();
        if (mobile == null) {
        	mobile = new PhoneData();
        }
        mobile.setBusEntityId(distributorForm.getDistributorId());
        mobile.setPhoneTypeCd(RefCodeNames.PHONE_TYPE_CD.MOBILE);
        mobile.setPhoneStatusCd(RefCodeNames.PHONE_STATUS_CD.ACTIVE);
        //mobile.setShortDesc(RefCodeNames.PHONE_SHORT_DESC_CD.PRIMARY_MOBILE);
        mobile.setPhoneNum(distributorForm.getContact().getMobile());
        mobile.setPrimaryInd(true);
        contact.setMobilePhone(mobile);
         */
        
        EmailData email = contact.getEmail();
        if (email == null) {
        	email = new EmailData();
        }
        email.setBusEntityId(distributorForm.getDistributorId());
        email.setEmailTypeCd(RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT);
        email.setEmailStatusCd(RefCodeNames.EMAIL_STATUS_CD.ACTIVE);
        email.setEmailAddress(distributorForm.getContact().getEmail());
        email.setPrimaryInd(true);
        contact.setEmail(email);

        AddressData address = contact.getAddress();
        if (address == null) {
            address = new AddressData();
        }

        address.setBusEntityId(distributorForm.getDistributorId());
        address.setName1(distributorForm.getContact().getFirstName());
        address.setName2(distributorForm.getContact().getLastName());
        address.setAddress1(distributorForm.getContact().getAddress().getAddress1());
        address.setAddress2(distributorForm.getContact().getAddress().getAddress2());
        address.setAddress3(distributorForm.getContact().getAddress().getAddress3());
        address.setAddress4(distributorForm.getContact().getAddress().getAddress4());
        address.setCountryCd(distributorForm.getContact().getAddress().getCountryCd());
        address.setPostalCode(distributorForm.getContact().getAddress().getPostalCode());
        address.setStateProvinceCd(distributorForm.getContact().getAddress().getStateProvinceCd());
        address.setAddressTypeCd(RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        address.setAddressStatusCd(RefCodeNames.ADDRESS_STATUS_CD.ACTIVE);
        address.setCity(distributorForm.getContact().getAddress().getCity());
        address.setPrimaryInd(true);

        contact.setAddress(address);

        return contact;
    }

    public static void fillOutEmailTemplateTypes(AppLocale locale, List<EmailTemplateListView> templates) {
        if (templates != null) {
            for (EmailTemplateListView temp : templates) {
                String messCode = "admin.template.email.emailType." +  temp.getEmailTypeCd();
                String emailType = I18nUtil.getMessage(messCode, null, true, locale.getLocale());
                if (Utility.isSet(emailType)) {
                    temp.setEmailType(emailType);
                }
            }
        }
    }
    
    public static List<Pair<String, String>> getOrderStatusesList(DbConstantResource resource) {
        List<Pair<String, String>> orderStatuses = new ArrayList<Pair<String, String>>();
        List<Pair<String, String>> orderStatusesTmp = resource.getOrderStatuses();
        if (Utility.isSet(orderStatusesTmp)) {
            String status;
            for (Pair<String, String> orderPair : orderStatusesTmp) {
                status = orderPair.getObject2();
                if (RefCodeNames.ORDER_STATUS_CD.ERP_RELEASED_PO_ERROR.equals(status) ||
                    RefCodeNames.ORDER_STATUS_CD.PRE_PROCESSED.equals(status) ||
                    RefCodeNames.ORDER_STATUS_CD.PROCESS_ERP_PO.equals(status) ||
                    RefCodeNames.ORDER_STATUS_CD.REFERENCE_ONLY.equals(status) ||
                    RefCodeNames.ORDER_STATUS_CD.READY_TO_SEND_TO_CUST_SYS.equals(status) ||
                    RefCodeNames.ORDER_STATUS_CD.SENDING_TO_ERP.equals(status) ||
                    RefCodeNames.ORDER_STATUS_CD.SENT_TO_CUST_SYSTEM.equals(status) ||
                    RefCodeNames.ORDER_STATUS_CD.WAITING_FOR_ACTION.equals(status)) {
                    continue;
                }
                orderStatuses.add(new Pair(orderPair.getObject1(), AppI18nUtil.getMessageOrNull("refcodes.ORDER_STATUS_CD." + orderPair.getObject1())));
            }
            Collections.sort(orderStatuses, new Comparator<Pair<String, String>>() {
                @Override
                public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                    return o1.getObject2().compareTo(o2.getObject2());
                }
            });
        }
        
        return orderStatuses;
    }
    
    public static List<Pair<String, String>> getWorkflowIndList(String workflowInd) {
        List<Pair<String, String>> inds = new ArrayList<Pair<String, String>>();
        if (Utility.isSet(workflowInd)) {
            inds.add(new Pair(workflowInd, AppI18nUtil.getMessageOrNull("refcodes.ORDER_WORKFLOW_IND." + workflowInd)));
        } else {
            inds.add(new Pair("", AppI18nUtil.getMessageOrDefault("admin.global.select", "- Select -")));
        }
        if (!RefCodeNames.WORKFLOW_IND_CD.SKIP.equals(workflowInd)) {
            inds.add(new Pair(RefCodeNames.WORKFLOW_IND_CD.SKIP,
                              AppI18nUtil.getMessageOrNull("refcodes.ORDER_WORKFLOW_IND." + RefCodeNames.WORKFLOW_IND_CD.SKIP)));
        }
        if (!RefCodeNames.WORKFLOW_IND_CD.SKIP.equals(workflowInd)) {
            inds.add(new Pair(RefCodeNames.WORKFLOW_IND_CD.TO_PROCESS,
                              AppI18nUtil.getMessageOrNull("refcodes.ORDER_WORKFLOW_IND." + RefCodeNames.WORKFLOW_IND_CD.TO_PROCESS)));
        }
        
        return inds;
    }

    public static List<Pair<String, String>> getOrderItemStatusList(DbConstantResource resource) {
    	List<Pair<String, String>> orderItemStatuses = new ArrayList<Pair<String, String>>();
        List<Pair<String, String>> orderItemStatusesTmp = resource.getOrderItemStatuses();
        orderItemStatuses.add(new Pair("",
                 AppI18nUtil.getMessageOrDefault("admin.global.select", "- Select -")));
        orderItemStatuses.add(new Pair("Ordered", AppI18nUtil.getMessageOrNull("refcodes.ORDER_ITEM_STATUS_CD.ORDERED")));
        for (Pair<String, String> pair : orderItemStatusesTmp) {
            orderItemStatuses.add(new Pair(pair.getObject2(), AppI18nUtil.getMessageOrNull("refcodes.ORDER_ITEM_STATUS_CD." + pair.getObject2())));
        } 
        return orderItemStatuses;
    }

    public static List<Pair<String, String>> getPoItemStatusList(DbConstantResource resource) {
        List<Pair<String, String>> poItemStatuses = new ArrayList<Pair<String, String>>();
        List<Pair<String, String>> poItemStatusesTmp = resource.getPurchaseOrderStatuses();
        poItemStatuses.add(new Pair("",
                 AppI18nUtil.getMessageOrDefault("admin.global.select", "- Select -")));
     
        for (Pair<String, String> pair : poItemStatusesTmp) {
            poItemStatuses.add(new Pair(pair.getObject2(), AppI18nUtil.getMessageOrNull("refcodes.PURCHASE_ORDER_STATUS_CD." + pair.getObject2())));
        }
        return poItemStatuses;
   }
    
    public static CumulativeSummaryView getCumulativeSummary(List<OrderItemIdentView> orderItems) {
        CumulativeSummaryView cumulativeSummary = new CumulativeSummaryView();
        
        Date lastDate = null;
        long ordered = 0L;
        long accepted = 0L;
        long backordered = 0L;
        long substituted = 0L;
        long invoiced = 0L;
        long returned = 0L;
        long shipped = 0L;
        if (Utility.isSet(orderItems)) {
            for (OrderItemIdentView orderItemView : orderItems) {
                long tmpAccepted = 0L;
                long tmpSubstituted = 0L;
                long tmpInvoiced = 0L;
                long tmpReturned = 0L;
                long tmpShipped = 0L;
                
                ordered += orderItemView.getOrderItem().getTotalQuantityOrdered();
                
                List<OrderItemActionData> itemActions = orderItemView.getOrderItemActions();
                if (Utility.isSet(itemActions)) {
                    for (OrderItemActionData itemAction : itemActions) {
                        
                        Date actionDate = itemAction.getActionDate();
                        if (actionDate == null){
                            actionDate = itemAction.getAddDate();
                        }
                        
                        if (lastDate == null) {
                            lastDate = actionDate;
                        } else {
                            if (lastDate.before(actionDate)) {
                                lastDate = actionDate;
                            }
                        }
                        
                        String actionCd = itemAction.getActionCd();
                        
                        if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.ACCEPTED.equals(actionCd)) {
                            tmpAccepted += itemAction.getQuantity();
                        } else if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.SUBSTITUTED.equals(actionCd)) {
                            tmpSubstituted += itemAction.getQuantity();
                        } else if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.CUST_INVOICED.equals(actionCd)) {
                            tmpInvoiced += itemAction.getQuantity();
                        } else if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.SHIPPED.equals(actionCd)) {
                            tmpShipped += itemAction.getQuantity();
                        } else if (RefCodeNames.ORDER_ITEM_DETAIL_ACTION_CD.RETURNED.equals(actionCd)) {
                            tmpReturned += itemAction.getQuantity();
                        }
                    }
                }
                
                // verify none of our totals went over on a per item basis...do not validate returned
                // as returned is not necessarily tied to qty ordered (may be overshipment/uom prob)
                
                long itmQty = orderItemView.getOrderItem().getTotalQuantityOrdered();
                
                if (itmQty < tmpAccepted) {
                    tmpAccepted = itmQty;
                }
                if (itmQty < tmpSubstituted) {
                    tmpSubstituted = itmQty;
                }
                if (itmQty < tmpInvoiced) {
                    tmpInvoiced = itmQty;
                }
                if (itmQty < tmpShipped) {
                    tmpShipped = itmQty;
                }
                
                accepted += tmpAccepted;
                substituted += tmpSubstituted;
                invoiced += tmpInvoiced;
                shipped += tmpShipped;
                returned += tmpReturned;
            }
            
            if (invoiced != 0L) {
                shipped = invoiced;
            }
            
            if (shipped == 0L) {
                backordered = 0L;
            } else {
                backordered = ordered - shipped;
            }
            cumulativeSummary.setLastDate(lastDate);
            cumulativeSummary.setOrdered(ordered);
            cumulativeSummary.setAccepted(accepted);
            cumulativeSummary.setBackordered(backordered);
            cumulativeSummary.setShipped(shipped);
            cumulativeSummary.setSubstituted(substituted);
            cumulativeSummary.setInvoiced(invoiced);
            cumulativeSummary.setReturned(returned);
        }
        
        return cumulativeSummary;
    }
    
    public static void setUiOrderAmounts(OrderForm form, OrderIdentView order) {
        OrderData orderD = order.getOrderData();
        
        if (Utility.isSet(orderD)) {
            BigDecimal subTotal = orderD.getTotalPrice();
            if (subTotal == null) {
                subTotal = new BigDecimal(0);
            }
            form.setSubTotal(subTotal.toPlainString());
            form.setSubTotalValue(subTotal);

            BigDecimal freight = orderD.getTotalFreightCost();
            if (freight == null) {
                freight = new BigDecimal(0);
            }
            form.setTotalFreightCost(freight.toPlainString());
            form.setTotalFreightCostValue(freight);

            BigDecimal misc = orderD.getTotalMiscCost();
            if (misc == null) {
                misc = new BigDecimal(0);
            }
            form.setTotalMiscCost(misc.toPlainString());
            form.setTotalMiscCostValue(misc);

            BigDecimal tax = orderD.getTotalTaxCost();
            if (tax == null) {
                tax = new BigDecimal(0);
            }
            form.setTotalTaxCost(tax.toPlainString());
            form.setTotalTaxCostValue(tax);
        
            BigDecimal rushCharge = orderD.getTotalRushCharge();
            if (rushCharge == null) {
                rushCharge = new BigDecimal(0);
            }
            form.setRushOrderCharge(rushCharge.toPlainString());
            form.setRushOrderChargeValue(rushCharge);
        
            List<OrderMetaData> orderMeta = order.getOrderMeta();
        
            if (Utility.isSet(orderMeta)) {
                OrderMetaData smallOrderFee = OrderUtil.getMetaObject(orderMeta, RefCodeNames.CHARGE_CD.SMALL_ORDER_FEE, orderD.getModDate());
                if (smallOrderFee != null) {
                    form.setSmallOrderFeeValue(new BigDecimal(smallOrderFee.getValue()));
                    form.setSmallOrderFee(smallOrderFee.getValue());
                } else {
                    form.setSmallOrderFeeValue(null);
                    form.setSmallOrderFee(null);
                }

                OrderMetaData fuelSurcharge = OrderUtil.getMetaObject(orderMeta, RefCodeNames.CHARGE_CD.FUEL_SURCHARGE, orderD.getModDate());
                if (fuelSurcharge != null) {
                    form.setFuelSurChargeValue(new BigDecimal(fuelSurcharge.getValue()));
                    form.setFuelSurCharge(fuelSurcharge.getValue());
                } else {
                    form.setFuelSurChargeValue(null);
                    form.setFuelSurCharge(null);
                }
                
                OrderMetaData discount = OrderUtil.getMetaObject(orderMeta, RefCodeNames.CHARGE_CD.DISCOUNT, orderD.getModDate());
                if (discount != null) {
                    form.setDiscountValue(new BigDecimal(discount.getValue()));
                    form.setDiscount(discount.getValue());
                } else {
                    form.setDiscountValue(null);
                    form.setDiscount(null);
                }
            }
        } else {
            form.setRushOrderChargeValue(null);
            form.setRushOrderCharge(null);
            form.setSmallOrderFeeValue(null);
            form.setSmallOrderFee(null);
            form.setFuelSurChargeValue(null);
            form.setFuelSurCharge(null);
        }

        form.calculateTotalAmount();
    }
    
    public static DistributionSummaryView getDistributionInformation(OrderIdentView orderInfo, List<OrderItemIdentView> orderItems) {
        DistributionSummaryView distSummary = new DistributionSummaryView();
        OrderData orderD = orderInfo.getOrderData();
        if (Utility.isSet(orderD)) {
            Long orderId = orderD.getOrderId();
            String orderStatus = orderD.getOrderStatusCd();
            Boolean allowChangeToShipping = RefCodeNames.ORDER_STATUS_CD.PENDING_REVIEW.equals(orderStatus) ||
                                            RefCodeNames.ORDER_STATUS_CD.PENDING_ORDER_REVIEW.equals(orderStatus);
            distSummary.setOrderId(orderId);
            distSummary.setOrderStatus(orderStatus);
            distSummary.setAllowChangeToShipping(allowChangeToShipping);
            
            if (Utility.isSet(orderItems)) {
                Map<String, DistSummaryLineView> distLineMap = new HashMap<String, DistSummaryLineView>();
                for (OrderItemIdentView orderItem : orderItems) {
                    if (!RefCodeNames.ORDER_ITEM_STATUS_CD.CANCELLED.equals(orderItem.getOrderItem().getOrderItemStatusCd())) {
                        Long distId = orderItem.getDistId();
                        String distName = orderItem.getDistName();
                        if (!Utility.isSet(distName)) {
                            distName = "-";
                        }
                        String distErpNum = orderItem.getOrderItem().getDistErpNum();
                        if (!Utility.isSet(distErpNum)) {
                            distErpNum = "-";
                        }
                        DistOptionsForShippingView distShipOption = OrderUtil.getDistShipOption(orderInfo.getDistShippingOptions(), distErpNum);
                        DistSummaryLineView distLine = distLineMap.get(distErpNum);
                        if (distLine == null) {
                            distLine = new DistSummaryLineView();
                            distLine.setDistId(distId);
                            distLine.setDistErpNum(distErpNum);
                            distLine.setDistName(distName);
                            distLine.setDistLineItemCount(0L);
                            distLine.setDistTotal(BigDecimal.ZERO);
                            distLine.setDistShipOption(distShipOption);
                        }
                        Long itemLineCount = distLine.getDistLineItemCount();
                        distLine.setDistLineItemCount(++itemLineCount);
                        
                        BigDecimal distItemCost = orderItem.getOrderItem().getDistItemCost();
                        BigDecimal orderedQty = new BigDecimal(orderItem.getOrderItem().getTotalQuantityOrdered());

                        if (distItemCost != null) {
                            distItemCost = distItemCost.multiply(orderedQty);
                            BigDecimal sum = distLine.getDistTotal();
                            sum = sum.add(distItemCost);
                            distLine.setDistTotal(sum);
                        }
                        distLineMap.put(distErpNum, distLine);
                    }
                }
                distSummary.setDistSummaryLines(new ArrayList(distLineMap.values()));
            }
        }
        return distSummary;
    }
    
    public static void fillOutOrderForm(OrderForm form,
                                        OrderIdentView orderInfo,
                                        List<OrderItemIdentView> orderItems,
                                        List<OrderPropertyData> customerOrderNotes,
                                        List<NoteJoinView> siteCRCNotes,
                                        List<InvoiceCustView> invoicesInfo,
                                        String storeType,
                                        String orderPlacedBy,
                                        Boolean isSimpleServiceOrder,
                                        Boolean showDistNote,
                                        Boolean reBillOrder,
                                        AppLocale locale,
                                        DbConstantResource resource) {
        if (form == null) {
            form = new OrderForm();
        }

        try {
            if (orderInfo != null && orderInfo.getOrderData() != null) {
                if (Utility.isSet(orderInfo.getOrderExtras())) {
                    orderInfo.setAccountId(orderInfo.getOrderExtras().getAccountId());
                    orderInfo.setAccountName(orderInfo.getOrderExtras().getAccountName());
                }
                form.setOrderInfo(orderInfo);
                OrderData orderData = orderInfo.getOrderData();
                
                form.setOrderStatuses(getOrderStatusesList(resource));
                form.setWorkflowStatuses(getWorkflowIndList(orderData.getWorkflowInd()));
                form.setOrderItemStatuses(getOrderItemStatusList(resource));
                form.setPoItemStatuses(getPoItemStatusList(resource));
                
                String orderedDate = "";
                if (orderData.getRevisedOrderDate() != null) {
                    orderedDate = AppI18nFormatter.formatDate(orderData.getRevisedOrderDate(), locale) + " " +
                                  AppI18nFormatter.formatTime(orderData.getRevisedOrderTime(), locale);
                } else {
                    orderedDate = AppI18nFormatter.formatDate(orderData.getOriginalOrderDate(), locale) + " " +
                                  AppI18nFormatter.formatTime(orderData.getOriginalOrderTime(), locale);
                }
                form.setOrderedDate(orderedDate);

                if (Utility.isSet(orderInfo.getOrderMeta())) {
                    for (OrderMetaData meta : orderInfo.getOrderMeta()) {
                        if (RefCodeNames.CHARGE_CD.SMALL_ORDER_FEE.equals(meta.getName())) {
                            form.setSmallOrderFeeValue(AppI18nUtil.parseAmountNN(AppLocale.SYSTEM_LOCALE, meta.getValue()));
                        } else if (RefCodeNames.CHARGE_CD.FUEL_SURCHARGE.equals(meta.getName())) {
                            form.setFuelSurChargeValue(AppI18nUtil.parseAmountNN(AppLocale.SYSTEM_LOCALE, meta.getValue()));
                        }
                    }
                }
                
                if (Utility.isSet(orderInfo.getOrderProperties())) {
                    List<OrderPropertyData> orderNotes = new ArrayList<OrderPropertyData>();
                    for (OrderPropertyData orderProperty : orderInfo.getOrderProperties()) {
                        if (RefCodeNames.ORDER_PROPERTY_TYPE_CD.ORDER_NOTE.equals(orderProperty.getOrderPropertyTypeCd())) {
                            orderNotes.add(orderProperty);
                        }
                    }
                    if (!orderNotes.isEmpty()) {
                        Collections.sort(orderNotes, AppComparator.ORDER_PROPERTY_COMPARATOR_DATE_DESC);
                        form.setOrderNote(orderNotes.get(0));
                    }
                }

                //form.calculateTotalAmount();
                setUiOrderAmounts(form, orderInfo);
                
                /*
                form.setSubTotal(AppI18nFormatter.formatCurrency(form.getSubTotalValue(), locale));
                form.setTotalFreightCost(AppI18nFormatter.formatCurrency(form.getTotalFreightCostValue(), locale));
                form.setTotalMiscCost(AppI18nFormatter.formatCurrency(form.getTotalMiscCostValue(), locale));
                form.setSmallOrderFee(AppI18nFormatter.formatCurrency(form.getSmallOrderFeeValue(), locale));
                form.setFuelSurCharge(AppI18nFormatter.formatCurrency(form.getFuelSurChargeValue(), locale));
                form.setTotalTaxCost(AppI18nFormatter.formatCurrency(form.getTotalTaxCostValue(), locale));                
                form.setTotalAmount(AppI18nFormatter.formatCurrency(form.getTotalAmountValue(), locale));
                

                form.setSubTotal(form.getSubTotalValue().toPlainString());
                form.setTotalFreightCost(form.getTotalFreightCostValue().toPlainString());
                form.setTotalMiscCost(form.getTotalMiscCostValue().toPlainString());
                form.setSmallOrderFee(form.getSmallOrderFeeValue().toPlainString());
                form.setFuelSurCharge(form.getFuelSurChargeValue().toPlainString());
                form.setTotalTaxCost(form.getTotalTaxCostValue().toPlainString());                
                form.setTotalAmount(form.getTotalAmountValue().toPlainString());
                */

                form.setOrderStatus(getFieldNameForValue(resource.getOrderStatuses(), orderData.getOrderStatusCd()));
                form.setWorkflowStatus(orderData.getWorkflowInd());                
                form.setStoreType(storeType);
                form.setSimpleServiceOrder(isSimpleServiceOrder);
                form.setOrderItems(orderItems);
                form.setCumulativeSummary(getCumulativeSummary(orderItems));
                form.setCustomerOrderNotes(customerOrderNotes);
                form.setOrderPlacedBy(orderPlacedBy);
                
                form.setDistributionSummary(getDistributionInformation(orderInfo, orderItems));
                
                boolean showCancelButton = true;
                if (Utility.isSet(orderItems)) {
                    for (OrderItemIdentView itemIdent : orderItems) {
                        if (RefCodeNames.STORE_TYPE_CD.MLA.equals(storeType)) {
                            if (Utility.isSet(itemIdent.getInvoiceCustDetails())) {
                                showCancelButton = false;
                            }
                        } else if (Utility.isSet(itemIdent.getInvoiceDistDetails())) {
                            showCancelButton = false;
                        }
                    }
                }
                
                form.setSiteNotes(siteCRCNotes);
                form.setInvoices(invoicesInfo);
                
                String workflowInd = orderInfo.getOrderData().getWorkflowInd();
                if (RefCodeNames.WORKFLOW_IND_CD.PROCESSED.equals(workflowInd) ||
                    RefCodeNames.WORKFLOW_IND_CD.SKIP.equals(workflowInd)){
                    form.setProcessCustomerWorkflow(false);
                } else {
                    form.setProcessCustomerWorkflow(true);
                }
                
                form.setShowCancelButton(showCancelButton);
                form.setShowDistNote(showDistNote);
                form.setRebillOrder(reBillOrder);
                
                form.setNewContractId("");
                form.setNewOrderDate("");
                
                boolean applyBudget = false;
                if (!RefCodeNames.ORDER_BUDGET_TYPE_CD.NON_APPLICABLE.equals(orderData.getOrderBudgetTypeCd())) {
                    applyBudget = true;
                }
                form.setApplyBudget(applyBudget);

            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
    
    private static String getFieldNameForValue(List<Pair<String, String>> pairs, String value) {
        String name = "";
        if (Utility.isSet(pairs) && Utility.isSet(value)) {
            for (Pair pair : pairs) {
                if (pair.getObject2().equals(value)) {
                    name = (String)pair.getObject1();
                    break;
                }
            }
        }
        return name;
    }

    public static String fillItemUomCd(String uomValue) {
        String uomCd = null;
        if (Utility.isSet(uomValue)) {
            String value = RefCodeNamesKeys.findRefCodeByValue("ITEM_UOM_CD", uomValue);
            if (Utility.isSet(value)) {
                return uomValue;
            } else {
                return RefCodeNames.ITEM_UOM_CD.UOM_OTHER;    
            }
        }
        return uomCd;
    }


    public static List<OrderGuideItemView> fillOutOrderGuideItemsList(
                            OrderGuideService orderGuideService,
                            Long catalogId,
                            List<ProductListView> sourceItemsList
                    ) {
        List<OrderGuideItemView> ogItems = new ArrayList<OrderGuideItemView>();
        if (sourceItemsList.size() > 0) {
            for (ProductListView product : sourceItemsList) {
                OrderGuideItemView ogItem = new OrderGuideItemView();

                ogItem.setItemId(product.getItemId());
                ogItem.setCategoryItemId(product.getItemCategoryId());
                ogItem.setCategory(product.getItemCategoryName());
                ogItem.setProductName(product.getItemName());
                ogItem.setPack(product.getItemPack());
                ogItem.setItemSkuNum(product.getItemSku());
                ogItem.setMfg(product.getItemManufacturerName());
                ogItem.setMfgSku(product.getItemManufacturerSku());

                ogItems.add(ogItem);
            }
            if (ogItems.size() > 0) {
                ogItems = orderGuideService.fillOutOrderGuideItemsData(ogItems, catalogId);
            }
        }
        return ogItems;
    }
    
}
