package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.WorkflowRuleTypeFilterForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

import org.apache.log4j.Logger;

public class WorkflowRuleTypeFilterFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(WorkflowRuleFormValidator.class);

    public WorkflowRuleTypeFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        WorkflowRuleTypeFilterForm valueObj = (WorkflowRuleTypeFilterForm) obj;

        ValidationResult vr;
        logger.info("WorkflowRuleTypeFilterFormValidator() ====>validate : valueObj.getRuleType() ="+ valueObj.getRuleType());  

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        vr = shortDescValidator.validate(valueObj.getRuleType(), new TextErrorWebResolver("admin.global.filter.label.rule"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

      logger.info("WorkflowRuleTypeFilterFormValidator() ====>validate : errors.size ="+ errors.size());  
        return new MessageValidationResult(errors.get());
    }


}
