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
import com.espendwise.manta.model.view.CMSView;
import com.espendwise.manta.service.CMSService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.CMSForm;
import com.espendwise.manta.web.resolver.CMSWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;

@Controller
@RequestMapping(UrlPathKey.CMS.CMS)
public class CMSController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(CMSController.class);

    private CMSService cmsService;
    
    @Autowired
    public CMSController(CMSService cmsService) {
        this.cmsService = cmsService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new CMSWebUpdateExceptionResolver());

        return "cms/edit";
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(HttpServletRequest request, @ModelAttribute(SessionKey.CMS) CMSForm form, @PathVariable("storeId") Long primaryEntityId, Model model) {

        logger.info("show()=> BEGIN");

        CMSView cms = cmsService.findCMS(primaryEntityId);
        	
        if (cms != null) {
	        	
        	form.setPrimaryEntityId(cms.getPrimaryEntityId());
        	form.setPrimaryEntityName(cms.getPrimaryEntityName());
        	
        	ContentManagementView content = cms.getContentManagementProperties();
        	
        	if(content != null){
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
        }

        model.addAttribute(SessionKey.CMS, form);

        logger.info("show()=> END.");

        
        return "cms/edit";

    }
    
    @ModelAttribute(SessionKey.CMS)
    public CMSForm initModel() {

    	CMSForm form = new CMSForm();
        form.initialize();

        return form;

    }
    
    @SuccessMessage
    @Clean(SessionKey.CMS_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.CMS) CMSForm cmsForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, cmsForm: "+cmsForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(cmsForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.CMS, cmsForm);
            return "cms/edit";
        }


        CMSView cmsView = new CMSView();

        if (!cmsForm.getIsNew()) {
        	cmsView = cmsService.findCMS(getStoreId());
        }
        
        cmsView.setContentManagementProperties(WebFormUtil.createContentManagementProperties(cmsView.getContentManagementProperties(), cmsForm));
       
        try {

        	cmsView = cmsService.saveCMS(getStoreId(),cmsView);

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }

        logger.info("redirect(()=> redirect to: " + cmsView.getPrimaryEntityId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(cmsView.getPrimaryEntityId()));
        
    }
    
    
}
