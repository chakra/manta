package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.ProfilePasswordMgrView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.ProfileService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.ProfilePasswordMgrForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;


import java.util.List;


@Controller
@RequestMapping(UrlPathKey.PROFILE.PASSWORD_MANAGEMENT)
public class ProfilePasswordMgrController extends BaseController {

    private static final Logger logger = Logger.getLogger(ProfilePasswordMgrController.class);
    
    private ProfileService profileService;

    @Autowired
    public ProfilePasswordMgrController(ProfileService profileService) {
        this.profileService = profileService;
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "profile/passwordManagement";
    }

    @SuccessMessage
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.PROFILE_PASSWORD_MANAGEMENT) ProfilePasswordMgrForm passwordMgrForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, passwordMgrForm: "+passwordMgrForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(passwordMgrForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.PROFILE_PASSWORD_MANAGEMENT, passwordMgrForm);
            return "profile/passwordManagement";
        }
        
        passwordMgrForm.setExpiryInDays(Utility.getIntegerStringValue(passwordMgrForm.getExpiryInDays()));
        passwordMgrForm.setNotifyExpiryInDays(Utility.getIntegerStringValue(passwordMgrForm.getNotifyExpiryInDays()));
        
        ProfilePasswordMgrView passwordView = new ProfilePasswordMgrView(this.getStoreId());
        passwordView.setAllowChangePassword(passwordMgrForm.isAllowChangePassword());
        passwordView.setReqPasswordResetUponInitLogin(passwordMgrForm.isReqPasswordResetUponInitLogin());
        passwordView.setReqPasswordResetInDays(passwordMgrForm.isReqPasswordResetInDays());
        passwordView.setExpiryInDays(passwordMgrForm.getExpiryInDays());
        passwordView.setNotifyExpiryInDays(passwordMgrForm.getNotifyExpiryInDays());
        
        
        try {
        	passwordView = profileService.savePasswordManagementInfo(passwordView);
        } catch (DatabaseUpdateException e) {
        	e.printStackTrace();
            return handleDatabaseUpdateException(e, request);

        }
        logger.info("save()=> END.");

        return "profile/passwordManagement";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.PROFILE_PASSWORD_MANAGEMENT) ProfilePasswordMgrForm passwordMgrForm, Model model) {

        logger.info("show()=> BEGIN");
        
        ProfilePasswordMgrView passwordView = profileService.getPasswordManagementInfo(this.getStoreId());
        passwordMgrForm.setAllowChangePassword(passwordView.isAllowChangePassword());
        passwordMgrForm.setReqPasswordResetUponInitLogin(passwordView.isReqPasswordResetUponInitLogin());
        passwordMgrForm.setReqPasswordResetInDays(passwordView.isReqPasswordResetInDays());
        passwordMgrForm.setExpiryInDays(passwordView.getExpiryInDays());
        passwordMgrForm.setNotifyExpiryInDays(passwordView.getNotifyExpiryInDays());

        model.addAttribute(SessionKey.PROFILE_PASSWORD_MANAGEMENT, passwordMgrForm);

        logger.info("show()=> END.");

        return "profile/passwordManagement";

    }

    @ModelAttribute(SessionKey.PROFILE_PASSWORD_MANAGEMENT)
    public ProfilePasswordMgrForm initModel() {

    	ProfilePasswordMgrForm form = new ProfilePasswordMgrForm();
        form.initialize();

        return form;
    }

}
