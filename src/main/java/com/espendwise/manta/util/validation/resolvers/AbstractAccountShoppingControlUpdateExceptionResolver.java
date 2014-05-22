package com.espendwise.manta.util.validation.resolvers;


import java.util.List;

import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;

public abstract class AbstractAccountShoppingControlUpdateExceptionResolver implements ValidationResolver<ValidationException> {


	@Override
	public List<? extends ArgumentedMessage> resolve(ValidationException code)
			throws RuntimeException {
		return null;
	}

}

