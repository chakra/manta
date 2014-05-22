package com.espendwise.manta.util.validation;


import org.apache.log4j.Logger;

import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.parser.AppParser;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.AppParserFactory;
import com.espendwise.manta.util.validation.resolvers.ExceptionResolver;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;
import com.espendwise.manta.web.validator.WorkflowRuleFormValidator;

public class IdValidator extends LongValidator {
	private static final Logger logger = Logger.getLogger(IdValidator.class);

     public boolean isPositiveValue(Long value) {
         logger.info("isPositiveValue() ===> value.intValue()= "+ value.intValue());
     	 return value.intValue() > 0;
    }

}
