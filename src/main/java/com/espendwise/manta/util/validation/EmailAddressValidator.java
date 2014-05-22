package com.espendwise.manta.util.validation;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.validation.resolvers.ExceptionResolver;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;

import com.espendwise.ocean.util.validation.EmailValidation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EmailAddressValidator implements CustomValidator<String, ValidationCodeResolver> {
    
    private static final String EMAIL_ADDRESS_SEPARATORS = "[,;]";
    
    private boolean allowMultipleAddresses;
    
    public EmailAddressValidator() {
    	this.allowMultipleAddresses = false;
    }
    
    public EmailAddressValidator(boolean allowMultipleAddresses) {
    	this();
    	this.allowMultipleAddresses = allowMultipleAddresses;
    }

    public CodeValidationResult validate(String obj, ValidationCodeResolver resolver) throws ValidationException {

        //MANTA-370 (allow ";" as email separator in addition to ",")
        if (allowMultipleAddresses) {
        	String[] emailAddresses = obj.split(EMAIL_ADDRESS_SEPARATORS);
        	List<String> invalidEmailAddresses = new ArrayList<String>();
        	for (int i=0; i<emailAddresses.length; i++) {
        		String emailAddress = emailAddresses[i].trim();
    	        if (!EmailValidation.isValidEmailAddress(emailAddress)) {
    	        	invalidEmailAddresses.add(emailAddress);
    	        }
        	}
        	if (Utility.isSet(invalidEmailAddresses)) {
        		boolean includeComma = false;
        		StringBuilder invalidEmailAddressString = new StringBuilder();
        		Iterator<String> invalidEmailAddressIterator = invalidEmailAddresses.iterator();
        		while (invalidEmailAddressIterator.hasNext()) {
        			if (includeComma) {
        				invalidEmailAddressString.append(", ");
        			}
    				invalidEmailAddressString.append(invalidEmailAddressIterator.next());
    				includeComma = true;
        		}
	            return new CodeValidationResult(
    	                resolver,
    	                new ValidationCodeImpl(ValidationReason.WRONG_EMAIL_ADDRESS_FORMAT, new StringArgument(invalidEmailAddressString.toString()))
    	            );
        		
        	}
        }
        else {
	        if (!EmailValidation.isValidEmailAddress(obj)) {
	            return new CodeValidationResult(
	                resolver,
	                new ValidationCodeImpl(ValidationReason.WRONG_EMAIL_ADDRESS_FORMAT, new StringArgument(obj))
	            );
	        }
        }

        return null;
    }

    @Override
    public CodeValidationResult validate(String obj) throws ValidationException {
        return validate(obj, new ExceptionResolver());
    }

}
