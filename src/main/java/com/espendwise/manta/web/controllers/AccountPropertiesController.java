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

import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.EntityPropertiesView;
import com.espendwise.manta.service.PropertyService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AccountIdentPropertyExtraCode;
import com.espendwise.manta.util.AccountIdentPropertyTypeCode;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.AccountPropertiesForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.PROPERTIES)
public class AccountPropertiesController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(AccountPropertiesController.class);

    private PropertyService propertyService;
    
    @Autowired
    public AccountPropertiesController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {
     //   WebErrors webErrors = new WebErrors(request);
     //   webErrors.putErrors(ex, new AccountPropertiesWebUpdateExceptionResolver());
        return "account/properties";
    }
    
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String show(HttpServletRequest request, @ModelAttribute(SessionKey.ACCOUNT_PROPERTIES) AccountPropertiesForm form, @PathVariable("accountId") Long accountId, Model model) {

        logger.info("show()=> BEGIN");

        EntityPropertiesView view = propertyService.findEntityProperties(accountId, Utility.toList(AccountPropertiesForm.PROPERTY_EXTRA_TYPE_CDS), Utility.toList(AccountPropertiesForm.PROPERTY_TYPE_CDS));
        	
        if (view != null) {
	        	
        	form.setAccountId(view.getBusEntityId());   
        	
        	List<PropertyData> properties = view.getProperties();
        	if (Utility.isSet(properties)) {
        		PropertyData prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyExtraCode.ALLOW_USER_CHANGE_PASSWORD) ;
        		if (prop != null){
           			form.setAllowToChangePassword(Utility.isTrue(prop.getValue()));
        		}
        		prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyTypeCode.ALLOW_CUSTOMER_PO_NUMBER ) ;
        		if (prop != null){
        			form.setAllowToEnterPurchaseNum(Utility.isTrue(prop.getValue()));
        		}
        		prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyExtraCode.ALLOW_CC_PAYMENT ) ;
        		if (prop != null){
        			form.setAllowToPayCreditCard(Utility.isTrue(prop.getValue()));
        		}
        		prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyTypeCode.TAXABLE_INDICATOR ) ;
        		if (prop != null){
        			form.setUseEstimatedSalesTax(Utility.isTrue(prop.getValue()));
        		}
        		prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyTypeCode.AUTHORIZED_FOR_RESALE ) ;
        		if (prop != null){
        			form.setUseResaleItems(Utility.isTrue(prop.getValue()));
        		}
        		prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyExtraCode.SUPPORTS_BUDGET ) ;
        		if (prop != null){
        			form.setUseBudgets(Utility.isTrue(prop.getValue()));
        		}
        		prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyTypeCode.USE_ALTERNATE_UI ) ;
        		if (prop != null){
        			form.setUseAlternateUserInterface(Utility.isTrue(prop.getValue()));
        		}
        		prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyTypeCode.CUSTOMER_SYSTEM_APPROVAL_CD ) ;
        		if (prop != null){
        			form.setPurchasingSystem(Utility.strNN(prop.getValue()));
        		}		        	
        		prop = (PropertyData)PropertyUtil.findP(properties, AccountIdentPropertyTypeCode.TRACK_PUNCHOUT_ORDER_MESSAGES) ;
        		if (prop != null){
        			form.setTrackPunchoutOrderMessages(Utility.isTrue(prop.getValue()));
        		}		        	
        	}        	
        	
        }

        model.addAttribute(SessionKey.ACCOUNT_PROPERTIES, form);

        logger.info("show()=> END.");
        
        return "account/properties";

    }
    
    @ModelAttribute(SessionKey.ACCOUNT_PROPERTIES)
    public AccountPropertiesForm initModel(@PathVariable("accountId") Long accountId) {

    	AccountPropertiesForm form = new AccountPropertiesForm(accountId);
        if (!form.isInitialized()) {

            form.initialize();

        }

        return form;

    }
    
    @SuccessMessage
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_PROPERTIES) AccountPropertiesForm accountPropertiesForm,
    		@PathVariable("accountId") Long accountId,Model model) throws Exception {

        logger.info("save()=> BEGIN, AccountPropertiesForm: "+accountPropertiesForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(accountPropertiesForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.ACCOUNT_PROPERTIES, accountPropertiesForm);
            return "account/properties";
        }

        EntityPropertiesView view = null;

        if (!accountPropertiesForm.getIsNew()) {
        	//view = propertyService.findEntityProperties(accountId, Utility.toList(accountPropertiesForm.PROPERTY_TYPE_CDS));
            view = propertyService.findEntityProperties(accountId, Utility.toList(AccountPropertiesForm.PROPERTY_EXTRA_TYPE_CDS), Utility.toList(AccountPropertiesForm.PROPERTY_TYPE_CDS));
        }
        else {
            view = new EntityPropertiesView();
        }

        view = WebFormUtil.createAccountProperties(view, accountPropertiesForm);
                
        try {
        	view = propertyService.saveEntityProperties(accountId, view);
        } 
        catch (ValidationException e) {
            return handleValidationException(e, request);
        }

        logger.info("save()=> END, redirect to " + ((view != null) ? ""+view.getBusEntityId(): "Null"));

        return redirect("../properties");
        
    }
    
    
}
