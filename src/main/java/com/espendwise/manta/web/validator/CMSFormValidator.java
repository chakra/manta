package com.espendwise.manta.web.validator;

import org.apache.log4j.Logger;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.CMSForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class CMSFormValidator extends AbstractFormValidator{
	
	private static final Logger logger = Logger.getLogger(CMSFormValidator.class);

    public CMSFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {
    	
    	WebErrors errors = new WebErrors();

        CMSForm valueObj = (CMSForm) obj;

        ValidationResult vr = null;
        
        //If custom content URL is set, custom content viewer must exist
        TextValidator customContentViewerValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.BIG_TEXT_LENGTH);
        
        if(Utility.isSet(valueObj.getCustomContentURL())){
	        vr = customContentViewerValidator.validate(valueObj.getCustomContentViewer(), new TextErrorWebResolver("admin.setup.label.customContentViewer"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        
        //Custom content editor and custom content viewer must be valid user names in our system
        
        return vr;
    }
}
