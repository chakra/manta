package com.espendwise.manta.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.ContentManagementView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.AccountContentManagementForm;
import com.espendwise.manta.web.resolver.AccountContentManagementWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.CONTENT_MANAGEMENT)
public class AccountContentManagementController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(AccountContentManagementController.class);

    private AccountService accountService;
    
    @Autowired
    public AccountContentManagementController(AccountService accountService) {
        this.accountService = accountService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new AccountContentManagementWebUpdateExceptionResolver());

        return "account/contentManagement";
    }
    
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String show(HttpServletRequest request, @ModelAttribute(SessionKey.ACCOUNT_CONTENT_MANAGEMENT) AccountContentManagementForm form, @PathVariable("accountId") Long accountId, Model model) {

        logger.info("show()=> BEGIN");

        ContentManagementView content = accountService.findContentManagement(accountId);
        	
        if (content != null) {
	        	
        	form.setAccountId(content.getBusEntityId());   
        	
        	if(Utility.isSet(content.getDisplayGenericContent())){
        		form.setDisplayGenericContent(Utility.isTrue(content.getDisplayGenericContent().getValue()));
        	}
        	if(Utility.isSet(content.getCustomContentURL())){
        		form.setCustomContentURL(PropertyUtil.toValueNN(content.getCustomContentURL()));
        	}
        	if(Utility.isSet(content.getCustomContentEditor())){
        		form.setCustomContentEditor(PropertyUtil.toValueNN(content.getCustomContentEditor()));
        	}
        	if(Utility.isSet(content.getCustomContentViewer())){
        		form.setCustomContentViewer(PropertyUtil.toValueNN(content.getCustomContentViewer()));
        	}
        	
        }

        model.addAttribute(SessionKey.ACCOUNT_CONTENT_MANAGEMENT, form);

        logger.info("show()=> END.");

        
        return "account/contentManagement";

    }
    
    @ModelAttribute(SessionKey.ACCOUNT_CONTENT_MANAGEMENT)
    public AccountContentManagementForm initModel(@PathVariable("accountId") Long accountId) {

    	AccountContentManagementForm form = new AccountContentManagementForm(accountId);
        if (!form.isInitialized()) {

            //AccountContentManagementView contentMgmtView = accountService.findContentManagement(accountId);
            form.initialize();

        }

        return form;

    }
    
    @SuccessMessage
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_CONTENT_MANAGEMENT) AccountContentManagementForm accountContentManagementForm,
    		@PathVariable("accountId") Long accountId,Model model) throws Exception {

        logger.info("save()=> BEGIN, AccountContentManagementForm: "+accountContentManagementForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(accountContentManagementForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.ACCOUNT_CONTENT_MANAGEMENT, accountContentManagementForm);
            return "account/contentManagement";
        }


        ContentManagementView content = new ContentManagementView();

        if (!accountContentManagementForm.getIsNew()) {
        	content = accountService.findContentManagement(accountId);
        }

        content = WebFormUtil.createContentManagementProperties(content, accountContentManagementForm);
        
        try {

        	content = accountService.saveAccountContentManagement(accountId, content);

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }

        logger.info("save()=> END, redirect to " + content.getBusEntityId());

        return redirect("../contentManagement");
        
    }
    
    
}
