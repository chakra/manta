package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.CommonLinkedProperty;
import com.espendwise.manta.util.CommonProperty;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.UserNotificationForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

import java.util.Map;

public class UserNotificationFormValidator  extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        UserNotificationForm valueObj = (UserNotificationForm) obj;

        CommonLinkedProperty userGetsEmailCutoffTomeReminder = valueObj
                .getEmailNotificationMapping()
                .get(valueObj.getCutoffTimeReminder());

        if (userGetsEmailCutoffTomeReminder != null && Utility.isTrue(userGetsEmailCutoffTomeReminder.getValue())) {

            if (Utility.isSet(userGetsEmailCutoffTomeReminder.getLinkedMap())) {

                CommonProperty cutoffTimeEmailReminderCnt = userGetsEmailCutoffTomeReminder
                        .getLinkedMap()
                        .get(valueObj.getCutoffTimeEmailReminderCntPropertyName());

                if (cutoffTimeEmailReminderCnt != null && Utility.isSet(cutoffTimeEmailReminderCnt.getValue())) {

                    ValidationResult vr = Validators
                            .getIntegerValidator()
                            .validate(cutoffTimeEmailReminderCnt.getValue(), new NumberErrorWebResolver("admin.user.configuration.notif.label.numberofTimes"));
                    if (vr != null) {
                        errors.putErrors(vr.getResult());
                    }

                }
            }
        }

        CommonLinkedProperty serviceReminder = valueObj
                .getEmailNotificationMapping()
                .get(valueObj.getServiceReminder());

        if (serviceReminder != null && Utility.isTrue(serviceReminder.getValue())) {

            if (Utility.isSet(serviceReminder.getLinkedMap())) {

                CommonProperty reminderNumberDaily = serviceReminder
                        .getLinkedMap()
                        .get(valueObj.getServiceReminderNumberDailyPropertyName());

                if (reminderNumberDaily != null && Utility.isSet(reminderNumberDaily.getValue())) {

                    ValidationResult vr = Validators
                            .getIntegerValidator()
                            .validate(reminderNumberDaily.getValue(), new NumberErrorWebResolver("admin.user.configuration.notif.label.numberofDailyReminders"));

                    if (vr != null) {
                        errors.putErrors(vr.getResult());
                    }
                }


                CommonProperty reminderNumbeWeekly = serviceReminder
                        .getLinkedMap()
                        .get(valueObj.getServiceReminderNumberWeeklyPropertyName());

                if (reminderNumbeWeekly != null && Utility.isSet(reminderNumbeWeekly.getValue())) {

                    ValidationResult vr = Validators
                            .getIntegerValidator()
                            .validate(reminderNumbeWeekly.getValue(), new NumberErrorWebResolver("admin.user.configuration.notif.label.numberofWeeklyReminders"));

                    if (vr != null) {
                        errors.putErrors(vr.getResult());
                    }
                }
            }
        }

        return new MessageValidationResult(errors.get());
    }

}
