package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.UserNotificationForm;
import com.espendwise.manta.web.tags.UserEmailNotificationFormBinder;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Controller
@RequestMapping(UrlPathKey.USER.NOTIFICATION)
public class UserNotificationController extends BaseController {

    private static final Logger logger = Logger.getLogger(UserGroupController.class);

    private UserService userService;

    @Autowired
    public UserNotificationController(UserService userService) {
        this.userService = userService;
    }

    private String handleValidationException(WebRequest request, ValidationException e) {
        logger.info("handleValidationException()=> exception: " + e);
        return "user/notification";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.USER_NOTIFICATION) UserNotificationForm form, @PathVariable Long userId) {

        UserData user = userService.findByUserId(userId);
        List<PropertyData> emailProperties = userService.findEmailNotificationProperties(user);

        UserEmailNotificationFormBinder.data2Form(
                form,
                user,
                emailProperties
        );


        return "user/notification";

    }


    @SuccessMessage
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String update(WebRequest request, @ModelAttribute(SessionKey.USER_NOTIFICATION) UserNotificationForm form) throws Exception {

        logger.info("update()=> BEGIN");

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {
            new WebErrors(request).putErrors(validationErrors);
            return "user/notification";
        }

        Long storeId = getStoreId();

        UserData user = userService.findByUserId(form.getUserId());

        if (user != null) {

            try {

                logger.info("update()=> emailNotificationMapping: " + form.getEmailNotificationMapping());

                List<PropertyData> emailProperties = userService.findEmailNotificationProperties(user);

                logger.info("update()=> original user role : " + user.getUserRoleCd());
                logger.info("update()=> original emailProperties : " + emailProperties);

                user = UserEmailNotificationFormBinder.refreshUserRightsProperties(
                        user,
                        form.getEmailNotificationMapping()
                );

                emailProperties = UserEmailNotificationFormBinder.refreshEmailNotificationProperties(form,
                        user,
                        emailProperties,
                        form.getEmailNotificationMapping()
                );

                logger.info("update()=> emailProperties: " + emailProperties);
                logger.info("update()=>  user role after refresh: " + user.getUserRoleCd());

                userService.configureUserNotifications(
                        storeId,
                        user,
                        emailProperties
                );

            } catch (ValidationException e) {

                return handleValidationException(request, e);

            }

        }

        logger.info("update()=> END.");

        return redirect(UrlPathAssistent.createPath(request, UrlPathKey.USER.NOTIFICATION));


    }


    @ModelAttribute(SessionKey.USER_NOTIFICATION)
    public UserNotificationForm init() {
        return new UserNotificationForm();
    }

}
