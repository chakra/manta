package com.espendwise.manta.web.resolver;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.resolvers.AbstractGenericErrorResolver;
import com.espendwise.manta.web.util.AppI18nUtil;

public class GenericErrorWebResolver extends AbstractGenericErrorResolver {
	private String messageKey;
	
    public GenericErrorWebResolver(String messageKey) {
    	this.messageKey = messageKey;
    }

    @Override
    protected ArgumentedMessage isDataValidationError(ApplicationExceptionCode code, TypedArgument[] args){
    	return new ArgumentedMessageImpl(getMessageKey(), args);
    }
    
    public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}
}
