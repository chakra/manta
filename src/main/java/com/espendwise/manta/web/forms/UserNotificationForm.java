package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.UserNotificationFormValidator;

import java.util.HashMap;
import java.util.Map;


@Validation(UserNotificationFormValidator.class)
public class UserNotificationForm extends WebForm implements Initializable {

    public static NamedPropertyTypeCode[] GOOD_PURCHASING_EMAIL = new NamedPropertyTypeCode[]{
            UserEmailRightCodes.USER_GETS_EMAIL_ORDER_DETAIL_APPROVED,
            UserEmailRightCodes.USER_GETS_EMAIL_ORDER_NEEDS_APPROVAL,
            UserEmailRightCodes.USER_GETS_EMAIL_ORDER_WAS_APPROVED,
            UserEmailRightCodes.USER_GETS_EMAIL_ORDER_WAS_REJECTED,
            UserEmailRightCodes.USER_GETS_EMAIL_ORDER_WAS_MODIFIED,
            UserEmailRightCodes.USER_GETS_EMAIL_ORDER_SHIPPED
    };

    public static NamedPropertyTypeCode[] CORPORATE_ORDER_NOTIFICATION = new NamedPropertyTypeCode[]{
            UserEmailRightCodes.USER_GETS_EMAIL_CUTOFF_TIME_REMINDER,
            UserEmailNotificationTypeCodes.RECEIVE_INV_MISSING_EMAIL,
            UserEmailRightCodes.USER_GETS_EMAIL_PHYSICAL_INV_NON_COMPL_SITE_LISTING,
            UserEmailRightCodes.USER_GETS_EMAIL_PHYSICAL_INV_COUNTS_PAST_DUE

    };

    public static NamedPropertyTypeCode[] SERVICE_TYPE_NOTIFICATIONS = new NamedPropertyTypeCode[]{
            UserEmailNotificationTypeCodes.NOTIFCTN_RESCHEDULED_SERVICE,
            UserEmailNotificationTypeCodes.NOTIF_REQ_SERVICE_APPROVAL,
            UserEmailNotificationTypeCodes.NOTIFCTN_NO_SHOW,
            UserEmailNotificationTypeCodes.NOTIFCTN_SMS_NO_SHOW,
            UserEmailNotificationTypeCodes.NOTIFCTN_SCHEDULED_SERVICE,
            UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_ACCEPTED,
            UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_ACCEPTED_AUTO,
            UserEmailNotificationTypeCodes.NOTIF_SERVICE_ACCEPTED_AUTO_V,
            UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER
    };

    public static NamedPropertyTypeCode[] ALL_SERVICE_TICKET_NOTIFICATIONS = new NamedPropertyTypeCode[]{
            UserEmailNotificationTypeCodes.NOTIF_SERVICE_REJ_BY_PROVIDER,
            UserEmailNotificationTypeCodes.NOTIF_SERVICE_REJ_BY_STORE,
            UserEmailNotificationTypeCodes.NOTIF_SERVICE_COMPLETED
    };


    private boolean init;
    private Long userId;

    private Map<String, CommonLinkedProperty> emailNotificationMapping = new HashMap<String, CommonLinkedProperty>();


    public void initialize() {
        this.init = true;
    }

    public boolean isInitialized() {
        return init;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<String, CommonLinkedProperty> getEmailNotificationMapping() {
        return emailNotificationMapping;
    }

    public void setEmailNotificationMapping(Map<String, CommonLinkedProperty> emailNotificationMapping) {
        this.emailNotificationMapping = emailNotificationMapping;
    }


    public String[] getAllServiceTicketNotifications() {
        return Utility.names(ALL_SERVICE_TICKET_NOTIFICATIONS).toArray(new String[ALL_SERVICE_TICKET_NOTIFICATIONS.length]);
    }

    public String[] getServiceTypesNotifications() {
        return Utility.names(SERVICE_TYPE_NOTIFICATIONS).toArray(new String[SERVICE_TYPE_NOTIFICATIONS.length]);
    }

    public String[] getCorporateOrderNotifications() {
        return Utility.names(CORPORATE_ORDER_NOTIFICATION).toArray(new String[CORPORATE_ORDER_NOTIFICATION.length]);
    }

    public String[] getFoodPurchasingEmail() {
        return Utility.names(GOOD_PURCHASING_EMAIL).toArray(new String[GOOD_PURCHASING_EMAIL.length]);
    }

    public String getServiceReminder() {
        return UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER.name();
    }

    public String getServiceReminderNumberWeeklyPropertyName(){
        return UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER_DAYS_WEEKLY.name();
    }

    public String getServiceReminderNumberDailyPropertyName(){
        return UserEmailNotificationTypeCodes.NOTIFCTN_SERVICE_REMINDER_DAYS_DAILY.name();
    }

    public String getCutoffTimeEmailReminderCntPropertyName(){
        return UserEmailNotificationTypeCodes.CUTOFF_TIME_EMAIL_REMINDER_CNT.name();
    }

    public String getCutoffTimeReminder(){
        return UserEmailRightCodes.USER_GETS_EMAIL_CUTOFF_TIME_REMINDER.name();
    }
}
