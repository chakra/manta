package com.espendwise.manta.web.validator;


import java.util.List;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.ProfilePasswordMgrForm;
import com.espendwise.manta.web.resolver.SearchByIdErrorResolver;
import com.espendwise.manta.web.util.WebErrors;

public class ProfilePasswordMgrFormValidator extends AbstractFormValidator {

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        ProfilePasswordMgrForm form = (ProfilePasswordMgrForm) obj;

        ValidationResult vr;
        
        if (Utility.isSet(form.getExpiryInDays())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            vr = validator.validate(form.getExpiryInDays(), new SearchByIdErrorResolver("profile.passwordManagement.label.requireResetEvery", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (Utility.isSet(form.getNotifyExpiryInDays())) {
            IntegerValidator validator = Validators.getIntegerValidator();
            vr = validator.validate(form.getNotifyExpiryInDays(), new SearchByIdErrorResolver("profile.passwordManagement.label.notifyWithin", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        if (errors.size() == 0){        
	        int expiryIndays = 0;
	        if (Utility.isSet(form.getExpiryInDays())) {
	    		expiryIndays = Integer.parseInt(form.getExpiryInDays());
	        }
	        
	        int notifyExpiryInDays = 0;
	        if (Utility.isSet(form.getNotifyExpiryInDays())) {
        		notifyExpiryInDays = Integer.parseInt(form.getNotifyExpiryInDays());
	        }
	        
	        if (form.isReqPasswordResetInDays()){
	    		if (expiryIndays == 0){
	    			errors.putError("validation.web.error.resetDaysIsRequired");
	    		}else if (notifyExpiryInDays >= expiryIndays){
	    			errors.putError("validation.web.error.notifyDaysNeedLessThanResetDays");
	    		}
	    		
	        }else{
	        	if (expiryIndays > 0){
	    			errors.putError("validation.web.error.resetDaysIsNotRequired");
	    		}else{
	    			form.setExpiryInDays(null);
	    		}
	        	
	        	if (Utility.isSet(form.getNotifyExpiryInDays())) {
	        		if (notifyExpiryInDays > 0){
	        			errors.putError("validation.web.error.notifyDaysIsNotRequired");
	        		}else{
	        			form.setNotifyExpiryInDays(null);
	        		}
	        	}
	        }	        
	        
        }
        
        return new MessageValidationResult(errors.get());

    }
    	

}
