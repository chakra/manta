package com.espendwise.manta.web.tags;

import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.util.*;
import com.espendwise.manta.web.forms.UserNotificationForm;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserEmailNotificationFormBinder {

    private static final Logger logger = Logger.getLogger(UserEmailNotificationFormBinder.class);

    public static void retrieveAllServiceTicketsForm(UserNotificationForm form, UserData user, UserEmailRights emailRights, List<PropertyData> emailProperties) {

        for (NamedPropertyTypeCode e : UserNotificationForm.ALL_SERVICE_TICKET_NOTIFICATIONS) {

            CommonLinkedProperty valueProperty =
                    PropertyUtil.createCommonLinkedProperty(
                            e.getTypeCode(),
                            Utility.isTrueStrOf(PropertyUtil.toValueNN(PropertyUtil.findP(emailProperties, e)))
                    );

            form.getEmailNotificationMapping().put(e.name(), valueProperty);

        }
    }


    public static void retrieveServiceTypesNotificationsForm(UserNotificationForm form, UserData user, UserEmailRights emailRights, List<PropertyData> emailProperties) {

        for (NamedPropertyTypeCode e : UserNotificationForm.SERVICE_TYPE_NOTIFICATIONS) {

            CommonLinkedProperty valueProperty = PropertyUtil.createCommonLinkedProperty(e.getTypeCode(),
                    Utility.isTrueStrOf(PropertyUtil.toValueNN(PropertyUtil.findP(emailProperties, e)))
            );

            if (UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER.name().equals(e.name())) {

                valueProperty.getLinkedMap().put(
                        UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER_DAYS_DAILY.name(),
                        PropertyUtil.createCommonLinkedProperty(
                                UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER_DAYS_DAILY.getTypeCode(),
                                PropertyUtil.toValueNN(PropertyUtil.findP(emailProperties, UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER_DAYS_DAILY))
                        )
                );

                valueProperty.getLinkedMap().put(
                        UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER_DAYS_WEEKLY.name(),
                        PropertyUtil.createCommonLinkedProperty(
                                UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER_DAYS_WEEKLY.getTypeCode(),
                                PropertyUtil.toValueNN(PropertyUtil.findP(emailProperties, UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER_DAYS_WEEKLY))
                        )
                );

            }


            form.getEmailNotificationMapping().put(e.name(), valueProperty);

        }
    }


    public static void retrieveGoodPurchasingEmailForm(UserNotificationForm form, UserData user, UserEmailRights emailRights, List<PropertyData> emailProperties) {

        for (NamedPropertyTypeCode e : UserNotificationForm.GOOD_PURCHASING_EMAIL) {

            CommonLinkedProperty valueProperty = PropertyUtil.createCommonLinkedProperty(e.getTypeCode(),
                    Utility.isTrueStrOf(emailRights.get(e.name()))
            );

            form.getEmailNotificationMapping().put(e.name(), valueProperty);

        }

    }

    public static void retrieveCorporateOrderNotificationForm(UserNotificationForm form, UserData user, UserEmailRights emailRights, List<PropertyData> emailProperties) {

        for (NamedPropertyTypeCode e : UserNotificationForm.CORPORATE_ORDER_NOTIFICATION) {

            CommonLinkedProperty valueProperty =
                    e instanceof UserEmailRightCodes
                            ? PropertyUtil.createCommonLinkedProperty(e.getTypeCode(), Utility.isTrueStrOf(emailRights.get(e.name())))
                            : PropertyUtil.createCommonLinkedProperty(e.getTypeCode(), Utility.isTrueStrOf(PropertyUtil.toValueNN(PropertyUtil.findP(emailProperties, e))));



            if (UserEmailRightCodes.USER_GETS_EMAIL_CUTOFF_TIME_REMINDER.name().equals(e.name())) {
                UserEmailNotificationTypeCodes propCode = UserEmailNotificationTypeCodes.CUTOFF_TIME_EMAIL_REMINDER_CNT;
                CommonLinkedProperty linkedValueProperty = PropertyUtil.createCommonLinkedProperty(propCode.getTypeCode(),
                        PropertyUtil.toValueNN(PropertyUtil.findP(emailProperties, propCode))
                );
                valueProperty.getLinkedMap().put(propCode.name(), linkedValueProperty);
            }

            form.getEmailNotificationMapping().put(e.name(), valueProperty);
        }


    }


    public static PropertyData handlePropertyData(UserData user,
                                                  UserEmailNotificationTypeCodes e,
                                                  List<PropertyData> propertyDatas,
                                                  CommonProperty p,
                                                  boolean booleanValue,
                                                  boolean skipValue) {


        String value = skipValue || p == null ? null : p.getValue();

        PropertyData property = PropertyUtil.findP(propertyDatas, e);

        logger.info("handlePropertyData()=>\n" +
                "\n, booleanValue: " + booleanValue +
                "\n, skipValue: " + skipValue +
                "\n, p: " + (p != null ? p.getTypeCd() + "->" + p.getValue() : "NULL") +
                "\n, property: " + (property != null ? property.getPropertyTypeCd() + "->" + property.getValue() : "NULL")

        );

        if (property == null && p != null && Utility.isSet(value)) {

            if (!booleanValue || Utility.isTrue(value)) {

                logger.info("handlePropertyData()=> create property[" + p.getTypeCd() + "]->" + value + "");

                return PropertyUtil.createProperty(
                        null,
                        user.getUserId(),
                        p.getTypeCd(),
                        p.getTypeCd(),
                        value,
                        RefCodeNames.PHONE_STATUS_CD.ACTIVE,
                        null
                );
            }

        } else if (property != null) {

            logger.info("handlePropertyData()=> create property[" + (p == null ? "NULL" : p.getTypeCd()) + "]->" + value + "");

            property.setValue(value);
            return property;

        }

        return property;

    }

    public static UserData refreshUserRightsProperties(UserData user, Map<String, CommonLinkedProperty> emailNotificationMapping) {

        logger.info("refreshUserRightsProperties()=> BEGIN");

        UserEmailRights emailRights = new UserEmailRights(user.getUserRoleCd());

        for (UserEmailRightCodes e : UserEmailRightCodes.values()) {
            CommonLinkedProperty property = emailNotificationMapping.get(e.name());
            emailRights.put(e.name(), property != null && Utility.isTrue(property.getValue()));
        }

        logger.info("refreshUserRightsProperties()=> updating user data");

        user = emailRights.updateRole(user);

        logger.info("refreshUserRightsProperties()=> END.");

        return user;
    }


    public static UserNotificationForm data2Form(UserNotificationForm form,
                                                 UserData user,
                                                 List<PropertyData> emailProperties) {


        UserEmailRights emailRights = new UserEmailRights(user.getUserRoleCd());

        UserEmailNotificationFormBinder.retrieveAllServiceTicketsForm(
                form,
                user,
                emailRights,
                emailProperties
        );

        UserEmailNotificationFormBinder.retrieveCorporateOrderNotificationForm(
                form,
                user,
                emailRights,
                emailProperties
        );

        UserEmailNotificationFormBinder.retrieveGoodPurchasingEmailForm(
                form,
                user,
                emailRights,
                emailProperties
        );

        UserEmailNotificationFormBinder.retrieveServiceTypesNotificationsForm(
                form,
                user,
                emailRights,
                emailProperties
        );

        return form;
    }


    public static List<PropertyData> refreshEmailNotificationProperties(UserNotificationForm form, UserData user, List<PropertyData> propertyDatas, Map<String, CommonLinkedProperty> emailNotificationMapping) {

        logger.info("refreshEmailNotificationProperties()=> BEGIN");

        List<PropertyData> properties = new ArrayList<PropertyData>();

        for (UserEmailNotificationTypeCodes e : UserEmailNotificationTypeCodes.values()) {

            CommonLinkedProperty formProperty = emailNotificationMapping.get(e.name());

            logger.info("refreshEmailNotificationProperties()=> processing code: " + e);
            logger.info("refreshEmailNotificationProperties()=> form property: " + (formProperty != null
                    ? "(" + formProperty.getTypeCd() + "->" + formProperty.getValue() + ")"
                    : "object is null")
            );

            if (e.name().equals(form.getCutoffTimeEmailReminderCntPropertyName())) {

                CommonLinkedProperty p = emailNotificationMapping.get(form.getCutoffTimeReminder());

                CommonProperty reminderCntFormProperty = Utility.isSet(p) && Utility.isSet(p.getLinkedMap())
                        ? p.getLinkedMap().get(form.getCutoffTimeEmailReminderCntPropertyName())
                        : null;

                properties.add(
                        UserEmailNotificationFormBinder.handlePropertyData(
                                user,
                                e,
                                propertyDatas,
                                reminderCntFormProperty,
                                false,
                                p == null || !Utility.isTrue(p.getValue())
                        )
                );


            } else if (e.name().equals(form.getServiceReminderNumberWeeklyPropertyName())) {

                CommonLinkedProperty p = emailNotificationMapping.get(form.getServiceReminder());

                CommonProperty reminderNumberWeeklyFormProperty = Utility.isSet(p) && Utility.isSet(p.getLinkedMap())
                        ? p.getLinkedMap().get(form.getServiceReminderNumberWeeklyPropertyName())
                        : null;


                properties.add(
                        UserEmailNotificationFormBinder.handlePropertyData(
                                user,
                                e,
                                propertyDatas,
                                reminderNumberWeeklyFormProperty,
                                false,
                                p == null || !Utility.isTrue(p.getValue())
                        )
                );


            } else if (e.name().equals(form.getServiceReminderNumberDailyPropertyName())) {

                CommonLinkedProperty p = emailNotificationMapping.get(form.getServiceReminder());

                CommonProperty reminderNumberDailyFormProperty = Utility.isSet(p) && Utility.isSet(p.getLinkedMap())
                        ? p.getLinkedMap().get(form.getServiceReminderNumberDailyPropertyName())
                        : null;

                properties.add(
                        UserEmailNotificationFormBinder.handlePropertyData(
                                user,
                                e,
                                propertyDatas,
                                reminderNumberDailyFormProperty,
                                false,
                                p == null || !Utility.isTrue(p.getValue())
                        )
                );

            } else {


                CommonLinkedProperty p = emailNotificationMapping.get(e.name());

                properties.add(
                        UserEmailNotificationFormBinder.handlePropertyData(
                                user,
                                e,
                                propertyDatas,
                                p,
                                true,
                                false)
                );


            }


        }

        logger.info("refreshEmailNotificationProperties()=> END.");

        return properties;

    }
}
